package org.imixs.eclipse.workflowreports.restservice;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants;
import org.imixs.eclipse.workflowreports.model.ReportObject;
import org.imixs.workflow.services.rest.RestClient;
import org.imixs.workflow.xml.XMLItemCollection;
import org.imixs.workflow.xml.XMLItemCollectionAdapter;

/**
 * This uploadJob synchronized the report object with a rest service. The
 * expected WebService Implementation is based on the Imixs JEE Workflow Version
 * 2.0.2 or grater. 
 * 
 * Before the Upload procedure starts the Job removes any existing report Entity
 * 
 * Notice! The UploadJob depends on the Imixs JEE Workflow Version 2.0.2 or
 * higher.
 * 
 * @author rsoika
 * 
 */
public class UploadJobMultiModel extends Job {
	ReportObject report;
	RestClient restClient;
	String modelVersion = "";
	String sURI;
	String httpErrorMessage=null;

	public UploadJobMultiModel(String aName, ReportObject amodel,String aURI,
			RestClient aClient) {
		super(aName);
		report = amodel;
		sURI=aURI;
		restClient = aClient;
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			
				
			monitor.beginTask("upload report", 1);

			// build an entityCollection
			XMLItemCollection xmlItemCol=XMLItemCollectionAdapter.putItemCollection( report.getItemCollection());
			// post the model to the provided location with additional model version
			int iHTTPResult=restClient.postEntity(sURI+"/"+modelVersion,xmlItemCol);
			
			System.out.println("[RestServiceConnector] post model: result="
					+ iHTTPResult);

			if (iHTTPResult < 200 || iHTTPResult > 299) {

				if (iHTTPResult == 404)
					httpErrorMessage = "The requested resource could not be found. Please verifiy your web service location.";
				else if (iHTTPResult == 403)
					httpErrorMessage = "The username/password you entered were not correct. Your request was denied as you have no permission to access the server. Please try again.";
				else
					httpErrorMessage = "The model data could not be uploaded to the workflow server. Please verifiy your server settings. HTTP Result="
							+ iHTTPResult;
				throw new Exception(httpErrorMessage);

			}
			
			monitor.worked(1);
			if (isModal(this)) {
				// The progress dialog is still open so
				// just open the message
				// showResults();

			} else {
				setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
				setProperty(IProgressConstants.ACTION_PROPERTY,
						getReservationCompletedAction());
			}

			return Status.OK_STATUS;
		} catch (Exception uploadException) {

			uploadException.printStackTrace();
			org.eclipse.core.runtime.Status status = new org.eclipse.core.runtime.Status(
					IStatus.ERROR,
					org.imixs.eclipse.workflowreports.WorkflowReportPlugin.PLUGIN_ID,
					-1,
					"Upload Report failed!\n\n" + httpErrorMessage,
					uploadException);
			return status;

		}
	}

	/*
	 * 
	 * display.asyncExec(new Runnable() { public void run() { } });
	 */
	protected Action getReservationCompletedAction() {
		return new Action("View Upload status") {
			public void run() {
				MessageDialog.openInformation(getShell(), "Upload Complete",
						"Upload of the workflow model has been completed");
			}
		};
	}

	public boolean isModal(Job job) {
		Boolean isModal = (Boolean) job
				.getProperty(IProgressConstants.PROPERTY_IN_DIALOG);
		if (isModal == null)
			return false;
		return isModal.booleanValue();
	}

	private Shell getShell() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getShell();
	}



}
