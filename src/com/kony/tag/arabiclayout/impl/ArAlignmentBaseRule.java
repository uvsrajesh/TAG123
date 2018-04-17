package com.kony.tag.arabiclayout.impl;

import java.util.TreeSet;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;

public abstract class ArAlignmentBaseRule implements ProjectConstants {
	
	private String _formQualifier = null;
	private TreeSet<String> _blackListedWidgetList = null;
	private String _currentWidgetName = BLANK;
	
	private ProjectConfig _codeReviewConfig = null;
	private String _buildChannel = null;
	
	private String _MARGIN = ".margin.";
	private String _PADDING = ".padding.";
	private String _SKIN = ".skin.";
	private String _ALL = "all";
	
	public String getFormQualifier() {
		return _formQualifier;
	}
	
	public ArAlignmentBaseRule() {
		
	}
	
	public ArAlignmentBaseRule(String formQualifier, TreeSet<String> blackListedWidgetList, ProjectConfig projectConfig, String buildChannel) {
		_formQualifier = formQualifier;
		_blackListedWidgetList = blackListedWidgetList;
		_codeReviewConfig = projectConfig;
		_buildChannel = buildChannel;
		
		if (_blackListedWidgetList == null) {
			_blackListedWidgetList = new TreeSet<String>();
		}
		
		if (null == _formQualifier) {
			_formQualifier = BLANK;
		}
		
		// give some impossible name !
		_currentWidgetName = "___ZZZZZ____";
	}
	
	protected boolean isBlackListedWidget() {
		return _blackListedWidgetList.contains(_currentWidgetName);
	}
	
	protected void updateCurrentWidgetName(String line) {
		
		int index1 = line.indexOf(KEYWORD_KONY_UI);
		int index2 = line.indexOf(CHAR_EQUALS);
		int index3 = line.indexOf(KEYWORD_VAR);
		
		if (index1 <0) {
			return;
		}
		
		if (index2 >=0 && index3>=0 && 
					index1>index2 && index2>(index3+3)) {
			_currentWidgetName = line.substring(index3+3,index2).trim();
		} else if (index2 >=0 && index3<0 && index1>index2) {
			
			// It should be a global variable
			_currentWidgetName = line.substring(0,index2).trim();
		}
	}
	
	protected String getCurrentWidgetName() {
		return _currentWidgetName;
	}
	
	protected String getPreconfiguredMargin() {
		String preConfiguredMargin = null;

		preConfiguredMargin = 
			_codeReviewConfig.getArabicLayoutProperty(
					_formQualifier+CHAR_DOT+_currentWidgetName+_MARGIN+_buildChannel);
		
		if (preConfiguredMargin == null || preConfiguredMargin.trim().length()==0 || preConfiguredMargin.equals(NOT_DEFINED)) {
			preConfiguredMargin = 
				_codeReviewConfig.getArabicLayoutProperty(
						_formQualifier+CHAR_DOT+_currentWidgetName+_MARGIN+_ALL);
		}
		
		if (preConfiguredMargin == null || preConfiguredMargin.trim().length()==0 || preConfiguredMargin.equals(NOT_DEFINED)) {
			preConfiguredMargin = null;
		} else {
			preConfiguredMargin = preConfiguredMargin.trim().substring(1,(preConfiguredMargin.length()-1));
		}
		
		return preConfiguredMargin;
	}
	
	protected String getPreconfiguredPadding() {
		String preConfiguredPadding = null;

		preConfiguredPadding = 
			_codeReviewConfig.getArabicLayoutProperty(
					_formQualifier+CHAR_DOT+_currentWidgetName+_PADDING+_buildChannel);

		if (preConfiguredPadding == null || preConfiguredPadding.trim().length()==0 || preConfiguredPadding.equals(NOT_DEFINED)) {
			preConfiguredPadding = 
				_codeReviewConfig.getArabicLayoutProperty(
						_formQualifier+CHAR_DOT+_currentWidgetName+_PADDING+_ALL);
		}
		
		if (preConfiguredPadding == null || preConfiguredPadding.trim().length()==0 || preConfiguredPadding.equals(NOT_DEFINED)) {
			preConfiguredPadding = 
				_codeReviewConfig.getArabicLayoutProperty(
						_formQualifier+_PADDING+_buildChannel);
		}
		
		if (preConfiguredPadding == null || preConfiguredPadding.trim().length()==0 || preConfiguredPadding.equals(NOT_DEFINED)) {
			preConfiguredPadding = 
				_codeReviewConfig.getArabicLayoutProperty(
						_formQualifier+_PADDING+_ALL);
		}		

		if (preConfiguredPadding == null || preConfiguredPadding.trim().length()==0 || preConfiguredPadding.equals(NOT_DEFINED)) {
			preConfiguredPadding = null;
		} else {
			preConfiguredPadding = preConfiguredPadding.trim().substring(1,(preConfiguredPadding.length()-1));
		}
		
		return preConfiguredPadding;
	}
}
