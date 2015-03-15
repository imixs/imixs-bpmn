package org.imixs.bpmn.properties;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
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

	final static EStructuralFeature METADATA_FEATURE = ModelPackage.eINSTANCE.getDocumentRoot_ConfigItem();
	final static EStructuralFeature METADATA_VALUE = ModelPackage.eINSTANCE.getConfigItem_Value();

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
			if (be==null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			
			setTitle("Mail Configuration");
			
			 // create a new Property Tab section with a twistie
	        Composite section = createSectionComposite(this, "Mail Body");
	        // get the MetaData object from this BaseElement's extension elements
	        ConfigItem metaData = (ConfigItem) findExtensionAttributeValue((BaseElement)be, METADATA_FEATURE,"txtSubject");
	        if (metaData==null) {
	        	// create the new MetaData and insert it into the
                // BaseElement's extension elements container
                // Note that this has to be done inside a transaction
                TransactionalEditingDomain domain = getDiagramEditor().getEditingDomain();
                domain.getCommandStack().execute(new RecordingCommand(domain) {
                    @Override
                    protected void doExecute() {
                    	ConfigItem metaData = ModelFactory.eINSTANCE.createConfigItem();
                        metaData.setValue("Some Subject....");
                        metaData.setName("txtSubject");
                        addExtensionAttributeValue((BaseElement)be, METADATA_FEATURE, metaData);
                        setBusinessObject(be);
                    }
                });
	        } else {
	        	 TextObjectEditor valueEditor = new TextObjectEditor(this, metaData, METADATA_VALUE);
	            // valueEditor.setMultiLine(true);
	            
	             valueEditor.createControl(this, "Value");
	        }

		}
		
		
		/**
		 * Add a new extension element to the given BaseElement.
		 * 
		 * @param be a BaseElement
		 * @param feature the structural feature
		 */
		void addExtensionAttributeValue(BaseElement be, EStructuralFeature feature, Object value) {
			ExtensionAttributeValue eav = null;
			if (be.getExtensionValues().size()>0) {
				// reuse the <bpmn2:extensionElements> container if this BaseElement already has one
				eav = be.getExtensionValues().get(0);
			}
			else {
				// otherwise create a new one
				eav = Bpmn2Factory.eINSTANCE.createExtensionAttributeValue();
				be.getExtensionValues().add(eav);
			}
			eav.getValue().add(feature, value);
		}
		
		/**
		 * Find the first entry in this BaseElement's extension elements container
		 * that matches the given structural feature ConfigItem and maches the name.
		 * 
		 * @param be a BaseElement
		 * @param feature the structural feature to search for
		 * @return the value of the extension element
		 */
		Object findExtensionAttributeValue(BaseElement be, EStructuralFeature feature,String itemName) {
		    for (ExtensionAttributeValue eav : be.getExtensionValues()) {
		        for (FeatureMap.Entry entry : eav.getValue()) {
		            if (entry.getEStructuralFeature() == feature) {
		            	
		            	if (entry.getValue() instanceof ConfigItem) {
		            		ConfigItem confItem=(ConfigItem) entry.getValue();
		            		if (confItem.getName().equals(itemName))
		            			return confItem;
		            			//return entry.getValue();		            
		            	}
		            }
		        }
		    }
		    return null;
		}

	}

}
