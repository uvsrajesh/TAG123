package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class KonyOSDeviceInfoRule extends JsReviewMasterTask  {
	
	public static final String PROPS_RULE_NAME = "rules.js.010.kony_os_device_info_rule";
	public static final String RULE_ID = "JS-010";
	private static final String KONY_DEVICE_INFO = "kony.os.deviceInfo";
	private static final String WARNING_SHORT_DESC = "kony.os.deviceInfo() is an expensive operation.";
	private static final String WARNING_DESC = "Use kony.os.deviceInfo() only once in the application and store the values in global variable to be referred throughout the application where necessary.";

	public KonyOSDeviceInfoRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		// TODO Auto-generated constructor stub
		String[] ruleDescriptions = {WARNING_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
	}

	public void init() {
		//JSReviewUtil.printToConsole("Initialized Kony Device Info rule");
	}
	
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName,
			int lineNum, int lastLineNum, String reviewLine, int passNumber,
			String currFunctionName, boolean functionEndFlag,
			int prevCommentedLinesCount) throws TagToolException {
		// TODO Auto-generated method stub
		//JSReviewUtil.printToConsole("Js-010:" + reviewLine);
		if(reviewLine.contains(KONY_DEVICE_INFO)){
			//JSReviewUtil.printToConsole("Js-010: Adding warning");
			addWarning(WARNING_DESC, WARNING_SHORT_DESC, reviewLine, fileName, lineNum, SEV_MED, RULE_ID);
		}
		return getComments();
	}

	@Override
	protected boolean isLineSplitNeeded() {
		// TODO Auto-generated method stub
		return false;
	}

}
