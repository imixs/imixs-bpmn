package org.imixs.bpmn.ui.event;

import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.BooleanEditor;
import org.imixs.bpmn.ui.CheckBoxEditor;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.ListEditor;

/**
 * This PorpertySection provides the attributes for ACL config.
 * 
 * @author rsoika
 *
 */
public class ACLPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new ACLDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new ACLDetailComposite(parent, style);
	}

	public class ACLDetailComposite extends ImixsDetailComposite {

		Composite aclComposite = null;

		public ACLDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public ACLDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (!ImixsBPMNPlugin.isImixsCatchEvent(businessObject)) {
				return;
			}

			setTitle("Access Control List");

			// Create checkbox to hide acl elements...
			Value valueUpdateACL = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "keyUpdateACL", "xs:boolean",
					"false");
			BooleanEditor bEditor = new BooleanEditor(this, valueUpdateACL);

			bEditor.createControl(attributesComposite, "Update ACL");
			bEditor.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					boolean checked = ((Button) e.getSource()).getSelection();
					aclComposite.setVisible(checked);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// no op
				}

			});

			// get Name Fields...
			Map<String, String> optionList = ImixsBPMNPlugin.getOptionListFromDefinition((BaseElement) be,
					"txtFieldMapping");

			aclComposite = toolkit.createComposite(attributesComposite);

			aclComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 3, 1));

			aclComposite.setLayout(new GridLayout(6, true));

			// create a separate labels
			Label labelWidget = getToolkit().createLabel(aclComposite, "Owner:");
			labelWidget.setFont(new Font(getDisplay(), "Arial", 11, SWT.BOLD));

			labelWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));

			labelWidget = getToolkit().createLabel(aclComposite, "Read Access:");
			labelWidget.setFont(new Font(getDisplay(), "Arial", 11, SWT.BOLD));
			labelWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));
			labelWidget = getToolkit().createLabel(aclComposite, "Write Access:");
			labelWidget.setFont(new Font(getDisplay(), "Arial", 11, SWT.BOLD));
			labelWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1));

			Item item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "keyOwnershipFields", null);
			CheckBoxEditor aEditor = new CheckBoxEditor(this, item, optionList);
			aEditor.createControl(aclComposite, null);
			ImixsBPMNPlugin.getDefault().registerActorCheckboxEditor(aEditor);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "keyAddReadFields", null);
			aEditor = new CheckBoxEditor(this, item, optionList);
			aEditor.createControl(aclComposite, null);
			ImixsBPMNPlugin.getDefault().registerActorCheckboxEditor(aEditor);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "keyAddWriteFields", null);
			aEditor = new CheckBoxEditor(this, item, optionList);
			aEditor.createControl(aclComposite, null);
			ImixsBPMNPlugin.getDefault().registerActorCheckboxEditor(aEditor);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "namOwnershipNames", null);
			ListEditor aListEditor = new ListEditor(this, item);
			aListEditor.createControl(aclComposite, null);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "namAddReadAccess", null);
			aListEditor = new ListEditor(this, item);
			aListEditor.createControl(aclComposite, null);

			item = ImixsBPMNPlugin.getItemByName((BaseElement) be, "namAddWriteAccess", null);
			aListEditor = new ListEditor(this, item);
			aListEditor.createControl(aclComposite, null);

			// hide section if no update...
			aclComposite.setVisible("true".equals(valueUpdateACL.getValue()));

		}

	}

}
