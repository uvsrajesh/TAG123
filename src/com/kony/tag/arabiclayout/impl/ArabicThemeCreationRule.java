package com.kony.tag.arabiclayout.impl;


import java.io.File;

import com.kony.tag.arabiclayout.tasks.CopyMasterTask;
import com.kony.tag.arabiclayout.util.ArabicThemeCreationUtil;
import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.util.ReviewUtil;

public class ArabicThemeCreationRule extends CopyMasterTask implements ProjectConstants{
		
	public ArabicThemeCreationRule(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	@Override
	public void execute() throws TagToolException{
		String themeDir = getProjectConfig().getProjectPath() + DEFAULT_THEMES_PATH;
		
		ReviewUtil reviewUtil = new ReviewUtil();
		reviewUtil.createDirectory(themeDir + ARABIC_THEME);
		
		reviewUtil.copyAllFiles(
				(themeDir + DEFAULT_THEME) ,
				(themeDir + ARABIC_THEME)
				);
		
		replaceFontNames(themeDir + ARABIC_THEME);
		
	}
	
	public void replaceFontNames(String fileLocation) {
		File file = new File(fileLocation + DEFAULT_SKINS_FILE);
		if(file.exists()) {
			ArabicThemeCreationUtil arabicThemeHandler = new ArabicThemeCreationUtil();
			arabicThemeHandler.updateFontNames(fileLocation + DEFAULT_SKINS_FILE);
		} else {
			CodeReviewStatus.getInstance().addErrorMessage("The "+DEFAULT_SKINS_FILE+" is not found.");
		}
	}
	
	
}
	
