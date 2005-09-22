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

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Line3DRenderEvent
 */
public final class Line3DRenderEvent extends LineRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = 33812052466380930L;

	private Object3D object3D;

	/**
	 * @param oSource
	 */
	public Line3DRenderEvent( Object oSource )
	{
		super( oSource );
		object3D = new Object3D( 2 );
	}

	/**
	 * @param start
	 */
	public void setStart3D( Location3D start )
	{
		object3D.getVectors( )[0] = new Vector( start );
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setStart3D( double x, double y, double z )
	{
		object3D.getVectors( )[0] = new Vector( x, y, z, true );
	}

	/**
	 * not a live object
	 * 
	 * @return
	 */
	public Location3D getStart3D( )
	{
		return object3D.getLocation3D( )[0];
	}

	/**
	 * @param start
	 */
	public void setEnd3D( Location3D end )
	{
		object3D.getVectors( )[1] = new Vector( end );
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setEnd3D( double x, double y, double z )
	{
		object3D.getVectors( )[1] = new Vector( x, y, z, true );
	}

	/**
	 * not a live object
	 * 
	 * @return
	 */
	public Location3D getEnd3D( )
	{
		return object3D.getLocation3D( )[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		Line3DRenderEvent lre = new Line3DRenderEvent( source );
		if ( lia != null )
		{
			lre.setLineAttributes( (LineAttributes) EcoreUtil.copy( lia ) );
		}
		if ( object3D != null )
		{
			lre.object3D = new Object3D( object3D );
		}
		return lre;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#getObject3D()
	 */
	public Object3D getObject3D( )
	{
		return object3D;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#prepare2D(double, double)
	 */
	public void prepare2D( double xOffset, double yOffset )
	{
		Location[] points = object3D.getPoints2D( xOffset, yOffset );
		setStart( points[0] );
		setEnd( points[1] );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#reset()
	 */
	public void reset( )
	{
		object3D = new Object3D( 2 );
		super.reset( );
	}

}
