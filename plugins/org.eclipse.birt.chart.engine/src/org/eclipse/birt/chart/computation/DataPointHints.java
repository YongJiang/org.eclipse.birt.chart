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

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.common.util.EList;

/**
 * DataPointHints
 */
public final class DataPointHints
{

	private final RunTimeContext rtc;

	private Object oBaseValue;

	private Object oOrthogonalValue;

	private Object oSeriesValue;

	private Object oPercentileOrthogonalValue;

	private Map userValueMap;

	private final Location lo;

	private final double[] dSize;

	private final DataPoint dp;

	private final FormatSpecifier fsBase, fsOrthogonal, fsSeries, fsPercentile;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/computation" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param _oBaseValue
	 * @param _oOrthogonalValue
	 * @param _sSeriesValue
	 * @param _dp
	 * @param _lo
	 * @param _dSize
	 * @param _lcl
	 * @throws UndefinedValueException
	 */
	public DataPointHints( Object _oBaseValue,
			Object _oOrthogonalValue,
			Object _oSeriesValue,
			Object _oPercentileValue,
			DataPoint _dp, // FOR COMBINED VALUE
			// RETRIEVAL
			FormatSpecifier _fsBase, FormatSpecifier _fsOrthogonal,
			FormatSpecifier _fsSeries, FormatSpecifier _fsPercentile,
			Location _lo, double _dSize, RunTimeContext _rtc )
			throws ChartException
	{
		if ( _dp == null )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.UNDEFINED_VALUE,
					"exception.undefined.data.point", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE, _rtc.getLocale( ) ) );
		}
		dp = _dp;
		oBaseValue = _oBaseValue;
		oOrthogonalValue = _oOrthogonalValue;
		oSeriesValue = _oSeriesValue;
		oPercentileOrthogonalValue = _oPercentileValue;

		fsBase = _fsBase;
		fsOrthogonal = _fsOrthogonal;
		fsSeries = _fsSeries;
		fsPercentile = _fsPercentile;

		lo = _lo;
		rtc = _rtc;

		dSize = new double[2];
		dSize[0] = _dSize;
	}

	/**
	 * The constructor.
	 * 
	 * @param _oBaseValue
	 * @param _oOrthogonalValue
	 * @param _oSeriesValue
	 * @param _dp
	 * @param _fsBase
	 * @param _fsOrthogonal
	 * @param _fsSeries
	 * @param _lo
	 * @param _dSize
	 * @param _rtc
	 * @throws ChartException
	 */
	public DataPointHints( Object _oBaseValue,
			Object _oOrthogonalValue,
			Object _oSeriesValue,
			Object _oPercentileValue,
			DataPoint _dp, // FOR COMBINED VALUE
			// RETRIEVAL
			FormatSpecifier _fsBase, FormatSpecifier _fsOrthogonal,
			FormatSpecifier _fsSeries, FormatSpecifier _fsPercentile,
			Location _lo, double[] _dSize, RunTimeContext _rtc )
			throws ChartException
	{
		this( _oBaseValue,
				_oOrthogonalValue,
				_oSeriesValue,
				_oPercentileValue,
				_dp,
				_fsBase,
				_fsOrthogonal,
				_fsSeries,
				_fsPercentile,
				_lo,
				0,
				_rtc );

		dSize[0] = _dSize[0];
		dSize[1] = _dSize[1];
	}

	/**
	 * Returns a copy of current DataPointHints object.
	 * 
	 * @return
	 * @throws ChartException
	 */
	public DataPointHints getCopy( ) throws ChartException
	{
		return new DataPointHints( oBaseValue,
				oOrthogonalValue,
				oSeriesValue,
				oPercentileOrthogonalValue,
				dp,
				fsBase,
				fsOrthogonal,
				fsSeries,
				fsPercentile,
				lo,
				dSize,
				rtc );
	}

	/**
	 * Accumulates values to current DataPointHintes.
	 * 
	 * @param _oBaseValue
	 * @param _oOrthogonalValue
	 * @param _oSeriesValue
	 */
	public void accumulate( Object _oBaseValue, Object _oOrthogonalValue,
			Object _oSeriesValue, Object _oPercentileOrthogonalValue )
	{
		if ( oBaseValue instanceof Number )
		{
			if ( _oBaseValue instanceof Number )
			{
				oBaseValue = new Double( ( (Number) oBaseValue ).doubleValue( )
						+ ( (Number) _oBaseValue ).doubleValue( ) );
			}
			else if ( _oBaseValue instanceof NumberDataElement )
			{
				oBaseValue = new Double( ( (Number) oBaseValue ).doubleValue( )
						+ ( (NumberDataElement) _oBaseValue ).getValue( ) );
			}
		}
		else if ( oBaseValue instanceof NumberDataElement )
		{
			if ( _oBaseValue instanceof Number )
			{
				( (NumberDataElement) oBaseValue ).setValue( ( (NumberDataElement) oBaseValue ).getValue( )
						+ ( (Number) _oBaseValue ).doubleValue( ) );
			}
			else if ( _oBaseValue instanceof NumberDataElement )
			{
				( (NumberDataElement) oBaseValue ).setValue( ( (NumberDataElement) oBaseValue ).getValue( )
						+ ( (NumberDataElement) _oBaseValue ).getValue( ) );
			}
		}

		if ( oOrthogonalValue instanceof Number )
		{
			if ( _oOrthogonalValue instanceof Number )
			{
				oOrthogonalValue = new Double( ( (Number) oOrthogonalValue ).doubleValue( )
						+ ( (Number) _oOrthogonalValue ).doubleValue( ) );
			}
			else if ( _oOrthogonalValue instanceof NumberDataElement )
			{
				oOrthogonalValue = new Double( ( (Number) oOrthogonalValue ).doubleValue( )
						+ ( (NumberDataElement) _oOrthogonalValue ).getValue( ) );
			}
		}
		else if ( oOrthogonalValue instanceof NumberDataElement )
		{
			if ( _oOrthogonalValue instanceof Number )
			{
				( (NumberDataElement) oOrthogonalValue ).setValue( ( (NumberDataElement) oOrthogonalValue ).getValue( )
						+ ( (Number) _oOrthogonalValue ).doubleValue( ) );
			}
			else if ( _oOrthogonalValue instanceof NumberDataElement )
			{
				( (NumberDataElement) oOrthogonalValue ).setValue( ( (NumberDataElement) oOrthogonalValue ).getValue( )
						+ ( (NumberDataElement) _oOrthogonalValue ).getValue( ) );
			}
		}

		if ( oSeriesValue instanceof Number )
		{
			if ( _oSeriesValue instanceof Number )
			{
				oSeriesValue = new Double( ( (Number) oSeriesValue ).doubleValue( )
						+ ( (Number) _oSeriesValue ).doubleValue( ) );
			}
			else if ( _oSeriesValue instanceof NumberDataElement )
			{
				oSeriesValue = new Double( ( (Number) oSeriesValue ).doubleValue( )
						+ ( (NumberDataElement) _oSeriesValue ).getValue( ) );
			}
		}
		else if ( oSeriesValue instanceof NumberDataElement )
		{
			if ( _oSeriesValue instanceof Number )
			{
				( (NumberDataElement) oSeriesValue ).setValue( ( (NumberDataElement) oSeriesValue ).getValue( )
						+ ( (Number) _oSeriesValue ).doubleValue( ) );
			}
			else if ( _oSeriesValue instanceof NumberDataElement )
			{
				( (NumberDataElement) oSeriesValue ).setValue( ( (NumberDataElement) oSeriesValue ).getValue( )
						+ ( (NumberDataElement) _oSeriesValue ).getValue( ) );
			}
		}

		if ( oPercentileOrthogonalValue instanceof Number )
		{
			if ( _oPercentileOrthogonalValue instanceof Number )
			{
				oPercentileOrthogonalValue = new Double( ( (Number) oPercentileOrthogonalValue ).doubleValue( )
						+ ( (Number) _oPercentileOrthogonalValue ).doubleValue( ) );
			}
			else if ( _oPercentileOrthogonalValue instanceof NumberDataElement )
			{
				oPercentileOrthogonalValue = new Double( ( (Number) oPercentileOrthogonalValue ).doubleValue( )
						+ ( (NumberDataElement) _oPercentileOrthogonalValue ).getValue( ) );
			}
		}
		else if ( oPercentileOrthogonalValue instanceof NumberDataElement )
		{
			if ( _oPercentileOrthogonalValue instanceof Number )
			{
				( (NumberDataElement) oPercentileOrthogonalValue ).setValue( ( (NumberDataElement) oPercentileOrthogonalValue ).getValue( )
						+ ( (Number) _oPercentileOrthogonalValue ).doubleValue( ) );
			}
			else if ( _oPercentileOrthogonalValue instanceof NumberDataElement )
			{
				( (NumberDataElement) oPercentileOrthogonalValue ).setValue( ( (NumberDataElement) oPercentileOrthogonalValue ).getValue( )
						+ ( (NumberDataElement) _oPercentileOrthogonalValue ).getValue( ) );
			}
		}

	}

	/**
	 * Returns the base value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final Object getBaseValue( )
	{
		return oBaseValue;
	}

	/**
	 * Returns the orthogonal value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final Object getOrthogonalValue( )
	{
		return oOrthogonalValue;
	}

	/**
	 * Returns the series value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final Object getSeriesValue( )
	{
		return oSeriesValue;
	}

	/**
	 * Returns the percentile orthogonal value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final Object getPercentileOrthogonalValue( )
	{
		return oPercentileOrthogonalValue;
	}

	/**
	 * Returns the location value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final Location getLocation( )
	{
		return lo;
	}

	/**
	 * Returns the 3d location value of current DataPointHintes(only available
	 * in 3d mode).
	 * 
	 * @return
	 */
	public final Location3D getLocation3D( )
	{
		if ( lo instanceof Location3D )
		{
			return (Location3D) lo;
		}

		return null;
	}

	/**
	 * Returns the size value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final double getSize( )
	{
		return dSize[0];
	}

	/**
	 * Returns the size value of current DataPointHintes(only available in 3d
	 * mode).
	 * 
	 * @return
	 */
	public final Size getSize2D( )
	{
		return SizeImpl.create( dSize[0], dSize[1] );
	}

	/**
	 * Returns the user value of current DataPointHintes.
	 * 
	 * @param key
	 * @return
	 */
	public final Object getUserValue( String key )
	{
		if ( userValueMap == null )
		{
			return null;
		}

		return userValueMap.get( key );
	}

	/**
	 * Sets the user value of current DataPointHintes.
	 * 
	 * @param key
	 * @param value
	 */
	public final void setUserValue( String key, Object value )
	{
		if ( userValueMap == null )
		{
			userValueMap = new HashMap( );
		}

		userValueMap.put( key, value );
	}

	/**
	 * Returns the orthogonal display value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final String getOrthogonalDisplayValue( )
	{
		return getOrthogonalDisplayValue( fsOrthogonal );
	}

	/**
	 * Returns the base display value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final String getBaseDisplayValue( )
	{
		return getBaseDisplayValue( fsBase );
	}

	/**
	 * Returns the series display value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final String getSeriesDisplayValue( )
	{
		return getSeriesDisplayValue( fsSeries );
	}

	/**
	 * Returns the percentile orthogonal display value of current
	 * DataPointHintes.
	 * 
	 * @return
	 */
	public final String getPercentileOrthogonalDisplayValue( )
	{
		return getPercentileOrthogonalDisplayValue( fsPercentile );
	}

	/**
	 * Returns the base display value of current DataPointHintes using given
	 * format specifier.
	 * 
	 * @return
	 */
	private final String getBaseDisplayValue( FormatSpecifier fs )
	{
		if ( oBaseValue == null )
		{
			return IConstants.NULL_STRING;
		}
		try
		{
			return ValueFormatter.format( oBaseValue,
					fs,
					rtc.getULocale( ),
					null );
		}
		catch ( Exception ex )
		{
			logger.log( ILogger.ERROR,
					Messages.getString( "exception.parse.value.format.specifier", //$NON-NLS-1$
							new Object[]{
									oBaseValue, fs
							},
							rtc.getULocale( ) ) );
		}
		return IConstants.NULL_STRING;
	}

	/**
	 * Returns the orthogonal display value of current DataPointHintes using
	 * given format specifier.
	 * 
	 * @return
	 */
	private final String getOrthogonalDisplayValue( FormatSpecifier fs )
	{
		if ( oOrthogonalValue == null )
		{
			return IConstants.NULL_STRING;
		}
		try
		{
			return ValueFormatter.format( oOrthogonalValue,
					fs,
					rtc.getULocale( ),
					null );
		}
		catch ( Exception ex )
		{
			logger.log( ILogger.ERROR,
					Messages.getString( "exception.parse.value.format.specifier", //$NON-NLS-1$
							new Object[]{
									oOrthogonalValue, fs
							},
							rtc.getULocale( ) ) );
		}
		return String.valueOf( oOrthogonalValue );
	}

	/**
	 * Returns the series display value of current DataPointHintes using given
	 * format specifier.
	 * 
	 * @return
	 */
	private final String getSeriesDisplayValue( FormatSpecifier fs )
	{
		if ( oSeriesValue == null )
		{
			return IConstants.NULL_STRING;
		}
		try
		{
			return ValueFormatter.format( oSeriesValue,
					fs,
					rtc.getULocale( ),
					null );
		}
		catch ( Exception ex )
		{
			logger.log( ILogger.ERROR,
					Messages.getString( "exception.parse.value.format.specifier", //$NON-NLS-1$
							new Object[]{
									oSeriesValue, fs
							},
							rtc.getULocale( ) ) );
		}
		return IConstants.NULL_STRING;
	}

	/**
	 * Returns the percentile orthogonal display value of current
	 * DataPointHintes using given format specifier.
	 * 
	 * @return
	 */
	private final String getPercentileOrthogonalDisplayValue( FormatSpecifier fs )
	{
		if ( oPercentileOrthogonalValue == null )
		{
			return IConstants.NULL_STRING;
		}
		try
		{
			return ValueFormatter.format( oPercentileOrthogonalValue,
					fs,
					rtc.getULocale( ),
					null );
		}
		catch ( Exception ex )
		{
			logger.log( ILogger.ERROR,
					Messages.getString( "exception.parse.value.format.specifier", //$NON-NLS-1$
							new Object[]{
									oPercentileOrthogonalValue, fs
							},
							rtc.getULocale( ) ) );
		}
		return String.valueOf( oPercentileOrthogonalValue );
	}

	/**
	 * Returns the display value of current DataPointHintes.
	 * 
	 * @return
	 */
	public final String getDisplayValue( )
	{
		final EList el = dp.getComponents( );
		final StringBuffer sb = new StringBuffer( );

		if ( dp.getPrefix( ) != null )
		{
			sb.append( dp.getPrefix( ) );
		}
		DataPointComponent dpc;
		DataPointComponentType dpct;

		for ( int i = 0; i < el.size( ); i++ )
		{
			dpc = (DataPointComponent) el.get( i );
			dpct = dpc.getType( );
			if ( dpct == DataPointComponentType.BASE_VALUE_LITERAL )
			{
				sb.append( getBaseDisplayValue( ) );
			}
			else if ( dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL )
			{
				sb.append( getOrthogonalDisplayValue( ) );
			}
			else if ( dpct == DataPointComponentType.SERIES_VALUE_LITERAL )
			{
				sb.append( getSeriesDisplayValue( ) );
			}
			else if ( dpct == DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL )
			{
				sb.append( getPercentileOrthogonalDisplayValue( ) );
			}

			if ( i < el.size( ) - 1 )
			{
				sb.append( dp.getSeparator( ) );
			}
		}
		if ( dp.getSuffix( ) != null )
		{
			sb.append( dp.getSuffix( ) );
		}
		return sb.toString( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString( )
	{
		return Messages.getString( "info.datapoint.to.string", //$NON-NLS-1$
				new Object[]{
						this, getDisplayValue( )
				},
				rtc.getULocale( ) );
	}
}