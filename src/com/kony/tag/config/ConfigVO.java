package com.kony.tag.config;

import java.util.Properties;

public class ConfigVO {
	
	// If not Lua , the project is assumed to be JS project
	private String projectPath = null;
	private String codeBackupPath = null;
	private String reviewOutputPath = null;
	private String tempWorkAreaLocation = null;

	public String getTempWorkAreaLocation() {
		return tempWorkAreaLocation;
	}

	public void setTempWorkAreaLocation(String tempWorkAreaLocation) {
		this.tempWorkAreaLocation = tempWorkAreaLocation;
	}
	
	public String getReviewOutputPath() {
		return reviewOutputPath;
	}

	public void setReviewOutputPath(String reviewOutputPath) {
		this.reviewOutputPath = reviewOutputPath;
	}
	
	public String getCodeBackupPath() {
		return codeBackupPath;
	}

	public void setCodeBackupPath(String codeBackupPath) {
		this.codeBackupPath = codeBackupPath;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
}
