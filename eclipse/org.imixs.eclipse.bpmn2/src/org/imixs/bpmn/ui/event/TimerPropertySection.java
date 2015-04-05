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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Property;
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
			if (be == null || !(be instanceof IntermediateCatchEvent)) {
				return;
			}
			setTitle("Timer Configuration");

			List<String> optionList = new ArrayList<String>();
			optionList.add("No|0");
			optionList.add("Yes|1");

			Property metaData = ImixsBPMNPlugin.getPropertyByName(
					(BaseElement) be, "keyScheduledActivity", null, "0");
			RadioButtonEditor aEditor = new RadioButtonEditor(this, metaData,
					optionList);

			aEditor.createControl(attributesComposite, "Enabled");

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"txtScheduledView", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.setStyle(SWT.NONE);

			valueEditor.createControl(attributesComposite, "Selection");

			/** Time GRID **/
			Label labelWidget = getToolkit().createLabel(attributesComposite,
					"Delay");
			labelWidget.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false,
					false, 1, 1));
			// updateLabelDecorator();

			Composite compositeTimeGrid = new Composite(attributesComposite,
					SWT.BORDER);
			GridData data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
			compositeTimeGrid.setLayoutData(data);
			compositeTimeGrid.setLayout(new GridLayout(6, false));

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"numActivityDelay", null, "");
			valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);

			valueEditor.createControl(compositeTimeGrid, null);

			optionList = new ArrayList<String>();
			optionList.add("min|1");
			optionList.add("hours|2");
			optionList.add("days|3");
			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"keyActivityDelayUnit", null, "");
			aEditor = new RadioButtonEditor(this, metaData, optionList);

			GridLayout gridlayout = new GridLayout(3, false);

			aEditor.setLayout(gridlayout);
			aEditor.createControl(compositeTimeGrid, null);

			optionList = new ArrayList<String>();
			optionList.add("creation | 3");
			optionList.add("last process |1");
			optionList.add("last modification|2");
			optionList.add("managed| 4");

			metaData = ImixsBPMNPlugin.getPropertyByName((BaseElement) be,
					"keyScheduledBaseObject", null, "");
			aEditor = new RadioButtonEditor(this, metaData, optionList);
			aEditor.createControl(attributesComposite, "Time");

		}

	}

}
