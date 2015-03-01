package org.imixs.bpmn.properties;

import java.util.List;

import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.model.ImixsTaskFeatureContainer;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.Parameter;
import org.imixs.bpmn.model.TaskConfig;

/**
 * This PorpertySection provides methods to manage the imixs bpmn model
 * extentions
 * 
 * @author rsoika
 *
 */
public abstract class AbstractImixsDetailComposite extends AbstractDetailComposite {
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
			
	
			if (!(be instanceof Task)) {
				System.out
						.println("WARNING: this proeprty tab in only working with Tasks Please check por plugin.xml!");
	
			}
	
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
	
				// initalize values
				initializeProperties() ;
	//			initializeProperty(taskConfig, "txtMailSubject", "");
	//			initializeProperty(taskConfig, "namMailReceiver", "");
	//			initializeProperty(taskConfig, "keyMailReceiverFields", "");
	//			initializeProperty(taskConfig, "namMailReceiverCC", "");
	//			initializeProperty(taskConfig, "keyMailReceiverFieldsCC", "");
	//			initializeProperty(taskConfig, "rtfMailBody", "");
	
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

	/**
	 * Can be used to initialize properties
	 */
	abstract public void initializeProperties() ;

	@Override
	public void setBusinessObject(EObject be) {
		super.setBusinessObject(be);
	}

	/**
	 * This method verifies if a specific property still exists. If not the
	 * method initializes the value
	 * 
	 * @param taskConfig
	 * @param propertyName
	 */
	protected Parameter initializeProperty(
			String propertyName, String defaultVaue) {

		// test all parameters if we have the propertyName
		EList<Parameter> parameters = taskConfig.getParameters();
		for (Parameter param : parameters) {
			if (param.getName().equals(propertyName)) {
				// param allready exists
				return param;
			}
		}

		// the property was not found so we initialize it...
		Parameter param = ModelFactory.eINSTANCE.createParameter();
		param.setName(propertyName);
		param.setValue(defaultVaue);
		taskConfig.getParameters().add(param);
		return param;
	}

	/**
	 * THis method returns the Parameter object for a specific object. If the
	 * object did not exist the method creates an empty new parameter
	 * 
	 * @param taskConfig
	 * @param propertyName
	 * @return
	 */
	protected Parameter getProperty( String propertyName) {

		// test all parameters if we have the propertyName
		EList<Parameter> parameters = taskConfig.getParameters();
		for (Parameter param : parameters) {
			if (param.getName().equals(propertyName)) {
				// param allready exists
				return param;
			}
		}

		// we have not found this param - so we add a new one....
		return initializeProperty( propertyName, "");
	}

}
