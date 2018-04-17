package com.kony.tag.js.codereview.tasks.copy;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;


public class CopyProjectConfigFilesTask extends CopyMasterTask implements ProjectConstants{

	public CopyProjectConfigFilesTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_EVENTS_FILE) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_EVENTS_FILE)
				);
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_TABLET_EVENTS_FILE) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_TABLET_EVENTS_FILE)
				);
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_DESKTOP_EVENTS_FILE) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_DESKTOP_EVENTS_FILE)
				);
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_SKIN_FILE) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_SKIN_FILE)
				);
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_SEQUENCE_FILE) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_SEQUENCE_FILE)
				);
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_TABLET_SEQUENCE_FILE) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_TABLET_SEQUENCE_FILE)
				);
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_DESKTOP_SEQUENCE_FILE) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_DESKTOP_SEQUENCE_FILE)
				);
		
		reviewUtil.copyFile(
				(getProjectConfig().getProjectPath() + LOCATION_CONFIG_PROJECT_GLOBAL_VARS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_CONFIG_PROJECT_GLOBAL_VARS)
				);
	}
}