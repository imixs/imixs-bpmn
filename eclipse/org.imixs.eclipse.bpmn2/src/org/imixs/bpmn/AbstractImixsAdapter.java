package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
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

	List<FlowNode> loopFlowCache = null;
	static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	/**
	 * This method searches a Imixs CachEvents element connected to the given
	 * SequenceFlow element. If the Sequence Flow is not connected to a Imixs
	 * Task element the method returns null.
	 * 
	 * 
	 * @return the Imixs Task element or null if no Task Element was found.
	 * @return
	 */
	Event findImixsSourceEvent(SequenceFlow flow) {
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

		if (ImixsBPMNPlugin.isImixsCatchEvent(sourceRef)) {
			return (Event) sourceRef;
		}

		// no Imixs Event found so we are trying to look for the next incoming
		// flow elements.
		List<SequenceFlow> refList = sourceRef.getIncoming();
		for (SequenceFlow aflow : refList) {
			return (findImixsSourceEvent(aflow));
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
	void findImixsTargetEvents(FlowNode sourceRef, List<Event> resultList) {

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
	 * This method searches all Imixs Event Element connected to the given
	 * FlowNode element and have no incomming flow.
	 * 
	 * @param sourceRef
	 *            list of already collected events
	 * @return a List of Imixs Event element or null if no Event Elements were
	 *         found.
	 * 
	 */
	void findImixsSourceCatchEvents(FlowNode targetRef, List<Event> resultList) {

		if (resultList == null)
			resultList = new ArrayList<Event>();

		if (targetRef == null) {
			return;
		}

		// detect loops...
		if (loopFlowCache.contains(targetRef)) {
			// loop!
			return;
		} else {
		//	loopFlowCache.add(targetRef);
		}

		// check all incomming flows....
		List<SequenceFlow> refList = targetRef.getIncoming();
		for (SequenceFlow aflow : refList) {

			FlowNode sourceRef = aflow.getSourceRef();

			if (sourceRef == null) {
				// stop
				return;
			}

			if (ImixsBPMNPlugin.isImixsTask(sourceRef)) {
				// again a Imixs task - so we can stop ....
				return;
			}
			if (ImixsBPMNPlugin.isImixsCatchEvent(sourceRef)) {
				// add to list
				if (!resultList.contains(sourceRef)) {
					resultList.add((Event) sourceRef);
				}
			}

			// recursive call
			findImixsSourceCatchEvents(sourceRef, resultList);
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
	Task findImixsSourceTask(SequenceFlow flow) {
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
			loopFlowCache = new ArrayList<FlowNode>();
			List<Event> imixsEvents = new ArrayList<>();
			findImixsTargetEvents(task, imixsEvents);
			// next find all Imixs CatchEvents connected with that task 
			findImixsSourceCatchEvents(task, imixsEvents);

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
