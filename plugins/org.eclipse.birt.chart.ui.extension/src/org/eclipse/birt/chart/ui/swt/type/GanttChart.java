/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. 
 * All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/


package org.eclipse.birt.chart.ui.swt.type;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.DefaultBaseSeriesComponent;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.graphics.Image;

public class GanttChart extends DefaultChartTypeImpl
{

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_GANTT;

	protected static final String STANDARD_SUBTYPE_LITERAL = "Standard Gantt Chart"; //$NON-NLS-1$

	private static final String sStandardDescription = Messages.getString( "GanttChart.Txt.Description" ); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image img2D = null;

	public GanttChart( )
	{
		imgIcon = UIHelper.getImage( "icons/obj16/ganttcharticon.gif" ); //$NON-NLS-1$
		super.chartTitle = Messages.getString( "GanttChart.Txt.DefaultGanttChartTitle" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getName()
	 */
	public String getName( )
	{
		return TYPE_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getImage()
	 */
	public Image getImage( )
	{
		return imgIcon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getHelp()
	 */
	public IHelpContent getHelp( )
	{
		return new HelpContentImpl( TYPE_LITERAL,
				Messages.getString( "GanttChart.Txt.HelpText" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String)
	 */
	public Collection<IChartSubType> getChartSubtypes( String sDimension,
			Orientation orientation )
	{
		Vector<IChartSubType> vSubTypes = new Vector<IChartSubType>( );
		if ( sDimension.equals( TWO_DIMENSION_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			if ( orientation.equals( Orientation.VERTICAL_LITERAL ) )
			{
				img2D = UIHelper.getImage( "icons/wizban/ganttchartimage.gif" ); //$NON-NLS-1$
			}
			else
			{
				img2D = UIHelper.getImage( "icons/wizban/horizontalganttchartimage.gif" ); //$NON-NLS-1$
			}
			vSubTypes.add( new DefaultChartSubTypeImpl( STANDARD_SUBTYPE_LITERAL,
					img2D,
					sStandardDescription,
					Messages.getString( "GanttChart.SubType.Standard" ) ) ); //$NON-NLS-1$
		}
		return vSubTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public Chart getModel( String sSubType, Orientation orientation,
			String sDimension, Chart currentChart )
	{
		ChartWithAxes newChart = null;
		if ( currentChart != null )
		{
			newChart = (ChartWithAxes) getConvertedChart( currentChart,
					sSubType,
					orientation,
					sDimension );
			if ( newChart != null )
			{
				return newChart;
			}
		}
		newChart = ChartWithAxesImpl.create( );
		newChart.setType( TYPE_LITERAL );
		newChart.setSubType( sSubType );
		newChart.setDimension( getDimensionFor( sDimension ) );
		newChart.setUnits( "Points" ); //$NON-NLS-1$
		newChart.setOrientation( orientation );

		Axis xAxis = newChart.getAxes( ).get( 0 );
		xAxis.setOrientation( Orientation.HORIZONTAL_LITERAL );
		xAxis.setType( AxisType.LINEAR_LITERAL );
		xAxis.setCategoryAxis( false );
		xAxis.getScale( ).setStep( 10.0 );
		xAxis.getLabel( ).setVisible( false );

		newChart.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( getDefaultTitle( ) );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		Series categorySeries = SeriesImpl.create( );
		sdX.getSeries( ).add( categorySeries );
		xAxis.getSeriesDefinitions( ).add( sdX );

		Axis yAxis = xAxis.getAssociatedAxes( ).get( 0 );
		yAxis.setOrientation( Orientation.VERTICAL_LITERAL );
		yAxis.setType( AxisType.DATE_TIME_LITERAL );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( 0 );
		Series valueSeries = GanttSeriesImpl.create( );
		valueSeries.setLabelPosition( Position.ABOVE_LITERAL );
		sdY.getSeries( ).add( valueSeries );
		yAxis.getSeriesDefinitions( ).add( sdY );

		addSampleData( newChart );
		return newChart;
	}

	private void addSampleData( Chart newChart )
	{
		SampleData sd = DataFactory.eINSTANCE.createSampleData( );
		sd.getBaseSampleData( ).clear( );
		sd.getOrthogonalSampleData( ).clear( );

		// Create Base Sample Data
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sdBase.setDataSetRepresentation( "5,15,25" ); //$NON-NLS-1$
		sd.getBaseSampleData( ).add( sdBase );

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		oSample.setDataSetRepresentation( "S01/01/2005 E02/01/2005 Label1,S01/15/2005 E02/15/2005 Label2,S02/01/2005 E03/01/2005 Label3" ); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( oSample );

		newChart.setSampleData( sd );
	}

	private Chart getConvertedChart( Chart currentChart, String sNewSubType,
			Orientation newOrientation, String sNewDimension )
	{
		Chart helperModel = currentChart.copyInstance( );
		helperModel.eAdapters( ).addAll( currentChart.eAdapters( ) );
		// Cache series to keep attributes during conversion
		ChartCacheManager.getInstance( )
				.cacheSeries( ChartUIUtil.getAllOrthogonalSeriesDefinitions( helperModel ) );
		IChartType oldType = ChartUIUtil.getChartType( currentChart.getType( ) );
		if ( ( currentChart instanceof ChartWithAxes ) ) // Chart is
		// ChartWithAxes
		{
			if ( currentChart.getType( ).equals( TYPE_LITERAL ) )
			{
				if ( !currentChart.getSubType( ).equals( sNewSubType ) )
				{
					currentChart.setSubType( sNewSubType );
					EList<Axis> axes = ( (ChartWithAxes) currentChart ).getAxes( )
							.get( 0 )
							.getAssociatedAxes( );
					for ( int i = 0; i < axes.size( ); i++ )
					{
						axes.get( i ).setPercent( false );
						EList<SeriesDefinition> seriesdefinitions = axes.get( i )
								.getSeriesDefinitions( );
						for ( int j = 0; j < seriesdefinitions.size( ); j++ )
						{
							Series series = seriesdefinitions.get( j )
									.getDesignTimeSeries( );
							series.setStacked( false );
						}
					}
				}

				if ( !( (ChartWithAxes) currentChart ).getOrientation( )
								.equals( newOrientation ) )
				{
					( (ChartWithAxes) currentChart ).setOrientation( newOrientation );
				}
				return currentChart;
			}
			else if ( currentChart.getType( ).equals( LineChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( AreaChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( BarChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( TubeChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( ConeChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( PyramidChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( ScatterChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( StockChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( BubbleChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( DifferenceChart.TYPE_LITERAL ) )
			{
				currentChart.setType( TYPE_LITERAL );

				( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ).setCategoryAxis( true );

				currentChart.setSubType( sNewSubType );
				Text title = currentChart.getTitle( ).getLabel( ).getCaption( );
				if ( title.getValue( ) == null
						|| title.getValue( ).trim( ).length( ) == 0
						|| title.getValue( )
								.trim( )
								.equals( oldType.getDefaultTitle( ).trim( ) ) )
				{
					title.setValue( getDefaultTitle( ) );
				}

				EList<Axis> axes = ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 )
						.getAssociatedAxes( );
				for ( int i = 0, seriesIndex = 0; i < axes.size( ); i++ )
				{
					axes.get( i ).setPercent( false );
					axes.get( i ).setType( AxisType.DATE_TIME_LITERAL );
					EList<SeriesDefinition> seriesdefinitions = axes.get( i )
							.getSeriesDefinitions( );
					for ( int j = 0; j < seriesdefinitions.size( ); j++ )
					{
						Series series = seriesdefinitions.get( j )
								.getDesignTimeSeries( );
						series = getConvertedSeries( series, seriesIndex++ );
						series.setStacked( false );
						seriesdefinitions.get( j ).getSeries( ).clear( );
						seriesdefinitions.get( j ).getSeries( ).add( series );
					}
				}

				currentChart.setSampleData( getConvertedSampleData( currentChart.getSampleData( ),
						( (ChartWithAxes) currentChart ).getAxes( )
								.get( 0 )
								.getType( ),
						AxisType.DATE_TIME_LITERAL ) );
			}
			else
			{
				return null;
			}
		}
		else
		{
			// Create a new instance of the correct type and set initial
			// properties
			currentChart = ChartWithAxesImpl.create( );
			currentChart.eAdapters( ).addAll( helperModel.eAdapters( ) );
			currentChart.setType( TYPE_LITERAL );
			currentChart.setSubType( sNewSubType );
			( (ChartWithAxes) currentChart ).setOrientation( newOrientation );
			currentChart.setDimension( getDimensionFor( sNewDimension ) );

			Axis xAxis = ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 );
			xAxis.setOrientation( Orientation.HORIZONTAL_LITERAL );
			xAxis.setType( AxisType.TEXT_LITERAL );
			xAxis.setCategoryAxis( true );

			Axis yAxis = xAxis.getAssociatedAxes( ).get( 0 );
			yAxis.setOrientation( Orientation.VERTICAL_LITERAL );
			yAxis.setType( AxisType.DATE_TIME_LITERAL );

			// Copy generic chart properties from the old chart
			currentChart.setBlock( helperModel.getBlock( ) );
			currentChart.setDescription( helperModel.getDescription( ) );
			currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );
			currentChart.setSampleData( getConvertedSampleData( helperModel.getSampleData( ),
					( (ChartWithAxes) currentChart ).getAxes( )
							.get( 0 )
							.getType( ),
					AxisType.DATE_TIME_LITERAL ) );
			currentChart.setScript( helperModel.getScript( ) );
			currentChart.setSeriesThickness( helperModel.getSeriesThickness( ) );
			currentChart.setUnits( helperModel.getUnits( ) );

			if ( helperModel.getType( ).equals( PieChart.TYPE_LITERAL )
					|| helperModel.getType( ).equals( MeterChart.TYPE_LITERAL ) )
			{
				// Clear existing series definitions
				xAxis.getSeriesDefinitions( ).clear( );

				// Copy base series definitions
				xAxis.getSeriesDefinitions( )
						.add( ( (ChartWithoutAxes) helperModel ).getSeriesDefinitions( )
								.get( 0 ) );

				// Clear existing series definitions
				yAxis.getSeriesDefinitions( ).clear( );

				// Copy orthogonal series definitions
				yAxis.getSeriesDefinitions( )
						.addAll( xAxis.getSeriesDefinitions( )
								.get( 0 )
								.getSeriesDefinitions( ) );

				// Update the base series
				Series series = xAxis.getSeriesDefinitions( )
						.get( 0 )
						.getDesignTimeSeries( );

				// Clear existing series
				xAxis.getSeriesDefinitions( ).get( 0 ).getSeries( ).clear( );

				// Add converted series
				xAxis.getSeriesDefinitions( )
						.get( 0 )
						.getSeries( )
						.add( series );

				// Update the orthogonal series
				EList<SeriesDefinition> seriesdefinitions = yAxis.getSeriesDefinitions( );
				for ( int j = 0; j < seriesdefinitions.size( ); j++ )
				{
					series = seriesdefinitions.get( j ).getDesignTimeSeries( );
					series = getConvertedSeries( series, j );
					series.getLabel( ).setVisible( false );
					series.setStacked( false );
					// Clear any existing series
					seriesdefinitions.get( j ).getSeries( ).clear( );
					// Add the new series
					seriesdefinitions.get( j ).getSeries( ).add( series );
				}
			}
			else
			{
				return null;
			}
			
			currentChart.getLegend( )
					.setItemType( LegendItemType.SERIES_LITERAL );
			
			Text title = currentChart.getTitle( ).getLabel( ).getCaption( );
			if ( title.getValue( ) == null
					|| title.getValue( ).trim( ).length( ) == 0
					|| title.getValue( )
							.trim( )
							.equals( oldType.getDefaultTitle( ).trim( ) ) )
			{
				title.setValue( getDefaultTitle( ) );
			}
		}
		if ( !( (ChartWithAxes) currentChart ).getOrientation( )
						.equals( newOrientation ) )
		{
			( (ChartWithAxes) currentChart ).setOrientation( newOrientation );
		}
		if ( !currentChart.getDimension( )
				.equals( getDimensionFor( sNewDimension ) ) )
		{
			currentChart.setDimension( getDimensionFor( sNewDimension ) );
		}

		ChartUIUtil.updateDefaultAggregations( currentChart );

		return currentChart;
	}

	private Series getConvertedSeries( Series series, int seriesIndex )
	{
		// Do not convert base series
		if ( series.getClass( )
				.getName( )
				.equals( "org.eclipse.birt.chart.model.component.impl.SeriesImpl" ) ) //$NON-NLS-1$
		{
			return series;
		}
		GanttSeries ganttseries = (GanttSeries) ChartCacheManager.getInstance( )
				.findSeries( GanttSeriesImpl.class.getName( ), seriesIndex );

		if ( ganttseries == null )
		{
			ganttseries = (GanttSeries) GanttSeriesImpl.create( );
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes( series, ganttseries );

		return ganttseries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions()
	 */
	public String[] getSupportedDimensions( )
	{
		return new String[]{
			TWO_DIMENSION_TYPE
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	public String getDefaultDimension( )
	{
		return TWO_DIMENSION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition()
	 */
	public boolean supportsTransposition( )
	{
		return false;
	}

	private ChartDimension getDimensionFor( String sDimension )
	{
		// Other types are not supported.
		return ChartDimension.TWO_DIMENSIONAL_LITERAL;
	}

	public Orientation getDefaultOrientation( )
	{
		return Orientation.HORIZONTAL_LITERAL;
	}

	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle )
	{
		return new DefaultBaseSeriesComponent( ChartUIUtil.getBaseSeriesDefinitions( chart )
				.get( 0 ),
				context,
				sTitle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "GanttChart.Txt.DisplayName" ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSeries()
	 */
	public Series getSeries( )
	{
		return GanttSeriesImpl.create( );
	}
}