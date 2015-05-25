package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The ImixsEventAdapter verifies incoming SequenceFlows for a ImixsTask object
 * and suggest the next ActivityID if the source is a ImixsEvent.
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
abstract public class AbstractImixsAdapter extends AdapterImpl {
	public final static int DEFAULT_ACTIVITY_ID = 10;

	static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

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
		EStructuralFeature feature = ModelDecorator.getAnyAttribute(
				currentEvent, "activityid");
		if (feature != null) {

			// first find all Imixs Events already connected with
			// that source Task element!
			List<Event> imixsEvents = new ArrayList<>();
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
