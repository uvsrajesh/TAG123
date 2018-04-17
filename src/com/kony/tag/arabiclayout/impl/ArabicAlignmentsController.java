package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import com.kony.tag.arabiclayout.util.ArabicLayoutPropertiesUtil;
import com.kony.tag.arabiclayout.util.ArabicLayoutUtil;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.tasks.JsReviewMasterTask;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ArabicAlignmentsController extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.ar.001.arabic_layout_reversal_rule";
	public static final String RULE_ID = "AR-001";
	public static final String widgetTree = "AR-001";
	
	private String _functionName = null;
	private List<String> _widgetRelationLines = new ArrayList<String>();
	private Map<String,Integer> _widgetContainerWeightMap = new HashMap<String,Integer>();
	private Map<String,String> _widgetLayoutTypeMap = new HashMap<String,String>();
	private List<WidgetVo> _widgetList = new ArrayList<WidgetVo>();
	
	private List<String> _lines = new ArrayList<String>();
	private boolean _isTemplateFile = false;
	private String _formName = null;
	private String _templateName = null;
	
	private static final String ERR_SHORT_DESC = "Arabic Layout Reversal";

	@Override
	protected boolean isLineSplitNeeded(){return false;}
	
	public ArabicAlignmentsController(ProjectConfig projectConfig) {
		super(projectConfig);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.initArabicTask(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,2);
		
		init();
	}
	
	public void init() {
		_functionName = null;
		_lines.clear();
	}
	
	//code for ignoring comments - please write
	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum, String line, int passNumber, String currFunctionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {
		
		if (passNumber == 1) {
			_lines.add(line);
			if (lineNum == lastLineNum) {
				processJSFile(fileName, _lines);
				
			}
		}
		return new ArrayList();
	}
	
	private void processJSFile(String fileName, List<String> lines) {
		JSReviewUtil.printDetailedLog("processJSFile START : " + fileName);
		
		try {
			_isTemplateFile = false;
			boolean is_iOS_Channel = ArLayoutManager.is_iOS_Channel();
			TreeSet<String> blackListedWidgets = null;
			String formQualifier = null;
			_widgetRelationLines.clear();
			_widgetContainerWeightMap.clear();
			_widgetLayoutTypeMap.clear();
			_widgetList.clear();
			String buildChannel = ArLayoutManager.getChannel();
			JSReviewUtil.printDetailedLog("Build Channel : " + buildChannel);
					
			List<String> formattedLines = getFormattedLinesForAddAtKeyWord(lines);
			formattedLines = getFormattedLinesForMasterData(formattedLines);
			formattedLines = getFormattedLinesForDataGrid(formattedLines);
			
			List<String> functions = getFunctionNames(formattedLines);
			for (String keyword : functions) {
				JSReviewUtil.printDetailedLog("Function : " + keyword);
			}
			
			List<String> horizontalContainers = getHorizontalContainers(formattedLines);
			for (String keyword : horizontalContainers) {
				JSReviewUtil.printDetailedLog("horizontalContainer : " + keyword);
			}
			
			
			if (_isTemplateFile) {
				formQualifier = _templateName;
				JSReviewUtil.printDetailedLog("Template Name : " + formQualifier);
			} else {
				formQualifier = _formName;
				JSReviewUtil.printDetailedLog("Form Name : " + formQualifier);
			}
			
			updateWidgetTree(_widgetRelationLines,_widgetContainerWeightMap);
			
			blackListedWidgets = fetchBlackListedWidgets(_widgetList,getProjectConfig(),formQualifier);
			
			for (String keyword : blackListedWidgets) {
				JSReviewUtil.printDetailedLog("BlackListed Widget : " + keyword);
			}
			
			ArFunctionReplaceRule  arFunctionReplaceRule = new ArFunctionReplaceRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArWidgetLayoutReversalRule arWidgetLayoutReversalRule = new ArWidgetLayoutReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArMarginPaddingReversalRule arMarginPaddingReversalRule = new ArMarginPaddingReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArWidgetAlignmentReversalRule arWidgetAlignmentReversalRule = new ArWidgetAlignmentReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArLayoutAlignmentReversalRule arLayoutAlignmentReversalRule = new ArLayoutAlignmentReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArContentAlignmentReversalRule arContentAlignmentReversalRule = new ArContentAlignmentReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArFormNameReplaceRule arFormNameReplaceRule = new ArFormNameReplaceRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArTemplateNameReplaceRule arTemplateNameReplaceRule = new ArTemplateNameReplaceRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArScrollBoxRule arScrollBoxRule = new ArScrollBoxRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArScrollConfigReversalRule arScrollConfigReversalRule = new ArScrollConfigReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArWidgetMasterDataReversalRule arWidgetMasterDataReversalRule = new ArWidgetMasterDataReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArSliderSwitchViewReversalRule arSliderSwitchViewReversalRule = new ArSliderSwitchViewReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			ArDataGridReversalRule arDataGridReversalRule = new ArDataGridReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			
			ArAnimationsReversalRule arAnimationsReversalRule = new ArAnimationsReversalRule(formQualifier,blackListedWidgets,getProjectConfig(),buildChannel);
			
			
			//Replace form Name with Ar Suffix
			JSReviewUtil.printDetailedLog("applying replaceFunctionNames");
			formattedLines = arFunctionReplaceRule.replaceFunctionNames(formattedLines,functions);
			JSReviewUtil.printDetailedLog("applying reverseWidgetLayout");
			formattedLines = arWidgetLayoutReversalRule.reverseWidgetLayout(formattedLines,horizontalContainers);
			
			JSReviewUtil.printDetailedLog("applying reverseMarginsPaddings");
			formattedLines = arMarginPaddingReversalRule.reverseMarginsPaddings(formattedLines);
			JSReviewUtil.printDetailedLog("applying reverseWidgetAlignment");
			formattedLines = arWidgetAlignmentReversalRule.reverseWidgetAlignment(formattedLines);
			JSReviewUtil.printDetailedLog("applying reverseLayoutAlignment");
			formattedLines = arLayoutAlignmentReversalRule.reverseLayoutAlignment(formattedLines);
			JSReviewUtil.printDetailedLog("applying reverseContentAlignment");
			formattedLines = arContentAlignmentReversalRule.reverseContentAlignment(formattedLines);
			
			JSReviewUtil.printDetailedLog("applying reverseScrollConfig");
			formattedLines = arScrollConfigReversalRule.reverseScrollConfig(formattedLines);
			JSReviewUtil.printDetailedLog("applying reverseMasterData");
			formattedLines = arWidgetMasterDataReversalRule.reverseMasterData(formattedLines,is_iOS_Channel);
			JSReviewUtil.printDetailedLog("applying reverseSliderSwitchViews");
			formattedLines = arSliderSwitchViewReversalRule.reverseSliderSwitchViews(formattedLines);
			JSReviewUtil.printDetailedLog("applying reverseDataGridData");
			formattedLines = arDataGridReversalRule.reverseDataGridData(formattedLines);
			
			if (_isTemplateFile) {
				if (horizontalContainers != null && horizontalContainers.size()>0) {
					JSReviewUtil.printDetailedLog("applying replaceTemplateNames");
					formattedLines = arTemplateNameReplaceRule.replaceTemplateNames(formattedLines);
				}
			} else {
				JSReviewUtil.printDetailedLog("applying replaceFormName");
				formattedLines = arFormNameReplaceRule.replaceFormName(formattedLines);
			}
			
			// Temporary rule !!
			JSReviewUtil.printDetailedLog("applying applyScrollBoxRules");
			formattedLines = arScrollBoxRule.applyScrollBoxRules(formattedLines);
			
			String fName = fileName.substring(0, fileName.length() - 3);
			String channel = ArLayoutManager.getChannel();
			String excludeChannelFlag = getProjectConfig().getArabicLayoutProperty(PROP_FORM_ANIMATION_EXCLUDE+fName+DOT+channel);
			String excludeFlag = getProjectConfig().getArabicLayoutProperty(PROP_FORM_ANIMATION_EXCLUDE+fName+DOT+"all");
			boolean excludeAnimation = (excludeChannelFlag != null && excludeChannelFlag.toLowerCase().equals(TRUE)) || (excludeFlag != null && excludeFlag.toLowerCase().equals(TRUE));
			if(!excludeAnimation) {
				JSReviewUtil.printDetailedLog("applying reverseAnimations");
				formattedLines = arAnimationsReversalRule.reverseAnimationValues(formattedLines);
			}
	
			// Add a comment at the top of the file
			String timestamp = BLANK + Calendar.getInstance().getTime().toString();
			formattedLines.add(0, "//Do not Modify!! This is an auto generated module for '"+ArLayoutManager.getChannel()+"'. Generated on " + timestamp);
			ArabicLayoutUtil.getInstance().createReportFile(formattedLines, AR_FILE_PREFIX + fileName);
			JSReviewUtil.printDetailedLog("successfully created : " + AR_FILE_PREFIX + fileName);
			JSReviewUtil.printDetailedLog("processJSFile END : " +  fileName);
			
		}catch (Exception e) {
			JSReviewUtil.printDetailedLog("ERROR processing File : " + fileName + " : " + e + " Stack Trace : " + e.getStackTrace());
			JSReviewUtil.printDetailedLog("ERROR : Could not transform File : " + fileName);
		}
		
	}
	
	private List<String> getFormattedLinesForAddAtKeyWord(List<String> lines) {
		List<String> formattedLines = new ArrayList<String>();
		int index1 = -1;
		int index2 = -1;
		StringBuffer strBuff = new StringBuffer();
		boolean isConsoliationMode = false; 
		
		for (String line : lines) {
			
			if (isConsoliationMode) {
				index2 = line.indexOf(CHAR_SEMI_COLON);
				if (index2 > 0) {
					isConsoliationMode= false;
					strBuff.append(line);
					formattedLines.add(strBuff.toString());
					_widgetRelationLines.add(strBuff.toString());
					continue;
				}
			}
			
			index1 = line.indexOf(KEYWORD_ADD);
			index2 = line.indexOf(CHAR_SEMI_COLON);
			if (index1>0 && index2<index1) {
				isConsoliationMode = true;
				strBuff = new StringBuffer();
				strBuff.append(line);
			} else {
				formattedLines.add(line);
			}
		}	
		
		return formattedLines;
		
	}
	
	private List<String> getFormattedLinesForMasterData(List<String> lines) {
		List<String> formattedLines = new ArrayList<String>();
		int index1 = -1;
		int index2 = -1;
		StringBuffer strBuff = new StringBuffer();
		boolean isConsoliationMode = false; 
		int openSqrBraceCount = 0;
		
		for (String line : lines) {
			
			if (isConsoliationMode) {
				openSqrBraceCount = getOpenSqrBraceCount(line,openSqrBraceCount);
				strBuff.append(line.trim());
					if (openSqrBraceCount>0) {
						continue;
					} else {
						formattedLines.add(strBuff.toString());
						isConsoliationMode = false;
						openSqrBraceCount = 0;
						continue;
					}
				
			}
			
			index1 = line.indexOf(KEYWORD_MASTER_DATA);
			index2 = line.indexOf(CHAR_SEMI_COLON);
			if (index1>0 && index2<index1) {
				openSqrBraceCount = 0;
				openSqrBraceCount = getOpenSqrBraceCount(line,openSqrBraceCount);
				if(openSqrBraceCount>0) {
					isConsoliationMode = true;
					strBuff = new StringBuffer();
					strBuff.append(line);
				} else {
					isConsoliationMode = false;
					formattedLines.add(line);
				}
			} else {
				formattedLines.add(line);
			}
		}	
		
		return formattedLines;
		
	}
	
	private List<String> getFunctionNames(List<String> formattedLines) {
		List<String> functions = new ArrayList<String>();
		String functionName = null;
		int formsCount = 0;
		
		int index1 =-1;
		int index2 = -1;
		int formIndex = -1;
		int popupIndex =-1;
		
		int index3 = -1;
		int index4 = -1;
		
		
		for (String line : formattedLines) {
			index1 =line.indexOf(FUNCTION);
			index2 = line.indexOf(CHAR_PARANTHESIS_OPEN);
			formIndex = line.indexOf(KEYWORD_KONY_UI_FORM);
			popupIndex = line.indexOf(KEYWORD_KONY_UI_POPUP);
			index3 = line.indexOf(KEYWORD_VAR);
			
			if (formIndex >= 0 && index3<0) {
				formsCount++;
				
				index4 = line.indexOf(CHAR_EQUALS);
				if (index4>=0 && formIndex>index4) {
					_formName = line.substring(0,index4).trim();
				}

			} else if (popupIndex >= 0 && index3<0) {
				formsCount++;
				
				index4 = line.indexOf(CHAR_EQUALS);
				if (index4>=0 && popupIndex>index4) {
					_formName = line.substring(0,index4).trim();
				}
			}

			
			if (index1>=0 && index2>(index1+8)) {
				functionName = line.substring(index1+8, index2);
				functions.add(functionName.trim());
			}
			
		}
		
		// Check if this is a template file
		if (formsCount == 0) {
			// This is a template file. Make a list of the initialize method applicable for thiss template
			for (String fnName : functions) {
					if (fnName.trim().startsWith(KEYWORD_INITIALIZE_FUNCTION)) {
					_isTemplateFile = true;
					_templateName = fnName.trim().replace(KEYWORD_INITIALIZE_FUNCTION,BLANK).trim();
					ArLayoutManager.addTemplateInitializeMethod(fnName);
					break;
				}
			}
		}
		
		return functions;
	}
	
	private List<String> getHorizontalContainers(List<String> formattedLines) {
		List<String> hzContainers = new ArrayList<String>();
		String currentWidget = null;
		boolean isContainer = false;
		String orientation = null;
		boolean isTemplate = false;
		int index1 = -1;
		int index2 = -1;
		int index3 = -1;
		
		int index4=-1;
		int index5=-1;
		
		int index6 = -1;
		int index7 = -1;
		int index8 = -1;
		Integer containerWeight = null;
		String percentageFlag = null;
		
		for (String line : formattedLines) {
			index1 = line.indexOf(KEYWORD_KONY_UI);

			// code needed for extracting container weight & percentage attributes of the current widget  - start
			index6 = line.indexOf(KEYWORD_CONTAINER_WEIGHT);
			index7 = line.indexOf(CHAR_COLON);
			index8 = line.indexOf(KEYWORD_PERCENTAGE_LAYOUT_FLAG);
			
			if (index6>=0 && index7>index6) {
				try {
					containerWeight = Integer.decode(line.substring(index7+1).replaceAll(COMMA, BLANK).trim());
					_widgetContainerWeightMap.put(currentWidget, containerWeight);
				} catch (Exception e) {
					JSReviewUtil.printDetailedLog("FYI - Exception (1) in getHorizontalContainers: " + e + " Stack Trace : " + e.getStackTrace());
					containerWeight = new Integer(0);
					_widgetContainerWeightMap.put(currentWidget, containerWeight);
				}
			}
			if (index8>=0 && index7>index8) {
				try {
					percentageFlag = line.substring(index7+1).replaceAll(COMMA, BLANK).trim();
					_widgetLayoutTypeMap.put(currentWidget, percentageFlag);
				} catch (Exception e) {
					JSReviewUtil.printDetailedLog("FYI - Exception (2) in getHorizontalContainers: " + e + " Stack Trace : " + e.getStackTrace());
					_widgetLayoutTypeMap.put(currentWidget, FALSE);
				}
			}
			
			// code needed for extracting container weight of the current widget - ends
			
			if(index1>=0) {
				index2 = line.indexOf(CHAR_EQUALS);
				index3 = line.indexOf(KEYWORD_VAR);
				
				if (index2 >=0 && index3>=0 && 
							index1>index2 && index2>(index3+3)) {
					currentWidget = line.substring(index3+3,index2).trim();
					isTemplate = false;
					if (line.indexOf(KEYWORD_KONY_UI_BOX)>=0 || line.indexOf(KEYWORD_KONY_UI_SCROLLBOX)>=0) {
						isContainer= true;
					}else {
						isContainer= false;
					}
				} else if (index2 >=0 && index3<0 && index1>index2) {
					
					// it should be header, footer or a segment template. change the logic later
					currentWidget = line.substring(0,index2).trim();
					isTemplate = true;
					if (line.indexOf(KEYWORD_KONY_UI_BOX_TEMPLATE)>=0 || line.indexOf(KEYWORD_KONY_UI_BOX)>=0 ) {
						isContainer= true;
					}else {
						isContainer= false;
					}
				}
				
			}	
			
			index4 = line.indexOf(KEYWORD_ORIENTATION);
			if (index4>=0) {
				index5 = line.indexOf(CHAR_COLON);
				if (index5>=0 && index5>index4) {
					orientation = line.substring(index5+1).trim();
					if (currentWidget != null && isContainer) {
						if (orientation.indexOf(KEYWORD_HZ_BOX_LAYOUT)>=0) {
							hzContainers.add(currentWidget);
							// To be used else where in the final module generation
							if(isTemplate) {
								ArLayoutManager.addTemplate(currentWidget);
							}
						}
					}
				}
			}
		}
		
		return hzContainers;
	}
	
	private int getOpenSqrBraceCount(String line, int currOpenSqrBraceCount) {
		if (line == null || line.trim().length()==0) {
			return currOpenSqrBraceCount;
		}
		
		int count = currOpenSqrBraceCount;
		
		char[] chars = line.toCharArray();
		
		for (char c : chars) {
			if (c==CHAR_SQUARE_OPEN) {
				count++;
			}else if (c==CHAR_SQUARE_CLOSE){
				count--;
			}
		}
		return count;
	}
	
	private List<String> getFormattedLinesForDataGrid(List<String> lines) {
		List<String> formattedLines = new ArrayList<String>();
		int index1 = -1;
		int index2 = -1;
		int index3 = -1;
		StringBuffer strBuff = new StringBuffer();
		boolean isConsoliationMode = false; 
		int openSqrBraceCount = 0;
		
		String currentWidgetType = BLANK;
		
		for (String line : lines) {
			index3 = line.indexOf(KEYWORD_KONY_UI);
			if (index3 >=0) {
				if(line.indexOf(KEYWORD_KONY_UI_DATAGRID)>=0) {
					// Ignore any transformation of CheckBox Master Data for iOS channels
					currentWidgetType = KEYWORD_KONY_UI_DATAGRID;
				}  else {
					currentWidgetType = BLANK;
				}
			}
			
			if (isConsoliationMode) {
				openSqrBraceCount = getOpenSqrBraceCount(line,openSqrBraceCount);
				strBuff.append(line.trim());
					if (openSqrBraceCount>0) {
						continue;
					} else {
						formattedLines.add(strBuff.toString());
						isConsoliationMode = false;
						openSqrBraceCount = 0;
						continue;
					}
				
			}
			
			index1 = line.indexOf(KEYWORD_DATAGRID_COLUMN_CONFIG);
			index2 = line.indexOf(CHAR_SEMI_COLON);
			if (index1>0 && index2<index1 && currentWidgetType.equals(KEYWORD_KONY_UI_DATAGRID)) {
				openSqrBraceCount = 0;
				openSqrBraceCount = getOpenSqrBraceCount(line,openSqrBraceCount);
				if(openSqrBraceCount>0) {
					isConsoliationMode = true;
					strBuff = new StringBuffer();
					strBuff.append(line);
				} else {
					isConsoliationMode = false;
					formattedLines.add(line);
				}
			} else {
				formattedLines.add(line);
			}
		}	
		
		return formattedLines;
		
	}
	
	private void updateWidgetTree(List<String> widgetLines, Map<String,Integer> widgetContainerWeightMap) {
		String line = null;
		int index1=-1;
		int index2=-1;
		String parentWidget = null;
		String[] childWidgets = null;
		int totalContainerWeight=0;
		String parentWidgetLayoutType = null;
		WidgetVo parentWidgetVo = null;
		WidgetVo childWidgetVo = null;
		String formName = null;
		int intVal = 0;
			
		// Give the tree that is useful for black listing
		for(String widgetLine : widgetLines) {
			//System.out.println("unformatted: " + widgetLine);
			index1 = widgetLine.indexOf(CHAR_PARANTHESIS_OPEN);
			index2 = widgetLine.indexOf(KEYWORD_ADD);
			parentWidget = widgetLine.substring(0,index2).trim();
			
			parentWidgetVo = new WidgetVo();
			parentWidgetVo.setWidgetName(parentWidget);
			
			line = widgetLine.substring(index1+1, widgetLine.length()-2).trim();
			//System.out.println("parent: " + parentWidget);
			//System.out.println("children: " + line);
			childWidgets = line.split(COMMA);
			//System.out.println("See Container Weights for : " + parentWidget);
			try {
				totalContainerWeight=0;
				for (String widget : childWidgets) {
					//System.out.println(widget.trim() + " : " + _widgetContainerWeightMap.get(widget.trim()).intValue());
					childWidgetVo = new WidgetVo();
					childWidgetVo.setWidgetName(widget.trim());
					
					parentWidgetVo.addChildWidget(childWidgetVo);
					intVal=0;
					try {
						intVal = _widgetContainerWeightMap.get(widget.trim()).intValue();
					} catch (Exception e){
						JSReviewUtil.printDetailedLog("FYI - Exception (1) in updateWidgetTree: " + e + " Stack Trace : " + e.getStackTrace());
						intVal = 0;
					}
					totalContainerWeight += intVal;
				}
				parentWidgetLayoutType = _widgetLayoutTypeMap.get(parentWidget.trim());
				if (totalContainerWeight <100 && null != parentWidgetLayoutType && parentWidgetLayoutType.trim().equalsIgnoreCase(TRUE)) {
					if (_isTemplateFile) {
						formName = _templateName;
					} else {
						formName = _formName;
					}
					ArLayoutManager.addWarning(formName + CHAR_DOT + parentWidget + " : Sum Total Width of Containers under this Widget is " + totalContainerWeight + "%, which is <100%. Mirror Layout may not be accurate");
				}
			} catch (Exception e) {
				//e.printStackTrace();
				JSReviewUtil.printDetailedLog("FYI - Exception in updateWidgetTree: " + e + " Stack Trace : " + e.getStackTrace());
			}
			
			consolidateWidgetList(parentWidgetVo);
			//System.out.println("consolidation done for " + parentWidgetVo.getWidgetName());
		}
		
	}
	
	private void consolidateWidgetList(WidgetVo newWidgetVo) {
		boolean isReplaced = false;
		boolean isLoopEnd = false;
		boolean isReplacedAny = false;
		int widgetListSize = 0;
		
		// Check if New Widget is a Child of Existing Widgets
		for (WidgetVo existingWidget : _widgetList) {
			isReplaced = insertWidget(newWidgetVo,existingWidget);
			if (isReplaced) {
				return;
			}
		}
		
		// Check if any of the existing widgets are child of this new widget !
		widgetListSize = _widgetList.size();
		
		isReplacedAny = false;
		for (int i=0;(i<widgetListSize && widgetListSize>0);i++) {
			WidgetVo existingWidget = _widgetList.get(i);
			isReplaced = insertWidget(existingWidget, newWidgetVo);
				
			if (isReplaced) {
				isReplacedAny = true;
				_widgetList.remove(i);
				widgetListSize--;
				i--;
				//break;
			}
		}
		
		if (isReplacedAny) {
			_widgetList.add(newWidgetVo);
		}
		
		// This is a new top level widget
		if (!isReplacedAny) {
			_widgetList.add(newWidgetVo);
		}
	}
	
	private boolean insertWidget(WidgetVo searchWidget, WidgetVo targetWidget) {
		boolean replaceFlag = false;
		
		if (null == searchWidget || null == targetWidget) {
			return false;
		}

		if (targetWidget.getWidgetName().equals(searchWidget.getWidgetName())) {
			replaceFlag = true;
			for (WidgetVo searchChildWidget : searchWidget.getChildWidgets()) {
				targetWidget.addChildWidget(searchChildWidget);
			}
		} else if (targetWidget.getChildWidgets() != null && targetWidget.getChildWidgets().size()>0){
			for (WidgetVo childWidget: targetWidget.getChildWidgets()) {
				replaceFlag = insertWidget(searchWidget, childWidget);
				if (replaceFlag) {
						break;
				}
			}
		}

		return replaceFlag;
	}
	
	private TreeSet<String> fetchBlackListedWidgets(List<WidgetVo> widgetsList, ProjectConfig projectConfig, String formQualifier) {
		TreeSet<String> forbiddenWidgetsList = new TreeSet<String>();
		String key = null;
		String value = null;
		
		for (WidgetVo widgetVo : widgetsList) {
			key = formQualifier+CHAR_DOT+widgetVo.getWidgetName()+KEYWORD_EXLCUDE;
			value = projectConfig.getArabicLayoutProperty(key);
			if (value != null && value.trim().equalsIgnoreCase(TRUE)) {
				forbiddenWidgetsList.add(widgetVo.getWidgetName());
				forbiddenWidgetsList.addAll(getAllChildWidgets(widgetVo));
			}
			
			// Repeat the same for all child widgets
			if (widgetVo.getChildWidgets() != null) {
				forbiddenWidgetsList.addAll(
						fetchBlackListedWidgets(widgetVo.getChildWidgets(),
								projectConfig,formQualifier));
			}

		}
		
		return forbiddenWidgetsList;
	}
	
	private TreeSet<String> getAllChildWidgets(WidgetVo widgetVo) {
		TreeSet<String> childWidgets = new TreeSet<String>();
		
		if (widgetVo != null && widgetVo.getChildWidgets() != null 
								&& widgetVo.getChildWidgets().size()>0) {
			for (WidgetVo childWidget : widgetVo.getChildWidgets()) {
				childWidgets.add(childWidget.getWidgetName());
				
				if (childWidget.getChildWidgets() != null) {
					for (WidgetVo deepChildWidget : childWidget.getChildWidgets()) {
						childWidgets.add(deepChildWidget.getWidgetName());
						childWidgets.addAll(getAllChildWidgets(deepChildWidget));
					}
				}
				
			}
		}
		
		return childWidgets;
	}
}
	