package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArWidgetMasterDataReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArWidgetMasterDataReversalRule() {
		
	}

	public ArWidgetMasterDataReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList,projectConfig,buildChannel);
	}
	

	private static final String CHECK_BOXGRP_HORIZONTAL = "constants.CHECKBOX_ITEM_ORIENTATION_HORIZONTAL";
	private static final String RADIO_BOXGRP_HORIZONTAL = "constants.RADIOGROUP_ITEM_ORIENTATION_HORIZONTAL";
	private static final String HORIZONTAL_ORIENTATION = "_ITEM_ORIENTATION_HORIZONTAL";
	private static final String TOGGLE_VIEW_TYPE = "constants.RADIOGROUP_VIEW_TYPE_TOGGLEVIEW";
	private static final String TOGGLE_VIEW_STYLE = "_TOGGLE_VIEW_";
	private static final String END_STYLE_COMMA = "],";
	private static final String END_STYLE_SQRBRACE = "]";
	
	public List<String> reverseMasterData(List<String> formattedLines, boolean is_iOS_Channel) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		int index2 = 0;
		int index3 = 0;

		String currentWidgetType = BLANK;
		String reverseMasterDataStr = null;
		String masterDataLine=null;
		int masterDataLineNum=-1;
		String itemOrientation = BLANK;
		String currentViewType = BLANK;
		String currentViewStyle = BLANK;
		
		for (String line : formattedLines) {

			index3 = line.indexOf(KEYWORD_KONY_UI);
			
			if (index3 >=0) {
				// First act on the previous master data element
				if (currentWidgetType.length()>0 &&
						masterDataLine != null && masterDataLine.length()>0 &&
						masterDataLineNum>=0 && masterDataLineNum<newLines.size()) {

					if(is_iOS_Channel) {
						
						if (currentWidgetType.equals(KEYWORD_KONY_UI_RADIOBUTTON_GRP) && currentViewType.indexOf(TOGGLE_VIEW_TYPE)>=0  && !isBlackListedWidget()) {
							// For iPhone/ iPad - master data for Radio Button Group to be reversed only for toggle view
							// Reversal not needed for checkbox group 
							reverseMasterDataStr = getReverseMasterDataStr(masterDataLine);
							newLines.remove(masterDataLineNum);
							newLines.add(masterDataLineNum, reverseMasterDataStr);
						} else if ((currentWidgetType.equals(KEYWORD_KONY_UI_COMBOBOX) || currentWidgetType.equals(KEYWORD_KONY_UI_LISTBOX) 
									&& currentViewStyle.indexOf(TOGGLE_VIEW_STYLE)>=0  && !isBlackListedWidget())) {
							// For iPhone/ iPad - master data for ComboBox and ListBox  to be reversed only for toggle view
							// Reversal not needed for any other View Style like Table View 
							reverseMasterDataStr = getReverseMasterDataStr(masterDataLine);
							newLines.remove(masterDataLineNum);
							newLines.add(masterDataLineNum, reverseMasterDataStr);
						}
					} else if ((currentWidgetType.equals(KEYWORD_KONY_UI_CHECKBOX_GRP) || currentWidgetType.equals(KEYWORD_KONY_UI_RADIOBUTTON_GRP))
							&& itemOrientation != null && itemOrientation.length()>0 && itemOrientation.indexOf(HORIZONTAL_ORIENTATION)>=0 && !isBlackListedWidget()){
						
						// For other channels - reverse master data, just if the Item Orientation is Horizontal ! - JUST for Radio Box groups and Check box groups
						reverseMasterDataStr = getReverseMasterDataStr(masterDataLine);
						newLines.remove(masterDataLineNum);
						newLines.add(masterDataLineNum, reverseMasterDataStr);
					}
					
					if (currentWidgetType.equals(KEYWORD_KONY_UI_PICKERVIEW) && !isBlackListedWidget()) {
						// Reverse Master data for Picker View in all circumstances
						reverseMasterDataStr = getReverseMasterDataStr(masterDataLine,KEYWORD_KONY_UI_PICKERVIEW);
						newLines.remove(masterDataLineNum);
						newLines.add(masterDataLineNum, reverseMasterDataStr);
					}
				}
				
		    	// Always call this at the beginning
		    	updateCurrentWidgetName(line);
		    	// Actual Code Starts

				currentWidgetType = BLANK;
				currentViewType = BLANK;
				currentViewStyle = BLANK;
				masterDataLine = BLANK;
				itemOrientation = BLANK;
				masterDataLineNum = -1;
				
				if(line.indexOf(KEYWORD_KONY_UI_CHECKBOX_GRP)>=0) {
					// Ignore any transformation of CheckBox Master Data for iOS channels
					currentWidgetType = KEYWORD_KONY_UI_CHECKBOX_GRP;
				} else if(line.indexOf(KEYWORD_KONY_UI_RADIOBUTTON_GRP)>=0) {
					currentWidgetType = KEYWORD_KONY_UI_RADIOBUTTON_GRP;
				} else if(line.indexOf(KEYWORD_KONY_UI_COMBOBOX)>=0) {
					currentWidgetType = KEYWORD_KONY_UI_COMBOBOX;
				} else if(line.indexOf(KEYWORD_KONY_UI_LISTBOX)>=0) {
					currentWidgetType = KEYWORD_KONY_UI_LISTBOX;
				} else if(line.indexOf(KEYWORD_KONY_UI_PICKERVIEW)>=0) {
					currentWidgetType = KEYWORD_KONY_UI_PICKERVIEW;
				}
			}
				
			index1 = line.indexOf(KEYWORD_MASTER_DATA);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1 && currentWidgetType.length()>0) {
				masterDataLine = line;
				masterDataLineNum = newLines.size();
			}

			index1 = line.indexOf(KEYWORD_ITEM_ORIENTATION);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1 && currentWidgetType.length()>0) {
				itemOrientation = line;
			}

			index1 = line.indexOf(KEYWORD_VIEW_TYPE);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1 && currentWidgetType.length()>0) {
				currentViewType = line;
			}

			index1 = line.indexOf(KEYWORD_VIEW_STYLE);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1 && currentWidgetType.length()>0) {
				currentViewStyle = line;
			}

			newLines.add(line);
		}
		
		// Act on any left over master data  widget
		/*
		if (currentWidgetType.length()>0 && 
				masterDataLine != null && masterDataLine.length()>0 &&
				itemOrientation != null && itemOrientation.length()>0 &&
				itemOrientation.indexOf(HORIZONTAL_ORIENTATION)>=0 && 
				masterDataLineNum>=0 && masterDataLineNum<newLines.size()
				 && !isBlackListedWidget()) {
			
			reverseMasterDataStr = getReverseMasterDataStr(masterDataLine);
			newLines.remove(masterDataLineNum);
			newLines.add(masterDataLineNum, reverseMasterDataStr);
		}
		*/
		
		return newLines;
		
	}

	private String getReverseMasterDataStr(String masterDataStr) {
		return getReverseMasterDataStr(masterDataStr,BLANK);
	}
private String getReverseMasterDataStr(String masterDataStr, String widgetType) {
	if (masterDataStr == null || masterDataStr.trim().length()==0) {
		return masterDataStr;
	}

	int index1 = -1;
	int index2 = -1;
	String KEYWORDS = BLANK;
	boolean isEndsWithComma = false;

	index1 = masterDataStr.indexOf(CHAR_COLON);
	if (index1>=0) {
		index2 = masterDataStr.indexOf(CHAR_SQUARE_OPEN,index1);
	}
	
	if (index2<0) {
		return masterDataStr;
	}
		
	if (masterDataStr.endsWith(END_STYLE_COMMA)) {
		isEndsWithComma = true;
	}
	
	String workingString = BLANK;
	
	if (widgetType.equals(KEYWORD_KONY_UI_PICKERVIEW)) {
		if (isEndsWithComma) {
			workingString = masterDataStr.substring(index2+1,(masterDataStr.length()-2));
		} else {
			workingString = masterDataStr.substring(index2+1,(masterDataStr.length()-1));
		}
	} else {
		if (isEndsWithComma) {
			workingString = masterDataStr.substring(index2+1,(masterDataStr.length()-2));
		} else {
			workingString = masterDataStr.substring(index2+1,(masterDataStr.length()-1));
		}
	}
	
	
	KEYWORDS = masterDataStr.substring(0,index2+1);
	
	StringBuffer strBuff = new StringBuffer();
	int openSqrBraceCount=0;
	List<String> values = new ArrayList<String>();
	char[] chars = workingString.toCharArray();
	for(char c : chars) {
		
		if (c==CHAR_COMMA && openSqrBraceCount==0) {
			values.add(strBuff.toString());
			strBuff = new StringBuffer();
		} else if (c==CHAR_SQUARE_OPEN){
			openSqrBraceCount++;
			strBuff.append(c);
		} else if (c==CHAR_SQUARE_CLOSE){
			openSqrBraceCount--;
			strBuff.append(c);
		}else {
			strBuff.append(c);
		}
	}
	values.add(strBuff.toString());
	strBuff = new StringBuffer();

	strBuff.append(KEYWORDS);
	for (int i=(values.size()-1); i>=0; i--) {
		strBuff.append(values.get(i));
		if (i!=0) {
			strBuff.append(CHAR_COMMA);
		}
	}
	if (widgetType.equals(KEYWORD_KONY_UI_PICKERVIEW)) {
		if (isEndsWithComma) {
			strBuff.append(END_STYLE_COMMA);
		} else {
			strBuff.append(END_STYLE_SQRBRACE);
		}
	}else {
		if (isEndsWithComma) {
			strBuff.append(END_STYLE_COMMA);
		} else {
			strBuff.append(END_STYLE_SQRBRACE);
		}
		
	}
	
	
	return strBuff.toString();
}


}
