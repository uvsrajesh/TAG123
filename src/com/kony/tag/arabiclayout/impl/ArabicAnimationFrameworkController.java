package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import com.kony.tag.arabiclayout.util.ArabicLayoutPropertiesUtil;
import com.kony.tag.arabiclayout.util.ArabicLayoutUtil;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ArabicAnimationFrameworkController extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.ar.002.arabic_animation_framework_rule";
	public static final String RULE_ID = "AR-002";
	public static final String widgetTree = "AR-002";
	
	
	private List<String> _lines = new ArrayList<String>();
	
	private static final String ERR_SHORT_DESC = "Animation Framework Reversal";

	@Override
	protected boolean isLineSplitNeeded(){return false;}
	
	public ArabicAnimationFrameworkController(ProjectConfig projectConfig) {
		super(projectConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.initArabicTask(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,2);
		
		init();
	}
	
	public void init() {
		_lines.clear();
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {
		if (passNumber == 1) {
			_lines.add(line);
			if (lineNum == lastLineNum) {
				processJSFile(fileName, _lines);
				
			}
		}
		return null;
	}
	
	private void processJSFile(String fileName, List<String> formattedLines) {
		
		try {
			String buildChannel = ArLayoutManager.getChannel();
			
			ArAnimationFrameworkReversalRule arAnimationFrameworkReversalRule = new ArAnimationFrameworkReversalRule(null,null,getProjectConfig(),buildChannel);
			
			//Replace form Name with Ar Suffix
			String fName = fileName.substring(0, fileName.length() - 3);
			String excludeFlag = getProjectConfig().getArabicLayoutProperty(PROP_JS_ANIMATION_EXCLUDE);
			boolean excludeAnimation = (excludeFlag == null || excludeFlag.equals(fName));
			if(!excludeAnimation) {
				formattedLines = arAnimationFrameworkReversalRule.reverseAnimationValues(formattedLines);
			}
	
			// Add a comment at the top of the file
			String timestamp = BLANK + Calendar.getInstance().getTime().toString();
			JSReviewUtil.printToConsole("excludeFlag1: "  );
			formattedLines.add(0, "//Do not Modify!! This is an auto generated module for '"+ArLayoutManager.getChannel()+"'. Generated on " + timestamp);
			ArabicLayoutUtil.getInstance().createReportFile(formattedLines, fileName);
			JSReviewUtil.printToConsole("successfully created : " + AR_FILE_PREFIX + fileName);
			JSReviewUtil.printToConsole("processJSFile END : " +  fileName);
			
		}catch (Exception e) {
			JSReviewUtil.printDetailedLog("ERROR processing File : " + fileName + " : " + e + " Stack Trace : " + e.getStackTrace());
			JSReviewUtil.printDetailedLog("ERROR : Could not transform File : " + fileName);
		}
		
	}
}
	