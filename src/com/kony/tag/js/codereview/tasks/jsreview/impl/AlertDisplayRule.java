package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class AlertDisplayRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.003.alert_display_rule";
	public static final String RULE_ID = "JS-003";
	
	public static final String ERR_IMPROPER_ALERT_DISPLAY = "Displaying an alert popup should be the last statement of a function. ";
	private final String NULL = "null"; 
	
	private static final String ALERT_API = "ui.alert";
	//private static List<String> myFunctions = new ArrayList<String>();
	//private static List<String> allFunctions = new ArrayList<String>();
	//private static boolean taskDone = false;
	
	private String _functionName = null;
	private List<String> _alertFunctions = null;
	private List<String> _konyAlertAPIFunctions = null;
	private String _alertDisplayFunction = null;
	private String _alertFunction = null;
	private int _alertDisplayLine = 0;
	private final char[] _acceptedPreChars = new char[]{CHAR_EQUALS,CHAR_COLON,CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT};
	private final char[] _acceptedPostChars = new char[]{CHAR_DOT, CHAR_PARANTHESIS_OPEN};
	
	private List<AlertDisplayRuleHelper> _alertHelpers = null;
	private static final String ERR_SHORT_DESC = "Statements below Alert display";

	@Override
	protected boolean isLineSplitNeeded(){return true;}
	
	public AlertDisplayRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME,2);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,2);
		
		init();
	}
	
	public void init() {
		_functionName = null;
		
		if (null == _alertHelpers) {
			_alertHelpers = new ArrayList<AlertDisplayRuleHelper>();
		} else {
			_alertHelpers.clear();
		}
		
		if (null == _alertFunctions) {
			_alertFunctions = new ArrayList<String>();
		}

		if (null == _konyAlertAPIFunctions) {
			_konyAlertAPIFunctions = new ArrayList<String>();
			_konyAlertAPIFunctions.add(ALERT_API);
		}

	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {

		/*
		final String CHECK_STRING = "var kumarVaraible = 1";
		
		if (line.indexOf(CHECK_STRING)>=0) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		
		boolean ignoreCase=false;		
		String fName = null;
		String tmpLine = null;
		int index;
		FunctionVo fnVo = null;
		List<String> alertKeywords = null;
		AlertDisplayRuleHelper alertDisplayRuleHelper = null;

		fnVo = getFunctionName(line);
		fName = fnVo.getFunctionName();
		
		if (fName != null) {
			if (fName.equals(NULL)) {
				_functionName = null;
			} else {
				if (_functionName == null || !_functionName.equals(fName)) {
					_functionName = fName;
					//System.out.println("ADDING FUNCTION NAME " + _functionName);
					//myFunctions.add(_functionName);
				}
			}
		}
		
		if (passNumber == 1) {
			
			// In First Pass - get the list of all functions which have alert display logic 
			if (_functionName != null && _functionName.length()>0 && getKeywordIndex(line,ALERT_API,_acceptedPreChars,_acceptedPostChars,true) >=0
					&& !_alertFunctions.contains(_functionName)) {
				_alertFunctions.add(_functionName);
				//System.out.println("ADDED " + _functionName);
			}
		}
		
		if (passNumber == 2) {
			
			if (_functionName != null && _alertFunctions.contains(_functionName)) {
				alertKeywords = _konyAlertAPIFunctions;
				ignoreCase = true;
			} else {
				alertKeywords = _alertFunctions;
				ignoreCase = false;
			}
			
			tmpLine = line;
			index=-1;
			
			// In Second Pass - check if any function has alert display logic in any line other than the last line
			String tmpReviewLine = null;
			tmpReviewLine = truncateBetweenQuotes(line);
			index = checkForAlert(tmpReviewLine, lineNum, alertKeywords, ignoreCase);
				
			if (index>=0) {
				tmpLine = tmpReviewLine.substring(index);
				alertDisplayRuleHelper = new AlertDisplayRuleHelper(getProjectConfig());
				alertDisplayRuleHelper.init(_alertFunctions,_alertDisplayFunction, _alertFunction,_alertDisplayLine);
				_alertHelpers.add(alertDisplayRuleHelper);
				//System.out.println("VERIFYING LINE FOR ALERT " + fileName + " : " +lineNum);
			}
			
			for (AlertDisplayRuleHelper alertHelper : _alertHelpers) {
				alertHelper.checkAlertDisplayErrors(fileName, lineNum, lastLineNum, tmpLine,_functionName, passNumber,fName);
				//System.out.println("COMMENTS SIZE : " + alertHelper.getComments().size());
			}

		}
		
		return getComments();
}
	
	public void updateFileLevelComments() {
		for (AlertDisplayRuleHelper alertHelper : _alertHelpers) {
				getComments().addAll(alertHelper.getComments());
		}
	}
	
	private int checkForAlert(String line, int lineNum, List<String> alertKeyWords, boolean ignoreCase) {
		int index = 0;
		index = fetchAlertDisplayIndex(line,alertKeyWords,ignoreCase);
		boolean isAlertLogicExisting = false;
		
		if (index >=0) {
			isAlertLogicExisting = true;
		} else {
			isAlertLogicExisting = false;
		}

		if (isAlertLogicExisting && _functionName != null && _functionName.length()>0) {
			_alertDisplayFunction = _functionName;
			_alertDisplayLine = lineNum;
		} 
		
		return index;
	}
	
	private int fetchAlertDisplayIndex(String reviewLine, List<String> alertKeyWords, boolean ignoreCase) {
		int index = -1;
		if (reviewLine == null || reviewLine.length() ==0) {
			return -1;
		}
		String line = truncateBetweenQuotes(reviewLine);
		
		if (line == null || line.length() ==0) {
			return -1;
		}
		
			for (String functionName : alertKeyWords) {
				index = getKeywordIndex(line,functionName,_acceptedPreChars,_acceptedPostChars,ignoreCase);
				if (index>=0) {
					// Function is defined.
					_alertFunction = functionName;
					break;
				}
			}
		
		return index;
	}
}
