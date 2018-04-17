package com.kony.tag.js.codereview.tasks.copy;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;


public class CopyFormsTask extends CopyMasterTask implements ProjectConstants{

	public CopyFormsTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		
	reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_ALL_FORMS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_ALL_FORMS)
				);
		
		/*reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath()) ,
				(getProjectConfig().getCodeBackupPath())
				);*/
		
		/*reviewUtil.copyAllFiles(
				("D:\\SampleApps\\temp\\SimulatorApp\\build\\luaandroid\\dist\\SimulatorApp\\assets\\js\\startup") ,
				("D:\\SampleApps\\temp\\SimulatorApp\\build\\luaandroid\\dist\\SimulatorApp\\assets\\js\\startup\\backup")
				);*/

		/*
		reviewUtil.copyAllFiles(
				(getCodeReviewConfig().getProjectPath() + LOCATION_TABLET_FORMS) ,
				(getCodeReviewConfig().getCodeBackupPath() + LOCATION_TABLET_FORMS)
				);
		
		reviewUtil.copyAllFiles(
				(getCodeReviewConfig().getProjectPath() + LOCATION_DESKTOP_FORMS) ,
				(getCodeReviewConfig().getCodeBackupPath() + LOCATION_DESKTOP_FORMS)
				);

		*/
	}
}