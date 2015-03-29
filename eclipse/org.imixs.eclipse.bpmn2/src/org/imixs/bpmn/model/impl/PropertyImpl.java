/**
 */
package org.imixs.bpmn.model.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.imixs.bpmn.model.ModelPackage;
import org.imixs.bpmn.model.Property;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Property</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.imixs.bpmn.model.impl.PropertyImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.imixs.bpmn.model.impl.PropertyImpl#getName <em>Name</em>}</li>
 * <li>{@link org.imixs.bpmn.model.impl.PropertyImpl#getType <em>Type</em>}</li>
 * <li>{@link org.imixs.bpmn.model.impl.PropertyImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PropertyImpl extends MinimalEObjectImpl.Container implements
		Property {
	
	
	
	
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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

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
	protected PropertyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.PROPERTY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, ModelPackage.PROPERTY__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelPackage.PROPERTY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					ModelPackage.PROPERTY__TYPE, oldType, type));
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
	 * @generated NOT
	 */
	public void setValue(String newValue) {
		getMixed().clear();

		FeatureMap.Entry dataEntry = null;
		if (this.getType() != null && this.getType().equals("CDATA"))
			dataEntry = FeatureMapUtil.createCDATAEntry(newValue);
		else {
			// if the value is a imixs <value> list then we use RawTextEntry....
			if (newValue.startsWith("<value>") && newValue.endsWith("</value>")) {
				//dataEntry = FeatureMapUtil.createRawTextEntry(newValue);
				dataEntry = FeatureMapUtil.createCDATAEntry(newValue);
			}
			else
				dataEntry = FeatureMapUtil.createTextEntry(newValue);
		}
		getMixed().add(dataEntry);
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
		case ModelPackage.PROPERTY__MIXED:
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
		case ModelPackage.PROPERTY__MIXED:
			if (coreType)
				return getMixed();
			return ((FeatureMap.Internal) getMixed()).getWrapper();
		case ModelPackage.PROPERTY__NAME:
			return getName();
		case ModelPackage.PROPERTY__TYPE:
			return getType();
		case ModelPackage.PROPERTY__VALUE:
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
		case ModelPackage.PROPERTY__MIXED:
			((FeatureMap.Internal) getMixed()).set(newValue);
			return;
		case ModelPackage.PROPERTY__NAME:
			setName((String) newValue);
			return;
		case ModelPackage.PROPERTY__TYPE:
			setType((String) newValue);
			return;
		case ModelPackage.PROPERTY__VALUE:
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
		case ModelPackage.PROPERTY__MIXED:
			getMixed().clear();
			return;
		case ModelPackage.PROPERTY__NAME:
			setName(NAME_EDEFAULT);
			return;
		case ModelPackage.PROPERTY__TYPE:
			setType(TYPE_EDEFAULT);
			return;
		case ModelPackage.PROPERTY__VALUE:
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
		case ModelPackage.PROPERTY__MIXED:
			return mixed != null && !mixed.isEmpty();
		case ModelPackage.PROPERTY__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case ModelPackage.PROPERTY__TYPE:
			return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT
					.equals(type);
		case ModelPackage.PROPERTY__VALUE:
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
		result.append(", name: ");
		result.append(name);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} // PropertyImpl



final class XMLTypeFeatures
{
  public static final EStructuralFeature TEXT = XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Text();
  public static final EStructuralFeature CDATA = XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_CDATA();
  public static final EStructuralFeature COMMENT = XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Comment();
  public static final EStructuralFeature PROCESSING_INSTRUCTION = XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_ProcessingInstruction();
  public static final FeatureMap.Entry.Internal TEXT_PROTOTYPE = ((EStructuralFeature.Internal)TEXT).getFeatureMapEntryPrototype();
  public static final FeatureMap.Entry.Internal CDATA_PROTOTYPE = ((EStructuralFeature.Internal)CDATA).getFeatureMapEntryPrototype();
  public static final FeatureMap.Entry.Internal COMMENT_PROTOTYPE = ((EStructuralFeature.Internal)COMMENT).getFeatureMapEntryPrototype();
  public static final FeatureMap.Entry.Internal PROCESSING_INSTRUCTION_PROTOTYPE = ((EStructuralFeature.Internal)PROCESSING_INSTRUCTION).getFeatureMapEntryPrototype();

  public static final List<EStructuralFeature> TEXTUAL_FEATURES = Arrays.asList(new EStructuralFeature [] {TEXT, CDATA});
}