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

package org.eclipse.birt.chart.log;

import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.eclipse.birt.chart.log.impl.JavaUtilLoggerImpl;
import org.eclipse.birt.core.framework.Platform;

/**
 * Logger Class
 */

final public class Logger
{

	private static StreamHandler tracingHandler;

	/**
	 * Don't instanciate.
	 */
	private Logger( )
	{
	}

	/**
	 * Returns the logger by the given name.
	 * 
	 * @param name
	 * @return
	 */
	synchronized public static final ILogger getLogger( String name )
	{
		// TODO use java logger impl as default, later will use the extension
		// configuration.

		JavaUtilLoggerImpl chartLogger = new JavaUtilLoggerImpl( name );

		if ( name != null )
		{
			int idx = name.indexOf( "/" ); //$NON-NLS-1$

			if ( idx > 0 )
			{
				String pluginId = name.substring( 0, idx );
				boolean isDebugging = "true".equals( Platform.getDebugOption( pluginId + "/debug" ) ); //$NON-NLS-1$ //$NON-NLS-2$

				if ( isDebugging )
				{
					// Enable tracing.
					String value = Platform.getDebugOption( name );

					if ( "true".equals( value ) ) //$NON-NLS-1$
					{
						// setup the logger.
						java.util.logging.Logger javaLogger = chartLogger.getJavaLogger( );
						if ( javaLogger.getLevel( ).intValue( ) > Level.FINEST.intValue( ) )
						{
							javaLogger.setLevel( Level.FINEST );
						}
						javaLogger.removeHandler( getTracingHandler( ) );
						javaLogger.addHandler( getTracingHandler( ) );
					}
				}
			}
		}

		return chartLogger;
	}

	private static StreamHandler getTracingHandler( )
	{
		if ( tracingHandler == null )
		{
			tracingHandler = new StreamHandler( System.out,
					new SimpleFormatter( ) );
			tracingHandler.setLevel( Level.ALL );
		}
		return tracingHandler;
	}

}
