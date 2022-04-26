package br.org.flem.util.conts;

public enum FileType {

	JPG("jpg"),
	JPEG("jpeg"),
	PNG("png"),
	PDF("pdf");
	
	private String value;
	
	private FileType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
