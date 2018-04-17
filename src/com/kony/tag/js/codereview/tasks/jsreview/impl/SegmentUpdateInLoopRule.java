package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.List;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;

public class SegmentUpdateInLoopRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.004.segment_update_in_loop_rule";
	public static final String RULE_ID = "JS-004";
	
	public static final String ERR_FORBIDDEN_LOOP_METHOD = "Segment/Datagrid data should not be updated inside a loop. This causes animation effect in UI display and is highly discouraged";
	private final String[] LOOP_FORBIDDEN_METHODS = new String[]{
			".addAll", ".addDataAt", ".addSectionAt",".removeAll",".removeAt",".removeSectionAt",".setCellDataAt",
			".setData",".setDataAt",".setSectionAt"};
	
	private static final String ERR_SHORT_DESC = "Segment/Datagrid being updated in a loop";
	
	private static final String FOR_LOOP = "for";
	private static final String WHILE_LOOP = "while";
	private static final String DO_WHILE_LOOP = "do";
	private String _loopForbiddenLine = null;
	private int _openBracesCount = 0;
	private int _loopForbiddenLineNum = 0;
	private boolean _isLoopOpen = false;
	private StringBuffer _concatenatedReviewLine = null;

	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public SegmentUpdateInLoopRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME,1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
	}
	
	public void init() {
		
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line,
			int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {
		
		/*
		final String CHECK_STRING = "var rewardList= frmGMRREAddToQueue.segAddToQueue.data";
		
		if (line.indexOf(CHECK_STRING)>=0) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		
		
		String tmpLine = null;
		int index = -1;
		char[] mychars = null;
		
		tmpLine = line;
		
		if (!_isLoopOpen) {
			tmpLine = truncateBetweenQuotes(line);
			index = getLoopStartIndex(tmpLine,FOR_LOOP);
			if (index<0) {
				index = getLoopStartIndex(tmpLine,WHILE_LOOP);
			}
			if (index<0) {
				index = getLoopStartIndex(tmpLine,DO_WHILE_LOOP);
			}
			
			if (index>=0) {
				tmpLine = tmpLine.substring(index);
				_concatenatedReviewLine = new StringBuffer();
				_openBracesCount = 0;
				_isLoopOpen = true;
				_loopForbiddenLine = line;
				_loopForbiddenLineNum = lineNum;
			}			
		}
		

		if (_isLoopOpen) {
			
			tmpLine = truncateBetweenQuotes(tmpLine);
			
			if (tmpLine != null && tmpLine.length()>0) {
				mychars = tmpLine.toCharArray();
			
				for (char c: mychars) {
					if(c == CHAR_CURLY_OPEN) {
						_openBracesCount++;
					}
					
					if (c== CHAR_CURLY_CLOSE) {
						_openBracesCount--;
					}
					
					if (!Character.isWhitespace(c)) {
						_concatenatedReviewLine.append(c);
					}
					
					if (_openBracesCount == 0 && c == CHAR_CURLY_CLOSE) {
						// Loop Ends Here
						_isLoopOpen = false;
						checkForSegmentPopulation(_concatenatedReviewLine.toString(), fileName);
						
						_concatenatedReviewLine = null;
						_openBracesCount = 0;
						_loopForbiddenLine = null;
						_loopForbiddenLineNum = 0;
						break;
					}
				}
			}
		}
				
		return getComments();
}
	
	private void checkForSegmentPopulation(String reviewLine, String fileName) throws TagToolException{
		
		String tmpLine = getCompressedString(reviewLine);
		
		for (String forbiddenMethod : LOOP_FORBIDDEN_METHODS) {
			if (tmpLine.indexOf(forbiddenMethod)>=0) {
				addError(ERR_FORBIDDEN_LOOP_METHOD, ERR_SHORT_DESC, forbiddenMethod + " : " +_loopForbiddenLine.trim(), fileName, _loopForbiddenLineNum, SEV_HIGH, RULE_ID);
			}
		}
		
	}
	
	private int getLoopStartIndex(String reviewLine, String keyWord) {
		int index = 0;
		char prevChar;
		char postChar;
		boolean prevCharOk = false;
		boolean postCharOk = false;
		String line = reviewLine;
		int keyWordLength = 0;
		char validPostChar;
		
		if (reviewLine == null || reviewLine.length()==0) {
			return -1;
		}

		if (keyWord == null || keyWord.length()==0) {
			return -1;
		}
		
		if (keyWord.equals(DO_WHILE_LOOP)) {
			validPostChar = CHAR_CURLY_OPEN;
		} else {
			validPostChar = CHAR_PARANTHESIS_OPEN;
		}
		
		keyWordLength = keyWord.length();
		index = reviewLine.indexOf(keyWord);
		
		while (index >=0) {
			
			prevCharOk = false;
			postCharOk = false;
			
			if (index ==0) {
				prevCharOk = true;
			}
			if (index >0 && !prevCharOk) {
				prevChar = reviewLine.charAt(index-1);
				if (Character.isWhitespace(prevChar)) {
					prevCharOk = true;	
				}
			}
			
			if (reviewLine.length() > (index+keyWordLength)) {
				postChar = reviewLine.charAt(index+keyWordLength);
				if (postChar == validPostChar || Character.isWhitespace(postChar)) {
					postCharOk = true;	
				}
			} else {
				postCharOk = true;
			}
			
			if (prevCharOk && postCharOk) {
				break;
			} 

			if (line.length()>(index+keyWordLength)) {
				index = line.indexOf(keyWord,index+keyWordLength);
			} else {
				index = -1;
			}

		}
		
		return index;
	}
}
