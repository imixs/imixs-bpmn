package org.imixs.bpmn;

import java.util.function.Predicate;
import java.util.logging.Logger;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.model.impl.ValueImpl;

/**
 * The ImixsTaskAdapter verifies if "keyUpdateACL", "txteditorid" or "processid"
 * have changed and updates the layout
 * 
 * 
 * @version 2.0
 * @author rsoika
 *
 */
public class ImixsLayoutTaskAdapter extends ImixsLayoutAdapter {

	private static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	public ImixsLayoutTaskAdapter(ContainerShape containerShape) {
		super(containerShape);
	}

	public void notifyChanged(Notification notification) {

		int type = notification.getEventType();

		if (type == Notification.SET) {

			// test for icons acl and editor....
			if (notification.getNotifier() instanceof Value) {
				ValueImpl value = (ValueImpl) notification.getNotifier();
				String itemName = ((Item) value.eContainer()).getName();
				if ("keyUpdateACL".equalsIgnoreCase(itemName) || "txteditorid".equalsIgnoreCase(itemName)) {
					layoutImixsElement();
				}
			} else {
				// extract feature to get the processid attribute
				EStructuralFeature feature = (notification.getFeature() instanceof EStructuralFeature
						? (EStructuralFeature) notification.getFeature()
						: null);
				if (feature != null && "processid".equals(feature.getName())) {
					layoutImixsElement();
				}
			}

		}
		super.notifyChanged(notification);
	}

	/**
	 * Common method used to set the fill color for Imixs CustomTask figure. This
	 * method is called by both the CreateFeature and the UpdateFeature.
	 * 
	 * @param containerShape
	 *            - the ContainerShape that corresponds to the Task.
	 */
	@Override
	public void layoutImixsElement() {
		if (imixsElement != null) {
			logger.fine("update layout...");
			// corner..
			int width = containerShape.getGraphicsAlgorithm().getWidth();
			int height = containerShape.getGraphicsAlgorithm().getHeight();
			int xPos = width - 20;

			Shape shape = containerShape.getChildren().get(0);
			
			// here we remove all images added by our event adapter class before...
			Predicate<GraphicsAlgorithm> customImages = p -> (p instanceof Image);
			shape.getGraphicsAlgorithm().getGraphicsAlgorithmChildren().removeIf(customImages);

			// now we need to clear all existing children of that shape...
			shape.getGraphicsAlgorithm().getGraphicsAlgorithmChildren().clear();

			// add the default image into the upper left corner....
			Image imga = loadCustomTaskIcon("process-bubble.png", shape.getGraphicsAlgorithm());
			Graphiti.getGaService().setLocationAndSize(imga, 2, 2, 24, 24);

			// Add 1. keyupdateacl custom Image
			Value valueUpdateACL = ImixsBPMNPlugin.getItemValueByName(imixsElement, "keyUpdateACL", "xs:boolean",
					"false");

			if ("true".equals(valueUpdateACL.getValue())) {
				Image img = loadCustomTaskIcon("acl.gif", shape.getGraphicsAlgorithm());
				Graphiti.getGaService().setLocation(img, xPos, 2);
				xPos = xPos - 20;
			}

			// add editor custom Image
			Value valueEditorId = ImixsBPMNPlugin.getItemValueByName((Task) imixsElement, "txteditorid", "xs:string",
					"");
			if (!valueEditorId.getValue().toString().isEmpty()) {
				Image img = loadCustomTaskIcon("form.gif", shape.getGraphicsAlgorithm());
				Graphiti.getGaService().setLocation(img, xPos, 2);
			}
			
			
			// we take the second shape to add the text
			EStructuralFeature feature = ModelDecorator.getAnyAttribute(imixsElement, "processid");
			Integer currentProcessID = (Integer) imixsElement.eGet(feature);

			// compute id
			Shape textShape = containerShape.getChildren().get(1);
			Text text = Graphiti.getGaService().createText(textShape, "ID: " + currentProcessID);
			text.setHorizontalAlignment(Orientation.ALIGNMENT_RIGHT);
			text.setVerticalAlignment(Orientation.ALIGNMENT_BOTTOM);
			text.setFont(Graphiti.getGaService().manageDefaultFont(DIUtils.getDiagram(imixsElement), false, false));
			Graphiti.getGaService().setLocationAndSize(text, width - 105, height - 20, 100, 20);
			// we set the active mode to false to avoid that the text element is clickable
			textShape.setActive(false);

		}
	}

}
