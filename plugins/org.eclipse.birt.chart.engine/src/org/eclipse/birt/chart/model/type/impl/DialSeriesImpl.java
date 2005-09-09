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

package org.eclipse.birt.chart.model.type.impl;

import java.util.Collection;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Needle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialImpl;
import org.eclipse.birt.chart.model.component.impl.NeedleImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dial Series</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl#getDial <em>Dial</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.DialSeriesImpl#getNeedle <em>Needle</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DialSeriesImpl extends SeriesImpl implements DialSeries
{

	/**
	 * The cached value of the '{@link #getDial() <em>Dial</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDial()
	 * @generated
	 * @ordered
	 */
	protected Dial dial = null;

	/**
	 * The cached value of the '{@link #getNeedle() <em>Needle</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNeedle()
	 * @generated
	 * @ordered
	 */
	protected Needle needle = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DialSeriesImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return TypePackage.eINSTANCE.getDialSeries( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Dial getDial( )
	{
		return dial;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDial( Dial newDial, NotificationChain msgs )
	{
		Dial oldDial = dial;
		dial = newDial;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__DIAL,
					oldDial,
					newDial );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDial( Dial newDial )
	{
		if ( newDial != dial )
		{
			NotificationChain msgs = null;
			if ( dial != null )
				msgs = ( (InternalEObject) dial ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__DIAL,
						null,
						msgs );
			if ( newDial != null )
				msgs = ( (InternalEObject) newDial ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - TypePackage.DIAL_SERIES__DIAL,
						null,
						msgs );
			msgs = basicSetDial( newDial, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__DIAL,
					newDial,
					newDial ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Needle getNeedle( )
	{
		return needle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetNeedle( Needle newNeedle,
			NotificationChain msgs )
	{
		Needle oldNeedle = needle;
		needle = newNeedle;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__NEEDLE,
					oldNeedle,
					newNeedle );
			if ( msgs == null )
				msgs = notification;
			else
				msgs.add( notification );
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setNeedle( Needle newNeedle )
	{
		if ( newNeedle != needle )
		{
			NotificationChain msgs = null;
			if ( needle != null )
				msgs = ( (InternalEObject) needle ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.DIAL_SERIES__NEEDLE,
						null,
						msgs );
			if ( newNeedle != null )
				msgs = ( (InternalEObject) newNeedle ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.DIAL_SERIES__NEEDLE,
						null,
						msgs );
			msgs = basicSetNeedle( newNeedle, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIAL_SERIES__NEEDLE,
					newNeedle,
					newNeedle ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, Class baseClass, NotificationChain msgs )
	{
		if ( featureID >= 0 )
		{
			switch ( eDerivedStructuralFeatureID( featureID, baseClass ) )
			{
				case TypePackage.DIAL_SERIES__LABEL :
					return basicSetLabel( null, msgs );
				case TypePackage.DIAL_SERIES__DATA_DEFINITION :
					return ( (InternalEList) getDataDefinition( ) ).basicRemove( otherEnd,
							msgs );
				case TypePackage.DIAL_SERIES__DATA_POINT :
					return basicSetDataPoint( null, msgs );
				case TypePackage.DIAL_SERIES__DATA_SET :
					return basicSetDataSet( null, msgs );
				case TypePackage.DIAL_SERIES__TRIGGERS :
					return ( (InternalEList) getTriggers( ) ).basicRemove( otherEnd,
							msgs );
				case TypePackage.DIAL_SERIES__CURVE_FITTING :
					return basicSetCurveFitting( null, msgs );
				case TypePackage.DIAL_SERIES__DIAL :
					return basicSetDial( null, msgs );
				case TypePackage.DIAL_SERIES__NEEDLE :
					return basicSetNeedle( null, msgs );
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
	 * 
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case TypePackage.DIAL_SERIES__VISIBLE :
				return isVisible( ) ? Boolean.TRUE : Boolean.FALSE;
			case TypePackage.DIAL_SERIES__LABEL :
				return getLabel( );
			case TypePackage.DIAL_SERIES__DATA_DEFINITION :
				return getDataDefinition( );
			case TypePackage.DIAL_SERIES__SERIES_IDENTIFIER :
				return getSeriesIdentifier( );
			case TypePackage.DIAL_SERIES__DATA_POINT :
				return getDataPoint( );
			case TypePackage.DIAL_SERIES__DATA_SET :
				return getDataSet( );
			case TypePackage.DIAL_SERIES__LABEL_POSITION :
				return getLabelPosition( );
			case TypePackage.DIAL_SERIES__STACKED :
				return isStacked( ) ? Boolean.TRUE : Boolean.FALSE;
			case TypePackage.DIAL_SERIES__TRIGGERS :
				return getTriggers( );
			case TypePackage.DIAL_SERIES__TRANSLUCENT :
				return isTranslucent( ) ? Boolean.TRUE : Boolean.FALSE;
			case TypePackage.DIAL_SERIES__CURVE_FITTING :
				return getCurveFitting( );
			case TypePackage.DIAL_SERIES__DIAL :
				return getDial( );
			case TypePackage.DIAL_SERIES__NEEDLE :
				return getNeedle( );
		}
		return eDynamicGet( eFeature, resolve );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eSet( EStructuralFeature eFeature, Object newValue )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case TypePackage.DIAL_SERIES__VISIBLE :
				setVisible( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case TypePackage.DIAL_SERIES__LABEL :
				setLabel( (Label) newValue );
				return;
			case TypePackage.DIAL_SERIES__DATA_DEFINITION :
				getDataDefinition( ).clear( );
				getDataDefinition( ).addAll( (Collection) newValue );
				return;
			case TypePackage.DIAL_SERIES__SERIES_IDENTIFIER :
				setSeriesIdentifier( (Object) newValue );
				return;
			case TypePackage.DIAL_SERIES__DATA_POINT :
				setDataPoint( (DataPoint) newValue );
				return;
			case TypePackage.DIAL_SERIES__DATA_SET :
				setDataSet( (DataSet) newValue );
				return;
			case TypePackage.DIAL_SERIES__LABEL_POSITION :
				setLabelPosition( (Position) newValue );
				return;
			case TypePackage.DIAL_SERIES__STACKED :
				setStacked( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case TypePackage.DIAL_SERIES__TRIGGERS :
				getTriggers( ).clear( );
				getTriggers( ).addAll( (Collection) newValue );
				return;
			case TypePackage.DIAL_SERIES__TRANSLUCENT :
				setTranslucent( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case TypePackage.DIAL_SERIES__CURVE_FITTING :
				setCurveFitting( (CurveFitting) newValue );
				return;
			case TypePackage.DIAL_SERIES__DIAL :
				setDial( (Dial) newValue );
				return;
			case TypePackage.DIAL_SERIES__NEEDLE :
				setNeedle( (Needle) newValue );
				return;
		}
		eDynamicSet( eFeature, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eUnset( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case TypePackage.DIAL_SERIES__VISIBLE :
				unsetVisible( );
				return;
			case TypePackage.DIAL_SERIES__LABEL :
				setLabel( (Label) null );
				return;
			case TypePackage.DIAL_SERIES__DATA_DEFINITION :
				getDataDefinition( ).clear( );
				return;
			case TypePackage.DIAL_SERIES__SERIES_IDENTIFIER :
				setSeriesIdentifier( SERIES_IDENTIFIER_EDEFAULT );
				return;
			case TypePackage.DIAL_SERIES__DATA_POINT :
				setDataPoint( (DataPoint) null );
				return;
			case TypePackage.DIAL_SERIES__DATA_SET :
				setDataSet( (DataSet) null );
				return;
			case TypePackage.DIAL_SERIES__LABEL_POSITION :
				unsetLabelPosition( );
				return;
			case TypePackage.DIAL_SERIES__STACKED :
				unsetStacked( );
				return;
			case TypePackage.DIAL_SERIES__TRIGGERS :
				getTriggers( ).clear( );
				return;
			case TypePackage.DIAL_SERIES__TRANSLUCENT :
				unsetTranslucent( );
				return;
			case TypePackage.DIAL_SERIES__CURVE_FITTING :
				setCurveFitting( (CurveFitting) null );
				return;
			case TypePackage.DIAL_SERIES__DIAL :
				setDial( (Dial) null );
				return;
			case TypePackage.DIAL_SERIES__NEEDLE :
				setNeedle( (Needle) null );
				return;
		}
		eDynamicUnset( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean eIsSet( EStructuralFeature eFeature )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case TypePackage.DIAL_SERIES__VISIBLE :
				return isSetVisible( );
			case TypePackage.DIAL_SERIES__LABEL :
				return label != null;
			case TypePackage.DIAL_SERIES__DATA_DEFINITION :
				return dataDefinition != null && !dataDefinition.isEmpty( );
			case TypePackage.DIAL_SERIES__SERIES_IDENTIFIER :
				return SERIES_IDENTIFIER_EDEFAULT == null ? seriesIdentifier != null
						: !SERIES_IDENTIFIER_EDEFAULT.equals( seriesIdentifier );
			case TypePackage.DIAL_SERIES__DATA_POINT :
				return dataPoint != null;
			case TypePackage.DIAL_SERIES__DATA_SET :
				return dataSet != null;
			case TypePackage.DIAL_SERIES__LABEL_POSITION :
				return isSetLabelPosition( );
			case TypePackage.DIAL_SERIES__STACKED :
				return isSetStacked( );
			case TypePackage.DIAL_SERIES__TRIGGERS :
				return triggers != null && !triggers.isEmpty( );
			case TypePackage.DIAL_SERIES__TRANSLUCENT :
				return isSetTranslucent( );
			case TypePackage.DIAL_SERIES__CURVE_FITTING :
				return curveFitting != null;
			case TypePackage.DIAL_SERIES__DIAL :
				return dial != null;
			case TypePackage.DIAL_SERIES__NEEDLE :
				return needle != null;
		}
		return eDynamicIsSet( eFeature );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#create()
	 */
	public static final Series create( )
	{
		final DialSeries ds = TypeFactory.eINSTANCE.createDialSeries( );
		( (DialSeriesImpl) ds ).initialize( );
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	protected final void initialize( )
	{
		super.initialize( );

		getLabel().setVisible(true);
		
		setDial( DialImpl.create( ) );
		setNeedle( NeedleImpl.create( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "DialSeriesImpl.displayName" ); //$NON-NLS-1$
	}

} // DialSeriesImpl
