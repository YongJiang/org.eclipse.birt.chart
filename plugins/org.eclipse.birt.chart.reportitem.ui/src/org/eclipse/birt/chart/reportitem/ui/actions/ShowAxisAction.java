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

import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.reportitem.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class ShowAxisAction extends Action
{

	private ExtendedItemHandle eih;

	public ShowAxisAction( ExtendedItemHandle eih )
	{
		super( Messages.getString( "ShowAxisAction.Text.ShowValueAxis" ),//$NON-NLS-1$
				Action.AS_CHECK_BOX );
		this.eih = eih;
		init( );
	}

	private void init( )
	{
		Chart cm = ChartXTabUtil.getChartFromHandle( eih );
		if ( cm instanceof ChartWithAxes )
		{
			this.setChecked( hasAxisChart( ) );
		}
		else
		{
			this.setEnabled( false );
		}
	}

	private boolean hasAxisChart( )
	{
		if ( ChartXTabUtil.isPlotChart( eih ) )
		{
			for ( Iterator iterator = eih.clientsIterator( ); iterator.hasNext( ); )
			{
				DesignElementHandle client = (DesignElementHandle) iterator.next( );
				if ( ChartXTabUtil.isAxisChart( client ) )
				{
					return true;
				}
			}
			return false;
		}
		if ( ChartXTabUtil.isAxisChart( eih ) )
		{
			return true;
		}
		return false;
	}

	@Override
	public void run( )
	{
		ModuleHandle mh = eih.getRoot( );
		try
		{
			mh.getCommandStack( ).startTrans( getText( ) );

			// Update chart model for axis visibility
			ExtendedItemHandle plotChart = eih;
			if ( ChartXTabUtil.isAxisChart( eih ) )
			{
				plotChart = (ExtendedItemHandle) eih.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
			}
			ChartReportItemImpl reportItem = (ChartReportItemImpl) plotChart.getReportItem( );
			ChartWithAxes cmOld = (ChartWithAxes) reportItem.getProperty( ChartReportItemConstants.PROPERTY_CHART );
			ChartWithAxes cmNew = (ChartWithAxes) EcoreUtil.copy( cmOld );
			Axis yAxis = (Axis) ( (Axis) cmNew.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 );
			if ( yAxis != null )
			{
				yAxis.getLineAttributes( ).setVisible( isChecked( ) );
				yAxis.getLabel( ).setVisible( isChecked( ) );
				yAxis.getMajorGrid( )
						.getTickAttributes( )
						.setVisible( isChecked( ) );
				reportItem.executeSetModelCommand( plotChart, cmOld, cmNew );
			}

			// Update axis chart in xtab
			AggregationCellHandle containerCell = ChartXTabUtil.getXtabContainerCell( eih );
			if ( containerCell != null )
			{
				if ( isChecked( ) )
				{
					// Add axis chart
					ChartXTabUIUtil.addAxisChartInXTab( containerCell,
							cmNew,
							eih,
							false );
				}
				else
				{
					// Delete axis chart
					ChartXTabUIUtil.removeAxisChartInXTab( containerCell,
							ChartXTabUIUtil.isTransposedChartWithAxes( cmNew ) );
				}
			}

			mh.getCommandStack( ).commit( );
		}
		catch ( BirtException e )
		{
			ChartWizard.displayException( e );
			mh.getCommandStack( ).rollback( );
		}
	}
}