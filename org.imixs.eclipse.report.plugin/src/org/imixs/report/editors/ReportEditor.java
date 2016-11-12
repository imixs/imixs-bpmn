package org.imixs.report.editors;

import java.beans.PropertyChangeEvent;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.imixs.report.ImixsReportPlugin;
import org.imixs.workflow.ItemCollection;

public class ReportEditor extends EditorPart {

	public static final String ID = "org.imixs.report.editors.reporteditor";

	private FormToolkit toolkit;
	private ScrolledForm form;
	private boolean isdirty = false;
	private Report report;
	private ReportEditorInput input;
	
	private IEditorInput originEditorInput; 

	// Will be called before createPartControl
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		originEditorInput=input;
		
		// TODO the report object need to be created from the input file object
		report = new Report(42);

		this.input = new ReportEditorInput(report.getId());

		setSite(site);
		setInput(this.input);
		setPartName("Todo ID: " + report.getId());

		report.addPropertyChangeListener((PropertyChangeEvent event) -> {
			isdirty = true;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText("Imixs-Report Definition");
//		form.setBackgroundImage(ImixsReportPlugin.getImageDescriptor("icons/form_banner.gif").createImage());

		form.setBackgroundImage(ImixsReportPlugin.getDefault().getIcon("form_banner.gif").createImage());
		form.setImage(ImixsReportPlugin.getDefault().getIcon("report-definition.gif").createImage());
		
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);

		Section section = toolkit.createSection(form.getBody(),
				Section.DESCRIPTION | Section.TITLE_BAR |  Section.EXPANDED);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		//td.colspan = 2;
		section.setLayoutData(td);
		
	
		section.setText("General Information");
		section.setDescription("This is the description that goes  below the title");
		Composite sectionClient = toolkit.createComposite(section);
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		sectionClient.setLayout(glayout);

		toolkit.createLabel(sectionClient, "Summary:");
		Text text = toolkit.createText(sectionClient, report.getStringValue("summary"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("summary", ((Text) event.widget).getText()));

		toolkit.createLabel(sectionClient, "Description:");
		text = toolkit.createText(sectionClient, report.getStringValue("description"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("description", ((Text) event.widget).getText()));

		section.setClient(sectionClient);

		
		
		
		
		
		
		
		 section = toolkit.createSection(form.getBody(),Section.DESCRIPTION | Section.TITLE_BAR | Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		//td.colspan = 2;
		section.setLayoutData(td);
		
		section.setText("Section2 title");
		section.setDescription("This is the 2nd description that goes  below the title");
		 sectionClient = toolkit.createComposite(section);
		 glayout = new GridLayout();
		glayout.numColumns = 2;
		sectionClient.setLayout(glayout);
		
		toolkit.createLabel(sectionClient, "Summary2:");
		text = toolkit.createText(sectionClient, report.getStringValue("summary2"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("summary2", ((Text) event.widget).getText()));

		section.setClient(sectionClient);

		
		
		
		
		
		
		
		// Supersection
		
		
		 section = toolkit.createSection(form.getBody(),Section.DESCRIPTION | Section.TITLE_BAR |  Section.EXPANDED);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		section.setLayoutData(td);
		
		section.setText("Super Section3 title");
		section.setDescription("This is the 2nd description that goes  below the title");
		 sectionClient = toolkit.createComposite(section);
		 glayout = new GridLayout();
		glayout.numColumns = 2;
		sectionClient.setLayout(glayout);
		
		toolkit.createLabel(sectionClient, "Summary3:");
		text = toolkit.createText(sectionClient, report.getStringValue("summary3"), SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> report.setItemValue("summary3", ((Text) event.widget).getText()));

		section.setClient(sectionClient);

		
		
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		
		ImixsReportPlugin.getDefault().saveReport(originEditorInput,report,monitor);
    	firePropertyChange(IEditorPart.PROP_DIRTY);
    	
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return isdirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
	}

}
