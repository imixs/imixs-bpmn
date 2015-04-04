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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.ModelPackage;
import org.imixs.bpmn.model.Property;
import org.osgi.framework.BundleContext;

public class ImixsBPMNPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.bpmn2.modeler.examples.customtask"; //$NON-NLS-1$
	public final static EStructuralFeature IMIXS_PROPERTY_FEATURE = ModelPackage.eINSTANCE
			.getDocumentRoot_Property();
	public final static EStructuralFeature IMIXS_PROPERTY_VALUE = ModelPackage.eINSTANCE
			.getProperty_Value();

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
	public static Property findPropertyByName(BaseElement be,
			EStructuralFeature feature, String itemName) {

		itemName = itemName.toLowerCase();

		for (ExtensionAttributeValue eav : be.getExtensionValues()) {
			// check all extensionAttribute values...
			for (FeatureMap.Entry entry : eav.getValue()) {
				if (entry.getEStructuralFeature() == feature) {
					if (entry.getValue() instanceof Property) {
						Property property = (Property) entry.getValue();
						// compare the configitem name element....
						if (property.getName().equals(itemName))
							return property;
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
	public static Property findDefinitionsPropertyByName(BaseElement be,
			String itemName) {
		Property property = null;
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
			property = ImixsBPMNPlugin.findPropertyByName(defs,
					IMIXS_PROPERTY_FEATURE, itemName);
		}
		return property;
	}

	/**
	 * This method returns a Imixs BPMN ConfigItem of a TaskElement by its name.
	 * If no ConfigItem with the given Name yet exists, the method creates a new
	 * ConfigItem and adds it into the ExtensionAttribute List of the Task
	 * Element.
	 * 
	 * The method uses InsertionAdapter to add new properties.
	 * 
	 * @see https
	 *      ://wiki.eclipse.org/BPMN2-Modeler/DeveloperTutorials/CustomPropertyTabs
	 *      #The_mysterious_IllegalStateException
	 * @param be
	 * @return
	 */
	public static Property getPropertyByName(BaseElement be, String itemName,
			String itemType, String defaultValue) {

		if (itemName == null)
			return null;

		// lowercase itemname
		itemName = itemName.toLowerCase();

		Property property = (Property) ImixsBPMNPlugin.findPropertyByName(be,
				ImixsBPMNPlugin.IMIXS_PROPERTY_FEATURE, itemName);
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

			// test if we already have ExtensionValues. If so reuse the first
			// one.
			ExtensionAttributeValue eav = null;
			if (be.getExtensionValues().size() > 0) {
				// reuse the <bpmn2:extensionElements> container if this
				// BaseElement already has one
				eav = be.getExtensionValues().get(0);
				// now add the new Property into the Extension
				InsertionAdapter.add(eav,
						ImixsBPMNPlugin.IMIXS_PROPERTY_FEATURE, property);
			} else {
				// add a new ExtensionAttributeValue to the EObject...
				eav = Bpmn2Factory.eINSTANCE.createExtensionAttributeValue();
				InsertionAdapter.add(eav,
						ImixsBPMNPlugin.IMIXS_PROPERTY_FEATURE, property);
				InsertionAdapter
						.add(be, Bpmn2Package.eINSTANCE
								.getBaseElement_ExtensionValues(), eav);
			}
		}

		return property;
	}

}
