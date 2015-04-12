package org.imixs.bpmn.ui.task;

import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.imixs.bpmn.ImixsBPMNPlugin;

/**
 * The AbstractProcessPropertySection is the basic class for all Imixs property
 * Sections. The class overwrite the doReplaceTab method to verify if a specific
 * property tab should be replaced. The property tab will only be replaced if
 * the selected EObject matches the Imixs NameSpace. So for all other model
 * elements the property section will not replace a existing.
 * 
 * @author rsoika
 *
 */
public class AbstractProcessPropertySection extends DefaultPropertySection {

	/**
	 * This method inspects the object to determine if the namespace matches the
	 * imixs targetNamespace. Only in this case the given property tab will be
	 * replaced.
	 */
	@Override
	public boolean doReplaceTab(String id, IWorkbenchPart part,
			ISelection selection) {

		EObject businessObject = BusinessObjectUtil
				.getBusinessObjectForSelection(selection);

		if (ImixsBPMNPlugin.isImixsTask(businessObject)) {
			return true;
		}

		return false;
	}
}
