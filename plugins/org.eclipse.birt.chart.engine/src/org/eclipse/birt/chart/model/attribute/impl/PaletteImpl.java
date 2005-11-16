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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Palette</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.PaletteImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.PaletteImpl#getEntries <em>Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PaletteImpl extends EObjectImpl implements Palette
{

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList entries = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/model.attribute.impl" ); //$NON-NLS-1$

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected PaletteImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getPalette( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getName( )
	{
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setName( String newName )
	{
		String oldName = name;
		name = newName;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.PALETTE__NAME,
					oldName,
					name ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EList getEntries( )
	{
		if ( entries == null )
		{
			entries = new EObjectContainmentEList( Fill.class,
					this,
					AttributePackage.PALETTE__ENTRIES );
		}
		return entries;
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
				case AttributePackage.PALETTE__ENTRIES :
					return ( (InternalEList) getEntries( ) ).basicRemove( otherEnd,
							msgs );
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
			case AttributePackage.PALETTE__NAME :
				return getName( );
			case AttributePackage.PALETTE__ENTRIES :
				return getEntries( );
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
			case AttributePackage.PALETTE__NAME :
				setName( (String) newValue );
				return;
			case AttributePackage.PALETTE__ENTRIES :
				getEntries( ).clear( );
				getEntries( ).addAll( (Collection) newValue );
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
			case AttributePackage.PALETTE__NAME :
				setName( NAME_EDEFAULT );
				return;
			case AttributePackage.PALETTE__ENTRIES :
				getEntries( ).clear( );
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
			case AttributePackage.PALETTE__NAME :
				return NAME_EDEFAULT == null ? name != null
						: !NAME_EDEFAULT.equals( name );
			case AttributePackage.PALETTE__ENTRIES :
				return entries != null && !entries.isEmpty( );
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
		result.append( " (name: " ); //$NON-NLS-1$
		result.append( name );
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience method provided to create an empty or pre-initialized
	 * palette
	 * 
	 * NOTE: Manually written
	 * 
	 * @param bEmpty
	 * @return
	 */
	public static final Palette create( int iIndex, boolean bEmpty )
	{
		final Palette p = AttributeFactory.eINSTANCE.createPalette( );

		if ( !bEmpty )
		{
			p.update( iIndex );
		}
		return p;
	}

	/**
	 * A convenience method provided to create a palette with a single color
	 * entry
	 * 
	 * NOTE: Manually written
	 * 
	 * @param f
	 * @return
	 */
	public static final Palette create( Fill f )
	{
		final Palette p = AttributeFactory.eINSTANCE.createPalette( );
		p.getEntries( ).add( f );
		return p;
	}

	/**
	 * Shift the list content from tail to head.
	 * 
	 * @param lst
	 * @param pos
	 */
	private static final void shiftList( final List lst, int pos )
	{
		int size = lst.size( );

		if ( pos < 1 )
		{
			pos = 0;
		}

		if ( pos >= size )
		{
			pos = pos % size;
		}

		if ( pos == 0 )
		{
			return;
		}

		Object[] array = lst.toArray( new Object[0] );

		lst.clear( );

		for ( int i = pos; i < array.length; i++ )
		{
			lst.add( array[i] );
		}

		for ( int i = 0; i < pos; i++ )
		{
			lst.add( array[i] );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Palette#update(int)
	 */
	public final void update( int iIndex )
	{
		final EList el = getEntries( );
		el.clear( );
		if ( iIndex < 0 )
		{
			// a rotation version of palette-0, rataion pos is the negatvie
			// index.
			ArrayList al = new ArrayList( );

			al.add( ColorDefinitionImpl.create( 80, 166, 218 ) );
			al.add( ColorDefinitionImpl.create( 242, 88, 106 ) );
			al.add( ColorDefinitionImpl.create( 232, 172, 57 ) );
			al.add( ColorDefinitionImpl.create( 128, 255, 128 ) );
			al.add( ColorDefinitionImpl.create( 64, 128, 128 ) );
			al.add( ColorDefinitionImpl.create( 128, 128, 192 ) );
			al.add( ColorDefinitionImpl.create( 170, 85, 85 ) );
			al.add( ColorDefinitionImpl.create( 128, 128, 0 ) );

			shiftList( al, -iIndex );

			el.addAll( al );
		}
		else if ( iIndex == 0 )
		{
			el.add( ColorDefinitionImpl.create( 80, 166, 218 ) );
			el.add( ColorDefinitionImpl.create( 242, 88, 106 ) );
			el.add( ColorDefinitionImpl.create( 232, 172, 57 ) );
			el.add( ColorDefinitionImpl.create( 128, 255, 128 ) );
			el.add( ColorDefinitionImpl.create( 64, 128, 128 ) );
			el.add( ColorDefinitionImpl.create( 128, 128, 192 ) );
			el.add( ColorDefinitionImpl.create( 170, 85, 85 ) );
			el.add( ColorDefinitionImpl.create( 128, 128, 0 ) );
		}
		else if ( iIndex == 1 )
		{
			el.add( ColorDefinitionImpl.create( 225, 225, 255 ) );
			el.add( ColorDefinitionImpl.create( 223, 197, 41 ) );
			el.add( ColorDefinitionImpl.create( 249, 225, 191 ) );
			el.add( ColorDefinitionImpl.create( 255, 205, 225 ) );
			el.add( ColorDefinitionImpl.create( 225, 255, 225 ) );
			el.add( ColorDefinitionImpl.create( 255, 191, 255 ) );
			el.add( ColorDefinitionImpl.create( 185, 185, 221 ) );
			el.add( ColorDefinitionImpl.create( 40, 255, 148 ) );
		}
		else
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "error.unknown.palette", //$NON-NLS-1$ 
							new Object[]{
								new Integer( iIndex )
							}, Locale.getDefault( ) ) );
			update( 0 );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Palette#update(org.eclipse.birt.chart.model.attribute.Fill)
	 */
	public final void update( Fill f )
	{
		final EList el = getEntries( );
		el.clear( );
		el.add( f );
	}

} // PaletteImpl
