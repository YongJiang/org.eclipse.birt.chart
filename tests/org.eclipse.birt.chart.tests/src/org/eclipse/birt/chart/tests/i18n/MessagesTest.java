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
import java.util.Locale;

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
		assertEquals("Message:", org.eclipse.birt.chart.device.extension.i18n.Messages.getString( "message.caption", new Locale("en") ) );
		assertEquals("!chart!", org.eclipse.birt.chart.device.extension.i18n.Messages.getString( "chart", new Locale("en") ) );
	}
	
	//org.eclipse.birt.chart.engine
	public void testEngineGetString( )
	{
		assertEquals("null", org.eclipse.birt.chart.engine.i18n.Messages.getString( "constant.null.string", new Locale("en") ) );
		assertEquals("!chart!", org.eclipse.birt.chart.engine.i18n.Messages.getString( "chart", new Locale("en") ) );
	}
	
	//org.eclipse.birt.chart.engine.extension
	public void testEngineExtGetString( )
	{
		assertEquals("Empty dataset found", org.eclipse.birt.chart.engine.extension.i18n.Messages.getString( "exception.empty.dataset", new Locale("en") ) );
		assertEquals("!chart!", org.eclipse.birt.chart.engine.extension.i18n.Messages.getString( "chart", new Locale("en") ) );
	}
	
	//org.eclipse.birt.chart.reportitem
	public void testItemGetString( )
	{
		assertEquals("Value", org.eclipse.birt.chart.reportitem.i18n.Messages.getString( "ChartDataBindingPage.Lbl.Value" ) );
		assertEquals("!chart!", org.eclipse.birt.chart.reportitem.i18n.Messages.getString( "chart") );
	}
	
	//org.eclipse.birt.chart.ui
	public void testUIGetString( )
	{
		assertEquals("Cancel", org.eclipse.birt.chart.ui.i18n.Messages.getString( "Shared.Lbl.Cancel" ) );
		assertEquals("!chart!", org.eclipse.birt.chart.ui.i18n.Messages.getString( "chart") );
	}
	
	//org.eclipse.birt.chart.ui.extension
	public void testUIExtGetString( )
	{
		assertEquals("Cancel", org.eclipse.birt.chart.ui.extension.i18n.Messages.getString( "Shared.Lbl.Cancel" ) );
		assertEquals("!chart!", org.eclipse.birt.chart.ui.extension.i18n.Messages.getString( "chart") );
	}
}