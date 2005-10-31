/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Interactivity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl#isEnable <em>Enable</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl#getLegendBehavior <em>Legend Behavior</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InteractivityImpl extends EObjectImpl implements Interactivity
{

	/**
	 * The default value of the '{@link #isEnable() <em>Enable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isEnable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isEnable() <em>Enable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isEnable()
	 * @generated
	 * @ordered
	 */
	protected boolean enable = ENABLE_EDEFAULT;

	/**
	 * This is true if the Enable attribute has been set.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean enableESet = false;

	/**
	 * The default value of the '{@link #getLegendBehavior() <em>Legend Behavior</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLegendBehavior()
	 * @generated
	 * @ordered
	 */
	protected static final LegendBehaviorType LEGEND_BEHAVIOR_EDEFAULT = LegendBehaviorType.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getLegendBehavior() <em>Legend Behavior</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getLegendBehavior()
	 * @generated
	 * @ordered
	 */
	protected LegendBehaviorType legendBehavior = LEGEND_BEHAVIOR_EDEFAULT;

	/**
	 * This is true if the Legend Behavior attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean legendBehaviorESet = false;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected InteractivityImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return AttributePackage.eINSTANCE.getInteractivity( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEnable( )
	{
		return enable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnable( boolean newEnable )
	{
		boolean oldEnable = enable;
		enable = newEnable;
		boolean oldEnableESet = enableESet;
		enableESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.INTERACTIVITY__ENABLE,
					oldEnable,
					enable,
					!oldEnableESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEnable( )
	{
		boolean oldEnable = enable;
		boolean oldEnableESet = enableESet;
		enable = ENABLE_EDEFAULT;
		enableESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.INTERACTIVITY__ENABLE,
					oldEnable,
					ENABLE_EDEFAULT,
					oldEnableESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEnable( )
	{
		return enableESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public LegendBehaviorType getLegendBehavior( )
	{
		return legendBehavior;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void setLegendBehavior( LegendBehaviorType newLegendBehavior )
	{
		LegendBehaviorType oldLegendBehavior = legendBehavior;
		legendBehavior = newLegendBehavior == null ? LEGEND_BEHAVIOR_EDEFAULT
				: newLegendBehavior;
		boolean oldLegendBehaviorESet = legendBehaviorESet;
		legendBehaviorESet = true;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.SET,
					AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR,
					oldLegendBehavior,
					legendBehavior,
					!oldLegendBehaviorESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetLegendBehavior( )
	{
		LegendBehaviorType oldLegendBehavior = legendBehavior;
		boolean oldLegendBehaviorESet = legendBehaviorESet;
		legendBehavior = LEGEND_BEHAVIOR_EDEFAULT;
		legendBehaviorESet = false;
		if ( eNotificationRequired( ) )
			eNotify( new ENotificationImpl( this,
					Notification.UNSET,
					AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR,
					oldLegendBehavior,
					LEGEND_BEHAVIOR_EDEFAULT,
					oldLegendBehaviorESet ) );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetLegendBehavior( )
	{
		return legendBehaviorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet( EStructuralFeature eFeature, boolean resolve )
	{
		switch ( eDerivedStructuralFeatureID( eFeature ) )
		{
			case AttributePackage.INTERACTIVITY__ENABLE :
				return isEnable( ) ? Boolean.TRUE : Boolean.FALSE;
			case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR :
				return getLegendBehavior( );
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
			case AttributePackage.INTERACTIVITY__ENABLE :
				setEnable( ( (Boolean) newValue ).booleanValue( ) );
				return;
			case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR :
				setLegendBehavior( (LegendBehaviorType) newValue );
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
			case AttributePackage.INTERACTIVITY__ENABLE :
				unsetEnable( );
				return;
			case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR :
				unsetLegendBehavior( );
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
			case AttributePackage.INTERACTIVITY__ENABLE :
				return isSetEnable( );
			case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR :
				return isSetLegendBehavior( );
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
		result.append( " (enable: " ); //$NON-NLS-1$
		if ( enableESet )
			result.append( enable );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ", legendBehavior: " ); //$NON-NLS-1$
		if ( legendBehaviorESet )
			result.append( legendBehavior );
		else
			result.append( "<unset>" ); //$NON-NLS-1$
		result.append( ')' );
		return result.toString( );
	}

	/**
	 * @return
	 */
	public static Interactivity create( )
	{
		Interactivity itr = AttributeFactory.eINSTANCE.createInteractivity( );
		return itr;
	}

} // InteractivityImpl
