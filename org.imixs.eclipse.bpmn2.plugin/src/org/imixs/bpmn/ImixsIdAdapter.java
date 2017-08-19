package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;

/**
 * The ImixsIdAdapter verifies in and outgoing SequenceFlows for a ImixsTask and
 * ImixsEvent objects. The adapter computes the next ActivityID if the source or
 * target is a ImixsEvent.
 * 
 * Therefore the adapter searches the source Imixs Task Element in a recurse
 * way. When a Imixs task element was found the adapter searches all existing
 * Imixs event elements and suggest the next possible unique ActvityID.
 * 
 * The Adapter uses a internal cache (loopFlowCache) to determine loops in the
 * model.
 * 
 * 
 * 
 * @version 2.0
 * @author rsoika
 *
 */
public class ImixsIdAdapter extends EContentAdapter { // AdapterImpl
	public final static int DEFAULT_ACTIVITY_ID = 10;

	static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	BaseElement imixsElement = null;

	/**
	 * This method verifies if the target element is of Type ImixsEvent or
	 * ImixsType.
	 */
	@Override
	public void setTarget(Notifier newTarget) {
		if (ImixsBPMNPlugin.isImixsCatchEvent(newTarget) || ImixsBPMNPlugin.isImixsTask(newTarget)) {
			imixsElement = (BaseElement) newTarget;
		}
		super.setTarget(newTarget);
	}

	public void notifyChanged(Notification notification) {

		int type = notification.getEventType();
		if (imixsElement != null && type == Notification.ADD) {
			// add notification - test if this is a SequenceFlow...
			if (notification.getNewValue() instanceof SequenceFlow) {

				// is Task Element?
				if (ImixsBPMNPlugin.isImixsTask(imixsElement)) {

					SequenceFlow seqFlow = (SequenceFlow) notification.getNewValue();

					if (seqFlow != null && ImixsBPMNPlugin.isImixsCatchEvent(seqFlow.getSourceRef())) {
						logger.fine("check source CatchEvent...");
						// new incoming sequence flow! Search for the source
						// Event
						Event imixsEvent = new Tracer().findImixsSourceEvent(seqFlow);
						if (imixsEvent != null) {
							// Source task found ! suggest next ActivityID....
							suggestNextActivityId(imixsEvent, (Task) imixsElement);
						}
					}
				}

				// is CatchEvent Element
				if (ImixsBPMNPlugin.isImixsCatchEvent(imixsElement)) {

					// add notification - test if this is a SequenceFlow...

					SequenceFlow seqFlow = (SequenceFlow) notification.getNewValue();

					if (seqFlow != null) {
						logger.fine("check sourceTask...");
						// new incoming sequence flow! Search for the source
						// Task
						Task imixsTask = new Tracer().findImixsSourceTask(seqFlow);
						if (imixsTask != null) {
							// Source task found ! suggest next ActivityID....
							suggestNextActivityId((Event) imixsElement, imixsTask);
						}
					}

				}
			}
		}

	}

	/**
	 * This method suggest the next free activiytID for the given current Event
	 * Element. The method is called when a incoming SeuqenceFlow is added to a
	 * ImxisEvent or ImixsTask Element.
	 * 
	 * The ActivtyID must be unique for all event elements connected to the same
	 * ImixsTask.
	 * 
	 * @param be
	 * @return
	 */
	void suggestNextActivityId(Event currentEvent, Task task) {
		// now test if the id is valid or suggest a new one...
		EStructuralFeature feature = ModelDecorator.getAnyAttribute(currentEvent, "activityid");
		if (feature != null) {

			// first find all Imixs Events already connected with
			// that source Task element!
			List<Event> imixsEvents = new ArrayList<Event>();
			new Tracer().findImixsTargetEvents(task, imixsEvents);
			// next find all Imixs CatchEvents connected with that task
			new Tracer().findImixsStartEvents(task, imixsEvents);

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
