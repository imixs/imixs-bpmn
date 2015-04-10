package org.imixs.bpmn;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
/**
 * Imixs ProcessEntity task container
 * 
 * @author rsoika
 *
 */
public class ImixsFeatureContainerTask extends CustomShapeFeatureContainer {

	// these values must match what's in the plugin.xml
	public final static String PROCESSENTITY_TASK_ID = "org.imixs.workflow.bpmn.ProcessEntityTask";
	private static final IColorConstant PROCESSENTITY_BACKGROUND = new ColorConstant(144, 176, 224);

	
	/**
	 * This method inspects the object to determine what its custom task ID
	 * should be. In this case, we check the namespace of the "type" attribute.
	 * If the namespace matches the imixs targetNamespace, return the
	 * PROCESSENTITY_TASK_ID string.
	 */
	@Override
	public String getId(EObject object) {
		EStructuralFeature feature = ModelDecorator.getAnyAttribute(object,
				"processid");
		if (feature != null && feature instanceof EAttribute) {
			if (ImixsRuntimeExtension.targetNamespace
					.equals(((EAttributeImpl) feature).getExtendedMetaData()
							.getNamespace())) {
				return PROCESSENTITY_TASK_ID;
			}
		}

		return null;
	}

	
	/**
	 * overwrite task features displayed during mouse over 
	 */
	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider fp) {
		//return new ICustomFeature[] { new ShowPropertiesFeature(fp) };
		return super.getCustomFeatures(fp);
	}


	
	

	@Override
	protected TaskFeatureContainer createFeatureContainer(IFeatureProvider fp) {
		return new TaskFeatureContainer() {

			
			/**
			 *  override the Add Feature from the chosen Feature Container
			 *  base class . Typically you will want to override the decorateShape() method
			 *   which allows you to customize the graphical representation of this Custom Task figure.
			 */
			@Override
			public IAddFeature getAddFeature(IFeatureProvider fp) {
				return new AddTaskFeature(fp) {

					
					@SuppressWarnings("unused")
					@Override
					protected void decorateShape(IAddContext context, ContainerShape containerShape, Task businessObject) {
						super.decorateShape(context, containerShape, businessObject);
						
						IGaService gaService = Graphiti.getGaService();
						IPeService peService = Graphiti.getPeService();
						// Change the size of the default TextAnnotation selection rectangle
						// Rectangle selectionRect =
						// (Rectangle)containerShape.getGraphicsAlgorithm();
						// int width = 140;
						// int height = 60;
						// selectionRect.setWidth(width);
						// selectionRect.setHeight(height);
				
						
						setFillColor(containerShape);
						

					}
				};
			}

			
			/**
			 *  this MUST be overridden if you intend to add extension attributes to
			 *   your business object (bpmn2 element) - see the code example below. You will also want to
			 *   provide your own images for the tool palette by overriding getCreateImageId() and
			 *   getCreateLargeImageId() in your Create Feature.
			 */
//			@Override
//			public ICreateFeature getCreateFeature(IFeatureProvider fp) {
//				return new CreateTaskFeature(fp) {
//				};
//			}


			/**
			 * Common method used to set the fill color for Imixs CustomTask figure.
			 * This method is called by both the CreateFeature and the UpdateFeature.
			
			 * @param containerShape - the ContainerShape that corresponds to the Task.
			 */
			private void setFillColor(ContainerShape containerShape) {
				Task ta = BusinessObjectUtil.getFirstElementOfType(containerShape, Task.class);
				if (ta!=null) {
					//ExtendedPropertiesAdapter adapter = ExtendedPropertiesAdapter.adapt(ta);
					//Boolean attributeValue = (Boolean)adapter.getFeatureDescriptor("evaluate").getValue();
					Shape shape = containerShape.getChildren().get(0);
					ShapeStyle ss = new ShapeStyle();
					
					
					
					ss.setDefaultColors(PROCESSENTITY_BACKGROUND);
					StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), ta, ss);
					//Graphiti.getPeService().setPropertyValue(containerShape, "evaluate.property", propertyValue);
				}
			}
			
		};
	}
	
	
}
