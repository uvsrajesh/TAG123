package com.kony.tag.js.codereview.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ConfigVO;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.base.FormReviewEngine;
import com.kony.tag.js.codereview.config.ReviewComment;
import com.kony.tag.js.codereview.config.ReviewModeConstants;
import com.kony.tag.js.codereview.tasks.copy.CleanupTask;
import com.kony.tag.js.codereview.tasks.copy.CopyFormsTask;
import com.kony.tag.js.codereview.tasks.copy.CopyModulesTask;
import com.kony.tag.js.codereview.tasks.copy.CopyProjectConfigFilesTask;
import com.kony.tag.js.codereview.tasks.copy.CopyTemplatesTask;
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
import com.kony.tag.js.codereview.util.GenericXmlContentHandler;
import com.kony.tag.js.codereview.util.KonyFormContentHandler;
import com.kony.tag.js.codereview.util.ReportsUtil;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.tasks.JSReviewEngine;
import com.kony.tag.util.FormUtil;
import com.kony.tag.util.ProjectPropertiesReader;
import com.kony.tag.util.RuleFactory;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ReviewHelper implements ProjectConstants {
	
	private List<String> _formNames = null;
	private FormUtil formUtil = new FormUtil();

	
	protected ProjectConfig fetchReviewConfiguration() {
		
		ProjectConfig codeReviewConfig = null;
		String projectLocation = null;
		
		projectLocation = JSReviewUtil.getProjectPath();
		
		// Read all this config information from projects..for now it is hard coded
		ConfigVO configVo = new ConfigVO();
		configVo.setProjectPath(ReviewModeConstants.TEST_LOCATION_PROJECT_PATH);
		configVo.setCodeBackupPath("C:\\Users\\kit249\\Desktop\\Instrumentation\\tool");
		configVo.setReviewOutputPath("C:\\Users\\kit249\\Desktop\\Instrumentation\\tool");
		ReviewPropertiesUtil.init(projectLocation);
		
		// Create the config Object and keep it ready
		codeReviewConfig = ProjectConfig.getCodeReviewConfig(configVo);
		
		// Read Project Properties
		ProjectPropertiesReader.readProjectProperties(projectLocation);
		
		// Reset the review report
 		ReportsUtil.initNewInstance(codeReviewConfig);
 		CodeReviewStatus.reset();
		return codeReviewConfig;
	}
	
	protected void copyFilesForReview(ProjectConfig codeReviewConfig) {
		CopyFormsTask copyFormsTask = new CopyFormsTask(codeReviewConfig);
		CopyModulesTask copyModulesTask = new CopyModulesTask(codeReviewConfig);
		CopyTemplatesTask copyTemplatesTask = new CopyTemplatesTask(codeReviewConfig);
		CopyProjectConfigFilesTask copyProjectConfigFilesTask = new CopyProjectConfigFilesTask(codeReviewConfig);
		
		copyFormsTask.run();
		copyModulesTask.run();
		//copyTemplatesTask.run();
		//copyProjectConfigFilesTask.run();
	}
	
	protected List<ReviewComment> executeJavaScriptReview(ProjectConfig codeReviewConfig) {
		// First Review All JS Files
		ReviewUtil reviewUtil = new ReviewUtil();
		File[] jsFiles = null;
		List<ReviewComment> allComments = new ArrayList<ReviewComment>();
		List<ReviewComment> comments = null; 
		List<String> jsFileNames = new ArrayList<String>();
		
		JSReviewEngine jsReviewEngine = new JSReviewEngine(codeReviewConfig);
		
		String[] jsFilesArray = {
				"account.js",
				"addDeviceFlow.js",
				"anchor.js",
				"ApplyS2S.js",
				"ApplyS2SIB.js",
				"ATMBranches.js",
				"billerSearch.js",
				"BillersTopupsComposite.js",
				"checkTxtField.js",
				"commonUtil.js",
				"contactUsMB.js",
				"crypto.js",
				"CryptoUtil.js",
				"CustomerAuthentication.js",
				"dynamicIBLogin.js",
				"dynamicIBMenu.js",
				"editMyProfile.js",
				"ExchangeRates.js",
				"FeedbackAndAppTour.js",
				"forceChangePwdFlow.js",
				"forceChangePwdSpa.js",
				"futurePayment.js",
				"getAccessTok.js",
				"Globals.js",
				"handleMenu.js",
				"hzRoundAbout.js",
				"i18nchange.js",
				"IBAccntFullStatement.js",
				"IBATMBranches.js",
				"IBContactUs.js",
				"IBDashboardMenuHandler.js",
				"IBDreamSAvingMAintenance.js",
				"IBeditmyprofile.js",
				"IBExchangeRates.js",
				"IBmodBillers.js",
				"IBModBPViewandEdit.js",
				"IBMyActivities.js",
				"IBPromotions.js",
				"IBRequestHistory.js",
				"IBtestBulkMessageForS2S.js",
				"IBtopUpBillers.js",
				"IButil.js",
				"InboxHistoryServiceInvokeIB.js",
				"InboxHistoryServiceInvokeMB.js",
				"konylibrary.js",
				"kpnsMB.js",
				"LoadResourceBundle.js",
				"localeChange.js",
				"localeChangeIB.js",
				"Login.js",
				"LoginProcessService.js",
				"MBAccountStatement.js",
				"MBDreamsaving.js",
				"MBModBPViewAndEdit.js",
				"MBRequestHistory.js",
				"MobIBEditFutureTopUP.js",
				"mobIBSSService.js",
				"ModAccIB.js",
				"modAccontSummarySwipe.js",
				"modAccounSummaryCallService.js",
				"modAccountDetails.js",
				"modApplyInternetBanking.js",
				"modApplyServices.js",
				"modAppMenu.js",
				"modBeepAndBill.js",
				"modBeepAndBillIB.js",
				"modBillPayment.js",
				"modChequeServiceIB.js",
				"modConnectAcc.js",
				"modDreamSavingsMnt.js",
				"modFrmAfterLogout.js",
				"modGeneratePDFService.js",
				"modIBAccountSummary.js",
				"modIBActivateBanking.js",
				"modIBApplyServices.js",
				"modIBBeepAndBill.js",
				"modIBBillPayment.js",
				"modIBChngPwd.js",
				"ModIBEditFutureTopUP.js",
				"modIBEditFutureTransfer.js",
				"modIBLogin.js",
				"modIBMenu.js",
				"modIBMyAccount.js",
				"modIphoneffi.js",
				"modMBBiller.js",
				"modMBEditFutureTransfer.js",
				"modMBFT.js",
				"modMBMyRecipients.js",
				"modMenuMB.js",
				"modMIBCalendarShowTxns.js",
				"modMIBMyActivities.js",
				"modMIBMyActivitiesCalendar.js",
				"modMyAccount.js",
				"modMyBillers.js",
				"modMyRecipients.js",
				"modMyRecipientsIB.js",
				"modOpenAccount.js",
				"modOpenActIB.js",
				"modOpenActMB.js",
				"modOTPSpa.js",
				"modPayCCFromAccountSummaryMB.js",
				"modPayCreditFromAccountSummaryIB.js",
				"modPayLoanFromAccountSummaryIB.js",
				"modPayLoanFromAccountSummaryMB.js",
				"modPopup.js",
				"modReturnChequeServiceIB.js",
				"modSaveInSession.js",
				"modSecurityAndroid.js",
				"modSendToSaveComposite.js",
				"ModSpaOTPValidation.js",
				"modTest.js",
				"modTopUpFunc.js",
				"modTopUpStaticDataIB.js",
				"modTransferFuture.js",
				"modTransferRecipientFlow.js",
				"modTransfers.js",
				"modVerifyChangePwd.js",
				"modXfer.js",
				"MyTopUpList.js",
				"NotificationHistoryServiceInvokeIB.js",
				"NotificationHistoryServiceInvokeMB.js",
				"OpenAccountsComposite.js",
				"passIBStrength.js",
				"passStrength.js",
				"passwordStrength.js",
				"preShowForAllForms.js",
				"profiler.js",
				"profilerResults.js",
				"Promotions.js",
				"securityCheck.js",
				"sendToSave.js",
				"spaChangePWD.js",
				"SpaChanguseridpwd.js",
				"SPALogin.js",
				"spaUtils.js",
				"splitXferIB.js",
				"srvIBActivation.js",
				"srvIBChangeUserID.js",
				"srvMyAccount.js",
				"svcChequeService.js",
				"svcIBChangePWD.js",
				"SVCModIBBillPayment.js",
				"SVCModIBMYBillTopUp.js",
				"SVCModIBSelectBillTopUp.js",
				"SVCmodIBTopup.js",
				"SVCModIBTransfersNow.js",
				"SVCModMBBillPayment.js",
				"SVCModMBBillPayment1.js",
				"SVCModMBBillPaymentConfirm.js",
				"SVCModMBMYBillTopUp.js",
				"SVCModMBTopUpPaymentConfirm.js",
				"svcTransfers.js",
				"TAGCodePreview.js",
				"TAGLogger.js",
				"timer.js",
				"TMBIBRcModuleVars.js",
				"TokenActivationIB.js",
				"TokenActivationSpa.js",
				"topUpSearch.js",
				"TransApprove.js",
				"TransUtil.js",
				"UpGradeAppVersion.js",
				"Util.js",
				"validations.js"
				};
		ArrayList<String> fileList = new ArrayList<String>(Arrays.asList(jsFilesArray));
		 // This is a JS project. Review All JS Files
			for(int k=0; k<1;k++){
				jsFiles = reviewUtil.fetchFiles("C:\\Users\\kit249\\Desktop\\Instrumentation\\tool\\"+ReviewModeConstants.INSTRUMENT_ON);
				ArrayList<String> callBacks = new ArrayList<String>();
				codeReviewConfig.setCallBackArray(callBacks);
				codeReviewConfig.setJsFilesArray(new ArrayList<String>(Arrays.asList(jsFilesArray)));
				ArrayList<String> arr = codeReviewConfig.getJsFilesArray();
				codeReviewConfig.setInstrumentMode(ReviewModeConstants.INSTRUMENT_ON);
				for (int passNumber=1; passNumber<=2; passNumber++) {
					for(File reviewFile : jsFiles) {
						if((ReviewModeConstants.INSTRUMENT_ON.equals("forms") && fileList.contains(reviewFile.getName()))
								|| (ReviewModeConstants.INSTRUMENT_ON.contains("modules") && !fileList.contains(reviewFile.getName()))){
							continue;
						}
						if (!reviewFile.getName().toLowerCase().endsWith(".js")) {
							continue;
						}
						String fName = reviewFile.getName().toLowerCase();
						//System.out.println("fileName: "+fName);
						if (fName.equalsIgnoreCase("application.js") || fName.equalsIgnoreCase("appskins.js") || fName.equalsIgnoreCase("profiler.js")
								|| fName.endsWith("globalsequences.js") || fName.equalsIgnoreCase("startup.js") || 
								fName.equalsIgnoreCase("frmEventInfo.js") || fName.equalsIgnoreCase("frmFunctionInfo.js") || fName.equalsIgnoreCase("profilerResults.js")
								|| fName.equalsIgnoreCase("TAGCodePreview.js") || fName.equalsIgnoreCase("TAGCPServerDetailsPopup.js") || fName.equalsIgnoreCase("TAGLogger.js")) {
							System.out.println("Skipping Review of " + reviewFile.getName() + "");
							CodeReviewStatus.getInstance().addErrorMessage("Skipping Review of " + reviewFile.getName() + "");
							continue;
						}
						if (reviewFile.getName().toLowerCase().startsWith("kony")) {
							if (passNumber==1) {
								CodeReviewStatus.getInstance().addErrorMessage("Skipping Review of " + reviewFile.getName() + " presuming it to be Kony specific library");
							}
							continue;
						}

						
						if (passNumber == 1) {
							jsFileNames.add(reviewFile.getName());
						}
						
						// Review Each Java Script file for all JS Rules
						try {

						// Rule #JS-001 : No. of lines in a function should be within a threshold limit
						jsReviewEngine.execute(reviewFile, passNumber);
						//comments = jsReviewEngine.execute(reviewFile);
						//consolidateComments(allComments, comments);
						
						} catch (TagToolException cdrExcp) {
							CodeReviewStatus.getInstance().addErrorMessage(cdrExcp,"JS Review Pass# " + passNumber + " : " + cdrExcp.getErrorMessage());
						} catch (Exception excp) {
							CodeReviewStatus.getInstance().addErrorMessage(excp,"JS Review Pass# " + passNumber + " : " + excp.toString());
						}					
					}
				}
			}
			
			
				/*try {
					AlertDisplayRule alertDisplayRule = (AlertDisplayRule) RuleFactory.getCodeReviewRuleInstance(AlertDisplayRule.PROPS_RULE_NAME,codeReviewConfig);
					GlobalVariablesRule globalVarRule = (GlobalVariablesRule) RuleFactory.getCodeReviewRuleInstance(GlobalVariablesRule.PROPS_RULE_NAME,codeReviewConfig);
					FunctionSizeRule functionSizeRule = (FunctionSizeRule) RuleFactory.getCodeReviewRuleInstance(FunctionSizeRule.PROPS_RULE_NAME,codeReviewConfig);
					HardCodedStringsRule hardCodedStringsRule = (HardCodedStringsRule) RuleFactory.getCodeReviewRuleInstance(HardCodedStringsRule.PROPS_RULE_NAME,codeReviewConfig);
					SegmentUpdateInLoopRule segmentUpdateInLoopRule = (SegmentUpdateInLoopRule) RuleFactory.getCodeReviewRuleInstance(SegmentUpdateInLoopRule.PROPS_RULE_NAME,codeReviewConfig);
					MultipleReturnsPerFunctionRule multipleReturnsPerFunctionRule = (MultipleReturnsPerFunctionRule) RuleFactory.getCodeReviewRuleInstance(MultipleReturnsPerFunctionRule.PROPS_RULE_NAME,codeReviewConfig);
					NetworkAPIWrapperRule networkAPIWrapperRule = (NetworkAPIWrapperRule) RuleFactory.getCodeReviewRuleInstance(NetworkAPIWrapperRule.PROPS_RULE_NAME,codeReviewConfig);
					FunctionLevelCommentsRule functionLevelCommentsRule = (FunctionLevelCommentsRule) RuleFactory.getCodeReviewRuleInstance(FunctionLevelCommentsRule.PROPS_RULE_NAME,codeReviewConfig);
					MultipleTimersRule multipleTimersRule = (MultipleTimersRule) RuleFactory.getCodeReviewRuleInstance(MultipleTimersRule.PROPS_RULE_NAME,codeReviewConfig);

					// This will be used later in form review to check module names against form names
					InvalidModuleNamesRule invalidModuleNamesRule = (InvalidModuleNamesRule) RuleFactory.getCodeReviewRuleInstance(InvalidModuleNamesRule.PROPS_RULE_NAME,codeReviewConfig);
					invalidModuleNamesRule.setAllModuleNames(jsFileNames);
					
					//alertDisplayRule.reviewJSLine("", 0,0, "", 2);
					
					// Consolidate the comments of all the Rules
					comments = functionSizeRule.getComments();
					consolidateComments(allComments, comments);
					
					comments = hardCodedStringsRule.getComments();
					consolidateComments(allComments, comments);
					
					comments = alertDisplayRule.getComments();
					consolidateComments(allComments, comments);
					
					comments = globalVarRule.getComments();
					consolidateComments(allComments, comments);
					
					comments = segmentUpdateInLoopRule.getComments();
					consolidateComments(allComments, comments);

					comments = multipleReturnsPerFunctionRule.getComments();
					consolidateComments(allComments, comments);
					
					networkAPIWrapperRule.updateComments();
					comments = networkAPIWrapperRule.getComments();
					consolidateComments(allComments, comments);
					
					comments = functionLevelCommentsRule.getComments();
					consolidateComments(allComments, comments);
					
					multipleTimersRule.postReview();
					comments = multipleTimersRule.getComments();
					consolidateComments(allComments, comments);
					
				} catch (TagToolException e) {
					CodeReviewStatus.getInstance().addErrorMessage(e,e.getErrorMessage());
				}*/
		
		
		//System.out.println("TOTAL No. of Comments " + allComments.size());
		return allComments;
	}
	
	protected List<ReviewComment> executeFormReview(ProjectConfig codeReviewConfig) {
	// First Review All JS Files
	List<ReviewComment> allComments = new ArrayList<ReviewComment>();
	List<ReviewComment> comments = null;
	List<ReviewComment> miscComments = new ArrayList<ReviewComment>();
	
	
			_formNames = new ArrayList<String>();
			
		 // This is a JS project. Review All Forms
			reviewAllForms(codeReviewConfig, codeReviewConfig.getCodeBackupPath() + LOCATION_ALL_FORMS);
			reviewAllForms(codeReviewConfig, codeReviewConfig.getCodeBackupPath() + LOCATION_ALL_POPUPS);
			reviewAllForms(codeReviewConfig, codeReviewConfig.getCodeBackupPath() + LOCATION_ALL_TEMPLATES);
			
			// Review App Menu to verify unused functions,images,i18 keys, skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getProjectPath() + LOCATION_APP_MENU_SKINS_FILE);
			
			// Review Project Skin file to verify unused skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getProjectPath() + LOCATION_CONFIG_PROJECT_SKIN_FILE);
			
			// Review Project Events file for unused functions,images,i18 keys, skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getCodeBackupPath() + LOCATION_CONFIG_PROJECT_EVENTS_FILE);
			
			//Review Project Tablet Events file for unused functions,images,i18 keys, skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getCodeBackupPath() + LOCATION_CONFIG_PROJECT_TABLET_EVENTS_FILE);
			
			//Review Project Desktop and kiosk Events file for unused functions,images,i18 keys, skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getCodeBackupPath() + LOCATION_CONFIG_PROJECT_DESKTOP_EVENTS_FILE);
			
			//Review Global Sequence file for unused functions,images,i18 keys, skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getProjectPath() + LOCATION_CONFIG_PROJECT_SEQUENCE_FILE);
			
			//Review Global Sequence file for Tablet for unused functions,images,i18 keys, skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getProjectPath() + LOCATION_CONFIG_PROJECT_TABLET_SEQUENCE_FILE);
			
			//Review Global Sequence file for Desktop for unused functions,images,i18 keys, skins etc
			reviewGenericProjectXml(codeReviewConfig, codeReviewConfig.getProjectPath() + LOCATION_CONFIG_PROJECT_DESKTOP_SEQUENCE_FILE);

			try {
				BlankI18KeysRule blankI18KeysRule = (BlankI18KeysRule) RuleFactory.getCodeReviewRuleInstance(BlankI18KeysRule.PROPS_RULE_NAME,codeReviewConfig);
				SecureSubmitDisabledRule secureSubmitDisabledRule = (SecureSubmitDisabledRule) RuleFactory.getCodeReviewRuleInstance(SecureSubmitDisabledRule.PROPS_RULE_NAME,codeReviewConfig);
				IdleTimeoutDisabledRule idleTimeoutDisabledRule = (IdleTimeoutDisabledRule) RuleFactory.getCodeReviewRuleInstance(IdleTimeoutDisabledRule.PROPS_RULE_NAME,codeReviewConfig);
				BlockUISkinDisabledRule blockUISkinDisabledRule = (BlockUISkinDisabledRule) RuleFactory.getCodeReviewRuleInstance(BlockUISkinDisabledRule.PROPS_RULE_NAME,codeReviewConfig);
				FocusUISkinDisabledRule focusUISkinDisabledRule = (FocusUISkinDisabledRule) RuleFactory.getCodeReviewRuleInstance(FocusUISkinDisabledRule.PROPS_RULE_NAME,codeReviewConfig);
				SingleWidgetRule singleWidgetRule = (SingleWidgetRule) RuleFactory.getCodeReviewRuleInstance(SingleWidgetRule.PROPS_RULE_NAME, codeReviewConfig);
				ScreenLevelWidgetRule screenLevelWidgetRule = (ScreenLevelWidgetRule) RuleFactory.getCodeReviewRuleInstance(ScreenLevelWidgetRule.PROPS_RULE_NAME, codeReviewConfig);
				SegmentOrientationRule segmentOrientationRule = (SegmentOrientationRule) RuleFactory.getCodeReviewRuleInstance(SegmentOrientationRule.PROPS_RULE_NAME, codeReviewConfig);
				NestedContainerWidgetRule nestedContainerWidgetRule = (NestedContainerWidgetRule) RuleFactory.getCodeReviewRuleInstance(NestedContainerWidgetRule.PROPS_RULE_NAME, codeReviewConfig);
				SingleBrowserMapWidgetsRule singleBrowserMapWidgetsRule = (SingleBrowserMapWidgetsRule) RuleFactory.getCodeReviewRuleInstance(SingleBrowserMapWidgetsRule.PROPS_RULE_NAME, codeReviewConfig);
				CodeSnippetsRule codeSnippetsRule = (CodeSnippetsRule) RuleFactory.getCodeReviewRuleInstance(CodeSnippetsRule.PROPS_RULE_NAME, codeReviewConfig);
				LastWidgetBrowserMapRule lastWidgetBrowserMapRule = (LastWidgetBrowserMapRule) RuleFactory.getCodeReviewRuleInstance(LastWidgetBrowserMapRule.PROPS_RULE_NAME, codeReviewConfig);
				ButtonImageBackgroundRule buttonImageBackgroundRule = (ButtonImageBackgroundRule) RuleFactory.getCodeReviewRuleInstance(ButtonImageBackgroundRule.PROPS_RULE_NAME, codeReviewConfig);
				ThresholdWidgetCountRule thresholdWidgetCountRule = (ThresholdWidgetCountRule) RuleFactory.getCodeReviewRuleInstance(ThresholdWidgetCountRule.PROPS_RULE_NAME, codeReviewConfig);
				HardcodedWidgetTextRule hardcodedTextForI18keysRule = (HardcodedWidgetTextRule)RuleFactory.getCodeReviewRuleInstance(HardcodedWidgetTextRule.PROPS_RULE_NAME, codeReviewConfig);
				InvalidModuleNamesRule invalidModuleNamesRule = (InvalidModuleNamesRule) RuleFactory.getCodeReviewRuleInstance(InvalidModuleNamesRule.PROPS_RULE_NAME,codeReviewConfig);
				ButtonProgressIndicatorEnabledRule btnProgressIndicatorEnabledRule = (ButtonProgressIndicatorEnabledRule) RuleFactory.getCodeReviewRuleInstance(ButtonProgressIndicatorEnabledRule.PROPS_RULE_NAME,codeReviewConfig);
				SelfSignedCertificatesEnabledRule selfSignedCertificateEnabledRule = (SelfSignedCertificatesEnabledRule) RuleFactory.getCodeReviewRuleInstance(SelfSignedCertificatesEnabledRule.PROPS_RULE_NAME, codeReviewConfig);
				invalidModuleNamesRule.setAllFormNames(_formNames);

				
				UnusedImagesRule unusedImagesRule = (UnusedImagesRule) RuleFactory.getCodeReviewRuleInstance(UnusedImagesRule.PROPS_RULE_NAME,codeReviewConfig);
				UnusedSkinsRule unusedSkinsRule = (UnusedSkinsRule) RuleFactory.getCodeReviewRuleInstance(UnusedSkinsRule.PROPS_RULE_NAME,codeReviewConfig);
				UnusedFunctionsRule unusedFunctionsRule = (UnusedFunctionsRule) RuleFactory.getCodeReviewRuleInstance(UnusedFunctionsRule.PROPS_RULE_NAME,codeReviewConfig);
				UnusedI18Rule unusedI18Rule = (UnusedI18Rule) RuleFactory.getCodeReviewRuleInstance(UnusedI18Rule.PROPS_RULE_NAME,codeReviewConfig);

				UnusedItemsRule unusedItemsRule = (UnusedItemsRule) RuleFactory.getCodeReviewRuleInstance(UnusedItemsRule.PROPS_RULE_NAME,codeReviewConfig);
				unusedItemsRule.setProjectConfig(codeReviewConfig);
				
				// Post Review Action to be executed as necessary
				unusedItemsRule.postReview();
				singleBrowserMapWidgetsRule.postReview();
					
				comments = blankI18KeysRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = secureSubmitDisabledRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = idleTimeoutDisabledRule.getComments();
				consolidateComments(allComments, comments);

				comments = blockUISkinDisabledRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = focusUISkinDisabledRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = singleWidgetRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = screenLevelWidgetRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = segmentOrientationRule.getComments();
				consolidateComments(allComments, comments);

				comments = nestedContainerWidgetRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = singleBrowserMapWidgetsRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = codeSnippetsRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = lastWidgetBrowserMapRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = buttonImageBackgroundRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = thresholdWidgetCountRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = hardcodedTextForI18keysRule.getComments();
				consolidateComments(allComments, comments);
				
				comments = btnProgressIndicatorEnabledRule.getComments();
				consolidateComments(allComments, comments);
				
				selfSignedCertificateEnabledRule.reviewForm(null);
				comments = selfSignedCertificateEnabledRule.getComments();
				ReportsUtil.getInstance().prepareSecurityReviewReport(comments);
				
				if (invalidModuleNamesRule.isEnabled()) {
					invalidModuleNamesRule.postReview();
					miscComments.addAll(invalidModuleNamesRule.getComments());
					//consolidateComments(allComments, comments);
				}

				// This is a Separate Report
				if (unusedFunctionsRule.isEnabled()) {
					unusedItemsRule.updateUnusedFunctions();
					comments = unusedItemsRule.getComments();
					ReportsUtil.getInstance().prepareUnusedFunctionsReport(comments);
				}
				
				// This is a Separate Report
				if (unusedSkinsRule.isEnabled()) {
					unusedItemsRule.updateUnusedSkins();
					comments = unusedItemsRule.getComments();
					ReportsUtil.getInstance().prepareUnusedSkinsReport(comments);
				}
				
				// This is a Separate Report
				if (unusedImagesRule.isEnabled()) {
					unusedItemsRule.updateUnusedImages();
					comments = unusedItemsRule.getComments();
					
					// Also the get the comments for Invalid Image Names 
					consolidateComments(comments, unusedImagesRule.getComments());
					ReportsUtil.getInstance().prepareUnusedImagesReport(comments);
				}
				
				// This is a Separate Report
				if (unusedI18Rule.isEnabled()) {
					unusedItemsRule.updateUnusedI18Keys();
					comments = unusedItemsRule.getComments();
					ReportsUtil.getInstance().prepareUnusedI18Report(comments);
				}
				
				ReportsUtil.getInstance().prepareMiscCommentsReport(miscComments);
				
				/*
				List<FunctionVo> unusedFunctions = unusedItemsRule.getUnusedFunctions();
				
				if (unusedFunctions.size()>0) {
					for (FunctionVo function : unusedFunctions) {
						System.out.println("UNUSED FUNCTION : " + function.getFunctionName());
					}
				} else {
					System.out.println("NO UNUSED FUNCTIONS");
				}
				*/
			} catch (TagToolException e) {
				CodeReviewStatus.getInstance().addErrorMessage(e,e.getErrorMessage());
			}
	
		// System.out.println("TOTAL No. of Comments " + allComments.size());
		return allComments;
	}
	
	protected void reviewAllForms(ProjectConfig codeReviewConfig, String directoryName) {
		// First Review All JS Files
		ReviewUtil reviewUtil = new ReviewUtil();
		File[] formFiles = null;
		KonyFormContentHandler formHandler = new KonyFormContentHandler(codeReviewConfig);
		FormReviewEngine formReviewEngine = new FormReviewEngine(codeReviewConfig);
		
		 // This is a JS project. Review All JS Files
		formFiles = reviewUtil.fetchFiles(directoryName);
		
		if (formFiles == null || formFiles.length==0) {
			return;
		}
			
				for(File reviewFile : formFiles) {
					
					if (reviewFile.isDirectory()) {
						reviewAllForms(codeReviewConfig, reviewFile.getAbsolutePath());
						continue;
					}
					
					if (!reviewFile.getName().toLowerCase().endsWith(".kl")) {
						continue;
					}
					
					if(!_formNames.contains(reviewFile.getName())) {
						_formNames.add(reviewFile.getName());
					}
					
					// Review Each Java Script file for all JS Rules
					try {

						formHandler.init();
						
						JSReviewUtil.printToConsole("Reviewing Form : " + reviewFile.getName());
						
						// Parse the XML and read all the required Widget Info
						formUtil.readXML(reviewFile.getAbsolutePath(), formHandler);
						
						// Wait till the file is read completely
						while(!formHandler.isEndOfFileReached()) {};
						
						// Now review the form widgets
						formReviewEngine.execute(formHandler.getFormWidgetVo(),directoryName);
					
					} catch (TagToolException cdrExcp) {
						CodeReviewStatus.getInstance().addErrorMessage(cdrExcp, "Error Reviewing file " + reviewFile.getName() + " : " +cdrExcp.getErrorMessage());
					} catch (Exception excp) {
						CodeReviewStatus.getInstance().addErrorMessage(excp, "Error Reviewing file " + reviewFile.getName() + " : " +excp);
					}					
				}
		}
 
	
	
	protected void prepareReviewWorkspace(ProjectConfig codeReviewConfig) {
		// Clean up the necessary temp directories needed for review
		CleanupTask cleanupTask = new CleanupTask(codeReviewConfig);
		cleanupTask.run();
	}

	private void consolidateComments(List<ReviewComment> allComments, List<ReviewComment>moduleComments) {
		if (null != moduleComments && null != allComments && moduleComments.size() > 0) {
			allComments.addAll(moduleComments);
		}
	}
	
	private void reviewGenericProjectXml(ProjectConfig codeReviewConfig, String xmlFilePath) {

		GenericXmlContentHandler genericXmlContentHandler = new GenericXmlContentHandler(codeReviewConfig);

		genericXmlContentHandler.init();
		
		try {
			
			File file = new File(xmlFilePath);
			
			// Parse the XML and read all the required Widget Info
			if (file.exists()) {
				formUtil.readXML(xmlFilePath, genericXmlContentHandler);
				
				// Wait till the file is read completely
				while(!genericXmlContentHandler.isEndOfFileReached()) {};

			} else {
				CodeReviewStatus.getInstance().addErrorMessage("Warning: Could not need read configuration file : " + xmlFilePath);
			}
		}catch (Exception e) {
			CodeReviewStatus.getInstance().addErrorMessage(e, "Warning: Could not need read configuration file : " + xmlFilePath);
		}
	}
	
	public void printErrorsWarnings() {
		
		// Print any warnings encountered during review
		if (CodeReviewStatus.getInstance().getWarningMessages() != null && 
				CodeReviewStatus.getInstance().getWarningMessages().size()>0) {
			JSReviewUtil.printToConsole("\n >>>>>>  WARNINGS : <<<<<<");
			for (String message : CodeReviewStatus.getInstance().getWarningMessages()) {
				JSReviewUtil.printToConsole(":: " + message);
			}
			JSReviewUtil.printToConsole(" <<<<<<  Done with WARNINGS : <<<<<< \n");
		}
		
		if (CodeReviewStatus.getInstance().getErrorMessages() != null && 
				CodeReviewStatus.getInstance().getErrorMessages().size()>0) {
			JSReviewUtil.printToConsole("\n >>>>>>  ERRORS : <<<<<<");
			for (String message : CodeReviewStatus.getInstance().getErrorMessages()) {
				JSReviewUtil.printToConsole("## " + message);
			}
			JSReviewUtil.printToConsole(" <<<<<<  Done with ERRORS : <<<<<< \n");
			JSReviewUtil.printToConsole("REVIEW COMPLETED with one or more Errors. See the Error log printed above !!");
		} else {
			JSReviewUtil.printToConsole("SUCCESS !! Code Review Completed.");
		}
	}
}
