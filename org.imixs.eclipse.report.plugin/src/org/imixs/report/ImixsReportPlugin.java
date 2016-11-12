package org.imixs.report;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.imixs.report.editors.Report;
import org.imixs.workflow.xml.XMLItemCollection;
import org.imixs.workflow.xml.XMLItemCollectionAdapter;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ImixsReportPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.imixs.report.plugin"; //$NON-NLS-1$

	// The shared instance
	private static ImixsReportPlugin plugin;

	/**
	 * The constructor
	 */
	public ImixsReportPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ImixsReportPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public ImageDescriptor getIcon(String name) {
		String iconPath = "icons/";
		URL pluginUrl = getBundle().getEntry("/");
		try {
			return ImageDescriptor.createFromURL(new URL(pluginUrl, iconPath + name));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	/**
	 * This Method save a Model Object into a IFile The Method uses the
	 * XMLItemCollectionAdapter Class
	 * 
	 * @param fileInput - IEditorInput
	 * @param report - report object
	 * @param monitor - progress monitor
	 * 
	 * @see org.imixs.eclipse.workflowmodeler.XMLModelParser
	 * 
	 */
	public void saveReport(IEditorInput fileInput, Report report, IProgressMonitor monitor) {
		try {

			IFile file = fileInput.getAdapter(IFile.class);
			if (file == null)
				throw new FileNotFoundException();

			// convert the ItemCollection into a XMLItemcollection...
			XMLItemCollection xmlItemCollection = XMLItemCollectionAdapter
					.putItemCollection(report.getItemCollection());

			// marshal the Object into an XML Stream....
			StringWriter writer = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(XMLItemCollection.class);
			Marshaller m = context.createMarshaller();
			m.marshal(xmlItemCollection, writer);
			InputStream stream = new ByteArrayInputStream(writer.toString().getBytes(StandardCharsets.UTF_8));

			file.setContents(stream, true, true, monitor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// editorSaving = false;
		}
	}

}
