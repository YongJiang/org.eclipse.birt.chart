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

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * PolygonRenderEvent
 */
public final class PolygonRenderEvent extends PrimitiveRenderEvent
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 7825900630615976817L;

	private Location[] _loa;

	private LineAttributes _lia;

	private Fill _ifBackground;

	/**
	 * @param oSource
	 */
	public PolygonRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * 
	 * @param la
	 *            Sets the co-ordinates for each point that defines the polygon
	 */
	public final void setPoints( Location[] la )
	{
		_loa = la;
	}

	/**
	 * 
	 * @return Returns the co-ordinates for each point in the polygon
	 */
	public final Location[] getPoints( )
	{
		return _loa;
	}

	/**
	 * @return Returns the background fill attributes for the polygon
	 */
	public Fill getBackground( )
	{
		return _ifBackground;
	}

	/**
	 * @param cd
	 *            The background fill attributes for the polygon
	 */
	public void setBackground( Fill ifBackground )
	{
		_ifBackground = ifBackground;
	}

	/**
	 * @return Returns the polygon outline attributes.
	 */
	public LineAttributes getOutline( )
	{
		return _lia;
	}

	/**
	 * @param ls
	 *            The polygon outline attributes
	 */
	public void setOutline( LineAttributes lia )
	{
		_lia = lia;
	}

	/**
	 *  
	 */
	public Bounds getBounds( ) throws ChartException
	{
		final Bounds bo = BoundsImpl.create( 0, 0, 0, 0 );
		bo.updateFrom( _loa );
		return bo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public final PrimitiveRenderEvent copy( )
	{
		final PolygonRenderEvent pre = new PolygonRenderEvent( source );
		if ( _loa != null )
		{
			final Location[] loa = new Location[this._loa.length];
			for ( int i = 0; i < loa.length; i++ )
			{
				loa[i] = (Location) EcoreUtil.copy( _loa[i] );
			}
			pre.setPoints( loa );
		}

		if ( _lia != null )
		{
			pre.setOutline( (LineAttributes) EcoreUtil.copy( _lia ) );
		}

		if ( _ifBackground != null )
		{
			pre.setBackground( (Fill) EcoreUtil.copy( _ifBackground ) );
		}

		pre.setDepth( getDepth( ) );
		return pre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart.device.IDeviceRenderer)
	 */
	public final void draw( IDeviceRenderer idr ) throws ChartException
	{
		idr.drawPolygon( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart.device.IDeviceRenderer)
	 */
	public final void fill( IDeviceRenderer idr ) throws ChartException
	{
		idr.fillPolygon( this );
	}
}