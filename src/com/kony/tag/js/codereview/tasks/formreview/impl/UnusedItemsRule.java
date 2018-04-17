package com.kony.tag.js.codereview.tasks.formreview.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;
import com.kony.tag.js.codereview.config.FunctionVo;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedSkinsRule;
import com.kony.tag.util.RuleFactory;

public class UnusedItemsRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.misc.all.xyz.unused_items";
	
	public static final String PROPS_RULE_NAME_UNUSED_FUNCTIONS = "rules.misc.all.001.unused_js_functions_rule";
	public static final String PROPS_RULE_NAME_UNUSED_SKINS = "rules.misc.all.002.unused_skins_rule";
	public static final String PROPS_RULE_NAME_UNUSED_IMAGES = "rules.misc.all.003.unused_images_rule";
	public static final String PROPS_RULE_NAME_UNUSED_I18 = "rules.misc.all.004.unused_i18_rule";
	
	
	public static final String RULE_ID_UNUSED_FUNCTIONS = "MISC-001";
	public static final String RULE_ID_UNUSED_SKINS = "MISC-002";
	public static final String RULE_ID_UNUSED_IMAGES = "MISC-003";
	public static final String RULE_ID_UNUSED_I18_KEYS = "MISC-004";
	
	private static final String ERR_UNUSED_FUNCTION = "Unused Function";
	private static final String ERR_UNUSED_SKIN = "Unused Skin";
	private static final String ERR_UNUSED_IMAGE = "Unused Image. (Delete corresponding Retina display images as well)";
	private static final String ERR_UNUSED_I18_KEY = "Unused I18 Key";
	
	public static final String ERR_SHORT_DESC_FUNCTIONS = "Unused Function";
	public static final String ERR_SHORT_DESC_SKINS = "Unused Skin";
	public static final String ERR_SHORT_DESC_IMAGES = "Unused Image";
	public static final String ERR_SHORT_DESC_I18 = "Unused I18 Key";
	
	private List<FunctionVo> _unusedFunctions = null;
	private List<FunctionVo> tmpUsedFunctionsList = null;
	private int _minFunctionLength = 0;
	
	private List<String> _unusedSkins = null;
	private List<String> tmpUsedSkinsList = null;
	private int _minSkinLength = 0;
	
	private List<String> _unusedImages = null;
	private List<String> tmpUsedImagesList = null;
	private int _minImageLength = 0;
	
	private List<String> _unusedI18Keys = null;
	private List<String> tmpUsedI18KeysList = null;
	private int _minI18KeyLength = 0;

	private final char[] _acceptedPreChars = new char[]{CHAR_EQUALS,CHAR_COLON,CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE,CHAR_EXCLAMATION};
	private final char[] _acceptedPostChars = new char[]{CHAR_CURLY_CLOSE,CHAR_CURLY_OPEN, CHAR_SEMI_COLON,CHAR_COMMA,CHAR_DOT,CHAR_PARANTHESIS_OPEN,CHAR_PARANTHESIS_CLOSE,CHAR_DOUBLE_QUOTE,CHAR_SINGLE_QUOTE};
	
	private final List<String> WIDGET_EVENT_FUNCTIONS = Arrays.asList(".addWidgets",".init",".onBeginEditing",
			".onCapture",".onClick",".onDeviceBack",".onDeviceMenu",".onDone",".onDownloadComplete",".onEditing",
			".onEndEditing",".onFailure",".onHide",".onLoadJS",".onOrientationChange",".onPinClick",".onRowClick",
			".onRowSelected",".onSelection",".onSelectionDone",".onSlide",".onSuccess",".onSwipe",".onTabClick",
			".onTextChange",".postOnclickJS",".postShow",".postluajs",".preOnclickJS",".preShow",".preluajs",
			".scrollingEvents",".unLoadJS");

	public List<FunctionVo> getUnusedFunctions() {
		return _unusedFunctions;
	}
	
	public List<String> getUnusedSkins() {
		return _unusedSkins;
	}
	
	public List<String> getUnusedImages() {
		return _unusedImages;
	}
	
	public UnusedItemsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		super.init(null,null,null);
		//String[] rulesDesc1 = {ERR_SHORT_DESC_FUNCTIONS};
		//super.init(PROPS_RULE_NAME_UNUSED_FUNCTIONS,RULE_ID_UNUSED_FUNCTIONS,rulesDesc1);
		//String[] rulesDesc2 = {ERR_SHORT_DESC_I18};
		//super.init(PROPS_RULE_NAME_UNUSED_I18,RULE_ID_UNUSED_I18_KEYS,rulesDesc2);
		//String[] rulesDesc3 = {ERR_SHORT_DESC_IMAGES};
		//super.init(PROPS_RULE_NAME_UNUSED_IMAGES,RULE_ID_UNUSED_IMAGES,rulesDesc3);
		//String[] rulesDesc4 = {ERR_SHORT_DESC_SKINS};
		//super.init(PROPS_RULE_NAME_UNUSED_SKINS,RULE_ID_UNUSED_SKINS,rulesDesc4);

		rulesInit(null,null,null,null,null,null);
	}

	public void rulesInit(String reviewFileName, String reviewFileDirectoryName, List<FunctionVo> unusedFunctions , List<String> unusedSkins, List<String> unusedImages, List<String> unusedI18Keys) {
		setReviewFileDirectory(reviewFileDirectoryName);
		setReviewFile(reviewFileName);
		_unusedFunctions = unusedFunctions;
		_unusedSkins = unusedSkins;
		_unusedImages = unusedImages;
		_unusedI18Keys = unusedI18Keys;
		
		if (_unusedFunctions == null) {
			_unusedFunctions = new ArrayList<FunctionVo>();
		}
		if (tmpUsedFunctionsList == null) {
			tmpUsedFunctionsList = new ArrayList<FunctionVo>();
		}
		for (FunctionVo function : _unusedFunctions) {
			
			if (_minFunctionLength == 0 && function.getFunctionName().length()>0) {
				_minFunctionLength = function.getFunctionName().length();
			} else if (function.getFunctionName().length()<_minFunctionLength) {
				_minFunctionLength = function.getFunctionName().length();
				//System.out.println("MIN LEN FUNCTION : " +function + " : " +_minFunctionLength);
			}
		}
		//System.out.println("MIN FUNCTIONn LENGTH " + _minFunctionLength);

		if (_unusedSkins == null) {
			_unusedSkins = new ArrayList<String>();
		}
		if (tmpUsedSkinsList == null) {
			tmpUsedSkinsList = new ArrayList<String>();
		}
		
		for (String skinName : _unusedSkins) {
			
			if (_minSkinLength == 0 && skinName.length()>0) {
				_minSkinLength = skinName.length();
			} else if (skinName.length()<_minSkinLength) {
				_minSkinLength = skinName.length();
				//System.out.println("MIN LEN FUNCTION : " +function + " : " +_minFunctionLength);
			}
		}

		if (_unusedImages == null) {
			_unusedImages = new ArrayList<String>();
		}
		if (tmpUsedImagesList == null) {
			tmpUsedImagesList = new ArrayList<String>();
		}
		
		for (String imageName : _unusedImages) {
			
			if (_minImageLength == 0 && imageName.length()>0) {
				_minImageLength = imageName.length();
			} else if (imageName.length()<_minImageLength) {
				_minImageLength = imageName.length();
				//System.out.println("MIN LEN FUNCTION : " +function + " : " +_minFunctionLength);
			}
		}
		
		if (_unusedI18Keys == null) {
			_unusedI18Keys = new ArrayList<String>();
		}
		if (tmpUsedI18KeysList == null) {
			tmpUsedI18KeysList = new ArrayList<String>();
		}
		
		for (String keyName : _unusedI18Keys) {
			
			if (_minI18KeyLength == 0 && keyName.length()>0) {
				_minI18KeyLength = keyName.length();
			} else if (keyName.length()<_minI18KeyLength) {
				_minI18KeyLength = keyName.length();
				//System.out.println("MIN LEN FUNCTION : " +function + " : " +_minFunctionLength);
			}
		}		
	}
	
	protected void review(WidgetVo widgetVo) throws TagToolException {
		// Nothing to do
	}
	
	public void checkForUsedFunctions(String attribute) {
		
		/*
		if (attribute != null && attribute.indexOf("reSendVerificationCode") >=0) {
			System.out.println("CHECKING");
		}
		*/
		
		if (attribute == null || attribute.trim().length()<_minFunctionLength) {
			// No need of review
			return;
		}
		
		tmpUsedFunctionsList.clear();
		
		for (FunctionVo function : _unusedFunctions) {
			if (getKeywordIndex(attribute, function.getFunctionName(), _acceptedPreChars, _acceptedPostChars, false)>=0) {
				tmpUsedFunctionsList.add(function);
			}
		}

		if (tmpUsedFunctionsList.size()>0) {
			for (FunctionVo function : tmpUsedFunctionsList) {
				_unusedFunctions.remove(function);
			}
		}

		tmpUsedFunctionsList.clear();
	}
	
	public void checkForUsedSkins(String attribute) {
		
		/*
		if (attribute != null && attribute.indexOf("reSendVerificationCode") >=0) {
			System.out.println("CHECKING");
		}
		*/
		
		if (attribute == null || attribute.trim().length()<_minSkinLength) {
			// No need of review
			return;
		}
		
		tmpUsedSkinsList.clear();
		
		for (String skinName : _unusedSkins) {
			if (getKeywordIndex(attribute, skinName, _acceptedPreChars, _acceptedPostChars, false, true)>=0) {
				tmpUsedSkinsList.add(skinName);
			}
		}

		if (tmpUsedSkinsList.size()>0) {
			for (String skinName : tmpUsedSkinsList) {
				_unusedSkins.remove(skinName);
			}
		}

		tmpUsedSkinsList.clear();
	}
	
	public void checkForUsedImages(String attribute) {
		
		/*
		if (attribute != null && attribute.indexOf("reSendVerificationCode") >=0) {
			System.out.println("CHECKING");
		}
		*/
		
		if (attribute == null || attribute.trim().length()<_minImageLength) {
			// No need of review
			return;
		}
		
		tmpUsedImagesList.clear();
		
		for (String imageName : _unusedImages) {
			if (getKeywordIndex(attribute, imageName, _acceptedPreChars, _acceptedPostChars, false, true)>=0) {
				tmpUsedImagesList.add(imageName);
			}
		}

		if (tmpUsedImagesList.size()>0) {
			for (String imageName : tmpUsedImagesList) {
				_unusedImages.remove(imageName);
			}
		}

		tmpUsedImagesList.clear();
	}	
	
	public void checkForUsedI18Keys(String attribute) {
		
		/*
		if (attribute != null && attribute.indexOf("reSendVerificationCode") >=0) {
			System.out.println("CHECKING");
		}
		*/
		
		if (attribute == null || attribute.trim().length()<_minI18KeyLength) {
			// No need of review
			return;
		}
		
		tmpUsedI18KeysList.clear();
		
		for (String keyName : _unusedI18Keys) {
			if (getKeywordIndex(attribute, keyName, _acceptedPreChars, _acceptedPostChars, false, true)>=0) {
				tmpUsedI18KeysList.add(keyName);
			}
		}

		if (tmpUsedI18KeysList.size()>0) {
			for (String keyName : tmpUsedI18KeysList) {
				_unusedI18Keys.remove(keyName);
			}
		}

		tmpUsedI18KeysList.clear();
	}
	
	
	public void updateUnusedFunctions() throws TagToolException {
		getComments().clear();
		boolean skipInd = false;
		
		setReviewFileDirectory(LOCATION_JS_MODULES);
		
		if (_unusedFunctions != null && _unusedFunctions.size()>0) {
			for (FunctionVo function : _unusedFunctions) {
				
				skipInd = false;
				for (String keyWord : WIDGET_EVENT_FUNCTIONS) {
					if (function.getFunctionName().indexOf(keyWord)>=0) {
						// FUNCTION NAME contains key word .. SKIP
						skipInd = true;
						break;
					}
				}
				
				if (skipInd) {
					continue;
				}
				
				addError(ERR_UNUSED_FUNCTION, ERR_SHORT_DESC_FUNCTIONS, function.getFunctionName(), function.getFileName(), function.getLineNumber(), SEV_MED, RULE_ID_UNUSED_FUNCTIONS);
			}
		}
	}
	
	public void updateUnusedSkins() throws TagToolException {
		getComments().clear();
		
		setReviewFileDirectory(DEFAULT_THEMES_PATH+getProjectConfig().getCodeReviewProperty(PROP_DEFAULT_THEME_FOR_SKINS));
		
		if (_unusedSkins != null && _unusedSkins.size()>0) {
			for (String skinName : _unusedSkins) {
				addError(ERR_UNUSED_SKIN, ERR_SHORT_DESC_SKINS, skinName, DEFAULT_SKINS_FILE, 0, SEV_MED, RULE_ID_UNUSED_SKINS);
			}
		}
	}
	
	public void updateUnusedImages() throws TagToolException {
		getComments().clear();
		setReviewFileDirectory(PROJECT_RESOURCES_PATH);
		
		// Account for the image names used in Skins. remove them from the unused list
		
		if (_unusedImages != null && _unusedImages.size()>0) {
			for (String imageName : _unusedImages) {
				addError(ERR_UNUSED_IMAGE, ERR_SHORT_DESC_IMAGES, BLANK, imageName, 0, SEV_MED, RULE_ID_UNUSED_IMAGES);
			}
		}
	}
	
	public void updateUnusedI18Keys() throws TagToolException {
		getComments().clear();
		
		setReviewFileDirectory(DEFAULT_I18_PROPS_PATH);
		
		if (_unusedI18Keys != null && _unusedI18Keys.size()>0) {
			for (String keyName : _unusedI18Keys) {
				addError(ERR_UNUSED_I18_KEY, ERR_SHORT_DESC_I18, keyName, BLANK, 0, SEV_MED, RULE_ID_UNUSED_I18_KEYS);
			}
		}
	}
	
	
	public void postReview() {
		checkImagesUsedInProjectSkins();
	}
	
	private  void checkImagesUsedInProjectSkins() {
		UnusedSkinsRule unusedSkinsRule = null;
		
		try {
			unusedSkinsRule = (UnusedSkinsRule) RuleFactory.getCodeReviewRuleInstance(UnusedSkinsRule.PROPS_RULE_NAME,getProjectConfig());
		}catch (TagToolException excp) {
			CodeReviewStatus.getInstance().addErrorMessage(excp, excp.getErrorMessage());
		}
		
		if (null == unusedSkinsRule || unusedSkinsRule.getImagesUsedInSkins()==null || unusedSkinsRule.getImagesUsedInSkins().size()==0) {
			return;
		}
		
		List<String> lUnusedSkinList = unusedSkinsRule.getAllUnusedSkins();	
		
		Set<String> lKeySet = unusedSkinsRule.getImagesUsedInSkins().keySet();
		if(lKeySet != null)
		{
			Iterator<String> lKeyIter = lKeySet.iterator();
			while(lKeyIter.hasNext())
			{
				String lImageName = lKeyIter.next();
				List<String> lSkinList = unusedSkinsRule.getImagesUsedInSkins().get(lImageName);
				
				if(!lUnusedSkinList.containsAll(lSkinList))
				{
					if (_unusedImages.contains(lImageName)) {
						//System.out.println("*********************************" + lImageName + " *** " + lSkinList.toString());
						_unusedImages.remove(lImageName);
					}
				}
			}
		}
		
		/*for (String imageName : unusedSkinsRule.getImagesUsedInSkins()) {
			if (_unusedImages.contains(imageName)) {
				_unusedImages.remove(imageName);
			}
		}*/
	}
}