package org.imixs.bpmn;

import org.eclipse.bpmn2.modeler.core.IBpmn2RuntimeExtension;
import org.eclipse.bpmn2.modeler.core.LifecycleEvent;
import org.eclipse.bpmn2.modeler.core.LifecycleEvent.EventType;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil.Bpmn2DiagramType;
import org.eclipse.bpmn2.modeler.ui.DefaultBpmn2RuntimeExtension.RootElementParser;
import org.eclipse.bpmn2.modeler.ui.wizards.FileService;
import org.eclipse.ui.IEditorInput;
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

	/**
	 * Add notification adapters here.
	 * 
	 */
	@Override
	public void notify(LifecycleEvent event) {

		// as we can not identify the the Imixs Event here we do add a event adapter
		// see: https://www.eclipse.org/forums/index.php/t/1065614/
		if (event.eventType == EventType.BUSINESSOBJECT_CREATED) {
			// EObject object = (EObject) event.target;
			// if (object instanceof IntermediateCatchEvent) {
			// object.eAdapters().add(new ImixsEventAdapter());
			// }

		}
	}

}
