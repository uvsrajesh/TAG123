package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class AlertDisplayRuleHelper extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.003.alert_display_rule";
	public static final String RULE_ID = "JS-003";
	
	public static final String ERR_IMPROPER_ALERT_DISPLAY = "Alert Popup display should be the last statement of a function";
	private final String NULL = "null"; 
	
	private static final String ERR_SHORT_DESC = "Statements below Alert display";
	
	private static final String ALERT_API = "ui.alert";
	
	private String _functionName = null;
	private List<String> _alertFunctions = null;
	private List<String> _konyAlertAPIFunctions = null;
	private String _alertDisplayFunction = null;
	private String _alertFunction = null;
	private int _openBracesCount = 0;
	private int _alertDisplayLine = 0;
	private boolean _isAlertLogicExisting = false;
	private List<String> _reviewLines = null;
	private int _openParanthesisCount = 0;

	@Override
	protected boolean isLineSplitNeeded(){return true;}
	
	public List<String> getAlertFunctions() {
		return _alertFunctions;
	}

	public void setAlertFunctions(List<String> alertFunctions) {
		this._alertFunctions = alertFunctions;
	}
	
	public void init(List<String> alertFunctions, String alertDisplayFunction, String alertFunction, int alertDisplayLineNum) {
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,2);


		_isAlertLogicExisting = true;
		setAlertFunctions(alertFunctions);
		
		_functionName = alertFunction;
		_openParanthesisCount = 0;
		_alertDisplayFunction = alertDisplayFunction;
		_alertFunction = alertFunction;
		
		_openBracesCount = 1;
		_alertDisplayLine = alertDisplayLineNum;

		if (null == _konyAlertAPIFunctions) {
			_konyAlertAPIFunctions = new ArrayList<String>();
			_konyAlertAPIFunctions.add(ALERT_API);
		}	
		
		if (null == _reviewLines) {
			_reviewLines = new ArrayList<String>();
		} else {
			_reviewLines.clear();
		}
	}
	
	public AlertDisplayRuleHelper(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
	}
	
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {
		// do not use this method
		return null;
	}
	// fName : function name found in the current "line" being reviewed
	public List<ReviewComment> checkAlertDisplayErrors(String fileName, int lineNum, int lastLineNum, String line, String currFunctionName, int passNumber, String fName) throws TagToolException {
		
		/*
		final String CHECK_STRING = "var kumarVaraible = 1";
		
		if (line.indexOf(CHECK_STRING)>=0) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		_functionName = currFunctionName;
		//System.out.println("Reviewing File : " + fileName + " Line Number " + lineNum + " alert display ind " + _isAlertLogicExisting);

		String tmpLine = null;
		int statusInd = 0;
		String referenceData = null;

		tmpLine = line;
		// In Second Pass - check if any function has alert display logic in any line other than the last line
			
		if (_isAlertLogicExisting) {
				
			if ((fName == null || fName.equals(NULL))) {
				statusInd = updateLineCount(tmpLine);
			}
			// Check if Alert has ended
				if ((_openBracesCount == 0) ||
						(!_alertDisplayFunction.equals(_functionName)) ||
						lineNum == lastLineNum || statusInd == -99) {
						
						
					if (_reviewLines != null &&  _reviewLines.size() > 1) {
							
						referenceData = "Alert Function: " + _alertFunction + "  ;; Some of the statements found after Alert display : ";
						//System.out.println(fileName + " : " +  _alertDisplayLine + " : ");
						for (int i=1; i<_reviewLines.size(); i++) {
								
							if (i<=5) {
								referenceData += " ; "  + _reviewLines.get(i);
							}
								//System.out.println("LINES AFTER ALERT: " + _reviewLines.get(i));
							}
						addError(ERR_IMPROPER_ALERT_DISPLAY,ERR_SHORT_DESC, referenceData,fileName, _alertDisplayLine, SEV_MED, RULE_ID);
					}
					_openBracesCount = 0;
					_alertDisplayLine = 0;
					_alertDisplayFunction = null;
					_alertFunction = null;
					_isAlertLogicExisting = false;
					_reviewLines = null;
					_openParanthesisCount=0;
				}
			}
		
		return getComments();
}
	
	private int updateLineCount(String reviewLine) {
		
		int statusInd = 100;
		
		if (reviewLine == null || reviewLine.length() ==0) {
			return statusInd;
		}

		String line = truncateBetweenQuotes(reviewLine);
		char[] mychars = null;
		StringBuffer tmpStrBuff = new StringBuffer();
		
		if (line == null || line.length() ==0) {
			return statusInd;
		}
		
		if (null == _reviewLines) {
			_reviewLines = new ArrayList<String>();
		}
		
		mychars = line.toCharArray();
		
		for (char c : mychars) {
			if (c == CHAR_CURLY_OPEN) {
				_openBracesCount++;
			}
			
			if (c == CHAR_CURLY_CLOSE && _openBracesCount>1) {
				_openBracesCount--;
			}
			
			if (c==CHAR_PARANTHESIS_OPEN) {
				_openParanthesisCount++;
			}
			
			if (c==CHAR_PARANTHESIS_CLOSE) {
				_openParanthesisCount--;
			}
			
			if (_openBracesCount == 1 && _openParanthesisCount==0 && !Character.isWhitespace(c) && c!=CHAR_CURLY_OPEN && c!= CHAR_CURLY_CLOSE && c!=CHAR_COMMA
					&& c!=CHAR_PARANTHESIS_CLOSE && c!= CHAR_PARANTHESIS_OPEN) {
				tmpStrBuff.append(c);
			}
		}
		
		if (tmpStrBuff.length() >0) {
			String[] lines = tmpStrBuff.toString().split(";");
			
			if (lines != null && lines.length >0) {
			
				for (String functionLine: lines) {
					if (functionLine != null && functionLine.trim().length()>0) {
						
						if (functionLine.startsWith("return")) {
							// No more counting. Flow ends here
							return -99;
						} else if (functionLine.toLowerCase().startsWith("else") || functionLine.toLowerCase().startsWith("else if") 
								|| functionLine.toLowerCase().startsWith("catch") || functionLine.toLowerCase().startsWith("kony.print")) {
							// Need not count this line
							continue;
						} else {
							_reviewLines.add(functionLine);
						}
					}
				}
			}
		}
		
		return statusInd;
	}
}
