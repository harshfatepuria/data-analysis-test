package typedetect.runner;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import typedetect.FileContentTypeSummary;
import typedetect.JsonWriterUtils;
import typedetect.TrainTestFileForType;

public class SeparateTestTrainDataRunner {
	public static void main(String[] args) throws ParseException {
		CommandLine cmd = parseCommand(args);
		
		/*
		String jsonBaseFolder = "C:\\cs599\\polar-json\\";
		
		if (args.length >= 1) {
			jsonBaseFolder = args[0];
		}
		
		if (!jsonBaseFolder.endsWith("\\")) {
			jsonBaseFolder += "\\";
		}
		
		String jsonByTypeFolder = jsonBaseFolder + "byType";
		String jsonOutputFolder = jsonBaseFolder + "trainTest";
		*/
		
		String jsonByTypeFolder = cmd.getOptionValue("byType");
		String jsonOutputFolder = cmd.getOptionValue("output");
		
		System.out.println("json byType folder " + jsonByTypeFolder);
		System.out.println("output folder " + jsonOutputFolder);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			Files.walk(Paths.get(jsonByTypeFolder))
				.filter(p -> {
					return p.getFileName().toString().endsWith(".json");
				}).forEach(p -> {
					try {
						FileContentTypeSummary sum = mapper.readValue(p.toFile(), FileContentTypeSummary.class);
						TrainTestFileForType t = new TrainTestFileForType(sum);
						
						String fileName = JsonWriterUtils.urlEncode(sum.getType());
						JsonWriterUtils.writeJson(t, fileName + ".json", jsonOutputFolder);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static CommandLine parseCommand(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("byType", true, "folder that contains list of files path for each detected type");
		options.addOption("output", true, "folder to store output json files");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		return cmd;
	}
}
