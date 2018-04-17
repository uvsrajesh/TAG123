package com.kony.tag.js.codereview.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConfig;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.config.TagToolException;
import com.kony.tag.js.codereview.tasks.formreview.impl.UnusedItemsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedFunctionsRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedI18Rule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedImagesRule;
import com.kony.tag.js.codereview.tasks.jsreview.impl.UnusedSkinsRule;
import com.kony.tag.util.RuleFactory;

public class GenericXmlContentHandler extends XmlContentHandler implements ProjectConstants {
	private UnusedItemsRule _unusedItemsRule = null;
	private String currentTag = null;
	private String currentAttribute = null;
	private String tmpAttributeValue = null;
	
	private UnusedFunctionsRule _unusedFunctionsRule = null;
	private UnusedSkinsRule _unusedSkinsRule = null;
	private UnusedImagesRule _unusedImagesRule = null;
	private UnusedI18Rule _unusedI18Rule = null;
	
	
	private ProjectConfig  codeReviewConfig = null;
	
	private GenericXmlContentHandler() {
		// Do not use
	}
	
	public GenericXmlContentHandler(ProjectConfig codeReviewConfig) {
		this.codeReviewConfig = codeReviewConfig;
		init();
	}
	
	public void init() {
		super.init(); 
		
		currentTag = null;
		currentAttribute = null;
		tmpAttributeValue = null;		
				 
		 if (_unusedItemsRule == null) {
			 try {
				_unusedFunctionsRule = (UnusedFunctionsRule) RuleFactory.getCodeReviewRuleInstance(UnusedFunctionsRule.PROPS_RULE_NAME,codeReviewConfig);
				_unusedSkinsRule = (UnusedSkinsRule) RuleFactory.getCodeReviewRuleInstance(UnusedSkinsRule.PROPS_RULE_NAME,codeReviewConfig);
				_unusedImagesRule = (UnusedImagesRule) RuleFactory.getCodeReviewRuleInstance(UnusedImagesRule.PROPS_RULE_NAME,codeReviewConfig);
				_unusedI18Rule = (UnusedI18Rule) RuleFactory.getCodeReviewRuleInstance(UnusedI18Rule.PROPS_RULE_NAME,codeReviewConfig);
				_unusedItemsRule = (UnusedItemsRule) RuleFactory.getCodeReviewRuleInstance(UnusedItemsRule.PROPS_RULE_NAME, codeReviewConfig);
			 } catch (TagToolException excp) {
				 CodeReviewStatus.getInstance().addErrorMessage(excp, excp.getErrorMessage());
			 }
		 }
	}
	

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		//System.out.println("Attribute : " + new String(ch, start, length));
		
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
		if (null == currentTag){
			return;
		}

		if (null == currentAttribute || currentAttribute.trim().length()==0) {
			currentAttribute = NONE;
		} else {
			currentAttribute = currentAttribute.trim();
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
		//System.out.println("End Element :" + qName);
	}

	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		currentTag = qName;
		currentAttribute = null;
		
		if(currentTag.equalsIgnoreCase("skin"))
		{
			String value = attributes.getValue("value");
			if((value != null) && (value.trim().length() > 0))
			{
				// Apply any misc code review rules	
				if(_unusedSkinsRule != null && _unusedSkinsRule.isEnabled()) {
					_unusedItemsRule.checkForUsedSkins(value);
				}
			}
		}
	}
}
