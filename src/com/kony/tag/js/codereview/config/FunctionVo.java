package com.kony.tag.js.codereview.config;

public class FunctionVo {
	private int startIndex;
	private String functionName;
	private String fileName;
	private int lineNumber;
	private String shortFunctionName;
	private final String DOT = ".";
	private String[] tmpShortNames = null;
	
	public String getShortFunctionName() {
		return shortFunctionName;
	}
	public void setShortFunctionName(String shortFunctionName) {
		this.shortFunctionName = shortFunctionName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		
		if (functionName != null && functionName.length()>0) {
			functionName = functionName.replaceAll("this.", "");
			/*
			tmpShortNames = functionName.split(DOT);
			this.shortFunctionName = tmpShortNames[tmpShortNames.length-1]; 
		} else {
			this.shortFunctionName = null;
			*/
		}
		
		this.functionName = functionName;
	}
	
	@Override
	public boolean equals(Object functionVo) {
		boolean isEqual = false;
		FunctionVo fnVo = null;
		
		if (functionVo instanceof FunctionVo) {
			fnVo = (FunctionVo) functionVo;
		} else {
			return false;
		}
		
		if (this.functionName.equals(fnVo.getFunctionName())) {
			isEqual = true;
		}
		
		return isEqual;
	}
}
