/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.integrate;

/**
 * Utility class for integration classes
 */

public class SimpleActionUtil
{

	private static final String SPLITOR = " "; //$NON-NLS-1$

	public static SimpleActionHandle deserializeAction( String strData )
	{
		SimpleActionHandle action = new SimpleActionHandle( );
		String[] array = strData.split( SPLITOR );
		if ( array != null )
		{
			if ( array.length > 0 )
			{
				action.setURI( array[0] );
			}
			if ( array.length > 1 )
			{
				action.setTargetWindow( array[1] );
			}
		}
		return action;
	}

	public static String serializeAction( SimpleActionHandle action )
	{
		StringBuffer sb = new StringBuffer( );
		sb.append( action.getURI( ) );
		sb.append( SPLITOR );
		sb.append( action.getTargetWindow( ) );
		return sb.toString( );
	}
}