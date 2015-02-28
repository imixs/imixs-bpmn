package org.imixs.bpmn.model;

import java.util.List;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.di.BPMNDiagram;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractListComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.DefaultPropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.Messages;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Check AdvancedDetailComposite AdvancedDetailComposite
 * 
 * @author rsoika
 *
 */
public class ProcessPropertySection extends AbstractBpmn2PropertySection {

	public ProcessPropertySection() {
		super();
	}

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		// This constructor is used to create the detail composite for use in
		// the Property Viewer.
		return new MyTaskDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		// This constructor is used to create the detail composite for use in
		// the popup Property Dialog.
		return new MyTaskDetailComposite(parent, style);
	}

	/**
	 * Here we extract the bpmn task element from the current ISelection
	 */
	@Override
	public EObject getBusinessObjectForSelection(ISelection selection) {

		EObject bo = BusinessObjectUtil
				.getBusinessObjectForSelection(selection);
		if (bo instanceof BPMNDiagram) {
			if (((BPMNDiagram) bo).getPlane() != null
					&& ((BPMNDiagram) bo).getPlane().getBpmnElement() != null)
				return ((BPMNDiagram) bo).getPlane().getBpmnElement();
		}
		return bo;
	}

	public class MyTaskDetailComposite extends AbstractDetailComposite {

		public MyTaskDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public MyTaskDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		public void initialize() {
			super.initialize();

	
			Section mailAttributesSection = createSection(parent,
					"Mail Konfiguration", true);

			mailAttributesSection.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
					true, true, 3, 1));
			Composite mailAttributesComposite = toolkit
					.createComposite(mailAttributesSection);
			mailAttributesSection.setClient(mailAttributesComposite);
			mailAttributesComposite.setLayout(new GridLayout(3, false));

			this.createText(mailAttributesComposite, "Subject", "Testmail....");
//			Text text = new Text(mailAttributesComposite, style);
//			text.setText("Huhu");

			this.createLabel(mailAttributesComposite, "noch ne label");

		
		}

		@Override
		public void setBusinessObject(EObject be) {
			super.setBusinessObject(be);
		}

		@Override
		public void createBindings(EObject be) {
			TaskConfig taskConfig = null;
			// This must be a Task because this Property Tab is only active for
			// Tasks.
			// The Property Tab will only display the Parameter list in our
			// TaskConfig
			// model element (see the definition of this element in
			// MyModel.ecore).

			Task myTask = (Task) be;

			// Fetch all TaskConfig extension objects from the Task
			List<TaskConfig> allTaskConfigs = ModelDecorator
					.getAllExtensionAttributeValues(myTask, TaskConfig.class);
			if (allTaskConfigs.size() == 0) {
				// There are none, so we need to construct a new TaskConfig
				// which is required by the Property Sheet UI.
				taskConfig = ModelFactory.eINSTANCE.createTaskConfig();

				Parameter parm = ModelFactory.eINSTANCE.createParameter();
				parm.setName("hello");
				parm.setValue("World");
				taskConfig.getParameters().add(parm);

				TargetRuntime rt = getTargetRuntime();
				// We need our CustomTaskDescriptor for this Task. The ID must
				// match
				// the one defined in the <customTask> extension point in
				// plugin.xml
				CustomTaskDescriptor ctd = rt
						.getCustomTask(ImixsTaskFeatureContainer.PROCESSENTITY_TASK_ID);
				// Get the model feature for the "taskConfig" element name.
				// Again, this must match the <property> element in <customTask>
				EStructuralFeature feature = ctd.getModelDecorator()
						.getEStructuralFeature(be, "taskConfig");

				// Add the newly constructed TaskConfig object to the Task's
				// Extension Values list.
				// Note that we will delay the actual insertion of the new
				// object until some feature
				// of the object changes (e.g. the Parameter.name)
				ModelDecorator.addExtensionAttributeValue(myTask, feature,
						taskConfig, true);

			} else {
				// Else reuse the existing TaskConfig object.
				taskConfig = allTaskConfigs.get(0);
			}

			// Display the Parameters list in TaskConfig
			AbstractListComposite ding = bindList(taskConfig,
					ModelPackage.eINSTANCE.getTaskConfig_Parameters());
			// ding.setVisible(false);

			// Composite x = bindProperty(be, "type");

		}

	}
}
