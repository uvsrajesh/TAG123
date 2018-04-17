package com.kony.tag.config;

import java.util.ArrayList;
import java.util.List;

public class CodeReviewStatus {
	
	List<String> errMessages = null;
	List<String> warningMessages = null;
	private static CodeReviewStatus codeReviewStatus = null;
	
	private CodeReviewStatus() {
		// do not use
	}
	
	private CodeReviewStatus(List<String>errMessages, List<String>warningMessages) {
		this.errMessages = errMessages;
		this.warningMessages = warningMessages;
	}
	
	public static void reset() {
		codeReviewStatus = null;
	}
	
	public static CodeReviewStatus getInstance() {
		if (null == codeReviewStatus) {
			codeReviewStatus = new CodeReviewStatus(new ArrayList<String>(),new ArrayList<String>());
		}
		return codeReviewStatus;
	}
	
	public List<String> getErrorMessages () {
		return this.errMessages;
	}
	
	public List<String> getWarningMessages () {
		return this.warningMessages;
	}	

	public void addErrorMessage(String errMessage) {
		if (!warningMessages.contains(errMessage)) {
			warningMessages.add(errMessage);
		}
	}	

	public void addErrorMessage(Exception e, String errMessage) {
		if (!errMessages.contains(errMessage)) {
			errMessages.add(errMessage);
		}

		errMessages.add(stackTraceToString(e));
	}	
	
	private static String stackTraceToString(Throwable e) 
	{
	    StringBuilder sb = new StringBuilder();
	    sb.append("Stack Trace : ");
	    for (StackTraceElement element : e.getStackTrace()) 
	    {
	        sb.append(element.toString());
	        sb.append(" ; ");
	    }
	    
	    return sb.toString();
	}
	
}
