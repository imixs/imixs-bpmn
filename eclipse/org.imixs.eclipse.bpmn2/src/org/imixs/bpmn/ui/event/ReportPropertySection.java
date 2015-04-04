package org.imixs.bpmn.ui.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.RadioButtonEditor;

/**
 * This PorpertySection provides the attributes for Mail config.
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
			if (be == null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			setTitle("Report Configuration");

			List<String> optionList = new ArrayList<String>();
			optionList.add("attach to Workitem|0");
			optionList.add("attach to LOB Workitem|1");
			optionList.add("save to disk|2");
			Property metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"txtReportTarget", null, "1");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, metaData,
					 optionList);
			aEditor.createControl(attributesComposite, "Target");

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be, "txtReportName",
					null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.createControl(attributesComposite, "Name");

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be, "txtReportFilePath",
					null, "");
			valueEditor = new TextObjectEditor(this, metaData, ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.createControl(attributesComposite, "Filename");

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be, "txtReportParams",
					null, "");
			valueEditor = new TextObjectEditor(this, metaData, ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.createControl(attributesComposite, "Parameter");

		}

	}

}
