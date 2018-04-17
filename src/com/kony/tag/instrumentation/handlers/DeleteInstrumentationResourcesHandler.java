package com.kony.tag.instrumentation.handlers;

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

public class DeleteInstrumentationResourcesHandler extends AbstractHandler
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
				
				File lProfilerFile = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + ProjectConstants.PROFILER_FILE);
				
				File lProfilerResultsFile = new File(projectPath + ProjectConstants.LOCATION_JS_MODULES + ProjectConstants.PROFILER_RESULTS_FILE);
				
				File lEventResultsForm = new File(projectPath + ProjectConstants.LOCATION_MOBILE_FORMS + ProjectConstants.EVENT_RESULTS_FORM);
				
				File lFunctionResultsForm = new File(projectPath + ProjectConstants.LOCATION_MOBILE_FORMS + ProjectConstants.FUNCTION_RESULTS_FORM);
				
				File lEventResultsDesktopForm = new File(projectPath + ProjectConstants.LOCATION_DESKTOP_FORMS + ProjectConstants.EVENT_RESULTS_FORM);
				
				File lFunctionResultsDesktopForm = new File(projectPath + ProjectConstants.LOCATION_DESKTOP_FORMS + ProjectConstants.FUNCTION_RESULTS_FORM);
				
				if(lProfilerFile.exists() || lProfilerResultsFile.exists() || lEventResultsForm.exists() || lFunctionResultsForm.exists() || lEventResultsDesktopForm.exists() || lFunctionResultsDesktopForm.exists()) {
					if(lProfilerFile.exists())
						lProfilerFile.delete();
					
					if(lProfilerResultsFile.exists())
						lProfilerResultsFile.delete();
					
					if(lEventResultsForm.exists())
						lEventResultsForm.delete();
					
					if(lFunctionResultsForm.exists())
						lFunctionResultsForm.delete();
					
					if(lEventResultsDesktopForm.exists())
						lEventResultsDesktopForm.delete();
					
					if(lFunctionResultsDesktopForm.exists())
						lFunctionResultsDesktopForm.delete();
					
					Util.displayDialog(shell, "Success", "Removed following Instrumentation resources from the selected project:" +
							"\n\n-> TAGprofiler.js" +
							"\n\n-> TAGprofilerResults.js" +
							"\n\n-> frmTAGFunctionInfo.kl" +
							"\n\n-> frmTAGEventInfo.kl" +
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
			Util.displayDialog(shell, "Error", "Error occurred while deleting Instrumentation resources from selected project. Please check Console for Errors.");
			Util.printToConsole("******************** Exception while deleting contents from Project: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
}