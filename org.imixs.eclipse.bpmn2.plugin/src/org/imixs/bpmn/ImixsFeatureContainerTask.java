package org.imixs.bpmn;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.AbstractUpdateBaseElementFeature;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

/**
 * Imixs ProcessEntity task container
 * 
 * @author rsoika
 *
 */
public class ImixsFeatureContainerTask extends CustomShapeFeatureContainer {

	public static final IColorConstant PROCESSENTITY_BACKGROUND = new ColorConstant(144, 176, 224);

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
						businessObject.eAdapters().add(new ImixsLayoutTaskAdapter(containerShape));
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

			/**
			 * This method updates the background color
			 */
			@Override
			public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
				MultiUpdateFeature multiUpdate = (MultiUpdateFeature) super.getUpdateFeature(fp);
				multiUpdate.addFeature(new AbstractUpdateBaseElementFeature<Task>(fp) {

					@Override
					public boolean update(IUpdateContext context) {
						// force update of background color
						updateShapeStyle((ContainerShape) context.getPictogramElement());
						return true;
					}
				});

				return multiUpdate;
			}

			/**
			 * Set new background color
			 * <p>
			 * This can not be done by the Adapter class - see issue #75
			 * 
			 * @param containerShape - the ContainerShape that corresponds to the Task.
			 */
			private void updateShapeStyle(ContainerShape containerShape) {
				Task task = BusinessObjectUtil.getFirstElementOfType(containerShape, Task.class);

				if (task != null) {
					// set background color
					ShapeStyle shapeStyle = new ShapeStyle();
					shapeStyle.setDefaultColors(PROCESSENTITY_BACKGROUND);
					Shape shape = containerShape.getChildren().get(0);
					StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), task, shapeStyle);
				}
			}

		};
	}

}
