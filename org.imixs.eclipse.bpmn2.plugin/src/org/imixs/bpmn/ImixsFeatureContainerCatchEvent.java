package org.imixs.bpmn;

import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.AbstractUpdateBaseElementFeature;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.event.IntermediateCatchEventFeatureContainer;
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

public class ImixsFeatureContainerCatchEvent extends CustomShapeFeatureContainer {

	private static final IColorConstant ACTIVITYENTITY_BACKGROUND = new ColorConstant(255, 217, 64);

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
						businessObject.eAdapters().add(new ImixsLayoutEventAdapter(containerShape));
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
			 * Set new background color. 
			 * <p>
			 * This can not be done by the Adapter class - see issue #75
			 * 
			 * @param containerShape
			 *            - the ContainerShape that corresponds to the Event.
			 */
			private void updateShapeStyle(ContainerShape containerShape) {
				IntermediateCatchEvent event = BusinessObjectUtil.getFirstElementOfType(containerShape,
						IntermediateCatchEvent.class);
				
				if (event != null) {
					// set background color
					ShapeStyle shapeStyle = new ShapeStyle();
					shapeStyle.setDefaultColors(ACTIVITYENTITY_BACKGROUND);
					Shape shape = containerShape.getChildren().get(0);
					StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), event, shapeStyle);
				}
			}

		};
	}

}