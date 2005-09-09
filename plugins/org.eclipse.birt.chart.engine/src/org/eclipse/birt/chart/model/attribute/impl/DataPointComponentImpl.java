/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Data Point Component</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl#getFormatSpecifier <em>Format Specifier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataPointComponentImpl extends EObjectImpl implements
		DataPointComponent
{

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final DataPointComponentType TYPE_EDEFAULT = DataPointComponentType.BASE_VALUE_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected DataPointComponentType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet = false;

	/**
	 * The cached value of the '{@link #getFormatSpecifier() <em>Format Specifier</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFormatSpecifier()
	 * @generated
	 * @ordered
	 */
	protected FormatSpecifier formatSpecifier = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DataPointComponentImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getDataPointComponent( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataPointComponentType getType( )
	{
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setType( DataPointComponentType newType )
	{
		DataPointComponentType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATA_POINT_COMPONENT__TYPE,
					oldType,
					type,
					!oldTypeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetType( )
	{
		DataPointComponentType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.DATA_POINT_COMPONENT__TYPE,
					oldType,
					TYPE_EDEFAULT,
					oldTypeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetType( )
	{
		return typeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public FormatSpecifier getFormatSpecifier( )
	{
		return formatSpecifier;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFormatSpecifier(
			FormatSpecifier newFormatSpecifier, NotificationChain msgs )
	{
		FormatSpecifier oldFormatSpecifier = formatSpecifier;
		formatSpecifier = newFormatSpecifier;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER,
					oldFormatSpecifier,
					newFormatSpecifier );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setFormatSpecifier( FormatSpecifier newFormatSpecifier )
	{
		if ( newFormatSpecifier != formatSpecifier )
		{
			NotificationChain msgs = null;
			if ( formatSpecifier != null )
				msgs = ( (InternalEObject) formatSpecifier ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER,
						null,
						msgs );
			if ( newFormatSpecifier != null )
				msgs = ( (InternalEObject) newFormatSpecifier ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER,
						null,
						msgs );
			msgs = basicSetFormatSpecifier( newFormatSpecifier, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER,
					newFormatSpecifier,
					newFormatSpecifier ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, Class baseClass, NotificationChain msgs )
	{
		if ( featureID >= 0 )
		{
			switch ( eDerivedStructuralFeatureID( featureID, baseClass ) )
			{
				case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER :
					return basicSetFormatSpecifier( null, msgs );
				default :
					return eDynamicInverseRemove( otherEnd,
							featureID,
							baseClass,
							msgs );
			}
		}
		return eBasicSetContainer( null, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.DATA_POINT_COMPONENT__TYPE :
				return getType( );
			case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER :
				return getFormatSpecifier( );
		}
		return eDynamicGet( eFeature, resolve );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet( EStructuralFeature eFeature, Object newValue )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.DATA_POINT_COMPONENT__TYPE :
				setType( (DataPointComponentType) newValue );
				return;
			case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER :
				setFormatSpecifier( (FormatSpecifier) newValue );
				return;
		}
		eDynamicSet( eFeature, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.DATA_POINT_COMPONENT__TYPE :
				unsetType( );
				return;
			case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER :
				setFormatSpecifier( (FormatSpecifier) null );
				return;
		}
		eDynamicUnset( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.DATA_POINT_COMPONENT__TYPE :
				return isSetType( );
			case AttributePackage.DATA_POINT_COMPONENT__FORMAT_SPECIFIER :
				return formatSpecifier != null;
		}
		return eDynamicIsSet( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (type: " ); //$NON-NLS-1$
		if ( typeESet )
			result.append( type );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience methods provided to create an initialized
	 * DataPointComponent instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @param dpct
	 * @param fs
	 * @return
	 */
	public static final DataPointComponent create( DataPointComponentType dpct,
			FormatSpecifier fs )
	{
		final DataPointComponent dpc = AttributeFactory.eINSTANCE.createDataPointComponent( );
		dpc.setFormatSpecifier( fs );
		dpc.setType( dpct );
		return dpc;
	}

} // DataPointComponentImpl
