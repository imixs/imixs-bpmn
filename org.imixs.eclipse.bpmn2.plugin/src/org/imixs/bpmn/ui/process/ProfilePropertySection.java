package org.imixs.bpmn.ui.process;

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
import org.imixs.bpmn.ImixsActorMappingAdapter;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.Value;
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
public class ProfilePropertySection extends DefaultPropertySection {

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

			setTitle("Workflow Profile");

			Value value = ImixsBPMNPlugin.getItemValueByName((BaseElement) be,
					"txtWorkflowModelVersion", null, "0.0.1");
			TextObjectEditor valueEditor = new TextObjectEditor(this, value,
					ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Model Version");

			// Timer Mappings editor
			Item item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"txtFieldMapping", null);
			ListEditor listEditor = new ListEditor(this, item);
			listEditor.setSortable(true);
			listEditor.setImage(ImixsBPMNPlugin.getDefault().getIcon(
					"name_obj.gif"));
			listEditor.createControl(attributesComposite, "Actor Properties");
			item.eAdapters().add(new ImixsActorMappingAdapter(be)); 
			

			// FieldMapping Actors editor
			item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"txtTimeFieldMapping", null);
			listEditor = new ListEditor(this, item);
			listEditor.setSortable(true);
			listEditor.setImage(ImixsBPMNPlugin.getDefault().getIcon(
					"time_obj.gif"));
			listEditor.createControl(attributesComposite, "Date Properties");

			// plugin editor
			item = ImixsBPMNPlugin.getItemByName((BaseElement) be,
					"txtPlugins", null);
			listEditor = new ListEditor(this, item);
			listEditor.setSortable(true);
			listEditor.setImage(ImixsBPMNPlugin.getDefault().getIcon(
					"plugin_obj.gif"));
			listEditor.createControl(attributesComposite, "Plugins");

		}

	}

}