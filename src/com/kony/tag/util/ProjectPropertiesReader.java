package com.kony.tag.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kony.tag.codereview.lua.Util;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.util.ProjectPropertiesHandler;
import com.kony.tag.config.CodeReviewStatus;

public class ProjectPropertiesReader implements ProjectConstants {
	
	private static Map<String, String> projectPropertiesMap = new HashMap<String, String>();
	public static final String PROP_PROJECT_TYPE = "projectType";
	public static final String PROP_DEFAULT_LOCALE = "defaultLocale";
	public static final String PROP_APP_LOGOS = "appLogos";
	public static final String PROP_SPLASH_IMAGES = "splashImages";
	
	public static String getProperty(String propertyName) {
		return projectPropertiesMap.get(propertyName);
	}
	
	public static void readProjectProperties(String projectLocation) {

		FormUtil formUtil = new FormUtil();
		ProjectPropertiesHandler projectPropertiesHandler = new ProjectPropertiesHandler();
		projectPropertiesHandler.init();
		
		try {
			
			File file = new File(projectLocation+PROJECT_PROPERTIES_FILE);
			if (file.exists()) {
				formUtil.readXML(projectLocation+PROJECT_PROPERTIES_FILE,projectPropertiesHandler);
				
				// Wait till the file is read completely
				while(!projectPropertiesHandler.isEndOfFileReached()) {};

				if (null != projectPropertiesHandler && projectPropertiesHandler.getProjectType() != null 
						&& projectPropertiesHandler.getProjectType().trim().length()>0) {
					projectPropertiesMap.put(PROP_PROJECT_TYPE,projectPropertiesHandler.getProjectType());
				} else {
					CodeReviewStatus.getInstance().addErrorMessage("Project Type defaulted to 'js'.  Unable to determine  default locale. Cannot review I18 Keys.");
					projectPropertiesMap.put(PROP_PROJECT_TYPE,"js");
				}

				projectPropertiesMap.put(PROP_DEFAULT_LOCALE,projectPropertiesHandler.getDefaultLocale());
				
				projectPropertiesMap.put(PROP_APP_LOGOS,projectPropertiesHandler.getAppLogoImages());
				
				projectPropertiesMap.put(PROP_SPLASH_IMAGES,projectPropertiesHandler.getSplashImages());
				
				projectPropertiesMap.put(PROP_IPHONE_SELF_SIGNED_CERT,projectPropertiesHandler.getIPhoneSelfCertificateValue());
				
				projectPropertiesMap.put(PROP_ANDROID_SELF_SIGNED_CERT,projectPropertiesHandler.getAndroidSelfCertificateValue());
				
			} else {
				CodeReviewStatus.getInstance().addErrorMessage(PROJECT_PROPERTIES_FILE + " not found. Assuming the Project to be 'js'.  Unable to determine  default locale. Cannot review I18 Keys");
				projectPropertiesMap.put(PROP_PROJECT_TYPE,"js");
			}
			
			
		} catch (Exception excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp,"Error reading the file : " + PROJECT_PROPERTIES_FILE + " :: " + excp + " . Assuming the Project to be 'js'. Unable to determine  default locale. Cannot review I18 Keys");
			projectPropertiesMap.put(PROP_PROJECT_TYPE,"js");
		}
	}
}
