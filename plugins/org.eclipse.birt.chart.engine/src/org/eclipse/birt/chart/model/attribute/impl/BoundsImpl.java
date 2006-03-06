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
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Bounds</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.BoundsImpl#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.BoundsImpl#getTop <em>Top</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.BoundsImpl#getWidth <em>Width</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.BoundsImpl#getHeight <em>Height</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BoundsImpl extends EObjectImpl implements Bounds
{

	/**
	 * The default value of the '{@link #getLeft() <em>Left</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected static final double LEFT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLeft() <em>Left</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected double left = LEFT_EDEFAULT;

	/**
	 * This is true if the Left attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean leftESet = false;

	/**
	 * The default value of the '{@link #getTop() <em>Top</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTop()
	 * @generated
	 * @ordered
	 */
	protected static final double TOP_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTop() <em>Top</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getTop()
	 * @generated
	 * @ordered
	 */
	protected double top = TOP_EDEFAULT;

	/**
	 * This is true if the Top attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean topESet = false;

	/**
	 * The default value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected static final double WIDTH_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected double width = WIDTH_EDEFAULT;

	/**
	 * This is true if the Width attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean widthESet = false;

	/**
	 * The default value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
	protected static final double HEIGHT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
	protected double height = HEIGHT_EDEFAULT;

	/**
	 * This is true if the Height attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean heightESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected BoundsImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.BOUNDS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getLeft( )
	{
		return left;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLeft( double newLeft )
	{
		double oldLeft = left;
		left = newLeft;
		boolean oldLeftESet = leftESet;
		leftESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.BOUNDS__LEFT,
					oldLeft,
					left,
					!oldLeftESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetLeft( )
	{
		double oldLeft = left;
		boolean oldLeftESet = leftESet;
		left = LEFT_EDEFAULT;
		leftESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.BOUNDS__LEFT,
					oldLeft,
					LEFT_EDEFAULT,
					oldLeftESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetLeft( )
	{
		return leftESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getTop( )
	{
		return top;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setTop( double newTop )
	{
		double oldTop = top;
		top = newTop;
		boolean oldTopESet = topESet;
		topESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.BOUNDS__TOP,
					oldTop,
					top,
					!oldTopESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTop( )
	{
		double oldTop = top;
		boolean oldTopESet = topESet;
		top = TOP_EDEFAULT;
		topESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.BOUNDS__TOP,
					oldTop,
					TOP_EDEFAULT,
					oldTopESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetTop( )
	{
		return topESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getWidth( )
	{
		return width;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setWidth( double newWidth )
	{
		double oldWidth = width;
		width = newWidth;
		boolean oldWidthESet = widthESet;
		widthESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.BOUNDS__WIDTH,
					oldWidth,
					width,
					!oldWidthESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetWidth( )
	{
		double oldWidth = width;
		boolean oldWidthESet = widthESet;
		width = WIDTH_EDEFAULT;
		widthESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.BOUNDS__WIDTH,
					oldWidth,
					WIDTH_EDEFAULT,
					oldWidthESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetWidth( )
	{
		return widthESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getHeight( )
	{
		return height;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeight( double newHeight )
	{
		double oldHeight = height;
		height = newHeight;
		boolean oldHeightESet = heightESet;
		heightESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.BOUNDS__HEIGHT,
					oldHeight,
					height,
					!oldHeightESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetHeight( )
	{
		double oldHeight = height;
		boolean oldHeightESet = heightESet;
		height = HEIGHT_EDEFAULT;
		heightESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.BOUNDS__HEIGHT,
					oldHeight,
					HEIGHT_EDEFAULT,
					oldHeightESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetHeight( )
	{
		return heightESet;
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
			case AttributePackage.BOUNDS__LEFT :
				return new Double( getLeft( ) );
			case AttributePackage.BOUNDS__TOP :
				return new Double( getTop( ) );
			case AttributePackage.BOUNDS__WIDTH :
				return new Double( getWidth( ) );
			case AttributePackage.BOUNDS__HEIGHT :
				return new Double( getHeight( ) );
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
			case AttributePackage.BOUNDS__LEFT :
				setLeft( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.BOUNDS__TOP :
				setTop( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.BOUNDS__WIDTH :
				setWidth( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.BOUNDS__HEIGHT :
				setHeight( ( (Double) newValue ).doubleValue( ) );
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
			case AttributePackage.BOUNDS__LEFT :
				unsetLeft( );
				return;
			case AttributePackage.BOUNDS__TOP :
				unsetTop( );
				return;
			case AttributePackage.BOUNDS__WIDTH :
				unsetWidth( );
				return;
			case AttributePackage.BOUNDS__HEIGHT :
				unsetHeight( );
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
			case AttributePackage.BOUNDS__LEFT :
				return isSetLeft( );
			case AttributePackage.BOUNDS__TOP :
				return isSetTop( );
			case AttributePackage.BOUNDS__WIDTH :
				return isSetWidth( );
			case AttributePackage.BOUNDS__HEIGHT :
				return isSetHeight( );
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
		result.append( " (left: " ); //$NON-NLS-1$
		if ( leftESet )
			result.append( left );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", top: " ); //$NON-NLS-1$
		if ( topESet )
			result.append( top );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", width: " ); //$NON-NLS-1$
		if ( widthESet )
			result.append( width );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", height: " ); //$NON-NLS-1$
		if ( heightESet )
			result.append( height );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenient method that allows initializing member variables.
	 * 
	 * Note: Manually written
	 * 
	 * @param dLeft
	 * @param dTop
	 * @param dWidth
	 * @param dHeight
	 */
	public void set( double dLeft, double dTop, double dWidth, double dHeight )
	{
		setLeft( dLeft );
		setTop( dTop );
		setWidth( dWidth );
		setHeight( dHeight );
	}

	/**
	 * Convenient creation of a Bounds object and instantiates member variables
	 * 
	 * NOTE: Manually written
	 * 
	 * @param dLeft
	 * @param dTop
	 * @param dWidth
	 * @param dHeight
	 * 
	 * @return
	 */
	public static final Bounds create( double dLeft, double dTop,
			double dWidth, double dHeight )
	{
		final Bounds bo = AttributeFactory.eINSTANCE.createBounds( );
		bo.setLeft( dLeft );
		bo.setTop( dTop );
		bo.setWidth( dWidth );
		bo.setHeight( dHeight );
		return bo;
	}

	/**
	 * Creates a new 'Bounds' instance by adjusting the existing 'Bounds'
	 * instance using the given 'Insets'
	 * 
	 * NOTE: Manually written
	 * 
	 * @param ins
	 * @return
	 */
	public final Bounds adjustedInstance( Insets ins )
	{
		return BoundsImpl.create( getLeft( ) + ins.getLeft( ),
				getTop( ) + ins.getTop( ),
				getWidth( ) - ins.getLeft( ) - ins.getRight( ),
				getHeight( ) - ins.getTop( ) - ins.getBottom( ) );
	}

	/**
	 * Creates a new 'Bounds' instance by translate the existing 'Bounds'
	 * instance using given offsets.
	 * 
	 * @param dTranslateX
	 * @param dTranslateY
	 */
	public final Bounds translateInstance( double dTranslateX,
			double dTranslateY )
	{
		return BoundsImpl.create( getLeft( ) + dTranslateX, getTop( )
				+ dTranslateY, getWidth( ), getHeight( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#delta(double, double,
	 *      double, double)
	 */
	public final void delta( double dLeft, double dTop, double dWidth,
			double dHeight )
	{
		setLeft( getLeft( ) + dLeft );
		setTop( getTop( ) + dTop );
		setWidth( getWidth( ) + dWidth );
		setHeight( getHeight( ) + dHeight );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#translate(double,
	 *      double)
	 */
	public final void translate( double dTranslateX, double dTranslateY )
	{
		setLeft( getLeft( ) + dTranslateX );
		setTop( getTop( ) + dTranslateY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#scale(double)
	 */
	public final void scale( double dScale )
	{
		setLeft( getLeft( ) * dScale );
		setTop( getTop( ) * dScale );
		setWidth( getWidth( ) * dScale );
		setHeight( getHeight( ) * dScale );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#scaledInstance(double)
	 */
	public final Bounds scaledInstance( double dScale )
	{
		final Bounds bo = (Bounds) EcoreUtil.copy( this );
		bo.scale( dScale );
		return bo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#adjust(org.eclipse.birt.chart.model.attribute.Insets)
	 */
	public void adjust( Insets ins )
	{
		set( getLeft( ) + ins.getLeft( ), getTop( ) + ins.getTop( ), getWidth( )
				- ins.getLeft( )
				- ins.getRight( ), getHeight( )
				- ins.getTop( )
				- ins.getBottom( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#updateFrom(org.eclipse.birt.chart.model.attribute.Location[])
	 */
	public void updateFrom( Location[] loa )
	{
		if ( loa == null )
		{
			return;
		}

		double dXMin = 0, dXMax = 0, dYMin = 0, dYMax = 0;
		for ( int i = 0; i < loa.length; i++ )
		{
			if ( i == 0 )
			{
				dXMin = loa[i].getX( );
				dXMax = loa[i].getX( );
				dYMin = loa[i].getY( );
				dYMax = loa[i].getY( );
			}
			else
			{
				dXMin = Math.min( loa[i].getX( ), dXMin );
				dYMin = Math.min( loa[i].getY( ), dYMin );
				dXMax = Math.max( loa[i].getX( ), dXMax );
				dYMax = Math.max( loa[i].getY( ), dYMax );
			}
		}
		set( dXMin, dYMin, dXMax - dXMin, dYMax - dYMin );
	}

	/**
	 * 
	 * @param bo
	 */
	public final void max( Bounds bo )
	{
		double dXMin = getLeft( ), dXMax = getLeft( ) + getWidth( ), dYMin = getTop( ), dYMax = getTop( )
				+ getHeight( );
		if ( dXMin > bo.getLeft( ) )
		{
			dXMin = bo.getLeft( );
		}
		if ( dXMax < bo.getLeft( ) + getWidth( ) )
		{
			dXMax = bo.getLeft( ) + bo.getWidth( );
		}
		if ( dYMin > bo.getTop( ) )
		{
			dYMin = bo.getTop( );
		}
		if ( dYMax < bo.getTop( ) + bo.getHeight( ) )
		{
			dYMax = bo.getTop( ) + bo.getHeight( );
		}
		setLeft( dXMin );
		setWidth( dXMax - dXMin );
		setTop( dYMin );
		setHeight( dYMax - dYMin );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#contains(org.eclipse.birt.chart.model.attribute.Location)
	 */
	public boolean contains( Location lo )
	{
		double w = getWidth( );
		double h = getHeight( );
		if ( w < 0 || h < 0 )
		{
			// At least one of the dimensions is negative...
			return false;
		}

		// Note: if either dimension is zero, tests below must return false...
		double x = getLeft( );
		double y = getTop( );
		if ( lo.getX( ) < x || lo.getY( ) < y )
		{
			return false;
		}

		w += x;
		h += y;
		// overflow || intersect
		return ( ( w < x || w > lo.getX( ) ) && ( h < y || h > lo.getY( ) ) );
	}

} // BoundsImpl
