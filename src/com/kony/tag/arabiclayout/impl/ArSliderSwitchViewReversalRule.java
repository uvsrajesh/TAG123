package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArSliderSwitchViewReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArSliderSwitchViewReversalRule() {
		
	}

	public ArSliderSwitchViewReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}


	public List<String> reverseSliderSwitchViews(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		int index2 = 0;
		int index3 = 0;
		boolean isBreak = false;

		String currentWidgetType = BLANK;
		
		for (String line : formattedLines) {
	    	// Always call this at the beginning
	    	updateCurrentWidgetName(line);
	    	// Actual Code Starts

			index3 = line.indexOf(KEYWORD_KONY_UI);
			isBreak = false;
			
			if (index3 >=0) {
				
				currentWidgetType = BLANK;
				
				if(line.indexOf(KEYWORD_KONY_UI_SLIDER)>=0) {
					// Ignore any transformation of CheckBox Master Data for iOS channels
					currentWidgetType = KEYWORD_KONY_UI_SLIDER;
				}else if (line.indexOf(KEYWORD_KONY_UI_SWITCH)>=0) {
					currentWidgetType = KEYWORD_KONY_UI_SWITCH;
				}
			}
			
			if (currentWidgetType.equals(KEYWORD_KONY_UI_SLIDER)) {
				index1 = line.indexOf(KEYWORD_SLIDER_MIN_LABEL);
				index2 = line.indexOf(CHAR_COLON);

				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_MIN_LABEL, KEYWORD_SLIDER_MAX_LABEL);
					isBreak = true;
				}
				
				index1 = line.indexOf(KEYWORD_SLIDER_MAX_LABEL);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_MAX_LABEL, KEYWORD_SLIDER_MIN_LABEL);
					isBreak = true;
				}

				index1 = line.indexOf(KEYWORD_SLIDER_MIN_LABEL_SKIN);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_MIN_LABEL_SKIN, KEYWORD_SLIDER_MAX_LABEL_SKIN);
					isBreak = true;
				}
				
				index1 = line.indexOf(KEYWORD_SLIDER_MAX_LABEL_SKIN);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_MAX_LABEL_SKIN, KEYWORD_SLIDER_MIN_LABEL_SKIN);
					isBreak = true;
				}

				index1 = line.indexOf(KEYWORD_SLIDER_LEFT_SKIN);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_LEFT_SKIN, KEYWORD_SLIDER_RIGHT_SKIN);
					isBreak = true;
				}
				
				index1 = line.indexOf(KEYWORD_SLIDER_RIGHT_SKIN);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_RIGHT_SKIN, KEYWORD_SLIDER_LEFT_SKIN);
					isBreak = true;
				}
				index1 = line.indexOf(KEYWORD_SLIDER_MIN_IMAGE);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_MIN_IMAGE, KEYWORD_SLIDER_MAX_IMAGE);
					isBreak = true;
				}
				index1 = line.indexOf(KEYWORD_SLIDER_MAX_IMAGE);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SLIDER_MAX_IMAGE, KEYWORD_SLIDER_MIN_IMAGE);
					isBreak = true;
				}
				
			} else if (currentWidgetType.equals(KEYWORD_KONY_UI_SWITCH)) {
				index1 = line.indexOf(KEYWORD_SWITCH_LEFT_TEXT);
				index2 = line.indexOf(CHAR_COLON);

				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SWITCH_LEFT_TEXT, KEYWORD_SWITCH_RIGHT_TEXT);
					isBreak = true;
				}
				
				index1 = line.indexOf(KEYWORD_SWITCH_RIGHT_TEXT);
				if (!isBreak && index1>=0 && index2>index1 && !isBlackListedWidget()) {
					line = line.replaceFirst(KEYWORD_SWITCH_RIGHT_TEXT, KEYWORD_SWITCH_LEFT_TEXT);
					isBreak = true;
				}
			}

			newLines.add(line);
		}
		
		return newLines;
		
	}
}
