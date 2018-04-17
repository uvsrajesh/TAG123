package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class ThresholdWidgetCountRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.014.max_widgets_rule";
	public static final String RULE_ID = "FRM-014";
	
	public static final String PROPS_THRESOLD_WIDGET_COUNT_MOBILE = "rules.form.all.014.max_widgets_allowed_mobile";
	public static final String PROPS_THRESOLD_WIDGET_COUNT_TABLET = "rules.form.all.014.max_widgets_allowed_tablet";
	public static final String PROPS_THRESOLD_WIDGET_COUNT_DESKTOP = "rules.form.all.014.max_widgets_allowed_desktop";
	
	public static final String PROPS_THRESOLD_WIDGET_COUNT_DIALOG_MOBILE = "rules.form.all.014.max_widgets_allowed_dialog_mobile";
	public static final String PROPS_THRESOLD_WIDGET_COUNT_DIALOG_TABLET = "rules.form.all.014.max_widgets_allowed_dialog_tablet";
	public static final String PROPS_THRESOLD_WIDGET_COUNT_DIALOG_DESKTOP = "rules.form.all.014.max_widgets_allowed_dialog_desktop";

	public static final String PROPS_THRESOLD_WIDGET_COUNT_TEMPLATE_MOBILE = "rules.form.all.014.max_widgets_allowed_template_mobile";
	public static final String PROPS_THRESOLD_WIDGET_COUNT_TEMPLATE_TABLET = "rules.form.all.014.max_widgets_allowed_template_tablet";
	public static final String PROPS_THRESOLD_WIDGET_COUNT_TEMPLATE_DESKTOP = "rules.form.all.014.max_widgets_allowed_template_desktop";
	
	private static final String ERR_SHORT_DESC = "Threshold Widgets Exceeded (per Form)";
	
	private static final String ERR_THRESOLD_EXCEEDED = "More than thresold number of widgets found. Form may be too heavy. Consider re design of the UI.";
	private static final String NOTES1 = " Suggested Thresold Widget count : ";
	private static final String NOTES2 = "  Actual Widget count : ";

	private static int THRESHOLD_WIDGET_COUNT_MOBILE = 0;
	private static int THRESHOLD_WIDGET_COUNT_TABLET = 0;
	private static int THRESHOLD_WIDGET_COUNT_DESKTOP = 0;
	
	private static int THRESHOLD_WIDGET_COUNT_DIALOG_MOBILE = 0;
	private static int THRESHOLD_WIDGET_COUNT_DIALOG_TABLET = 0;
	private static int THRESHOLD_WIDGET_COUNT_DIALOG_DESKTOP = 0;

	private static int THRESHOLD_WIDGET_COUNT_TEMPLATE_MOBILE = 0;
	private static int THRESHOLD_WIDGET_COUNT_TEMPLATE_TABLET = 0;
	private static int THRESHOLD_WIDGET_COUNT_TEMPLATE_DESKTOP = 0;

	private String _formName = null;
	
	private int _widgetCount = 0;

	public ThresholdWidgetCountRule(ProjectConfig codeReviewConfig) {
		
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);
		
		String count = null;
		
		
		try {
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_MOBILE);
			THRESHOLD_WIDGET_COUNT_MOBILE = Integer.parseInt(count);
			
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_TABLET);
			THRESHOLD_WIDGET_COUNT_TABLET = Integer.parseInt(count);
			
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_DESKTOP);
			THRESHOLD_WIDGET_COUNT_DESKTOP = Integer.parseInt(count);
			
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_DIALOG_MOBILE);
			THRESHOLD_WIDGET_COUNT_DIALOG_MOBILE = Integer.parseInt(count);
			
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_DIALOG_TABLET);
			THRESHOLD_WIDGET_COUNT_DIALOG_TABLET = Integer.parseInt(count);
			
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_DIALOG_DESKTOP);
			THRESHOLD_WIDGET_COUNT_DIALOG_DESKTOP = Integer.parseInt(count);

			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_TEMPLATE_MOBILE);
			THRESHOLD_WIDGET_COUNT_TEMPLATE_MOBILE = Integer.parseInt(count);
			
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_TEMPLATE_TABLET);
			THRESHOLD_WIDGET_COUNT_TEMPLATE_TABLET = Integer.parseInt(count);
			
			count = codeReviewConfig.getCodeReviewProperty(PROPS_THRESOLD_WIDGET_COUNT_TEMPLATE_DESKTOP);
			THRESHOLD_WIDGET_COUNT_TEMPLATE_DESKTOP = Integer.parseInt(count);

		} catch (Exception e) {
			// Set Default value
			CodeReviewStatus.getInstance().addErrorMessage(e, "Error reading the threshold widget counts from code review properties file. " + e + " . Defaulting all max widget thresholds to the  hardcoded values !!");
			THRESHOLD_WIDGET_COUNT_MOBILE = 50;
			THRESHOLD_WIDGET_COUNT_TABLET = 50;
			THRESHOLD_WIDGET_COUNT_DESKTOP = 50;
			
			THRESHOLD_WIDGET_COUNT_DIALOG_MOBILE = 20;
			THRESHOLD_WIDGET_COUNT_DIALOG_TABLET = 20;
			THRESHOLD_WIDGET_COUNT_DIALOG_DESKTOP = 20;

			THRESHOLD_WIDGET_COUNT_TEMPLATE_MOBILE = 10;
			THRESHOLD_WIDGET_COUNT_TEMPLATE_TABLET = 10;
			THRESHOLD_WIDGET_COUNT_TEMPLATE_DESKTOP = 10;
			
		}
		
		_widgetCount= 0;
		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);

		_widgetCount = 0;
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
			_widgetCount++;
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
		
		return true;
	}
	
	public void postFormReview() throws TagToolException {
		
		if(_widgetCount>0) {
			_widgetCount--;
		}

		if (getReviewFileDirectory() != null && getReviewFileDirectory().startsWith(LOCATION_MOBILE_FORMS)) {

			/*
			if (getReviewFileDirectory().equals(LOCATION_MOBILE_FORMS)) {
				System.out.println("WIDGET COUNT,mobile," + _formName +"," + _widgetCount);
			}
			*/
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_MOBILE) {
				addError(ERR_THRESOLD_EXCEEDED, ERR_SHORT_DESC, NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_MOBILE + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		} else if (getReviewFileDirectory() != null && getReviewFileDirectory().startsWith(LOCATION_TABLET_FORMS)) {
			/*
			if (getReviewFileDirectory().equals(LOCATION_TABLET_FORMS)) {
				System.out.println("WIDGET COUNT,tablet," + _formName +"," + _widgetCount);
			}
			*/
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_TABLET) {
				addError(ERR_THRESOLD_EXCEEDED, ERR_SHORT_DESC, NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_TABLET + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		} else if (getReviewFileDirectory() != null && getReviewFileDirectory().startsWith(LOCATION_DESKTOP_FORMS)) {
			/*
			if (getReviewFileDirectory().equals(LOCATION_DESKTOP_FORMS)) {
				System.out.println("WIDGET COUNT,Desktop," + _formName +"," + _widgetCount);
			}
			*/

			if (_widgetCount > THRESHOLD_WIDGET_COUNT_DESKTOP) {
				addError(ERR_THRESOLD_EXCEEDED, ERR_SHORT_DESC, NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_DESKTOP + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		}
		
		if (getReviewFileDirectory() != null && getReviewFileDirectory().startsWith(LOCATION_MOBILE_POPUPS)) {

			/*
			if (getReviewFileDirectory().equals(LOCATION_MOBILE_POPUPS)) {
				System.out.println("WIDGET COUNT,mobilePopUp," + _formName +"," + _widgetCount);
			}
			*/
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_DIALOG_MOBILE) {
				addError(ERR_THRESOLD_EXCEEDED,ERR_SHORT_DESC,  NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_DIALOG_MOBILE + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		} else if (getReviewFileDirectory() != null && getReviewFileDirectory().startsWith(LOCATION_TABLET_POPUPS)) {
			/*
			if (getReviewFileDirectory().equals(LOCATION_TABLET_POPUPS)) {
				System.out.println("WIDGET COUNT,tabletPopUp," + _formName +"," + _widgetCount);
			}
			*/
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_DIALOG_TABLET) {
				addError(ERR_THRESOLD_EXCEEDED, ERR_SHORT_DESC, NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_DIALOG_TABLET + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		} else if (getReviewFileDirectory() != null && getReviewFileDirectory().startsWith(LOCATION_DESKTOP_POPUPS)) {
			/*
			if (getReviewFileDirectory().equals(LOCATION_DESKTOP_POPUPS)) {
				System.out.println("WIDGET COUNT,DesktopPopUp," + _formName +"," + _widgetCount);
			}
			*/
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_DIALOG_DESKTOP) {
				addError(ERR_THRESOLD_EXCEEDED, ERR_SHORT_DESC, NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_DIALOG_DESKTOP + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		}
		
		if (getReviewFileDirectory() != null && 
				(getReviewFileDirectory().startsWith(LOCATION_MOBILE_HEADERS) || getReviewFileDirectory().startsWith(LOCATION_MOBILE_FOOTERS))) {
			
			//System.out.println("WIDGET COUNT,mobileHeader/Footer," + _formName +"," + _widgetCount);
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_TEMPLATE_MOBILE) {
				addError(ERR_THRESOLD_EXCEEDED, ERR_SHORT_DESC, NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_TEMPLATE_MOBILE + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		} else if (getReviewFileDirectory() != null && 
				(getReviewFileDirectory().startsWith(LOCATION_TABLET_HEADERS) || getReviewFileDirectory().startsWith(LOCATION_TABLET_FOOTERS))) {
			//System.out.println("WIDGET COUNT,tabletHeader/Footer," + _formName +"," + _widgetCount);
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_TEMPLATE_TABLET) {
				addError(ERR_THRESOLD_EXCEEDED,ERR_SHORT_DESC,  NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_TEMPLATE_TABLET + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		} else if (getReviewFileDirectory() != null && 
				(getReviewFileDirectory().startsWith(LOCATION_DESKTOP_HEADERS) || getReviewFileDirectory().startsWith(LOCATION_DESKTOP_FOOTERS))) {
			//System.out.println("WIDGET COUNT,desktopHeader/Footer," + _formName +"," + _widgetCount);
			if (_widgetCount > THRESHOLD_WIDGET_COUNT_TEMPLATE_DESKTOP) {
				addError(ERR_THRESOLD_EXCEEDED, ERR_SHORT_DESC, NOTES1 + " : " + THRESHOLD_WIDGET_COUNT_TEMPLATE_DESKTOP + NOTES2 + " : " + _widgetCount, _formName, 0, SEV_MED, RULE_ID);
			}
		}		
	}	
}