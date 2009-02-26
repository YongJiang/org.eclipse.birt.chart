/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Angle3 D</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl#getXAngle <em>XAngle</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl#getYAngle <em>YAngle</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl#getZAngle <em>ZAngle</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Angle3DImpl extends EObjectImpl implements Angle3D
{

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Angle3D create( double x, double y, double z )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setXAngle( x );
		angle.setYAngle( y );
		angle.setZAngle( z );
		angle.setType( AngleType.NONE_LITERAL );
		return angle;
	}

	/**
	 * @param val
	 * @return
	 */
	public static Angle3D createX( double val )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setXAngle( val );
		angle.setType( AngleType.X_LITERAL );
		return angle;
	}

	/**
	 * @param val
	 * @return
	 */
	public static Angle3D createY( double val )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setYAngle( val );
		angle.setType( AngleType.Y_LITERAL );
		return angle;
	}

	/**
	 * @param val
	 * @return
	 */
	public static Angle3D createZ( double val )
	{
		final Angle3D angle = AttributeFactory.eINSTANCE.createAngle3D( );
		angle.setZAngle( val );
		angle.setType( AngleType.Z_LITERAL );
		return angle;
	}

	/**
	 * The default value of the '{@link #getXAngle() <em>XAngle</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getXAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double XANGLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getXAngle() <em>XAngle</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getXAngle()
	 * @generated
	 * @ordered
	 */
	protected double xAngle = XANGLE_EDEFAULT;

	/**
	 * This is true if the XAngle attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean xAngleESet;

	/**
	 * The default value of the '{@link #getYAngle() <em>YAngle</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getYAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double YANGLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getYAngle() <em>YAngle</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getYAngle()
	 * @generated
	 * @ordered
	 */
	protected double yAngle = YANGLE_EDEFAULT;

	/**
	 * This is true if the YAngle attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean yAngleESet;

	/**
	 * The default value of the '{@link #getZAngle() <em>ZAngle</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZAngle()
	 * @generated
	 * @ordered
	 */
	protected static final double ZANGLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getZAngle() <em>ZAngle</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZAngle()
	 * @generated
	 * @ordered
	 */
	protected double zAngle = ZANGLE_EDEFAULT;

	/**
	 * This is true if the ZAngle attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean zAngleESet;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final AngleType TYPE_EDEFAULT = AngleType.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected AngleType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected Angle3DImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.ANGLE3_D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getXAngle( )
	{
		return xAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setXAngle( double newXAngle )
	{
		double oldXAngle = xAngle;
		xAngle = newXAngle;
		boolean oldXAngleESet = xAngleESet;
		xAngleESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.ANGLE3_D__XANGLE,
					oldXAngle,
					xAngle,
					!oldXAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetXAngle( )
	{
		double oldXAngle = xAngle;
		boolean oldXAngleESet = xAngleESet;
		xAngle = XANGLE_EDEFAULT;
		xAngleESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.ANGLE3_D__XANGLE,
					oldXAngle,
					XANGLE_EDEFAULT,
					oldXAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetXAngle( )
	{
		return xAngleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getYAngle( )
	{
		return yAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setYAngle( double newYAngle )
	{
		double oldYAngle = yAngle;
		yAngle = newYAngle;
		boolean oldYAngleESet = yAngleESet;
		yAngleESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.ANGLE3_D__YANGLE,
					oldYAngle,
					yAngle,
					!oldYAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetYAngle( )
	{
		double oldYAngle = yAngle;
		boolean oldYAngleESet = yAngleESet;
		yAngle = YANGLE_EDEFAULT;
		yAngleESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.ANGLE3_D__YANGLE,
					oldYAngle,
					YANGLE_EDEFAULT,
					oldYAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetYAngle( )
	{
		return yAngleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public double getZAngle( )
	{
		return zAngle;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setZAngle( double newZAngle )
	{
		double oldZAngle = zAngle;
		zAngle = newZAngle;
		boolean oldZAngleESet = zAngleESet;
		zAngleESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.ANGLE3_D__ZANGLE,
					oldZAngle,
					zAngle,
					!oldZAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetZAngle( )
	{
		double oldZAngle = zAngle;
		boolean oldZAngleESet = zAngleESet;
		zAngle = ZANGLE_EDEFAULT;
		zAngleESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.ANGLE3_D__ZANGLE,
					oldZAngle,
					ZANGLE_EDEFAULT,
					oldZAngleESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetZAngle( )
	{
		return zAngleESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AngleType getType( )
	{
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setType( AngleType newType )
	{
		AngleType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.ANGLE3_D__TYPE,
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
		AngleType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.ANGLE3_D__TYPE,
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet( int featureID, boolean resolve, boolean coreType )
	{
		switch ( featureID )
		{
			case AttributePackage.ANGLE3_D__XANGLE :
				return new Double( getXAngle( ) );
			case AttributePackage.ANGLE3_D__YANGLE :
				return new Double( getYAngle( ) );
			case AttributePackage.ANGLE3_D__ZANGLE :
				return new Double( getZAngle( ) );
			case AttributePackage.ANGLE3_D__TYPE :
				return getType( );
		}
		return super.eGet( featureID, resolve, coreType );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet( int featureID, Object newValue )
	{
		switch ( featureID )
		{
			case AttributePackage.ANGLE3_D__XANGLE :
				setXAngle( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.ANGLE3_D__YANGLE :
				setYAngle( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.ANGLE3_D__ZANGLE :
				setZAngle( ( (Double) newValue ).doubleValue( ) );
				return;
			case AttributePackage.ANGLE3_D__TYPE :
				setType( (AngleType) newValue );
				return;
		}
		super.eSet( featureID, newValue );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset( int featureID )
	{
		switch ( featureID )
		{
			case AttributePackage.ANGLE3_D__XANGLE :
				unsetXAngle( );
				return;
			case AttributePackage.ANGLE3_D__YANGLE :
				unsetYAngle( );
				return;
			case AttributePackage.ANGLE3_D__ZANGLE :
				unsetZAngle( );
				return;
			case AttributePackage.ANGLE3_D__TYPE :
				unsetType( );
				return;
		}
		super.eUnset( featureID );
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet( int featureID )
	{
		switch ( featureID )
		{
			case AttributePackage.ANGLE3_D__XANGLE :
				return isSetXAngle( );
			case AttributePackage.ANGLE3_D__YANGLE :
				return isSetYAngle( );
			case AttributePackage.ANGLE3_D__ZANGLE :
				return isSetZAngle( );
			case AttributePackage.ANGLE3_D__TYPE :
				return isSetType( );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString( )
	{
		if ( eIsProxy( ) )
			return super.toString( );

		StringBuffer result = new StringBuffer( super.toString( ) );
		result.append( " (xAngle: " ); //$NON-NLS-1$
		if ( xAngleESet )
			result.append( xAngle );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", yAngle: " ); //$NON-NLS-1$
		if ( yAngleESet )
			result.append( yAngle );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", zAngle: " ); //$NON-NLS-1$
		if ( zAngleESet )
			result.append( zAngle );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", type: " ); //$NON-NLS-1$
		if ( typeESet )
			result.append( type );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getAxisAngle()
	 */
	public double getAxisAngle( )
	{
		AngleType at = getType( );

		if ( at == AngleType.X_LITERAL )
		{
			return getXAngle( );
		}
		else if ( at == AngleType.Y_LITERAL )
		{
			return getYAngle( );
		}
		else if ( at == AngleType.Z_LITERAL )
		{
			return getZAngle( );
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#set(double, double,
	 *      double)
	 */
	public void set( double x, double y, double z )
	{
		setXAngle( x );
		setYAngle( y );
		setZAngle( z );
		setType( AngleType.NONE_LITERAL );
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static Angle3D copyInstance( Angle3D src )
	{
		if ( src == null )
		{
			return null;
		}

		Angle3DImpl dest = new Angle3DImpl( );

		dest.xAngle = src.getXAngle( );
		dest.xAngleESet = src.isSetXAngle( );
		dest.yAngle = src.getYAngle( );
		dest.yAngleESet = src.isSetYAngle( );
		dest.zAngle = src.getZAngle( );
		dest.zAngleESet = src.isSetZAngle( );
		dest.type = src.getType( );
		dest.typeESet = src.isSetType( );

		return dest;
	}

} // Angle3DImpl
