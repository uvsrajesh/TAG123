package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class ScreenLevelWidgetRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.007.screen_level_widget_rule";
	public static final String RULE_ID = "FRM-007";
	
	private static final String ERR_SEGMENT_SCREEN_LEVEL_WIDGET = "Consider marking the Segment Widget as screen level widget";
	private static final String ERR_MAP_SCREEN_LEVEL_WIDGET = "Consider marking the Map Widget as screen level widget";
	private static final String ERR_BROWSER_SCREEN_LEVEL_WIDGET = "Consider marking the Browser Widget as screen level widget";
	
	private static final String ERR_MULTIPLE_SCREEN_LEVEL_WIDGET = "Only one screen level widget should be designed per UI Form.";
	private static final String ERR_HEADER_FOOTER_SCREEN_LEVEL_WIDGET = "Box should be specified as header/footer to make it visible along with screen level segment widget.";
	
	private static final String ERR_SHORT_DESC_SCREEN_LEVEL_WIDGET = "Potential Screen Level Widget";
	private static final String ERR_SHORT_DESC_MULTIPLE_WIDGETS = "Multiple Screen Level Widgets per Form";
	
	private static final String NOTES_SEGMENT = "ignore the warning for all platforms other than  - [iPad,iPhone,Android/Android Tablet,Windows Phone/Windows Kiosk]";
	private static final String NOTES_MAP = "ignore the warning for all platforms other than  - [SPA]";
	private static final String NOTES_MULTIPLE_SCREEN_LEVEL_WIDGETS = " Screen Level Widgets found : ";
	
	private List<String> _screenLevelWidgets = null;
	private List<WidgetVo> _headerFooterWidgets = null;
	
	
	private String _formName = null;
	
	public ScreenLevelWidgetRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC_MULTIPLE_WIDGETS,ERR_SHORT_DESC_SCREEN_LEVEL_WIDGET};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
		
		if (null == _screenLevelWidgets) {
			_screenLevelWidgets = new ArrayList<String>();
		}
		if(null == _headerFooterWidgets) {
			_headerFooterWidgets = new ArrayList<WidgetVo>();
		}
		_headerFooterWidgets.clear();
		_screenLevelWidgets.clear();
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		List<WidgetVo> widgetList = null;
		String widgetType = widgetVo.getWidgetType();
		String reviewDirectory = getReviewFileDirectory();
		String errorMessage = null; 
		String specialNote = null;
	
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		widgetList = widgetVo.getChildWidgets();
		
		if (widgetVo.isScreenLevelWidget() && widgetVo.getWidgetName() != null 
				&& !_screenLevelWidgets.contains(widgetVo.getWidgetName())) {
			
				_screenLevelWidgets.add(widgetVo.getWidgetName());
		}
		
		if (isRuleApplicable(reviewDirectory, widgetType)) {
			if (!widgetVo.isScreenLevelWidget()) {
				if (widgetType.equals(KONY_SEGMENT)) {
					errorMessage = ERR_SEGMENT_SCREEN_LEVEL_WIDGET;
					specialNote = NOTES_SEGMENT;
				} else if (widgetType.equals(KONY_BROWSER)) {
					errorMessage = ERR_BROWSER_SCREEN_LEVEL_WIDGET;
					specialNote = BLANK;
				} else if (widgetType.equals(KONY_MAP)) {
					errorMessage = ERR_MAP_SCREEN_LEVEL_WIDGET;
					specialNote = NOTES_MAP;
				}
				
				addError(errorMessage, ERR_SHORT_DESC_SCREEN_LEVEL_WIDGET, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName()  + " : " + specialNote, _formName, 0, SEV_MED, RULE_ID);
			}

		}

		// Perform Form Level Attributes Review Here
		if (widgetList == null || widgetList.size()==0) {
			return;
		}

		// Recursively review all widgets
		for (WidgetVo widget: widgetList) {
				if(widget.getWidgetType().equals(KONY_HBOX)) {
					_headerFooterWidgets.add(widget);
				}
				review(widget);
		} 
	}
	
	private boolean isRuleApplicable(String directory, String widgetType) {
		boolean isApplicable = false;
		
		if (directory == null || widgetType == null) {
			return false;
		}
		
		if (widgetType.equals(KONY_BROWSER)) {
			// All folders applicable. Screen level widget property supported for all paltforms for a browser widget
			return true;
		}
		
		if (!(widgetType.equals(KONY_SEGMENT) || widgetType.equals(KONY_MAP))) {
			return false;
		}

		if (FORM_FOLDERS_ROOT_ALL_MOBILE_TAB.contains(directory)) {
			return true;
		}
		
		if (widgetType.equals(KONY_SEGMENT)) {
			if (FORM_FOLDERS_NATIVE_ONLY.contains(directory)) {
				isApplicable = true;
			}
		} else if (widgetType.equals(KONY_MAP)) {
			if (FORM_FOLDERS_NATIVE_ONLY.contains(directory) || 
					FORM_FOLDERS_MOBILEWEB_ONLY.contains(directory)) {
				isApplicable = true;
			}
		}	
		
		return isApplicable;
	}
	
	public void postFormReview() throws TagToolException {
		if (_screenLevelWidgets != null && _screenLevelWidgets.size()>1) {
			StringBuffer tmpStrBuffer = new StringBuffer();
			
			for (String name: _screenLevelWidgets) {
				tmpStrBuffer.append(name);
				tmpStrBuffer.append(SEMI_COLON);
			}
			
			addError(ERR_MULTIPLE_SCREEN_LEVEL_WIDGET, ERR_SHORT_DESC_MULTIPLE_WIDGETS, NOTES_MULTIPLE_SCREEN_LEVEL_WIDGETS + " : " + tmpStrBuffer.toString(), _formName, 0, SEV_MED, RULE_ID);
		}
		if(_screenLevelWidgets != null && _screenLevelWidgets.size() == 1) {
			for (WidgetVo widgetVo : _headerFooterWidgets) {
				if(widgetVo.getBoxPosition() == BOX_POSITION_AS_NORMAL) {
					addError(ERR_HEADER_FOOTER_SCREEN_LEVEL_WIDGET, ERR_SHORT_DESC_SCREEN_LEVEL_WIDGET, widgetVo.getWidgetType() + ":" + widgetVo.getWidgetName(), _formName, 0, SEV_MED, RULE_ID);
				}
			}
		}
	}

}

