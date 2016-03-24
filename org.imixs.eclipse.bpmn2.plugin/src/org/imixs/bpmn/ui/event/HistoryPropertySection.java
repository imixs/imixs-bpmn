package org.imixs.bpmn.ui.event;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.ImixsDetailComposite;

/**
 * This PorpertySection provides the attributes for History config.
 * 
 * @author rsoika
 *
 */
public class HistoryPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new HistoryDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new HistoryDetailComposite(parent, style);
	}

	public class HistoryDetailComposite extends ImixsDetailComposite {
		public HistoryDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public HistoryDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (!ImixsBPMNPlugin.isImixsCatchEvent(businessObject)) {
				return;
			}

			setTitle("Process History");

			// Body
			Value value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"rtfresultlog", "CDATA", "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.setMultiLine(true);
			valueEditor.createControl(attributesComposite, "History");
		}

	}

}
