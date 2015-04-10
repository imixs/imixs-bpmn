package org.imixs.bpmn;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.ModelPackage;
import org.imixs.bpmn.model.Value;
import org.osgi.framework.BundleContext;

/**
 * The ImixsBPMNPlugin is the activator class for the BPMN2 extension.
 * 
 * The class provides convenience methods to create and manage the imixs EObject
 * business object 'Item' to be used to store prperties into the BPMN extension
 * element.
 * 
 * 
 * @author rsoika
 *
 */
public class ImixsBPMNPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.bpmn2.modeler.examples.customtask"; //$NON-NLS-1$
	public final static EStructuralFeature IMIXS_ITEM_FEATURE = ModelPackage.eINSTANCE
			.getDocumentRoot_Item();
	public final static EStructuralFeature IMIXS_ITEMLIST_FEATURE = ModelPackage.eINSTANCE
			.getItem_Valuelist();

	public final static EStructuralFeature IMIXS_ITEMVALUE = ModelPackage.eINSTANCE
			.getValue_Value();

	// The shared instance
	private static ImixsBPMNPlugin plugin;

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ImixsBPMNPlugin getDefault() {
		return plugin;
	}

	/**
	 * The constructor
	 */
	public ImixsBPMNPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * returns an ImageDescriptor to the Image Ressource name
	 * 
	 * @param name
	 * @return
	 */
	public Image getIcon(String name) {

		ImageDescriptor imd = null;

		String iconPath = "icons/";
		URL pluginUrl = getBundle().getEntry("/");
		try {
			imd = ImageDescriptor.createFromURL(new URL(pluginUrl, iconPath
					+ name));
		} catch (MalformedURLException e) {
			imd = ImageDescriptor.getMissingImageDescriptor();
		}

		if (imd != null)
			return imd.createImage();
		else
			return null;

	}

	/**
	 * This method returns the an <imixs:item> entry from a BaseElement.
	 * 
	 * The Item is identified by the given Name. If no Item with the requested
	 * name yet exists, the method creates a new Item and adds it into the
	 * ExtensionAttribute List of the BaseElement.
	 * 
	 * The method uses InsertionAdapter to add new Items.
	 * 
	 * @see https 
	 *      ://wiki.eclipse.org/BPMN2-Modeler/DeveloperTutorials/CustomPropertyTabs
	 *      #The_mysterious_IllegalStateException
	 * @param be
	 * @return
	 */
	public static Item getItemByName(BaseElement be, String itemName,
			String itemType) {

		if (itemName == null)
			return null;

		// lowercase itemname
		itemName = itemName.toLowerCase();

		// first test if a item with the given name exits...
		Item item = (Item) ImixsBPMNPlugin.findItemByName(be,
				ImixsBPMNPlugin.IMIXS_ITEM_FEATURE, itemName);
		if (item == null) {
			// no Item with hat name exists. So we need to
			// create the new Item and insert it into the
			// BaseElement's extension elements container
			item = ModelFactory.eINSTANCE.createItem();
			item.setName(itemName);
			if (itemType == null || "".equals(itemType))
				item.setType("xs:string");
			else
				item.setType(itemType);

			// reuse the <bpmn2:extensionElements> container if this
			// BaseElement already has one
			ExtensionAttributeValue extensionAttribute = null;
			if (be.getExtensionValues().size() > 0) {
				extensionAttribute = be.getExtensionValues().get(0);
				// now add the new Item into the Extension
				InsertionAdapter.add(extensionAttribute,
						ImixsBPMNPlugin.IMIXS_ITEM_FEATURE, item);
			} else {
				// we still have no <bpmn2:extensionElements> container. So we
				// add a new ExtensionAttributeValue to the EObject...
				extensionAttribute = Bpmn2Factory.eINSTANCE
						.createExtensionAttributeValue();
				// insert the extension into the base element
				InsertionAdapter
						.add(be, Bpmn2Package.eINSTANCE
								.getBaseElement_ExtensionValues(),
								extensionAttribute);
				
				// we need to execute to avoid the generation of empty
				// extensionElements
				InsertionAdapter.executeIfNeeded(extensionAttribute);
				
				// insert the item into the extension
				InsertionAdapter.add(extensionAttribute,
						ImixsBPMNPlugin.IMIXS_ITEM_FEATURE, item);

			}
		}
		
		

		return item;
	}

	/**
	 * This method returns the first Value entry of a Imixs BPMN Item EObject
	 * from a BaseElement.
	 * 
	 * The Item is identified by the given Name. If no Item with the requested
	 * name yet exists, the method creates a new Item and adds it into the
	 * ExtensionAttribute List of the BaseElement.
	 * 
	 * The method uses InsertionAdapter to add new Items.
	 * 
	 * 
	 * @see https 
	 *      ://wiki.eclipse.org/BPMN2-Modeler/DeveloperTutorials/CustomPropertyTabs
	 *      #The_mysterious_IllegalStateException
	 * @param be
	 * @return
	 */
	public static Value getItemValueByName(BaseElement be, String itemName,
			String itemType, String defaultValue) {

		if (itemName == null)
			return null;

		// lowercase itemname
		itemName = itemName.toLowerCase();

		// first test if we still hav a Item with the given name...
		Item item = getItemByName(be, itemName, itemType);

		Value value = null;
		// now we test if the item contains a <imixs:value> container. If so we
		// reuse the frist one.
		EList<Value> valuelist = item.getValuelist();
		if (valuelist != null && valuelist.size() > 0) {
			value = item.getValuelist().get(0);
		} else {
			// insert a new value element
			value = ModelFactory.eINSTANCE.createValue();
			if (defaultValue != null)
				value.setValue(defaultValue);
			InsertionAdapter.add(item, ImixsBPMNPlugin.IMIXS_ITEMLIST_FEATURE,
					value);

			
		}

		return value;
	}

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
	public static Item findItemByName(BaseElement be,
			EStructuralFeature feature, String itemName) {

		itemName = itemName.toLowerCase();

		for (ExtensionAttributeValue eav : be.getExtensionValues()) {
			// check all extensionAttribute values...
			for (FeatureMap.Entry entry : eav.getValue()) {
				if (entry.getEStructuralFeature() == feature) {
					if (entry.getValue() instanceof Item) {
						Item item = (Item) entry.getValue();
						// compare the configitem name element....
						if (item.getName().equals(itemName))
							return item;
					}
				}
			}
		}
		return null;
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
	public static Item findDefinitionsItemByName(BaseElement be, String itemName) {
		Item property = null;
		EObject container = be.eContainer();
		if (container == null)
			return null;

		/*
		 * Here we extract the parent Definitions element from the selection
		 * container which can be a process or a collaboration selection.
		 */
		Definitions defs = null;
		if (container instanceof Participant)
			container = ((Participant) container).getProcessRef();
		if (container instanceof Process || container instanceof Collaboration) {
			// includes also Choreography
			defs = ModelUtil.getDefinitions(be);
		}

		if (defs != null) {
			// we found the defs! Now try to get the property by name....
			property = ImixsBPMNPlugin.findItemByName(defs, IMIXS_ITEM_FEATURE,
					itemName);
		}
		return property;
	}

}