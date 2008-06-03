/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.util.CubeUtil;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.emf.common.util.EList;

/**
 * Query helper for cube query definition
 */

public class ChartCubeQueryHelper
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	private final ExtendedItemHandle handle;
	private final Chart cm;

	/**
	 * Maps for registered column bindings.<br>
	 * Key: binding name, value: Binding
	 */
	private Map<String, IBinding> registeredBindings = new HashMap<String, IBinding>( );
	/**
	 * Maps for registered queries.<br>
	 * Key: binding name, value: raw query expression
	 */
	private Map<String, String> registeredQueries = new HashMap<String, String>( );

	/**
	 * Maps for registered level definitions.<br>
	 * Key: Binding name of query, value: ILevelDefinition
	 */
	private Map<String, ILevelDefinition> registeredLevels = new HashMap<String, ILevelDefinition>( );

	/**
	 * Maps for registered measure definitions.<br>
	 * Key: Binding name of query, value: IMeasureDefinition
	 */
	private Map<String, IMeasureDefinition> registeredMeasures = new HashMap<String, IMeasureDefinition>( );

	/**
	 * Maps for registered level handles.<br>
	 * Key: LevelHandle, value: ILevelDefinition
	 */
	private Map<LevelHandle, ILevelDefinition> registeredLevelHandles = new HashMap<LevelHandle, ILevelDefinition>( );

	private String rowEdgeDimension;

	/**
	 * Indicates if used for single chart case, such as Live preview in chart
	 * builder, or no inheritance. In this case, sub/nest query is not
	 * supported, and aggregateOn in measure binding should be removed to make
	 * preview work.
	 */
	private boolean bSingleChart = false;

	public ChartCubeQueryHelper( ExtendedItemHandle handle, Chart cm )
	{
		this.handle = handle;
		this.cm = cm;
	}

	/**
	 * Creates the cube query definition for chart. If parent definition is
	 * null, it's usually used for Live preview in chart builder. If chart in
	 * xtab, will return sub cube query definition.
	 * 
	 * @param parent
	 * @return ICubeQueryDefinition for cube consuming or
	 *         ISubCubeQueryDefinition for chart in xtab case
	 * @throws BirtException
	 */
	public IBaseCubeQueryDefinition createCubeQuery( IDataQueryDefinition parent )
			throws BirtException
	{
		bSingleChart = parent == null;

		CubeHandle cubeHandle = handle.getCube( );
		ICubeQueryDefinition cubeQuery = null;
		if ( cubeHandle == null )
		{
			// Create sub query for chart in xtab
			cubeHandle = ChartXTabUtil.getBindingCube( handle );
			if ( cubeHandle == null )
			{
				throw new ChartException( ChartReportItemPlugin.ID,
						ChartException.NULL_DATASET,
						Messages.getString( "ChartCubeQueryHelper.Error.MustBindCube" ) ); //$NON-NLS-1$
			}

			// Do not support sub query in Live preview.
			if ( !bSingleChart )
			{
				ISubCubeQueryDefinition subQuery = createSubCubeQuery( );
				if ( subQuery != null )
				{
					if ( parent instanceof ICubeQueryDefinition )
					{
						// Adds min and max binding to parent query definition
						// for shared scale.
						addMinMaxBinding( (ICubeQueryDefinition) parent );
					}
					return subQuery;
				}
			}
		}

		cubeQuery = ChartXTabUtil.getCubeElementFactory( )
				.createCubeQuery( cubeHandle.getQualifiedName( ) );

		// Add column bindings from handle
		initBindings( cubeQuery, cubeHandle );

		List<SeriesDefinition> sdList = getAllSeriesDefinitions( cm );

		// Add measures and dimensions
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = sdList.get( i );
			List<Query> queryList = sd.getDesignTimeSeries( )
					.getDataDefinition( );
			for ( int j = 0; j < queryList.size( ); j++ )
			{
				Query query = queryList.get( j );
				// Add measures or dimensions for data definition, and update
				// query expression
				bindSeriesQuery( query.getDefinition( ), cubeQuery, cubeHandle );
			}

			// Add measures or dimensions for optional grouping, and update
			// query expression
			bindSeriesQuery( sd.getQuery( ).getDefinition( ),
					cubeQuery,
					cubeHandle );
		}

		// Add aggregation list to measure bindings on demand
		Collection<ILevelDefinition> levelsInOrder = getAllLevelsInHierarchyOrder( cubeHandle,
				cubeQuery );
		for ( Iterator<String> measureNames = registeredMeasures.keySet( )
				.iterator( ); measureNames.hasNext( ); )
		{
			IBinding binding = registeredBindings.get( measureNames.next( ) );
			if ( binding != null && binding.getAggregatOns( ).isEmpty( ) )
			{
				for ( Iterator<ILevelDefinition> levels = levelsInOrder.iterator( ); levels.hasNext( ); )
				{
					ILevelDefinition level = levels.next( );
					String dimensionName = level.getHierarchy( )
							.getDimension( )
							.getName( );
					binding.addAggregateOn( ExpressionUtil.createJSDimensionExpression( dimensionName,
							level.getName( ) ) );
				}
			}
		}

		// Add sorting
		// Sorting must be added after measures and dimensions, since sort
		// key references to measures or dimensions
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = (SeriesDefinition) sdList.get( i );
			addSorting( cubeQuery, cubeHandle, sd, i );
		}

		// Add filter
		addCubeFilter( cubeQuery );

		return cubeQuery;
	}

	private ISubCubeQueryDefinition createSubCubeQuery( ) throws BirtException
	{
		String queryName = ChartReportItemConstants.CHART_SUBQUERY;
		AggregationCellHandle containerCell = ChartXTabUtil.getXtabContainerCell( handle );
		if ( containerCell == null )
		{
			return null;
		}
		CrosstabReportItemHandle xtab = containerCell.getCrosstab( );
		int columnLevelCount = ChartXTabUtil.getLevelCount( xtab,
				ICrosstabConstants.COLUMN_AXIS_TYPE );
		int rowLevelCount = ChartXTabUtil.getLevelCount( xtab,
				ICrosstabConstants.ROW_AXIS_TYPE );
		if ( cm instanceof ChartWithAxes )
		{
			if ( ( (ChartWithAxes) cm ).isTransposed( ) )
			{
				if ( columnLevelCount >= 1 )
				{
					ISubCubeQueryDefinition subCubeQuery = ChartXTabUtil.getCubeElementFactory( )
							.createSubCubeQuery( queryName );
					subCubeQuery.setStartingLevelOnColumn( ChartXTabUtil.createDimensionExpression( ChartXTabUtil.getLevel( xtab,
							ICrosstabConstants.COLUMN_AXIS_TYPE,
							columnLevelCount - 1 )
							.getCubeLevel( ) ) );
					if ( rowLevelCount > 1 )
					{
						// Only add another level in multiple levels case
						subCubeQuery.setStartingLevelOnRow( ChartXTabUtil.createDimensionExpression( ChartXTabUtil.getLevel( xtab,
								ICrosstabConstants.ROW_AXIS_TYPE,
								rowLevelCount - 2 )
								.getCubeLevel( ) ) );
					}
					return subCubeQuery;
				}
				else if ( rowLevelCount > 1 )
				{
					// No column level and multiple row levels, use the top
					// row level
					ISubCubeQueryDefinition subCubeQuery = ChartXTabUtil.getCubeElementFactory( )
							.createSubCubeQuery( queryName );
					subCubeQuery.setStartingLevelOnRow( ChartXTabUtil.createDimensionExpression( ChartXTabUtil.getLevel( xtab,
							ICrosstabConstants.ROW_AXIS_TYPE,
							rowLevelCount - 2 )
							.getCubeLevel( ) ) );
					return subCubeQuery;
				}
				// If corresponding column is null and without multiple
				// levels, do not use sub query
			}
			else
			{
				if ( rowLevelCount >= 1 )
				{
					ISubCubeQueryDefinition subCubeQuery = ChartXTabUtil.getCubeElementFactory( )
							.createSubCubeQuery( queryName );
					subCubeQuery.setStartingLevelOnRow( ChartXTabUtil.createDimensionExpression( ChartXTabUtil.getLevel( xtab,
							ICrosstabConstants.ROW_AXIS_TYPE,
							rowLevelCount - 1 )
							.getCubeLevel( ) ) );
					if ( columnLevelCount > 1 )
					{
						// Only add another level in multiple levels case
						subCubeQuery.setStartingLevelOnColumn( ChartXTabUtil.createDimensionExpression( ChartXTabUtil.getLevel( xtab,
								ICrosstabConstants.COLUMN_AXIS_TYPE,
								columnLevelCount - 2 )
								.getCubeLevel( ) ) );
					}
					return subCubeQuery;
				}
				else if ( columnLevelCount > 1 )
				{
					// No row level and multiple column levels, use the top
					// column level
					ISubCubeQueryDefinition subCubeQuery = ChartXTabUtil.getCubeElementFactory( )
							.createSubCubeQuery( queryName );
					subCubeQuery.setStartingLevelOnColumn( ChartXTabUtil.createDimensionExpression( ChartXTabUtil.getLevel( xtab,
							ICrosstabConstants.COLUMN_AXIS_TYPE,
							columnLevelCount - 2 )
							.getCubeLevel( ) ) );
					return subCubeQuery;
				}
				// If corresponding row is null and without multiple levels,
				// do not use sub query
			}
		}

		// Do not use sub query for other cases
		return null;
	}

	/**
	 * Adds min and max binding to parent query definition
	 * 
	 * @param parent
	 * @throws BirtException
	 */
	private void addMinMaxBinding( ICubeQueryDefinition parent )
			throws BirtException
	{
		List<IBinding> bs = parent.getBindings( );
		Axis xAxis = (Axis) ( (ChartWithAxes) cm ).getAxes( ).get( 0 );
		SeriesDefinition sdValue = (SeriesDefinition) ( (ChartWithAxes) cm ).getOrthogonalAxes( xAxis,
				true )[0].getSeriesDefinitions( ).get( 0 );
		Query queryValue = (Query) sdValue.getDesignTimeSeries( )
				.getDataDefinition( )
				.get( 0 );
		String bindingValue = ChartXTabUtil.getBindingName( queryValue.getDefinition( ),
				false );
		String maxBindingName = ChartReportItemConstants.QUERY_MAX
				+ bindingValue;
		String minBindingName = ChartReportItemConstants.QUERY_MIN
				+ bindingValue;
		for ( int i = 0; i < bs.size( ); i++ )
		{
			IBinding binding = bs.get( i );
			if ( binding.getBindingName( ).equals( maxBindingName )
					|| binding.getBindingName( ).equals( minBindingName ) )
			{
				// Do not add min/max multiple times
				return;
			}
		}

		for ( Iterator<ComputedColumnHandle> bindings = ChartReportItemUtil.getAllColumnBindingsIterator( handle ); bindings.hasNext( ); )
		{
			ComputedColumnHandle column = bindings.next( );
			if ( column.getName( ).equals( bindingValue ) )
			{
				// Create nest total aggregation binding
				IBinding maxBinding = new Binding( maxBindingName );
				maxBinding.setExpression( new ScriptExpression( queryValue.getDefinition( ) ) );
				maxBinding.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );
				maxBinding.setExportable( false );

				IBinding minBinding = new Binding( minBindingName );
				minBinding.setExpression( new ScriptExpression( queryValue.getDefinition( ) ) );
				minBinding.setAggrFunction( IBuildInAggregation.TOTAL_MIN_FUNC );
				minBinding.setExportable( false );

				// create adding nest aggregations operation
				ICubeOperation op = ChartXTabUtil.getCubeElementFactory( )
						.getCubeOperationFactory( )
						.createAddingNestAggregationsOperation( new IBinding[]{
								maxBinding, minBinding
						} );

				// add cube operations to cube query definition
				parent.addCubeOperation( op );

				break;
			}

		}
	}

	private void initBindings( ICubeQueryDefinition cubeQuery, CubeHandle cube )
			throws BirtException
	{
		for ( Iterator bindings = ChartReportItemUtil.getAllColumnBindingsIterator( handle ); bindings.hasNext( ); )
		{
			ComputedColumnHandle column = (ComputedColumnHandle) bindings.next( );
			// Create new binding
			IBinding binding = new Binding( column.getName( ) );
			binding.setDataType( DataAdapterUtil.adaptModelDataType( column.getDataType( ) ) );
			binding.setAggrFunction( column.getAggregateFunction( ) == null ? null
					: DataAdapterUtil.adaptModelAggregationType( column.getAggregateFunction( ) ) );
			String expression = column.getExpression( );
			if ( expression == null )
			{
				for ( Iterator argItr = column.argumentsIterator( ); argItr.hasNext( ); )
				{
					AggregationArgumentHandle aah = (AggregationArgumentHandle) argItr.next( );
					expression = aah.getValue( );
					// Not add expression to arguements
					// binding.addArgument( new ScriptExpression( expression )
					// );
				}
			}
			// Even if expression is null, create the script expression
			binding.setExpression( new ScriptExpression( expression ) );

			List lstAggOn = column.getAggregateOnList( );

			// Do not add aggregateOn to binding in single chart case, because
			// it doesn't use sub query.
			// If expression is null, such as count aggregation, always add all
			// aggregate on levels
			if ( expression == null || !bSingleChart && !lstAggOn.isEmpty( ) )
			{
				// Add aggregate on in binding
				addAggregateOn( binding, lstAggOn, cubeQuery, cube );
			}

			// Add binding query expression here
			registeredBindings.put( column.getName( ), binding );
			// Add raw query expression here
			registeredQueries.put( binding.getBindingName( ), expression );

			// Do not add every binding to cube query, since it may be not used.
			// The binding will be added when found in chart.
		}
	}

	private void addAggregateOn( IBinding binding, List lstAggOn,
			ICubeQueryDefinition cubeQuery, CubeHandle cube )
			throws BirtException
	{
		for ( Iterator iAggs = lstAggOn.iterator( ); iAggs.hasNext( ); )
		{
			String aggOn = (String) iAggs.next( );
			// Convert full level name to dimension expression
			String[] levelNames = CubeUtil.splitLevelName( aggOn );
			String dimExpr = ExpressionUtil.createJSDimensionExpression( levelNames[0],
					levelNames[1] );
			binding.addAggregateOn( dimExpr );
		}
	}

	private void addSorting( ICubeQueryDefinition cubeQuery, CubeHandle cube,
			SeriesDefinition sd, int i ) throws BirtException
	{
		if ( sd.getSortKey( ) == null )
		{
			return;
		}

		String sortKey = sd.getSortKey( ).getDefinition( );
		if ( sd.isSetSorting( ) && sortKey != null && sortKey.length( ) > 0 )
		{
			String sortKeyBinding = ChartXTabUtil.getBindingName( sd.getSortKey( )
					.getDefinition( ),
					true );
			if ( registeredLevels.containsKey( sortKeyBinding ) )
			{
				// Add sorting on dimension
				ICubeSortDefinition sortDef = ChartXTabUtil.getCubeElementFactory( )
						.createCubeSortDefinition( sortKey,
								registeredLevels.get( sortKeyBinding ),
								null,
								null,
								sd.getSorting( ) == SortOption.ASCENDING_LITERAL ? ISortDefinition.SORT_ASC
										: ISortDefinition.SORT_DESC );
				cubeQuery.addSort( sortDef );
			}
			else if ( registeredMeasures.containsKey( sortKeyBinding ) )
			{
				// Add sorting on measures
				Query targetQuery = i > 0 ? sd.getQuery( )
						: (Query) sd.getDesignTimeSeries( )
								.getDataDefinition( )
								.get( 0 );
				IMeasureDefinition mDef = registeredMeasures.get( sortKeyBinding );
				String targetBindingName = ChartXTabUtil.getBindingName( targetQuery.getDefinition( ),
						true );

				// Find measure binding
				IBinding measureBinding = registeredBindings.get( sortKeyBinding );
				// Create new total binding on measure
				IBinding aggBinding = new Binding( measureBinding.getBindingName( )
						+ targetBindingName );
				aggBinding.setDataType( measureBinding.getDataType( ) );
				aggBinding.setExpression( measureBinding.getExpression( ) );
				aggBinding.addAggregateOn( registeredQueries.get( targetBindingName ) );
				aggBinding.setAggrFunction( mDef.getAggrFunction( ) );
				aggBinding.setExportable( false );
				cubeQuery.addBinding( aggBinding );

				ICubeSortDefinition sortDef = ChartXTabUtil.getCubeElementFactory( )
						.createCubeSortDefinition( ExpressionUtil.createJSDataExpression( aggBinding.getBindingName( ) ),
								registeredLevels.get( targetBindingName ),
								null,
								null,
								sd.getSorting( ) == SortOption.ASCENDING_LITERAL ? ISortDefinition.SORT_ASC
										: ISortDefinition.SORT_DESC );
				cubeQuery.addSort( sortDef );
			}
		}
	}

	/**
	 * Adds measure or row/column edge according to query expression.
	 */
	private void bindSeriesQuery( String expr, ICubeQueryDefinition cubeQuery,
			CubeHandle cube ) throws BirtException
	{
		if ( expr != null && expr.length( ) > 0 )
		{
			String bindingName = ChartXTabUtil.getBindingName( expr, true );

			IBinding colBinding = null;
			if ( bindingName != null )
			{
				List<String> nameList = ChartXTabUtil.getBindingNameList( expr );
				if ( nameList.size( ) > 1 )
				{
					// Support multiple data expression concatenation like:
					// data["a"]+data["b"]
					for ( String bn : nameList )
					{
						bindSeriesQuery( ExpressionUtil.createJSDataExpression( bn ),
								cubeQuery,
								cube );
					}
					return;
				}

				colBinding = registeredBindings.get( bindingName );
			}

			if ( colBinding != null )
			{
				bindingName = colBinding.getBindingName( );
				// Convert binding expression like data[] to raw expression
				// like dimension[] or measure[]
				expr = registeredQueries.get( bindingName );

				// Add binding to query definition
				if ( !cubeQuery.getBindings( ).contains( colBinding ) )
				{
					cubeQuery.addBinding( colBinding );
				}

				if ( ChartXTabUtil.isBinding( expr, true ) )
				{
					// Support nest data expression in binding
					bindSeriesQuery( expr, cubeQuery, cube );
					return;
				}

				String measure = ChartXTabUtil.getMeasureName( expr );
				if ( measure != null )
				{
					if ( registeredMeasures.containsKey( bindingName ) )
					{
						return;
					}

					// Add measure
					IMeasureDefinition mDef = cubeQuery.createMeasure( measure );

					String aggFun = DataAdapterUtil.adaptModelAggregationType( cube.getMeasure( measure )
							.getFunction( ) );
					mDef.setAggrFunction( aggFun );
					registeredMeasures.put( bindingName, mDef );

					// AggregateOn has been added in binding when initializing
					// column bindings
				}
				else if ( ChartXTabUtil.isDimensionExpresion( expr ) )
				{
					if ( registeredLevels.containsKey( bindingName ) )
					{
						return;
					}

					// Add row/column edge
					String[] levels = ChartXTabUtil.getLevelNameFromDimensionExpression( expr );
					String dimensionName = levels[0];
					final int edgeType = getEdgeType( dimensionName );
					IEdgeDefinition edge = cubeQuery.getEdge( edgeType );
					IHierarchyDefinition hieDef = null;
					if ( edge == null )
					{
						// Only create one edge/dimension/hierarchy in one
						// direction
						edge = cubeQuery.createEdge( edgeType );
						IDimensionDefinition dimDef = edge.createDimension( dimensionName );
						// Do not use qualified name since it may be from
						// library
						hieDef = dimDef.createHierarchy( cube.getDimension( dimDef.getName( ) )
								.getDefaultHierarchy( )
								.getName( ) );
					}
					else
					{
						hieDef = (IHierarchyDefinition) ( (IDimensionDefinition) edge.getDimensions( )
								.get( 0 ) ).getHierarchy( ).get( 0 );
					}

					// Create level
					boolean bMultipleLevels = !hieDef.getLevels( ).isEmpty( );
					ILevelDefinition levelDef = hieDef.createLevel( levels[1] );

					registeredLevels.put( bindingName, levelDef );

					LevelHandle levelHandle = handle.getModuleHandle( )
							.findLevel( levelDef.getHierarchy( )
									.getDimension( )
									.getName( )
									+ "/" + levelDef.getName( ) ); //$NON-NLS-1$

					registeredLevelHandles.put( levelHandle, levelDef );

					// Reset the level definitions by hierarchy order in
					// multiple levels case
					if ( bMultipleLevels )
					{
						Iterator levelsInOrder = getAllLevelsInHierarchyOrder( cube,
								cubeQuery ).iterator( );
						hieDef.getLevels( ).clear( );
						while ( levelsInOrder.hasNext( ) )
						{
							ILevelDefinition level = (ILevelDefinition) levelsInOrder.next( );
							hieDef.createLevel( level.getName( ) );
						}
					}
				}
			}
		}
	}

	private List<FilterConditionElementHandle> getCubeFiltersFromHandle( )
	{
		PropertyHandle propHandle = handle.getPropertyHandle( ChartReportItemConstants.PROPERTY_CUBE_FILTER );
		if ( propHandle == null )
		{
			return Collections.EMPTY_LIST;
		}
		return propHandle.getListValue( );
	}

	private void addCubeFilter( ICubeQueryDefinition cubeQuery )
			throws BirtException
	{
		List<ILevelDefinition> levels = new ArrayList<ILevelDefinition>( );
		List<String> values = new ArrayList<String>( );

		List<FilterConditionElementHandle> filters = null;
		if ( handle.getContainer( ) instanceof MultiViewsHandle )
		{
			// In multi-view case, query definition will be created by xtab.
			// This code may never be invoked. Leave this for potential risk
			DesignElementHandle xtabHandle = handle.getContainer( )
					.getContainer( );
			if ( xtabHandle instanceof ExtendedItemHandle )
			{
				CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) ( (ExtendedItemHandle) xtabHandle ).getReportItem( );
				filters = getFiltersFromXtab( crossTab );
			}
		}
		else if ( handle.getContainer( ) instanceof ExtendedItemHandle )
		{
			ExtendedItemHandle xtabHandle = (ExtendedItemHandle) handle.getContainer( );
			String exName = xtabHandle.getExtensionName( );
			if ( ICrosstabConstants.AGGREGATION_CELL_EXTENSION_NAME.equals( exName )
					|| ICrosstabConstants.CROSSTAB_CELL_EXTENSION_NAME.equals( exName ) )
			{
				// In xtab cell
				CrosstabCellHandle cell = (CrosstabCellHandle) xtabHandle.getReportItem( );
				filters = getFiltersFromXtab( cell.getCrosstab( ) );
				filters.addAll( getCubeFiltersFromHandle( ) );
			}
		}
		if ( filters == null )
		{
			filters = getCubeFiltersFromHandle( );
		}
		for ( FilterConditionElementHandle filterCon : filters )
		{
			// clean up first
			levels.clear( );
			values.clear( );

			addMembers( levels, values, filterCon.getMember( ) );

			ILevelDefinition[] qualifyLevels = null;
			Object[] qualifyValues = null;

			if ( levels.size( ) > 0 )
			{
				qualifyLevels = levels.toArray( new ILevelDefinition[levels.size( )] );
				qualifyValues = values.toArray( new Object[values.size( )] );
			}

			ConditionalExpression filterCondExpr;

			if ( ModuleUtil.isListFilterValue( filterCon ) )
			{
				filterCondExpr = new ConditionalExpression( filterCon.getExpr( ),
						DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
						filterCon.getValue1List( ) );
			}
			else
			{
				filterCondExpr = new ConditionalExpression( filterCon.getExpr( ),
						DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
						filterCon.getValue1( ),
						filterCon.getValue2( ) );
			}

			ILevelDefinition levelDefinition = null;
			if ( filterCon.getMember( ) != null )
			{
				levelDefinition = registeredLevelHandles.get( filterCon.getMember( )
						.getLevel( ) );
			}
			else
			{
				levelDefinition = registeredLevels.get( ChartXTabUtil.getBindingName( filterCondExpr.getExpression( )
						.getText( ),
						true ) );
			}
			ICubeFilterDefinition filterDef = ChartXTabUtil.getCubeElementFactory( )
					.creatCubeFilterDefinition( filterCondExpr,
							levelDefinition,
							qualifyLevels,
							qualifyValues );

			cubeQuery.addFilter( filterDef );

		}
	}

	private List<FilterConditionElementHandle> getFiltersFromXtab(
			CrosstabReportItemHandle crossTab )
	{
		List<FilterConditionElementHandle> list = new ArrayList<FilterConditionElementHandle>( );
		if ( crossTab == null )
		{
			return list;
		}
		if ( crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE )
					.getModelHandle( );
			list.addAll( getLevelOnCrosstab( (ExtendedItemHandle) elementHandle ) );
		}

		if ( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE )
					.getModelHandle( );
			list.addAll( getLevelOnCrosstab( (ExtendedItemHandle) elementHandle ) );
		}

		int measureCount = crossTab.getMeasureCount( );
		for ( int i = 0; i < measureCount; i++ )
		{
			MeasureViewHandle measureView = crossTab.getMeasure( i );
			Iterator<FilterConditionElementHandle> iter = measureView.filtersIterator( );
			while ( iter.hasNext( ) )
			{
				list.add( iter.next( ) );
			}
		}

		return list;
	}

	private List getLevelOnCrosstab( ExtendedItemHandle handle )
	{
		CrosstabViewHandle crossTabViewHandle = null;
		try
		{
			crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			logger.log( e );
		}
		List list = new ArrayList( );
		if ( crossTabViewHandle == null )
		{
			return list;
		}
		int dimensionCount = crossTabViewHandle.getDimensionCount( );

		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dimension = crossTabViewHandle.getDimension( i );
			int levelCount = dimension.getLevelCount( );
			for ( int j = 0; j < levelCount; j++ )
			{
				LevelViewHandle levelHandle = dimension.getLevel( j );
				Iterator iter = levelHandle.filtersIterator( );
				while ( iter.hasNext( ) )
				{
					list.add( iter.next( ) );
				}

			}
		}
		return list;
	}

	/**
	 * Recursively add all member values and associated levels to the given
	 * list.
	 */
	private void addMembers( List<ILevelDefinition> levels,
			List<String> values, MemberValueHandle member )
	{
		if ( member != null )
		{
			ILevelDefinition levelDef = registeredLevelHandles.get( member.getLevel( ) );

			if ( levelDef != null )
			{
				levels.add( levelDef );
				values.add( member.getValue( ) );

				if ( member.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) > 0 )
				{
					// only use first member here
					addMembers( levels,
							values,
							(MemberValueHandle) member.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
									0 ) );
				}
			}
		}
	}

	/**
	 * Gets all levels and sorts them in hierarchy order in multiple levels
	 * case.
	 * 
	 * @param cubeHandle
	 * @param cubeQuery
	 */
	private Collection<ILevelDefinition> getAllLevelsInHierarchyOrder(
			CubeHandle cubeHandle, ICubeQueryDefinition cubeQuery )
	{
		Collection<ILevelDefinition> levelValues = registeredLevels.values( );
		// Only sort the level for multiple levels case
		if ( cubeQuery.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) == null
				&& levelValues.size( ) > 1 )
		{
			List<ILevelDefinition> levelList = new ArrayList<ILevelDefinition>( levelValues.size( ) );
			String dimensionName = null;
			int firstLevelIndex = 0;
			int i = 0;
			HierarchyHandle hh = null;
			for ( Iterator<ILevelDefinition> iterator = levelValues.iterator( ); iterator.hasNext( ); i++ )
			{
				ILevelDefinition level = iterator.next( );

				if ( i == 0 )
				{
					dimensionName = level.getHierarchy( )
							.getDimension( )
							.getName( );
					hh = cubeHandle.getDimension( dimensionName )
							.getDefaultHierarchy( );
					while ( firstLevelIndex < hh.getLevelCount( ) )
					{
						if ( hh.getLevel( firstLevelIndex )
								.getName( )
								.equals( level.getName( ) ) )
						{
							break;
						}
						firstLevelIndex++;
					}

					levelList.add( level );
				}
				else
				{
					while ( firstLevelIndex < hh.getLevelCount( ) )
					{
						if ( hh.getLevel( firstLevelIndex )
								.getName( )
								.equals( level.getName( ) ) )
						{
							break;
						}
						firstLevelIndex++;
					}

					if ( firstLevelIndex < hh.getLevelCount( ) )
					{
						// Ascending order
						levelList.add( level );
					}
					else
					{
						// Descending order
						levelList.add( 0, level );
					}
				}
			}

			return levelList;
		}
		return levelValues;
	}

	private int getEdgeType( String dimensionName )
	{
		if ( this.rowEdgeDimension == null )
		{
			this.rowEdgeDimension = dimensionName;
			return ICubeQueryDefinition.ROW_EDGE;
		}
		return this.rowEdgeDimension.equals( dimensionName ) ? ICubeQueryDefinition.ROW_EDGE
				: ICubeQueryDefinition.COLUMN_EDGE;
	}

	static List<SeriesDefinition> getAllSeriesDefinitions( Chart chart )
	{
		List<SeriesDefinition> seriesList = new ArrayList<SeriesDefinition>( );
		if ( chart instanceof ChartWithAxes )
		{
			Axis xAxis = (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 );
			// Add base series definitions
			seriesList.addAll( xAxis.getSeriesDefinitions( ) );
			EList axisList = xAxis.getAssociatedAxes( );
			for ( int i = 0; i < axisList.size( ); i++ )
			{
				// Add value series definitions
				seriesList.addAll( ( (Axis) axisList.get( i ) ).getSeriesDefinitions( ) );
			}
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			SeriesDefinition sdBase = (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 );
			seriesList.add( sdBase );
			seriesList.addAll( sdBase.getSeriesDefinitions( ) );
		}
		return seriesList;
	}

}
