package org.imixs.report.editors;

import java.beans.PropertyChangeEvent;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.imixs.report.ImixsReportPlugin;

public class ReportEditor extends EditorPart {

	public static final String ID = "org.imixs.report.editors.reporteditor";

	private FormToolkit toolkit;
	private ScrolledForm form;
	private Report report;
	private ReportEditorInput reportEditorInput;
	private static IProject project;

	/**
	 * This method will be called before createPartControl
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// Create an instance of the ReportEditorInput from the given FileInpurt
		this.reportEditorInput = new ReportEditorInput(input);
		// load Report Model Object
		this.report = reportEditorInput.getReport();

		IFile file = input.getAdapter(IFile.class);
		project = file.getProject();

		setSite(site);
		setInput(this.reportEditorInput);
		setPartName(reportEditorInput.getName());

		report.addPropertyChangeListener((PropertyChangeEvent event) -> {
			// isdirty = true;
			this.reportEditorInput.setDirtyFlag();
			firePropertyChange(IEditorPart.PROP_DIRTY);
		});

	}

	public FormToolkit getToolkit() {
		return toolkit;
	}

	public Report getReport() {
		return report;
	}

	/**
	 * Creats the editor sections
	 * 
	 * 
	 * Query Statement query Name name
	 * 
	 * contenttype encoding xsl 
	 * 
	 * Attributes attributelist
	 * 
	 * XSL
	 * 
	 */
	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Imixs-Report Definition");
		// form.setBackgroundImage(ImixsReportPlugin.getImageDescriptor("icons/form_banner.gif").createImage());

		form.setBackgroundImage(ImixsReportPlugin.getDefault().getIcon("form_banner.gif").createImage());
		form.setImage(ImixsReportPlugin.getDefault().getIcon("report-definition.gif").createImage());

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);

		/*******************************************************
		 * 
		 * General Section
		 */

		Section section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.colspan = 2;
		section.setLayoutData(td);

		section.setText("General Information");
		// section.setDescription("This is the description that goes below the
		// title");
		Composite sectionClient = toolkit.createComposite(section);
		// createSectionIcon(section, "query.gif");
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		sectionClient.setLayout(glayout);

		toolkit.createLabel(sectionClient, "Name:");
		Text text = toolkit.createText(sectionClient, report.getStringValue("txtname"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("txtname", ((Text) event.widget).getText()));

		Label description = toolkit.createLabel(sectionClient, "Specify the search term to query the result-set:");
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		description.setLayoutData(gd);

		// Query
		text = toolkit.createText(sectionClient, report.getStringValue("txtquery"),
				SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		text.addModifyListener(event -> report.setItemValue("txtquery", ((Text) event.widget).getText()));

		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.heightHint = 135;
		gd.widthHint = 350;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = GridData.FILL;
		text.setLayoutData(gd);

		section.setClient(sectionClient);

		/*******************************************************
		 * 
		 * XSL Section
		 */

		section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		section.setLayoutData(td);

		section.setText("XSL");
		section.setDescription("A report may define a XSL template to transform the output.");
		sectionClient = toolkit.createComposite(section);
		createSectionIcon(section, "xslt.gif");

		glayout = new GridLayout();
		glayout.numColumns = 2;
		sectionClient.setLayout(glayout);

		toolkit.createLabel(sectionClient, "XSL:");
		createResourceSelectionControl(sectionClient, "xsl");

		toolkit.createLabel(sectionClient, "Encoding:");
		text = toolkit.createText(sectionClient, report.getStringValue("encoding"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("encoding", ((Text) event.widget).getText()));

		toolkit.createLabel(sectionClient, "Content Type:");
		text = toolkit.createText(sectionClient, report.getStringValue("contenttype"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("contenttype", ((Text) event.widget).getText()));

		section.setClient(sectionClient);

		/*******************************************************
		 * 
		 * Attribute Section
		 */

		section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		section.setLayoutData(td);

		section.setText("Attributes");
		createSectionIcon(section, "attributes.gif");

		// section.setDescription("This is the 2nd description that goes below
		// the title");
		sectionClient = toolkit.createComposite(section);
		glayout = new GridLayout();
		glayout.numColumns = 1;
		sectionClient.setLayout(glayout);

		text = toolkit.createText(sectionClient, "List of attributes, converters and agregators", SWT.NONE);

		AttributeView attributesTable = new AttributeView();
		attributesTable.create(sectionClient, this);

		section.setClient(sectionClient);

		/*******************************************************
		 * 
		 * Description Section
		 */

		section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		section.setLayoutData(td);
		section.setText("Description");
		createSectionIcon(section, "description.gif");

		sectionClient = toolkit.createComposite(section);
		glayout = new GridLayout();
		glayout.numColumns = 1;
		sectionClient.setLayout(glayout);

		// description
		text = toolkit.createText(sectionClient, report.getStringValue("txtdescription"),
				SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		text.addModifyListener(event -> report.setItemValue("txtdescription", ((Text) event.widget).getText()));

		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.heightHint = 95;
		gd.widthHint = 350;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = GridData.FILL;
		text.setLayoutData(gd);

		section.setClient(sectionClient);

	}

	/**
	 * creates a secion icon on the top right corner
	 * 
	 * @param section
	 * @param icon
	 */
	private void createSectionIcon(Section section, String icon) {
		ImageHyperlink createLink = toolkit.createImageHyperlink(section, SWT.NONE);
		createLink.setImage(ImixsReportPlugin.getDefault().getIcon(icon).createImage());
		section.setTextClient(createLink);
	}

	@Override
	public boolean isDirty() {
		return reportEditorInput.isDirty();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		ImixsReportPlugin.getDefault().saveReport(reportEditorInput, report, monitor);
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSaveAs() {
		// no implementation needed
	}

	@Override
	public void setFocus() {
	}

	public void createResourceSelectionControl(Composite parent, String fileExtension) {

		Composite client = toolkit.createComposite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.numColumns = 2;
		client.setLayout(layout);
		client.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final Text xsltext = toolkit.createText(client, report.getStringValue("xslresource"), SWT.BORDER);
		xsltext.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		xsltext.addModifyListener(event -> report.setItemValue("xslresource", ((Text) event.widget).getText()));
		Button button = toolkit.createButton(client, "Browse", SWT.NONE);
		final Shell shell = parent.getShell();
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				IFile result = ImixsReportPlugin.selectResource(shell, project, "Select XSL Resource", fileExtension);
				if (result != null) {
					xsltext.setText(result.getFullPath().toString());
				}
			}
		});

	}

}
