package nnmodel.runner;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import byteanalysis.BFA;
import typedetect.FileContentTypeSummary;
import typedetect.JsonWriterUtils;

/**
 * Create datasets to feed to R neural network training script 
 *
 */
public class CreateNNDatasetRunner {
	static String dataDir = "";
	static String byTypeDir = "";
	static String outputDir = "";
	static String positiveType = "application/xhtml+xml";
	static String[] negativeTypes = new String[] {"application/pdf", "image/jpeg", "image/gif", "text/html", "text/plain" };
	static Integer positiveSize = 50000;
	static Integer negativeSize = 10000;
	
	public static void main(String args[]) throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException, ParseException {
		CommandLine cmd = parseCommand(args);
		
		if (cmd.hasOption("data")) {
			dataDir = cmd.getOptionValue("data");
		}
		
		if (cmd.hasOption("byType")) {
			byTypeDir = cmd.getOptionValue("byType");
		}
		
		if (cmd.hasOption("output")) {
			outputDir = cmd.getOptionValue("output");
		}
		
		Map<String, ArrayList<String>> train = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> test = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> val = new HashMap<String, ArrayList<String>>();
		
		ObjectMapper mapper = new ObjectMapper();
		
		/* read data of positive type */
		FileContentTypeSummary sum = mapper.readValue(new File(byTypeDir, JsonWriterUtils.urlEncode(positiveType + ".json")), FileContentTypeSummary.class);
		readPaths(sum, train, test, val);
		
		/* read data of negative type */
		for(String type : negativeTypes) {
			sum = mapper.readValue(new File(byTypeDir, JsonWriterUtils.urlEncode(type + ".json")), FileContentTypeSummary.class);
			readPaths(sum, train, test, val);
		}
		
		/* write json files to keep track of the dataset */
		JsonWriterUtils.writeJson(train, "train.json", outputDir);
		JsonWriterUtils.writeJson(test, "test.json", outputDir);
		JsonWriterUtils.writeJson(val, "val.json", outputDir);
		
		/* write tab separated dataset files */
		writeData(train, "train.data");
		writeData(test, "test.data");
		writeData(val, "val.data");
	}
	
	/**
	 * Read paths of a specific file type, populate them to be training, validation and test dataset
	 * @param sum FileContentTypeSummary tuple for a specific file type
	 * @param train accumulated training dataset
	 * @param test accumulated test dataset
	 * @param val accumulated validation dataset
	 * @throws IOException
	 */
	private static void readPaths(FileContentTypeSummary sum, Map<String, ArrayList<String>> train, Map<String, ArrayList<String>> test, Map<String, ArrayList<String>> val) throws IOException {
		ArrayList<String> trainList = new ArrayList<>();
		ArrayList<String> testList = new ArrayList<>();
		ArrayList<String> valList = new ArrayList<>();
		
		Integer size = sum.getType().equals(positiveType) ? positiveSize : negativeSize;
		
		for(String path : sum.getFiles()) {
			File f = new File(dataDir, path);
			if (Files.size(f.toPath()) == 0) {
				continue;
			}
			
			if (trainList.size() < size) {
				trainList.add(path);
			}
			else if (testList.size() < size) {
				testList.add(path);
			} 
			else if (valList.size() < size) {
				valList.add(path);
			}
			else {
				break;
			}
		}
		
		train.put(sum.getType(), trainList);
		test.put(sum.getType(), testList);
		val.put(sum.getType(), valList);
	}
	
	/**
	 * Write data to a tab separated file
	 * @param map the dataset to be written
	 * @param fileName file path to write
	 */
	private static void writeData(Map<String, ArrayList<String>> map, String fileName) {
		try (PrintStream ps = new PrintStream(outputDir + fileName)) {
			for(String type : map.keySet()) {
				Integer label = type.equals(positiveType) ? 1 : 0;
				
				for(String path : map.get(type)) {
					File f = new File(dataDir, path);
					byte[] bytes = Files.readAllBytes(f.toPath());
					
					/* generate normalized byte frequency distribution of that file */
					BFA bfa = new BFA(bytes);
					bfa = bfa.normalize();
					
					StringBuilder sb = new StringBuilder();
					for (float val : bfa.getFrequencies()) {
						sb.append(val + "\t");
					}
					sb.append(label);
					
					ps.println(sb.toString());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static CommandLine parseCommand(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("data", true, "base data folder");
		options.addOption("byType", true, "folder that contains list of files path for each detected type");
		options.addOption("output", true, "folder to store output json files");
		
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		return cmd;
	}
	
}
