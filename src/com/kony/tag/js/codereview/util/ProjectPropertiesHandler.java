package com.kony.tag.js.codereview.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.kony.tag.codereview.lua.Util;
import com.kony.tag.util.ProjectPropertiesReader;

public class ProjectPropertiesHandler extends XmlContentHandler {
	
	private String projectType = null;
	private String defaultLocale = null;
	private String appLogoImages = null;
	private String splashImages = null;
	private String android_allowselfsignedcertificates = null;
	private String allowselfsignedcertificates = null;
	private static final String[] IMAGE_PROPERTY_KEYS = {"applogokey","splashlogokey"};
	
	public String getProjectType() {
		return projectType;
	}
	
	public String getDefaultLocale() {
		return defaultLocale;
	}
	
	public String getAppLogoImages() {
		return appLogoImages;
	}
	
	public String getSplashImages() {
		return splashImages;
	}
	
	public String getAndroidSelfCertificateValue(){
		return android_allowselfsignedcertificates;
	}
	
	public String getIPhoneSelfCertificateValue(){
		return allowselfsignedcertificates;
	}

	public void init() {
		projectType = null;
		defaultLocale = null;
		appLogoImages = null;
		super.init();
	}
		
	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		String key = null;

		key = attributes.getValue("name");
		if (key != null && key.equals("project.type")) {
			projectType = attributes.getValue("value");
			//Util.printToConsole("JS: Project Type is :" + projectType);
		}
		
		if (key != null && key.equals("defaultlocalekey")) {
			defaultLocale = attributes.getValue("value");
			//Util.printToConsole("JS: Project Type is :" + projectType);
		}
		
		if(key != null && key.equals(ProjectPropertiesReader.PROP_ANDROID_SELF_SIGNED_CERT)){
			android_allowselfsignedcertificates = attributes.getValue("value");
		}
		
		if(key != null && key.equals(ProjectPropertiesReader.PROP_IPHONE_SELF_SIGNED_CERT)){
			allowselfsignedcertificates = attributes.getValue("value");
		}
		
		for(int i=0;i<IMAGE_PROPERTY_KEYS.length;i++) {
			if (key != null && key.equals(IMAGE_PROPERTY_KEYS[i])) {
				String logos = attributes.getValue("value");
				if(logos != null) {
					String[] temp = logos.split(",");
					if(temp != null) {
						for(String image : temp) {
							String[] channelLogo = image.split("=");
							if(channelLogo == null || channelLogo.length <= 1) {
								continue;
							}
							String fileName = channelLogo[1];
							if (fileName == null || fileName.length() <= 4) {
								continue;
							}
							int fileNameLength = fileName.length();
							String fileType = fileName.substring(fileNameLength-3);
							fileType = fileType.toLowerCase();
							if (!(fileType.equals("png") 
									|| fileType.equals("gif")
									|| fileType.equals("jpg")
									|| fileType.equals("jpeg"))) {
								continue;
							}
							sb.append(fileName + ",");
						}
						if(i == 0) {
							appLogoImages = sb.toString();
						} else if(i == 1) {
							splashImages = sb.toString();
						}
						
					}
				}
			}
		}
		
	}

}
