/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;

/**
 * Presentation implementation for Chart Plot in Cross tab
 */
public final class ChartReportItemPresentationPlotImpl extends
		ChartReportItemPresentationBase
{

	protected Bounds computeBounds( ) throws ChartException
	{
		final Bounds originalBounds = cm.getBlock( ).getBounds( );

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container
		Bounds bounds = originalBounds.copyInstance( );

		try
		{
			AggregationCellHandle xtabCell = ChartCubeUtil.getXtabContainerCell( modelHandle );
			if ( xtabCell != null )
			{
				if ( xtabCell.getSpanOverOnColumn( ) != null )
				{
					// Horizontal direction
					// Get the column width plus border
					double dWidth = getColumnCellWidth( xtabCell.getCrosstab( ),
							dpi );
					if ( ChartUtil.mathEqual( dWidth, 0 ) )
					{
						dWidth = ChartCubeUtil.DEFAULT_COLUMN_WIDTH.getMeasure( );
					}
					StyleHandle style = xtabCell.getModelHandle( )
							.getPrivateStyle( );
					double dLeftBorder = ChartItemUtil.convertToPoints( style.getBorderLeftWidth( ),
							dpi );
					double dRightBorder = ChartItemUtil.convertToPoints( style.getBorderRightWidth( ),
							dpi );
					// Set negative size to be replaced by actual size
					// In IE, cell size doesn't include padding, but FF and PDF
					// includes padding. To avoid this computation conflict, set
					// 0 padding in design time.
					bounds.setWidth( -roundPointsWithPixels( dWidth
							+ ( dLeftBorder + dRightBorder )
							/ 2 ) );

					// If user specifies row cell height manually, set the
					// height to chart model
					double dHeight = getRowCellHeight( xtabCell.getCrosstab( ),
							dpi );
					if ( !ChartUtil.mathEqual( dHeight, 0 )
							&& !ChartUtil.mathEqual( dHeight,
									ChartCubeUtil.DEFAULT_ROW_HEIGHT.getMeasure( ) ) )
					{
						bounds.setHeight( dHeight );
					}
				}
				else if ( xtabCell.getSpanOverOnRow( ) != null )
				{
					// Vertical direction plus border
					double dHeight = getRowCellHeight( xtabCell.getCrosstab( ),
							dpi );
					if ( ChartUtil.mathEqual( dHeight, 0 ) )
					{
						dHeight = ChartCubeUtil.DEFAULT_ROW_HEIGHT.getMeasure( );
					}
					StyleHandle style = xtabCell.getModelHandle( )
							.getPrivateStyle( );
					double dTopBorder = ChartItemUtil.convertToPoints( style.getBorderTopWidth( ),
							dpi );
					double dBottomBorder = ChartItemUtil.convertToPoints( style.getBorderBottomWidth( ),
							dpi );
					// Set negative size to be replaced by actual size
					// In IE, cell size doesn't include padding, but FF and PDF
					// includes padding. To avoid this computation conflict, set
					// 0 padding in design time.
					bounds.setHeight( -roundPointsWithPixels( dHeight
							+ ( dTopBorder + dBottomBorder )
							/ 2 ) );

					// If user specifies column cell width manually, set the
					// width to chart model
					double dWidth = getColumnCellWidth( xtabCell.getCrosstab( ),
							dpi );
					if ( !ChartUtil.mathEqual( dWidth, 0 )
							&& !ChartUtil.mathEqual( dWidth,
									ChartCubeUtil.DEFAULT_COLUMN_WIDTH.getMeasure( ) ) )
					{
						bounds.setWidth( dWidth );
					}
				}
			}
		}
		catch ( BirtException e )
		{
			throw new ChartException( ChartReportItemConstants.ID,
					ChartException.GENERATION,
					e );
		}

		return bounds;
	}

	protected void updateChartModel( )
	{
		super.updateChartModel( );

		// Update runtime model to render plot only
		ChartCubeUtil.updateModelToRenderPlot( cm, rtc.isRightToLeft( ) );
	}

	private double roundPointsWithPixels( double points )
	{
		// Bugzilla#247924: Since each cell size in x table is rendered with
		// pixels rounding, chart should round base size with pixels first so
		// that chart could align with each cell.
		return ( (int) ( points / 72 * dpi ) ) * 72 / (double) dpi;
	}

	static double getColumnCellWidth( CrosstabReportItemHandle xtabHandle,
			int dpi ) throws BirtException
	{
		CrosstabCellHandle columnCell = ChartCubeUtil.getInnermostLevelCell( xtabHandle,
				ICrosstabConstants.COLUMN_AXIS_TYPE );
		return ChartItemUtil.convertToPoints( xtabHandle.getColumnWidth( columnCell ),
				dpi );
	}

	static double getRowCellHeight( CrosstabReportItemHandle xtabHandle, int dpi )
			throws BirtException
	{
		CrosstabCellHandle rowCell = ChartCubeUtil.getInnermostLevelCell( xtabHandle,
				ICrosstabConstants.ROW_AXIS_TYPE );
		return ChartItemUtil.convertToPoints( xtabHandle.getRowHeight( rowCell ),
				dpi );
	}
}
