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

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.reportitem.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;

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
	 * @param bTransposed
	 * @param chartHandle
	 * @throws BirtException
	 * 
	 */
	public static void addAxisChartInXTab( AggregationCellHandle cell,
			boolean bTransposed, ExtendedItemHandle hostChartHandle )
			throws BirtException
	{
		int axisType = bTransposed ? ICrosstabConstants.ROW_AXIS_TYPE
				: ICrosstabConstants.COLUMN_AXIS_TYPE;
		if ( bTransposed )
		{
			// Set cell span
			cell.setSpanOverOnRow( cell.getAggregationOnRow( ) );
			CrosstabCellHandle rowCell = ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.ROW_AXIS_TYPE );
			if ( rowCell == null )
			{
				return;
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
			CrosstabCellHandle columnCell = ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.COLUMN_AXIS_TYPE );
			if ( columnCell == null )
			{
				return;
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

		// Create grand total cell on demand
		if ( cell.getCrosstab( ).getGrandTotal( axisType ) == null )
		{
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
		Object content = ChartXTabUtil.getFirstContent( grandTotalAggCell );
		if ( content instanceof DesignElementHandle )
		{
			( (DesignElementHandle) content ).dropAndClear( );
		}
		grandTotalAggCell.addContent( axisChartHandle );
	}

	/**
	 * Updates Axis chart in Xtab by replacing date item with axis chart.
	 * 
	 * @param cell
	 * @param bTransposed
	 * @param hostChartHandle
	 * @throws BirtException
	 */
	public static void updateAxisChart( AggregationCellHandle cell,
			boolean bTransposed, ExtendedItemHandle hostChartHandle )
			throws BirtException
	{
		int axisType = bTransposed ? ICrosstabConstants.ROW_AXIS_TYPE
				: ICrosstabConstants.COLUMN_AXIS_TYPE;
		if ( cell.getCrosstab( ).getGrandTotal( axisType ) != null )
		{
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
			Object content = ChartXTabUtil.getFirstContent( grandTotalAggCell );
			if ( content instanceof DataItemHandle )
			{
				( (DesignElementHandle) content ).dropAndClear( );

				// Create axis chart handle which references to host chart
				ExtendedItemHandle axisChartHandle = createChartHandle( cell.getModelHandle( ),
						ChartReportItemConstants.TYPE_AXIS_CHART,
						hostChartHandle );
				grandTotalAggCell.addContent( axisChartHandle );
			}
		}
	}

	/**
	 * Removes Axis chart in Xtab.
	 * 
	 * @param cell
	 * @param cmOld
	 * @throws BirtException
	 */
	public static void removeAxisChartInXTab( AggregationCellHandle cell,
			Chart cmOld ) throws BirtException
	{
		CrosstabReportItemHandle xtab = cell.getCrosstab( );
		if ( cmOld instanceof ChartWithAxes )
		{
			if ( ( (ChartWithAxes) cmOld ).isTransposed( ) )
			{
				cell.setSpanOverOnRow( null );
				CrosstabCellHandle grandTotalCell = xtab.getGrandTotal( ICrosstabConstants.ROW_AXIS_TYPE );
				if ( grandTotalCell != null
						&& grandTotalCell.getContents( ).size( ) <= 1 )
				{
					xtab.removeGrandTotal( ICrosstabConstants.ROW_AXIS_TYPE );
				}
			}
			else
			{
				cell.setSpanOverOnColumn( null );
				CrosstabCellHandle grandTotalCell = xtab.getGrandTotal( ICrosstabConstants.COLUMN_AXIS_TYPE );
				if ( grandTotalCell != null
						&& grandTotalCell.getContents( ).size( ) <= 1 )
				{
					xtab.removeGrandTotal( ICrosstabConstants.COLUMN_AXIS_TYPE );
				}
			}
		}
	}

	/**
	 * Updates XTab for Axis chart sync
	 * 
	 * @param cell
	 * @param hostChartHandle
	 * @param cmOld
	 * @param cmNew
	 * @throws BirtException
	 */
	public static void updateXTabForAxis( AggregationCellHandle cell,
			ExtendedItemHandle hostChartHandle, Chart cmOld, Chart cmNew )
			throws BirtException
	{
		if ( cmOld instanceof ChartWithoutAxes )
		{
			if ( cmNew instanceof ChartWithAxes )
			{
				addAxisChartInXTab( cell,
						( (ChartWithAxes) cmNew ).isTransposed( ),
						hostChartHandle );
			}
		}
		else if ( cmOld instanceof ChartWithAxes )
		{
			if ( cmNew instanceof ChartWithoutAxes )
			{
				removeAxisChartInXTab( cell, cmOld );
			}
			else
			{
				boolean bTransOld = ( (ChartWithAxes) cmOld ).isTransposed( );
				boolean bTransNew = ( (ChartWithAxes) cmNew ).isTransposed( );
				if ( bTransOld != bTransNew )
				{
					removeAxisChartInXTab( cell, cmOld );
					addAxisChartInXTab( cell, bTransNew, hostChartHandle );
				}
			}
		}
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
			hostChartHandle.setProperty( ChartReportItemConstants.PROPERTY_HOST_CHART,
					chartHandle );
		}
		return chartHandle;
	}

}
