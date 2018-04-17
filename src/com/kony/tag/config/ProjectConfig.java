package com.kony.tag.config;

import java.util.ArrayList;

import com.kony.tag.arabiclayout.util.ArabicLayoutPropertiesUtil;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;

public class ProjectConfig {
	
	private ConfigVO reviewConfigVo = null;
	private static ProjectConfig projectConfig = null;
	ArrayList<String> callBacks = new ArrayList<String>();
	ArrayList<String> jsFileArray = new ArrayList<String>();
	private String instrumentMode = "";
	
	private ProjectConfig() {
		// Do not use this constructor
	}
	
	public String getInstrumentMode(){
		return instrumentMode;	
	}
	
	public void setInstrumentMode(String mode){
		instrumentMode = mode;
	}
	
	public ArrayList<String> getCallBackArray(){
		return callBacks;
	}
	
	public void setJsFilesArray(ArrayList<String> jsFileArray) {
		this.jsFileArray = jsFileArray;
	}
	
	public ArrayList<String> getJsFilesArray(){
		return jsFileArray;
	}
	
	public void setCallBackArray(ArrayList<String> callBackArray) {
		this.callBacks = callBackArray;
	}
	
	private ProjectConfig(ConfigVO configVo) {
		this.reviewConfigVo = configVo;
	}
	
	public static ProjectConfig getCodeReviewConfig(ConfigVO configVo) {
		projectConfig = new ProjectConfig(configVo);
		return projectConfig;
	}
	
	public String getProjectPath() {
		if (null != reviewConfigVo) {
			return reviewConfigVo.getProjectPath();
		} else {
			return null;
		}
	}
	
	
	public String getCodeBackupPath() {
		if (null != reviewConfigVo) {
			return reviewConfigVo.getCodeBackupPath();
		} else {
			return null;
		}
	}

	public String getReviewOutputPath() {
		if (null != reviewConfigVo) {
			return reviewConfigVo.getReviewOutputPath();
		} else {
			return null;
		}
	}
	
	public String getTempWorkAreaLocation() {
		if (null != reviewConfigVo) {
			return reviewConfigVo.getTempWorkAreaLocation();
		} else {
			return null;
		}
	}

	
	public String getCodeReviewProperty(String propertyName) {
		return ReviewPropertiesUtil.getProperty(propertyName).trim();
	}
	
	public String getArabicLayoutProperty(String propertyName) {
		return  ArabicLayoutPropertiesUtil.getProperty(propertyName).trim();
	}

}
