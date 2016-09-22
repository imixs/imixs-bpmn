package org.imixs.eclipse.workflowreports.model;

import org.imixs.workflow.ItemCollection;

/**
 * This Class manages Report.
 * Reports are used to define a generic Report ItemCollection
 * 
 * @author Ralph Soika
 */
public class ReportObject extends ModelObject  {  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sName;

	public ReportObject(String aName) {
		super();
		
		setName(aName);
	}
	
	public void clearDirtyFlag() {
		bIsDirty=false;

	}
	
	public void setDirtyFlag() {
		bIsDirty=true;
		//firePropertyChange("model_dirty", null, null);
	}

	

	public String getName() {
		return sName;
	}
 

	public void setName(String aName) {
		sName=aName;
		try {
			this.getItemCollection().replaceItemValue("txtName",sName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 

	/**
	 * overwrites to reset the txtname attribute
	 * to change txtname use setName method.
	 */
	public void setItemCollection(ItemCollection itemCol) {
		try {
			itemCol.replaceItemValue("txtName", getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.setItemCollection(itemCol);
		
		
	}


}



