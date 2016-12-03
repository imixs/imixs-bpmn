package org.imixs.report.editors;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.imixs.workflow.ItemCollection;

/**
 * This class represents the Report Data Object. All properties of an report are
 * stored in a Imixs ItemCollection. The ItemCollection is converted into a XML
 * file during the save method.
 * 
 * To handle the item 'attributes' which is holding a List of Strings, the
 * Report object provides a set of methods to modify this embedded item list.
 * 
 * In addition the Report Object holds a transient field to locate an external
 * XSL file resource.
 * 
 * @version 1.0
 * @author rsoika
 *
 */
public class Report {
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	private ItemCollection itemCollection;

	/**
	 * Creates a new Report Model object based on a given ItemCollection
	 * containing the report data.
	 * 
	 * @param reportData
	 */
	public Report(ItemCollection reportData) {
		itemCollection = (ItemCollection) reportData.clone();
		if (itemCollection.getUniqueID().isEmpty()) {

		}
	}

	public String getId() {
		return itemCollection.getUniqueID();
	}

	public ItemCollection getItemCollection() {
		return itemCollection;
	}

	@SuppressWarnings("unchecked")
	public String getStringValue(String key) {
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
		firePropertyChange(key, oldValue, value);
	}

	public void firePropertyChange(String key, Object oldValue, Object value) {
		changes.firePropertyChange(key, oldValue, value);
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

	/**
	 * This method returns the attribute list stored in the property
	 * 'attributes'. The value contains a list of list of String.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<List<String>> getAttributeList() {
		List<List<String>> attributes = itemCollection.getItemValue("attributes");
		if (attributes.isEmpty()) {
			attributes = new ArrayList<List<String>>();
			itemCollection.replaceItemValue("attributes", attributes);
		}
		return attributes;
	}

	/**
	 * adds a new attribute entry to the attributeList
	 * 
	 * @return
	 */
	public List<List<String>> addAttribute() {
		List<List<String>> attributes = getAttributeList();
		List<String> values = new ArrayList<String>();
		values.add("");
		values.add("");
		values.add("");
		values.add("");
		values.add("");
		attributes.add(values);
		setItemValue("attributes", attributes);

		firePropertyChange("attributes.size", attributes.size() - 1, attributes.size());
		return attributes;
	}

	/**
	 * removes an attribute entry to the attributelist
	 * 
	 * @return
	 */
	public List<List<String>> removeAttribute(int index) {
		List<List<String>> attributes = getAttributeList();
		attributes.remove(index);
		setItemValue("attributes", attributes);
		firePropertyChange("attributes.size", attributes.size() + 1, attributes.size());
		return attributes;
	}
	
	/**
	 * Moves the given row in the attributes up  
	 * @param index
	 * @return
	 */
	public List<List<String>> moveAttributeUp(int index) {
		List<List<String>> attributes = getAttributeList();
		
		if (index>0 && attributes.size()>1) {
			List<String> row=attributes.remove(index);
			attributes.add(index-1,row);
			setItemValue("attributes", attributes);
			firePropertyChange("attributes."+index+".pos", index , index- 1);
		}
		return attributes;
		
	}

	
	/**
	 * Moves the given row in the attributes down  
	 * @param index
	 * @return
	 */
	public List<List<String>> moveAttributeDown(int index) {
		List<List<String>> attributes = getAttributeList();
		
		if (index < attributes.size()-1) {
			List<String> row=attributes.remove(index);
			attributes.add(index+1,row);
			setItemValue("attributes", attributes);
			firePropertyChange("attributes."+index+".pos", index , index+ 1);
		}
		return attributes;
		
	}

}