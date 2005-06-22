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

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Scale</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getMin <em>Min</em>}
 * </li>
 * <li>
 * {@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getMax <em>Max</em>}
 * </li>
 * <li>
 * {@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getStep <em>Step</em>}
 * </li>
 * <li>
 * {@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getUnit <em>Unit</em>}
 * </li>
 * <li>
 * {@link org.eclipse.birt.chart.model.component.impl.ScaleImpl#getMinorGridsPerUnit <em>Minor Grids Per Unit</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ScaleImpl extends EObjectImpl implements Scale
{

	/**
	 * The cached value of the '{@link #getMin() <em>Min</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMin()
	 * @generated
	 * @ordered
	 */
	protected DataElement min = null;

	/**
	 * The cached value of the '{@link #getMax() <em>Max</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMax()
	 * @generated
	 * @ordered
	 */
	protected DataElement max = null;

	/**
	 * The default value of the '{@link #getStep() <em>Step</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStep()
	 * @generated
	 * @ordered
	 */
	protected static final double STEP_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getStep() <em>Step</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStep()
	 * @generated
	 * @ordered
	 */
	protected double step = STEP_EDEFAULT;

	/**
	 * This is true if the Step attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean stepESet = false;

	/**
	 * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected static final ScaleUnitType UNIT_EDEFAULT = ScaleUnitType.SECONDS_LITERAL;

	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected ScaleUnitType unit = UNIT_EDEFAULT;

	/**
	 * This is true if the Unit attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean unitESet = false;

	/**
	 * The default value of the '
	 * {@link #getMinorGridsPerUnit() <em>Minor Grids Per Unit</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMinorGridsPerUnit()
	 * @generated
	 * @ordered
	 */
	protected static final int MINOR_GRIDS_PER_UNIT_EDEFAULT = 0;

	/**
	 * The cached value of the '
	 * {@link #getMinorGridsPerUnit() <em>Minor Grids Per Unit</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMinorGridsPerUnit()
	 * @generated
	 * @ordered
	 */
	protected int minorGridsPerUnit = MINOR_GRIDS_PER_UNIT_EDEFAULT;

	/**
	 * This is true if the Minor Grids Per Unit attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean minorGridsPerUnitESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ScaleImpl( )
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
		return ComponentPackage.eINSTANCE.getScale( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataElement getMin( )
	{
		return min;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetMin( DataElement newMin,
			NotificationChain msgs )
	{
		DataElement oldMin = min;
		min = newMin;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MIN,
					oldMin,
					newMin );
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
	public void setMin( DataElement newMin )
	{
		if ( newMin != min )
		{
			NotificationChain msgs = null;
			if ( min != null )
				msgs = ( (InternalEObject) min ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MIN,
						null,
						msgs );
			if ( newMin != null )
				msgs = ( (InternalEObject) newMin ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MIN,
						null,
						msgs );
			msgs = basicSetMin( newMin, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MIN,
					newMin,
					newMin ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataElement getMax( )
	{
		return max;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetMax( DataElement newMax,
			NotificationChain msgs )
	{
		DataElement oldMax = max;
		max = newMax;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MAX,
					oldMax,
					newMax );
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
	public void setMax( DataElement newMax )
	{
		if ( newMax != max )
		{
			NotificationChain msgs = null;
			if ( max != null )
				msgs = ( (InternalEObject) max ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MAX,
						null,
						msgs );
			if ( newMax != null )
				msgs = ( (InternalEObject) newMax ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE - ComponentPackage.SCALE__MAX,
						null,
						msgs );
			msgs = basicSetMax( newMax, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MAX,
					newMax,
					newMax ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getStep( )
	{
		return step;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setStep( double newStep )
	{
		double oldStep = step;
		step = newStep;
		boolean oldStepESet = stepESet;
		stepESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__STEP,
					oldStep,
					step,
					!oldStepESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetStep( )
	{
		double oldStep = step;
		boolean oldStepESet = stepESet;
		step = STEP_EDEFAULT;
		stepESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__STEP,
					oldStep,
					STEP_EDEFAULT,
					oldStepESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetStep( )
	{
		return stepESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ScaleUnitType getUnit( )
	{
		return unit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setUnit( ScaleUnitType newUnit )
	{
		ScaleUnitType oldUnit = unit;
		unit = newUnit == null ? UNIT_EDEFAULT : newUnit;
		boolean oldUnitESet = unitESet;
		unitESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__UNIT,
					oldUnit,
					unit,
					!oldUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetUnit( )
	{
		ScaleUnitType oldUnit = unit;
		boolean oldUnitESet = unitESet;
		unit = UNIT_EDEFAULT;
		unitESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__UNIT,
					oldUnit,
					UNIT_EDEFAULT,
					oldUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetUnit( )
	{
		return unitESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getMinorGridsPerUnit( )
	{
		return minorGridsPerUnit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMinorGridsPerUnit( int newMinorGridsPerUnit )
	{
		int oldMinorGridsPerUnit = minorGridsPerUnit;
		minorGridsPerUnit = newMinorGridsPerUnit;
		boolean oldMinorGridsPerUnitESet = minorGridsPerUnitESet;
		minorGridsPerUnitESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT,
					oldMinorGridsPerUnit,
					minorGridsPerUnit,
					!oldMinorGridsPerUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetMinorGridsPerUnit( )
	{
		int oldMinorGridsPerUnit = minorGridsPerUnit;
		boolean oldMinorGridsPerUnitESet = minorGridsPerUnitESet;
		minorGridsPerUnit = MINOR_GRIDS_PER_UNIT_EDEFAULT;
		minorGridsPerUnitESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT,
					oldMinorGridsPerUnit,
					MINOR_GRIDS_PER_UNIT_EDEFAULT,
					oldMinorGridsPerUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetMinorGridsPerUnit( )
	{
		return minorGridsPerUnitESet;
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
				case ComponentPackage.SCALE__MIN :
					return basicSetMin( null, msgs );
				case ComponentPackage.SCALE__MAX :
					return basicSetMax( null, msgs );
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
			case ComponentPackage.SCALE__MIN :
				return getMin( );
			case ComponentPackage.SCALE__MAX :
				return getMax( );
			case ComponentPackage.SCALE__STEP :
				return new Double( getStep( ) );
			case ComponentPackage.SCALE__UNIT :
				return getUnit( );
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				return new Integer( getMinorGridsPerUnit( ) );
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
			case ComponentPackage.SCALE__MIN :
				setMin( (DataElement) newValue );
				return;
			case ComponentPackage.SCALE__MAX :
				setMax( (DataElement) newValue );
				return;
			case ComponentPackage.SCALE__STEP :
				setStep( ( (Double) newValue ).doubleValue( ) );
				return;
			case ComponentPackage.SCALE__UNIT :
				setUnit( (ScaleUnitType) newValue );
				return;
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				setMinorGridsPerUnit( ( (Integer) newValue ).intValue( ) );
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
			case ComponentPackage.SCALE__MIN :
				setMin( (DataElement) null );
				return;
			case ComponentPackage.SCALE__MAX :
				setMax( (DataElement) null );
				return;
			case ComponentPackage.SCALE__STEP :
				unsetStep( );
				return;
			case ComponentPackage.SCALE__UNIT :
				unsetUnit( );
				return;
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				unsetMinorGridsPerUnit( );
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
			case ComponentPackage.SCALE__MIN :
				return min != null;
			case ComponentPackage.SCALE__MAX :
				return max != null;
			case ComponentPackage.SCALE__STEP :
				return isSetStep( );
			case ComponentPackage.SCALE__UNIT :
				return isSetUnit( );
			case ComponentPackage.SCALE__MINOR_GRIDS_PER_UNIT :
				return isSetMinorGridsPerUnit( );
		}
		return eDynamicIsSet( eFeature );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (step: " ); //$NON-NLS-1$
		if ( stepESet )
			result.append( step );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", unit: " ); //$NON-NLS-1$
		if ( unitESet )
			result.append( unit );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", minorGridsPerUnit: " ); //$NON-NLS-1$
		if ( minorGridsPerUnitESet )
			result.append( minorGridsPerUnit );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

} //ScaleImpl
