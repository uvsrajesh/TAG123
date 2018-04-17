package com.kony.tag.codereview.lua;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

/**
 * Author : TAG Group
 */
public class MasterClass 
{
	/**
	 * @param args
	 * @throws Throwable 
	 */
	 //destination Lua files path
	
	public static void main(final String projectPath,final Shell shell) throws CodeReviewException, Exception 
	{
		final long startTime = System.currentTimeMillis();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell); 
		dialog.run(true, true, new IRunnableWithProgress()
		{ 
		    public void run(IProgressMonitor monitor) throws InterruptedException
		    { 
		    	try
		    	{
		    		monitor.beginTask("Lua code reivew is started ...", 100);		        
			        runLuaCodeReview(projectPath, startTime,monitor,shell);		        
					monitor.done();
		    	}
		    	catch (CodeReviewException e) 
		    	{
					// TODO: handle exception
		    		throw new InterruptedException(e.getMessage());
				} 
		    } 
		});
	}
	
	public static void runLuaCodeReview(String projectPath,long startTime,IProgressMonitor monitor,Shell shell) throws CodeReviewException
	{	
		monitor.worked(5); // to change the progress indicator
		
		Util.printToConsole("******************* PREPARING THE PROCESS *******************");
		Util.printToConsole("Creating the temporary directories .......... ");
		monitor.setTaskName("Creating the temporary directories ..........");
		Util.setProjectPath(projectPath);
		Util.prepareNecessaryDir(); 
		
		monitor.setTaskName("Copying the files to temp directory ...");
		Util.printToConsole("Copying the files to temp directory ... ");
		Util.validateNCopyFilesToTempDir(shell);
		long endPrepareTime = System.currentTimeMillis();
		Util.printToConsole("Total Time Taken for preparing for the review is...... "+(endPrepareTime-startTime)+" ms");
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.setTaskName("Checking for common mistakes ....");
		monitor.worked(5); // to change the progress indicator
		Util.printToConsole("******************* CHECKING FOR COMMON MISTAKES *******************");
		CheckCommonMistakes.findCommonMistakes(shell);
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.setTaskName("Checking for form level mistakes....");
		monitor.worked(20); // to change the progress indicator
		Util.printToConsole("******************* CHECKING FOR FORM LEVEL MISTAKES *******************");
		FormReview.main(shell);
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.setTaskName("Finding un-used functions ....");
		monitor.worked(20); // to change the progress indicator
		Util.printToConsole("******************* FINDING UN-USED FUNCTIONS *******************");
		FindUnusedFunctions.main(shell);
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.setTaskName("Finding un-used i18n ....");
		monitor.worked(10); // to change the progress indicator
		Util.printToConsole("******************* FINDING UN-USED I18N *******************");
		FindUnusedi18n.main(shell);
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.setTaskName("Finding un-used skins ....");
		monitor.worked(10); // to change the progress indicator
		Util.printToConsole("******************* FINDING UN-USED SKINS *******************");
		CleanSkins.main(shell);
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.setTaskName("Checking for lua to Js porting ....");
		monitor.worked(10); // to change the progress indicator
		Util.printToConsole("******************* CHECKING FOR LUA TO JS PORTING *******************");
		LuaToJs.luaToJsPorting();
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.setTaskName("Checking for Unused Images....");
		monitor.worked(10); // to change the progress indicator
		Util.printToConsole("******************* CHECKING FOR UNUSED IMAGES*******************");
		UnusedImages obj=new UnusedImages();
		obj.filterImages(projectPath+"//resources");
		Util.printToConsole("********************************************************************\n\n");
		
		monitor.worked(5); // to change the progress indicator
		Util.printToConsole("******************* DELETING THE TEMPORARILY CREATED FILES/FOLDERS *******************");
		Util.deleteTempDirectory(shell);
		Util.printToConsole("********************************************************************\n\n");		
		
		Util.printToConsole("Code Review is completed please check the output at "+Util.Output_DIR);
		Util.printToConsole("********************************************************************\n\n");
	
		long endTime = System.currentTimeMillis();
		monitor.worked(5); // to change the progress indicator
		Util.printToConsole("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\n\n");
		Util.printToConsole("Total time taken for the entire processing is "+(endTime-startTime)+" ms\n\n");
		Util.printToConsole("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	}
}