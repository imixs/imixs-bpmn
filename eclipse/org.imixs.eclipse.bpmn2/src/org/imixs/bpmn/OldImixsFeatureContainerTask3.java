package org.imixs.bpmn;

import org.eclipse.bpmn2.BoundaryEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.IShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer.CreateTaskFeature;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.imixs.bpmn.ImixsTaskFeatureContainer.ImixsAddTaskFeature;

/**
 * Imixs ProcessEntity task container
 * 
 * @author rsoika
 *
 */
public class OldImixsFeatureContainerTask3 extends CustomShapeFeatureContainer {
 
	// these values must match what's in the plugin.xml
	public final static String PROCESSENTITY_TASK_ID = "xxxorg.imixs.workflow.bpmn.ProcessEntityTask";
	private static final IColorConstant PROCESSENTITY_BACKGROUND = new ColorConstant(
			144, 176, 224);
  
	
	
	public OldImixsFeatureContainerTask3() {
		System.out.println("DEBUG ImixsCreateCustomTaskFeature- constructor...");
	}
	
	
	@Override
	public boolean canApplyTo(Object o) {
		System.out.println("DEBUG ImixsCreateCustomTaskFeature- canApplyTo...");
		boolean b1 =  o instanceof Task;
		boolean b2 = o.getClass().isAssignableFrom(Task.class);
		return b1 || b2;
	}

	
	@Override
	protected IShapeFeatureContainer createFeatureContainer(IFeatureProvider fp) {
		System.out.println("DEBUG ImixsCreateCustomTaskFeature->createFeatureContainer");
		return new TaskFeatureContainer() {
			@Override
			public ICreateFeature getCreateFeature(IFeatureProvider fp) {
				return new ImixsCreateCustomTaskFeature(fp);
			}
			
			@Override
			public IAddFeature getAddFeature(IFeatureProvider fp) {
				return new ImixsmAddCustomTaskFeature(fp);
			}
		};
	}
	
	
	protected class ImixsCreateCustomTaskFeature extends CreateTaskFeature {

		public ImixsCreateCustomTaskFeature(IFeatureProvider fp) {
			super(fp);
			System.out.println("DEBUG ImixsCreateCustomTaskFeature");
		
		}

		
	}
	
	
	protected class ImixsmAddCustomTaskFeature extends ImixsAddTaskFeature {

		public ImixsmAddCustomTaskFeature(IFeatureProvider fp) {
			super(fp);
			System.out.println("DEBUG ImixsmAddCustomTaskFeature");
		}
		
		@Override
		protected void decorateShape(IAddContext context, ContainerShape containerShape, Task businessObject) {
			System.out.println("DEBUG ImixsmAddCustomTaskFeature decorateShape");
			
			super.decorateShape(context, containerShape, businessObject);
			setFillColor(containerShape);
			
			// suggest next free processID
			ImixsBPMNPlugin.suggestNextProcessId(businessObject);
			
			// add a notifyChangeAdapter to validate the ActiviytID
			businessObject.eAdapters().add(new ImixsTaskAdapter());
		}
	}
	
	/**
	 * 
	 * This method inspects the object to determine what its custom task ID
	 * should be. In this case, we check the namespace of the "type" attribute.
	 * If the namespace matches the imixs targetNamespace, return the
	 * PROCESSENTITY_TASK_ID string.
	 */
	@Override
	public String getId(EObject object) {
		System.out.println("ImixsFeatureContaierTask - getID ....");
		if (ImixsBPMNPlugin.isImixsTask(object)) {
			
			System.out.println("     - getID : "+PROCESSENTITY_TASK_ID);
			
			return PROCESSENTITY_TASK_ID;
		}
		System.out.println("    - getID : null");
		
		return null;
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
		Task ta = BusinessObjectUtil.getFirstElementOfType(
				containerShape, Task.class);
		if (ta != null) {
			// ExtendedPropertiesAdapter adapter =
			// ExtendedPropertiesAdapter.adapt(ta);
			// Boolean attributeValue =
			// (Boolean)adapter.getFeatureDescriptor("evaluate").getValue();
			Shape shape = containerShape.getChildren().get(0);
			ShapeStyle ss = new ShapeStyle();

			ss.setDefaultColors(PROCESSENTITY_BACKGROUND);
			StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), ta, ss);
			// Graphiti.getPeService().setPropertyValue(containerShape,
			// "evaluate.property", propertyValue);
		}
	}

}
