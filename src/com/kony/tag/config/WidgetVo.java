package com.kony.tag.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.kony.tag.config.ProjectConstants;

public class WidgetVo implements ProjectConstants {
	
	private String widgetName;
	private String widgetType;
	private Map<String, String> propertiesMap;
	private List<WidgetVo> childWidgets;
	private boolean isContainerWidget = false;
	private boolean isSecureSubmitEnabled = false;
	private boolean isIdleTimeOutEnabled = false;
	
	private boolean isOnClickApplicable = false;
	private boolean isI18KeysApplicable = false;
	private boolean isI18TitleApplicable = false;
	private boolean isI18PlaceHolderApplicable = false;
	private boolean isBlockUISkinApplicable = false;
	private boolean isFocusUISkinApplicable = false;
	private boolean isWidgetTextApplicable = false;
	private List<String> i18Keys = null;
	private String i18Title = null;
	private String i18PlaceHolder = null;
	private String widgetText = null;
	private List<String> widgetTextList = null;
	private String onClickEventString = null;

	// Properties for Blocked UI Skin
	private String blockedUISkinDefault = null;
	private String blockedUISkinTC = null;
	private String blockedUISkinSPA = null;
	
	private String focusSkin = null;
	private boolean isScreenLevelWidget = false;
	
	private List<String> codeSnippets = null;
	
	private String skinId = null;
	private boolean isExpandHorizontalEnabled = false;
	private boolean isExpandVerticalEnabled = false;
	private boolean isBtnProgressIndicatorEnabled = false;
	private boolean masterDataSetToWidget = false;
	private boolean masterDataSetToWidgetForPreview = true;
	private int boxPosition = -1;

	
	public int getBoxPosition() {
		return boxPosition;
	}

	public void setBoxPosition(int boxPosition) {
		this.boxPosition = boxPosition;
	}

	public boolean isMasterDataSetToWidget() {
		return masterDataSetToWidget;
	}

	public void setMasterDataSetToWidget(boolean masterDataSetToWidget) {
		this.masterDataSetToWidget = masterDataSetToWidget;
	}

	public boolean isMasterDataSetToWidgetForPreview() {
		return masterDataSetToWidgetForPreview;
	}

	public void setMasterDataSetToWidgetForPreview(
			boolean masterDataSetToWidgetForPreview) {
		this.masterDataSetToWidgetForPreview = masterDataSetToWidgetForPreview;
	}

	public boolean isExpandHorizontalEnabled() {
		return isExpandHorizontalEnabled;
	}

	public void setExpandHorizontalEnabled(boolean isExpandHorizontalEnabled) {
		this.isExpandHorizontalEnabled = isExpandHorizontalEnabled;
	}
	
	public boolean isBtnProgressIndicatorEnabled() {
		return isBtnProgressIndicatorEnabled;
	}

	public void setBtnProgressIndicatorEnabled(boolean isBtnProgressIndicatorEnabled) {
		this.isBtnProgressIndicatorEnabled = isBtnProgressIndicatorEnabled;
	}

	public boolean isExpandVerticalEnabled() {
		return isExpandVerticalEnabled;
	}

	public void setExpandVerticalEnabled(boolean isExpandVerticalEnabled) {
		this.isExpandVerticalEnabled = isExpandVerticalEnabled;
	}

	public String getSkinId() {
		return skinId;
	}

	public void setSkinId(String skinId) {
		this.skinId = skinId;
	}

	public List<String> getCodeSnippets() {
		return codeSnippets;
	}

	public void addCodeSnippet(String codeSnippet) {
		
		if (this.codeSnippets == null) {
			this.codeSnippets = new ArrayList<String>();
		}
		this.codeSnippets.add(codeSnippet);
	}

	public boolean isScreenLevelWidget() {
		return isScreenLevelWidget;
	}

	public void setScreenLevelWidget(boolean isScreenLevelWidget) {
		this.isScreenLevelWidget = isScreenLevelWidget;
	}

	public String getFocusSkin() {
		return focusSkin;
	}

	public void setFocusSkin(String focusSkin) {
		this.focusSkin = focusSkin;
	}

	public String getBlockedUISkinDefault() {
		return blockedUISkinDefault;
	}

	public void setBlockedUISkinDefault(String blockedUISkinDefault) {
		this.blockedUISkinDefault = blockedUISkinDefault;
	}

	public String getBlockedUISkinTC() {
		return blockedUISkinTC;
	}

	public void setBlockedUISkinTC(String blockedUISkinTC) {
		this.blockedUISkinTC = blockedUISkinTC;
	}

	public String getBlockedUISkinSPA() {
		return blockedUISkinSPA;
	}

	public void setBlockedUISkinSPA(String blockedUISkinSPA) {
		this.blockedUISkinSPA = blockedUISkinSPA;
	}

	private final ArrayList<String> containerWidgetsMasterList = new ArrayList<String>(
			Arrays.asList(KONY_FORM,KONY_HBOX,KONY_VBOX,KONY_SCROLL_BOX,KONY_TAB_PANE));

	private final ArrayList<String> i18KeyList = new ArrayList<String>(
			Arrays.asList(KONY_CHECKBOX_GROUP,KONY_COMBO_BOX,KONY_LIST_BOX,KONY_RADIO_BUTTON_GROUP));

	private final ArrayList<String> i18PlaceHolderList = new ArrayList<String>(
			Arrays.asList(KONY_COMBO_BOX,KONY_LIST_BOX));

	
	private final ArrayList<String> i18TitleList = new ArrayList<String>(
			Arrays.asList(KONY_BUTTON,KONY_LABEL,KONY_LINK,KONY_RICHTEXT_BOX,KONY_TEXT_AREA,KONY_TEXT_BOX,
					KONY_CAMERA,KONY_PHONE,KONY_VIDEO));
	
	private final ArrayList<String> newList = new ArrayList<String>(
			Arrays.asList(KONY_LABEL, KONY_BUTTON));
	
	private final ArrayList<String> masterDataApplicableList = new ArrayList<String>(
			Arrays.asList(KONY_CHECKBOX_GROUP, KONY_COMBO_BOX, KONY_LIST_BOX, KONY_RADIO_BUTTON_GROUP, KONY_BROWSER,
					KONY_HZ_IMAGE_STRIP, KONY_IMAGE_GALLERY, KONY_SEGMENT));
	
	private final ArrayList<String> blockUISkinWidgetList = new ArrayList<String>(
			Arrays.asList(KONY_HBOX,KONY_VBOX,KONY_BUTTON,KONY_COMBO_BOX,KONY_LINK,KONY_LIST_BOX,KONY_TEXT_AREA,KONY_TEXT_BOX,KONY_SEGMENT,KONY_TEXT_AREA_OLD,KONY_TEXT_BOX_OLD,KONY_SEGMENT_OLD));
	

	/*
	private final ArrayList<String> focusUISkinWidgetList = new ArrayList<String>(
			Arrays.asList(KONY_FORM,KONY_HBOX,KONY_VBOX,KONY_TAB_PANE,KONY_BUTTON,KONY_CALENDAR,KONY_CHECKBOX_GROUP,KONY_COMBO_BOX,KONY_DATA_GRID,KONY_LINK,KONY_LIST_BOX,KONY_RADIO_BUTTON_GROUP,KONY_TEXT_AREA,KONY_TEXT_BOX,KONY_CAMERA,KONY_HZ_IMAGE_STRIP,KONY_IMAGE_GALLERY,KONY_OBJ_SELECTOR_3D,KONY_PHONE,KONY_PICKER_VIEW,KONY_SEGMENT));
	*/

	// Do not report Menu Focus skin rule for a FORM
	private final ArrayList<String> focusUISkinWidgetList = new ArrayList<String>(
			Arrays.asList(KONY_HBOX,KONY_VBOX,KONY_TAB_PANE,KONY_BUTTON,KONY_CALENDAR,KONY_CHECKBOX_GROUP,KONY_COMBO_BOX,KONY_DATA_GRID,KONY_LINK,KONY_LIST_BOX,KONY_RADIO_BUTTON_GROUP,KONY_TEXT_AREA,KONY_TEXT_BOX,KONY_CAMERA,KONY_HZ_IMAGE_STRIP,KONY_IMAGE_GALLERY,KONY_OBJ_SELECTOR_3D,KONY_PHONE,KONY_PICKER_VIEW,KONY_SEGMENT));
	
	public ArrayList<String> getMasterDataApplicableList() {
		return masterDataApplicableList;
	}
	
	public void addI18Key(String i18Key) {
		if (null == i18Keys) {
			i18Keys = new ArrayList<String>();
		}
		i18Keys.add(i18Key);
	}
	
	public List<String> getI18Keys() {
		return i18Keys;
	}

	public String getI18Title() {
		return i18Title;
	}

	public String getI18PlaceHolder() {
		return i18PlaceHolder;
	}
	
	public void setI18Title(String i18Title) {
		this.i18Title = i18Title;
	}

	public void setI18PlaceHolder(String i18PlaceHolder) {
		this.i18PlaceHolder = i18PlaceHolder;
	}
	
	public String getWidgetName() {
		return widgetName;
	}
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	public String getWidgetText() {
		return widgetText;
	}

	public void setWidgetText(String widgetText) {
		this.widgetText = widgetText;
	}
	
	public void addWidgetText(String widgetText) {
		
		if (null == widgetTextList) {
			widgetTextList = new ArrayList<String>();
		}
		widgetTextList.add(widgetText);
	}
	
	public List<String> getWidgetTextList() {
		return widgetTextList;
	}

	public String getWidgetType() {
		return widgetType;
	}
	public void setWidgetType(String widgetType) {
		this.widgetType = widgetType;
		updateWidgetType();
	}

	public Map<String, String> getPropertiesMap() {
		return propertiesMap;
	}
	public void setProperty(String key, String value) {
		this.propertiesMap = propertiesMap;
	}

	public List<WidgetVo> getChildWidgets() {
		return childWidgets;
	}
	public void setChildWidgets(List<WidgetVo> childWidgets) {
		this.childWidgets = childWidgets;
	}

	public void addChildWidget(WidgetVo widgetVo) {
		
		if (this.childWidgets == null) {
			this.childWidgets = new ArrayList<WidgetVo>();
		}
		
		this.childWidgets.add(widgetVo);
	}
	
	public boolean isContainerWidget() {
		
		if (widgetType == null) {
			isContainerWidget = false;
		}
		return isContainerWidget;
	}

	public boolean isI18KeysApplicable() {
		
		if (widgetType == null) {
			isI18KeysApplicable = false;
		}
		return isI18KeysApplicable;
	}

	public boolean isI18TitleApplicable() {
		
		if (widgetType == null) {
			isI18TitleApplicable = false;
		}
		return isI18TitleApplicable;
	}

	public boolean isI18PlaceHolderApplicable() {
		
		if (widgetType == null) {
			isI18PlaceHolderApplicable = false;
		}
		return isI18PlaceHolderApplicable;
	}
	
	public boolean isBlockUISkinApplicable() {
		
		if (widgetType == null) {
			isBlockUISkinApplicable = false;
		}
		return isBlockUISkinApplicable;
	}
	
	public boolean isFocusUISkinApplicable() {
		
		if (widgetType == null) {
			isFocusUISkinApplicable = false;
		}
		return isFocusUISkinApplicable;
	}

	public boolean isWidgetTextApplicable() {
		
		if (widgetType == null) {
			isWidgetTextApplicable = false;
		}

		return isWidgetTextApplicable;
	}

	private void updateWidgetType() {
		if (null == widgetType || widgetType.equals(NONE)) {
			return;
		}
		
		/*
		if (widgetType.equals("kObjectSelector3D")) {
			System.out.println("Checking");
		}
		*/
		
		if (containerWidgetsMasterList.contains(widgetType)) {
			isContainerWidget = true;
		} else {
			isContainerWidget = false;
		}
		
		if (i18KeyList.contains(widgetType)) {
			isI18KeysApplicable = true;
		} else {
			isI18KeysApplicable = false;
		}

		if (i18PlaceHolderList.contains(widgetType)) {
			isI18PlaceHolderApplicable = true;
		} else {
			isI18PlaceHolderApplicable = false;
		}
		
		if (i18TitleList.contains(widgetType)) {
			isI18TitleApplicable = true;
		} else {
			isI18TitleApplicable = false;
		}
		
		if (blockUISkinWidgetList.contains(widgetType)) {
			isBlockUISkinApplicable = true;
		} else {
			isBlockUISkinApplicable = false;
		}
		
		if (focusUISkinWidgetList.contains(widgetType)) {
			isFocusUISkinApplicable = true;
		} else {
			isFocusUISkinApplicable = false;
		}
		
		isWidgetTextApplicable = newList.contains(widgetType) || masterDataApplicableList.contains(widgetType);
		

	}
	
	public boolean isOnClickApplicable() {
		return isOnClickApplicable;
	}

	public void setOnClickApplicable(boolean isOnClickApplicable) {
		this.isOnClickApplicable = isOnClickApplicable;
	}

	public boolean isSecureSubmitEnabled() {
		return isSecureSubmitEnabled;
	}

	public void setSecureSubmitEnabled(boolean isSecureSubmitEnabled) {
		this.isSecureSubmitEnabled = isSecureSubmitEnabled;
	}
	
	public boolean isIdleTimeOutEnabled() {
		return isIdleTimeOutEnabled;
	}

	public void setIdleTimeOutEnabled(boolean isIdleTimeOutEnabled) {
		this.isIdleTimeOutEnabled = isIdleTimeOutEnabled;
	}
	
	public String getOnClickEventString() {
		return onClickEventString;
	}

	public void setOnClickEventString(String onClickEventString) {
		this.onClickEventString = onClickEventString;
	}
}
