

package org.imixs.eclipse.workflowreports;

import org.eclipse.core.resources.IFile;
import org.imixs.eclipse.workflowreports.model.ReportObject;

/**
 * Interface for a ServerConnector to upload and download workflowmodels to a external server
 * The ServerConnectors appears in a WorkflowModelDefaultPage .
 
 * @author Ralph Soika
 * @see org.imixs.eclipse.OverviewPage.ui.editors.WorkflowModelDefaultPage
 *
 */
public interface ServerConnector {
	
	/**
	 * Downloads a model from a Server and stores the model into a file instance.
	 * 
	 * @param workflowModel
	 * @param file
	 */
	public void downloadModel(ReportObject workflowModel, IFile file) throws Exception;
		
	/**
	 * uploads a model form a server
	 * @param workflowModel
	 */
	public void uploadModel(ReportObject workflowModel) throws Exception;
			
}
