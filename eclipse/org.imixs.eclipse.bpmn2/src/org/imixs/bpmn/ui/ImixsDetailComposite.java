package org.imixs.bpmn.ui;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.Property;

public abstract class ImixsDetailComposite extends AbstractDetailComposite {
//	protected final static EStructuralFeature METADATA_FEATURE = ModelPackage.eINSTANCE
//			.getDocumentRoot_Property();
//	protected final static EStructuralFeature METADATA_VALUE = ModelPackage.eINSTANCE
//			.getProperty_Value();

	public ImixsDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public ImixsDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	// @Override
	// public void createBindings(EObject be) {
	//
	// }

	/**
	 * This method returns a ConfigItem of a TaskElement by its name. If no
	 * ConfigItem with the given Name was yet inserted, the method creates a new
	 * ConfigItem and adds it into the ExtensionAttribute List of the Task
	 * Element.
	 * 
	 * @param be
	 * @return
	 */
	protected Property getPropertyByName(BaseElement be, String itemName,
			String itemType, String defaultValue) {

		if (itemName == null)
			return null;

		// lowercase itemname
		itemName = itemName.toLowerCase();

		Property property = (Property)  ImixsBPMNPlugin.findPropertyByName(be, ImixsBPMNPlugin.IMIXS_PROPERTY_FEATURE,
				itemName);
		if (property == null) {
			// create the new MetaData and insert it into the
			// BaseElement's extension elements container
			property = ModelFactory.eINSTANCE.createProperty();
			property.setValue(defaultValue);
			property.setName(itemName);
			if (itemType == null || "".equals(itemType))
				property.setType("xs:string");
			else
				property.setType(itemType);

			// addExtensionAttributeValue
			ExtensionAttributeValue eav = Bpmn2Factory.eINSTANCE
					.createExtensionAttributeValue();
			InsertionAdapter.add(eav, ImixsBPMNPlugin.IMIXS_PROPERTY_FEATURE, property);
			InsertionAdapter.add(be,
					Bpmn2Package.eINSTANCE.getBaseElement_ExtensionValues(),
					eav);
		}

		return property;
	}

	/**
	 * This method returns the property by Name of the Definitions form the
	 * given EObject (Task or Event)
	 * 
	 * The method id not create the value!
	 * 
	 * 
	 * @return
	 */
//	protected Property getDefinitionsPropertyByName(BaseElement be,
//			String itemName) {
//		Property property = null;
//		EObject container = be.eContainer();
//		if (container == null)
//			return null;
//
//		/*
//		 * Here we extract the parent Definitions element from the selection
//		 * container which can be a process or a collaboration selection.
//		 */
//		Definitions defs = null;
//		if (container instanceof Participant)
//			container = ((Participant) container).getProcessRef();
//		if (container instanceof Process || container instanceof Collaboration) {
//			// includes also Choreography
//			defs = ModelUtil.getDefinitions(be);
//		}
//
//		if (defs != null) {
//			// we found the defs! Now try to get the property by name....
//			property = ImixsBPMNPlugin.findPropertyByName(defs, METADATA_FEATURE, itemName);
//		}
//		return property;
//	}

	/**
	 * Find the first entry in this BaseElement's extension elements container
	 * that matches the given structural feature ConfigItem with the given name.
	 * 
	 * @param be
	 *            a BaseElement
	 * @param feature
	 *            the structural feature to search for
	 * @return the value of the extension element or null if no ConfigItem with
	 *         this name exists
	 */
//	private Property findPropertyByName(BaseElement be,
//			EStructuralFeature feature, String itemName) {
//
//		itemName = itemName.toLowerCase();
//
//		for (ExtensionAttributeValue eav : be.getExtensionValues()) {
//			// check all extensionAttribute values...
//			for (FeatureMap.Entry entry : eav.getValue()) {
//				if (entry.getEStructuralFeature() == feature) {
//
//					if (entry.getValue() instanceof Property) {
//						Property property = (Property) entry.getValue();
//						// compare the configitem name element....
//						if (property.getName().equals(itemName))
//							return property;
//					}
//				}
//			}
//		}
//		return null;
//	}

}
