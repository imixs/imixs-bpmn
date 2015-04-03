package org.imixs.bpmn.ui.process;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Participant;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Property;
import org.imixs.bpmn.ui.BooleanEditor;
import org.imixs.bpmn.ui.CheckBoxEditor;
import org.imixs.bpmn.ui.ImixsDetailComposite;
import org.imixs.bpmn.ui.ListEditor;

/**
 * This PorpertySection provides the attributes for the main workflow
 * configuration. The property section is activated for process or Collaboration
 * selections.
 * 
 * @author rsoika
 *
 */
public class TestPropertySection extends DefaultPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new SummaryDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new SummaryDetailComposite(parent, style);
	}

	/**
	 * Here we extract the parent Definitions element from the selection which
	 * can be a process or a collaboration selection. This selection depends on
	 * the diagram type and can not be configured by the type attribute of the
	 * propertyTab extension point.
	 * 
	 * As we only want to set the properties once in a bpmn file with select the
	 * definitions object here.
	 * 
	 */
	@Override
	public EObject getBusinessObjectForSelection(ISelection selection) {

		EObject be = super.getBusinessObjectForSelection(selection);
		if (be instanceof Participant)
			be = ((Participant) be).getProcessRef();
		if (be instanceof Process || be instanceof Collaboration) {
			// includes also Choreography
			return ModelUtil.getDefinitions(be);
		}
		return null;

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

			setTitle("Imixs");

			Property metaData = getPropertyByName((BaseElement) be,
					"txtSubject", null, "Some Subject....");
			TextObjectEditor valueEditor = new TextObjectEditor(this, metaData,
					ImixsBPMNPlugin.IMIXS_PROPERTY_VALUE);
			valueEditor.createControl(this, "Subject");

			// boolean test
			metaData = getPropertyByName((BaseElement) be, "keyListe", null,
					"true");
			BooleanEditor aBooleanEditor = new BooleanEditor(this, metaData);
			aBooleanEditor.createControl(this, "Was1");

			// checkbox multi test
			List<String> optionList = new ArrayList<String>();
			optionList.add("Small|1");
			optionList.add("Medium|2");
			optionList.add("Large|3");
			metaData = getPropertyByName((BaseElement) be, "keyMultiListe",
					null, "");
			CheckBoxEditor aEditor = new CheckBoxEditor(this, metaData,
					 optionList);
			aEditor.createControl(this, "Priority");

			// boolean test 2
			metaData = getPropertyByName((BaseElement) be, "keyListe2", null,
					"true");
			aBooleanEditor = new BooleanEditor(this, metaData);
			aBooleanEditor.createControl(this, "Was2");

			// plugin editor
			metaData = getPropertyByName((BaseElement) be, "keyPluginListe",
					null, "");
			ListEditor pluginEditor = new ListEditor(this, metaData);
			pluginEditor.setSortable(true);
			pluginEditor.setImage(ImixsBPMNPlugin.getDefault().getIcon(
					"plugin_obj.gif"));
			pluginEditor.createControl(this, "Plugins");

		}

	}

}
