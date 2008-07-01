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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.eclipse.birt.chart.model.attribute.AxisType;
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
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
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
	public static final double EPS = 1E-9;	
	private static final String EPS_FORMAT = "%.9f";//$NON-NLS-1$	
	
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
	 * Formats the double value with fixed precision.
	 */
	public static String formatDouble( double value )
	{
		return String.format( EPS_FORMAT, value );
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
	 * Create row full expression of value series.
	 * 
	 * @param expr
	 * @param orthSD
	 * @param categorySD
	 * @return
	 * @throws ChartException
	 * @since 2.3
	 */
	public static String createValueSeriesRowFullExpression( String expr,
			SeriesDefinition orthSD, SeriesDefinition categorySD )
			throws ChartException
	{
		if ( isCubeRowExpression( expr, true ) )
		{
			return expr;
		}

		return getValueSeriesRowFullExpression( expr,
				orthSD,
				categorySD );
	}
	
	/**
	 * Checks if the expression references a cube binding name
	 * 
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 * @since 2.3
	 */
	private static boolean isCubeRowExpression( String expr, boolean hasOperation )
	{
		if ( expr == null )
		{
			return false;
		}
		String regExp = hasOperation ? ".*\\Qdata[\"\\E.*\\Q\"]\\E.*" //$NON-NLS-1$
				: "\\Qdata[\"\\E.*\\Q\"]\\E"; //$NON-NLS-1$
		return expr.matches( regExp );
	}
	
	/**
	 * Returns full expression of value series.
	 * 
	 * @param valueExpr
	 * @param orthoSD
	 * @param categorySD
	 * @return
	 * @throws ChartException
	 * @since 2.3
	 *  
	 */
	public static String getValueSeriesFullExpression( String valueExpr, SeriesDefinition orthoSD, SeriesDefinition categorySD ) throws ChartException
	{
		String returnExpr = null;
		String fullAggExpr = getFullAggregateExpression( orthoSD, categorySD );
		if ( fullAggExpr == null )
		{
			returnExpr = valueExpr;
		}
		else
		{
			returnExpr = valueExpr + "_" + fullAggExpr; //$NON-NLS-1$
		}
		return returnExpr.replaceAll( "\"", "" ); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Returns row full expression of value series.
	 *  
	 * @param valueExpr
	 * @param orthoSD
	 * @param categorySD
	 * @return
	 * @throws ChartException
	 * @since 2.3
	 * 
	 */
	public static String getValueSeriesRowFullExpression( String valueExpr, SeriesDefinition orthoSD, SeriesDefinition categorySD ) throws ChartException
	{
		String fullAggExpr = getFullAggregateExpression( orthoSD, categorySD );
		if ( fullAggExpr == null )
		{
			return valueExpr;
		}
		else
		{
			return ExpressionUtil.createRowExpression( ( valueExpr + "_" + fullAggExpr ).replaceAll( "\"", //$NON-NLS-1$ //$NON-NLS-2$
					"" ) ); //$NON-NLS-1$
		}
	}
	
	/**
	 * Return full aggregate expression which includes aggregate func and aggregate parameters.
	 * 
	 * @param orthoSD
	 * @param categorySD
	 * @return
	 * @throws ChartException
	 * @since 2.3
	 */
	public static String getFullAggregateExpression( SeriesDefinition orthoSD, SeriesDefinition categorySD ) throws ChartException
	{
		String expr = getAggregateFuncExpr( orthoSD, categorySD );
		if ( expr == null )
		{
			return null;
		}

		IAggregateFunction aFunc = PluginSettings.instance( )
				.getAggregateFunction( expr );
		Object[] parameters = ChartUtil.getAggFunParameters( orthoSD,
				categorySD );
		for ( int i = 0; i < parameters.length &&
				i < aFunc.getParametersCount( ); i++ )
		{
			String param = ( parameters[i] ) == null ? "" : (String) parameters[i]; //$NON-NLS-1$
			expr = expr + "_" + param; //$NON-NLS-1$
		}
		return expr;
	}
	
	/**
	 * Returns value of aggregate function parameters.
	 * 
	 * @param orthSD
	 * @param baseSD
	 * @return
	 * @since 2.3
	 */
	public static Object[] getAggFunParameters( SeriesDefinition orthSD,
			SeriesDefinition baseSD )
	{
		if ( baseSD.getGrouping( ) != null &&
				baseSD.getGrouping( ).isSetEnabled( ) &&
				baseSD.getGrouping( ).isEnabled( ) )
		{
			SeriesGrouping grouping = orthSD.getGrouping( );
			if ( grouping.isSetEnabled( ) && grouping.isEnabled( ) )
			{
				// Set own group
				return grouping.getAggregateParameters( ).toArray( );
			}

			return baseSD.getGrouping( ).getAggregateParameters( ).toArray( );
		}
		else
		{
			return orthSD.getGrouping( ).getAggregateParameters( ).toArray( );
		}
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
	 * @return aggregation function name or null
	 * @throws ChartException
	 * @since BIRT 2.3
	 */
	public static String getAggregateFuncExpr( SeriesDefinition orthSD,
			SeriesDefinition baseSD ) throws ChartException
	{
		String strBaseAggExp = null;
		if ( baseSD.getGrouping( ) != null
				&& baseSD.getGrouping( ).isSetEnabled( )
				&& baseSD.getGrouping( ).isEnabled( ) )
		{
			strBaseAggExp = baseSD.getGrouping( ).getAggregateExpression( );
		}
		strBaseAggExp = getAggregateFunctionExpr( orthSD, strBaseAggExp );
		if ( strBaseAggExp != null && strBaseAggExp.trim( ).length( ) == 0 )
		{
			strBaseAggExp = null;
		}
		return strBaseAggExp;
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
		return PluginSettings.DefaultAggregations.COUNT.equals( aggFunc )
				|| PluginSettings.DefaultAggregations.DISTINCT_COUNT.equals( aggFunc )
				|| PluginSettings.DefaultAggregations.TOP.equals( aggFunc )
				|| PluginSettings.DefaultAggregations.TOP_PERCENT.equals( aggFunc )
				|| PluginSettings.DefaultAggregations.BOTTOM.equals( aggFunc )
				|| PluginSettings.DefaultAggregations.BOTTOM_PERCENT.equals( aggFunc )
				|| PluginSettings.DefaultAggregations.RANK.equals( aggFunc )
				|| PluginSettings.DefaultAggregations.PERCENT_RANK.equals( aggFunc );
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
	 * value is less than EPS, and if dValue is lager than 1E15, the maximum
	 * count of significant digit is set to 15
	 * 
	 * @param dValue
	 * @param bForce
	 * @return
	 */
	public static double alignWithInt( double dValue, boolean bForced )
	{
		int power = (int) ( Math.log10( dValue ) );

		if ( power < 16 )
		{
			long lValue = Math.round( dValue );

			if ( bForced || ChartUtil.mathEqual( dValue, lValue ) )
			{
				dValue = lValue;
			}

			return dValue;
		}
		else
		{
			double dPower = Math.pow( 10, power - 14 );
			long lValue = Math.round( dValue / dPower );
			return lValue * dPower;
		}
	}

	/**
	 * Returns all instances of <code>SeriesDefinition</code> on category of
	 * chart.
	 * 
	 * @param chart
	 *            chart model object.
	 * @return a list of instances of <code>SeriesDefinition</code>.
	 * @since 2.3
	 */
	public static EList<SeriesDefinition> getBaseSeriesDefinitions( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return ( (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 ) ).getSeriesDefinitions( );
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			return ( (ChartWithoutAxes) chart ).getSeriesDefinitions( );
		}
		return null;
	}

	/**
	 * Return specified axis definitions or all series definitions. Remember
	 * return type is ArrayList, not EList, no event is fired when adding or
	 * removing an element.
	 * 
	 * @param chart
	 *            chart
	 * @return specified axis definitions or all series definitions
	 * @since 2.3
	 */
	public static List<SeriesDefinition> getAllOrthogonalSeriesDefinitions(
			Chart chart )
	{
		List<SeriesDefinition> seriesList = new ArrayList<SeriesDefinition>( );
		if ( chart instanceof ChartWithAxes )
		{
			EList axisList = ( (Axis) ( (ChartWithAxes) chart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( );
			for ( int i = 0; i < axisList.size( ); i++ )
			{
				seriesList.addAll( ( (Axis) axisList.get( i ) ).getSeriesDefinitions( ) );
			}
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			seriesList.addAll( ( (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ) );
		}
		return seriesList;
	}
	
	
	/**
	 * Create a regular row expression for the matching operation.
	 * 
	 * @param expression
	 *            specified expression.
	 * @param hasOperation
	 *            indicate if the expression will include operations.
	 * @return a regular row expression
	 * @since 2.3
	 */
	public static String createRegularRowExpression( String expression,
			boolean hasOperation )
	{
		if ( expression == null )
		{
			return null;
		}

		String regularExpr = "row\\[\\\"" + expression + "\\\"\\]"; //$NON-NLS-1$ //$NON-NLS-2$
		if ( hasOperation )
		{
			regularExpr = ".*" + regularExpr + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return regularExpr;
	}
	
	/**
	 * Returns all value expressions of chart.
	 * 
	 * @param cm
	 * @return
	 * @since 2.3
	 */
	public static String[] getValueSeriesExpressions( Chart cm )
	{
		Set<String> valueExprs = new LinkedHashSet( );
		List<SeriesDefinition> orthSDs = ChartUtil.getAllOrthogonalSeriesDefinitions( cm );
		for ( SeriesDefinition sd : orthSDs )
		{
			Series s = sd.getDesignTimeSeries( );
			EList<Query> queries = s.getDataDefinition( );
			for ( Query q : queries )
			{
				if ( q.getDefinition( ) != null
						&& !"".equals( q.getDefinition( ).trim( ) ) ) //$NON-NLS-1$
				{
					valueExprs.add( q.getDefinition( ) );
				}
			}
		}

		return valueExprs.toArray( new String[valueExprs.size( )] );
	}

	/**
	 * Returns all Y optional expressions of chart.
	 * 
	 * @param cm
	 * @return
	 * @since 2.3
	 */
	public static String[] getYOptoinalExpressions( Chart cm )
	{
		Set<String> yOptionalExprs = new LinkedHashSet<String>( );
		List<SeriesDefinition> orthSDs = ChartUtil.getAllOrthogonalSeriesDefinitions( cm );
		for ( SeriesDefinition sd : orthSDs )
		{
			if ( sd.getQuery( ) != null
					&& sd.getQuery( ).getDefinition( ) != null
					&& !"".equals( sd.getQuery( ) //$NON-NLS-1$
							.getDefinition( ) ) )
			{
				yOptionalExprs.add( sd.getQuery( ).getDefinition( ) );
			}
		}

		return yOptionalExprs.toArray( new String[yOptionalExprs.size( )] );
	}

	/**
	 * Returns all category expressions of chart.
	 * 
	 * @param cm
	 * @return
	 * @since 2.3
	 */
	public static String[] getCategoryExpressions( Chart cm )
	{
		Set<String> categoryExprs = new LinkedHashSet<String>( );
		EList<SeriesDefinition> baseSDs = ChartUtil.getBaseSeriesDefinitions( cm );
		for ( SeriesDefinition sd : baseSDs )
		{
			EList<Query> dds = sd.getDesignTimeSeries( ).getDataDefinition( );
			for ( Query q : dds )
			{
				if ( q.getDefinition( ) != null
						&& !"".equals( q.getDefinition( ) ) ) //$NON-NLS-1$
				{
					categoryExprs.add( q.getDefinition( ) );
				}
			}
		}
		return categoryExprs.toArray( new String[categoryExprs.size( )] );
	}
	
	/**
	 * Compare version number, the format of version number should be X.X.X
	 * style.
	 * 
	 * @param va
	 *            version number 1.
	 * @param vb
	 *            version number 2.
	 * @since 2.3
	 */
	public static int compareVersion( String va, String vb )
	{
		String[] vas = va.split( "\\." ); //$NON-NLS-1$
		String[] vbs = vb.split( "\\." ); //$NON-NLS-1$

		List<String> vaList = new ArrayList<String>( );
		for ( int i = 0; i < vas.length; i++ )
		{
			vaList.add( vas[i].trim( ).equals( "" ) ? "0" : vas[i] ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		List<String> vbList = new ArrayList<String>( );
		for ( int i = 0; i < vbs.length; i++ )
		{
			vbList.add( vbs[i].trim( ).equals( "" ) ? "0" : vbs[i] ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if ( vas.length < vbs.length )
		{
			for ( int i = vas.length; i < vbs.length; i++ )
			{
				vaList.add( "0" ); //$NON-NLS-1$
			}
		}
		else if ( vas.length > vbs.length )
		{
			for ( int i = vbs.length; i < vas.length; i++ )
			{
				vbList.add( "0" ); //$NON-NLS-1$
			}
		}

		for ( int i = 0; i < vaList.size( ); i++ )
		{
			int a = Integer.valueOf( vaList.get( i ) ).intValue( );
			int b = Integer.valueOf( vbList.get( i ) ).intValue( );
			if ( a == b )
			{
				continue;
			}
			else
			{
				return a - b;
			}
		}

		return 0;
	}

	public static String[] getStringTokens( String str )
	{
		// No ESC, return API results
		if ( str.indexOf( "\\," ) < 0 ) //$NON-NLS-1$
		{
			return str.split( "," ); //$NON-NLS-1$
		}

		ArrayList<String> list = new ArrayList<String>( );
		char[] charArray = ( str + "," ).toCharArray( ); //$NON-NLS-1$
		int startIndex = 0;
		for ( int i = 0; i < charArray.length; i++ )
		{
			char c = charArray[i];
			if ( c == ',' )
			{
				if ( charArray[i - 1] != '\\' && i > 0 )
				{
					list.add( str.substring( startIndex, i )
							.replaceAll( "\\\\,", "," ) //$NON-NLS-1$ //$NON-NLS-2$
							.trim( ) );
					startIndex = i + 1;
				}
			}
		}
		return list.toArray( new String[list.size( )] );
	}

	/**
	 * Creates new sample data according to specified axis type.
	 * 
	 * @param axisType
	 *            axis type
	 * @param index
	 *            sample data index
	 */
	public static String getNewSampleData( AxisType axisType, int index )
	{
		if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
		{
			String dsRepresentation = "01/05/2000,02/01/2000,04/12/2000,03/12/2000,02/29/2000"; //$NON-NLS-1$
			String[] strTok = getStringTokens( dsRepresentation );
			StringBuffer sb = new StringBuffer( );
			for ( int i = 0; i < strTok.length; i++ )
			{
				String strDataElement = strTok[i];
				SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" ); //$NON-NLS-1$

				try
				{
					Date dateElement = sdf.parse( strDataElement );
					long value;
					if ( ( i * index ) % 2 == 0 )
					{
						value = dateElement.getTime( )
								+ ( dateElement.getTime( ) * index )
								/ 10;
					}
					else
					{
						value = dateElement.getTime( )
								- ( dateElement.getTime( ) * index )
								/ 10;
					}
					dateElement.setTime( value );
					sb.append( sdf.format( dateElement ) );
				}
				catch ( ParseException e1 )
				{
					e1.printStackTrace( );
				}

				if ( i < strTok.length - 1 )
				{
					sb.append( "," ); //$NON-NLS-1$
				}
			}
			return sb.toString( );
		}
		else if ( axisType.equals( AxisType.TEXT_LITERAL ) )
		{
			return "'A','B','C','D','E'"; //$NON-NLS-1$
		}

		String dsRepresentation = "6,4,12,8,10"; //$NON-NLS-1$
		String[] strTok = getStringTokens( dsRepresentation );
		StringBuffer sb = new StringBuffer( );
		for ( int i = 0; i < strTok.length; i++ )
		{
			String strDataElement = strTok[i];
			NumberFormat nf = NumberFormat.getNumberInstance( );

			try
			{
				Number numberElement = nf.parse( strDataElement );
				double value = numberElement.doubleValue( )
						* ( index + 1 )
						+ i
						* index;
				sb.append( (int) value );
			}
			catch ( ParseException e1 )
			{
				e1.printStackTrace( );
			}

			if ( i < strTok.length - 1 )
			{
				sb.append( "," ); //$NON-NLS-1$
			}
		}
		if ( index > 0 )
		{
			return sb.reverse( ).toString( );
		}
		else
		{
			return sb.toString( );
		}
	}
	
	
	/**
	 * backtraces the chart model from a given series
	 * 
	 * @param series
	 * @return
	 */
	public static Chart getChartFromSeries( Series series )
	{
		Chart cm = null;
		EObject e = series.eContainer( );

		int loop_limit = 10;
		while ( e != null && loop_limit-- > 0 )
		{
			if ( e instanceof Chart )
			{
				cm = (Chart) e;
				break;
			}
			e = e.eContainer( );
		}

		return cm;
	}
	
}
