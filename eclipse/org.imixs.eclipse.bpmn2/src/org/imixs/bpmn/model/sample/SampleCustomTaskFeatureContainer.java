/*******************************************************************************
 * Copyright (c) 2011, 2012 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.imixs.bpmn.model.sample;

import org.eclipse.bpmn2.TextAnnotation;
import org.eclipse.bpmn2.modeler.core.adapters.ExtendedPropertiesAdapter;
import org.eclipse.bpmn2.modeler.core.features.CustomShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.IShapeFeatureContainer;
import org.eclipse.bpmn2.modeler.core.features.MultiUpdateFeature;
import org.eclipse.bpmn2.modeler.core.features.artifact.AddTextAnnotationFeature;
import org.eclipse.bpmn2.modeler.core.features.artifact.UpdateTextAnnotationFeature;
import org.eclipse.bpmn2.modeler.core.features.label.UpdateLabelFeature;
import org.eclipse.bpmn2.modeler.core.model.ModelDecorator;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle;
import org.eclipse.bpmn2.modeler.core.preferences.ShapeStyle.LabelPosition;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.StyleUtil;
import org.eclipse.bpmn2.modeler.ui.features.artifact.CreateTextAnnotationFeature;
import org.eclipse.bpmn2.modeler.ui.features.artifact.TextAnnotationFeatureContainer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.AbstractText;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;
import org.imixs.bpmn.model.sample.SampleImageProvider.IconSize;

/**
 * Example implementation of a Custom Shape feature container. The main things to consider
 * here are:
 * 
 * createFeatureContainer() - creates the Feature Container that is responsible for
 *   building the "custom shape". This can be a subclass of an existing Feature Container
 *   from the editor core, or a new one. Typically, this should be a subclass of the
 *   Feature Container for the type of bpmn2 element defined in the "type" attribute
 *   of this Custom Task extension point.
 * 
 * If your Feature Container extends one of the existing classes from editor core, you should
 * override the following methods:
 * 
 * getAddFeature() - this should override the Add Feature from the chosen Feature Container
 *   base class (see above). Typically you will want to override the decorateShape() method
 *   which allows you to customize the graphical representation of this Custom Task figure.
 * getCreateFeature() - this MUST be overridden if you intend to add extension attributes to
 *   your business object (bpmn2 element) - see the code example below. You will also want to
 *   provide your own images for the tool palette by overriding getCreateImageId() and
 *   getCreateLargeImageId() in your Create Feature.
 * 
 * For example, if you are extending a TextAnnotation object, you should use the core editor's
 * TextAnnotationFeatureContainer and then override the getCreateFeature() and getAddFeature()
 * methods so they return subclasses of CreateTextAnnotationFeature and AddTextAnnotationFeature
 * 
 * @author Bob Brodt
 */
public class SampleCustomTaskFeatureContainer extends CustomShapeFeatureContainer {

	public final static String MITIGATION_ID = "org.eclipse.bpmn2.modeler.examples.dynamic.mitigation";
	public final static String RISK_ID = "org.eclipse.bpmn2.modeler.examples.dynamic.risk";

	@Override
	protected IShapeFeatureContainer createFeatureContainer(IFeatureProvider fp) {
		return new TextAnnotationFeatureContainer() {

			@Override
			public IAddFeature getAddFeature(IFeatureProvider fp) {
				return new AddTextAnnotationFeature(fp) {

					/* (non-Javadoc)
					 * @see org.eclipse.bpmn2.modeler.core.features.AbstractBpmn2AddFeature#decorateShape(org.eclipse.graphiti.features.context.IAddContext, org.eclipse.graphiti.mm.pictograms.ContainerShape, org.eclipse.bpmn2.BaseElement)
					 * 
					 * This implementation of TextAnnotation's decorateShape() method changes the appearance of
					 * the figure by removing the large "[" shape and replacing it with a rounded rectangle,
					 * similar to a Task figure. It also adds an image in the top-left corner of the
					 * rectangle; the image is determined by the string value of the extension attribute "icon"
					 * (see the SampleImageProvider class for details of exactly how and where this happens).
					 * Finally, the fill gradient color of the figure is set according to the value
					 * of the boolean extension attribute "evaluate" (see setFillColor(), below).
					 */
					@Override
					protected void decorateShape(IAddContext context, ContainerShape containerShape, TextAnnotation businessObject) {
						IGaService gaService = Graphiti.getGaService();
						IPeService peService = Graphiti.getPeService();
						// Change the size of the default TextAnnotation selection rectangle
						Rectangle selectionRect = (Rectangle)containerShape.getGraphicsAlgorithm();
						int width = 140;
						int height = 60;
						selectionRect.setWidth(width);
						selectionRect.setHeight(height);
						
						// Remove the "bracket" polygon that is the visual for TextAnnotation... 
						peService.deletePictogramElement(containerShape.getChildren().get(0));
						// ...and replace it with a RoundedRectangle
						Shape rectShape = peService.createShape(containerShape, false);
						peService.sendToBack(rectShape);
						RoundedRectangle roundedRect = gaService.createRoundedRectangle(rectShape, 5, 5);
						// apply the same styling as TextAnnotation
						StyleUtil.applyStyle(roundedRect, businessObject);
						gaService.setLocationAndSize(roundedRect, 0, 0, width, height);
						// set fill color based on the "evaluate" extension attribute
						setFillColor(containerShape);

						// add an image to the top-left corner of the rectangle
						Image img = SampleImageProvider.createImage(roundedRect, customTaskDescriptor, 38, 38);
						Graphiti.getGaService().setLocation(img, 2, 2);
						
						// change location of the MultiText pictogram so it doesn't overlap the image
						for (PictogramElement pe : containerShape.getChildren()) {
							GraphicsAlgorithm ga = pe.getGraphicsAlgorithm();
							if (ga instanceof MultiText) {
								Graphiti.getGaService().setLocationAndSize(ga, 40, 2, width-42, height-2);
							}
						}
					}
				};
			}

			@Override
			public ICreateFeature getCreateFeature(IFeatureProvider fp) {
				return new CreateTextAnnotationFeature(fp) {

					@Override
					public TextAnnotation createBusinessObject(ICreateContext context) {
						TextAnnotation businessObject = super.createBusinessObject(context);
						return businessObject;
					}

					@Override
					public String getCreateImageId() {
						return SampleImageProvider.getImageId(customTaskDescriptor, IconSize.SMALL);
					}

					@Override
					public String getCreateLargeImageId() {
						return SampleImageProvider.getImageId(customTaskDescriptor, IconSize.LARGE);
					}
					
					@Override
					public String getCreateDescription() {
						return "Create "+customTaskDescriptor.getName();
					}
				};
			}

			@Override
			public IUpdateFeature getUpdateFeature(IFeatureProvider fp) {

				MultiUpdateFeature multiUpdate = new MultiUpdateFeature(fp);
				multiUpdate.addFeature(new UpdateTextAnnotationFeature(fp) {
					/* (non-Javadoc)
					 * @see org.eclipse.bpmn2.modeler.core.features.AbstractUpdateBaseElementFeature#updateNeeded(org.eclipse.graphiti.features.context.IUpdateContext)
					 * 
					 * This override compares the ContainerShape's "evaluate.property" string property
					 * with the boolean extension attribute "evaluate" in the TextAnnotation object,
					 * and returns true if they differ, false if the same.
					 */
					@Override
					public IReason updateNeeded(IUpdateContext context) {
						IReason reason = super.updateNeeded(context);
						if (reason.toBoolean())
							return reason;
						
						PictogramElement pe = context.getPictogramElement();
						String propertyValue = Graphiti.getPeService().getPropertyValue(pe, "evaluate.property");
						if (propertyValue==null || propertyValue.isEmpty())
							propertyValue = "false";
						
						Boolean attributeValue = false;
						TextAnnotation ta = (TextAnnotation) getBusinessObjectForPictogramElement(pe);
						ExtendedPropertiesAdapter adapter = ExtendedPropertiesAdapter.adapt(ta);
						EStructuralFeature f = adapter.getFeature("evaluate");
						if (f!=null) {
							attributeValue = (Boolean)adapter.getFeatureDescriptor(f).getValue();
							if (attributeValue==null)
								attributeValue = false;
						}
						if (Boolean.parseBoolean(propertyValue) != attributeValue)
							return Reason.createTrueReason("evalute property changed");
						return Reason.createFalseReason("");
					}

					@Override
					public boolean update(IUpdateContext context) {
						super.update(context);
						setFillColor((ContainerShape)context.getPictogramElement());
						return true;
					}
				});
				multiUpdate.addFeature(new UpdateLabelFeature(fp) {

					@Override
					protected LabelPosition getLabelPosition(AbstractText text) {
						return LabelPosition.CENTER;
					}
					
				});
				return multiUpdate;
			}

			@Override
			public IDirectEditingFeature getDirectEditingFeature(IFeatureProvider fp) {
				return null;
			}

			@Override
			public IResizeShapeFeature getResizeFeature(IFeatureProvider fp) {
				return null;
			}
			
			/**
			 * Common method used to set the fill color for Mitigation and Risk CustomTask figures.
			 * This method is called by both the CreateFeature and the UpdateFeature.
			 * The fill color is set according to the current value of the boolean extension attribute
			 * "evaluate". If this attribute does not exist, it is created and set to its default value (false).
			 * The fill color is set to LIGHT_GREEN if "evaluate" is true and LIGHT_GRAY if it is false.
			 * 
			 * Finally, a string property in the given ContainerShape (named "evaluate.property")
			 * is set to the current value of "evaluate". This is later used to determine if the
			 * attribute value has changed and needs to be updated. See UpdateTextAnnotationFeature#updateNeeded()
			 * above, for details.
			 *
			 * @param containerShape - the ContainerShape that corresponds to this Risk or Mitigation.
			 */
			private void setFillColor(ContainerShape containerShape) {
				TextAnnotation ta = BusinessObjectUtil.getFirstElementOfType(containerShape, TextAnnotation.class);
				if (ta!=null) {
					ExtendedPropertiesAdapter adapter = ExtendedPropertiesAdapter.adapt(ta);
					Boolean attributeValue = (Boolean)adapter.getFeatureDescriptor("evaluate").getValue();
					Shape shape = containerShape.getChildren().get(0);
					ShapeStyle ss = new ShapeStyle();
					String propertyValue;
					if (Boolean.TRUE.equals(attributeValue)) {
						propertyValue = Boolean.TRUE.toString();
						ss.setDefaultColors(IColorConstant.LIGHT_GREEN);
					}
					else {
						propertyValue = Boolean.FALSE.toString();
						ss.setDefaultColors(IColorConstant.LIGHT_GRAY);
					}
					StyleUtil.applyStyle(shape.getGraphicsAlgorithm(), ta, ss);
					Graphiti.getPeService().setPropertyValue(containerShape, "evaluate.property", propertyValue);
				}
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.ui.features.activity.task.CustomTaskFeatureContainer#getId(org.eclipse.emf.ecore.EObject)
	 * 
	 * This method is called by the Feature Provider when it needs to find the Feature Container that will be handling the
	 * creation of a new object. @see org.eclipse.bpmn2.modeler.ui.diagram.BPMN2FeatureProvider.getAddFeature(IAddContext).
	 * This method should inspect the object (which will be a bpmn2 element) and determine whether it is responsible for
	 * managing this object's lifecycle, typically by examining extension attributes, as shown in this example.
	 */
	public String getId(EObject object) {
		if (object instanceof TextAnnotation) {
			if (ModelDecorator.getAnyAttribute(object, "benefit")!=null) {
				return MITIGATION_ID; 
			}
			if (ModelDecorator.getAnyAttribute(object, "cost")!=null) {
				return RISK_ID; 
			}
		}
		return null;
	}
}
