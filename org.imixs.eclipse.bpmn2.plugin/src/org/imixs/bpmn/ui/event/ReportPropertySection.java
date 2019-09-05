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
 * This PorpertySection provides the attributes for Report config.
 * 
 * @author rsoika
 *
 */
public class ReportPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new ReportDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new ReportDetailComposite(parent, style);
	}

	public class ReportDetailComposite extends ImixsDetailComposite {
		public ReportDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public ReportDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (!ImixsBPMNPlugin.isImixsCatchEvent(businessObject)){
				return ;
			}

			setTitle("Report Configuration");

			
			Value value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txtReportName",
					null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Report-Name");

			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txtReportFilePath",
					null, "");
			valueEditor = new TextObjectEditor(this, value, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Filename");

			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txtReportParams",
					null, "");
			valueEditor = new TextObjectEditor(this, value, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Parameters");

			
			Map<String,String> optionList = new HashMap<String,String>();
			optionList.put("0","Attach to Workitem");
			optionList.put("2","Save to filesystem");
			 value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txtReportTarget", null, "0");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, value,
					 optionList);
			aEditor.createControl(attributesComposite, "Target");

			
		}

	}

}
