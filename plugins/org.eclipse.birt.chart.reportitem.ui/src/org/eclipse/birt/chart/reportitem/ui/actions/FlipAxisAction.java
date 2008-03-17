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

package org.eclipse.birt.chart.reportitem.ui.actions;

import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class FlipAxisAction extends Action
{

	private ExtendedItemHandle eih;

	public FlipAxisAction( ExtendedItemHandle eih )
	{
		super( Messages.getString( "FlipAxisAction.Text.FlipAxis" ),//$NON-NLS-1$
				Action.AS_CHECK_BOX );
		this.eih = eih;
		this.setChecked( ChartXTabUIUtil.isTransposedChartWithAxes( ChartXTabUtil.getChartFromHandle( eih ) ) );
		this.setEnabled( checkEnabled( ) );
	}

	private boolean checkEnabled( )
	{
		try
		{
			if ( ChartXTabUtil.isAxisChart( eih ) )
			{
				// Get plot chart if it's axis chart
				eih = (ExtendedItemHandle) eih.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
			}
			if ( ChartXTabUtil.isPlotChart( eih ) )
			{
				AggregationCellHandle containerCell = ChartXTabUtil.getXtabContainerCell( eih );
				if ( containerCell != null )
				{
					List<String> exprs = ChartXTabUtil.getAllLevelsBindingExpression( containerCell.getCrosstab( ) );
					// Grand total always supports only one direction
					return exprs.size( ) == 2
							&& !ChartXTabUtil.isAggregationCell( containerCell );
				}
			}
		}
		catch ( BirtException e )
		{
			ChartWizard.displayException( e );
		}
		return false;
	}

	@Override
	public void run( )
	{
		try
		{
			AggregationCellHandle containerCell = ChartXTabUtil.getXtabContainerCell( eih );
			if ( containerCell != null )
			{
				ChartReportItemImpl reportItem = (ChartReportItemImpl) eih.getReportItem( );
				ChartWithAxes cmOld = (ChartWithAxes) reportItem.getProperty( ChartReportItemConstants.PROPERTY_CHART );
				ChartWithAxes cmNew = (ChartWithAxes) EcoreUtil.copy( cmOld );
				List<String> exprs = ChartXTabUtil.getAllLevelsBindingExpression( containerCell.getCrosstab( ) );
				Query query = (Query) ( (SeriesDefinition) ( (Axis) cmNew.getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getDesignTimeSeries( )
						.getDataDefinition( )
						.get( 0 );
				eih.getRoot( ).getCommandStack( ).startTrans( getText( ) );
				if ( cmNew.isTransposed( ) )
				{
					cmNew.setTransposed( false );
					query.setDefinition( exprs.get( 0 ) );
					ChartXTabUIUtil.updateXTabForAxis( containerCell,
							eih,
							true,
							cmNew );
				}
				else
				{
					cmNew.setTransposed( true );
					query.setDefinition( exprs.get( 1 ) );
					ChartXTabUIUtil.updateXTabForAxis( containerCell,
							eih,
							false,
							cmNew );
				}
				reportItem.executeSetModelCommand( eih, cmOld, cmNew );
			}
			eih.getRoot( ).getCommandStack( ).commit( );
		}
		catch ( BirtException e )
		{
			ChartWizard.displayException( e );
			eih.getRoot( ).getCommandStack( ).rollback( );
		}
	}
}
