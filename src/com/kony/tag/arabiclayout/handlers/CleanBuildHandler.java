package com.kony.tag.arabiclayout.handlers;

import java.util.LinkedHashSet;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.kony.tag.arabiclayout.main.ArabicLayoutMasterClass;
import com.kony.tag.arabiclayout.util.BuildPropertiesUtil;
import com.kony.tag.js.codereview.util.ReviewUtil;
import com.kony.tag.util.ProjectTypeReader;
import com.kony.tag.util.TAGToolsUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class CleanBuildHandler extends AbstractHandler 
{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		ExecutionModeHandler.init();
		JSReviewUtil.init();
		ExecutionModeHandler.setExecutionMode(ExecutionModeHandler.EXEC_MODE_TYPE_CLEAN_BUILD);
		
		TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 1" + (new java.util.Date()).toString());
		
		TAGToolsUtil.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Clean & Build Handler ");
		Shell shell = null;
		try 
		{
			shell = HandlerUtil.getActiveShell(event);
			IProject project = TAGToolsUtil.getCurrentSelectedProject();
			if(project!=null)
			{
				
				String projectPath = project.getLocation().toString();
				TAGToolsUtil.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Project Location " + projectPath);
				TAGToolsUtil.printToConsole("************** Started the RTL Tool *************\n");
				
				if (ProjectTypeReader.isJSProject(projectPath)) {
					// command for JS Project Review
					TAGToolsUtil.printToConsole("Project Type : Javascript(JS) \n");
					BuildPropertiesUtil.clear();
					BuildPropertiesUtil.init(projectPath);
					
					LinkedHashSet<String> channelList = BuildPropertiesUtil.getSelectedChannelList();
					if(channelList.size() == 0) {
						TAGToolsUtil.printToConsole("Invalid/No Platforms selected. Please re-check your build.properties file");
						JSReviewUtil.displayDialog("Error", "Invalid/No Platforms selected. Please re-check your build.properties file");
						return null;
					}
					
					
					TAGToolsUtil.printToConsole("\n************** Starting Clean Build of selected Channels *************\n");
					//JSReviewUtil.setTaskName("Executing Clean Build");
					TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 2A" + (new java.util.Date()).toString());
					int status = BuildPropertiesUtil.executeBuild(projectPath);
					TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 2B" + (new java.util.Date()).toString());
					
					if(status == 0){
						TAGToolsUtil.printToConsole("\n**************  Build Execution is successful. **************\n");
					} else {
						TAGToolsUtil.printToConsole("\n**************  Build Execution Failed. Please check the console Logs. **************\n");
						JSReviewUtil.displayDialog("Error", "Build Failed. Please check the console Logs.");
						return null;
					}
					TAGToolsUtil.printToConsole("SEE-PERF-LOG: step - 3 START" + (new java.util.Date()).toString());
					ArabicLayoutMasterClass.main(projectPath, shell);
					JSReviewUtil.printToConsole("SEE-PERF-LOG: step - 3 END" + (new java.util.Date()).toString());
					ReviewUtil.resetDefaultBuildProperties();
				}
			}
			else
			{
				TAGToolsUtil.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			ReviewUtil.resetDefaultBuildProperties();
			TAGToolsUtil.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Error occured. Stopping the execution of the program with message: " + e.getMessage());
			TAGToolsUtil.displayDialog(shell, "Error", e.getMessage());
		}
		
		return null;
	}
} 