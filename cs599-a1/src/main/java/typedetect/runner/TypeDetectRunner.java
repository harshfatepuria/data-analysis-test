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

import org.apache.tika.Tika;

import typedetect.FileContentType;
import typedetect.FileContentTypeSummary;
import typedetect.JsonWriterUtils;


public class TypeDetectRunner {
	public static void main(String[] args) {
		String baseFolder = "C:\\cs599\\polar-fulldump";
		String jsonFolder = "C:\\cs599\\polar-json";
	
		if (args.length >= 1) {
			baseFolder = args[0];
		}
		if (args.length >= 2) {
			jsonFolder = args[1];
		}
		
		if (!jsonFolder.endsWith("\\")) {
			jsonFolder += "\\";
		}
		String jsonByTypeFolder = jsonFolder + "byType";

		
		List<FileContentType> fileContentTypeList = generateFileContentTypeList(baseFolder);
		System.out.println("fileContentTypeList generated with size " + fileContentTypeList.size());
		JsonWriterUtils.writeJson(fileContentTypeList, "allRecords.json", jsonFolder);
		System.out.println("fileContentTypeList JSON written");
		
		Map<String, FileContentTypeSummary> summaryMap = generateFileContentTypeSummaryMap(fileContentTypeList);
		System.out.println("summaryMap generated");
		
		summaryMap.values().stream().forEach(sum -> {
			try {
				String fileName = JsonWriterUtils.urlEncode(sum.getType());
				JsonWriterUtils.writeJson(sum, fileName + ".json", jsonByTypeFolder);
				
				System.out.println("summary " + sum.getType() + " JSON written");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
		
		SortedMap<String, Integer> typeCount = new TreeMap<>();
		summaryMap.values().forEach(sum -> {
			typeCount.put(sum.getType(), sum.getCount());
		});
		JsonWriterUtils.writeJson(typeCount, "summary.json", jsonFolder);
	}
	
	private static List<FileContentType> generateFileContentTypeList(String baseFolder) {
		List<FileContentType> list = new ArrayList<>();
		
		File baseFolderFile = new File(baseFolder);
		URI baseFolderUri = baseFolderFile.toURI();
		
		Tika tika = new Tika();
		
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
