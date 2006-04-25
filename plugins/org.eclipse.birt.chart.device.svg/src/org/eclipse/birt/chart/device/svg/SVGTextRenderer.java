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

package org.eclipse.birt.chart.device.svg;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.ITextMetrics;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * Provides convenience methods for rendering rotated text with configurable
 * attributes on a SVG graphics context.
 */
final class SVGTextRenderer implements IConstants
{

	/**
	 * 
	 */
	private static SVGTextRenderer _tr = null;

	/**
	 * 
	 */
	private SVGDisplayServer _sxs = null;

	/**
	 * 
	 */
	private SVGTextRenderer( )
	{

	}

	/**
	 * 
	 * @return
	 */
	public synchronized static final SVGTextRenderer instance(
			SVGDisplayServer sxs )
	{
		if ( _tr == null )
		{
			_tr = new SVGTextRenderer( );
		}
		_tr._sxs = sxs;
		return _tr;
	}

	/**
	 * This method renders the 'shadow' at an offset from the text 'rotated
	 * rectangle' subsequently rendered.
	 * 
	 * @param ipr
	 * @param iLabelPosition
	 *            The position of the label w.r.t. the location specified by
	 *            'lo'
	 * @param lo
	 *            The location (specified as a 2d point) where the text is to be
	 *            rendered
	 * @param la
	 *            The chart model structure containing the encapsulated text
	 *            (and attributes) to be rendered
	 */
	public final void renderShadowAtLocation( IPrimitiveRenderer idr,
			int iLabelPosition, Location lo, Label la ) throws ChartException
	{
		final ColorDefinition cdShadow = la.getShadowColor( );
		if ( cdShadow == null )
		{
			throw new ChartException( ChartDeviceExtensionPlugin.ID,
					ChartException.RENDERING,
					"exception.undefined.shadow.color", //$NON-NLS-1$
					Messages.getResourceBundle( _sxs.getULocale( ) ) );
		}

		final Graphics2D g2d = (Graphics2D) ( (IDeviceRenderer) idr ).getGraphicsContext( );
		g2d.setFont( (java.awt.Font) _sxs.createFont( la.getCaption( )
				.getFont( ) ) );

		switch ( iLabelPosition )
		{
			case ABOVE :
				showTopValue( idr, lo, la, true );
				break;

			case BELOW :
				showBottomValue( idr, lo, la, true );
				break;

			case LEFT :
				showLeftValue( idr, lo, la, true );
				break;

			case RIGHT :
				showRightValue( idr, lo, la, true );
				break;
		}
	}

	/**
	 * 
	 * @param ipr
	 * @param iLabelPosition
	 *            IConstants. LEFT, RIGHT, ABOVE or BELOW
	 * @param lo
	 *            POINT WHERE THE CORNER OF THE ROTATED RECTANGLE (OR EDGE
	 *            CENTERED) IS RENDERED
	 * @param la
	 * @throws RenderingException
	 */
	public final void renderTextAtLocation( IPrimitiveRenderer ipr,
			int iLabelPosition, Location lo, Label la ) throws ChartException
	{
		final ColorDefinition cdText = la.getCaption( ).getColor( );
		if ( cdText == null )
		{
			throw new ChartException( ChartDeviceExtensionPlugin.ID,
					ChartException.RENDERING,
					"exception.undefined.text.color", //$NON-NLS-1$
					Messages.getResourceBundle( _sxs.getULocale( ) ) );
		}

		final Graphics2D g2d = (Graphics2D) ( (IDeviceRenderer) ipr ).getGraphicsContext( );
		g2d.setFont( (java.awt.Font) _sxs.createFont( la.getCaption( )
				.getFont( ) ) );

		switch ( iLabelPosition )
		{
			case ABOVE :
				if ( ChartUtil.isShadowDefined( la ) )
				{
					showTopValue( ipr, lo, la, true );
				}
				showTopValue( ipr, lo, la, false );
				break;

			case BELOW :
				if ( ChartUtil.isShadowDefined( la ) )
				{
					showBottomValue( ipr, lo, la, true );
				}
				showBottomValue( ipr, lo, la, false );
				break;

			case LEFT :
				if ( ChartUtil.isShadowDefined( la ) )
				{
					showLeftValue( ipr, lo, la, true );
				}
				showLeftValue( ipr, lo, la, false );
				break;

			case RIGHT :
				if ( ChartUtil.isShadowDefined( la ) )
				{
					showRightValue( ipr, lo, la, true );
				}
				showRightValue( ipr, lo, la, false );
				break;
		}

	}

	/**
	 * 
	 * @param idr
	 * @param boBlock
	 * @param taBlock
	 * @param la
	 */
	public final void renderTextInBlock( IDeviceRenderer idr, Bounds boBlock,
			TextAlignment taBlock, Label la ) throws ChartException
	{
		Text t = la.getCaption( );
		String sText = t.getValue( );
		FontDefinition fd = t.getFont( );
		ColorDefinition cdText = t.getColor( );
		if ( cdText == null )
		{
			throw new ChartException( ChartDeviceExtensionPlugin.ID,
					ChartException.RENDERING,
					"exception.undefined.text.color", //$NON-NLS-1$
					Messages.getResourceBundle( _sxs.getULocale( ) ) );
		}
		IDisplayServer xs = idr.getDisplayServer( );
		Graphics2D g2d = (Graphics2D) idr.getGraphicsContext( );
		g2d.setFont( (java.awt.Font) xs.createFont( fd ) );

		la.getCaption( ).setValue( sText );
		BoundingBox bb = null;
		try
		{
			bb = Methods.computeBox( xs, ABOVE, la, 0, 0 );
		}
		catch ( IllegalArgumentException uiex )
		{
			throw new ChartException( ChartDeviceExtensionPlugin.ID,
					ChartException.RENDERING,
					uiex );
		}
		if ( taBlock == null )
		{
			taBlock = AttributeFactory.eINSTANCE.createTextAlignment( );
			taBlock.setHorizontalAlignment( HorizontalAlignment.CENTER_LITERAL );
			taBlock.setVerticalAlignment( VerticalAlignment.CENTER_LITERAL );
		}
		HorizontalAlignment haBlock = taBlock.getHorizontalAlignment( );
		VerticalAlignment vaBlock = taBlock.getVerticalAlignment( );

		switch ( haBlock.getValue( ) )
		{
			case HorizontalAlignment.CENTER :
				bb.setLeft( boBlock.getLeft( )
						+ ( boBlock.getWidth( ) - bb.getWidth( ) )
						/ 2 );
				break;
			case HorizontalAlignment.LEFT :
				bb.setLeft( boBlock.getLeft( ) );
				break;
			case HorizontalAlignment.RIGHT :
				bb.setLeft( boBlock.getLeft( )
						+ boBlock.getWidth( )
						- bb.getWidth( ) );
				break;
		}

		switch ( vaBlock.getValue( ) )
		{
			case VerticalAlignment.TOP :
				bb.setTop( boBlock.getTop( ) );
				break;
			case VerticalAlignment.CENTER :
				bb.setTop( boBlock.getTop( )
						+ ( boBlock.getHeight( ) - bb.getHeight( ) )
						/ 2 );
				break;
			case VerticalAlignment.BOTTOM :
				bb.setTop( boBlock.getTop( )
						+ boBlock.getHeight( )
						- bb.getHeight( ) );
				break;
		}

		bb.setLeft( bb.getLeft( ) + bb.getHotPoint( ) );
		if ( ChartUtil.isShadowDefined( la ) )
		{
			showTopValue( idr, LocationImpl.create( bb.getLeft( ), bb.getTop( )
					+ bb.getHeight( ) ), la, true );
		}
		showTopValue( idr, LocationImpl.create( bb.getLeft( ), bb.getTop( )
				+ bb.getHeight( ) ), la, false );
	}

	/**
	 * 
	 * @param g2d
	 * @param dX
	 * @param dY
	 * @param sText
	 * @param dAngleInDegrees
	 */
	private final void showLeftValue( IPrimitiveRenderer ipr, Location lo,
			Label la, boolean bShadow )
	{
		Graphics2D g2d = (Graphics2D) ( (IDeviceRenderer) ipr ).getGraphicsContext( );
		double dX = lo.getX( ), dY = lo.getY( );
		FontDefinition fd = la.getCaption( ).getFont( );
		double dAngleInDegrees = fd.getRotation( );
		if ( bShadow ) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = la.getShadowColor( ) != null;
		}
		Color clrText = (Color) _sxs.getColor( la.getCaption( ).getColor( ) );
		Color clrBackground = null;
		if ( la.getBackground( ) != null )
		{
			clrBackground = (Color) _sxs.getColor( (ColorDefinition) la.getBackground( ) );
		}
		final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
		final double dSineTheta = ( Math.sin( dAngleInRadians ) );
		final double dCosTheta = ( Math.cos( dAngleInRadians ) );

		final ITextMetrics itm = new SVGTextMetrics( _sxs, la );
		final double dFW = itm.getFullWidth( );
		final double dH = itm.getHeight( );
		final double dD = itm.getDescent( );
		final double dFH = itm.getFullHeight( );
		double dXOffset = 0, dW = 0;
		final int iLC = itm.getLineCount( );
		final Insets ins = la.getInsets( )
				.scaledInstance( _sxs.getDpiResolution( ) / 72d );
		SVGTextLayout tl;

		final HorizontalAlignment ha = la.getCaption( )
				.getFont( )
				.getAlignment( )
				.getHorizontalAlignment( );
		final boolean bRightAligned = ha.getValue( ) == HorizontalAlignment.RIGHT;
		final boolean bCenterAligned = ha.getValue( ) == HorizontalAlignment.CENTER;

		double dRotateX = ( dX - dFW );
		double dRotateY = ( dY + dH / 2 );
		dX -= dFW;
		dY += dH / 2;

		if ( dAngleInDegrees == 0 )
		{
			double dYHalfOffset = ( dFH + dH ) / 2d;
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dYHalfOffset )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dYHalfOffset, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY
									- dYHalfOffset
									+ ins.getTop( )
									+ dH
									* ( i + 1 ) - dD ) );
				}

				// RENDER THE OUTLINE
				renderOutline( ipr, la.getOutline( ), r2d );
			}
		}

		// DRAW POSITIVE ANGLE (> 0)
		else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
		{
			double dDeltaX = dFW - dFW * dCosTheta;
			double dDeltaY = dFW * dSineTheta + dH / 2;
			dX += dDeltaX;
			dY -= dDeltaY;

			g2d.rotate( dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY );
			if ( bShadow )
			{
				// RENDER THE SHADOW
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dFH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dFH )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dFH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dFH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < itm.getLineCount( ); i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( iLC - i - 1 );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( ( dY - dD ) - ( dH * i ) ) - ins.getBottom( ) ) );
				}

				// RENDER THE OUTLINE
				renderOutline( ipr, la.getOutline( ), r2d );
			}
			g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY
					- dDeltaY );
		}

		// DRAW NEGATIVE ANGLE (< 0)
		else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
		{
			double dDeltaX = dFW - dFW * dCosTheta - dH * dSineTheta;
			double dDeltaY = dFW * dSineTheta + dH / 2 - dH * dCosTheta;
			dX += dDeltaX;
			dY -= dDeltaY;
			g2d.rotate( dAngleInRadians, dRotateX + dDeltaX, dRotateY - dDeltaY );
			if ( bShadow )
			{
				// RENDER THE SHADOW
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dH ) + 3 + dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dH, dFW, dFH );

				// RENDER THE BACKGROUND FILL
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( ( dY - dD ) + ( dH * i ) ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}
			g2d.rotate( -dAngleInRadians, dRotateX + dDeltaX, dRotateY
					- dDeltaY );
		}

		// VERTICALLY UP
		else if ( dAngleInDegrees == 90 )
		{
			double dDeltaX = dFW;
			double dDeltaY = ( dFW - dH ) / 2;
			dX += dDeltaX;
			dY += dDeltaY;
			g2d.rotate( dAngleInRadians, dX, dY );
			if ( bShadow )
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dFH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dFH )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dFH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dFH, dFW, dFH );

				// RENDER THE BACKGROUND FILL
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}

					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( ( dY - dD ) - ( dH * ( iLC - i - 1 ) ) ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}
			g2d.rotate( -dAngleInRadians, dX, dY );
		}

		// VERTICALLY DOWN
		else if ( dAngleInDegrees == -90 )
		{
			double dDeltaX = dFW - dH;
			double dDeltaY = ( dFW + dH ) / 2;
			dX += dDeltaX;
			dY -= dDeltaY;
			g2d.rotate( dAngleInRadians, dX, dY );
			if ( bShadow )
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dFH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dFH )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dFH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dH, dFW, dFH );

				// RENDER THE BACKGROUND FILL
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( ( dY - dD ) + ( dH * i ) ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}
			g2d.rotate( -dAngleInRadians, dX, dY );
		}
		itm.dispose( );
	}

	/**
	 * 
	 * @param g2d
	 * @param f
	 * @param dX
	 * @param dY
	 * @param sText
	 * @param iAngleInDegrees
	 */
	private final void showRightValue( IPrimitiveRenderer ipr, Location lo,
			Label la, boolean bShadow )
	{
		Graphics2D g2d = (Graphics2D) ( (IDeviceRenderer) ipr ).getGraphicsContext( );
		double dX = lo.getX( ), dY = lo.getY( );
		FontDefinition fd = la.getCaption( ).getFont( );
		double dAngleInDegrees = fd.getRotation( );
		if ( bShadow ) // UPDATE TO FALSE IF SHADOW COLOR UNDEFINED BUT SHADOW
		// REQUESTED FOR
		{
			bShadow = la.getShadowColor( ) != null;
		}
		Color clrText = (Color) _sxs.getColor( la.getCaption( ).getColor( ) );
		Color clrBackground = null;
		if ( la.getBackground( ) != null )
		{
			clrBackground = (Color) _sxs.getColor( (ColorDefinition) la.getBackground( ) );
		}

		// dX += 2;
		dY += 1;

		final ITextMetrics itm = new SVGTextMetrics( _sxs, la );
		final double dFW = itm.getFullWidth( );
		final double dH = itm.getHeight( );
		final double dD = itm.getDescent( );
		final double dFH = itm.getFullHeight( );
		double dXOffset = 0, dW = 0;
		final int iLC = itm.getLineCount( );
		final Insets ins = la.getInsets( )
				.scaledInstance( _sxs.getDpiResolution( ) / 72d );
		SVGTextLayout tl;

		final HorizontalAlignment ha = la.getCaption( )
				.getFont( )
				.getAlignment( )
				.getHorizontalAlignment( );
		final boolean bRightAligned = ha.getValue( ) == HorizontalAlignment.RIGHT;
		final boolean bCenterAligned = ha.getValue( ) == HorizontalAlignment.CENTER;

		double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
		int iRotateX = (int) dX;
		int iRotateY = (int) ( dY + dH / 2 );
		dY += dH / 2;

		// HORIZONTAL TEXT
		if ( dAngleInDegrees == 0 )
		{
			double dYHalfOffset = ( dFH + dH ) / 2d;
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dYHalfOffset )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dYHalfOffset, dFW, dFH );

				// RENDER THE BACKGROUND FILL
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY
									- dYHalfOffset
									+ ins.getTop( )
									+ dH
									* ( i + 1 ) - dD )
					// (float)(((dY - dD) - ((iLC - i) * dH - (iLC + 1) * dH/2))
					// + ins.getTop())
					);
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}
		}

		// DRAW POSITIVE ANGLE (> 0)
		else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
		{
			double dDeltaX = dH * Math.sin( dAngleInRadians );
			double dDeltaY = dH * Math.cos( dAngleInRadians ) - dH / 2;
			dX -= dDeltaX;
			dY += dDeltaY;
			g2d.rotate( dAngleInRadians, iRotateX - dDeltaX, iRotateY + dDeltaY );

			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dH ) + 3 + dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX,
						( dY - dH ),
						dFW,
						dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( dY - dD + dH * i ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, iRotateX - dDeltaX, iRotateY
					+ dDeltaY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// DRAW NEGATIVE ANGLE (< 0)
		else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
		{
			double dDeltaY = -dH / 2;
			dY += dDeltaY;
			g2d.rotate( dAngleInRadians, iRotateX, iRotateY + dDeltaY );

			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dFH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dFH )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dFH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dFH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( dY - dD - dH * ( iLC - i - 1 ) ) - ins.getBottom( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, iRotateX, iRotateY + dDeltaY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// VERTICALLY UP
		else if ( dAngleInDegrees == 90 )
		{
			double dDeltaX = dH;
			double dDeltaY = ( dFW - dH ) / 2;
			dX += dDeltaX;
			dY += dDeltaY;
			g2d.rotate( dAngleInRadians, dX, dY );

			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dH ) + 3 + dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( dY - dD + dH * i ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// VERTICALLY DOWN
		else if ( dAngleInDegrees == -90 )
		{
			double dDeltaX = 0;
			double dDeltaY = ( dFW + dH ) / 2;
			dX += dDeltaX;
			dY -= dDeltaY;
			g2d.rotate( dAngleInRadians, dX, dY );

			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dH ) + 3 + dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( dY - dD + dH * i ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}
		itm.dispose( );
	}

	/**
	 * 
	 * @param g2d
	 * @param f
	 * @param dX
	 * @param dY
	 * @param sText
	 * @param iAngleInDegrees
	 */
	private final void showBottomValue( IPrimitiveRenderer ipr, Location lo,
			Label la, boolean bShadow )
	{
		Graphics2D g2d = (Graphics2D) ( (IDeviceRenderer) ipr ).getGraphicsContext( );
		double dX = lo.getX( ), dY = lo.getY( );
		FontDefinition fd = la.getCaption( ).getFont( );
		// Color clrShadow = bShadow ? (Color)
		// _sxs.getColor(la.getShadowColor()) : null;
		double dAngleInDegrees = fd.getRotation( );
		Color clrText = (Color) _sxs.getColor( la.getCaption( ).getColor( ) );
		Color clrBackground = null;
		if ( la.getBackground( ) != null )
		{
			clrBackground = (Color) _sxs.getColor( (ColorDefinition) la.getBackground( ) );
		}
		double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );

		final ITextMetrics itm = new SVGTextMetrics( _sxs, la );
		final double dFW = itm.getFullWidth( );
		final double dH = itm.getHeight( );
		final double dD = itm.getDescent( );
		final double dFH = itm.getFullHeight( );
		double dXOffset = 0, dW = 0;
		final int iLC = itm.getLineCount( );
		final Insets ins = la.getInsets( )
				.scaledInstance( _sxs.getDpiResolution( ) / 72d );
		SVGTextLayout tl;

		final HorizontalAlignment ha = la.getCaption( )
				.getFont( )
				.getAlignment( )
				.getHorizontalAlignment( );
		final boolean bRightAligned = ha.getValue( ) == HorizontalAlignment.RIGHT;
		final boolean bCenterAligned = ha.getValue( ) == HorizontalAlignment.CENTER;

		dX -= dFW / 2;
		dY += dH;

		// HORIZONTAL TEXT
		if ( dAngleInDegrees == 0 )
		{
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dH ) + 3 + dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY - dD + dH * i + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// DRAW IT AT A POSITIVE ANGLE
		else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
		{
			double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
			double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );
			double dDeltaX = dFW * dCosTheta - dH * dSineTheta - dFW / 2.0;
			double dDeltaY = dH * dCosTheta + dFW * dSineTheta - dH;

			dX -= dDeltaX;
			dY += dDeltaY;
			g2d.rotate( dAngleInRadians, dX, dY );

			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dH ) + 3 + dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY - dD + dH * i + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
		}

		// DRAW IT AT A NEGATIVE ANGLE
		else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
		{
			dX += dFW / 2;
			g2d.rotate( dAngleInRadians, dX, dY - dH );
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dH ) + 3 + dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY - dD + dH * i + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY - dH );
		}

		// VERTICALLY UP
		else if ( dAngleInDegrees == 90 )
		{
			double dYHalfOffset = ( dFH + dH ) / 2.0;
			double dDeltaX = ( dFW + dH ) / 2;
			double dDeltaY = ( dFW - dH );
			dX += dDeltaX;
			dY += dDeltaY;
			g2d.rotate( dAngleInRadians, dX, dY );
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dYHalfOffset )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dYHalfOffset, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( ( dY - dD ) - ( ( iLC - i ) * dH - ( iLC + 1 )
									* dH
									/ 2 ) ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// VERTICALLY DOWN
		else if ( dAngleInDegrees == -90 )
		{
			dX += dFW / 2;
			dY -= dH;

			double dYHalfOffset = ( dFH + dH ) / 2d;
			double dDeltaX = dYHalfOffset - dFH / 2d;
			dX -= dDeltaX;
			g2d.rotate( dAngleInRadians, dX, dY );
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dYHalfOffset )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dYHalfOffset, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < iLC; i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( dY - dD )
									- dYHalfOffset
									+ dH
									* ( i + 1 ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
		}
		itm.dispose( );
	}

	/**
	 * 
	 * @param g2d
	 * @param f
	 * @param dX
	 * @param dY
	 * @param sText
	 * @param iAngleInDegrees
	 */
	private final void showTopValue( IPrimitiveRenderer ipr, Location lo,
			Label la, boolean bShadow )
	{
		final Graphics2D g2d = (Graphics2D) ( (IDeviceRenderer) ipr ).getGraphicsContext( );
		double dX = lo.getX( ), dY = lo.getY( );
		final FontDefinition fd = la.getCaption( ).getFont( );
		// final Color clrShadow = bShadow ? (Color)
		// _sxs.getColor(la.getShadowColor()) : null;
		final double dAngleInDegrees = fd.getRotation( );
		final Color clrText = (Color) _sxs.getColor( la.getCaption( )
				.getColor( ) );
		Color clrBackground = null;
		if ( la.getBackground( ) != null )
		{
			clrBackground = (Color) _sxs.getColor( (ColorDefinition) la.getBackground( ) );
		}
		double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );

		final ITextMetrics itm = new SVGTextMetrics( _sxs, la );
		final double dFW = itm.getFullWidth( );
		final double dH = itm.getHeight( );
		final double dD = itm.getDescent( );
		final double dFH = itm.getFullHeight( );
		double dXOffset = 0, dW = 0;
		final int iLC = itm.getLineCount( );
		final Insets ins = la.getInsets( )
				.scaledInstance( _sxs.getDpiResolution( ) / 72d );
		SVGTextLayout tl;

		final HorizontalAlignment ha = la.getCaption( )
				.getFont( )
				.getAlignment( )
				.getHorizontalAlignment( );
		final boolean bRightAligned = ha.getValue( ) == HorizontalAlignment.RIGHT;
		final boolean bCenterAligned = ha.getValue( ) == HorizontalAlignment.CENTER;

		dX -= dFW / 2;

		// HORIZONTAL TEXT
		if ( dAngleInDegrees == 0 )
		{
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dFH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dFH )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dFH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX,
						( dY - dFH ),
						dFW,
						dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );

				for ( int i = 0; i < itm.getLineCount( ); i++ )
				{
					// tl = new TextLayout(itm.getLine(iLC - i - 1),
					// g2d.getFont(), g2d.getFontRenderContext());
					tl = ( (SVGTextMetrics) itm ).getLayout( iLC - i - 1 );

					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY - dD - dH * i - ins.getBottom( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// DRAW IT AT A POSITIVE ANGLE
		else if ( dAngleInDegrees > 0 && dAngleInDegrees < 90 )
		{
			double dDeltaX = dFW / 2;

			dX += dDeltaX;
			g2d.rotate( dAngleInRadians, dX, dY );

			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dFH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dFH )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dFH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX,
						( dY - dFH ),
						dFW,
						dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < itm.getLineCount( ); i++ )
				{
					tl = new SVGTextLayout( itm.getLine( iLC - i - 1 ),
							g2d.getFont( ).getAttributes( ),
							g2d.getFontRenderContext( ) );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY - dD - dH * i - ins.getBottom( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// DRAW IT AT A NEGATIVE ANGLE
		else if ( dAngleInDegrees < 0 && dAngleInDegrees > -90 )
		{
			double dCosTheta = Math.abs( Math.cos( dAngleInRadians ) );
			double dSineTheta = Math.abs( Math.sin( dAngleInRadians ) );
			dX -= dFW / 2 - ( dFW - dFW * dCosTheta );
			dY -= dFW * dSineTheta;
			g2d.rotate( dAngleInRadians, dX, dY );
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dFH ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dFH )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dFH ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dFH, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < itm.getLineCount( ); i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( iLC - i - 1 );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( dY - dD - dH * i - ins.getBottom( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
			/*
			 * final RotatedRectangle rr = computePolygon(IConstants.ABOVE, la,
			 * lo.getX(), lo.getY()); g2d.setColor(Color.blue); g2d.draw(rr);
			 * final BoundingBox bb = computeBox(IConstants.ABOVE, la,
			 * lo.getX(), lo.getY()); renderBox(g2d, bb, Color.black, null);
			 */
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// VERTICALLY UP
		else if ( dAngleInDegrees == 90 )
		{
			double dYHalfOffset = ( dFH + dH ) / 2.0;
			double dDeltaX = ( dFW + dH ) / 2;
			dX += dDeltaX;
			g2d.rotate( dAngleInRadians, dX, dY );
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dYHalfOffset )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dYHalfOffset, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < itm.getLineCount( ); i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( ( dY - dD ) - ( ( itm.getLineCount( ) - i )
									* dH - ( iLC + 1 ) * dH / 2 ) ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		// VERTICALLY DOWN
		else if ( dAngleInDegrees == -90 )
		{
			double dYHalfOffset = ( dFH + dH ) / 2.0;
			double dDeltaX = ( dFW - dH ) / 2;
			double dDeltaY = dFW;
			dX += dDeltaX;
			dY -= dDeltaY;
			g2d.rotate( dAngleInRadians, dX, dY );
			if ( bShadow ) // RENDER THE SHADOW
			{
				g2d.setPaint( new GradientPaint( new Point2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3 ),
						(Color) _sxs.getColor( la.getShadowColor( ) ),
						new Point2D.Double( dX + 3 + dFW, ( dY - dYHalfOffset )
								+ 3
								+ dFH ),
						(Color) _sxs.getColor( la.getShadowColor( )
								.translucent( ) ) ) );
				g2d.fill( new Rectangle2D.Double( dX + 3,
						( dY - dYHalfOffset ) + 3,
						dFW,
						dFH ) );
			}
			else
			{
				final Rectangle2D.Double r2d = new Rectangle2D.Double( dX, dY
						- dYHalfOffset, dFW, dFH );

				// RENDER THE BACKGROUND
				if ( clrBackground != null )
				{
					g2d.setColor( clrBackground );
					g2d.fill( r2d );
				}

				// RENDER THE TEXT
				g2d.setColor( clrText );
				for ( int i = 0; i < itm.getLineCount( ); i++ )
				{
					tl = ( (SVGTextMetrics) itm ).getLayout( i );
					if ( bRightAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + dFW - dW - ins.getRight( );
					}
					else if ( bCenterAligned )
					{
						dW = tl.getBounds( ).getWidth( );
						dXOffset = -ins.getLeft( ) + ( dFW - dW ) / 2;
					}
					tl.draw( g2d,
							(float) ( dX + dXOffset + ins.getLeft( ) ),
							(float) ( ( ( dY - dD ) - ( ( itm.getLineCount( ) - i )
									* dH - ( iLC + 1 ) * dH / 2 ) ) + ins.getTop( ) ) );
				}

				// RENDER THE OUTLINE/BORDER
				renderOutline( ipr, la.getOutline( ), r2d );
			}

			// UNDO THE 'ROTATED' STATE OF THE GRAPHICS CONTEXT
			g2d.rotate( -dAngleInRadians, dX, dY );
			// crossHairs(g2d, (int)dX, (int)dY);
		}

		itm.dispose( );
	}

	// private static final void renderBox(Graphics2D g2d, BoundingBox bb, Color
	// cFG, Color cBG)
	// {
	// if (cBG != null)
	// {
	// g2d.setColor(cBG);
	// g2d.fillRect((int) bb.getLeft(), (int) bb.getTop(), (int) bb.getWidth(),
	// (int) bb.getHeight());
	// }
	// g2d.setColor(cFG);
	// g2d.drawRect((int) bb.getLeft(), (int) bb.getTop(), (int) bb.getWidth(),
	// (int) bb.getHeight());
	// }

	private final void renderOutline( IPrimitiveRenderer ipr,
			LineAttributes lia, Rectangle2D.Double r2d )
	{
		if ( lia != null && lia.isVisible( ) && lia.getColor( ) != null )
		{
			Graphics2D g2d = (Graphics2D) ( (IDeviceRenderer) ipr ).getGraphicsContext( );
			Stroke sPrevious = null;
			final ColorDefinition cd = lia.getColor( );
			final Stroke sCurrent = ( (SVGRendererImpl) ipr ).getCachedStroke( lia );
			if ( sCurrent != null ) // SOME STROKE DEFINED?
			{
				sPrevious = g2d.getStroke( );
				g2d.setStroke( sCurrent );
			}
			g2d.setColor( (Color) _sxs.getColor( cd ) );
			g2d.draw( r2d );
			if ( sPrevious != null ) // RESTORE PREVIOUS STROKE
			{
				g2d.setStroke( sPrevious );
			}
		}
	}
}