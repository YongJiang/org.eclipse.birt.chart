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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.withaxes.ScaleContext;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ChartReportStyleProcessor;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */
public final class DesignerRepresentation extends Figure
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	private final static String ERROR_MSG = Messages.getString( "DesignerRepresentation.error.Error" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private IDeviceRenderer idr = null;

	/**
	 * 
	 */
	private final ChartReportItemImpl crii;

	private transient Chart cm;

	/**
	 * 
	 */
	private Image imgChart = null;

	/**
	 * 
	 */
	private GC gc = null;

	/**
	 * 
	 */
	private transient boolean bDirty = true;

	/**
	 * 
	 */
	private static final PaletteData PALETTE_DATA = new PaletteData( 0xFF0000,
			0xFF00,
			0xFF );

	/**
	 * 
	 */
	private static final int TRANSPARENT_COLOR = 0x123456;

	/**
	 * Prevent re-entrancy of the paint method
	 */
	private boolean bPainting = false;

	/**
	 * 
	 * @param crii
	 */
	DesignerRepresentation( ChartReportItemImpl crii )
	{
		this.crii = crii;

		updateChartModelAndSize( );

		try
		{
			idr = PluginSettings.instance( ).getDevice( "dv.SWT" ); //$NON-NLS-1$
		}
		catch ( ChartException pex )
		{
			logger.log( pex );
		}
	}

	private void updateChartModelAndSize( )
	{
		if ( crii != null )
		{
			cm = (Chart) crii.getProperty( ChartReportItemUtil.PROPERTY_CHART );

			// GET THE MODEL WRAPPED INSIDE THE REPORT ITEM IMPL
			if ( cm != null )
			{
				if ( ChartXTabUtil.isPlotChart( crii.getHandle( ) ) )
				{
					// Update model for Plot chart
					cm = ChartXTabUtil.updateModelToRenderPlot( (Chart) EcoreUtil.copy( cm ),
							crii.getHandle( ).isDirectionRTL( ) );
				}
				else if ( ChartXTabUtil.isAxisChart( crii.getHandle( ) ) )
				{
					// Update model for Axis chart
					cm = ChartXTabUtil.updateModelToRenderAxis( (Chart) EcoreUtil.copy( cm ),
							crii.getHandle( ).isDirectionRTL( ) );
				}

				// Do not modify size for axis chart
				if ( !ChartXTabUtil.isAxisChart( crii.getHandle( ) ) )
				{
					final IDisplayServer idsSWT = ChartUIUtil.getDisplayServer( ); // REUSE
					final Bounds bo = cm.getBlock( )
							.getBounds( )
							.scaledInstance( idsSWT.getDpiResolution( ) / 72d );
					setSize( (int) bo.getWidth( ), (int) bo.getHeight( ) );
				}
			}
			else
			{
				setSize( (int) ChartReportItemUtil.DEFAULT_CHART_BLOCK_WIDTH,
						(int) ChartReportItemUtil.DEFAULT_CHART_BLOCK_HEIGHT );
			}
		}
		else
		{
			setSize( (int) ChartReportItemUtil.DEFAULT_CHART_BLOCK_WIDTH,
					(int) ChartReportItemUtil.DEFAULT_CHART_BLOCK_HEIGHT );
		}
	}

	/**
	 * 
	 * @param bDirty
	 */
	final void setDirty( boolean bDirty )
	{
		this.bDirty = bDirty;
		updateChartModelAndSize( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize( int wHint, int hHint )
	{
		if ( minSize != null )
		{
			return minSize;
		}

		DimensionHandle dimWidth = crii.getHandle( ).getWidth( );
		DimensionHandle dimHeight = crii.getHandle( ).getHeight( );

		boolean isPerWidth = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimWidth.getUnits( ) );
		boolean isPerHeight = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimHeight.getUnits( ) );

		Dimension dim = new Dimension( );
		Dimension size = getSize( );

		if ( !isPerWidth )
		{
			dim.width = Math.max( wHint, size.width );
		}

		if ( !isPerHeight )
		{
			dim.height = Math.max( hHint, size.height );
		}

		return dim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize( int wHint, int hHint )
	{
		if ( prefSize != null )
		{
			return prefSize;
		}

		DimensionHandle dimWidth = crii.getHandle( ).getWidth( );
		DimensionHandle dimHeight = crii.getHandle( ).getHeight( );

		boolean isPerWidth = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimWidth.getUnits( ) );
		boolean isPerHeight = DesignChoiceConstants.UNITS_PERCENTAGE.equals( dimHeight.getUnits( ) );

		Dimension dim = getSize( ).getCopy( );

		if ( isPerWidth && wHint != -1 )
		{
			// dim.width = Math.min( dim.width, wHint );
			// ?? TODO calculate the percentage value here?
			dim.width = (int) ( wHint * dimWidth.getMeasure( ) / 100d );
		}

		if ( isPerHeight && hHint != -1 )
		{
			// dim.height = Math.min( dim.height, hHint );
			// ?? TODO calculate the percentage value here?
			dim.height = (int) ( hHint * dimHeight.getMeasure( ) / 100d );
		}

		// If width or height is set to 0, set 72 as minimum size for the chart
		// figure displaying.
		Dimension newSize = dim.getCopy( );
		if ( dim.width == 0 )
		{
			newSize.width = 72;
		}
		if ( dim.height == 0 )
		{
			newSize.height = 72;
		}
		setSize( newSize.width, newSize.height );

		// If figure size of chart has no change, no need to update chart
		// bounds, returns directly.
		if ( newSize.equals( dim ) )
		{
			return newSize;
		}

		// ?? refresh the model size.
		// TODO this is a temp solution, better not refresh model here. and this
		// can not handle all the cases.
		if ( cm != null )
		{
			IDisplayServer ids = ChartUIUtil.getDisplayServer( );
			ChartAdapter.beginIgnoreNotifications( );
			cm.getBlock( )
					.getBounds( )
					.setWidth( ChartUtil.convertPixelsToPoints( ids, dim.width ) );
			cm.getBlock( )
					.getBounds( )
					.setHeight( ChartUtil.convertPixelsToPoints( ids,
							dim.height ) );
			ChartAdapter.endIgnoreNotifications( );
		}

		return newSize;
	}

	private List<Double> parseSampleData( String s )
	{
		List<Double> list = new ArrayList<Double>( );
		String[] sa = s.split( ",", 100 ); //$NON-NLS-1$

		for ( int i = 0; i < sa.length; i++ )
		{
			try
			{
				list.add( Double.valueOf( sa[i] ) );
			}
			catch ( RuntimeException e )
			{
				return null;
			}
		}

		return list;
	}

	private ScaleContext createSharedScaleFromSampleData( )
	{
		Double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;

		SampleData sd = cm.getSampleData( );

		if ( sd == null
				|| sd.getBaseSampleData( ).size( ) == 0
				|| sd.getOrthogonalSampleData( ).size( ) == 0 )
		{
			return null;
		}

		OrthogonalSampleData osd = (OrthogonalSampleData) sd.getOrthogonalSampleData( )
				.get( 0 );

		String sData = osd.getDataSetRepresentation( );

		List<Double> lData = parseSampleData( sData );

		if ( lData == null )
		{
			return null;
		}

		for ( int i = 0; i < lData.size( ); i++ )
		{
			double v = lData.get( i );

			min = Math.min( min, v );
			max = Math.max( max, v );
		}

		return ScaleContext.createSimpleScale( min, max );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.Figure#paintClientArea(org.eclipse.draw2d.Graphics)
	 */
	public final void paintClientArea( Graphics g )
	{
		if ( bPainting ) // PREVENT RE-ENTRANCY
		{
			return;
		}
		final Rectangle r = getClientArea( ).getCopy( );
		if ( r.width <= 0 || r.height <= 0 )
		{
			return;
		}
		bPainting = true;

		if ( bDirty )
		{
			bDirty = false;

			final Display d = Display.getCurrent( );
			Dimension dSize = r.getSize( );

			// OFFSCREEN IMAGE CREATION STRATEGY
			if ( imgChart == null
					|| imgChart.getImageData( ).width != dSize.width
					|| imgChart.getImageData( ).height != dSize.height )
			{
				if ( gc != null )
				{
					gc.dispose( );
				}
				if ( imgChart != null )
				{
					imgChart.dispose( );
				}
				bDirty = true;

				// FILL IMAGE WITH TRANSPARENCY
				final ImageData ida = new ImageData( dSize.width,
						dSize.height,
						32,
						PALETTE_DATA );
				ida.transparentPixel = TRANSPARENT_COLOR;
				/*
				 * for (int i = 0; i < ida.width; i++) { for (int j = 0; j <
				 * ida.height; j++) { ida.setPixel(i, j, TRANSPARENT_COLOR); } }
				 */
				imgChart = new Image( d, ida );
				gc = new GC( imgChart );
			}

			final Color clrPreviousBG = gc.getBackground( );
			gc.setBackground( Display.getDefault( )
					.getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
			gc.fillRectangle( 0,
					0,
					imgChart.getImageData( ).width,
					imgChart.getImageData( ).height );
			gc.setBackground( clrPreviousBG ); // RESTORE

			if ( cm == null )
			{
				showNullChart( dSize );
			}
			else
			{
				showChart( dSize );
			}
		}

		if ( imgChart != null )
		{
			g.drawImage( imgChart, r.x, r.y );
		}
		bPainting = false;
	}

	private void showNullChart( Dimension dSize )
	{
		// Display error message for null chart. This behavior is consistent
		// with invalid table.
		String MSG = Messages.getString( "DesignerRepresentation.msg.InvalidChart" ); //$NON-NLS-1$
		logger.log( ILogger.ERROR,
				Messages.getString( "DesignerRepresentation.log.UnableToFind" ) ); //$NON-NLS-1$

		Device dv = Display.getCurrent( );
		Font font = FontManager.getFont( "Dialog", 10, SWT.ITALIC ); //$NON-NLS-1$
		gc.setFont( font );
		FontMetrics fm = gc.getFontMetrics( );
		gc.setForeground( dv.getSystemColor( SWT.COLOR_RED ) );
		gc.setBackground( dv.getSystemColor( SWT.COLOR_WHITE ) );
		gc.fillRectangle( 0, 0, dSize.width - 1, dSize.height - 1 );
		gc.drawRectangle( 0, 0, dSize.width - 1, dSize.height - 1 );
		String[] texts = splitOnBreaks( MSG, font, dSize.width - 10 );
		int y = 5;
		for ( String text : texts )
		{
			gc.drawText( text, 5, y );
			y += fm.getHeight( );
		}
	}
	
	private List<Axis> findAllAxes( ChartWithAxes cwa )
	{
		List<Axis> al = new ArrayList<Axis>( );
		final Axis axBase = cwa.getPrimaryBaseAxes( )[0];
		al.add( axBase );
		al.addAll( Arrays.asList( cwa.getOrthogonalAxes( axBase, true ) ) );
		return al;
	}
	
	private void removeStepInfoForSample( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) chart;
			List<Axis> axisList = findAllAxes( cwa );
			for ( Axis ax : axisList )
			{
				Scale scale = ax.getScale( );
				if ( scale != null )
				{
					scale.unsetStep( );
					scale.unsetStepNumber( );
				}
			}
		}
	}

	private void showChart( Dimension dSize )
	{
		// SETUP THE RENDERING CONTEXT
		Bounds bo = BoundsImpl.create( 0, 0, dSize.width, dSize.height );
		bo.scale( 72d / idr.getDisplayServer( ).getDpiResolution( ) );
		idr.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, gc );

		Generator gr = Generator.instance( );
		ChartAdapter.beginIgnoreNotifications( );
		cm.clearSections( IConstants.RUN_TIME ); // REMOVE OLD TRANSIENT
		// RUNTIME SERIES
		cm.createSampleRuntimeSeries( ); // USING SAMPLE DATA STORED IN
		ChartAdapter.endIgnoreNotifications( );

		// MODEL
		try
		{
			Chart cmRunTime = (Chart) EcoreUtil.copy( cm );
			removeStepInfoForSample( cmRunTime );

			RunTimeContext rtc = new RunTimeContext( );
			rtc.setScriptingEnabled( false );
			rtc.setMessageLookup( new BIRTDesignerMessageLookup( crii.getHandle( ) ) );

			// Set direction from model to chart runtime context
			rtc.setRightToLeft( crii.isLayoutDirectionRTL( ) );
			// Set text direction from StyleHandle to chart runtime context
			rtc.setRightToLeftText( crii.getHandle( ).isDirectionRTL( ) );

			rtc.setResourceFinder( crii );
			rtc.setExternalizer( crii );

			// Create shared scale if needed
			boolean bPlotChart = ChartXTabUtil.isPlotChart( crii.getHandle( ) );
			if ( bPlotChart )
			{
				rtc.setScale( createSharedScaleFromSampleData( ) );
			}

			gr.render( idr, gr.build( idr.getDisplayServer( ),
					cmRunTime,
					bo,
					null,
					rtc,
					new ChartReportStyleProcessor( crii.getHandle( ), true ) ) );
		}
		catch ( ChartException gex )
		{
			showException( gc, gex );
		}
	}

	/**
	 * Show the exception message that prevented to draw the chart
	 * 
	 * @param g2d
	 * @param ex
	 *            The exception that occured
	 */
	private final void showException( GC g2d, Exception ex )
	{
		Point pTLC = new Point( 0, 0 );

		// String sWrappedException = ex.getClass( ).getName( );
		Throwable th = ex;

		String sMessage = null;
		if ( th instanceof BirtException )
		{
			sMessage = ( (BirtException) th ).getLocalizedMessage( );
		}
		else
		{
			sMessage = ex.getMessage( );
		}

		if ( sMessage == null )
		{
			sMessage = "<null>"; //$NON-NLS-1$
		}
		// StackTraceElement[] stea = ex.getStackTrace( );
		Dimension d = getSize( );

		Device dv = Display.getCurrent( );
		Font fo = new Font( dv, "Courier", SWT.BOLD, 12 ); //$NON-NLS-1$
		g2d.setFont( fo );
		FontMetrics fm = g2d.getFontMetrics( );
		g2d.setBackground( dv.getSystemColor( SWT.COLOR_WHITE ) );
		g2d.fillRectangle( pTLC.x + 20,
				pTLC.y + 20,
				d.width - 40,
				d.height - 40 );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
		g2d.drawRectangle( pTLC.x + 20,
				pTLC.y + 20,
				d.width - 40,
				d.height - 40 );
		Region rgPrev = new Region( );
		g2d.getClipping( rgPrev );
		g2d.setClipping( pTLC.x + 20, pTLC.y + 20, d.width - 40, d.height - 40 );
		int x = pTLC.x + 25, y = pTLC.y + 20 + fm.getHeight( );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_BLACK ) );
		g2d.drawString( ERROR_MSG, x, y );
		y += fm.getHeight( );
		g2d.setForeground( dv.getSystemColor( SWT.COLOR_RED ) );
		g2d.drawText( sMessage, x, y );

		g2d.setClipping( rgPrev );
		rgPrev.dispose( );
		fo.dispose( );
	}

	/**
	 * 
	 */
	public final void dispose( )
	{
		if ( imgChart != null )
		{
			gc.dispose( );
			imgChart.dispose( );
			idr.dispose( );
			gc = null;
			imgChart = null;
			bDirty = true;
			idr = null;
		}
	}

	private static String[] splitOnBreaks( String s, Font font, double maxSize )
	{
		List<String> al = new ArrayList<String>( );

		// check hard break first
		int i = 0, j;
		do
		{
			j = s.indexOf( '\n', i );

			if ( j == -1 )
			{
				j = s.length( );
			}
			String ss = s.substring( i, j );
			if ( ss != null && ss.length( ) > 0 )
			{
				al.add( ss );
			}

			i = j + 1;

		} while ( j != -1 && j < s.length( ) );

		// check wrapping
		if ( maxSize > 0 )
		{
			TextLayout tl = new TextLayout( Display.getCurrent( ) );
			tl.setFont( font );
			tl.setWidth( (int) maxSize );

			List<String> nal = new ArrayList<String>( );

			for ( Iterator<String> itr = al.iterator( ); itr.hasNext( ); )
			{
				String ns = itr.next( );

				tl.setText( ns );

				int[] offsets = tl.getLineOffsets( );
				String ss;

				for ( i = 1; i < offsets.length; i++ )
				{
					ss = ns.substring( offsets[i - 1], offsets[i] );

					nal.add( ss );
				}
			}

			tl.dispose( );

			al = nal;
		}

		final int n = al.size( );
		if ( n == 1 || n == 0 )
		{
			return null;
		}

		final String[] sa = new String[n];
		for ( i = 0; i < al.size( ); i++ )
		{
			sa[i] = al.get( i );
		}
		return sa;
	}
}