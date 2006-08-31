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

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

public class PercentageValueFormat
{

	public static final Chart createPercentageValueFormat( )
	{
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create( );

		// Plot
		cwoaPie.setSeriesThickness( 25 );
		cwoaPie.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );

		// Legend
		cwoaPie.getLegend( ).getText( ).getFont( ).setBold( true );

		// Title
		cwoaPie.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( "Pie Chart with Percentage Values" );//$NON-NLS-1$
		cwoaPie.getTitle( ).getOutline( ).setVisible( true );

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"New York", "Boston", "Chicago", "San Francisco", "Dallas"} );//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$

		double data[] = {
				54.65, 21, 75.95, 91.28, 37.43
		};

		double value = 0;
		for ( int i = 0; i < data.length; i++ )
		{
			value += data[i];
		}

		double values[] = new double[data.length];
		for ( int i = 0; i < data.length; i++ )
		{
			values[i] += ( data[i] / value ) * 100;
		}

		NumberDataSet seriesValues = NumberDataSetImpl.create( values );

		// Base Series
		Series seCategory = (Series) SeriesImpl.create( );
		seCategory.setDataSet( categoryValues );

		SeriesDefinition sd = SeriesDefinitionImpl.create( );
		cwoaPie.getSeriesDefinitions( ).add( sd );
		sd.getSeriesPalette( ).update( 0 );
		sd.getSeries( ).add( seCategory );

		// Orthogonal Series
		PieSeries sePie = (PieSeries) PieSeriesImpl.create( );
		sePie.setDataSet( seriesValues );
		sePie.setExplosion( 3 );

		SeriesDefinition sdCity = SeriesDefinitionImpl.create( );
		sd.getSeriesDefinitions( ).add( sdCity );
		sdCity.getSeries( ).add( sePie );

		DataPointComponent dpc = DataPointComponentImpl.create( DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create( "0'%'" ) );//$NON-NLS-1$
		sePie.getDataPoint( ).getComponents( ).clear( );
		sePie.getDataPoint( ).getComponents( ).add( dpc );

		return cwoaPie;
	}

}
