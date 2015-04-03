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
 * Implementation of Radio Button widget based on a option list.
 * 
 * This value results to a String object with a <value> tag for each selected
 * option.
 * 
 * 
 * yes|2
 * 
 * results in
 * 
 * <code>
 *   <value>2</value>
 * </code>
 * 
 * @see org.eclipse.bpmn2.modeler.core.merrimac.dialogs.BooleanObjectEditor
 * @author Ralph Soika
 *
 */
public class RadioButtonEditor extends ObjectEditor {

	protected Composite editorComposite;
	protected List<String> optionList;

	/**
	 * @param businessObject
	 * @param feature
	 */
	public RadioButtonEditor(AbstractDetailComposite parent, EObject obj,
			EStructuralFeature feat, List<String> aoptionList) {
		super(parent, obj, feat);
		optionList = aoptionList;
	}

	protected Control createControl(Composite composite, String label, int style) {

		// create a separate label to the LEFT of the checkbox set
		Label labelWidget = getToolkit().createLabel(composite, label);
		labelWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1));
		updateLabelDecorator();

		editorComposite = new Composite(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
		editorComposite.setLayoutData(data);
		editorComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		String sCurrentValue = getValue();

		// create a checkbox for each entry from the OptionList
		for (String aOption : optionList) {

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
					SWT.RADIO);
			button.setSelection(aValue.equals(sCurrentValue));
			button.setData(aValue);
			button.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Button bb = (Button) e.getSource();
					if (!isWidgetUpdating) {
						boolean checked = bb.getSelection();
						if (checked) {
							String aValue = (String) bb.getData();
							setValue(aValue);
							bb.setSelection(checked);
						}
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
		Object v = getBusinessObjectDelegate().getValue(object, feature);

		return v.toString();

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
