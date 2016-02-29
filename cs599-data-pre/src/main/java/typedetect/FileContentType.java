package typedetect;

public class FileContentType {
	private String key;
	private String type;
//	private Integer fragment;
	
	public FileContentType() {
//		fragment = 0;
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

	/*
	public Integer getFragment() {
		return fragment;
	}

	public void setFragment(Integer fragment) {
		this.fragment = fragment;
	}
	*/
}
