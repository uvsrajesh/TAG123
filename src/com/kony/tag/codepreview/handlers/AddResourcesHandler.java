package com.kony.tag.codepreview.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.kony.tag.codereview.lua.Util;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.util.TAGToolsUtil;

public class AddResourcesHandler extends AbstractHandler
{
	private final String lMenuCodeStr = "TAG.CodePreviewController.addAsAppMenuItem(\"menuCodePreview\")";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		Shell shell = null;
		InputStream lCPJS = null;
		InputStream lLoggerJS = null;
		InputStream lMobikeKl = null;
		InputStream lTabletKl = null;
		InputStream lDesktopKl = null;
		
		try
		{
			shell = HandlerUtil.getActiveShell(event);
			IProject project = TAGToolsUtil.getCurrentSelectedProject();
			if(project!=null)
			{
				String projectPath = project.getLocation().toString();
				
				lCPJS = this.getClass().getResourceAsStream("/com/kony/tag/codepreview/resources/TAGCodePreview.js");
				if(lCPJS != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + "\\TAGCodePreview.js");
					TAGToolsUtil.copyFile(lCPJS, lDestFile);
				}
				
				lLoggerJS = this.getClass().getResourceAsStream("/com/kony/tag/codepreview/resources/TAGLogger.js");
				if(lLoggerJS != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + "\\TAGLogger.js");
					TAGToolsUtil.copyFile(lLoggerJS, lDestFile);
				}
				
				lMobikeKl = this.getClass().getResourceAsStream("/com/kony/tag/codepreview/resources/mobile/TAGCPServerDetailsPopup.kl");
				if(lMobikeKl != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_MOBILE_POPUPS + "\\TAGCPServerDetailsPopup.kl");
					TAGToolsUtil.copyFile(lMobikeKl, lDestFile);
				}
				
				lTabletKl = this.getClass().getResourceAsStream("/com/kony/tag/codepreview/resources/tablet/TAGCPServerDetailsPopup.kl");
				if(lTabletKl != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_TABLET_POPUPS + "\\TAGCPServerDetailsPopup.kl");
					TAGToolsUtil.copyFile(lTabletKl, lDestFile);
				}
				
				lDesktopKl = this.getClass().getResourceAsStream("/com/kony/tag/codepreview/resources/desktop/TAGCPServerDetailsPopup.kl");
				if(lDesktopKl != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_DESKTOP_POPUPS + "\\TAGCPServerDetailsPopup.kl");
					TAGToolsUtil.copyFile(lDesktopKl, lDestFile);
				}
				
				updateProjEventsXML(projectPath + ProjectConstants.LOCATION_CONFIG_PROJECT_EVENTS_FILE, "/com/kony/tag/codepreview/resources/projectevents.xml");
				updateProjEventsXML(projectPath + ProjectConstants.LOCATION_CONFIG_PROJECT_TABLET_EVENTS_FILE, "/com/kony/tag/codepreview/resources/projecttabletevents.xml");
				updateProjEventsXML(projectPath + ProjectConstants.LOCATION_CONFIG_PROJECT_DESKTOP_EVENTS_FILE, "/com/kony/tag/codepreview/resources/projectdesktop_kioskevents.xml");
				
				Util.displayDialog(shell, "Success", "Added following Code Preview resources to the selected project:\n\n-> TAGCodePreview.js, TAGLogger.js are added to JS Modules\n-> TAGCPServerDetailsPopup.kl is added to Mobile, Tablet and Desktop dialog folders\n-> Required code for populating Code Preview menu item is added to Mobile, Tablet and Desktop PostAppInit events\n\nRefresh your project for changes to be reflected...");
			}
			else
			{
				Util.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch(Exception e)
		{
			Util.displayDialog(shell, "Error", "Error occurred while adding Code Preview resources to selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while adding contents to Project: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			//Close input stream
			if(lCPJS != null)
			{
				try 
				{
					lCPJS.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lLoggerJS != null)
			{
				try 
				{
					lLoggerJS.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lMobikeKl != null)
			{
				try 
				{
					lMobikeKl.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lTabletKl != null)
			{
				try 
				{
					lTabletKl.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	private void updateProjEventsXML(String aProjEventsFilePath, String aResourceStreamPath)
	{
		InputStream lProjEventXML = null;
		
		try
		{
			File lProjEvents = new File(aProjEventsFilePath);
			if(lProjEvents.exists())
			{
				boolean lIsMenuItemExists = isMenuItemElemExists(lProjEvents);
				
				if(lIsMenuItemExists)
				{
					Util.printToConsole("Required code for Menu Item is already present in " + aProjEventsFilePath + " file");
				}
				else
				{
					addMenuItemToProjEventsXML(lProjEvents);
				}
			}
			else
			{
				lProjEventXML = this.getClass().getResourceAsStream(aResourceStreamPath);
				if(lProjEventXML != null)
				{
					File lDestFile = new File(aProjEventsFilePath);
					TAGToolsUtil.copyFile(lProjEventXML, lDestFile);
				}
			}
		}
		catch(Exception e)
		{
			Util.printToConsole("Exception wihle updating project events file: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(lProjEventXML != null)
			{
				try 
				{
					lProjEventXML.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean isMenuItemElemExists(File aProjEventsFile)
	{
		boolean lIsPostAppInit = false;
		boolean lIsEventSeq = false;
		boolean lIsValue = false;
		boolean lRetVal = false;
		
		//Get the factory instance first.
        XMLInputFactory lFactory = XMLInputFactory.newInstance();
		XMLEventReader lReader = null;
		
		try
		{
			//create the XMLEventReader & Writer
	    	lReader = lFactory.createXMLEventReader(new FileInputStream(aProjEventsFile));
	    	
	        //iterate as long as there are more events on the input stream
	        while(lReader.hasNext()) 
	        {
	        	//Check if it is start element and see if it has an attribute with "name" as key and "Name" as the value
	            XMLEvent lEvent = lReader.nextEvent();
	            
	            if(lEvent.getEventType() == XMLEvent.START_ELEMENT)
	            {
	            	StartElement lStartElem = (StartElement)lEvent;
	            	String lStartElemStr = lStartElem.getName().toString().trim();
	            	if(("value").equals(lStartElemStr))
	            	{
	            		if(lIsEventSeq)
	            			lIsValue = true;
	            	}
	            	else if("evtSequence".equals(lStartElemStr))
                	{
	            		if(lIsPostAppInit)
	            			lIsEventSeq = true;
                	}
	            	else if(("postappinit").equals(lStartElemStr))
                	{
                		lIsPostAppInit = true;
                	}
	            }
	            else if(lEvent.getEventType() == XMLEvent.END_ELEMENT)
	            {
	            	EndElement lEndElem = (EndElement)lEvent;
	            	String lEndElemStr = lEndElem.getName().toString().trim();
	            	
	            	if(("value").equals(lEndElemStr))
	            	{
	            		lIsValue = false;
	            	}
	            	else if("evtSequence".equals(lEndElemStr))
                	{
	            		lIsEventSeq = false;
                	}
	            	else if(("postappinit").equals(lEndElemStr))
                	{
                		lIsPostAppInit = false;
                	}
	            }
	            else if(lEvent.getEventType() == XMLEvent.CHARACTERS)
	            {
	            	if(lIsValue)
	            	{
	            		Characters lCharElem = (Characters)lEvent;
	            		String lValueStr = lCharElem.getData();
	            		if(lValueStr.contains(lMenuCodeStr))
	            		{
	            			lRetVal = true;
	            			break;
	            		}
	            	}
	            }
	        }
		}
		catch(Exception e)
		{
			Util.printToConsole("Exception wihle checking for Menu Item Element: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if(lReader != null)
			{
				try 
				{
					lReader.close();
				} 
				catch (XMLStreamException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		return lRetVal;
	}
	
	private void addMenuItemToProjEventsXML(File aProjEventsFile)
	{
		boolean lIsPostAppInit = false;
		
		//Get the factory instance first.
		XMLEventFactory lXmlEventFactory = XMLEventFactory.newInstance();
        XMLInputFactory lFactory = XMLInputFactory.newInstance();
        XMLOutputFactory lOutputFactory = XMLOutputFactory.newInstance();
        
		XMLEventReader lReader = null;
		XMLEventWriter lOutputWriter = null;
        OutputStream lFileOutputStream = null;
        
        try
		{
        	String lTempFileName = aProjEventsFile.getAbsolutePath() + ProjectConstants.STR_UNDERSCORE + ProjectConstants.STR_TEMP;
    		File lTempEventsFile = new File(lTempFileName);
    		TAGToolsUtil.copyFile(aProjEventsFile, lTempEventsFile);
    		
    		aProjEventsFile.delete();
    		
    		//create the XMLEventReader & Writer
        	lReader = lFactory.createXMLEventReader(new FileInputStream(lTempFileName));
        	lFileOutputStream = new FileOutputStream(aProjEventsFile.getAbsolutePath());
            lOutputWriter = lOutputFactory.createXMLEventWriter(lFileOutputStream);
            
            //iterate as long as there are more events on the input stream
            while(lReader.hasNext()) 
            {
            	//Check if it is start element and see if it has an attribute with "name" as key and "Name" as the value
                XMLEvent lEvent = lReader.nextEvent();
                lOutputWriter.add(lEvent);
                
                if(lEvent.getEventType() == XMLEvent.START_ELEMENT)
                {
                	StartElement lStartElem = (StartElement)lEvent;
                	String lStartElemStr = lStartElem.getName().toString().trim();
                	if(("postappinit").equals(lStartElemStr))
                	{
                		lIsPostAppInit = true;
                	}
                	else if("evtSequence".equals(lStartElemStr))
                	{
                		if(lIsPostAppInit)
                		{
                			//Creating Namespaces for ActionList Element
                			Set<Namespace> lNameSpacesList = new HashSet<Namespace>();
                            lNameSpacesList.add(lXmlEventFactory.createNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
                            
                            Set<Attribute> lAttributesList = new HashSet<Attribute>();
                            lAttributesList.add(lXmlEventFactory.createAttribute("xsi:type", "eventExpressionAction"));
                            
                            //Creating Action List Element - START
                            StartElement lActionListElem = lXmlEventFactory.createStartElement(new QName("actionList"), lAttributesList.iterator(), lNameSpacesList.iterator());
                            lOutputWriter.add(lActionListElem);
                            
                            //Creating Enabled Element - START
                            StartElement lEnabledElem = lXmlEventFactory.createStartElement(new QName("enabled"), null, null);
                            lOutputWriter.add(lEnabledElem);
                            
                            Characters lEnabledChars = lXmlEventFactory.createCharacters("true");
                            lOutputWriter.add(lEnabledChars);
                            
                            EndElement lEnabledEndElem = lXmlEventFactory.createEndElement(new QName("enabled"), null);
                            lOutputWriter.add(lEnabledEndElem);
                            //Creating Enabled Element - END
                            
                            //Creating Name Element - START
                            StartElement lNameElem = lXmlEventFactory.createStartElement(new QName("name"), null, null);
                            lOutputWriter.add(lNameElem);

                            Characters lNameChars = lXmlEventFactory.createCharacters("");
                            lOutputWriter.add(lNameChars);

                            EndElement lNameEndElem = lXmlEventFactory.createEndElement(new QName("name"), null);
                            lOutputWriter.add(lNameEndElem);
                            //Creating Name Element - END
                            
                            //Creating Type Element - START
                            StartElement lTypeElem = lXmlEventFactory.createStartElement(new QName("type"), null, null);
                            lOutputWriter.add(lTypeElem);

                            Characters lTypeChars = lXmlEventFactory.createCharacters("5");
                            lOutputWriter.add(lTypeChars);

                            EndElement lTypeEndElem = lXmlEventFactory.createEndElement(new QName("type"), null);
                            lOutputWriter.add(lTypeEndElem);
                            //Creating Type Element - END
                            
                            //Creating Expression Element - START
                            StartElement lExprElem = lXmlEventFactory.createStartElement(new QName("expr"), null, null);
                            lOutputWriter.add(lExprElem);

                            Characters lExprChars = lXmlEventFactory.createCharacters("");
                            lOutputWriter.add(lExprChars);

                            EndElement lExprEndElem = lXmlEventFactory.createEndElement(new QName("expr"), null);
                            lOutputWriter.add(lExprEndElem);
                            //Creating Expression Element - END
                            
                            //Creating Expression Map Element - START
                            StartElement lExprMapElem = lXmlEventFactory.createStartElement(new QName("exprMap"), null, null);
                            lOutputWriter.add(lExprMapElem);

                            //Creating Entry Element - START
                            StartElement lEntryElem = lXmlEventFactory.createStartElement(new QName("entry"), null, null);
                            lOutputWriter.add(lEntryElem);

                            //Creating Key Element - START
                            StartElement lKeyElem = lXmlEventFactory.createStartElement(new QName("key"), null, null);
                            lOutputWriter.add(lKeyElem);

                            Characters lKeyChars = lXmlEventFactory.createCharacters("js");
                            lOutputWriter.add(lKeyChars);

                            EndElement lKeyEndElem = lXmlEventFactory.createEndElement(new QName("key"), null);
                            lOutputWriter.add(lKeyEndElem);
                            //Creating Key Element - END
                            
                            //Creating Value Element - START
                            StartElement lValueElem = lXmlEventFactory.createStartElement(new QName("value"), null, null);
                            lOutputWriter.add(lValueElem);

                            Characters lValueChars = lXmlEventFactory.createCharacters(lMenuCodeStr);
                            lOutputWriter.add(lValueChars);

                            EndElement lValueEndElem = lXmlEventFactory.createEndElement(new QName("value"), null);
                            lOutputWriter.add(lValueEndElem);
                            //Creating Value Element - END

                            EndElement lEntryEndElem = lXmlEventFactory.createEndElement(new QName("entry"), null);
                            lOutputWriter.add(lEntryEndElem);
                            //Creating Entry Element - END

                            EndElement lExprMapEndElem = lXmlEventFactory.createEndElement(new QName("exprMap"), null);
                            lOutputWriter.add(lExprMapEndElem);
                            //Creating Expression Map Element - END
                            
                            EndElement lActionListEndElem = lXmlEventFactory.createEndElement(new QName("actionList"), null);
                            lOutputWriter.add(lActionListEndElem);
                            //Creating Action List Element - END
                		}
                	}
                }
                else if(lEvent.getEventType() == XMLEvent.END_ELEMENT)
                {
                	EndElement lEndElem = (EndElement)lEvent;
                	String lEndElemStr = lEndElem.getName().toString().trim();
                	
                	if(("postappinit").equals(lEndElemStr))
                	{
                		lIsPostAppInit = false;
                	}
                }
            }
            
            lOutputWriter.flush();
            
            boolean lIsTempDeleted = lTempEventsFile.delete();
    		if(!lIsTempDeleted)
    			Util.printToConsole("******************** Cannot delete temporary projects event file " + lTempEventsFile.getName());
		}
        catch(Exception e)
		{
        	Util.printToConsole("Exception wihle updating project events file: " + e.getMessage());
        	e.printStackTrace();
		}
		finally
		{
			//Close input stream
			if(lReader != null)
			{
				try 
				{
					lReader.close();
				} 
				catch (XMLStreamException e) 
				{
					Util.printToConsole("Exception wihle closing reader stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lFileOutputStream != null)
			{
				try 
				{
					lFileOutputStream.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}