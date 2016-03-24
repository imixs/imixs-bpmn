package org.imixs.bpmn.ui.event;

import java.util.HashMap;
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
import org.eclipse.swt.widgets.Label;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.RadioButtonEditor;

/**
 * This PorpertySection provides the attributes for scheduled activities
 * 
 * @author rsoika
 *
 */
public class TimerPropertySection extends AbstractPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new TimerDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new TimerDetailComposite(parent, style);
	}

	public class TimerDetailComposite extends ImixsDetailComposite {
		public TimerDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public TimerDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {
			if (!ImixsBPMNPlugin.isImixsCatchEvent(businessObject)){
				return ;
			}

			setTitle("Timer Configuration");

			Map<String,String> optionList = new HashMap<String,String>();
			optionList.put("0","No");
			optionList.put("1","Yes");

			Value value = ImixsBPMNPlugin.getItemValueByName(
					(BaseElement) be, "keyScheduledActivity", null, "0");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, value,
					optionList);

			aEditor.createControl(attributesComposite, "Enabled");

			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txtScheduledView", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.setStyle(SWT.NONE);

			valueEditor.createControl(attributesComposite, "Selection");

			/** Time GRID **/
			Label labelWidget = getToolkit().createLabel(attributesComposite,
					"Delay");
			labelWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false,
					false, 1, 1));
			// updateLabelDecorator();

			Composite compositeTimeGrid = new Composite(attributesComposite,
					SWT.NONE);
			GridData data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
			compositeTimeGrid.setLayoutData(data);
			compositeTimeGrid.setLayout(new GridLayout(6, false));

			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"numActivityDelay", null, "");
			valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);

			valueEditor.createControl(compositeTimeGrid, null);

			optionList = new HashMap<String,String>();
			optionList.put("1","min");
			optionList.put("2","hours");
			optionList.put("3","days");
			optionList.put("4","workdays");
			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"keyActivityDelayUnit", null, "");
			aEditor = new RadioButtonEditor(this, value, optionList);

			GridLayout gridlayout = new GridLayout(4, false);

			aEditor.setLayout(gridlayout);
			aEditor.createControl(compositeTimeGrid, null);

			optionList = new HashMap<String,String>();
			optionList.put("3","creation");
			optionList.put("1","last process");
			optionList.put("2","last modification");
			optionList.put("4","managed");

			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"keyScheduledBaseObject", null, "");
			aEditor = new RadioButtonEditor(this, value, optionList);
			aEditor.createControl(attributesComposite, "From");

			
			
			// date field selecton
			optionList = ImixsBPMNPlugin
					.getOptionListFromDefinition((BaseElement) be,
							"txtTimeFieldMapping");
			value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"keyTimeCompareField", null, "");
			aEditor = new RadioButtonEditor(this, value, optionList);
			aEditor.createControl(attributesComposite, "Attribute");

		}

	}

}
