package org.imixs.bpmn.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * This ObjectEditor creates a composite with a list of Check boxes based on a
 * given OptionList. This value results to a String object with a <value> tag
 * for each selected option.
 * 
 * @see org.imixs.bpmn.ui.ValueListAdapter
 * @see org.eclipse.bpmn2.modeler.core.merrimac.dialogs.BooleanObjectEditor
 * @author Ralph Soika
 *
 */
public class CopyOfPluginEditor extends ObjectEditor {
	protected Composite buttons;
	protected ValueListAdapter valueListAdapter;

	/**
	 * Initialize the default values...
	 * 
	 * @param businessObject
	 * @param feature
	 */
	public CopyOfPluginEditor(AbstractDetailComposite parent, EObject obj,
			EStructuralFeature feat, List<String> aoptionList) {
		super(parent, obj, feat);

		Object v = getBusinessObjectDelegate().getValue(object, feature);
		if (v == null)
			v = "";
		valueListAdapter = new ValueListAdapter(aoptionList, v.toString());

	}

	/**
	 * This method creates a composite with a separate CheckBox for each Element
	 * form the OpitonList. The selection is managed by the valueListAdapter
	 * class.
	 */
	protected Control createControl(Composite composite, String label, int style) {

		
				final Table table = getToolkit().createTable(composite, SWT.MULTI
						| SWT.V_SCROLL);
				// fill Tabel
				Collection collection = new Vector();
//				try {
//					collection = modelObject.getItemCollection().getItemValue(
//							sPropertyName);
//				} catch (Exception e) {
//				}
				Iterator iter = collection.iterator();
				while (iter.hasNext()) {
					TableItem tabelItem = new TableItem(table, SWT.NONE);
					tabelItem.setText(iter.next().toString());
				//	tabelItem.setImage(itemImage);
				}
				

				setGridData(table, 180, 250, GridData.FILL, true,
						GridData.FILL, true);

				
				// create buttons
				Composite compositeButtons =  getToolkit().createComposite(composite,
						SWT.NONE);
				FillLayout fillLayoutButtons = new FillLayout();
				fillLayoutButtons.spacing = 2;
				fillLayoutButtons.type = SWT.VERTICAL;
				compositeButtons.setLayout(fillLayoutButtons);
				setGridData(compositeButtons, -1, -1, GridData.BEGINNING, false,
						GridData.BEGINNING, false);

				final Shell shell = parent.getShell();
				final Object localModelObject = "";
				final String localPropertyName = "";
				Button button = getToolkit().createButton(compositeButtons, "Add...",
						SWT.PUSH);
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						InputDialog inputDlg = new InputDialog(shell, "Add Entry",
								"New value", "", null);
						if (inputDlg.open() == InputDialog.OK) {
							TableItem tabelItem = new TableItem(table, SWT.NONE);
							tabelItem.setText(inputDlg.getValue());
							//tabelItem.setImage(itemImage);

							// werte neu setzen
							Vector v = new Vector();
							TableItem[] items = table.getItems();
							for (int i = 0; i < items.length; i++) {
								v.add(items[i].getText());
							}
							//localModelObject.setPropertyValue(localPropertyName, v);
						}
					}
				});

				// Remove Button 
				button = getToolkit().createButton(compositeButtons, "Remove", SWT.PUSH);
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
						//localModelObject.setPropertyValue(localPropertyName, v);
					}
				});

				
				button = getToolkit().createButton(compositeButtons, "Edit",
						SWT.PUSH);
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						
						int iCurrent = table.getSelectionIndex();
						if (iCurrent>=0) {
							TableItem tabelItem=table.getItem(iCurrent);
							
							String sValue=tabelItem.getText();
							
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
								//localModelObject.setPropertyValue(localPropertyName, v);
							}
						}
						
					}
				});
				
				
				
				
				
				
				
				
				
				
				// Move Up Button
				button = getToolkit().createButton(compositeButtons, "Up", SWT.PUSH);
				button.addSelectionListener(new SelectionAdapter() {
					@SuppressWarnings("rawtypes")
					public void widgetSelected(SelectionEvent e) {
						//MessageDialog.o
						int iCurrent = table.getSelectionIndex();
						if (iCurrent>0) {
							TableItem tabelItem=table.getItem(iCurrent);
							TableItem tabelItemnew=new TableItem(table, SWT.NONE,iCurrent-1);
							tabelItemnew.setText(tabelItem.getText());
							tabelItemnew.setImage(tabelItem.getImage());
							table.select(iCurrent-1);
							table.remove(iCurrent+1);
							// refresh values
							
							Vector v = new Vector();
							TableItem[] items = table.getItems();
							for (int i = 0; i < items.length; i++) {
								v.add(items[i].getText());
							}
							//localModelObject.setPropertyValue(localPropertyName, v);
						}
					}
				});

				
				// Move Down Button 
				button = getToolkit().createButton(compositeButtons, "Down", SWT.PUSH);
				button.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						//MessageDialog.o
						int iCurrent = table.getSelectionIndex();
						if (iCurrent<table.getItems().length-1) {
							TableItem tabelItem=table.getItem(iCurrent);
							TableItem tabelItemnew=new TableItem(table, SWT.NONE,iCurrent+2);
							tabelItemnew.setText(tabelItem.getText());
							tabelItemnew.setImage(tabelItem.getImage());
							table.select(iCurrent+2);
							table.remove(iCurrent);
							//updatePluginList();
							
							// werte neu setzen
							Vector v = new Vector();
							TableItem[] items = table.getItems();
							for (int i = 0; i < items.length; i++) {
								v.add(items[i].getText());
							}
							//localModelObject.setPropertyValue(localPropertyName, v);
						}
					}
				});
				


				
		
		
		
		
		
		
		
		
		
		
		return buttons;
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
		buttons.setVisible(visible);
		GridData data = (GridData) buttons.getLayoutData();
		data.exclude = !visible;
	}

	public void dispose() {
		super.dispose();
		if (buttons != null && !buttons.isDisposed()) {
			buttons.dispose();
			buttons = null;
		}
	}

	public Control getControl() {
		return buttons;
	}












	/**
	 * Helper Method to set GridData
	 * 
	 * @param component
	 * @param horizontalAligment
	 * @param grabExcessHorizontalSpace
	 * @param verticalAligment
	 * @param grabExcessVerticalSpace
	 */
	protected void setGridData(Control component, int horizontalAligment,
			boolean grabExcessHorizontalSpace, int verticalAligment,
			boolean grabExcessVerticalSpace) {
		GridData gd = new GridData();
		gd.horizontalAlignment = horizontalAligment;
		gd.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
		gd.verticalAlignment = verticalAligment;
		gd.grabExcessVerticalSpace = grabExcessVerticalSpace;
		component.setLayoutData(gd);
	}

	protected void setGridData(Control component, int width, int hight,
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
