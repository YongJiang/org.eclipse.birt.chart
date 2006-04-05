/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.tests.i18n;

import junit.framework.TestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test class for all of Messages.java classes in chart-related packages.
 * 
 * It checks whether the Messages.java classes could retreive the correct value according to 
 * the provided key. If the required value-key pair does not exist, returns !key!.
 */

public class MessagesTest extends TestCase
{	
	//org.eclipse.birt.chart.device.extension
	public void testDeviceGetString( )
	{
		assertEquals("Message:", org.eclipse.birt.chart.device.extension.i18n.Messages.getString( "JavaxImageIOWriter.message.caption", new ULocale("en") ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("!chart!", org.eclipse.birt.chart.device.extension.i18n.Messages.getString( "chart", new ULocale("en") ) ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}
	
	//org.eclipse.birt.chart.engine
	public void testEngineGetString( )
	{
		assertEquals("null", org.eclipse.birt.chart.engine.i18n.Messages.getString( "constant.null.string", new ULocale("en") ) );//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("!chart!", org.eclipse.birt.chart.engine.i18n.Messages.getString( "chart", new ULocale("en") ) );//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}
	
	//org.eclipse.birt.chart.engine.extension
	public void testEngineExtGetString( )
	{
		assertEquals("Empty dataset found", org.eclipse.birt.chart.engine.extension.i18n.Messages.getString( "exception.empty.dataset", new ULocale("en") ) );//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("!chart!", org.eclipse.birt.chart.engine.extension.i18n.Messages.getString( "chart", new ULocale("en") ) );//$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}
}