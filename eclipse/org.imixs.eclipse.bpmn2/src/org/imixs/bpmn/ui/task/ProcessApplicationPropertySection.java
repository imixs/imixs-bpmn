package org.imixs.bpmn.ui.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.bpmn2.modeler.core.preferences.Bpmn2Preferences;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.ImixsDetailComposite;

/**
 * This PorpertySection provides the attributes for Application config.
 * 
 * @author rsoika
 *
 */
public class ProcessApplicationPropertySection extends
DefaultPropertySection {

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

			// create a new Property Tab section with a twistie
			// Composite section = createSectionComposite(this, "Mail Body");

			Property metaData = getPropertyByName((BaseElement) be, "txtForm",null,
					"Some Form....");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					METADATA_VALUE);
			valueEditor.createControl(this, "Subject2");
			
			
			
			this.bindAttribute(this,be, "name");
			this.bindAttribute(this,be, "processid");

			//bindReference(be, "documentation");
		//	bindList(be, "documentation"); //$NON-NLS-1$
			//bindDescription(be);
			
			
		}

	}

}
