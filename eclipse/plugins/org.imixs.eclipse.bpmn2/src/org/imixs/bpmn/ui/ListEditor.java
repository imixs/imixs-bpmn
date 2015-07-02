package org.imixs.bpmn.ui;

import java.util.Iterator;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
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
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.Value;

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
	// protected ValueListAdapter valueListAdapter;
	Image image;
	boolean sortable = false;
	Table table;
	Item item = null;

	/**
	 * Initialize the default values...
	 * 
	 * @param businessObject
	 * @param feature
	 */
	public ListEditor(AbstractDetailComposite parent, Item item) {
		super(parent, item, ImixsBPMNPlugin.IMIXS_ITEMLIST_FEATURE);

		this.item = item;
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
		if (label != null) {
			Label labelWidget = getToolkit()
					.createLabel(parentcomposite, label);
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
					addValue(inputDlg.getValue());
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
				if (iCurrent >= 0) {
					String sCurrent = table.getItems()[iCurrent].getText();
					removeValue(sCurrent);
					updateTable();
				}

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
						replaceValue(sOldValue, sNewValue);
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
						moveUp(iCurrent);
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
					if (iCurrent < item.getValuelist().size() - 1) {
						moveDown(iCurrent);

						updateTable();
						table.select(iCurrent + 1);
					}
				}
			});
		}
		return editorComposite;
	}

	public void addValue(final String newvalue) {
		TransactionalEditingDomain domain = getDiagramEditor()
				.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {

				// insert a new value element
				Value value = ModelFactory.eINSTANCE.createValue();
				value.setValue(newvalue);
				item.getValuelist().add(value);

			}
		});
	}

	public void removeValue(final String sCurrent) {
		TransactionalEditingDomain domain = getDiagramEditor()
				.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {

				// iterate over all item values
				Iterator<Value> iter = item.getValuelist().iterator();
				while (iter.hasNext()) {

					Value val = iter.next();
					if (sCurrent.equals(val.getValue())) {
						item.getValuelist().remove(val);
						break;
					}
				}
			}
		});
	}

	public void replaceValue(final String sold, final String snew) {
		TransactionalEditingDomain domain = getDiagramEditor()
				.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {

				// find position
				// iterate over all item values
				Iterator<Value> iter = item.getValuelist().iterator();
				while (iter.hasNext()) {
					Value val = iter.next();
					if (sold.equals(val.getValue())) {

						val.setValue(snew);
						// item.getValuelist().remove(val);
						break;
					}
				}

			}
		});
	}

	/**
	 * moves a given value up inside the value list
	 * 
	 * @param value
	 */
	public void moveUp(final int i) {
		if (i < 1)
			return;
		TransactionalEditingDomain domain = getDiagramEditor()
				.getEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {
			@Override
			protected void doExecute() {
				String value1 = item.getValuelist().get(i).getValue();
				String value2 = item.getValuelist().get(i - 1).getValue();
				item.getValuelist().get(i).setValue(value2);
				item.getValuelist().get(i - 1).setValue(value1);
			}
		});

	}

	/**
	 * moves a given value down inside the value list
	 * 
	 * @param value
	 */
	public void moveDown(final int i) {

		if (i < item.getValuelist().size() - 1) {
			TransactionalEditingDomain domain = getDiagramEditor()
					.getEditingDomain();
			domain.getCommandStack().execute(new RecordingCommand(domain) {
				@Override
				protected void doExecute() {
					String value1 = item.getValuelist().get(i).getValue();
					String value2 = item.getValuelist().get(i + 1).getValue();
					item.getValuelist().get(i).setValue(value2);
					item.getValuelist().get(i + 1).setValue(value1);
				}
			});
		}
	}

	private void updateTable() {

		table.removeAll();
		// add current values
		for (Value avalue : item.getValuelist()) {
			TableItem tabelItem = new TableItem(table, SWT.NONE);
			tabelItem.setText(avalue.getValue());
			if (image != null) {
				tabelItem.setImage(image);
			}
		}

	}

	@Override
	public Item getValue() {
		return item;
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