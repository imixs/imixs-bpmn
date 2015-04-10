/**
 */
package org.imixs.bpmn.model.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.imixs.bpmn.model.Item;
import org.imixs.bpmn.model.ModelPackage;
import org.imixs.bpmn.model.Value;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.imixs.bpmn.model.impl.ValueImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.imixs.bpmn.model.impl.ValueImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValueImpl extends MinimalEObjectImpl.Container implements Value {
	/**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap mixed;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.VALUE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, ModelPackage.VALUE__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getValue() {
		if (mixed != null) {
			StringBuilder result = new StringBuilder();
			for (FeatureMap.Entry cur : mixed) {
				switch (cur.getEStructuralFeature().getFeatureID()) {
				case XMLTypePackage.XML_TYPE_DOCUMENT_ROOT__CDATA:
				case XMLTypePackage.XML_TYPE_DOCUMENT_ROOT__TEXT:
					result.append(cur.getValue());
					break;

				default:
					break;
				}
			}
			return result.toString();
		}

		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * The content of a Value EObject will be wrapped into a CDATA element,
	 * except the type of the containing Item is set to 'xs:boolean'.
	 * 
	 * A null value is not allowed and will be transformed into an empty string
	 * 
	 * @generated NOT
	 */
	public void setValue(String newValue) {
		
		// do not allow null values!
		if (newValue==null) {
			newValue="";
		}
		
		getMixed().clear();

		FeatureMap.Entry cdata = null;

		// try to get the container Item EObject to determine the type
		Item item = (Item) this.eContainer;
		if (item != null && "xs:boolean".equals(item.getType())) {
			cdata = FeatureMapUtil.createTextEntry(newValue);
		} else {
			// default will wrap the value into a CDATA entry
			cdata = FeatureMapUtil.createCDATAEntry(newValue);
		}
		getMixed().add(cdata);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.VALUE__MIXED:
			return ((InternalEList<?>) getMixed()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ModelPackage.VALUE__MIXED:
			if (coreType)
				return getMixed();
			return ((FeatureMap.Internal) getMixed()).getWrapper();
		case ModelPackage.VALUE__VALUE:
			return getValue();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ModelPackage.VALUE__MIXED:
			((FeatureMap.Internal) getMixed()).set(newValue);
			return;
		case ModelPackage.VALUE__VALUE:
			setValue((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ModelPackage.VALUE__MIXED:
			getMixed().clear();
			return;
		case ModelPackage.VALUE__VALUE:
			setValue(VALUE_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ModelPackage.VALUE__MIXED:
			return mixed != null && !mixed.isEmpty();
		case ModelPackage.VALUE__VALUE:
			return VALUE_EDEFAULT == null ? getValue() != null
					: !VALUE_EDEFAULT.equals(getValue());
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} // ValueImpl