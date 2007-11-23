/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class BubbleDataDefinitionComponent extends DefaultSelectDataComponent
{

	public static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl"; //$NON-NLS-1$

	private transient ISelectDataComponent[] dataComArray;

	private transient Composite cmpSeries = null;

	private transient SeriesDefinition seriesDefn = null;

	private transient String sTitle = null;

	private transient ChartWizardContext context = null;

	public BubbleDataDefinitionComponent( SeriesDefinition seriesDefn,
			ChartWizardContext context, String sTitle )
	{
		super( );
		this.seriesDefn = seriesDefn;
		this.context = context;
		this.sTitle = sTitle;
		init( );
	}

	private void init( )
	{
		dataComArray = new ISelectDataComponent[2];

		// Value
		dataComArray[0] = new BaseDataDefinitionComponent( ChartUIConstants.QUERY_VALUE,
				seriesDefn,
				ChartUIUtil.getDataQuery( seriesDefn, 0 ),
				context,
				sTitle );
		// Size
		dataComArray[1] = new BaseDataDefinitionComponent( BaseDataDefinitionComponent.BUTTON_AGGREGATION,
				ChartUIConstants.QUERY_VALUE,
				seriesDefn,
				ChartUIUtil.getDataQuery( seriesDefn, 1 ),
				context,
				sTitle );
	}

	public Composite createArea( Composite parent )
	{
		cmpSeries = new Composite( parent, SWT.NONE );
		{
			GridData gridData = new GridData( GridData.FILL_BOTH );
			cmpSeries.setLayoutData( gridData );

			GridLayout gridLayout = new GridLayout( 1, false );
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			cmpSeries.setLayout( gridLayout );
		}

		for ( int i = 0; i < dataComArray.length; i++ )
		{
			( (BaseDataDefinitionComponent) dataComArray[i] ).setDescription( ChartUIUtil.getBubbleTitle( i ) );
			Composite cmpData = dataComArray[i].createArea( cmpSeries );
			cmpData.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}
		return cmpSeries;
	}

	public void selectArea( boolean selected, Object data )
	{
		if ( data instanceof Integer )
		{
			int queryIndex = ( (Integer) data ).intValue( );
			dataComArray[queryIndex].selectArea( selected, data );
		}
		else if ( data instanceof Object[] )
		{
			Object[] array = (Object[]) data;
			SeriesDefinition seriesdefinition = (SeriesDefinition) array[0];
			for ( int i = 0; i < dataComArray.length; i++ )
			{
				dataComArray[i].selectArea( selected, new Object[]{
						seriesdefinition,
						ChartUIUtil.getDataQuery( seriesdefinition, i )
				} );
			}
		}
		else
		{
			for ( int i = 0; i < dataComArray.length; i++ )
			{
				dataComArray[i].selectArea( selected, null );
			}
		}
	}

	public void dispose( )
	{
		for ( int i = 0; i < dataComArray.length; i++ )
		{
			dataComArray[i].dispose( );
		}
		super.dispose( );
	}

}
