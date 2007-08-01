/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.integrate;

import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIPlugin;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple implementation of IUIServiceProvider for integration.
 */
public class SimpleUIServiceProviderImpl implements IUIServiceProvider
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui/integrate" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(java.lang.String)
	 */
	public String invoke( String sExpression, Object oContext, String sTitle )
	{
		logger.log( ILogger.WARNING,
				Messages.getString( "SimpleUIServiceProviderImpl.Warn.Placeholder" ) ); //$NON-NLS-1$
		return sExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(java.lang.String)
	 */
	public String invoke( String sExpression, Object oContext, String sTitle,
			boolean isChartProvider )
	{
		logger.log( ILogger.WARNING,
				Messages.getString( "SimpleUIServiceProviderImpl.Warn.Placeholder" ) ); //$NON-NLS-1$
		return sExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#validate(org.eclipse.birt.chart.model.Chart,
	 *      java.lang.Object)
	 */
	public String[] validate( Chart chartModel, Object oContext )
	{
		return null;
	}

	/**
	 * Fetches the list of registered keys for externalizing chart content
	 * 
	 * @return List containing available keys for externalized content
	 */
	public List getRegisteredKeys( )
	{
		List list = new Vector( );
		list.add( "SampleKey" ); //$NON-NLS-1$
		return list;
	}

	/**
	 * Fetches the value for the externalized resource identified by the
	 * specified key
	 * 
	 * @return String that represents the value for the specified resource in
	 *         the current locale
	 */
	public String getValue( String sKey )
	{
		if ( sKey.equals( "SampleKey" ) ) //$NON-NLS-1$
		{
			return "Sample Value"; //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getConvertedValue(double,
	 *      java.lang.String, java.lang.String)
	 */
	public double getConvertedValue( double dOriginalValue, String sFromUnits,
			String sToUnits )
	{
		return dOriginalValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(int,
	 *      java.lang.String, java.lang.Object, java.lang.String)
	 */
	public String invoke( int command, String value, Object context,
			String sTitle ) throws ChartException
	{
		switch ( command )
		{
			case IUIServiceProvider.COMMAND_HYPERLINK :
				Shell shell = new Shell( Display.getDefault( ), SWT.DIALOG_TRIM
						| SWT.RESIZE | SWT.APPLICATION_MODAL );
				ChartUIUtil.bindHelp( shell,
						ChartHelpContextIds.DIALOG_EDIT_URL );
				SimpleHyperlinkBuilder hb = new SimpleHyperlinkBuilder( shell );
				try
				{
					hb.setInputString( value );
					if ( sTitle != null )
					{
						hb.setTitle( hb.getTitle( ) + " - " + sTitle ); //$NON-NLS-1$
					}
					if ( hb.open( ) == Window.OK )
					{
						value = hb.getResultString( );
					}
				}
				catch ( Exception e )
				{
					throw new ChartException( ChartUIPlugin.ID,
							ChartException.UNDEFINED_VALUE,
							e );
				}
				break;
		}
		return value;
	}

	public boolean isInvokingSupported( )
	{
		return true;
	}

	public boolean isEclipseModeSupported( )
	{
		return false;
	}
}