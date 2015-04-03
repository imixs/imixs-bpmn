package org.imixs.bpmn.ui.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.RadioButtonEditor;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class VersionPropertySection extends DefaultPropertySection {

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
			if (be == null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			setTitle("Version-Management");

			List<String> optionList = new ArrayList<String>();
			optionList.add("no Version (default) |0");
			optionList.add("create a new Version|1");
			optionList.add("convert to Master Version |2");
			Property metaData = getPropertyByName((BaseElement) be,
					"keyVersion", null, "");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, metaData,
					METADATA_VALUE, optionList);
			aEditor.createControl(this, "Action");

			
			metaData = getPropertyByName((BaseElement) be, "numVersionActivityID",
					null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					METADATA_VALUE);
			valueEditor.createControl(this, "Activity ID");


		}

	}

}
