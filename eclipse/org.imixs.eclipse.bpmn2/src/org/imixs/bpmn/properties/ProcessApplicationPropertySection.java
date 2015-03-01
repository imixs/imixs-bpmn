package org.imixs.bpmn.properties;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Composite;

/**
 * This PorpertySection provides the attributes for Application config.
 * 
 * @author rsoika
 *
 */
public class ProcessApplicationPropertySection extends
		AbstractImixsPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new ApplicationDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new ApplicationDetailComposite(parent, style);
	}

	public class ApplicationDetailComposite extends
			AbstractImixsDetailComposite {

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

			// if the domain was created before we need to put the code into a
			// transaction...
			TransactionalEditingDomain domain = TransactionUtil
					.getEditingDomain(taskConfig);
			if (domain != null) {

				domain.getCommandStack().execute(new RecordingCommand(domain) {
					public void doExecute() {
						bindAttribute(getAttributesParent(),
								getProperty("txtEditorID"), "value",
								"Form Name");
						bindAttribute(getAttributesParent(),
								getProperty("txtImageURL"), "value",
								"Image URL");
						bindAttribute(getAttributesParent(),

						getProperty("txtType"), "value", "Type");
					}
				});
			}
		}

	}

}
