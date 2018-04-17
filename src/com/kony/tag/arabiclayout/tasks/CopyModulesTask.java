package com.kony.tag.arabiclayout.tasks;

import com.kony.tag.arabiclayout.impl.ArLayoutManager;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;

public class CopyModulesTask extends CopyMasterTask implements ProjectConstants{

	public CopyModulesTask(ProjectConfig projectConfig) {
		super(projectConfig);
	}
	
	public void execute() throws TagToolException {
		ReviewUtil reviewUtil = new ReviewUtil();
			/*String androidAnimationFile = getProjectConfig().getArabicLayoutProperty(PROP_ANDROID_ANIMATION_FILE);
			String channel = ArLayoutManager.getChannel();
			if(androidAnimationFile != null && androidAnimationFile.endsWith(".js") && (channel.equals("android") || channel.equals("iphone"))){
				String androidSrcAnimationFile = getProjectConfig().getProjectPath() + LOCATION_JS_MODULES + FILE_DELIMITER + androidAnimationFile;
				reviewUtil.copyFile(androidSrcAnimationFile, getProjectConfig().getCodeBackupPath() + FILE_DELIMITER +ArLayoutManager.getChannel() + FILE_DELIMITER + androidAnimationFile);
			}
			
			String iphoneAnimationFile = getProjectConfig().getArabicLayoutProperty(PROP_IPHONE_ANIMATION_FILE);
			if(iphoneAnimationFile != null && iphoneAnimationFile.endsWith(".js") && (channel.equals("android") || channel.equals("iphone"))){
				String iphoneSrcAnimationFile = getProjectConfig().getProjectPath() + LOCATION_JS_MODULES + FILE_DELIMITER + iphoneAnimationFile;
				reviewUtil.copyFile(iphoneSrcAnimationFile, getProjectConfig().getCodeBackupPath() + FILE_DELIMITER +ArLayoutManager.getChannel() + FILE_DELIMITER + iphoneAnimationFile);
			}*/			
			reviewUtil.copyAllFiles(
					(getProjectConfig().getProjectPath() + LOCATION_JS_GEN_SRC_ROOT_FOLDER +FILE_DELIMITER + ArLayoutManager.getChannel() + LOCATION_JS_GEN_SRC_FOLDER) ,
					(getProjectConfig().getCodeBackupPath()) + FILE_DELIMITER +ArLayoutManager.getChannel()
					);
	}
}