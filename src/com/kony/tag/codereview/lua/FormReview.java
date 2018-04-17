package com.kony.tag.codereview.lua;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class FormReview
{	
	public static void main(Shell shell)
	{
		Writer output = null;
		XPath xpath = null;
		File file = new File(Util.Output_DIR+"\\FormReview.csv");
		long startTime = System.currentTimeMillis();
		try
		{
			output = new BufferedWriter(new FileWriter(file, false));
			Util.printToConsole("Checking for form level mistakes .....");
			//inputDir = Util.formSrcDir+"mobile\\";
			
			File[] formFiles = new File(Util.tempCodeDir).listFiles(new KLFileFilter());
			xpath = XPathFactory.newInstance().newXPath();
			Properties properties = new Properties();
			properties.load(FormReview.class.getResourceAsStream("formWdgts.properties"));//new FileInputStream(new File("src\\formWdgts.properties")));
	
			for (File formFileToReview : formFiles) 
			{
				Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(formFileToReview);	
				
				//xPath to get the form Name
				String xpathExprFormName = "//formName";								
				XPathExpression exprFormName = xpath.compile(xpathExprFormName);
				NodeList nodeListToChange1 = (NodeList) exprFormName.evaluate(document, XPathConstants.NODESET);
				String formName = nodeListToChange1.item(0).getTextContent();
				
				// Now for the children of kPage
				for (Object xpathExpressionObj : properties.keySet()) 
				{
					String xpathExpression = (String) xpathExpressionObj;								
					XPathExpression expression = xpath.compile(xpathExpression);
					NodeList formChildrenNodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
	
					for (int i = 0; i < formChildrenNodeList.getLength(); i++) 
					{
						Node currentNode = formChildrenNodeList.item(i);
						String childType;
	
						if (currentNode.hasAttributes()) 
						{
							NamedNodeMap attrs = currentNode.getAttributes();
							for (int j = 0; j < attrs.getLength(); j++) 
							{
								Attr attribute = (Attr) attrs.item(j);
								childType = attribute.getValue();
	
								if (childType.equals("kForm")) 
								{
									checkForm(formName, currentNode, output);
								}// End of childType.equals("kForm")
	
								if (childType.equals("kSegment")) 
								{
									checkSegment(formName, currentNode, xpath, output);
								}// End of childType.equals("kSegment")
	
								if (childType.equals("kBrowser")) 
								{
									checkBrowser(formName, currentNode, xpath, output);
								}// End of childType.equals("kBrowser")
	
								if (childType.equals("kMap")) 
								{
									checkMap(formName, currentNode, xpath, output);
								}// End of childType.equals("kMap")
	
								if (childType.equals("khBox")) 
								{
									checkHBox(formName, currentNode, xpath, output);
								}// End of childType.equals("khBox")
	
								if (childType.equals("kTextBox") || 
									childType.equals("kTextArea")) 
								{
									checkforPlaceholder(formName, currentNode, output);
								}
	
								if (childType.equals("kCamera")  || 
									childType.equals("kLabel")   || 
									childType.equals("kButton")  || 
									childType.equals("kMenuItem")|| 
									childType.equals("kTab")) 
								{
									checkfori18n(formName, currentNode, output);
								}
	
								/* KUMAR CHITTA: MODIFIED on Jan 9th: this checkForBlockedUI was being  
								 * executed for kButton and kSegment only. 
								 * Now included:  khBox, kvBox,kLink,kComboBox,kListBox,kTextArea,kTextBox
								 */
								if (childType.equals("kButton") || childType.equals("kSegment") ||
										childType.equals("khBox") || childType.equals("kvBox") ||
											childType.equals("kLink") ||
												childType.equals("kComboBox") || childType.equals("kListBox") ||
													childType.equals("kTextArea") || childType.equals("kTextBox")) 
								{
									checkForBlockedUI(formName, currentNode, output);
								}
	
								if (childType.equals("kButton")  || 
									childType.equals("kComboBox")|| 
									childType.equals("kCamera")  || 
									childType.equals("kLink")    || 
									childType.equals("kListBox") || 
									childType.equals("kSegment")) 
								{
									checkForFocusSkin(formName, currentNode, output);
								}								
							}
						}// End of if(parentNode.hasAttributes())
					}
				}
				checkForSnippets(formName, formFileToReview, output);
			}
			
			long endTime = System.currentTimeMillis();
			Util.printToConsole("Total Time Taken for completing the forms review is ...... "+(endTime-startTime)+" ms");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.printToConsole(e.getMessage());
			Util.printToConsole(Util.stackTraceToString(e)); 			
		}
		finally
		{
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void checkforPlaceholder(String formName, Node currNode, Writer output) throws IOException 
	{
		// TODO Auto-generated method stub
		NodeList childNodeList = currNode.getChildNodes();
		int nodelen = childNodeList.getLength();
		String iPhHolder = "";
		String iPHi18Holder = "";
		String andHolder = "";
		String andi18Holder = "";
		String txtBxId = "";
		String bbHolder = "";
		String bbi18Holder = "";

		String j2meHolder = "";
		String j2mei18Holder = "";

		String spaHolder = "";
		String spai18Holder = "";

		String tcHolder = "";
		String tci18Holder = "";

		String wmHolder = "";
		String wmi18Holder = "";

		String wm6Holder = "";
		String wm6i18Holder = "";

		String syHolder = "";
		String syi18Holder = "";
		
		for (int t = 0; t < nodelen; t++)
		{
			Node childNode = childNodeList.item(t);
			
			if("ID".equals(childNode.getNodeName()))
			{
				// get the ID value
				txtBxId = childNode.getTextContent();							
			}
			
			if ("iphone_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				iPhHolder = childNode.getTextContent();
			}
			
			if ("iphone_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				iPhHolder = childNode.getTextContent();
			}
			
			if ("iphone_ph_i18nId".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				iPHi18Holder = childNode.getTextContent();
				if(!iPhHolder.equals("") && iPHi18Holder.equals("") || iPHi18Holder.equals("None") )
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the iPhone Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
			
			if ("android_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				andHolder = childNode.getTextContent();
			}
			
			if ("android_ph_i18nId".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				andi18Holder = childNode.getTextContent();
				if(!andHolder.equals("") && andi18Holder.equals("") || andi18Holder.equals("None") )
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the android Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
			
			if ("j2me_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				j2meHolder = childNode.getTextContent();
			}
			
			if ("j2me_ph_i18nId".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				j2mei18Holder = childNode.getTextContent();
				if(!j2meHolder.equals("") && j2mei18Holder.equals("") || j2mei18Holder.equals("None"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the J2ME Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}// j2me
			
			if ("bb_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				bbHolder = childNode.getTextContent();
			}
			
			if ("bb_ph_i18nId".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				bbi18Holder = childNode.getTextContent();
				if(!bbHolder.equals("") && bbi18Holder.equals("") || bbi18Holder.equals("None") )
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the Blackberry Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			} //BB
			
			//thinclient
			if ("tc_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				tcHolder = childNode.getTextContent();
			}
			
			if ("tc_ph_i18nId".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				tci18Holder = childNode.getTextContent();
				if(!tcHolder.equals("") && tci18Holder.equals("") || tci18Holder.equals("None"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the Thin Client Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
			
			//symbian
			if ("symbianPlaceHolderText".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				syHolder = childNode.getTextContent();
			}
			
			if ("symbianPlaceHolderI18nKey".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				syi18Holder = childNode.getTextContent();
				if(!syHolder.equals("") && syi18Holder.equals("") || syi18Holder.equals("None") )
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the Symbian Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
			
			//SPA
			if ("spa_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				spaHolder = childNode.getTextContent();
			}
			
			if ("spa_placeHolderI18n".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				spai18Holder = childNode.getTextContent();
				if(!spaHolder.equals("") && spai18Holder.equals("") || spai18Holder.equals("None"))
				{	
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the SPA Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
			
			//windows
			if ("wm_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				wmHolder = childNode.getTextContent();
			}
			
			if ("wm_placeHolder_i18nId".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				wmi18Holder = childNode.getTextContent();
				if(!wmHolder.equals("") && wmi18Holder.equals("") || wmi18Holder.equals("None"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the Windows Mobile Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
			
			//windows6
			if ("wm6x_placeHolder".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				wm6Holder = childNode.getTextContent();
			}
			
			if ("wm6x_placeHolder_i18nId".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				wm6i18Holder = childNode.getTextContent();
				if(!wm6Holder.equals("") && wm6i18Holder.equals("") || wm6i18Holder.equals("None"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+","+txtBxId+","+"i18 Text for the Windows6 Mobile Placeholder is not set "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
		}		
	}

	private static void checkForm(String formName,Node parentNode, Writer output) throws IllegalArgumentException, Exception 
	{
		String secureBool;
		String idealtime;
		NodeList childNodes = parentNode.getChildNodes();
		int numChildren = childNodes.getLength();

		for (int k = 0; k < numChildren; k++) 
		{
			Node childNode = childNodes.item(k);
			if ("secure".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				secureBool = childNode.getTextContent();
				if (secureBool.equals("false")) 
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName + 
								  "," + 
								  "SecureSubmit" + 
								  "," + 
								  "SecureSubmit is set to False. When this form is submitted it will be a HTTP request to the server instead of HTTPS request. Does your server accept HTTP requests and is the customer OK with this?" + 
								  "," + 
								  "Suggestion" + 
								  "," + 
								  "High" + 
								"\n");
					output.flush();
				}
			}
			
			if ("idle_timeout".equals(childNode.getNodeName())) 
			{
				// retrieve the secure submit property at the form level
				idealtime = childNode.getTextContent();
				if(idealtime.equals("false"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
					output.append(formName+ ","+ "Idle Time out is set to \"false\""+ ","+ " Application does not logout in this form. "+ "," + "Suggestion" + "," + "Medium" + "\n");
					output.flush();
				}
			}
		}		
	}
	
	private static void checkForBlockedUI(String formName, Node currNode, Writer output) throws IOException 
	{	
		String blockedUISkin;		
		String widgetId = "";
		//String focusSkin ;
		
		NodeList childNodes = currNode.getChildNodes();					
		int numChildren = childNodes.getLength();
		
		for(int l=0; l<numChildren; l++)
		{
			Node childNode = childNodes.item(l);	
			if("ID".equals(childNode.getNodeName()))
			{
				// get the ID value
				widgetId = childNode.getTextContent();							
			}
			
			if("blockedUISkin".equals(childNode.getNodeName()))
			{
				// blockedUISkin for the corresponding widget
				blockedUISkin = childNode.getTextContent();	
				if(blockedUISkin.equals("None"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
			    	output.append(formName+","+widgetId+","+"No Block UI Skin is assigned. Setting Block UI skin can prevent form double submission in advanced Thin Clients."+","+"Suggestion"+","+"High"+"\n");
			    	output.flush();
				}				
			}
		}		
	}

	private static void checkForFocusSkin(String formName, Node currNode, Writer output) throws IOException 
	{	
		String widgetId = "";
		String focusSkin ;
		
		NodeList childNodes = currNode.getChildNodes();					
		int numChildren = childNodes.getLength();
		
		for(int l=0; l<numChildren; l++)
		{
			Node childNode = childNodes.item(l);			
			if("ID".equals(childNode.getNodeName()))
			{
				// get the ID value
				widgetId = childNode.getTextContent();							
			}
			if("focusSkinId".equals(childNode.getNodeName()))
			{
				// blockedUISkin for the corresponding widget
				focusSkin = childNode.getTextContent();	
				if(focusSkin.equals("None"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
			    	output.append(formName+","+widgetId+","+"Focus Skin is not assigned. This can confuse the users of non-touch devices as he/she would not know which widget is in focus."+","+"Suggestion"+","+"Medium"+"\n");
			    	output.flush();
				}				
			}				
		}		
	}

	private static void checkfori18n(String formName, Node parentNode, Writer output) throws IOException 
	{
	
		String localeId;		
		String widgetId = "";
		
		NodeList childNodes = parentNode.getChildNodes();					
		int numChildren = childNodes.getLength();
		
		for(int m=0; m<numChildren; m++)
		{
			Node childNode = childNodes.item(m);
			if("ID".equals(childNode.getNodeName()))
			{
				// get the ID value
				widgetId = childNode.getTextContent();							
			}
			
			if("localeId".equals(childNode.getNodeName()))
			{
				// get the i18n text for the corresponding widget
				localeId = childNode.getTextContent();	
				if (localeId.equals("None"))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
				    output.append(formName+","+widgetId+","+"No i18N key assigned."+","+"Suggestion"+","+"Medium"+"\n");
				    output.flush();
				}
			}
		}	
	}

	private static void checkHBox(String formName, Node currNode, XPath xpath, Writer output) throws Exception, Exception 
	{		
		String hBoxSkin = "";		
		String hBoxID = "";
		boolean actionListFlag = false; 
		
		// get its ID
		String xpathExprFormName = "ID";								
		XPathExpression exprFormName = xpath.compile(xpathExprFormName);
		NodeList idNodeList = (NodeList) exprFormName.evaluate(currNode, XPathConstants.NODESET); // should return only one node
		hBoxID = idNodeList.item(0).getTextContent();
		
		// get its skin
		xpathExprFormName = "skinId";								
		exprFormName = xpath.compile(xpathExprFormName);
		NodeList skinNodeList = (NodeList) exprFormName.evaluate(currNode, XPathConstants.NODESET); // should return only one node
		hBoxSkin = skinNodeList.item(0).getTextContent();
		
		// get its onClick event handler
		xpathExprFormName = "tc_onClick_root";								
		exprFormName = xpath.compile(xpathExprFormName);
		NodeList onClickNodeList = (NodeList) exprFormName.evaluate(currNode, XPathConstants.NODESET); // should return only one node
		int onClickNodeCount = onClickNodeList.getLength();
		
		//Util.printToConsole("onClickNodeCount   ... "+onClickNodeCount);
		if(onClickNodeCount == 0)
		{
			actionListFlag = false;
		}			
		
		/*if(onClickNodeList.item(0).hasChildNodes())
		{					
			//Node onClick_rootChild = onClickNodeList.item(0).getChildNodes().item(1);			
			if(onClick_rootChild.hasChildNodes())
			{
				NodeList evtSequenceChildList = onClick_rootChild.getChildNodes();						
				for( int o =0; o < evtSequenceChildList.getLength(); o++)
				{
					Node evtSequenceChild = evtSequenceChildList.item(o);	
				
					//Checking On Click Event for HBox							
					if("actionList".equals(evtSequenceChild.getNodeName().trim()))
					{
						actionListFlag = true;								
					}
					else
					{
						actionListFlag = false;								
					}								
				}
			}
		}*/
		
		// get child widgets
		xpathExprFormName = "children";								
		exprFormName = xpath.compile(xpathExprFormName);
		NodeList childWidgetNodeList = (NodeList) exprFormName.evaluate(currNode, XPathConstants.NODESET);
		if(childWidgetNodeList != null && childWidgetNodeList.getLength() == 1)
		{
			// get the child type
			NamedNodeMap attribList = childWidgetNodeList.item(0).getAttributes();
			Node typeAttribNode = attribList.getNamedItem("xsi:type");
			String nodeType = typeAttribNode.getNodeValue();

			if("kvBox".equals(nodeType))
			{
				//if the child is a vbox, check again if it has only hbox
				xpathExprFormName = "children";								
				exprFormName = xpath.compile(xpathExprFormName);
				NodeList vBxChildNodeList = (NodeList) exprFormName.evaluate(currNode, XPathConstants.NODESET);
				if(vBxChildNodeList != null && vBxChildNodeList.getLength() == 1)
				{
					// get the child type
					NamedNodeMap attrList = childWidgetNodeList.item(0).getAttributes();
					Node typeAttrNode = attrList.getNamedItem("xsi:type");
					String typeNode = typeAttrNode.getNodeValue();

					if("khBox".equals(typeNode))
					{
						//Util.printToConsole("Multiple nested container widgets are found "+","+"Suggestion"+","+"Medium"+"\n");
						//output = new BufferedWriter(new FileWriter(file, true));
				    	output.append(formName+","+hBoxID+","+"Multiple nested container widgets are found."+","+"Suggestion"+","+"Medium"+"\n");//Medium
				    	output.flush();
					}
				}
			}
			else
			{
				//if the child is not a vbox
				if(!actionListFlag && "0".equalsIgnoreCase(hBoxSkin))
				{
					//output = new BufferedWriter(new FileWriter(file, true));
			    	output.append(formName+","+hBoxID+","+"HBox is used for containing only one widget."+","+"Suggestion"+","+"Medium"+"\n");//Medium
			    	output.flush();
				}
			}
		}
	}

	private static void checkSegment(String formName, Node parentNode, XPath xpath, Writer output) throws XPathExpressionException, Exception 
	{
		NodeList segChildNodeList = null;
		String isScreenLevelWidget = "";
		String segOrientation = "";														
		String iD = "";

		NodeList childNodes = parentNode.getChildNodes();					
		int numChildren = childNodes.getLength();

		for(int k=0; k<numChildren; k++)
		{
			Node childNode = childNodes.item(k);						
			if("ID".equals(childNode.getNodeName()))
			{
				// get the ID value
				iD = childNode.getTextContent();
			}
			
			if("_screenLevelWidget".equals(childNode.getNodeName()))
			{
				// get the isScreenLevelWidget value
				isScreenLevelWidget = childNode.getTextContent();												
			}
			
			if("orientation".equals(childNode.getNodeName()))
			{
				// get the orientation value
				segOrientation = childNode.getTextContent();
				if(segOrientation.equals("0"))
				{
					segOrientation = "vertical";
				}
				else
				{								
					segOrientation = "horizontal";
				}
			}						
		}
		
		if (isScreenLevelWidget.equals("false"))
		{
			//output = new BufferedWriter(new FileWriter(file, true));
		    output.append(formName+","+iD+","+"Setting ScreenLevelWidget property to \"true\" increases the segment holding capacity by many times thus avoiding some crashes in the application."+","+"Suggestion"+","+"High"+"\n");//high
		    output.flush();
		}
		
		XPathExpression[] segChildExpressionArray = {xpath.compile("children")};
		for (XPathExpression snippetExpression : segChildExpressionArray) 
		{
			segChildNodeList = (NodeList) snippetExpression.evaluate(parentNode, XPathConstants.NODESET);					
			int childLength = segChildNodeList.getLength();
			if(childLength == 1)
			{
				Node segChildList = segChildNodeList.item(0);
				Node segChildName = segChildList.getAttributes().item(0);

				if(("kvBox".equals(segChildName.getNodeValue())))
				{
					String Vbx = segChildName.getNodeValue();
					String VBox =  Vbx;
					if(!VBox.isEmpty())
					{
						//output = new BufferedWriter(new FileWriter(file, true));
				    	output.append(formName+","+iD+","+"Instead of using VBox set segment orientation property to vertical. This avoids creation of unnecessary container widgets thus reducing the application memory footprint."+","+"Suggestion"+","+"High"+"\n");//high
				    	output.flush();
				    }
				}
				
				if ("khBox".equals(segChildName.getNodeValue()))
				{
					String Hbx = segChildName.getNodeValue();
					String HBox =  Hbx;
					if(!HBox.isEmpty())
					{
						//output = new BufferedWriter(new FileWriter(file, true));
				    	output.append(formName+","+iD+","+"Instead of using HBox set segment orientation property to horizontal. This avoids creation of unnecessary container widgets thus reducing the application memory footprint."+","+"Suggestion"+","+"High"+"\n");//HIgh
				    	output.flush();
				    }
				}
			}
		}			
	}
	
	
	private static void checkBrowser(String formName, Node browserNode, XPath xpath, Writer output) throws XPathExpressionException, Exception 
	{
		String isScreenLevelWidget = "";														
		String iD = "";
		int yNodeVal = 0 ;
		
		NodeList childNodes = browserNode.getChildNodes();					
		int numChildren = childNodes.getLength();

		for(int k=0; k<numChildren; k++)
		{
			Node childNode = childNodes.item(k);						
			if("ID".equals(childNode.getNodeName()))
			{
				// get the ID value
				iD = childNode.getTextContent();							
			}
			
			if("_screenLevelWidget".equals(childNode.getNodeName()))
			{
				// get the isScreenLevelWidget value
				isScreenLevelWidget = childNode.getTextContent();												
			}
		}
		
		Node browserParentNode = browserNode.getParentNode();
		
		// if browserParentNode is not "form" node, navigate until you get the form node
		NamedNodeMap attribList = browserParentNode.getAttributes();
		Node typeAttribNode = attribList.getNamedItem("xsi:type");
		String nodeType = typeAttribNode.getNodeValue();
		
		//String yLocVal = null;
		while(!("kForm".equals(nodeType)))
		{
			//output = new BufferedWriter(new FileWriter(file, true));
	    	output.append(formName+","+iD+","+"Browser is placed inside a container"+","+"Suggestion"+","+"Medium"+"\n");//HIgh
	    	output.flush();

	    	XPathExpression[] locyNodeExprObj = {xpath.compile("location/y")};
			for (XPathExpression yPathExpression : locyNodeExprObj) 
			{
				// should return only one node as there is only one "location/y" for each child
				NodeList yLocNodeList = (NodeList) yPathExpression.evaluate(browserParentNode,XPathConstants.NODESET);
				Node yNode = yLocNodeList.item(0);
				String yLocValue = yNode.getTextContent();
				yNodeVal = Integer.parseInt(yLocValue);
			}
			
			// print warning that browser is placed inside a container widget
			// get its y location value and save it yLocVal
			browserParentNode = browserParentNode.getParentNode();
			
			// if browserParentNode is not "form" node, navigate until you get the form node
			attribList = browserParentNode.getAttributes();
			typeAttribNode = attribList.getNamedItem("xsi:type");
			nodeType = typeAttribNode.getNodeValue();
		}
		// Now we're at form level

		if (isScreenLevelWidget.equals("false"))
		{
			//output = new BufferedWriter(new FileWriter(file, true));
		    output.append(formName+","+iD+","+"Browser ScreenLevelWidget is set to false"+","+"Suggestion"+","+"High"+"\n");//high
		    output.flush();
		}
		
		checkforLastWidget(formName, browserParentNode, yNodeVal,"Browser",iD, xpath, output);		
	}

	private static void checkMap(String formName, Node mapNode, XPath xpath, Writer output) throws XPathExpressionException, Exception 
	{
		String isScreenLevelWidget = "";														
		String iD = "";
		int yNodeVal = 0;
		
		NodeList childNodes = mapNode.getChildNodes();					
		int numChildren = childNodes.getLength();

		for(int k=0; k<numChildren; k++)
		{
			Node childNode = childNodes.item(k);						
			if("ID".equals(childNode.getNodeName()))
			{
				// get the ID value
				iD = childNode.getTextContent();							
			}
			
			if("_screenLevelWidget".equals(childNode.getNodeName()))
			{
				// get the isScreenLevelWidget value
				isScreenLevelWidget = childNode.getTextContent();												
			}
		}

		Node mapParentNode = mapNode.getParentNode();
		// if browserParentNode is not "form" node, navigate until you get the form node
		NamedNodeMap attribList = mapParentNode.getAttributes();
		Node typeAttribNode = attribList.getNamedItem("xsi:type");
		String nodeType = typeAttribNode.getNodeValue();
		//String yLocVal = null;
		
		while(!("kForm".equals(nodeType)))
		{
			//output = new BufferedWriter(new FileWriter(file, true));
	    	output.append(formName+","+iD+","+"Map Widget is placed inside a container"+","+"Suggestion"+","+"Medium"+"\n");//HIgh
	    	output.flush();
			
			XPathExpression[] locyNodeExprObj = {xpath.compile("location/y")};
			for (XPathExpression yPathExpression : locyNodeExprObj) 
			{
				// should return only one node as there is only one "location/y" for each child
				NodeList yLocNodeList = (NodeList) yPathExpression.evaluate(mapParentNode,XPathConstants.NODESET);
				Node yNode = yLocNodeList.item(0);
				String yLocValue = yNode.getTextContent();
				yNodeVal = Integer.parseInt(yLocValue);
			}
			
			// print warning that browser is placed inside a container widget
			// get its y location value and save it yLocVal
			mapParentNode = mapParentNode.getParentNode();
			
			// if browserParentNode is not "form" node, navigate until you get the form node
			attribList = mapParentNode.getAttributes();
			typeAttribNode = attribList.getNamedItem("xsi:type");
			nodeType = typeAttribNode.getNodeValue();
		}
				
		if (isScreenLevelWidget.equals("false"))
		{
			//output = new BufferedWriter(new FileWriter(file, true));
		    output.append(formName+","+iD+","+"Map ScreenLevelWidget is set to false"+","+"Suggestion"+","+"High"+"\n");//high
		    output.flush();
		}
		
		checkforLastWidget(formName, mapParentNode, yNodeVal,"Map",iD, xpath, output);
	}


	private static void checkForSnippets(String formName, File fileToFormat, Writer output) throws Exception, Exception 
	{
		XPath xpath = XPathFactory.newInstance().newXPath();
		Properties properties = new Properties();
		properties.load(FormReview.class.getResourceAsStream("SnippetXpath.properties"));//new FileInputStream(new File("SnippetXpath.properties"))); 
			
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileToFormat);

		for (Object xpathExpressionObj : properties.keySet()) 
		{
			String xpathExpression = (String) xpathExpressionObj;
			XPathExpression expression = xpath.compile(xpathExpression);
			NodeList snippetChildList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < snippetChildList.getLength(); i++) 
			{
				Node snippetChildNode = snippetChildList.item(i);
				Node parentNode = snippetChildNode.getParentNode();
				String widgetID = null;
				while ("children".compareTo(parentNode.getNodeName()) != 0) 
				{
					parentNode = parentNode.getParentNode();
				}
				
				// parent node of "children" is the actual widget node
				NodeList childNodes = parentNode.getChildNodes();
				int numChildren = childNodes.getLength();
				for (int j = 0; j < numChildren; j++) 
				{
					Node childNode = childNodes.item(j);
					if ("ID".equals(childNode.getNodeName())) 
					{
						// get the ID value
						widgetID = childNode.getTextContent();
						break;
					}
				}
					
				if(null!=widgetID && !widgetID.isEmpty())
				{
//				    	output = new BufferedWriter(new FileWriter(file, true));
			    	output.append(formName+","+widgetID+","+ "Widget has code Sinppet. It is recomended to use lua functions instead of form level Snippet for Code reuseablity. "+","+"Suggestion"+","+"Low"+"\n");//Low
			    	output.flush();
			    }
			}
		}	
	}
	
	private static void checkforLastWidget(String formName, Node formNode, int brwNodeYVal,String widgetType, String widgetID, XPath xpath, Writer output) throws XPathExpressionException, Exception 
	{
		XPathExpression[] frmExpressionArray = { xpath.compile("children")};
		for (XPathExpression frmHbxExpression : frmExpressionArray) 
		{
			NodeList frmChildNodes = (NodeList) frmHbxExpression.evaluate(formNode, XPathConstants.NODESET);
			int frmHbxChildLength = frmChildNodes.getLength();
			int yNodeLocValue = 0;

			for (int b = 0; b < frmHbxChildLength; b++) 
			{
				Node frmChildNode = frmChildNodes.item(b);

				// get its ID
				String xpathExprChildID = "ID";								
				XPathExpression exprChildID = xpath.compile(xpathExprChildID);
				NodeList idNodeList = (NodeList) exprChildID.evaluate(frmChildNode, XPathConstants.NODESET); // should return only one node
				String childID = idNodeList.item(0).getTextContent();

				XPathExpression[] locyNodeExprObj = { xpath.compile("location/y") };
				for (XPathExpression yPathExpression : locyNodeExprObj) 
				{
					// should return only one node as there is only one
					// "location/y" for each child
					NodeList yLocNodeList = (NodeList) yPathExpression.evaluate(frmChildNode, XPathConstants.NODESET);
					Node yNode = yLocNodeList.item(0);
					String yLocValue = yNode.getTextContent();
					yNodeLocValue = Integer.parseInt(yLocValue);
					
					if (brwNodeYVal > 0 && brwNodeYVal < yNodeLocValue) 
					{
						// browser or map widget is not the last widget in the form
						//output = new BufferedWriter(new FileWriter(file,true));
						output.append(formName + 
									  "," +
									  widgetID + 
									  "," + 
									  "The widget " + childID + " is placed below the " + widgetType + " widget. It is recommended that the " + 
									  widgetType + " widget should be the last widget in the form." + 
									  "," + 
									  "Suggestion" + 
									  "," + 
									  "Medium" + 
									  "\n");
						output.flush();
						break;
					}
				}
			}
		}
	}
}