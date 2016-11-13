package org.imixs.report.editors;

import java.beans.PropertyChangeEvent;

import javax.swing.event.HyperlinkEvent.EventType;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.imixs.report.ImixsReportPlugin;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

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

	/**
	 * Creats the editor sections
	 * 
	 * 
	 * Query Statement txtquery Name txtname
	 * 
	 * txtcontenttype txtencoding txtxsl Attributes txtattributelist
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
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		sectionClient.setLayout(glayout);

		toolkit.createLabel(sectionClient, "Name:");
		 Text text = toolkit.createText(sectionClient, report.getStringValue("txtname"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("txtname", ((Text) event.widget).getText()));

		
			
		Composite seppi = toolkit.createCompositeSeparator(sectionClient);
		GridData gds = new GridData();
		gds.heightHint = 2;
		gds.horizontalSpan=2;
		gds.grabExcessHorizontalSpace = true;
		gds.verticalAlignment = GridData.BEGINNING;
		gds.horizontalAlignment = GridData.FILL;
	seppi.setLayoutData(gds);
		
		
		toolkit.createLabel(sectionClient, "XSL:");
	
//		Composite client = toolkit.createComposite(sectionClient,SWT.BORDER);
//		toolkit.createLabel(sectionClient, "A:");
//		toolkit.createLabel(sectionClient, "B:");

		
		createResourceSelectionControl(sectionClient,"xsl");
		
		//ResourceSelectionWidget rsw=new ResourceSelectionWidget(sectionClient, SWT.NONE, toolkit, report, project);
		
		
		//	ResourceSelectionWidget rsw=new ResourceSelectionWidget(sectionClient, SWT.NONE, toolkit, report, project);
	/*	

		toolkit.createLabel(sectionClient, "XSL:");
		final Text xsltext = toolkit.createText(sectionClient, report.getStringValue("txtxsl"), SWT.BORDER);
		xsltext.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		xsltext.addModifyListener(event -> report.setItemValue("txtxsl", ((Text) event.widget).getText()));
		Button button = toolkit.createButton(sectionClient, "Browse", SWT.NONE);
		final Shell shell = parent.getShell();
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				
				IFile result = selectResource(shell, "Select XSL Resource", "xsl");
				if (result!=null) {
					
					System.out.println(result.getName());
					System.out.println(result.getLocation());
					xsltext.setText(result.getFullPath().toString());
					//report.setItemValue("txtxsl",result.getFullPath().toString());
				}
			}
		});
		
		*/
		
		
		toolkit.createLabel(sectionClient, "Encoding:");
		text = toolkit.createText(sectionClient, report.getStringValue("txtencoding"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("txtencoding", ((Text) event.widget).getText()));

		toolkit.createLabel(sectionClient, "Content Type:");
		text = toolkit.createText(sectionClient, report.getStringValue("txtcontenttype"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("txtcontenttype", ((Text) event.widget).getText()));

		

		section.setClient(sectionClient);

		/*******************************************************
		 * 
		 * Query Section
		 */

		section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.colspan = 2;
		section.setLayoutData(td);

		section.setText("Query");
		// section.setDescription("This is the 2nd description that goes below
		// the title");
		sectionClient = toolkit.createComposite(section);
		createSectionIcon(section, "query.gif");

		glayout = new GridLayout();
		glayout.numColumns = 1;
		sectionClient.setLayout(glayout);

		text = toolkit.createText(sectionClient, report.getStringValue("txtquery"),
				SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		text.addModifyListener(event -> report.setItemValue("txtquery", ((Text) event.widget).getText()));

		GridData gd = new GridData();
		gd.heightHint = 135;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = GridData.FILL;
		text.setLayoutData(gd);

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

		toolkit.createLabel(sectionClient, "Summary3:");
		text = toolkit.createText(sectionClient, report.getStringValue("summary3"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("summary3", ((Text) event.widget).getText()));

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
	
	
	
	
	
	
	public void createResourceSelectionControl(Composite parent,String fileExtension) {

		Composite client = toolkit.createComposite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.numColumns = 2;
		client.setLayout(layout);
		client.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		final Text xsltext = toolkit.createText(client, report.getStringValue("txtxsl"), SWT.BORDER);
		xsltext.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		xsltext.addModifyListener(event -> report.setItemValue("txtxsl", ((Text) event.widget).getText()));
		Button button = toolkit.createButton(client, "Browse", SWT.NONE);
		final Shell shell = parent.getShell();
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {

				IFile result = ImixsReportPlugin.selectResource(shell, project, "Select XSL Resource", fileExtension);
				if (result != null) {

					System.out.println(result.getName());
					System.out.println(result.getLocation());
					xsltext.setText(result.getFullPath().toString());
					// report.setItemValue("txtxsl",result.getFullPath().toString());
				}
			}
		});
		
		
	}

}
