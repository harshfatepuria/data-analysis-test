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

import org.apache.tika.Tika;
import org.apache.tika.detect.NNExampleModelDetector;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NNBasedTypeDetectRunner {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		Path modelPath = Paths.get("C:\\cs599\\polar-nnmodel\\model", "tika-nn-xml.model");
		NNExampleModelDetector detector = new NNExampleModelDetector(modelPath);

		Tika tikaNN = new Tika(detector);

		File dataJson = new File("C:\\cs599\\polar-nnmodel\\dataset\\test.json");
		String dataDir = "C:\\cs599\\polar-fulldump\\";
		String positiveType = "application/xhtml+xml";

		ObjectMapper mapper = new ObjectMapper();
		Map<String, ArrayList<String>> map = mapper.readValue(dataJson,
				new TypeReference<Map<String, ArrayList<String>>>() {
				});

		Integer count = 0;
		Integer correct = 0;

		try (PrintStream ps = new PrintStream("C:\\cs599\\polar-nnmodel\\result\\result-test.txt")) {
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
}
