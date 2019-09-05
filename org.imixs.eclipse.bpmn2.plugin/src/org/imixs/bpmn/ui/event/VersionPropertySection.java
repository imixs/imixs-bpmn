package org.imixs.bpmn.ui.event;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.RadioButtonEditor;

/**
 * This PorpertySection provides the attributes for Version config.
 * 
 * @author rsoika
 *
 */
public class VersionPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new VersionDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new VersionDetailComposite(parent, style);
	}

	public class VersionDetailComposite extends ImixsDetailComposite {
		public VersionDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public VersionDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (!ImixsBPMNPlugin.isImixsCatchEvent(businessObject)){
				return ;
			}

			setTitle("Version Management");

			Map<String,String> optionList = new HashMap<String,String>();
			optionList.put("0","no Version (default)");
			optionList.put("1","create a new Version");
			optionList.put("2", "convert to Master Version");
			Value value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"keyVersion", null, "");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, value,
					optionList);
			aEditor.createControl(attributesComposite, "Action");

			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"numVersionActivityID", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Event ID");
		}

	}

}
