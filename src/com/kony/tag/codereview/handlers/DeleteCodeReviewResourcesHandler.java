package com.kony.tag.codereview.handlers;

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

public class DeleteCodeReviewResourcesHandler extends AbstractHandler
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
				
				File lCRIgnoreFiles = new File(projectPath + ProjectConstants.LOCATION_REVIEW_FILES_IGNORE_LIST);
				
				File lCRIgnoreFunc = new File(projectPath + ProjectConstants.LOCATION_FUNCTIONS_IGNORE_LIST);
				
				File lCRIgnoreI18 = new File(projectPath + ProjectConstants.LOCATION_I18NKEYS_IGNORE_LIST);
				
				File lCRIgnoreImages = new File(projectPath + ProjectConstants.LOCATION_IMAGES_IGNORE_LIST);
				
				File lCRIgnoreSkins = new File(projectPath + ProjectConstants.LOCATION_SKINS_IGNORE_LIST);
				
				File lCRProp = new File(projectPath + ProjectConstants.LOCATION_CODE_REVIEW_PROPS_FILE);
				
				File lCRConf = new File(projectPath + ProjectConstants.CODEREVIEW_CONFIG_LOCATION);
				
				if(lCRConf.exists()){
					
					if(lCRIgnoreFiles.exists())
						lCRIgnoreFiles.delete();
					if(lCRIgnoreFunc.exists())
						lCRIgnoreFunc.delete();
					if(lCRIgnoreI18.exists())
						lCRIgnoreI18.delete();
					if(lCRIgnoreImages.exists())
						lCRIgnoreImages.delete();
					if(lCRIgnoreSkins.exists())
						lCRIgnoreSkins.delete();
					if(lCRProp.exists())
						lCRProp.delete();
					
					if(lCRConf.exists())
						lCRConf.delete();
					
					Util.displayDialog(shell, "Success", "Removed following Code Review resources from the selected project:" +
							"\n\n-> codeReviewConf Directory" +
							"\n\n-> codereview.properties " +
							"\n\n-> ignoreReviewFiles.properties " +
							"\n\n-> ignoreUnusedFunctions.properties " +
							"\n\n-> ignoreUnusedI18Keys.properties " +
							"\n\n-> ignoreUnusedImages.properties " +
							"\n\n-> ignoreUnusedSkins.properties " +
							"\n\nRefresh your project for changes to be reflected...");
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
			Util.displayDialog(shell, "Error", "Error occurred while deleting Code Preview resources from selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while deleting contents from Project: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
}