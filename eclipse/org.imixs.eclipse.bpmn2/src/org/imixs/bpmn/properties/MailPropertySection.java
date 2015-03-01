package org.imixs.bpmn.properties;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.ImixsRuntimeExtension;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class MailPropertySection extends AbstractImixsPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new MailDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new MailDetailComposite(parent, style);
	}

	public class MailDetailComposite extends AbstractImixsDetailComposite {

		public MailDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public MailDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {

			super.createBindings(be);

			setTitle("Mail Configuration");

			// if the domain was created before we need to put the code into a
			// transaction...
			TransactionalEditingDomain domain = TransactionUtil
					.getEditingDomain(taskConfig);
			if (domain != null) {

				domain.getCommandStack().execute(new RecordingCommand(domain) {
					public void doExecute() {
						bindAttribute(getAttributesParent(),
								getProperty(
										"namMailReceiver"), "value", "To");
						bindAttribute(getAttributesParent(),
								getProperty(
										"txtMailSubject"), "value", "Subject");
						bindAttribute(getAttributesParent(),
								getProperty(
										"rtfMailBody"), "value", "Body");

					}
				});
			}

		}

	}

}
