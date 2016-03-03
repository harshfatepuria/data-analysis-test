package typedetect.runner;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypesFactory;

import typedetect.FileContentType;
import typedetect.FileContentTypeSummary;
import typedetect.JsonWriterUtils;

/**
 * Use Apache Tika to detect file types and output json files containing types and path of each file.
 *
 */
public class TypeDetectRunner {
	
	public static void main(String[] args) throws ParseException, MimeTypeException, IOException {
		CommandLine cmd = parseCommand(args);
		
		String baseFolder = "";
		String jsonFolder = "";
	
		if (cmd.hasOption("data")) {
			baseFolder = cmd.getOptionValue("data");
		}
	
		if (cmd.hasOption("output")) {
			jsonFolder = cmd.getOptionValue("output");
		}
		
		String jsonByTypeFolder = jsonFolder + "byType";
		
		System.out.println("Data folder " + baseFolder);
		System.out.println("Result folder " + jsonFolder);
		
		String mimetype = null;
		if (cmd.hasOption("mimetype")) {
			mimetype = cmd.getOptionValue("mimetype");
			System.out.println("Using custom mimetype.xml at " + mimetype);
		} else {
			System.out.println("Using Tika default mimetype.xml");
		}
		
		List<FileContentType> fileContentTypeList = generateFileContentTypeList(baseFolder, mimetype);
		
		System.out.println("fileContentTypeList generated with size " + fileContentTypeList.size());
		JsonWriterUtils.writeJson(fileContentTypeList, "allRecords.json", jsonFolder);
		System.out.println("fileContentTypeList JSON written");
		
		Map<String, FileContentTypeSummary> summaryMap = generateFileContentTypeSummaryMap(fileContentTypeList);
		System.out.println("summaryMap generated");
		
		/* create a json file for each type */
		summaryMap.values().stream().forEach(sum -> {
			try {
				String fileName = JsonWriterUtils.urlEncode(sum.getType());
				JsonWriterUtils.writeJson(sum, fileName + ".json", jsonByTypeFolder);
				
				System.out.println("summary " + sum.getType() + " JSON written");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		
		/* create a summary file */
		SortedMap<String, Integer> typeCount = new TreeMap<>();
		summaryMap.values().forEach(sum -> {
			typeCount.put(sum.getType(), sum.getCount());
		});
		JsonWriterUtils.writeJson(typeCount, "summary.json", jsonFolder);
	}
	
	private static CommandLine parseCommand(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("data", true, "base data folder");
		options.addOption("output", true, "folder to store output json files");
		options.addOption("mimetype", true, "path to custom mimetype.xml");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		return cmd;
	}
	
	/**
	 * Walks the base folder, detect their file type using Tika and generated a list of tuples
	 * @param baseFolder base folder of files to be detected
	 * @param mimetype path to custom tika-mimetype.xml (if null, use Tika default mimetype.xml
	 * @return List of tuples, file path and its type
	 * @throws MimeTypeException
	 * @throws IOException
	 */
	private static List<FileContentType> generateFileContentTypeList(String baseFolder, String mimetype) throws MimeTypeException, IOException {
		List<FileContentType> list = new ArrayList<>();
		
		File baseFolderFile = new File(baseFolder);
		URI baseFolderUri = baseFolderFile.toURI();
		
		Tika tika; 
		
		if (mimetype == null) {
			tika = new Tika();
		} else {
			tika = new Tika(MimeTypesFactory.create(Paths.get(mimetype).toUri().toURL()));
		}
		
		try {
			Files.walk(Paths.get(baseFolder))
				.filter(Files::isRegularFile)
				.forEach(path -> {
					try {
						String type = tika.detect(path);
						String relativePath = baseFolderUri.relativize(path.toUri()).toString();
						
						FileContentType fct = new FileContentType(relativePath, type);
						list.add(fct);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * Create a summary map (index file path by detected type) from list of tuple
	 * @param list List of FileContentType tuple
	 * @return a summary map
	 */
	private static Map<String, FileContentTypeSummary> generateFileContentTypeSummaryMap(List<FileContentType> list) {
		Map<String, FileContentTypeSummary> map = new HashMap<>();
		
		list.stream().forEach(fct -> {
			String type = fct.getType();
			if (!map.containsKey(type)) {
				FileContentTypeSummary sum = new FileContentTypeSummary();
				sum.setType(type);
				map.put(type, sum);
			}
			
			map.get(type).addFile(fct.getKey());
		});
		
		return map;
	}
	
}
