package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.io.File;
import java.util.List;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;
import com.kony.tag.js.codereview.util.KonyWidgetSpecificSkinsHandler;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.FormUtil;

public class ButtonImageBackgroundRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.013.button_image_background_rule";
	public static final String RULE_ID = "FRM-013";
	
	private static final String ERR_HEXPAND = "Horizontal Expand property enabled when button has Image Background. Check if this is intentional. Note: This rule is applicable only if the Button has a Skin defined for Image background";
	private static final String ERR_VEXPAND = "Vertical Expand property enabled when button has Image Background. Check if this is intentional. Note: This rule is applicable only if the Button has a Skin defined for Image background";
	
	private static final String ERR_SHORT_DESC = "Button Expand property not disabled";
	
	private String _formName = null;
	
	private List<String> _buttonSkinsWithImages = null;
	private boolean skinsRead = false;
	private KonyWidgetSpecificSkinsHandler konyWidgetSpecificSkinsHandler = null;

	public ButtonImageBackgroundRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		skinsRead = false;
		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
		
		if (!skinsRead) {
			String projectSkinsFileLocation = getProjectConfig().getProjectPath() + DEFAULT_THEMES_PATH + getProjectConfig().getCodeReviewProperty(PROP_DEFAULT_THEME_FOR_SKINS);
			konyWidgetSpecificSkinsHandler = new KonyWidgetSpecificSkinsHandler();
			readAllButtonSkinsWithImages(projectSkinsFileLocation);
			_buttonSkinsWithImages = konyWidgetSpecificSkinsHandler.getAllButtonSkinsWithImages();
			konyWidgetSpecificSkinsHandler = null;
			skinsRead = true;
		}
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		
		List<WidgetVo> widgetList = null;
		String widgetType = widgetVo.getWidgetType();
		String skinId = null;
		
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		widgetList = widgetVo.getChildWidgets();
		
		if (isRuleApplicable(getReviewFileDirectory(), widgetType)) {
			skinId = widgetVo.getSkinId();
			
			if (_buttonSkinsWithImages.contains(skinId)) {
				if (widgetVo.isExpandHorizontalEnabled()) {
					addWarning(ERR_HEXPAND,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
				}
				if (widgetVo.isExpandVerticalEnabled()) {
					addWarning(ERR_VEXPAND,ERR_SHORT_DESC, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);					
				}
				
			}
		}
		
		if (widgetList == null || widgetList.size()==0) {
			return;
		}

		// Recursively review all widgets
		for (WidgetVo widget: widgetList) {
				review(widget);
		} 
		
	}
	
	private boolean isRuleApplicable(String directory, String widgetType) {
		
		if (directory == null || widgetType == null) {
			return false;
		}
		
		if (widgetType.equals(KONY_BUTTON)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	private void readAllButtonSkinsWithImages(String directoryName) {
		// First Review All JS Files
		ReviewUtil reviewUtil = new ReviewUtil();
		File[] skinFiles = null;
		String fileName = null;
		
		FormUtil formUtil = new FormUtil();
		konyWidgetSpecificSkinsHandler.init();
		
			
		 // This is a JS project. Review All JS Files
		skinFiles = reviewUtil.fetchFiles(directoryName);
			
			if (skinFiles == null || skinFiles.length==0) {
				return;
			}
				
			for(File reviewFile : skinFiles) {
					
				if (reviewFile.isDirectory()) {
					readAllButtonSkinsWithImages(reviewFile.getAbsolutePath());
					continue;
				}
				fileName = 	reviewFile.getName().trim();
				
				// Take out the @2x tag used for retina display
				if (!fileName.toLowerCase().endsWith(".xml")) {
					continue;
				}

				
				try {
					formUtil.readXML(reviewFile.getAbsolutePath(), konyWidgetSpecificSkinsHandler);
					
					// Wait till the file is read completely
					while(!konyWidgetSpecificSkinsHandler.isEndOfFileReached()) {};
				} catch (Exception e) {
					CodeReviewStatus.getInstance().addErrorMessage(e,"Error reading project skins " + e);
				}
			}		
	}	
}