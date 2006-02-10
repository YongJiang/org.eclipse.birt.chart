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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Java Date Format Specifier</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl#getPattern <em>Pattern</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JavaDateFormatSpecifierImpl extends FormatSpecifierImpl implements
		JavaDateFormatSpecifier
{

	/**
	 * The default value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected static final String PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected String pattern = PATTERN_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected JavaDateFormatSpecifierImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.JAVA_DATE_FORMAT_SPECIFIER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getPattern( )
	{
		return pattern;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setPattern( String newPattern )
	{
		String oldPattern = pattern;
		pattern = newPattern;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.JAVA_DATE_FORMAT_SPECIFIER__PATTERN,
					oldPattern,
					pattern ) );
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
			case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER__PATTERN :
				return getPattern( );
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
			case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER__PATTERN :
				setPattern( (String) newValue );
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
			case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER__PATTERN :
				setPattern( PATTERN_EDEFAULT );
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
			case AttributePackage.JAVA_DATE_FORMAT_SPECIFIER__PATTERN :
				return PATTERN_EDEFAULT == null ? pattern != null
						: !PATTERN_EDEFAULT.equals( pattern );
		}
		return super.eIsSet( featureID );
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
		result.append( " (pattern: " ); //$NON-NLS-1$
		result.append( pattern );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience methods provided to create an initialized
	 * JavaDateFormatSpecifier instance
	 * 
	 * NOTE: Manually written
	 * 
	 * @param sJavaPattern
	 * @return
	 */
	public static final JavaDateFormatSpecifier create( String sJavaPattern )
	{
		final JavaDateFormatSpecifier jdfs = AttributeFactory.eINSTANCE.createJavaDateFormatSpecifier( );
		jdfs.setPattern( sJavaPattern );
		return jdfs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#format(java.util.Calendar,
	 *      java.util.Locale)
	 */
	public String format( Calendar c, Locale lcl )
	{
		// ATTN: LOCALE IS UNUSED WHEN THE FORMAT PATTERN IS SPECIFIED
		final SimpleDateFormat sdf = new SimpleDateFormat( getPattern( ) );
		return sdf.format( c.getTime( ) );
	}
} // JavaDateFormatSpecifierImpl
