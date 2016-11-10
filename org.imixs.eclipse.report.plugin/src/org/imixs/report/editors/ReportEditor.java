package org.imixs.report.editors;

import java.beans.PropertyChangeEvent;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ReportEditor extends EditorPart {

	public static final String ID = "org.imixs.report.editors.reporteditor";

	private boolean isdirty = false;
	private Report todo;
	private ReportEditorInput input;

	// Will be called before createPartControl
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		// TODO the report object need to be created from the input file object
		todo = new Report(42);

		this.input = new ReportEditorInput(todo.getId());

		setSite(site);
		setInput(this.input);
		setPartName("Todo ID: " + todo.getId());

		todo.addPropertyChangeListener((PropertyChangeEvent event) -> {
			isdirty = true;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		});
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		new Label(parent, SWT.NONE).setText("Summary");
		Text text = new Text(parent, SWT.BORDER);
		text.setText(todo.getItem("summary"));
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		text.addModifyListener(event -> todo.setItem("summary", ((Text) event.widget).getText()));

		new Label(parent, SWT.NONE).setText("Description");
		Text lastName = new Text(parent, SWT.BORDER);
		lastName.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		lastName.setText(todo.getItem("description"));

		lastName.addModifyListener(event -> todo.setItem("description", ((Text) event.widget).getText()));

	}

	@Override
	public void doSave(IProgressMonitor monitor) {
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
