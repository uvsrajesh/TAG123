package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public class ArDataGridReversalRule extends ArAlignmentBaseRule implements ProjectConstants{
	private ArDataGridReversalRule() {
		
	}

	public ArDataGridReversalRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		super(formQualifier,blackListedWidgetList, projectConfig,buildChannel);
	}
	
	private static final String _COMMA = ",";

	public List<String> reverseDataGridData(List<String> formattedLines) {
		List<String> newLines = new ArrayList<String>();
		int index1 = 0;
		int index2 = 0;
		int index3 = 0;

		String currentWidgetType = BLANK;

		String reverseColumnConfigStr = null;
		String columnConfigLine=null;
		int columnConfigLineNum = -1;

		for (String line : formattedLines) {

			index3 = line.indexOf(KEYWORD_KONY_UI);
			
			if (index3 >=0) {
				// First act on the previous master data element
				if (currentWidgetType.equals(KEYWORD_KONY_UI_DATAGRID) &&
						columnConfigLine != null && columnConfigLine.length()>0 &&
						columnConfigLineNum>=0 && columnConfigLineNum<newLines.size()
						 && !isBlackListedWidget()) {

						reverseColumnConfigStr = getReverseObjectArrayStr(columnConfigLine);
						newLines.remove(columnConfigLineNum);
						newLines.add(columnConfigLineNum, reverseColumnConfigStr);
				}
				
		    	// Always call this at the beginning
		    	updateCurrentWidgetName(line);
		    	// Actual Code Starts
				
				currentWidgetType = BLANK;

				reverseColumnConfigStr = null;
				columnConfigLine=null;
				columnConfigLineNum = -1;
				
				if(line.indexOf(KEYWORD_KONY_UI_DATAGRID)>=0) {
					currentWidgetType = KEYWORD_KONY_UI_DATAGRID;
				} 
			}
				
			index1 = line.indexOf(KEYWORD_DATAGRID_COLUMN_CONFIG);
			index2 = line.indexOf(CHAR_COLON);
			
			if (index1>=0 && index2>index1 && currentWidgetType.length()>0) {
				columnConfigLine = line;
				columnConfigLineNum = newLines.size();
			}

			newLines.add(line);
		}
		
		// Act on any left over data fields of Data Grid
		if (currentWidgetType.equals(KEYWORD_KONY_UI_DATAGRID) &&
				columnConfigLine != null && columnConfigLine.length()>0 &&
				columnConfigLineNum>=0 && columnConfigLineNum<newLines.size() && !isBlackListedWidget()) {

				reverseColumnConfigStr = getReverseObjectArrayStr(columnConfigLine);
				newLines.remove(columnConfigLineNum);
				newLines.add(columnConfigLineNum, reverseColumnConfigStr);
		}
		
		
		return newLines;
		
	}

private String getReverseObjectArrayStr(String masterDataStr) {
	if (masterDataStr == null || masterDataStr.trim().length()==0) {
		return masterDataStr;
	}

	int index1 = -1;
	int index2 = -1;
	String KEYWORDS_START = BLANK;
	String KEYWORDS_END = BLANK;

	index1 = masterDataStr.indexOf(CHAR_COLON);
	if (index1>=0) {
		index2 = masterDataStr.indexOf(CHAR_SQUARE_OPEN,index1);
	}
	
	if (index2<0) {
		return masterDataStr;
	}
	
	String workingString = BLANK;

	if (masterDataStr.trim().endsWith(_COMMA)) {
		workingString = masterDataStr.substring(index2+1,(masterDataStr.length()-2));
	}else {
		workingString = masterDataStr.substring(index2+1,(masterDataStr.length()-1));
	}
	
	KEYWORDS_START = masterDataStr.substring(0,index2+1);
	
	StringBuffer strBuff = new StringBuffer();
	int openCurlyBraceCount=0;
	List<String> values = new ArrayList<String>();
	char[] chars = workingString.toCharArray();
	for(char c : chars) {
		
		if (c==CHAR_COMMA && openCurlyBraceCount==0) {
			values.add(strBuff.toString());
			strBuff = new StringBuffer();
		} else if (c==CHAR_CURLY_OPEN){
			openCurlyBraceCount++;
			strBuff.append(c);
		} else if (c==CHAR_CURLY_CLOSE){
			openCurlyBraceCount--;
			strBuff.append(c);
		}else {
			strBuff.append(c);
		}
	}
	values.add(strBuff.toString());
	strBuff = new StringBuffer();

	strBuff.append(KEYWORDS_START);
	for (int i=(values.size()-1); i>=0; i--) {
		strBuff.append(values.get(i));
		if (i!=0) {
			strBuff.append(CHAR_COMMA);
		}
	}

	if (masterDataStr.trim().endsWith(_COMMA)) {
		strBuff.append("],");
	} else {
		strBuff.append("]");
	}
	
	
	
	return strBuff.toString();
}


}
