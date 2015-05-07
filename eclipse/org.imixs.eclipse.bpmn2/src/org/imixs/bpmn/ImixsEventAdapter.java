package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.IntermediateThrowEvent;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The ImixsEventAdapter verifies incoming SequenceFlows for a Imixs Event
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
public class ImixsEventAdapter extends AdapterImpl {
	public final static int DEFAULT_ACTIVITY_ID = 10;

	private List<FlowNode> loopFlowCache = null;
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

	/**
	 * This method searches a Imixs Task Element connected to the given
	 * SequenceFlow element. If the Sequence Flow is not connected to a Imixs
	 * Task element the method returns null.
	 * 
	 * 
	 * @return the Imixs Task element or null if no Task Element was found.
	 * @return
	 */
	private Task findImixsSourceTask(SequenceFlow flow) {
		FlowNode sourceRef = flow.getSourceRef();

		if (sourceRef == null) {
			return null;
		}

		// detect loops...
		if (loopFlowCache.contains(sourceRef)) {
			// loop!
			return null;
		} else {
			loopFlowCache.add(sourceRef);
		}

		if (ImixsBPMNPlugin.isImixsTask(sourceRef)) {
			return (Task) sourceRef;
		}

		// no Imixs task found so we are trying ot look for the next incoming
		// flow elements.
		List<SequenceFlow> refList = sourceRef.getIncoming();
		for (SequenceFlow aflow : refList) {
			return (findImixsSourceTask(aflow));
		}
		return null;
	}

	/**
	 * This method searches all Imixs Event Element connected to the given
	 * FlowNode element.
	 * 
	 * @param sourceRef
	 *            list of already collected events
	 * @return a List of Imixs Event element or null if no Event Elements were
	 *         found.
	 * 
	 */
	private void findImixsTargetEvents(FlowNode sourceRef,
			List<Event> resultList) {

		if (resultList == null)
			resultList = new ArrayList<Event>();

		if (sourceRef == null) {
			return;
		}

		// detect loops...
		if (loopFlowCache.contains(sourceRef)) {
			// loop!
			return;
		} else {
			loopFlowCache.add(sourceRef);
		}

		// check all outgoing flows....
		List<SequenceFlow> refList = sourceRef.getOutgoing();
		for (SequenceFlow aflow : refList) {

			FlowNode targetRef = aflow.getTargetRef();

			if (targetRef == null) {
				// stop
				return;
			}

			if (ImixsBPMNPlugin.isImixsTask(targetRef)) {
				// again a Imixs task - so we can stop ....
				return;
			}
			if (ImixsBPMNPlugin.isImixsEvent(targetRef)) {
				// add to list
				if (!resultList.contains(targetRef)) {
					resultList.add((Event) targetRef);
				}
			}

			// recursive call
			findImixsTargetEvents(targetRef, resultList);
		}

	}

	/**
	 * This method suggest the next free activiytID for the given current Event
	 * Element. The method is called when a incoming SeuqenceFlow is added to a
	 * Imxis Eent Element.
	 * 
	 * The method tests all if the businessObject is contained in a pool. The
	 * processID must be unique for all imixs task elements in the same pool.
	 * Otherwise the definitions ID will be the container identifier.
	 * 
	 * @param be
	 * @return
	 */
	private void suggestNextActivityId(Event currentEvent, Task sourceTask) {
		// now test if the id is valid or suggest a new one...
		EStructuralFeature feature = ModelDecorator.getAnyAttribute(
				currentEvent, "activityid");
		if (feature != null) {

			// first find all Imixs Events already connected with
			// that source Task element!
			loopFlowCache = new ArrayList<FlowNode>();
			List<Event> imixsEvents = new ArrayList<>();
			findImixsTargetEvents(sourceTask, imixsEvents);

			logger.fine("found " + imixsEvents.size() + " Imxis Events");

			Integer currentActivityID = (Integer) currentEvent.eGet(feature);
			int bestID = -1;
			boolean duplicateID = false;
			for (Event aEvent : imixsEvents) {

				if (aEvent == currentEvent)
					continue;

				int aID = (Integer) aEvent.eGet(feature);
				if (aID > bestID)
					bestID = aID;

				// test for dupplicates!
				if (aID == currentActivityID) {
					duplicateID = true;
				}

			}

			// if duplicate or currentID<=0 suggest a new one!
			if (duplicateID || currentActivityID <= 0) {
				if (bestID <= 0)
					currentActivityID = DEFAULT_ACTIVITY_ID;
				else
					currentActivityID = bestID + 10;
			}
			// suggest a new ProcessID...
			logger.fine("ActiviytID=" + currentActivityID);
			currentEvent.eSet(feature, currentActivityID);

		}
	}
}
