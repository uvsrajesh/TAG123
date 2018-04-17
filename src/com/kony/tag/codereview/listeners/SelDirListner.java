package com.kony.tag.codereview.listeners;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

public class SelDirListner implements SelectionListener
{	
	private Shell parentShell = null;
	
	public SelDirListner(Shell paramParentShell)
	{
		parentShell = paramParentShell;
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void widgetSelected(SelectionEvent event) 
	{
		// TODO Auto-generated method stub
		// Open the directory dialog
		DirectoryDialog dirDialog = new DirectoryDialog(parentShell);
		dirDialog.open();
	}
}