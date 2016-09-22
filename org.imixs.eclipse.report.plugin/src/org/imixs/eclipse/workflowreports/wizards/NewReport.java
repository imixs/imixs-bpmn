package org.imixs.eclipse.workflowreports.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.imixs.eclipse.workflowreports.WorkflowReportPlugin;
import org.imixs.eclipse.workflowreports.model.ReportObject;

/**
 * Wizard Page to create a new Model inside a model Project
 * 
 * @author Ralph Soika
 */
public class NewReport extends Wizard implements INewWizard {
	private WizardNewFileCreationPage filePage;
	private IWorkbench workbench;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench aworkbench, IStructuredSelection aselection) {
		workbench = aworkbench;
		setWindowTitle("Imixs Workflow Report Wizard");
		setDefaultPageImageDescriptor(WorkflowReportPlugin.getPlugin().getIcon(
				"wizard/wizard.gif"));
		setNeedsProgressMonitor(false);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		String sFileName = filePage.getFileName();
		if (sFileName != null && !"".equals(sFileName)) {
			if (!sFileName.endsWith(".ixr")) {
				filePage.setFileName(filePage.getFileName() + ".ixr");
			}
			IFile file = filePage.createNewFile();
			ReportObject model = new ReportObject(sFileName);
			WorkflowReportPlugin.getPlugin().saveWorkflowModel(model,
					file, null);
		}
		return true;
		
	
	}

	public void addPages() {
		IStructuredSelection currentSelection = (IStructuredSelection) workbench
		.getActiveWorkbenchWindow().getSelectionService()
		.getSelection();

		filePage = new WizardNewFileCreationPage("Model File", currentSelection);
		addPage(filePage);
	}

}
