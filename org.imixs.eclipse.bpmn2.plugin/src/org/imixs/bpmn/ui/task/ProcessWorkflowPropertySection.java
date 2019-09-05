package org.imixs.bpmn.ui.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.ImixsDetailComposite;

/**
 * This PorpertySection provides the attributes for Process config.
 * 
 * @author rsoika
 *
 */
public class ProcessWorkflowPropertySection extends AbstractProcessPropertySection {

	public final static String REPLACE_PROPERTY_TAB_ID = "org.eclipse.bpmn2.modeler.activity.io.tab";

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new SummaryDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new SummaryDetailComposite(parent, style);
	}

	
	public class SummaryDetailComposite extends ImixsDetailComposite {

		public SummaryDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public SummaryDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {
			setTitle("Workflow Configuration");

			// ProcessID
		//	this.bindAttribute(attributesComposite, be, "processid");

			// Summary
			Value itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txtworkflowsummary", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, itemValue,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Summary");

			// Abstract
			itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txtworkflowabstract", "CDATA", "");
			valueEditor = new TextObjectEditor(this, itemValue, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.setMultiLine(true);
			
			valueEditor.createControl(attributesComposite, "Abstract");

			
		}

	}

}
