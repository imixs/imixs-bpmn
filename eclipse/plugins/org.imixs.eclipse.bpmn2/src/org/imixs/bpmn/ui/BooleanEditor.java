package org.imixs.bpmn.ui;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.ObjectEditor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;

/**
 * Alternative implementation of the BPMN2 BooleanObjectEditor. This editors
 * value results to a imixs:value object
 * 
 * <code>
 *   <imixs:item name="keybolean" type="xs:bolean">
      <imixs:value>true</imixs:value>
    </imixs:item>
 * </code>
 * 
 * @see org.eclipse.bpmn2.modeler.core.merrimac.dialogs.BooleanObjectEditor
 * @author Bob Brodt, Ralph Soika
 *
 */
public class BooleanEditor extends ObjectEditor {

	protected Button button;

	/**
	 * @param businessObject
	 * @param feature
	 */
	public BooleanEditor(AbstractDetailComposite parent, Value obj) {
		super(parent, obj, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
	}

	public void addSelectionListener(SelectionListener sl) {
		button.addSelectionListener(sl);
	}

	protected Control createControl(Composite composite, String label, int style) {

		// create a separate label to the LEFT of the checkbox, otherwise the
		// grid layout will
		// be off by one column for all other widgets that are created after
		// this one.
		createLabel(composite, label);

		button = getToolkit().createButton(composite, "", SWT.CHECK); //$NON-NLS-1$
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2,
				1));
		button.setSelection(getValue());
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isWidgetUpdating) {
					boolean checked = button.getSelection();

					setValue((checked == true) ? "true" : "false");

					button.setSelection(getValue());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		return button;
	}

	@Override
	public Boolean getValue() {
		Object v = getBusinessObjectDelegate().getValue(object, feature);
		if (v instanceof Boolean)
			return (Boolean) v;
		if (v instanceof String) {
			if ("true".equalsIgnoreCase((String) v)) //$NON-NLS-1$
				return Boolean.TRUE;
			// translate integer values as strings
			try {
				if (Integer.parseInt((String) v) != 0)
					return Boolean.TRUE;
			} catch (Exception e) {
			}
		}
		if (v instanceof Integer && ((Integer) v).intValue() != 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		if (notification.getEventType() == -1
				|| (object == notification.getNotifier() && feature == notification
						.getFeature())) {
			Object value = getValue();
			if (value == null) {
				value = Boolean.FALSE;
			} else {
				value = Boolean.parseBoolean(value.toString());
			}
			button.setSelection((Boolean) value);
		}
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		button.setVisible(visible);
		GridData data = (GridData) button.getLayoutData();
		data.exclude = !visible;
	}

	public void dispose() {
		super.dispose();
		if (button != null && !button.isDisposed()) {
			button.dispose();
			button = null;
		}
	}

	public Control getControl() {
		return button;
	}
}
