package org.imixs.report.editors;



import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Attributes TableViewer
 * 
 * 
 * @see http://www.vogella.com/tutorials/EclipseJFaceTable/article.html#jface-table-viewer
 * 
 * @author rsoika
 *
 */
public class AttributesTableView {

	private TableViewer tableViewer;

	// @PostConstruct
	public void create(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.BORDER );
		
		 createColumns(parent, tableViewer);
		 
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		
		// set the content provider
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		
		// provide the input to the viewer
		// setInput() calls getElements() on the
		// content provider instance
		
		List<List<String>> attribs=new ArrayList();
		
		
		List<String> zeile1=new ArrayList();
		zeile1.add("seppi");
		zeile1.add("deppi");
		zeile1.add("");zeile1.add("");
		
		
		List<String> zeile2=new ArrayList();
		zeile2.add("txtWorkflowGroup");
		zeile2.add("Workflow");
		zeile2.add("integer");
		zeile2.add("sum");
		
		attribs.add(zeile1);
		attribs.add(zeile2);
		
		tableViewer.setInput(attribs);
	}

	// create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Item", "Label", "Converter", "Aggregator" };
		int[] bounds = { 100, 100, 100, 100 };

		// first column is for the first name
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				List<String> zeile = (List)element;
				return zeile.get(0);
			}
		});

		// second column is for the last name
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				List<String> zeile = (List)element;
				return zeile.get(1);
			}
		});
		
		// third column is for the last name
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				List<String> zeile = (List)element;
				return zeile.get(2);
			}
		});

		
		// third column is for the last name
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				List<String> zeile = (List)element;
				return zeile.get(3);
			}
		});

		

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

}
