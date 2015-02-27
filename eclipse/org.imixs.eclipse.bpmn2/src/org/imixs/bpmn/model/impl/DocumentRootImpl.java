/**
 */
package org.imixs.bpmn.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.imixs.bpmn.model.DocumentRoot;
import org.imixs.bpmn.model.ModelPackage;
import org.imixs.bpmn.model.TaskConfig;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.imixs.bpmn.model.impl.DocumentRootImpl#getTaskConfig <em>Task Config</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends MinimalEObjectImpl.Container implements DocumentRoot {
	/**
	 * The cached value of the '{@link #getTaskConfig() <em>Task Config</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaskConfig()
	 * @generated
	 * @ordered
	 */
	protected TaskConfig taskConfig;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TaskConfig getTaskConfig() {
		return taskConfig;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTaskConfig(TaskConfig newTaskConfig, NotificationChain msgs) {
		TaskConfig oldTaskConfig = taskConfig;
		taskConfig = newTaskConfig;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__TASK_CONFIG, oldTaskConfig, newTaskConfig);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaskConfig(TaskConfig newTaskConfig) {
		if (newTaskConfig != taskConfig) {
			NotificationChain msgs = null;
			if (taskConfig != null)
				msgs = ((InternalEObject)taskConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DOCUMENT_ROOT__TASK_CONFIG, null, msgs);
			if (newTaskConfig != null)
				msgs = ((InternalEObject)newTaskConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DOCUMENT_ROOT__TASK_CONFIG, null, msgs);
			msgs = basicSetTaskConfig(newTaskConfig, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DOCUMENT_ROOT__TASK_CONFIG, newTaskConfig, newTaskConfig));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.DOCUMENT_ROOT__TASK_CONFIG:
				return basicSetTaskConfig(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.DOCUMENT_ROOT__TASK_CONFIG:
				return getTaskConfig();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.DOCUMENT_ROOT__TASK_CONFIG:
				setTaskConfig((TaskConfig)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.DOCUMENT_ROOT__TASK_CONFIG:
				setTaskConfig((TaskConfig)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.DOCUMENT_ROOT__TASK_CONFIG:
				return taskConfig != null;
		}
		return super.eIsSet(featureID);
	}

} //DocumentRootImpl
