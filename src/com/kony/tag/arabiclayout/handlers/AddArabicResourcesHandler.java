package com.kony.tag.arabiclayout.handlers;

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

public class AddArabicResourcesHandler extends AbstractHandler
{
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		Shell shell = null;
		InputStream lALProp = null;
		InputStream lALMargPaddProp = null;
		
		try
		{
			shell = HandlerUtil.getActiveShell(event);
			IProject project = TAGToolsUtil.getCurrentSelectedProject();
			if(project!=null)
			{
				String projectPath = project.getLocation().toString();
				
				lALProp = this.getClass().getResourceAsStream("/com/kony/tag/arabiclayout/resources/arLayout.properties");
				if(lALProp != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.AR_LAYOUT_PROPS_FILE);
					TAGToolsUtil.copyFile(lALProp, lDestFile);
				}
				
				lALMargPaddProp = this.getClass().getResourceAsStream("/com/kony/tag/arabiclayout/resources/arCustomMarginPadding.properties");
				if(lALMargPaddProp != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.AR_MARGIN_PADDING_PROPS_FILE);
					TAGToolsUtil.copyFile(lALMargPaddProp, lDestFile);
				}
				
				Util.displayDialog(shell, "Success", "Added following Arabic Layout Tool resources to the selected project:\n\n-> arLayout.properties, arCustomMarginPadding.properties\n\nRefresh your project for changes to be reflected...");
			}
			else
			{
				Util.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch(Exception e)
		{
			Util.displayDialog(shell, "Error", "Error occurred while adding Arabic Layout Tool resources to selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while adding contents to Project: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			//Close input stream
			if(lALProp != null)
			{
				try 
				{
					lALProp.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lALMargPaddProp != null)
			{
				try 
				{
					lALMargPaddProp.close();
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
	
	
}