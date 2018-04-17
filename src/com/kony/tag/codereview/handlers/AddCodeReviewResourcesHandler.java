package com.kony.tag.codereview.handlers;

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

public class AddCodeReviewResourcesHandler extends AbstractHandler
{
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		Shell shell = null;
		InputStream lCRProp = null;
		InputStream lCRIgnoreFiles = null;
		InputStream lCRIgnoreFunc = null;
		InputStream lCRIgnoreI18 = null;
		InputStream lCRIgnoreImages = null;
		InputStream lCRIgnoreSkins = null;
		
		try
		{
			shell = HandlerUtil.getActiveShell(event);
			IProject project = TAGToolsUtil.getCurrentSelectedProject();
			if(project!=null)
			{
				String projectPath = project.getLocation().toString();
				
				TAGToolsUtil.createDirectory(projectPath + ProjectConstants.CODEREVIEW_CONFIG_LOCATION);
				
				lCRProp = this.getClass().getResourceAsStream("/com/kony/tag/codereview/resources/codereview.properties");
				if(lCRProp != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_CODE_REVIEW_PROPS_FILE);
					TAGToolsUtil.copyFile(lCRProp, lDestFile);
				}
				
				lCRIgnoreFiles = this.getClass().getResourceAsStream("/com/kony/tag/codereview/resources/ignoreReviewFiles.properties");
				if(lCRIgnoreFiles != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_REVIEW_FILES_IGNORE_LIST);
					TAGToolsUtil.copyFile(lCRIgnoreFiles, lDestFile);
				}
				
				lCRIgnoreFunc = this.getClass().getResourceAsStream("/com/kony/tag/codereview/resources/ignoreUnusedFunctions.properties");
				if(lCRIgnoreFunc != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_FUNCTIONS_IGNORE_LIST);
					TAGToolsUtil.copyFile(lCRIgnoreFunc, lDestFile);
				}
				
				lCRIgnoreI18 = this.getClass().getResourceAsStream("/com/kony/tag/codereview/resources/ignoreUnusedI18Keys.properties");
				if(lCRIgnoreI18 != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_I18NKEYS_IGNORE_LIST);
					TAGToolsUtil.copyFile(lCRIgnoreI18, lDestFile);
				}
				
				lCRIgnoreImages = this.getClass().getResourceAsStream("/com/kony/tag/codereview/resources/ignoreUnusedImages.properties");
				if(lCRIgnoreImages != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_IMAGES_IGNORE_LIST);
					TAGToolsUtil.copyFile(lCRIgnoreImages, lDestFile);
				}
				
				lCRIgnoreSkins = this.getClass().getResourceAsStream("/com/kony/tag/codereview/resources/ignoreUnusedSkins.properties");
				if(lCRIgnoreSkins != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_SKINS_IGNORE_LIST);
					TAGToolsUtil.copyFile(lCRIgnoreSkins, lDestFile);
				}
				
				Util.displayDialog(shell, "Success", "Added following Code Review resources to the selected project:" +
						"\n\n-> codeReviewConf Directory" +
						"\n\n-> codereview.properties " +
						"\n\n-> ignoreReviewFiles.properties " +
						"\n\n-> ignoreUnusedFunctions.properties " +
						"\n\n-> ignoreUnusedI18Keys.properties " +
						"\n\n-> ignoreUnusedImages.properties " +
						"\n\n-> ignoreUnusedSkins.properties " +
						"\n\nRefresh your project for changes to be reflected...");
			}
			else
			{
				Util.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch(Exception e)
		{
			Util.displayDialog(shell, "Error", "Error occurred while adding Code Review resources to selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while adding contents to Project: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			//Close input stream
			if(lCRProp != null)
			{
				try 
				{
					lCRProp.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lCRIgnoreFiles != null)
			{
				try 
				{
					lCRIgnoreFiles.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lCRIgnoreFunc != null)
			{
				try 
				{
					lCRIgnoreFunc.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lCRIgnoreI18 != null)
			{
				try 
				{
					lCRIgnoreI18.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lCRIgnoreImages != null)
			{
				try 
				{
					lCRIgnoreImages.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lCRIgnoreImages != null)
			{
				try 
				{
					lCRIgnoreImages.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lCRIgnoreSkins != null)
			{
				try 
				{
					lCRIgnoreSkins.close();
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