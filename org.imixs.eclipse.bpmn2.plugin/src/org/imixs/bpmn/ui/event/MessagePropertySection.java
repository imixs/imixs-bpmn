package org.imixs.bpmn.ui.event;

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

/**
 * This PorpertySection provides the attributes for Mail config.
 * 
 * @author rsoika
 *
 */
public class MessagePropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new MessageDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new MessageDetailComposite(parent, style);
	}

	public class MessageDetailComposite extends ImixsDetailComposite {
		public MessageDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public MessageDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (!ImixsBPMNPlugin.isImixsCatchEvent(businessObject)){
				return ;
			}

			setTitle("Message Content");

			Value value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txtMailSubject", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Subject");

			// Body
			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"rtfMailBody", "CDATA", "");
			valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.setMultiLine(true);

			valueEditor.createControl(attributesComposite, "Body");

			// get Name Fields...
			Map<String, String> optionList = ImixsBPMNPlugin
					.getOptionListFromDefinition((BaseElement) be,
							"txtFieldMapping");

			// send to
			Section section = createSection(this, "To", false);
			section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
					3, 1));
			Composite sectionSendTo = toolkit.createComposite(section);
			section.setClient(sectionSendTo);
			sectionSendTo.setLayout(new GridLayout(4, true));

			Item item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"keyMailReceiverFields", null);
			CheckBoxEditor aEditor = new CheckBoxEditor(this, item, optionList);
			aEditor.createControl(sectionSendTo, null);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"nammailreceiver", null);
			ListEditor aListEditor = new ListEditor(this, item);
			aListEditor.createControl(sectionSendTo, null);

			// send CC
			section = createSection(this, "CC", false);
			section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
					3, 1));
			sectionSendTo = toolkit.createComposite(section);
			section.setClient(sectionSendTo);
			sectionSendTo.setLayout(new GridLayout(4, true));

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"keyMailReceiverFieldsCC", null);
			aEditor = new CheckBoxEditor(this, item, optionList);
			aEditor.createControl(sectionSendTo, null);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"namMailReceiverCC", null);
			aListEditor = new ListEditor(this, item);
			aListEditor.createControl(sectionSendTo, null);

			// send CC
			section = createSection(this, "BCC", false);
			section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
					3, 1));
			sectionSendTo = toolkit.createComposite(section);
			section.setClient(sectionSendTo);
			sectionSendTo.setLayout(new GridLayout(4, true));

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"keyMailReceiverFieldsBCC", null);
			aEditor = new CheckBoxEditor(this, item, optionList);
			aEditor.createControl(sectionSendTo, null);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"namMailReceiverBCC", null);
			aListEditor = new ListEditor(this, item);
			aListEditor.createControl(sectionSendTo, null);

		}

	}

}
