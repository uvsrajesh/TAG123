package com.kony.tag.arabiclayout.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class CommandLineDialog  extends Dialog{

	private Combo combo;
	private String channel;

	public CommandLineDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Select a Platform");
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Platform: ");
		GridDataFactory.swtDefaults().applyTo(label);
		
		List<String> options  = new ArrayList<String>();
		options.add("Select One");
		options.add("IPhone");
		options.add("Android");
		options.add("Windows 8");
		options.add("Windows 7.5");
		options.add("Blackberry");
		options.add("Blackberry10");
		options.add("IPad");
		options.add("Android Tablet");
		options.add("Windows Tablet");
		options.add("SPA-IPhone");
		options.add("SPA-Android");
		options.add("SPA-Windows 8");
		options.add("SPA-Windows 7.5");
		options.add("SPA-Blackberry");
		options.add("SPA-Blackberry Non Touch");
		options.add("SPA-IPad");
		options.add("SPA-Android Tablet");
		options.add("SPA-Windows Tablet");
		options.add("SPA Desktop Web");
		options.add("Windows Native/Kiosk");
		
		combo = new Combo(container, SWT.READ_ONLY);
		combo.setItems(options.toArray(new String[0]));
		combo.select(0);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(combo);
		
		return container;
	}
	
	
	@Override
	protected void okPressed() {
		channel = combo.getText();
		super.okPressed();
	}
	
	public String getChannel() {
		return channel;
	}
	
}
