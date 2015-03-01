package org.imixs.bpmn.properties;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class ProcessApplicationPropertySection extends AbstractImixsPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new ApplicationDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new ApplicationDetailComposite(parent, style);
	}

	public class ApplicationDetailComposite extends AbstractImixsDetailComposite {

		public ApplicationDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public ApplicationDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {

			super.createBindings(be);

			setTitle("Application Properties");
			bindAttribute(this.getAttributesParent(),
					getProperty("txtEditorID"), "value", "Form Name");
			bindAttribute(this.getAttributesParent(),
					getProperty("txtImageURL"), "value", "Image URL");
			bindAttribute(this.getAttributesParent(),
					getProperty("txtType"), "value", "Type");
		}

		@Override
		public void initializeProperties() {
			initializeProperty("txtEditorID", "");
			initializeProperty("txtImageURL", "");
			initializeProperty("txtType", "");

		}

	}

}
