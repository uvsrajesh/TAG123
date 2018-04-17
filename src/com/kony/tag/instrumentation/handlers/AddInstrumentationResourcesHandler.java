package com.kony.tag.instrumentation.handlers;

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

public class AddInstrumentationResourcesHandler extends AbstractHandler
{
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		Shell shell = null;
		InputStream lProfilerFile = null;
		InputStream lProfilerResultsFile = null;
		InputStream lEventResultsForm = null;
		InputStream lFunctionResultsForm = null;
		InputStream lEventResultsDesktopForm = null;
		InputStream lFunctionResultsDesktopForm = null;
		
		try
		{
			shell = HandlerUtil.getActiveShell(event);
			IProject project = TAGToolsUtil.getCurrentSelectedProject();
			if(project!=null)
			{
				String projectPath = project.getLocation().toString();
				
				lProfilerFile = this.getClass().getResourceAsStream("/com/kony/tag/instrumentation/resources/TAGprofiler.js");
				if(lProfilerFile != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + ProjectConstants.PROFILER_FILE);
					TAGToolsUtil.copyFile(lProfilerFile, lDestFile);
				}
				
				lProfilerResultsFile = this.getClass().getResourceAsStream("/com/kony/tag/instrumentation/resources/TAGprofilerResults.js");
				if(lProfilerResultsFile != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + ProjectConstants.PROFILER_RESULTS_FILE);
					TAGToolsUtil.copyFile(lProfilerResultsFile, lDestFile);
				}
				
				lEventResultsForm = this.getClass().getResourceAsStream("/com/kony/tag/instrumentation/resources/mobile/frmTAGEventInfo.kl");
				if(lEventResultsForm != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_MOBILE_FORMS + ProjectConstants.EVENT_RESULTS_FORM);
					TAGToolsUtil.copyFile(lEventResultsForm, lDestFile);
				}
				
				lFunctionResultsForm = this.getClass().getResourceAsStream("/com/kony/tag/instrumentation/resources/mobile/frmTAGFunctionInfo.kl");
				if(lFunctionResultsForm != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_MOBILE_FORMS + ProjectConstants.FUNCTION_RESULTS_FORM);
					TAGToolsUtil.copyFile(lFunctionResultsForm, lDestFile);
				}
				
				lEventResultsDesktopForm = this.getClass().getResourceAsStream("/com/kony/tag/instrumentation/resources/desktop/frmTAGEventInfo.kl");
				if(lEventResultsDesktopForm != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_DESKTOP_FORMS + ProjectConstants.EVENT_RESULTS_FORM);
					TAGToolsUtil.copyFile(lEventResultsDesktopForm, lDestFile);
				}
				
				lFunctionResultsDesktopForm = this.getClass().getResourceAsStream("/com/kony/tag/instrumentation/resources/desktop/frmTAGFunctionInfo.kl");
				if(lFunctionResultsDesktopForm != null)
				{
					File lDestFile = new File(projectPath + ProjectConstants.LOCATION_DESKTOP_FORMS + ProjectConstants.FUNCTION_RESULTS_FORM);
					TAGToolsUtil.copyFile(lFunctionResultsDesktopForm, lDestFile);
				}
				
				Util.displayDialog(shell, "Success", "Added following resources to the selected project:" +
						"\n\n-> TAGprofiler.js" +
						"\n\n-> TAGprofilerResults.js" +
						"\n\n-> frmTAGFunctionInfo.kl" +
						"\n\n-> frmTAGEventInfo.kl" +
						"\n\n Refresh your project for changes to be reflected..."+
						"\n\n Please perform a build along with the added resources.");
			}
			else
			{
				Util.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch(Exception e)
		{
			Util.displayDialog(shell, "Error", "Error occurred while adding Instrumentation tool resources to selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while adding contents to Project: " + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			
			//Close input stream
			if(lProfilerFile != null)
			{
				try 
				{
					lProfilerFile.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lProfilerResultsFile != null)
			{
				try 
				{
					lProfilerResultsFile.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lEventResultsForm != null)
			{
				try 
				{
					lEventResultsForm.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lFunctionResultsForm != null)
			{
				try 
				{
					lFunctionResultsForm.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lEventResultsDesktopForm != null)
			{
				try 
				{
					lEventResultsDesktopForm.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle closing stream with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
			
			if(lFunctionResultsDesktopForm != null)
			{
				try 
				{
					lFunctionResultsDesktopForm.close();
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