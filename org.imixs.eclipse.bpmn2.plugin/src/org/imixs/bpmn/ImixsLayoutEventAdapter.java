package org.imixs.bpmn;

import java.util.function.Predicate;
import java.util.logging.Logger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.model.impl.ValueImpl;

/**
 * The ImixsEventAdapter verifies outgoing SequenceFlows for a Imixs Event
 * object and suggest the next ActivityID.
 * 
 * Therefore the adapter searches the source Imixs Task Element in a recurse
 * way. When a Imixs task element was found the adapter searches all existing
 * Imixs event elements and suggest the next possible unique ActvityID.
 * 
 * The Adapter uses a internal cache (loopFlowCache) to determine loops in the
 * model.
 * 
 * @version 1.0
 * @author rsoika
 *
 */
public class ImixsLayoutEventAdapter extends ImixsLayoutAdapter {

	private static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	public ImixsLayoutEventAdapter(ContainerShape containerShape) {
		super(containerShape);
	}

	@Override
	public void notifyChanged(Notification notification) {

		int type = notification.getEventType();

		if (type == Notification.SET) {

			// test if we have a change of a Imixs Item keypublicresult
			if (notification.getNotifier() instanceof Value) {
				boolean blayout = false;
				ValueImpl value = (ValueImpl) notification.getNotifier();
				String itemName = ((Item) value.eContainer()).getName();
				// acl change
				if ("keyupdateacl".equalsIgnoreCase(itemName)) {
					blayout = true;
				}
				// business rule
				if ("txtbusinessrule".equalsIgnoreCase(itemName)) {
					blayout = true;
				}

				if (blayout) {
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
			Shape shape = containerShape.getChildren().get(0);

			// here we remove all images added by our event adapter class before...
			Predicate<GraphicsAlgorithm> customImages = p -> (p instanceof Image);
			shape.getGraphicsAlgorithm().getGraphicsAlgorithmChildren().removeIf(customImages);

			// Add 1. keyupdateacl custom Image
			Value valueBusinessRule = ImixsBPMNPlugin.getItemValueByName(imixsElement, "txtbusinessrule", null, "");
			if (!valueBusinessRule.getValue().isEmpty()) {
				int width = containerShape.getGraphicsAlgorithm().getWidth();
				Image img = loadCustomTaskIcon("conditions.png", shape.getGraphicsAlgorithm());
				Graphiti.getGaService().setLocation(img, width - 14, 0);
			}

			// 2. Add acl custom Image
			Value valueUpdateACL = ImixsBPMNPlugin.getItemValueByName(imixsElement, "keyupdateacl", "xs:boolean",
					"false");
			if ("true".equals(valueUpdateACL.getValue())) {
				int width = containerShape.getGraphicsAlgorithm().getWidth();
				Image img2 = loadCustomTaskIcon("acl-event.gif", shape.getGraphicsAlgorithm());
				Graphiti.getGaService().setLocation(img2, width - 12, 20);
			}

		}
	}

}
