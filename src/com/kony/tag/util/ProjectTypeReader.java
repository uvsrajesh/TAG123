package com.kony.tag.util;

import com.kony.tag.codereview.lua.Util;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.util.ProjectPropertiesHandler;
import com.kony.tag.config.CodeReviewStatus;

public class ProjectTypeReader implements ProjectConstants {
	
	
	public static boolean isJSProject(String projectLocation) {

		String projectType = null;
		FormUtil formUtil = new FormUtil();
		ProjectPropertiesHandler projectPropertiesHandler = new ProjectPropertiesHandler();
		projectPropertiesHandler.init();
		
		try {
			formUtil.readXML(projectLocation+PROJECT_PROPERTIES_FILE,projectPropertiesHandler);
			// Wait till the file is read completely
			while(!projectPropertiesHandler.isEndOfFileReached()) {};
		} catch (Exception excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, excp.toString());
		}
		
		projectType = projectPropertiesHandler.getProjectType();
		if (null != projectType && projectType.trim().equalsIgnoreCase("js")) {
			return true;
		} else {
			return false;
		}
	}
}
