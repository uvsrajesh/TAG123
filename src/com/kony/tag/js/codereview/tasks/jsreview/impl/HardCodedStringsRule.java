package com.kony.tag.js.codereview.tasks.jsreview.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.config.JSONObject;
import com.kony.tag.js.codereview.config.KeyValuePair;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.util.JSONUtil;
import com.kony.tag.tasks.JsReviewMasterTask;

public class HardCodedStringsRule extends JsReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.js.002.hardcoded_strings_rule";
	public static final String RULE_ID = "JS-002";
	public static final String ERR_HARDCODED_STRING = "Avoid hard coding for better portability. Consider using I18 resource bundle or a properties file";
	private static final String ERR_SHORT_DESC = "Hardcoded Data found";
	
	public static List<String> HARDCODING_VALID_KEYS = null;
	
	private final String KEYWORD_TYPE_SRC = "src";
	private final String KEYWORD_I18N_STRING = ".getLocalizedString";
	
	private final static String I18 = "i18";
	private final static String MIME = "mime";

	
	private static String[] HARDCODING_FORBIDDEN_KEYWORDS = null;
	private static Map<String, Boolean> HARDCODING_PERMITTED_KEYWORDS = null;
	private static final String RETURN = "return";
	private char[] _chars = null;
	private int _openParanthesisCount = 0;
	private StringBuffer _reviewString = null;
	private StringBuffer _jsonReviewString = null;
	private static JSONUtil _jsonUtil = null;
	private int _openCurlyBraceCount = 0;
	private StringBuffer _jsonKey = null;
	private boolean _skipHardCodeReport = false;
	
	@Override
	protected boolean isLineSplitNeeded(){return true;}

	public HardCodedStringsRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		//super.init(PROPS_RULE_NAME, 1);
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions,1);
		
		init();
		_jsonUtil = new JSONUtil();
		initKeyWords();		
	}
	
	public void init() {
		_openParanthesisCount = 0;
		_openCurlyBraceCount = 0;
		_reviewString = new StringBuffer();		
		_jsonReviewString = new StringBuffer();
		_jsonKey = new StringBuffer();
	}

	@Override
	protected List<ReviewComment> reviewSingleJSLine(String fileName, int lineNum, int lastLineNum,
			String line, int passNumber, String currFucntionName, boolean functionEndFlag ,int prevCommentedLinesCount) throws TagToolException {

		StringBuffer sameLineReviewString = new StringBuffer();
		
		/*
		final String CHECK_STRING = "frmGMACCTransactionDetails.segTransactionDetails.widgetDataMap = {";
		if (line.indexOf(CHECK_STRING)>=0) {
			System.out.println("CHECKING : ");
			System.out.println(line);
		}
		*/
		
		// populate _jsonReviewString for CATEGORY 3 review
		
		// Get the String to be reviewed for hard coded values. Get the data between delimiters for review
		_chars = line.toCharArray();
		boolean isDelimiter = false;
		
		for (char c : _chars) {
			
			isDelimiter = false;

			// Following line of code is specific to verifying catgeory-3 hard coded string
			if (c == CHAR_CURLY_OPEN) {
				_openCurlyBraceCount++;
				
				if (null != _jsonKey && _jsonKey.length()>0 && _jsonKey.indexOf(":") <0) {
					// previous string not a valid JSON String. start fresh
					_openCurlyBraceCount = 1;
				}
				
				if (_openCurlyBraceCount == 1 && null != _jsonReviewString && _jsonReviewString.length()>0) {
					// Start fresh review string
						_jsonReviewString = new StringBuffer();
				}
			} else if (c==CHAR_CURLY_CLOSE) {
				_openCurlyBraceCount--;
				if (null != _jsonKey && _jsonKey.length()>0 && _jsonKey.indexOf(":") <0) {
					// previous string not a valid JSON String. start fresh
					_openCurlyBraceCount = 0;
				}
			}
			
			// If line ends - start fresh !
			if (c == CHAR_SEMI_COLON) {
				_openCurlyBraceCount = 0;
				if (null != _jsonReviewString && _jsonReviewString.length()>0) {
					_jsonReviewString = new StringBuffer();
				}
				if (null != _jsonKey && _jsonKey.length()>0) {
					_jsonKey = new StringBuffer();
				}
			}
			
			if (_openCurlyBraceCount < 0) {
				_openCurlyBraceCount = 0;
			} else {
				//System.out.println("NEGATIVE COUNT: "  + fileName + " : " + lineNum + " : " + line);
			}
			
			if (_openCurlyBraceCount > 0) {
				_jsonReviewString.append(c);
				_jsonKey.append(c);
				if(_jsonReviewString.length() == 1 && line.indexOf(".widgetDataMap") >=0) {
					_skipHardCodeReport = true;
				} 
			} else if (_openCurlyBraceCount == 0) {
				
				if (null != _jsonReviewString && _jsonReviewString.length()>0) {
					_jsonReviewString.append(CHAR_CURLY_CLOSE);
					
					if (_jsonReviewString.indexOf(":") >=0) {
						//System.out.println("JSON STRING: " + fileName + " : " + lineNum + " : " + _jsonReviewString);
						if(!_skipHardCodeReport) {
							checkCategory3HardCoding(_jsonReviewString.toString(), fileName, lineNum);
						}
						_skipHardCodeReport = false;
					}
					_jsonReviewString = new StringBuffer();
				}				
				
				/*
				if (null != _jsonReviewString && _jsonReviewString.length()>0 && _jsonReviewString.indexOf(":") >=0) {
					System.out.println("JSON STRING: " + fileName + " : " + lineNum + " : " + _jsonReviewString);
				}
				*/
				
				/*
				if ((_jsonReviewString.indexOf(":{") >=0) || (_jsonReviewString.indexOf(":[")>=0)) {
						System.out.println("JSON STRING: " + fileName + " : " + lineNum + " : " + _jsonReviewString);
				} 
				*/
				
			}

			if (c == CHAR_CURLY_CLOSE || c == CHAR_CURLY_OPEN) {
				if (null != _jsonKey && _jsonKey.length()>0) {
					_jsonKey = new StringBuffer();
				}
			}
			
			// Following line of code is specific to verifying catgeory-1 hard coded string
			if (c == CHAR_PARANTHESIS_OPEN) {
				_openParanthesisCount++;
			} else if (c==CHAR_PARANTHESIS_CLOSE) {
				_openParanthesisCount--;
			}
			
			if (_openParanthesisCount > 0) {
				// No need to count chars between paranthesis
				continue;
			}

			// just this line of code is specific to verifying catgeory-2 hard coded string. rest of the code below is again for category-1
			sameLineReviewString.append(c);
			
			if (( c== CHAR_SEMI_COLON) || ( c== CHAR_CURLY_CLOSE) || (c== CHAR_CURLY_OPEN)) {
				isDelimiter = true;
			}
			
			if (isDelimiter) {
				
				// Need not add this char to the Review String
				if (_reviewString != null && _reviewString.length()>0) {
					
					// This review string does not have any hard coded strings enclosed in paranthesis
					checkCategory1HardCoding(_reviewString.toString(), fileName, lineNum);
				}
				_reviewString = new StringBuffer();
				continue;
			}
			
			// Add this character to the String to be reviewed. 
			_reviewString.append(c);
			
		}
		
		checkCategory2HardCoding(sameLineReviewString.toString(), fileName, lineNum);
		
		// additional rules for all categories
		///be careful with info:{key:"TabPane"}, widgetDataMap:{widgetId1:"dataid1"} - hardcoding is valid even if the key values are not keywords !
		//ignore if the hardcoded string contains image names like .png - check all supported image formats - OK
		//take care: single quote vs double quote - both are valid - OK
		// Ignore all hardcoded strings which are "" or empty - OK
		//Ignore all hardcoded strings within paranthesis(""); - OK		
		
		// check for category-4 hardcoding
		// "return" string should not have any hard codes

		return getComments();
	}

	private void checkCategory2HardCoding(String line, String fileName, int lineNum) throws TagToolException {
		// check for category-2 hardcoding. Multiple assignments in a single line
		// Eg: GMACC_accountSearchUI.js : 181 : Hard Coded data found : 			frmGMACCSearchMain.lblDateValue.text = gblGMSelectedSliderDate = getCurrentDate ? "Today" : gblGMSelectedSliderDate
		// Eg: GMKANA_landing.js : 981 : Hard Coded data found : 	        frmGMKNAHome[id].text = dateToSet = day + "/" + month + "/" + year
		
		List<String> reviewLines = new ArrayList<String>();
		int currIndex = 0;
		int prevIndex = 0;
		
		List<Integer> splitList = getLineSplitList(line, CHAR_SEMI_COLON);
		
		if (null == splitList || splitList.size() == 0) {
			// Nothing to check
			return;
		}		
		
		prevIndex = 0;
		for (int i=0;i<splitList.size();i++) {
			currIndex = splitList.get(i).intValue();
			reviewLines.add(line.substring(prevIndex,currIndex));
			prevIndex = currIndex;
		}
		
		if (line.length()>(prevIndex+1)) {
			reviewLines.add(line.substring(prevIndex+1));
		}
		
		for (String reviewLine : reviewLines) {
			checkCategory2Line(reviewLine,fileName, lineNum);

			// Check if the there are any hard coded return statements
			checkHardCodedReturnStatements(reviewLine,fileName,lineNum);
			
		}
		
	}
	
	private void checkCategory2Line(String line, String fileName, int lineNum) throws TagToolException {
		String[] reviewLines = line.split(EQUALS);
		String value = null;
		String key = null;
		boolean isHardCoded = false;
		boolean isHarcodingViolation = false;
		boolean isRecorded = false;

		if (null == reviewLines || reviewLines.length < 3) {
			// Not this category
			return;
		}

		value = reviewLines[(reviewLines.length-1)];
		isHardCoded = isThereHardCodedString(value);
		
		if (!isHardCoded) {
			// Nothing of interest
			return;
		}
 		
		for (int i=0; i<(reviewLines.length-1); i++) {
			key = reviewLines[i];
			isHarcodingViolation = isHardcodingForbidden(key,value);
			if (isHarcodingViolation) {
				// Add a comment for this line

				addError(ERR_HARDCODED_STRING,ERR_SHORT_DESC,line.trim(), 
					fileName, lineNum, SEV_HIGH, RULE_ID);
				isRecorded = true;
				break;
			}
		}
		
		if (!isRecorded) {
			// Add a comment for this line
			addError(ERR_HARDCODED_STRING,ERR_SHORT_DESC, line.trim(),
				fileName, lineNum, SEV_HIGH, RULE_ID);
			
		}
	}
	
	private void checkCategory1HardCoding(String line,String fileName, int lineNum) throws TagToolException {
		// check for category-1 hardcoding
		//check for "hardcodes" if the statement block contains the following (hardcode should be to the right of the "=" sign":.
		//check for hardcoded numbers in the arrays/objects that follow after the "=" symbol when these key words are used:ewg: .text or ["text"]
		
		// This cannot check for the following type:
		// GMACC_accountSearchUI.js : 181 : Hard Coded data found : 			frmGMACCSearchMain.lblDateValue.text = gblGMSelectedSliderDate = getCurrentDate ? "Today" : gblGMSelectedSliderDate
		// GMKANA_landing.js : 981 : Hard Coded data found : 	        frmGMKNAHome[id].text = dateToSet = day + "/" + month + "/" + year

		int index = 0;
		String key = null;
		String value = null;
		String reviewLine = line;
		boolean isHardCoded = false;
		index = reviewLine.indexOf(CHAR_EQUALS);
		int index2=0;

		if (index <0) {
			// Nothing to review
			return;
		}
		
		List<Integer> splitList = getLineSplitList(reviewLine, CHAR_EQUALS);
		
		if (null == splitList || splitList.size() == 0) {
			// Nothing to check
			return;
		}
		
		for (int i=0; i<splitList.size(); i++) {
			//key = reviewPairs[i];
			//value = reviewPairs[i+1];
			index = splitList.get(i).intValue();
			key = reviewLine.substring(0,(index+1));
			if (splitList.size()>(i+1)) {
				index2 = splitList.get(i+1).intValue();
				value = reviewLine.substring((index+1),(index2+1));
			} else {
				value = reviewLine.substring(index+1);
			}
			
			isHardCoded = false;
			
			if (isThereHardCodedString(value)) {
				
				isHardCoded = isHardcodingForbidden(key,value);
				
				if (isHardCoded) {
					// Add a comment for this line
					addError(ERR_HARDCODED_STRING, ERR_SHORT_DESC,line.trim(),
						fileName, lineNum, SEV_HIGH, RULE_ID);
					break;
				}
			}
			if (isHardCoded) {
				// already recorded as hard coded string.. continue with next line
				break;
			}
			
		}			
	}
	
	private List<Integer> getLineSplitList(String reviewLine, char splitChar) {
		List<Integer> numberList = new ArrayList<Integer>();
		int singleQuoteCount = 0;
		int doubleQuoteCount = 0;
		boolean isWithinDoubleQuotes = false;
		boolean isWithinSingleQuotes = false;
		boolean isWithinQuotes = false;
		char[] mychars = reviewLine.toCharArray();
		char c;
		
		if (reviewLine == null || reviewLine.trim().length() == 0) {
			return null;
		}
		
		for (int i=0;i<mychars.length;i++) {
			c = mychars[i];
			
			if (c == CHAR_SINGLE_QUOTE && !isWithinDoubleQuotes) {
				singleQuoteCount++;
			}
			if (c == CHAR_DOUBLE_QUOTE && !isWithinSingleQuotes) {
				doubleQuoteCount++;
			}
			
			if ((singleQuoteCount%2)==1) {
				isWithinSingleQuotes = true;
			}else {
				isWithinSingleQuotes = false;
			}

			if ((doubleQuoteCount%2)==1) {
				isWithinDoubleQuotes = true;
			}else {
				isWithinDoubleQuotes = false;
			}
			
			if (isWithinSingleQuotes || isWithinDoubleQuotes) {
				isWithinQuotes = true;
			} else {
				isWithinQuotes = false;
			}
			
			if (c == splitChar && !isWithinQuotes) {
				// Line can be split.
				numberList.add(new Integer(i));
			}
		}
		
		return numberList;
	}
	
	private boolean isThereHardCodedString(String reviewString) {
		boolean hardCodingStart = false;
		boolean hardCodingEnd = false;
		char hardCodingStartChar = 0;
		StringBuffer hardCodedString = null; 
		boolean isArgument = false;
		boolean isImage = false;
		
		char[] reviewChars = reviewString.toCharArray();
		
		for (char c : reviewChars) {
			
			if (c==CHAR_SQUARE_OPEN) {
				isArgument = true;
			} else if (c==CHAR_SQUARE_CLOSE) {
				isArgument = false;
			}
			
			if ((c == CHAR_SINGLE_QUOTE)  || (c==CHAR_DOUBLE_QUOTE)) {
				
				if (!hardCodingStart) {
					hardCodingStartChar = c;
					hardCodingStart = true;
					hardCodedString = new StringBuffer();
				} else if (c == hardCodingStartChar){
					hardCodingEnd = true;
				}
			}
			
			if (hardCodingStart && !hardCodingEnd && c != hardCodingStartChar && !isArgument) {
				hardCodedString.append(c);
			}
			
			if (hardCodingStart && hardCodingEnd) {
				if ((hardCodedString != null && hardCodedString.toString().trim().length()>0)
						&& !isHardCodedImage(hardCodedString.toString())) {
					break;
				} else {
					// start fresh search for a hard coded string
					hardCodingStart = false;
					hardCodingEnd = false;
					hardCodingStartChar = 0;
				}
			}
		}
		
		if (hardCodingStart && hardCodedString != null && hardCodedString.toString().trim().length()>0) {
			return true;
		} else {
			return false;
		}
		//return (hardCodingStart && hardCodingEnd);
}

	private boolean isHardCodedImage(String reviewStr) {
		boolean isLocalImageFile = false;
		
		if (null == reviewStr || reviewStr.length()==0) {
			return isLocalImageFile;
		}
		String tmpStr = reviewStr.trim().toLowerCase();
		if (tmpStr.indexOf(HTTP)>=0) {
			isLocalImageFile = false;
		} else if (tmpStr.indexOf(FILE_TYPE_PNG) >=0 || tmpStr.indexOf(FILE_TYPE_GIF)>=0 ||
				tmpStr.indexOf(FILE_TYPE_JPEG) >=0 || tmpStr.indexOf(FILE_TYPE_JPG)>=0) {
			isLocalImageFile = true;
		}
		
		return isLocalImageFile;
	}
	private void initKeyWords()  {
		
		if (HARDCODING_FORBIDDEN_KEYWORDS != null && HARDCODING_PERMITTED_KEYWORDS != null) {
			return;
		}
		
		HARDCODING_FORBIDDEN_KEYWORDS = new String[]
		{"text","rawBytes","externalURL","submitURL","base64","src","title","placeholder",
				"htmlString","leftSideText","rightSideText","source"
		};
		
		Map<String, Boolean> forbiddenKeyMap = new HashMap<String, Boolean>();
		for (String val: HARDCODING_FORBIDDEN_KEYWORDS) {
			forbiddenKeyMap.put(val.toLowerCase(),Boolean.TRUE);
		}
		
		String[] hardCodingPermittedWords = new String[]
		{"Event","Focus","Icon","Id","No","Platform","Properties","Render","Rows","Skin","Specific","Title","WidgetDirection","accessMode","activeFocusSkin","activeSkin","activeTabs","add","addAll","addAppMenuItemAt","addAt","addDataAt","addModel","addSectionsAt","addTab","addTabAt","addWidgets","address","alternateRowSkin","animateHeaderFooter","appID","applyCellSkin","applySkinsToPopup","arrowConfig","autoCapitalize","autoComplete","autoCorrect","autoFilter","autofocus","base64","blockedUISkin","border","borderCollapse","bounces","calenderIcon","calloutTemplate","calloutWidth","canGoBack","canGoForward","captureGPS","captureOrientation","clearAllData","clearHistory","closeButtonText","closureLeftSideView","closureRightSideView","columnHeadersConfig","compressionLevel","containerHeight","containerHeightReference","containerWeight","containerWidget","contentAlignment","contextMenu","contextPath","controls","createAppMenu","date","dateComponents","dateFormat","day","defaultPinImage",
				"defaultSelection","defineSpecialModels","destroy","detectTelNumber","dismiss","displayOrientation","displayText","dockableAppMenu","dockableFooter","dockableHeader","dropDownImage","dropdownImage","editStyle","editable","enableDictionary","enableOverlay","enablePhotoCropFeature","enableRangeOfDates","enableScrollByPage","enableZoom","enableback","enabledForIdleTimeout","enableonscreenformmenu","enableonscreenmenu","externalURL","filterlist","fixedHeight","fixedHeightRow","focusImage","focusIndex","focusItem","focusSkin","focusThumbImage","focusTickedImage","focusUnTickedImage","focusimage","footerOverlap","footers","formTransperencyDuringPostShow","format","formattedDate","frameHeight","getAppMenuBadgeValue","getCurrentAppMenu","getSelectedCells","glossyEffect","glowEffect","goBack","goForward","gridHeight","gridlineColor","groupCells","hExpand","headerOverlap","headerSkin","headers","height","heightReference","hideTitleBar","hour","htmlString","httpheaders","id","image","imageFormat",
				"imageLeftSideView","imageRightSideView","imageScaleMode","imageWhenFailed","imageWhileDownloading","imagewhileDownloading","inTransitionConfig","inactiveSkin","indicator","info","init","inputAccessoryViewType","inputMode","inputStyle","isModal","isMultiSelect","isVisible","itemOrientation","itemsPerRow","keyBoardStyle","keyBoardType","keyboardActionLabel","layoutAlignment","leftSideText","leftSkin","leftViewImage","linkFocusSkin","linkSkin","locationData","mapHeight","mapKey","mapSource","mapWidth","margin","marginInPixel","masterdataload","max","maxLabel","maxLabelSkin","maxSideOfTheImage","maxTextLength","maxValueImage","menuFocusSkin","menuItems","menuNormalSkin","menuPosition","menuRenderer","min","minLabel","minLabelSkin","minValueImage","minutes","mode","month","monthI18Nkey","multiple","nativeListFieldFocusSkin","nativeListFieldSkin","nativeUserInterface","navControlsImageConfig","navigateToLocation","navigationBarPosition","navigationSkin","needAppMenu",
				"needPageIndicator","needSectionHeader","needsIndicatorDuringPostShow","nift","noCache","normalImage","normalimage","numberOfVisibleLines","of","onBeginEditing","onCapture","onClick","onDeviceBack","onDeviceMenu","onDone","onDownloadComplete","onEditing","onEndEditing","onFailure","onHide","onLoadJS","onOrientationChange","onRowClick","onRowSelected","onSelection","onSelectionDone","onSlide","onSuccess","onSwipe","onTabClick","onTextChange","ondeleteclick","oninsertclick","onselect","orientation","orientationmode","outTransitionConfig","overlayConfig","padding","paddingInPixel","pageOffDotImage","pageOnDotImage","pasteboardType","percent","placeholder","placeholderSkin","placeholderi18nkey","popupFocusSkin","popupIcon","popupSkin","popupStyle","popupTitle","position","postOnclickJS","postShow","poster","preOnClickJS","preOnclickJS","preShow","pressedSkin","progressIndicatorColor","provider","rawBytes","referenceHeight","referenceWidth","releaseRawBytes","reload","remove","removeAll",
				"removeAppMenuItemAt","removeAt","removeSectionsAt","removeTabAt","removeTabByld","renderAsAnchor","renderTitleText","requestURLConfig","retainHeaderFooter","retainPositionInTab","retainScrollPosition","retainSelection","rightSideText","rightSkin","rowAlternateSkin","rowCount","rowFocusSkin","rowNormalSkin","rowSkin","rowTemplate","scaleFactor","scaleMode","screenLevelWidget","scrollArrowConfig","scrollDirection","scrollOption","scrollToBeginning","scrollToEnd","scrollToWidget","scrollable","scrollingEvents","searchBy","searchCriteria","seconds","sectionHeaderSkin","sectionHeaderTemplate","secureData","secureTextEntry","selectAllRows","selectedIndex","selectedIndices","selectedItem","selectedItems","selectedKey","selectedKeyValue","selectedKeyValues","selectedKeys","selectedValue","selectedkeys","selectedkeyvalues","selectionBehavior","selectionBehaviorConfig","separatorColor","separatorRequired","separatorThickness","serviceID","setAppMenuBadgeValue","setAppMenuFocusByID","setCameraProperties",
				"setCellDataAt","setComponentData","setContext","setCurrentAppMenu","setData","setDataAt","setDatesSkin","setEnableAll","setEnabled","setMapData","setRequiredSelectionsCount","setSectionAt","setSelectedCells","setSelectedKeyInComponent","setTitleBarLeftSideButton","setTitleBarRightSideButton","setTitleBarSkin","show","showArrows","showClearButton","showCloseButton","showColumnHeaders","showCurrentLocation","showItemCount","showMiniAppMenu","showProgressIndicator","showScrollBars","showScrollbars","showTitleBar","showZoomControl","sizePercent","skin","source","spaceBetweenImages","spaceBetweenSections","src","step","submitSecure","submitURL","superScriptSkin","telephoneLinkSkin","text","textInputMode","textInputmode","thickness","thumbImage","tickedImage","title","titleBar","titleBarConfig","titleBarLeftSideView","titleBarRightSideView","titleBarSkin","titleOnPopup","transactionaldataload","transparencyBehindThePopup","type","unLoadJS","unTickedImage","undockappfooter","undockappheader",
				"untickedImage","untickedmage","vExpand","validEndDate","validStartDate","viewConfig","viewType","weekdayI18Nkey","widgetAlignment","widgetDataMap","widgetDataMapForCallout","widgetSkin","widgets","width","windowSoftInputMode","wrapping","year","zoomlevel",
				"context","widget","anchor","shouldShowLabelInBottom","fingers","swipedistance","swipevelocity"
		};

		HARDCODING_PERMITTED_KEYWORDS = new HashMap<String, Boolean>();
		for (String keyWord : hardCodingPermittedWords) {
			if (forbiddenKeyMap.get(keyWord.toLowerCase()) == null) {
				HARDCODING_PERMITTED_KEYWORDS.put(keyWord.toLowerCase().trim(), Boolean.TRUE);
			}
		}
		
		HARDCODING_VALID_KEYS = new ArrayList<String>();
		HARDCODING_VALID_KEYS.add("serviceid");
		HARDCODING_VALID_KEYS.add("appid");
		HARDCODING_VALID_KEYS.add("httpheaders");
	}
	
	
	private boolean isHardcodingForbidden(String key, String value) {
		
		boolean isHardcodingForbidden = false;
		String tmpStr = null;
		int tmpIndex = -1;
		int keyWordIndex = 0;
		StringBuffer newKey = new StringBuffer();
		char[] mychars = null;
		
		if (null == key || key.trim().length() ==0) {
			return false;
		}

		mychars = key.toCharArray();
		
		for (char c : mychars) {
			if (!Character.isWhitespace(c)) {
				newKey.append(c);
			}
		}
		for (String keyword : HARDCODING_FORBIDDEN_KEYWORDS) {
			
			// If Hard Coding is being done where it is not appropriate . .catch it
			keyWordIndex = newKey.toString().toLowerCase().indexOf(keyword.toLowerCase());
			if (keyWordIndex>=0) {
		
				tmpIndex = keyWordIndex-2;
				
				if (keyWordIndex >=2) {
					tmpIndex = keyWordIndex-2;
				} else if (keyWordIndex >=2) {
					tmpIndex = keyWordIndex-1;
				} else {
					tmpIndex = -1;
				}

				if (tmpIndex >=0) {
					tmpStr = newKey.toString().substring(tmpIndex);
				} else {
					tmpStr = null;
				}
				
				if (null == tmpStr) {
					break;
				}
				
				if ((tmpStr.indexOf(DOT)>=0) || (tmpStr.indexOf(SQUARE_BRACES_CLOSE)>=0)) {
					
					if (keyword.equals(KEYWORD_TYPE_SRC) && isHardCodedImage(value)) {
						isHardcodingForbidden = false;
					} else {
						isHardcodingForbidden = true;
						break;
					}
				} else {
					// System.out.println("IGNORING : " + key);
					continue;
				}

				}
			}		
		
		return isHardcodingForbidden;
		
	}
	
	private boolean isHardcodingPermitted(String key) {
		
		boolean isHardCodingPermitted = false;
		Boolean val;
		
		if (key == null || key.trim().length()==0) {
			return false;
		}
		
		if(key.toLowerCase().trim().indexOf(I18) >=0 ||
				key.toLowerCase().trim().indexOf(MIME) >=0) {
			// i18 or mime strings can be hardcoded
			return true;
		}
		
		val = HARDCODING_PERMITTED_KEYWORDS.get(key.toLowerCase().trim());
		
		if (val != null && val == Boolean.TRUE) {
			isHardCodingPermitted = true;
		} else {
			isHardCodingPermitted = false;
		}
		
		return isHardCodingPermitted;
	}
	
	private void checkCategory3HardCoding(String jsonReviewString, String fileName, int lineNum) throws TagToolException {
		// check for category-3 hardcoding
		// check for "hardcoded" values within a key value pair type of object or Array eg: ["key","value"], {"key":"value"}:	
		// Also check object definitions like: accountActivityReq["serviceID"] = "GMgetFutureActivityDetails"; 
		// key can be hardcoded - but not the value
	

		// additional rules for category-3
		//ignore this when there are key value some keyword which can have hardcoded data like serviceID
		// --see the keyword list - exclude those keywords which should not have hardcoded text
		//key value "URL" has to be verified for hardcodes
		JSONObject jsonObject = null;
		boolean isHardCoded = false;
		
		jsonObject = _jsonUtil.fetchJSONObject(jsonReviewString);
		
		isHardCoded = checkCategory3Hardcoding(jsonObject, fileName, lineNum);

	}
	
	private boolean checkCategory3Hardcoding(JSONObject jsonObject, String fileName, int lineNum) throws TagToolException {

		String key;
		String value;
		int valueType;
		List<String> values;
		boolean isHardCodingPermitted = false;
		boolean isHardCoded = false;
		List<KeyValuePair> keyValuePairs = null;
		boolean isHardCodingRecorded = false;
		
		if (null == jsonObject) {
			return false;
		}
		
		keyValuePairs = jsonObject.getKeyValuePairs();

		// Check for hard coded key value pairs
		if (null == keyValuePairs || keyValuePairs.size()==0) {
			return false;
		}
		
		for (KeyValuePair keyValuePair: keyValuePairs) {
			key = keyValuePair.getKey();
			value = keyValuePair.getValue();
			valueType = keyValuePair.getValueType();

			if (HARDCODING_VALID_KEYS.contains(key.toLowerCase().trim())) {
				// These  are network API params. Hardcoding is ok
				return false;
			}
			
			isHardCodingPermitted = isHardcodingPermitted(key);
			
			if (isHardCodingPermitted) {
				continue;
			}
			
			if (valueType == KeyValuePair.VALUE_TYPE_STRING) {
				// check is value is hard coded
				isHardCoded = isThereHardCodedString(value); 
				if (isHardCoded) {
					addError(ERR_HARDCODED_STRING,ERR_SHORT_DESC, jsonObject.getJsonString(),
							fileName, lineNum, SEV_HIGH, RULE_ID);
					isHardCodingRecorded = true;
					break;
				}
			} else if (valueType == KeyValuePair.VALUE_TYPE_ARRAY) {
				values = keyValuePair.getValues();
				
				if (null == values || values.size()<=1) {
					continue;
				} else {
					for(int i=1; i<values.size(); i++) {
						value = values.get(i);
						// Check is value is hard coded
						isHardCoded = isThereHardCodedString(value);
						if (isHardCoded) {
							addError(ERR_HARDCODED_STRING,ERR_SHORT_DESC, jsonObject.getJsonString(),
									fileName, lineNum, SEV_HIGH, RULE_ID);
							isHardCodingRecorded = true;
							break;
						}
					}
				}
			} else if (valueType == KeyValuePair.VALUE_TYPE_JSON_OBJ) {
				if (null != keyValuePair.getJsonObject()) {
					isHardCodingRecorded = checkCategory3Hardcoding(keyValuePair.getJsonObject(), fileName, lineNum);
					if (isHardCodingRecorded) {
						break;
					}
				} 
				
			}
		}
		return isHardCodingRecorded;
	}
	
	
	private void checkHardCodedReturnStatements(String line,  String fileName, int lineNum) throws TagToolException {
		String value = null;
		int index = 0;
		boolean isHardCoded = false;
		
		if (null == line || line.trim().length() ==0) {
			return;
		}
		
		value = line.toLowerCase().trim();
		index = value.indexOf(RETURN);
		
		if (index <0 || value.length() <(index+7)) {
			return;
		}
		
		value = value.substring(index+6);
		
		isHardCoded= isThereHardCodedString(value);
		
		if (isHardCoded) {
			
			// Add a comment for this line
			addError(ERR_HARDCODED_STRING, ERR_SHORT_DESC, line.trim(),
				fileName, lineNum, SEV_HIGH, RULE_ID);
		}

	}
	
	@Override
	public void addError( 
			String reviewComment,
			String shortDesc,
			String referenceData,
			String fileName,
			int lineNumber,
			String severity,
			String ruleId) throws TagToolException {

		if (referenceData != null && isValidHardCodedString(referenceData)) {
			addComment(getComments(),reviewComment,shortDesc,referenceData,fileName,lineNumber,severity,ruleId,false);
		}
	}
	
	private boolean isValidHardCodedString(String inputStr) {
		boolean isValid = true;
		
		// Return False if the string contains key words
		if (inputStr != null) {
			if (inputStr.indexOf(KEYWORD_I18N_STRING)>=0) {
				isValid = false;
			}
		}
		
		return isValid;
	}
}
