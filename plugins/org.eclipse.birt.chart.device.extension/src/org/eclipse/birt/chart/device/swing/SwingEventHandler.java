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

package org.eclipse.birt.chart.device.swing;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComponent;

import org.eclipse.birt.chart.device.ICallBackNotifier;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.util.DeviceUtil;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.ULocale;

/**
 * Provides a reference implementation into handling events generated on a SWING
 * JComponent with a rendered chart.
 */
public final class SwingEventHandler implements
		MouseListener,
		MouseMotionListener,
		KeyListener
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swing" ); //$NON-NLS-1$

	private ShapedAction saTooltip = null;

	private ShapedAction saHighlighted = null;

	private final LinkedHashMap lhmAllTriggers;

	private final IUpdateNotifier iun;

	private final ULocale lcl;

	/**
	 * The constructor.
	 * 
	 * @param _lhmAllTriggers
	 * @param _jc
	 * @param _lcl
	 */
	SwingEventHandler( LinkedHashMap _lhmAllTriggers, IUpdateNotifier _jc,
			ULocale _lcl )
	{
		lhmAllTriggers = _lhmAllTriggers;
		iun = _jc;
		lcl = _lcl;
	}

	private final boolean isLeftButton( MouseEvent e )
	{
		return ( ( e.getButton( ) & MouseEvent.BUTTON1 ) == MouseEvent.BUTTON1 );
	}

	private final List getActionsForConditions( TriggerCondition[] tca )
	{
		if ( tca == null || tca.length == 0 )
		{
			return null;
		}

		ArrayList al = new ArrayList( );

		for ( int i = 0; i < tca.length; i++ )
		{
			ArrayList tal = (ArrayList) lhmAllTriggers.get( tca[i] );

			if ( tal != null )
			{
				al.addAll( tal );
			}
		}

		if ( al.size( ) > 0 )
		{
			return al;
		}

		return null;
	}

	private void handleAction( List al, Object event )
	{
		handleAction( al, event, true );
	}

	private synchronized void handleAction( List al, Object event,
			boolean cleanState )
	{
		if ( al == null || event == null )
		{
			return;
		}

		ShapedAction sa;
		Shape sh;
		Action ac;

		Point p = null;

		if ( event instanceof MouseEvent )
		{
			p = ( (MouseEvent) event ).getPoint( );
		}

		if ( event instanceof KeyEvent )
		{
			// TODO filter key ?
		}

		boolean bFound = false;

		// POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
		LOOP: for ( int i = 0; i < al.size( ); i++ )
		{
			sa = (ShapedAction) al.get( i );
			sh = sa.getShape( );
			if ( p == null || sh.contains( p ) )
			{
				ac = sa.getAction( );
				final StructureSource src = (StructureSource) sa.getSource( );

				switch ( ac.getType( ).getValue( ) )
				{
					case ActionType.URL_REDIRECT :
						final URLValue uv = (URLValue) ac.getValue( );
						logger.log( ILogger.INFORMATION,
								Messages.getString( "info.redirect.url", lcl ) //$NON-NLS-1$
										+ uv.getBaseUrl( ) );
						DeviceUtil.openURL( uv.getBaseUrl( ) );
						break LOOP;

					case ActionType.SHOW_TOOLTIP :

						if ( sa != saTooltip )
						{
							hideTooltip( );
						}
						saTooltip = sa;
						bFound = true;
						showTooltip( saTooltip );
						break LOOP;

					case ActionType.TOGGLE_VISIBILITY :
						if ( src.getType( ) == StructureType.SERIES
								|| src.getType( ) == StructureType.SERIES_DATA_POINT )
						{
							final Series seRT;
							if ( src.getType( ) == StructureType.SERIES )
							{
								seRT = (Series) src.getSource( );
							}
							else
							{
								seRT = (Series) ( (WrappedStructureSource) src ).getParent( )
										.getSource( );
							}
							logger.log( ILogger.INFORMATION,
									Messages.getString( "info.toggle.visibility", //$NON-NLS-1$
											lcl )
											+ seRT );
							Series seDT = null;
							try
							{
								seDT = findDesignTimeSeries( seRT );
							}
							catch ( ChartException oosx )
							{
								logger.log( oosx );
								return;
							}
							seDT.setVisible( !seDT.isVisible( ) );
							iun.regenerateChart( );
							break LOOP;
						}
						break;

					case ActionType.TOGGLE_DATA_POINT_VISIBILITY :
						if ( src.getType( ) == StructureType.SERIES
								|| src.getType( ) == StructureType.SERIES_DATA_POINT )
						{
							final Series seRT;
							if ( src.getType( ) == StructureType.SERIES )
							{
								seRT = (Series) src.getSource( );
							}
							else
							{
								seRT = (Series) ( (WrappedStructureSource) src ).getParent( )
										.getSource( );
							}
							logger.log( ILogger.INFORMATION,
									Messages.getString( "info.toggle.datapoint.visibility", //$NON-NLS-1$
											lcl )
											+ seRT );
							Series seDT = null;
							try
							{
								seDT = findDesignTimeSeries( seRT );
							}
							catch ( ChartException oosx )
							{
								logger.log( oosx );
								return;
							}
							seDT.getLabel( ).setVisible( !seDT.getLabel( )
									.isVisible( ) );
							iun.regenerateChart( );
							break LOOP;
						}
						break;

					case ActionType.HIGHLIGHT :
						bFound = true;

						boolean newRegion = saHighlighted == null;

						if ( !newRegion )
						{
							if ( p == null
									|| !saHighlighted.getShape( ).contains( p ) )
							{
								newRegion = true;
							}
						}

						if ( newRegion )
						{
							saHighlighted = sa;
							toggleHighlight( sa );
							break LOOP;
						}
						break;

					case ActionType.CALL_BACK :
						if ( iun instanceof ICallBackNotifier )
						{
							final CallBackValue cv = (CallBackValue) ac.getValue( );
							( (ICallBackNotifier) iun ).callback( event,
									sa.getSource( ),
									cv );
						}
						else
						{
							logger.log( ILogger.WARNING,
									Messages.getString( "info.improper.callback.notifier", //$NON-NLS-1$
											new Object[]{
												iun
											},
											lcl ) );
						}
						break LOOP;
				}
			}
		}

		if ( !bFound && saTooltip != null )
		{
			hideTooltip( );
			saTooltip = null;
		}

		if ( cleanState || ( !bFound && saHighlighted != null ) )
		{
			saHighlighted = null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE CLICKS ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
				TriggerCondition.MOUSE_CLICK_LITERAL,
				TriggerCondition.ONCLICK_LITERAL
		} );

		handleAction( al, e );
	}

	/**
	 * Locates a design-time series corresponding to a given cloned run-time
	 * series.
	 * 
	 * @param seRT
	 * @return
	 */
	private final Series findDesignTimeSeries( Series seRT )
			throws ChartException
	{

		Series seDT = null;

		final Chart cmRT = iun.getRunTimeModel( );
		final Chart cmDT = iun.getDesignTimeModel( );

		if ( cmDT instanceof ChartWithAxes )
		{
			final ChartWithAxes cwaRT = (ChartWithAxes) cmRT;
			final ChartWithAxes cwaDT = (ChartWithAxes) cmDT;

			Axis[] axaBase = cwaRT.getPrimaryBaseAxes( );
			Axis axBase = axaBase[0];
			Axis[] axaOrthogonal = cwaRT.getOrthogonalAxes( axBase, true );
			EList elSD, elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = axaBase[0].getSeriesDefinitions( );
			for ( j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				elSE = sd.getSeries( );
				for ( k = 0; k < elSE.size( ); k++ )
				{
					se = (Series) elSE.get( k );
					if ( seRT == se )
					{
						bFound = true;
						break;
					}
				}
				if ( bFound )
				{
					break;
				}
			}

			if ( !bFound )
			{
				// LOCATE INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN RUN TIME
				// MODEL
				for ( i = 0; i < axaOrthogonal.length; i++ )
				{
					elSD = axaOrthogonal[i].getSeriesDefinitions( );
					for ( j = 0; j < elSD.size( ); j++ )
					{
						sd = (SeriesDefinition) elSD.get( j );
						elSE = sd.getSeries( );
						for ( k = 0; k < elSE.size( ); k++ )
						{
							se = (Series) elSE.get( k );
							if ( seRT == se )
							{
								bFound = true;
								break;
							}
						}
						if ( bFound )
						{
							break;
						}
					}
					if ( bFound )
					{
						break;
					}
				}
			}

			if ( !bFound )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								lcl.toLocale( ) ) );
			}

			// MAP TO INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN DESIGN TIME
			// MODEL
			axaBase = cwaDT.getPrimaryBaseAxes( );
			axBase = axaBase[0];
			axaOrthogonal = cwaDT.getOrthogonalAxes( axBase, true );
			if ( i == -1 )
			{
				elSD = axaBase[0].getSeriesDefinitions( );
			}
			else
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
			}
			sd = (SeriesDefinition) elSD.get( j );
			elSE = sd.getSeries( );
			seDT = (Series) elSE.get( k );
		}
		else if ( cmDT instanceof ChartWithoutAxes )
		{
			final ChartWithoutAxes cwoaRT = (ChartWithoutAxes) cmRT;
			final ChartWithoutAxes cwoaDT = (ChartWithoutAxes) cmDT;

			EList elSD, elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = cwoaRT.getSeriesDefinitions( );
			for ( j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				elSE = sd.getSeries( );
				for ( k = 0; k < elSE.size( ); k++ )
				{
					se = (Series) elSE.get( k );
					if ( seRT == se )
					{
						bFound = true;
						break;
					}
				}
				if ( bFound )
				{
					break;
				}
			}

			if ( !bFound )
			{
				i = 1;
				elSD = ( (SeriesDefinition) cwoaRT.getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( );

				for ( j = 0; j < elSD.size( ); j++ )
				{
					sd = (SeriesDefinition) elSD.get( j );
					elSE = sd.getSeries( );
					for ( k = 0; k < elSE.size( ); k++ )
					{
						se = (Series) elSE.get( k );
						if ( seRT == se )
						{
							bFound = true;
							break;
						}
					}
					if ( bFound )
					{
						break;
					}
				}
			}

			if ( !bFound )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								lcl.toLocale( ) ) );
			}

			if ( i == -1 )
			{
				elSD = cwoaDT.getSeriesDefinitions( );
			}
			else
			{
				elSD = ( (SeriesDefinition) cwoaDT.getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( );
			}
			sd = (SeriesDefinition) elSD.get( j );
			elSE = sd.getSeries( );
			seDT = (Series) elSE.get( k );
		}

		return seDT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE DOWN ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONMOUSEDOWN_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE UP ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONMOUSEUP_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved( MouseEvent e )
	{
		final Point p = e.getPoint( );

		// 1. CHECK FOR MOUSE-CLICK TRIGGERS
		List al = getActionsForConditions( new TriggerCondition[]{
				TriggerCondition.MOUSE_CLICK_LITERAL,
				TriggerCondition.ONCLICK_LITERAL
		} );

		if ( al != null )
		{
			ShapedAction sa;
			Shape sh;

			// POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
			boolean bFound = false;
			for ( int i = 0; i < al.size( ); i++ )
			{
				sa = (ShapedAction) al.get( i );
				sh = sa.getShape( );
				if ( sh.contains( p ) )
				{
					( (JComponent) iun.peerInstance( ) ).setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
					bFound = true;
					break;
				}
			}

			if ( !bFound )
			{
				( (JComponent) iun.peerInstance( ) ).setCursor( Cursor.getDefaultCursor( ) );
			}
		}

		// 2. CHECK FOR MOUSE-HOVER CONDITION
		al = getActionsForConditions( new TriggerCondition[]{
				TriggerCondition.MOUSE_HOVER_LITERAL,
				TriggerCondition.ONMOUSEMOVE_LITERAL,
				TriggerCondition.ONMOUSEOVER_LITERAL
		} );

		handleAction( al, e, false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed( KeyEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR KEY DOWN ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONKEYDOWN_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased( KeyEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR KEY UP/PRESS ONLY
		List al = getActionsForConditions( new TriggerCondition[]{
				TriggerCondition.ONKEYUP_LITERAL,
				TriggerCondition.ONKEYPRESS_LITERAL
		} );

		handleAction( al, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped( KeyEvent e )
	{
	}

	private final void hideTooltip( )
	{
		( (JComponent) iun.peerInstance( ) ).setToolTipText( null );
	}

	private final void showTooltip( ShapedAction sa )
	{
		Action ac = sa.getAction( );
		TooltipValue tv = (TooltipValue) ac.getValue( );

		// Handle character conversion of \n in the tooltip
		String tooltip = tv.getText( ).replaceAll( "\\\n", "<br>" ); //$NON-NLS-1$//$NON-NLS-2$
		if ( !tooltip.equals( tv.getText( ) ) )
		{
			tooltip = "<html>" + tooltip + "</html>"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			tooltip = tv.getText( );
		}
		( (JComponent) iun.peerInstance( ) ).setToolTipText( tooltip );
	}

	private final void toggleHighlight( ShapedAction sa )
	{
		if ( sa == null )
		{
			return;
		}

		final StructureSource src = (StructureSource) sa.getSource( );

		if ( src.getType( ) == StructureType.SERIES
				|| src.getType( ) == StructureType.SERIES_DATA_POINT )
		{
			final Series seRT;
			if ( src.getType( ) == StructureType.SERIES )
			{
				seRT = (Series) src.getSource( );
			}
			else
			{
				seRT = (Series) ( (WrappedStructureSource) src ).getParent( )
						.getSource( );
			}
			logger.log( ILogger.INFORMATION,
					Messages.getString( "info.toggle.visibility", //$NON-NLS-1$
							lcl ) + seRT );
			Series seDT = null;
			SeriesDefinition sdDT = null;
			try
			{
				seDT = findDesignTimeSeries( seRT ); // LOCATE
				if ( seDT.eContainer( ) instanceof SeriesDefinition )
				{
					sdDT = (SeriesDefinition) seDT.eContainer( );
				}
			}
			catch ( ChartException oosx )
			{
				logger.log( oosx );
				return;
			}

			boolean highlight = iun.getContext( seDT ) == null;

			boolean changed = false;
			if ( seDT != null )
			{
				changed = performHighlight( seDT, highlight );
			}
			if ( sdDT != null )
			{
				for ( Iterator itr = sdDT.getSeriesPalette( )
						.getEntries( )
						.iterator( ); itr.hasNext( ); )
				{
					Object entry = itr.next( );
					if ( entry instanceof ColorDefinition )
					{
						performHighlight( (ColorDefinition) entry, highlight );
						changed = true;
					}
				}
			}

			if ( highlight )
			{
				if ( iun.getContext( seDT ) == null )
				{
					iun.putContext( seDT, Boolean.TRUE );
				}
			}
			else
			{
				iun.removeContext( seDT );
			}

			if ( changed )
			{
				iun.regenerateChart( );
			}
		}
	}

	private boolean performHighlight( Series se, boolean highlighted )
	{
		boolean changed = false;

		List lineContext;

		if ( highlighted )
		{
			lineContext = new ArrayList( );
		}
		else
		{
			Object context = iun.getContext( se );

			if ( context instanceof List )
			{
				lineContext = (List) context;
			}
			else
			{
				lineContext = new ArrayList( );
			}
		}

		int idx = 0;

		for ( Iterator itr = se.eAllContents( ); itr.hasNext( ); )
		{
			Object obj = itr.next( );

			if ( obj instanceof ColorDefinition
					&& !( ( (ColorDefinition) obj ).eContainer( ) instanceof LineAttributes ) )
			{
				performHighlight( (ColorDefinition) obj, highlighted );

				changed = true;
			}
			else if ( obj instanceof LineAttributes )
			{
				LineAttributes la = (LineAttributes) obj;

				if ( highlighted )
				{
					int[] ls = new int[5];
					ls[0] = la.getThickness( );
					ls[1] = la.getColor( ).getRed( );
					ls[2] = la.getColor( ).getGreen( );
					ls[3] = la.getColor( ).getBlue( );
					ls[4] = la.getColor( ).getTransparency( );

					lineContext.add( ls );

					la.setThickness( 3 );
					la.getColor( ).set( 255, 255, 255, 127 );

					changed = true;
				}
				else
				{
					if ( idx < lineContext.size( ) )
					{
						Object context = lineContext.get( idx );

						if ( context instanceof int[]
								&& ( (int[]) context ).length > 4 )
						{
							int[] ls = (int[]) context;

							la.setThickness( ls[0] );
							la.getColor( ).setRed( ls[1] );
							la.getColor( ).setGreen( ls[2] );
							la.getColor( ).setBlue( ls[3] );
							la.getColor( ).setTransparency( ls[4] );

							changed = true;
						}
					}
				}

				idx++;
			}
		}

		if ( highlighted && lineContext.size( ) > 0 )
		{
			iun.putContext( se, lineContext );
		}

		return changed;
	}

	private void performHighlight( ColorDefinition cd, boolean highlighted )
	{
		if ( cd != null )
		{
			if ( highlighted )
			{
				cd.setRed( ( cd.getRed( ) + 255 ) / 2 );
				cd.setGreen( ( cd.getGreen( ) + 255 ) / 2 );
				cd.setBlue( ( cd.getBlue( ) + 255 ) / 2 );
			}
			else
			{
				cd.setRed( Math.max( 0, cd.getRed( ) * 2 - 255 ) );
				cd.setGreen( Math.max( 0, cd.getGreen( ) * 2 - 255 ) );
				cd.setBlue( Math.max( 0, cd.getBlue( ) * 2 - 255 ) );
			}
		}
	}

}