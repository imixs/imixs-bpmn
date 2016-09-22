package org.imixs.eclipse.workflowreports.restservice;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class LoginDialog extends TitleAreaDialog implements ModifyListener {
	Text textUserID, textPassword;

	String sUserID, sPassword;

	public LoginDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * @see org.eclipse.jface.window.Window#create() We complete the dialog with
	 *      a title and a message
	 */
	public void create() {
		super.create();
		setTitle("Login");
		setMessage("Please enter user name and password");
	}

	protected Control createDialogArea(Composite parent) {
		// Create new composite as container
		final Composite area = new Composite(parent, SWT.NULL);
		final GridLayout gridLayout = new GridLayout(1, true);
		gridLayout.marginWidth = 35;
		gridLayout.marginHeight = 15;
		area.setLayout(gridLayout);

		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.grabExcessHorizontalSpace = true;
		area.setLayoutData(gd);

		createLoginControls(area);

		return area;
	}

	private void createLoginControls(Composite acomposite) {
		/*
		 * Zweispaltiges Layout f�r Boxen
		 */
		GridLayout layoutGrid = new GridLayout();
		layoutGrid.marginHeight = 10;
		layoutGrid.marginWidth = 10;
		layoutGrid.numColumns = 2;

		GridData gdGroup = new GridData();
		gdGroup.horizontalAlignment = GridData.CENTER;
		// gdGroup.verticalAlignment = GridData.CENTER;
		gdGroup.widthHint = 280;
		gdGroup.grabExcessHorizontalSpace = true;

		GridData gdLabel = new GridData();
		gdLabel.widthHint = 60;

		GridData gdText = new GridData();
		gdText.horizontalAlignment = GridData.CENTER;
		gdText.verticalAlignment = GridData.CENTER;
		gdText.widthHint = 180;

		/*
		 * Group for WebService connection data 2 spaltiges Layout
		 */
		Group group = new Group(acomposite, SWT.NONE);
		/*
		 * Zweispaltiges Layout f�r Boxen
		 */
		group.setLayout(layoutGrid);
		group.setText("Login");
		group.setLayoutData(gdGroup);

		/*
		 * textWebServiceLocation
		 */
		Label label = new Label(group, SWT.NONE);
		label.setText("Name: ");
		label.setLayoutData(gdLabel);
		textUserID = new Text(group, SWT.BORDER);
		textUserID.setLayoutData(gdText);
		textUserID.addModifyListener(this);
		/*
		 * textServiceName
		 */
		label = new Label(group, SWT.NONE);
		label.setText("Password: ");
		textPassword = new Text(group, SWT.BORDER);
		textPassword.setLayoutData(gdText);
		textPassword.setEchoChar('*');
		textPassword.addModifyListener(this);
	}

	public String getSecurityPrinzipal() {
		return sUserID;
	}

	public String getSecurityCredentials() {
		return sPassword;
	}

	public void modifyText(ModifyEvent e) {
		try {
			if (e.widget == textUserID)
				sUserID = textUserID.getText();
			if (e.widget == textPassword)
				sPassword = textPassword.getText();
						
		} catch (Exception exception) {
			// no op;
		}
	}
}
