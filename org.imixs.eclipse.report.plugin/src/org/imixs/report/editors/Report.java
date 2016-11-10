package org.imixs.report.editors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the Report Data Object.
 * All properties of an report are stored into a HashMap.
 * 
 * 
 *  
 * @author rsoika
 *
 */
public class Report {
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	private  Map<String,List<String>> itemCollection=new HashMap<String,List<String>>();
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_SUMMARY = "summary";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_DONE = "done";
	public static final String FIELD_DUEDATE = "dueDate";
	public static final String FIELD_DEPENDENTS = "dependents";

	private long id;
	private String summary;
	private String description;
	private boolean done;
	private Date dueDate;
	private List<Long> dependents = new ArrayList<>();

	public Report(long i) {
		id = i;
	}

	public Report(long i, String summary, String description, boolean b, Date date) {
		this.id = i;
		this.summary = summary;
		this.description = description;
		this.done = b;
		this.dueDate = date;
	}

	public long getId() {
		return id;
	}


	public String getItem(String key) {
		//return description;
		List<String> values= itemCollection.get(key);
		if (values==null ||  itemCollection.get(key).size()==0) {
			return "";
		} else {
			return values.get(0);
		}
	}

	public void setItem(String key,String value) {
		String oldValue=getItem(key);
		List valueList=new ArrayList<String>();
		valueList.add(value);
		itemCollection.put(key, valueList);
		
		
		changes.firePropertyChange(FIELD_DESCRIPTION, oldValue,  value);
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
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
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
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", summary=" + summary + "]";
	}

	public Report copy() {
		return new Report(id, summary, description, done, dueDate);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}
}