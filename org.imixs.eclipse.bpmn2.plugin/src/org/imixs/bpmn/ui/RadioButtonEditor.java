package org.imixs.bpmn.ui;

import java.util.Map;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;

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
	protected Map<String, String> optionMap;
	private Layout layout = null;

	/**
	 * Initializes a new ChackBox Editor with a given Option List containing
	 * Keys and Lable Strings
	 * 
	 * @param Item
	 * @param OptionMap
	 */
	public RadioButtonEditor(AbstractDetailComposite parent, Value obj,
			Map<String, String> optionMap) {
		super(parent, obj, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
		this.optionMap = optionMap;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	protected Control createControl(Composite composite, String label, int style) {

		// create a separate label to the LEFT of the radioBox set
		Label labelWidget = getToolkit().createLabel(composite, label);
		labelWidget.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false,
				1, 1));
		updateLabelDecorator();

		editorComposite = new Composite(composite, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		editorComposite.setLayoutData(data);

		if (layout != null)
			editorComposite.setLayout(layout);
		else
			editorComposite.setLayout(new FillLayout(SWT.VERTICAL));

		String sCurrentValue = getValue();

		// create a radiobutton for each entry from the OptionList
		for (Map.Entry<String, String> entry : optionMap.entrySet()) {
			final String aKey = entry.getKey();
			final String aLabel = entry.getValue();

			Button button = getToolkit().createButton(editorComposite, aLabel,
					SWT.RADIO);
			button.setSelection(aKey.equals(sCurrentValue));
			button.setData(aKey);
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
		if (v == null)
			return null;
		else
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
