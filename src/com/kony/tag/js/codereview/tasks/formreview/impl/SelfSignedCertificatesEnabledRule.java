package com.kony.tag.js.codereview.tasks.formreview.impl;

import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.TagToolException;
import com.kony.tag.config.WidgetVo;
import com.kony.tag.js.codereview.base.FormReviewMasterTask;
import com.kony.tag.util.ProjectPropertiesReader;

public class SelfSignedCertificatesEnabledRule extends FormReviewMasterTask {
	
	public static final String PROPS_RULE_NAME = "rules.app.security.001.self_signed_cert_enabled";
	public static final String RULE_ID = "SEC-001";
	
	private static final String ERR_SECURITY_ANDROID = "Allow self signed certificates property is set to \"All\" for Android. This is a security issue and will allow any self signed certificate to be trusted. Change the option to \"None\" or \"Allow Bundled\" through IDE";
	private static final String ERR_SECURITY_ANDROID_NONE = "Allow self signed certificates property is set to \"None\" for Android. This ensures that the certificate would be validated against the device trust store.";
	private static final String ERR_SECURITY_IPHONE = "Allow self signed certificates property is set to \"true\" for IPhone. This is a security issue and will allow any self signed certificated to be trusted. Change the option to \"false\" through IDE";
	
	private static final String ERR_SHORT_DESC = "Self signed certificates are being allowed";
	private static final String ALL = "All";
	private static final String NONE = "None";
	private static final String BUNDLED = "Allow Bundled";

	public SelfSignedCertificatesEnabledRule(ProjectConfig codeReviewConfig) {
		super(codeReviewConfig);
		
		String[] ruleDescriptions = {ERR_SHORT_DESC};
		super.init(PROPS_RULE_NAME,RULE_ID,ruleDescriptions);			
	}

	@Override
	protected void review(WidgetVo formWidgetsVo) throws TagToolException {
		
		String androidSelfCertificateValue = ProjectPropertiesReader.getProperty(PROP_ANDROID_SELF_SIGNED_CERT);
		System.out.println("androidSelfCertificateValue: "+androidSelfCertificateValue);
		if(androidSelfCertificateValue != null && androidSelfCertificateValue.equals(ALL)){
			addError(ERR_SECURITY_ANDROID,ERR_SHORT_DESC, "Project Properties xml", "Android", 0, SEV_CRITICAL, RULE_ID);
		}
		if(androidSelfCertificateValue != null && androidSelfCertificateValue.equals(NONE)){
			addWarning(ERR_SECURITY_ANDROID_NONE, ERR_SHORT_DESC, "Project Properties xml", "Android", 0, SEV_MED, RULE_ID);
		}
		String iphoneSelfCertificateValue = ProjectPropertiesReader.getProperty(PROP_IPHONE_SELF_SIGNED_CERT);
		System.out.println("iphoneSelfCertificateValue: "+iphoneSelfCertificateValue);
		
		if(iphoneSelfCertificateValue != null && iphoneSelfCertificateValue.equals("true")){
			addError(ERR_SECURITY_IPHONE,ERR_SHORT_DESC, "Project Properties xml", "IPhone", 0, SEV_CRITICAL, RULE_ID);
		}
	}
	
}
