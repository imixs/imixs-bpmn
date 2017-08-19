package org.imixs.bpmn;

import java.util.logging.Logger;

import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;
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
public class ImixsEventAdapter extends ImixsLayoutAdapter {

	private static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	private static final IColorConstant ACTIVITYENTITY_BACKGROUND = new ColorConstant(255, 217, 64);
	private static final IColorConstant ACTIVITYENTITY_BACKGROUND_ACL = new ColorConstant(249, 222, 150);

	public ImixsEventAdapter(ContainerShape containerShape) {
		super(containerShape);
	}

	@Override
	public void notifyChanged(Notification notification) {

		int type = notification.getEventType();

		if (type == Notification.SET) {
			// test if we have a change of a Imixs Item keypublicresult
			if (notification.getNotifier() instanceof Value) {
				ValueImpl value = (ValueImpl) notification.getNotifier();
				String itemName = ((Item) value.eContainer()).getName();
				if ("keypublicresult".equalsIgnoreCase(itemName)) {
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
			ShapeStyle shapeStyle = new ShapeStyle();
			String sPublicResult = ImixsBPMNPlugin.getItemValueByName(imixsElement, "keypublicresult", null, "1")
					.getValue();

			if ("1".equals(sPublicResult))
				shapeStyle.setDefaultColors(ACTIVITYENTITY_BACKGROUND);
			else
				shapeStyle.setDefaultColors(ACTIVITYENTITY_BACKGROUND_ACL);
			StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), imixsElement, shapeStyle);

		}
	}

}
