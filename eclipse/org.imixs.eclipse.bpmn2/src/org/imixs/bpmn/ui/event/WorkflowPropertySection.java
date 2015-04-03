package org.imixs.bpmn.ui.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.Activator;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.ListEditor;
import org.imixs.bpmn.ui.RadioButtonEditor;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class WorkflowPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new BasicDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new BasicDetailComposite(parent, style);
	}

	public class BasicDetailComposite extends ImixsDetailComposite {
		public BasicDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public BasicDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (be == null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			setTitle("Basic");

			// ProcessID
			this.bindAttribute(this, be, "activityid", "ID");

			// Result
			Property metaData = getPropertyByName((BaseElement) be,
					"txtactivityresult", "CDATA", "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					METADATA_VALUE);
			valueEditor.setMultiLine(true);
			valueEditor.setStyle(SWT.MULTI | SWT.V_SCROLL);
			valueEditor.createControl(this, "Result");

			// visibility and roles
			List<String> optionList = new ArrayList<String>();

			optionList.add("No | 0");
			optionList.add("Yes | 1");
			metaData = getPropertyByName((BaseElement) be, "keypublicresult",
					null, "1");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, metaData,
					METADATA_VALUE, optionList);
			aEditor.createControl(this, "Visible");

			// Roles

			metaData = getPropertyByName((BaseElement) be, "$readaccess", null,
					"");
			ListEditor pluginEditor = new ListEditor(this, metaData,
					METADATA_VALUE);

			pluginEditor.setImage(Activator.getDefault()
					.getIcon("name_obj.gif"));
			pluginEditor.createControl(this, "Roles");

		}

	}

}
