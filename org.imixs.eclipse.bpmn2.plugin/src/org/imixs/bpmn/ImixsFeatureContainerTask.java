package org.imixs.bpmn;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;

/**
 * Imixs ProcessEntity task container
 * 
 * @author rsoika
 *
 */
public class ImixsFeatureContainerTask extends CustomShapeFeatureContainer {

	// these values must match what's in the plugin.xml
	public final static String PROCESSENTITY_TASK_ID = "org.imixs.workflow.bpmn.ProcessEntityTask";

	/**
	 * 
	 * This method inspects the object to determine what its custom task ID should
	 * be. In this case, we check the namespace of the "type" attribute. If the
	 * namespace matches the imixs targetNamespace, return the PROCESSENTITY_TASK_ID
	 * string.
	 */
	@Override
	public String getId(EObject object) {
		if (ImixsBPMNPlugin.isImixsTask(object)) {
			return PROCESSENTITY_TASK_ID;
		}
		return null;
	}

	@Override
	protected TaskFeatureContainer createFeatureContainer(IFeatureProvider fp) {

		return new TaskFeatureContainer() {

			/**
			 * override the Add Feature from the chosen Feature Container base class .
			 * Typically you will want to override the decorateShape() method which allows
			 * you to customize the graphical representation of this Custom Task figure.
			 */
			@Override
			public IAddFeature getAddFeature(IFeatureProvider fp) {
				return new AddTaskFeature(fp) {

					@Override
					protected void decorateShape(IAddContext context, ContainerShape containerShape,
							Task businessObject) {
						super.decorateShape(context, containerShape, businessObject);

						// suggest next free processID
						ImixsBPMNPlugin.suggestNextProcessId(businessObject);

						// add a notifyChangeAdapter to validate the ActiviytID
						businessObject.eAdapters().add(new ImixsIdAdapter());
						businessObject.eAdapters().add(new ImixsTaskAdapter(containerShape));
					}
				};
			}

			/**
			 * this MUST be overridden if you intend to add extension attributes to your
			 * business object (bpmn2 element) - see the code example below. You will also
			 * want to provide your own images for the tool palette by overriding
			 * getCreateImageId() and getCreateLargeImageId() in your Create Feature.
			 */
			@Override
			public ICreateFeature getCreateFeature(IFeatureProvider fp) {
				return new CreateTaskFeature(fp) {
				};
			}

		};
	}

}
