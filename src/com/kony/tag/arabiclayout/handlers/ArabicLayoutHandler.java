package com.kony.tag.arabiclayout.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.kony.tag.arabiclayout.dialog.CommandLineDialog;
import com.kony.tag.arabiclayout.main.ArabicLayoutMasterClass;
import com.kony.tag.arabiclayout.util.ArabicLayoutPropertiesUtil;
import com.kony.tag.util.ProjectTypeReader;
import com.kony.tag.util.TAGToolsUtil;
import com.kony.tag.util.eclipse.JSReviewUtil;

public class ArabicLayoutHandler extends AbstractHandler 
{
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		ExecutionModeHandler.init();
		JSReviewUtil.init();
		ExecutionModeHandler.setExecutionMode(ExecutionModeHandler.EXEC_MODE_INCREMENTAL_CODE_GEN);
	
		TAGToolsUtil.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Incremental Build Handler ");
		Shell shell = null;
		String endOfWorkWarningMessage = null;
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
					 ArabicLayoutPropertiesUtil.setChannelName(dialog.getChannel());
					TAGToolsUtil.printToConsole("\n************** Started the RTL Tool *************\n");
					
					if (ProjectTypeReader.isJSProject(projectPath)) {
						// command for JS Project Review
						TAGToolsUtil.printToConsole("Project Type : Javascript(JS) \n");
						ArabicLayoutMasterClass.main(projectPath, shell);
						endOfWorkWarningMessage = JSReviewUtil.getEndOfWorkWarningMessage();
						
						if (endOfWorkWarningMessage != null && endOfWorkWarningMessage.trim().length()>0) {
							JSReviewUtil.displayDialog("Info",endOfWorkWarningMessage);
							TAGToolsUtil.printToConsole(endOfWorkWarningMessage);
						} else {
							JSReviewUtil.displayDialog("Info", "Arabic Layout Incremental Code Gen completed for channel '"+ArabicLayoutPropertiesUtil.getChannelKey()+"'. Now Please perform incremental build for your Project in Kony IDE for '" + ArabicLayoutPropertiesUtil.getChannelKey()+"' channel");
							TAGToolsUtil.printToConsole("Arabic Layout Incremental Code Gen completed for channel '"+ArabicLayoutPropertiesUtil.getChannelKey()+"'. Now Please perform incremental build for your Project in Kony IDE for '" + ArabicLayoutPropertiesUtil.getChannelKey()+"' channel");
						}
						
					} else {
						// command for Lua Project Review
						TAGToolsUtil.printToConsole("Project Type LUA: Not supported \n");
						//MasterClass.main(projectPath, shell);
					}
				} else {
					return null;
				}
				
				/*
				// ENABLE FOLLOWING CODE FOR HYBRID MODE ONLY
				Util.printToConsole("RUNNING HYBRID MODE : LUA REVIEW");
				MasterClass.main(projectPath, shell);
				
				Util.printToConsole("RUNNING HYBRID MODE : JS REVIEW");
				JSReviewMasterClass.main(projectPath, shell);
				*/
				
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