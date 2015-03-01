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
public class ProcessSummaryPropertySection extends AbstractImixsPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new SummaryDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new SummaryDetailComposite(parent, style);
	}

	public class SummaryDetailComposite extends AbstractImixsDetailComposite {

		public SummaryDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public SummaryDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {

			super.createBindings(be);

			setTitle("Workflow Summary");
			bindAttribute(this.getAttributesParent(),
					getProperty("txtworkflowsummary"), "value", "Summary");
			bindAttribute(this.getAttributesParent(),
					getProperty("txtworkflowabstract"), "value", "Abstract");
		}

		@Override
		public void initializeProperties() {
			initializeProperty("txtworkflowsummary", "");
			initializeProperty("txtworkflowabstract", "");

		}

	}

}
