package com.kony.tag.config;

public class TagToolException extends Exception {
	
	public static final int ERR_CD_SYNTAX_NOT_SUPPORTED = 9901;
	public static final String ERR_MSG_FUNCTION_NOT_ON_NEWLINE = "A new function is defined on the same line where the previous function ends. Unable to review this JS File : ";
	
	private int errorCode;
	private String errorMessage;

    public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

    private TagToolException() {
    	// Do not use
    }
    
	public TagToolException(String message)  {
		this.errorMessage = message;
	}

	public TagToolException(int errorCode, String message)  {
		this.errorMessage = message;
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
