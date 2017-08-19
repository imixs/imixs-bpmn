package org.imixs.bpmn;

import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskImageProvider;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The ImixsLayoutAdapter is an abstract Adapter class to be extended by
 * specific element adapters. A subclass can overwrite the method
 * layoutImixsElement to to layout a element.
 * 
 * @version 2.0
 * @author rsoika
 *
 */
abstract public class ImixsLayoutAdapter extends EContentAdapter { // AdapterImpl
	public final static int DEFAULT_ACTIVITY_ID = 10;

	static Logger logger = Logger.getLogger(ImixsBPMNPlugin.class.getName());
	Map<String, String> propertyCache;
	BaseElement imixsElement;

	ContainerShape containerShape = null;

	public ImixsLayoutAdapter(ContainerShape containerShape) {
		super();
		this.containerShape = containerShape;
	}

	/**
	 * This method verifies if the target element is of Type ImixsEvent or
	 * ImixsType.
	 */
	@Override
	public void setTarget(Notifier newTarget) {
		if (ImixsBPMNPlugin.isImixsCatchEvent(newTarget) || ImixsBPMNPlugin.isImixsTask(newTarget)) {
			imixsElement = (BaseElement) newTarget;
			layoutImixsElement();
		}
		super.setTarget(newTarget);
	}

	/**
	 * The method layoutImixsElement can be overwritten by a sub class to set a
	 * custom layout for a task or event. The method is called during the
	 * initialization by the method setTarget()
	 */
	public void layoutImixsElement() {

	}

	/**
	 * This method loads a custom image and register it automatically on first load.
	 * 
	 * @param fileName
	 * @param ga
	 * @return
	 */
	Image loadCustomTaskIcon(String fileName, GraphicsAlgorithm ga) {
		// TODO: Did we need to optimize this method? Will images be unregistered
		// automatically?

		String imageId = fileName;
		String filename = "/icons/large/" + fileName;
		URL url = getClass().getClassLoader().getResource(filename);
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);

		CustomTaskImageProvider.registerImage(imageId, descriptor);
		Image img = Graphiti.getGaService().createImage(ga, imageId);
		img.setProportional(false);
		img.setWidth(16);
		img.setHeight(16);
		img.setStretchH(true);
		img.setStretchV(true);

		return img;

	}
}
