package typedetect;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class responsible for writing object to a json file
 *
 */
public class JsonWriterUtils {
	
	public static String urlEncode(String url) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, "UTF-8");
	}
	
	public static String urlDecode(String url) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, "UTF-8");
	}
	
	public static void writeJson(Object objToWrite, String fileName, String jsonFolder) {
		File jsonFolderFile = new File(jsonFolder);
		jsonFolderFile.mkdirs();
		
		File jsonFile = new File(jsonFolderFile, fileName);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, objToWrite);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
