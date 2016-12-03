package org.imixs.report.editors;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;

/**
 * AttributesViewer provides a widget showing the attribute list of a report.
 * Attributes can be added modified or removed.
 * 
 * 
 * @see http://www.vogella.com/tutorials/EclipseJFaceTable/article.html#jface-table-viewer
 * 
 * @author rsoika
 *
 */
public class AttributeView {

	// private FormToolkit toolkit;
	private Composite containerComposite;
	private TableViewer tableViewer;
	// private Report report;
	private ReportEditor reportEditor;

	public void create(Composite parent, ReportEditor editor) {
		reportEditor = editor;
		// this.toolkit = _toolkit;
		// this.report = _report;

		// create container composite
		containerComposite = new Composite(parent, SWT.NONE);
		containerComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		containerComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		GridLayout gridlayout = new GridLayout();
		gridlayout.numColumns = 2;
		containerComposite.setLayout(gridlayout);

		// create table viewer
		tableViewer = new TableViewer(containerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		// create for each element in an attribute row a editable column
		createEditableColumn("Item", 150, 0);
		createEditableColumn("Label", 150, 1);
		createEditableColumn("convert", 100, 2);
		createEditableColumn("format", 300, 3);
		createEditableColumn("aggregate", 50, 4);

		GridData tableLayoutData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		tableLayoutData.heightHint = 160;
		tableViewer.getTable().setLayoutData(tableLayoutData);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		// set the content provider
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		// provide the input to the viewer
		tableViewer.setInput(reportEditor.getReport().getAttributeList());

		// create Button composite
		Composite compositeButtons = reportEditor.getToolkit().createComposite(containerComposite, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		FillLayout fillLayoutButtons = new FillLayout();
		fillLayoutButtons.spacing = 2;
		fillLayoutButtons.type = SWT.VERTICAL;
		compositeButtons.setLayout(fillLayoutButtons);
		
		Button button = reportEditor.getToolkit().createButton(compositeButtons, "Add...", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setInput(reportEditor.getReport().addAttribute());
				tableViewer.refresh();
			}
		});

		// Remove Button
		button = reportEditor.getToolkit().createButton(compositeButtons, "Remove", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = tableViewer.getTable().getSelectionIndex();
				reportEditor.getReport().removeAttribute(index);
				tableViewer.refresh();
			}
		});

		// Move Up Button
		button = reportEditor.getToolkit().createButton(compositeButtons, "Up", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = tableViewer.getTable().getSelectionIndex();
				reportEditor.getReport().moveAttributeUp(index);
				tableViewer.refresh();
			}
		});

		// Move Down Button
		button = reportEditor.getToolkit().createButton(compositeButtons, "Down", SWT.PUSH);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = tableViewer.getTable().getSelectionIndex();
				reportEditor.getReport().moveAttributeDown(index);
				tableViewer.refresh();
			}
		});

	}

	/**
	 * This method add a new editable column to the tableViewer. The index
	 * indicates which element from the current row should be updated. In case a
	 * value is updated, the method fires a PropertyChange event, so that the
	 * isDirty flag will be updated.
	 * 
	 * @param title
	 *            - column header
	 * @param width
	 *            - default width
	 * @param index
	 *            - index in row
	 */
	private void createEditableColumn(String title, int width, final int index) {
		TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(width);
		column.setResizable(true);
		column.setMoveable(true);
		viewerColumn.setEditingSupport(new EditingSupport(tableViewer) {

			@SuppressWarnings("unchecked")
			@Override
			protected void setValue(Object element, Object value) {
				List<String> row = (List<String>) element;
				Object oldValue = row.get(index);
				row.set(index, value.toString());
				tableViewer.refresh();
				reportEditor.getReport().firePropertyChange("attributes." + index, oldValue, value);

			}

			@SuppressWarnings("unchecked")
			@Override
			protected Object getValue(Object element) {
				List<String> row = (List<String>) element;
				return row.get(index);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tableViewer.getTable());
			}

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@SuppressWarnings("unchecked")
			@Override
			public String getText(Object element) {
				List<String> row = (List<String>) element;
				return row.get(index);
			}
		});
	}
}
