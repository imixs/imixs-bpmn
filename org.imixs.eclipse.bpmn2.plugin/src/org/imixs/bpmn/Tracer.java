package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.Event;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.Task;

/**
 * This method is used to trace the flow of a BPMN element back or forward
 * 
 * @author rsoika
 *
 */
public class Tracer {

	List<FlowNode> loopFlowCache = null;

	public Tracer() {
		super();
		loopFlowCache = new ArrayList<FlowNode>();
	}

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
			if (ImixsBPMNPlugin.isImixsCatchEvent(targetRef)) {
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
	 * This method searches all Imixs Event Elements connected to the given
	 * FlowNode element and have no incoming flow.
	 * 
	 * @param sourceRef
	 *            list of already collected events
	 * @return a List of Imixs Event element or null if no Event Elements were
	 *         found.
	 * 
	 */
	void findImixsStartEvents(FlowNode targetRef, List<Event> resultList) {

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
			loopFlowCache.add(targetRef);
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

					// we only add this event if the CatchEvent
					// has no ImixsTask as a source!
					boolean found = false;
					List<SequenceFlow> eventSourceList = sourceRef
							.getIncoming();
					for (SequenceFlow aSflow : eventSourceList) {
						if (new Tracer().findImixsSourceTask(aSflow) != null) {
							found = true;
						}
					}
					if (!found)
						resultList.add((Event) sourceRef);
				}
			}

			// recursive call
			findImixsStartEvents(sourceRef, resultList);
		}

	}

}
