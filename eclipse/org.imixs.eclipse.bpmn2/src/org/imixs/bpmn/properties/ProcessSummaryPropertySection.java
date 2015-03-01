package org.imixs.bpmn.properties;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.ImixsRuntimeExtension;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.Parameter;

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
			

			// if the domain was created before we need to put the code into a transaction... 
			TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(taskConfig);
			if (domain!=null) {
			domain.getCommandStack().execute(new RecordingCommand(domain) {
			   public void doExecute() {
			      // do the changes here
					bindAttribute(getAttributesParent(),
							ImixsRuntimeExtension.getProperty(taskConfig,"txtworkflowsummary"), "value", "Summary");
					bindAttribute(getAttributesParent(),
							ImixsRuntimeExtension.getProperty(taskConfig,"txtworkflowabstract"), "value", "Abstract");
			   }
			});
			} else {
				bindAttribute(this.getAttributesParent(),
						ImixsRuntimeExtension.getProperty(taskConfig,"txtworkflowsummary"), "value", "Summary");
				bindAttribute(this.getAttributesParent(),
						ImixsRuntimeExtension.getProperty(taskConfig,"txtworkflowabstract"), "value", "Abstract");
				
			}
			
			
		}

	

	}

}
