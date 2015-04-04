package org.imixs.bpmn.ui;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.imixs.bpmn.ImixsBPMNPlugin;

/**
 * This ObjectEditor creates a composite with a list of editable values. This
 * value results to a String object with a <value> tag for each entry option.
 * 
 * @see org.imixs.bpmn.ui.OptionListAdapter
 * @see org.eclipse.bpmn2.modeler.core.merrimac.dialogs.BooleanObjectEditor
 * @author Ralph Soika
 *
 */
public class ListEditor extends ObjectEditor {
	protected Composite editorComposite;
	protected ValueListAdapter valueListAdapter;
	Image image;
	boolean sortable = false;
	Table table;

	/**
	 * Initialize the default values...
	 * 
	 * @param businessObject
	 * @param feature
	 */
	public ListEditor(AbstractDetailComposite parent, EObject obj) {
		super(parent, obj, ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);

		Object v = getBusinessObjectDelegate().getValue(object, feature);
		if (v == null)
			v = "";
		valueListAdapter = new ValueListAdapter(v.toString());

	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	/**
	 * This method creates a composite with a separate CheckBox for each Element
	 * form the OpitonList. The selection is managed by the valueListAdapter
	 * class.
	 */
	protected Control createControl(Composite parentcomposite, String label,
			int style) {

		// create a separate label to the LEFT of the checkbox set
		if (label!=null) {
			Label labelWidget = getToolkit().createLabel(parentcomposite, label);
			labelWidget.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
					false, 1, 1));
			updateLabelDecorator();
		}

		// == editor composite
		editorComposite = new Composite(parentcomposite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		editorComposite.setLayoutData(data);
		editorComposite.setBackground(Display.getDefault().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));

		GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		editorComposite.setLayout(gridlayout);

		// == Table composite
		table = getToolkit().createTable(editorComposite,
				SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		GridData tableGridData = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		table.setLayoutData(tableGridData);

		// add current values
		this.updateTable();

		// == Button composite
		Composite compositeButtons = getToolkit().createComposite(
				editorComposite, SWT.NONE);
		FillLayout fillLayoutButtons = new FillLayout();
		fillLayoutButtons.spacing = 2;
		fillLayoutButtons.type = SWT.VERTICAL;
		compositeButtons.setLayout(fillLayoutButtons);

		final Shell shell = parent.getShell();

		Button button = getToolkit().createButton(compositeButtons, "Add...",
				SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDlg = new InputDialog(shell, "Add Entry",
						"New value", "", null);
				if (inputDlg.open() == InputDialog.OK) {
					valueListAdapter.addValue(inputDlg.getValue());
					setValue(valueListAdapter.getValue());
					updateTable();
				}
			}
		});

		// Remove Button
		button = getToolkit()
				.createButton(compositeButtons, "Remove", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int iCurrent = table.getSelectionIndex();
				String sCurrent = table.getItems()[iCurrent].getText();
				valueListAdapter.removeValue(sCurrent);
				setValue(valueListAdapter.getValue());
				updateTable();

			}
		});

		button = getToolkit().createButton(compositeButtons, "Edit", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int iCurrent = table.getSelectionIndex();
				if (iCurrent >= 0) {
					TableItem tabelItem = table.getItem(iCurrent);
					String sOldValue = tabelItem.getText();
					InputDialog inputDlg = new InputDialog(shell, "Edit Entry",
							"New value", sOldValue, null);
					if (inputDlg.open() == InputDialog.OK) {
						String sNewValue = inputDlg.getValue();

						valueListAdapter.replaceValue(sOldValue, sNewValue);
						setValue(valueListAdapter.getValue());
						updateTable();
					}
				}
			}
		});

		if (sortable) {
			// Move Up Button
			button = getToolkit()
					.createButton(compositeButtons, "Up", SWT.PUSH);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int iCurrent = table.getSelectionIndex();
					if (iCurrent > 0) {
						valueListAdapter.moveUp(iCurrent);
						setValue(valueListAdapter.getValue());
						updateTable();
						table.select(iCurrent - 1);
					}
				}
			});

			// Move Down Button
			button = getToolkit().createButton(compositeButtons, "Down",
					SWT.PUSH);
			button.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int iCurrent = table.getSelectionIndex();
					if (iCurrent < valueListAdapter.valueList.size() - 1) {
						valueListAdapter.moveDown(iCurrent);
						setValue(valueListAdapter.getValue());
						updateTable();
						table.select(iCurrent + 1);
					}
				}
			});
		}
		return editorComposite;
	}

	private void updateTable() {

		table.removeAll();
		// add current values
		for (String avalue : valueListAdapter.valueList) {
			TableItem tabelItem = new TableItem(table, SWT.NONE);
			tabelItem.setText(avalue);
			if (image != null) {
				tabelItem.setImage(image);
			}
		}

	}

	@Override
	public String getValue() {
		return valueListAdapter.getValue();
	}

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		editorComposite.setVisible(visible);
		GridData data = (GridData) editorComposite.getLayoutData();
		data.exclude = !visible;
	}

	public void dispose() {
		super.dispose();
		if (editorComposite != null && !editorComposite.isDisposed()) {
			editorComposite.dispose();
			editorComposite = null;
		}
	}

	public Control getControl() {
		return editorComposite;
	}

}
