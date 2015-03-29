package org.imixs.bpmn.ui;

import java.util.List;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

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
public class CheckBoxEditor extends ObjectEditor {
	protected Composite buttons;
	protected ValueListAdapter valueListAdapter;

	/**
	 * @param businessObject
	 * @param feature
	 */
	public CheckBoxEditor(AbstractDetailComposite parent, EObject obj,
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

		// create a separate label to the LEFT of the checkbox set
		Label labelWidget = getToolkit().createLabel(composite, label);
		labelWidget.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		updateLabelDecorator();

		buttons = new Composite(composite, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false,
				1, 1));
		buttons.setLayout(new FillLayout(SWT.VERTICAL));
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

			Button button = getToolkit().createButton(buttons, aLabel,
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
}
