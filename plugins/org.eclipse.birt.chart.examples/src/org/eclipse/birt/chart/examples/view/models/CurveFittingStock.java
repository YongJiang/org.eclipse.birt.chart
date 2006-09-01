/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.models;

import org.eclipse.birt.chart.datafeed.StockEntry;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

public class CurveFittingStock
{

	public final static Chart createCurveFittingStock( )
	{
		ChartWithAxes cwaStock = ChartWithAxesImpl.create( );

		// Title
		cwaStock.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( "Curve Fitting Stock Chart" );//$NON-NLS-1$

		// Plot
		cwaStock.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		cwaStock.getPlot( )
				.getClientArea( )
				.setBackground( ColorDefinitionImpl.create( 255, 255, 225 ) );

		// Legend
		cwaStock.getLegend( ).setVisible( false );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaStock ).getPrimaryBaseAxes( )[0];

		xAxisPrimary.setLabelPosition( Position.BELOW_LITERAL );
		xAxisPrimary.setType( AxisType.DATE_TIME_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
		xAxisPrimary.setCategoryAxis( true );
		xAxisPrimary.setFormatSpecifier( JavaDateFormatSpecifierImpl.create( "MM/dd/yyyy" ) );//$NON-NLS-1$

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaStock ).getPrimaryOrthogonalAxis( xAxisPrimary );

		yAxisPrimary.getScale( ).setMin( NumberDataElementImpl.create( 25.0 ) );
		yAxisPrimary.getScale( ).setMax( NumberDataElementImpl.create( 27.5 ) );
		yAxisPrimary.getScale( ).setStep( 0.5 );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );

		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );

		// Data Set
		DateTimeDataSet dsDateValues = DateTimeDataSetImpl.create( new Calendar[]{
				new CDateTime( 2004, 12, 27 ),
				new CDateTime( 2004, 12, 23 ),
				new CDateTime( 2004, 12, 22 ),
				new CDateTime( 2004, 12, 21 ),
				new CDateTime( 2004, 12, 20 ),
				new CDateTime( 2004, 12, 17 ),
				new CDateTime( 2004, 12, 16 ),
				new CDateTime( 2004, 12, 15 )
		} );

		StockDataSet dsStockValues = StockDataSetImpl.create( new StockEntry[]{
				new StockEntry( 27.01, 27.10, 26.82, 26.85 ),
				new StockEntry( 26.87, 27.15, 26.83, 27.01 ),
				new StockEntry( 26.84, 27.15, 26.78, 26.97 ),
				new StockEntry( 27.00, 27.17, 26.94, 27.07 ),
				new StockEntry( 27.01, 27.15, 26.89, 26.95 ),
				new StockEntry( 27.00, 27.32, 26.80, 26.96 ),
				new StockEntry( 27.15, 27.28, 27.01, 27.16 ),
				new StockEntry( 27.22, 27.40, 27.07, 27.11 ),
		} );

		// X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( dsDateValues );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seBase );

		// Y-Series
		StockSeries ss = (StockSeries) StockSeriesImpl.create( );
		ss.getLineAttributes( ).setColor( ColorDefinitionImpl.RED( ) );
		ss.setDataSet( dsStockValues );
		ss.setTranslucent( true );
		ss.setCurveFitting( CurveFittingImpl.create( ) );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).update( -1 );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( ss );

		return cwaStock;
	}
}
