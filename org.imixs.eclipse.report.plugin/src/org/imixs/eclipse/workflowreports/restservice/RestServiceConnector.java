package org.imixs.eclipse.workflowreports.restservice;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.imixs.eclipse.workflowreports.ServerConnector;
import org.imixs.eclipse.workflowreports.model.ReportObject;
import org.imixs.workflow.services.rest.RestClient;

/**
 * This class supports function for download and upload a model from a REST
 * service endpoint. The URI to be used is
 * stored in the environment ENVIRONMENT_WEBSERVICE
 * 
 * This Environment can be configured by the WorkflowModelWebServicePage
 * 
 * This ServiceConnector provides the multi-model feature. Upload is performed
 * by using spcific modelversion. 
 * 
 * @see org.imixs.eclipse.workflowmodeler.ui.editors.WorkflowModelWebServicePage
 * @author Ralph Soika
 */
public class RestServiceConnector implements ServerConnector {
	private ReportObject report = null;
 
	private RestClient restClient;
 

	private String sUserID,sURI;

	/**
	 * This Method initialize JAX-RCP Web Service Client
	 * 
	 * The ServerConnector could be improved. Feel free to post your thoughts!
	 * 
	 */
	public boolean initializeService() {
		String sPassword;
		try {
			/** * Login Dialog to get UserID and Password ** */
			Shell shell = getShell();
			LoginDialog loginDlg = new LoginDialog(shell);
			if (loginDlg.open() != LoginDialog.OK)
				return false; // cancel login!
			sUserID = loginDlg.getSecurityPrinzipal();
			sPassword = loginDlg.getSecurityCredentials();

		

			sURI = report.getItemCollection().getItemValueString(
			"txtWorkflowManagerURI");
			// cut '/' if last char
			if (sURI.endsWith("/"))
				sURI=sURI.substring(0,sURI.length()-1);

			System.out.println("[WebServiceConnector] " + sURI);

			restClient = new RestClient();
			restClient.setCredentials(sUserID,sPassword);
		} catch (Exception e) {

			System.out
					.println("[WebServiceConnector] initializeService failed!");

			MessageDialog.openInformation(getShell(), "WebServiceConnector",
					"Initialize WebService failed! \n\n" + e.getMessage());

			e.printStackTrace();
			return false;

		}
		return true;
	}

	
	/**
	 * This method saves a report up to a Imixs JEE Rest Service endpoint
	 * 
	 * @param workflowModel
	 */
	public void uploadModel(ReportObject areport) throws Exception {
		report = areport;
		// Initialize Web Service
		if (initializeService() == false)
			throw new Exception("Error inizialize WebService");
 
		final UploadJobMultiModel job = new UploadJobMultiModel("uploading workflowmodel...",
				report,sURI, restClient); 
		job.setUser(true);
		job.schedule();
	}
 
	private Shell getShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getShell();
	}


	public void downloadModel(ReportObject workflowModel, IFile file)
			throws Exception {
		throw new Exception ("downlaodmodel is not implemented");
	}






}