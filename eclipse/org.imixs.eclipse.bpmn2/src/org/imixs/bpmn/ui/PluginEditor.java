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
import org.eclipse.swt.layout.GridLayout;
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
public class PluginEditor extends ObjectEditor {
	protected Composite editorComposite;
	protected ValueListAdapter valueListAdapter;

	/**
	 * Initialize the default values...
	 * 
	 * @param businessObject
	 * @param feature
	 */
	public PluginEditor(AbstractDetailComposite parent, EObject obj,
			EStructuralFeature feat) {
		super(parent, obj, feat);

		Object v = getBusinessObjectDelegate().getValue(object, feature);
		if (v == null)
			v = "";
		// valueListAdapter = new ValueListAdapter(aoptionList, v.toString());

	}

	/**
	 * This method creates a composite with a separate CheckBox for each Element
	 * form the OpitonList. The selection is managed by the valueListAdapter
	 * class.
	 */
	protected Control createControl(Composite parentcomposite, String label,
			int style) {

		// create a separate label to the LEFT of the checkbox set
		Label labelWidget = getToolkit().createLabel(parentcomposite, label);
		labelWidget.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		updateLabelDecorator();

		editorComposite = new Composite(parentcomposite, SWT.BORDER);

		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);

		editorComposite.setLayoutData(data);
		//editorComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		
		GridLayout layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		// the number of pixels of vertical margin that will be placed along
		// the top and bottom edges of the layout.
 
		layout.makeColumnsEqualWidth = true;// make each column have same width
		layout.numColumns = 4; // number of columns
		layout.verticalSpacing = 10;
		

		editorComposite.setLayout(layout);
		
		//editorComposite.setLayout(new GridLayout());
		
		

		final Table table = getToolkit().createTable(editorComposite,
				SWT.MULTI | SWT.V_SCROLL);

//		 table.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false,
//		 1, 1));

		// table.setLayoutData(new FillLayout(SWT.VERTICAL));

		final Shell shell = parent.getShell();

		Button button = getToolkit().createButton(editorComposite, "Add...",
				SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				InputDialog inputDlg = new InputDialog(shell, "Add Entry",
						"New value", "", null);
				if (inputDlg.open() == InputDialog.OK) {
					TableItem tabelItem = new TableItem(table, SWT.NONE);
					tabelItem.setText(inputDlg.getValue());
					// tabelItem.setImage(itemImage);

					// werte neu setzen
					Vector v = new Vector();
					TableItem[] items = table.getItems();
					for (int i = 0; i < items.length; i++) {
						v.add(items[i].getText());
					}
					// localModelObject.setPropertyValue(localPropertyName, v);
				}
			}
		});

		return editorComposite;
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
