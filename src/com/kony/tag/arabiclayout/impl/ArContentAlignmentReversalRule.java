package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArContentAlignmentReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArContentAlignmentReversalRule() {
		
	}

	public ArContentAlignmentReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	
	private static final String CONTENT_ALIGN_TOP_LEFT = "constants.CONTENT_ALIGN_TOP_LEFT";
	private static final String CONTENT_ALIGN_TOP_RIGHT = "constants.CONTENT_ALIGN_TOP_RIGHT";
	private static final String CONTENT_ALIGN_MIDDLE_LEFT = "constants.CONTENT_ALIGN_MIDDLE_LEFT";
	private static final String CONTENT_ALIGN_MIDDLE_RIGHT = "constants.CONTENT_ALIGN_MIDDLE_RIGHT";
	private static final String CONTENT_ALIGN_BOTTOM_LEFT = "constants.CONTENT_ALIGN_BOTTOM_LEFT";
	private static final String CONTENT_ALIGN_BOTTOM_RIGHT = "constants.CONTENT_ALIGN_BOTTOM_RIGHT";
	
	public List<String> reverseContentAlignment(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int index1 = -1;
		int index2=-1;
		int index3 = -1;
		
		for (String line : formattedLines) {
	    	// Always call this at the beginning
	    	updateCurrentWidgetName(line);
	    	// Actual Code Starts
			
			index1 = line.indexOf(KEYWORD_CONTENT_ALIGNMENT);
			index3 = line.indexOf(KEYWORD_DATAGRID_COLUMN_CONFIG);
			index2 = line.indexOf(CHAR_COLON);
			
			if ((index1>=0 && index2>index1) || (index3>=0 && index3>index1)) {
				if(line.indexOf(CONTENT_ALIGN_TOP_LEFT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(CONTENT_ALIGN_TOP_LEFT, CONTENT_ALIGN_TOP_RIGHT);
				} else if(line.indexOf(CONTENT_ALIGN_TOP_RIGHT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(CONTENT_ALIGN_TOP_RIGHT, CONTENT_ALIGN_TOP_LEFT);
				} else if(line.indexOf(CONTENT_ALIGN_MIDDLE_LEFT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(CONTENT_ALIGN_MIDDLE_LEFT, CONTENT_ALIGN_MIDDLE_RIGHT);
				} else if(line.indexOf(CONTENT_ALIGN_MIDDLE_RIGHT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(CONTENT_ALIGN_MIDDLE_RIGHT, CONTENT_ALIGN_MIDDLE_LEFT);
				} else if(line.indexOf(CONTENT_ALIGN_BOTTOM_LEFT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(CONTENT_ALIGN_BOTTOM_LEFT, CONTENT_ALIGN_BOTTOM_RIGHT);
				} else if(line.indexOf(CONTENT_ALIGN_BOTTOM_RIGHT)>=0 && !isBlackListedWidget()) {
					line = line.replaceAll(CONTENT_ALIGN_BOTTOM_RIGHT, CONTENT_ALIGN_BOTTOM_LEFT);
				}
			}
			
			newLines.add(line);
		}
		return newLines;
	}
	}
	
