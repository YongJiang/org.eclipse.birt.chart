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

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Action Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines the allowed values for Action types (for interactivity with
 * chart). Any new Action type needs to be added here to be supported.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getActionType()
 * @model
 * @generated
 */
public final class ActionType extends AbstractEnumerator
{

	/**
	 * The '<em><b>URL Redirect</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #URL_REDIRECT_LITERAL
	 * @model name="URL_Redirect"
	 * @generated
	 * @ordered
	 */
	public static final int URL_REDIRECT = 0;

	/**
	 * The '<em><b>Show Tooltip</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #SHOW_TOOLTIP_LITERAL
	 * @model name="Show_Tooltip"
	 * @generated
	 * @ordered
	 */
	public static final int SHOW_TOOLTIP = 1;

	/**
	 * The '<em><b>Toggle Visibility</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #TOGGLE_VISIBILITY_LITERAL
	 * @model name="Toggle_Visibility"
	 * @generated
	 * @ordered
	 */
	public static final int TOGGLE_VISIBILITY = 2;

	/**
	 * The '<em><b>Invoke Script</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #INVOKE_SCRIPT_LITERAL
	 * @model name="Invoke_Script"
	 * @generated
	 * @ordered
	 */
	public static final int INVOKE_SCRIPT = 3;

	/**
	 * The '<em><b>Highlight</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Highlight</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HIGHLIGHT_LITERAL
	 * @model name="Highlight"
	 * @generated
	 * @ordered
	 */
	public static final int HIGHLIGHT = 4;

	/**
	 * The '<em><b>Call Back</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Call Back</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CALL_BACK_LITERAL
	 * @model name="CallBack"
	 * @generated
	 * @ordered
	 */
	public static final int CALL_BACK = 5;

	/**
	 * The '<em><b>Toggle Data Point Visibility</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Toggle Data Point Visibility</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TOGGLE_DATA_POINT_VISIBILITY_LITERAL
	 * @model name="ToggleDataPointVisibility" literal="Toggle_DataPoint_Visibility"
	 * @generated
	 * @ordered
	 */
	public static final int TOGGLE_DATA_POINT_VISIBILITY = 6;

	/**
	 * The '<em><b>URL Redirect</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>URL Redirect</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #URL_REDIRECT
	 * @generated
	 * @ordered
	 */
	public static final ActionType URL_REDIRECT_LITERAL = new ActionType( URL_REDIRECT,
			"URLRedirect", "URL_Redirect" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Show Tooltip</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Show Tooltip</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #SHOW_TOOLTIP
	 * @generated
	 * @ordered
	 */
	public static final ActionType SHOW_TOOLTIP_LITERAL = new ActionType( SHOW_TOOLTIP,
			"ShowTooltip", "Show_Tooltip" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Toggle Visibility</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Toggle Visibility</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #TOGGLE_VISIBILITY
	 * @generated
	 * @ordered
	 */
	public static final ActionType TOGGLE_VISIBILITY_LITERAL = new ActionType( TOGGLE_VISIBILITY,
			"ToggleVisibility", "Toggle_Visibility" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Invoke Script</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Invoke Script</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #INVOKE_SCRIPT
	 * @generated
	 * @ordered
	 */
	public static final ActionType INVOKE_SCRIPT_LITERAL = new ActionType( INVOKE_SCRIPT,
			"InvokeScript", "Invoke_Script" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Highlight</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HIGHLIGHT
	 * @generated
	 * @ordered
	 */
	public static final ActionType HIGHLIGHT_LITERAL = new ActionType( HIGHLIGHT,
			"Highlight", "Highlight" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Call Back</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CALL_BACK
	 * @generated
	 * @ordered
	 */
	public static final ActionType CALL_BACK_LITERAL = new ActionType( CALL_BACK,
			"CallBack", "CallBack" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Toggle Data Point Visibility</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TOGGLE_DATA_POINT_VISIBILITY
	 * @generated
	 * @ordered
	 */
	public static final ActionType TOGGLE_DATA_POINT_VISIBILITY_LITERAL = new ActionType( TOGGLE_DATA_POINT_VISIBILITY,
			"ToggleDataPointVisibility", "Toggle_DataPoint_Visibility" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * An array of all the '<em><b>Action Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ActionType[] VALUES_ARRAY = new ActionType[]{
			URL_REDIRECT_LITERAL,
			SHOW_TOOLTIP_LITERAL,
			TOGGLE_VISIBILITY_LITERAL,
			INVOKE_SCRIPT_LITERAL,
			HIGHLIGHT_LITERAL,
			CALL_BACK_LITERAL,
			TOGGLE_DATA_POINT_VISIBILITY_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Action Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Action Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static ActionType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			ActionType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Action Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ActionType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			ActionType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Action Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static ActionType get( int value )
	{
		switch ( value )
		{
			case URL_REDIRECT :
				return URL_REDIRECT_LITERAL;
			case SHOW_TOOLTIP :
				return SHOW_TOOLTIP_LITERAL;
			case TOGGLE_VISIBILITY :
				return TOGGLE_VISIBILITY_LITERAL;
			case INVOKE_SCRIPT :
				return INVOKE_SCRIPT_LITERAL;
			case HIGHLIGHT :
				return HIGHLIGHT_LITERAL;
			case CALL_BACK :
				return CALL_BACK_LITERAL;
			case TOGGLE_DATA_POINT_VISIBILITY :
				return TOGGLE_DATA_POINT_VISIBILITY_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private ActionType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //ActionType
