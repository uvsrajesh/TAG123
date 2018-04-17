package com.kony.tag.codepreview.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
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

public class DeleteResourcesHandler extends AbstractHandler
{
	private final String lMenuCodeStr = "TAG.CodePreviewController.addAsAppMenuItem(\"menuCodePreview\")";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		Shell shell = null;
		try
		{
			shell = HandlerUtil.getActiveShell(event);
			IProject project = TAGToolsUtil.getCurrentSelectedProject();
			if(project!=null)
			{
				String projectPath = project.getLocation().toString();
				
				File lCPJS = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + "\\TAGCodePreview.js");
				if(lCPJS.exists())
					lCPJS.delete();
				
				File lLoggerJS = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + "\\TAGLogger.js");
				if(lLoggerJS.exists())
					lLoggerJS.delete();
				
				File lMobikeKl = new File(projectPath + ProjectConstants.LOCATION_MOBILE_POPUPS + "\\TAGCPServerDetailsPopup.kl");
				if(lMobikeKl.exists())
					lMobikeKl.delete();
				
				File lTabletKl = new File(projectPath + ProjectConstants.LOCATION_TABLET_POPUPS + "\\TAGCPServerDetailsPopup.kl");
				if(lTabletKl.exists())
					lTabletKl.delete();
				
				File lDesktopKl = new File(projectPath + ProjectConstants.LOCATION_DESKTOP_POPUPS + "\\TAGCPServerDetailsPopup.kl");
				if(lDesktopKl.exists())
					lDesktopKl.delete();
				
				updateProjEventsXML(projectPath + ProjectConstants.LOCATION_CONFIG_PROJECT_EVENTS_FILE);
				updateProjEventsXML(projectPath + ProjectConstants.LOCATION_CONFIG_PROJECT_TABLET_EVENTS_FILE);
				updateProjEventsXML(projectPath + ProjectConstants.LOCATION_CONFIG_PROJECT_DESKTOP_EVENTS_FILE);
				
				Util.displayDialog(shell, "Success", "Removed following Code Preview resources from the selected project:\n\n-> TAGCodePreview.js, TAGLogger.js are deleted from JS Modules\n-> TAGCPServerDetailsPopup.kl is deleted from Mobile, Tablet and Desktop dialog folders\n-> Required code for populating Code Preview menu item is removed from Mobile, Tablet and Desktop PostAppInit events\n\nRefresh your project for changes to be reflected...");
			}
			else
			{
				Util.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch(Exception e)
		{
			Util.displayDialog(shell, "Error", "Error occurred while deleting Code Preview resources from selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while deleting contents from Project: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void updateProjEventsXML(String aProjEventsFilePath)
	{
		try
		{
			File lProjEvents = new File(aProjEventsFilePath);
			if(lProjEvents.exists())
			{
				int lMenuItemPos = getMenuItemElemPos(lProjEvents);
				
				if(lMenuItemPos > 0)
				{
					removeMenuItemFromProjEventsXML(lProjEvents, lMenuItemPos);
				}
			}
		}
		catch(Exception e)
		{
			Util.printToConsole("Exception wihle updating project events file: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private int getMenuItemElemPos(File aProjEventsFile)
	{
		boolean lIsPostAppInit = false;
		boolean lIsEventSeq = false;
		boolean lIsValue = false;
		int lMenuPosition = 0;
		
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
	            	else if("actionList".equals(lStartElemStr))
                	{
	            		if(lIsEventSeq)
	            			lMenuPosition++;
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
	            			break;
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
		
		return lMenuPosition;
	}
	
	private void removeMenuItemFromProjEventsXML(File aProjEventsFile, int aMenuItemPos)
	{
		boolean lIsPostAppInit = false;
		boolean lIsEventSeq = false;
		boolean lIsToAddEvent = true;
		boolean lIsToUpdateBool = false;
		int lMenuPosition = 0;
		
		//Get the factory instance first.
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
                			lIsEventSeq = true;
                	}
                	else if("actionList".equals(lStartElemStr))
                	{
                		if(lIsEventSeq)
                		{
                			lMenuPosition++;
                			if(lMenuPosition == aMenuItemPos)
                				lIsToAddEvent = false;
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
                	else if("evtSequence".equals(lEndElemStr))
                	{
                		lIsEventSeq = false;
                	}
                	else if("actionList".equals(lEndElemStr))
                	{
                		lIsToUpdateBool = true;
                	}
                }
                
                if(lIsToAddEvent)
                	lOutputWriter.add(lEvent);
                
                if(lIsToUpdateBool)
                {
                	lIsToAddEvent = true;
                	lIsToUpdateBool = false;
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