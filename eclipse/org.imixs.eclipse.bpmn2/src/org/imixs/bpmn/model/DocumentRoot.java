/**
 */
package org.imixs.bpmn.model;

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
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Task Config</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task Config</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task Config</em>' containment reference.
	 * @see #setTaskConfig(TaskConfig)
	 * @see org.imixs.bpmn.model.ModelPackage#getDocumentRoot_TaskConfig()
	 * @model containment="true" upper="-2"
	 *        extendedMetaData="name='taskConfig' kind='element' namespace='##targetNamespace'"
	 * @generated
	 */
	TaskConfig getTaskConfig();

	/**
	 * Sets the value of the '{@link org.imixs.bpmn.model.DocumentRoot#getTaskConfig <em>Task Config</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task Config</em>' containment reference.
	 * @see #getTaskConfig()
	 * @generated
	 */
	void setTaskConfig(TaskConfig value);

} // DocumentRoot
