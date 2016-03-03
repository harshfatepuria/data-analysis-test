package typedetect;

/**
 * Tuple of file path and its type
 *
 */
public class FileContentType {
	private String key;
	private String type;
	
	public FileContentType() {
		
	}
	
	public FileContentType(String key, String type) {
		setKey(key);
		setType(type);
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
