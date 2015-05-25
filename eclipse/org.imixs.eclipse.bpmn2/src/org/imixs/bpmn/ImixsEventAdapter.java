package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.emf.common.notify.Notification;

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
	private static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class
			.getName());

	public void notifyChanged(Notification notification) {

		Event imixsEvent = null;

		if (ImixsBPMNPlugin.isImixsCatchEvent(notification.getNotifier())) {
			imixsEvent = (IntermediateCatchEvent) notification.getNotifier();
		}
		if (ImixsBPMNPlugin.isImixsThrowEvent(notification.getNotifier())) {
			imixsEvent = (IntermediateThrowEvent) notification.getNotifier();
		}

		if (imixsEvent != null) {

			int type = notification.getEventType();
			if (type == Notification.ADD) {
				// add notification - test if this is a SequenceFlow...
				if (notification.getNewValue() instanceof SequenceFlow) {
					SequenceFlow seqFlow = (SequenceFlow) notification
							.getNewValue();

					if (seqFlow != null) {
						logger.fine("check sourceTask...");
						// new incoming sequence flow! Search for the source
						// Task

						// clear the flowNodeCache first!
						loopFlowCache = new ArrayList<FlowNode>();
						Task imixsTask = findImixsSourceTask(seqFlow);
						if (imixsTask != null) {
							// Source task found ! suggest next ActivityID....
							suggestNextActivityId(imixsEvent, imixsTask);
						}
					}

				}

			}
		}

	}
}
