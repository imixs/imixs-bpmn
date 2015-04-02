package org.imixs.bpmn.ui.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.ImixsDetailComposite;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class ProcessSummaryPropertySection extends DefaultPropertySection {

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
			setTitle("Application");

			// create a new Property Tab section with a twistie
			// Composite section = createSectionComposite(this, "Mail Body");

			Property metaData = getPropertyByName((BaseElement) be, "txtSummary",null,
					"Summ Form....");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					METADATA_VALUE);
			valueEditor.createControl(this, "Summary");


		}

	}

}
