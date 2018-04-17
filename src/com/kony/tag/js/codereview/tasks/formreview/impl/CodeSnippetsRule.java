package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;

public class CodeSnippetsRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.form.all.011.code_snippets_rule";
	public static final String RULE_ID = "FRM-011";
	
	private static final String ERR_CODE_SNIPPET = "Use functions instead of Code Snippets, for better re-usability";
	private static final String ERR_SHORT_DESC = "Code Snippet found";

	private static final String NOTES_SNIPPETS = " Snippets Found : ";

	private String _formName = null;

	public CodeSnippetsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);

		rulesInit(null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName) {
		_formName = null;
		
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
		
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		List<WidgetVo> widgetList = null;
		String widgetType = widgetVo.getWidgetType();
		StringBuffer snippets = null;
		int count=0;
		
		if (widgetType != null && 
				widgetType.equals(KONY_FORM)) {
			_formName = widgetVo.getWidgetName();
		}
		
		widgetList = widgetVo.getChildWidgets();
		
		if (isRuleApplicable(getReviewFileDirectory(), widgetVo.getWidgetType())) {
			if (widgetVo.getCodeSnippets() != null && 
					widgetVo.getCodeSnippets().size()>0) {
				
				snippets = new StringBuffer();
				count = widgetVo.getCodeSnippets().size();
				
				for (String snippet : widgetVo.getCodeSnippets()) {
					snippets.append(">> SNIPPET >>");
					snippets.append(snippet);
				}
				addWarning(ERR_CODE_SNIPPET, ERR_SHORT_DESC,widgetVo.getWidgetName() + SEMI_COLON + count + NOTES_SNIPPETS  + snippets.toString(), _formName, 0, SEV_MED, RULE_ID);
			} 
		}
		
		// Perform Form Level Attributes Review Here
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
}


