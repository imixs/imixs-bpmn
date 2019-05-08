package org.imixs.bpmn;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.RootElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.graphiti.mm.PropertyContainer;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.ModelPackage;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.CheckBoxEditor;
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
	public static final String PLUGIN_ID = "org.imixs.workflow.bpmn.runtime";
	public final static EStructuralFeature IMIXS_ITEM_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_Item();
	public final static EStructuralFeature IMIXS_ITEMLIST_FEATURE = ModelPackage.eINSTANCE.getItem_Valuelist();

	public final static EStructuralFeature IMIXS_ITEMVALUE = ModelPackage.eINSTANCE.getValue_Value();

	private static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	public final static Map<String, Integer> processIdCache = new HashMap<String, Integer>();
	public final static int DEFAULT_PROCESS_ID = 1000;

	// The shared instance
	private static ImixsBPMNPlugin plugin;

	private List<CheckBoxEditor> actorCheckboxEditorRegistry;

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

	/**
	 * This method registers a checkBoxEditor with its item object holding values of
	 * actor mappings. The ImixsActorMappingAdapter observes the values of these
	 * item objects..
	 * 
	 * @param checkBoxEditor
	 */
	public void registerActorCheckboxEditor(CheckBoxEditor checkBoxEditor) {
		if (actorCheckboxEditorRegistry == null) {
			actorCheckboxEditorRegistry = new ArrayList<CheckBoxEditor>();
		}
		actorCheckboxEditorRegistry.add(checkBoxEditor);
	}

	public List<CheckBoxEditor> getActorCheckBoxEditorList() {
		if (actorCheckboxEditorRegistry == null) {
			actorCheckboxEditorRegistry = new ArrayList<CheckBoxEditor>();
		}
		return actorCheckboxEditorRegistry;
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
			imd = ImageDescriptor.createFromURL(new URL(pluginUrl, iconPath + name));
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
	 * The Item is identified by the given Name. If no Item with the requested name
	 * yet exists, the method creates a new Item and adds it into the
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
	public static Item getItemByName(BaseElement be, String itemName, String itemType) {

		if (itemName == null)
			return null;

		// lowercase itemname
		itemName = itemName.toLowerCase();

		// first test if a item with the given name exits...
		Item item = (Item) ImixsBPMNPlugin.findItemByName(be, ImixsBPMNPlugin.IMIXS_ITEM_FEATURE, itemName);
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
				InsertionAdapter.add(extensionAttribute, ImixsBPMNPlugin.IMIXS_ITEM_FEATURE, item);
			} else {
				// we still have no <bpmn2:extensionElements> container. So we
				// add a new ExtensionAttributeValue to the EObject...
				extensionAttribute = Bpmn2Factory.eINSTANCE.createExtensionAttributeValue();
				// insert the extension into the base element
				InsertionAdapter.add(be, Bpmn2Package.eINSTANCE.getBaseElement_ExtensionValues(), extensionAttribute);

				// we need to execute to avoid the generation of empty
				// extensionElements
				InsertionAdapter.executeIfNeeded(extensionAttribute);

				// insert the item into the extension
				InsertionAdapter.add(extensionAttribute, ImixsBPMNPlugin.IMIXS_ITEM_FEATURE, item);

			}
		}

		return item;
	}

	/**
	 * This method returns the first Value entry of a Imixs BPMN Item EObject from a
	 * BaseElement.
	 * 
	 * The Item is identified by the given Name. If no Item with the requested name
	 * yet exists, the method creates a new Item and adds it into the
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
	public static Value getItemValueByName(BaseElement businessObject, String itemName, String itemType,
			String defaultValue) {

		if (itemName == null)
			return null;

		// lowercase itemname
		itemName = itemName.toLowerCase();

		// first test if we still hav a Item with the given name...
		Item item = getItemByName(businessObject, itemName, itemType);

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
			InsertionAdapter.add(item, ImixsBPMNPlugin.IMIXS_ITEMLIST_FEATURE, value);

		}

		return value;
	}

	/**
	 * Find the first entry in this BaseElement's extension elements container that
	 * matches the given structural feature ConfigItem with the given name.
	 * 
	 * @param be
	 *            a BaseElement
	 * @param feature
	 *            the structural feature to search for
	 * @return the value of the extension element or null if no ConfigItem with this
	 *         name exists
	 */
	public static Item findItemByName(BaseElement businessObject, EStructuralFeature feature, String itemName) {

		itemName = itemName.toLowerCase();

		for (ExtensionAttributeValue eav : businessObject.getExtensionValues()) {
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
	 * This Method finds the definition object for a busiensElement
	 * 
	 * @param be
	 * @param itemName
	 * @return
	 */
	public static Definitions findDefinitions(BaseElement businessObject) {
		EObject container = businessObject.eContainer();
		if (container == null)
			return null;

		/*
		 * Here we extract the parent Definitions element from the selection container
		 * which can be a process or a collaboration selection.
		 */
		Definitions defs = null;
		if (container instanceof Participant)
			container = ((Participant) container).getProcessRef();
		if (container instanceof Process || container instanceof Collaboration) {
			// includes also Choreography
			defs = ModelUtil.getDefinitions(businessObject);

		}

		return defs;
	}

	/**
	 * This method returns the property by Name of the Definitions form the given
	 * EObject (Task or Event)
	 * 
	 * The method id not create the value!
	 * 
	 * 
	 * @return
	 */
	public static Item findDefinitionsItemByName(BaseElement businessObject, String itemName) {
		Item property = null;

		/*
		 * Here we extract the parent Definitions element from the selection container
		 * which can be a process or a collaboration selection.
		 */
		Definitions defs = findDefinitions(businessObject);
		if (defs == null)
			return null;

		// we found the defs! Now try to get the property by name....
		property = ImixsBPMNPlugin.findItemByName(defs, IMIXS_ITEM_FEATURE, itemName);
		return property;
	}

	/**
	 * This method returns all Item instances with a specific name.
	 * <p>
	 * The method expects a Definitions object as the base to search for the root
	 * elements.
	 * 
	 * @param defs
	 * @param itemName
	 * @return
	 */
	public static List<Item> findAllItemsByName(Definitions defs, String itemName) {

		List<Item> result = new ArrayList<Item>();
		List<RootElement> rootElements = defs.getRootElements();

		// iterate over all Root Elements
		for (RootElement re : rootElements) {

			if (re instanceof org.eclipse.bpmn2.Process) {
				org.eclipse.bpmn2.Process process = (org.eclipse.bpmn2.Process) re;

				// iterate over all elements and test if the element is an item with the given
				// name
				TreeIterator<EObject> eObjectList = process.eAllContents();
				while (eObjectList.hasNext()) {

					EObject eo = eObjectList.next();
					if (eo instanceof Item) {

						Item item = (Item) eo;
						// verify the item name
						if (itemName.equalsIgnoreCase(item.getName())) {
							result.add(item);
						}
					}
				}
			}

		}

		return result;
	}

	/**
	 * returns a HashMap with the options from the process definition item. The
	 * method expects a BaseElement to find the underling Definition object.
	 * 
	 * @param be
	 *            - a BaseElement part of a concrete model
	 * @return
	 */
	public static Map<String, String> getOptionListFromDefinition(BaseElement be, String fieldName) {
		Item iteNameField = ImixsBPMNPlugin.findDefinitionsItemByName(be, fieldName);
		return getOptionListFromDefinitionItem(iteNameField);
	}

	/**
	 * returns a HashMap with the options from a process definition item.
	 */
	public static Map<String, String> getOptionListFromDefinitionItem(Item iteNameField) {
		// get Name Fields... user LInkedHashMap to prevent the order of the entries
		Map<String, String> optionList = new LinkedHashMap<String, String>();
		if (iteNameField != null) {
			// iterate over all item values and extract "key|value" pairs
			// we iterate from last to first to order the map entries
			Iterator<Value> iter = iteNameField.getValuelist().iterator();
			while (iter.hasNext()) {
				Value val = iter.next();
				// split '|'
				String key = val.getValue().trim();
				String label = key;
				int ipos = key.indexOf('|');
				if (ipos > -1) {
					label = key.substring(0, ipos).trim();
					key = key.substring(ipos + 1).trim();
				}
				optionList.put(key, label);
			}
		}
		return optionList;
	}

	/**
	 * This method suggest the next free processID for a Task Element. The method is
	 * called when a ImixsFeatureContainerTask is added.
	 * 
	 * The method test if the businessObject is contained in a pool. The processID
	 * must be unique for all imixs task elements in the same pool. Otherwise the
	 * definitions ID will be the container identifier.
	 * 
	 * @param be
	 * @return
	 */
	public static void suggestNextProcessId(BaseElement businessObject) {
		int containerID = 0;
		Integer result = 10;

		// test for the processid feature
		EStructuralFeature feature = ModelDecorator.getAnyAttribute(businessObject, "processid");
		if (feature != null && isImixsTask(businessObject)) {

			/*
			 * Find the container. We extract either the Participant parent or Definitions
			 * element.
			 */
			EObject container = businessObject.eContainer();
			if (container == null)
				return;
			// test if we have a pool
			if (container instanceof Participant) {
				container = ((Participant) container).getProcessRef();
				containerID = ((Participant) container).hashCode();// .getId();
			} else {
				Definitions defs = ModelUtil.getDefinitions(businessObject);
				containerID = defs.hashCode();

			}

			// get ID
			Integer currentProcessID = (Integer) businessObject.eGet(feature);
			String id = containerID + ":" + businessObject.getId();
			// did we already verified the ProcessID?
			result = processIdCache.get(id);
			if (result == null) {

				// if processID>0 verify if the id is still unique in the
				// current container
				if (currentProcessID > 0) {
					for (Map.Entry<String, Integer> entry : processIdCache.entrySet()) {
						String aontainerID = entry.getKey();
						int aprocessid = entry.getValue();
						if (aontainerID.startsWith(containerID + ":") && currentProcessID == aprocessid) {
							// Not a uni1ue processID!!
							currentProcessID = 0;
							break;
						}
					}
				}

				// if the processID==0 we compute the next best ID
				if (currentProcessID <= 0) {
					// get highest ProcesID
					int bestProcessID = -1;
					for (Map.Entry<String, Integer> entry : processIdCache.entrySet()) {
						String aontainerID = entry.getKey();
						int aprocessid = entry.getValue();
						if (aontainerID.startsWith(containerID + ":") && bestProcessID < aprocessid) {
							bestProcessID = aprocessid;
						}
					}
					// update ID
					if (bestProcessID <= 0)
						currentProcessID = DEFAULT_PROCESS_ID;
					else
						currentProcessID = bestProcessID + 100;

					// suggest a new ProcessID...
					logger.fine(id + "=" + currentProcessID);
					businessObject.eSet(feature, currentProcessID);
				}
				// store the id into the cache
				processIdCache.put(id, currentProcessID);
				return;
			}

		}

	}

	/**
	 * This Method verifies if a given object is an instance of a Imixs
	 * IntermediateCatchEvent
	 * 
	 * @param businessObject
	 * @return true if the object is a IntermediateCatchEvent and assigned to the
	 *         Imixs TargetNamespace
	 */
	public static boolean isImixsCatchEvent(Object businessObject) {
		if (businessObject == null)
			return false;
		if (businessObject instanceof IntermediateCatchEvent) {
			EStructuralFeature feature = ModelDecorator.getAnyAttribute((IntermediateCatchEvent) businessObject,
					"activityid");
			if (feature != null && feature instanceof EAttribute) {
				if (ImixsRuntimeExtension.targetNamespace
						.equals(((EAttributeImpl) feature).getExtendedMetaData().getNamespace())) {
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * This Method verifies if a given object is an instance of a Imixs Task.
	 * 
	 * @param businessObject
	 * @return true if the object is a Task element and assigned to the Imixs
	 *         TargetNamespace
	 */
	public static boolean isImixsTask(Object businessObject) {
		if (businessObject instanceof Task) {
			EStructuralFeature feature = ModelDecorator.getAnyAttribute((Task) businessObject, "processid");
			if (feature != null && feature instanceof EAttribute) {
				if (ImixsRuntimeExtension.targetNamespace
						.equals(((EAttributeImpl) feature).getExtendedMetaData().getNamespace())) {
					return true;
				}
			}

		}

		return false;
	}

	/**
	 * This method is part of the next release of the BMPN plugin provided by the
	 * class FeatureSuppor
	 * 
	 * @param propertyContainer
	 * @param key
	 * @param value
	 */
	public static void setPropertyValue(PropertyContainer propertyContainer, String key, String value) {
		while (Graphiti.getPeService().getPropertyValue(propertyContainer, key) != null)
			Graphiti.getPeService().removeProperty(propertyContainer, key);
		if (value != null)
			Graphiti.getPeService().setPropertyValue(propertyContainer, key, value);
	}

	public static String getPropertyValue(PropertyContainer propertyContainer, String key) {
		return Graphiti.getPeService().getPropertyValue(propertyContainer, key);
	}
}