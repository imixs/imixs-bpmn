/**
 */
package org.imixs.bpmn.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.imixs.bpmn.model.DocumentRoot#getTaskConfig <em>Task Config</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.imixs.bpmn.model.ModelPackage#getDocumentRoot()
 * @model annotation="http://org.eclipse/emf/ecore/util/ExtendedMetaData name='null' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Task Config</b></em>' containment reference list.
	 * The list contents are of type {@link org.imixs.bpmn.model.TaskConfig}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Config</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Config</em>' containment reference list.
	 * @see org.imixs.bpmn.model.ModelPackage#getDocumentRoot_TaskConfig()
	 * @model containment="true"
	 *        annotation="http://org.eclipse/emf/ecore/util/ExtendedMetaData name='taskConfig' kind='element' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<TaskConfig> getTaskConfig();

} // DocumentRoot
