package org.imixs.bpmn.ui.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.ImixsDetailComposite;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class ProcessWorkflowPropertySection extends AbstractProcessPropertySection {

	public final static String REPLACE_PROPERTY_TAB_ID = "org.eclipse.bpmn2.modeler.activity.io.tab";

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new SummaryDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new SummaryDetailComposite(parent, style);
	}

	
	public class SummaryDetailComposite extends ImixsDetailComposite {

		public SummaryDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public SummaryDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(EObject be) {
			setTitle("Workflow");

			// ProcessID
			this.bindAttribute(this, be, "processid");

			// Summary
			Property metaData = getPropertyByName((BaseElement) be,
					"txtworkflowsummary", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					METADATA_VALUE);
			valueEditor.createControl(this, "Summary");

			// Abstract
			metaData = getPropertyByName((BaseElement) be,
					"txtworkflowabstract", "CDATA", "");
			valueEditor = new TextObjectEditor(this, metaData, METADATA_VALUE);
			valueEditor.setMultiLine(true);
			valueEditor.setStyle(SWT.MULTI | SWT.V_SCROLL);
			valueEditor.createControl(this, "Abstract");

		}

	}

}
