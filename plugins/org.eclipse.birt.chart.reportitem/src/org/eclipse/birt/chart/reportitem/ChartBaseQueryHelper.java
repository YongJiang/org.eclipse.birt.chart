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

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.emf.common.util.EList;

/**
 * The class is responsible to add group bindings of chart on query definition.
 * 
 * @since BIRT 2.3
 */
public class ChartBaseQueryHelper extends AbstractChartBaseQueryGenerator
{
	
	/**
	 * Constructor of the class.
	 * 
	 * @param chart
	 * @param handle
	 */
	public ChartBaseQueryHelper( ReportItemHandle handle, Chart cm )
	{
		super( handle, cm );
	}
	
	public IDataQueryDefinition createBaseQuery( IDataQueryDefinition parent ) throws ChartException
	{
		BaseQueryDefinition query = createQueryDefinition( parent );

		if ( query == null )
		{
			return null;
		}
		
		generateGroupBindings( query );

		return query;
	}
	
	protected BaseQueryDefinition createQueryDefinition(
			IDataQueryDefinition parent ) throws ChartException
	{
		BaseQueryDefinition query = null;

		BaseQueryDefinition parentQuery = null;
		if ( parent instanceof BaseQueryDefinition )
		{
			parentQuery = (BaseQueryDefinition) parent;
		}

		DataSetHandle dsHandle = fReportItemHandle.getDataSet( );

		if ( dsHandle == null )
		{
			// dataset reference error
			String dsName = (String) fReportItemHandle.getProperty( ReportItemHandle.DATA_SET_PROP );
			if ( dsName != null && dsName.length( ) > 0 )
			{
				throw new ChartException( ChartReportItemPlugin.ID,
						ChartException.DATA_BINDING,
						new EngineException( MessageConstants.UNDEFINED_DATASET_ERROR,
								dsName ) );
			}
			// we has data set name defined, so test if we have column
			// binding here.

			if ( parent instanceof ICubeQueryDefinition )
			{
				return null;
				// return createSubQuery(item, null);
			}

			if ( ChartReportItemUtil.canScaleShared( fReportItemHandle,
					fChartModel ) )
			{
				// Add min/max binding to parent query since it's global min/max
				addMinMaxBinding( ChartReportItemUtil.getBindingHolder( fReportItemHandle ),
						parentQuery );
			}

			// we have column binding, create a sub query.
			query = createSubQuery( fReportItemHandle, parentQuery );
		}
		else
		{

			// The report item has a data set definition, must create a query
			// for
			// it.
			query = new QueryDefinition( parentQuery );
			( (QueryDefinition) query ).setDataSetName( dsHandle.getQualifiedName( ) );

			// bind the query with parameters
			( (QueryDefinition) query ).getInputParamBindings( )
					.addAll( createParamBindings( fReportItemHandle.paramBindingsIterator( ) ) );

			Iterator iter = fReportItemHandle.columnBindingsIterator( );
			while ( iter.hasNext( ) )
			{
				ComputedColumnHandle binding = (ComputedColumnHandle) iter.next( );
				addColumBinding( query, binding );
			}

			addSortAndFilter( fReportItemHandle, query );
		}
		return query;
	}

	protected void addColumBinding( IBaseQueryDefinition transfer,
			ComputedColumnHandle columnBinding ) throws ChartException
	{
		String name = columnBinding.getName( );
		String expr = columnBinding.getExpression( );
		String type = columnBinding.getDataType( );
		int dbType = ModelDteApiAdapter.toDteDataType( type );
		IBaseExpression dbExpr = new ScriptExpression( expr, dbType );
		if ( columnBinding.getAggregateOn( ) != null )
		{
			dbExpr.setGroupName( columnBinding.getAggregateOn( ) );
		}
		IBinding binding = new Binding( name, dbExpr );
		try
		{
			if ( columnBinding.getAggregateOn( ) != null )
				binding.addAggregateOn( columnBinding.getAggregateOn( ) );
			if ( columnBinding.getAggregateFunction( ) != null )
			{
				binding.setAggrFunction( columnBinding.getAggregateFunction( ) );
			}
			String filter = columnBinding.getFilterExpression( );
			if ( filter != null )
			{
				binding.setFilter( new ScriptExpression( filter ) );
			}
			Iterator arguments = columnBinding.argumentsIterator( );
			if ( arguments != null )
			{
				while ( arguments.hasNext( ) )
				{
					AggregationArgumentHandle argumentHandle = (AggregationArgumentHandle) arguments.next( );
					String argument = argumentHandle.getValue( );
					if ( argument != null )
					{
						binding.addArgument( new ScriptExpression( argument ) );
					}
				}
			}
			transfer.addBinding( binding );
		}
		catch ( DataException ex )
		{
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.DATA_BINDING,
					ex );
		}
	}

	private void addMinMaxBinding( ReportItemHandle handle,
			BaseQueryDefinition query ) throws ChartException
	{
		// Add min/max bindings for the query expression in first value
		// series, so share the scale later
		try
		{
			String queryExp = getExpressionOfValueSeries( );

			ComputedColumn ccMin = StructureFactory.newComputedColumn( handle,
					ChartReportItemUtil.QUERY_MIN );
			ccMin.setAggregateFunction( DesignChoiceConstants.AGGREGATION_FUNCTION_MIN );
			ccMin.setExpression( queryExp );
			addColumBinding( query, handle.addColumnBinding( ccMin, false ) );

			ComputedColumn ccMax = StructureFactory.newComputedColumn( handle,
					ChartReportItemUtil.QUERY_MAX );
			ccMax.setAggregateFunction( DesignChoiceConstants.AGGREGATION_FUNCTION_MAX );
			ccMax.setExpression( queryExp );
			addColumBinding( query, handle.addColumnBinding( ccMax, false ) );
		}
		catch ( SemanticException e )
		{
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.DATA_BINDING,
					e );
		}
	}

	protected BaseQueryDefinition createSubQuery( ReportItemHandle handle,
			BaseQueryDefinition parentQuery ) throws ChartException
	{
		BaseQueryDefinition query = null;
		// sub query must be defined in a transform
		if ( parentQuery == null )
		{
			// no parent query exits, so create a empty query for it.
			query = new QueryDefinition( null );
		}
		else
		{
			// create a sub query
			query = new SubqueryDefinition( ChartReportItemConstants.CHART_SUBQUERY, parentQuery );
			parentQuery.getSubqueries( ).add( query );
		}

		Iterator iter = handle.columnBindingsIterator( );
		while ( iter.hasNext( ) )
		{
			ComputedColumnHandle binding = (ComputedColumnHandle) iter.next( );
			addColumBinding( query, binding );
		}

		addSortAndFilter( handle, query );

		return query;
	}

	protected void addSortAndFilter( ReportItemHandle handle,
			BaseQueryDefinition query )
	{
		if ( handle instanceof ExtendedItemHandle )
		{
			query.getFilters( )
					.addAll( createFilters( ( (ExtendedItemHandle) handle ).filtersIterator( ) ) );
		}
		else if ( handle instanceof TableHandle )
		{
			query.getFilters( )
					.addAll( createFilters( ( (TableHandle) handle ).filtersIterator( ) ) );
		}		
	}

	/**
	 * create a filter array given a filter condition handle iterator
	 * 
	 * @param iter
	 *            the iterator
	 * @return filter array
	 */
	private ArrayList createFilters( Iterator iter )
	{
		ArrayList filters = new ArrayList( );
		if ( iter != null )
		{

			while ( iter.hasNext( ) )
			{
				FilterConditionHandle filterHandle = (FilterConditionHandle) iter.next( );
				IFilterDefinition filter = createFilter( filterHandle );
				filters.add( filter );
			}
		}
		return filters;
	}

	/**
	 * create one Filter given a filter condition handle
	 * 
	 * @param handle
	 *            a filter condition handle
	 * @return the filter
	 */
	protected IFilterDefinition createFilter( FilterConditionHandle handle )
	{
		String filterExpr = handle.getExpr( );
		if ( filterExpr == null || filterExpr.length( ) == 0 )
			return null; // no filter defined

		// converts to DtE exprFilter if there is no operator
		String filterOpr = handle.getOperator( );
		if ( filterOpr == null || filterOpr.length( ) == 0 )
			return new FilterDefinition( new ScriptExpression( filterExpr ) );

		/*
		 * has operator defined, try to convert filter condition to
		 * operator/operand style column filter with 0 to 2 operands
		 */

		String column = filterExpr;
		int dteOpr = ModelDteApiAdapter.toDteFilterOperator( filterOpr );
		if ( ModuleUtil.isListFilterValue( handle ) )
		{
			List operand1List = handle.getValue1List( );
			return new FilterDefinition( new ConditionalExpression( column,
					dteOpr,
					operand1List ) );
		}
		String operand1 = handle.getValue1( );
		String operand2 = handle.getValue2( );
		return new FilterDefinition( new ConditionalExpression( column,
				dteOpr,
				operand1,
				operand2 ) );
	}

	/**
	 * create input parameter bindings
	 * 
	 * @param iter
	 *            parameter bindings iterator
	 * @return a list of input parameter bindings
	 */
	protected List createParamBindings( Iterator iter )
	{
		ArrayList list = new ArrayList( );
		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				ParamBindingHandle modelParamBinding = (ParamBindingHandle) iter.next( );
				IInputParameterBinding binding = createParamBinding( modelParamBinding );
				if ( binding != null )
				{
					list.add( binding );
				}
			}
		}
		return list;
	}

	/**
	 * create input parameter binding
	 * 
	 * @param handle
	 * @return
	 */
	protected IInputParameterBinding createParamBinding(
			ParamBindingHandle handle )
	{
		if ( handle.getExpression( ) == null )
			return null; // no expression is bound
		ScriptExpression expr = new ScriptExpression( handle.getExpression( ) );
		// model provides binding by name only
		return new InputParameterBinding( handle.getParamName( ), expr );
	}

	
	private String getExpressionOfValueSeries( )
	{
		SeriesDefinition ySd;
		if ( fChartModel instanceof ChartWithAxes )
		{
			Axis yAxis = (Axis) ( (Axis) ( (ChartWithAxes) fChartModel ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 );
			ySd = (SeriesDefinition) yAxis.getSeriesDefinitions( ).get( 0 );
		}
		else
		{
			ySd = (SeriesDefinition) ( (SeriesDefinition) ( (ChartWithoutAxes) fChartModel ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).get( 0 );
		}
		Query query = (Query) ySd.getDesignTimeSeries( )
				.getDataDefinition( )
				.get( 0 );
		return query.getDefinition( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator#createBaseQuery(java.util.List)
	 */
	public IDataQueryDefinition createBaseQuery( List columns )
	{
		throw new UnsupportedOperationException( "Don't be implemented in the class." ); //$NON-NLS-1$
	}
	
	/**
	 * Returns all query experssion definitions on chart.
	 * 
	 * @param chart
	 * @return
	 * @since 2.3
	 */
	public static List getAllQueryExpressionDefinitions( Chart chart )
	{
		List queryList = new ArrayList();
		if ( chart instanceof ChartWithAxes )
		{
			Axis xAxis = (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 );
			// Add base series query
			queryList.addAll( getQueries( xAxis.getSeriesDefinitions( ) ) );
			
			EList axisList = xAxis.getAssociatedAxes( );
			for ( int i = 0; i < axisList.size( ); i++ )
			{
				EList sds = ( (Axis) axisList.get( i ) ).getSeriesDefinitions( );
				
				// Add Y grouping query.
				Query q =  ((SeriesDefinition)sds.get( 0 )).getQuery( );
				if ( q != null ) 
				{
					queryList.add( q );
				}
				
				// Add value series querys.
				queryList.addAll( getQueries( sds ));
				
			}
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			SeriesDefinition sdBase = (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 );
			queryList.addAll( sdBase.getDesignTimeSeries( ).getDataDefinition( ) );
			
			Query q =  ((SeriesDefinition)sdBase.getSeriesDefinitions( ).get( 0 )).getQuery( );
			if ( q != null ) 
			{
				queryList.add( q );
			}
			
			queryList.addAll( getQueries( sdBase.getSeriesDefinitions( ) ) );
		}
		return queryList;
	}
	
	/**
	 * Returns queries of series definition.
	 * 
	 * @param seriesDefinitions
	 * @return
	 */
	private static List getQueries( EList seriesDefinitions )
	{
		List querys = new ArrayList( );
		for ( Iterator iter = seriesDefinitions.iterator( ); iter.hasNext( ); ) 
		{
			querys.addAll( ((SeriesDefinition)iter.next( )).getDesignTimeSeries( ).getDataDefinition( ) );
		}
		return querys;
	}
}
