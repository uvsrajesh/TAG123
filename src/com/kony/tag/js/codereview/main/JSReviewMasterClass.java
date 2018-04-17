package com.kony.tag.js.codereview.main;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import com.kony.tag.util.eclipse.EclipseReviewUtilJS;
import com.kony.tag.util.eclipse.JSReviewUtil;

/**
 * Author : TAG Group
 */
public class JSReviewMasterClass 
{
	/**
	 * @param args
	 * @throws Throwable 
	 */
	 //destination Lua files path
	
	public static void main(final String projectPath,final Shell shell) throws Exception {
		final long startTime = System.currentTimeMillis();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell); 
		dialog.run(true, true, new IRunnableWithProgress()
		{ 
		    public void run(IProgressMonitor monitor) throws InterruptedException
		    { 
		    	try
		    	{
		    		EclipseReviewUtilJS.init(shell, monitor, projectPath);
		    		monitor.beginTask("JS code review is started ...", 100);
			        runJSCodeReview(startTime);	
		    	}
		    	catch (Exception e) 
		    	{
					// TODO: handle exception
		    		throw new InterruptedException(e.getMessage());
				}
		    	
		    	finally{
		    		EclipseReviewUtilJS.close();
		    	}
		    } 
		});
	}
	
	public static void runJSCodeReview(long startTime) {
		
		ReviewOrchestrator reviewOrchestrator = new ReviewOrchestrator();
		reviewOrchestrator.performCodeReview(startTime);
	}
}