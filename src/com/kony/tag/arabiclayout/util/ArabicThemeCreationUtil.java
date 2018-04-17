package com.kony.tag.arabiclayout.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.kony.tag.config.CodeReviewStatus;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.js.codereview.util.ReviewPropertiesUtil;
import com.kony.tag.util.TAGToolsUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ArabicThemeCreationUtil implements ProjectConstants{
	
	public void updateFontNames(String destFile)
	{
 
	    try { 
	    	HashMap<String, String> fontNameMap = getFontNameMap();
	        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse(destFile);

	        boolean lIsNodeSet = false;
	        NodeList lSkinsList = doc.getElementsByTagName("skin");
	        if (lSkinsList != null)
	        {
	        	for (int i = 0; i < lSkinsList.getLength(); i++)
	        	{
	        		Node lAttributesNode = lSkinsList.item(i);
	        		NodeList attributesList = lAttributesNode.getChildNodes();
	        		for(int j=0;j<attributesList.getLength();j++){
        				Node attribute = attributesList.item(j);
		        		if(attribute.getNodeType() == Node.ELEMENT_NODE) {
		        			Element fontElement = (Element)attribute;
		        			Attr attr = fontElement.getAttributeNode("name");
		        			if(attr != null){
		        				String attributeName = attr.getNodeValue();
		        				if(attributeName.equalsIgnoreCase(KEYWORD_FONTNAME)) {
		        					Attr attrValue = fontElement.getAttributeNode("value");
		        					if(attrValue != null) {
		        						String fontMap = attrValue.getNodeValue();
				        				
				        				if(fontMap != null && !fontMap.equals("") && fontMap.indexOf(",") > 0) {
				        					int index = fontMap.indexOf(",");
				        					String fontName = fontMap.substring(index + 1);
				        					JSReviewUtil.printToConsole("fontMap : "+fontMap);
				        					if(fontNameMap.containsKey(fontName)){
				        						attrValue.setValue(fontMap.substring(0, index + 1) + fontNameMap.get(fontName));
				        						lIsNodeSet = true;
				        					}
				        				}
				        			}
			        			}
		        			}
		        		}
	        		}
	        	}
	        }

	        if (lIsNodeSet)
	        {
	        	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        	Transformer transformer = transformerFactory.newTransformer();

	        	DOMSource source = new DOMSource(doc);
	        	StreamResult result = new StreamResult(new File(destFile));
	        	transformer.transform(source, result);
	        	TAGToolsUtil.printToConsole("Arabic theme is succesfully created.");
	        }
	    }
	    catch (IOException excp)
	    {
	    	CodeReviewStatus.getInstance().addErrorMessage(excp, excp.toString());
	    }
	    catch (ParserConfigurationException excp)
	    {
	    	CodeReviewStatus.getInstance().addErrorMessage(excp, excp.toString());
	    }
	    catch (SAXException excp)
	    {
	    	CodeReviewStatus.getInstance().addErrorMessage(excp, excp.toString());
	    }
	    catch (TransformerException excp)
	    {
	    	CodeReviewStatus.getInstance().addErrorMessage(excp, excp.toString());
	    }
	}
	
	private HashMap<String, String> getFontNameMap(){
		HashMap<String, String> fontNameMap = new HashMap<String, String>();
		String fontNames = ArabicLayoutPropertiesUtil.getConfigFileProperties().getProperty(PROP_ARABIC_FONTNAMEMAP);
		if(fontNames != null && !fontNames.equals("")){
			String[] fontNameArr = fontNames.split(";");
			if(fontNameArr.length > 0){
				for(String fontName: fontNameArr){
					int index = fontName.indexOf(":");
					if(index > 0){
						String srcFontName = fontName.substring(0, index);
						String dstFontName = fontName.substring(index +1); 
						fontNameMap.put(srcFontName, dstFontName);
					}
				}
			}
		}
		JSReviewUtil.printToConsole("fontNameMap : "+fontNameMap);
		return fontNameMap;
	}
	
}
