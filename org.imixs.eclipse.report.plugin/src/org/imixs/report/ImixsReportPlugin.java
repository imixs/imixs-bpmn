package org.imixs.report;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.imixs.report.editors.Report;
import org.imixs.report.editors.ReportEditorInput;
import org.imixs.workflow.ItemCollection;
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
	 * @param fileInput
	 *            - IEditorInput
	 * @param report
	 *            - report object
	 * @param monitor
	 *            - progress monitor
	 * 
	 * @see org.imixs.workflow.xml.XMLItemCollectionAdapter
	 * 
	 */
	public void saveReport(ReportEditorInput reportInput, Report report, IProgressMonitor monitor) {
		try {
			IEditorInput fileInput = reportInput.getFileInput();
			IFile file = fileInput.getAdapter(IFile.class);
			if (file == null)
				throw new FileNotFoundException();

			// load the content of the XSL Resource file, if defined...
			String sXSLResource = report.getStringValue("xslresource");
			if (sXSLResource != null && !sXSLResource.isEmpty()) {
				// load the XSL resource from the workspace...
				IFile xslFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(sXSLResource));
				InputStream is = xslFile.getContents();
				StringBuilder xslContent = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String read;
				while ((read = br.readLine()) != null) {
					xslContent.append(read);
				}
				br.close();
				report.setItemValue("xsl", xslContent.toString());
			}

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

			reportInput.clearDirtyFlag();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// editorSaving = false;
		}
	}

	/**
	 * This Method loads the file content form an EditorInput and creates a new
	 * Instance of a Report Model Object.
	 * 
	 * @param fileInput
	 *            - IEditorInput
	 * @return report - report object
	 * 
	 * @see org.imixs.workflow.xml.XMLItemCollectionAdapter
	 * 
	 */
	public ItemCollection loadReportData(IEditorInput fileInput) {
		try {
			IFile file = fileInput.getAdapter(IFile.class);
			if (file == null)
				throw new FileNotFoundException();

			ItemCollection itemCollection = null;
			// extract item collections from request stream.....
			JAXBContext context = JAXBContext.newInstance(XMLItemCollection.class);
			Unmarshaller u = context.createUnmarshaller();
			XMLItemCollection ecol = (XMLItemCollection) u.unmarshal(file.getContents());
			itemCollection = XMLItemCollectionAdapter.getItemCollection(ecol);

			// migration deprecated fields....
			migrateDeprecatedFields(itemCollection);

			return itemCollection;
		} catch (Exception e) {
			// unable to read file - return null!
			return null;
		}
	}

	/**
	 * This method is used to migrate deprecated item filed names
	 * 
	 * txtcontenttype, txtencoding and txtxslresource
	 * 
	 * into 
	 * 
	 * contenttype, encoding and xslresource

	 * @param itemCollection
	 */
	private void migrateDeprecatedFields(ItemCollection itemCollection) {
		if (itemCollection.hasItem("txtcontenttype")) {
			itemCollection.replaceItemValue("contenttype", itemCollection.getItemValue("txtcontenttype"));
			itemCollection.removeItem("txtcontenttype");
		}

		if (itemCollection.hasItem("txtencoding")) {
			itemCollection.replaceItemValue("encoding", itemCollection.getItemValue("txtencoding"));
			itemCollection.removeItem("txtencoding");
		}

		if (itemCollection.hasItem("txtxslresource")) {
			itemCollection.replaceItemValue("xslresource", itemCollection.getItemValue("txtxslresource"));
			itemCollection.removeItem("txtxslresource");
		}
		
		
		itemCollection.removeItem("txtxsl");
		
		// following two lines can be removed in later versions...
		itemCollection.removeItem("name");
		itemCollection.removeItem("description");
		itemCollection.removeItem("query");
	}

	/**
	 * This method shows a resource selection dialog to select a file resource
	 * out of the current project (which is containing the current report file)
	 * 
	 * @param shell
	 * @param description
	 * @param fileExtension
	 *            - filters the file selection to the given extension
	 * @return
	 */
	public static IFile selectResource(Shell shell, IProject aproject, String description, final String fileExtension) {
		ResourceListSelectionDialog fsd = new ResourceListSelectionDialog(shell, aproject, IResource.FILE) {

			protected String adjustPattern() {
				String s = super.adjustPattern();
				if (s.equals("") && fileExtension != null) { //$NON-NLS-1$
					s = "*." + fileExtension; //$NON-NLS-1$
				}
				return s;
			}

			public void create() {
				super.create();
				refresh(true);
			}

			protected void updateOKState(boolean state) {
				super.updateOKState(true); // allow to select nothing
			}
		};

		IFile selected = null;
		fsd.setTitle(description);
		fsd.setAllowUserToToggleDerived(true);
		if (fsd.open() == Window.OK) {
			Object[] result = fsd.getResult();
			if (result != null && result.length > 0 && result[0] instanceof IFile) {
				selected = (IFile) result[0];
			} else {
				selected = null;
			}
		}
		return selected;
	}

}
