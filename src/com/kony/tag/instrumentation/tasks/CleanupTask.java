package com.kony.tag.instrumentation.tasks;

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
		
		reviewUtil.deleteDirectory(getProjectConfig().getProjectPath() + CODE_INSTRUMENTATION_PATH);
		reviewUtil.createDirectory(getProjectConfig().getProjectPath() + CODE_INSTRUMENTATION_PATH);
		
		reviewUtil.createDirectory(codeBackupDir);
		reviewUtil.createDirectory(getProjectConfig().getReviewOutputPath());
		reviewUtil.createDirectory(codeBackupDir + LOCATION_JS_GEN_SRC_FOLDER);
		reviewUtil.createDirectory(codeBackupDir + LOCATION_JS_NONGEN_SRC_FOLDER);

	}
}