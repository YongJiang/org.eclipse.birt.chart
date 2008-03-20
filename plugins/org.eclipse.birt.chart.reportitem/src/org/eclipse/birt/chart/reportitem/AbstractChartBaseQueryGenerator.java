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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.emf.common.util.EList;

/**
 * The class defines basic functions for creating base query.
 * 
 * @since BIRT 2.3
 */
public abstract class AbstractChartBaseQueryGenerator
{

	/** The handle of report item handle. */
	protected ReportItemHandle fReportItemHandle;

	/** Current chart handle. */
	protected Chart fChartModel;

	/** The set stores created binding names. */
	protected Set fNameSet = new HashSet( );

	/**
	 * Constructor of the class.
	 * 
	 * @param chart
	 * @param handle
	 */
	public AbstractChartBaseQueryGenerator( ReportItemHandle handle, Chart cm )
	{
		fChartModel = cm;
		fReportItemHandle = handle;
	}

	/**
	 * Create base query definition.
	 * 
	 * @param parent
	 * @return
	 * @throws ChartException
	 */
	public abstract IDataQueryDefinition createBaseQuery(
			IDataQueryDefinition parent ) throws ChartException;

	/**
	 * Create base query definition.
	 * 
	 * @param columns
	 * @return
	 * @throws DataException
	 */
	public abstract IDataQueryDefinition createBaseQuery( List columns )
			throws ChartException;

	/**
	 * Add aggregate bindings of value series for grouping case.
	 * 
	 * @param query
	 * @param seriesDefinitions
	 * @param innerMostGroupDef
	 * @param valueExprMap
	 * @param baseSD
	 * @throws DataException
	 * @throws DataException
	 */
	protected void addValueSeriesAggregateBindingForGrouping(
			BaseQueryDefinition query, EList seriesDefinitions,
			GroupDefinition innerMostGroupDef, Map valueExprMap,
			SeriesDefinition baseSD ) throws ChartException
	{
		for ( Iterator iter = seriesDefinitions.iterator( ); iter.hasNext( ); )
		{
			SeriesDefinition orthSD = (SeriesDefinition) iter.next( );
			Series series = orthSD.getDesignTimeSeries( );
			List qlist = ChartEngine.instance( )
					.getDataSetProcessor( series.getClass( ) )
					.getDataDefinitionsForGrouping( series );

			for ( Iterator iter_datadef = series.getDataDefinition( )
					.iterator( ); iter_datadef.hasNext( ); )
			{
				Query qry = (Query) iter_datadef.next( );
				
				String expr = qry.getDefinition( );
				if ( expr == null || "".equals( expr ) ) //$NON-NLS-1$
				{
					continue;
				}
				
				String aggName = ChartUtil.getAggregateFuncExpr( orthSD, baseSD );
				if ( aggName == null || "".equals( aggName ) ) //$NON-NLS-1$
				{
					continue;
				}
				
				// Get a unique name.
				String name = ChartUtil.getValueSeriesFullExpression( expr, orthSD, baseSD );
				if ( fNameSet.contains( name ) )
				{
					continue;
				}
				fNameSet.add( name );
				
				Binding colBinding = new Binding( name );

				colBinding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
				
				// Set binding expression by different aggregation, some
				// aggregations can't set expression, like Count and so on.
				try
				{
					setBindingExpressionDueToAggregation( colBinding, expr, aggName );
				}
				catch ( DataException e1 )
				{
					throw new ChartException( ChartReportItemPlugin.ID,
							ChartException.DATA_BINDING,
							e1 );
				}
				
				if ( qlist.contains( qry ) )
				{
					if ( innerMostGroupDef != null )
					{
						try
						{
							colBinding.addAggregateOn( innerMostGroupDef.getName( ) );
						}
						catch ( DataException e )
						{
							throw new ChartException( ChartReportItemPlugin.ID,
									ChartException.DATA_BINDING,
									e );
						}
					}

					// Set aggregate parameters.
					colBinding.setAggrFunction( ChartReportItemUtil.convertToDtEAggFunction( aggName ) );

					IAggregateFunction aFunc = PluginSettings.instance( )
							.getAggregateFunction( aggName );
					if ( aFunc.getParametersCount( ) > 0 )
					{
						Object[] parameters = ChartUtil.getAggFunParameters( orthSD,
								baseSD );

						for ( int i = 0; i < parameters.length &&
								i < aFunc.getParametersCount( ); i++ )
						{
							String param = (String) parameters[i];
							colBinding.addArgument( new ScriptExpression( param ) );
						}
					}
				}
				String newExpr = getExpressionForEvaluator( name );

				try
				{
					query.addBinding( colBinding );
				}
				catch ( DataException e )
				{
					throw new ChartException( ChartReportItemPlugin.ID,
							ChartException.DATA_BINDING,
							e );
				}

				valueExprMap.put( expr, new String[]{
						name, newExpr
				} );
			}
		}
	}

	/**
	 * Generate a unique binding name.
	 * 
	 * @param expr
	 * @return
	 */
	protected String generateUniqueBindingName( String expr )
	{
		String name = StructureFactory.newComputedColumn( fReportItemHandle,
				expr.replaceAll( "\"", "" ) ) //$NON-NLS-1$ //$NON-NLS-2$
				.getName( );
		if ( fNameSet.contains( name ) )
		{
			name = name + fNameSet.size( );
			return generateUniqueBindingName( name );
		}

		fNameSet.add( name );
		return name;
	}

	/**
	 * Generate grouping bindings and add into query definition.
	 * 
	 * @param query
	 * @throws DataException
	 */
	protected void generateGroupBindings( BaseQueryDefinition query )
			throws ChartException
	{
		// 1. Get first category and orthogonal series definition to get
		// grouping definition.
		SeriesDefinition categorySD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		if ( fChartModel instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) fChartModel;
			categorySD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );

			orthAxisArray = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0], true );
			orthSD = (SeriesDefinition) ( (Axis) orthAxisArray[0] ).getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( fChartModel instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) fChartModel;
			categorySD = (SeriesDefinition) cwoa.getSeriesDefinitions( )
					.get( 0 );
			orthSD = (SeriesDefinition) categorySD.getSeriesDefinitions( )
					.get( 0 );
		}

		// 2. Add grouping.
		// 2.1 Add Y optional grouping.
		initYGroupingNSortKey( query, categorySD, orthSD, orthAxisArray );

		// 2.2 Add base grouping.
		GroupDefinition categoryGroupDefinition = initCategoryGrouping( query,
				categorySD );

		// 3. Add binding for value series aggregate.
		// The returned map is used to map old expression and new expression,
		// the key is old expression, the value is new expression.
		// <p>For aggregate case, the expression of value series will be replace
		// with new expression for evaluator. And if the expression of value
		// series is a sort key, it also need using new expression instead of
		// old expression as sort expression, the related code show in section 4.
		Map valueExprMap = addAggregateBindings( query,
				categorySD,
				orthAxisArray );

		// 4. Binding sort on category series.
		bindSortOnCategorySeries( query,
				categorySD,
				categoryGroupDefinition,
				valueExprMap );
	}

	/**
	 * @param query
	 * @param categorySD
	 * @param categoryGroupDefinition
	 * @param valueExprMap
	 */
	private void bindSortOnCategorySeries( BaseQueryDefinition query,
			SeriesDefinition categorySD,
			GroupDefinition categoryGroupDefinition, Map valueExprMap )
	{
		String baseSortExpr = getValidSortExpr( categorySD );
		if ( !categorySD.isSetSorting( ) || baseSortExpr == null )
		{
			return;
		}
		
		SortDefinition sd = new SortDefinition( );

		// Chart need to set SortStrength to support sorting locale
		// characters, it is compatibility with old logic of
		// chart(version<2.3).
		sd.setSortStrength( ISortDefinition.DEFAULT_SORT_STRENGTH );

		sd.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( categorySD.getSorting( ) ) );
		
		if ( ChartReportItemUtil.isBaseGroupingDefined( categorySD ) )
		{
			// If base series set group, add sort on group definition.
			String baseExpr = ( (Query) categorySD.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) ).getDefinition( );
			if ( baseExpr.equals( getValidSortExpr( categorySD ) ) )
			{
				sd.setExpression( baseExpr );
			}
			else
			{
				String[] nameNewExprArray = (String[]) valueExprMap.get( baseSortExpr );
				if ( nameNewExprArray != null && nameNewExprArray.length == 2 )
				{
					sd.setExpression( nameNewExprArray[1] );
				}
				else
				{
					sd.setExpression( baseSortExpr );
				}
			}
			
			categoryGroupDefinition.addSort( sd );			
		}
		else
		{
			sd.setExpression( baseSortExpr );
			query.addSort( sd );
		}
	}

	/**
	 * @param query
	 * @param categorySD
	 * @param orthAxisArray
	 * @return
	 * @throws ChartException
	 */
	private Map addAggregateBindings( BaseQueryDefinition query,
			SeriesDefinition categorySD, Object[] orthAxisArray )
			throws ChartException
	{
		GroupDefinition innerMostGroupDef = null;
		if ( query.getGroups( ) != null && query.getGroups( ).size( ) > 0 )
		{
			innerMostGroupDef = (GroupDefinition) query.getGroups( )
					.get( query.getGroups( ).size( ) - 1 );
		}

		Map valueExprMap = new HashMap( );
		// Add aggregates.
		if ( fChartModel instanceof ChartWithAxes )
		{
			for ( int i = 0; i < orthAxisArray.length; i++ )
			{
				addValueSeriesAggregateBindingForGrouping( query,
						( (Axis) orthAxisArray[i] ).getSeriesDefinitions( ),
						innerMostGroupDef,
						valueExprMap,
						categorySD );
			}
		}
		else if ( fChartModel instanceof ChartWithoutAxes )
		{
			addValueSeriesAggregateBindingForGrouping( query,
					categorySD.getSeriesDefinitions( ),
					innerMostGroupDef,
					valueExprMap,
					categorySD );
		}
		return valueExprMap;
	}

	private GroupDefinition initCategoryGrouping( BaseQueryDefinition query,
			SeriesDefinition categorySD )
	{
		GroupDefinition baseGroupDefinition = createBaseGroupingDefinition( categorySD );
		if ( baseGroupDefinition != null )
		{
			query.addGroup( baseGroupDefinition );
		}
		return baseGroupDefinition;
	}

	/**
	 * @param query
	 * @param categorySD
	 * @param orthSD
	 * @param orthAxisArray
	 * @return
	 * @throws ChartException
	 */
	private GroupDefinition initYGroupingNSortKey( BaseQueryDefinition query,
			SeriesDefinition categorySD, SeriesDefinition orthSD,
			Object[] orthAxisArray ) throws ChartException
	{
		GroupDefinition yGroupingDefinition = createOrthogonalGroupingDefinition( orthSD );
		if ( yGroupingDefinition != null )
		{
			// Add y optional group to query.
			query.addGroup( yGroupingDefinition );

			if ( !orthSD.isSetSorting( ) )
			{
				return yGroupingDefinition;
			}

			// Create a new sort
			SortDefinition sortDefinition = new SortDefinition( );

			// Chart need to set SortStrength to support sorting locale
			// characters, it is compatibility with old logic of
			// chart(version<2.3).
			sortDefinition.setSortStrength( ISortDefinition.DEFAULT_SORT_STRENGTH );
			
			sortDefinition.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( orthSD.getSorting( ) ) );

			String sortKey = null;
			if ( orthSD.getSortKey( ) != null &&
					orthSD.getSortKey( ).getDefinition( ) != null )
			{
				sortKey = orthSD.getSortKey( ).getDefinition( );
			}

			if ( sortKey == null || yGroupingDefinition.getKeyExpression( ).equals( sortKey ) )
			{
				// Sort key is group expression.
				sortDefinition.setExpression( yGroupingDefinition.getKeyExpression( ) );
			}
			else
			{
				// Add additional sort on the grouping.
				String name = generateUniqueBindingName( sortKey );
				Binding binding = new Binding( name );
				try
				{
					query.addBinding( binding );
					binding.setExpression( new ScriptExpression( sortKey ) );
					binding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
					binding.addAggregateOn( yGroupingDefinition.getName( ) );
				}
				catch ( DataException e )
				{
					throw new ChartException( ChartReportItemPlugin.ID,
							ChartException.DATA_BINDING,
							e );
				}

				// Get aggregate func.
				String aggFunc = getAggFunExpr( sortKey,
						categorySD,
						orthAxisArray );
				if ( aggFunc != null )
				{
					binding.setAggrFunction( ChartReportItemUtil.convertToDtEAggFunction( aggFunc ) );

					IAggregateFunction aFunc = PluginSettings.instance( )
							.getAggregateFunction( aggFunc );
					if ( aFunc.getParametersCount( ) > 0 )
					{
						Object[] parameters = ChartUtil.getAggFunParameters( orthSD,
								categorySD );

						for ( int i = 0; i < parameters.length &&
								i < aFunc.getParametersCount( ); i++ )
						{
							String param = (String) parameters[i];
							binding.addArgument( new ScriptExpression( param ) );
						}
					}
				}
		
				sortDefinition.setExpression( ExpressionUtil.createRowExpression( binding.getBindingName( ) ) );
			}
			
			yGroupingDefinition.addSort( sortDefinition );
		}

		return yGroupingDefinition;
	}

	/**
	 * Create Y grouping definition.
	 * 
	 * @param orthSD
	 * @return
	 */
	private GroupDefinition createOrthogonalGroupingDefinition( SeriesDefinition orthSD )
	{

		if ( ChartReportItemUtil.isYGroupingDefined( orthSD ) )
		{
			DataType dataType = null;
			GroupingUnitType groupUnit = null;
			double groupIntervalRange = 0; // Default value is 0.

			String yGroupExpr = orthSD.getQuery( ).getDefinition( );
			SeriesGrouping yGroupingInterval = orthSD.getQuery( ).getGrouping( );
			
			if ( yGroupingInterval != null &&
					yGroupingInterval.isSetGroupType( ) )
			{
				dataType = yGroupingInterval.getGroupType( );
				groupUnit = yGroupingInterval.getGroupingUnit( );
				groupIntervalRange = yGroupingInterval.getGroupingInterval( );
			}

			String name = generateUniqueBindingName( yGroupExpr );

			GroupDefinition yGroupDefinition = new GroupDefinition( name );

			yGroupDefinition.setKeyExpression( yGroupExpr );

			yGroupDefinition.setInterval( ChartReportItemUtil.convertToDtEGroupUnit( dataType,
					groupUnit,
					groupIntervalRange ) );
			yGroupDefinition.setIntervalRange( ChartReportItemUtil.convertToDtEIntervalRange( dataType,
					groupUnit,
					groupIntervalRange ) );

			return yGroupDefinition;
		}

		return null;
	}

	/**
	 * Create base grouping definition.
	 * 
	 * @param baseSD
	 * @return
	 */
	private GroupDefinition createBaseGroupingDefinition(
			SeriesDefinition baseSD )
	{
		DataType dataType;
		GroupingUnitType groupUnit;
		double groupIntervalRange;
		if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD ) )
		{
			dataType = baseSD.getGrouping( ).getGroupType( );
			groupUnit = baseSD.getGrouping( ).getGroupingUnit( );
			groupIntervalRange = baseSD.getGrouping( ).getGroupingInterval( );
			if ( groupIntervalRange < 0 )
			{
				groupIntervalRange = 0;
			}

			String baseExpr = ( (Query) baseSD.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) ).getDefinition( );

			String name = generateUniqueBindingName( baseExpr );

			GroupDefinition baseGroupDefinition = new GroupDefinition( name );

			baseGroupDefinition.setKeyExpression( baseExpr );
			baseGroupDefinition.setInterval( ChartReportItemUtil.convertToDtEGroupUnit( dataType,
					groupUnit,
					groupIntervalRange ) );
			baseGroupDefinition.setIntervalRange( ChartReportItemUtil.convertToDtEIntervalRange( dataType,
					groupUnit,
					groupIntervalRange ) );

			return baseGroupDefinition;
		}
		return null;
	}

	/**
	 * Get aggregation function string of sort key related with value series.
	 * 
	 * @param sortKey
	 * @param baseSD
	 * @param orthAxisArray
	 * @return
	 * @throws ChartException 
	 */
	protected String getAggFunExpr( String sortKey, SeriesDefinition baseSD,
			Object[] orthAxisArray ) throws ChartException
	{
		String baseAggFunExpr = null;
		if ( baseSD.getGrouping( ) != null &&
				baseSD.getGrouping( ).isSetEnabled( ) &&
				baseSD.getGrouping( ).isEnabled( ) )
		{
			baseAggFunExpr = baseSD.getGrouping( ).getAggregateExpression( );
		}

		String aggFunction = null;

		if ( fChartModel instanceof ChartWithAxes )
		{
			for ( int i = 0; i < orthAxisArray.length; i++ )
			{
				EList sds = ( (Axis) orthAxisArray[i] ).getSeriesDefinitions( );
				for ( Iterator iter = sds.iterator( ); iter.hasNext( ); )
				{
					SeriesDefinition sd = (SeriesDefinition) iter.next( );
					if ( sd.getDesignTimeSeries( ).getDataDefinition( ) != null &&
							sd.getDesignTimeSeries( )
									.getDataDefinition( )
									.get( 0 ) != null )
					{
						Query q = (Query) sd.getDesignTimeSeries( )
								.getDataDefinition( )
								.get( 0 );
						if ( sortKey.equals( q.getDefinition( ) ) )
						{
							aggFunction = ChartUtil.getAggregateFunctionExpr( sd,
									baseAggFunExpr );
							break;
						}
					}
				}
			}
		}
		else if ( fChartModel instanceof ChartWithoutAxes )
		{

			for ( Iterator iter = baseSD.getSeriesDefinitions( ).iterator( ); iter.hasNext( ); )
			{
				SeriesDefinition sd = (SeriesDefinition) iter.next( );
				if ( sd.getDesignTimeSeries( ).getDataDefinition( ) != null &&
						sd.getDesignTimeSeries( ).getDataDefinition( ).get( 0 ) != null )
				{
					Query q = (Query) sd.getDesignTimeSeries( )
							.getDataDefinition( )
							.get( 0 );
					if ( sortKey.equals( q.getDefinition( ) ) )
					{
						aggFunction =  ChartUtil.getAggregateFunctionExpr( sd,
								baseAggFunExpr );
						break;
					}
				}
			}

		}

		if ( aggFunction == null || "".equals( aggFunction ) ) { //$NON-NLS-1$
			return baseAggFunExpr;
		}

		return aggFunction;
	}

	/**
	 * Get valid sort expression from series definition.
	 * 
	 * @param sd
	 * @return
	 */
	protected String getValidSortExpr( SeriesDefinition sd )
	{
		if ( !sd.isSetSorting( ) )
		{
			return null;
		}

		String sortExpr = null;
		if ( sd.getSortKey( ) != null &&
				sd.getSortKey( ).getDefinition( ) != null )
		{
			sortExpr = sd.getSortKey( ).getDefinition( );
		}
		else
		{
			sortExpr = ( (Query) sd.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) ).getDefinition( );
		}
		if ( "".equals( sortExpr ) ) //$NON-NLS-1$
		{
			sortExpr = null;
		}

		return sortExpr;
	}

	/**
	 * @param expression
	 * @return
	 */
	protected String getExpressionForEvaluator( String expression )
	{
		return ExpressionUtil.createJSRowExpression( expression );
	}
	
	/**
	 * Set binding expression due to aggregation, some aggregation can't set expression, like Count.
	 * 
	 * @param binding binding object.
	 * @param expression specified expression.
	 * @param chartAggFunName aggregation function name of chart.
	 * @throws DataException if the aggregation restrict info is got failure.
	 */
	protected void setBindingExpressionDueToAggregation( Binding binding,
			String expression, String chartAggFunName ) throws DataException
	{
		IAggrFunction dteAggFunc = AggregationManager.getInstance( )
				.getAggregation( ChartReportItemUtil.convertToDtEAggFunction( chartAggFunName ) );
		IParameterDefn[] parameters = dteAggFunc.getParameterDefn( );
		if ( parameters != null && parameters.length > 0 )
		{
			binding.setExpression( new ScriptExpression( expression ) );
		}
	}
}
