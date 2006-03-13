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

package org.eclipse.birt.chart.render;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.LegendLayoutHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.computation.withoutaxes.Coordinates;
import org.eclipse.birt.chart.computation.withoutaxes.PlotWithoutAxes;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DeferredCache;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.ULocale;

/**
 * Provides a generic framework that initiates the rendering sequence of the
 * various chart components. Series type extensions could subclass this class if
 * they plan on rendering everything for themselves in the plot area.
 */
public abstract class BaseRenderer implements ISeriesRenderer
{

	protected static final String TIMER = "T"; //$NON-NLS-1$

	ISeriesRenderingHints srh;

	private IDisplayServer xs;

	private IDeviceRenderer ir;

	private DeferredCache dc;

	private Chart cm;

	private Object oComputations;

	private Series se;

	private SeriesDefinition sd;

	/**
	 * All renders associated with the chart provided for convenience and
	 * inter-series calculations
	 */
	private BaseRenderer[] brna;

	/**
	 * Identifies the series sequence # in the list of series renders
	 */
	protected transient int iSeriesIndex = -1;

	/**
	 * Identifies the series count in the list of series renders
	 */
	protected transient int iSeriesCount = 1;

	/**
	 * Internally used to simulate a translucent shadow
	 */
	public static final ColorDefinition SHADOW = ColorDefinitionImpl.create( 64,
			64,
			64,
			127 );

	/**
	 * Internally used to darken a tiled image with a translucent dark grey
	 * color
	 */
	protected static final ColorDefinition DARK_GLASS = ColorDefinitionImpl.create( 64,
			64,
			64,
			127 );

	/**
	 * Internally used to brighten a tiled image with a translucent light grey
	 * color
	 */
	protected static final ColorDefinition LIGHT_GLASS = ColorDefinitionImpl.create( 196,
			196,
			196,
			127 );

	/**
	 * Transparency for translucent color. Should between 0 and 100.
	 */
	protected static double OVERRIDE_TRANSPARENCY = 50;

	/**
	 * The associated runtimeContext.
	 */
	private transient RunTimeContext rtc = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/render" ); //$NON-NLS-1$

	/**
	 * The internal constructor that must be defined as public
	 * 
	 * @param _ir
	 * @param _cm
	 */
	public BaseRenderer( )
	{
	}

	/**
	 * @param _cm
	 * @param _o
	 * @param _se
	 * @param _ax
	 * @param _sd
	 */
	public void set( Chart _cm, Object _oComputation, Series _se,
			SeriesDefinition _sd )
	{
		cm = _cm;
		oComputations = _oComputation;
		se = _se;
		sd = _sd;
	}

	/**
	 * @param _dc
	 */
	public final void set( DeferredCache _dc )
	{
		dc = _dc;
	}

	/**
	 * @param _ir
	 */
	public final void set( IDeviceRenderer _ir )
	{
		ir = _ir;
	}

	/**
	 * @param _xs
	 */
	public final void set( IDisplayServer _xs )
	{
		xs = _xs;
	}

	/**
	 * @param _srh
	 */
	public final void set( ISeriesRenderingHints _srh )
	{
		srh = _srh;
	}

	/**
	 * @param _brna
	 */
	public final void set( BaseRenderer[] _brna )
	{
		brna = _brna;
	}

	/**
	 * @param _rtc
	 */
	public final void set( RunTimeContext _rtc )
	{
		rtc = _rtc;
	}

	/**
	 * @return
	 */
	public final ISeriesRenderingHints getSeriesRenderingHints( )
	{
		return srh;
	}

	/**
	 * @return
	 */
	public final IDisplayServer getXServer( )
	{
		return xs;
	}

	/**
	 * @return
	 */
	public final SeriesDefinition getSeriesDefinition( )
	{
		return sd;
	}

	/**
	 * 
	 * @return
	 */
	public final int getSeriesIndex( )
	{
		return iSeriesIndex;
	}

	/**
	 * 
	 * @return
	 */
	public final int getSeriesCount( )
	{
		return iSeriesCount;
	}

	/**
	 * 
	 * @return
	 */
	public final DeferredCache getDeferredCache( )
	{
		return dc;
	}

	/**
	 * Provides access to any other renderer in the group that participates in
	 * chart rendering
	 * 
	 * @param iIndex
	 * @return
	 */
	public final BaseRenderer getRenderer( int iIndex )
	{
		return brna[iIndex];
	}

	/**
	 * @return
	 */
	public final RunTimeContext getRunTimeContext( )
	{
		return rtc;
	}

	/**
	 * Renders all blocks using the appropriate block z-order and the
	 * containment hierarchy.
	 * 
	 * @param bo
	 */
	public void render( Map htRenderers, Bounds bo ) throws ChartException
	{
		final boolean bFirstInSequence = ( iSeriesIndex == 0 );
		final boolean bLastInSequence = ( iSeriesIndex == iSeriesCount - 1 );
		boolean bStarted = bFirstInSequence;

		long lTimer = System.currentTimeMillis( );

		Block bl = cm.getBlock( );
		final Enumeration e = bl.children( true );
		final BlockGenerationEvent bge = new BlockGenerationEvent( this );
		final IDeviceRenderer idr = getDevice( );
		final ScriptHandler sh = getRunTimeContext( ).getScriptHandler( );

		if ( bFirstInSequence )
		{
			// ALWAYS RENDER THE OUTERMOST BLOCK FIRST
			ScriptHandler.callFunction( sh,
					ScriptHandler.BEFORE_DRAW_BLOCK,
					bl,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
					bl );
			bge.updateBlock( bl );
			renderChartBlock( idr, bl, StructureSource.createUnknown( bl ) );
			ScriptHandler.callFunction( sh,
					ScriptHandler.AFTER_DRAW_BLOCK,
					bl,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
					bl );
		}

		// RENDER ALL BLOCKS EXCEPT FOR THE LEGEND IN THIS ITERATIVE LOOP
		while ( e.hasMoreElements( ) )
		{
			bl = (Block) e.nextElement( );
			bge.updateBlock( bl );

			if ( bl instanceof Plot )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
						bl );
				renderPlot( ir, (Plot) bl );
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
						bl );
				if ( bFirstInSequence && !bLastInSequence )
				{
					break;
				}

				if ( !bStarted )
				{
					bStarted = true;
				}
			}
			else if ( bl instanceof TitleBlock && bStarted )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
						bl );
				renderTitle( ir, (TitleBlock) bl );
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
						bl );
			}
			else if ( bl instanceof LabelBlock && bStarted )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
						bl );
				renderLabel( ir, bl, StructureSource.createUnknown( bl ) );
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
						bl );
			}
			else if ( bl instanceof Legend && bStarted && bLastInSequence )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
						bl );
				renderLegend( idr, (Legend) bl, htRenderers );
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
						bl );
			}
			else if ( bStarted )
			{
				ScriptHandler.callFunction( sh,
						ScriptHandler.BEFORE_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_BLOCK,
						bl );
				renderBlock( ir, bl, StructureSource.createUnknown( bl ) );
				ScriptHandler.callFunction( sh,
						ScriptHandler.AFTER_DRAW_BLOCK,
						bl,
						getRunTimeContext( ).getScriptContext( ) );
				getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_BLOCK,
						bl );
			}
		}

		lTimer = System.currentTimeMillis( ) - lTimer;
		if ( htRenderers.containsKey( TIMER ) )
		{
			final Long l = (Long) htRenderers.get( TIMER );
			htRenderers.put( TIMER, new Long( l.longValue( ) + lTimer ) );
		}
		else
		{
			htRenderers.put( TIMER, new Long( lTimer ) );
		}

		if ( bLastInSequence )
		{
			try
			{
				dc.flush( ); // FLUSH DEFERRED CACHE
			}
			catch ( ChartException ex )
			{
				// NOTE: RENDERING EXCEPTION ALREADY BEING THROWN
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						ex );
			}
			logger.log( ILogger.INFORMATION,
					Messages.getString( "info.elapsed.render.time", //$NON-NLS-1$
							new Object[]{
								new Long( lTimer )
							},
							rtc.getULocale( ) ) );
			htRenderers.remove( TIMER );
		}
	}

	/**
	 * Renders the legend block based on the legend rendering rules.
	 * 
	 * @param ipr
	 * @param lg
	 * @param htRenderers
	 * 
	 * @throws RenderingException
	 */
	public void renderLegend( IPrimitiveRenderer ipr, Legend lg, Map htRenderers )
			throws ChartException
	{
		if ( !lg.isVisible( ) ) // CHECK VISIBILITY
		{
			return;
		}

		renderBlock( ipr, lg, StructureSource.createLegend( lg ) );
		final IDisplayServer xs = getDevice( ).getDisplayServer( );
		final double dScale = xs.getDpiResolution( ) / 72d;
		Bounds bo = lg.getBounds( ).scaledInstance( dScale );

		Size sz = null;
		double dX, dY;
		if ( lg.getPosition( ) != Position.INSIDE_LITERAL )
		{
			try
			{
				sz = lg.getPreferredSize( xs, cm, rtc );
			}
			catch ( Exception ex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						ex );
			}
			sz.scale( dScale );

			// USE ANCHOR IN POSITIONING THE LEGEND CLIENT AREA WITHIN THE BLOCK
			// SLACK SPACE
			dX = bo.getLeft( ) + ( bo.getWidth( ) - sz.getWidth( ) ) / 2;
			dY = 0;
			if ( lg.isSetAnchor( ) )
			{
				int iAnchor = lg.getAnchor( ).getValue( );

				// swap west/east
				if ( isRightToLeft( ) )
				{
					if ( iAnchor == Anchor.EAST )
					{
						iAnchor = Anchor.WEST;
					}
					else if ( iAnchor == Anchor.NORTH_EAST )
					{
						iAnchor = Anchor.NORTH_WEST;
					}
					else if ( iAnchor == Anchor.SOUTH_EAST )
					{
						iAnchor = Anchor.SOUTH_WEST;
					}
					else if ( iAnchor == Anchor.WEST )
					{
						iAnchor = Anchor.EAST;
					}
					else if ( iAnchor == Anchor.NORTH_WEST )
					{
						iAnchor = Anchor.NORTH_EAST;
					}
					else if ( iAnchor == Anchor.SOUTH_WEST )
					{
						iAnchor = Anchor.SOUTH_EAST;
					}
				}

				switch ( iAnchor )
				{
					case Anchor.NORTH :
					case Anchor.NORTH_EAST :
					case Anchor.NORTH_WEST :
						dY = bo.getTop( );
						break;

					case Anchor.SOUTH :
					case Anchor.SOUTH_EAST :
					case Anchor.SOUTH_WEST :
						dY = bo.getTop( ) + bo.getHeight( ) - sz.getHeight( );
						break;

					default : // CENTERED
						dY = bo.getTop( )
								+ ( bo.getHeight( ) - sz.getHeight( ) )
								/ 2;
						break;
				}

				switch ( iAnchor )
				{
					case Anchor.WEST :
					case Anchor.NORTH_WEST :
						dX = bo.getLeft( );
						break;

					case Anchor.EAST :
					case Anchor.SOUTH_EAST :
						dX = bo.getLeft( ) + bo.getWidth( ) - sz.getWidth( );
						break;

					default : // CENTERED
						dX = bo.getLeft( )
								+ ( bo.getWidth( ) - sz.getWidth( ) )
								/ 2;
						break;
				}
			}
			else
			{
				dX = bo.getLeft( ) + ( bo.getWidth( ) - sz.getWidth( ) ) / 2;
				dY = bo.getTop( ) + ( bo.getHeight( ) - sz.getHeight( ) ) / 2;
			}
		}
		else
		{
			// USE PREVIOUSLY COMPUTED POSITION IN THE GENERATOR FOR LEGEND
			// 'INSIDE' PLOT
			dX = bo.getLeft( );
			dY = bo.getTop( );
			sz = SizeImpl.create( bo.getWidth( ), bo.getHeight( ) );
		}

		// get cached legend info.
		final LegendLayoutHints lilh = rtc.getLegendLayoutHints( );

		// consider legend title size.
		Label lgTitle = lg.getTitle( );
		double lgTitleWidth = 0, lgTitleHeight = 0;
		double yOffset = 0, xOffset = 0, wOffset = 0, hOffset = 0;

		final boolean bRenderLegendTitle = lgTitle != null
				&& lgTitle.isSetVisible( )
				&& lgTitle.isVisible( );
		int iTitlePos = Position.ABOVE;

		if ( bRenderLegendTitle )
		{
			lgTitle = LabelImpl.copyInstance( lgTitle );

			if ( lilh != null )
			{
				// use cached value
				Size titleSize = lilh.getTitleSize( );

				lgTitleWidth = titleSize.getWidth( );
				lgTitleHeight = titleSize.getHeight( );
			}
			else
			{
				// handle external resource string
				final String sPreviousValue = lgTitle.getCaption( ).getValue( );
				lgTitle.getCaption( )
						.setValue( rtc.externalizedMessage( sPreviousValue ) );

				BoundingBox bb = null;
				try
				{
					bb = Methods.computeBox( xs,
							IConstants.ABOVE,
							lgTitle,
							0,
							0 );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.RENDERING,
							uiex );
				}

				lgTitleWidth = bb.getWidth( );
				lgTitleHeight = bb.getHeight( );
			}

			iTitlePos = lg.getTitlePosition( ).getValue( );

			// swap left/right
			if ( isRightToLeft( ) )
			{
				if ( iTitlePos == Position.LEFT )
				{
					iTitlePos = Position.RIGHT;
				}
				else if ( iTitlePos == Position.RIGHT )
				{
					iTitlePos = Position.LEFT;
				}
			}

			switch ( iTitlePos )
			{
				case Position.ABOVE :
					yOffset = lgTitleHeight;
					hOffset = -yOffset;
					break;
				case Position.BELOW :
					hOffset = -lgTitleHeight;
					break;
				case Position.LEFT :
					xOffset = lgTitleWidth;
					wOffset = -xOffset;
					break;
				case Position.RIGHT :
					wOffset = -lgTitleWidth;
					break;
			}
		}

		// RENDER THE LEGEND CLIENT AREA
		final ClientArea ca = lg.getClientArea( );
		final Insets lgIns = lg.getInsets( ).scaledInstance( dScale );
		LineAttributes lia = ca.getOutline( );
		bo = BoundsImpl.create( dX, dY, sz.getWidth( ), sz.getHeight( ) );
		bo = bo.adjustedInstance( lgIns );
		dX = bo.getLeft( );
		dY = bo.getTop( );

		// Adjust bounds.
		bo.delta( xOffset, yOffset, wOffset, hOffset );
		dX = bo.getLeft( );
		dY = bo.getTop( );

		final double dBaseX = dX;
		final double dBaseY = dY;

		final RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) ir ).getEventObject( StructureSource.createLegend( lg ),
				RectangleRenderEvent.class );

		// render client area shadow.
		if ( ca.getShadowColor( ) != null )
		{
			rre.setBounds( bo.translateInstance( 3, 3 ) );
			rre.setBackground( ca.getShadowColor( ) );
			ipr.fillRectangle( rre );
		}

		// render client area
		rre.setBounds( bo );
		rre.setOutline( lia );
		rre.setBackground( ca.getBackground( ) );
		ipr.fillRectangle( rre );
		ipr.drawRectangle( rre );
		lia = LineAttributesImpl.copyInstance( lia );
		lia.setVisible( true ); // SEPARATOR LINES MUST BE VISIBLE
		
		LineAttributes liSep = lg.getSeparator( ) == null ? lia
				: lg.getSeparator( );

		final SeriesDefinition[] seda = cm.getSeriesForLegend( );

		// INITIALIZATION OF VARS USED IN FOLLOWING LOOPS
		final Orientation o = lg.getOrientation( );
		final Direction d = lg.getDirection( );
		final Label la = LabelImpl.create( );
		la.setCaption( TextImpl.copyInstance( lg.getText( ) ) );
		la.getCaption( ).setValue( "X" ); //$NON-NLS-1$
		final ITextMetrics itm = xs.getTextMetrics( la );

		final double maxWrappingSize = lg.getWrappingSize( );
		final double dItemHeight = itm.getFullHeight( );
		final double dHorizontalSpacing = 4;
		final double dVerticalSpacing = 4;
		double dSeparatorThickness = lia.getThickness( );
		Insets insCA = ca.getInsets( ).scaledInstance( dScale );

		Series seBase;
		ArrayList al;
		LegendItemRenderingHints lirh;
		Palette pa;
		int iPaletteCount;
		EList elPaletteEntries;
		Fill fPaletteEntry;
		final boolean bPaletteByCategory = ( cm.getLegend( )
				.getItemType( )
				.getValue( ) == LegendItemType.CATEGORIES );

		// Get available maximum block width/height.
		Block bl = cm.getBlock( );
		Bounds boFull = bl.getBounds( ).scaledInstance( dScale );
		Insets ins = bl.getInsets( ).scaledInstance( dScale );

		double dMaxX = boFull.getLeft( )
				+ boFull.getWidth( )
				- ins.getRight( )
				- lgIns.getRight( );
		double dMaxY = boFull.getTop( )
				+ boFull.getHeight( )
				- ins.getBottom( )
				- lgIns.getBottom( );

		// Calculate if minSlice applicable.
		String sMinSliceLabel = null;
		boolean bMinSliceApplied = false;

		if ( lilh != null )
		{
			// use cached value.
			sMinSliceLabel = lilh.getMinSliceText( );
			bMinSliceApplied = lilh.isMinSliceApplied( );
		}
		else
		{
			boolean bMinSliceDefined = false;
			double dMinSlice = 0;
			boolean bPercentageMinSlice = false;

			if ( cm instanceof ChartWithoutAxes )
			{
				bMinSliceDefined = ( (ChartWithoutAxes) cm ).isSetMinSlice( );
				dMinSlice = ( (ChartWithoutAxes) cm ).getMinSlice( );
				bPercentageMinSlice = ( (ChartWithoutAxes) cm ).isMinSlicePercent( );
				sMinSliceLabel = ( (ChartWithoutAxes) cm ).getMinSliceLabel( );
				if ( sMinSliceLabel == null || sMinSliceLabel.length( ) == 0 )
				{
					sMinSliceLabel = IConstants.UNDEFINED_STRING;
				}
				else
				{
					sMinSliceLabel = rtc.externalizedMessage( sMinSliceLabel );
				}
			}

			// calculate if need an extra legend item when minSlice defined.
			if ( bMinSliceDefined
					&& bPaletteByCategory
					&& cm instanceof ChartWithoutAxes )
			{
				if ( !( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
						.isEmpty( ) )
				{
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					SeriesDefinition sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.get( 0 );

					SeriesDefinition[] sdOrtho = (SeriesDefinition[]) sdBase.getSeriesDefinitions( )
							.toArray( );

					DataSetIterator dsiOrtho = null;
					double dCurrentMinSlice = 0;

					for ( int i = 0; i < sdOrtho.length && !bMinSliceApplied; i++ )
					{
						try
						{
							dsiOrtho = new DataSetIterator( ( (Series) sdOrtho[i].getRunTimeSeries( )
									.get( 0 ) ).getDataSet( ) );
						}
						catch ( Exception ex )
						{
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.RENDERING,
									ex );
						}

						// TODO Check dataset type, throw exception or ignore?.

						if ( bPercentageMinSlice )
						{
							double total = 0;

							while ( dsiOrtho.hasNext( ) )
							{
								Object obj = dsiOrtho.next( );

								if ( obj instanceof Number )
								{
									total += ( (Number) obj ).doubleValue( );
								}
							}

							dsiOrtho.reset( );

							dCurrentMinSlice = total * dMinSlice / 100d;
						}
						else
						{
							dCurrentMinSlice = dMinSlice;
						}

						while ( dsiOrtho.hasNext( ) )
						{
							Object obj = dsiOrtho.next( );
							if ( obj instanceof Number )
							{
								double val = ( (Number) obj ).doubleValue( );

								if ( val < dCurrentMinSlice )
								{
									bMinSliceApplied = true;
									break;
								}
							}
						}
					}
				}
			}
		}

		// COMPUTATIONS HERE MUST BE IN SYNC WITH THE ACTUAL RENDERER
		if ( o.getValue( ) == Orientation.VERTICAL )
		{
			double dXOffset = 0, dMaxW = 0;

			if ( bPaletteByCategory )
			{
				SeriesDefinition sdBase = null;
				FormatSpecifier fs = null;
				if ( cm instanceof ChartWithAxes )
				{
					// ONLY SUPPORT 1 BASE AXIS FOR NOW
					final Axis axPrimaryBase = ( (ChartWithAxes) cm ).getBaseAxes( )[0];
					if ( axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
					{
						// NOTHING TO RENDER (BASE AXIS HAS NO SERIES
						// DEFINITIONS)
						return;
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
							.get( 0 );
				}
				else if ( cm instanceof ChartWithoutAxes )
				{
					if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.isEmpty( ) )
					{
						// NOTHING TO RENDER (BASE AXIS HAS NO SERIES
						// DEFINITIONS)
						return;
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.get( 0 );
				}

				// OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
				seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 );
				pa = sdBase.getSeriesPalette( );
				elPaletteEntries = pa.getEntries( );
				iPaletteCount = elPaletteEntries.size( );

				if ( lilh != null && lilh.getLegendItemHints( ) != null )
				{
					// use cached value
					LegendItemHints[] liha = lilh.getLegendItemHints( );
					LegendItemHints lih;

					Map columnCache = searchMaxColumnWidth( liha );

					for ( int i = 0; i < liha.length; i++ )
					{
						// render each legend item.
						lih = liha[i];

						if ( ( lih.getType( ) & IConstants.LEGEND_ENTRY ) == IConstants.LEGEND_ENTRY )
						{
							la.getCaption( ).setValue( lih.getText( ) );

							// CYCLE THROUGH THE PALETTE
							fPaletteEntry = (Fill) elPaletteEntries.get( lih.getCategoryIndex( )
									% iPaletteCount );

							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

							double columnWidth = bo.getWidth( );
							Double cachedWidth = (Double) columnCache.get( lih );
							if ( cachedWidth != null )
							{
								columnWidth = cachedWidth.doubleValue( )
										+ 3
										* dItemHeight
										/ 2
										+ 2
										* insCA.getLeft( );
							}

							renderLegendItem( ipr,
									lg,
									la,
									null,
									dBaseX + lih.getLeft( ),
									dBaseY + lih.getTop( ) + insCA.getTop( ),
									lih.getWidth( ),
									dItemHeight,
									lih.getHeight( ),
									0,
									columnWidth,
									insCA.getLeft( ),
									dHorizontalSpacing,
									seBase,
									fPaletteEntry,
									lirh );
						}
					}
				}
				else
				{
					DataSetIterator dsiBase = null;
					try
					{
						dsiBase = new DataSetIterator( seBase.getDataSet( ) );
					}
					catch ( Exception ex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								ex );
					}

					if ( sdBase != null )
					{
						fs = sdBase.getFormatSpecifier( );
					}

					int i = 0;
					while ( dsiBase.hasNext( ) )
					{
						Object obj = dsiBase.next( );

						// TODO filter the not-used legend.

						// render legend item.
						dY += insCA.getTop( );
						String lgtext = String.valueOf( obj );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( obj,
										fs,
										getRunTimeContext( ).getULocale( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize ); // RECYCLED
						fPaletteEntry = (Fill) elPaletteEntries.get( i++
								% iPaletteCount ); // CYCLE THROUGH THE PALETTE
						lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

						if ( dY + itm.getFullHeight( ) + insCA.getBottom( ) > dMaxY )
						{
							dXOffset += dMaxW
									+ insCA.getLeft( )
									+ insCA.getRight( )
									+ ( 3 * dItemHeight )
									/ 2
									+ dHorizontalSpacing;
							dMaxW = 0;
							dY = bo.getTop( ) + insCA.getTop( );
							dX = bo.getLeft( ) + dXOffset;
						}

						dMaxW = Math.max( dMaxW, itm.getFullWidth( ) );

						renderLegendItem( ipr,
								lg,
								la,
								null,
								dX,
								dY,
								itm.getFullWidth( ),
								dItemHeight,
								itm.getFullHeight( ),
								0,
								bo.getWidth( ),
								insCA.getLeft( ),
								dHorizontalSpacing,
								seBase,
								fPaletteEntry,
								lirh );

						dY += itm.getFullHeight( ) + insCA.getBottom( );
					}

					// render the extra MinSlice legend item if applicable.
					if ( bMinSliceApplied )
					{
						dY += insCA.getTop( );
						la.getCaption( ).setValue( sMinSliceLabel );
						itm.reuse( la, maxWrappingSize ); // RECYCLED
						fPaletteEntry = (Fill) elPaletteEntries.get( dsiBase.size( )
								% iPaletteCount ); // CYCLE THROUGH THE PALETTE
						lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

						if ( dY + itm.getFullHeight( ) + insCA.getBottom( ) > dMaxY )
						{
							dXOffset += dMaxW
									+ insCA.getLeft( )
									+ insCA.getRight( )
									+ ( 3 * dItemHeight )
									/ 2
									+ dHorizontalSpacing;
							dMaxW = 0;
							dY = bo.getTop( ) + insCA.getTop( );
							dX = bo.getLeft( ) + dXOffset;
						}

						dMaxW = Math.max( dMaxW, itm.getFullWidth( ) );

						renderLegendItem( ipr,
								lg,
								la,
								null,
								dX,
								dY,
								itm.getFullWidth( ),
								dItemHeight,
								itm.getFullHeight( ),
								0,
								bo.getWidth( ),
								insCA.getLeft( ),
								dHorizontalSpacing,
								seBase,
								fPaletteEntry,
								lirh );
						dY += itm.getFullHeight( ) + insCA.getBottom( );
					}
				}
			}
			else if ( d.getValue( ) == Direction.TOP_BOTTOM )
			{
				if ( lilh != null && lilh.getLegendItemHints( ) != null )
				{
					LegendItemHints[] liha = lilh.getLegendItemHints( );
					LegendItemHints lih;
					int k = 0;

					Map columnCache = searchMaxColumnWidth( liha );

					for ( int j = 0; j < seda.length; j++ )
					{
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

							lih = liha[k++];

							if ( lih.getType( ) == IConstants.LEGEND_ENTRY )
							{
								la.getCaption( ).setValue( lih.getText( ) );

								Label valueLa = null;
								if ( lg.isShowValue( ) )
								{
									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( )
											.setValue( lih.getExtraText( ) );
								}

								// CYCLE THROUGH THE PALETTE
								fPaletteEntry = (Fill) elPaletteEntries.get( i
										% iPaletteCount );

								double columnWidth = bo.getWidth( );
								Double cachedWidth = (Double) columnCache.get( lih );
								if ( cachedWidth != null )
								{
									columnWidth = cachedWidth.doubleValue( )
											+ 3
											* dItemHeight
											/ 2
											+ 2
											* insCA.getLeft( );
								}

								renderLegendItem( ipr,
										lg,
										la,
										valueLa,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ) + insCA.getTop( ),
										lih.getWidth( ),
										dItemHeight,
										lih.getHeight( ),
										lih.getExtraHeight( ),
										columnWidth,
										insCA.getLeft( ),
										dHorizontalSpacing,
										seBase,
										fPaletteEntry,
										lirh );
							}
						}

						if ( j < seda.length - 1 )
						{
							lih = liha[k];

							if ( lih.getType( ) == IConstants.LEGEND_SEPERATOR )
							{
								k++;
								renderSeparator( ipr,
										lg,
										liSep,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ),
										lih.getWidth( ),
										Orientation.HORIZONTAL_LITERAL );
							}
						}
					}
				}
				else
				{
					dSeparatorThickness += dVerticalSpacing;

					for ( int j = 0; j < seda.length; j++ )
					{
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						FormatSpecifier fs = seda[j].getFormatSpecifier( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

							dY += insCA.getTop( );
							Object obj = seBase.getSeriesIdentifier( );
							String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
							if ( fs != null )
							{
								try
								{
									lgtext = ValueFormatter.format( obj,
											fs,
											getRunTimeContext( ).getULocale( ),
											null );
								}
								catch ( ChartException e )
								{
									// ignore, use original text.
								}
							}
							la.getCaption( ).setValue( lgtext );
							itm.reuse( la, maxWrappingSize ); // RECYCLED

							double dFWidth = itm.getFullWidth( );
							double dFHeight = itm.getFullHeight( );

							Label valueLa = null;
							double valueHeight = 0;

							if ( lg.isShowValue( ) )
							{
								DataSetIterator dsiBase = null;
								try
								{
									dsiBase = new DataSetIterator( seBase.getDataSet( ) );
								}
								catch ( Exception ex )
								{
									throw new ChartException( ChartEnginePlugin.ID,
											ChartException.GENERATION,
											ex );
								}

								// Use first value for each series.
								if ( dsiBase.hasNext( ) )
								{
									obj = dsiBase.next( );
									String valueText = String.valueOf( obj );
									if ( fs != null )
									{
										try
										{
											lgtext = ValueFormatter.format( obj,
													fs,
													ULocale.getDefault( ),
													null );
										}
										catch ( ChartException e )
										{
											// ignore, use original text.
										}
									}

									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( ).setValue( valueText );
									itm.reuse( valueLa );

									dFWidth = Math.max( dFWidth,
											itm.getFullWidth( ) );

									valueHeight = itm.getFullHeight( );
								}
							}

							if ( dY
									+ dFHeight
									+ valueHeight
									+ 2
									+ insCA.getBottom( ) > dMaxY )
							{
								dXOffset += dMaxW
										+ insCA.getLeft( )
										+ insCA.getRight( )
										+ ( 3 * dItemHeight )
										/ 2
										+ dHorizontalSpacing;
								dMaxW = 0;
								dY = bo.getTop( ) + insCA.getTop( );
								dX = bo.getLeft( ) + dXOffset;
							}

							dMaxW = Math.max( dMaxW, dFWidth );

							fPaletteEntry = (Fill) elPaletteEntries.get( i
									% iPaletteCount ); // CYCLE THROUGH THE
							// PALETTE
							renderLegendItem( ipr,
									lg,
									la,
									valueLa,
									dX,
									dY,
									dFWidth,
									dItemHeight,
									dFHeight,
									valueHeight,
									bo.getWidth( ),
									insCA.getLeft( ),
									dHorizontalSpacing,
									seBase,
									fPaletteEntry,
									lirh );
							dY += dFHeight
									+ insCA.getBottom( )
									+ valueHeight
									+ 2;
						}

						if ( j < seda.length - 1 )
						{
							renderSeparator( ipr, lg, liSep, dX, dY
									+ dSeparatorThickness
									/ 2, dMaxW
									+ insCA.getLeft( )
									+ insCA.getRight( )
									+ ( 3 * dItemHeight )
									/ 2, Orientation.HORIZONTAL_LITERAL );
							dY += dSeparatorThickness;
						}
					}
				}
			}
			else if ( d.getValue( ) == Direction.LEFT_RIGHT )
			{
				if ( lilh != null && lilh.getLegendItemHints( ) != null )
				{
					LegendItemHints[] liha = lilh.getLegendItemHints( );
					LegendItemHints lih;
					int k = 0;

					Map columnCache = searchMaxColumnWidth( liha );

					for ( int j = 0; j < seda.length; j++ )
					{
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

							lih = liha[k++];

							if ( lih.getType( ) == IConstants.LEGEND_ENTRY )
							{
								la.getCaption( ).setValue( lih.getText( ) );

								Label valueLa = null;
								if ( lg.isShowValue( ) )
								{
									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( )
											.setValue( lih.getExtraText( ) );
								}

								// CYCLE THROUGH THE PALETTE
								fPaletteEntry = (Fill) elPaletteEntries.get( i
										% iPaletteCount );

								double columnWidth = bo.getWidth( );
								Double cachedWidth = (Double) columnCache.get( lih );
								if ( cachedWidth != null )
								{
									columnWidth = cachedWidth.doubleValue( )
											+ 3
											* dItemHeight
											/ 2
											+ 2
											* insCA.getLeft( );
								}

								renderLegendItem( ipr,
										lg,
										la,
										valueLa,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ) + insCA.getTop( ),
										lih.getWidth( ),
										dItemHeight,
										lih.getHeight( ),
										lih.getExtraHeight( ),
										columnWidth,
										insCA.getLeft( ),
										dHorizontalSpacing,
										seBase,
										fPaletteEntry,
										lirh );
							}
						}

						if ( j < seda.length - 1 )
						{
							lih = liha[k];

							if ( lih.getType( ) == IConstants.LEGEND_SEPERATOR )
							{
								k++;
								renderSeparator( ipr,
										lg,
										liSep,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ),
										bo.getHeight( ),
										Orientation.VERTICAL_LITERAL );
							}
						}
					}
				}
				else
				{
					dSeparatorThickness += dHorizontalSpacing;

					for ( int j = 0; j < seda.length; j++ )
					{
						dMaxW = 0;
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						FormatSpecifier fs = seda[j].getFormatSpecifier( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							dY += insCA.getTop( );
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );
							Object obj = seBase.getSeriesIdentifier( );
							String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
							if ( fs != null )
							{
								try
								{
									lgtext = ValueFormatter.format( obj,
											fs,
											getRunTimeContext( ).getULocale( ),
											null );
								}
								catch ( ChartException e )
								{
									// ignore, use original text.
								}
							}
							la.getCaption( ).setValue( lgtext );
							itm.reuse( la, maxWrappingSize ); // RECYCLED

							double dFWidth = itm.getFullWidth( );
							double dFHeight = itm.getFullHeight( );

							Label valueLa = null;
							double valueHeight = 0;

							if ( lg.isShowValue( ) )
							{
								DataSetIterator dsiBase = null;
								try
								{
									dsiBase = new DataSetIterator( seBase.getDataSet( ) );
								}
								catch ( Exception ex )
								{
									throw new ChartException( ChartEnginePlugin.ID,
											ChartException.GENERATION,
											ex );
								}

								// Use first value for each series.
								if ( dsiBase.hasNext( ) )
								{
									obj = dsiBase.next( );
									String valueText = String.valueOf( obj );
									if ( fs != null )
									{
										try
										{
											lgtext = ValueFormatter.format( obj,
													fs,
													ULocale.getDefault( ),
													null );
										}
										catch ( ChartException e )
										{
											// ignore, use original text.
										}
									}

									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( ).setValue( valueText );
									itm.reuse( valueLa );

									dFWidth = Math.max( dFWidth,
											itm.getFullWidth( ) );

									valueHeight = itm.getFullHeight( );
								}
							}

							if ( dY
									+ dFHeight
									+ valueHeight
									+ 2
									+ insCA.getBottom( ) > dMaxY )
							{
								dXOffset += dMaxW
										+ insCA.getLeft( )
										+ insCA.getRight( )
										+ ( 3 * dItemHeight )
										/ 2
										+ dHorizontalSpacing;
								dMaxW = 0;
								dY = bo.getTop( );
								dX = bo.getLeft( ) + dXOffset;
							}

							dMaxW = Math.max( dMaxW, dFWidth );

							fPaletteEntry = (Fill) elPaletteEntries.get( i
									% iPaletteCount ); // CYCLE THROUGH THE
							// PALETTE
							renderLegendItem( ipr,
									lg,
									la,
									valueLa,
									dX,
									dY,
									dFWidth,
									dItemHeight,
									dFHeight,
									valueHeight,
									bo.getWidth( ),
									insCA.getLeft( ),
									dHorizontalSpacing,
									seBase,
									fPaletteEntry,
									lirh );
							dY += dFHeight
									+ insCA.getBottom( )
									+ valueHeight
									+ 2;
						}

						// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL SPACING
						// +
						// MAX ITEM WIDTH + RIGHT INSETS
						dXOffset += insCA.getLeft( )
								+ ( 3 * dItemHeight / 2 )
								+ dHorizontalSpacing
								+ dMaxW
								+ insCA.getRight( );
						dX += insCA.getLeft( )
								+ ( 3 * dItemHeight / 2 )
								+ dHorizontalSpacing
								+ dMaxW
								+ insCA.getRight( );
						dY = bo.getTop( );

						// SETUP VERTICAL SEPARATOR SPACING
						if ( j < seda.length - 1 )
						{
							renderSeparator( ipr,
									lg,
									liSep,
									dX + dSeparatorThickness / 2,
									dY,
									bo.getHeight( ),
									Orientation.VERTICAL_LITERAL );
							dX += dSeparatorThickness;
						}
					}
				}
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						"exception.illegal.legend.direction", //$NON-NLS-1$
						new Object[]{
							d.getName( )
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
		}
		else if ( o.getValue( ) == Orientation.HORIZONTAL )
		{
			double dYOffset = 0, dMaxH = 0;

			if ( bPaletteByCategory )
			{
				SeriesDefinition sdBase = null;
				FormatSpecifier fs = null;
				if ( cm instanceof ChartWithAxes )
				{
					// ONLY SUPPORT 1 BASE AXIS FOR NOW
					final Axis axPrimaryBase = ( (ChartWithAxes) cm ).getBaseAxes( )[0];
					if ( axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
					{
						// NOTHING TO RENDER (BASE AXIS HAS NO SERIES
						// DEFINITIONS)
						return;
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
							.get( 0 );
				}
				else if ( cm instanceof ChartWithoutAxes )
				{
					if ( ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.isEmpty( ) )
					{
						// NOTHING TO RENDER (BASE AXIS HAS NO SERIES
						// DEFINITIONS)
						return;
					}
					// OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
					sdBase = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
							.get( 0 );
				}
				// OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
				seBase = (Series) sdBase.getRunTimeSeries( ).get( 0 );
				pa = sdBase.getSeriesPalette( );
				elPaletteEntries = pa.getEntries( );
				iPaletteCount = elPaletteEntries.size( );

				if ( lilh != null && lilh.getLegendItemHints( ) != null )
				{
					// use cached value
					LegendItemHints[] liha = lilh.getLegendItemHints( );
					LegendItemHints lih;

					for ( int i = 0; i < liha.length; i++ )
					{
						// render each legend item.
						lih = liha[i];

						if ( ( lih.getType( ) & IConstants.LEGEND_ENTRY ) == IConstants.LEGEND_ENTRY )
						{
							la.getCaption( ).setValue( lih.getText( ) );

							// CYCLE THROUGH THE PALETTE
							fPaletteEntry = (Fill) elPaletteEntries.get( lih.getCategoryIndex( )
									% iPaletteCount );

							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

							renderLegendItem( ipr,
									lg,
									la,
									null,
									dBaseX + lih.getLeft( ),
									dBaseY + lih.getTop( ) + insCA.getTop( ),
									lih.getWidth( ),
									dItemHeight,
									lih.getHeight( ),
									0,
									lih.getWidth( )
											+ 3
											* dItemHeight
											/ 2
											+ 2
											* insCA.getLeft( ),
									insCA.getLeft( ),
									dHorizontalSpacing,
									seBase,
									fPaletteEntry,
									lirh );
						}
					}
				}
				else
				{
					DataSetIterator dsiBase = null;
					try
					{
						dsiBase = new DataSetIterator( seBase.getDataSet( ) );
					}
					catch ( Exception ex )
					{
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.RENDERING,
								ex );
					}

					if ( sdBase != null )
					{
						fs = sdBase.getFormatSpecifier( );
					}

					int i = 0;
					double dFullWidth = 0;
					dY += insCA.getTop( );
					while ( dsiBase.hasNext( ) )
					{
						Object obj = dsiBase.next( );

						// TODO filter the not-used legend.

						dX += insCA.getLeft( );
						String lgtext = String.valueOf( obj );
						if ( fs != null )
						{
							try
							{
								lgtext = ValueFormatter.format( obj,
										fs,
										getRunTimeContext( ).getULocale( ),
										null );
							}
							catch ( ChartException e )
							{
								// ignore, use original text.
							}
						}
						la.getCaption( ).setValue( lgtext );
						itm.reuse( la, maxWrappingSize ); // RECYCLED
						fPaletteEntry = (Fill) elPaletteEntries.get( i++
								% iPaletteCount ); // CYCLE THROUGH THE PALETTE
						lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

						dFullWidth = itm.getFullWidth( );

						if ( dX
								+ dFullWidth
								+ ( 3 * dItemHeight )
								/ 2
								+ insCA.getRight( ) > dMaxX )
						{
							dYOffset += dMaxH
									+ insCA.getTop( )
									+ insCA.getBottom( )
									+ dVerticalSpacing;
							dMaxH = 0;
							dX = bo.getLeft( ) + insCA.getLeft( );
							dY = bo.getTop( ) + insCA.getTop( ) + dYOffset;
						}

						dMaxH = Math.max( dMaxH, itm.getFullHeight( ) );

						renderLegendItem( ipr,
								lg,
								la,
								null,
								dX,
								dY,
								dFullWidth,
								dItemHeight,
								itm.getFullHeight( ),
								0,
								dFullWidth
										+ 3
										* dItemHeight
										/ 2
										+ 2
										* insCA.getLeft( ),
								insCA.getLeft( ),
								dHorizontalSpacing,
								seBase,
								fPaletteEntry,
								lirh );
						dX += dFullWidth
								+ ( 3 * dItemHeight )
								/ 2
								+ insCA.getRight( );
					}

					// render the extra MinSlice legend item if applicable.
					if ( bMinSliceApplied )
					{
						dX += insCA.getLeft( );
						la.getCaption( ).setValue( sMinSliceLabel );
						itm.reuse( la, maxWrappingSize ); // RECYCLED
						fPaletteEntry = (Fill) elPaletteEntries.get( dsiBase.size( )
								% iPaletteCount ); // CYCLE THROUGH THE PALETTE
						lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

						dFullWidth = itm.getFullWidth( );
						if ( dX
								+ dFullWidth
								+ ( 3 * dItemHeight )
								/ 2
								+ insCA.getRight( ) > dMaxX )
						{
							dYOffset += dMaxH
									+ insCA.getTop( )
									+ insCA.getBottom( )
									+ dVerticalSpacing;
							dMaxH = 0;
							dX = bo.getLeft( ) + insCA.getLeft( );
							dY = bo.getTop( ) + insCA.getTop( ) + dYOffset;
						}

						dMaxH = Math.max( dMaxH, itm.getFullHeight( ) );

						renderLegendItem( ipr,
								lg,
								la,
								null,
								dX,
								dY,
								dFullWidth,
								dItemHeight,
								itm.getFullHeight( ),
								0,
								dFullWidth
										+ 3
										* dItemHeight
										/ 2
										+ 2
										* insCA.getLeft( ),
								insCA.getLeft( ),
								dHorizontalSpacing,
								seBase,
								fPaletteEntry,
								lirh );
						dX += dFullWidth
								+ ( 3 * dItemHeight )
								/ 2
								+ insCA.getRight( );
					}
				}
			}
			else if ( d.getValue( ) == Direction.TOP_BOTTOM )
			{
				if ( lilh != null && lilh.getLegendItemHints( ) != null )
				{
					LegendItemHints[] liha = lilh.getLegendItemHints( );
					LegendItemHints lih;
					int k = 0;

					for ( int j = 0; j < seda.length; j++ )
					{
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

							lih = liha[k++];

							if ( lih.getType( ) == IConstants.LEGEND_ENTRY )
							{
								la.getCaption( ).setValue( lih.getText( ) );

								Label valueLa = null;
								if ( lg.isShowValue( ) )
								{
									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( )
											.setValue( lih.getExtraText( ) );
								}

								// CYCLE THROUGH THE PALETTE
								fPaletteEntry = (Fill) elPaletteEntries.get( i
										% iPaletteCount );
								renderLegendItem( ipr,
										lg,
										la,
										valueLa,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ) + insCA.getTop( ),
										lih.getWidth( ),
										dItemHeight,
										lih.getHeight( ),
										lih.getExtraHeight( ),
										lih.getWidth( )
												+ 3
												* dItemHeight
												/ 2
												+ 2
												* insCA.getLeft( ),
										insCA.getLeft( ),
										dHorizontalSpacing,
										seBase,
										fPaletteEntry,
										lirh );
							}
						}

						if ( j < seda.length - 1 )
						{
							lih = liha[k];

							if ( lih.getType( ) == IConstants.LEGEND_SEPERATOR )
							{
								k++;
								renderSeparator( ipr,
										lg,
										liSep,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ),
										bo.getWidth( ),
										Orientation.HORIZONTAL_LITERAL );
							}
						}
					}
				}
				else
				{
					dSeparatorThickness += dVerticalSpacing;
					for ( int j = 0; j < seda.length; j++ )
					{
						dMaxH = 0;
						dY += insCA.getTop( );
						dX = bo.getLeft( ) + insCA.getLeft( );
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						FormatSpecifier fs = seda[j].getFormatSpecifier( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );
							Object obj = seBase.getSeriesIdentifier( );
							String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
							if ( fs != null )
							{
								try
								{
									lgtext = ValueFormatter.format( obj,
											fs,
											getRunTimeContext( ).getULocale( ),
											null );
								}
								catch ( ChartException e )
								{
									// ignore, use original text.
								}
							}
							la.getCaption( ).setValue( lgtext );
							itm.reuse( la, maxWrappingSize ); // RECYCLED

							double dFWidth = itm.getFullWidth( );
							double dFHeight = itm.getFullHeight( );

							Label valueLa = null;
							double valueHeight = 0;

							if ( lg.isShowValue( ) )
							{
								DataSetIterator dsiBase = null;
								try
								{
									dsiBase = new DataSetIterator( seBase.getDataSet( ) );
								}
								catch ( Exception ex )
								{
									throw new ChartException( ChartEnginePlugin.ID,
											ChartException.GENERATION,
											ex );
								}

								// Use first value for each series.
								if ( dsiBase.hasNext( ) )
								{
									obj = dsiBase.next( );
									String valueText = String.valueOf( obj );
									if ( fs != null )
									{
										try
										{
											lgtext = ValueFormatter.format( obj,
													fs,
													ULocale.getDefault( ),
													null );
										}
										catch ( ChartException e )
										{
											// ignore, use original text.
										}
									}

									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( ).setValue( valueText );
									itm.reuse( valueLa );

									dFWidth = Math.max( dFWidth,
											itm.getFullWidth( ) );

									valueHeight = itm.getFullHeight( );
								}
							}

							if ( dX
									+ insCA.getLeft( )
									+ dFWidth
									+ ( 3 * dItemHeight )
									/ 2
									+ insCA.getRight( ) > dMaxX )
							{
								dYOffset += dMaxH
										+ insCA.getTop( )
										+ insCA.getBottom( )
										+ dVerticalSpacing;
								dMaxH = 0;
								dX = bo.getLeft( ) + insCA.getLeft( );
								dY = bo.getTop( ) + insCA.getTop( ) + dYOffset;
							}

							dMaxH = Math.max( dMaxH, dFHeight + valueHeight + 2 );

							fPaletteEntry = (Fill) elPaletteEntries.get( i
									% iPaletteCount ); // CYCLE THROUGH THE
							// PALETTE
							renderLegendItem( ipr,
									lg,
									la,
									valueLa,
									dX,
									dY,
									dFWidth,
									dItemHeight,
									dFHeight,
									valueHeight,
									dFWidth
											+ 3
											* dItemHeight
											/ 2
											+ 2
											* insCA.getLeft( ),
									insCA.getLeft( ),
									dHorizontalSpacing,
									seBase,
									fPaletteEntry,
									lirh );

							// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL
							// SPACING
							// + MAX ITEM WIDTH + RIGHT INSETS
							dX += insCA.getLeft( )
									+ ( 3 * dItemHeight )
									/ 2
									+ dFWidth
									+ insCA.getRight( );
						}

						dYOffset += insCA.getTop( )
								+ insCA.getBottom( )
								+ dMaxH
								+ dVerticalSpacing;
						dY += insCA.getTop( )
								+ insCA.getBottom( )
								+ dMaxH
								+ dVerticalSpacing;
						dX = bo.getLeft( ) + insCA.getLeft( );

						// SETUP HORIZONTAL SEPARATOR SPACING
						if ( j < seda.length - 1 )
						{
							renderSeparator( ipr,
									lg,
									liSep,
									dX,
									dY + dSeparatorThickness / 2,
									bo.getWidth( ),
									Orientation.HORIZONTAL_LITERAL );
							dY += dSeparatorThickness;
						}
					}
				}
			}
			else if ( d.getValue( ) == Direction.LEFT_RIGHT )
			{
				if ( lilh != null && lilh.getLegendItemHints( ) != null )
				{
					LegendItemHints[] liha = lilh.getLegendItemHints( );
					LegendItemHints lih;
					int k = 0;

					for ( int j = 0; j < seda.length; j++ )
					{
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );

							lih = liha[k++];

							if ( lih.getType( ) == IConstants.LEGEND_ENTRY )
							{
								la.getCaption( ).setValue( lih.getText( ) );

								Label valueLa = null;
								if ( lg.isShowValue( ) )
								{
									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( )
											.setValue( lih.getExtraText( ) );
								}

								// CYCLE THROUGH THE PALETTE
								fPaletteEntry = (Fill) elPaletteEntries.get( i
										% iPaletteCount );
								renderLegendItem( ipr,
										lg,
										la,
										valueLa,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ) + insCA.getTop( ),
										lih.getWidth( ),
										dItemHeight,
										lih.getHeight( ),
										lih.getExtraHeight( ),
										lih.getWidth( )
												+ 3
												* dItemHeight
												/ 2
												+ 2
												* insCA.getLeft( ),
										insCA.getLeft( ),
										dHorizontalSpacing,
										seBase,
										fPaletteEntry,
										lirh );
							}
						}

						if ( j < seda.length - 1 )
						{
							lih = liha[k];

							if ( lih.getType( ) == IConstants.LEGEND_SEPERATOR )
							{
								k++;
								renderSeparator( ipr,
										lg,
										liSep,
										dBaseX + lih.getLeft( ),
										dBaseY + lih.getTop( ),
										lih.getHeight( ),
										Orientation.VERTICAL_LITERAL );
							}
						}
					}
				}
				else
				{
					dSeparatorThickness += dHorizontalSpacing;
					dX += insCA.getLeft( );
					dY += insCA.getTop( );
					for ( int j = 0; j < seda.length; j++ )
					{
						al = seda[j].getRunTimeSeries( );
						pa = seda[j].getSeriesPalette( );
						elPaletteEntries = pa.getEntries( );
						iPaletteCount = elPaletteEntries.size( );

						FormatSpecifier fs = seda[j].getFormatSpecifier( );

						for ( int i = 0; i < al.size( ); i++ )
						{
							seBase = (Series) al.get( i );
							lirh = (LegendItemRenderingHints) htRenderers.get( seBase );
							Object obj = seBase.getSeriesIdentifier( );
							String lgtext = rtc.externalizedMessage( String.valueOf( obj ) );
							if ( fs != null )
							{
								try
								{
									lgtext = ValueFormatter.format( obj,
											fs,
											getRunTimeContext( ).getULocale( ),
											null );
								}
								catch ( ChartException e )
								{
									// ignore, use original text.
								}
							}
							la.getCaption( ).setValue( lgtext );
							itm.reuse( la, maxWrappingSize ); // RECYCLED

							double dFWidth = itm.getFullWidth( );
							double dFHeight = itm.getFullHeight( );

							Label valueLa = null;
							double valueHeight = 0;

							if ( lg.isShowValue( ) )
							{
								DataSetIterator dsiBase = null;
								try
								{
									dsiBase = new DataSetIterator( seBase.getDataSet( ) );
								}
								catch ( Exception ex )
								{
									throw new ChartException( ChartEnginePlugin.ID,
											ChartException.GENERATION,
											ex );
								}

								// Use first value for each series.
								if ( dsiBase.hasNext( ) )
								{
									obj = dsiBase.next( );
									String valueText = String.valueOf( obj );
									if ( fs != null )
									{
										try
										{
											lgtext = ValueFormatter.format( obj,
													fs,
													ULocale.getDefault( ),
													null );
										}
										catch ( ChartException e )
										{
											// ignore, use original text.
										}
									}

									valueLa = LabelImpl.copyInstance( seBase.getLabel( ) );
									valueLa.getCaption( ).setValue( valueText );
									itm.reuse( valueLa );

									dFWidth = Math.max( dFWidth,
											itm.getFullWidth( ) );

									valueHeight = itm.getFullHeight( );
								}
							}

							if ( dX
									+ insCA.getLeft( )
									+ dFWidth
									+ ( 3 * dItemHeight )
									/ 2
									+ insCA.getRight( ) > dMaxX )
							{
								dYOffset += dMaxH
										+ insCA.getTop( )
										+ insCA.getBottom( )
										+ dVerticalSpacing;
								dMaxH = 0;
								dX = bo.getLeft( ) + insCA.getLeft( );
								dY = bo.getTop( ) + dYOffset;
							}

							dMaxH = Math.max( dMaxH, dFHeight + valueHeight + 2 );

							fPaletteEntry = (Fill) elPaletteEntries.get( i
									% iPaletteCount ); // CYCLE THROUGH THE
							// PALETTE
							renderLegendItem( ipr,
									lg,
									la,
									valueLa,
									dX,
									dY,
									dFWidth,
									dItemHeight,
									dFHeight,
									valueHeight,
									dFWidth
											+ 3
											* dItemHeight
											/ 2
											+ 2
											* insCA.getLeft( ),
									insCA.getLeft( ),
									dHorizontalSpacing,
									seBase,
									fPaletteEntry,
									lirh );

							// LEFT INSETS + LEGEND ITEM WIDTH + HORIZONTAL
							// SPACING
							// + MAX ITEM WIDTH + RIGHT INSETS
							dX += insCA.getLeft( )
									+ ( 3 * dItemHeight )
									/ 2
									+ dHorizontalSpacing
									+ dFWidth
									+ insCA.getRight( );
						}

						// SETUP VERTICAL SEPARATOR SPACING
						if ( j < seda.length - 1 )
						{
							renderSeparator( ipr,
									lg,
									liSep,
									dX + dSeparatorThickness / 2,
									dY,
									dMaxH,
									Orientation.VERTICAL_LITERAL );
							dX += dSeparatorThickness;
						}
					}
				}
			}
			else
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						"exception.illegal.legend.direction", //$NON-NLS-1$
						new Object[]{
							d.getName( )
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) ); // i18n_CONCATENATIONS_REMOVED
			}
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.illegal.legend.orientation", //$NON-NLS-1$ 
					new Object[]{
						o.getName( )
					},
					ResourceBundle.getBundle( Messages.ENGINE, rtc.getLocale( ) ) );
		}

		// Render legend title if defined.
		if ( bRenderLegendTitle )
		{
			double lX = bo.getLeft( );
			double lY = bo.getTop( );

			switch ( iTitlePos )
			{
				case Position.ABOVE :
					lX = bo.getLeft( ) + ( bo.getWidth( ) - lgTitleWidth ) / 2d;
					lY = bo.getTop( ) - lgTitleHeight;
					break;
				case Position.BELOW :
					lX = bo.getLeft( ) + ( bo.getWidth( ) - lgTitleWidth ) / 2d;
					lY = bo.getTop( ) + bo.getHeight( );
					break;
				case Position.LEFT :
					lX = bo.getLeft( ) - lgTitleWidth;
					lY = bo.getTop( )
							+ ( bo.getHeight( ) - lgTitleHeight )
							/ 2d;
					break;
				case Position.RIGHT :
					lX = bo.getLeft( ) + bo.getWidth( );
					lY = bo.getTop( )
							+ ( bo.getHeight( ) - lgTitleHeight )
							/ 2d;
					break;
			}

			final TextRenderEvent tre = (TextRenderEvent) ( (EventObjectCache) ir ).getEventObject( WrappedStructureSource.createLegendTitle( lg,
					lgTitle ),
					TextRenderEvent.class );
			tre.setBlockBounds( BoundsImpl.create( lX,
					lY,
					lgTitleWidth,
					lgTitleHeight ) );
			TextAlignment ta = TextAlignmentImpl.create( );
			ta.setHorizontalAlignment( HorizontalAlignment.CENTER_LITERAL );
			ta.setVerticalAlignment( VerticalAlignment.CENTER_LITERAL );
			tre.setBlockAlignment( ta );
			tre.setLabel( lgTitle );
			tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
			ipr.drawText( tre );
		}

		itm.dispose( ); // DISPOSE RESOURCES AFTER USE
	}

	/**
	 * Internally used to render a legend item separator
	 * 
	 * @param ipr
	 * @param lg
	 * @param dX
	 * @param dY
	 * @param dLength
	 * @param o
	 */
	protected static final void renderSeparator( IPrimitiveRenderer ipr,
			Legend lg, LineAttributes lia, double dX, double dY,
			double dLength, Orientation o ) throws ChartException
	{
		if ( o.getValue( ) == Orientation.HORIZONTAL )
		{
			final LineRenderEvent lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createLegend( lg ),
					LineRenderEvent.class );
			lre.setLineAttributes( lia );
			lre.setStart( LocationImpl.create( dX, dY ) );
			lre.setEnd( LocationImpl.create( dX + dLength, dY ) );
			ipr.drawLine( lre );
		}
		else if ( o.getValue( ) == Orientation.VERTICAL )
		{
			final LineRenderEvent lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createLegend( lg ),
					LineRenderEvent.class );
			lre.setLineAttributes( lia );
			lre.setStart( LocationImpl.create( dX, dY ) );
			lre.setEnd( LocationImpl.create( dX, dY + dLength ) );
			ipr.drawLine( lre );
		}
	}

	/**
	 * Search the width for each column when legend is vertical.
	 * 
	 * @param liha
	 * @return
	 */
	protected Map searchMaxColumnWidth( LegendItemHints[] liha )
	{
		HashMap rt = new HashMap( );

		int start = -1;
		double x = 0;
		double maxWidth = 0;

		for ( int i = 0; i < liha.length; i++ )
		{
			if ( liha[i].getType( ) == IConstants.LEGEND_SEPERATOR )
			{
				continue;
			}

			if ( start < 0 )
			{
				start = i;
				x = liha[i].getLeft( );
				maxWidth = liha[i].getWidth( );
			}
			else if ( liha[i].getLeft( ) != x )
			{
				for ( int j = start; j < i; j++ )
				{
					rt.put( liha[j], new Double( maxWidth ) );
				}

				start = i;
				x = liha[i].getLeft( );
				maxWidth = liha[i].getWidth( );
			}
			else
			{
				maxWidth = Math.max( maxWidth, liha[i].getWidth( ) );
			}
		}

		for ( int j = Math.max( start, 0 ); j < liha.length; j++ )
		{
			rt.put( liha[j], new Double( maxWidth ) );
		}

		return rt;
	}

	/**
	 * Internally provided to render a single legend entry
	 * 
	 * @param ipr
	 * @param lg
	 * @param la
	 * @param dX
	 * @param dY
	 * @param dW
	 * @param dItemHeight
	 * @param dLeftInset
	 * @param dHorizontalSpacing
	 * @param se
	 * @param fPaletteEntry
	 * @param lirh
	 * 
	 * @throws RenderingException
	 */
	protected final void renderLegendItem( IPrimitiveRenderer ipr, Legend lg,
			Label la, Label valueLa, double dX, double dY, double dW,
			double dItemHeight, double dFullHeight, double dExtraHeight,
			double dColumnWidth, double dLeftInset, double dHorizontalSpacing,
			Series se, Fill fPaletteEntry, LegendItemRenderingHints lirh )
			throws ChartException
	{
		ScriptHandler sh = getRunTimeContext( ).getScriptHandler( );
		ScriptHandler.callFunction( sh,
				ScriptHandler.BEFORE_DRAW_LEGEND_ENTRY,
				la,
				getRunTimeContext( ).getScriptContext( ) );
		getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_LEGEND_ENTRY,
				la );

		final Bounds bo = lirh.getLegendGraphicBounds( );

		if ( isRightToLeft( ) )
		{
			bo.setLeft( dX
					+ dColumnWidth
					- dLeftInset
					- 1
					- 3
					* dItemHeight
					/ 2 );
		}
		else
		{
			bo.setLeft( dX + dLeftInset + 1 );
		}
		bo.setTop( dY + 1 + ( dFullHeight - dItemHeight ) / 2 );
		bo.setWidth( 3 * dItemHeight / 2 );
		bo.setHeight( dItemHeight - 2 );

		final BaseRenderer br = lirh.getRenderer( );
		br.renderLegendGraphic( ipr, lg, fPaletteEntry, bo );

		final TextRenderEvent tre = (TextRenderEvent) ( (EventObjectCache) ir ).getEventObject( StructureSource.createLegend( lg ),
				TextRenderEvent.class );
		if ( isRightToLeft( ) )
		{
			tre.setLocation( LocationImpl.create( dX
					+ dColumnWidth
					- dLeftInset
					- 3
					* dItemHeight
					/ 2
					- dHorizontalSpacing, dY + dFullHeight / 2 - 1 ) );
			tre.setTextPosition( TextRenderEvent.LEFT );
		}
		else
		{
			tre.setLocation( LocationImpl.create( dX
					+ dLeftInset
					+ ( 3 * dItemHeight / 2 )
					+ dHorizontalSpacing, dY + dFullHeight / 2 - 1 ) );
			tre.setTextPosition( TextRenderEvent.RIGHT );
		}
		tre.setLabel( la );
		tre.setAction( TextRenderEvent.RENDER_TEXT_AT_LOCATION );
		ipr.drawText( tre );

		if ( valueLa != null )
		{
			Location[] loaBack = new Location[4];
			final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ir ).getEventObject( StructureSource.createLegend( lg ),
					PolygonRenderEvent.class );
			pre.setBackground( valueLa.getBackground( ) );
			pre.setOutline( valueLa.getOutline( ) );
			pre.setPoints( loaBack );

			final double dValueWidth = dColumnWidth - 2 * dLeftInset;

			loaBack[0] = LocationImpl.create( dX + dLeftInset + 1, dY
					+ dFullHeight
					+ 1 );
			loaBack[1] = LocationImpl.create( dX + dLeftInset + 1, dY
					+ dFullHeight
					+ dExtraHeight );
			loaBack[2] = LocationImpl.create( dX + dLeftInset + dValueWidth, dY
					+ dFullHeight
					+ dExtraHeight );
			loaBack[3] = LocationImpl.create( dX + dLeftInset + dValueWidth, dY
					+ dFullHeight
					+ 1 );

			ipr.fillPolygon( pre );
			ipr.drawPolygon( pre );

			Label tmpLa = LabelImpl.copyInstance( valueLa );
			tmpLa.setOutline( null );
			tmpLa.setBackground( null );

			TextAlignment ta = TextAlignmentImpl.create( );
			ta.setHorizontalAlignment( HorizontalAlignment.CENTER_LITERAL );
			ta.setVerticalAlignment( VerticalAlignment.CENTER_LITERAL );
			tre.setBlockAlignment( ta );
			tre.setBlockBounds( BoundsImpl.create( dX + dLeftInset + 1, dY
					+ dFullHeight
					+ 1, dValueWidth - 2, dExtraHeight - 1 ) );
			tre.setLabel( tmpLa );
			tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );
			ipr.drawText( tre );
		}

		if ( isInteractivityEnabled( ) )
		{
			// PROCESS 'SERIES LEVEL' TRIGGERS USING SOURCE='bs'
			Trigger tg;
			EList elTriggers = lg.getTriggers( );
			Location[] loaHotspot = new Location[4];

			if ( isRightToLeft( ) )
			{
				double dTextStartX = tre.getLocation( ).getX( ) + 1;
				loaHotspot[0] = LocationImpl.create( dTextStartX, dY );
				loaHotspot[1] = LocationImpl.create( dTextStartX - dW, dY + 1 );
				loaHotspot[2] = LocationImpl.create( dTextStartX - dW, dY
						+ dItemHeight
						+ dExtraHeight );
				loaHotspot[3] = LocationImpl.create( dTextStartX, dY
						+ dItemHeight
						+ dExtraHeight );
			}
			else
			{
				double dTextStartX = tre.getLocation( ).getX( ) - 1;
				loaHotspot[0] = LocationImpl.create( dTextStartX, dY );
				loaHotspot[1] = LocationImpl.create( dTextStartX + dW, dY + 1 );
				loaHotspot[2] = LocationImpl.create( dTextStartX + dW, dY
						+ dItemHeight
						+ dExtraHeight );
				loaHotspot[3] = LocationImpl.create( dTextStartX, dY
						+ dItemHeight
						+ dExtraHeight );
			}

			Trigger buildinTg = null;

			if ( cm.getInteractivity( ) != null )
			{
				boolean customed = false;

				switch ( cm.getInteractivity( ).getLegendBehavior( ).getValue( ) )
				{
					case LegendBehaviorType.HIGHLIGHT_SERIE :
						for ( Iterator itr = elTriggers.iterator( ); itr.hasNext( ); )
						{
							tg = (Trigger) itr.next( );
							if ( tg.getCondition( ) == TriggerCondition.ONCLICK_LITERAL
									|| tg.getAction( ).getType( ) == ActionType.HIGHLIGHT_LITERAL )
							{
								customed = true;
							}
						}
						if ( !customed )
						{
							buildinTg = TriggerImpl.create( TriggerCondition.ONCLICK_LITERAL,
									ActionImpl.create( ActionType.HIGHLIGHT_LITERAL,
											SeriesValueImpl.create( String.valueOf( se.getSeriesIdentifier( ) ) ) ) );
						}
						break;
					case LegendBehaviorType.TOGGLE_SERIE_VISIBILITY :
						for ( Iterator itr = elTriggers.iterator( ); itr.hasNext( ); )
						{
							tg = (Trigger) itr.next( );
							if ( tg.getCondition( ) == TriggerCondition.ONCLICK_LITERAL
									|| tg.getAction( ).getType( ) == ActionType.TOGGLE_VISIBILITY_LITERAL )
							{
								customed = true;
							}
						}
						if ( !customed )
						{
							buildinTg = TriggerImpl.create( TriggerCondition.ONCLICK_LITERAL,
									ActionImpl.create( ActionType.TOGGLE_VISIBILITY_LITERAL,
											SeriesValueImpl.create( String.valueOf( se.getSeriesIdentifier( ) ) ) ) );
						}
						break;
					case LegendBehaviorType.NONE :
						break;
				}
			}

			if ( !elTriggers.isEmpty( ) || buildinTg != null )
			{
				final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createSeries( se ),
						InteractionEvent.class );
				for ( int t = 0; t < elTriggers.size( ); t++ )
				{
					tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
					processTrigger( tg,
							WrappedStructureSource.createLegendEntry( lg, la ) );
					iev.addTrigger( tg );
				}

				if ( buildinTg != null )
				{
					processTrigger( buildinTg,
							WrappedStructureSource.createLegendEntry( lg, la ) );
					iev.addTrigger( buildinTg );
				}

				final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createLegend( lg ),
						PolygonRenderEvent.class );
				pre.setPoints( loaHotspot );
				iev.setHotSpot( pre );
				ipr.enableInteraction( iev );
			}
		}

		ScriptHandler.callFunction( sh,
				ScriptHandler.AFTER_DRAW_LEGEND_ENTRY,
				la,
				getRunTimeContext( ).getScriptContext( ) );
		getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_LEGEND_ENTRY,
				la );
	}

	/**
	 * 
	 * @param ipr
	 * @param p
	 * 
	 * @throws RenderingException
	 */
	public void renderPlot( IPrimitiveRenderer ipr, Plot p )
			throws ChartException
	{
		if ( !p.isVisible( ) ) // CHECK VISIBILITY
		{
			return;
		}

		final boolean bFirstInSequence = ( iSeriesIndex == 0 );
		final boolean bLastInSequence = ( iSeriesIndex == iSeriesCount - 1 );

		if ( bFirstInSequence )
		{
			renderBackground( ipr, p );
		}

		if ( getSeries( ) != null )
		{
			ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
					ScriptHandler.BEFORE_DRAW_SERIES,
					getSeries( ),
					this,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.BEFORE_DRAW_SERIES,
					getSeries( ) );
			renderSeries( ipr, p, srh ); // CALLS THE APPROPRIATE SUBCLASS
			// FOR
			ScriptHandler.callFunction( getRunTimeContext( ).getScriptHandler( ),
					ScriptHandler.AFTER_DRAW_SERIES,
					getSeries( ),
					this,
					getRunTimeContext( ).getScriptContext( ) );
			getRunTimeContext( ).notifyStructureChange( IStructureDefinitionListener.AFTER_DRAW_SERIES,
					getSeries( ) );
		}

		if ( bLastInSequence )
		{
			// RENDER OVERLAYS HERE IF ANY
		}
	}

	/**
	 * 
	 * @param ipr
	 * @param p
	 * 
	 * @throws RenderingException
	 */
	protected void renderBackground( IPrimitiveRenderer ipr, Plot p )
			throws ChartException
	{
		final double dScale = getDevice( ).getDisplayServer( )
				.getDpiResolution( ) / 72d;
		final RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
				RectangleRenderEvent.class );
		rre.updateFrom( p, dScale ); // POINTS => PIXELS
		ipr.fillRectangle( rre );
		ipr.drawRectangle( rre );

		Object oComputations = getComputations( );
		if ( oComputations instanceof PlotWithoutAxes )
		{
			final ClientArea ca = p.getClientArea( );

			Bounds cbo = rre.getBounds( );

			// render client area shadow
			if ( ca.getShadowColor( ) != null )
			{
				rre.setBounds( cbo.translateInstance( 3, 3 ) );
				rre.setBackground( ca.getShadowColor( ) );
				ipr.fillRectangle( rre );
			}

			// render client area
			rre.setBounds( cbo );
			rre.setBackground( ca.getBackground( ) );
			ipr.fillRectangle( rre );
			ipr.drawRectangle( rre );

			if ( !ca.getOutline( ).isSetVisible( ) )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.RENDERING,
						"exception.client.area.outline.visibility", //$NON-NLS-1$ 
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
			if ( ca.getOutline( ).isVisible( ) )
			{
				final Bounds bo = p.getBounds( ).scaledInstance( dScale ); // POINTS
				// =>
				// PIXELS
				final PlotWithoutAxes pwoa = (PlotWithoutAxes) oComputations;
				final Size sz = SizeImpl.create( bo.getWidth( )
						/ pwoa.getColumnCount( ), bo.getHeight( )
						/ pwoa.getRowCount( ) );
				final LineRenderEvent lre = (LineRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createPlot( p ),
						LineRenderEvent.class );
				lre.setLineAttributes( ca.getOutline( ) );

				int colCount = pwoa.getColumnCount( );
				int rowCount = pwoa.getRowCount( );

				ChartWithoutAxes cwoa = pwoa.getModel( );
				if ( cwoa instanceof DialChart
						&& ( (DialChart) cwoa ).isDialSuperimposition( ) )
				{
					colCount = 1;
					rowCount = 1;
				}

				for ( int i = 1; i < colCount; i++ )
				{
					lre.setStart( LocationImpl.create( bo.getLeft( )
							+ i
							* sz.getWidth( ), bo.getTop( ) ) );
					lre.setEnd( LocationImpl.create( bo.getLeft( )
							+ i
							* sz.getWidth( ), bo.getTop( ) + bo.getHeight( ) ) );
					ipr.drawLine( lre );
				}
				for ( int j = 1; j < rowCount; j++ )
				{
					lre.setStart( LocationImpl.create( bo.getLeft( ),
							bo.getTop( ) + j * sz.getHeight( ) ) );
					lre.setEnd( LocationImpl.create( bo.getLeft( )
							+ bo.getWidth( ), bo.getTop( ) + j * sz.getHeight( ) ) );
					ipr.drawLine( lre );
				}
			}
		}

	}

	/**
	 * 
	 * @param ipr
	 * @param b
	 * 
	 * @throws RenderingException
	 */
	protected void renderBlock( IPrimitiveRenderer ipr, Block b, Object oSource )
			throws ChartException
	{
		final double dScale = getDevice( ).getDisplayServer( )
				.getDpiResolution( ) / 72d;
		final RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
				RectangleRenderEvent.class );
		rre.updateFrom( b, dScale );
		ipr.fillRectangle( rre );
		ipr.drawRectangle( rre );
	}

	/**
	 * 
	 * @param ipr
	 * @param b
	 * 
	 * @throws RenderingException
	 */
	protected void renderChartBlock( IPrimitiveRenderer ipr, Block b,
			Object oSource ) throws ChartException
	{
		final double dScale = getDevice( ).getDisplayServer( )
				.getDpiResolution( ) / 72d;
		final RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
				RectangleRenderEvent.class );
		rre.updateFrom( b, dScale );
		ipr.fillRectangle( rre );
		ipr.drawRectangle( rre );

		if ( isInteractivityEnabled( ) )
		{
			Trigger tg;
			EList elTriggers = b.getTriggers( );
			Location[] loaHotspot = new Location[4];
			Bounds bo = b.getBounds( ).scaledInstance( dScale );
			double dLeft = bo.getLeft( );
			double dTop = bo.getTop( );
			double dWidth = bo.getWidth( );
			double dHeight = bo.getHeight( );
			loaHotspot[0] = LocationImpl.create( dLeft, dTop );
			loaHotspot[1] = LocationImpl.create( dLeft + dWidth, dTop );
			loaHotspot[2] = LocationImpl.create( dLeft + dWidth, dTop + dHeight );
			loaHotspot[3] = LocationImpl.create( dLeft, dTop + dHeight );

			if ( !elTriggers.isEmpty( ) )
			{
				final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createUnknown( b ),
						InteractionEvent.class );
				for ( int t = 0; t < elTriggers.size( ); t++ )
				{
					tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
					processTrigger( tg, StructureSource.createChartBlock( b ) );
					iev.addTrigger( tg );
				}

				final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createUnknown( b ),
						PolygonRenderEvent.class );
				pre.setPoints( loaHotspot );
				iev.setHotSpot( pre );
				ipr.enableInteraction( iev );
			}

		}
	}

	/**
	 * 
	 * @param ipr
	 * @param b
	 * 
	 * @throws RenderingException
	 */
	public void renderLabel( IPrimitiveRenderer ipr, Block b, Object oSource )
			throws ChartException
	{
		if ( !b.isVisible( ) )
		{
			return;
		}
		renderBlock( ipr, b, oSource );
		final double dScale = getDevice( ).getDisplayServer( )
				.getDpiResolution( ) / 72d;
		final LabelBlock lb = (LabelBlock) b;
		final TextRenderEvent tre = (TextRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
				TextRenderEvent.class );

		// need backup original non-externalized value.
		final String sRestoreValue = tre.updateFrom( lb, dScale, rtc );
		ipr.drawText( tre );
		lb.getLabel( ).getCaption( ).setValue( sRestoreValue );
	}

	/**
	 * 
	 * @param ipr
	 * @param b
	 * 
	 * @throws RenderingException
	 */
	public void renderTitle( IPrimitiveRenderer ipr, TitleBlock b )
			throws ChartException
	{
		// switch lable alignment
		TextAlignment restoreValue = b.getLabel( )
				.getCaption( )
				.getFont( )
				.getAlignment( );
		b.getLabel( )
				.getCaption( )
				.getFont( )
				.setAlignment( switchTextAlignment( restoreValue ) );

		renderLabel( ipr, b, StructureSource.createTitle( b ) );

		// restore original value
		b.getLabel( ).getCaption( ).getFont( ).setAlignment( restoreValue );

		if ( isInteractivityEnabled( ) )
		{
			Trigger tg;
			EList elTriggers = b.getTriggers( );
			Location[] loaHotspot = new Location[4];
			final double dScale = getDevice( ).getDisplayServer( )
					.getDpiResolution( ) / 72d;
			Bounds bo = b.getBounds( ).scaledInstance( dScale );
			double dLeft = bo.getLeft( );
			double dTop = bo.getTop( );
			double dWidth = bo.getWidth( );
			double dHeight = bo.getHeight( );
			loaHotspot[0] = LocationImpl.create( dLeft, dTop );
			loaHotspot[1] = LocationImpl.create( dLeft + dWidth, dTop );
			loaHotspot[2] = LocationImpl.create( dLeft + dWidth, dTop + dHeight );
			loaHotspot[3] = LocationImpl.create( dLeft, dTop + dHeight );

			if ( !elTriggers.isEmpty( ) )
			{
				final InteractionEvent iev = (InteractionEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createTitle( b ),
						InteractionEvent.class );
				for ( int t = 0; t < elTriggers.size( ); t++ )
				{
					tg = TriggerImpl.copyInstance( (Trigger) elTriggers.get( t ) );
					processTrigger( tg, StructureSource.createTitle( b ) );
					iev.addTrigger( tg );
				}

				final PolygonRenderEvent pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( StructureSource.createTitle( b ),
						PolygonRenderEvent.class );
				pre.setPoints( loaHotspot );
				iev.setHotSpot( pre );
				ipr.enableInteraction( iev );
			}

		}
	}

	/**
	 * 
	 * @param cm
	 * @param oComputations
	 * @return
	 */
	private static final BaseRenderer[] createEmptyInstance( Chart cm,
			RunTimeContext rtc, Object oComputations )
	{
		final BaseRenderer[] brna = new BaseRenderer[1];
		final AxesRenderer ar = new EmptyWithAxes( );
		ar.iSeriesIndex = 0;
		ar.set( cm, oComputations, null, null, null );
		ar.set( rtc );
		brna[0] = ar;
		return brna;
	}

	/**
	 * This method returns appropriate renders for the given chart model. It
	 * uses extension points to identify a renderer corresponding to a custom
	 * series.
	 * 
	 * @param cm
	 * @param rtc
	 * @param oComputations
	 * 
	 * @return
	 * @throws PluginException
	 */
	public static final BaseRenderer[] instances( Chart cm, RunTimeContext rtc,
			Object oComputations ) throws ChartException
	{
		final PluginSettings ps = PluginSettings.instance( );
		BaseRenderer[] brna = null;
		final boolean bPaletteByCategory = ( cm.getLegend( )
				.getItemType( )
				.getValue( ) == LegendItemType.CATEGORIES );

		if ( cm instanceof ChartWithAxes )
		{
			final ChartWithAxes cwa = (ChartWithAxes) cm;
			final Axis[] axa = cwa.getPrimaryBaseAxes( );
			Axis axPrimaryBase = axa[0];
			Series se;
			AxesRenderer ar = null;
			ArrayList al = new ArrayList( ), alRunTimeSeries;
			EList elBase, elOrthogonal;
			SeriesDefinition sd = null;

			int iSI = 0; // SERIES INDEX COUNTER

			elBase = axPrimaryBase.getSeriesDefinitions( );
			if ( elBase.isEmpty( ) ) // NO SERIES DEFINITIONS
			{
				return createEmptyInstance( cm, rtc, oComputations );
			}
			else
			{
				// ONLY 1 SERIES DEFINITION MAY BE
				// ASSOCIATED
				// WITH THE BASE AXIS
				final SeriesDefinition sdBase = (SeriesDefinition) elBase.get( 0 );

				alRunTimeSeries = sdBase.getRunTimeSeries( );
				if ( alRunTimeSeries.isEmpty( ) )
				{
					return createEmptyInstance( cm, rtc, oComputations );
				}
				// ONLY 1 SERIES MAY BE
				// ASSOCIATED WITH THE
				// BASE SERIES DEFINITION
				se = (Series) alRunTimeSeries.get( 0 );
				ar = ( se.getClass( ) == SeriesImpl.class ) ? new EmptyWithAxes( )
						: (AxesRenderer) ps.getRenderer( se.getClass( ) );
				// INITIALIZE THE RENDERER
				ar.set( cm, oComputations, se, axPrimaryBase, sdBase );
				ar.set( rtc );
				ar.iSeriesIndex = iSI++;
				al.add( ar );

				final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase,
						true );
				for ( int i = 0; i < axaOrthogonal.length; i++ )
				{
					elOrthogonal = axaOrthogonal[i].getSeriesDefinitions( );
					for ( int j = 0; j < elOrthogonal.size( ); j++ )
					{
						sd = (SeriesDefinition) elOrthogonal.get( j );
						alRunTimeSeries = sd.getRunTimeSeries( );
						for ( int k = 0; k < alRunTimeSeries.size( ); k++ )
						{
							se = (Series) alRunTimeSeries.get( k );
							ar = ( se.getClass( ) == SeriesImpl.class ) ? new EmptyWithAxes( )
									: (AxesRenderer) ps.getRenderer( se.getClass( ) );
							// INITIALIZE THE RENDERER
							ar.set( cm,
									oComputations,
									se,
									axaOrthogonal[i],
									bPaletteByCategory ? sdBase : sd );
							ar.iSeriesIndex = iSI++;
							al.add( ar );
						}
					}
				}

				// CONVERT INTO AN ARRAY AS REQUESTED
				brna = new BaseRenderer[iSI];
				for ( int i = 0; i < iSI; i++ )
				{
					ar = (AxesRenderer) al.get( i );
					ar.iSeriesCount = iSI;
					brna[i] = ar;
				}
			}
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			final ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			EList elBase = cwoa.getSeriesDefinitions( );
			EList elOrthogonal;
			SeriesDefinition sd, sdBase;
			ArrayList alRuntimeSeries;

			final Series[] sea = cwoa.getRunTimeSeries( );

			Series se;
			final int iSeriesCount = sea.length;
			brna = new BaseRenderer[iSeriesCount];
			int iSI = 0; // SERIES INDEX COUNTER

			for ( int i = 0; i < elBase.size( ); i++ )
			{
				sdBase = (SeriesDefinition) elBase.get( i );
				alRuntimeSeries = sdBase.getRunTimeSeries( );
				// CHECK FOR A SINGLE BASE SERIES ONLY
				if ( alRuntimeSeries.size( ) != 1 )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.PLUGIN,
							"exception.illegal.base.runtime.series.count", //$NON-NLS-1$
							new Object[]{
								new Integer( alRuntimeSeries.size( ) )
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) );
				}
				se = (Series) alRuntimeSeries.get( 0 );
				brna[iSI] = ( se.getClass( ) == SeriesImpl.class ) ? new EmptyWithoutAxes( )
						: ps.getRenderer( se.getClass( ) );
				// INITIALIZE THE RENDERER
				brna[iSI].set( cm, oComputations, se, sdBase );
				brna[iSI].set( rtc );
				brna[iSI].iSeriesIndex = iSI++;

				elOrthogonal = ( (SeriesDefinition) elBase.get( i ) ).getSeriesDefinitions( );
				for ( int j = 0; j < elOrthogonal.size( ); j++ )
				{
					sd = (SeriesDefinition) elOrthogonal.get( j );
					alRuntimeSeries = sd.getRunTimeSeries( );
					for ( int k = 0; k < alRuntimeSeries.size( ); k++ )
					{
						se = (Series) alRuntimeSeries.get( k );
						brna[iSI] = ( se.getClass( ) == SeriesImpl.class ) ? new EmptyWithoutAxes( )
								: ps.getRenderer( se.getClass( ) );
						// INITIALIZE THE RENDERER
						brna[iSI].set( cm,
								oComputations,
								se,
								bPaletteByCategory ? sdBase : sd );
						brna[iSI].iSeriesIndex = iSI++;
					}
				}
			}

			for ( int k = 0; k < iSI; k++ )
			{
				brna[k].iSeriesCount = iSI;
			}
		}

		return brna;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.IModelAccess#getSeries()
	 */
	public final Series getSeries( )
	{
		return se;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.IModelAccess#getModel()
	 */
	public final Chart getModel( )
	{
		return cm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.IModelAccess#getComputations()
	 */
	public final Object getComputations( )
	{
		return oComputations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.IModelAccess#getDevice()
	 */
	public final IDeviceRenderer getDevice( )
	{
		return ir;
	}

	/**
	 * Renders a 2D or extruded 2D plane as necessary for a given front surface
	 * polygon. Takes into account the correct z-ordering of each plane and
	 * applies basic lighting. This convenience method may be used by series
	 * type rendering extensions if needed.
	 * 
	 * @param ipr
	 *            A handle to the primitive rendering device
	 * @param oSource
	 *            The object wrapped in the polygon rendering event
	 * @param loaFront
	 *            The co-ordinates of the front face polygon
	 * @param f
	 *            The fill color for the front face
	 * @param lia
	 *            The edge color for the polygon
	 * @param dSeriesThickness
	 *            The thickness or the extrusion level (for 2.5D or 3D)
	 * 
	 * @throws RenderingException
	 */
	protected final void renderPlane( IPrimitiveRenderer ipr, Object oSource,
			Location[] loaFront, Fill f, LineAttributes lia, ChartDimension cd,
			double dSeriesThickness, boolean bDeferred ) throws ChartException
	{
		PolygonRenderEvent pre;
		if ( cd.getValue( ) == ChartDimension.TWO_DIMENSIONAL )
		{
			// RENDER THE POLYGON
			pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
					PolygonRenderEvent.class );
			pre.setPoints( loaFront );
			pre.setBackground( f );
			pre.setOutline( lia );
			ipr.fillPolygon( pre );
			ipr.drawPolygon( pre );
			return;
		}

		final boolean bSolidColor = f instanceof ColorDefinition;
		Fill fDarker = null, fBrighter = null;
		if ( cd.getValue( ) == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH
				|| cd.getValue( ) == ChartDimension.THREE_DIMENSIONAL )
		{
			fDarker = f;
			if ( fDarker instanceof ColorDefinition )
			{
				fDarker = ( (ColorDefinition) fDarker ).darker( );
			}
			fBrighter = f;
			if ( fBrighter instanceof ColorDefinition )
			{
				fBrighter = ( (ColorDefinition) fBrighter ).brighter( );
			}
		}

		final int nSides = loaFront.length;
		final Location[][] loaa = new Location[nSides + 1][];
		Location[] loa;
		double dY, dSmallestY = 0;
		for ( int j, i = 0; i < nSides; i++ )
		{
			j = i + 1;
			if ( j >= loaFront.length )
				j = 0;
			loa = new Location[4];
			loa[0] = LocationImpl.create( loaFront[i].getX( ),
					loaFront[i].getY( ) );
			loa[1] = LocationImpl.create( loaFront[j].getX( ),
					loaFront[j].getY( ) );
			loa[2] = LocationImpl.create( loaFront[j].getX( )
					+ dSeriesThickness, loaFront[j].getY( ) - dSeriesThickness );
			loa[3] = LocationImpl.create( loaFront[i].getX( )
					+ dSeriesThickness, loaFront[i].getY( ) - dSeriesThickness );
			loaa[i] = loa;
		}
		loaa[nSides] = loaFront;

		// SORT ON MULTIPLE KEYS (GREATEST Y, SMALLEST X)
		double dI, dJ;
		Location[] loaI, loaJ;
		for ( int i = 0; i < nSides - 1; i++ )
		{
			loaI = loaa[i];
			for ( int j = i + 1; j < nSides; j++ )
			{
				loaJ = loaa[j];

				dI = getY( loaI, IConstants.AVERAGE );
				dJ = getY( loaJ, IConstants.AVERAGE );

				// Use fuzzy comparison here due to possible precision loss
				// during computation.
				if ( ChartUtil.mathGT( dJ, dI ) ) // SWAP
				{
					loaa[i] = loaJ;
					loaa[j] = loaI;
					loaI = loaJ;
				}
				else if ( ChartUtil.mathEqual( dJ, dI ) )
				{
					dI = getX( loaI, IConstants.AVERAGE );
					dJ = getX( loaJ, IConstants.AVERAGE );
					if ( ChartUtil.mathGT( dI, dJ ) )
					{
						loaa[i] = loaJ;
						loaa[j] = loaI;
						loaI = loaJ;
					}
				}
			}
		}

		int iSmallestYIndex = 0;
		for ( int i = 0; i < nSides; i++ )
		{
			dY = getY( loaa[i], IConstants.AVERAGE );
			if ( i == 0 )
			{
				dSmallestY = dY;
			}
			else if ( dSmallestY > dY )
			{
				dSmallestY = dY;
				iSmallestYIndex = i;
			}
		}

		ArrayList alModel = new ArrayList( nSides + 1 );
		Fill fP;
		for ( int i = 0; i <= nSides; i++ )
		{
			pre = (PolygonRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
					PolygonRenderEvent.class );
			pre.setOutline( lia );
			pre.setPoints( loaa[i] );
			if ( i < nSides ) // OTHER SIDES (UNKNOWN ORDER) ARE DEEP
			{
				pre.setDepth( -dSeriesThickness );
			}
			else
			// FRONT FACE IS NOT DEEP
			{
				pre.setDepth( 0 );
			}
			if ( i == nSides )
			{
				fP = f;
			}
			else if ( i == iSmallestYIndex )
			{
				fP = fBrighter;
			}
			else
			{
				fP = fDarker;
			}
			pre.setBackground( fP );
			if ( bDeferred )
			{
				alModel.add( pre.copy( ) );
			}
			else
			{
				ipr.fillPolygon( pre );
			}

			if ( i == nSides )
			{
			}
			else if ( i == iSmallestYIndex )
			{
				// DRAW A TRANSLUCENT LIGHT GLASS PANE OVER THE BRIGHTER SURFACE
				// (IF NOT A SOLID COLOR)
				if ( !bSolidColor )
				{
					pre.setBackground( LIGHT_GLASS );
				}
				if ( bDeferred )
				{
					alModel.add( pre.copy( ) );
				}
				else
				{
					ipr.fillPolygon( pre );
				}
			}
			else
			{
				// DRAW A TRANSLUCENT DARK GLASS PANE OVER THE DARKER SURFACE
				// (IF NOT A SOLID COLOR)
				if ( !bSolidColor )
				{
					pre.setBackground( DARK_GLASS );
				}
				if ( bDeferred )
				{
					alModel.add( pre.copy( ) );
				}
				else
				{
					ipr.fillPolygon( pre );
				}
			}
			if ( !bDeferred )
			{
				ipr.drawPolygon( pre );
			}
		}
		if ( !alModel.isEmpty( ) )
		{
			dc.addModel( new WrappedInstruction( getDeferredCache( ),
					alModel,
					PrimitiveRenderEvent.FILL ) );
		}
	}

	/**
	 * Renders planes as 3D presentation.
	 * 
	 * @param ipr
	 * @param oSource
	 * @param loaFace
	 * @param f
	 * @param lia
	 * @throws ChartException
	 */
	protected final void render3DPlane( IPrimitiveRenderer ipr, Object oSource,
			Location3D[][] loaFace, Fill f, LineAttributes lia )
			throws ChartException
	{
		Polygon3DRenderEvent pre = (Polygon3DRenderEvent) ( (EventObjectCache) ipr ).getEventObject( oSource,
				Polygon3DRenderEvent.class );

		int nSides = loaFace.length;

		for ( int i = 0; i < nSides; i++ )
		{
			pre.setOutline( lia );
			pre.setPoints3D( loaFace[i] );
			pre.setBackground( f );
			dc.addPlane( pre, PrimitiveRenderEvent.FILL
					| PrimitiveRenderEvent.DRAW );
		}
	}

	/**
	 * 
	 * @param loa
	 * @param iProperty
	 * 
	 * @return
	 */
	public static final double getY( Location[] loa, int iProperty )
	{
		int iCount = loa.length;
		double dY = 0;
		if ( iProperty == IConstants.MIN )
		{
			dY = loa[0].getY( );
			for ( int i = 1; i < iCount; i++ )
			{
				dY = Math.min( dY, loa[i].getY( ) );
			}
		}
		else if ( iProperty == IConstants.MAX )
		{
			dY = loa[0].getY( );
			for ( int i = 1; i < iCount; i++ )
			{
				dY = Math.max( dY, loa[i].getY( ) );
			}
		}
		else if ( iProperty == IConstants.AVERAGE )
		{
			for ( int i = 0; i < iCount; i++ )
			{
				dY += loa[i].getY( );
			}
			dY /= iCount;
		}
		return dY;
	}

	/**
	 * 
	 * @param loa
	 * @param iProperty
	 * 
	 * @return
	 */
	public static final double getX( Location[] loa, int iProperty )
	{
		int iCount = loa.length;
		double dX = 0;
		if ( iProperty == IConstants.MIN )
		{
			dX = loa[0].getX( );
			for ( int i = 1; i < iCount; i++ )
			{
				dX = Math.min( dX, loa[i].getX( ) );
			}
		}
		else if ( iProperty == IConstants.MAX )
		{
			dX = loa[0].getX( );
			for ( int i = 1; i < iCount; i++ )
			{
				dX = Math.max( dX, loa[i].getX( ) );
			}
		}
		else if ( iProperty == IConstants.AVERAGE )
		{
			for ( int i = 0; i < iCount; i++ )
			{
				dX += loa[i].getX( );
			}
			dX /= iCount;
		}
		return dX;
	}

	/**
	 * post-process the triggers.
	 * 
	 * @param tg
	 * @param dph
	 */
	public void processTrigger( Trigger tg, StructureSource source )
	{
		// use user action renderer first.
		IActionRenderer iar = getRunTimeContext( ).getActionRenderer( );
		if ( iar != null )
		{
			iar.processAction( tg.getAction( ), source );
		}

		// internal processing.
		if ( StructureType.SERIES_DATA_POINT.equals( source.getType( ) ) )
		{
			DataPointHints dph = (DataPointHints) source.getSource( );

			if ( tg.getAction( ).getType( ) == ActionType.SHOW_TOOLTIP_LITERAL )
			{
				// BUILD THE VALUE
				String toolText = ( (TooltipValue) tg.getAction( ).getValue( ) ).getText( );

				// if blank, then use DataPoint label.
				if ( toolText == null || toolText.length( ) == 0 )
				{
					( (TooltipValue) tg.getAction( ).getValue( ) ).setText( dph.getDisplayValue( ) );
				}
			}
			else if ( tg.getAction( ).getType( ) == ActionType.URL_REDIRECT_LITERAL )
			{
				// BUILD A URI
				final URLValue uv = (URLValue) tg.getAction( ).getValue( );
				final String sBaseURL = uv.getBaseUrl( );
				final StringBuffer sb = new StringBuffer( sBaseURL );
				char c = '?';
				if ( sBaseURL.indexOf( c ) != -1 )
				{
					c = '&';
				}
				if ( uv.getBaseParameterName( ) != null
						&& uv.getBaseParameterName( ).length( ) > 0 )
				{
					sb.append( c );
					c = '&';
					sb.append( URLValueImpl.encode( uv.getBaseParameterName( ) ) );
					sb.append( '=' );
					sb.append( URLValueImpl.encode( dph.getBaseDisplayValue( ) ) );
				}

				if ( uv.getValueParameterName( ) != null
						&& uv.getValueParameterName( ).length( ) > 0 )
				{
					sb.append( c );
					c = '&';
					sb.append( URLValueImpl.encode( uv.getValueParameterName( ) ) );
					sb.append( '=' );
					sb.append( URLValueImpl.encode( dph.getOrthogonalDisplayValue( ) ) );
				}
				if ( uv.getSeriesParameterName( ) != null
						&& uv.getSeriesParameterName( ).length( ) > 0 )
				{
					sb.append( c );
					c = '&';
					sb.append( URLValueImpl.encode( uv.getSeriesParameterName( ) ) );
					sb.append( '=' );
					sb.append( URLValueImpl.encode( dph.getSeriesDisplayValue( ) ) );
				}
				uv.setBaseUrl( sb.toString( ) );
			}
		}
	}

	/**
	 * @return
	 */
	protected final Bounds getCellBounds( )
	{
		return getCellBounds( iSeriesIndex );
	}

	/**
	 * Returns the bounds of an individual cell (if the rendered model is a
	 * ChartWithoutAxis and plot is to be split into a grid) or the entire plot
	 * bounds (if the rendered model is a ChartWithAxis).
	 * 
	 * @return
	 */
	protected final Bounds getCellBounds( int seriesIndex )
	{
		Object obj = getComputations( );

		Bounds bo = null;

		if ( obj instanceof PlotWithoutAxes )
		{
			PlotWithoutAxes pwoa = (PlotWithoutAxes) obj;
			Coordinates co = pwoa.getCellCoordinates( seriesIndex - 1 );
			Size sz = pwoa.getCellSize( );

			bo = BoundsImpl.copyInstance( pwoa.getBounds( ) );
			bo.setLeft( bo.getLeft( ) + co.getColumn( ) * sz.getWidth( ) );
			bo.setTop( bo.getTop( ) + co.getRow( ) * sz.getHeight( ) );
			bo.setWidth( sz.getWidth( ) );
			bo.setHeight( sz.getHeight( ) );
			bo = bo.adjustedInstance( pwoa.getCellInsets( ) );
		}
		else if ( obj instanceof PlotWithAxes )
		{
			PlotWithAxes pwa = (PlotWithAxes) obj;

			bo = BoundsImpl.copyInstance( pwa.getPlotBounds( ) );
			bo = bo.adjustedInstance( pwa.getPlotInsets( ) );
		}

		return bo;
	}

	/**
	 * Returns the bounds of the plot area, NOTE this bounds has reduced the
	 * insets.
	 * 
	 * @return
	 */
	protected final Bounds getPlotBounds( )
	{
		Object obj = getComputations( );

		Bounds bo = null;

		if ( obj instanceof PlotWithoutAxes )
		{
			PlotWithoutAxes pwoa = (PlotWithoutAxes) obj;

			bo = BoundsImpl.copyInstance( pwoa.getBounds( ) );
			bo = bo.adjustedInstance( pwoa.getCellInsets( ) );
		}
		else if ( obj instanceof PlotWithAxes )
		{
			PlotWithAxes pwa = (PlotWithAxes) obj;

			bo = BoundsImpl.copyInstance( pwa.getPlotBounds( ) );
			bo = bo.adjustedInstance( pwa.getPlotInsets( ) );
		}

		return bo;
	}

	/**
	 * This convenience method renders the data point label along with the
	 * shadow If there's a need to render the data point label and the shadow
	 * separately, each call should be made separately by calling into the
	 * primitive rendering interface directly.
	 */
	public final void renderLabel( Object oSource, int iTextRenderType,
			Label laDataPoint, Position lp, Location lo, Bounds bo )
			throws ChartException
	{
		final IDeviceRenderer idr = getDevice( );
		TextRenderEvent tre = (TextRenderEvent) ( (EventObjectCache) idr ).getEventObject( oSource,
				TextRenderEvent.class );
		if ( iTextRenderType != TextRenderEvent.RENDER_TEXT_IN_BLOCK )
		{
			tre.setTextPosition( Methods.getLabelPosition( lp ) );
			tre.setLocation( lo );
		}
		else
		{
			tre.setBlockBounds( bo );
			tre.setBlockAlignment( null );
		}
		tre.setLabel( laDataPoint );
		tre.setAction( iTextRenderType );
		dc.addLabel( tre );
	}

	/**
	 * @param dphs
	 */
	protected void validateNullDatapoint( DataPointHints[] dphs )
			throws ChartException
	{
		if ( dphs == null )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					"exception.base.orthogonal.null.datapoint", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE, rtc.getLocale( ) ) );
		}
	}

	/**
	 * @param isrh
	 */
	protected void validateDataSetCount( ISeriesRenderingHints isrh )
			throws ChartException
	{
		if ( ( isrh.getDataSetStructure( ) & ISeriesRenderingHints.BASE_ORTHOGONAL_OUT_OF_SYNC ) == ISeriesRenderingHints.BASE_ORTHOGONAL_OUT_OF_SYNC )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					"exception.base.orthogonal.inconsistent.count", //$NON-NLS-1$
					new Object[]{
							new Integer( isrh.getBaseDataSet( ).size( ) ),
							new Integer( isrh.getOrthogonalDataSet( ).size( ) )
					},
					ResourceBundle.getBundle( Messages.ENGINE, rtc.getLocale( ) ) );
		}
	}

	/**
	 * Filters the Null or invalid entry(contains NaN value) from the list. Each
	 * entry should be a double[2] or double[3] array object.
	 * 
	 * @param ll
	 * @return
	 */
	protected static List filterNull( List ll )
	{
		ArrayList al = new ArrayList( );
		for ( Iterator itr = ll.iterator( ); itr.hasNext( ); )
		{
			double[] obj = (double[]) itr.next( );

			if ( obj == null || Double.isNaN( obj[0] ) || Double.isNaN( obj[1] ) )
			{
				continue;
			}

			al.add( obj );
		}

		return al;
	}

	/**
	 * Filters the Null or invalid entry(contains NaN value) from the array.
	 * 
	 * @param ll
	 * @return
	 */
	protected static Location[] filterNull( Location[] ll )
	{
		ArrayList al = new ArrayList( );
		for ( int i = 0; i < ll.length; i++ )
		{
			if ( Double.isNaN( ll[i].getX( ) ) || Double.isNaN( ll[i].getY( ) ) )
			{
				continue;
			}

			al.add( ll[i] );
		}

		if ( ll instanceof Location3D[] )
		{
			return (Location3D[]) al.toArray( new Location3D[0] );
		}
		else
		{
			return (Location[]) al.toArray( new Location[0] );
		}
	}

	/**
	 * Check the if the given value is NaN.
	 * 
	 * @param value
	 * @return
	 */
	protected static boolean isNaN( Object value )
	{
		return ( !( value instanceof Number ) || Double.isNaN( ( (Number) value ).doubleValue( ) ) );
	}

	/**
	 * Returns if the right-left mode is enabled.
	 * 
	 * @return
	 */
	protected boolean isRightToLeft( )
	{
		if ( rtc == null )
		{
			return false;
		}
		return rtc.isRightToLeft( );
	}

	/**
	 * Returns if current palette is from the category series.
	 * 
	 * @return
	 */
	protected boolean isPaletteByCategory( )
	{
		return ( cm.getLegend( ).getItemType( ).getValue( ) == LegendItemType.CATEGORIES );
	}

	/**
	 * Switch Anchor value due to right-left setting.
	 * 
	 * @param anchor
	 * @return
	 */
	public Anchor switchAnchor( Anchor anchor )
	{
		if ( anchor != null && isRightToLeft( ) )
		{
			switch ( anchor.getValue( ) )
			{
				case Anchor.EAST :
					anchor = Anchor.WEST_LITERAL;
					break;
				case Anchor.NORTH_EAST :
					anchor = Anchor.NORTH_WEST_LITERAL;
					break;
				case Anchor.SOUTH_EAST :
					anchor = Anchor.SOUTH_WEST_LITERAL;
					break;
				case Anchor.WEST :
					anchor = Anchor.EAST_LITERAL;
					break;
				case Anchor.NORTH_WEST :
					anchor = Anchor.NORTH_EAST_LITERAL;
					break;
				case Anchor.SOUTH_WEST :
					anchor = Anchor.SOUTH_EAST_LITERAL;
					break;
			}
		}
		return anchor;
	}

	/**
	 * Switch Position value due to right-left setting.
	 * 
	 * @param po
	 * @return
	 */
	public Position switchPosition( Position po )
	{
		if ( po != null && isRightToLeft( ) )
		{
			if ( po == Position.RIGHT_LITERAL )
			{
				po = Position.LEFT_LITERAL;
			}
			else if ( po == Position.LEFT_LITERAL )
			{
				po = Position.RIGHT_LITERAL;
			}
		}
		return po;
	}

	/**
	 * Switch TextAlignment value due to right-left setting.
	 * 
	 * @param ta
	 * @return
	 */
	public TextAlignment switchTextAlignment( TextAlignment ta )
	{
		if ( ta != null && isRightToLeft( ) )
		{
			if ( ta.getHorizontalAlignment( ) == HorizontalAlignment.LEFT_LITERAL )
			{
				ta.setHorizontalAlignment( HorizontalAlignment.RIGHT_LITERAL );
			}
			else if ( ta.getHorizontalAlignment( ) == HorizontalAlignment.RIGHT_LITERAL )
			{
				ta.setHorizontalAlignment( HorizontalAlignment.LEFT_LITERAL );
			}
		}
		return ta;
	}

	/**
	 * Returns if interactivity is enabled on the model.
	 * 
	 * @return
	 */
	public boolean isInteractivityEnabled( )
	{
		return ( cm.getInteractivity( ) == null || cm.getInteractivity( )
				.isEnable( ) );
	}

	/**
	 * Returns if the corresponding category entry is filtered as minslice in
	 * legend. Subclass should override this method to implement their own
	 * legend strategy.
	 * 
	 * @return return null if no minslice applied or minslice feature is not
	 *         supported.
	 */
	public int[] getFilteredMinSliceEntry( DataSetIterator dsi )
	{
		// no filter by default.
		return null;
	}

	/**
	 * Updates the tranlucency of the fill according to series setting.
	 * 
	 * @param fill
	 * @param se
	 */
	public void updateTranslucency( Fill fill, Series se )
	{
		if ( se != null
				&& se.isTranslucent( )
				&& ( fill instanceof ColorDefinition ) )
		{
			( (ColorDefinition) fill ).setTransparency( (int) ( OVERRIDE_TRANSPARENCY * 255d / 100d ) );
		}
	}

}