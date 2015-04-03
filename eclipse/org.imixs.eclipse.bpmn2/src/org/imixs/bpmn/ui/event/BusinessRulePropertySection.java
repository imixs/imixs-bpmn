package org.imixs.bpmn.ui.event;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.IntermediateCatchEvent;
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
public class BusinessRulePropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new BusinessRuleDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new BusinessRuleDetailComposite(parent, style);
	}

	public class BusinessRuleDetailComposite extends ImixsDetailComposite {
		public BusinessRuleDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public BusinessRuleDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (be == null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			setTitle("Business Rule");

			// create a new Property Tab section with a twistie
			// Composite section = createSectionComposite(this, "Mail Body");

			Property metaData = getPropertyByName((BaseElement) be,
					"txtBusinessRuleEngine", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					METADATA_VALUE);
			valueEditor.createControl(this, "Engine");

			metaData = getPropertyByName((BaseElement) be, "txtBusinessRule",
					"CDATA", "");
			valueEditor = new TextObjectEditor(this, metaData, METADATA_VALUE);
			valueEditor.setMultiLine(true);

			valueEditor.setStyle(SWT.MULTI | SWT.V_SCROLL);
			valueEditor.createControl(this, "Rule");

		}

	}

}