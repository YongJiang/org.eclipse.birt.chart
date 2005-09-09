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

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;

import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Series Grouping</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingUnit <em>Grouping Unit</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingOrigin <em>Grouping Origin</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingInterval <em>Grouping Interval</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupType <em>Group Type</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getAggregateExpression <em>Aggregate Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SeriesGroupingImpl extends EObjectImpl implements SeriesGrouping
{

	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;

	/**
	 * This is true if the Enabled attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean enabledESet = false;

	/**
	 * The default value of the '
	 * {@link #getGroupingUnit() <em>Grouping Unit</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupingUnit()
	 * @generated
	 * @ordered
	 */
	protected static final GroupingUnitType GROUPING_UNIT_EDEFAULT = GroupingUnitType.SECONDS_LITERAL;

	/**
	 * The cached value of the '
	 * {@link #getGroupingUnit() <em>Grouping Unit</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupingUnit()
	 * @generated
	 * @ordered
	 */
	protected GroupingUnitType groupingUnit = GROUPING_UNIT_EDEFAULT;

	/**
	 * This is true if the Grouping Unit attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean groupingUnitESet = false;

	/**
	 * The cached value of the '{@link #getGroupingOrigin() <em>Grouping Origin</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getGroupingOrigin()
	 * @generated
	 * @ordered
	 */
	protected DataElement groupingOrigin = null;

	/**
	 * The default value of the '{@link #getGroupingInterval() <em>Grouping Interval</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getGroupingInterval()
	 * @generated
	 * @ordered
	 */
	protected static final int GROUPING_INTERVAL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getGroupingInterval() <em>Grouping Interval</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getGroupingInterval()
	 * @generated
	 * @ordered
	 */
	protected int groupingInterval = GROUPING_INTERVAL_EDEFAULT;

	/**
	 * This is true if the Grouping Interval attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean groupingIntervalESet = false;

	/**
	 * The default value of the '{@link #getGroupType() <em>Group Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getGroupType()
	 * @generated
	 * @ordered
	 */
	protected static final DataType GROUP_TYPE_EDEFAULT = DataType.NUMERIC_LITERAL;

	/**
	 * The cached value of the '{@link #getGroupType() <em>Group Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getGroupType()
	 * @generated
	 * @ordered
	 */
	protected DataType groupType = GROUP_TYPE_EDEFAULT;

	/**
	 * This is true if the Group Type attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean groupTypeESet = false;

	/**
	 * The default value of the '{@link #getAggregateExpression() <em>Aggregate Expression</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAggregateExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String AGGREGATE_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAggregateExpression() <em>Aggregate Expression</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAggregateExpression()
	 * @generated
	 * @ordered
	 */
	protected String aggregateExpression = AGGREGATE_EXPRESSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected SeriesGroupingImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return DataPackage.eINSTANCE.getSeriesGrouping( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEnabled( )
	{
		return enabled;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnabled( boolean newEnabled )
	{
		boolean oldEnabled = enabled;
		enabled = newEnabled;
		boolean oldEnabledESet = enabledESet;
		enabledESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.SERIES_GROUPING__ENABLED,
					oldEnabled,
					enabled,
					!oldEnabledESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEnabled( )
	{
		boolean oldEnabled = enabled;
		boolean oldEnabledESet = enabledESet;
		enabled = ENABLED_EDEFAULT;
		enabledESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					DataPackage.SERIES_GROUPING__ENABLED,
					oldEnabled,
					ENABLED_EDEFAULT,
					oldEnabledESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEnabled( )
	{
		return enabledESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public GroupingUnitType getGroupingUnit( )
	{
		return groupingUnit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupingUnit( GroupingUnitType newGroupingUnit )
	{
		GroupingUnitType oldGroupingUnit = groupingUnit;
		groupingUnit = newGroupingUnit == null ? GROUPING_UNIT_EDEFAULT
				: newGroupingUnit;
		boolean oldGroupingUnitESet = groupingUnitESet;
		groupingUnitESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.SERIES_GROUPING__GROUPING_UNIT,
					oldGroupingUnit,
					groupingUnit,
					!oldGroupingUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetGroupingUnit( )
	{
		GroupingUnitType oldGroupingUnit = groupingUnit;
		boolean oldGroupingUnitESet = groupingUnitESet;
		groupingUnit = GROUPING_UNIT_EDEFAULT;
		groupingUnitESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					DataPackage.SERIES_GROUPING__GROUPING_UNIT,
					oldGroupingUnit,
					GROUPING_UNIT_EDEFAULT,
					oldGroupingUnitESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetGroupingUnit( )
	{
		return groupingUnitESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataElement getGroupingOrigin( )
	{
		return groupingOrigin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupingOrigin(
			DataElement newGroupingOrigin, NotificationChain msgs )
	{
		DataElement oldGroupingOrigin = groupingOrigin;
		groupingOrigin = newGroupingOrigin;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					DataPackage.SERIES_GROUPING__GROUPING_ORIGIN,
					oldGroupingOrigin,
					newGroupingOrigin );
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
	public void setGroupingOrigin( DataElement newGroupingOrigin )
	{
		if ( newGroupingOrigin != groupingOrigin )
		{
			NotificationChain msgs = null;
			if ( groupingOrigin != null )
				msgs = ( (InternalEObject) groupingOrigin ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- DataPackage.SERIES_GROUPING__GROUPING_ORIGIN,
						null,
						msgs );
			if ( newGroupingOrigin != null )
				msgs = ( (InternalEObject) newGroupingOrigin ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- DataPackage.SERIES_GROUPING__GROUPING_ORIGIN,
						null,
						msgs );
			msgs = basicSetGroupingOrigin( newGroupingOrigin, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.SERIES_GROUPING__GROUPING_ORIGIN,
					newGroupingOrigin,
					newGroupingOrigin ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getGroupingInterval( )
	{
		return groupingInterval;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupingInterval( int newGroupingInterval )
	{
		int oldGroupingInterval = groupingInterval;
		groupingInterval = newGroupingInterval;
		boolean oldGroupingIntervalESet = groupingIntervalESet;
		groupingIntervalESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.SERIES_GROUPING__GROUPING_INTERVAL,
					oldGroupingInterval,
					groupingInterval,
					!oldGroupingIntervalESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetGroupingInterval( )
	{
		int oldGroupingInterval = groupingInterval;
		boolean oldGroupingIntervalESet = groupingIntervalESet;
		groupingInterval = GROUPING_INTERVAL_EDEFAULT;
		groupingIntervalESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					DataPackage.SERIES_GROUPING__GROUPING_INTERVAL,
					oldGroupingInterval,
					GROUPING_INTERVAL_EDEFAULT,
					oldGroupingIntervalESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetGroupingInterval( )
	{
		return groupingIntervalESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataType getGroupType( )
	{
		return groupType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupType( DataType newGroupType )
	{
		DataType oldGroupType = groupType;
		groupType = newGroupType == null ? GROUP_TYPE_EDEFAULT : newGroupType;
		boolean oldGroupTypeESet = groupTypeESet;
		groupTypeESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.SERIES_GROUPING__GROUP_TYPE,
					oldGroupType,
					groupType,
					!oldGroupTypeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetGroupType( )
	{
		DataType oldGroupType = groupType;
		boolean oldGroupTypeESet = groupTypeESet;
		groupType = GROUP_TYPE_EDEFAULT;
		groupTypeESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					DataPackage.SERIES_GROUPING__GROUP_TYPE,
					oldGroupType,
					GROUP_TYPE_EDEFAULT,
					oldGroupTypeESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetGroupType( )
	{
		return groupTypeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getAggregateExpression( )
	{
		return aggregateExpression;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setAggregateExpression( String newAggregateExpression )
	{
		String oldAggregateExpression = aggregateExpression;
		aggregateExpression = newAggregateExpression;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION,
					oldAggregateExpression,
					aggregateExpression ) );
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
				case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN :
					return basicSetGroupingOrigin( null, msgs );
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
			case DataPackage.SERIES_GROUPING__ENABLED :
				return isEnabled( ) ? Boolean.TRUE : Boolean.FALSE;
			case DataPackage.SERIES_GROUPING__GROUPING_UNIT :
				return getGroupingUnit( );
			case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN :
				return getGroupingOrigin( );
			case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL :
				return new Integer( getGroupingInterval( ) );
			case DataPackage.SERIES_GROUPING__GROUP_TYPE :
				return getGroupType( );
			case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION :
				return getAggregateExpression( );
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
			case DataPackage.SERIES_GROUPING__ENABLED :
				setEnabled( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case DataPackage.SERIES_GROUPING__GROUPING_UNIT :
				setGroupingUnit( (GroupingUnitType) newValue );
				return;
			case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN :
				setGroupingOrigin( (DataElement) newValue );
				return;
			case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL :
				setGroupingInterval( ( (Integer) newValue ).intValue( ) );
				return;
			case DataPackage.SERIES_GROUPING__GROUP_TYPE :
				setGroupType( (DataType) newValue );
				return;
			case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION :
				setAggregateExpression( (String) newValue );
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
			case DataPackage.SERIES_GROUPING__ENABLED :
				unsetEnabled( );
				return;
			case DataPackage.SERIES_GROUPING__GROUPING_UNIT :
				unsetGroupingUnit( );
				return;
			case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN :
				setGroupingOrigin( (DataElement) null );
				return;
			case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL :
				unsetGroupingInterval( );
				return;
			case DataPackage.SERIES_GROUPING__GROUP_TYPE :
				unsetGroupType( );
				return;
			case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION :
				setAggregateExpression( AGGREGATE_EXPRESSION_EDEFAULT );
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
			case DataPackage.SERIES_GROUPING__ENABLED :
				return isSetEnabled( );
			case DataPackage.SERIES_GROUPING__GROUPING_UNIT :
				return isSetGroupingUnit( );
			case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN :
				return groupingOrigin != null;
			case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL :
				return isSetGroupingInterval( );
			case DataPackage.SERIES_GROUPING__GROUP_TYPE :
				return isSetGroupType( );
			case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION :
				return AGGREGATE_EXPRESSION_EDEFAULT == null ? aggregateExpression != null
						: !AGGREGATE_EXPRESSION_EDEFAULT.equals( aggregateExpression );
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
		result.append( " (enabled: " ); //$NON-NLS-1$
		if ( enabledESet )
			result.append( enabled );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", groupingUnit: " ); //$NON-NLS-1$
		if ( groupingUnitESet )
			result.append( groupingUnit );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", groupingInterval: " ); //$NON-NLS-1$
		if ( groupingIntervalESet )
			result.append( groupingInterval );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", groupType: " ); //$NON-NLS-1$
		if ( groupTypeESet )
			result.append( groupType );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", aggregateExpression: " ); //$NON-NLS-1$
		result.append( aggregateExpression );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience method provided to create a series grouping instance and
	 * initialize its member variables
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	public static final SeriesGrouping create( )
	{
		final SeriesGrouping sg = DataFactory.eINSTANCE.createSeriesGrouping( );
		sg.setAggregateExpression( "Sum" ); //$NON-NLS-1$
		sg.setGroupingInterval( 2 );
		sg.setEnabled( false );
		sg.setGroupType( DataType.TEXT_LITERAL );
		return sg;
	}

} //SeriesGroupingImpl
