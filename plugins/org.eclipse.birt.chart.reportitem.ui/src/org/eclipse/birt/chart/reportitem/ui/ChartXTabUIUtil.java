/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Utility class for XTab integration in UI
 */

public class ChartXTabUIUtil extends ChartXTabUtil
{

	private final static DimensionValue DEFAULT_COLUMN_WIDTH = new DimensionValue( 80,
			DesignChoiceConstants.UNITS_PT );
	private final static DimensionValue DEFAULT_ROW_WIDTH = new DimensionValue( 30,
			DesignChoiceConstants.UNITS_PT );

	/**
	 * Adds Axis chart in XTab
	 * 
	 * @param cell
	 * @param cwa
	 * @param chartHandle
	 * @param bNewTotalJustAdded
	 * @throws BirtException
	 * 
	 */
	public static boolean addAxisChartInXTab( AggregationCellHandle cell,
			ChartWithAxes cwa, ExtendedItemHandle hostChartHandle,
			boolean bNewTotalJustAdded ) throws BirtException
	{
		boolean bTransposed = cwa.isTransposed( );
		int axisType = getXTabAxisType( bTransposed );
		if ( bTransposed )
		{
			// Set cell span
			cell.setSpanOverOnRow( cell.getAggregationOnRow( ) );
			cell.setSpanOverOnColumn( null );
			CrosstabCellHandle rowCell = ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.ROW_AXIS_TYPE );
			if ( rowCell == null )
			{
				return false;
			}
			if ( rowCell.getHeight( ) == null
					|| rowCell.getHeight( ).getMeasure( ) == 0 )
			{
				// Set a default height for cell to fit with chart
				cell.getCrosstab( ).setRowHeight( rowCell, DEFAULT_ROW_WIDTH );
			}
			rowCell.getCrosstabHandle( )
					.setProperty( StyleHandle.PADDING_TOP_PROP,
							new DimensionValue( 0,
									DesignChoiceConstants.UNITS_PT ) );
			rowCell.getCrosstabHandle( )
					.setProperty( StyleHandle.PADDING_BOTTOM_PROP,
							new DimensionValue( 0,
									DesignChoiceConstants.UNITS_PT ) );
		}
		else
		{
			// Set cell span
			cell.setSpanOverOnColumn( cell.getAggregationOnColumn( ) );
			cell.setSpanOverOnRow( null );
			CrosstabCellHandle columnCell = ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.COLUMN_AXIS_TYPE );
			if ( columnCell == null )
			{
				return false;
			}
			if ( columnCell.getWidth( ) != null
					|| columnCell.getWidth( ).getMeasure( ) == 0 )
			{
				// Set a default width for cell to fit with chart
				cell.getCrosstab( ).setColumnWidth( columnCell,
						DEFAULT_COLUMN_WIDTH );
			}
			columnCell.getCrosstabHandle( )
					.setProperty( StyleHandle.PADDING_LEFT_PROP,
							new DimensionValue( 0,
									DesignChoiceConstants.UNITS_PT ) );
			columnCell.getCrosstabHandle( )
					.setProperty( StyleHandle.PADDING_RIGHT_PROP,
							new DimensionValue( 0,
									DesignChoiceConstants.UNITS_PT ) );
		}

		if ( !isYAxisVisible( cwa ) )
		{
			// Do not add axis chart if Y axis is invisible
			return bNewTotalJustAdded;
		}

		// Create grand total cell on demand
		// If just added, even if grand total is not added this time, delete
		// data item
		boolean bNewGrandTotol = bNewTotalJustAdded;
		if ( cell.getCrosstab( ).getGrandTotal( axisType ) == null )
		{
			bNewGrandTotol = true;
			cell.getCrosstab( ).addGrandTotal( axisType );
		}
		// Create axis chart handle which references to host chart
		ExtendedItemHandle axisChartHandle = createChartHandle( cell.getModelHandle( ),
				ChartReportItemConstants.TYPE_AXIS_CHART,
				hostChartHandle );

		AggregationCellHandle grandTotalAggCell;
		if ( bTransposed )
		{
			grandTotalAggCell = ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( null,
					null,
					cell.getDimensionName( ICrosstabConstants.COLUMN_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.COLUMN_AXIS_TYPE ) );
		}
		else
		{
			grandTotalAggCell = ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( cell.getDimensionName( ICrosstabConstants.ROW_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.ROW_AXIS_TYPE ),
					null,
					null );
		}
		if ( bNewGrandTotol )
		{
			// Only delete data item in grand total when it's created by chart
			Object content = ChartXTabUtil.getFirstContent( grandTotalAggCell );
			if ( content instanceof DesignElementHandle )
			{
				( (DesignElementHandle) content ).dropAndClear( );
			}
		}
		if ( grandTotalAggCell != null )
		{
			grandTotalAggCell.addContent( axisChartHandle, 0 );
		}
		return bNewGrandTotol;
	}

	/**
	 * Updates Axis chart in Xtab by replacing date item with axis chart.
	 * 
	 * @param cell
	 * @param cwa
	 * @param hostChartHandle
	 * @throws BirtException
	 */
	public static void updateAxisChart( AggregationCellHandle cell,
			ChartWithAxes cwa, ExtendedItemHandle hostChartHandle )
			throws BirtException
	{
		boolean bTransposed = cwa.isTransposed( );
		if ( getGrandTotalCell( cell, bTransposed ) != null )
		{
			AggregationCellHandle grandTotalAggCell = getGrandTotalAggregationCell( cell,
					bTransposed );

			Object content = ChartXTabUtil.getFirstContent( grandTotalAggCell );
			if ( content instanceof DataItemHandle )
			{
				// Do not delete the data item
				// Create axis chart handle, and insert it before data item
				if ( isYAxisVisible( cwa ) )
				{
					ExtendedItemHandle axisChartHandle = createChartHandle( cell.getModelHandle( ),
							ChartReportItemConstants.TYPE_AXIS_CHART,
							hostChartHandle );
					grandTotalAggCell.addContent( axisChartHandle, 0 );
				}
			}
		}
	}

	private static AggregationCellHandle getGrandTotalAggregationCell(
			AggregationCellHandle cell, boolean bTransposed )
	{
		if ( cell == null )
		{
			return null;
		}
		if ( bTransposed )
		{
			return ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( null,
					null,
					cell.getDimensionName( ICrosstabConstants.COLUMN_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.COLUMN_AXIS_TYPE ) );
		}
		else
		{
			return ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( cell.getDimensionName( ICrosstabConstants.ROW_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.ROW_AXIS_TYPE ),
					null,
					null );
		}
	}

	private static CrosstabCellHandle getGrandTotalCell(
			AggregationCellHandle cell, boolean bTransposed )
	{
		if ( cell == null )
		{
			return null;
		}
		return cell.getCrosstab( )
				.getGrandTotal( bTransposed ? ICrosstabConstants.ROW_AXIS_TYPE
						: ICrosstabConstants.COLUMN_AXIS_TYPE );
	}

	/**
	 * Finds the plot/axis chart handle in other measures. If only one measure,
	 * return empty list.
	 * 
	 * @param cell
	 * @param bPlotChart
	 *            true returns plot chart, false returns axis true
	 * @return list contains plot chart handle
	 */
	public static List<ExtendedItemHandle> findChartInOtherMeasures(
			AggregationCellHandle cell, boolean bPlotChart )
	{
		if ( cell.getCrosstab( ).getMeasureCount( ) > 1 )
		{
			List<ExtendedItemHandle> list = new ArrayList<ExtendedItemHandle>( );
			for ( int i = 0; i < cell.getCrosstab( ).getMeasureCount( ); i++ )
			{
				MeasureViewHandle mv = cell.getCrosstab( ).getMeasure( i );
				if ( mv == cell.getContainer( ) )
				{
					// Do not check current measure
					continue;
				}
				// Check detail cell
				AggregationCellHandle aggCell = mv.getCell( );
				Object content = ChartXTabUtil.getFirstContent( aggCell );
				if ( bPlotChart
						&& ChartXTabUtil.isPlotChart( (DesignElementHandle) content )
						|| !bPlotChart
						&& ChartXTabUtil.isAxisChart( (DesignElementHandle) content ) )
				{
					list.add( (ExtendedItemHandle) content );
				}
				// Check total cells
				for ( int j = 0; j < mv.getAggregationCount( ); j++ )
				{
					aggCell = mv.getAggregationCell( j );
					content = ChartXTabUtil.getFirstContent( aggCell );
					if ( bPlotChart
							&& ChartXTabUtil.isPlotChart( (DesignElementHandle) content )
							|| !bPlotChart
							&& ChartXTabUtil.isAxisChart( (DesignElementHandle) content ) )
					{
						list.add( (ExtendedItemHandle) content );
					}
				}
			}
			return list;
		}
		return Collections.EMPTY_LIST;
	}

	private static boolean isEmptyInAllGrandTotalCells(
			CrosstabReportItemHandle xtab, boolean bTransposed )
	{
		for ( int i = 0; i < xtab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle mv = xtab.getMeasure( i );
			AggregationCellHandle grandTotalCell = getGrandTotalAggregationCell( mv.getCell( ),
					bTransposed );
			if ( grandTotalCell != null
					&& grandTotalCell.getContents( ).size( ) > 0 )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Removes Axis chart in Xtab.
	 * 
	 * @param cell
	 * @param bTransposed
	 * @throws BirtException
	 */
	public static void removeAxisChartInXTab( AggregationCellHandle cell,
			boolean bTransposed ) throws BirtException
	{
		cell.setSpanOverOnRow( null );
		cell.setSpanOverOnColumn( null );
		AggregationCellHandle grandTotalAggCell = getGrandTotalAggregationCell( cell,
				bTransposed );
		if ( grandTotalAggCell != null
				&& grandTotalAggCell.getContents( ).size( ) > 0 )
		{
			Object content = getFirstContent( grandTotalAggCell );
			if ( isAxisChart( (DesignElementHandle) content ) )
			{
				( (DesignElementHandle) content ).dropAndClear( );
			}

			if ( isEmptyInAllGrandTotalCells( cell.getCrosstab( ), bTransposed ) )
			{
				// Delete blank grand total cell
				cell.getCrosstab( )
						.removeGrandTotal( getXTabAxisType( bTransposed ) );
			}
		}
	}

	/**
	 * Updates XTab for Axis chart sync
	 * 
	 * @param cell
	 * @param hostChartHandle
	 * @param bTransOld
	 * @param cmNew
	 * @throws BirtException
	 */
	public static void updateXTabForAxis( AggregationCellHandle cell,
			ExtendedItemHandle hostChartHandle, boolean bTransOld,
			ChartWithAxes cmNew ) throws BirtException
	{
		boolean bTransNew = cmNew.isTransposed( );
		if ( bTransOld != bTransNew )
		{
			// Update xtab direction for multiple measure case
			updateXTabDirection( cell.getCrosstab( ), bTransNew );

			// Update the chart's direction in other measures
			boolean bNewTotalJustAdded = false;
			List<ExtendedItemHandle> otherPlotCharts = findChartInOtherMeasures( cell,
					true );
			for ( int i = 0; i < otherPlotCharts.size( ); i++ )
			{
				ExtendedItemHandle chartInOtherMeasure = otherPlotCharts.get( i );
				if ( chartInOtherMeasure != null )
				{
					// Update some properties when transposing
					ChartWithAxes cwa = updateChartModelWhenTransposing( chartInOtherMeasure,
							cmNew );
					AggregationCellHandle cellAgg = getXtabContainerCell( chartInOtherMeasure );
					removeAxisChartInXTab( cellAgg, bTransOld );
					bNewTotalJustAdded = addAxisChartInXTab( cellAgg,
							cwa,
							chartInOtherMeasure,
							bNewTotalJustAdded );
				}
			}

			// Delete grand total only once, since assume that multiple
			// measures will have the same grand total
			removeAxisChartInXTab( cell, bTransOld );
			addAxisChartInXTab( cell,
					cmNew,
					hostChartHandle,
					bNewTotalJustAdded );
		}
	}

	private static ChartWithAxes updateChartModelWhenTransposing(
			ExtendedItemHandle eih, ChartWithAxes cmFrom )
			throws ExtendedElementException
	{
		ChartReportItemImpl reportItem = (ChartReportItemImpl) eih.getReportItem( );
		ChartWithAxes cmOld = (ChartWithAxes) reportItem.getProperty( ChartReportItemConstants.PROPERTY_CHART );
		ChartWithAxes cmNew = (ChartWithAxes) EcoreUtil.copy( cmOld );

		cmNew.setTransposed( cmFrom.isTransposed( ) );
		Query queryFrom = (Query) ( (SeriesDefinition) ( (Axis) cmFrom.getAxes( )
				.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getDesignTimeSeries( )
				.getDataDefinition( )
				.get( 0 );
		Query queryTo = (Query) ( (SeriesDefinition) ( (Axis) cmNew.getAxes( )
				.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getDesignTimeSeries( )
				.getDataDefinition( )
				.get( 0 );
		queryTo.setDefinition( queryFrom.getDefinition( ) );
		reportItem.executeSetModelCommand( eih, cmOld, cmNew );
		return cmNew;
	}

	public static ExtendedItemHandle createChartHandle(
			DesignElementHandle anyHandle, String chartType,
			ExtendedItemHandle hostChartHandle ) throws SemanticException
	{
		String name = ReportPlugin.getDefault( )
				.getCustomName( ChartReportItemConstants.CHART_EXTENSION_NAME );
		ExtendedItemHandle chartHandle = anyHandle.getElementFactory( )
				.newExtendedItem( name,
						ChartReportItemConstants.CHART_EXTENSION_NAME );
		if ( chartType != null )
		{
			chartHandle.setProperty( ChartReportItemConstants.PROPERTY_CHART_TYPE,
					chartType );
		}
		if ( hostChartHandle != null )
		{
			chartHandle.setProperty( ChartReportItemConstants.PROPERTY_HOST_CHART,
					hostChartHandle );
		}
		return chartHandle;
	}

	/**
	 * Check if the expressions of category and Y optional have same dimension.
	 * 
	 * @param checkType
	 * @param data
	 * @param cm
	 * @param itemHandle
	 * @param provider
	 * @return <code>true</code> means the data check is past.
	 * @since 2.3
	 */
	public static boolean checkQueryExpression( String checkType, Object data,
			Chart cm, ExtendedItemHandle itemHandle,
			ReportDataServiceProvider provider )
	{
		if ( data == null || "".equals( data ) ) //$NON-NLS-1$
		{
			return true;
		}

		String categoryDimension = null;
		String yOptionDimension = null;
		String categoryBindName = null;
		String yOptionBindName = null;

		String expression = (String) data;

		Map<String, Query[]> queryDefinitionsMap = QueryUIHelper.getQueryDefinitionsMap( cm );

		// Compare if dimensions between category expression and Y optional
		// expression are same.
		Iterator<ComputedColumnHandle> columnBindings = null;
		if ( ChartXTabUtil.getBindingCube( itemHandle ) != null
				&& provider.isInheritanceOnly( )
				|| provider.isSharedBinding( ) )
		{
			ReportItemHandle reportItemHandle = provider.getReportItemHandle( );
			columnBindings = reportItemHandle.getColumnBindings( ).iterator( );
		}
		else if ( ChartXTabUtil.getBindingCube( itemHandle ) != null
				|| ( provider.isInXTabMeasureCell( ) && !provider.isPartChart( ) ) ) // 
		{
			columnBindings = ChartXTabUtil.getAllColumnBindingsIterator( itemHandle );
		}

		if ( ChartUIConstants.QUERY_OPTIONAL.equals( checkType ) )
		{
			String categoryExpr = null;
			Query[] querys = queryDefinitionsMap.get( ChartUIConstants.QUERY_CATEGORY );
			if ( querys != null && querys.length > 0 )
			{
				categoryExpr = querys[0].getDefinition( );
			}
			if ( categoryExpr == null || "".equals( categoryExpr ) ) //$NON-NLS-1$
			{
				return true;
			}

			categoryBindName = ChartXTabUtil.getBindingName( categoryExpr, true );
			yOptionBindName = ChartXTabUtil.getBindingName( expression, true );
		}
		else if ( ChartUIConstants.QUERY_CATEGORY.equals( checkType ) )
		{
			String yOptionExpr = null;
			Query[] querys = queryDefinitionsMap.get( ChartUIConstants.QUERY_OPTIONAL );
			if ( querys != null && querys.length > 0 )
			{
				yOptionExpr = querys[0].getDefinition( );
			}
			if ( yOptionExpr == null || "".equals( yOptionExpr ) ) //$NON-NLS-1$
			{
				return true;
			}

			categoryBindName = ChartXTabUtil.getBindingName( expression, true );
			yOptionBindName = ChartXTabUtil.getBindingName( yOptionExpr, true );
		}

		if ( columnBindings == null )
		{
			return true;
		}

		while ( columnBindings.hasNext( ) )
		{
			ComputedColumnHandle columnHandle = columnBindings.next( );
			String bindName = columnHandle.getName( );
			String expr = columnHandle.getExpression( );
			if ( !ChartXTabUtil.isDimensionExpresion( expr ) )
			{
				continue;
			}

			if ( bindName.equals( categoryBindName ) )
			{
				categoryDimension = ChartXTabUtil.getLevelNameFromDimensionExpression( expr )[0];
			}

			if ( bindName.equals( yOptionBindName ) )
			{
				yOptionDimension = ChartXTabUtil.getLevelNameFromDimensionExpression( expr )[0];
			}
		}

		if ( ( categoryDimension != null && yOptionDimension != null && categoryDimension.equals( yOptionDimension ) ) )
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static boolean isTransposedChartWithAxes( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			return ( (ChartWithAxes) cm ).isTransposed( );
		}
		throw new IllegalArgumentException( Messages.getString( "Error.ChartShouldIncludeAxes" ) ); //$NON-NLS-1$
	}

	private static boolean isYAxisVisible( ChartWithAxes cwa )
	{
		Axis yAxis = (Axis) ( (Axis) cwa.getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.get( 0 );
		return yAxis.getLineAttributes( ).isVisible( );
	}

	public static void updateXTabDirection( CrosstabReportItemHandle xtab,
			boolean bTransposed ) throws SemanticException
	{
		if ( bTransposed )
		{
			if ( !ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL.equals( xtab.getMeasureDirection( ) ) )
			{
				xtab.setMeasureDirection( ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL );
			}
		}
		else
		{
			if ( !ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( xtab.getMeasureDirection( ) ) )
			{
				xtab.setMeasureDirection( ICrosstabConstants.MEASURE_DIRECTION_VERTICAL );
			}
		}
	}
}
