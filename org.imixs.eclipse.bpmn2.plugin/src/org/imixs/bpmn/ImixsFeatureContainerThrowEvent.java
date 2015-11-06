package org.imixs.bpmn;

import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.event.IntermediateThrowEventFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

public class ImixsFeatureContainerThrowEvent extends CustomShapeFeatureContainer {

	// these values must match what's in the plugin.xml
	public final static String ACTIVITYENTITY_THROW_EVENT_ID = "org.imixs.workflow.bpmn.ActivityEntityThrowEvent";
	private static final IColorConstant ACTIVITYENTITY_BACKGROUND = new ColorConstant(255, 217, 64);

	/**
	 * This method inspects the object to determine what its custom task ID
	 * should be. In this case, we check the namespace of the "type" attribute.
	 * If the namespace matches the imixs targetNamespace, return the
	 * PROCESSENTITY_TASK_ID string.
	 */
	@Override
	public String getId(EObject object) {
		if (ImixsBPMNPlugin.isImixsThrowEvent(object)) {
			return ACTIVITYENTITY_THROW_EVENT_ID;
		}
		return null;
	}

	@Override
	public boolean canApplyTo(Object o) {
		boolean b1 = o instanceof IntermediateThrowEvent;
		boolean b2 = o.getClass().isAssignableFrom(IntermediateThrowEvent.class);
		return b1 || b2;
	}

	@Override
	protected IntermediateThrowEventFeatureContainer createFeatureContainer(IFeatureProvider fp) {
		return new IntermediateThrowEventFeatureContainer() {

			/**
			 * override the Add Feature from the chosen Feature Container base
			 * class . Typically you will want to override the decorateShape()
			 * method which allows you to customize the graphical representation
			 * of this Custom Task figure.
			 */
			@Override
			public IAddFeature getAddFeature(IFeatureProvider fp) {
				return new AddIntermediateThrowEventFeature(fp) {

					@Override
					protected void decorateShape(IAddContext context, ContainerShape containerShape,
							IntermediateThrowEvent businessObject) {
						super.decorateShape(context, containerShape, businessObject);

						setFillColor(containerShape);

						// add a notifyChangeAdapter to validate the ActiviytID
						businessObject.eAdapters().add(new ImixsEventAdapter());

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
				return new CreateIntermediateThrowEventFeature(fp) {
				};
			}

			/**
			 * Common method used to set the fill color for Imixs CustomTask
			 * figure. This method is called by both the CreateFeature and the
			 * UpdateFeature.
			 * 
			 * @param containerShape
			 *            - the ContainerShape that corresponds to the Task.
			 */
			private void setFillColor(ContainerShape containerShape) {
				IntermediateThrowEvent ta = BusinessObjectUtil.getFirstElementOfType(containerShape,
						IntermediateThrowEvent.class);
				if (ta != null) {
					Shape shape = containerShape.getChildren().get(0);
					ShapeStyle shapeStyle = new ShapeStyle();

					shapeStyle.setDefaultColors(ACTIVITYENTITY_BACKGROUND);
					StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), ta, shapeStyle);
				}
			}

		};
	}

}