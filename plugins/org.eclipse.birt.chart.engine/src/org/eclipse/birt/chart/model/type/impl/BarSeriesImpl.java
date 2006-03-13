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

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.util.StringTokenizer;
import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Bar Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.BarSeriesImpl#getRiser <em>Riser</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.BarSeriesImpl#getRiserOutline <em>Riser Outline</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BarSeriesImpl extends SeriesImpl implements BarSeries
{

	/**
	 * The default value of the '{@link #getRiser() <em>Riser</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getRiser()
	 * @generated
	 * @ordered
	 */
	protected static final RiserType RISER_EDEFAULT = RiserType.RECTANGLE_LITERAL;

	/**
	 * The cached value of the '{@link #getRiser() <em>Riser</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getRiser()
	 * @generated
	 * @ordered
	 */
	protected RiserType riser = RISER_EDEFAULT;

	/**
	 * This is true if the Riser attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean riserESet = false;

	/**
	 * The cached value of the '{@link #getRiserOutline() <em>Riser Outline</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getRiserOutline()
	 * @generated
	 * @ordered
	 */
	protected ColorDefinition riserOutline = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected BarSeriesImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return TypePackage.Literals.BAR_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public RiserType getRiser( )
	{
		return riser;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setRiser( RiserType newRiser )
	{
		RiserType oldRiser = riser;
		riser = newRiser == null ? RISER_EDEFAULT : newRiser;
		boolean oldRiserESet = riserESet;
		riserESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.BAR_SERIES__RISER,
					oldRiser,
					riser,
					!oldRiserESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetRiser( )
	{
		RiserType oldRiser = riser;
		boolean oldRiserESet = riserESet;
		riser = RISER_EDEFAULT;
		riserESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					TypePackage.BAR_SERIES__RISER,
					oldRiser,
					RISER_EDEFAULT,
					oldRiserESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetRiser( )
	{
		return riserESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ColorDefinition getRiserOutline( )
	{
		return riserOutline;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRiserOutline(
			ColorDefinition newRiserOutline, NotificationChain msgs )
	{
		ColorDefinition oldRiserOutline = riserOutline;
		riserOutline = newRiserOutline;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					TypePackage.BAR_SERIES__RISER_OUTLINE,
					oldRiserOutline,
					newRiserOutline );
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
	public void setRiserOutline( ColorDefinition newRiserOutline )
	{
		if ( newRiserOutline != riserOutline )
		{
			NotificationChain msgs = null;
			if ( riserOutline != null )
				msgs = ( (InternalEObject) riserOutline ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.BAR_SERIES__RISER_OUTLINE,
						null,
						msgs );
			if ( newRiserOutline != null )
				msgs = ( (InternalEObject) newRiserOutline ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.BAR_SERIES__RISER_OUTLINE,
						null,
						msgs );
			msgs = basicSetRiserOutline( newRiserOutline, msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.BAR_SERIES__RISER_OUTLINE,
					newRiserOutline,
					newRiserOutline ) );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case TypePackage.BAR_SERIES__RISER_OUTLINE :
				return basicSetRiserOutline( null, msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
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
			case TypePackage.BAR_SERIES__RISER :
				return getRiser( );
			case TypePackage.BAR_SERIES__RISER_OUTLINE :
				return getRiserOutline( );
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
			case TypePackage.BAR_SERIES__RISER :
				setRiser( (RiserType) newValue );
				return;
			case TypePackage.BAR_SERIES__RISER_OUTLINE :
				setRiserOutline( (ColorDefinition) newValue );
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
			case TypePackage.BAR_SERIES__RISER :
				unsetRiser( );
				return;
			case TypePackage.BAR_SERIES__RISER_OUTLINE :
				setRiserOutline( (ColorDefinition) null );
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
			case TypePackage.BAR_SERIES__RISER :
				return isSetRiser( );
			case TypePackage.BAR_SERIES__RISER_OUTLINE :
				return riserOutline != null;
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
		result.append( " (riser: " ); //$NON-NLS-1$
		if ( riserESet )
			result.append( riser );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * 
	 */
	public final boolean canParticipateInCombination( )
	{
		return true;
	}

	/**
	 * 
	 */
	public boolean canShareAxisUnit( )
	{
		return true;
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return
	 */
	public static final Series create( )
	{
		final BarSeries bs = TypeFactory.eINSTANCE.createBarSeries( );
		( (BarSeriesImpl) bs ).initialize( );
		return bs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.type.BarSeries#initialize()
	 */
	protected final void initialize( )
	{
		super.initialize( );
		setRiserOutline( null );
		setRiser( RiserType.RECTANGLE_LITERAL );
		setVisible( true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
	 */
	public final boolean canBeStacked( )
	{
		return true;
	}

	public void translateFrom( Series series, int iSeriesDefinitionIndex,
			Chart chart )
	{
		this.setRiser( RiserType.RECTANGLE_LITERAL );

		// Copy generic series properties
		this.setLabel( series.getLabel( ) );
		if ( series.getLabelPosition( ).equals( Position.INSIDE_LITERAL )
				|| series.getLabelPosition( ).equals( Position.OUTSIDE_LITERAL ) )
		{
			this.setLabelPosition( series.getLabelPosition( ) );
		}
		else
		{
			this.setLabelPosition( Position.OUTSIDE_LITERAL );
		}

		this.setVisible( series.isVisible( ) );
		this.setStacked( series.isStacked( ) );
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_Triggers( ) ) )
		{
			this.getTriggers( ).addAll( series.getTriggers( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataPoint( ) ) )
		{
			this.setDataPoint( series.getDataPoint( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataDefinition( ) ) )
		{
			this.getDataDefinition( )
					.add( series.getDataDefinition( ).get( 0 ) );
		}

		// Copy series specific properties
		if ( series instanceof BarSeries )
		{
			this.setRiserOutline( ( (BarSeries) series ).getRiserOutline( ) );
		}
		else if ( series instanceof PieSeries )
		{
			this.setRiserOutline( ( (PieSeries) series ).getSliceOutline( ) );
		}
		else if ( series instanceof StockSeries )
		{
			this.setRiserOutline( ( (StockSeries) series ).getLineAttributes( )
					.getColor( ) );
		}

		// Update the base axis to type text if it isn't already
		if ( chart instanceof ChartWithAxes )
		{
			( (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 ) ).setCategoryAxis( true );
		}
		else
		{
			throw new IllegalArgumentException( Messages.getString( "error.invalid.argument.for.barSeries", //$NON-NLS-1$
					new Object[]{
						chart.getClass( ).getName( )
					},
					ULocale.getDefault( ) ) );
		}

		// Update the sampledata in the model
		chart.setSampleData( getConvertedSampleData( chart.getSampleData( ),
				iSeriesDefinitionIndex ) );
	}

	private SampleData getConvertedSampleData( SampleData currentSampleData,
			int iSeriesDefinitionIndex )
	{
		// Base sample data should NOT be converted since base Axis type is not
		// being changed

		// Convert orthogonal sample data
		EList osdList = currentSampleData.getOrthogonalSampleData( );
		for ( int i = 0; i < osdList.size( ); i++ )
		{
			if ( i == iSeriesDefinitionIndex )
			{
				OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get( i );
				osd.setDataSetRepresentation( getConvertedOrthogonalSampleDataRepresentation( osd.getDataSetRepresentation( ) ) );
				currentSampleData.getOrthogonalSampleData( ).set( i, osd );
			}
		}
		return currentSampleData;
	}

	private String getConvertedOrthogonalSampleDataRepresentation(
			String sOldRepresentation )
	{
		StringTokenizer strtok = new StringTokenizer( sOldRepresentation, "," ); //$NON-NLS-1$
		StringBuffer sbNewRepresentation = new StringBuffer( "" ); //$NON-NLS-1$
		while ( strtok.hasMoreTokens( ) )
		{
			String sElement = strtok.nextToken( ).trim( );
			if ( sElement.startsWith( "H" ) ) //$NON-NLS-1$ // Orthogonal sample data is for a
			// stock chart (Orthogonal sample
			// data CANNOT
			// be text
			{
				StringTokenizer strStockTokenizer = new StringTokenizer( sElement );
				sbNewRepresentation.append( strStockTokenizer.nextToken( )
						.trim( )
						.substring( 1 ) );
			}
			else
			{
				sbNewRepresentation.append( sElement );
			}
			sbNewRepresentation.append( "," ); //$NON-NLS-1$
		}
		return sbNewRepresentation.toString( ).substring( 0,
				sbNewRepresentation.length( ) - 1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "BarSeriesImpl.displayName" ); //$NON-NLS-1$
	}
} // BarSeriesImpl
