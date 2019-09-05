package org.imixs.bpmn.ui.event;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.CheckBoxEditor;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.ListEditor;
import org.imixs.bpmn.ui.RadioButtonEditor;

/**
 * This PorpertySection provides the attributes for Workflow config.
 * 
 * @author rsoika
 *
 */
public class WorkflowPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new BasicDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new BasicDetailComposite(parent, style);
	}

	public class BasicDetailComposite extends ImixsDetailComposite {
		public BasicDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public BasicDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (!ImixsBPMNPlugin.isImixsCatchEvent(businessObject)) {
				return;
			}

			setTitle("Workflow Configuration");

			// ProcessID
			this.bindAttribute(attributesComposite, be, "activityid");

			// Result
			Value value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txtactivityresult", "CDATA", "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.setMultiLine(true);
			valueEditor.createControl(attributesComposite, "Result");

			// visibility and roles

			Section section = createSection(this, "Visibility", false);
			section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 3, 1));

			Composite sectionVisibility = toolkit.createComposite(section);
			section.setClient(sectionVisibility);
			sectionVisibility.setLayout(new GridLayout(3, false));

			Map<String, String> yesnooptionList = new HashMap<String, String>();

			yesnooptionList.put("0", "No");
			yesnooptionList.put("1", "Yes");
			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "keypublicresult", null, "1");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, value, yesnooptionList);
			aEditor.createControl(sectionVisibility, "Public Event");

			// restricted visibility
			Map<String, String> optionList = ImixsBPMNPlugin.getOptionListFromDefinition((BaseElement) be,
					"txtFieldMapping");

			Item item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "keyRestrictedVisibility", null);
			CheckBoxEditor checkboxEditor = new CheckBoxEditor(this, item, optionList);
			checkboxEditor.createControl(sectionVisibility, "Restrict to");
			
			// Read Access
			item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "$readaccess", null);
			ListEditor pluginEditor = new ListEditor(this, item);
			pluginEditor.setImage(ImixsBPMNPlugin.getDefault().getIcon("name_obj.gif"));
			pluginEditor.createControl(sectionVisibility, "Read Access");

		}

	}

}
