package org.imixs.bpmn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.LifecycleEvent;
import org.eclipse.bpmn2.modeler.core.LifecycleEvent.EventType;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.AbstractBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.ui.wizards.FileService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorInput;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
import org.xml.sax.InputSource;

/**
 * This is the Imxis BPMN2 Runtime Extension.
 * <p>
 * The class provides the Runitme ID and the target namespace.
 * <p>
 * In addtion the method notify() is used to validate deprecated FieldMappings.
 * This can happen with old models or models which where created
 * programmatically. In this case we need to ensure that the checkboxes for the
 * ACL settings did match the exact list of defined actor mappings. See also
 * issue #71
 * 
 * @author rsoika
 *
 */
public class ImixsRuntimeExtension extends AbstractBpmn2RuntimeExtension {

	public static final String RUNTIME_ID = "org.imixs.workflow.bpmn.runtime";
	public static final String targetNamespace = "http://www.imixs.org/bpmn2";

	private static Logger logger = Logger.getLogger(ImixsRuntimeExtension.class.getName());

	@Override
	public String getTargetNamespace(Bpmn2DiagramType diagramType) {
		return targetNamespace;
	}

	/**
	 * IMPORTANT: The plugin is responsible for inspecting the file contents! Unless
	 * you are absolutely sure that the file is targeted for this runtime (by, e.g.
	 * looking at the targetNamespace or some other feature) then this method must
	 * return FALSE.
	 */
	@Override
	public boolean isContentForRuntime(IEditorInput input) {
		InputSource source = new InputSource(FileService.getInputContents(input));
		RootElementParser parser = new RootElementParser(targetNamespace);
		parser.parse(source);
		return parser.getResult();
	}

	/**
	 * Add notification adapters here.
	 * 
	 */
	@Override
	public void notify(LifecycleEvent event) {

		// as we can not identify the the Imixs Event here we do add a event
		// adapter
		// see: https://www.eclipse.org/forums/index.php/t/1065614/
		// if (event.eventType == EventType.BUSINESSOBJECT_CREATED) {
		if (event.eventType == EventType.BUSINESSOBJECT_LOADED) {
			EObject object = (EObject) event.target;

			if (object instanceof Task || object instanceof IntermediateCatchEvent) {
				validateDeprecatedFieldMappings(object);
			}
		}
	}

	/**
	 * This method is called by the EventType.BUSINESSOBJECT_LOADED after a Imixs
	 * Task or Imixs Event is loaded. The method verifies if the value of items
	 * based on the definitions FieldMapping are deprecated. If so, than the method
	 * will update the item value automatically.
	 * <p>
	 * The method does not set an isDirty flag (because this is not possible during
	 * BUSINESSOBJECT_LOADED). But we assume that the model will be updated at the
	 * end by the user so this seems not to be a problem on the long run. We hook in
	 * addition a event handler to react directly on changes to the field mapping
	 * list,
	 * 
	 * 
	 * @param object
	 *            - the target EObject of the live cycle event.
	 */
	private void validateDeprecatedFieldMappings(EObject object) {

		// test for deprecated fieldMappings for Task elements...
		if (ImixsBPMNPlugin.isImixsTask(object) || ImixsBPMNPlugin.isImixsCatchEvent(object)) {
			removeDeprecatedFieldMappings(object, "keyownershipfields", "txtFieldMapping");
			removeDeprecatedFieldMappings(object, "keyaddwritefields", "txtFieldMapping");
			removeDeprecatedFieldMappings(object, "keyaddreadfields", "txtFieldMapping");
		}

		// test for deprecated fieldMappings for Event elements
		
		if (ImixsBPMNPlugin.isImixsCatchEvent(object)) {
			// test timer settings
			removeDeprecatedFieldMappings(object, "keytimecomparefield", "txttimefieldmapping");
			// test messaging settings.
			removeDeprecatedFieldMappings(object, "keymailreceiverfields", "txtFieldMapping");
			removeDeprecatedFieldMappings(object, "keymailreceiverfieldscc", "txtFieldMapping");
			removeDeprecatedFieldMappings(object, "keymailreceiverfieldsbcc", "txtFieldMapping");
		}

	}

	/**
	 * Validates a specific item for deprecated FieldMappings. The method can
	 * validate field mappings from different sources. Currently 'txtFieldMapping'
	 * for actors and 'txttimefieldmapping' for Time Fields are known.
	 * 
	 * @param object
	 *            - task or event entity
	 * @param itenName
	 *            - name of the value to validate
	 */
	private void removeDeprecatedFieldMappings(EObject object, String itenName, String mappingSource) {

		// we can not cache the fieldMapping here!
		// We need to load for each BaseElement separately because we do not know in
		// case of multiple opened editors which mapping list is the correct one.
		Map<String, String> fieldMappings = ImixsBPMNPlugin.getOptionListFromDefinition((BaseElement) object,
				mappingSource);

		Item item = ImixsBPMNPlugin.getItemByName((BaseElement) object, itenName, null);
		EList<Value> valueList = item.getValuelist();
		List<Value> deprecatedList = new ArrayList<Value>();
		if (!valueList.isEmpty()) {

			Iterator<Value> iter = valueList.iterator();
			while (iter.hasNext()) {
				Value entry = iter.next();
				logger.fine("...value = " + entry.getValue());

				// test if value still is defined
				if (!fieldMappings.containsKey(entry.getValue())) {
					String objectType="";
					if (ImixsBPMNPlugin.isImixsTask(object)) {
						objectType="ImixsTask";
					}
					
					if (ImixsBPMNPlugin.isImixsCatchEvent(object)) {
						objectType="ImixsEvent";
					}
					logger.info("... " + objectType + ": remove deprecated fieldmapping '" + itenName + "' : " + entry.getValue());
					deprecatedList.add(entry);
				}
			}
			valueList.removeAll(deprecatedList);

		}
	}

}
