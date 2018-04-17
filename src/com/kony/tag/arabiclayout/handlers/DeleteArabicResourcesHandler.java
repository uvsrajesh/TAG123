package com.kony.tag.arabiclayout.handlers;

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

public class DeleteArabicResourcesHandler extends AbstractHandler
{
	
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
				
				File lALProp = new File(projectPath + ProjectConstants.AR_LAYOUT_PROPS_FILE);
				
				File lALMargPaddProp = new File(projectPath + ProjectConstants.AR_MARGIN_PADDING_PROPS_FILE);
				
				if(lALProp.exists() || lALMargPaddProp.exists()) {
					if(lALProp.exists())
						lALProp.delete();
					
					if(lALMargPaddProp.exists())
						lALMargPaddProp.delete();
					
					Util.displayDialog(shell, "Success", "Removed following Arabic Layout resources from the selected project:\n\n-> arLayout.properties, arCustomMarginPadding.properties \n\nRefresh your project for changes to be reflected...");
				} else {
					Util.displayDialog(shell, "Success", "The resources have already been removed.\n\nRefresh your project for changes to be reflected...");
				}
				
				
			}
			else
			{
				Util.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch(Exception e)
		{
			Util.displayDialog(shell, "Error", "Error occurred while deleting Arabic Layout resources from selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while deleting contents from Project: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
}