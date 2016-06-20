package org.imixs.bpmn;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2AddFeature;
import org.eclipse.bpmn2.modeler.core.features.AbstractUpdateBaseElementFeature;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.IFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.ShowPropertiesFeature;
import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskImageProvider;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.FeatureSupport;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.PropertyContainer;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.styles.Color;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.jface.resource.ImageDescriptor;

import org.imixs.bpmn.model.Value;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

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

	private static final IColorConstant PROCESSENTITY_BACKGROUND_ACL = new ColorConstant(144, 106, 224);

	private List<String> registeredTaskIcons;

	/**
	 * 
	 * This method inspects the object to determine what its custom task ID
	 * should be. In this case, we check the namespace of the "type" attribute.
	 * If the namespace matches the imixs targetNamespace, return the
	 * PROCESSENTITY_TASK_ID string.
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
			 * override the Add Feature from the chosen Feature Container base
			 * class . Typically you will want to override the decorateShape()
			 * method which allows you to customize the graphical representation
			 * of this Custom Task figure.
			 */
			@Override
			public IAddFeature getAddFeature(IFeatureProvider fp) {
				return new AddTaskFeature(fp) {

					@Override
					protected void decorateShape(IAddContext context, ContainerShape containerShape,
							Task businessObject) {
						super.decorateShape(context, containerShape, businessObject);

						// test current ACL setting...
						Value valueUpdateACL = ImixsBPMNPlugin.getItemValueByName(businessObject, "keyUpdateACL",
								"xs:boolean", "false");
						System.out.println("Task - ACL=" + valueUpdateACL);

						// ContainerShape komischesDing = (ContainerShape)
						// ((IPictogramElementContext)
						// context).getPictogramElement();

						updateShapeStyle(containerShape);

						// suggest next free processID
						ImixsBPMNPlugin.suggestNextProcessId(businessObject);

						// add a notifyChangeAdapter to validate the ActiviytID
						businessObject.eAdapters().add(new ImixsTaskAdapter());

					}
				};
			}

			/**
			 * this MUST be overridden if you intend to add extension attributes
			 * to your business object (bpmn2 element) - see the code example
			 * below. You will also want to provide your own images for the tool
			 * palette by overriding getCreateImageId() and
			 * getCreateLargeImageId() in your Create Feature.
			 */
			@Override
			public ICreateFeature getCreateFeature(IFeatureProvider fp) {
				return new CreateTaskFeature(fp) {
				};
			}

			/**
			 * This method updates the layout in case the ACL property of an
			 * Imixs Task Element changed
			 */
			@Override
			public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {
				MultiUpdateFeature multiUpdate = (MultiUpdateFeature) super.getUpdateFeature(fp);
				multiUpdate.addFeature(new AbstractUpdateBaseElementFeature<Task>(fp) {

					@Override
					public boolean update(IUpdateContext context) {
						DIUtils.updateDIShape(context.getPictogramElement());

						System.out.println("update....");

						updateShapeStyle((ContainerShape) context.getPictogramElement());
						return true;
					}

					@Override
					public IReason updateNeeded(IUpdateContext context) {

						IReason reason = super.updateNeeded(context);
						if (reason.toBoolean())
							return reason;

						PictogramElement pe = context.getPictogramElement();
						BaseElement ta = (BaseElement) getBusinessObjectForPictogramElement(pe);

						String oldLayoutStatus = getLastLayoutStatus(context);
						String newLayoutStatus = getNewLayoutStatus(pe, ta);

						// update is needed if the property has changed....
						if (!oldLayoutStatus.equals(newLayoutStatus)) {

							System.out.println("newLayoutStatus=" + newLayoutStatus + " -> update needed!!");
							// getDiagramBehavior().refreshContent();
							return Reason.createTrueReason("layoutstatus.property changed");

						}
						return Reason.createFalseReason("");
					}

				});

				return multiUpdate;
			}

			/**
			 * Common method used to set the fill color for Imixs CustomTask
			 * figure. This method is called by both the CreateFeature and the
			 * UpdateFeature.
			 * 
			 * The method evaluates the property of the business object to
			 * compute the layout
			 * 
			 * @param containerShape
			 *            - the ContainerShape that corresponds to the Task.
			 */
			private void updateShapeStyle(ContainerShape containerShape) {
				Task ta = BusinessObjectUtil.getFirstElementOfType(containerShape, Task.class);
				if (ta != null) {
					
					// compute current width to place icons into the upper right corner..
					int width = containerShape.getGraphicsAlgorithm().getWidth();
					int xPos=width-20;

					PictogramElement pe = containerShape.getGraphicsAlgorithm().getPictogramElement();
					String newLayoutStatus = getNewLayoutStatus(pe, ta);

					Shape shape = containerShape.getChildren().get(0);
					
					// now we need to clear all existing children of that shape...
					shape.getGraphicsAlgorithm().getGraphicsAlgorithmChildren().clear();
					
					
					// set background color
					ShapeStyle shapeStyle = new ShapeStyle();
					shapeStyle.setDefaultColors(PROCESSENTITY_BACKGROUND);

					
					// add the default image into the upper left corner....
					Image imga = loadCustomTaskIcon("process-bubble.png", shape.getGraphicsAlgorithm());
					Graphiti.getGaService().setLocationAndSize(imga, 2, 2, 24, 24);

					// Add 1. custom Image
					if (newLayoutStatus.contains("keyupdateacl=true")) {
						Image img = loadCustomTaskIcon("user_group.gif", shape.getGraphicsAlgorithm());
						Graphiti.getGaService().setLocation(img,xPos, 2);
						xPos=xPos-20;
					}

					// Add 2. custom Image
					if (newLayoutStatus.contains("txteditorid=")) {
						Image img = loadCustomTaskIcon("form.gif", shape.getGraphicsAlgorithm());
						Graphiti.getGaService().setLocation(img,xPos, 2);
					}

					StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), ta, shapeStyle);

					// finally we update the new property value stored in the
					System.out.println("set new layout status: ");
					FeatureSupport.setPropertyValue(containerShape, "layoutstatus.property", newLayoutStatus);
				}

			}

			/**
			 * This method returns the current status of the layout for the
			 * custom shape. This layout status is used to verify if an updates
			 * is needed. The layout status is saved in the property
			 * 'lyoutstatus.property' of the feature context.
			 * 
			 * @see updateNeeded()
			 * @return
			 */
			private String getLastLayoutStatus(IUpdateContext context) {
				PictogramElement pe = context.getPictogramElement();
				String layoutStatus = FeatureSupport.getPropertyValue(pe, "layoutstatus.property");
				if (layoutStatus == null || layoutStatus.isEmpty()) {
					System.out.println("layoutstatus.property not yet created...");
					layoutStatus = "";
				}

				return layoutStatus;
			}

			/**
			 * This method computes the new layout status of the current
			 * business object and the size of the custom shape element.
			 * 
			 * @return
			 */
			private String getNewLayoutStatus(PictogramElement pe, BaseElement task) {
				String result = "";

				// Now evaluate the property 'keyUpdateACL' of the task
				// object
				Value valueUpdateACL = ImixsBPMNPlugin.getItemValueByName(task, "keyUpdateACL", "xs:boolean", "false");
				result += "keyupdateacl=" + valueUpdateACL.getValue() + ";";

				// evaluate the property 'txteditorid' of the task object
				Value valueEditorId = ImixsBPMNPlugin.getItemValueByName(task, "txteditorid", "xs:string", "");
				if (!valueEditorId.getValue().isEmpty()) {
					result += "txteditorid=" + valueEditorId.getValue() + ";";
				}

				// evaluate element width
				int width = pe.getGraphicsAlgorithm().getWidth();
				result += "width=" + width + ";";

				return result;
			}
		};
	}

	/**
	 * This method loads a custom image and register it automatically on first
	 * load.
	 * 
	 * @param fileName
	 * @param ga
	 * @return
	 */
	private Image loadCustomTaskIcon(String fileName, GraphicsAlgorithm ga) {
		if (registeredTaskIcons == null) {
			registeredTaskIcons = new ArrayList<String>();
		}

		String imageId = customTaskDescriptor.getImageId(fileName, CustomTaskImageProvider.IconSize.LARGE);
		if (!registeredTaskIcons.contains(imageId)) {
			String filename = "/icons/large/" + fileName;
			URL url = getClass().getClassLoader().getResource(filename);
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
			CustomTaskImageProvider.registerImage(imageId, descriptor);
			registeredTaskIcons.add(imageId);
		}
		Image img = Graphiti.getGaService().createImage(ga, imageId);
		img.setProportional(false);
		img.setWidth(16);
		img.setHeight(16);
		img.setStretchH(true);
		img.setStretchV(true);

		return img;
	}
}
