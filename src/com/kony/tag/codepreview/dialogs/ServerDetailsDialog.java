package com.kony.tag.codepreview.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class ServerDetailsDialog extends Dialog 
{
	public ServerDetailsDialog(IShellProvider parentShell) 
	{
		super(parentShell);
		//mProjDir = projDir;
		// TODO Auto-generated constructor stub
	}

	public ServerDetailsDialog(Shell parentShell) 
	{
		super(parentShell);
		//mProjDir = projDir;
		// TODO Auto-generated constructor stub
	}
	
	protected Control createDialogArea(Composite parent)
	{
		Composite serverCmp = new Composite(parent, SWT.EMBEDDED);
		RowLayout serverCmpLayout = new RowLayout(SWT.VERTICAL);
		serverCmp.setLayout(serverCmpLayout);
		Label lblServerIP = new Label(serverCmp, SWT.LEFT);
		lblServerIP.setText("Please enter server IP/Name:");
		Text txtServerIP = new Text(serverCmp, SWT.SINGLE);
		Control[] serverCntrols = {lblServerIP, txtServerIP};
		serverCmp.setTabList(serverCntrols);

		// empty composite
		Composite emptyCmp = new Composite(parent, SWT.EMBEDDED);
		emptyCmp.setLayout(serverCmpLayout);

		Group jsGrpBox = new Group(parent, SWT.SHADOW_ETCHED_OUT);
		jsGrpBox.setText("Select JS Files:");
		// group for JS files
		Button checkI18n = new Button(jsGrpBox, SWT.CHECK);
		checkI18n.setText("I18 Keys Where Applicable");
		Button checkSecureSubmit = new Button(jsGrpBox, SWT.CHECK);
		checkSecureSubmit.setText("Secure Submit For Forms");
		Button checkBlockUI = new Button(jsGrpBox, SWT.CHECK);
		checkBlockUI.setText("Block UI For Clickable Widgets");
		Button checkFocusSkin = new Button(jsGrpBox, SWT.CHECK);
		checkFocusSkin.setText("Focus Skin For Clickable Widgets");
		Button checkI18PlaceHolders = new Button(jsGrpBox, SWT.CHECK);
		checkI18PlaceHolders.setText("I18 Keys For Textbox Place Holders");
		Button checkContainers = new Button(jsGrpBox, SWT.CHECK);
		checkContainers.setText("Unnecessary Container Widgets");
		Button checkScreenLevel = new Button(jsGrpBox, SWT.CHECK);
		checkScreenLevel.setText("Segments Not Set As Screenlevel Widgets");
		Button checkSegOrientation = new Button(jsGrpBox, SWT.CHECK);
		checkSegOrientation.setText("Using HBox/VBox Instead Of Segment Orientation Property");
		Button checkBrowserMapLast = new Button(jsGrpBox, SWT.CHECK);
		checkBrowserMapLast.setText("Is Browser/Map Last Widget In The Form");
		Button checkFormCodeSnippets = new Button(jsGrpBox, SWT.CHECK);
		checkFormCodeSnippets.setText("Forms Contain Code Snippets");
		Control[] tabJSList = {
				 checkI18n,checkSecureSubmit,checkBlockUI,
				 checkFocusSkin,checkI18PlaceHolders,checkContainers,
				 checkScreenLevel,checkSegOrientation,checkBrowserMapLast,checkFormCodeSnippets
				};
		GridLayout colJSLayout = new  GridLayout();
		colJSLayout.numColumns  = 1;
		
		jsGrpBox.setLayout(colJSLayout);
		jsGrpBox.setTabList(tabJSList);
		
		// group for images
		Group imgGrpBox = new Group(parent, SWT.SHADOW_ETCHED_OUT);
		imgGrpBox.setText("Select Images:");
		Button checkHardCodedStrings = new Button(imgGrpBox, SWT.CHECK);
		checkHardCodedStrings.setText("Hard Coded Strings");
		Button checkUndeclaredGlobals = new Button(imgGrpBox, SWT.CHECK);
		checkUndeclaredGlobals.setText("Possible Undeclared Global Variables");
		Button checkWidgetsWithoutLogicalNames = new Button(imgGrpBox, SWT.CHECK);
		checkWidgetsWithoutLogicalNames.setText("Widgets Used Without Logical Names");
		Button checkVarTrueFalse = new Button(imgGrpBox, SWT.CHECK);
		checkVarTrueFalse.setText("Comparisons Like var==true or var==false");
		Button checkFunctionLevelComments = new Button(imgGrpBox, SWT.CHECK);
		checkFunctionLevelComments.setText("Function Level Comments");
		Button checkCommentsClosed = new Button(imgGrpBox, SWT.CHECK);
		checkCommentsClosed.setText("Comments Improperly Closed");
		Button checkElseIf = new Button(imgGrpBox, SWT.CHECK);
		checkElseIf.setText("\"Else If\" Instead Of \"ElseIf\"");
		Button checkBigLines = new Button(imgGrpBox, SWT.CHECK);
		checkBigLines.setText("Lines Longer Than 150 Characters");
		Button checkVarNameConventions = new Button(imgGrpBox, SWT.CHECK);
		checkVarNameConventions.setText("Standards For Variable Names");

		Control[] tabImageList = {
				checkHardCodedStrings,checkUndeclaredGlobals,checkWidgetsWithoutLogicalNames,
				checkVarTrueFalse,checkFunctionLevelComments,checkCommentsClosed,
				checkElseIf,checkBigLines,checkVarNameConventions
				};
		GridLayout colImageLayout = new  GridLayout();
		colImageLayout.numColumns  = 1;
		
		imgGrpBox.setLayout(colImageLayout);
		imgGrpBox.setTabList(tabImageList);

		Button checkResourceBundle = new Button(parent, SWT.CHECK);
		checkResourceBundle.setText("Resource Bundle");

		/*
		Control[] tabList = {
							 checkI18n,checkHardCodedStrings,checkSecureSubmit,checkUndeclaredGlobals,checkBlockUI,checkWidgetsWithoutLogicalNames,
							 checkFocusSkin,checkVarTrueFalse,checkI18PlaceHolders,checkFunctionLevelComments,checkContainers,checkCommentsClosed,
							 checkScreenLevel,checkElseIf,checkSegOrientation,checkBigLines,checkBrowserMapLast,checkVarNameConventions,checkFormCodeSnippets
							};
		*/
		Control[] tabList = {serverCmp, emptyCmp, jsGrpBox, imgGrpBox, checkResourceBundle};
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