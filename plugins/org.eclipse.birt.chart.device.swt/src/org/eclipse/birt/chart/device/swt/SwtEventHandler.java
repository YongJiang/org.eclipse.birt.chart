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

package org.eclipse.birt.chart.device.swt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.device.ICallBackNotifier;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.swt.i18n.Messages;
import org.eclipse.birt.chart.device.swt.util.SwtUtil;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.CallBackValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.render.InteractiveRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.ibm.icu.util.ULocale;

/**
 * SwtEventHandler
 */
class SwtEventHandler
		implements
			MouseListener,
			MouseMoveListener,
			MouseTrackListener,
			KeyListener,
			FocusListener
{

	private static final ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swt" ); //$NON-NLS-1$

	private final Cursor hand_cursor;

	private final LinkedHashMap lhmAllTriggers;

	private final IUpdateNotifier iun;

	private RegionAction raTooltip = null;

	private final ULocale lcl;

	private final GC _gc;
	
	private InteractiveRenderer iv = null;

	private StructureSource srcHighlight;

	private StructureSource srcToggleDataPoint;

	private StructureSource srcToggleVisibility;

	/**
	 * The constructor.
	 * @param Interactive Renderer
	 * @param swtRendererImpl 
	 * 
	 * @param _lhmAllTriggers
	 * @param _jc
	 * @param _lcl
	 */
	SwtEventHandler(InteractiveRenderer iv, LinkedHashMap _lhmAllTriggers, IUpdateNotifier _jc,
			ULocale _lcl )
	{

		lhmAllTriggers = _lhmAllTriggers;
		iun = _jc;
		lcl = _lcl;
		hand_cursor = new Cursor( Display.getDefault( ), SWT.CURSOR_HAND );
		_gc = new GC( Display.getDefault( ) );
		this.iv = iv;
		
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

	private final boolean isLeftButton( MouseEvent e )
	{
		return ( e.button == 1 );
	}

	private void handleAction( TriggerCondition[] tgArray, Object event )
	{
		handleAction( tgArray, event, true );
	}
	

	


	private synchronized void handleAction( TriggerCondition[] tgArray, Object event,
			boolean cleanState )
	{
		List al = getActionsForConditions( tgArray );
		
		if ( al == null || event == null )
		{
			return;
		}

		RegionAction ra = null;
		Action ac = null;

		Point p = null;

		if ( event instanceof MouseEvent )
		{
			p = new Point( ( (MouseEvent) event ).x, ( (MouseEvent) event ).y );
		}

		if ( event instanceof KeyEvent )
		{
			// TODO filter key ?
		}

		boolean bFound = false;

		// POLL EACH EVENT REGISTERED
		LOOP: for ( int i = 0; i < al.size( ); i++ )
		{
			ra = (RegionAction) al.get( i );
			if ( p == null || ra.contains( p, _gc ) )
			{
				ac = ra.getAction( );
				final StructureSource src = ra.getSource( );

				switch ( ac.getType( ).getValue( ) )
				{
					case ActionType.URL_REDIRECT :
						final URLValue uv = (URLValue) ac.getValue( );
						logger.log( ILogger.INFORMATION,
								Messages.getString( "SwtEventHandler.info.redirect.url", lcl ) //$NON-NLS-1$
										+ uv.getBaseUrl( ) );
						SwtUtil.openURL( uv.getBaseUrl( ) );
						break LOOP;

					case ActionType.SHOW_TOOLTIP :

						if ( ra != raTooltip )
						{
							hideTooltip( );
						}
						raTooltip = ra;
						bFound = true;
						showTooltip( raTooltip );
						break LOOP;

					case ActionType.TOGGLE_VISIBILITY :
						
							bFound = true;
							srcToggleVisibility = handleGraphicAction( src,
									srcToggleVisibility,
									ActionType.TOGGLE_VISIBILITY_LITERAL,
									cleanState );

							break LOOP;
						

					case ActionType.TOGGLE_DATA_POINT_VISIBILITY :
						
							bFound = true;
							srcToggleDataPoint = handleGraphicAction( src,
									srcToggleDataPoint,
									ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL,
									cleanState );

							break LOOP;
						

					case ActionType.HIGHLIGHT :

						bFound = true;
						srcHighlight = handleGraphicAction( src,
								srcHighlight,
								ActionType.HIGHLIGHT_LITERAL,
								cleanState );

						break LOOP;
						

					case ActionType.CALL_BACK :
						if ( iun instanceof ICallBackNotifier )
						{
							final CallBackValue cv = (CallBackValue) ac.getValue( );
							( (ICallBackNotifier) iun ).callback( event,
									ra.getSource( ),
									cv );
						}
						else
						{
							logger.log( ILogger.WARNING,
									Messages.getString( "SwtEventHandler.info.improper.callback.notifier", //$NON-NLS-1$
											new Object[]{
												iun
											},
											lcl ) );
						}
						break LOOP;
				}
			}
		}

		if ( !bFound )
			disableActions( getActionTypesForConditions( tgArray ) );
		



	}



	protected Set getActionTypesForConditions( TriggerCondition[] tca )
	{
		if ( tca == null || tca.length == 0 )
		{
			return null;
		}
		Set set = new HashSet( );
		
		for ( int i = 0; i < tca.length; i++ )
		{
			List tal = (List) lhmAllTriggers.get( tca[i] );
			if ( tal == null )
				continue;
			for ( Iterator iter = tal.iterator( ); iter.hasNext( ); )
			{
				RegionAction rg = (RegionAction) iter.next( );
				ActionType actionType = rg.getAction( ).getType( );
				set.add(  actionType  );
			}
		}
		return set;
			
	}

	// if the event was fired without new action disable the previous action
	
	private void disableActions( Set actions  )
	{
		if ( actions == null )
			return;
		
		for ( Iterator iter = actions.iterator( ); iter.hasNext( ); )
		{
			ActionType action = (ActionType)iter.next();
			if ( action == null )
				continue;
			switch ( action.getValue( ) )
			{
				case ActionType.SHOW_TOOLTIP:
					if ( raTooltip != null )
					{
						hideTooltip( );
						raTooltip = null;
					}
					break;
				case ActionType.HIGHLIGHT:
					if ( srcHighlight != null  )
					{
						iv.unregisterAction( srcHighlight, ActionType.HIGHLIGHT_LITERAL );
						srcHighlight = null;
						iun.repaintChart( );
					}
					break;
				case ActionType.TOGGLE_DATA_POINT_VISIBILITY:
					if ( srcToggleDataPoint != null  )
					{
						iv.unregisterAction( srcToggleDataPoint, ActionType.TOGGLE_DATA_POINT_VISIBILITY_LITERAL );
						srcToggleDataPoint = null;
						iun.repaintChart( );
					}
					break;
				case ActionType.TOGGLE_VISIBILITY:
					if ( srcToggleVisibility != null  )
					{
						iv.unregisterAction( srcToggleVisibility, ActionType.TOGGLE_VISIBILITY_LITERAL );
						srcToggleVisibility = null;
						iun.repaintChart( );
					}
					break;
			}
		}
	}

	private StructureSource handleGraphicAction( StructureSource src, StructureSource previousSrc, ActionType actionType, boolean cleanState )
	{
		if ( previousSrc == null )
		{
			previousSrc = src;
			iv.registerAction( src,
					actionType);
			iun.repaintChart( );
		}
		else if (! iv.getSource(src ).equals(iv.getSource(previousSrc) ))
		{	
			if ( actionType == ActionType.HIGHLIGHT_LITERAL )
			{
				// unhighlight previous region
				iv.unregisterAction( previousSrc, actionType );
				previousSrc = src;
				iv.registerAction( src, actionType );
			}
			else
			{
				previousSrc = src;
				// Register action only once.
				if ( iv.isRegisteredAction( src, actionType ) )
				{
					iv.unregisterAction( src, actionType );
				}
				else
				{
					iv.registerAction( src, actionType );
				}
			}
			iun.repaintChart( );
		}
		else
		{
			if ( cleanState )
			{
				iv.unregisterAction( previousSrc,
						actionType);
				previousSrc = null;
				iun.repaintChart( );
			}
		}
		return previousSrc;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE DOUBLE CLICK ONLY
		TriggerCondition[] tgArray =  new TriggerCondition[]{
			TriggerCondition.ONDBLCLICK_LITERAL
		} ;

		handleAction( tgArray, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE DOWN ONLY
		TriggerCondition[] tgArray =  new TriggerCondition[]{
			TriggerCondition.ONMOUSEDOWN_LITERAL
		} ;

		handleAction( tgArray, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp( MouseEvent e )
	{
		if ( !isLeftButton( e ) )
		{
			return;
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE UP/CLICK ONLY
		TriggerCondition[] tgArray =  new TriggerCondition[]{
				TriggerCondition.ONMOUSEUP_LITERAL,
				TriggerCondition.ONCLICK_LITERAL,
				TriggerCondition.MOUSE_CLICK_LITERAL,
		} ;

		handleAction( tgArray, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove( MouseEvent e )
	{
		// 1. CHECK FOR MOUSE-CLICK TRIGGERS
		List al = getActionsForConditions( new TriggerCondition[]{
			TriggerCondition.ONCLICK_LITERAL,
			TriggerCondition.ONMOUSEDOWN_LITERAL,
			TriggerCondition.MOUSE_CLICK_LITERAL
		} );

		if ( al != null )
		{
			RegionAction ra;

			// POLL EACH EVENT REGISTERED FOR MOUSE CLICKS
			boolean bFound = false;
			for ( int i = 0; i < al.size( ); i++ )
			{
				ra = (RegionAction) al.get( i );
				if ( ra.contains( e.x, e.y, _gc ) )
				{
					( (Composite) iun.peerInstance( ) ).setCursor( hand_cursor );
					bFound = true;
					break;
				}
			}

			if ( !bFound )
			{
				( (Composite) iun.peerInstance( ) ).setCursor( null );
			}
		}

		// FILTER OUT ALL TRIGGERS FOR MOUSE MOVE/OVER ONLY
		TriggerCondition[] tgArray =  new TriggerCondition[]{
				TriggerCondition.ONMOUSEMOVE_LITERAL,
				TriggerCondition.ONMOUSEOVER_LITERAL
		} ;

		if ( tgArray != null )
			handleAction( tgArray, e, false );

		
		
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed( KeyEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR MOUSE CLICKS ONLY
		TriggerCondition[] tg = new TriggerCondition[]{
			TriggerCondition.ONKEYDOWN_LITERAL
		} ;

		handleAction( tg, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased( KeyEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR KEY UP/PRESS ONLY
		TriggerCondition[] tg = new TriggerCondition[]{
				TriggerCondition.ONKEYUP_LITERAL,
				TriggerCondition.ONKEYPRESS_LITERAL
		} ;

		handleAction( tg, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseEnter( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipsse.swt.events.MouseEvent)
	 */
	public void mouseExit( MouseEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR MOUSE OUT ONLY
		TriggerCondition tg[] = new TriggerCondition[]{
			TriggerCondition.ONMOUSEOUT_LITERAL
		} ;

		handleAction( tg, e );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseHover( MouseEvent e )
	{
	}
	
	public void focusGained( FocusEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR FOCUS IN ONLY
		handleAction( new TriggerCondition[]{
			TriggerCondition.ONFOCUS_LITERAL
		} , e );
	}

	public void focusLost( FocusEvent e )
	{
		// FILTER OUT ALL TRIGGERS FOR FOCUS OUT ONLY
		handleAction(new TriggerCondition[]{
			TriggerCondition.ONBLUR_LITERAL
		} , e );
	}

	

	private final void hideTooltip( )
	{
		( (Composite) iun.peerInstance( ) ).setToolTipText( null );
	}

	private final void showTooltip( RegionAction ra )
	{
		Action ac = ra.getAction( );
		TooltipValue tv = (TooltipValue) ac.getValue( );
		String s = tv.getText( );

		( (Composite) iun.peerInstance( ) ).setToolTipText( s );
	}

	public final void dispose( )
	{
	

		hand_cursor.dispose( );
		_gc.dispose( );
	}

}