package typedetect.runner;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;

import typedetect.FileContentTypeSummary;
import typedetect.JsonWriterUtils;
import typedetect.TrainTestFileForType;

public class SeparateTestTrainDataRunner {
	public static void main(String[] args) {
		String jsonBaseFolder = "C:\\cs599\\polar-json\\";
		
		if (args.length >= 1) {
			jsonBaseFolder = args[0];
		}
		
		if (!jsonBaseFolder.endsWith("\\")) {
			jsonBaseFolder += "\\";
		}
		
		String jsonByTypeFolder = jsonBaseFolder + "byType";
		String jsonOutputFolder = jsonBaseFolder + "trainTest";
		
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
}
