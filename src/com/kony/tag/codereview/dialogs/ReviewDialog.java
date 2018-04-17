package com.kony.tag.codereview.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class ReviewDialog extends Dialog 
{
	private String mProjDir = null;
	
	public ReviewDialog(IShellProvider parentShell, String projDir) 
	{
		super(parentShell);
		mProjDir = projDir;
		// TODO Auto-generated constructor stub
	}

	public ReviewDialog(Shell parentShell, String projDir) 
	{
		super(parentShell);
		mProjDir = projDir;
		// TODO Auto-generated constructor stub
	}
	
	protected Control createDialogArea(Composite parent)
	{	
		// group for Form Review Items
		Button checkI18n = new Button(parent, SWT.CHECK);
		checkI18n.setText("I18 Keys Where Applicable");
		Button checkSecureSubmit = new Button(parent, SWT.CHECK);
		checkSecureSubmit.setText("Secure Submit For Forms");
		Button checkBlockUI = new Button(parent, SWT.CHECK);
		checkBlockUI.setText("Block UI For Clickable Widgets");
		Button checkFocusSkin = new Button(parent, SWT.CHECK);
		checkFocusSkin.setText("Focus Skin For Clickable Widgets");
		Button checkI18PlaceHolders = new Button(parent, SWT.CHECK);
		checkI18PlaceHolders.setText("I18 Keys For Textbox Place Holders");
		Button checkContainers = new Button(parent, SWT.CHECK);
		checkContainers.setText("Unnecessary Container Widgets");
		Button checkScreenLevel = new Button(parent, SWT.CHECK);
		checkScreenLevel.setText("Segments Not Set As Screenlevel Widgets");
		Button checkSegOrientation = new Button(parent, SWT.CHECK);
		checkSegOrientation.setText("Using HBox/VBox Instead Of Segment Orientation Property");
		Button checkBrowserMapLast = new Button(parent, SWT.CHECK);
		checkBrowserMapLast.setText("Is Browser/Map Last Widget In The Form");
		Button checkFormCodeSnippets = new Button(parent, SWT.CHECK);
		checkFormCodeSnippets.setText("Forms Contain Code Snippets");
		
		// group for Lua review
		Button checkHardCodedStrings = new Button(parent, SWT.CHECK);
		checkHardCodedStrings.setText("Hard Coded Strings");
		Button checkUndeclaredGlobals = new Button(parent, SWT.CHECK);
		checkUndeclaredGlobals.setText("Possible Undeclared Global Variables");
		Button checkWidgetsWithoutLogicalNames = new Button(parent, SWT.CHECK);
		checkWidgetsWithoutLogicalNames.setText("Widgets Used Without Logical Names");
		Button checkVarTrueFalse = new Button(parent, SWT.CHECK);
		checkVarTrueFalse.setText("Comparisons Like var==true or var==false");
		Button checkFunctionLevelComments = new Button(parent, SWT.CHECK);
		checkFunctionLevelComments.setText("Function Level Comments");
		Button checkCommentsClosed = new Button(parent, SWT.CHECK);
		checkCommentsClosed.setText("Comments Improperly Closed");
		Button checkElseIf = new Button(parent, SWT.CHECK);
		checkElseIf.setText("\"Else If\" Instead Of \"ElseIf\"");
		Button checkBigLines = new Button(parent, SWT.CHECK);
		checkBigLines.setText("Lines Longer Than 150 Characters");
		Button checkVarNameConventions = new Button(parent, SWT.CHECK);
		checkVarNameConventions.setText("Standards For Variable Names");

		Control[] tabList = {
							 checkI18n,checkHardCodedStrings,checkSecureSubmit,checkUndeclaredGlobals,checkBlockUI,checkWidgetsWithoutLogicalNames,
							 checkFocusSkin,checkVarTrueFalse,checkI18PlaceHolders,checkFunctionLevelComments,checkContainers,checkCommentsClosed,
							 checkScreenLevel,checkElseIf,checkSegOrientation,checkBigLines,checkBrowserMapLast,checkVarNameConventions,checkFormCodeSnippets
							};
		
		GridLayout colLayout = new  GridLayout();
		colLayout.numColumns  = 2;
		
		parent.setLayout(colLayout);
		parent.setTabList(tabList);
		
		return super.createDialogArea(parent);
	}
	
	protected void okPressed()
	{
		/*
		MessageDialog.openInformation(
				this.getShell(),
				"Kony Project Review",
				"Reviewing Project At Location:" + this.mProjDir);
		*/
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(this.getShell());
		progressDialog.open();
		this.close();
	}
}