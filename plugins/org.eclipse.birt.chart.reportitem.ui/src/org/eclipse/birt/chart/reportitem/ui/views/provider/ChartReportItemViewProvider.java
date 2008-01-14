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

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemViewAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * ChartReportItemViewProvider
 * @since BIRT 2.3
 */
public class ChartReportItemViewProvider extends ReportItemViewAdapter
{
	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem.ui" ); //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.extensions.ReportItemViewAdapter#createView(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public DesignElementHandle createView( DesignElementHandle host ) throws BirtException
	{
		if ( host instanceof TableHandle )
		{
			// Create chart
			ChartWithAxes cm = ChartWithAxesImpl.create( );
			cm.setType( "Bar Chart" );//$NON-NLS-1$
			cm.setSubType( "Side-by-side" );//$NON-NLS-1$
			cm.setUnits( "Points" ); //$NON-NLS-1$
			cm.setUnitSpacing( 50 );

			// Add base series
			SeriesDefinition sdBase = SeriesDefinitionImpl.create( );
			sdBase.getSeriesPalette( ).shift( 0 );
			Series series = SeriesImpl.create( );
			sdBase.getSeries( ).add( series );
			cm.getBaseAxes( )[0].getSeriesDefinitions( ).add( sdBase );

			// Add orthogonal series
			SeriesDefinition sdOrth = SeriesDefinitionImpl.create( );
			sdOrth.getSeriesPalette( ).shift( 0 );
			series = BarSeriesImpl.create( );
			sdOrth.getSeries( ).add( series );
			cm.getOrthogonalAxes( cm.getBaseAxes( )[0], true )[0].getSeriesDefinitions( )
					.add( sdOrth );

			// Add sample data
			SampleData sampleData = DataFactory.eINSTANCE.createSampleData( );
			sampleData.getBaseSampleData( ).clear( );
			sampleData.getOrthogonalSampleData( ).clear( );
			// Create Base Sample Data
			BaseSampleData sampleDataBase = DataFactory.eINSTANCE.createBaseSampleData( );
			sampleDataBase.setDataSetRepresentation( "A, B, C" ); //$NON-NLS-1$
			sampleData.getBaseSampleData( ).add( sampleDataBase );
			// Create Orthogonal Sample Data (with simulation count of 2)
			OrthogonalSampleData sampleDataOrth = DataFactory.eINSTANCE.createOrthogonalSampleData( );
			sampleDataOrth.setDataSetRepresentation( "5,4,12" ); //$NON-NLS-1$
			sampleDataOrth.setSeriesDefinitionIndex( 0 );
			sampleData.getOrthogonalSampleData( ).add( sampleDataOrth );
			cm.setSampleData( sampleData );

			// Create a new item handle.
			ExtendedItemHandle itemHandle = host.getElementFactory( )
					.newExtendedItem( null, getViewName( ) );

			itemHandle.getReportItem( ).setProperty( ChartReportItemUtil.PROPERTY_CHART, cm );
			
			return itemHandle;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider#getViewName()
	 */
	public String getViewName( )
	{
		return "Chart"; //$NON-NLS-1$
	}

}
