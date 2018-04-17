package com.kony.tag.util;

import java.util.HashMap;
import java.util.Map;

import com.kony.tag.arabiclayout.impl.ArabicAlignmentsController;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.tasks.formreview.impl.BlankI18KeysRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.BlockUISkinDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ButtonImageBackgroundRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ButtonProgressIndicatorEnabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.CodeSnippetsRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.FocusUISkinDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.HardcodedWidgetTextRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.IdleTimeoutDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.InvalidModuleNamesRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.LastWidgetBrowserMapRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.NestedContainerWidgetRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ScreenLevelWidgetRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SecureSubmitDisabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SegmentOrientationRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SelfSignedCertificatesEnabledRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SingleBrowserMapWidgetsRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.SingleWidgetRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.ThresholdWidgetCountRule;
import com.kony.tag.js.codereview.tasks.formreview.impl.UnusedItemsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.AlertDisplayRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.FunctionLevelCommentsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.FunctionSizeRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.GlobalVariablesRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.HardCodedStringsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.MultipleReturnsPerFunctionRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.MultipleTimersRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.NetworkAPIWrapperRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.SegmentUpdateInLoopRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedFunctionsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedI18Rule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedImagesRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedSkinsRule;
import com.kony.tag.tasks.base.CodeReviewMasterTask;

public class RuleFactory {
	
	private static Map<String, CodeReviewMasterTask> rulesMap = null;
	
	public static void init() {
			rulesMap = null;
	}
	
	public static CodeReviewMasterTask getCodeReviewRuleInstance(String ruleName, ProjectConfig codeReviewConfig) throws TagToolException{
		CodeReviewMasterTask codeReviewMasterTask = null;
		
		if (null == rulesMap) {
			rulesMap = new HashMap<String, CodeReviewMasterTask>();
		}
		
		codeReviewMasterTask = rulesMap.get(ruleName);
		
		if (codeReviewMasterTask == null) {
			codeReviewMasterTask = getRuleInstance(ruleName,codeReviewConfig);
			if (null != codeReviewMasterTask) {
				rulesMap.put(ruleName, codeReviewMasterTask);
			}
		}
		
		return codeReviewMasterTask;
	}

	private static CodeReviewMasterTask getRuleInstance(String ruleName, ProjectConfig projectConfig) throws TagToolException{
		
		CodeReviewMasterTask codeReviewTask = null;
		if (ruleName.equals(ArabicAlignmentsController.PROPS_RULE_NAME)) {
			codeReviewTask = new ArabicAlignmentsController(projectConfig);
		} else if (ruleName.equals(FunctionSizeRule.PROPS_RULE_NAME)) {
			codeReviewTask = new FunctionSizeRule(projectConfig);
		} else if (ruleName.equals(HardCodedStringsRule.PROPS_RULE_NAME)) {
			codeReviewTask = new HardCodedStringsRule(projectConfig);
		} else if (ruleName.equals(AlertDisplayRule.PROPS_RULE_NAME)) {
			codeReviewTask = new AlertDisplayRule(projectConfig);
		} else if (ruleName.equals(SegmentUpdateInLoopRule.PROPS_RULE_NAME)) {
			codeReviewTask = new SegmentUpdateInLoopRule(projectConfig);
		} else if (ruleName.equals(MultipleReturnsPerFunctionRule.PROPS_RULE_NAME)) {
			codeReviewTask = new MultipleReturnsPerFunctionRule(projectConfig);
		} else if (ruleName.equals(NetworkAPIWrapperRule.PROPS_RULE_NAME)) {
			codeReviewTask = new NetworkAPIWrapperRule(projectConfig);
		} else if (ruleName.equals(FunctionLevelCommentsRule.PROPS_RULE_NAME)) {
			codeReviewTask = new FunctionLevelCommentsRule(projectConfig);
		} else if (ruleName.equals(MultipleTimersRule.PROPS_RULE_NAME)) {
			codeReviewTask = new MultipleTimersRule(projectConfig);
		} else if (ruleName.equals(BlankI18KeysRule.PROPS_RULE_NAME)) {
			codeReviewTask = new BlankI18KeysRule(projectConfig);
		} else if (ruleName.equals(SecureSubmitDisabledRule.PROPS_RULE_NAME)) {
			codeReviewTask = new SecureSubmitDisabledRule(projectConfig);
		} else if (ruleName.equals(IdleTimeoutDisabledRule.PROPS_RULE_NAME)) {
			codeReviewTask = new IdleTimeoutDisabledRule(projectConfig);
		} else if (ruleName.equals(BlockUISkinDisabledRule.PROPS_RULE_NAME)) {
			codeReviewTask = new BlockUISkinDisabledRule(projectConfig);
		} else if (ruleName.equals(FocusUISkinDisabledRule.PROPS_RULE_NAME)) {
			codeReviewTask = new FocusUISkinDisabledRule(projectConfig);
		} else if (ruleName.equals(SingleWidgetRule.PROPS_RULE_NAME)) {
			codeReviewTask = new SingleWidgetRule(projectConfig);
		} else if (ruleName.equals(ScreenLevelWidgetRule.PROPS_RULE_NAME)) {
			codeReviewTask = new ScreenLevelWidgetRule(projectConfig);
		} else if (ruleName.equals(SegmentOrientationRule.PROPS_RULE_NAME)) {
			codeReviewTask = new SegmentOrientationRule(projectConfig);
		} else if (ruleName.equals(NestedContainerWidgetRule.PROPS_RULE_NAME)) {
			codeReviewTask = new NestedContainerWidgetRule(projectConfig);
		} else if (ruleName.equals(SingleBrowserMapWidgetsRule.PROPS_RULE_NAME)) {
			codeReviewTask = new SingleBrowserMapWidgetsRule(projectConfig);
		} else if (ruleName.equals(CodeSnippetsRule.PROPS_RULE_NAME)) {
			codeReviewTask = new CodeSnippetsRule(projectConfig);
		} else if (ruleName.equals(LastWidgetBrowserMapRule.PROPS_RULE_NAME)) {
			codeReviewTask = new LastWidgetBrowserMapRule(projectConfig);
		} else if (ruleName.equals(ButtonImageBackgroundRule.PROPS_RULE_NAME)) {
			codeReviewTask = new ButtonImageBackgroundRule(projectConfig);
		} else if (ruleName.equals(ThresholdWidgetCountRule.PROPS_RULE_NAME)) {
			codeReviewTask = new ThresholdWidgetCountRule(projectConfig);
		} else if (ruleName.equals(InvalidModuleNamesRule.PROPS_RULE_NAME)) {
			codeReviewTask = new InvalidModuleNamesRule(projectConfig);
		} else if (ruleName.equals(UnusedFunctionsRule.PROPS_RULE_NAME)) {
			codeReviewTask = new UnusedFunctionsRule(projectConfig);
		} else if (ruleName.equals(UnusedItemsRule.PROPS_RULE_NAME)) {
			codeReviewTask = new UnusedItemsRule(projectConfig);
		} else if (ruleName.equals(UnusedSkinsRule.PROPS_RULE_NAME)) {
			codeReviewTask = new UnusedSkinsRule(projectConfig);
		} else if (ruleName.equals(UnusedImagesRule.PROPS_RULE_NAME)) {
			codeReviewTask = new UnusedImagesRule(projectConfig);
		} else if (ruleName.equals(UnusedI18Rule.PROPS_RULE_NAME)) {
			codeReviewTask = new UnusedI18Rule(projectConfig);
		} else if (ruleName.equals(GlobalVariablesRule.PROPS_RULE_NAME)) {
			codeReviewTask = new GlobalVariablesRule(projectConfig);
		} else if (ruleName.equals(HardcodedWidgetTextRule.PROPS_RULE_NAME)) {
			codeReviewTask = new HardcodedWidgetTextRule(projectConfig);
		} else if (ruleName.equals(ButtonProgressIndicatorEnabledRule.PROPS_RULE_NAME)) {
			codeReviewTask = new ButtonProgressIndicatorEnabledRule(projectConfig);
		} else if (ruleName.equals(SelfSignedCertificatesEnabledRule.PROPS_RULE_NAME)) {
			codeReviewTask = new SelfSignedCertificatesEnabledRule(projectConfig);
		}
		
		return codeReviewTask;
	}
}
