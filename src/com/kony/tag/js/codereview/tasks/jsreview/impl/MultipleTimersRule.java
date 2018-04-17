package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class MultipleTimersRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.008.multiple_timers_rule";
	public static final String RULE_ID = "JS-008";
	
	public static final String ERR_MULTIPLE_TIMERS = "Use a single timer per App. Configure it accommodate all timer requirements. Use various flags in the timer callback method to achieve the same functionality";
	
	
	private final String NULL = "null"; 
	private static final String NOTES = "Timers Found : ";
	private static final String TIMER_API = "timer.schedule";
	
	private static final String ERR_SHORT_DESC = "Multiple Timers Found";
	
	//private static List<String> myFunctions = new ArrayList<String>();
	//private static List<String> allFunctions = new ArrayList<String>();
	//private static boolean taskDone = false;
	
	private List<String> _timers = null;
	private boolean _timerIdContinuationFlag = false;

	@Override
	protected boolean isLineSplitNeeded(){return false;}
	
	public MultipleTimersRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME,2);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
	}
	
	public void init() {
		_timerIdContinuationFlag = false;
		if (null == _timers) {
			_timers = new ArrayList<String>();
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
		int index = 0;
		String timerId = null;
		
		index = line.indexOf(TIMER_API);
		
		// Following logic to be used if timer.schedule API is spread over two lines with timer id in the second line
		if (_timerIdContinuationFlag) {
			timerId = fetchTimerIdContinued(line);
			if (timerId != null) {
				_timerIdContinuationFlag = false;
			}
		}
		
		if (index>=0) {
			timerId = fetchTimerId(line);
			
			if(timerId != null && timerId.equals(NULL)) {
				_timerIdContinuationFlag = true;
			} else {
				_timerIdContinuationFlag = false;
			}
		} 

		if(timerId != null && !timerId.equals(NULL) && !_timers.contains(timerId)) {
			_timers.add(timerId);
		}
		
		if (lineNum == lastLineNum) {
			init();
		}
		
		return getComments();
}
	
	private String fetchTimerId(String line) {
		StringBuffer tmpStrBuffer = null;
		String tmpLine = null;
		int index = line.indexOf(TIMER_API);
		boolean idBeginFlag = false;
		boolean idEndFlag = false;
		
		if (index >=0 && line.length()>(index + (TIMER_API.length()))) {
			tmpLine = line.substring(index+TIMER_API.length()).trim();
		}
		
		if (tmpLine != null && tmpLine.trim().length()>0) {
			tmpStrBuffer = new StringBuffer();
			for (char c : tmpLine.toCharArray()) {
				if(c == CHAR_PARANTHESIS_OPEN) {
					idBeginFlag = true;
					continue;
				} 
				if (!idBeginFlag) {
					continue;
				} else {
					if (c==CHAR_COMMA || c==CHAR_PARANTHESIS_CLOSE || c==CHAR_CURLY_CLOSE || c==CHAR_SEMI_COLON) {
						idEndFlag = true;
						break;
					}
					tmpStrBuffer.append(c);
				}
			}
		}
		
		if (tmpStrBuffer != null && tmpStrBuffer.toString().trim().length()>0) {
			return stripQuotes(tmpStrBuffer.toString().trim());
		} else {
			if (!idEndFlag) {
				// Looks like Id is in the next line
				return NULL;
			} else {
				return null;
			}
			
		}
	}
	
	private String fetchTimerIdContinued(String line) {
		StringBuffer tmpStrBuffer = null;
		String tmpLine = null;
		
		tmpLine = line;
		
		if (tmpLine != null && tmpLine.trim().length()>0) {
			tmpStrBuffer = new StringBuffer();
			for (char c : tmpLine.toCharArray()) {
				// timer id is just starting . so reset the Id
				if(c == CHAR_PARANTHESIS_OPEN) {
					tmpStrBuffer = new StringBuffer();
					continue;
				} 
				if (c==CHAR_COMMA || c==CHAR_PARANTHESIS_CLOSE || c==CHAR_CURLY_CLOSE || c==CHAR_SEMI_COLON) {
					break;
				} else {
					tmpStrBuffer.append(c);
				}
			}
		}
		
		if (tmpStrBuffer != null && tmpStrBuffer.toString().trim().length()>0) {
			return stripQuotes(tmpStrBuffer.toString().trim());
		} else {
			return null;
		}
	}	
	
	public void postReview() throws TagToolException {
		StringBuffer tmpStrBuffer = null;
		
		if (_timers != null && _timers.size()>1) {
			tmpStrBuffer = new StringBuffer();
			for (String id : _timers) {
				tmpStrBuffer.append(id);
				tmpStrBuffer.append(SEMI_COLON);
			}
			addWarning(ERR_MULTIPLE_TIMERS , ERR_SHORT_DESC, NOTES + tmpStrBuffer.toString(),
					BLANK, 0, SEV_MED, RULE_ID);
			
		}
		
	}
}
