/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeElementFactory;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	private static ICubeElementFactory cubeFactory = null;

	/**
	 * Specified the query expression of min aggregation binding
	 */
	public static final String QUERY_MIN = "chart__min"; //$NON-NLS-1$

	/**
	 * Specified the query expression of max aggregation binding
	 */
	public static final String QUERY_MAX = "chart__max"; //$NON-NLS-1$

	public static final String CHART_EXTENSION_NAME = "Chart";//$NON-NLS-1$

	/**
	 * Specified property names defined in ExtendedItemHandle or IReportItem
	 */
	public static final String PROPERTY_XMLPRESENTATION = "xmlRepresentation"; //$NON-NLS-1$
	public static final String PROPERTY_CHART = "chart.instance"; //$NON-NLS-1$
	public static final String PROPERTY_SCALE = "chart.scale"; //$NON-NLS-1$
	public static final String PROPERTY_SCRIPT = "script"; //$NON-NLS-1$
	public static final String PROPERTY_ONRENDER = "onRender"; //$NON-NLS-1$
	public static final String PROPERTY_OUTPUT = "outputFormat"; //$NON-NLS-1$
	public static final String PROPERTY_CUBE_FILTER = "cubeFilter";//$NON-NLS-1$
	public static final String PROPERTY_HOST_CHART = "hostChart";//$NON-NLS-1$

	public static final double DEFAULT_CHART_BLOCK_HEIGHT = 130;
	public static final double DEFAULT_CHART_BLOCK_WIDTH = 212;
	public static final double DEFAULT_AXIS_CHART_BLOCK_SIZE = 50;

	public synchronized static ICubeElementFactory getCubeElementFactory( )
			throws BirtException
	{
		if ( cubeFactory != null )
		{
			return cubeFactory;
		}

		try
		{
			Class cls = Class.forName( ICubeElementFactory.CUBE_ELEMENT_FACTORY_CLASS_NAME );
			cubeFactory = (ICubeElementFactory) cls.newInstance( );
		}
		catch ( Exception e )
		{
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.ERROR,
					e );
		}
		return cubeFactory;
	}

	/**
	 * Checks current chart is within cross tab.
	 * 
	 * @param chartHandle
	 *            the handle holding chart
	 * @return true means within cross tab, false means not
	 * @since 2.3
	 */
	public static boolean isChartInXTab( DesignElementHandle chartHandle )
	{
		DesignElementHandle container = chartHandle.getContainer( );
		if ( container instanceof ExtendedItemHandle )
		{
			String exName = ( (ExtendedItemHandle) container ).getExtensionName( );
			if ( ICrosstabConstants.AGGREGATION_CELL_EXTENSION_NAME.equals( exName ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the element handle which can save binding columns the given
	 * element
	 * 
	 * @param handle
	 *            the handle of the element which needs binding columns
	 * @return the holder for the element,or itself if no holder available
	 */
	public static ReportItemHandle getBindingHolder( DesignElementHandle handle )
	{
		if ( handle instanceof ReportElementHandle )
		{
			if ( handle instanceof ListingHandle )
			{
				return (ReportItemHandle) handle;
			}
			if ( handle instanceof ReportItemHandle )
			{
				if ( ( (ReportItemHandle) handle ).getDataBindingReference( ) != null
						|| ( (ReportItemHandle) handle ).getCube( ) != null
						|| ( (ReportItemHandle) handle ).getDataSet( ) != null
						|| ( (ReportItemHandle) handle ).columnBindingsIterator( )
								.hasNext( ) )
				{
					return (ReportItemHandle) handle;
				}
			}
			ReportItemHandle result = getBindingHolder( handle.getContainer( ) );
			if ( result == null
					&& handle instanceof ReportItemHandle
					&& !( handle instanceof GridHandle ) )
			{
				result = (ReportItemHandle) handle;
			}
			return result;
		}
		return null;
	}

	/**
	 * Checks if shared scale is needed when computation
	 * 
	 * @param eih
	 *            handle
	 * @param cm
	 *            chart model
	 * @return shared binding needed or not
	 * @since 2.3
	 */
	public static boolean canScaleShared( ExtendedItemHandle eih, Chart cm )
	{
		return cm instanceof ChartWithAxes
				&& eih.getDataSet( ) == null && getBindingHolder( eih ) != null
				&& isChartInXTab( eih );
	}

	/**
	 * @return Returns if current eclipse environment is RtL.
	 */
	public static boolean isRtl( )
	{
		// get -dir rtl option
		boolean rtl = false;
		String eclipseCommands = System.getProperty( "eclipse.commands" ); //$NON-NLS-1$
		if ( eclipseCommands != null )
		{
			String[] options = eclipseCommands.split( "-" ); //$NON-NLS-1$
			String regex = "[\\s]*[dD][iI][rR][\\s]*[rR][tT][lL][\\s]*"; //$NON-NLS-1$
			Pattern pattern = Pattern.compile( regex );
			for ( int i = 0; i < options.length; i++ )
			{
				String option = options[i];
				if ( pattern.matcher( option ).matches( ) )
				{
					rtl = true;
					break;
				}
			}
		}
		return rtl;
	}

	/**
	 * Gets all column bindings from handle and its container
	 * 
	 * @param itemHandle
	 *            handle
	 * @return Iterator of all bindings
	 */
	public static Iterator getColumnDataBindings( ReportItemHandle itemHandle )
	{
		if ( itemHandle.getDataSet( ) != null )
		{
			return itemHandle.columnBindingsIterator( );
		}
		DesignElementHandle handle = getBindingHolder( itemHandle );
		if ( handle instanceof ReportItemHandle )
		{
			ArrayList list = new ArrayList( );
			Iterator i = ( (ReportItemHandle) handle ).columnBindingsIterator( );
			while ( i.hasNext( ) )
			{
				list.add( i.next( ) );
			}
			i = itemHandle.columnBindingsIterator( );
			while ( i.hasNext( ) )
			{
				list.add( i.next( ) );
			}
			return list.iterator( );
		}
		return null;
	}

	/**
	 * Convert group unit type from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static int convertToDtEGroupUnit( DataType dataType,
			GroupingUnitType groupUnitType, double intervalRange )
	{
		if ( dataType == DataType.NUMERIC_LITERAL )
		{
			if ( intervalRange == 0 )
			{
				return IGroupDefinition.NO_INTERVAL;
			}

			return IGroupDefinition.NUMERIC_INTERVAL;
		}
		else if ( dataType == DataType.DATE_TIME_LITERAL )
		{
			switch ( groupUnitType.getValue( ) )
			{
				case GroupingUnitType.SECONDS :
					return IGroupDefinition.SECOND_INTERVAL;

				case GroupingUnitType.MINUTES :
					return IGroupDefinition.MINUTE_INTERVAL;

				case GroupingUnitType.HOURS :
					return IGroupDefinition.HOUR_INTERVAL;

				case GroupingUnitType.DAYS :
					return IGroupDefinition.DAY_INTERVAL;

				case GroupingUnitType.WEEKS :
					return IGroupDefinition.WEEK_INTERVAL;

				case GroupingUnitType.MONTHS :
					return IGroupDefinition.MONTH_INTERVAL;

				case GroupingUnitType.QUARTERS :
					return IGroupDefinition.QUARTER_INTERVAL;

				case GroupingUnitType.YEARS :
					return IGroupDefinition.YEAR_INTERVAL;
			}
		}
		else if ( dataType == DataType.TEXT_LITERAL )
		{
			switch ( groupUnitType.getValue( ) )
			{
				case GroupingUnitType.STRING_PREFIX :
					return IGroupDefinition.STRING_PREFIX_INTERVAL;
			}

			return IGroupDefinition.NO_INTERVAL;
		}

		return IGroupDefinition.NO_INTERVAL;
	}

	/**
	 * Convert interval range from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static double convertToDtEIntervalRange( DataType dataType,
			GroupingUnitType groupUnitType, double intervalRange )
	{
		double range = intervalRange;
		if ( Double.isNaN( intervalRange ) )
		{
			range = 0;
		}

		if ( dataType == DataType.DATE_TIME_LITERAL && range <= 0 )
		{
			range = 1;
		}
		else if ( dataType == DataType.TEXT_LITERAL )
		{
			return (long) range;
		}

		return range;
	}

	/**
	 * Convert sort direction from Chart's to DtE's.
	 * 
	 * @param sortOption
	 * @since BIRT 2.3
	 */
	public static int convertToDtESortDirection( SortOption sortOption )
	{
		if ( sortOption == SortOption.ASCENDING_LITERAL )
		{
			return IGroupDefinition.SORT_ASC;
		}
		else if ( sortOption == SortOption.DESCENDING_LITERAL )
		{
			return IGroupDefinition.SORT_DESC;
		}
		return IGroupDefinition.NO_SORT;
	}

	/**
	 * Convert aggregation name from Chart's to DtE's.
	 * 
	 * @param agg
	 * @since BIRT 2.3
	 */
	public static String convertToDtEAggFunction( String agg )
	{

		if ( "Sum".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_SUM;

		}
		else if ( "Average".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE;

		}
		else if ( "Count".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT;

		}
		else if ( "DistinctCount".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT;

		}
		else if ( "First".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST;

		}
		else if ( "Last".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_LAST;

		}
		else if ( "Min".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MIN;

		}
		else if ( "Max".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MAX;
		}
		else if ( "WeightedAverage".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG;
		}
		else if ( "Median".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN;
		}
		else if ( "Mode".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MODE;
		}
		else if ( "STDDEV".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV;
		}
		else if ( "Variance".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE;
		}
		else if ( "Irr".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IRR;
		}
		else if ( "Mirr".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR;
		}
		else if ( "NPV".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_NPV;
		}
		else if ( "Percentile".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE;
		}
		else if ( "Quartile".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE;
		}
		else if ( "MovingAverage".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE;
		}
		else if ( "RunningSum".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM;
		}
		else if ( "RunningNPV".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV;
		}
		else if ( "Rank".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RANK;
		}
		else if ( "Top".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N;
		}
		else if ( "TopPercent".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT;
		}
		else if ( "Bottom".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N;
		}
		else if ( "BottomPercent".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT;
		}
		else if ( "PercentRank".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK;
		}
		else if ( "PercentSum".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM;
		}
		else if ( "RunningCount".equals( agg ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT;
		}

		return null;
	}

	/**
	 * Checks if result set is empty
	 * 
	 * @param set
	 *            result set
	 * @throws BirtException
	 * @since 2.3
	 */
	public static boolean isEmpty( IBaseResultSet set ) throws BirtException
	{
		if ( set instanceof IQueryResultSet )
		{
			return ( (IQueryResultSet) set ).isEmpty( );
		}
		// TODO add code to check empty for ICubeResultSet
		return false;
	}

	/**
	 * Check if Y grouping is defined.
	 * 
	 * @param orthSeriesDefinition
	 * @since BIRT 2.3
	 */
	public static boolean isYGroupingDefined(
			SeriesDefinition orthSeriesDefinition )
	{
		if ( orthSeriesDefinition == null )
		{
			return false;
		}
		String yGroupExpr = null;
		if ( orthSeriesDefinition.getQuery( ) != null )
		{
			yGroupExpr = orthSeriesDefinition.getQuery( ).getDefinition( );
		}

		return yGroupExpr != null && !"".equals( yGroupExpr ); //$NON-NLS-1$
	}

	/**
	 * Check if base series grouping is defined.
	 * 
	 * @param baseSD
	 * @since BIRT 2.3
	 */
	public static boolean isBaseGroupingDefined( SeriesDefinition baseSD )
	{
		if ( baseSD.getGrouping( ) != null && baseSD.getGrouping( ).isEnabled( ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * Check if current chart has defined grouping.
	 * 
	 * @param cm
	 * @since BIRT 2.3
	 */
	public static boolean canContainGrouping( Chart cm )
	{
		SeriesDefinition baseSD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );

			orthAxisArray = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0], true );
			orthSD = (SeriesDefinition) ( (Axis) orthAxisArray[0] ).getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( ).get( 0 );
			orthSD = (SeriesDefinition) baseSD.getSeriesDefinitions( ).get( 0 );
		}

		if ( isBaseGroupingDefined( baseSD ) || isYGroupingDefined( orthSD ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * Check if chart has aggregation.
	 * 
	 * @param cm
	 */
	public static boolean hasAggregation( Chart cm )
	{
		SeriesDefinition baseSD = null;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( ).get( 0 );
		}

		if ( isBaseGroupingDefined( baseSD ) )
		{
			return true;
		}

		return false;
	}

	/**
	 * Finds chart report item from handle
	 * 
	 * @param eih
	 *            extended item handle with chart
	 * @since 2.3
	 */
	public static ChartReportItemImpl getChartReportItemFromHandle(
			ExtendedItemHandle eih )
	{
		ChartReportItemImpl item = null;
		if ( !"Chart".endsWith( eih.getExtensionName( ) ) ) //$NON-NLS-1$
		{
			return null;
		}
		try
		{
			item = (ChartReportItemImpl) eih.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( e );
		}
		if ( item == null )
		{
			try
			{
				eih.loadExtendedElement( );
				item = (ChartReportItemImpl) eih.getReportItem( );
			}
			catch ( ExtendedElementException eeex )
			{
				logger.log( eeex );
			}
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemPresentationImpl.log.UnableToLocateWrapper" ) ); //$NON-NLS-1$
			}
		}
		return item;
	}

	/**
	 * Finds Chart model from handle
	 * 
	 * @param handle
	 *            the handle with chart
	 * @since 2.3
	 */
	public static Chart getChartFromHandle( ExtendedItemHandle handle )
	{
		ChartReportItemImpl item = getChartReportItemFromHandle( handle );
		if ( item == null )
		{
			return null;
		}
		return (Chart) ( item ).getProperty( PROPERTY_CHART );
	}

	/**
	 * Returns the binding cube if the element or its container has cube binding
	 * or the reference to the cube
	 * 
	 * @param element
	 *            element handle
	 * @return the binding cube or null
	 * @since 2.3
	 */
	public static CubeHandle getBindingCube( DesignElementHandle element )
	{
		if ( element == null )
		{
			return null;
		}
		if ( element instanceof ReportItemHandle )
		{
			CubeHandle cube = ( (ReportItemHandle) element ).getCube( );
			if ( cube != null )
			{
				return cube;
			}
			else if ( ( (ReportItemHandle) element ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				return getBindingCube( ( (ReportItemHandle) element ).getDataBindingReference( ) );
			}
			else if ( ( (ReportItemHandle) element ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_DATA )
			{
				return null;
			}
		}
		if ( element.getContainer( ) != null )
		{
			return getBindingCube( element.getContainer( ) );
		}
		return null;
	}

	/**
	 * Gets all measure handles in the cube.
	 * 
	 * @param cube
	 *            cube handle
	 * @return all measure handles or empty list if no measure. The element in
	 *         list is <code>MeasureHandle</code>
	 * @since 2.3
	 */
	public static List getAllMeasures( CubeHandle cube )
	{
		if ( cube.getContentCount( CubeHandle.MEASURE_GROUPS_PROP ) > 0 )
		{
			List measures = new ArrayList( );
			Iterator measureGroups = cube.getContents( CubeHandle.MEASURE_GROUPS_PROP )
					.iterator( );
			while ( measureGroups.hasNext( ) )
			{
				MeasureGroupHandle measureGroup = (MeasureGroupHandle) measureGroups.next( );
				measures.addAll( measureGroup.getContents( MeasureGroupHandle.MEASURES_PROP ) );
			}
			return measures;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Gets all level handles in the cube.
	 * 
	 * @param cube
	 *            cube handle
	 * @return all level handles or empty list if no level. The element in list
	 *         is <code>LevelHandle</code>
	 * @since 2.3
	 */
	public static List getAllLevels( CubeHandle cube )
	{
		if ( cube.getContentCount( CubeHandle.DIMENSIONS_PROP ) > 0 )
		{
			List levels = new ArrayList( );
			Iterator dimensions = cube.getContents( CubeHandle.DIMENSIONS_PROP )
					.iterator( );
			while ( dimensions.hasNext( ) )
			{
				DimensionHandle dimensionHandle = (DimensionHandle) dimensions.next( );
				HierarchyHandle hierarchy = (HierarchyHandle) ( dimensionHandle ).getContent( DimensionHandle.HIERARCHIES_PROP,
						0 );
				int count = hierarchy.getLevelCount( );
				for ( int i = 0; i < count; i++ )
				{
					levels.add( hierarchy.getLevel( i ) );
				}
			}
			return levels;
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * Gets all column bindings. If the handle's contain has column bindings,
	 * will combine the bindings with the handle's.
	 * 
	 * @param itemHandle
	 *            handle
	 * @return the iterator of all column bindings
	 * @since 2.3
	 */
	public static Iterator getAllColumnBindingsIterator(
			ReportItemHandle itemHandle )
	{
		ReportItemHandle container = ChartReportItemUtil.getBindingHolder( itemHandle );
		if ( container != null )
		{
			// Add all bindings to an iterator
			List allBindings = new ArrayList( );
			for ( Iterator ownBindings = itemHandle.columnBindingsIterator( ); ownBindings.hasNext( ); )
			{
				allBindings.add( ownBindings.next( ) );
			}
			for ( Iterator containerBindings = container.columnBindingsIterator( ); containerBindings.hasNext( ); )
			{
				allBindings.add( containerBindings.next( ) );
			}
			return allBindings.iterator( );
		}
		return itemHandle.columnBindingsIterator( );
	}

	/**
	 * Gets the cell in cross tab which contains the chart
	 * 
	 * @param chartHandle
	 *            the handle with chart
	 * @return the cell which contains the chart or null
	 * @throws BirtException
	 * @since 2.3
	 */
	public static AggregationCellHandle getXtabContainerCell(
			DesignElementHandle chartHandle ) throws BirtException
	{
		DesignElementHandle container = chartHandle.getContainer( );
		if ( container instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle xtabHandle = (ExtendedItemHandle) container;
			String exName = xtabHandle.getExtensionName( );
			if ( ICrosstabConstants.AGGREGATION_CELL_EXTENSION_NAME.equals( exName ) )
			{
				return (AggregationCellHandle) xtabHandle.getReportItem( );
			}
		}
		return null;
	}

	/**
	 * Creates the dimension expression according to level
	 * 
	 * @param level
	 *            level handle
	 * @return the dimension expression or null
	 * @since 2.3
	 */
	public static String createDimensionExpression( LevelHandle level )
	{
		if ( level == null )
		{
			return null;
		}
		return ExpressionUtil.createJSDimensionExpression( level.getContainer( )
				.getContainer( )
				.getName( ), level.getName( ) );
	}

	/**
	 * Updates runtime model to render chart plot only.
	 * 
	 * @param cm
	 *            chart model
	 * @return the modified chart model
	 * @since 2.3
	 */
	public static Chart updateModelToRenderPlot( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes chart = (ChartWithAxes) cm;
			chart.getLegend( ).setVisible( false );
			chart.getTitle( ).setVisible( false );
			chart.getPlot( ).getOutline( ).setVisible( false );
			chart.getBlock( ).getInsets( ).set( 0, 0, 0, 0 );
			// chart.getPlot( ).getInsets( ).set( 0, 0, 0, 0 );
			// chart.getPlot( ).getClientArea( ).getInsets( ).set( 0, 0, 0, 0 );

			// boolean bTransposed = chart.isTransposed( );
			Axis xAxis = (Axis) chart.getAxes( ).get( 0 );
			Axis yAxis = (Axis) xAxis.getAssociatedAxes( ).get( 0 );

			xAxis.getTitle( ).setVisible( false );
			xAxis.getLabel( ).setVisible( false );
			xAxis.getLineAttributes( ).setVisible( false );
			xAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );

			yAxis.getTitle( ).setVisible( false );
			yAxis.getLabel( ).setVisible( false );
			yAxis.getLineAttributes( ).setVisible( false );
			yAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			yAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );
		}
		return cm;
	}

	/**
	 * Updates runtime model to render chart axis only.
	 * 
	 * @param cm
	 *            chart model
	 * @return the modified chart model
	 * @since 2.3
	 */
	public static Chart updateModelToRenderAxis( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes chart = (ChartWithAxes) cm;
			chart.getLegend( ).setVisible( false );
			chart.getTitle( ).setVisible( false );
			chart.getPlot( ).getOutline( ).setVisible( false );
			chart.getPlot( ).getClientArea( ).setVisible( false );
			chart.getBlock( ).getInsets( ).set( 0, 0, 0, 0 );
			// chart.getPlot( ).getInsets( ).set( 0, 0, 0, 0 );
			// chart.getPlot( ).getClientArea( ).getInsets( ).set( 0, 0, 0, 0 );

			boolean bTransposed = chart.isTransposed( );
			Axis xAxis = (Axis) chart.getAxes( ).get( 0 );
			Axis yAxis = (Axis) xAxis.getAssociatedAxes( ).get( 0 );

			xAxis.getTitle( ).setVisible( false );
			xAxis.getLabel( ).setVisible( false );
			xAxis.getLineAttributes( ).setVisible( false );
			xAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.getMajorGrid( ).getLineAttributes( ).setVisible( false );
			xAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.getMinorGrid( ).getLineAttributes( ).setVisible( false );

			yAxis.getTitle( ).setVisible( false );
			yAxis.getLineAttributes( ).setVisible( false );
			yAxis.getMajorGrid( ).getLineAttributes( ).setVisible( false );
			yAxis.getMinorGrid( ).getLineAttributes( ).setVisible( false );
			yAxis.getMajorGrid( ).setTickStyle( bTransposed
					? TickStyle.LEFT_LITERAL : TickStyle.RIGHT_LITERAL );
			yAxis.setLabelPosition( bTransposed ? Position.LEFT_LITERAL
					: Position.RIGHT_LITERAL );
			yAxis.setLabelWithinAxes( true );
			if ( bTransposed )
			{
				// Show axis in the top in vertical direction
				yAxis.getOrigin( ).setType( IntersectionType.MAX_LITERAL );
			}
		}
		else
		{
			cm = null;
		}
		return cm;
	}

	/**
	 * Transform dimension value to points.
	 * 
	 * @param handle
	 * 
	 * @return the dimension value with measure of points
	 * @since 2.3
	 */
	public static double convertToPoints(
			org.eclipse.birt.report.model.api.DimensionHandle handle )
	{
		double retValue = 0.0;

		if ( handle.isSet( ) )
		{
			retValue = DimensionUtil.convertTo( handle.getMeasure( ),
					handle.getUnits( ),
					DesignChoiceConstants.UNITS_PT ).getMeasure( );
		}
		return retValue;
	}

	/**
	 * Creates the default bounds for chart model.
	 * 
	 * @param crii
	 *            ChartReportItemImpl
	 * @param cm
	 *            chart model
	 * @return default bounds
	 * @since 2.3
	 */
	public static Bounds createDefaultChartBounds( ChartReportItemImpl crii,
			Chart cm )
	{
		// Axis chart case
		if ( crii.hasHostChart( ) )
		{
			// Axis chart must be ChartWithAxes
			ChartWithAxes cmWA = (ChartWithAxes) cm;
			if ( cmWA.isTransposed( ) )
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_BLOCK_WIDTH,
						DEFAULT_AXIS_CHART_BLOCK_SIZE );
			}
			else
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_AXIS_CHART_BLOCK_SIZE,
						DEFAULT_CHART_BLOCK_HEIGHT );
			}
		}
		// Plot or ordinary chart case
		else
		{
			return BoundsImpl.create( 0,
					0,
					DEFAULT_CHART_BLOCK_WIDTH,
					DEFAULT_CHART_BLOCK_HEIGHT );
		}
	}
}
