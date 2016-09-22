package org.imixs.eclipse.workflowreports;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.imixs.eclipse.workflowreports.model.ReportObject;
import org.imixs.workflow.ItemCollection;
import org.osgi.framework.BundleContext;


/**
 * The main plugin class to be used in the desktop.
 */
public class WorkflowReportPlugin extends AbstractUIPlugin {
	private transient PropertyChangeSupport propertyChangeSupport;
	//The shared instance.
	private static WorkflowReportPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	// WorkflowModel
	private ReportObject queryObject=null;
	private Hashtable workflowModelList = new Hashtable();
	
	// copy of a model object for Copy&Paste Attributes
	private ItemCollection entityCopy = null;
	
	// Speicher f�r gel�schte Entities
	private Vector deletionStore=new Vector();
	
	
	public static String PLUGIN_ID = "org.imixs.eclipse.workflowreports";
	
	
	/**
	 * Constructor of the WorkflowmodlerPlugiin initializes the ResourceBundle
	 * and registers the ServerConnectors defined by the ServerConnector Extension Points.
	 */
	public WorkflowReportPlugin() {
		super();
		
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("org.imixs.eclipse.workflowmodeler.WorkflowqueryPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	
	}
	
	

	
	protected void initializeDefaultPreferences(IPreferenceStore store) {
		// set default preference for editor style
		//store.setDefault("editorstyle","Popular");

	}
	
	
	
	
	/**
	 * Returns a WorkflowModel specified by its name
	 * @param aName
	 * @return
	 */
	public ReportObject getQueryObject(String aName) {
		return (ReportObject)workflowModelList.get(aName);
	}
	
	
	

	/**
	 * This Method read a Model from an xml or .ixm file int a new 
	 * WorkfowModel Object
	 * @param fileInput
	 * @see org.imixs.eclipse.workflowmodeler.XMLModelParser
	 */
	public ReportObject loadQueryObject(IFile fileInput) {
		ReportObject queryObjectNew=null;
		
		 queryObjectNew=XMLModelParser.parseModel(fileInput);
		
		workflowModelList.put(queryObjectNew.getName(),queryObjectNew);
		// clear Dirty Flag
		queryObjectNew.clearDirtyFlag();
		
		return queryObjectNew;
    }

	
	/**
	 * This Method save a Model Object into a IFile 
	 * The Method uses the XMLModelParser Class
	 * @param aModel
	 * @param file
	 * @param monitor
	 * 
	 * @see org.imixs.eclipse.workflowmodeler.XMLModelParser
	 * 
	 */
	public void saveWorkflowModel(ReportObject aModel, IFile file, IProgressMonitor monitor) {
		try {
			ByteArrayOutputStream out = XMLModelParser.transformModel(aModel);
			file.setContents(new ByteArrayInputStream(out.toByteArray()),
			       true, true, monitor);
			out.close();
			// clear Dirty Flag
			aModel.clearDirtyFlag();
			
			
		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		  // editorSaving = false;
		}	
	}
	
	
	
	
	
	
	
	/**
	 * Das Plugin selber ist in der Lage PropertyChanges zu senden
	 * Dadurch k�nnen sich z.B.: ContentProvider direkt am Plugin registrieren, 
	 * um sich �ber eine �nderung des Models zu informieren. 
	 * Dies tut z.B.: der WorkflwoModelContnetProvider f�r seinen TreeViewer das neue InputElement zu setzen
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        getPropertyChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
    }    
    
        
	
	public Vector getDeletionStore() {
		return deletionStore;
	}
	

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	//	loadWorkflowModel();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		queryObject=null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static WorkflowReportPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = WorkflowReportPlugin.getPlugin().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
	/**
	 * returns an ImageDescriptor to the Image Ressource name
	 * @param name
	 * @return
	 */
	public ImageDescriptor getIcon(String name) {
        String iconPath = "icons/";
        URL pluginUrl = getBundle().getEntry("/");
        try {
            return ImageDescriptor.createFromURL(new URL(pluginUrl, iconPath + name));
        } catch (MalformedURLException e) {
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }
	 

	
    
    private PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
    }
    
    
    
 
  
    
}

