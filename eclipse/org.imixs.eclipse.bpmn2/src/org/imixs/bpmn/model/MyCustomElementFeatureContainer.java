package org.imixs.bpmn.model;

import org.eclipse.bpmn2.modeler.core.features.ICustomElementFeatureContainer;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IDeleteFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;

public class MyCustomElementFeatureContainer implements
		ICustomElementFeatureContainer {

	public MyCustomElementFeatureContainer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canApplyTo(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IAddFeature getAddFeature(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getApplyObject(IContext arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDeleteFeature getDeleteFeature(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILayoutFeature getLayoutFeature(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRemoveFeature getRemoveFeature(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUpdateFeature getUpdateFeature(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAvailable(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ICustomFeature[] getCustomFeatures(IFeatureProvider arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId(EObject arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCustomTaskDescriptor(CustomTaskDescriptor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub

	}

}
