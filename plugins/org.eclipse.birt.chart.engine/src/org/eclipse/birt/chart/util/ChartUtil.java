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

package org.eclipse.birt.chart.util;

import java.text.MessageFormat;
import java.util.Iterator;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.computation.Polygon;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * Utility class for Charts.
 */
public class ChartUtil
{

	/**
	 * Precision for chart rendering. Increase this to avoid unnecessary
	 * precision check.
	 */
	private static final double EPS = 1E-9;
	
	/**
	 * Default max row count that will be supported in charts.
	 */
	private static final int DEFAULT_MAX_ROW_COUNT = 10000;
	
	/**
	 * The constant defined as the key in RuntimeContext or JVM arguments, to
	 * represent the value of chart max row number.
	 */
	public static final String CHART_MAX_ROW = "CHART_MAX_ROW"; //$NON-NLS-1$	
	
	/**
	 * Returns if the given color definition is totally transparent. e.g.
	 * transparency==0.
	 * 
	 * @param cdef
	 * @return if the given color definition is totally transparent
	 */
	public static final boolean isColorTransparent( ColorDefinition cdef )
	{
		return cdef == null
				|| ( cdef.isSetTransparency( ) && cdef.getTransparency( ) == 0 );
	}

	/**
	 * Returns if the given label has defined a shadow.
	 * 
	 * @param la
	 * @return if the given label has defined a shadow.
	 */
	public static final boolean isShadowDefined( Label la )
	{
		return !isColorTransparent( la.getShadowColor( ) );
	}

	/**
	 * Returns if the given two double values are equal within a small
	 * precision.
	 * 
	 * @param v1
	 * @param v2
	 */
	public static final boolean mathEqual( double v1, double v2 )
	{
		return Math.abs( v1 - v2 ) < EPS;
	}

	/**
	 * Returns if the given two double values are not equal within a small
	 * precision.
	 * 
	 * @param v1
	 * @param v2
	 */
	public static final boolean mathNE( double v1, double v2 )
	{
		return Math.abs( v1 - v2 ) >= EPS;
	}

	/**
	 * Returns if the given left double value is less than the given right value
	 * within a small precision.
	 * 
	 * @param v1
	 * @param v2
	 */
	public static final boolean mathLT( double lv, double rv )
	{
		return ( rv - lv ) > EPS;
	}

	/**
	 * Returns if the given left double value is less than or equals to the
	 * given right value within a small precision.
	 * 
	 * @param v1
	 * @param v2
	 */
	public static final boolean mathLE( double lv, double rv )
	{
		return ( rv - lv ) > EPS || Math.abs( lv - rv ) < EPS;
	}

	/**
	 * Returns if the given left double value is greater than the given right
	 * value within a small precision.
	 * 
	 * @param v1
	 * @param v2
	 */
	public static final boolean mathGT( double lv, double rv )
	{
		return ( lv - rv ) > EPS;
	}

	/**
	 * Returns if the given left double value is greater than or equals to the
	 * given right value within a small precision.
	 * 
	 * @param lv
	 * @param rv
	 */
	public static final boolean mathGE( double lv, double rv )
	{
		return ( lv - rv ) > EPS || Math.abs( lv - rv ) < EPS;
	}

	/**
	 * Convert pixel value to points.
	 * 
	 * @param idsSWT
	 * @param dOriginalHeight
	 * @return points value
	 */
	public static final double convertPixelsToPoints(
			final IDisplayServer idsSWT, double dOriginalHeight )
	{
		return ( dOriginalHeight * 72d ) / idsSWT.getDpiResolution( );
	}

	/**
	 * Returns the quadrant (1-4) for given angle in degree. Specially, -1 means
	 * Zero degree. -2 means 90 degree, -3 means 180 degree, -4 means 270
	 * degree.
	 * 
	 * @param dAngle
	 * @return quadrant
	 */
	public static final int getQuadrant( double dAngle )
	{
		dAngle = dAngle - ( ( (int) dAngle ) / 360 ) * 360;

		if ( dAngle < 0 )
		{
			dAngle += 360;
		}
		if ( dAngle == 0 )
		{
			return -1;
		}
		if ( dAngle == 90 )
		{
			return -2;
		}
		if ( dAngle == 180 )
		{
			return -3;
		}
		if ( dAngle == 270 )
		{
			return -4;
		}
		if ( dAngle >= 0 && dAngle < 90 )
		{
			return 1;
		}
		if ( dAngle > 90 && dAngle < 180 )
		{
			return 2;
		}
		if ( dAngle > 180 && dAngle < 270 )
		{
			return 3;
		}
		return 4;
	}

	/**
	 * Returns if two polygons intersect each other.
	 * 
	 * @param pg1
	 * @param pg2
	 * @return if two polygons intersect each other
	 */
	public static boolean intersects( Polygon pg1, Polygon pg2 )
	{
		if ( pg1 != null )
		{
			return pg1.intersects( pg2 );
		}

		return false;
	}

	/**
	 * Merges two fonts to the original one from a source. The original one can
	 * not be null. ?Only consider inheritable properties.
	 * 
	 * @param original
	 * @param source
	 */
	public static void mergeFont( FontDefinition original, FontDefinition source )
	{
		if ( source != null )
		{
			if ( original.getAlignment( ) == null )
			{
				original.setAlignment( source.getAlignment( ) );
			}
			else if ( !original.getAlignment( ).isSetHorizontalAlignment( )
					&& source.getAlignment( ) != null )
			{
				original.getAlignment( )
						.setHorizontalAlignment( source.getAlignment( )
								.getHorizontalAlignment( ) );
			}
			if ( original.getName( ) == null )
			{
				original.setName( source.getName( ) );
			}
			if ( !original.isSetBold( ) )
			{
				original.setBold( source.isBold( ) );
			}
			if ( !original.isSetItalic( ) )
			{
				original.setItalic( source.isItalic( ) );
			}
			if ( !original.isSetRotation( ) )
			{
				original.setRotation( source.getRotation( ) );
			}
			if ( !original.isSetSize( ) )
			{
				original.setSize( source.getSize( ) );
			}
			if ( !original.isSetWordWrap( ) )
			{
				original.setWordWrap( source.isWordWrap( ) );
			}
			if ( !original.isSetUnderline( ) )
			{
				original.setUnderline( source.isUnderline( ) );
			}
			if ( !original.isSetStrikethrough( ) )
			{
				original.setStrikethrough( source.isStrikethrough( ) );
			}
		}
	}

	/**
	 * Returns the string representation for given object. null for null object.
	 * 
	 * @param value
	 * @return string value
	 */
	public static String stringValue( Object value )
	{
		if ( value == null )
		{
			return null;
		}

		return String.valueOf( value );
	}

	/**
	 * Converts Fill if possible. If Fill is MultipleFill type, convert to
	 * positive/negative Color according to the value. If not MultipleFill type,
	 * return original fill for positive value, or negative fill for negative
	 * value.
	 * 
	 * @param fill
	 *            Fill to convert
	 * @param dValue
	 *            numeric value
	 * @param fNegative
	 *            Fill for negative value. Useless for positive value or
	 *            MultipleFill
	 */
	public static Fill convertFill( Fill fill, double dValue, Fill fNegative )
	{
		if ( dValue >= 0 )
		{
			if ( fill instanceof MultipleFill )
			{
				fill = ColorDefinitionImpl.copyInstance( (ColorDefinition) ( (MultipleFill) fill ).getFills( )
						.get( 0 ) );
			}
		}
		else
		{
			if ( fill instanceof MultipleFill )
			{
				fill = ColorDefinitionImpl.copyInstance( (ColorDefinition) ( (MultipleFill) fill ).getFills( )
						.get( 1 ) );
			}
			else if ( fNegative != null )
			{
				fill = fNegative;
			}
		}
		return fill;
	}
	
	/**
	 * Transposes the anchor
	 * 
	 * @param an
	 *            anchor
	 * 
	 */
	public static Anchor transposeAnchor( Anchor an )
			throws IllegalArgumentException
	{
		if ( an == null )
		{
			return null; // CENTERED ANCHOR
		}

		switch ( an.getValue( ) )
		{
			case Anchor.NORTH :
				return Anchor.EAST_LITERAL;
			case Anchor.SOUTH :
				return Anchor.WEST_LITERAL;
			case Anchor.EAST :
				return Anchor.NORTH_LITERAL;
			case Anchor.WEST :
				return Anchor.SOUTH_LITERAL;
			case Anchor.NORTH_WEST :
				return Anchor.SOUTH_EAST_LITERAL;
			case Anchor.NORTH_EAST :
				return Anchor.NORTH_EAST_LITERAL;
			case Anchor.SOUTH_WEST :
				return Anchor.SOUTH_WEST_LITERAL;
			case Anchor.SOUTH_EAST :
				return Anchor.NORTH_WEST_LITERAL;
		}
		throw new IllegalArgumentException( MessageFormat.format( Messages.getResourceBundle( )
				.getString( "exception.anchor.transpose" ), //$NON-NLS-1$ 
				new Object[]{
					an
				} )

		);
	}
	
	public static TextAlignment transposeAlignment( TextAlignment ta )
	{
		if ( ta == null )
		{
			return null;
		}
		
		HorizontalAlignment ha = ta.getHorizontalAlignment( );
		VerticalAlignment va = ta.getVerticalAlignment( );
		switch ( ha.getValue( ) )
		{
			case HorizontalAlignment.LEFT:
				ta.setVerticalAlignment( VerticalAlignment.BOTTOM_LITERAL );
				break;
			case HorizontalAlignment.RIGHT:
				ta.setVerticalAlignment( VerticalAlignment.TOP_LITERAL );
				break;
			case HorizontalAlignment.CENTER:
				ta.setVerticalAlignment( VerticalAlignment.CENTER_LITERAL );
		}
		
		switch ( va.getValue( ) )
		{
			case VerticalAlignment.BOTTOM:
				ta.setHorizontalAlignment( HorizontalAlignment.LEFT_LITERAL );
				break;
			case VerticalAlignment.TOP:
				ta.setHorizontalAlignment( HorizontalAlignment.RIGHT_LITERAL );
				break;
			case VerticalAlignment.CENTER:
				ta.setHorizontalAlignment( HorizontalAlignment.CENTER_LITERAL );
		}
		return ta;
	}
	
	/**
	 * Convers Scale unit type to ICU Calendar constant.
	 * 
	 * @param unitType
	 *            Scale unit type
	 * @return Calendar constant or -1 if not found
	 */
	public static int convertUnitTypeToCalendarConstant( ScaleUnitType unitType )
	{
		switch ( unitType.getValue( ) )
		{
			case ScaleUnitType.DAYS :
				return Calendar.DATE;
			case ScaleUnitType.HOURS :
				return Calendar.HOUR_OF_DAY;
			case ScaleUnitType.MINUTES :
				return Calendar.MINUTE;
			case ScaleUnitType.MONTHS :
				return Calendar.MONTH;
			case ScaleUnitType.SECONDS :
				return Calendar.SECOND;
			case ScaleUnitType.WEEKS :
				return Calendar.WEEK_OF_YEAR;
			case ScaleUnitType.YEARS :
				return Calendar.YEAR;
		}
		return -1;
	}
	
	/**
	 * Returns max row count that will be supported in charts. Users can set it
	 * in JVM argument "CHART_MAX_ROW" or RuntimeContext. Default value is 10000.
	 * 
	 * @return max row count that will be supported in charts.
	 * @since 2.2.0
	 */
	public static int getSupportedMaxRowCount( RunTimeContext rtc )
	{
		int iMaxRowCount = DEFAULT_MAX_ROW_COUNT;

		// To get value from runtime context first
		Object contextMaxRow = rtc.getState( CHART_MAX_ROW );
		if ( contextMaxRow != null )
		{
			iMaxRowCount = ( (Number) contextMaxRow ).intValue( );
		}
		else
		{
			// Then to get value from JVM
			String jvmMaxRow = System.getProperty( CHART_MAX_ROW );
			if ( jvmMaxRow != null )
			{
				try
				{
					iMaxRowCount = Integer.parseInt( jvmMaxRow );
				}
				catch ( NumberFormatException e )
				{
					iMaxRowCount = DEFAULT_MAX_ROW_COUNT;
				}
			}
		}
		// In case of negative value
		if ( iMaxRowCount <= 0 )
		{
			iMaxRowCount = DEFAULT_MAX_ROW_COUNT;
		}
		return iMaxRowCount;
	}
	
	/**
	 * Gets all supported output formats.
	 * 
	 * @return string array of output formats
	 * @since 2.2
	 */
	public static String[] getSupportedOutputFormats( ) throws ChartException
	{
		String[][] outputFormatArray = PluginSettings.instance( )
				.getRegisteredOutputFormats( );
		String[] formats = new String[outputFormatArray.length];
		for ( int i = 0; i < formats.length; i++ )
		{
			formats[i] = outputFormatArray[i][0];
		}
		return formats;
	}
	
	/**
	 * Checks current output format can be supported
	 * 
	 * @param output
	 *            current output format
	 * @return can be supported or not
	 * @throws ChartException
	 * @since 2.2
	 */
	public static boolean isOutputFormatSupport( String output )
			throws ChartException
	{
		if ( output == null || output.trim( ).length( ) == 0 )
		{
			return false;
		}
		output = output.toUpperCase( );
		String[] allTypes = getSupportedOutputFormats( );
		for ( int i = 0; i < allTypes.length; i++ )
		{
			if ( output.equals( allTypes[i] ) )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns if specified locale uses right-to-left direction. See ISO codes
	 * at http://www.unicode.org/unicode/onlinedat/languages.html RTL languages
	 * are Hebrew, Arabic, Urdu, Farsi (Persian), Yiddish
	 * 
	 * @param lcl
	 *            locale to check direction
	 * @return if specified locale uses right-to-left direction
	 * @since 2.2
	 */
	public static boolean isRightToLeftLocale( ULocale lcl )
	{
		if ( lcl != null )
		{
			String language = lcl.getLanguage( );
			if ( language.equals( "he" ) || //$NON-NLS-1$
					language.equals( "iw" ) || //$NON-NLS-1$
					language.equals( "ar" ) || //$NON-NLS-1$
					language.equals( "fa" ) || //$NON-NLS-1$
					language.equals( "ur" ) || //$NON-NLS-1$
					language.equals( "yi" ) || //$NON-NLS-1$
					language.equals( "ji" ) ) //$NON-NLS-1$ 
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks a double value is double precise. If value is 2.1, then return
	 * true; if value is 2.1000000001 or 2.099999999999, then return false.
	 * 
	 * @param dValue
	 * @return
	 */
	public static boolean checkDoublePrecise( double dValue )
	{
		if ( dValue - (int) dValue == 0 )
		{
			return true;
		}
		String sValue = String.valueOf( dValue );
		if ( sValue.length( ) < 8 )
		{
			return true;
		}
		int iPoint = sValue.indexOf( '.' );
		int iZero = sValue.lastIndexOf( "00000000" ); //$NON-NLS-1$
		if ( iZero >= iPoint )
		{
			return false;
		}
		int iNine = sValue.lastIndexOf( "99999999" ); //$NON-NLS-1$
		if ( iNine >= iPoint )
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Computes the height of orthogonal axis title. Orthogonal axis is Y axis
	 * in non-transposed direction or X axis in transpose direction. Current
	 * algorithm of Axis layout is to use Axis Scale width for category axis
	 * title, and to use the chart height except chart title section for
	 * orthogonal axis title.
	 * 
	 * @param cm
	 *            chart model
	 * @param xs
	 *            display server to compute pixel
	 * @return height of orthogonal axis title in form of pixels
	 */
	public static double computeHeightOfOrthogonalAxisTitle( ChartWithAxes cm,
			IDisplayServer xs )
	{
		Bounds chartBounds = cm.getBlock( ).getBounds( );
		Bounds titleBounds = cm.getTitle( ).getBounds( );
		Bounds legendBounds = cm.getLegend( ).getBounds( );
		int titleAnchor = cm.getTitle( ).getAnchor( ).getValue( );
		int legendPosition = cm.getLegend( ).getPosition( ).getValue( );
		if ( titleAnchor == Anchor.NORTH )
		{
			if ( legendPosition == Position.ABOVE )
			{
				return ( chartBounds.getHeight( )
						+ chartBounds.getTop( ) - legendBounds.getTop( ) - legendBounds.getHeight( ) )
						/ 72 * xs.getDpiResolution( );
			}
			else if ( legendPosition == Position.BELOW )
			{
				return ( legendBounds.getTop( ) - titleBounds.getTop( ) - titleBounds.getHeight( ) )
						/ 72 * xs.getDpiResolution( );
			}
			else
			{
				return ( chartBounds.getHeight( )
						+ chartBounds.getTop( ) - titleBounds.getTop( ) - titleBounds.getHeight( ) )
						/ 72 * xs.getDpiResolution( );
			}
		}
		else if ( titleAnchor == Anchor.SOUTH )
		{
			if ( legendPosition == Position.ABOVE )
			{
				return ( titleBounds.getTop( ) - legendBounds.getTop( ) - legendBounds.getHeight( ) )
						/ 72 * xs.getDpiResolution( );
			}
			else if ( legendPosition == Position.BELOW )
			{
				return ( legendBounds.getTop( ) - chartBounds.getTop( ) )
						/ 72 * xs.getDpiResolution( );
			}
			else
			{
				return ( titleBounds.getTop( ) - chartBounds.getTop( ) )
						/ 72 * xs.getDpiResolution( );
			}
		}
		else
		{
			if ( legendPosition == Position.ABOVE )
			{
				return ( chartBounds.getHeight( )
						+ chartBounds.getTop( ) - legendBounds.getTop( ) - legendBounds.getHeight( ) )
						/ 72 * xs.getDpiResolution( );
			}
			else if ( legendPosition == Position.BELOW )
			{
				return ( legendBounds.getTop( ) - chartBounds.getTop( ) )
						/ 72 * xs.getDpiResolution( );
			}
			else
			{
				return chartBounds.getHeight( ) / 72 * xs.getDpiResolution( );
			}
		}
	}	
	
	/**
	 * Returns grouping unit name of series grouping.
	 * 
	 * @param grouping
	 * @return
	 * @since BIRT 2.3
	 */
	public static String getGroupingUnitName( SeriesGrouping grouping )
	{
		if ( grouping.getGroupType( ) == DataType.NUMERIC_LITERAL )
		{
			return null;
		}
		else if ( grouping.getGroupType( ) == DataType.DATE_TIME_LITERAL )
		{
			if ( grouping.getGroupingUnit( ) == null )
			{
				return GroupingUnitType.SECONDS_LITERAL.getName( );
			}

			return grouping.getGroupingUnit( ).getName( );
		}
		else if ( grouping.getGroupType( ) == DataType.TEXT_LITERAL )
		{
			if ( grouping.getGroupingUnit( ) == null ||
					!GroupingUnitType.STRING_PREFIX_LITERAL.getName( )
							.equals( grouping.getGroupingUnit( ).getName( ) ) )
			{
				return GroupingUnitType.STRING_LITERAL.getName( );
			}

			return grouping.getGroupingUnit( ).getName( );
		}

		return null;
	}
	

	/**
	 * Gets the aggregation function expression
	 * 
	 * @param orthoSD
	 * @param strBaseAggExp
	 * @throws ChartException 
	 * @since BIRT 2.3
	 */
	public static String getAggregateFunctionExpr( SeriesDefinition orthoSD,
			String strBaseAggExp ) throws ChartException
	{
		String strOrthoAgg = null;
		SeriesGrouping grouping = orthoSD.getGrouping( );
		// The enabled grouping means that aggregate is set on orthogonal series.
		if ( grouping != null && grouping.isSetEnabled( ) && grouping.isEnabled( ) )
		{
			// Set own group
			strOrthoAgg = grouping.getAggregateExpression( );
			if ( strBaseAggExp == null && strOrthoAgg != null )
			{
				// If no category grouping is defined, value series aggregate
				// only allow running aggregates.
				// Check if series aggregate is running aggregate.
				IAggregateFunction aFunc = PluginSettings.instance( )
						.getAggregateFunction( strOrthoAgg );
				if ( aFunc.getType( ) != IAggregateFunction.RUNNING_AGGR )
				{
					strOrthoAgg = null;
				}
			}
		}

		// Set base group
		if ( strOrthoAgg == null || "".equals( strOrthoAgg ) ) //$NON-NLS-1$
		{
			strOrthoAgg = strBaseAggExp;
		}
		return strOrthoAgg;
	}
	

	/**
	 * Returns aggregation function expression.
	 * 
	 * @param orthSD
	 * @param baseSD
	 * @return
	 * @throws ChartException 
	 * @since BIRT 2.3
	 */
	public static String getAggregateFuncExpr( SeriesDefinition orthSD,
			SeriesDefinition baseSD ) throws ChartException
	{

		String strBaseAggExp = null;
		if ( baseSD.getGrouping( ) != null &&
				baseSD.getGrouping( ).isSetEnabled( ) &&
				baseSD.getGrouping( ).isEnabled( ) )
		{
			strBaseAggExp = baseSD.getGrouping( ).getAggregateExpression( );
		}
		return getAggregateFunctionExpr( orthSD, strBaseAggExp );
	}
	
	/**
	 * The method checks if specified aggregate function is a magic aggregate,
	 * it means these aggregates operations will change data type.
	 * <p>
	 * Now the magic aggregates in chart include Count, DistinctCount, Top,
	 * TopPercent, Bottom, BottomPercent, Rank and PercentRank.
	 * 
	 * @param aggFunc
	 * @return
	 * @since BIRT 2.3
	 */
	public static boolean isMagicAggregate(String aggFunc )
	{
		return "Count".equals( aggFunc ) //$NON-NLS-1$
				|| "DistinctCount".equals( aggFunc )  //$NON-NLS-1$
				|| "Top".equals( aggFunc ) //$NON-NLS-1$
				|| "TopPercent".equals( aggFunc ) //$NON-NLS-1$
				|| "Bottom".equals( aggFunc ) //$NON-NLS-1$
				|| "BottomPercent".equals( aggFunc ) //$NON-NLS-1$
				|| "Rank".equals( aggFunc ) //$NON-NLS-1$
				|| "PercentRank".equals( aggFunc ); //$NON-NLS-1$
	}
	
	
	/**
	 * Remove all invisible SeriesDefinitions from a EList of SeriesDefinitions.
	 * This is a help function of the pruneInvisibleSeries. ( see below )
	 * 
	 * @param elSed
	 *            (will be changed)
	 * @since 2.3
	 */
	private static void pruneInvisibleSedsFromEList( EList elSed )
	{
		Iterator itSed = elSed.iterator( );

		while ( itSed.hasNext( ) )
		{
			SeriesDefinition sed = (SeriesDefinition) itSed.next( );
			// Design time series may be null in API test
			Series ds = sed.getDesignTimeSeries( );
			if ( ds != null && !ds.isVisible( ) )
			{
				itSed.remove( );
			}
		}
	}

	/**
	 * Remove all invisible SeriesDefinitions from the runtime chart model.
	 * 
	 * @param cm
	 *            (will be changed)
	 * @since 2.3
	 */
	public static void pruneInvisibleSeries( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cmWithAxes = (ChartWithAxes) cm;
			Axis[] axBaseAxis = cmWithAxes.getBaseAxes( );

			for ( int j = 0; j < axBaseAxis.length; j++ )
			{
				Axis axBase = axBaseAxis[j];
				Axis[] axis = cmWithAxes.getOrthogonalAxes( axBase, true );

				for ( int i = 0; i < axis.length; i++ )
				{
					Axis ax = axis[i];
					pruneInvisibleSedsFromEList( ax.getSeriesDefinitions( ) );
				}
			}
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cmNoAxes = (ChartWithoutAxes) cm;
			Iterator itCata = cmNoAxes.getSeriesDefinitions( ).iterator( );

			while ( itCata.hasNext( ) )
			{
				SeriesDefinition sedCata = (SeriesDefinition) itCata.next( );
				pruneInvisibleSedsFromEList( sedCata.getSeriesDefinitions( ) );
			}
		}
	}
	
	/**
	 * Aligns a double value with a int value, if the differance between the two
	 * value is less than EPS
	 * 
	 * @param dValue
	 * @return
	 */
	public static double alignWithInt( double dValue )
	{
		long lValue = Math.round( dValue );

		if ( ChartUtil.mathEqual( dValue, lValue ) )
		{
			dValue = lValue;
		}

		return dValue;
	}

}
