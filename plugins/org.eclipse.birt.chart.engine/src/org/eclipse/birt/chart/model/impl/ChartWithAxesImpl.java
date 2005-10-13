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

package org.eclipse.birt.chart.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Rotation3D;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Chart With Axes</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getAxes <em>Axes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getWallFill <em>Wall Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getFloorFill <em>Floor Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getOrientation <em>Orientation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getUnitSpacing <em>Unit Spacing</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithAxesImpl#getRotation <em>Rotation</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ChartWithAxesImpl extends ChartImpl implements ChartWithAxes
{

	/**
	 * The cached value of the '{@link #getAxes() <em>Axes</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAxes()
	 * @generated
	 * @ordered
	 */
	protected EList axes = null;

	/**
	 * The cached value of the '{@link #getWallFill() <em>Wall Fill</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getWallFill()
	 * @generated
	 * @ordered
	 */
	protected Fill wallFill = null;

	/**
	 * The cached value of the '{@link #getFloorFill() <em>Floor Fill</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFloorFill()
	 * @generated
	 * @ordered
	 */
	protected Fill floorFill = null;

	/**
	 * The default value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected static final Orientation ORIENTATION_EDEFAULT = Orientation.HORIZONTAL_LITERAL;

	/**
	 * The cached value of the '{@link #getOrientation() <em>Orientation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getOrientation()
	 * @generated
	 * @ordered
	 */
	protected Orientation orientation = ORIENTATION_EDEFAULT;

	/**
	 * This is true if the Orientation attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean orientationESet = false;

	/**
	 * The default value of the '
	 * {@link #getUnitSpacing() <em>Unit Spacing</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnitSpacing()
	 * @generated
	 * @ordered
	 */
	protected static final double UNIT_SPACING_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getUnitSpacing() <em>Unit Spacing</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUnitSpacing()
	 * @generated
	 * @ordered
	 */
	protected double unitSpacing = UNIT_SPACING_EDEFAULT;

	/**
	 * This is true if the Unit Spacing attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean unitSpacingESet = false;

	/**
	 * The cached value of the '{@link #getRotation() <em>Rotation</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getRotation()
	 * @generated
	 * @ordered
	 */
	protected Rotation3D rotation = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ChartWithAxesImpl( )
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
		return ModelPackage.eINSTANCE.getChartWithAxes( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getAxes( )
	{
		if ( axes == null )
		{
			axes = new EObjectContainmentEList( Axis.class,
					this,
					ModelPackage.CHART_WITH_AXES__AXES );
		}
		return axes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getWallFill( )
	{
		return wallFill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetWallFill( Fill newWallFill,
			NotificationChain msgs )
	{
		Fill oldWallFill = wallFill;
		wallFill = newWallFill;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__WALL_FILL,
					oldWallFill,
					newWallFill );
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
	public void setWallFill( Fill newWallFill )
	{
		if ( newWallFill != wallFill )
		{
			NotificationChain msgs = null;
			if ( wallFill != null )
				msgs = ( (InternalEObject) wallFill ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- ModelPackage.CHART_WITH_AXES__WALL_FILL,
						null,
						msgs );
			if ( newWallFill != null )
				msgs = ( (InternalEObject) newWallFill ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- ModelPackage.CHART_WITH_AXES__WALL_FILL,
						null,
						msgs );
			msgs = basicSetWallFill( newWallFill, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__WALL_FILL,
					newWallFill,
					newWallFill ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Fill getFloorFill( )
	{
		return floorFill;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetFloorFill( Fill newFloorFill,
			NotificationChain msgs )
	{
		Fill oldFloorFill = floorFill;
		floorFill = newFloorFill;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__FLOOR_FILL,
					oldFloorFill,
					newFloorFill );
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
	public void setFloorFill( Fill newFloorFill )
	{
		if ( newFloorFill != floorFill )
		{
			NotificationChain msgs = null;
			if ( floorFill != null )
				msgs = ( (InternalEObject) floorFill ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- ModelPackage.CHART_WITH_AXES__FLOOR_FILL,
						null,
						msgs );
			if ( newFloorFill != null )
				msgs = ( (InternalEObject) newFloorFill ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- ModelPackage.CHART_WITH_AXES__FLOOR_FILL,
						null,
						msgs );
			msgs = basicSetFloorFill( newFloorFill, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__FLOOR_FILL,
					newFloorFill,
					newFloorFill ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Orientation getOrientation( )
	{
		return orientation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setOrientation( Orientation newOrientation )
	{
		Orientation oldOrientation = orientation;
		orientation = newOrientation == null ? ORIENTATION_EDEFAULT
				: newOrientation;
		boolean oldOrientationESet = orientationESet;
		orientationESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__ORIENTATION,
					oldOrientation,
					orientation,
					!oldOrientationESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetOrientation( )
	{
		Orientation oldOrientation = orientation;
		boolean oldOrientationESet = orientationESet;
		orientation = ORIENTATION_EDEFAULT;
		orientationESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ModelPackage.CHART_WITH_AXES__ORIENTATION,
					oldOrientation,
					ORIENTATION_EDEFAULT,
					oldOrientationESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetOrientation( )
	{
		return orientationESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getUnitSpacing( )
	{
		return unitSpacing;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setUnitSpacing( double newUnitSpacing )
	{
		double oldUnitSpacing = unitSpacing;
		unitSpacing = newUnitSpacing;
		boolean oldUnitSpacingESet = unitSpacingESet;
		unitSpacingESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__UNIT_SPACING,
					oldUnitSpacing,
					unitSpacing,
					!oldUnitSpacingESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetUnitSpacing( )
	{
		double oldUnitSpacing = unitSpacing;
		boolean oldUnitSpacingESet = unitSpacingESet;
		unitSpacing = UNIT_SPACING_EDEFAULT;
		unitSpacingESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					ModelPackage.CHART_WITH_AXES__UNIT_SPACING,
					oldUnitSpacing,
					UNIT_SPACING_EDEFAULT,
					oldUnitSpacingESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetUnitSpacing( )
	{
		return unitSpacingESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Rotation3D getRotation( )
	{
		return rotation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetRotation( Rotation3D newRotation,
			NotificationChain msgs )
	{
		Rotation3D oldRotation = rotation;
		rotation = newRotation;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__ROTATION,
					oldRotation,
					newRotation );
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
	public void setRotation( Rotation3D newRotation )
	{
		if ( newRotation != rotation )
		{
			NotificationChain msgs = null;
			if ( rotation != null )
				msgs = ( (InternalEObject) rotation ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- ModelPackage.CHART_WITH_AXES__ROTATION,
						null,
						msgs );
			if ( newRotation != null )
				msgs = ( (InternalEObject) newRotation ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- ModelPackage.CHART_WITH_AXES__ROTATION,
						null,
						msgs );
			msgs = basicSetRotation( newRotation, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					ModelPackage.CHART_WITH_AXES__ROTATION,
					newRotation,
					newRotation ) );
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
				case ModelPackage.CHART_WITH_AXES__DESCRIPTION :
					return basicSetDescription( null, msgs );
				case ModelPackage.CHART_WITH_AXES__BLOCK :
					return basicSetBlock( null, msgs );
				case ModelPackage.CHART_WITH_AXES__EXTENDED_PROPERTIES :
					return ( (InternalEList) getExtendedProperties( ) ).basicRemove( otherEnd,
							msgs );
				case ModelPackage.CHART_WITH_AXES__SAMPLE_DATA :
					return basicSetSampleData( null, msgs );
				case ModelPackage.CHART_WITH_AXES__STYLES :
					return ( (InternalEList) getStyles( ) ).basicRemove( otherEnd,
							msgs );
				case ModelPackage.CHART_WITH_AXES__AXES :
					return ( (InternalEList) getAxes( ) ).basicRemove( otherEnd,
							msgs );
				case ModelPackage.CHART_WITH_AXES__WALL_FILL :
					return basicSetWallFill( null, msgs );
				case ModelPackage.CHART_WITH_AXES__FLOOR_FILL :
					return basicSetFloorFill( null, msgs );
				case ModelPackage.CHART_WITH_AXES__ROTATION :
					return basicSetRotation( null, msgs );
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
			case ModelPackage.CHART_WITH_AXES__VERSION :
				return getVersion( );
			case ModelPackage.CHART_WITH_AXES__TYPE :
				return getType( );
			case ModelPackage.CHART_WITH_AXES__SUB_TYPE :
				return getSubType( );
			case ModelPackage.CHART_WITH_AXES__DESCRIPTION :
				return getDescription( );
			case ModelPackage.CHART_WITH_AXES__BLOCK :
				return getBlock( );
			case ModelPackage.CHART_WITH_AXES__DIMENSION :
				return getDimension( );
			case ModelPackage.CHART_WITH_AXES__SCRIPT :
				return getScript( );
			case ModelPackage.CHART_WITH_AXES__UNITS :
				return getUnits( );
			case ModelPackage.CHART_WITH_AXES__SERIES_THICKNESS :
				return new Double( getSeriesThickness( ) );
			case ModelPackage.CHART_WITH_AXES__GRID_COLUMN_COUNT :
				return new Integer( getGridColumnCount( ) );
			case ModelPackage.CHART_WITH_AXES__EXTENDED_PROPERTIES :
				return getExtendedProperties( );
			case ModelPackage.CHART_WITH_AXES__SAMPLE_DATA :
				return getSampleData( );
			case ModelPackage.CHART_WITH_AXES__STYLES :
				return getStyles( );
			case ModelPackage.CHART_WITH_AXES__AXES :
				return getAxes( );
			case ModelPackage.CHART_WITH_AXES__WALL_FILL :
				return getWallFill( );
			case ModelPackage.CHART_WITH_AXES__FLOOR_FILL :
				return getFloorFill( );
			case ModelPackage.CHART_WITH_AXES__ORIENTATION :
				return getOrientation( );
			case ModelPackage.CHART_WITH_AXES__UNIT_SPACING :
				return new Double( getUnitSpacing( ) );
			case ModelPackage.CHART_WITH_AXES__ROTATION :
				return getRotation( );
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
			case ModelPackage.CHART_WITH_AXES__VERSION :
				setVersion( (String) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__TYPE :
				setType( (String) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__SUB_TYPE :
				setSubType( (String) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__DESCRIPTION :
				setDescription( (Text) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__BLOCK :
				setBlock( (Block) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__DIMENSION :
				setDimension( (ChartDimension) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__SCRIPT :
				setScript( (String) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__UNITS :
				setUnits( (String) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__SERIES_THICKNESS :
				setSeriesThickness( ( (Double) newValue ).doubleValue( ) );
				return;
			case ModelPackage.CHART_WITH_AXES__GRID_COLUMN_COUNT :
				setGridColumnCount( ( (Integer) newValue ).intValue( ) );
				return;
			case ModelPackage.CHART_WITH_AXES__EXTENDED_PROPERTIES :
				getExtendedProperties( ).clear( );
				getExtendedProperties( ).addAll( (Collection) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__SAMPLE_DATA :
				setSampleData( (SampleData) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__STYLES :
				getStyles( ).clear( );
				getStyles( ).addAll( (Collection) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__AXES :
				getAxes( ).clear( );
				getAxes( ).addAll( (Collection) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__WALL_FILL :
				setWallFill( (Fill) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__FLOOR_FILL :
				setFloorFill( (Fill) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__ORIENTATION :
				setOrientation( (Orientation) newValue );
				return;
			case ModelPackage.CHART_WITH_AXES__UNIT_SPACING :
				setUnitSpacing( ( (Double) newValue ).doubleValue( ) );
				return;
			case ModelPackage.CHART_WITH_AXES__ROTATION :
				setRotation( (Rotation3D) newValue );
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
			case ModelPackage.CHART_WITH_AXES__VERSION :
				unsetVersion( );
				return;
			case ModelPackage.CHART_WITH_AXES__TYPE :
				setType( TYPE_EDEFAULT );
				return;
			case ModelPackage.CHART_WITH_AXES__SUB_TYPE :
				setSubType( SUB_TYPE_EDEFAULT );
				return;
			case ModelPackage.CHART_WITH_AXES__DESCRIPTION :
				setDescription( (Text) null );
				return;
			case ModelPackage.CHART_WITH_AXES__BLOCK :
				setBlock( (Block) null );
				return;
			case ModelPackage.CHART_WITH_AXES__DIMENSION :
				unsetDimension( );
				return;
			case ModelPackage.CHART_WITH_AXES__SCRIPT :
				setScript( SCRIPT_EDEFAULT );
				return;
			case ModelPackage.CHART_WITH_AXES__UNITS :
				setUnits( UNITS_EDEFAULT );
				return;
			case ModelPackage.CHART_WITH_AXES__SERIES_THICKNESS :
				unsetSeriesThickness( );
				return;
			case ModelPackage.CHART_WITH_AXES__GRID_COLUMN_COUNT :
				unsetGridColumnCount( );
				return;
			case ModelPackage.CHART_WITH_AXES__EXTENDED_PROPERTIES :
				getExtendedProperties( ).clear( );
				return;
			case ModelPackage.CHART_WITH_AXES__SAMPLE_DATA :
				setSampleData( (SampleData) null );
				return;
			case ModelPackage.CHART_WITH_AXES__STYLES :
				getStyles( ).clear( );
				return;
			case ModelPackage.CHART_WITH_AXES__AXES :
				getAxes( ).clear( );
				return;
			case ModelPackage.CHART_WITH_AXES__WALL_FILL :
				setWallFill( (Fill) null );
				return;
			case ModelPackage.CHART_WITH_AXES__FLOOR_FILL :
				setFloorFill( (Fill) null );
				return;
			case ModelPackage.CHART_WITH_AXES__ORIENTATION :
				unsetOrientation( );
				return;
			case ModelPackage.CHART_WITH_AXES__UNIT_SPACING :
				unsetUnitSpacing( );
				return;
			case ModelPackage.CHART_WITH_AXES__ROTATION :
				setRotation( (Rotation3D) null );
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
			case ModelPackage.CHART_WITH_AXES__VERSION :
				return isSetVersion( );
			case ModelPackage.CHART_WITH_AXES__TYPE :
				return TYPE_EDEFAULT == null ? type != null
						: !TYPE_EDEFAULT.equals( type );
			case ModelPackage.CHART_WITH_AXES__SUB_TYPE :
				return SUB_TYPE_EDEFAULT == null ? subType != null
						: !SUB_TYPE_EDEFAULT.equals( subType );
			case ModelPackage.CHART_WITH_AXES__DESCRIPTION :
				return description != null;
			case ModelPackage.CHART_WITH_AXES__BLOCK :
				return block != null;
			case ModelPackage.CHART_WITH_AXES__DIMENSION :
				return isSetDimension( );
			case ModelPackage.CHART_WITH_AXES__SCRIPT :
				return SCRIPT_EDEFAULT == null ? script != null
						: !SCRIPT_EDEFAULT.equals( script );
			case ModelPackage.CHART_WITH_AXES__UNITS :
				return UNITS_EDEFAULT == null ? units != null
						: !UNITS_EDEFAULT.equals( units );
			case ModelPackage.CHART_WITH_AXES__SERIES_THICKNESS :
				return isSetSeriesThickness( );
			case ModelPackage.CHART_WITH_AXES__GRID_COLUMN_COUNT :
				return isSetGridColumnCount( );
			case ModelPackage.CHART_WITH_AXES__EXTENDED_PROPERTIES :
				return extendedProperties != null
						&& !extendedProperties.isEmpty( );
			case ModelPackage.CHART_WITH_AXES__SAMPLE_DATA :
				return sampleData != null;
			case ModelPackage.CHART_WITH_AXES__STYLES :
				return styles != null && !styles.isEmpty( );
			case ModelPackage.CHART_WITH_AXES__AXES :
				return axes != null && !axes.isEmpty( );
			case ModelPackage.CHART_WITH_AXES__WALL_FILL :
				return wallFill != null;
			case ModelPackage.CHART_WITH_AXES__FLOOR_FILL :
				return floorFill != null;
			case ModelPackage.CHART_WITH_AXES__ORIENTATION :
				return isSetOrientation( );
			case ModelPackage.CHART_WITH_AXES__UNIT_SPACING :
				return isSetUnitSpacing( );
			case ModelPackage.CHART_WITH_AXES__ROTATION :
				return rotation != null;
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
		result.append( " (orientation: " ); //$NON-NLS-1$
		if ( orientationESet )
			result.append( orientation );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", unitSpacing: " ); //$NON-NLS-1$
		if ( unitSpacingESet )
			result.append( unitSpacing );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * This method returns all base axes associated with the chart model
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	public final Axis[] getBaseAxes( )
	{
		final EList elAxes = getAxes( );
		final int iAxisCount = elAxes.size( );
		final Axis[] axa = new Axis[iAxisCount];
		for ( int i = 0; i < iAxisCount; i++ )
		{
			axa[i] = (Axis) elAxes.get( i );
		}
		return axa;
	}

	/**
	 * This method returns all primary base axes associated with the chart model
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	public final Axis[] getPrimaryBaseAxes( )
	{
		final EList elAxes = getAxes( );
		final int iAxisCount = elAxes.size( );
		final Axis[] axa = new Axis[iAxisCount];
		for ( int i = 0; i < iAxisCount; i++ )
		{
			axa[i] = (Axis) elAxes.get( i );
		}
		return axa;
	}

	/**
	 * This method returns all (primary and overlay) orthogonal axes for a given
	 * base axis If the primary orthogonal is requested for, it would be
	 * returned as the first element in the array
	 * 
	 * NOTE: Manually written
	 * 
	 * @param axBase
	 * @return
	 */
	public final Axis[] getOrthogonalAxes( Axis axBase, boolean bIncludePrimary )
	{
		final EList elAxes = axBase.getAssociatedAxes( );
		final int iAxisCount = elAxes.size( );
		final int iDecrease = bIncludePrimary ? 0 : 1;
		final Axis[] axa = new Axis[iAxisCount - iDecrease];
		Axis ax;

		for ( int i = 0, j = 1 - iDecrease; i < iAxisCount; i++ )
		{
			ax = (Axis) elAxes.get( i );
			if ( !ax.isPrimaryAxis( ) )
			{
				axa[j++] = ax;
			}
			else if ( bIncludePrimary )
			{
				axa[0] = ax;
			}
		}
		return axa;
	}

	/**
	 * This method returns the primary orthogonal axis for a given base axis
	 * 
	 * NOTE: Manually written
	 * 
	 * @param axBase
	 * @return
	 */
	public final Axis getPrimaryOrthogonalAxis( Axis axBase )
	{
		final EList elAxes = axBase.getAssociatedAxes( );
		final int iAxisCount = elAxes.size( );
		Axis ax;
		for ( int i = 0; i < iAxisCount; i++ )
		{
			ax = (Axis) elAxes.get( i );
			if ( ax.isPrimaryAxis( ) )
			{
				return ax;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ChartWithAxes#getAncillaryBaseAxis(org.eclipse.birt.chart.model.component.Axis)
	 */
	public Axis getAncillaryBaseAxis( Axis axBase )
	{
		final EList elAxes = axBase.getAncillaryAxes( );
		final int iAxisCount = elAxes.size( );

		if ( iAxisCount > 0 )
		{
			return (Axis) elAxes.get( 0 );
		}

		return null;
	}

	/**
	 * A convenience method to create an initialized 'ChartWithAxes' instance
	 * 
	 * Note: Manually written
	 * 
	 * @return
	 */
	public static final ChartWithAxes create( )
	{
		final ChartWithAxes cwa = ModelFactory.eINSTANCE.createChartWithAxes( );
		( (ChartWithAxesImpl) cwa ).initialize( );
		return cwa;
	}

	/**
	 * Initializes all member variables
	 * 
	 * Note: Manually written
	 */
	protected final void initialize( )
	{
		// INITIALIZE SUPER'S MEMBERS
		super.initialize( );

		// SETUP A BASE AXIS
		Axis xAxisBase = AxisImpl.create( Axis.BASE );
		xAxisBase.setTitlePosition( Position.BELOW_LITERAL );
		xAxisBase.getTitle( )
				.getCaption( )
				.setValue( Messages.getString( "ChartWithAxesImpl.X_Axis.title" ) ); //$NON-NLS-1$
		xAxisBase.getTitle( ).setVisible( true );
		xAxisBase.setPrimaryAxis( true );
		xAxisBase.setLabelPosition( Position.BELOW_LITERAL );
		xAxisBase.setOrientation( Orientation.HORIZONTAL_LITERAL );
		xAxisBase.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
		xAxisBase.getOrigin( ).setValue( NumberDataElementImpl.create( 0 ) );
		xAxisBase.getTitle( ).setVisible( true );
		xAxisBase.setType( AxisType.TEXT_LITERAL );

		// SETUP AN ORTHOGONAL AXIS
		Axis yAxisOrthogonal = AxisImpl.create( Axis.ORTHOGONAL );
		yAxisOrthogonal.setTitlePosition( Position.LEFT_LITERAL );
		yAxisOrthogonal.getTitle( )
				.getCaption( )
				.setValue( Messages.getString( "ChartWithAxesImpl.Y_Axis.title" ) ); //$NON-NLS-1$
		yAxisOrthogonal.getTitle( ).getCaption( ).getFont( ).setRotation( 90 );
		yAxisOrthogonal.getTitle( ).setVisible( true );
		yAxisOrthogonal.setPrimaryAxis( true );
		yAxisOrthogonal.setLabelPosition( Position.LEFT_LITERAL );
		yAxisOrthogonal.setOrientation( Orientation.VERTICAL_LITERAL );
		yAxisOrthogonal.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
		yAxisOrthogonal.getOrigin( )
				.setValue( NumberDataElementImpl.create( 0 ) );
		yAxisOrthogonal.setType( AxisType.LINEAR_LITERAL );

		xAxisBase.getAssociatedAxes( ).add( yAxisOrthogonal ); // ADD THE
		// ORTHOGONAL
		// AXIS TO THE
		// BASE AXIS

		getAxes( ).add( xAxisBase ); // ADD THE BASE AXIS TO THE CHART

		setRotation( Rotation3DImpl.create( ) );
	}

	/**
	 * This method needs to be called after the chart has been populated with
	 * runtime datasets and runtime series have been associated with each of the
	 * series definitions.
	 * 
	 * @param iBaseOrOrthogonal
	 * @return All series associated with the specified axis types
	 */
	public final Series[] getSeries( int iBaseOrOrthogonal )
	{
		final ArrayList al = new ArrayList( 8 );
		final Axis[] axaBase = getBaseAxes( );
		Axis[] axaOrthogonal;
		SeriesDefinition sd;
		EList el;

		for ( int i = 0; i < axaBase.length; i++ )
		{
			if ( ( iBaseOrOrthogonal | IConstants.BASE ) == IConstants.BASE )
			{
				el = axaBase[i].getSeriesDefinitions( );
				for ( int j = 0; j < el.size( ); j++ )
				{
					sd = (SeriesDefinition) el.get( j );
					al.addAll( sd.getRunTimeSeries( ) );
				}
			}
			axaOrthogonal = getOrthogonalAxes( axaBase[i], true );
			for ( int j = 0; j < axaOrthogonal.length; j++ )
			{
				if ( ( iBaseOrOrthogonal | IConstants.ORTHOGONAL ) == IConstants.ORTHOGONAL )
				{
					el = axaOrthogonal[j].getSeriesDefinitions( );
					for ( int k = 0; k < el.size( ); k++ )
					{
						sd = (SeriesDefinition) el.get( k );
						al.addAll( sd.getRunTimeSeries( ) );
					}
				}
			}
		}

		return (Series[]) al.toArray( Series.EMPTY_ARRAY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ChartWithAxes#isTransposed()
	 */
	public final boolean isTransposed( )
	{
		return ( isSetOrientation( ) && getOrientation( ).getValue( ) == Orientation.HORIZONTAL );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.ChartWithAxes#setTransposed(boolean)
	 */
	public void setTransposed( boolean bTransposed )
	{
		setOrientation( bTransposed ? Orientation.HORIZONTAL_LITERAL
				: Orientation.VERTICAL_LITERAL );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.Chart#getSeriesForLegend()
	 */
	public final SeriesDefinition[] getSeriesForLegend( )
	{
		final ArrayList al = new ArrayList( 8 );
		final Axis[] axaBase = getBaseAxes( );
		Axis[] axaOrthogonal;
		EList el;

		for ( int i = 0; i < axaBase.length; i++ )
		{
			axaOrthogonal = getOrthogonalAxes( axaBase[i], true );
			for ( int j = 0; j < axaOrthogonal.length; j++ )
			{
				el = axaOrthogonal[j].getSeriesDefinitions( );
				al.addAll( el );
			}
		}

		return (SeriesDefinition[]) al.toArray( SeriesDefinition.EMPTY_ARRAY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.Chart#clearSections(int)
	 */
	public final void clearSections( int iSectionType )
	{
		if ( ( iSectionType & IConstants.RUN_TIME ) == IConstants.RUN_TIME )
		{
			final Axis[] axaBase = getBaseAxes( );
			Axis[] axaOrthogonal;
			Axis axaAncillary;
			SeriesDefinition sd;
			EList el;

			for ( int i = 0; i < axaBase.length; i++ )
			{
				el = axaBase[i].getSeriesDefinitions( );
				for ( int j = 0; j < el.size( ); j++ )
				{
					sd = (SeriesDefinition) el.get( j );
					sd.getSeries( ).removeAll( sd.getRunTimeSeries( ) );
				}
				axaOrthogonal = getOrthogonalAxes( axaBase[i], true );
				for ( int j = 0; j < axaOrthogonal.length; j++ )
				{
					el = axaOrthogonal[j].getSeriesDefinitions( );
					for ( int k = 0; k < el.size( ); k++ )
					{
						sd = (SeriesDefinition) el.get( k );
						sd.getSeries( ).removeAll( sd.getRunTimeSeries( ) );
					}
				}
				axaAncillary = getAncillaryBaseAxis( axaBase[i] );
				el = axaAncillary.getSeriesDefinitions( );
				for ( int k = 0; k < el.size( ); k++ )
				{
					sd = (SeriesDefinition) el.get( k );
					sd.getSeries( ).removeAll( sd.getRunTimeSeries( ) );
				}
			}
		}
	}
}