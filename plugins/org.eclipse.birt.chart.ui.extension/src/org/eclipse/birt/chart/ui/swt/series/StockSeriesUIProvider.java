/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.composites.StockSeriesDataDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 * 
 */
public class StockSeriesUIProvider extends DefaultSeriesUIProvider
{

	private static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.StockSeriesImpl"; //$NON-NLS-1$

	/**
	 * 
	 */
	public StockSeriesUIProvider( )
	{
		super( );
	}

	public Composite getSeriesAttributeSheet( Composite parent, Series series,
			IUIServiceProvider builder, Object oContext )
	{
		return new StockSeriesAttributeComposite( parent, SWT.NONE, series );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesDataSheet(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getSeriesDataSheet( Composite parent,
			SeriesDefinition seriesdefinition, IUIServiceProvider builder,
			Object oContext )
	{
		return new StockSeriesDataDefinitionComposite( parent,
				SWT.NONE,
				seriesdefinition,
				builder,
				oContext );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesClass()
	 */
	public String getSeriesClass( )
	{
		return SERIES_CLASS;
	}

	public ISelectDataComponent getSeriesDataComponent( int seriesType,
			SeriesDefinition seriesDefn, IUIServiceProvider builder,
			Object oContext, String sTitle )
	{
		if ( seriesType == ISelectDataCustomizeUI.ORTHOGONAL_SERIES )
		{
			return new StockDataDefinitionComponent( seriesDefn,
					builder,
					oContext,
					sTitle );
		}
		else if ( seriesType == ISelectDataCustomizeUI.GROUPING_SERIES )
		{
			BaseDataDefinitionComponent ddc = new BaseDataDefinitionComponent( seriesDefn,
					seriesDefn.getQuery( ),
					builder,
					oContext,
					sTitle );
			ddc.setFormatSpecifierEnabled( false );
			return ddc;
		}
		return new DefaultSelectDataComponent( );
	}
}