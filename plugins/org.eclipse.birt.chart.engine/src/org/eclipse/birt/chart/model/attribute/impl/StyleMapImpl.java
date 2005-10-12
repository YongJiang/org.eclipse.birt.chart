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
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Style Map</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl#getComponentName <em>Component Name</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StyleMapImpl extends EObjectImpl implements StyleMap
{

	/**
	 * The default value of the '
	 * {@link #getComponentName() <em>Component Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected static final StyledComponent COMPONENT_NAME_EDEFAULT = StyledComponent.CHART_ALL_LITERAL;

	/**
	 * The cached value of the '
	 * {@link #getComponentName() <em>Component Name</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected StyledComponent componentName = COMPONENT_NAME_EDEFAULT;

	/**
	 * This is true if the Component Name attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean componentNameESet = false;

	/**
	 * The cached value of the '{@link #getStyle() <em>Style</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getStyle()
	 * @generated
	 * @ordered
	 */
	protected Style style = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected StyleMapImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getStyleMap( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public StyledComponent getComponentName( )
	{
		return componentName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setComponentName( StyledComponent newComponentName )
	{
		StyledComponent oldComponentName = componentName;
		componentName = newComponentName == null ? COMPONENT_NAME_EDEFAULT
				: newComponentName;
		boolean oldComponentNameESet = componentNameESet;
		componentNameESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.STYLE_MAP__COMPONENT_NAME,
					oldComponentName,
					componentName,
					!oldComponentNameESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetComponentName( )
	{
		StyledComponent oldComponentName = componentName;
		boolean oldComponentNameESet = componentNameESet;
		componentName = COMPONENT_NAME_EDEFAULT;
		componentNameESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.STYLE_MAP__COMPONENT_NAME,
					oldComponentName,
					COMPONENT_NAME_EDEFAULT,
					oldComponentNameESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetComponentName( )
	{
		return componentNameESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Style getStyle( )
	{
		return style;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetStyle( Style newStyle,
			NotificationChain msgs )
	{
		Style oldStyle = style;
		style = newStyle;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.STYLE_MAP__STYLE,
					oldStyle,
					newStyle );
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
	public void setStyle( Style newStyle )
	{
		if ( newStyle != style )
		{
			NotificationChain msgs = null;
			if ( style != null )
				msgs = ( (InternalEObject) style ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- AttributePackage.STYLE_MAP__STYLE,
						null,
						msgs );
			if ( newStyle != null )
				msgs = ( (InternalEObject) newStyle ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- AttributePackage.STYLE_MAP__STYLE,
						null,
						msgs );
			msgs = basicSetStyle( newStyle, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.STYLE_MAP__STYLE,
					newStyle,
					newStyle ) );
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
				case AttributePackage.STYLE_MAP__STYLE :
					return basicSetStyle( null, msgs );
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
			case AttributePackage.STYLE_MAP__COMPONENT_NAME :
				return getComponentName( );
			case AttributePackage.STYLE_MAP__STYLE :
				return getStyle( );
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
			case AttributePackage.STYLE_MAP__COMPONENT_NAME :
				setComponentName( (StyledComponent) newValue );
				return;
			case AttributePackage.STYLE_MAP__STYLE :
				setStyle( (Style) newValue );
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
			case AttributePackage.STYLE_MAP__COMPONENT_NAME :
				unsetComponentName( );
				return;
			case AttributePackage.STYLE_MAP__STYLE :
				setStyle( (Style) null );
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
			case AttributePackage.STYLE_MAP__COMPONENT_NAME :
				return isSetComponentName( );
			case AttributePackage.STYLE_MAP__STYLE :
				return style != null;
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
		result.append( " (componentName: " ); //$NON-NLS-1$
		if ( componentNameESet )
			result.append( componentName );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * @param name
	 * @param style
	 * @return
	 */
	public static StyleMap create( StyledComponent name, Style style )
	{
		StyleMap sm = AttributeFactory.eINSTANCE.createStyleMap( );
		sm.setComponentName( name );
		sm.setStyle( style );
		return sm;
	}

} // StyleMapImpl
