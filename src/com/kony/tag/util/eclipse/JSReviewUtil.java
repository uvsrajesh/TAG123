package com.kony.tag.util.eclipse;

import com.kony.tag.js.codereview.config.ReviewModeConstants;

public class JSReviewUtil {
	
	private static String _message = null;
	private static final String LOG = "-----LOG : ";
	
	public static void init() {
		_message = null;
	}
	
	public static void setEndOfWorkWarningMessage(String message) {
		if (null != message &&  message.trim().length()>0) {
			_message = message;
		} else {
			_message = null;
		}
	}
	
	public static String getEndOfWorkWarningMessage() {
		if (null != _message && _message.trim().length()>0) {
			return "\nNOTE : " + _message;
		} else {
			return "";
		}
	}

	public static String getProjectPath() {
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			return ReviewModeConstants.TEST_LOCATION_PROJECT_PATH;
		} else {
			//return "unknown";
			return EclipseReviewUtilJS.getProjectPath();
		}
	}
	
	
	public static void printToConsole(String message) {
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			System.out.println(message);
		} else {
			EclipseReviewUtilJS.printToConsole(message);
		}
		
	}
	
	public static void printDetailedLog(String message) {
		if (!ReviewModeConstants.IS_DETAILED_LOGGING_ENABLED) {
			return;
		}
		
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			System.out.println(LOG+ message);
		} else {
			EclipseReviewUtilJS.printToConsole(LOG+message);
		}
	}

	public static void displayDialog(String mesType, String message) 
	{
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			System.out.println("ALERT MESSAGE : " + message);
		}else {
			EclipseReviewUtilJS.displayDialog(mesType, message);
		}
		
	}
	
	public static void setTaskName(String taskName) {
		if (!ReviewModeConstants.TESTING_MODE_FLAG) {
			EclipseReviewUtilJS.setTaskName(taskName);
		}
	}
}
