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

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;

/**
 * This class provides a region definition and an associated action that is
 * invoked when interaction occurs with a chart rendered on a SWT device.
 */
public final class RegionAction
{

	private final StructureSource _oSource;

	private Cursor cursor = null;

	/**
	 * the bounding box used to describe the RegionAction's working area
	 */
	private Rectangle _bb;

	private final Action _ac;

	private static final IGObjectFactory goFactory = GObjectFactory.instance( );

	private RegionAction( StructureSource source, Rectangle bb, Action ac )
	{
		this._oSource = source;
		this._ac = ac;
		this._bb = bb;
	}

	/**
	 * RegionAction constructor taking a polygon to define the region
	 * 
	 * @param oSource
	 *            StructureSource
	 * @param loa
	 *            Polygon points
	 * @param ac
	 *            Action
	 * @param dTranslateX
	 *            X Translation to apply on polygon coordinates
	 * @param dTranslateY
	 *            Y Translation to apply on polygon coordinates
	 * @param dScale
	 *            Scale to apply on polygon coordinates
	 * @param clipping
	 *            Clipping area, points outside it will be clipped
	 */
	RegionAction( StructureSource oSource, Location[] loa, Action ac,
			double dTranslateX, double dTranslateY, double dScale,
			Region clipping )
	{
		_oSource = oSource;
		final int[] i2a = SwtRendererImpl.getCoordinatesAsInts( loa,
				SwtRendererImpl.TRUNCATE,
				dTranslateX,
				dTranslateY,
				dScale );
		Region sh = new Region( );
		sh.add( i2a );
		if ( clipping != null )
		{
			sh.intersect( clipping );
		}
		_ac = ac;

		_bb = sh.getBounds( );
		sh.dispose( );
	}

	/**
	 * This constructor supports shape definition via a rectangle.
	 * 
	 * @param oSource
	 *            StructureSource
	 * @param bo
	 *            Rectangle
	 * @param ac
	 *            Action
	 * @param dTranslateX
	 *            X translation to apply to rectangle
	 * @param dTranslateY
	 *            Y translation to apply to rectangle
	 * @param dScale
	 *            scale to apply to rectangle
	 * @param clipping
	 *            Clipping area, points outside it will be clipped
	 */
	RegionAction( StructureSource oSource, Bounds bo, Action ac,
			double dTranslateX, double dTranslateY, double dScale,
			Region clipping )
	{
		_oSource = oSource;

		bo = goFactory.copyOf( bo );
		bo.translate( dTranslateX, dTranslateY );
		bo.scale( dScale );

		Rectangle rect = new Rectangle( (int) bo.getLeft( ),
				(int) bo.getTop( ),
				(int) bo.getWidth( ),
				(int) bo.getHeight( ) );

		Region sh = new Region( );
		sh.add( rect );
		if ( clipping != null )
		{
			sh.intersect( clipping );
		}
		_ac = ac;

		_bb = sh.getBounds( );
		sh.dispose( );
	}

	/**
	 * This constructor supports shape definition via an elliptical arc
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param dStart
	 * @param dExtent
	 * @param iArcType
	 * @param ac
	 */
	RegionAction( StructureSource oSource, Bounds boEllipse, double dStart,
			double dExtent, boolean bSector, Action ac, double dTranslateX,
			double dTranslateY, double dScale, Region clipping )
	{
		_oSource = oSource;

		boEllipse = goFactory.copyOf( boEllipse );
		boEllipse.translate( dTranslateX, dTranslateY );
		boEllipse.scale( dScale );

		double x = boEllipse.getLeft( );
		double y = boEllipse.getTop( );
		double width = boEllipse.getWidth( );
		double height = boEllipse.getHeight( );

		Path ph = new Path( Display.getDefault( ) );
		ph.addArc( (float) x,
				(float) y,
				(float) width,
				(float) height,
				(float) dStart,
				(float) dExtent );

		if ( bSector )
		{
			ph.lineTo( (float) ( x + width / 2 ), (float) ( y + height / 2 ) );
		}

		ph.close( );

		if ( clipping != null )
		{
			// TODO intersect with clipping
		}

		_ac = ac;

		// use bounding box of Path
		float[] b = new float[4];
		ph.getBounds( b );
		_bb = new Rectangle( (int) b[0], (int) b[1], (int) b[2], (int) b[3] );
		ph.dispose( );
	}

	/**
	 * @return The action associated with current ShapedAction.
	 */
	public final Action getAction( )
	{
		return _ac;
	}

	/**
	 * @return The source object associated with current ShapedAction
	 */
	public final StructureSource getSource( )
	{
		return _oSource;
	}

	/**
	 * Note the Region object is value copied, others are just reference copy.
	 * <b>The invoker must call <code>dispose()</code> explicitly when this is
	 * not used anymore</b>.
	 * 
	 * @return A copy of current RegionAction
	 */
	public RegionAction copy( )
	{
		Rectangle nbb = null;

		if ( _bb != null )
		{
			nbb = new Rectangle( _bb.x, _bb.y, _bb.width, _bb.height );
		}

		return new RegionAction( _oSource, nbb, _ac );
	}

	/**
	 * Returns if the current region contains given point.
	 * 
	 * @param p
	 * @param gc
	 * @return if the current region contains given point
	 */
	public boolean contains( Point p, GC gc )
	{
		return contains( p.x, p.y, gc );
	}

	/**
	 * Returns if the current region contains given x,y.
	 * 
	 * @param x
	 * @param y
	 * @param gc
	 * @return if the current region contains given x,y
	 */
	public boolean contains( double x, double y, GC gc )
	{
		if ( _bb != null )
		{
			// we are operating on pixels (swt), there should be
			// no problems down-casting to int
			return _bb.contains( (int) x, (int) y );
		}

		return false;
	}

	/**
	 * Dispose the resources.
	 */
	public void dispose( )
	{
		// empty
	}

	/**
	 * Returns if current region is empty.
	 * 
	 * @return if current region is empty
	 */
	public boolean isEmpty( )
	{
		if ( _bb != null )
		{
			return _bb.isEmpty( );
		}

		return true;
	}
	
	
	/**
	 * Returns mouse cursor of the region.
	 * 
	 * @return
	 */
	public Cursor getCursor( )
	{
		return cursor;
	}

	
	/**
	 * Set mouse cursor of the region.
	 * 
	 * @param cursor
	 */
	public void setCursor( Cursor cursor )
	{
		this.cursor = cursor;
	}
}
