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
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.ListEditor;
import org.imixs.bpmn.ui.RadioButtonEditor;

/**
 * This PorpertySection provides the attributes for Mail config.
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
			if (!ImixsBPMNPlugin.isImixsEvent(businessObject)){
				return ;
			}

			setTitle("Basic");

			// ProcessID
			this.bindAttribute(attributesComposite, be, "activityid");

			// Result
			Value value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txtactivityresult", "CDATA", "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.setMultiLine(true);
			valueEditor.createControl(attributesComposite, "Result");

			// visibility and roles
			Map<String,String> optionList = new HashMap<String,String>();

			optionList.put("0","No");
			optionList.put("1","Yes");
			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "keypublicresult",
					null, "1");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, value,
					optionList);
			aEditor.createControl(attributesComposite, "Visible");

			// Roles
			Item item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "$readaccess", null);
			ListEditor pluginEditor = new ListEditor(this, item);

			pluginEditor.setImage(ImixsBPMNPlugin.getDefault().getIcon(
					"name_obj.gif"));
			pluginEditor.createControl(attributesComposite, "Roles");

		}

	}

}
