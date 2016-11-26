package org.imixs.report.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.imixs.report.ImixsReportPlugin;
import org.imixs.workflow.ItemCollection;

/**
 * IEditorInput serves as the model for the editor and is supposed to be a
 * light-weight representation of the model. Eclipse will buffer IEditorInput
 * objects therefore this object should be relatively small.
 * 
 * The ReportEditorInput provides methods to convert the file content into a
 * Imixs ItemCollection.
 * 
 * 
 * The flag isDirty is used by the ReportEditor to controll the isDirty status.
 *
 * http://www.vogella.com/tutorials/EclipseEditors/article.html
 * https://www.eclipse.org/articles/Article-Forms/article.html
 * 
 * @author rsoika
 *
 */
public class ReportEditorInput implements IEditorInput {

	private boolean dirty = false;
	private IEditorInput fileInput;

	public ReportEditorInput(IEditorInput input) {
		fileInput = input;
	}

	/**
	 * This method loads the file content and creates an instance of a report
	 * model object. If the file contains no report data, the method creates a
	 * new empty report with default values
	 * 
	 * 
	 * @return
	 */
	public Report getReport() {
		ItemCollection itemCol = ImixsReportPlugin.getDefault().loadReportData(fileInput);
		if (itemCol == null) {
			// create a empty report with default values
			itemCol = new ItemCollection();
			itemCol.replaceItemValue("txtquery", "(type:\"workitem\")");
			setDirtyFlag();
		}
		return new Report(itemCol);
	}

	public IEditorInput getFileInput() {
		return fileInput;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clearDirtyFlag() {
		dirty = false;

	}

	public void setDirtyFlag() {
		dirty = true;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		// return String.valueOf(id);
		return this.fileInput.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Imixs-Report Definition";
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public int hashCode() {
		return this.fileInput.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (obj instanceof IEditorInput) {
			IFile file1 = ((IEditorInput) obj).getAdapter(IFile.class);
			if (file1 != null) {
				IFile fileich = this.fileInput.getAdapter(IFile.class);
				return (file1.equals(fileich));
			}
		}
		return false;

	}
}