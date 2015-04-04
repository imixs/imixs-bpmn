package org.imixs.bpmn.ui;

import java.util.List;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.imixs.bpmn.ImixsBPMNPlugin;

/**
 * This ObjectEditor creates a composite with a list of Check boxes based on a
 * given OptionList. This value results to a String object with a <value> tag
 * for each selected option.
 * 
 * @see org.imixs.bpmn.ui.OptionListAdapter
 * @see org.eclipse.bpmn2.modeler.core.merrimac.dialogs.BooleanObjectEditor
 * @author Ralph Soika
 *
 */
public class CheckBoxEditor extends ObjectEditor {
	protected Composite editorComposite;
	protected OptionListAdapter valueListAdapter;

	/**
	 * @param businessObject
	 * @param feature
	 */
	public CheckBoxEditor(AbstractDetailComposite parent, EObject obj,
			 List<String> aoptionList) {
		super(parent, obj, ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);

		Object v = getBusinessObjectDelegate().getValue(object, feature);
		if (v == null)
			v = "";
		valueListAdapter = new OptionListAdapter(aoptionList, v.toString());

	}

	/**
	 * This method creates a composite with a separate CheckBox for each Element
	 * form the OpitonList. The selection is managed by the valueListAdapter
	 * class.
	 */
	protected Control createControl(Composite composite, String label, int style) {

		// create a separate label to the LEFT of the checkbox set
		if (label!=null) {
		Label labelWidget = getToolkit().createLabel(composite, label);
		labelWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 1));
		updateLabelDecorator();
		}
		editorComposite = new Composite(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		editorComposite.setLayoutData(data);
		editorComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		
		// create a checkbox for each entry from the OptionList
		for (String aOption : valueListAdapter.getOptionList()) {

			String aLabel = null;
			String aValue = null;
			int ipos = aOption.indexOf("|");
			if (ipos > -1) {
				aLabel = aOption.substring(0, ipos).trim();
				aValue = aOption.substring(ipos + 1).trim();
			} else {
				aLabel = aOption;
				aValue = aOption;
			}

			Button button = getToolkit().createButton(editorComposite, aLabel,
					SWT.CHECK);
			button.setSelection(valueListAdapter.isSelected(aValue));
			button.setData(aValue);
			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button bb = (Button) e.getSource();
					if (!isWidgetUpdating) {
						boolean checked = bb.getSelection();
						String aValue = (String) bb.getData();
						valueListAdapter.setSelection(aValue, checked);
						setValue(valueListAdapter.getValue());
						bb.setSelection(checked);
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});

		}
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
}
