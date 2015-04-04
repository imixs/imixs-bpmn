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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.CheckBoxEditor;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.ListEditor;
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

			Property metaData = ImixsBPMNPlugin.getPropertyByName(
					(BaseElement) be, "txtSubject", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.createControl(this, "Subject");

			// Body
			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"txtBody", "CDATA", "");
			valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.setMultiLine(true);

			valueEditor.setStyle(SWT.MULTI | SWT.V_SCROLL);
			valueEditor.createControl(this, "Body");

			// get Name Fields...
			List<String> optionList = new ArrayList<String>();
			Property nameFieldsData = ImixsBPMNPlugin
					.findDefinitionsPropertyByName((BaseElement) be,
							"txtFieldMapping");
			if (nameFieldsData != null) {
				ValueListAdapter adapter = new ValueListAdapter(
						nameFieldsData.getValue());
				optionList = adapter.getValueList();
			}

			// send to
			Section section = createSection(this, "Send To", false);
			section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
					3, 1));
			Composite sectionSendTo = toolkit.createComposite(section);
			section.setClient(sectionSendTo);
			sectionSendTo.setLayout(new GridLayout(4, true));

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"keyMailReceiverFields", null, "");
			CheckBoxEditor aEditor = new CheckBoxEditor(this, metaData,
					optionList);
			aEditor.createControl(sectionSendTo, null);

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"nammailreceiver", null, "");
			ListEditor aListEditor = new ListEditor(this, metaData);
			aListEditor.createControl(sectionSendTo, null);

			// send CC
			section = createSection(this, "Copy To", false);
			section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true,
					3, 1));
			Composite sectionSendCC = toolkit.createComposite(section);
			section.setClient(sectionSendCC);
			sectionSendCC.setLayout(new GridLayout(4, true));

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"keyMailReceiverFieldsCC", null, "");
			aEditor = new CheckBoxEditor(this, metaData, optionList);
			aEditor.createControl(sectionSendCC, null);

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"namMailReceiverCC", null, "");
			aListEditor = new ListEditor(this, metaData);
			aListEditor.createControl(sectionSendCC, null);

		}

	}

}
