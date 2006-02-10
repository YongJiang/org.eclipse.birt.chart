/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Rotation3D;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Rotation3 D</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl#getAngles <em>Angles</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Rotation3DImpl extends EObjectImpl implements Rotation3D
{

	/**
	 * The cached value of the '{@link #getAngles() <em>Angles</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAngles()
	 * @generated
	 * @ordered
	 */
	protected EList angles = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected Rotation3DImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.Literals.ROTATION3_D;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList getAngles( )
	{
		if ( angles == null )
		{
			angles = new EObjectContainmentEList( Angle3D.class,
					this,
					AttributePackage.ROTATION3_D__ANGLES );
		}
		return angles;
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
			case AttributePackage.ROTATION3_D__ANGLES :
				return ( (InternalEList) getAngles( ) ).basicRemove( otherEnd,
						msgs );
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
			case AttributePackage.ROTATION3_D__ANGLES :
				return getAngles( );
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
			case AttributePackage.ROTATION3_D__ANGLES :
				getAngles( ).clear( );
				getAngles( ).addAll( (Collection) newValue );
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
			case AttributePackage.ROTATION3_D__ANGLES :
				getAngles( ).clear( );
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
			case AttributePackage.ROTATION3_D__ANGLES :
				return angles != null && !angles.isEmpty( );
		}
		return super.eIsSet( featureID );
	}

	/**
	 * Creates an empty Roatation3D object.
	 * 
	 * @param ala
	 * @return
	 */
	public static Rotation3D create( )
	{
		return create( null );
	}

	/**
	 * Creates a Rotation3D object using given Angle3D array.
	 * 
	 * @param ala
	 * @return
	 */
	public static Rotation3D create( Angle3D[] ala )
	{
		Rotation3D rt = AttributeFactory.eINSTANCE.createRotation3D( );
		if ( ala != null )
		{
			for ( int i = 0; i < ala.length; i++ )
			{
				rt.getAngles( ).add( ala[i] );
			}
		}
		return rt;
	}

} // Rotation3DImpl
