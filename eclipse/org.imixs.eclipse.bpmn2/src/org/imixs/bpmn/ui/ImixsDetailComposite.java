package org.imixs.bpmn.ui;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.Bpmn2Factory;
import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.bpmn2.ExtensionAttributeValue;
import org.eclipse.bpmn2.modeler.core.adapters.InsertionAdapter;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractBpmn2PropertySection;
import org.eclipse.bpmn2.modeler.core.merrimac.clad.AbstractDetailComposite;
import org.eclipse.swt.widgets.Composite;
import org.imixs.bpmn.ImixsBPMNPlugin;
import org.imixs.bpmn.model.ModelFactory;
import org.imixs.bpmn.model.Property;

public abstract class ImixsDetailComposite extends AbstractDetailComposite {
	
	public ImixsDetailComposite(AbstractBpmn2PropertySection section) {
		super(section);
	}

	public ImixsDetailComposite(Composite parent, int style) {
		super(parent, style);
	}

	
	
	
}
