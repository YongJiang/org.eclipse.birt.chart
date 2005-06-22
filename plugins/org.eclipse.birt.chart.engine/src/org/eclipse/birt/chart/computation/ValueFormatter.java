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

package org.eclipse.birt.chart.computation;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;

/**
 *  
 */
public final class ValueFormatter
{

	/**
	 *  
	 */
	private static final String sNegativeZero = "-0."; //$NON-NLS-1$

	/**
	 * 
	 * @param oValue
	 * @param fs
	 * @param lcl
	 * @return
	 */
	public static final String format( Object oValue, FormatSpecifier fs,
			Locale lcl, Object oCachedJavaFormatter ) throws ChartException
	{
		String sValue;
		if ( oValue == null ) // NULL VALUES CANNOT BE FORMATTED
		{
			return null;
		}
		else if ( oValue instanceof String )
		{
			return (String) oValue;
		}

		if ( fs == null ) // IF A FORMAT SPECIFIER WAS NOT ASSOCIATED WITH THE
		// VALUE
		{
			if ( oCachedJavaFormatter != null ) // CHECK IF AN INTERNAL JAVA
			// FORMAT SPECIFIER WAS COMPUTED
			{
				if ( oValue instanceof Double )
				{
					if ( oCachedJavaFormatter instanceof DecimalFormat )
					{
						final double dValue = ( (Double) oValue ).doubleValue( );
						sValue = ( (DecimalFormat) oCachedJavaFormatter ).format( ( (Double) oValue ).doubleValue( ) );
						return correctNumber( sValue, dValue );
					}
				}
				else if ( oValue instanceof NumberDataElement )
				{
					if ( oCachedJavaFormatter instanceof DecimalFormat )
					{
						final double dValue = ( (NumberDataElement) oValue ).getValue( );
						sValue = ( (DecimalFormat) oCachedJavaFormatter ).format( dValue );
						return correctNumber( sValue, dValue );
					}
				}
				else if ( oValue instanceof Calendar )
				{
					if ( oCachedJavaFormatter instanceof DateFormat )
					{
						return ( (DateFormat) oCachedJavaFormatter ).format( ( (Calendar) oValue ).getTime( ) );
					}
				}
				else if ( oValue instanceof DateTimeDataElement )
				{
					if ( oCachedJavaFormatter instanceof DecimalFormat )
					{
						return ( (DateFormat) oCachedJavaFormatter ).format( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
					}
				}
			}
			else
			{
				if ( oValue instanceof NumberDataElement )
				{
					return String.valueOf( ( (NumberDataElement) oValue ).getValue( ) );
				}
				else if ( oValue instanceof DateTimeDataElement )
				{
					return String.valueOf( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
				}
			}
		}
		else if ( NumberFormatSpecifier.class.isInstance( fs ) )
		{
			final NumberFormatSpecifier nfs = (NumberFormatSpecifier) fs;
			final double dValue = asPrimitiveDouble( oValue, lcl );
			return correctNumber( nfs.format( dValue, lcl ), dValue );
		}
		else if ( JavaNumberFormatSpecifier.class.isInstance( fs ) )
		{
			final JavaNumberFormatSpecifier nfs = (JavaNumberFormatSpecifier) fs;
			final double dValue = asPrimitiveDouble( oValue, lcl );
			return correctNumber( nfs.format( dValue, lcl ), dValue );
		}
		else if ( DateFormatSpecifier.class.isInstance( fs ) )
		{
			final DateFormatSpecifier dfs = (DateFormatSpecifier) fs;
			return dfs.format( asCalendar( oValue, lcl ), lcl );
		}
		else if ( JavaDateFormatSpecifier.class.isInstance( fs ) )
		{
			final JavaDateFormatSpecifier jdfs = (JavaDateFormatSpecifier) fs;
			return jdfs.format( asCalendar( oValue, lcl ), lcl );
		}
		else
		{
			if ( oValue instanceof NumberDataElement )
			{
				return String.valueOf( ( (NumberDataElement) oValue ).getValue( ) );
			}
			else if ( oValue instanceof DateTimeDataElement )
			{
				return String.valueOf( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
			}
		}
		return oValue.toString( );
	}

	private static final double asPrimitiveDouble( Object o, Locale lcl )
			throws ChartException
	{
		if ( o instanceof Double )
		{
			return ( (Double) o ).doubleValue( );
		}
		else if ( o instanceof NumberDataElement )
		{
			return ( (NumberDataElement) o ).getValue( );
		}
		throw new ChartException( ChartException.DATA_FORMAT,
				"exception.convert.double", //$NON-NLS-1$
				new Object[]{
					o
				},
				ResourceBundle.getBundle( Messages.ENGINE, lcl ) ); 
	}

	private static final Calendar asCalendar( Object o, Locale lcl )
			throws ChartException
	{
		if ( o instanceof Calendar )
		{
			return (Calendar) o;
		}
		else if ( o instanceof DateTimeDataElement )
		{
			return ( (DateTimeDataElement) o ).getValueAsCalendar( );
		}
		throw new ChartException( ChartException.DATA_FORMAT,
				"exception.convert.calendar", //$NON-NLS-1$
				new Object[]{
					o
				},
				ResourceBundle.getBundle( Messages.ENGINE, lcl ) ); 
	}

	/**
	 * Takes care of problems while presenting -0.00
	 * 
	 * @param df
	 * @param dValue
	 * @return
	 */
	public static final String correctNumber( String sValue, double dValue )
	{
		int n = ( sValue.length( ) - sNegativeZero.length( ) );
		final StringBuffer sb = new StringBuffer( sNegativeZero );
		for ( int i = 0; i < n; i++ )
		{
			sb.append( '0' );
		}

		if ( sValue.equals( sb.toString( ) ) )
		{
			return sb.substring( 1 ); // JUST THE ZERO IN THE EXPECTED PATTERN
			// WITHOUT THE STRAY NEGATIVE SYMBOL
		}
		return sValue;
	}
}