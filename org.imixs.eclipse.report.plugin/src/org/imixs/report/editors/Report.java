package org.imixs.report.editors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.imixs.workflow.ItemCollection;

/**
 * This class represents the Report Data Object. All properties of an report are
 * stored in a Imixs ItemCollection. The ItemCollection is converted into a XML
 * file during the save method.
 * 
 * In addtion the Report Object holds a transient field to locate an external
 * XSL file resource.
 * 
 * 
 * 
 * @author rsoika
 *
 */
public class Report {
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	//private Map<String, List<String>> itemCxollection = new HashMap<String, List<String>>();

	private ItemCollection itemCollection;
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_SUMMARY = "summary";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_DONE = "done";
	public static final String FIELD_DUEDATE = "dueDate";
	public static final String FIELD_DEPENDENTS = "dependents";

	private long id;
	private boolean done;
	private Date dueDate;
	private List<Long> dependents = new ArrayList<>();

	public Report(long i) {
		itemCollection=new ItemCollection();
		id = i;
	}


	public long getId() {
		return id;
	}

	public ItemCollection getItemCollection() {
		return itemCollection;
	}


	public String getStringValue(String key) {
		// return description;
		List<String> values = itemCollection.getItemValue(key);
		if (values == null || values.size() == 0) {
			return "";
		} else {
			return values.get(0);
		}
	}

	public void setItemValue(String key, Object value) {
		Object oldValue = itemCollection.getItemValue(key);
		itemCollection.replaceItemValue(key, value);
		changes.firePropertyChange(FIELD_DESCRIPTION, oldValue, value);
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean isDone) {
		changes.firePropertyChange(FIELD_DONE, this.done, this.done = isDone);
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		changes.firePropertyChange(FIELD_DUEDATE, this.dueDate, this.dueDate = dueDate);
	}

	public List<Long> getDependentTasks() {
		return dependents;
	}

	public void setDependentTasks(List<Long> dependents) {
		this.dependents = dependents;
	}

	@Override
	public int hashCode() {
		return itemCollection.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Report other = (Report) obj;
		
		return this.itemCollection.equals(other.itemCollection);
	}

	

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}
}