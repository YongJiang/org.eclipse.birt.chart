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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.composites.YOptionalGroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.ModifyEvent;


/**
 * The component is used to set value series grouping. 
 * 
 * @since 2.3
 */
public class YOptionalDataDefinitionComponent extends BaseDataDefinitionComponent
{

	/**
	 * @param queryType
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param title
	 */
	public YOptionalDataDefinitionComponent( String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String title )
	{
		super( queryType, seriesdefinition, query, context, title );
	}

	/**
	 * @param style
	 * @param queryType
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param title
	 */
	public YOptionalDataDefinitionComponent( int style, String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String title )
	{
		super( style, queryType, seriesdefinition, query, context, title );
	}

	/**
	 * Create instance of <code>GroupSortingDialog</code> for Y
	 * series.
	 * 
	 * @param sdBackup
	 * @return
	 */
	protected GroupSortingDialog createGroupSortingDialog(
			SeriesDefinition sdBackup )
	{
		return new YOptionalGroupSortingDialog( cmpTop.getShell( ),
				context,
				sdBackup,
				false,
				false );
	}
	
	protected void handleGroupAction( )
	{
		SeriesDefinition sdBackup = (SeriesDefinition) EcoreUtil.copy( seriesdefinition );
		GroupSortingDialog groupDialog = createGroupSortingDialog( sdBackup );

		if ( groupDialog.open( ) == Window.OK )
		{
			if ( !sdBackup.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
			{
				seriesdefinition.eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			}
			else
			{
				seriesdefinition.setSorting( sdBackup.getSorting( ) );
			}
			
			// Update the query sorting of other series.
			ChartAdapter.beginIgnoreNotifications( );
			List<?> sds = ChartUIUtil.getAllOrthogonalSeriesDefinitions( context.getModel( ) );
			for ( int i = 0; i < sds.size( ); i++ )
			{
				if ( i != 0 )
				{
					// Except for the first, which should be
					// changed manually.
					SeriesDefinition sdf = (SeriesDefinition) sds.get( i );
					if ( !sdBackup.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
					{
						sdf.eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
					}
					else
					{
						sdf.setSorting( sdBackup.getSorting( ) );
					}
				}
			}
			ChartAdapter.endIgnoreNotifications( );

			seriesdefinition.setSortKey( sdBackup.getSortKey( ) );
			seriesdefinition.getSortKey( )
					.eAdapters( )
					.addAll( seriesdefinition.eAdapters( ) );

			if ( seriesdefinition.getQuery( ) != null )
			{
				if ( sdBackup.getQuery( ).getGrouping( ) == null )
				{
					// If it is cube set case, the grouping should be null.
					return;
				}
				
				seriesdefinition.getQuery( )
						.setGrouping( sdBackup.getQuery( ).getGrouping( ) );
				seriesdefinition.getQuery( )
						.getGrouping( )
						.eAdapters( )
						.addAll( seriesdefinition.getQuery( ).eAdapters( ) );

				// Update the query grouping of other series.
				ChartAdapter.beginIgnoreNotifications( );
				for ( int i = 0; i < sds.size( ); i++ )
				{
					if ( i != 0 )
					{
						SeriesDefinition sdf = (SeriesDefinition) sds.get( i );
						sdf.getQuery( )
								.setGrouping( (SeriesGrouping) EcoreUtil.copy( seriesdefinition.getQuery( )
										.getGrouping( ) ) );
						sdf.getQuery( )
								.getGrouping( )
								.eAdapters( )
								.addAll( sdf.getQuery( ).eAdapters( ) );
					}
				}
				ChartAdapter.endIgnoreNotifications( );

				ChartUIUtil.checkGroupType( context, context.getModel( ) );
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent#saveQuery()
	 */
	protected void saveQuery()
	{
		super.saveQuery( );
		updateBaseSeriesSortKey();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		super.modifyText( e );
		if ( isQueryModified && e.getSource( ) == txtDefinition )
		{
			updateBaseSeriesSortKey( );
		}
	}
	
	/**
	 * If Y grouping is set, the SortKey of base series only allow base series expression.
	 */
	private void updateBaseSeriesSortKey( )
	{
		String yGrouping = query.getDefinition( );
		if ( yGrouping != null && !"".equals( yGrouping ) ) //$NON-NLS-1$
		{
			// If Y grouping is set and if the sort key of base series is set,
			// we must ensure the sort key is base series expression, don't
			// allow other expression.
			Chart cm = context.getModel( );
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
				baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( )
						.get( 0 );
			}

			if ( baseSD.isSetSorting( ) )
			{
				Series s = (Series) baseSD.getSeries( ).get( 0 );
				String baseExpr = ( (Query) s.getDataDefinition( ).get( 0 ) ).getDefinition( );
				if ( baseSD.getSortKey( ) == null )
				{
					Query q = QueryImpl.create( baseExpr );
					baseSD.setSortKey( q );
					q.eAdapters( ).addAll( baseSD.eAdapters( ) );
				}
				else
				{
					baseSD.getSortKey( ).setDefinition( baseExpr );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent
	 * #updateQuery(java.lang.String)
	 */
	@Override
	public void updateQuery( String expression )
	{
		super.updateQuery( expression );
		
		ChartUIUtil.setAllGroupingQueryExceptFirst( context.getModel( ),
				expression );
	}

}
