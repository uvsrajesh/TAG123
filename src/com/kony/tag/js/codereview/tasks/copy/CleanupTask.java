package com.kony.tag.js.codereview.tasks.copy;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;


public class CleanupTask extends CopyMasterTask implements ProjectConstants{

	public CleanupTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		String codeBackupDir = getProjectConfig().getCodeBackupPath();
		
		//reviewUtil.deleteDirectory(getCodeReviewConfig().getFormattedLuaOutputPath());
		reviewUtil.deleteDirectory(getProjectConfig().getReviewOutputPath());
		
		reviewUtil.deleteDirectory(codeBackupDir);
		reviewUtil.createDirectory(codeBackupDir);
		
		reviewUtil.createDirectory(getProjectConfig().getReviewOutputPath());
		//reviewUtil.createDirectory(getCodeReviewConfig().getFormattedLuaOutputPath());
		System.out.println("codebackupir: "+(codeBackupDir + LOCATION_ALL_FORMS));
		reviewUtil.createDirectory(codeBackupDir + LOCATION_ALL_FORMS);
		System.out.println("codebackupir: "+(codeBackupDir + LOCATION_ALL_MODULES + FILE_DELIMITER +"js"));
		reviewUtil.createDirectory(codeBackupDir + LOCATION_ALL_MODULES);
		reviewUtil.createDirectory(codeBackupDir + LOCATION_ALL_MODULES + FILE_DELIMITER +"js");

	}
}