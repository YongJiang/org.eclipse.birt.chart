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

package org.eclipse.birt.chart.event;

import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Polygon3DRenderEvent
 */
public final class Polygon3DRenderEvent extends PolygonRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = -6572679563207168795L;

	private boolean bDoubleSided = false;

	private double dBrightness = 1d;

	private boolean bBehind = false;

	private Object3D object3D;

	private Fill runtimeBackground;

	/**
	 * @param oSource
	 */
	public Polygon3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * Returns true if double sided polygons (not enclosing a volume)
	 * 
	 * @return
	 */
	public boolean isDoubleSided( )
	{
		return bDoubleSided;
	}

	/**
	 * @param value
	 */
	public void setDoubleSided( boolean value )
	{
		this.bDoubleSided = value;
	}

	/**
	 * @return
	 */
	public boolean isBehind( )
	{
		return bBehind;
	}

	/**
	 * @param value
	 */
	public void setBehind( boolean value )
	{
		this.bBehind = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PolygonRenderEvent#setBackground(org.eclipse.birt.chart.model.attribute.Fill)
	 */
	public void setBackground( Fill ifBackground )
	{
		super.setBackground( ifBackground );

		runtimeBackground = ifBackground;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PolygonRenderEvent#getBackground()
	 */
	public Fill getBackground( )
	{
		return runtimeBackground;
	}

	/**
	 * @return
	 */
	public double getBrightness( )
	{
		return dBrightness;
	}

	/**
	 * Sets the brightness of this polygon, the value ranges 0.0 - 1.0.
	 */
	public void setBrightness( double value )
	{
		dBrightness = value;

		if ( _ifBackground instanceof ColorDefinition )
		{
			ColorDefinition cdf = ColorDefinitionImpl.copyInstance( (ColorDefinition) _ifBackground );

			cdf.set( (int) ( cdf.getRed( ) * dBrightness ),
					(int) ( cdf.getGreen( ) * dBrightness ),
					(int) ( cdf.getBlue( ) * dBrightness ) );

			runtimeBackground = cdf;
		}
	}

	/**
	 * Note that setPoints3D must be called with the points in the right order:
	 * that is needed for the right orientation of the polygon. Points must be
	 * given in anti-clockwise order if looking at the face from outside the
	 * enclosed volume, and so that two adjacent points define a line of the
	 * polygon. A minimum of three points is required, less will throw an
	 * IllegalArgumentException, three consecutive points cannot be aligned.
	 * 
	 * @param la
	 *            Sets the co-ordinates for each point that defines the polygon
	 */
	public final void setPoints3D( Location3D[] loa ) throws ChartException
	{

		if ( loa.length < 3 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.RENDERING,
					"exception.3D.points.length.less.than.3", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE ) );
		}
		object3D = new Object3D( loa );
	}

	/**
	 * 
	 * @return Returns the co-ordinates for each point in the polygon
	 */
	public Location3D[] getPoints3D( )
	{
		return object3D.getLocation3D( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#prepare2D(double,
	 *      double)
	 */
	public void prepare2D( double xOffset, double yOffset )
	{
		Location[] points = object3D.getPoints2D( xOffset, yOffset );
		setPoints( points );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		final Polygon3DRenderEvent pre = new Polygon3DRenderEvent( source );
		if ( object3D != null )
		{
			pre.object3D = new Object3D( object3D );
		}

		if ( _lia != null )
		{
			pre.setOutline( LineAttributesImpl.copyInstance( _lia ) );
		}

		if ( _ifBackground != null )
		{
			pre.setBackground( (Fill) EcoreUtil.copy( _ifBackground ) );
		}

		pre.bDoubleSided = bDoubleSided;
		pre.dBrightness = dBrightness;
		pre.bBehind = bBehind;

		return pre;
	}

	// must be implemented as the object is cached and reused.
	public void reset( )
	{
		if ( object3D != null )
		{
			object3D.reset( );
		}
		this.bDoubleSided = false;
		this.dBrightness = 1;
		this.bBehind = false;
		this.runtimeBackground = null;
		super.reset( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#getObject3D()
	 */
	public Object3D getObject3D( )
	{
		return object3D;
	}
}
