package org.imixs.bpmn.ui.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.CheckBoxEditor;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.ValueListAdapter;

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class MailPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new MailDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new MailDetailComposite(parent, style);
	}

	public class MailDetailComposite extends ImixsDetailComposite {
		public MailDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public MailDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (be == null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			setTitle("Mail Configuration");

			Property metaData = getPropertyByName((BaseElement) be,
					"txtSubject", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.createControl(this, "Subject");

			// Body
			metaData = getPropertyByName((BaseElement) be, "txtBody", "CDATA",
					"");
			valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.setMultiLine(true);

			valueEditor.setStyle(SWT.MULTI | SWT.V_SCROLL);
			valueEditor.createControl(this, "Body");

			// create a new Property Tab section with a twistie
			Composite section = createSectionComposite(this, "Receipients");

			// send to
			List<String> optionList = new ArrayList<String>();

			Property pluginData = ImixsBPMNPlugin
					.findDefinitionsPropertyByName((BaseElement) be,
							"keyPluginListe");
			if (pluginData != null) {
				ValueListAdapter adapter = new ValueListAdapter(
						pluginData.getValue());
				optionList = adapter.getValueList();
			}

			metaData = getPropertyByName((BaseElement) be, "nammailreceiver",
					null, "");
			CheckBoxEditor aEditor = new CheckBoxEditor(this, metaData,
					optionList);
			aEditor.createControl(section, "Send To");

		}

	}

}
