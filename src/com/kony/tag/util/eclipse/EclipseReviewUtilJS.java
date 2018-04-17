package com.kony.tag.util.eclipse;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.kony.tag.codereview.console.ReviewConsole;
import com.kony.tag.codereview.lua.Util;
import com.kony.tag.js.codereview.config.ReviewModeConstants;


public class EclipseReviewUtilJS {

	private static MessageConsole messageConsole = null;
	private static MessageConsoleStream mesConStream = null;
	private static IProgressMonitor monitor = null;
	private static String projectPath = null;
	private static Shell shell = null;

	public static void init(Shell shellObj, IProgressMonitor monitorObj, String path) {

		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			return;
		} else {
			monitor = monitorObj;
			projectPath = path;
			shell = shellObj;
	
			try {
				messageConsole = ReviewConsole.getMessageConsole();
				mesConStream = messageConsole.newMessageStream();
				mesConStream.setActivateOnWrite(true);
			}catch (PartInitException e) 
			{
				Util.printToConsole(e.toString());
				Util.stackTraceToString(e);
				//e.printStackTrace();
				//printToConsole(Util.stackTraceToString(e));
			}catch (Exception e) 
			{
				Util.printToConsole(e.toString());
				Util.stackTraceToString(e);
			}
		}
	}
	
	public static String getProjectPath() {
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			return ReviewModeConstants.TEST_LOCATION_PROJECT_PATH;
		} else {
			return projectPath;
		}
	}
	
	public static void setTaskName(String taskName) {
		if (!ReviewModeConstants.TESTING_MODE_FLAG) {
			monitor.worked(5);  // to change the progress indicator
			monitor.setTaskName(taskName);
		}
	}
	
	public static void printToConsole(String message) {
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			System.out.println(message);
		} else {
		
			try 
			{
				mesConStream.println(message);
			}
			catch (Exception e) 
			{
				// SWALLOW THIS ONE !!
				//System.out.println(message);
			}		
		}
	}	

	public static void close() {
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			return;
		} else {
			try {
				mesConStream.flush();
				mesConStream.close();
				monitor.done();
			} catch (IOException e) {
				e.printStackTrace();
				//printToConsole(Util.stackTraceToString(e));
			}
			catch (Exception e) {
				// SWALLOW THIS ONE !!
				//System.out.println(e.toString());
			}
		}
	}
	
	public static void displayDialog(String mesType, String message) 
	{
		if (ReviewModeConstants.TESTING_MODE_FLAG) {
			System.out.println("ALERT MESSAGE : " + message);
		} else {
			MessageDialog.openInformation(shell, mesType,message);
		}
	}

}
