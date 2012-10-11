package me.mcgavin.lms;



public class Subject {

	private String code;
	private String name;
	private String actualCode;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public void setCode(String code) {
		this.code = code.substring(0, 9);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActualCode() {
		return actualCode;
	}
	public void setActualCode(String actualCode) {
		this.actualCode = actualCode;
	}
}
