package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Definitions;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.CheckBoxEditor;
import org.imixs.bpmn.ui.event.ACLPropertySection;

/**
 * The ImixsActorMappingAdapter updates item values based on the actor mappings
 * defined in the process definition BusinessObject. If the actor mapping list
 * changes, the adapter verifies if registered ItemValues must be updated. In
 * concrete the adapter looks if an itemValue contains entries which were
 * actually removed from the field list. In this way deprecated values are
 * removed automatically.
 * <p>
 * The ImixsActorMappingAdapter is used for the Itemvalues
 * <ul>
 * <li>keyownershipfields</li>
 * <li>keyaddwritefields</li>
 * <li>keyaddreadfields</li>
 * <li>keytimecomparefield</li>
 * <ul>
 * A property Section class can use the method
 * ImixsBPMNPlugin.registerItemActorMapping to register a single item object.
 * 
 * @see ACLPropertySection
 * @version 1.0
 * @author rsoika
 *
 */
public class ImixsActorMappingAdapter extends EContentAdapter {
	public final static int DEFAULT_ACTIVITY_ID = 10;

	static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());

	private String oldValue = null;
	BaseElement imixsElement = null;

	public ImixsActorMappingAdapter(EObject be) {
		// TODO Auto-generated constructor stub
		imixsElement = (BaseElement) be;
	}


	public void notifyChanged(Notification notification) {
		int type = notification.getEventType();

		if (type == Notification.REMOVE) {
			Object oldValueObject = notification.getOldValue();
			if (oldValueObject != null && oldValueObject instanceof Value) {
				logger.fine("...deprecated value: " + oldValueObject);
				// test format: LABEL | ITEMNAME
				String singleValue = ((Value) oldValueObject).getValue();
				String[] singleValueList = singleValue.split("\\|");
				if (singleValueList.length == 2) {
					singleValue = singleValueList[1];
				}
				singleValue = singleValue.trim();
				updateDeprecatedValue(singleValue, null);

				updateEditorOptionList();
			}

		}

		// When a value is changed, we receive 2 notification. The first one with the
		// old value the second one with the new value. We hold the value of the first
		// call to update the value when the second call arrives.
		if (type == Notification.SET) {
			Object oldValueObject = notification.getOldValue();
			if (oldValueObject instanceof String && oldValueObject != null && !((String) oldValueObject).isEmpty()) {
				oldValue = (String) oldValueObject;
				return;
			}
			// do we have a new value so we can update the registered items....?
			Object newValueObject = notification.getNewValue();
			if (oldValue != null && !oldValue.isEmpty() && newValueObject != null && newValueObject instanceof String) {

				// test format: LABEL | ITEMNAME
				String singleOldValue = oldValue;
				String[] singleValueList = singleOldValue.split("\\|");
				if (singleValueList.length == 2) {
					singleOldValue = singleValueList[1];
				}
				singleOldValue = singleOldValue.trim();
				String singleNewValue = (String) newValueObject;
				singleValueList = singleNewValue.split("\\|");
				if (singleValueList.length == 2) {
					singleNewValue = singleValueList[1];
				}
				singleNewValue = singleNewValue.trim();

				logger.info("...deprecated value: " + singleOldValue + " -> " + singleNewValue);
				updateDeprecatedValue(singleOldValue, singleNewValue);

				// reset old value
				oldValue = null;

				 updateEditorOptionList();
				return;
			}
		}

	}

	/**
	 * This method iterates over all registers editors and updates the option List
	 * for the FieldMapping
	 */
	private void updateEditorOptionList() {

		// find new option list...
		Item fieldMappingItem = ImixsBPMNPlugin.findItemByName(imixsElement, ImixsBPMNPlugin.IMIXS_ITEM_FEATURE,
				"txtFieldMapping");

		Map<String, String> optionList = ImixsBPMNPlugin.getOptionListFromDefinitionItem(fieldMappingItem);
		if (optionList == null) {
			// undefined!
			return;
		}

		// now update all registered editors
		List<CheckBoxEditor> editors = ImixsBPMNPlugin.getDefault().getActorCheckBoxEditorList();
		for (CheckBoxEditor editor : editors) {
			editor.setOptionMap(optionList);
		}
	}

	/**
	 * This method iterates over Items with the ItemNames:
	 * <ul>
	 * <li>keyOwnershipFields</li>
	 * <li>keyAddReadFields</li>
	 * <li>keyAddWriteFields</li>
	 * </ul>
	 * <p>
	 * and updates a given deprecated value.
	 * <p>
	 * If the param newValue is null, than the deprecatedValue will be removed.
	 * <p>
	 * If the param newValue is not null, than the deprecatedValue will be updated.
	 */
	private void updateDeprecatedValue(String deprecatedValue, String newValue) {

		List<Item> allItems=new ArrayList<Item>();
		// collect all items of interest
		allItems.addAll(ImixsBPMNPlugin.findAllItemsByName((Definitions) imixsElement, "keyOwnershipFields"));
		allItems.addAll(ImixsBPMNPlugin.findAllItemsByName((Definitions) imixsElement, "keyAddReadFields"));
		allItems.addAll(ImixsBPMNPlugin.findAllItemsByName((Definitions) imixsElement, "keyAddWriteFields"));
		
		// test messaging settings.
		allItems.addAll(ImixsBPMNPlugin.findAllItemsByName((Definitions) imixsElement, "keymailreceiverfields"));
		allItems.addAll(ImixsBPMNPlugin.findAllItemsByName((Definitions) imixsElement, "keymailreceiverfieldscc"));
		allItems.addAll(ImixsBPMNPlugin.findAllItemsByName((Definitions) imixsElement, "keymailreceiverfieldsbcc"));
				
		// time compare filed
		allItems.addAll(ImixsBPMNPlugin.findAllItemsByName((Definitions) imixsElement, "keytimecomparefield"));
		
		
		for (Item item: allItems) {
			
			// test if the value is part of this item...
			EList<Value> valueList = item.getValuelist();
			for (Value value : valueList) {
				if (value.getValue().equals(deprecatedValue)) {
					if (newValue == null) {
						logger.info("...remove deprecated value '" + deprecatedValue + "' from item " + item.getName()
								+ "...");
						valueList.remove(value);
						break;
					} else {
						logger.fine("...update deprecated value '" + deprecatedValue + "' from item " + item.getName()
								+ " -> " + newValue + "...");
						// we need to update the deprecated value with the new value!
						int i = valueList.indexOf(value);
						value.setValue(newValue);
						valueList.set(i, value);
						break;
					}
				}
			}

		}
	}
}
