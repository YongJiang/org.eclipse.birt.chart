/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.component.ComponentPackage;

import org.eclipse.birt.chart.model.data.DataSet;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EString To Data Set Map Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.EStringToDataSetMapEntryImpl#getTypedKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.component.impl.EStringToDataSetMapEntryImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EStringToDataSetMapEntryImpl extends EObjectImpl implements
		BasicEMap.Entry
{

	/**
	 * The default value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected DataSet value = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EStringToDataSetMapEntryImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return ComponentPackage.Literals.ESTRING_TO_DATA_SET_MAP_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTypedKey( )
	{
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypedKey( String newKey )
	{
		String oldKey = key;
		key = newKey;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__KEY,
					oldKey,
					key ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSet getTypedValue( )
	{
		if ( value != null && value.eIsProxy( ) )
		{
			InternalEObject oldValue = (InternalEObject) value;
			value = (DataSet) eResolveProxy( oldValue );
			if ( value != oldValue )
			{
				if ( eNotificationRequired( ) )
					eNotify( new ENotificationImpl( this,
							Notification.RESOLVE,
							ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE,
							oldValue,
							value ) );
			}
		}
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSet basicGetTypedValue( )
	{
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypedValue( DataSet newValue )
	{
		DataSet oldValue = value;
		value = newValue;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE,
					oldValue,
					value ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__KEY :
				return getTypedKey( );
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE :
				if ( resolve )
					return getTypedValue( );
				return basicGetTypedValue( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__KEY :
				setTypedKey( (String) newValue );
				return;
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE :
				setTypedValue( (DataSet) newValue );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__KEY :
				setTypedKey( KEY_EDEFAULT );
				return;
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE :
				setTypedValue( (DataSet) null );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__KEY :
				return KEY_EDEFAULT == null ? key != null
						: !KEY_EDEFAULT.equals( key );
			case ComponentPackage.ESTRING_TO_DATA_SET_MAP_ENTRY__VALUE :
				return value != null;
		}
		return super.eIsSet( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (key: " ); //$NON-NLS-1$
		result.append( key );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected int hash = -1;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHash( )
	{
		if ( hash == -1 )
		{
			Object theKey = getKey( );
			hash = ( theKey == null ? 0 : theKey.hashCode( ) );
		}
		return hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHash( int hash )
	{
		this.hash = hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getKey( )
	{
		return getTypedKey( );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey( Object key )
	{
		setTypedKey( (String) key );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getValue( )
	{
		return getTypedValue( );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object setValue( Object value )
	{
		Object oldValue = getValue( );
		setTypedValue( (DataSet) value );
		return oldValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getEMap( )
	{
		EObject container = eContainer( );
		return container == null ? null
				: (EMap) container.eGet( eContainmentFeature( ) );
	}

} //EStringToDataSetMapEntryImpl
