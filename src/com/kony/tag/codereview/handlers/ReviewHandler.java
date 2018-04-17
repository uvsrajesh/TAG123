package com.kony.tag.codereview.handlers;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.kony.tag.codereview.lua.MasterClass;
import com.kony.tag.codereview.lua.Util;
import com.kony.tag.js.codereview.main.JSReviewMasterClass;
import com.kony.tag.util.ProjectTypeReader;
import com.kony.tag.util.TAGToolsUtil;

public class ReviewHandler extends AbstractHandler 
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
				Util.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Location " + projectPath);
				
				Util.printToConsole("************** Started the Review *************\n");
				
				if (ProjectTypeReader.isJSProject(projectPath)) {
					// command for JS Project Review
					Util.printToConsole("Project Type : Javascript(JS) \n");
					JSReviewMasterClass.main(projectPath, shell);
				} else {
					// command for Lua Project Review
					Util.printToConsole("Project Type : LUA \n");
					MasterClass.main(projectPath, shell);
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
				Util.displayDialog(shell, "Error", "Please select a Kony Project (in Project Explorer View)");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			Util.deleteTempDirectory(shell);
			Util.deleteOutputDirectory(shell);
			Util.printToConsole("%%%%%%%%%%%%%%%%%%%%%%%%%% Error occured. Stopping the execution of the program with message: " + e.getMessage());
			Util.displayDialog(shell, "Error", e.getMessage());
		}
		
		return null;
	}	 
} 