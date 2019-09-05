package org.imixs.bpmn.ui.task;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Task;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.bpmn2.modeler.core.merrimac.dialogs.TextObjectEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.Value;
import org.imixs.bpmn.ui.ImixsDetailComposite;

/**
 * This PorpertySection provides the attributes for Application config.
 * 
 * @author rsoika
 *
 */
public class ProcessApplicationPropertySection extends AbstractProcessPropertySection {

	@Override
	protected AbstractDetailComposite createSectionRoot() {
		return new ApplicationDetailComposite(this);
	}

	@Override
	public AbstractDetailComposite createSectionRoot(Composite parent, int style) {
		return new ApplicationDetailComposite(parent, style);
	}

	public class ApplicationDetailComposite extends ImixsDetailComposite {

		public ApplicationDetailComposite(AbstractBpmn2PropertySection section) {
			super(section);
		}

		public ApplicationDetailComposite(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		public void createBindings(final EObject be) {

			if (be == null || !(be instanceof Task)) {
				return;
			}
			setTitle("Application Properties");

			// Input Form
			Value itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txteditorid", null, "");
			TextObjectEditor valueEditor = new TextObjectEditor(this, itemValue, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Input Form");

			/*
			 * the following code forces an update of the task element if the
			 * value change. Bob means this code is unnecessary but I can not
			 * solve it in other way
			 * (https://www.eclipse.org/forums/index.php/t/1077944/)
			 */
			/*
			itemValue.eAdapters().add(new AdapterImpl() {
				
				public void notifyChanged(Notification notification) {
					int type = notification.getEventType();
					if (type == Notification.SET) {
						Object newValue = notification.getNewValue();
						if (newValue != null) {
							// System.out.println("notify type=SET value=" +
							// newValue);
							PictogramElement pe = null;
							//PictogramElement[] pictogramElementList = getDiagramEditor().getSelectedPictogramElements();
							List<PictogramElement> pictogramElementList = Graphiti.getLinkService().getPictogramElements(DIUtils.getDiagram((BaseElement)be), be);
							if (pictogramElementList != null && pictogramElementList.size() > 0) {
								pe = pictogramElementList.get(0);//[0];
								UpdateContext updateContext = new UpdateContext(pe);
								IUpdateFeature iUpdateFeature = getDiagramEditor().getDiagramTypeProvider()
										.getFeatureProvider().getUpdateFeature(updateContext);
								if (iUpdateFeature.canUpdate(updateContext))
									iUpdateFeature.update(updateContext);
							}

						}
					}
				}
			});
			*/

			// Image URL
			itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txtimageurl", null, "");
			valueEditor = new TextObjectEditor(this, itemValue, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Status Icon");

			// Type
			itemValue = ImixsBPMNPlugin.getItemValueByName((BaseElement) be, "txttype", null, "");
			valueEditor = new TextObjectEditor(this, itemValue, ImixsBPMNPlugin.IMIXS_ITEMVALUE);
			valueEditor.createControl(attributesComposite, "Workitem Type");

		}

	}

}
