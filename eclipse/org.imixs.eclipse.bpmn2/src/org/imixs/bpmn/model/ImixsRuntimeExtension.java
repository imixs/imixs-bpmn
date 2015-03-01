package org.imixs.bpmn.model;

import org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.core.LifecycleEvent;
import org.eclipse.bpmn2.modeler.core.LifecycleEvent.EventType;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.DefaultBpmn2RuntimeExtension.RootElementParser;
import org.eclipse.bpmn2.modeler.ui.wizards.FileService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.IEditorInput;
import org.imixs.bpmn.model.sample.SampleImageProvider;
import org.xml.sax.InputSource;

public class ImixsRuntimeExtension implements IBpmn2RuntimeExtension {

	public static final String RUNTIME_ID = "org.imixs.workflow.bpmn.runtime";

	public static final String targetNamespace = "http://www.imixs.org/bpmn2";

	@Override
	public String getTargetNamespace(Bpmn2DiagramType diagramType) {
		return targetNamespace;
	}

	/**
	 * IMPORTANT: The plugin is responsible for inspecting the file contents!
	 * Unless you are absolutely sure that the file is targeted for this runtime
	 * (by, e.g. looking at the targetNamespace or some other feature) then this
	 * method must return FALSE.
	 */
	@Override
	public boolean isContentForRuntime(IEditorInput input) {
		InputSource source = new InputSource(
				FileService.getInputContents(input));
		RootElementParser parser = new RootElementParser(
				"http://www.imixs.org/bpmn2");
		parser.parse(source);
		return parser.getResult();
	}

	@Override
	public void notify(LifecycleEvent event) {
		if (event.eventType == EventType.EDITOR_INITIALIZED)
			SampleImageProvider.registerAvailableImages();

	}

	/**
	 * This method initalizes a new taskConfig Element and adds all parameters
	 * for the Imxis_Workflow system
	 * 
	 */
	public static void initializeTaskConfig(TaskConfig taskConfig) {
		// mail
		initializeProperty(taskConfig, "namMailReceiver", "");
		initializeProperty(taskConfig, "keyMailReceiverFields", "");
		initializeProperty(taskConfig, "namMailReceiverCC", "");
		initializeProperty(taskConfig, "keyMailReceiverFieldsCC", "");
		initializeProperty(taskConfig, "rtfMailBody", "");

		// application
		initializeProperty(taskConfig, "txtEditorID", "");
		initializeProperty(taskConfig, "txtImageURL", "");
		initializeProperty(taskConfig, "txtType", "");

		// summary
		initializeProperty(taskConfig, "txtworkflowsummary", "");
		initializeProperty(taskConfig, "txtworkflowabstract", "");

	}

	/**
	 * This method verifies if a specific property still exists. If not the
	 * method initializes the value
	 * 
	 * @param taskConfig
	 * @param propertyName
	 */
	public static Parameter initializeProperty(TaskConfig taskConfig,
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
	public static Parameter getProperty(TaskConfig taskConfig,
			String propertyName) {

		// test all parameters if we have the propertyName
		EList<Parameter> parameters = taskConfig.getParameters();
		for (Parameter param : parameters) {
			if (param.getName().equals(propertyName)) {
				// param allready exists
				return param;
			}
		}

		// we have not found this param - so we add a new one....
		return initializeProperty(taskConfig, propertyName, "");
	}

}
