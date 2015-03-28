package org.imixs.bpmn.ui;

import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public abstract class AbstractImixsPropertySection extends AbstractBpmn2PropertySection {

	public AbstractImixsPropertySection() {
		super();
	}

	
	/**
	 * Here we extract the bpmn task element from the current ISelection
	 */
	@Override
	public EObject getBusinessObjectForSelection(ISelection selection) {

		EObject bo = BusinessObjectUtil
				.getBusinessObjectForSelection(selection);
		if (bo instanceof BPMNDiagram) {
			if (((BPMNDiagram) bo).getPlane() != null
					&& ((BPMNDiagram) bo).getPlane().getBpmnElement() != null)
				return ((BPMNDiagram) bo).getPlane().getBpmnElement();
		}
		return bo;
	}


	
	
}
