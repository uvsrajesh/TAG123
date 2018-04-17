package com.kony.tag.config;

import java.util.ArrayList;
import java.util.Arrays;

public interface ProjectConstants
{
	
	String BLANK = "";
	String COMMA = ",";
	String COLON = ":";
	
	String FILE_DELIMITER = "\\";
	
	// New Variables for Arabic Layout Tool
	String LOCATION_JS_GEN_SRC_ROOT_FOLDER = "\\jssrc";
	String LOCATION_JS_GEN_SRC_FOLDER = "\\generated";
	String AR_LAYOUT_PROPS_FILE = "\\arLayout.properties";
	String AR_MARGIN_PADDING_PROPS_FILE = "\\arCustomMarginPadding.properties";
	String AR_SKINS_PROPS_FILE = "\\arCustomSkins.properties";
	String AR_FILE_PREFIX = "_ar_";
	String AR_LAYOUT_MANAGER = "arLayoutManager";
	
	String PROP_BUILD_CHANNEL = "ar.layout.target.channel";
	String PROP_ARABIC_THEME = "ar.theme.required";
	String PROP_ARABIC_FONTNAMEMAP = "ar.theme.fontnamemap";
	String PROP_ANDROID_ANIMATION_FILE = "ar.animation.framework.android.file";
	String PROP_IPHONE_ANIMATION_FILE = "ar.animation.framework.iphone.file";
	
	public static final String FUNCTION = "function ";
	public static final String CHAR_SPACE = " ";
	public static final String AR_SUFFIX = "Ar";
	public static final String KEYWORD_FUNCTION_CALL = "()";
	public static final String KEYWORD_ADD = ".add";
	public static final String KEYWORD_KONY_UI = "new kony.ui.";
	public static final String KEYWORD_KONY_UI_BOX = "new kony.ui.Box";
	public static final String KEYWORD_KONY_UI_SCROLLBOX = "new kony.ui.ScrollBox";
	public static final String KEYWORD_KONY_UI_FORM = "new kony.ui.Form";
	public static final String KEYWORD_KONY_UI_POPUP = "new kony.ui.Popup";
	public static final String KEYWORD_KONY_UI_CHECKBOX_GRP = "new kony.ui.CheckBoxGroup";
	public static final String KEYWORD_KONY_UI_RADIOBUTTON_GRP = "new kony.ui.RadioButtonGroup";
	public static final String KEYWORD_KONY_UI_COMBOBOX = "new kony.ui.ComboBox";
	public static final String KEYWORD_KONY_UI_LISTBOX = "new kony.ui.ListBox";
	public static final String KEYWORD_KONY_UI_PICKERVIEW = "new kony.ui.PickerView";
	public static final String KEYWORD_KONY_UI_SLIDER = "new kony.ui.Slider";
	public static final String KEYWORD_KONY_UI_SWITCH = "new kony.ui.Switch";
	public static final String KEYWORD_KONY_UI_DATAGRID = "new kony.ui.DataGrid";
	
	public static final String KEYWORD_VAR = "var ";
	public static final String KEYWORD_ORIENTATION = "\"orientation\"";
	public static final String KEYWORD_HZ_BOX_LAYOUT = "constants.BOX_LAYOUT_HORIZONTAL";
	public static final String KEYWORD_KONY_UI_BOX_TEMPLATE = "new kony.ui.BoxTemplate";
	public static final String KEYWORD_MARGIN =  "\"margin\"";
	public static final String KEYWORD_PADDING =  "\"padding\"";
	public static final String KEYWORD_WIDGET_ALIGNMENT = "\"widgetAlignment\"";
	public static final String KEYWORD_CONTENT_ALIGNMENT = "\"contentAlignment\"";
	public static final String KEYWORD_LAYOUT_ALIGNMENT = "\"layoutAlignment\"";
	public static final String KEYWORD_SCROLL_ARROW_CONFIG = "\"scrollArrowConfig\"";
	public static final String KEYWORD_MASTER_DATA = "\"masterData\"";
	public static final String KEYWORD_ITEM_ORIENTATION = "\"itemOrientation\"";
	public static final String KEYWORD_VIEW_TYPE = "\"viewType\"";
	public static final String KEYWORD_VIEW_STYLE = "\"viewStyle\"";
	public static final String KEYWORD_CONTAINER_WEIGHT = "\"containerWeight\"";
	public static final String KEYWORD_PERCENTAGE_LAYOUT_FLAG = "\"percent\"";
		
	public static final String KEYWORD_SLIDER_MIN_LABEL = "\"minLabel\"";
	public static final String KEYWORD_SLIDER_MAX_LABEL = "\"maxLabel\"";
	public static final String KEYWORD_SLIDER_MIN_LABEL_SKIN = "\"minLabelSkin\"";
	public static final String KEYWORD_SLIDER_MAX_LABEL_SKIN = "\"maxLabelSkin\"";
	public static final String KEYWORD_SLIDER_LEFT_SKIN = "\"leftSkin\"";
	public static final String KEYWORD_SLIDER_RIGHT_SKIN = "\"rightSkin\"";
	public static final String KEYWORD_SLIDER_MIN_IMAGE = "\"minValueImage\"";
	public static final String KEYWORD_SLIDER_MAX_IMAGE = "\"maxValueImage\"";
	
	public static final String KEYWORD_SWITCH_LEFT_TEXT = "\"leftSideText\"";
	public static final String KEYWORD_SWITCH_RIGHT_TEXT = "\"rightSideText\"";

	public static final String KEYWORD_DATAGRID_COLUMN_CONFIG = "\"columnHeadersConfig\"";

	public static final String KEYWORD_INITIALIZE_FUNCTION = "initialize";
	public static final String KEYWORD_EXLCUDE = ".exclude";
	
	public static final String PROP_FORM_EXCLUDE = "ar.layout.exclude.form.";
	public static final String PROP_TEMPLATE_EXCLUDE = "ar.layout.exclude.template.";
	public static final String PROP_FORM_ANIMATION_EXCLUDE = "ar.layout.exclude.form.animation.";
	
	public static final String PROP_FORM_DUPLICATE = "ar.layout.duplicated.form.";
	public static final String PROP_TEMPLATE_DUPLICATE = "ar.layout.duplicated.template.";
	public static final String PROP_JS_ANIMATION_EXCLUDE = "ar.layout.js.exclude";
	public static final String PROP_DETAILED_LOG = "ar.tool.detailedlog.enabled";
	
	public static String NOT_DEFINED = "NotDefined";
	
	// New Variables - END
	
	// Modified Variables
	String ARABICCODE_BACKUP_PATH = "\\arLayout";
	String ARABICCODE_REVIEW_OUTPUT_PATH = "\\armodules";
	String LOCATION_ORIGINAL_JS_MODULES = "\\originalmodules";
	// Modified Variables - End
	
	String LOCATION_ALL_FORMS = "\\forms";
	String LOCATION_MOBILE_FORMS = "\\forms\\mobile";
	String LOCATION_TABLET_FORMS = "\\forms\\tablet";
	String LOCATION_DESKTOP_FORMS = "\\forms\\desktop";
	
	String LOCATION_MOBILE_FORMS_NATIVE_ANDROID = "\\forms\\mobile\\android";
	String LOCATION_MOBILE_FORMS_NATIVE_IPHONE = "\\forms\\mobile\\iphone";
	String LOCATION_MOBILE_FORMS_NATIVE_BLACKBERRY = "\\forms\\mobile\\blackberry";
	String LOCATION_MOBILE_FORMS_NATIVE_WINDOWS = "\\forms\\mobile\\winmobile";
	
	String LOCATION_MOBILE_FORMS_SPA_ANDROID = "\\forms\\mobile\\spaandroid";
	String LOCATION_MOBILE_FORMS_SPA_IPHONE = "\\forms\\mobile\\spaiphone";
	String LOCATION_MOBILE_FORMS_SPA_BLACKBERRY = "\\forms\\mobile\\spablackberry";
	String LOCATION_MOBILE_FORMS_SPA_BLACKBERRY_NONTOUCH = "\\forms\\mobile\\spatouch";
	String LOCATION_MOBILE_FORMS_SPA_WINDOWS_NONTOUCH = "\\forms\\mobile\\spawindows";
	
	String LOCATION_MOBILE_FORMS_ADVPALM = "\\forms\\mobile\\advpalm";
	String LOCATION_MOBILE_FORMS_BJS = "\\forms\\mobile\\bjs";
	String LOCATION_MOBILE_FORMS_NORMAL = "\\forms\\mobile\\normal";
	
	String LOCATION_TABLET_FORMS_NATIVE_ANDROID = "\\forms\\tablet\\andriodtab";
	String LOCATION_TABLET_FORMS_NATIVE_IPAD = "\\forms\\tablet\\ipad";
	String LOCATION_TABLET_FORMS_NATIVE_WINDOWS = "\\forms\\tablet\\windows8";
	
	String LOCATION_TABLET_FORMS_SPA_ANDROID = "\\forms\\tablet\\spaandroidtablet";
	String LOCATION_TABLET_FORMS_SPA_IPAD = "\\forms\\tablet\\spaipad";
	
	String LOCATION_ALL_MODULES = "\\modules";
	String LOCATION_LUA_MODULES = "\\modules\\lua"; 
	String LOCATION_JS_MODULES =  "\\modules\\js";
	
	String LOCATION_ALL_POPUPS = "\\dialogs";
	String LOCATION_MOBILE_POPUPS = "\\dialogs\\mobile";
	String LOCATION_TABLET_POPUPS = "\\dialogs\\tablet";
	String LOCATION_DESKTOP_POPUPS = "\\dialogs\\desktop";
	
	String MOBILE = "mobile";
	String TABLET = "tablet";
	String DESKTOP = "desktop";
	String HEADERS = "headers";
	String FOOTERS = "footers";
	
	String MOBILE_SEPARATOR = "\\mobile";
	String TABLET_SEPARATOR = "\\tablet";
	String DESKTOP_SEPARATOR = "\\desktop";
	
	String SPA = "spa";
	String ADVPALM = "advpalm";
	String BJS = "bjs";
	String NORMAL = "normal";
	
	
	ArrayList<String> EXCLUDED_FUNCTION_NAMES = new ArrayList<String>(
			Arrays.asList(
					"profiler.startEvent",
					"profiler.endEvent",
					"profiler.startFunction",
					"profiler.endFunction",
					"profiler.updateFunctionArray",
					"profiler.updateEndTime",
					"profiler.startNetworkCall",
					"profiler.endNetworkCall",
					"profiler.getEventStackTrace",
					"loopThroughNetworkStack",
					"loopThroughStack",
					"showProfilerResults",
					"showDetailedProfilerResults",
					"TAG.Logger",
					"logcontroller.TRACE",
					"logcontroller.DEBUG",
					"logcontroller.INFO",
					"logcontroller.WARN",
					"logcontroller.ERROR",
					"log",
					"logcontroller.getLogLevel",
					"logcontroller.gettrace",
					"logcontroller.trace",
					"logcontroller.info",
					"logcontroller.warn",
					"logcontroller.error",
					"logcontroller.debug",
					"logcontroller.getMessages",
					"logcontroller.getNumberOfMessages",
					"logcontroller.setNumberOfMessagesToHold",
					"logcontroller.emailLogInfo",
					"logcontroller.showErrorAlert",
					"logcontroller.showInfoAlert",
					"kony.decrement",
					"kony.increment",
					"kony.decrementIndices",
					"kony.incrementIndices",
					"pi",
					"random",
					"randomSeed",
					"toInteger",
					"pow",
					"findExtreme",
					"min",
					"max",
					"sqrt",
					"find ",
					"len",
					"compare",
					"charat",
					"flipCase",
					"lower",
					"upper",
					"rep",
					"reverse",
					"trim",
					"equalsIgnoreCase",
					"equals",
					"matchEnds",
					"startsWith",
					"endsWith",
					"split",
					"sub",
					"replace",
					"isAsciiAlpha",
					"isAsciiAlphaNumeric",
					"isNumeric",
					"containsChars",
					"containsOnlyGivenChars",
					"containsNoGivenChars",
					"isValidEmail",
					"escapeRegExp",
					"concat",
					"insert",
					"remove",
					"sort",
					"filter",
					"map ",
					"mapNew",
					"get",
					"contains",
					"append",
					"removeAll",
					"unpack",
					"returnResult"
			));

	// ######################################################
	String LOCATION_MOBILE_POPUPS_NATIVE_ANDROID = "\\dialogs\\mobile\\android";
	String LOCATION_MOBILE_POPUPS_NATIVE_IPHONE = "\\dialogs\\mobile\\iphone";
	String LOCATION_MOBILE_POPUPS_NATIVE_BLACKBERRY = "\\dialogs\\mobile\\blackberry";
	String LOCATION_MOBILE_POPUPS_NATIVE_WINDOWS = "\\dialogs\\mobile\\winmobile";
	
	String LOCATION_MOBILE_POPUPS_SPA_ANDROID = "\\dialogs\\mobile\\spaandroid";
	String LOCATION_MOBILE_POPUPS_SPA_IPHONE = "\\dialogs\\mobile\\spaiphone";
	String LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY = "\\dialogs\\mobile\\spablackberry";
	String LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY_NONTOUCH = "\\dialogs\\mobile\\spatouch";
	String LOCATION_MOBILE_POPUPS_SPA_WINDOWS_NONTOUCH = "\\dialogs\\mobile\\spawindows";
	
	String LOCATION_MOBILE_POPUPS_ADVPALM = "\\dialogs\\mobile\\advpalm";
	String LOCATION_MOBILE_POPUPS_BJS = "\\dialogs\\mobile\\bjs";
	String LOCATION_MOBILE_POPUPS_NORMAL = "\\dialogs\\mobile\\normal";
	
	String LOCATION_TABLET_POPUPS_NATIVE_ANDROID = "\\dialogs\\tablet\\andriodtab";
	String LOCATION_TABLET_POPUPS_NATIVE_IPAD = "\\dialogs\\tablet\\ipad";
	String LOCATION_TABLET_POPUPS_NATIVE_WINDOWS = "\\dialogs\\tablet\\windows8";
	
	String LOCATION_TABLET_POPUPS_SPA_ANDROID = "\\dialogs\\tablet\\spaandroidtablet";
	String LOCATION_TABLET_POPUPS_SPA_IPAD = "\\dialogs\\tablet\\spaipad";

	ArrayList<String> FORM_FOLDERS_ROOT_ALL_MOBILE_TAB = new ArrayList<String>(
			Arrays.asList(
					LOCATION_MOBILE_FORMS,
					LOCATION_TABLET_FORMS,
					LOCATION_MOBILE_POPUPS,
					LOCATION_TABLET_POPUPS
			));

	ArrayList<String> FORM_FOLDERS_ROOT_ALL_DESKTOPWEB = new ArrayList<String>(
			Arrays.asList(
					LOCATION_DESKTOP_FORMS,
					LOCATION_DESKTOP_POPUPS
					));

	
	ArrayList<String> FORM_FOLDERS_NATIVE_ONLY = new ArrayList<String>(
			Arrays.asList(
					LOCATION_MOBILE_FORMS_NATIVE_ANDROID,
					LOCATION_MOBILE_FORMS_NATIVE_IPHONE,
					LOCATION_MOBILE_FORMS_NATIVE_BLACKBERRY,
					LOCATION_MOBILE_FORMS_NATIVE_WINDOWS,
					
					LOCATION_TABLET_FORMS_NATIVE_ANDROID,
					LOCATION_TABLET_FORMS_NATIVE_IPAD,
					LOCATION_TABLET_FORMS_NATIVE_WINDOWS,
					
					LOCATION_MOBILE_POPUPS_NATIVE_ANDROID,
					LOCATION_MOBILE_POPUPS_NATIVE_IPHONE,
					LOCATION_MOBILE_POPUPS_NATIVE_BLACKBERRY,
					LOCATION_MOBILE_POPUPS_NATIVE_WINDOWS,
					
					LOCATION_TABLET_POPUPS_NATIVE_ANDROID,
					LOCATION_TABLET_POPUPS_NATIVE_IPAD,
					LOCATION_TABLET_POPUPS_NATIVE_WINDOWS
					));
	
	ArrayList<String> FORM_FOLDERS_SPA_ONLY = new ArrayList<String>(
			Arrays.asList(
					LOCATION_MOBILE_FORMS_SPA_ANDROID,
					LOCATION_MOBILE_FORMS_SPA_IPHONE,
					LOCATION_MOBILE_FORMS_SPA_BLACKBERRY,
					LOCATION_MOBILE_FORMS_SPA_BLACKBERRY_NONTOUCH,
					LOCATION_MOBILE_FORMS_SPA_WINDOWS_NONTOUCH,

					LOCATION_TABLET_FORMS_SPA_ANDROID,
					LOCATION_TABLET_FORMS_SPA_IPAD,

					LOCATION_MOBILE_POPUPS_SPA_ANDROID,
					LOCATION_MOBILE_POPUPS_SPA_IPHONE,
					LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY,
					LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY_NONTOUCH,
					LOCATION_MOBILE_POPUPS_SPA_WINDOWS_NONTOUCH,

					LOCATION_TABLET_POPUPS_SPA_ANDROID,
					LOCATION_TABLET_POPUPS_SPA_IPAD
					));	

	ArrayList<String> FORM_FOLDERS_MOBILEWEB_ONLY = new ArrayList<String>(
			Arrays.asList(
					LOCATION_MOBILE_FORMS_ADVPALM,
					LOCATION_MOBILE_FORMS_BJS,
					LOCATION_MOBILE_FORMS_NORMAL,

					LOCATION_MOBILE_POPUPS_ADVPALM,
					LOCATION_MOBILE_POPUPS_BJS,
					LOCATION_MOBILE_POPUPS_NORMAL
					));	
	
	ArrayList<String> FORM_FOLDERS_MOBILE_APP_ONLY = new ArrayList<String>(
			Arrays.asList(
					LOCATION_MOBILE_FORMS,
					LOCATION_MOBILE_POPUPS,
					
					LOCATION_MOBILE_FORMS_NATIVE_ANDROID,
					LOCATION_MOBILE_FORMS_NATIVE_IPHONE,
					LOCATION_MOBILE_FORMS_NATIVE_BLACKBERRY,
					LOCATION_MOBILE_FORMS_NATIVE_WINDOWS,
					
					LOCATION_MOBILE_POPUPS_NATIVE_ANDROID,
					LOCATION_MOBILE_POPUPS_NATIVE_IPHONE,
					LOCATION_MOBILE_POPUPS_NATIVE_BLACKBERRY,
					LOCATION_MOBILE_POPUPS_NATIVE_WINDOWS,

					LOCATION_MOBILE_FORMS_SPA_ANDROID,
					LOCATION_MOBILE_FORMS_SPA_IPHONE,
					LOCATION_MOBILE_FORMS_SPA_BLACKBERRY,
					LOCATION_MOBILE_FORMS_SPA_BLACKBERRY_NONTOUCH,
					LOCATION_MOBILE_FORMS_SPA_WINDOWS_NONTOUCH,

					LOCATION_MOBILE_POPUPS_SPA_ANDROID,
					LOCATION_MOBILE_POPUPS_SPA_IPHONE,
					LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY,
					LOCATION_MOBILE_POPUPS_SPA_BLACKBERRY_NONTOUCH,
					LOCATION_MOBILE_POPUPS_SPA_WINDOWS_NONTOUCH,

					LOCATION_MOBILE_FORMS_ADVPALM,
					LOCATION_MOBILE_FORMS_BJS,
					LOCATION_MOBILE_FORMS_NORMAL,

					LOCATION_MOBILE_POPUPS_ADVPALM,
					LOCATION_MOBILE_POPUPS_BJS,
					LOCATION_MOBILE_POPUPS_NORMAL
					
					
			));
	
	ArrayList<String> FORM_FOLDERS_TAB_APP_ONLY = new ArrayList<String>(
			Arrays.asList(
					LOCATION_TABLET_FORMS,
					LOCATION_TABLET_POPUPS,
					
					LOCATION_TABLET_FORMS_NATIVE_ANDROID,
					LOCATION_TABLET_FORMS_NATIVE_IPAD,
					LOCATION_TABLET_FORMS_NATIVE_WINDOWS,
					
					LOCATION_TABLET_POPUPS_NATIVE_ANDROID,
					LOCATION_TABLET_POPUPS_NATIVE_IPAD,
					LOCATION_TABLET_POPUPS_NATIVE_WINDOWS,
					
					LOCATION_TABLET_FORMS_SPA_ANDROID,
					LOCATION_TABLET_FORMS_SPA_IPAD,

					LOCATION_TABLET_POPUPS_SPA_ANDROID,
					LOCATION_TABLET_POPUPS_SPA_IPAD
			));

	// ######################################################	
	
	String LOCATION_ALL_TEMPLATES = "\\templates";
	String LOCATION_ALL_HEADERS = "\\templates\\headers";
	String LOCATION_MOBILE_HEADERS = "\\templates\\headers\\mobile";
	String LOCATION_TABLET_HEADERS = "\\templates\\headers\\tablet";
	String LOCATION_DESKTOP_HEADERS = "\\templates\\headers\\desktop";
	
	String LOCATION_ALL_FOOTERS = "\\templates\\footers";
	String LOCATION_MOBILE_FOOTERS = "\\templates\\footers\\mobile";
	String LOCATION_TABLET_FOOTERS = "\\templates\\footers\\tablet";
	String LOCATION_DESKTOP_FOOTERS = "\\templates\\footers\\desktop";

	String LOCATION_ALL_MAPS = "\\templates\\maps";
	String LOCATION_MOBILE_TEMPLATE_MAPS = "\\templates\\maps\\mobile";
	String LOCATION_TABLET_TEMPLATE_MAPS = "\\templates\\maps\\tablet";
	String LOCATION_DESKTOP_TEMPLATE_MAPS = "\\templates\\maps\\desktop";

	String LOCATION_ALL_SEGMENTS = "\\templates\\segments";
	String LOCATION_MOBILE_TEMPLATE_SEGMENTS = "\\templates\\segments\\mobile";
	String LOCATION_TABLET_TEMPLATE_SEGMENTS = "\\templates\\segments\\tablet";
	String LOCATION_DESKTOP_TEMPLATE_SEGMENTS = "\\templates\\segments\\desktop";

	String LOCATION_ALL_GRIDS = "\\templates\\grids";
	String LOCATION_DESKTOP_TEMPLATE_GRIDS = "\\templates\\grids\\desktop";
	String LOCATION_ALL_TABHEADERS = "\\tabheaders\\grids";
	String LOCATION_DESKTOP_TEMPLATE_TABHEADERS = "\\templates\\tabheaders\\desktop";
	
	String LOCATION_CONFIG_PROJECT_EVENTS_FILE = "\\projectevents.xml";
	String LOCATION_CONFIG_PROJECT_TABLET_EVENTS_FILE = "\\projecttabletevents.xml";
	String LOCATION_CONFIG_PROJECT_DESKTOP_EVENTS_FILE = "\\projectdesktop_kioskevents.xml";
	String LOCATION_CONFIG_PROJECT_SKIN_FILE = "\\projectskin.xml";
	String LOCATION_CONFIG_PROJECT_SEQUENCE_FILE = "\\globalsequences.xml";
	String LOCATION_CONFIG_PROJECT_TABLET_SEQUENCE_FILE = "\\globalsequences_tablet.xml";
	String LOCATION_CONFIG_PROJECT_DESKTOP_SEQUENCE_FILE = "\\globalsequences_desktop.xml";
	String LOCATION_CONFIG_PROJECT_GLOBAL_VARS = "\\projectvar.xml";
	String CODEREVIEW_CONFIG_LOCATION = "\\codeReviewConf";
	String LOCATION_CODE_REVIEW_PROPS_FILE = CODEREVIEW_CONFIG_LOCATION + "\\codereview.properties";
	public static final String BUILD_PROPERTIES_FILE = "build.properties";
	public static final String GLOBAL_PROPERTIES_FILE = "global.properties";

	String LOCATION_SKINS_IGNORE_LIST = CODEREVIEW_CONFIG_LOCATION + "\\ignoreUnusedSkins.properties";
	String LOCATION_IMAGES_IGNORE_LIST = CODEREVIEW_CONFIG_LOCATION + "\\ignoreUnusedImages.properties";
	String LOCATION_FUNCTIONS_IGNORE_LIST = CODEREVIEW_CONFIG_LOCATION + "\\ignoreUnusedFunctions.properties";
	String LOCATION_I18NKEYS_IGNORE_LIST = CODEREVIEW_CONFIG_LOCATION + "\\ignoreUnusedI18Keys.properties";
	String LOCATION_REVIEW_FILES_IGNORE_LIST = CODEREVIEW_CONFIG_LOCATION + "\\ignoreReviewFiles.properties";
	
	String LOCATION_IDLE_TIMEOUT_FILES_IGNORE_LIST = CODEREVIEW_CONFIG_LOCATION + "\\ignoreIdleTimeoutFileslist.txt";
	String LOCATION_SECURE_SUBMIT_FILES_IGNORE_LIST = CODEREVIEW_CONFIG_LOCATION + "\\ignoreSecureSubmitFileslist.txt";
	String LOCATION_APP_MENU_SKINS_FILE = "\\iphoneappmenu.xml";
	String PROP_ANDROID_SELF_SIGNED_CERT = "android_allowselfsignedcertificates";
	String PROP_IPHONE_SELF_SIGNED_CERT = "allowselfsignedcertificate";
	
	String YES = "yes";
	String NO = "no";
	String EQUALS = "=";
	String DOT = ".";
	String SQUARE_BRACES_CLOSE = "]";
	String SEMI_COLON = ";";
	String STR_UNDERSCORE = "_";
	String STR_TEMP = "temp";
	String NEW_LINE = "\n";
	String TRUE = "true";
	String FALSE = "false";
	
	String FILE_TYPE_PNG = ".png";
	String FILE_TYPE_JPEG = ".jpeg";
	String FILE_TYPE_JPG = ".jpg";
	String FILE_TYPE_GIF = ".gif";
	
	String HTTP = "http";
	
	String PROP_DEFAULT_THEME_FOR_SKINS = "rules.misc.all.002.project_ui_theme";
	
	// CODE REVIEW BACK UP and OUTPUT LOCATIONS
	String CODE_BACKUP_PATH = "\\codeReview";
	String CODE_BACKUP_PATH_DISPLAY_NAME = "codeReview";
	String CODE_REVIEW_OUTPUT_PATH = "\\output-JS";
	
	String CODE_REVIEW_LOG_FILE = "\\review.log";
	
	String JS_REVIEW_COMMENTS_FILE = "\\01-JavascriptReviewComments.csv";

	String MOBILE_FORM_REVIEW_COMMENTS_FILE = "\\02A-FormReviewComments-MobileApp.csv";
	String TABLET_FORM_REVIEW_COMMENTS_FILE = "\\03A-FormReviewComments-TabletApp.csv";
	String DESKTOP_FORM_REVIEW_COMMENTS_FILE = "\\04-FormReviewComments-DesktopApp.csv";
	
	String FORKED_MOBILE_FORM_REVIEW_COMMENTS_FILE = "\\02B-forkedFormsReviewComments-MobileApp.csv";
	String FORKED_TABLET_FORM_REVIEW_COMMENTS_FILE = "\\03B-forkedFormsReviewComments-TabletApp.csv";
	
	
	String EXECUTIVE_SUMMARY_FILE = "\\00-SUMMARY.csv";
	String SECURITY_REVIEW_COMMENTS_FILE = "\\06-SecurityReviewComments.csv";
	String UNUSED_FUNCTIONS_COMMENTS_FILE = "\\05A-UnusedFunctions.csv";
	String UNUSED_SKINS_COMMENTS_FILE = "\\05B-UnusedSkins.csv";
	String UNUSED_IMAGES_COMMENTS_FILE = "\\05C-UnusedImages.csv";
	String UNUSED_I18KEYS_COMMENTS_FILE = "\\05D-UnusedI18Keys.csv";
	String MISC_COMMENTS_FILE = "\\05E-MiscComments.csv";
	String PROJECT_PROPERTIES_FILE = "\\projectprop.xml";
	String PROJECT_RESOURCES_PATH = "\\resources";
	
	String DEFAULT_THEMES_PATH = "\\themes\\";
	String DEFAULT_THEME  = "default";
	String DEFAULT_SKINS_FILE = "\\skin.xml";
	String ARABIC_THEME = "arabicTheme";
	String DEFAULT_I18_PROPS_PATH = "\\resources\\i18n\\properties\\";
	
	String NA = "NA";
	String JS = "JS";
	String FRM = "FRM";
	String MISC = "MISC";
	String KEYWORD_FONTNAME = "FontNameMap";
	

	//String CODE_REVIEW_FORMATTED_LUA_PATH = "\\output\\formattedLua";
	
	char CHAR_CURLY_OPEN = '{';
	char CHAR_CURLY_CLOSE = '}';
	char CHAR_SQUARE_OPEN = '[';
	char CHAR_SQUARE_CLOSE = ']';
	char CHAR_EQUALS = '=';
	char CHAR_PARANTHESIS_OPEN = '(';
	char CHAR_PARANTHESIS_CLOSE =  ')';
	char CHAR_DOUBLE_QUOTE =  '"';
	char CHAR_SINGLE_QUOTE =  '\'';
	char CHAR_SEMI_COLON =  ';';
	char CHAR_COLON = ':';
	char CHAR_COMMA = ',';
	char CHAR_DOT = '.';
	char CHAR_EXCLAMATION = '!';
	
	String NULL = "null"; 
	String ERROR = "Error";
	String WARNING = "Warning";
	String NONE = "None";
	
	// Constants for Form Review
	String WIDGET_TYPE  = "widgetType";
	String KONY_FORM_CHILDREN = "children";
	String CDATA = "CDATA";
	String TAG_ID = "ID";
	
	// Container Widgets
	String KONY_FORM = "kForm";
	String KONY_HBOX = "khBox";
	String KONY_VBOX = "kvBox";
	String KONY_TAB_PANE = "kTabPane";
	String KONY_SCROLL_BOX = "kScrollBox";
	String KONY_MENU_CONTAINER = "kMenuContainer";

	// Basic UI Widgets
	String KONY_BUTTON = "kButton";
	String KONY_CALENDAR = "kDateField";
	String KONY_CHECKBOX_GROUP = "kCheckBox";
	String KONY_COMBO_BOX = "kComboBox";
	String KONY_DATA_GRID = "kTable";
	String KONY_IMAGE = "kImage2";
	String KONY_LABEL = "kLabel";
	String KONY_LINE = "kLine";
	String KONY_LINK = "kLink";
	String KONY_LIST_BOX = "kListBox";
	String KONY_MENU_ITEM = "kMenuItem";
	String KONY_RADIO_BUTTON_GROUP = "kRadio";
	String KONY_RICHTEXT_BOX = "kRichText";
	String KONY_SLIDER = "kSlider";
	String KONY_TEXT_AREA = "kTextArea2";
	String KONY_TEXT_AREA_OLD = "kTextArea";
	String KONY_TEXT_BOX = "kTextBox2";
	String KONY_TEXT_BOX_OLD = "kTextBox";

	// Advanced UI Widgets
	String KONY_BROWSER = "kBrowser";
	String KONY_CAMERA = "kCamera";
	// String KONY_CHART = ""; // Not supported Now 
	String KONY_HZ_IMAGE_STRIP = "khImageStrip2";
	String KONY_IMAGE_GALLERY = "kImageGallery2";
	String KONY_MAP = "kMap";
	String KONY_OBJ_SELECTOR_3D = "kObjectSelector3D";
	String KONY_PHONE = "kCall";
	String KONY_PICKER_VIEW = "kPickerView";
	String KONY_SEGMENT = "kSegment2";
	String KONY_SEGMENT_OLD = "kSegment";
	String KONY_SWITCH = "kSwitch";
	String KONY_VIDEO = "kVideo";
	
	int BOX_POSITION_AS_NORMAL = 0;
	
	String COMMENT_CATEGORY_JS = "jsComments"; 
	String COMMENT_CATEGORY_MOBILE_APP = "mobileAppcomments";
	String COMMENT_CATEGORY_FORKED_MOBILE_APP = "forkedMobileAppComments";
	String COMMENT_CATEGORY_TABLET_APP = "tabletAppcomments";
	String COMMENT_CATEGORY_FORKED_TABLET_APP = "forkedTabletAppComments";
	String COMMENT_CATEGORY_DESKTOP_APP = "desktopAppComments";
	String COMMENT_CATEGORY_MISC = "miscComments";
	
	String PROP_MOBILE_NATIVE_APP_REVIEW_FLAG = "review.mobile.native";
	String PROP_MOBILE_SPA_APP_REVIEW_FLAG = "review.mobile.spa";
	String PROP_MOBILE_MOBILEWEB_APP_REVIEW_FLAG = "review.mobile.mobileweb";

	String PROP_TABLET_NATIVE_APP_REVIEW_FLAG = "review.tablet.native";
	String PROP_TABLET_SPA_APP_REVIEW_FLAG = "review.tablet.spa";

	String PROP_DESKTOPWEB_APP_REVIEW_FLAG = "review.desktopweb";
	
	String PROP_FORKED_FORMS_REVIEW_FLAG = "review.forkedforms";
}

