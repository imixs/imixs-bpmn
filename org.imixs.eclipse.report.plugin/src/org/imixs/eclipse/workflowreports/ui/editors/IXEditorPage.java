package org.imixs.eclipse.workflowreports.ui.editors;

import java.util.*;
import org.eclipse.ui.forms.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.FormColors; //import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.*;
import org.eclipse.ui.forms.events.*;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.imixs.eclipse.workflowreports.model.*;

/**
 * A abstract base class that all Pages that should be added to IXXMLEditor must
 * subclass. Subclasses should override method 'createFormContent(ManagedForm)'
 * to fill the form with content. The method
 * 'initializeWorkflowModel(WorkflowModel)' is called by the IXXMLEditor to
 * signal the page that a WorkflowModel is set or reloaded.
 * 
 * Note that page itself can be loaded lazily (on first open). Consequently, the
 * call to create the form content can come after the editor has been opened for
 * a while (in fact, it is possible to open and close the editor and never
 * create the form because no attempt has been made to show the page).
 * 
 * 
 * 
 * @author Ralph Soika
 */
public abstract class IXEditorPage extends FormPage { // implements
	// KeyListener

	// public FormToolkit toolkit ;

	public IXEditorPage() {
		super("id", "name");
	}

	/**
	 * Primes the form page with a Workflowmodel.
	 * 
	 * @param editor
	 *            the parent editor
	 */
	public abstract void initializeWorkflowModel(ReportObject amodel);

	/**
	 * Helper Method to set GridData
	 * 
	 * @param component
	 * @param horizontalAligment
	 * @param grabExcessHorizontalSpace
	 * @param verticalAligment
	 * @param grabExcessVerticalSpace
	 */
	public void setGridData(Control component, int horizontalAligment,
			boolean grabExcessHorizontalSpace, int verticalAligment,
			boolean grabExcessVerticalSpace) {
		GridData gd = new GridData();
		gd.horizontalAlignment = horizontalAligment;
		gd.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		gd.verticalAlignment = verticalAligment;
		gd.grabExcessVerticalSpace = grabExcessVerticalSpace;
		component.setLayoutData(gd);
	}

	/**
	 * Helper Method to create a Section
	 * 
	 * @param mform
	 * @param title
	 * @param desc
	 * @param numColumns
	 * @return
	 */
	public Composite createSection(IManagedForm managedForm, String titel,
			String desc, int iSectionStyle, int numColumns, GridData aGridData) {

		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		Section section = toolkit.createSection(form.getBody(), iSectionStyle);

		if (aGridData == null)
			section.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true,
					false));
		else
			section.setLayoutData(aGridData);

		section.setText(titel);
		section.setDescription(desc);

		Composite client = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 1;
		layout.marginHeight = 2;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 5;
		layout.numColumns = numColumns;
		client.setLayout(layout);
		client.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		toolkit.paintBordersFor(client);
		section.setClient(client);

		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});
		return client;

	}

	/**
	 * Helper Method to create a Section with an Image HyperLink in the tile bar
	 * 
	 * @param mform
	 * @param title
	 * @param desc
	 * @param numColumns
	 * @param aImageDesc
	 * @return
	 */
	public Composite createSection(IManagedForm mform, String title,
			String desc, int iSectionStyle, int numColumns, GridData aGridData,
			ImageDescriptor aImageDesc) {
		FormToolkit toolkit = mform.getToolkit();

		Composite client = createSection(mform, title, desc, iSectionStyle,
				numColumns, aGridData);
		Section section = (Section) client.getParent();
		ImageHyperlink createLink = toolkit.createImageHyperlink(section,
				SWT.NULL);
		createLink.setImage(aImageDesc.createImage());
		section.setTextClient(createLink);
		return client;
	}

	public void createFormInput(Composite parent, FormToolkit toolkit,
			String sLabel, String sPropertyName, ReportObject modelObject) {
		// Create Label
		Label label = toolkit.createLabel(parent, sLabel, SWT.LEFT);
		// label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		setGridData(label, GridData.BEGINNING, false, GridData.CENTER, false);
		// Create Input Field
		String sValue = (String) modelObject.getPropertyValue(sPropertyName);
		if (sValue == null)
			sValue = "";
		Text textInput = toolkit.createText(parent, sValue, SWT.SINGLE);
		setGridData(textInput, GridData.FILL, true, GridData.CENTER, false);

		final ModelObject localModelObject = modelObject;
		final String localPropertyName = sPropertyName;
		textInput.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Widget widget = e.widget;
				localModelObject.setPropertyValue(localPropertyName,
						((Text) widget).getText());
			}
		});

		// return textInput;
	}

	public void createTextAreaInput(Composite parent, FormToolkit toolkit,
			GridData gd, String sLabel, String sPropertyName,
			ReportObject modelObject) {
		// Create Label
		if (sLabel != null) {
			Label label = toolkit.createLabel(parent, sLabel, SWT.LEFT);
			//label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE
			// ));
			label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
			setGridData(label, GridData.BEGINNING, false, GridData.CENTER,
					false);
		}
		// Create Input Field
		String sValue = (String) modelObject.getPropertyValue(sPropertyName);
		if (sValue == null)
			sValue = "";
		Text textInput = toolkit.createText(parent, sValue, SWT.MULTI
				| SWT.V_SCROLL);
		// setGridData(textInput, GridData.FILL, true, GridData.CENTER, false);
		textInput.setLayoutData(gd);

		final ModelObject localModelObject = modelObject;
		final String localPropertyName = sPropertyName;
		textInput.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Widget widget = e.widget;
				localModelObject.setPropertyValue(localPropertyName,
						((Text) widget).getText());
			}
		});

		// return textInput;
	}

	public void createTableInput(Composite parent, FormToolkit toolkit,
			String sPropertyName, ReportObject modelObject, final Image itemImage) {

		// create composite to arange list and buttons
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 5;
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;

		Composite composite = toolkit.createComposite(parent, SWT.NONE);
		composite.setLayout(layout);
		setGridData(composite, 280, 165, GridData.FILL, true, GridData.FILL,
				true);

		final Table table = toolkit.createTable(composite, SWT.MULTI
				| SWT.V_SCROLL);
		// Tabel f�llen
		Collection collection = new Vector();
		try {
			collection = modelObject.getItemCollection().getItemValue(
					sPropertyName);
		} catch (Exception e) {
		}
		Iterator iter = collection.iterator();
		while (iter.hasNext()) {
			TableItem tabelItem = new TableItem(table, SWT.NONE);
			tabelItem.setText(iter.next().toString());
			tabelItem.setImage(itemImage);
		}

		setGridData(table, 180, 250, GridData.FILL, true, GridData.FILL, true);
		// Buttons erg�nzen
		// Button Composite erzeugen
		Composite compositeButtons = toolkit.createComposite(composite,
				SWT.NONE);
		FillLayout fillLayoutButtons = new FillLayout();
		fillLayoutButtons.spacing = 2;
		fillLayoutButtons.type = SWT.VERTICAL;
		compositeButtons.setLayout(fillLayoutButtons);
		setGridData(compositeButtons, -1, -1, GridData.BEGINNING, false,
				GridData.BEGINNING, false);

		final Shell shell = parent.getShell();
		final ReportObject localModelObject = modelObject;
		final String localPropertyName = sPropertyName;
		Button button = toolkit.createButton(compositeButtons, "Add...",
				SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDlg = new InputDialog(shell, "Add Entry",
						"New value", "", null);
				if (inputDlg.open() == InputDialog.OK) {
					TableItem tabelItem = new TableItem(table, SWT.NONE);
					tabelItem.setText(inputDlg.getValue());
					tabelItem.setImage(itemImage);

					// werte neu setzen
					Vector v = new Vector();
					TableItem[] items = table.getItems();
					for (int i = 0; i < items.length; i++) {
						v.add(items[i].getText());
					}
					localModelObject.setPropertyValue(localPropertyName, v);
				}
			}
		});

		// Remove Button
		button = toolkit.createButton(compositeButtons, "Remove", SWT.PUSH);
		// button.setData(table);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// MessageDialog.o
				int iCurrent = table.getSelectionIndex();
				if (iCurrent >= 0)
					table.remove(iCurrent);
				// werte neu setzen
				Vector v = new Vector();
				TableItem[] items = table.getItems();
				for (int i = 0; i < items.length; i++) {
					v.add(items[i].getText());
				}
				localModelObject.setPropertyValue(localPropertyName, v);
			}
		});

		button = toolkit.createButton(compositeButtons, "Edit", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				int iCurrent = table.getSelectionIndex();
				if (iCurrent >= 0) {
					TableItem tabelItem = table.getItem(iCurrent);

					String sValue = tabelItem.getText();

					InputDialog inputDlg = new InputDialog(shell, "Edit Entry",
							"New value", sValue, null);
					if (inputDlg.open() == InputDialog.OK) {
						tabelItem.setText(inputDlg.getValue());

						// werte neu setzen
						Vector v = new Vector();
						TableItem[] items = table.getItems();
						for (int i = 0; i < items.length; i++) {
							v.add(items[i].getText());
						}
						localModelObject.setPropertyValue(localPropertyName, v);
					}
				}

			}
		});

		// Move Up Button
		button = toolkit.createButton(compositeButtons, "Up", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// MessageDialog.o
				int iCurrent = table.getSelectionIndex();
				if (iCurrent > 0) {
					TableItem tabelItem = table.getItem(iCurrent);
					TableItem tabelItemnew = new TableItem(table, SWT.NONE,
							iCurrent - 1);
					tabelItemnew.setText(tabelItem.getText());
					tabelItemnew.setImage(tabelItem.getImage());
					table.select(iCurrent - 1);
					table.remove(iCurrent + 1);
					// werte neu setzen
					Vector v = new Vector();
					TableItem[] items = table.getItems();
					for (int i = 0; i < items.length; i++) {
						v.add(items[i].getText());
					}
					localModelObject.setPropertyValue(localPropertyName, v);
				}
			}
		});

		// Move Down Button
		button = toolkit.createButton(compositeButtons, "Down", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// MessageDialog.o
				int iCurrent = table.getSelectionIndex();
				if (iCurrent < table.getItems().length - 1) {
					TableItem tabelItem = table.getItem(iCurrent);
					TableItem tabelItemnew = new TableItem(table, SWT.NONE,
							iCurrent + 2);
					tabelItemnew.setText(tabelItem.getText());
					tabelItemnew.setImage(tabelItem.getImage());
					table.select(iCurrent + 2);
					table.remove(iCurrent);
					// updatePluginList();

					// werte neu setzen
					Vector v = new Vector();
					TableItem[] items = table.getItems();
					for (int i = 0; i < items.length; i++) {
						v.add(items[i].getText());
					}
					localModelObject.setPropertyValue(localPropertyName, v);
				}
			}
		});

		toolkit.paintBordersFor(composite);

	}

	public ImageHyperlink createLink(Composite parent, FormToolkit toolkit,
			ImageDescriptor aImageDesc, String sText) {
		// Create new ProcessTree Link bauen
		ImageHyperlink createLink = toolkit.createImageHyperlink(parent,
				SWT.NULL);
		createLink.setImage(aImageDesc.createImage());
		createLink.setText(sText);

		return createLink;
	}

	public Hyperlink createLink(Composite parent, FormToolkit toolkit,
			String sText) {
		Hyperlink link = toolkit.createHyperlink(parent, sText, SWT.WRAP);
		return link;
	}

	public void setGridData(Control component, int width, int hight,
			int horizontalAligment, boolean grabExcessHorizontalSpace,
			int verticalAligment, boolean grabExcessVerticalSpace) {
		GridData gd = new GridData();
		gd.widthHint = width;

		gd.heightHint = hight;
		gd.horizontalAlignment = horizontalAligment;
		gd.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		gd.verticalAlignment = verticalAligment;
		gd.grabExcessVerticalSpace = grabExcessVerticalSpace;
		component.setLayoutData(gd);
	}
}
