package com.kony.tag.codereview.console;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class ReviewConsole 
{
	private static MessageConsole myConsole = null;
	private ReviewConsole()
	{	
	}
	
	public synchronized static MessageConsole getMessageConsole() throws PartInitException
	{	
		if(myConsole == null)
		{
			ConsolePlugin plugin = ConsolePlugin.getDefault();
		    IConsoleManager conMan = plugin.getConsoleManager();
		    
		    myConsole = new MessageConsole("Kony Project Review", null);
		    conMan.addConsoles(new IConsole[]{myConsole});
		}
		
		ConsolePlugin.getDefault().getConsoleManager().showConsoleView(myConsole);
	    myConsole.activate();
	    
		return myConsole;
	}
}
