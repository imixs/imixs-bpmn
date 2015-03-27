package org.imixs.bpmn.properties;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.ConfigItem;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.ModelPackage;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class MailPropertySection extends AbstractImixsPropertySection {

	final static EStructuralFeature METADATA_FEATURE = ModelPackage.eINSTANCE
			.getDocumentRoot_ConfigItem();
	final static EStructuralFeature METADATA_VALUE = ModelPackage.eINSTANCE
			.getConfigItem_Value();

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new MailDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new MailDetailComposite(parent, style);
	}

	public class MailDetailComposite extends AbstractDetailComposite {
		public MailDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public MailDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (be == null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			setTitle("Mail Configuration");

			// create a new Property Tab section with a twistie
		//	Composite section = createSectionComposite(this, "Mail Body");

			ConfigItem metaData = getConfigItemByName((BaseElement) be,
					"txtSubject", "Some Subject....");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					METADATA_VALUE);
			valueEditor.createControl(this, "Subject");

			metaData = getConfigItemByName((BaseElement) be,
					"txtBody", "Mail Body....");
			 valueEditor = new TextObjectEditor(this,
					metaData, METADATA_VALUE);
			 valueEditor.setMultiLine(true);

			valueEditor.setStyle(SWT.MULTI | SWT.V_SCROLL);
			valueEditor.createControl(this, "Body");

			
			
		
			
			
		}

		/**
		 * This method returns a ConfigItem of a TaskElement by its name. If no
		 * ConfigItem with the given Name was yet inserted, the method creates a
		 * new ConfigItem and adds it into the ExtensionAttribute List of the
		 * Task Element.
		 * 
		 * @param be
		 * @return
		 */
		ConfigItem getConfigItemByName(BaseElement be, String itemName,
				String defaultValue) {
			ConfigItem metaData = (ConfigItem) findExtensionAttributeValue(be,
					METADATA_FEATURE, itemName);
			if (metaData == null) {
				// create the new MetaData and insert it into the
				// BaseElement's extension elements container
				metaData = ModelFactory.eINSTANCE.createConfigItem();
				metaData.setValue(defaultValue);
				metaData.setName(itemName);

				// addExtensionAttributeValue
				ExtensionAttributeValue eav = Bpmn2Factory.eINSTANCE
						.createExtensionAttributeValue();
				InsertionAdapter.add(eav, METADATA_FEATURE, metaData);
				InsertionAdapter
						.add(be, Bpmn2Package.eINSTANCE
								.getBaseElement_ExtensionValues(), eav);
			}

			return metaData;
		}

		/**
		 * Find the first entry in this BaseElement's extension elements
		 * container that matches the given structural feature ConfigItem with
		 * the given name.
		 * 
		 * @param be
		 *            a BaseElement
		 * @param feature
		 *            the structural feature to search for
		 * @return the value of the extension element or null if no ConfigItem
		 *         with this name exists
		 */
		Object findExtensionAttributeValue(BaseElement be,
				EStructuralFeature feature, String itemName) {
			for (ExtensionAttributeValue eav : be.getExtensionValues()) {
				// check all extensionAttribute values...
				for (FeatureMap.Entry entry : eav.getValue()) {
					if (entry.getEStructuralFeature() == feature) {

						if (entry.getValue() instanceof ConfigItem) {
							ConfigItem confItem = (ConfigItem) entry.getValue();
							// compare the configitem name element....
							if (confItem.getName().equals(itemName))
								return confItem;
						}
					}
				}
			}
			return null;
		}

	}

}
