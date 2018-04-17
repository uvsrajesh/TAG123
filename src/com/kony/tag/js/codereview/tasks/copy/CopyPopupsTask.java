package com.kony.tag.js.codereview.tasks.copy;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;


public class CopyPopupsTask extends CopyMasterTask implements ProjectConstants{

	public CopyPopupsTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_MOBILE_POPUPS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_MOBILE_POPUPS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_TABLET_POPUPS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_TABLET_POPUPS)
				);
		
		reviewUtil.copyAllFiles(
				(getProjectConfig().getProjectPath() + LOCATION_DESKTOP_POPUPS) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_DESKTOP_POPUPS)
				);
	}
}