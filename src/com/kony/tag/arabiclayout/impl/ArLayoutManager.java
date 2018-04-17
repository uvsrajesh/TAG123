package com.kony.tag.arabiclayout.impl;

import java.util.ArrayList;
import java.util.List;

import com.kony.tag.arabiclayout.util.ArabicLayoutUtil;
import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.ReviewModeConstants;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.TAGToolsUtil;

public class ArLayoutManager implements ProjectConstants {
	
	private static List<String> _forms = new ArrayList<String>();
	private static List<String> _templates = new ArrayList<String>();
	private static List<String> _templateInitMethods = new ArrayList<String>();
	private static List<String> _manuallyDuplicatedForms = new ArrayList<String>();
	private static final String AR = "Ar";
	private static String _channel = "Not Known";
	private static final String WARNING_PREFIX = "WARNING : ";
	
	
	private static List<String> _warnings = new ArrayList<String>();
	
	public static List<String> getWarnings() {
		return _warnings;
	}

	public static void addWarning(String warning) {
		_warnings.add(WARNING_PREFIX + warning);
	}

	public static String getChannel() {
		return _channel;
	}
	
	public static String updateChannel(String channel) {
		return _channel = channel;
	}	

	public static void init() {
		_forms.clear();
		_templates.clear();
		_templateInitMethods.clear();
		_manuallyDuplicatedForms.clear();
		_warnings.clear();
		_channel = "Not Known";
	}

	public static void addForm(String formName){
		if (!_forms.contains(formName)) {
			_forms.add(formName);
		}
	}
	
	public static void addManuallyDuplicatedForm(String manuallyDuplicatedForm) {
		if (!_manuallyDuplicatedForms.contains(manuallyDuplicatedForm)) {
			_manuallyDuplicatedForms.add(manuallyDuplicatedForm);
		}
	}
	
	public static void addTemplateInitializeMethod(String templateInitMethod){
		if (!_templateInitMethods.contains(templateInitMethod)) {
			_templateInitMethods.add(templateInitMethod);
		}
	}

	
	public static void addTemplate(String templateName){
		if (!_templates.contains(templateName)) {
			_templates.add(templateName);
		}
	}

	// Auto Generated Function to destroy all the forms
	public static List<String> generateAutoDestroyFunction(List<String> linesOfCode){
		for (String formName : _forms) {
			linesOfCode.add(formName+".destroy();");
		}
		
		for (String formName : _manuallyDuplicatedForms) {
			linesOfCode.add(formName+".destroy();");
		}
		
		return linesOfCode;
	}
	
	// Auto Generate Layout Manager JS Module needed for Alignment reverse functionality
	public static void createAutogenModule() {
		List<String> linesOfCode = new ArrayList<String>();
		linesOfCode.add("// Auto generated layout manager module. Do not Modify");
		linesOfCode.add("var LAYOUTMANAGER = LAYOUTMANAGER || {}");
		linesOfCode.add("\n");
		linesOfCode.add("LAYOUTMANAGER.getInstance = function() {");
		linesOfCode.add("\n");
		linesOfCode.add("//*********************Private Properties and Methods****************************//");
		linesOfCode.add("var isDefaultLayout = true;");
		linesOfCode.add("\n");
		linesOfCode.add("//*********************Public Properties and Methods****************************//");
		linesOfCode.add("var layoutManager = {}");
		linesOfCode.add("\n");
		linesOfCode.add("//Invoke the following function in the post app init - without fail");
		linesOfCode.add("//This function is not accessible from Event Editor. See below for alternative API that will be accessible from Event Editor");
		linesOfCode.add("layoutManager.init = function(){");
		linesOfCode.add("isDefaultLayout = true;");
		/*linesOfCode.add("// Initialize Templates");
		for (String templateName : _templates) {
			linesOfCode.add(templateName+EN + " = " + templateName+";");
			//linesOfCode.add(templateName+AR + " = null;" );
		}
		linesOfCode.add("\n");
		linesOfCode.add("// Initialize Forms");
		for (String formName : _forms) {
			linesOfCode.add(formName+EN + " = " + formName+";");
			//linesOfCode.add(formName+AR + " = null;" );
		}*/
		linesOfCode.add("kony.print(\"layoutManager : init successful\");");
		linesOfCode.add("}");
		linesOfCode.add("\n");
		
		linesOfCode.add("// Invoke this when you want to switch to the Default Layout for which the App is designed.");
		linesOfCode.add("// Example: Invoke this for switching to Left To Right (LTR) Layout (for English)");
		linesOfCode.add("//This function is not accessible from Event Editor. See below for alternative API that will be accessible from Event Editor");
		linesOfCode.add("layoutManager.switchToDefaultLayout = function(fnCallBack){");
		linesOfCode.add("kony.print(\"layoutManager : switchToDefaultLayout : starts\");");
		linesOfCode.add("");
		linesOfCode.add("isDefaultLayout = true;");
		linesOfCode.add("");
		linesOfCode.add("// Destroy all un necessary data in the current layout");
		/*for (String formName : _forms) {
			linesOfCode.add(formName+".destroy();");
		}*/
		//linesOfCode = generateAutoDestroyFunction(linesOfCode);
		linesOfCode.add("this.destroyForms();");
		linesOfCode.add("");
		linesOfCode.add("// Re Assign forms/templates to original layout");
		/*for (String templateName : _templates) {
			//linesOfCode.add(templateName + " = " + templateName+EN+";");
		}
		for (String formName : _forms) {
			//linesOfCode.add(formName + " = " + formName+EN+";");
		}*/

		// CHECK STARTS
		linesOfCode.add("// Create all the templates specific to En Layout");
		for (String templateInitMethodName : _templateInitMethods) {
			linesOfCode.add(templateInitMethodName + "(); ");
		}
		linesOfCode.add("// Create all the forms specific to En Layout");
		for (String formName : _forms) {
			linesOfCode.add(formName + "Globals" + "();");
		}
		linesOfCode.add("// Provision for Manually Duplicated forms in En Layout");
		for (String formName : _manuallyDuplicatedForms) {
			linesOfCode.add(formName + "Globals" + "();");
		}
		
		
		/*for (String templateName : _templates) {
			linesOfCode.add(templateName+EN + " = " + templateName+";");
		}
		for (String formName : _forms) {
			linesOfCode.add(formName+EN + " = " + formName+";");
		}*/
		// CHECK ENDS

		linesOfCode.add("\n");
		
		linesOfCode.add("// Nullify all Arabic layout objects");
		/*for (String templateName : _templates) {
			//linesOfCode.add(templateName + AR + " = " + templateName + ";");
		}
		for (String formName : _forms) {
			//linesOfCode.add(formName + AR + " = null;");
			//linesOfCode.add(formName + AR + " = " + formName + ";");
		}*/
		
		linesOfCode.add("\n");
		linesOfCode.add("kony.print(\"layoutManager : switchToDefaultLayout : ends\");");
		linesOfCode.add("// Proceed with user defined logic after layout reversal");
		linesOfCode.add("fnCallBack();");
		linesOfCode.add("}");
		
		linesOfCode.add("\n");
		linesOfCode.add("// Invoke this when you want to reverse the default layout of the App");
		linesOfCode.add("// Example: Invoke this for switching to Right To Left (RTL) Layout (for Arabic)");
		linesOfCode.add("//This function is not accessible from Event Editor. See below for alternative API that will be accessible from Event Editor");
		linesOfCode.add("layoutManager.switchToArabicLayout = function(fnCallBack){");
		linesOfCode.add("kony.print(\"layoutManager : switchToArabicLayout : starts\");");
		linesOfCode.add("");
		linesOfCode.add("isDefaultLayout = false;");
		linesOfCode.add("");
		linesOfCode.add("// Destroy all un necessary data in the current layout");
		/*for (String formName : _forms) {
			linesOfCode.add(formName+".destroy();");
		}*/
		
		//linesOfCode = generateAutoDestroyFunction(linesOfCode);
		linesOfCode.add("this.destroyForms();");
		linesOfCode.add("");
		linesOfCode.add("// Create all the templates specific to Ar Layout");
		for (String templateInitMethodName : _templateInitMethods) {
			linesOfCode.add(templateInitMethodName + AR + "(); ");
		}

		linesOfCode.add("\n");
		linesOfCode.add("// Re Assign original template variables to Ar objects");
		for (String templateName : _templates) {
			linesOfCode.add(templateName + " = " + templateName+AR+";");
		}

		linesOfCode.add("\n");
		linesOfCode.add("// Create all the forms specific to Ar Layout");
		for (String formName : _forms) {
			linesOfCode.add(formName + "Globals" + AR + "();");
		}
		
		linesOfCode.add("// Provision for Manually Duplicated forms in Ar Layout");
		for (String formName : _manuallyDuplicatedForms) {
			linesOfCode.add(formName + AR_SUFFIX+ "Globals" + "();");
		}
		
		
		linesOfCode.add("\n");
		linesOfCode.add("// Re Assign original forms variables to Ar objects");
		for (String formName : _forms) {
			linesOfCode.add(formName + " = " + formName+AR+";");
		}
		
		linesOfCode.add("// Provision for Manually Duplicated forms in Ar Layout");
		for (String formName : _manuallyDuplicatedForms) {
			linesOfCode.add(formName + " = " + formName+AR+";");
		}
		
		linesOfCode.add("");
		linesOfCode.add("kony.print(\"layoutManager : switchToArabicLayout : ends\");");
		linesOfCode.add("// Proceed with user defined logic after layout reversal");
		linesOfCode.add("fnCallBack();");
		linesOfCode.add("}");
		linesOfCode.add("");
		linesOfCode.add("// This function is used to destroy all the forms of the application");
		linesOfCode.add("layoutManager.destroyForms = function(){");
		linesOfCode.add("kony.print(\"Inside the destroy forms function\");");
		linesOfCode = generateAutoDestroyFunction(linesOfCode);
		linesOfCode.add("}");
		linesOfCode.add("\n");
		linesOfCode.add("// Invoke this when you want to know the current layout being used by the App");
		linesOfCode.add("//This function is not accessible from Event Editor. ");
		linesOfCode.add("layoutManager.isDefaultLayout = function(){");
		linesOfCode.add("return isDefaultLayout;");
		linesOfCode.add("}");
		linesOfCode.add("\n");
		linesOfCode.addAll(generateMasterDataUpdateFunction());
		linesOfCode.addAll(generateSliderFunctions());
		linesOfCode.addAll(generateSegPageViewUpdateFunction());
		linesOfCode.addAll(generateSliderSkinUpdateFunction());
		linesOfCode.add("return layoutManager;");
		linesOfCode.add("}");
		
		linesOfCode.add("\n");
		linesOfCode.add("//Invoke the following function in the post app init - without fail");
		linesOfCode.add("//This function is accessible from Event Editor");
		linesOfCode.add("function layoutManagerInit() {");
		linesOfCode.add("arLayoutManager = LAYOUTMANAGER.getInstance();");
		linesOfCode.add("arLayoutManager.init();");
		linesOfCode.add("}");
		
		linesOfCode.add("\n");
		linesOfCode.add("// Invoke this when you want to switch to the Default Layout for which the App is designed.");
		linesOfCode.add("// Example: Invoke this for switching to Left To Right (LTR) Layout (for English)");
		linesOfCode.add("//This function is accessible from Event Editor");
		linesOfCode.add("function switchToDefaultLayout(fnCallBack) {");
		linesOfCode.add("arLayoutManager.switchToDefaultLayout(fnCallBack);");
		linesOfCode.add("}");
		
		linesOfCode.add("\n");
		linesOfCode.add("// Invoke this when you want to reverse the default layout of the App");
		linesOfCode.add("// Example: Invoke this for switching to Right To Left (RTL) Layout (for Arabic)");
		linesOfCode.add("//This function is accessible from Event Editor");
		linesOfCode.add("function switchToArabicLayout(fnCallBack) {");
		linesOfCode.add("arLayoutManager.switchToArabicLayout(fnCallBack);");
		linesOfCode.add("}");
		
		
		ArabicLayoutUtil.getInstance().createReportFile(linesOfCode, "arLayoutManager.js");
	}
	
	public static void copyTransformedJSModules(ProjectConfig projectConfig) {
		ReviewUtil reviewUtil = new ReviewUtil();
		try {
			reviewUtil.deleteSpecificFiles(projectConfig.getProjectPath()+FILE_DELIMITER+LOCATION_JS_MODULES,AR_FILE_PREFIX);
			reviewUtil.deleteSpecificFiles(projectConfig.getProjectPath()+FILE_DELIMITER+LOCATION_JS_MODULES,AR_LAYOUT_MANAGER);
			reviewUtil.copyGeneratedArabicFiles(projectConfig.getReviewOutputPath() + FILE_DELIMITER + ArLayoutManager.getChannel(), projectConfig.getProjectPath()+FILE_DELIMITER+LOCATION_JS_MODULES);
		}catch (Exception e) {
			CodeReviewStatus.getInstance().addErrorMessage("Copy Exception : " + e);
		}
		
	}
	
	public static boolean is_iOS_Channel() {
		if (_channel != null && _channel.length()>0) {
			if(_channel.equals("iphone") || _channel.equals("ipad")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	private static List<String> generateMasterDataUpdateFunction() {
		List <String> fnLines = new ArrayList<String>();
		fnLines.add("\n");
		fnLines.add("layoutManager.updateMasterData = function(widgetObj, values){");
		fnLines.add("var widgetType = \"\";");
		fnLines.add("try {");
		fnLines.add("	widgetType = kony.type(widgetObj);");
		fnLines.add("}catch (e) {	");
		fnLines.add("	//swallow, nothing can be done");
		fnLines.add("	kony.print(\"layoutManager: Cannot Update Master Data. Error : \" + e);");
		fnLines.add("	return;");
		fnLines.add("}");
		fnLines.add("kony.print(\"layoutManager: Master Data update requested for widget type : \" + widgetType);");
		fnLines.add("if (widgetType == \"kony.ui.CheckBoxGroup\" || ");
		fnLines.add("	widgetType == \"kony.ui.RadioButtonGroup\" ||");
		fnLines.add("	widgetType == \"kony.ui.ComboBox\" ||");
		fnLines.add("	widgetType == \"kony.ui.ListBox\" ||");
		fnLines.add("	widgetType == \"kony.ui.PickerView\") {");
		fnLines.add("kony.print(\"layoutManager: Master Data update supported\");");
		fnLines.add("} else {");
		fnLines.add("	kony.print(\"layoutManager: Master Data update not supported\");");
		fnLines.add("	return;");
		fnLines.add("}");
		fnLines.add("kony.print(\"layoutManager: Checking -- Is Default Layout ? \" + arLayoutManager.isDefaultLayout());");
		fnLines.add("if (!arLayoutManager.isDefaultLayout()) {");
		fnLines.add("	var reverseValues = [];");
		fnLines.add("	try {");
		fnLines.add("		for (var i=(values.length-1);i>=0;i--) {");
		fnLines.add("			reverseValues.push(values[i]);");
		fnLines.add("		}");
		fnLines.add("	kony.print(\"layoutManager: This is Reverse Layout. Assigning master data in Reverse order\");");
		fnLines.add("	widgetObj.masterData = reverseValues;");
		fnLines.add("	}catch (e) {");
		fnLines.add("	// swallow- do nothing");
		fnLines.add("	kony.print(\"layoutManager: This is Reverse Layout. Assigning master data in AS IS order because of exception \" + e);");
		fnLines.add("		widgetObj.masterData = values;");
		fnLines.add("	}");
		fnLines.add("}else {");
		fnLines.add("	kony.print(\"layoutManager: This is Default Layout. Assigning master data in AS IS order\");");
		fnLines.add("	widgetObj.masterData = values;");
		fnLines.add("}");
		fnLines.add("}");
		
		fnLines.add("\n");
		return fnLines;
	}
	
	private static List<String> generateSliderFunctions() {
		List <String> fnLines = new ArrayList<String>();
		fnLines.add("\n");
		fnLines.add("layoutManager.getSliderSelectedValue = function(sliderObj){");
		fnLines.add("	var currValue = sliderObj.selectedValue;");
		fnLines.add("	if (!arLayoutManager.isDefaultLayout()) {");
		fnLines.add("		var stepVal = sliderObj.step;");
		fnLines.add("		var minVal = sliderObj.min;");
		fnLines.add("		var maxVal = sliderObj.max;");
		fnLines.add("		var reverseValue = maxVal - (currValue-minVal);");
		fnLines.add("		kony.print(\"layoutManager: Reverse Layout. So returning reverse value: \" + reverseValue);");
		fnLines.add("		return reverseValue;");
		fnLines.add("	} else {");
		fnLines.add("		kony.print(\"layoutManager: Default Layout. So returning actual value: \" + currValue);");
		fnLines.add("		return currValue;");
		fnLines.add("	}");
		fnLines.add("}");
		fnLines.add("layoutManager.updateSliderSelectedValue = function(sliderObj, newValue){");
		fnLines.add("	var stepVal = sliderObj.step;");
		fnLines.add("	var minVal = sliderObj.min;");
		fnLines.add("	var maxVal = sliderObj.max;");
		fnLines.add("	if (newValue<minVal || newValue>maxVal) {");
		fnLines.add("		kony.print(\"Cannot udpate slider value. \" + newValue +\" not within min-max limits of the slider : \" +minVal+\"-\"+maxVal);");
		fnLines.add("		return;");
		fnLines.add("	}");
		fnLines.add("	if (!arLayoutManager.isDefaultLayout()) {");
		fnLines.add("		var reverseValue = maxVal - (newValue-minVal);");
		fnLines.add("		kony.print(\"layoutManager: Reverse Layout. So updating reverse value: \" + reverseValue);");		
		fnLines.add("		sliderObj.selectedValue = reverseValue;");
		fnLines.add("	} else {");
		fnLines.add("		kony.print(\"layoutManager: Default Layout. So updating actual value: \" + newValue);");
		fnLines.add("		sliderObj.selectedValue = newValue;");
		fnLines.add("	}");
		fnLines.add("}");
		
		fnLines.add("\n");
		return fnLines;
	}	
	
	private static List<String> generateSegPageViewUpdateFunction() {
		List <String> fnLines = new ArrayList<String>();
		fnLines.add("\n");
		fnLines.add("layoutManager.updatePageViewSegmentData = function(segmentObj, values) {");
		fnLines.add("var widgetType = \"\";");
		fnLines.add("try {");
		fnLines.add("	widgetType = kony.type(segmentObj);");
		fnLines.add("}catch (e) {	");
		fnLines.add("	//swallow, nothing can be done");
		fnLines.add("	kony.print(\"layoutManager: Cannot Update Segment Data. Error : \" + e);");
		fnLines.add("	return;");
		fnLines.add("}");
		fnLines.add("kony.print(\"layoutManager: Master Data update requested for widget type : \" + widgetType + \" values:\" + JSON.stringify(values));");
		fnLines.add("if (widgetType == \"kony.ui.SegmentUI2\") {");
		fnLines.add("	kony.print(\"layoutManager: Master Data update supported for the segment/view\");");
		fnLines.add("} else {");
		fnLines.add("	kony.print(\"layoutManager: Master Data update not needed for the widget type/view type\");");
		fnLines.add("	return;");
		fnLines.add("}");
		fnLines.add("kony.print(\"layoutManager: Checking -- Is Default Layout ? \" + arLayoutManager.isDefaultLayout());");
		fnLines.add("if (!arLayoutManager.isDefaultLayout()) {");
		fnLines.add("	var reverseValues = [];");
		fnLines.add("	try {");
		fnLines.add("		for (var i=(values.length-1);i>=0;i--) {");
		fnLines.add("			reverseValues.push(values[i]);");
		fnLines.add("		}");
		fnLines.add("	kony.print(\"layoutManager: This is Reverse Layout. Assigning master data in Reverse order\");");
		fnLines.add("	segmentObj.setData(reverseValues);");
		fnLines.add("	}catch (e) {");
		fnLines.add("	// swallow- do nothing");
		fnLines.add("	kony.print(\"layoutManager: This is Reverse Layout. Assigning master data in AS IS order because of exception \" + e);");
		fnLines.add("		segmentObj.setData(values);");
		fnLines.add("	}");
		fnLines.add("}else {");
		fnLines.add("	kony.print(\"layoutManager: This is Default Layout. Assigning master data in AS IS order\");");
		fnLines.add("	segmentObj.setData(values);");
		fnLines.add("}");
		fnLines.add("}");

		fnLines.add("\n");
		return fnLines;
	}

	private static List<String> generateSliderSkinUpdateFunction() {
		List <String> fnLines = new ArrayList<String>();
		fnLines.add("\n");
		fnLines.add("layoutManager.updateSliderLeftRightSkins = function(sliderObj, leftSkinValue, rightSkinValue) {");
		fnLines.add("	if (!arLayoutManager.isDefaultLayout()) {");
		fnLines.add("		kony.print(\"layoutManager: This is Reverse Layout. Assigning left/right skins in reverse order\");");
		fnLines.add("		sliderObj.leftSkin = rightSkinValue;");
		fnLines.add("		sliderObj.rightSkin = leftSkinValue;");
		fnLines.add("	}else {");
		fnLines.add("		kony.print(\"layoutManager: This is Default Layout. Assigning left/right skins in AS IS order\");");
		fnLines.add("		sliderObj.leftSkin = leftSkinValue;");
		fnLines.add("		sliderObj.rightSkin = rightSkinValue;");
		fnLines.add("	} ");
		fnLines.add("}");

		fnLines.add("\n");
		return fnLines;
	}
	
	public static void printWarnings() {
		for (String warning : _warnings) {
			if(ReviewModeConstants.TESTING_MODE_FLAG) {
				System.out.println(warning);
			} else {
				TAGToolsUtil.printToConsole(warning);
			}
		}
	}
}

	
