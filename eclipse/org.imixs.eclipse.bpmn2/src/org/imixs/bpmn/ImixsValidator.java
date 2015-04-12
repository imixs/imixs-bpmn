package org.imixs.bpmn;

import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.impl.IntermediateCatchEventImpl;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

public class ImixsValidator extends AbstractModelConstraint {

	public IStatus validate(IValidationContext ctx) {
		EObject eObj = ctx.getTarget();
		EMFEventType eType = ctx.getEventType();

		// In the case of batch mode.
		System.out.println("ImixsValidator is validating..."
				+ eObj.getClass().getSimpleName() + " "
				+ eObj.eContainer().eContainer().getClass().getSimpleName());

		EObject parent = eObj.eContainer().eContainer();
		if (parent instanceof IntermediateCatchEventImpl) {
			System.out.println("ImixsValidator es geht los");

			EStructuralFeature feature = ModelDecorator.getAnyAttribute(parent,
					"activityid");
			
			
			if (feature != null && feature instanceof EAttribute) {
				if (ImixsRuntimeExtension.targetNamespace
						.equals(((EAttributeImpl) feature)
								.getExtendedMetaData().getNamespace())) {
					System.out.println("ImixsValidator Volltreffer!");

					
					
				
					
					
					
					Object alterWert = parent.eGet(feature);
					System.out.println("alter wert="+alterWert);
					
					IntermediateCatchEvent	event= (IntermediateCatchEvent)parent;// instanceof Impl
					parent.eSet(feature, 5);
					
					Object neuerWert = parent.eGet(feature);
					
					if (neuerWert==Integer.valueOf(5)) {
						System.out.println("Neuer wert="+neuerWert);
					}
				}
			}
		}

		
		return ctx.createSuccessStatus();
	}

}