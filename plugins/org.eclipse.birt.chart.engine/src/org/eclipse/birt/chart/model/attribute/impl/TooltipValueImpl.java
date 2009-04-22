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
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Tooltip Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl#getText <em>Text</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl#getDelay <em>Delay</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TooltipValueImpl extends ActionValueImpl implements TooltipValue
{

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDelay() <em>Delay</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDelay()
	 * @generated
	 * @ordered
	 */
	protected static final int DELAY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDelay() <em>Delay</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getDelay()
	 * @generated
	 * @ordered
	 */
	protected int delay = DELAY_EDEFAULT;

	/**
	 * This is true if the Delay attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean delayESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected TooltipValueImpl( )
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
		return AttributePackage.Literals.TOOLTIP_VALUE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getText( )
	{
		return text;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setText( String newText )
	{
		String oldText = text;
		text = newText;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.TOOLTIP_VALUE__TEXT,
					oldText,
					text ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public int getDelay( )
	{
		return delay;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setDelay( int newDelay )
	{
		int oldDelay = delay;
		delay = newDelay;
		boolean oldDelayESet = delayESet;
		delayESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.TOOLTIP_VALUE__DELAY,
					oldDelay,
					delay,
					!oldDelayESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDelay( )
	{
		int oldDelay = delay;
		boolean oldDelayESet = delayESet;
		delay = DELAY_EDEFAULT;
		delayESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.TOOLTIP_VALUE__DELAY,
					oldDelay,
					DELAY_EDEFAULT,
					oldDelayESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDelay( )
	{
		return delayESet;
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
			case AttributePackage.TOOLTIP_VALUE__TEXT :
				return getText( );
			case AttributePackage.TOOLTIP_VALUE__DELAY :
				return new Integer( getDelay( ) );
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
			case AttributePackage.TOOLTIP_VALUE__TEXT :
				setText( (String) newValue );
				return;
			case AttributePackage.TOOLTIP_VALUE__DELAY :
				setDelay( ( (Integer) newValue ).intValue( ) );
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
			case AttributePackage.TOOLTIP_VALUE__TEXT :
				setText( TEXT_EDEFAULT );
				return;
			case AttributePackage.TOOLTIP_VALUE__DELAY :
				unsetDelay( );
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
			case AttributePackage.TOOLTIP_VALUE__TEXT :
				return TEXT_EDEFAULT == null ? text != null
						: !TEXT_EDEFAULT.equals( text );
			case AttributePackage.TOOLTIP_VALUE__DELAY :
				return isSetDelay( );
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
		result.append( " (text: " ); //$NON-NLS-1$
		result.append( text );
		result.append( ", delay: " ); //$NON-NLS-1$
		if ( delayESet )
			result.append( delay );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * A convenience method provided to build a tooltip action value when needed
	 * 
	 * NOTE: Manually written
	 * 
	 * @param iDelay
	 * @param sText
	 * @return
	 */
	public static final TooltipValue create( int iDelay, String sText )
	{
		final TooltipValue tv = AttributeFactory.eINSTANCE.createTooltipValue( );
		tv.setDelay( iDelay );
		if ( sText != null )
		{
			tv.setText( sText );
		}
		return tv;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public TooltipValue copyInstance( )
	{
		TooltipValueImpl dest = new TooltipValueImpl( );
		dest.set( this );
		return dest;
	}


	protected void set( TooltipValue src )
	{
		super.set( src );

		text = src.getText( );
		delay = src.getDelay( );
		delayESet = src.isSetDelay( );
	}

	public static TooltipValue create( EObject parent )
	{
		return new TooltipValueImpl( );
	}

} // TooltipValueImpl
