package com.kony.tag.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.kony.tag.codereview.console.ReviewConsole;
import com.kony.tag.codereview.lua.Util;

public class TAGToolsUtil
{	
	public static String getCurrentSelectedProjectPath() throws PartInitException, IOException 
	{ 
    	IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if(editorPart == null)
		{
			return null;			
		}
		
		IFileEditorInput input = (IFileEditorInput)editorPart.getEditorInput();
		IFile file = input.getFile();
		IProject activeProject = file.getProject();
		
		if(activeProject == null)
		{
			return null;
		}
		
		String projectFolder = activeProject.getLocation().toString();

	    return projectFolder;
	}
	
	public static IProject getCurrentSelectedProject() throws PartInitException, IOException 
	{ 
	    IProject project = null;
	    IResource resource = null;
	    ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService(); 	 
	    ISelection selection = selectionService.getSelection(); 
	 
	    if(selection instanceof IStructuredSelection)
	    { 
	        Object element = ((IStructuredSelection)selection).getFirstElement(); 	 
	        if (element instanceof IResource) 
	        { 
	        	resource = ((IResource)element);
	        	//Util.printToConsole("%%%%%%%%%%%%%%%%%%%% Resource Type is "+resource.getType());
	        	
	        	if(resource.getType()==IResource.PROJECT)
	        	{
	        		 project= ((IResource)element).getProject();
	        	}
	        	else
	        	{
	        		return null;
	        	}
	        } 
	        /*else if (element instanceof PackageFragmentRoot) 
	        { 
	            IJavaProject jProject =  ((PackageFragmentRoot)element).getJavaProject(); 
	            project = jProject.getProject(); 
	        }
	        else if (element instanceof IJavaElement) 
	        { 
	            IJavaProject jProject= ((IJavaElement)element).getJavaProject(); 
	            project = jProject.getProject(); 
	        }*/
	    } 
	    
	    return project; 
	}
	
	/**
	 * Utility method to create a new directory if it does not exist 
	 * 
	 * @param directoryName Directory name to be created
	 */
	public static void createDirectory(String directoryName) {
		File directory = new File(directoryName);
		
		if(!directory.exists()) {
			directory.mkdir();
		}
	}
	
	/**
	 * Utility method to copy contents of a file from source to destination file 
	 * 
	 * @param aSrcFile File for source
	 * @param aDestFile File for destination
	 */
	public static void copyFile(InputStream aInputStream, File aDestFile)
	{
		OutputStream lOutputStream = null;
		
		try
		{
			if(aInputStream == null)
			{
				return;
			}
			
			if(aDestFile.exists())
				aDestFile.delete();
			
			//Create the respective input and output streams for source and destination files respectively
			lOutputStream = new FileOutputStream(aDestFile);
			
			byte[] lBuffer = new byte[1024];
			int lBuffLen = 0;
			
			//Read the buffer from input file and write the same to output file
			while((lBuffLen = aInputStream.read(lBuffer)) > 0)
			{
				lOutputStream.write(lBuffer, 0, lBuffLen);
			}
		}
		catch(IOException ioe)
		{
			Util.printToConsole("Exception wihle copying file from source to " + aDestFile.getAbsolutePath() + " with message: " + ioe.getMessage());
			ioe.printStackTrace();
		}
		finally
		{
			//Close output stream
			if(lOutputStream != null)
			{
				try 
				{
					lOutputStream.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle copying file from source to " + aDestFile.getAbsolutePath() + " with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * Utility method to copy contents of a file from source to destination file 
	 * 
	 * @param aSrcFile File for source
	 * @param aDestFile File for destination
	 */
	public static void copyFile(File aSourceFile, File aDestFile)
	{
		InputStream lInputStream = null;
		
		try
		{
			lInputStream = new FileInputStream(aSourceFile);
			if(lInputStream != null)
			{
				copyFile(lInputStream, aDestFile);
			}
		}
		catch(IOException ioe)
		{
			Util.printToConsole("Exception wihle copying file from source to " + aDestFile.getAbsolutePath() + " with message: " + ioe.getMessage());
			ioe.printStackTrace();
		}
		finally
		{
			//Close input stream
			if(lInputStream != null)
			{
				try 
				{
					lInputStream.close();
				} 
				catch (IOException e) 
				{
					Util.printToConsole("Exception wihle copying file from source to " + aDestFile.getAbsolutePath() + " with message: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}		
	}
	
	public static void printToConsole(String message)
	{		
		try 
		{
			MessageConsole messageConsole = ReviewConsole.getMessageConsole();
			MessageConsoleStream mesConStream = messageConsole.newMessageStream();
			mesConStream.setActivateOnWrite(true);
			mesConStream.println(message);
			mesConStream.flush();
			mesConStream.close();
		}
		catch (PartInitException e) 
		{
			e.printStackTrace();
			//printToConsole(Util.stackTraceToString(e));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			//printToConsole(Util.stackTraceToString(e));
		}
	}
	
	public static void displayDialog(Shell shell, String mesType, String message) 
	{
		MessageDialog.openInformation(shell, mesType,message);	
	}
	
	public static String stackTraceToString(Throwable e) 
	{
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) 
	    {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    
	    return sb.toString();
	}
}