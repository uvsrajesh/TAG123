package com.kony.tag.instrumentation.tasks;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.TAGToolsUtil;


public class CopyFilesTask extends CopyMasterTask implements ProjectConstants{

	public CopyFilesTask(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
		System.out.println("channel: "+TAGToolsUtil.getChannelName());
	reviewUtil.copyGeneratedFiles(
				(getProjectConfig().getProjectPath() + LOCATION_JS_GEN_SRC_ROOT_FOLDER + FILE_DELIMITER + TAGToolsUtil.getChannelName() + LOCATION_JS_GEN_SRC_FOLDER) ,
				(getProjectConfig().getCodeBackupPath() + LOCATION_JS_GEN_SRC_FOLDER)
				);
		
	reviewUtil.copyGeneratedFiles(
			(getProjectConfig().getProjectPath() + LOCATION_JS_GEN_SRC_ROOT_FOLDER + FILE_DELIMITER + TAGToolsUtil.getChannelName() + LOCATION_JS_NONGEN_SRC_FOLDER) ,
			(getProjectConfig().getCodeBackupPath() + LOCATION_JS_NONGEN_SRC_FOLDER)
			);
	}
}