package org.imixs.eclipse.workflowreports.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.imixs.eclipse.workflowreports.WorkflowReportPlugin;
import org.imixs.eclipse.workflowreports.model.ReportObject;

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
public class XSLPage extends IXEditorPage {
	// implements
	// PropertyChangeListener {
	private FormToolkit toolkit;

	private ReportObject report;

	ScrolledForm form;

	Composite clientXSL = null;

	ImageHyperlink hyperLinkSynchronize = null;

	Label labelID = null;

	Label labelVersion = null;

	/**
	 * 
	 */
	public void initializeWorkflowModel(ReportObject areport) {
		report = areport;
	}

	/**
	 * create the XSL Section
	 * 
	 * @param mform
	 */
	public void createFormContent(IManagedForm managedForm) {
		form = managedForm.getForm();

		toolkit = managedForm.getToolkit();
		form.setText("XSL Definition");

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 10;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		form.getBody().setLayout(gridLayout);

		form.setBackgroundImage(WorkflowReportPlugin.getPlugin().getIcon(
				"editor/form_banner.gif").createImage());

		createXSLSection(managedForm);

	}

	/**
	 * This Section supports 3 input fields for xsl and contenttype
	 * 
	 * @param mform
	 */
	private void createXSLSection(IManagedForm mform) {

		GridData gdSection = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		gdSection.widthHint = 250;
		gdSection.verticalSpan = 1;
		clientXSL = createSection(
				mform,
				"XSL Template",
				"This section defines an optional XSL Template for XSL Transformation during a Report generation. Also optional Contenttype and Encoding can be defined for a Report here.",

				Section.TITLE_BAR | Section.DESCRIPTION, 4, gdSection,
				WorkflowReportPlugin.getPlugin().getIcon("editor/contents.gif"));

		createFormInput(clientXSL, toolkit, "Content Type:", "txtContentType",
				report);

		createFormInput(clientXSL, toolkit, "Encoding:", "txtEncoding", report);

		GridData gd = new GridData();
		gd.heightHint = 380;
		gd.horizontalSpan = 4;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;

		gd.verticalAlignment = GridData.FILL;
		gd.horizontalAlignment = GridData.FILL;

		/*** Query ***/
		createTextAreaInput(clientXSL, toolkit, gd, "Template:", "txtXSL",
				report);

	}

	private void addXMLEditor() {

		//org.eclipse.wst.sse.ui.StructuredTextEditor s1;
		
		// open File in StructuredTextEditor
		//StructuredTextEditor editor = new StructuredTextEditor();
		
		/*
		FileEditorInput fileEditorInput = new FileEditorInput(fileToOpen);
		index = addPage(editor, fileEditorInput);
		setPageText(index, "Fatwire Java Editor"); 
		*/
		
		/*
		 * and when you pass in the IEditorSite for the editor, make sure the
		 * site you pass in has an id of ContentTypeIdForXML.ContentTypeID_XML +
		 * ".source" (see XMLMultiPageEditorPart#createSite for example)
		 */
		// org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML
		// org.eclipse.wst.xml.ui.StructuredTextEditorXML s;

	}

}
