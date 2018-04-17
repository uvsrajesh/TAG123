package com.kony.tag.codereview.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.kony.tag.codereview.dialogs.ReviewDialog;
import com.kony.tag.codereview.lua.Util;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler 
{
	/**
	 * The constructor.
	 */
	public SampleHandler() 
	{
	}

	/**
	 * the command has been executed, so extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException 
	{
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		/*
		MessageDialog.openInformation(
				window.getShell(),
				"HelloWorld",
				"Hello, Eclipse world");
		*/

		Util.printToConsole("OK Pressed");
		IWorkbenchWindow actWorkbenchWindow =  PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		
		if (actWorkbenchWindow == null)
		{
			MessageDialog.openInformation(window.getShell(),
										  "Code Review",
										  "No Active Project.");
			return null;
		}
		
		IWorkbenchPage actWorkbenchPage = actWorkbenchWindow.getActivePage();
		if(actWorkbenchPage == null)
		{
			MessageDialog.openInformation(window.getShell(),
										  "Code Review",
										  "No Active Project.");
			return null;
		}
		
		IEditorPart editorPart = actWorkbenchPage.getActiveEditor();
		if(editorPart == null)
		{
			MessageDialog.openInformation(window.getShell(),
										  "Code Review",
										  "No Active Project.");
			return null;			
		}
		
		IFileEditorInput input = (IFileEditorInput)editorPart.getEditorInput();
		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		
		if(activeProject == null)
		{
			MessageDialog.openInformation(window.getShell(),
										  "Code Review",
					   					  "No Active Project.");
			return null;
		}
		
		String projectFolder = activeProject.getLocation().toString();
		Util.printToConsole("Active Project Folder=" + projectFolder);

		ReviewDialog reviewDialog = new ReviewDialog(window.getShell(), projectFolder);
		return reviewDialog.open();		
	}
}
