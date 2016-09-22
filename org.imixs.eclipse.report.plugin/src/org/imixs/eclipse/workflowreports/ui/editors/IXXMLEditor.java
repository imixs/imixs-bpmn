/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.imixs.eclipse.workflowreports.ui.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.imixs.eclipse.workflowreports.WorkflowReportPlugin;
import org.imixs.eclipse.workflowreports.model.ReportObject;

/**
 * A simple multi-page form editor that uses Eclipse Forms support.
 * Example plug-in is configured to create one instance of
 * form colors that is shared between multiple editor instances.
 */
public class IXXMLEditor extends FormEditor implements PropertyChangeListener {
	private ReportObject queryObject=null;
	
	/**
	 * The isDirty Flag is asked to the current workflowModel class
	 */
	public boolean isDirty() {
		
 		return queryObject.isDirty();
	}
	
	public ReportObject getQueryObject() {
		return queryObject;
	}
	
	/**
	 * initial workflowModel
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site,input);
		
		FileEditorInput fileEditorInput=(FileEditorInput)input;
		
		queryObject=WorkflowReportPlugin.getPlugin().loadQueryObject(fileEditorInput.getFile());
		
		// Auf �nderungen am Modell reagieren! damit das isDiry Flag gesetzt werden kann
		queryObject.addPropertyChangeListener(this);
		
		this.setPartName(input.getName());
		
		
		
		
		

		
		
	
	
	}	
	
	/**
	 * Disposes the workflowmodel by calling the closeModel Method of the WorkflowmodelerPlugin
	 * @see WorkflowmodelerPlugin#closeModel(WorkflowModel)
	 */
	public void dispose() {
		super.dispose();
		 // WorkflowqueryPlugin.getPlugin().closeModel(queryObject);
	}
	 
	
	/**
	 * This Mehtode add the IXEditorPages - registered by the Extension org.imixs.eclipse.workflowmodeler.ixeditorpages
	 * to the FormEditor.
	 * The Code is al litle bit crazy because the order of Extensions spread about different plugins and features
	 * is only defined be the instalation order form the user. So this method trys to put the "overview" and
	 * "profile" page in the first two solots.
	 * 
	 * @see org.imixs.workflowmodeler.ui.editors.IXEditorInput  
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	protected void addPages() {
		Hashtable hashtable=new Hashtable();
		
		
		
		
		// read Extension Point 
		IExtensionRegistry registry=Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint= registry.getExtensionPoint("org.imixs.eclipse.workflowreports.ixeditorpages");
		IExtension[] extensions = extensionPoint.getExtensions();
		//ArrayList results=new ArrayList();

		// collect all extensions - there could be more then one difinition because extensions could be spread
		// over different features.
		for (int i=0;i<extensions.length; i++) {
			
			IConfigurationElement[] elements=extensions[i].getConfigurationElements();
			for (int j=0; j<elements.length; j++ ) {
				try {
					Object tab=elements[j].createExecutableExtension("class");
					if (tab instanceof IXEditorPage) {
						IXEditorPage editorpage = (IXEditorPage)tab;
						editorpage.initialize(this);
						editorpage.initializeWorkflowModel(queryObject);
						// Name auslesen
						String sname=elements[j].getAttribute("name");
						hashtable.put(sname,editorpage);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
			
		/* now add pages to the editor and try to put "overview" and "profile" int the first
		 * two slots 
		 */
		try {
			int iPageCount=0;
			
			IXEditorPage editorpage;
			// try "Overview" and remove this page form the hashtable
			editorpage=(IXEditorPage)hashtable.get("Overview");
			if (editorpage!=null) {
				addPage(editorpage);
				setPageText(iPageCount, "Overview");
				iPageCount++;
				hashtable.remove("Overview");
			}
			// try "Profile" and remove this page form the hashtable
			editorpage=(IXEditorPage)hashtable.get("XSL");
			if (editorpage!=null) {
				addPage(editorpage);
				setPageText(iPageCount, "XSL");
				iPageCount++;
				hashtable.remove("XSL");
			}
			
			// add other pages
			Iterator it = hashtable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String sName = (String) entry.getKey();
				editorpage = (IXEditorPage) entry.getValue();
				addPage(editorpage);
				setPageText(iPageCount, sName);
				iPageCount++;
			}
			
			
			
/*
			org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart xmltest=new 	org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart();
			this.addPage(xmltest,this.getEditorInput());
	*/		
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method saves the current model. 
	 * The method also allows the registred EditorParts to do ser own doSave() Event.
	 * An EditorPart normaly should not override the doSave methode. The current Model
	 * will be saved entire by the saveWorkflowModel() Methode of the WorkflowmodelerPlugin Instance
	 */
	public void doSave(IProgressMonitor monitor) {
		// Pages sichern...
		Vector v=this.pages;
		Iterator iter=v.iterator();
		// try to save editor pages
		int i=0;
		while (iter.hasNext()) {
			try {
				EditorPart epart=(EditorPart)iter.next();
				if (epart!=null)
					epart.doSave(monitor);
				i++;
			} catch (Exception e) {
				System.out.println("[IXXMLEditor] doSave() error when saving " + i + ". EditorPage");
				//e.printStackTrace();
			}
		}
		IFile file = ((IFileEditorInput)getEditorInput()).getFile();
		WorkflowReportPlugin.getPlugin().saveWorkflowModel(queryObject,file,monitor);
    	firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
		
	/**
	 * This method is not implemented yet.
	 */
	public void doSaveAs() {
		
	}

	/**
	 * doSaveAs is not implemented yet.
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	 
	/**
	 * This method switches the model by the WorkflowmodelerPlugin method switchModel to notify listeners
	 */
	public void setFocus() {
		super.setFocus();
		//WorkflowmodelerPlugin.getPlugin().switchModel(queryObject);
	}

	
	 
	/**
	 * Diese Methode reagiert auf PropertyChange Events des WorkflowModells um das 
	 * IEditorPart.PROP_DIRTY Event abzufeuern
	 * Der Editor registriert sich dazu als PropertyChangeListener am workflowModel 
	 */	
    public void propertyChange(PropertyChangeEvent evt) {
    	firePropertyChange(IEditorPart.PROP_DIRTY);
	}	 	
	
	
	
}