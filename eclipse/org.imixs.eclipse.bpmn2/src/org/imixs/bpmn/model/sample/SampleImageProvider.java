/*******************************************************************************
 * Copyright (c) 2011, 2012, 2013 Red Hat, Inc.
 * All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 	Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.imixs.bpmn.model.sample;

import java.net.URL;

import org.eclipse.bpmn2.modeler.core.runtime.CustomTaskDescriptor;
import org.eclipse.bpmn2.modeler.core.runtime.TargetRuntime;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.internal.GraphitiUIPlugin;
import org.eclipse.graphiti.ui.platform.AbstractImageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.imixs.bpmn.model.ImixsRuntimeExtension;

/**
 * Image provider class for our Custom Task extensions.
 * 
 * TODO: In Kepler, this may change at which time we can register these icons
 * in the plugin.xml as a Graphiti extension point. If this doesn't happen,
 * we should probably consider pushing image registration up to the core editor.
 * 
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=366452
 * @author bbrodt
 */
public class SampleImageProvider extends AbstractImageProvider {
	
	public final static String IMAGE_ID_PREFIX =
			SampleImageProvider.class.getPackage().getName() + ".";
	public final static String ICONS_FOLDER = "icons/";

	// Sneaky tip: The values of this enum correspond to the subfolder names in "icons"
	public enum IconSize {
		SMALL("small"),
		LARGE("large"),
		HUGE("huge");
		private String value;
		private IconSize(String value) {
			this.value = value;
		}
	}
	private static boolean registered = false;

	public SampleImageProvider() {
		super();
	}

	public static void registerAvailableImages() {
		if (!registered) {
			ImageRegistry imageRegistry = GraphitiUIPlugin.getDefault().getImageRegistry();
			TargetRuntime rt = TargetRuntime.getRuntime(ImixsRuntimeExtension.RUNTIME_ID);
			for (IconSize size : IconSize.values()) {
				for (CustomTaskDescriptor ctd : rt.getCustomTaskDescriptors()) {
					String imageId = getImageId(ctd,size); 
					if (imageId != null) {
						String filename = getImagePath(ctd,size);
						URL url = SampleImageProvider.class.getClassLoader().getResource(filename);
						ImageDescriptor descriptor =  ImageDescriptor.createFromURL(url);
						imageRegistry.put(imageId, descriptor);
					}
				}
			}
			registered = true;
		}
	}
	
	@Override
	protected void addAvailableImages() {
		TargetRuntime rt = TargetRuntime.getRuntime(ImixsRuntimeExtension.RUNTIME_ID);
		for (IconSize size : IconSize.values()) {
			for (CustomTaskDescriptor ctd : rt.getCustomTaskDescriptors()) {
				String imageId = getImageId(ctd,size); 
				if (imageId != null) {
					addImageFilePath(imageId, getImagePath(ctd,size));
				}
			}
		}
	}

	public static Image createImage(GraphicsAlgorithmContainer ga, CustomTaskDescriptor ctd, int w, int h) {
		// To create an image of a specific size, use the "huge" versions
		// to prevent pixelation when stretching a small image
		String imageId = getImageId(ctd, IconSize.HUGE);
		Image img = null;
		if (imageId != null) {
			img = Graphiti.getGaService().createImage(ga, imageId);
			img.setProportional(false);
			img.setWidth(w);
			img.setHeight(h);
			img.setStretchH(true);
			img.setStretchV(true);
		}
		return img;
	}
	
	public static String getImageId(CustomTaskDescriptor ctd, IconSize size) {
		String icon = (String) ctd.getPropertyValue("icon"); 
		if (icon != null && icon.trim().length() > 0) {
			return IMAGE_ID_PREFIX + icon.trim() + "." + size.value;
		}
		return null;
	}
	
	public static String getImagePath(CustomTaskDescriptor ctd, IconSize size) {
		String icon = (String) ctd.getPropertyValue("icon"); 
		if (icon != null && icon.trim().length() > 0) {
			return ICONS_FOLDER + size.value + "/" + icon.trim();
		}
		return null;
	}
}
