package org.imixs.bpmn.properties;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.ItemValue;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.Parameter;

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
		AbstractDetailComposite ding;
		public MailDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
			ding=this;
		}

		public MailDetailComposite(Composite parent, int style) {
			super(parent, style);
			ding=this;
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
						
						
						
						Parameter subjectParam = getProperty("txtMailSubject");
						
						
						
						
						
						subjectParam.getItem().add("Hello");
						
					
						 EStructuralFeature itemFeature = subjectParam.eClass().getEStructuralFeature("item");
						
						 EAttribute itemAtrib = (EAttribute)itemFeature;
						 EObject wer = itemAtrib.eContents().get(0);
						
						
						bindAttribute(getAttributesParent(),subjectParam,"item", "Subject");
						
//						bindAttribute(getAttributesParent(),
//								getProperty(
//										"txtMailSubject"), "value", "Subject");
						
//						
//						bindAttribute(getAttributesParent(),
//								getProperty(
//										"rtfMailBody"), "value", "Body");
						
						
						// Neu
						Parameter rtfBodyParam = getProperty("rtfMailBody");
//						bindAttribute(getAttributesParent(),
//								rtfBodyParam, "value", "Body");

					//	rtfBodyParam.getItem().add("servus");
						
						
						bindAttribute(getAttributesParent(),
								rtfBodyParam, "item", "Body");

						
					//	bindReference(getAttributesParent(), rtfBodyParam,rtfBodyParam.getItem().get(0));

						
						
//
//						EStructuralFeature item = rtfBodyParam.eClass().getEStructuralFeature("value");
//						TextObjectEditor scriptEditor = new TextObjectEditor(ding,rtfBodyParam,item);
//						
//					
//						scriptEditor.createControl(getAttributesParent(),"Script"); //$NON-NLS-1$
//						scriptEditor.setMultiLine(true);
						
						
					}
				});
			}

		}

	}

}
