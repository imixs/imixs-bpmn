package org.imixs.bpmn.properties;

import java.util.List;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.ImixsRuntimeExtension;
import org.imixs.bpmn.model.ImixsTaskFeatureContainer;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.TaskConfig;

/**
 * This PorpertySection provides methods to manage the imixs bpmn model
 * extentions
 * 
 * @author rsoika
 *
 */
public abstract class AbstractImixsDetailComposite extends
		AbstractDetailComposite {

	TaskConfig taskConfig = null;

	public AbstractImixsDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public AbstractImixsDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	public void initialize() {
		super.initialize();
	}

	@Override
	public void createBindings(EObject be) {
		// TaskConfig taskConfig = null;

		TargetRuntime rt = getTargetRuntime();
		// Get the CustomTaskDescriptor for this Task.
		CustomTaskDescriptor ctd = rt
				.getCustomTask(ImixsTaskFeatureContainer.PROCESSENTITY_TASK_ID);

		Task myTask = (Task) be;

		// Fetch all TaskConfig extension objects from the Task
		List<TaskConfig> allTaskConfigs = ModelDecorator
				.getAllExtensionAttributeValues(myTask, TaskConfig.class);
		if (allTaskConfigs.size() == 0) {
			// There are none, so we need to construct a new TaskConfig
			// which is required by the Property Sheet UI.
			taskConfig = ModelFactory.eINSTANCE.createTaskConfig();

			// Initialize all values now. This can not be split up to several
			// calls because of graffiti transaction
			ImixsRuntimeExtension.initializeTaskConfig(taskConfig);

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

	}

	@Override
	public void setBusinessObject(EObject be) {
		super.setBusinessObject(be);
	}

}
