package com.kony.tag.js.codereview.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.tasks.formreview.impl.UnusedItemsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedFunctionsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedI18Rule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedImagesRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedSkinsRule;
import com.kony.tag.util.RuleFactory;

public class KonyFormContentHandler extends XmlContentHandler implements ProjectConstants {
	
	private WidgetVo formWidgetVo = null;
	private WidgetVo menuContainerWidgetVo = null;
	private List<WidgetVo> widgetStack = null;
	private String currentTag = null;
	private String currentAttribute = null;
	
	private UnusedItemsRule _unusedItemsRule = null;
	private UnusedFunctionsRule _unusedFunctionsRule = null;
	private UnusedSkinsRule _unusedSkinsRule = null;
	private UnusedImagesRule _unusedImagesRule = null;
	private UnusedI18Rule _unusedI18Rule = null;
	
	private boolean _isOnClickApplicable = false;
	private boolean _isExprMapTagActive = false;
	private boolean _isActionListTagActive = false;
	private String _actionTypeTag = null;
	private boolean _isOverrideFlagActive = false;
	private ProjectConfig  codeReviewConfig = null;
	
	
	private final ArrayList<String> onClickWidgetsTypeA = new ArrayList<String>(
			Arrays.asList(KONY_HBOX,KONY_VBOX,KONY_BUTTON,KONY_LINK));

	private final ArrayList<String> onClickWidgetsTypeB = new ArrayList<String>(
			Arrays.asList(KONY_COMBO_BOX,KONY_LIST_BOX,KONY_TEXT_AREA,KONY_TEXT_BOX,KONY_SEGMENT,KONY_CALENDAR,KONY_TAB_PANE,KONY_CHECKBOX_GROUP,KONY_DATA_GRID,KONY_RADIO_BUTTON_GROUP,KONY_CAMERA,KONY_HZ_IMAGE_STRIP,KONY_IMAGE_GALLERY,KONY_OBJ_SELECTOR_3D,KONY_PICKER_VIEW,KONY_TEXT_AREA_OLD,KONY_TEXT_BOX_OLD,KONY_SEGMENT_OLD));	
	
	private String tmpAttributeValue = null;
	
	private final ArrayList<String> focusUISkinWidgetList = new ArrayList<String>(
			Arrays.asList(KONY_HBOX,KONY_VBOX,KONY_TAB_PANE,KONY_BUTTON,KONY_CALENDAR,KONY_CHECKBOX_GROUP,KONY_COMBO_BOX,KONY_LINK,KONY_LIST_BOX,KONY_RADIO_BUTTON_GROUP,KONY_TEXT_AREA,KONY_TEXT_BOX,KONY_CAMERA,KONY_HZ_IMAGE_STRIP,KONY_IMAGE_GALLERY,KONY_OBJ_SELECTOR_3D,KONY_PHONE,KONY_PICKER_VIEW));
	
	private KonyFormContentHandler() {
		// Do not use
	}
	
	public KonyFormContentHandler(ProjectConfig codeReviewConfig) {
		this.codeReviewConfig = codeReviewConfig;
		init();
	}
	
	public void init() {
		super.init(); 
		formWidgetVo = new WidgetVo();
		widgetStack = new ArrayList<WidgetVo>();
		currentTag = null;
		_isExprMapTagActive = false;
		_isActionListTagActive = false;
		_actionTypeTag = null;
		_isOverrideFlagActive = false;
				 
		 if (_unusedItemsRule == null) {
			 try {
				 
				List<FunctionVo> unusedFunctions = null;
				List<String> unusedSkins = null;
				List<String> unusedImages = null;
				List<String> unusedI18Keys = null;
				
				_unusedFunctionsRule = (UnusedFunctionsRule) RuleFactory.getCodeReviewRuleInstance(UnusedFunctionsRule.PROPS_RULE_NAME,codeReviewConfig);
				if (_unusedFunctionsRule != null) {
					unusedFunctions = _unusedFunctionsRule.getAllUnusedFunctions();
				}
				
				_unusedSkinsRule = (UnusedSkinsRule) RuleFactory.getCodeReviewRuleInstance(UnusedSkinsRule.PROPS_RULE_NAME,codeReviewConfig);
				if (_unusedSkinsRule != null) {
					unusedSkins = _unusedSkinsRule.getAllUnusedSkins();
				}
				
				_unusedImagesRule = (UnusedImagesRule) RuleFactory.getCodeReviewRuleInstance(UnusedImagesRule.PROPS_RULE_NAME,codeReviewConfig);
				if (_unusedImagesRule != null) {
					unusedImages = _unusedImagesRule.getAllUnusedImages();
				}

				_unusedI18Rule = (UnusedI18Rule) RuleFactory.getCodeReviewRuleInstance(UnusedI18Rule.PROPS_RULE_NAME,codeReviewConfig);
				if (_unusedI18Rule != null) {
					unusedI18Keys = _unusedI18Rule.getAllUnusedI18Keys();
				}
				
					
				_unusedItemsRule = (UnusedItemsRule) RuleFactory.getCodeReviewRuleInstance(UnusedItemsRule.PROPS_RULE_NAME, codeReviewConfig);
				_unusedItemsRule.rulesInit(BLANK, BLANK, unusedFunctions,unusedSkins,unusedImages,unusedI18Keys);
				 
			 } catch (TagToolException excp) {
				 CodeReviewStatus.getInstance().addErrorMessage(excp, excp.getErrorMessage());
			 }
		 }
	}
	
	public WidgetVo getFormWidgetVo() {
		return formWidgetVo;
	}

	public WidgetVo getMenuContainerWidgetVo() {
		return menuContainerWidgetVo;
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		//System.out.println("Attribute : " + new String(ch, start, length));
		tmpAttributeValue = null;
		
		if (currentTag != null) {
			
			if (length <= 0 || ch==null || ch.length<=0) {
				tmpAttributeValue = NONE;
			} else {
				tmpAttributeValue = new String(ch, start, length);
			}
			
			if (currentAttribute != null) {
				currentAttribute += tmpAttributeValue;
			} else {
				currentAttribute = tmpAttributeValue;
			}
		}
		
		//System.out.println("READ Attribute : " + currentAttribute);
		
		// Apply any misc code review rules
		if (_unusedItemsRule != null) {
			if(_unusedFunctionsRule != null && _unusedFunctionsRule.isEnabled()) {
				_unusedItemsRule.checkForUsedFunctions(currentAttribute);
			}
			
			if(_unusedSkinsRule != null && _unusedSkinsRule.isEnabled()) {
				_unusedItemsRule.checkForUsedSkins(currentAttribute);
			}
			
			if(_unusedImagesRule != null && _unusedImagesRule.isEnabled()) {
				_unusedItemsRule.checkForUsedImages(currentAttribute);
			}
			
			if(_unusedI18Rule != null && _unusedI18Rule.isEnabled()) {
				_unusedItemsRule.checkForUsedI18Keys(currentAttribute);
			}
			
		}
	}
	
	private void updateTagValue() {
		WidgetVo widgetVo = null;
		StringBuffer tmpStrBuffer = null;

		if (null == currentTag){
			return;
		}

		if (null == currentAttribute || currentAttribute.trim().length()==0) {
			currentAttribute = NONE;
		} else {
			currentAttribute = currentAttribute.trim();
		}
		
		if (widgetStack.size()>0) {
			widgetVo = widgetStack.get(0);
			/*if(!currentTag.equalsIgnoreCase("right") && !currentTag.equalsIgnoreCase("left") && 
					!currentTag.equalsIgnoreCase("top") && !currentTag.equalsIgnoreCase("bottom") && !currentTag.equalsIgnoreCase("unit") && 
					!currentTag.startsWith("spa") && !currentTag.startsWith("xhtml") && !currentTag.startsWith("tc")) {
				Util.printToConsole("currentTag : "+currentTag+" currentAttribute: "+currentAttribute);
			}*/
			
			if (currentTag.equals("ID")) {
				widgetVo.setWidgetName(currentAttribute);
			} else if (currentTag.equals("localeId")) {
				widgetVo.setI18Title(currentAttribute);
			} else if (currentTag.equals("i18key")) {
				widgetVo.addI18Key(currentAttribute);
			} else if (currentTag.equals("placeHolderI18NId")) {
				widgetVo.setI18PlaceHolder(currentAttribute);
			} else if (currentTag.equals("secure")) {
				if (currentAttribute != null && currentAttribute.trim().equals("true")) {
					widgetVo.setSecureSubmitEnabled(true);
				} else {
					widgetVo.setSecureSubmitEnabled(false);
				}
			} else if (currentTag.equals("idle_timeout")) {
				if (currentAttribute != null && currentAttribute.trim().equals("true")) {
					widgetVo.setIdleTimeOutEnabled(true);
				} else {
					widgetVo.setIdleTimeOutEnabled(false);
				}
			} else if (currentTag.equals("blockedSkin")
					|| currentTag.equals("parent_blocked_UI")
					|| currentTag.equals("blockedUISkin")) {
				widgetVo.setBlockedUISkinDefault(currentAttribute);
			} else if (currentTag.equals("tc_blockedUI") || 
					currentTag.equals("TCBlockedUISkin") ||
					currentTag.equals("tc_blockedUISkin")) {
				widgetVo.setBlockedUISkinTC(currentAttribute);
			}else if (currentTag.equals("spa_blockedUISkin")
					|| currentTag.equals("spa_blockedUI") 
					|| currentTag.equals("spaBlockedUISkin")
					|| currentTag.equals("spa_blockedSkin")) {
				widgetVo.setBlockedUISkinSPA(currentAttribute);
			} else if (currentTag.equals("blockedUISkin")) {
				if (widgetVo.getWidgetType().equals(KONY_COMBO_BOX)
						|| widgetVo.getWidgetType().equals(KONY_LIST_BOX) 
						|| widgetVo.getWidgetType().equals(KONY_TEXT_AREA)) {
					
					if (widgetVo.getBlockedUISkinDefault() == null) {
						widgetVo.setBlockedUISkinDefault(currentAttribute);
					}
				} else {
					if (widgetVo.getBlockedUISkinTC() == null) {
						widgetVo.setBlockedUISkinTC(currentAttribute);
					}
				}
			} else if (currentTag.equals("itemFocusSkinId")) {
				if (widgetVo.getWidgetType().equals(KONY_FORM)
						|| widgetVo.getWidgetType().equals(KONY_DATA_GRID)) {
					widgetVo.setFocusSkin(currentAttribute);
				}
			} else if (currentTag.equals("rowFocusSkin")) {
				if (widgetVo.getWidgetType().equals(KONY_SEGMENT)) {
					widgetVo.setFocusSkin(currentAttribute);
				}
			} else if (currentTag.equals("focusSkinId")) {
				if (focusUISkinWidgetList.contains(widgetVo.getWidgetType())) {
					widgetVo.setFocusSkin(currentAttribute);
				}
			} else if (currentTag.equals("skinId")) {
					widgetVo.setSkinId(currentAttribute);
			} else if (currentTag.equalsIgnoreCase("screenLevelWidget")) {
				
				if (widgetVo.getWidgetType().equals(KONY_SEGMENT) || widgetVo.getWidgetType().equals(KONY_BROWSER) || widgetVo.getWidgetType().equals(KONY_MAP)) {
					if (null != currentAttribute && currentAttribute.trim().equalsIgnoreCase("true")) {
						widgetVo.setScreenLevelWidget(true);
					} else {
						widgetVo.setScreenLevelWidget(false);
					}
					
				}
			} else if (currentTag.equalsIgnoreCase("_screenLevelWidget")) {
				if (widgetVo.getWidgetType().equals(KONY_MAP)) {
					if (null != currentAttribute && currentAttribute.trim().equalsIgnoreCase("true")) {
						widgetVo.setScreenLevelWidget(true);
					} else {
						widgetVo.setScreenLevelWidget(false);
					}
					
				}
			} else if (_isActionListTagActive && _isExprMapTagActive && currentTag.equalsIgnoreCase("value")) {
					if (null != currentAttribute && currentAttribute.trim().length()>0) {
						tmpStrBuffer = new StringBuffer();
						for (char c : currentAttribute.toCharArray()) {
							if (!Character.isWhitespace(c)) {
								tmpStrBuffer.append(c);
							}
						}
						widgetVo.addCodeSnippet(tmpStrBuffer.toString());
					} 
			} else if (_isOverrideFlagActive && currentTag.equalsIgnoreCase("width")) {
				if (null != currentAttribute && currentAttribute.equalsIgnoreCase("true")) {
					widgetVo.setExpandHorizontalEnabled(true);
				} else {
					widgetVo.setExpandHorizontalEnabled(false);
				}
			} else if (_isOverrideFlagActive && currentTag.equalsIgnoreCase("height")) {
				if (null != currentAttribute && currentAttribute.equalsIgnoreCase("true")) {
					widgetVo.setExpandVerticalEnabled(true);
				} else {
					widgetVo.setExpandVerticalEnabled(false);
				}
			} else if (currentTag.equals("SText") || currentTag.equals("buttonText")
					 || currentTag.equals("leftButtonText") || currentTag.equals("textChecked")
					 || currentTag.equals("android_titleText") || currentTag.equals("symbian_titleText")
					 || currentTag.equals("left_text") || currentTag.equals("right_text")) {
				widgetVo.addWidgetText(currentAttribute);
			} else if (currentTag.equals("showProgressIndicator")) {
				widgetVo.setBtnProgressIndicatorEnabled(currentAttribute != null && currentAttribute.equalsIgnoreCase("true"));
			} else if(currentTag.equals("dummydata")) {
				widgetVo.setMasterDataSetToWidgetForPreview(currentAttribute != null && currentAttribute.equalsIgnoreCase("true"));
			} else if(currentTag.equals("htmlcontent")) {
				widgetVo.setMasterDataSetToWidgetForPreview(currentAttribute == null || currentAttribute.equals(""));
			} else if(currentTag.equals("position")) {
				int position = currentAttribute == null ? 0 : Integer.parseInt(currentAttribute);
				widgetVo.setBoxPosition(position);
			}
		}		
	}

	@Override
	public void endElement(String uri, String localName,
			String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		updateTagValue();
		currentTag = null;
		currentAttribute = null;
		
		if (qName.equals(KONY_FORM_CHILDREN)) {
			if (widgetStack.size()>0) {
				//printContainerStack(widgetStack);
				
				if (widgetStack.size()==1) { 
						if (widgetStack.get(0).getWidgetType().equals(KONY_FORM)) {
							formWidgetVo = widgetStack.get(0);
						} else if (widgetStack.get(0).getWidgetType().equals(KONY_MENU_CONTAINER)) {
							menuContainerWidgetVo = widgetStack.get(0);
						}
					
				} else if (widgetStack.size()>1) {
					widgetStack.get(1).addChildWidget(widgetStack.get(0));
				}
				widgetStack.remove(0);
				
				/*
				if (widgetStack.size()==1 && 
						widgetStack.get(0).getWidgetType().equals(KONY_FORM)) {
					formWidgetVo = widgetStack.get(0);
				} else if (widgetStack.size()>1) {
					widgetStack.get(1).addChildWidget(widgetStack.get(0));
				}
				widgetStack.remove(0);
				*/
			}
		}
		
		if (qName != null && qName.equalsIgnoreCase("exprMap")) {
			_isExprMapTagActive = false;
		}
		
		if (qName != null && _actionTypeTag != null && qName.equalsIgnoreCase(_actionTypeTag)) {
			_isActionListTagActive = false;
			_isExprMapTagActive = false;
			_actionTypeTag = null;
		}
		
		if (qName != null && (qName.equalsIgnoreCase("overridePerferred") || qName.equalsIgnoreCase("overridePreferred"))) {
			_isOverrideFlagActive = false;
		}
		
		//System.out.println("End Element :" + qName);
	}

	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {

		// TODO Auto-generated method stub
		String widgetType = BLANK;
		WidgetVo widgetVo = null;
		String actionType=null;
		currentTag = qName;
		currentAttribute = null;
		
		if (qName.equals(KONY_FORM_CHILDREN)) {
			widgetVo = new WidgetVo();
			//widgetType = getCDATAValue(attributes);
			widgetType = getAttribute(attributes, "xsi:type");
			widgetVo.setWidgetType(widgetType);
			widgetStack.add(0,widgetVo);
			//printContainerStack(widgetStack);
		}

		actionType = getAttribute(attributes, "xsi:type");
		if(currentTag  != null && actionType != null && actionType.equals("eventExpressionAction")) {
			_isActionListTagActive = true;
			_actionTypeTag = currentTag;
		}
		
		if (_isActionListTagActive && currentTag != null && currentTag.equalsIgnoreCase("exprMap")) {
				_isExprMapTagActive = true;
		}
		
		if (qName != null && (qName.equalsIgnoreCase("overridePerferred") || qName.equalsIgnoreCase("overridePreferred"))) {
			_isOverrideFlagActive = true;
		}
		if(currentTag != null ) {
			
				if (widgetStack.size()>0) {
					
					widgetVo = widgetStack.get(0);
					
					if(currentTag.equalsIgnoreCase("event_onClick") || 
							currentTag.equalsIgnoreCase("tc_onClick_root") || 
							currentTag.equalsIgnoreCase("onClick")) {
					
					if(onClickWidgetsTypeA.contains(widgetVo.getWidgetType())) {
						widgetVo.setOnClickApplicable(true);
						widgetVo.setOnClickEventString(currentTag);
					}
				} else if(currentTag.equalsIgnoreCase("actionList")) {
					if(onClickWidgetsTypeB.contains(widgetVo.getWidgetType())) {
						widgetVo.setOnClickApplicable(true);
						widgetVo.setOnClickEventString(currentTag);
					}
				}					
			}
		}
		if(currentTag != null && (currentTag.equals("choiceData") || currentTag.equals("browseData"))) {
			if (widgetStack.size()>0) {
				widgetVo = widgetStack.get(0);
				widgetVo.setMasterDataSetToWidget(true);
			}
		}
		//System.out.println("Start Element :" + qName + "     widgetType :: " + widgetType);		
	}
	
	/*
	private String getCDATAValue1(Attributes attributes) {
		String cDataVal = null;
		int attributeCount = attributes.getLength();
		
		for (int i=0;i<attributeCount;i++) {
			if(attributes.getType(i).equals(CDATA)) {
				cDataVal = attributes.getValue(i);
				break;
			}
		}

		return cDataVal;
	}
	*/
	
	private String getAttribute(Attributes attributes, String attributeName) {
		String val = null;
		
		if (attributes ==null || attributeName==null) {
			return null;
		}
		
		int attributeCount = attributes.getLength();
		
		for (int i=0;i<attributeCount;i++) {
			if(attributes.getQName(i).equals(attributeName)) {
				val = attributes.getValue(i);
				break;
			}
		}

		return val;
	}	
	
	private void printContainerStack(List<WidgetVo> widgetStack) {
		if (widgetStack ==null || widgetStack.size()==0) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		
		for (WidgetVo widget : widgetStack) {
			
			if (null == widget.getWidgetType()) {
				sb.append(BLANK);
			} else {
				sb.append(widget.getWidgetType());
			}
			
			sb.append(":");
			if (null == widget.getWidgetName()) {
				sb.append(BLANK);
			} else {
				sb.append(widget.getWidgetName());
			}
			sb.append(" , ");
		}
		
		//System.out.println("Current Stack : " + sb.toString());
	}
}
