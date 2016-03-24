package org.imixs.bpmn.ui.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.ImixsDetailComposite;

/**
 * This PorpertySection provides the attributes for Application config.
 * 
 * @author rsoika
 *
 */
public class ProcessApplicationPropertySection extends AbstractProcessPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new ApplicationDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new ApplicationDetailComposite(parent, style);
	}

	
	public class ApplicationDetailComposite extends ImixsDetailComposite {

		public ApplicationDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public ApplicationDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {

			if (be == null || !(be instanceof Task)) {
				return;
			}
			setTitle("Application");

			// Input Form
			Value itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txteditorid", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, itemValue,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Input Form");

			// Image URL
			itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txtimageurl", null,
					"");
			valueEditor = new TextObjectEditor(this, itemValue, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Status Icon");

			// Type
			itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txttype", null, "");
			valueEditor = new TextObjectEditor(this, itemValue, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Workitem Type");

		}

	}

}
