package com.kony.tag.js.codereview.base;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.tasks.formreview.impl.BlankI18KeysRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.BlockUISkinDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ButtonImageBackgroundRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ButtonProgressIndicatorEnabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.CodeSnippetsRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.FocusUISkinDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.HardcodedWidgetTextRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.IdleTimeoutDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.LastWidgetBrowserMapRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.NestedContainerWidgetRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ScreenLevelWidgetRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SecureSubmitDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SegmentOrientationRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SingleBrowserMapWidgetsRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SingleWidgetRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ThresholdWidgetCountRule;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.util.RuleFactory;

public class FormReviewEngine implements ProjectConstants {
	BlankI18KeysRule _blankI18KeysRule = null;
	SecureSubmitDisabledRule _secureSubmitEnabledRule = null;
	IdleTimeoutDisabledRule _idleTimeoutDisabledRule = null;
	BlockUISkinDisabledRule _blockUISkinDisabledRule = null;
	FocusUISkinDisabledRule _focusUISkinDisabledRule = null;
	SingleWidgetRule _singleWidgetRule = null;
	ScreenLevelWidgetRule _screenLevelWidgetRule = null;
	SegmentOrientationRule _segmentOrientationRule = null;
	NestedContainerWidgetRule _nestedContainerWidgetRule = null;
	SingleBrowserMapWidgetsRule _singleBrowserMapWidgetsRule = null;
	LastWidgetBrowserMapRule _lastWidgetBrowserMapRule = null;
	ButtonImageBackgroundRule _buttonImageBackgroundRule = null;
	ThresholdWidgetCountRule _thresholdWidgetCountRule = null;
	HardcodedWidgetTextRule _hardcodedTextForI18keysRule = null;
	CodeSnippetsRule _codeSnippetsRule = null;
	ButtonProgressIndicatorEnabledRule _btnProgressIndicatorEnabledRule = null;
//	UnusedItemsRule _unusedItemsRule = null;
	ProjectConfig _codeReviewConfig = null;
	List<String> filesList = null;
	Map<String, Map<String,Boolean>> ruleLevelExclusionMap = null;

	
	public FormReviewEngine(ProjectConfig codeReviewConfig) {
		this._codeReviewConfig = codeReviewConfig;
	}
	
	private void rulesInit(String reviewFileName, String reviewFileDirectoryName) throws TagToolException {
		_blankI18KeysRule = (BlankI18KeysRule) RuleFactory.getCodeReviewRuleInstance(BlankI18KeysRule.PROPS_RULE_NAME,_codeReviewConfig);
		_secureSubmitEnabledRule = (SecureSubmitDisabledRule) RuleFactory.getCodeReviewRuleInstance(SecureSubmitDisabledRule.PROPS_RULE_NAME, _codeReviewConfig);
		_idleTimeoutDisabledRule = (IdleTimeoutDisabledRule) RuleFactory.getCodeReviewRuleInstance(IdleTimeoutDisabledRule.PROPS_RULE_NAME, _codeReviewConfig);
		_blockUISkinDisabledRule = (BlockUISkinDisabledRule) RuleFactory.getCodeReviewRuleInstance(BlockUISkinDisabledRule.PROPS_RULE_NAME, _codeReviewConfig);
		_focusUISkinDisabledRule = (FocusUISkinDisabledRule) RuleFactory.getCodeReviewRuleInstance(FocusUISkinDisabledRule.PROPS_RULE_NAME, _codeReviewConfig);
		_singleWidgetRule = (SingleWidgetRule) RuleFactory.getCodeReviewRuleInstance(SingleWidgetRule.PROPS_RULE_NAME, _codeReviewConfig);
		_screenLevelWidgetRule = (ScreenLevelWidgetRule) RuleFactory.getCodeReviewRuleInstance(ScreenLevelWidgetRule.PROPS_RULE_NAME, _codeReviewConfig);
		_segmentOrientationRule = (SegmentOrientationRule) RuleFactory.getCodeReviewRuleInstance(SegmentOrientationRule.PROPS_RULE_NAME, _codeReviewConfig);
		_nestedContainerWidgetRule = (NestedContainerWidgetRule) RuleFactory.getCodeReviewRuleInstance(NestedContainerWidgetRule.PROPS_RULE_NAME, _codeReviewConfig);
		_singleBrowserMapWidgetsRule = (SingleBrowserMapWidgetsRule) RuleFactory.getCodeReviewRuleInstance(SingleBrowserMapWidgetsRule.PROPS_RULE_NAME, _codeReviewConfig);
		_codeSnippetsRule = (CodeSnippetsRule) RuleFactory.getCodeReviewRuleInstance(CodeSnippetsRule.PROPS_RULE_NAME, _codeReviewConfig);
		_lastWidgetBrowserMapRule = (LastWidgetBrowserMapRule) RuleFactory.getCodeReviewRuleInstance(LastWidgetBrowserMapRule.PROPS_RULE_NAME, _codeReviewConfig);
		_buttonImageBackgroundRule = (ButtonImageBackgroundRule) RuleFactory.getCodeReviewRuleInstance(ButtonImageBackgroundRule.PROPS_RULE_NAME, _codeReviewConfig);
		_thresholdWidgetCountRule = (ThresholdWidgetCountRule) RuleFactory.getCodeReviewRuleInstance(ThresholdWidgetCountRule.PROPS_RULE_NAME, _codeReviewConfig);
		_hardcodedTextForI18keysRule = (HardcodedWidgetTextRule) RuleFactory.getCodeReviewRuleInstance(HardcodedWidgetTextRule.PROPS_RULE_NAME, _codeReviewConfig);
		_btnProgressIndicatorEnabledRule = (ButtonProgressIndicatorEnabledRule) RuleFactory.getCodeReviewRuleInstance(ButtonProgressIndicatorEnabledRule.PROPS_RULE_NAME, _codeReviewConfig);
//		_unusedItemsRule = (UnusedItemsRule) RuleFactory.getCodeReviewRuleInstance(UnusedItemsRule.PROPS_RULE_NAME, _codeReviewConfig); 
		
		_blankI18KeysRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_secureSubmitEnabledRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_idleTimeoutDisabledRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_blockUISkinDisabledRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_focusUISkinDisabledRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_singleWidgetRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_screenLevelWidgetRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_segmentOrientationRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_nestedContainerWidgetRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_singleBrowserMapWidgetsRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_codeSnippetsRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_lastWidgetBrowserMapRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_buttonImageBackgroundRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_thresholdWidgetCountRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_hardcodedTextForI18keysRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		_btnProgressIndicatorEnabledRule.rulesInit(reviewFileName, reviewFileDirectoryName);
		
		filesList = null;
		ruleLevelExclusionMap = new HashMap<String, Map<String,Boolean>>();
	}
	

	public void execute(WidgetVo formWidgetsVo, String reviewFileDirectory) throws TagToolException {

		if (null == formWidgetsVo || formWidgetsVo.getWidgetType() == null ||
				formWidgetsVo.getWidgetType().equals(NONE)) {
			return;
		}
		
		rulesInit(null,reviewFileDirectory);
		
		// Reviewing against all the form rules
		if (_blankI18KeysRule.isEnabledForCurrentFormCategory() 
				&& !isExcludedFile(_blankI18KeysRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_blankI18KeysRule.reviewForm(formWidgetsVo);
		}
		if (_secureSubmitEnabledRule.isEnabledForCurrentFormCategory() 
				&& !isExcludedFile(_secureSubmitEnabledRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_secureSubmitEnabledRule.reviewForm(formWidgetsVo);
		}
		
		if (_idleTimeoutDisabledRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_idleTimeoutDisabledRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_idleTimeoutDisabledRule.reviewForm(formWidgetsVo);
		}
		
		if (_blockUISkinDisabledRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_blockUISkinDisabledRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_blockUISkinDisabledRule.reviewForm(formWidgetsVo);
		}
		
		if (_focusUISkinDisabledRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_focusUISkinDisabledRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_focusUISkinDisabledRule.reviewForm(formWidgetsVo);
		}	
		
		if (_singleWidgetRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_singleWidgetRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_singleWidgetRule.reviewForm(formWidgetsVo);
		}
		
		if (_screenLevelWidgetRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_screenLevelWidgetRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_screenLevelWidgetRule.reviewForm(formWidgetsVo);
			_screenLevelWidgetRule.postFormReview();
		}
		
		if (_segmentOrientationRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_segmentOrientationRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_segmentOrientationRule.reviewForm(formWidgetsVo);
		}
		
		if (_nestedContainerWidgetRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_nestedContainerWidgetRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_nestedContainerWidgetRule.reviewForm(formWidgetsVo);
		}
		
		if (_singleBrowserMapWidgetsRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_singleBrowserMapWidgetsRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_singleBrowserMapWidgetsRule.reviewForm(formWidgetsVo);
		}
		
		if (_codeSnippetsRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_codeSnippetsRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_codeSnippetsRule.reviewForm(formWidgetsVo);
		}
		
		if (_lastWidgetBrowserMapRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_lastWidgetBrowserMapRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_lastWidgetBrowserMapRule.reviewForm(formWidgetsVo);
		}		

		if (_buttonImageBackgroundRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_buttonImageBackgroundRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_buttonImageBackgroundRule.reviewForm(formWidgetsVo);
		}
		
		if (_thresholdWidgetCountRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_thresholdWidgetCountRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_thresholdWidgetCountRule.reviewForm(formWidgetsVo);
			_thresholdWidgetCountRule.postFormReview();
		}

		if(_hardcodedTextForI18keysRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_hardcodedTextForI18keysRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_hardcodedTextForI18keysRule.reviewForm(formWidgetsVo);
		}
		
		if(_btnProgressIndicatorEnabledRule.isEnabledForCurrentFormCategory()
				&& !isExcludedFile(_btnProgressIndicatorEnabledRule.RULE_ID,formWidgetsVo.getWidgetName())) {
			_btnProgressIndicatorEnabledRule.reviewForm(formWidgetsVo);
		}
	}
	
	private boolean isExcludedFile(String ruleId, String formName) {
		boolean isExcluded = false;
		Boolean boolObjVal = null;
		Map<String,Boolean> fileLevelExclusionMap = null;
		
		if (null != ruleId && null != formName) {
			
			ruleId = ruleId.trim().toUpperCase();
			
			formName = formName.trim().toLowerCase();
			
			fileLevelExclusionMap = ruleLevelExclusionMap.get(ruleId);
			
			if (fileLevelExclusionMap == null) {
				fileLevelExclusionMap = new HashMap<String,Boolean>();
				ruleLevelExclusionMap.put(ruleId, new HashMap<String,Boolean>());
			}
			
			boolObjVal = fileLevelExclusionMap.get(formName);
			
			if(boolObjVal != null) {
				return boolObjVal.booleanValue();
			} else {
				filesList = ReviewPropertiesUtil.getFilesToIgnore(ruleId);
				if (filesList != null && filesList.size()>0 &&  
						filesList.contains(formName)) {
					isExcluded = true;
				}
				
				fileLevelExclusionMap.put(formName, Boolean.valueOf(isExcluded));
			}
		}
		
		
		return isExcluded;
	}	
}