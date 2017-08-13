package org.imixs.bpmn;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

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
public class ImixsEventAdapter extends AbstractImixsAdapter {
	private static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	private static final IColorConstant ACTIVITYENTITY_BACKGROUND = new ColorConstant(255, 217, 64);
	private static final IColorConstant ACTIVITYENTITY_BACKGROUND_ACL = new ColorConstant(249, 222, 150);

	private Map<String, String> propertyCache;
	Event imixsEvent = null;
	ContainerShape containerShape = null;

	public ImixsEventAdapter(ContainerShape containerShape) {
		super();
		this.containerShape = containerShape;
	}

	@Override
	public void setTarget(Notifier newTarget) {
		if (newTarget instanceof IntermediateCatchEvent) {
			imixsEvent = (IntermediateCatchEvent) newTarget;
		}
		super.setTarget(newTarget);

		setFillColor();
	}

	@Override
	public void notifyChanged(Notification notification) {

		int type = notification.getEventType();
		if (type == Notification.ADD) {
			// add notification - test if this is a SequenceFlow...
			if (notification.getNewValue() instanceof SequenceFlow) {
				SequenceFlow seqFlow = (SequenceFlow) notification.getNewValue();

				if (seqFlow != null) {
					logger.fine("check sourceTask...");
					// new incoming sequence flow! Search for the source
					// Task
					Task imixsTask = new Tracer().findImixsSourceTask(seqFlow);
					if (imixsTask != null) {
						// Source task found ! suggest next ActivityID....
						suggestNextActivityId(imixsEvent, imixsTask);
					}
				}

			}

		}

		if (type == Notification.SET) {
			EStructuralFeature feature = (notification.getFeature() instanceof EStructuralFeature
					? (EStructuralFeature) notification.getFeature()
					: null);

			if ("cDATA".equals(feature.getName())) {
				// TODO we are still not able to determine the name of the changed attrebute
				// here.
				if (propertyChanged("keypublicresult")) {
					setFillColor();
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
	private void setFillColor() {

		if (imixsEvent != null) {
			Shape shape = containerShape.getChildren().get(0);
			ShapeStyle shapeStyle = new ShapeStyle();
			String sPublicResult = ImixsBPMNPlugin.getItemValueByName(imixsEvent, "keypublicresult", null, "1").getValue();

			if ("1".equals(sPublicResult))
				shapeStyle.setDefaultColors(ACTIVITYENTITY_BACKGROUND);
			else
				shapeStyle.setDefaultColors(ACTIVITYENTITY_BACKGROUND_ACL);
			StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), imixsEvent, shapeStyle);

		}
	}

	/**
	 * Verifies if the value has changed. The method uses the internal propertyCache
	 * to store existing values
	 * 
	 * @param propertyName
	 * @return
	 */
	private boolean propertyChanged(String propertyName) {

		if (propertyCache == null) {
			propertyCache = new HashMap<String, String>();
		}

		if (imixsEvent != null) {
			String oldValue = propertyCache.get(propertyName);
			String sNewValue = ImixsBPMNPlugin.getItemValueByName(imixsEvent, propertyName, null, null).getValue();

			if ((sNewValue != null && !sNewValue.isEmpty()) && (oldValue == null || !oldValue.equals(sNewValue))) {
				propertyCache.put(propertyName, sNewValue);
				return true;
			}
		}

		return false;

	}

}
