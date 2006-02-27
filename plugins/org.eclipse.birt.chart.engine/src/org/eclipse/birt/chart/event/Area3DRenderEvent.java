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

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 * Area3DRenderEvent
 */
public class Area3DRenderEvent extends AreaRenderEvent implements
		I3DRenderEvent
{

	public Area3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	private static final long serialVersionUID = -308233971777301084L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#getObject3D()
	 */
	public Object3D getObject3D( ) throws ChartException
	{
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.UNSUPPORTED_FEATURE,
				"exception.unsupported.area3d.getOjbect3D", //$NON-NLS-1$ 
				new Object[]{
					this
				},
				ResourceBundle.getBundle( Messages.ENGINE, Locale.getDefault( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#prepare2D(double,
	 *      double)
	 */
	public void prepare2D( double xOffset, double yOffset )
	{
		for ( int i = 0; i < getElementCount( ); i++ )
		{
			PrimitiveRenderEvent pre = getElement( i );

			if ( pre instanceof I3DRenderEvent )
			{
				( (I3DRenderEvent) pre ).prepare2D( xOffset, yOffset );
			}
		}
	}

}
