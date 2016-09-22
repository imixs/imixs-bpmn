package org.imixs.eclipse.workflowreports.ui.editors;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.imixs.eclipse.workflowreports.ServerConnector;
import org.imixs.eclipse.workflowreports.WorkflowReportPlugin;
import org.imixs.eclipse.workflowreports.model.ReportObject;
import org.imixs.eclipse.workflowreports.restservice.RestServiceConnector;

/**
 * This Page is the default Page of the IXXMLEditor. It will show the
 * Serverconfiguration and the existing ProcessTrees The SyncSection includes
 * widgets to start a syncronation process connecting to a webservice defined on
 * the WorkflowModelWebServicePage
 * 
 * @see org.imixs.eclipse.workflowmodeler.ui.editors.WorkflowModelWebServicePage
 * @author Ralph Soika
 * 
 */
public class OverviewPage extends IXEditorPage {
	// implements
	// PropertyChangeListener {
	private FormToolkit toolkit;

	private ReportObject report;

	ScrolledForm form;

	Composite clientProcessList = null, clientSync = null;
	Composite clientTest = null;

	ImageHyperlink hyperLinkSynchronize = null;

	Label labelID = null;

	Label labelVersion = null;

	/**
	 * 
	 */
	public void initializeWorkflowModel(ReportObject amodel) {
		report = amodel;
	}

	/**
	 * @param mform
	 */
	public void createFormContent(IManagedForm managedForm) {
		form = managedForm.getForm();

		toolkit = managedForm.getToolkit();
		form.setText("Report Overview");

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		form.getBody().setLayout(gridLayout);

		form.setBackgroundImage(WorkflowReportPlugin.getPlugin().getIcon(
				"editor/form_banner.gif").createImage());

		createQuerySection(managedForm);
		createAttributeSection(managedForm);
		createUploadSection(managedForm);
		createTestSection(managedForm);
	}

	/**
	 * This Section supports a HyperLink to upload the servermodel. The
	 * Hyperlink uses the corresponding ServerConnector to update the model
	 * 
	 * @param mform
	 * @param title
	 * @param desc
	 */
	private void createQuerySection(IManagedForm mform) {

		GridData gdSection = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gdSection.widthHint = 250;
		gdSection.verticalSpan = 2;
		clientSync = createSection(mform, "EQL Statement",
				"This section defines the EQL Statement.",

				Section.TITLE_BAR | Section.DESCRIPTION, 2, gdSection,
				WorkflowReportPlugin.getPlugin().getIcon("editor/contents.gif"));

	
		GridData gd = new GridData();
		gd.heightHint = 135;
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;

		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = GridData.FILL;

		/*** Query ***/
		createTextAreaInput(clientSync, toolkit, gd, "Query:", "txtQuery",
				report);

	}

	private void createAttributeSection(IManagedForm mform) {

		GridData gdSection = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gdSection.widthHint = 250;
		gdSection.verticalSpan = 2;
		
		Composite compositeObjectsNames = createSection(mform, "Attributes",
				"Set of Attriubtes to be included in a HTML Report",
				Section.TITLE_BAR | Section.DESCRIPTION, 1, gdSection);

		final Image imageName;
		imageName = WorkflowReportPlugin.getPlugin().getIcon(
				"editor/attribute.gif").createImage();

		this.createTableInput(compositeObjectsNames, toolkit,
				"txtAttributeList", report, imageName);

	}

	private void createUploadSection(IManagedForm mform) {

	
		GridData gdSection = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gdSection.widthHint = 250;
		gdSection.verticalSpan = 2;
		
		
		clientProcessList = createSection(mform, "Upload Report",
				"Upload this report with to an Imixs Workflow Server", Section.TWISTIE
						| Section.EXPANDED | Section.TITLE_BAR
						| Section.DESCRIPTION, 1, gdSection, WorkflowReportPlugin
						.getPlugin().getIcon("editor/config.gif"));

		createFormInput(clientProcessList, toolkit, "Web Service Location:",
				"txtWorkflowManagerURI", report);

		
		
		// Create new Upload Link for REST Service
		ImageHyperlink createLinkRest = this.createLink(clientProcessList, toolkit,
				WorkflowReportPlugin.getPlugin().getIcon("editor/upload.gif"),
				"upload report");
		createLinkRest.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent linkEvent) {
				// create ProcessTree

				RestServiceConnector wsc = new RestServiceConnector();
				try {
					wsc.uploadModel(report);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});

	}

	private void createTestSection(IManagedForm mform) {

		
		GridData gdSection = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gdSection.widthHint = 250;
		gdSection.verticalSpan = 2;
		
		clientTest = createSection(mform, "Test Report",
				"Test current Report on an Imixs Workflow Server", Section.TWISTIE
						| Section.TITLE_BAR | Section.DESCRIPTION, 1, gdSection);

		createFormInput(clientTest, toolkit, "REST Service:",
				"txtWorkflowManagerRESTURI", report);

		// Create new ProcessTree Link bauen
		ImageHyperlink createLink = this.createLink(clientTest, toolkit,
				WorkflowReportPlugin.getPlugin().getIcon("editor/test.gif"),
				"test report");
		createLink.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent linkEvent) {
				// create ProcessTree
				System.out.println(" Testing report not implemented ");
			}
		});

	}

	/**
	 * returns the selected server connector
	 * 
	 * @param sType
	 * @return
	 */
	public ServerConnector getServerConnector(String sType) {

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint("org.imixs.eclipse.workflowmodeler.serverconnectors");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				final String sname = elements[j].getAttribute("name");
				// ist der name der richtige?
				if (sname.equals(sType)) {
					try {
						Object o = elements[j]
								.createExecutableExtension("class");
						if (o instanceof ServerConnector) {
							ServerConnector serverConnector = (ServerConnector) o;
							return serverConnector;
						}
					} catch (org.eclipse.core.runtime.CoreException ec) {
						System.out
								.println("[WorkflowReportOverviewPage] unable to create ServerConnector "
										+ sType);
						ec.printStackTrace();
					} catch (Exception e) {
						System.out
								.println("[WorkflowReportOverviewPage] unable to create ServerConnector "
										+ sType + " " + e.toString());
					}
					break;
				}
			}
		}
		return null;
	}

	
}
