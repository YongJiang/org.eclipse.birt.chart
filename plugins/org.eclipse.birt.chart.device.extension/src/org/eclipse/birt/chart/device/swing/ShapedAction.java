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

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Action;

/**
 * This class provides a shape definition and an associated action that is
 * invoked when interaction occurs with a chart rendered on a SWING device.
 */
public final class ShapedAction
{

	private final StructureSource _oSource;

	private final Shape _sh;

	private final Map _triggers = new HashMap();


	/**
	 * This constructor supports polygon shapes Future shapes (and corresponding
	 * constructors) will be added later
	 * 
	 * @param source
	 * @param loa
	 * @param clipping
	 */
	ShapedAction( StructureSource oSource, Location[] loa, Shape clipping )
	{
		_oSource = oSource;
		final int[][] i2a = SwingRendererImpl.getCoordinatesAsInts( loa );
		if ( clipping != null )
		{
			Area ar1 = new Area( clipping );
			Area ar2 = new Area( new Polygon( i2a[0], i2a[1], loa.length ) );
			ar2.intersect( ar1 );
			_sh = ar2;
		}
		else
		{
			_sh = new Polygon( i2a[0], i2a[1], loa.length );
		}
	}


	/**
	 * This constructor supports shape definition via an ellipse
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param clipping
	 */
	ShapedAction( StructureSource oSource, Bounds boEllipse, Shape clipping )
	{
		_oSource = oSource;
		if ( clipping != null )
		{
			Area ar1 = new Area( clipping );
			Area ar2 = new Area( new Ellipse2D.Double( boEllipse.getLeft( ),
					boEllipse.getTop( ),
					boEllipse.getWidth( ),
					boEllipse.getHeight( ) ) );
			ar2.intersect( ar1 );
			_sh = ar2;
		}
		else
		{
			_sh = new Ellipse2D.Double( boEllipse.getLeft( ),
					boEllipse.getTop( ),
					boEllipse.getWidth( ),
					boEllipse.getHeight( ) );
		}
	}

	/**
	 * This constructor supports shape definition via an elliptical arc
	 * @param oSource
	 * @param boEllipse
	 * @param dStart
	 * @param dExtent
	 * @param iArcType
	 * @param clipping
	 */
	ShapedAction( StructureSource oSource, Bounds boEllipse, double dStart,
			double dExtent, int iArcType,  Shape clipping )
	{
		_oSource = oSource;
		if ( clipping != null )
		{
			Area ar1 = new Area( clipping );
			Area ar2 = new Area( new Arc2D.Double( boEllipse.getLeft( ),
					boEllipse.getTop( ),
					boEllipse.getWidth( ),
					boEllipse.getHeight( ),
					dStart,
					dExtent,
					iArcType ) );
			ar2.intersect( ar1 );
			_sh = ar2;
		}
		else
		{
			_sh = new Arc2D.Double( boEllipse.getLeft( ),
					boEllipse.getTop( ),
					boEllipse.getWidth( ),
					boEllipse.getHeight( ),
					dStart,
					dExtent,
					iArcType );
		}
	}


	/**
	 * Returns the shape associated with current ShapedAction.
	 * 
	 * @return
	 */
	public final Shape getShape( )
	{
		return _sh;
	}

	/**
	 * Returns the action associated with current ShapedAction.
	 * 
	 * @return
	 */
	public final Action getActionForCondition( TriggerCondition condition )
	{
		return (Action)_triggers.get( condition );
	}


	/**
	 * Returns the source object associated with current ShapedAction.
	 * 
	 * @return
	 */
	public final StructureSource getSource( )
	{
		return _oSource;
	}

	public void add( TriggerCondition tc, Action ac )
	{
		_triggers.put( tc, ac );
		
	}
}