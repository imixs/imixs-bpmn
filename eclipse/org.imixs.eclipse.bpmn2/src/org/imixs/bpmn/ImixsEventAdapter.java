package org.imixs.bpmn;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

/**
 * The ImixsEventAdapter verifies incoming connections for a Imixs Event object
 * and suggest the next ActivityID for the Base Element
 * 
 * @author rsoika
 *
 */
public class ImixsEventAdapter extends AdapterImpl {
	public void notifyChanged(Notification notification) {
		System.out
				.println("ImixsAdapterEvent Notfication received from the data model. Data model has changed!!!");
	}

}
