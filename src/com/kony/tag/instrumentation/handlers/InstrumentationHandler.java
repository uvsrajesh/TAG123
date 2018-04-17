package com.kony.tag.instrumentation.handlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.kony.tag.arabiclayout.dialog.CommandLineDialog;
import com.kony.tag.arabiclayout.util.ArabicLayoutPropertiesUtil;
import com.kony.tag.config.ProjectConstants;
import com.kony.tag.instrumentation.main.InstrumentationMasterClass;
import com.kony.tag.util.ProjectTypeReader;
import com.kony.tag.util.TAGToolsUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class InstrumentationHandler extends AbstractHandler 
{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		JSReviewUtil.init();
		
		TAGToolsUtil.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Instrumentation Handler ");
		Shell shell = null;
		try 
		{
			shell = HandlerUtil.getActiveShell(event);
			IProject project = TAGToolsUtil.getCurrentSelectedProject();
			if(project!=null)
			{
				String projectPath = project.getLocation().toString();
				TAGToolsUtil.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Project Location " + projectPath);
				
				CommandLineDialog dialog = new CommandLineDialog(null);
				
				int open = dialog.open();
				if (open == IDialogConstants.OK_ID) {
					TAGToolsUtil.setChannelName(dialog.getChannel());
					TAGToolsUtil.printToConsole("\n************** Started the Instrumentation Tool *************\n");
					
					if (ProjectTypeReader.isJSProject(projectPath)) {
						
						TAGToolsUtil.printToConsole("Project Type : Javascript(JS) \n");
						File file = new File(projectPath + ProjectConstants.LOCATION_JS_GEN_SRC_ROOT_FOLDER + ProjectConstants.FILE_DELIMITER + dialog.getChannel());
						if(file.isDirectory() && file.exists()){
							InstrumentationMasterClass.main(projectPath, shell);
							JSReviewUtil.displayDialog("Info", "Code Instrumentation is complete. Please copy the instrumented files from the directory '"+projectPath+"/codeInstrumentation/output/' and repackage the binary");
							TAGToolsUtil.printToConsole("Code Instrumentation is complete. Please copy the instrumented files from the directory '"+projectPath+"/codeInstrumentation/output/' and repackage the binary");
						} else {
							JSReviewUtil.displayDialog("Error", "Please build for the selected channel and execute the tool.");
						}
					} else {
						// command for Lua Project Review
						TAGToolsUtil.printToConsole("Project Type LUA: Not supported \n");
					}
				} else {
					return null;
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
			//Util.deleteTempDirectory(shell);
			//Util.deleteOutputDirectory(shell);
			TAGToolsUtil.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Error occured. Stopping the execution of the program with message: " + e.getMessage());
			TAGToolsUtil.displayDialog(shell, "Error", e.getMessage());
		}
		
		return null;
	}	 
} 