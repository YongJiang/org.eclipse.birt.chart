/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.DefaultBaseSeriesComponent;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class ConeChart extends DefaultChartTypeImpl
{

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = "Cone Chart"; //$NON-NLS-1$

	private static final String STACKED_SUBTYPE_LITERAL = "Stacked"; //$NON-NLS-1$

	private static final String PERCENTSTACKED_SUBTYPE_LITERAL = "Percent Stacked"; //$NON-NLS-1$

	private static final String SIDE_SUBTYPE_LITERAL = "Side-by-side"; //$NON-NLS-1$

	public static final String CHART_TITLE = Messages.getString( "ConeChart.Txt.DefaultConeChartTitle" ); //$NON-NLS-1$

	private static final String sStackedDescription = Messages.getString( "ConeChart.Txt.StackedDescription" ); //$NON-NLS-1$

	private static final String sPercentStackedDescription = Messages.getString( "ConeChart.Txt.PercentStackedDescription" ); //$NON-NLS-1$

	private static final String sSideBySideDescription = Messages.getString( "ConeChart.Txt.SideBySideDescription" ); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image imgStacked = null;

	private transient Image imgStackedWithDepth = null;

	private transient Image imgPercentStacked = null;

	private transient Image imgPercentStackedWithDepth = null;

	private transient Image imgSideBySide = null;

	private transient Image imgSideBySideWithDepth = null;

	private transient Image imgSideBySide3D = null;

	private static final String[] saDimensions = new String[]{
			TWO_DIMENSION_TYPE,
			TWO_DIMENSION_WITH_DEPTH_TYPE,
			THREE_DIMENSION_TYPE
	};

	public ConeChart( )
	{
		imgIcon = UIHelper.getImage( "icons/obj16/conecharticon.gif" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
	 */
	public String getName( )
	{
		return TYPE_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
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
				Messages.getString( "ConeChart.Txt.HelpText" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String)
	 */
	public Collection getChartSubtypes( String sDimension,
			Orientation orientation )
	{
		Vector vSubTypes = new Vector( );
		if ( sDimension.equals( TWO_DIMENSION_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			if ( orientation.equals( Orientation.VERTICAL_LITERAL ) )
			{
				imgStacked = UIHelper.getImage( "icons/wizban/stackedconechartimage.gif" ); //$NON-NLS-1$
				imgPercentStacked = UIHelper.getImage( "icons/wizban/percentstackedconechartimage.gif" ); //$NON-NLS-1$
				imgSideBySide = UIHelper.getImage( "icons/wizban/sidebysideconechartimage.gif" ); //$NON-NLS-1$
			}
			else
			{
				imgStacked = UIHelper.getImage( "icons/wizban/horizontalstackedconechartimage.gif" ); //$NON-NLS-1$
				imgPercentStacked = UIHelper.getImage( "icons/wizban/horizontalpercentstackedconechartimage.gif" ); //$NON-NLS-1$
				imgSideBySide = UIHelper.getImage( "icons/wizban/horizontalsidebysideconechartimage.gif" ); //$NON-NLS-1$
			}

			vSubTypes.add( new DefaultChartSubTypeImpl( SIDE_SUBTYPE_LITERAL,
					imgSideBySide,
					sSideBySideDescription,
					Messages.getString( "ConeChart.SubType.Side" ) ) ); //$NON-NLS-1$
			vSubTypes.add( new DefaultChartSubTypeImpl( STACKED_SUBTYPE_LITERAL,
					imgStacked,
					sStackedDescription,
					Messages.getString( "ConeChart.SubType.Stacked" ) ) ); //$NON-NLS-1$
			vSubTypes.add( new DefaultChartSubTypeImpl( PERCENTSTACKED_SUBTYPE_LITERAL,
					imgPercentStacked,
					sPercentStackedDescription,
					Messages.getString( "ConeChart.SubType.PercentStacked" ) ) ); //$NON-NLS-1$
		}
		else if ( sDimension.equals( TWO_DIMENSION_WITH_DEPTH_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName( ) ) )
		{
			if ( orientation.equals( Orientation.VERTICAL_LITERAL ) )
			{
				imgStackedWithDepth = UIHelper.getImage( "icons/wizban/stackedconechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgPercentStackedWithDepth = UIHelper.getImage( "icons/wizban/percentstackedconechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgSideBySideWithDepth = UIHelper.getImage( "icons/wizban/sidebysideconechartwithdepthimage.gif" ); //$NON-NLS-1$
			}
			else
			{
				imgStackedWithDepth = UIHelper.getImage( "icons/wizban/horizontalstackedconechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgPercentStackedWithDepth = UIHelper.getImage( "icons/wizban/horizontalpercentstackedconechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgSideBySideWithDepth = UIHelper.getImage( "icons/wizban/horizontalsidebysideconechartwithdepthimage.gif" ); //$NON-NLS-1$
			}
			vSubTypes.add( new DefaultChartSubTypeImpl( SIDE_SUBTYPE_LITERAL,
					imgSideBySideWithDepth,
					sSideBySideDescription,
					Messages.getString( "ConeChart.SubType.Side" ) ) ); //$NON-NLS-1$
			vSubTypes.add( new DefaultChartSubTypeImpl( STACKED_SUBTYPE_LITERAL,
					imgStackedWithDepth,
					sStackedDescription,
					Messages.getString( "ConeChart.SubType.Stacked" ) ) ); //$NON-NLS-1$
			vSubTypes.add( new DefaultChartSubTypeImpl( PERCENTSTACKED_SUBTYPE_LITERAL,
					imgPercentStackedWithDepth,
					sPercentStackedDescription,
					Messages.getString( "ConeChart.SubType.PercentStacked" ) ) ); //$NON-NLS-1$
		}
		else if ( sDimension.equals( THREE_DIMENSION_TYPE )
				|| sDimension.equals( ChartDimension.THREE_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			if ( orientation.equals( Orientation.VERTICAL_LITERAL ) )
			{
				imgSideBySide3D = UIHelper.getImage( "icons/wizban/sidebysideconechart3dimage.gif" ); //$NON-NLS-1$
			}
			else
			{
				imgSideBySide3D = UIHelper.getImage( "icons/wizban/horizontalsidebysideconechart3dimage.gif" ); //$NON-NLS-1$
			}
			vSubTypes.add( new DefaultChartSubTypeImpl( SIDE_SUBTYPE_LITERAL,
					imgSideBySide3D,
					sSideBySideDescription,
					Messages.getString( "ConeChart.SubType.Side" ) ) ); //$NON-NLS-1$
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
		newChart.setOrientation( orientation );
		newChart.setDimension( ChartUIUtil.getDimensionType( sDimension ) );
		newChart.setUnits( "Points" ); //$NON-NLS-1$

		( (Axis) newChart.getAxes( ).get( 0 ) ).setOrientation( Orientation.HORIZONTAL_LITERAL );
		( (Axis) newChart.getAxes( ).get( 0 ) ).setType( AxisType.TEXT_LITERAL );
		( (Axis) newChart.getAxes( ).get( 0 ) ).setCategoryAxis( true );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		Series categorySeries = SeriesImpl.create( );
		sdX.getSeries( ).add( categorySeries );
		sdX.getSeriesPalette( ).shift( 0 );
		( (Axis) newChart.getAxes( ).get( 0 ) ).getSeriesDefinitions( )
				.add( sdX );

		newChart.getTitle( ).getLabel( ).getCaption( ).setValue( CHART_TITLE );

		newChart.setUnitSpacing( 50 );

		if ( sSubType.equalsIgnoreCase( STACKED_SUBTYPE_LITERAL ) )
		{
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setType( AxisType.LINEAR_LITERAL );

			SeriesDefinition sdY = SeriesDefinitionImpl.create( );
			sdY.getSeriesPalette( ).shift( 0 );
			Series valueSeries = BarSeriesImpl.create( );
			( (BarSeries) valueSeries ).setRiser( RiserType.CONE_LITERAL );
			valueSeries.setStacked( true );
			sdY.getSeries( ).add( valueSeries );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).add( sdY );
		}
		else if ( sSubType.equalsIgnoreCase( PERCENTSTACKED_SUBTYPE_LITERAL ) )
		{
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setType( AxisType.LINEAR_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setPercent( true );

			SeriesDefinition sdY = SeriesDefinitionImpl.create( );
			sdY.getSeriesPalette( ).shift( 0 );
			Series valueSeries = BarSeriesImpl.create( );
			( (BarSeries) valueSeries ).setRiser( RiserType.CONE_LITERAL );
			valueSeries.setStacked( true );
			( (BarSeries) valueSeries ).setStacked( true );
			sdY.getSeries( ).add( valueSeries );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).add( sdY );
		}
		else if ( sSubType.equalsIgnoreCase( SIDE_SUBTYPE_LITERAL ) )
		{
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setType( AxisType.LINEAR_LITERAL );

			SeriesDefinition sdY = SeriesDefinitionImpl.create( );
			sdY.getSeriesPalette( ).shift( 0 );
			Series valueSeries = BarSeriesImpl.create( );
			( (BarSeries) valueSeries ).setRiser( RiserType.CONE_LITERAL );
			( (BarSeries) valueSeries ).setStacked( false );
			sdY.getSeries( ).add( valueSeries );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).add( sdY );
		}

		if ( sDimension.equals( THREE_DIMENSION_TYPE ) )
		{
			newChart.setRotation( Rotation3DImpl.create( new Angle3D[]{
				Angle3DImpl.create( -20, 45, 0 )
			} ) );

			newChart.getPrimaryBaseAxes( )[0].getAncillaryAxes( ).clear( );

			Axis zAxisAncillary = AxisImpl.create( Axis.ANCILLARY_BASE );
			zAxisAncillary.setTitlePosition( Position.BELOW_LITERAL );
			zAxisAncillary.getTitle( )
					.getCaption( )
					.setValue( Messages.getString( "ChartWithAxesImpl.Z_Axis.title" ) ); //$NON-NLS-1$
			zAxisAncillary.getTitle( ).setVisible( true );
			zAxisAncillary.setPrimaryAxis( true );
			zAxisAncillary.setLabelPosition( Position.BELOW_LITERAL );
			zAxisAncillary.setOrientation( Orientation.HORIZONTAL_LITERAL );
			zAxisAncillary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
			zAxisAncillary.getOrigin( )
					.setValue( NumberDataElementImpl.create( 0 ) );
			zAxisAncillary.getTitle( ).setVisible( false );
			zAxisAncillary.setType( AxisType.TEXT_LITERAL );
			newChart.getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.add( zAxisAncillary );

			newChart.getPrimaryOrthogonalAxis( newChart.getPrimaryBaseAxes( )[0] )
					.getTitle( )
					.getCaption( )
					.getFont( )
					.setRotation( 0 );

			SeriesDefinition sdZ = SeriesDefinitionImpl.create( );
			sdZ.getSeriesPalette( ).shift( 0 );
			sdZ.getSeries( ).add( SeriesImpl.create( ) );
			zAxisAncillary.getSeriesDefinitions( ).add( sdZ );
		}

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
		sdBase.setDataSetRepresentation( "A, B, C" ); //$NON-NLS-1$
		sd.getBaseSampleData( ).add( sdBase );

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		oSample.setDataSetRepresentation( "5,4,12" ); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( oSample );

		if ( newChart.getDimension( ) == ChartDimension.THREE_DIMENSIONAL_LITERAL )
		{
			BaseSampleData sdAncillary = DataFactory.eINSTANCE.createBaseSampleData( );
			sdAncillary.setDataSetRepresentation( "Series 1" ); //$NON-NLS-1$
			sd.getAncillarySampleData( ).add( sdAncillary );
		}

		newChart.setSampleData( sd );
	}

	private Chart getConvertedChart( Chart currentChart, String sNewSubType,
			Orientation newOrientation, String sNewDimension )
	{
		Chart helperModel = (Chart) EcoreUtil.copy( currentChart );
		ChartDimension oldDimension = currentChart.getDimension( );
		// Cache series to keep attributes during conversion
		ChartCacheManager.getInstance( )
				.cacheSeries( ChartUIUtil.getAllOrthogonalSeriesDefinitions( helperModel ) );
		if ( ( currentChart instanceof ChartWithAxes ) )
		{
			if ( currentChart.getType( ).equals( LineChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( AreaChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( StockChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( ScatterChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( BubbleChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( DifferenceChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( GanttChart.TYPE_LITERAL ) )
			{
				currentChart.setType( TYPE_LITERAL );
				currentChart.setSubType( sNewSubType );
				currentChart.getTitle( )
						.getLabel( )
						.getCaption( )
						.setValue( CHART_TITLE );

				ArrayList axisTypes = new ArrayList( );
				EList axes = ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( );
				for ( int i = 0, seriesIndex = 0; i < axes.size( ); i++ )
				{
					if ( sNewSubType.equalsIgnoreCase( PERCENTSTACKED_SUBTYPE_LITERAL ) )
					{
						if ( !ChartPreviewPainter.isLivePreviewActive( )
								&& !isNumbericAxis( (Axis) axes.get( i ) ) )
						{
							( (Axis) axes.get( i ) ).setType( AxisType.LINEAR_LITERAL );
						}
						( (Axis) axes.get( i ) ).setPercent( true );
					}
					else
					{
						( (Axis) axes.get( i ) ).setPercent( false );
					}
					EList seriesdefinitions = ( (Axis) axes.get( i ) ).getSeriesDefinitions( );
					for ( int j = 0; j < seriesdefinitions.size( ); j++ )
					{
						Series series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
						series = getConvertedSeries( series, seriesIndex++ );
						if ( ( sNewSubType.equalsIgnoreCase( STACKED_SUBTYPE_LITERAL ) || sNewSubType.equalsIgnoreCase( PERCENTSTACKED_SUBTYPE_LITERAL ) ) )
						{
							if ( !ChartPreviewPainter.isLivePreviewActive( )
									&& !isNumbericAxis( (Axis) axes.get( i ) ) )
							{
								( (Axis) axes.get( i ) ).setType( AxisType.LINEAR_LITERAL );
							}
							series.setStacked( true );
						}
						else
						{
							series.setStacked( false );
						}
						( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
								.clear( );
						( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
								.add( series );
						axisTypes.add( ( (Axis) axes.get( i ) ).getType( ) );
					}
				}

				currentChart.setSampleData( getConvertedSampleData( currentChart.getSampleData( ),
						( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
								.get( 0 ) ).getType( ),
						axisTypes ) );
			}
			else
			{
				currentChart.getTitle( )
						.getLabel( )
						.getCaption( )
						.setValue( CHART_TITLE );
				EList axes = ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( );
				for ( int i = 0; i < axes.size( ); i++ )
				{
					if ( !currentChart.getSubType( ).equals( sNewSubType ) )
					{
						if ( sNewSubType.equalsIgnoreCase( PERCENTSTACKED_SUBTYPE_LITERAL ) )
						{
							if ( !ChartPreviewPainter.isLivePreviewActive( )
									&& !isNumbericAxis( (Axis) axes.get( i ) ) )
							{
								( (Axis) axes.get( i ) ).setType( AxisType.LINEAR_LITERAL );
							}
							( (Axis) axes.get( i ) ).setPercent( true );
						}
						else
						{
							( (Axis) axes.get( i ) ).setPercent( false );
						}
					}
					EList seriesdefinitions = ( (Axis) axes.get( i ) ).getSeriesDefinitions( );
					for ( int j = 0; j < seriesdefinitions.size( ); j++ )
					{
						Series series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
						( (BarSeries) series ).setRiser( RiserType.CONE_LITERAL );
						if ( !currentChart.getSubType( ).equals( sNewSubType ) )
						{
							if ( ( sNewSubType.equalsIgnoreCase( STACKED_SUBTYPE_LITERAL ) || sNewSubType.equalsIgnoreCase( PERCENTSTACKED_SUBTYPE_LITERAL ) ) )
							{
								if ( !ChartPreviewPainter.isLivePreviewActive( )
										&& !isNumbericAxis( (Axis) axes.get( i ) ) )
								{
									( (Axis) axes.get( i ) ).setType( AxisType.LINEAR_LITERAL );
								}
								series.setStacked( true );
							}
							else
							{
								series.setStacked( false );
							}
						}
					}
				}
				if ( !currentChart.getSubType( ).equals( sNewSubType ) )
				{
					currentChart.setSubType( sNewSubType );
				}
			}
		}
		else
		{
			// Create a new instance of the correct type and set initial
			// properties
			currentChart = ChartWithAxesImpl.create( );
			currentChart.setType( TYPE_LITERAL );
			currentChart.setSubType( sNewSubType );
			( (ChartWithAxes) currentChart ).setOrientation( newOrientation );
			currentChart.setDimension( ChartUIUtil.getDimensionType( sNewDimension ) );

			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setOrientation( Orientation.HORIZONTAL_LITERAL );
			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setCategoryAxis( true );

			( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );

			// Copy generic chart properties from the old chart
			currentChart.setBlock( helperModel.getBlock( ) );
			currentChart.setDescription( helperModel.getDescription( ) );
			currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );
			currentChart.setSampleData( helperModel.getSampleData( ) );
			currentChart.setScript( helperModel.getScript( ) );
			currentChart.setSeriesThickness( helperModel.getSeriesThickness( ) );
			currentChart.setUnits( helperModel.getUnits( ) );

			if ( helperModel.getInteractivity( ) != null )
			{
				currentChart.getInteractivity( )
						.setEnable( helperModel.getInteractivity( ).isEnable( ) );
				currentChart.getInteractivity( )
						.setLegendBehavior( helperModel.getInteractivity( )
								.getLegendBehavior( ) );
			}

			if ( helperModel.getType( ).equals( PieChart.TYPE_LITERAL )
					|| helperModel.getType( ).equals( MeterChart.TYPE_LITERAL ) )
			{
				// Clear existing series definitions
				( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.clear( );

				// Copy base series definitions
				( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.add( ( (ChartWithoutAxes) helperModel ).getSeriesDefinitions( )
								.get( 0 ) );

				// Clear existing series definitions
				( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.clear( );

				// Copy orthogonal series definitions
				( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.addAll( ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
								.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getSeriesDefinitions( ) );

				// Update the base series
				Series series = ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getDesignTimeSeries( );
				// series = getConvertedSeries( series );

				// Clear existing series
				( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getSeries( )
						.clear( );

				// Add converted series
				( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getSeries( )
						.add( series );

				// Update the orthogonal series
				EList seriesdefinitions = ( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( );
				for ( int j = 0; j < seriesdefinitions.size( ); j++ )
				{
					series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
					series = getConvertedSeries( series, j );
					series.getLabel( ).setVisible( false );
					if ( ( sNewSubType.equalsIgnoreCase( STACKED_SUBTYPE_LITERAL ) || sNewSubType.equalsIgnoreCase( PERCENTSTACKED_SUBTYPE_LITERAL ) ) )
					{
						series.setStacked( true );
					}
					else
					{
						series.setStacked( false );
					}
					// Clear any existing series
					( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
							.clear( );
					// Add the new series
					( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
							.add( series );
				}
			}
			else
			{
				return null;
			}
			currentChart.getLegend( )
					.setItemType( LegendItemType.SERIES_LITERAL );
			currentChart.getTitle( )
					.getLabel( )
					.getCaption( )
					.setValue( CHART_TITLE );
		}
		if ( currentChart instanceof ChartWithAxes
				&& !( (ChartWithAxes) currentChart ).getOrientation( )
						.equals( newOrientation ) )
		{
			( (ChartWithAxes) currentChart ).setOrientation( newOrientation );
		}
		if ( !currentChart.getDimension( )
				.equals( ChartUIUtil.getDimensionType( sNewDimension ) ) )
		{
			currentChart.setDimension( ChartUIUtil.getDimensionType( sNewDimension ) );
		}

		if ( sNewDimension.equals( THREE_DIMENSION_TYPE )
				&& ChartUIUtil.getDimensionType( sNewDimension ) != oldDimension )
		{
			( (ChartWithAxes) currentChart ).setRotation( Rotation3DImpl.create( new Angle3D[]{
				Angle3DImpl.create( -20, 45, 0 )
			} ) );

			( (ChartWithAxes) currentChart ).getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.clear( );

			Axis zAxisAncillary = AxisImpl.create( Axis.ANCILLARY_BASE );
			zAxisAncillary.setTitlePosition( Position.BELOW_LITERAL );
			zAxisAncillary.getTitle( )
					.getCaption( )
					.setValue( Messages.getString( "ChartWithAxesImpl.Z_Axis.title" ) ); //$NON-NLS-1$
			zAxisAncillary.getTitle( ).setVisible( true );
			zAxisAncillary.setPrimaryAxis( true );
			zAxisAncillary.setLabelPosition( Position.BELOW_LITERAL );
			zAxisAncillary.setOrientation( Orientation.HORIZONTAL_LITERAL );
			zAxisAncillary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
			zAxisAncillary.getOrigin( )
					.setValue( NumberDataElementImpl.create( 0 ) );
			zAxisAncillary.getTitle( ).setVisible( false );
			zAxisAncillary.setType( AxisType.TEXT_LITERAL );
			( (ChartWithAxes) currentChart ).getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.add( zAxisAncillary );

			SeriesDefinition sdZ = SeriesDefinitionImpl.create( );
			sdZ.getSeriesPalette( ).shift( 0 );
			sdZ.getSeries( ).add( SeriesImpl.create( ) );
			zAxisAncillary.getSeriesDefinitions( ).add( sdZ );

			if ( currentChart.getSampleData( )
					.getAncillarySampleData( )
					.isEmpty( ) )
			{
				BaseSampleData sdAncillary = DataFactory.eINSTANCE.createBaseSampleData( );
				sdAncillary.setDataSetRepresentation( "Series 1" ); //$NON-NLS-1$
				currentChart.getSampleData( )
						.getAncillarySampleData( )
						.add( sdAncillary );
			}

			EList seriesdefinitions = ChartUIUtil.getOrthogonalSeriesDefinitions( currentChart,
					0 );
			for ( int j = 0; j < seriesdefinitions.size( ); j++ )
			{
				Series series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
				( (BarSeries) series ).setRiser( RiserType.CONE_LITERAL );
				series.setStacked( false );// Stacked is unsupported in 3D
				if ( ( series instanceof BarSeries )
						&& ( series.getLabelPosition( ) != Position.OUTSIDE_LITERAL ) )
				{
					series.setLabelPosition( Position.OUTSIDE_LITERAL );
				}
			}
		}

		return currentChart;
	}

	private boolean isNumbericAxis( Axis axis )
	{
		return ( axis.getType( ).getValue( ) == AxisType.LINEAR )
				|| ( axis.getType( ).getValue( ) == AxisType.LOGARITHMIC );
	}

	private Series getConvertedSeries( Series series, int seriesIndex )
	{
		// Do not convert base series
		if ( series.getClass( ).getName( ).equals( SeriesImpl.class.getName( ) ) )
		{
			return series;
		}

		BarSeries barseries = (BarSeries) ChartCacheManager.getInstance( )
				.findSeries( BarSeriesImpl.class.getName( ), seriesIndex );
		if ( barseries == null )
		{
			barseries = (BarSeries) BarSeriesImpl.create( );
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes( series, barseries );
		barseries.setRiser( RiserType.CONE_LITERAL );

		return barseries;
	}

	private SampleData getConvertedSampleData( SampleData currentSampleData,
			AxisType xAxisType, ArrayList axisTypes )
	{
		// Convert base sample data
		EList bsdList = currentSampleData.getBaseSampleData( );
		Vector vNewBaseSampleData = getConvertedBaseSampleDataRepresentation( bsdList,
				xAxisType );
		currentSampleData.getBaseSampleData( ).clear( );
		currentSampleData.getBaseSampleData( ).addAll( vNewBaseSampleData );

		// Convert orthogonal sample data
		EList osdList = currentSampleData.getOrthogonalSampleData( );
		Vector vNewOrthogonalSampleData = getConvertedOrthogonalSampleDataRepresentation( osdList,
				axisTypes );
		currentSampleData.getOrthogonalSampleData( ).clear( );
		currentSampleData.getOrthogonalSampleData( )
				.addAll( vNewOrthogonalSampleData );
		return currentSampleData;
	}

	private Vector getConvertedBaseSampleDataRepresentation( EList bsdList,
			AxisType xAxisType )
	{
		Vector vNewBaseSampleData = new Vector( );
		for ( int i = 0; i < bsdList.size( ); i++ )
		{
			BaseSampleData bsd = (BaseSampleData) bsdList.get( i );
			bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( xAxisType,
					bsd.getDataSetRepresentation( ),
					i ) );
			vNewBaseSampleData.add( bsd );
		}
		return vNewBaseSampleData;
	}

	private Vector getConvertedOrthogonalSampleDataRepresentation(
			EList osdList, ArrayList axisTypes )
	{
		Vector vNewOrthogonalSampleData = new Vector( );
		for ( int i = 0; i < osdList.size( ); i++ )
		{
			OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get( i );
			osd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( (AxisType) axisTypes.get( i ),
					osd.getDataSetRepresentation( ),
					i ) );
			vNewOrthogonalSampleData.add( osd );
		}
		return vNewOrthogonalSampleData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions()
	 */
	public String[] getSupportedDimensions( )
	{
		return saDimensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	public String getDefaultDimension( )
	{
		return saDimensions[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition()
	 */
	public boolean supportsTransposition( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition(java.lang.String)
	 */
	public boolean supportsTransposition( String dimension )
	{
		if ( ChartUIUtil.getDimensionType( dimension ) == ChartDimension.THREE_DIMENSIONAL_LITERAL )
		{
			return false;
		}

		return supportsTransposition( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#canAdapt(org.eclipse.birt.chart.model.Chart,
	 *      java.util.Hashtable)
	 */
	public boolean canAdapt( Chart cModel, Hashtable htModelHints )
	{
		return false;
	}

	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle )
	{
		return new DefaultBaseSeriesComponent( (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( chart )
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
		return Messages.getString( "ConeChart.Txt.DisplayName" ); //$NON-NLS-1$
	}
}