package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.emf.common.notify.Notification;

/**
 * The ImixsEventAdapter verifies incoming SequenceFlows for a ImixsTask
 * object and suggest the next ActivityID if the source is a ImixsEvent.
 * 
 * Therefore the adapter searches the source Imixs Task Element in a recurse
 * way. When a Imixs task element was found the adapter searches all existing
 * Imixs event elements and suggest the next possible unique ActvityID.
 * 
 * The Adapter uses a internal cache (loopFlowCache) to determine loops in the
 * model.
 * 
 
 * 
 * @version 2.0
 * @author rsoika
 *
 */
public class ImixsTaskAdapter extends AbstractImixsAdapter {
	private static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class
			.getName());

	public void notifyChanged(Notification notification) {

		Event imixsEvent = null;
		Task imixsTask =null;

		if (ImixsBPMNPlugin.isImixsTask(notification.getNotifier())) {
			imixsTask = (Task) notification.getNotifier();
		}
		

		if (imixsTask != null) {

			int type = notification.getEventType();
			if (type == Notification.ADD) {
				// add notification - test if this is a SequenceFlow...
				if (notification.getNewValue() instanceof SequenceFlow) {
					SequenceFlow seqFlow = (SequenceFlow) notification
							.getNewValue();

					if (seqFlow != null) {
						logger.fine("check sourceEvent...");
						// new incoming sequence flow! Search for the source
						// Event

						// clear the flowNodeCache first!
						loopFlowCache = new ArrayList<FlowNode>();
						imixsEvent = findImixsSourceEvent(seqFlow);
						if (imixsEvent != null) {
							// Source task found ! suggest next ActivityID....
							suggestNextActivityId(imixsEvent, imixsTask);							
						}
						
						
					}

				}

			}
		}

	}
}
