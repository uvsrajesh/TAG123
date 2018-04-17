package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;


public class ArAnimationFrameworkReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArAnimationFrameworkReversalRule() {
		
	}

	public ArAnimationFrameworkReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	
	private static final String FORM_TRANSITION_KEY = "formAnimation";
	private static final String POPUP_ANDROID_TRANSITION_KEY = "animation";
	private static final String DEFINED = "DEFINED";
	private static final String IPHONE_TRANSITION_DIRECTION = "transitionDirection";
	private static final String IPHONE_LEFT_TRANSITION_VALUE = "fromLeft";
	private static final String IPHONE_RIGHT_TRANSITION_VALUE = "fromRight";
	private static final String ANDROID_LEFT_OUT_TRANSITION_VALUE = "5";
	private static final String ANDROID_RIGHT_OUT_TRANSITION_VALUE = "4";
	private static final String ANDROID_LEFT_INTRANSITION_VALUE = "3";
	private static final String ANDROID_RIGHT_INTRANSITION_VALUE = "2";
	
	public List<String> reverseAnimationValues(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		
		int iphoneTransition = -1;
		int androidTransition = -1;
		int popupTransition = -1;
		int animationIndex = -1;
		
		for (String line : formattedLines) {
						
			if(animationIndex < 0){
				animationIndex = line.indexOf(DEFINED);
				newLines.add(line);
				continue;
			}
			androidTransition = line.indexOf(FORM_TRANSITION_KEY);
			iphoneTransition = line.indexOf(IPHONE_TRANSITION_DIRECTION);
			popupTransition = line.indexOf(POPUP_ANDROID_TRANSITION_KEY);
						
			if(iphoneTransition > 0){
				int iphoneLeftTransition = line.indexOf(IPHONE_LEFT_TRANSITION_VALUE);
				int iphoneRightTransition = line.indexOf(IPHONE_RIGHT_TRANSITION_VALUE);
				if(iphoneLeftTransition > 0 && iphoneLeftTransition > iphoneTransition){
					line = line.replaceAll(IPHONE_LEFT_TRANSITION_VALUE, IPHONE_RIGHT_TRANSITION_VALUE);
				} else if(iphoneRightTransition > 0 && iphoneRightTransition > iphoneTransition){
					line = line.replaceAll(IPHONE_RIGHT_TRANSITION_VALUE, IPHONE_LEFT_TRANSITION_VALUE);
				}
			} else if(androidTransition > 0 || popupTransition > 0){
				int androidLeftOutTransition = line.indexOf(ANDROID_LEFT_OUT_TRANSITION_VALUE);
				int androidLeftInTransition = line.indexOf(ANDROID_LEFT_INTRANSITION_VALUE);
				int androidRightOutTransition = line.indexOf(ANDROID_RIGHT_OUT_TRANSITION_VALUE);
				int androidRightInTransition = line.indexOf(ANDROID_RIGHT_INTRANSITION_VALUE);
				
				if(androidLeftOutTransition > 0 && androidLeftOutTransition > androidTransition){
					line = line.replaceAll(ANDROID_LEFT_OUT_TRANSITION_VALUE, ANDROID_RIGHT_OUT_TRANSITION_VALUE);
				} else if(androidRightOutTransition > 0 && androidRightOutTransition > androidTransition){
					line = line.replaceAll(ANDROID_RIGHT_OUT_TRANSITION_VALUE, ANDROID_LEFT_OUT_TRANSITION_VALUE);
				}
				
				if(androidLeftInTransition > 0 && androidLeftInTransition > androidTransition){
					line = line.replaceAll(ANDROID_LEFT_INTRANSITION_VALUE, ANDROID_RIGHT_INTRANSITION_VALUE);
				} else if(androidRightInTransition > 0 && androidRightInTransition > androidTransition){
					line = line.replaceAll(ANDROID_RIGHT_INTRANSITION_VALUE, ANDROID_LEFT_INTRANSITION_VALUE);
				}
			}
			
			newLines.add(line);
		}
		return newLines;
	}
}
	
