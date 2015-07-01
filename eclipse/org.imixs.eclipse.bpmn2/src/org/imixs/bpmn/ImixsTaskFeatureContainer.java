package org.imixs.bpmn;

import org.eclipse.bpmn2.modeler.core.features.activity.task.AddTaskFeature;
import org.eclipse.bpmn2.modeler.ui.features.activity.task.TaskFeatureContainer;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;

public class ImixsTaskFeatureContainer extends TaskFeatureContainer {

	@Override
	public IAddFeature getAddFeature(IFeatureProvider fp) {
		return new ImixsAddTaskFeature(fp);
	}

	public static class ImixsAddTaskFeature extends AddTaskFeature {

		public ImixsAddTaskFeature(IFeatureProvider fp) {
			super(fp);
		}

		
		
	}
}