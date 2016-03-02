package nnmodel.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.tika.Tika;
import org.apache.tika.detect.NNExampleModelDetector;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NNBasedTypeDetectRunner {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, ParseException {
		String modelFile = "C:\\cs599\\polar-nnmodel\\model\\tika-nn-xml.model";
		String dataDir = "C:\\cs599\\polar-fulldump\\";
		String testFile = "C:\\cs599\\polar-nnmodel\\dataset\\test.json";
		String outputFile = "C:\\cs599\\polar-nnmodel\\result\\result-test.txt";
		
		CommandLine cmd = parseCommand(args);
		
		if (cmd.hasOption("model")) {
			modelFile = cmd.getOptionValue("model");
		}
		
		if (cmd.hasOption("data")) {
			dataDir = cmd.getOptionValue("data");
		}
		
		if (cmd.hasOption("test")) {
			testFile = cmd.getOptionValue("test");
		}
		
		if (cmd.hasOption("output")) {
			outputFile = cmd.getOptionValue("output");
		}
		
		Path modelPath = Paths.get(modelFile);
		NNExampleModelDetector detector = new NNExampleModelDetector(modelPath);

		Tika tikaNN = new Tika(detector);
		File dataJson = new File(testFile);
		String positiveType = "application/xhtml+xml";

		ObjectMapper mapper = new ObjectMapper();
		Map<String, ArrayList<String>> map = mapper.readValue(dataJson,
				new TypeReference<Map<String, ArrayList<String>>>() {
				});

		Integer count = 0;
		Integer correct = 0;

		try (PrintStream ps = new PrintStream(outputFile)) {
			for (String type : map.keySet()) {
				boolean expectPositive = Objects.equals(type, positiveType);

				for (String path : map.get(type)) {
					File file = new File(dataDir, path);
					String print = path + "\t" + type + "\t";
					Integer cor = 0;
					count++;
					
					String nnType = tikaNN.detect(file);
					if (expectPositive) {
						if (Objects.equals(positiveType, nnType)) {
							cor = 1;
							correct++;
						}
					} else {
						if (!Objects.equals(positiveType, nnType)) {
							cor = 1;
							correct++;
						}
					}

					ps.println(print + nnType + "\t" + cor);
				}
			}

			ps.println("##\t" + correct + "\t" + count);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static CommandLine parseCommand(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("model", true, "path to model file");
		options.addOption("data", true, "base data folder");
		options.addOption("test", true, "path to test set json file");
		options.addOption("output", true, "path to store output file");
		
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		return cmd;
	}
}
