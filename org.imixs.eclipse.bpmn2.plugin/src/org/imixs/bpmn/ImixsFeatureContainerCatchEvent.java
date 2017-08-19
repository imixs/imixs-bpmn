package org.imixs.bpmn;

import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.event.IntermediateCatchEventFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

public class ImixsFeatureContainerCatchEvent extends CustomShapeFeatureContainer {

	// these values must match what's in the plugin.xml
	public final static String ACTIVITYENTITY_CATCH_EVENT_ID = "org.imixs.workflow.bpmn.ActivityEntityCatchEvent";
	
	/**
	 * This method inspects the object to determine what its custom task ID
	 * should be. In this case, we check the namespace of the "type" attribute.
	 * If the namespace matches the imixs targetNamespace, return the
	 * PROCESSENTITY_TASK_ID string.
	 */
	@Override
	public String getId(EObject object) {
		if (ImixsBPMNPlugin.isImixsCatchEvent(object)) {
			return ACTIVITYENTITY_CATCH_EVENT_ID;
		}
		return null;
	}

	@Override
	public boolean canApplyTo(Object o) {
		boolean b1 = o instanceof IntermediateCatchEvent;
		boolean b2 = o.getClass().isAssignableFrom(IntermediateCatchEvent.class);
		return b1 || b2;
	}

	@Override
	protected IntermediateCatchEventFeatureContainer createFeatureContainer(IFeatureProvider fp) {
		return new IntermediateCatchEventFeatureContainer() {

			/**
			 * override the Add Feature from the chosen Feature Container base
			 * class . Typically you will want to override the decorateShape()
			 * method which allows you to customize the graphical representation
			 * of this Custom Task figure.
			 */
			@Override
			public IAddFeature getAddFeature(IFeatureProvider fp) {
				return new AddIntermediateCatchEventFeature(fp) {
					@Override
					protected void decorateShape(IAddContext context, ContainerShape containerShape,
							IntermediateCatchEvent businessObject) {
						super.decorateShape(context, containerShape, businessObject);

						// add a notifyChangeAdapter to validate the ActiviytID
						businessObject.eAdapters().add(new ImixsIdAdapter());
						businessObject.eAdapters().add(new ImixsEventAdapter(containerShape));
					}
				};
			}

			/**
			 * This method MUST be overridden if we intend to add extension
			 * attributes to your business object (bpmn2 element) - see the
			 * BPMN2 tutorials for details:
			 * https://wiki.eclipse.org/BPMN2-Modeler/DeveloperTutorials
			 * 
			 */
			@Override
			public ICreateFeature getCreateFeature(IFeatureProvider fp) {
				return new CreateIntermediateCatchEventFeature(fp) {
				};
			}

		};
	}

}