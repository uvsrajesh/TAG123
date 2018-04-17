package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class RedundantServiceCallStatusCheckRule extends JsReviewMasterTask  {
	
	public static final String PROPS_RULE_NAME = "rules.js.014.redundant_service_call_status_check_rule";
	public static final String RULE_ID = "JS-014";
	private static final String STATUS_CHECK_INFO = "(.*)status *== *\"*'*400(.*)";
	private static final String WARNING_SHORT_DESC = "This status check might be redundant.";
	private static final String WARNING_DESC = "Perform the network call status check in the common service callback.  Don't even pass the status to the service specific callback.";

	public RedundantServiceCallStatusCheckRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		// TODO Auto-generated constructor stub
		String[] ruleDescriptions = {WARNING_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
	}

	public void init() {
	}
	
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName,
			int lineNum, int lastLineNum, String reviewLine, int passNumber,
			String currFunctionName, boolean functionEndFlag,
			int prevCommentedLinesCount) throws TagToolException {
		// TODO Auto-generated method stub
		if(reviewLine.matches(STATUS_CHECK_INFO)){
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

