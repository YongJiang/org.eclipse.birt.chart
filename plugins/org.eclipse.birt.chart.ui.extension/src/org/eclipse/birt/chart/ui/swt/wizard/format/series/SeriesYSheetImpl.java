/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DecorationSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DialLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DialScaleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.DialTickSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.LineSeriesMarkerSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.NeedleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.PieTitleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesRegionSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesTrendlineSheet;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 * Subtask for Value Series
 * 
 */
public class SeriesYSheetImpl extends SubtaskSheetImpl implements
		Listener,
		SelectionListener
{

	private Button btnShowLine;

	private Button btnTrendline;

	private Button btnDialLabel;

	private Button btnLabel;

	private Button btnDecoration;

	private Button cbVisible;

	private Button cbDecoVisible;

	public void createControl( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		// Series details composite
		Group grpDetails = new Group( cmpContent, SWT.NONE );
		GridData gdCMPDetails = new GridData( GridData.FILL_BOTH );
		gdCMPDetails.horizontalSpan = 2;
		grpDetails.setLayoutData( gdCMPDetails );
		grpDetails.setLayout( new FillLayout( ) );
		grpDetails.setText( Messages.getString( "OrthogonalSeriesAttributeSheetImpl.Lbl.SeriesDetails" ) ); //$NON-NLS-1$

		// Series composite
		Series series = getSeriesDefinitionForProcessing( ).getDesignTimeSeries( );
		getSeriesAttributeUI( series, grpDetails );

		Composite cmpBottom = new Composite( cmpContent, SWT.NONE );
		GridLayout glBottom = new GridLayout( 3, false );
		cmpBottom.setLayout( glBottom );

		cbVisible = new Button( cmpBottom, SWT.CHECK );
		{
			cbVisible.addSelectionListener( this );
			cbVisible.setSelection( isMeterSeries( ) ? ( (DialSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) ).getDial( )
					.getLabel( )
					.isVisible( )
					: getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
							.getLabel( )
							.isVisible( ) );
		}
		if ( isMeterSeries( ) )
		{
			cbVisible.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowDialLabels" ) ); //$NON-NLS-1$
		}
		else
		{
			cbVisible.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowLabels" ) ); //$NON-NLS-1$
		}

		if ( isGanttSeries( ) )
		{
			cbDecoVisible = new Button( cmpBottom, SWT.CHECK );
			{
				cbDecoVisible.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowDecoLabels" ) ); //$NON-NLS-1$
				cbDecoVisible.addSelectionListener( this );
				cbDecoVisible.setSelection( ( (GanttSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) ).getDecorationLabel( )
						.isVisible( ) );
			}
		}

		if ( isTrendlineAvailable( ) )
		{
			btnShowLine = new Button( cmpBottom, SWT.CHECK );
			{
				btnShowLine.setText( Messages.getString( "SeriesYSheetImpl.Label.ShowTrendline" ) ); //$NON-NLS-1$
				btnShowLine.addSelectionListener( this );
				btnShowLine.setSelection( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
						.getCurveFitting( ) != null );
			}
		}

		createButtonGroup( cmpContent );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 6, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		// For Meter series and other non-Stock series
		ITaskPopupSheet popup;

		if ( isMeterSeries( ) )
		{
			// Label
			popup = new DialLabelSheet( Messages.getString( "SeriesYSheetImpl.Label.DialLabels" ), //$NON-NLS-1$
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			btnDialLabel = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.DialLabels&" ), //$NON-NLS-1$
					popup );
			btnDialLabel.addSelectionListener( this );
			btnDialLabel.setEnabled( cbVisible.getSelection( ) );

			if ( getChart( ) instanceof DialChart
					&& !( (DialChart) getChart( ) ).isDialSuperimposition( ) )
			{
				// Needles
				popup = new NeedleSheet( Messages.getString( "SeriesYSheetImpl.Label.Needles" ), //$NON-NLS-1$
						getContext( ),
						getSeriesDefinitionForProcessing( ) );
				Button btnNeedles = createToggleButton( cmp,
						Messages.getString( "SeriesYSheetImpl.Label.Needles&" ), //$NON-NLS-1$
						popup );
				btnNeedles.addSelectionListener( this );
			}

			// Region
			popup = new SeriesRegionSheet( Messages.getString( "SeriesYSheetImpl.Label.Region" ), //$NON-NLS-1$
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			Button btnDialRegion = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Region&" ), //$NON-NLS-1$
					popup );
			btnDialRegion.addSelectionListener( this );

			// Ticks
			popup = new DialTickSheet( Messages.getString( "DialTicksDialog.Title.DialTicks" ), //$NON-NLS-1$
					getContext( ),
					( (DialSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) ) );
			Button btnDialTicks = createToggleButton( cmp,
					Messages.getString( "MeterSeriesAttributeComposite.Lbl.DialTicks" ), //$NON-NLS-1$
					popup );
			btnDialTicks.addSelectionListener( this );

			// Scale
			popup = new DialScaleSheet( Messages.getString( "DialScaleDialog.Title.DialScale" ), //$NON-NLS-1$
					getContext( ),
					( (DialSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) ) );
			Button btnDialScale = createToggleButton( cmp,
					Messages.getString( "MeterSeriesAttributeComposite.Lbl.DialScale" ), //$NON-NLS-1$
					popup );
			btnDialScale.addSelectionListener( this );
		}
		else
		{
			// Label
			popup = new SeriesLabelSheet( Messages.getString( "SeriesYSheetImpl.Label.Labels" ), //$NON-NLS-1$
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			btnLabel = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Labels&" ), //$NON-NLS-1$
					popup );
			btnLabel.addSelectionListener( this );
			btnLabel.setEnabled( cbVisible.getSelection( ) );

		}

		// Titles for Pie series
		if ( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof PieSeries )
		{
			popup = new PieTitleSheet( Messages.getString( "SeriesYSheetImpl.Label.Titles" ), //$NON-NLS-1$
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			Button btnPieTitle = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Titles&" ), //$NON-NLS-1$
					popup );
			btnPieTitle.addSelectionListener( this );
		}

		// Markers for Line/Area/Scatter series
		if ( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof LineSeries
				&& !isDifferenceSeries( ) )
		{
			popup = new LineSeriesMarkerSheet( Messages.getString( "SeriesYSheetImpl.Label.Markers" ), //$NON-NLS-1$
					getContext( ),
					(LineSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) );
			Button btnLineMarker = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Markers&" ), //$NON-NLS-1$
					popup );
			btnLineMarker.addSelectionListener( this );
		}

		// Markers for Difference series
		if ( isDifferenceSeries( ) )
		{
			popup = new LineSeriesMarkerSheet( Messages.getString( "SeriesYSheetImpl.Label.PositiveMarkers" ), //$NON-NLS-1$
					getContext( ),
					(DifferenceSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ),
					true );
			Button btnPLineMarker = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.PositiveMarkers&" ), //$NON-NLS-1$
					popup );
			btnPLineMarker.addSelectionListener( this );

			popup = new LineSeriesMarkerSheet( Messages.getString( "SeriesYSheetImpl.Label.NegativeMarkers" ), //$NON-NLS-1$
					getContext( ),
					(DifferenceSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ),
					false );
			Button btnNLineMarker = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.NegativeMarkers&" ), //$NON-NLS-1$
					popup );
			btnNLineMarker.addSelectionListener( this );
		}

		// Decoration Label for Gantt series
		if ( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof GanttSeries )
		{
			popup = new DecorationSheet( Messages.getString( "SeriesYSheetImpl.Label.Decoration" ), //$NON-NLS-1$
					getContext( ),
					(GanttSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) );
			btnDecoration = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Decoration&" ), //$NON-NLS-1$
					popup );
			btnDecoration.addSelectionListener( this );
			btnDecoration.setEnabled( cbDecoVisible.getSelection( ) );
		}

		// Trendline
		if ( isTrendlineAvailable( ) )
		{
			popup = new SeriesTrendlineSheet( Messages.getString( "SeriesYSheetImpl.Label.Trendline" ), //$NON-NLS-1$
					getContext( ),
					getSeriesDefinitionForProcessing( ) );
			btnTrendline = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Trendline&" ), //$NON-NLS-1$
					popup );
			btnTrendline.addSelectionListener( this );
			btnTrendline.setEnabled( btnShowLine.getSelection( ) );
		}

		if ( !( getChart( ) instanceof DialChart && ( (DialChart) getChart( ) ).isDialSuperimposition( ) ) )
		{
			// Interactivity
			popup = new InteractivitySheet( Messages.getString( "SeriesYSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
					getContext( ),
					getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
							.getTriggers( ),
					TriggerSupportMatrix.TYPE_DATAPOINT,
					true,
					false );
			Button btnInteractivity = createToggleButton( cmp,
					Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
					popup );
			btnInteractivity.addSelectionListener( this );
			btnInteractivity.setEnabled( getChart( ).getInteractivity( )
					.isEnable( ) );
		}
	}

	private void getSeriesAttributeUI( Series series, Composite parent )
	{
		ChartUIUtil.getSeriesUIProvider( series )
				.getSeriesAttributeSheet( parent, series, getContext( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{

	}

	public void widgetSelected( SelectionEvent e )
	{
		// Detach popup dialog if there's selected popup button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getText( ) );
		}

		if ( e.widget.equals( btnShowLine ) )
		{
			btnTrendline.setEnabled( btnShowLine.getSelection( ) );
			if ( btnShowLine.getSelection( ) )
			{
				CurveFitting cf = CurveFittingImpl.create( );
				cf.eAdapters( )
						.addAll( getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
								.eAdapters( ) );
				getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
						.setCurveFitting( cf );
			}
			else
			{
				getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
						.setCurveFitting( null );
				// Close trendline popup
				if ( btnTrendline.getSelection( ) )
				{
					btnTrendline.setSelection( false );
					detachPopup( btnTrendline );
				}
			}
		}
		else if ( e.widget.equals( cbVisible ) )
		{
			if ( isMeterSeries( ) )
			{
				btnDialLabel.setEnabled( cbVisible.getSelection( ) );
				( (DialSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) ).getDial( )
						.getLabel( )
						.setVisible( cbVisible.getSelection( ) );
				if ( !cbVisible.getSelection( ) && btnDialLabel.getSelection( ) )
				{
					btnDialLabel.setSelection( false );
					detachPopup( btnDialLabel );
				}
			}
			else
			{
				btnLabel.setEnabled( cbVisible.getSelection( ) );
				getSeriesDefinitionForProcessing( ).getDesignTimeSeries( )
						.getLabel( )
						.setVisible( cbVisible.getSelection( ) );
				if ( !cbVisible.getSelection( ) && btnLabel.getSelection( ) )
				{
					btnLabel.setSelection( false );
					detachPopup( btnLabel );
				}
			}
			if ( cbVisible.getSelection( ) )
			{
				refreshPopupSheet( );
			}

		}
		else if ( e.widget.equals( cbDecoVisible ) )
		{
			btnDecoration.setEnabled( cbDecoVisible.getSelection( ) );
			( (GanttSeries) getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) ).getDecorationLabel( )
					.setVisible( cbDecoVisible.getSelection( ) );
			if ( !cbDecoVisible.getSelection( ) && btnDecoration.getSelection( ) )
			{
				btnDecoration.setSelection( false );
				detachPopup( btnDecoration );
			}
			else
			{
				refreshPopupSheet( );
			}
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	private SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		SeriesDefinition sd = null;
		if ( getChart( ) instanceof ChartWithAxes )
		{
			int iAxis = getParentAxisIndex( getIndex( ) );
			int iAxisSeries = getSeriesIndexWithinAxis( getIndex( ) );
			sd = ( (SeriesDefinition) ( (Axis) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( iAxis ) ).getSeriesDefinitions( )
					.get( iAxisSeries ) );
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			sd = (SeriesDefinition) ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).get( getIndex( ) );
		}
		return sd;
	}

	private int getParentAxisIndex( int iSeriesDefinitionIndex )
	{
		int iTmp = 0;
		int iAxisCount = ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( ).size( );
		for ( int i = 0; i < iAxisCount; i++ )
		{
			iTmp += ( (Axis) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( i ) ).getSeriesDefinitions( )
					.size( );
			if ( iTmp - 1 >= iSeriesDefinitionIndex )
			{
				return i;
			}
		}
		return 0;
	}

	private int getSeriesIndexWithinAxis( int iSeriesDefinitionIndex )
	{
		int iTotalDefinitions = 0;
		int iAxisCount = ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( ).size( );
		for ( int i = 0; i < iAxisCount; i++ )
		{
			int iOldTotal = iTotalDefinitions;
			iTotalDefinitions += ( (Axis) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( i ) ).getSeriesDefinitions( )
					.size( );
			if ( iTotalDefinitions - 1 >= iSeriesDefinitionIndex )
			{
				return iSeriesDefinitionIndex - iOldTotal;
			}
		}
		return iSeriesDefinitionIndex;
	}

	private boolean isTrendlineAvailable( )
	{
		return ( getChart( ) instanceof ChartWithAxes )
				&& ( !isGanttSeries( ) )
				&& ( !isDifferenceSeries( ) )
				&& ( getChart( ).getDimension( ) != ChartDimension.THREE_DIMENSIONAL_LITERAL );
	}

	private boolean isMeterSeries( )
	{
		return getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof DialSeries;
	}

	private boolean isGanttSeries( )
	{
		return getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof GanttSeries;
	}

	private boolean isDifferenceSeries( )
	{
		return getSeriesDefinitionForProcessing( ).getDesignTimeSeries( ) instanceof DifferenceSeries;
	}

}