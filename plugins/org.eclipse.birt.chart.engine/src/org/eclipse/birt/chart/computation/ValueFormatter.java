/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.datafeed.IDataPointEntry;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * This class handles the formatting work of any data value.
 */
public final class ValueFormatter
{

	private static final String sNegativeZero = "-0."; //$NON-NLS-1$
	
	/**
	 * A default numeric pattern for integer number representation of category
	 * data or axis label
	 */
	private static final String sNumericPattern = "0"; //$NON-NLS-1$

	/**
	 * Returns the formatted string representation of given object.
	 * 
	 * @param oValue
	 * @param fs
	 * @param lcl
	 * @return formatted string
	 */
	public static final String format( Object oValue, FormatSpecifier fs,
			ULocale lcl, Object oCachedJavaFormatter ) throws ChartException
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
					if ( oCachedJavaFormatter instanceof IDateFormatWrapper )
					{
						return ( (IDateFormatWrapper) oCachedJavaFormatter ).format( ( (Calendar) oValue ).getTime( ) );
					}
				}
				else if ( oValue instanceof DateTimeDataElement )
				{
					if ( oCachedJavaFormatter instanceof DateFormat )
					{
						return ( (DateFormat) oCachedJavaFormatter ).format( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
					}
				}
				else if ( oValue instanceof IDataPointEntry )
				{
					return ( (IDataPointEntry) oValue ).getFormattedString( oCachedJavaFormatter,
							lcl );
				}
			}
			else
			{
				if ( oValue instanceof Number )
				{
					return NumberFormat.getInstance( lcl )
							.format( ( (Number) oValue ).doubleValue( ) );
				}
				else if ( oValue instanceof NumberDataElement )
				{
					return NumberFormat.getInstance( lcl )
							.format( ( (NumberDataElement) oValue ).getValue( ) );
				}
				else if ( oValue instanceof Calendar )
				{
					return DateFormat.getDateTimeInstance( DateFormat.SHORT,
							DateFormat.SHORT,
							lcl ).format( oValue );
				}
				else if ( oValue instanceof DateTimeDataElement )
				{
					return DateFormat.getDateTimeInstance( DateFormat.SHORT,
							DateFormat.SHORT,
							lcl )
							.format( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
				}
				else if ( oValue instanceof IDataPointEntry )
				{
					return ( (IDataPointEntry) oValue ).getFormattedString( null,
							lcl );
				}
			}
		}
		else if ( oValue instanceof IDataPointEntry )
		{
			return ( (IDataPointEntry) oValue ).getFormattedString( fs, lcl );
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
		else if ( FractionNumberFormatSpecifier.class.isInstance( fs ) )
		{
			final FractionNumberFormatSpecifier fnfs = (FractionNumberFormatSpecifier) fs;
			final double dValue = asPrimitiveDouble( oValue, lcl );
			return correctNumber( fnfs.format( dValue, lcl ), dValue );
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
			if ( oValue instanceof Number )
			{
				return NumberFormat.getInstance( lcl )
						.format( ( (Number) oValue ).doubleValue( ) );
			}
			else if ( oValue instanceof NumberDataElement )
			{
				return NumberFormat.getInstance( lcl )
						.format( ( (NumberDataElement) oValue ).getValue( ) );
			}
			else if ( oValue instanceof Calendar )
			{
				return DateFormat.getDateTimeInstance( DateFormat.SHORT,
						DateFormat.SHORT,
						lcl ).format( oValue );
			}
			else if ( oValue instanceof DateTimeDataElement )
			{
				return DateFormat.getDateTimeInstance( DateFormat.SHORT,
						DateFormat.SHORT,
						lcl )
						.format( ( (DateTimeDataElement) oValue ).getValueAsCalendar( ) );
			}
		}
		return oValue.toString( );
	}

	private static final double asPrimitiveDouble( Object o, ULocale lcl )
			throws ChartException
	{
		if ( o instanceof Number )
		{
			return ( (Number) o ).doubleValue( );
		}
		else if ( o instanceof NumberDataElement )
		{
			return ( (NumberDataElement) o ).getValue( );
		}
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.DATA_FORMAT,
				"exception.convert.double", //$NON-NLS-1$
				new Object[]{
					o
				},
				Messages.getResourceBundle( lcl ) );
	}

	private static final Calendar asCalendar( Object o, ULocale lcl )
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
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.DATA_FORMAT,
				"exception.convert.calendar", //$NON-NLS-1$
				new Object[]{
					o
				},
				Messages.getResourceBundle( lcl ) );
	}

	/**
	 * Takes care of problems while presenting -0.00
	 * 
	 * @param df
	 * @param dValue
	 * @return corrected number
	 */
	private static final String correctNumber( String sValue, double dValue )
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
	
	/**
	 * Returns an auto computed decimal format pattern for category data or axis
	 * label. If it's an integer, no decimal point and no separator. This is
	 * also used for representing logarithmic values.
	 * 
	 * @return numeric pattern
	 */
	public static String getNumericPattern( double dValue )
	{
		if ( ChartUtil.mathEqual( dValue, (long) dValue ) )
		{
			// IF MANTISSA IS INSIGNIFICANT, SHOW LABELS AS INTEGERS
			return sNumericPattern;
		}

		final DecimalFormatSymbols dfs = new DecimalFormatSymbols( );
		String sValue = String.valueOf( dValue );
		int iEPosition = sValue.indexOf( dfs.getExponentSeparator( ) );

		if ( iEPosition > 0 )
		{
			dValue = Double.valueOf( sValue.substring( 0, iEPosition ) )
					.doubleValue( );

			if ( ChartUtil.mathEqual( dValue, Math.round( dValue ) ) )
			{
				// IF MANTISSA IS INSIGNIFICANT, SHOW LABELS AS INTEGERS
				return "0E0"; //$NON-NLS-1$
			}
			else
			{
				sValue = String.valueOf( dValue );
			}
		}

		final int iDecimalPosition = sValue.indexOf( dfs.getDecimalSeparator( ) );
		// THIS RELIES ON THE FACT THAT IN ANY LOCALE, DECIMAL IS A DOT
		if ( iDecimalPosition >= 0 )
		{
			int n = sValue.length( );
			for ( int i = n - 1; i > 0; i-- )
			{
				if ( sValue.charAt( i ) == '0' )
				{
					n--;
				}
				else
				{
					break;
				}
			}
			final int iMantissaCount = n - 1 - iDecimalPosition;
			final StringBuffer sb = new StringBuffer( sNumericPattern );
			if ( iMantissaCount > 0 )
			{
				sb.append( '.' );
				for ( int i = 0; i < iMantissaCount; i++ )
				{
					sb.append( '0' );
				}
			}
			if ( iEPosition > 0 )
			{
				sb.append( "E0" ); //$NON-NLS-1$
			}
			return sb.toString( );
		}
		return sNumericPattern;
	}
}