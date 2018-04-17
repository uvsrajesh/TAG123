package com.kony.tag.js.codereview.tasks.copy;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;


public class CopyModulesTask extends CopyMasterTask implements ProjectConstants{

	public CopyModulesTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		
			reviewUtil.copyAllFiles(
					(getProjectConfig().getProjectPath() + LOCATION_JS_MODULES) ,
					(getProjectConfig().getCodeBackupPath() + LOCATION_JS_MODULES)
					);
	}
}