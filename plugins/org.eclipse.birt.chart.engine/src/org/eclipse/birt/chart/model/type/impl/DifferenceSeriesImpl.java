/**
 * <copyright>
 * </copyright>
 *
 * $Id: DifferenceSeriesImpl.java,v 1.1 2006/12/28 03:49:32 anonymous Exp $
 */

package org.eclipse.birt.chart.model.type.impl;

import java.util.Collection;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Difference Series</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl#getNegativeMarkers <em>Negative Markers</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl#getNegativeLineAttributes <em>Negative Line Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DifferenceSeriesImpl extends AreaSeriesImpl implements
		DifferenceSeries
{

	/**
	 * The cached value of the '{@link #getNegativeMarkers() <em>Negative Markers</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getNegativeMarkers()
	 * @generated
	 * @ordered
	 */
	protected EList negativeMarkers = null;

	/**
	 * The cached value of the '{@link #getNegativeLineAttributes() <em>Negative Line Attributes</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getNegativeLineAttributes()
	 * @generated
	 * @ordered
	 */
	protected LineAttributes negativeLineAttributes = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DifferenceSeriesImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return TypePackage.Literals.DIFFERENCE_SERIES;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList getNegativeMarkers( )
	{
		if ( negativeMarkers == null )
		{
			negativeMarkers = new EObjectContainmentEList( Marker.class,
					this,
					TypePackage.DIFFERENCE_SERIES__NEGATIVE_MARKERS );
		}
		return negativeMarkers;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LineAttributes getNegativeLineAttributes( )
	{
		return negativeLineAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNegativeLineAttributes(
			LineAttributes newNegativeLineAttributes, NotificationChain msgs )
	{
		LineAttributes oldNegativeLineAttributes = negativeLineAttributes;
		negativeLineAttributes = newNegativeLineAttributes;
		if ( eNotificationRequired( ) )
		{
			ENotificationImpl notification = new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES,
					oldNegativeLineAttributes,
					newNegativeLineAttributes );
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
	public void setNegativeLineAttributes(
			LineAttributes newNegativeLineAttributes )
	{
		if ( newNegativeLineAttributes != negativeLineAttributes )
		{
			NotificationChain msgs = null;
			if ( negativeLineAttributes != null )
				msgs = ( (InternalEObject) negativeLineAttributes ).eInverseRemove( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES,
						null,
						msgs );
			if ( newNegativeLineAttributes != null )
				msgs = ( (InternalEObject) newNegativeLineAttributes ).eInverseAdd( this,
						EOPPOSITE_FEATURE_BASE
								- TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES,
						null,
						msgs );
			msgs = basicSetNegativeLineAttributes( newNegativeLineAttributes,
					msgs );
			if ( msgs != null )
				msgs.dispatch( );
		}
		else if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES,
					newNegativeLineAttributes,
					newNegativeLineAttributes ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove( InternalEObject otherEnd,
			int featureID, NotificationChain msgs )
	{
		switch ( featureID )
		{
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_MARKERS :
				return ( (InternalEList) getNegativeMarkers( ) ).basicRemove( otherEnd,
						msgs );
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES :
				return basicSetNegativeLineAttributes( null, msgs );
		}
		return super.eInverseRemove( otherEnd, featureID, msgs );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_MARKERS :
				return getNegativeMarkers( );
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES :
				return getNegativeLineAttributes( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_MARKERS :
				getNegativeMarkers( ).clear( );
				getNegativeMarkers( ).addAll( (Collection) newValue );
				return;
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES :
				setNegativeLineAttributes( (LineAttributes) newValue );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_MARKERS :
				getNegativeMarkers( ).clear( );
				return;
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES :
				setNegativeLineAttributes( (LineAttributes) null );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_MARKERS :
				return negativeMarkers != null && !negativeMarkers.isEmpty( );
			case TypePackage.DIFFERENCE_SERIES__NEGATIVE_LINE_ATTRIBUTES :
				return negativeLineAttributes != null;
		}
		return super.eIsSet( featureID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#canBeStacked()
	 */
	public boolean canBeStacked( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#canParticipateInCombination()
	 */
	public boolean canParticipateInCombination( )
	{
		return false;
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return
	 */
	public static final Series create( )
	{
		final DifferenceSeries ds = TypeFactory.eINSTANCE.createDifferenceSeries( );
		( (DifferenceSeriesImpl) ds ).initialize( );
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	protected void initialize( )
	{
		super.initialize( );

		final LineAttributes lia = LineAttributesImpl.create( ColorDefinitionImpl.BLACK( ),
				LineStyle.SOLID_LITERAL,
				1 );
		lia.setVisible( true );

		setNegativeLineAttributes( lia );

		final Marker m = AttributeFactory.eINSTANCE.createMarker( );
		m.setType( MarkerType.BOX_LITERAL );
		m.setSize( 4 );
		m.setVisible( false );
		getNegativeMarkers( ).add( m );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "DifferenceSeriesImpl.displayName" ); //$NON-NLS-1$	
	}

} // DifferenceSeriesImpl
