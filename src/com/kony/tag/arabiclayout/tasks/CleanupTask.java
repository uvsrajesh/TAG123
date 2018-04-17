package com.kony.tag.arabiclayout.tasks;

import com.kony.tag.arabiclayout.impl.ArLayoutManager;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;


public class CleanupTask extends CopyMasterTask implements ProjectConstants{

	public CleanupTask(ProjectConfig projectConfig) {
		super(projectConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		String codeBackupDir = getProjectConfig().getCodeBackupPath();
		
		//reviewUtil.deleteDirectory(getProjectConfig()).getFormattedLuaOutputPath());
		//reviewUtil.deleteDirectory(getProjectConfig()).getReviewOutputPath());
		
		//reviewUtil.deleteDirectory(getProjectConfig().getTempWorkAreaLocation());
		reviewUtil.createDirectory(getProjectConfig().getTempWorkAreaLocation());
		reviewUtil.createDirectory(getProjectConfig().getCodeBackupPath());
		reviewUtil.createDirectory(getProjectConfig().getCodeBackupPath() + FILE_DELIMITER + ArLayoutManager.getChannel());
		reviewUtil.createDirectory(getProjectConfig().getReviewOutputPath());
		reviewUtil.createDirectory(getProjectConfig().getReviewOutputPath() + FILE_DELIMITER + ArLayoutManager.getChannel());
		
	}
}