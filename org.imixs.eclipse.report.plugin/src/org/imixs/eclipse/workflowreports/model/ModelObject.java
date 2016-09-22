package org.imixs.eclipse.workflowreports.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.imixs.workflow.ItemCollection;

/**
 * Diese Klasse stellt die Basis für die Modellkalssen dar und biete über 
 * <code>java.beans.PropertyChangeListener</code> ein Observer Pattern 
 * um Änderungen an den Modellen an die beteiligten EditParts 
 * 
 * Gleichzeitig verfügt das ModelObjekt über das PropertyChangeListener Interface
 * und reagiert so selbst auf Propertyänderungen. Diese werden dann per Default einfach
 * an die registireten Listener weitergegeben. Somit wird ein Event in einem hirachrischen
 * Domänen Modell "nach oben durchgereicht"
 * 
 * @author Ralph Soika
 *
 */ 
abstract public class ModelObject implements Serializable, IPropertySource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient PropertyChangeSupport propertyChangeSupport;
    protected boolean bIsDirty=false;
	private  ItemCollection itemCollection=new ItemCollection();
	public transient IPropertyDescriptor[] propertyDescriptors;

    private PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    	
    	try {
    	
        getPropertyChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
        
    	} catch (Exception e) {
    		//System.out.println("[ModelObject] unable to update display");
    	}
    }

    protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
    	try {
    		getPropertyChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
        
    	} catch (Exception e) {
    		//System.out.println("[ModelObject] unable to update display");
    	}
    }

    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    	try {
    		getPropertyChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
        
    	} catch (Exception e) {
    		//System.out.println("[ModelObject] unable to update display");
    	}
    }

    public Object getEditableValue() {
        return "";
    }
    
    /**
     * Diese Methode gibt true zur�ck falls bereits PropertyChanges vorkamen
     * @return
     */
    public boolean isDirty() {
    	return bIsDirty;
    }

	 
    
	/***************************************************************************
	 * Property Handling for ItemCollection
	 **************************************************************************/

	public Object getPropertyValue(Object id) {
		// eintrag aus ItemCollection holen
		try {
			Vector v = itemCollection.getItemValue(id.toString());
			if (v.size() == 0)
				return null;
			if (v.size() == 1)
				//return v.elementAt(0).toString();
				return v.elementAt(0);
			else
				return v;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * This Method update a property inside the itemCollection of the ModelObject
	 * and converts number Attributes (beginning with 'num') into an Double or Integer Object
	 * The Method fires an event to inform outher classes about the change
	 */
	public void setPropertyValue(Object id, Object value) {
		Object oldValue=null;
		try {
			// convert to number object?
			if (id.toString().toLowerCase().startsWith("num")) 
				value=convertToNumber(value);
			oldValue = getPropertyValue(id.toString());
			// if equals - return
			if (oldValue!=null && value!=null && value.equals(oldValue))
				return;
		
			itemCollection.replaceItemValue(id.toString(), value);
			
			bIsDirty=true;
			firePropertyChange(id.toString(), oldValue, value);
		} catch (Exception e) {
			e.printStackTrace();
			// no op
		}
	}
	    
	
	public Object convertToNumber(Object value) {
		if ( value instanceof Integer || value instanceof Double)
			return value;
		// convert to number object?
		try {
			// double or integer?
			if (value.toString().indexOf(".")>-1) {
				Double number=new Double(value.toString());
				value=number;
			} else {
				Integer number=new Integer(value.toString());
				value=number;
			}
		} catch (NumberFormatException e) {
			// convertion not possible
		}
		return value;
			
	}
    
	/**
	 * This Method converts a org.imixs.workflow.ItemCollection into a
	 * IPropertyDescriptor Array every Item will be displayed as a Value inside
	 * the category "Items"
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {

		if (propertyDescriptors == null) {

			// get count of ItemCollection 
			Map map = itemCollection.getAllItems();
			int iColSize = map.size();

			// increase size for Name 
			propertyDescriptors = new IPropertyDescriptor[iColSize];

			Iterator it = itemCollection.getAllItems().entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String sName = (String) entry.getKey();
				String sDisplayName = sName;
				TextPropertyDescriptor descriptor = new TextPropertyDescriptor(
						sName.toLowerCase(), sDisplayName);
				propertyDescriptors[i] = (IPropertyDescriptor) descriptor;
				descriptor.setCategory("Items");
				i++;
			}
		}
		return propertyDescriptors;

	}
	public void resetPropertyValue(Object id) {
	}
	
	public boolean isPropertySet(Object id) {
		return true;
	}
	public ItemCollection getItemCollection() {
		return itemCollection;
	}
     
	public void setItemCollection(ItemCollection itemCol) {
		itemCollection=itemCol;
		firePropertyChange("set_itemcollection", null, itemCollection);
	}

}