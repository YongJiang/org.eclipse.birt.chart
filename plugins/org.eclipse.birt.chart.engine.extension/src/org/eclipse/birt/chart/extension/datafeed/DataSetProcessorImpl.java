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

package org.eclipse.birt.chart.extension.datafeed;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.datafeed.DataSetAdapter;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NullDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

/**
 * Capable of processing data sets that contain simple data elements that wrap a
 * single value (e.g. double, datetime, etc)
 */
public class DataSetProcessorImpl extends DataSetAdapter
{
	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine.extension/datafeed" ); //$NON-NLS-1$

	/**
	 * A default constructor provided for successful creation
	 */
	public DataSetProcessorImpl( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMaximum(org.eclipse.birt.chart.model.data.DataSet)
	 */
	public Object getMaximum( DataSet ds ) throws ChartException
	{
		DataSetIterator dsi = null;
		try
		{
			dsi = new DataSetIterator( ds );
			dsi.reset( );
		}
		catch ( IllegalArgumentException uiex )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					uiex );
		}
		if ( dsi.size( ) == 0 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.empty.dataset", //$NON-NLS-1$ 
					Messages.getResourceBundle( getULocale( ) ) );
		}

		if ( ds instanceof NumberDataSet )
		{
			boolean bAnyNonNull = false;
			Object o;
			double d, dMax = 0;
			while ( dsi.hasNext( ) )
			{
				o = dsi.next( );
				if ( o == null ) // NOT SET = NULL
				{
					continue;
				}
				d = ( (Number) o ).doubleValue( );
				if ( !bAnyNonNull )
				{
					dMax = d;
					bAnyNonNull = true;
				}
				else if ( dMax < d )
				{
					dMax = d;
				}
			}
			if ( !bAnyNonNull )
			{
				logger.log( new ChartException( ChartEngineExtensionPlugin.ID,
						ChartException.ALL_NULL_DATASET,
						"exception.null.values", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) ) );
			}
			return new Double( dMax );
		}
		else if ( ds instanceof DateTimeDataSet )
		{
			boolean bAnyNonNull = false;
			Calendar cal = null;
			Calendar calMax = Calendar.getInstance( getULocale( ) );
			while ( dsi.hasNext( ) )
			{
				cal = (Calendar) dsi.next( );
				if ( cal == null ) // NOT SET = NULL
				{
					continue;
				}
				if ( !bAnyNonNull )
				{
					calMax = cal;
					bAnyNonNull = true;
				}
				else if ( calMax.before( cal ) )
				{
					calMax = cal;
				}
			}
			if ( !bAnyNonNull )
			{
				logger.log( new ChartException( ChartEngineExtensionPlugin.ID,
						ChartException.ALL_NULL_DATASET,
						"exception.null.values", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) ) );
			}

			return calMax;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IDataSetProcessor#getMinimum(org.eclipse.birt.chart.model.data.DataSet)
	 */
	public Object getMinimum( DataSet ds ) throws ChartException
	{
		DataSetIterator dsi = null;
		try
		{
			dsi = new DataSetIterator( ds );
			dsi.reset( );
		}
		catch ( IllegalArgumentException uiex )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					uiex );
		}
		if ( dsi.size( ) == 0 )
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.empty.dataset", //$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}

		if ( ds instanceof NumberDataSet )
		{
			boolean bAnyNonNull = false;
			Object o;
			double d, dMin = 0;
			while ( dsi.hasNext( ) )
			{
				o = dsi.next( );
				if ( o == null ) // NOT SET = NULL
				{
					continue;
				}
				d = ( (Number) o ).doubleValue( );
				if ( !bAnyNonNull )
				{
					dMin = d;
					bAnyNonNull = true;
				}
				else if ( dMin > d )
				{
					dMin = d;
				}
			}
			if ( !bAnyNonNull )
			{
				logger.log( new ChartException( ChartEngineExtensionPlugin.ID,
						ChartException.ALL_NULL_DATASET,
						"exception.null.values", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) ) );
			}
			return new Double( dMin );
		}
		else if ( ds instanceof DateTimeDataSet )
		{
			boolean bAnyNonNull = false;
			Calendar cal = null;
			Calendar calMin = Calendar.getInstance( getULocale( ) );
			while ( dsi.hasNext( ) )
			{
				cal = (Calendar) dsi.next( );
				if ( cal == null ) // NOT SET = NULL
				{
					continue;
				}
				if ( !bAnyNonNull )
				{
					calMin = cal;
					bAnyNonNull = true;
				}
				else if ( calMin.after( cal ) )
				{
					calMin = cal;
				}
			}
			if ( !bAnyNonNull )
			{
				logger.log( new ChartException( ChartEngineExtensionPlugin.ID,
						ChartException.ALL_NULL_DATASET,
						"exception.null.values", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) ) );
			}

			return calMin;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.data.IDataSetProcessor#populate(java.lang.Object,
	 *      org.eclipse.birt.chart.model.data.DataSet)
	 */
	public DataSet populate( Object oResultSetDef, DataSet ds )
			throws ChartException
	{
		if ( oResultSetDef instanceof IResultSetDataSet )
		{
			final IResultSetDataSet rsds = (IResultSetDataSet) oResultSetDef;
			final long lRowCount = rsds.getSize( );

			if ( lRowCount <= 0 )
			{
				throw new ChartException( ChartEngineExtensionPlugin.ID,
						ChartException.ZERO_DATASET,
						"exception.empty.dataset",//$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );
			}

			int i = 0;

			switch ( rsds.getDataType( ) )
			{
				case IConstants.TEXT :
					final String[] saDataSet = new String[(int) lRowCount];
					while ( rsds.hasNext( ) )
					{
						Object o = rsds.next( )[0];
						saDataSet[i++] = (String) ( o );
					}
					if ( ds == null )
					{
						ds = TextDataSetImpl.create( saDataSet );
					}
					else
					{
						ds.setValues( saDataSet );
					}
					break;

				case IConstants.NUMERICAL :
					final Double[] doaDataSet = new Double[(int) lRowCount];
					while ( rsds.hasNext( ) )
					{
						doaDataSet[i++] = Methods.asDouble( rsds.next( )[0] );
					}
					if ( ds == null )
					{
						ds = NumberDataSetImpl.create( doaDataSet );
					}
					else
					{
						ds.setValues( doaDataSet );
					}
					break;

				case IConstants.DATE_TIME :
					final Calendar[] caDataSet = new Calendar[(int) lRowCount];
					while ( rsds.hasNext( ) )
					{
						caDataSet[i++] = Methods.asDateTime( rsds.next( )[0] );
					}
					if ( ds == null )
					{
						ds = DateTimeDataSetImpl.create( caDataSet );
					}
					else
					{
						ds.setValues( caDataSet );
					}
					break;

				default :
					boolean allNullValues = true;
					while ( rsds.hasNext( ) )
					{
						if ( rsds.next( )[0] != null )
						{
							allNullValues = false;
							break;
						}
					}
					if ( !allNullValues )
					{
						// if can't determine applicable data type
						throw new ChartException( ChartEngineExtensionPlugin.ID,
								ChartException.DATA_SET,
								"exception.unknown.datatype",//$NON-NLS-1$
								Messages.getResourceBundle( getULocale( ) ) );
					}
					else
					{
						// create a dummy dataset which represents null
						ds = NullDataSetImpl.create( (int)lRowCount );
					}
			}
		}
		else
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.unknown.custom.dataset", //$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.DataSetProcessor#fromString(java.lang.String,
	 *      org.eclipse.birt.chart.model.data.DataSet)
	 */
	public DataSet fromString( String sDataSetRepresentation, DataSet ds )
			throws ChartException
	{
		// Do NOT create a DataSet if the content string is null
		if ( sDataSetRepresentation == null )
		{
			return ds;
		}
		List vData = new ArrayList( );
		String[] strTok = getStringTokens( sDataSetRepresentation );
		int iType = 0;
		for ( int i = 0; i < strTok.length; i++ )
		{
			String strDataElement = strTok[i];
			if ( strDataElement.startsWith( "'" ) ) //$NON-NLS-1$
			{
				iType = 3;
			}
			// Try to deduce the data type of the element
			SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" ); //$NON-NLS-1$
			NumberFormat nf = NumberFormat.getNumberInstance( );
			switch ( iType )
			{
				case 0 :
					try
					{
						// First try Date
						Date dateElement = sdf.parse( strDataElement );
						Calendar cal = Calendar.getInstance( );
						cal.setTime( dateElement );
						ds = DateTimeDataSetImpl.create( null );
						vData.add( cal );
						iType = 1;
					}
					catch ( ParseException e )
					{
						// Next try double
						try
						{
							Number numberElement = nf.parse( strDataElement );
							ds = NumberDataSetImpl.create( null );
							vData.add( new Double( numberElement.doubleValue( ) ) );
							iType = 2;
						}
						catch ( ParseException e1 )
						{
							ds = TextDataSetImpl.create( null );
							vData.add( strDataElement );
							iType = 3;
						}
					}
					break;
				case 1 :
					if ( ds == null )
					{
						ds = DateTimeDataSetImpl.create( null );
					}
					Date dateElement = null;
					try
					{
						dateElement = sdf.parse( strDataElement );
					}
					catch ( ParseException e1 )
					{
						dateElement = new Date( );
					}
					Calendar cal = Calendar.getInstance( );
					cal.setTime( dateElement );
					vData.add( cal );
					break;
				case 2 :
					if ( ds == null )
					{
						ds = NumberDataSetImpl.create( null );
					}
					Number numberElement = null;
					try
					{
						numberElement = nf.parse( strDataElement );
					}
					catch ( ParseException e2 )
					{
						numberElement = null;// new Double( 0.0 );
					}
					vData.add( numberElement == null ? null
							: new Double( numberElement.doubleValue( ) ) );
					break;
				case 3 :
					if ( ds == null )
					{
						ds = TextDataSetImpl.create( null );
					}
					if ( strDataElement.startsWith( "'" ) ) //$NON-NLS-1$
					{
						strDataElement = strDataElement.substring( 1,
								strDataElement.length( ) - 1 );
					}
					vData.add( strDataElement );
					break;
			}
		}
		if ( ds == null ) // IF EMPTY
		{
			throw new ChartException( ChartEngineExtensionPlugin.ID,
					ChartException.DATA_SET,
					"exception.cannot.parse.sample", //$NON-NLS-1$
					Messages.getResourceBundle( getULocale( ) ) );
		}
		ds.setValues( vData );
		return ds;
	}

	protected String[] getStringTokens( String str )
	{
		// No ESC, return API results
		if ( str.indexOf( "\\," ) < 0 ) //$NON-NLS-1$
		{
			return str.split( DELIMITER );
		}

		ArrayList list = new ArrayList( );
		char[] charArray = ( str + DELIMITER ).toCharArray( );
		int startIndex = 0;
		for ( int i = 0; i < charArray.length; i++ )
		{
			char c = charArray[i];
			if ( c == ',' )
			{
				if ( charArray[i - 1] != '\\' && i > 0 )
				{
					list.add( str.substring( startIndex, i )
							.replaceAll( "\\\\,", DELIMITER ) //$NON-NLS-1$
							.trim( ) );
					startIndex = i + 1;
				}
			}
		}
		return (String[]) list.toArray( new String[list.size( )] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.DataSetProcessor#getExpectedStringFormat()
	 */
	public String getExpectedStringFormat( )
	{
		return Messages.getString( "info.sample.formats", getULocale( ) ); //$NON-NLS-1$
	}

}