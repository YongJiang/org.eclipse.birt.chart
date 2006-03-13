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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.DeviceAdapter;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.render.BaseRenderer;
import org.eclipse.birt.chart.util.PluginSettings;

/**
 * Provides a reference implementation of a SWING device renderer. It translates
 * chart primitives into standard J2SDK AWT/SWING rendering primitives.
 */
public class SwingRendererImpl extends DeviceAdapter
{

	/**
	 * KEY = TRIGGER_CONDITION VAL = COLLECTION OF SHAPE-ACTION INSTANCES
	 */
	private final LinkedHashMap _lhmAllTriggers = new LinkedHashMap( );

	private final Hashtable _htLineStyles = new Hashtable( );

	protected Graphics2D _g2d;

	protected IDisplayServer _ids;

	private IUpdateNotifier _iun = null;

	private SwingEventHandler _eh = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.extension/swing" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public SwingRendererImpl( )
	{
		final PluginSettings ps = PluginSettings.instance( );
		try
		{
			_ids = ps.getDisplayServer( "ds.SWING" ); //$NON-NLS-1$
		}
		catch ( ChartException pex )
		{
			logger.log( pex );
		}

	}

	/**
	 * Free all allocated system resources.
	 */
	public void dispose( )
	{
		_lhmAllTriggers.clear( );

		if ( _iun != null )
		{
			Object obj = _iun.peerInstance( );

			if ( obj instanceof JComponent )
			{
				JComponent jc = (JComponent) obj;

				if ( _eh != null )
				{
					jc.removeMouseListener( _eh );
					jc.removeMouseMotionListener( _eh );
					jc.removeKeyListener( _eh );

					_eh = null;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setProperty( String sProperty, Object oValue )
	{
		if ( sProperty.equals( IDeviceRenderer.UPDATE_NOTIFIER ) )
		{
			_iun = (IUpdateNotifier) oValue;
			_lhmAllTriggers.clear( );
			Object obj = _iun.peerInstance( );

			if ( obj instanceof JComponent )
			{
				JComponent jc = (JComponent) obj;

				MouseListener[] mla = jc.getMouseListeners( );
				for ( int i = 0; i < mla.length; i++ )
				{
					if ( mla[i] instanceof SwingEventHandler )
					{
						jc.removeMouseListener( mla[i] );
					}
				}

				MouseMotionListener[] mmla = jc.getMouseMotionListeners( );
				for ( int i = 0; i < mmla.length; i++ )
				{
					if ( mmla[i] instanceof SwingEventHandler )
					{
						jc.removeMouseMotionListener( mmla[i] );
					}
				}

				KeyListener[] kla = jc.getKeyListeners( );
				for ( int i = 0; i < kla.length; i++ )
				{
					if ( kla[i] instanceof SwingEventHandler )
					{
						jc.removeKeyListener( kla[i] );
					}
				}

				_eh = new SwingEventHandler( _lhmAllTriggers, _iun, getULocale( ) );
				jc.addMouseListener( _eh );
				jc.addMouseMotionListener( _eh );
				jc.addKeyListener( _eh );
			}
		}
		else if ( sProperty.equals( IDeviceRenderer.GRAPHICS_CONTEXT ) )
		{
			_g2d = (Graphics2D) oValue;
			_g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
			_g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON );
			_g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON );
			_g2d.setRenderingHint( RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY );

			// _frc = new FontRenderContext(new AffineTransform(), true, false);
			logger.log( ILogger.INFORMATION,
					Messages.getString( "info.using.graphics.context", //$NON-NLS-1$
							new Object[]{
								_g2d
							},
							getULocale( ) ) );
		}
		else if ( sProperty.equals( IDeviceRenderer.DPI_RESOLUTION ) )
		{
			getDisplayServer( ).setDpiResolution( ( (Integer) oValue ).intValue( ) );
		}

	}

	/**
	 * 
	 * @param g2d
	 */
	public final Object getGraphicsContext( )
	{
		return _g2d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#setClip(org.eclipse.birt.chart.output.ClipRenderEvent)
	 */
	public void setClip( ClipRenderEvent pre )
	{
		final Location[] loa = pre.getVertices( );

		if ( loa == null )
		{
			_g2d.setClip( null );
		}
		else
		{
			final int[][] i2a = getCoordinatesAsInts( loa );
			_g2d.setClip( new Polygon( i2a[0], i2a[1], loa.length ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawImage(org.eclipse.birt.chart.output.ImageRenderEvent)
	 */
	public void drawImage( ImageRenderEvent pre ) throws ChartException
	{
		if ( pre.getImage( ) == null || pre.getLocation( ) == null )
		{
			return;
		}

		java.awt.Image img = null;

		if ( pre.getImage( ) instanceof EmbeddedImage )
		{
			try
			{
				byte[] data = Base64.decodeBase64( ( (EmbeddedImage) pre.getImage( ) ).getData( )
						.getBytes( ) );

				img = createImage( data );
			}
			catch ( Exception ilex )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						ilex );
			}
		}
		else
		{
			try
			{
				final String sUrl = pre.getImage( ).getURL( );
				img = (java.awt.Image) _ids.loadImage( new URL( sUrl ) );
			}
			catch ( ChartException ilex )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						ilex );
			}
			catch ( MalformedURLException muex )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						muex );
			}
		}

		if ( img == null )
		{
			return;
		}

		Location loc = pre.getLocation( );
		Position pos = pre.getPosition( );
		if ( pos == null )
		{
			pos = Position.INSIDE_LITERAL;
		}

		ImageObserver io = (ImageObserver) _ids.getObserver( );

		int width = img.getWidth( io );
		int height = img.getHeight( io );
		int x = (int) loc.getX( );
		int y = (int) loc.getY( );

		switch ( pos.getValue( ) )
		{
			case Position.INSIDE :
			case Position.OUTSIDE :
				x -= width / 2;
				y -= height / 2;
				break;
			case Position.LEFT :
				x -= width;
				y -= height / 2;
				break;
			case Position.RIGHT :
				y -= height / 2;
				break;
			case Position.ABOVE :
				x -= width / 2;
				y -= height;
				break;
			case Position.BELOW :
				x -= width / 2;
				break;
		}

		_g2d.drawImage( img, x, y, width, height, io );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawLine(org.eclipse.birt.chart.output.LineRenderEvent)
	 */
	public void drawLine( LineRenderEvent lre ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = lre.getLineAttributes( );
		if ( !validateLineAttributes( lre.getSource( ), lia )
				|| lia.getColor( ) == null )
		{
			return;
		}

		// DRAW THE LINE
		final Location loStart = lre.getStart( );
		final Location loEnd = lre.getEnd( );
		Stroke sPrevious = null, sCurrent = getCachedStroke( lia );
		if ( sCurrent != null ) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke( );
			_g2d.setStroke( sCurrent );
		}

		_g2d.setColor( (Color) _ids.getColor( lia.getColor( ) ) );
		_g2d.draw( new Line2D.Double( loStart.getX( ),
				loStart.getY( ),
				loEnd.getX( ),
				loEnd.getY( ) ) );

		if ( sPrevious != null ) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke( sPrevious );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
	 */
	public void drawRectangle( RectangleRenderEvent rre ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = rre.getOutline( );
		if ( !validateLineAttributes( rre.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				rre.getBackground( ),
				_ids );
		if ( cFG == null )
		{
			return;
		}

		// RENDER THE RECTANGLE WITH THE APPROPRIATE LINE STYLE
		final Bounds bo = normalizeBounds( rre.getBounds( ) );
		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke( lia );
		if ( sCurrent != null ) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke( );
			_g2d.setStroke( sCurrent );
		}
		_g2d.setColor( cFG );
		_g2d.draw( new Rectangle2D.Double( bo.getLeft( ),
				bo.getTop( ),
				bo.getWidth( ) - 1,
				bo.getHeight( ) - 1 ) );
		if ( sPrevious != null ) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke( sPrevious );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#fillRectangle(org.eclipse.birt.chart.output.RectangleRenderEvent)
	 */
	public void fillRectangle( RectangleRenderEvent rre ) throws ChartException
	{
		final Fill flBackground = rre.getBackground( );
		final Bounds bo = normalizeBounds( rre.getBounds( ) );
		final Rectangle2D.Double r2d = new Rectangle2D.Double( bo.getLeft( ),
				bo.getTop( ),
				bo.getWidth( ),
				bo.getHeight( ) );
		if ( flBackground instanceof ColorDefinition )
		{
			final ColorDefinition cd = (ColorDefinition) flBackground;
			_g2d.setColor( (Color) _ids.getColor( cd ) );
			_g2d.fill( r2d );
		}
		else if ( flBackground instanceof Gradient )
		{
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor( );
			final ColorDefinition cdEnd = g.getEndColor( );
			// boolean bCyclic = g.isCyclic();
			double dAngleInDegrees = g.getDirection( );
			final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
			// int iAlpha = g.getTransparency();

			/*
			 * if (bCyclic) { }
			 */

			if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.gradient.angle",//$NON-NLS-1$
						new Object[]{
							new Double( dAngleInDegrees )
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) );
			}

			Point2D.Double p2dStart, p2dEnd;
			if ( dAngleInDegrees == 90 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees == -90 )
			{
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees > 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getHeight( )
								- bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else if ( dAngleInDegrees < 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( ) );
			}
			_g2d.setPaint( new GradientPaint( p2dStart,
					(Color) _ids.getColor( cdStart ),
					p2dEnd,
					(Color) _ids.getColor( cdEnd ) ) );
			_g2d.fill( r2d );
		}
		else if ( flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
		{
			java.awt.Image img = null;

			if ( flBackground instanceof EmbeddedImage )
			{
				try
				{
					byte[] data = Base64.decodeBase64( ( (EmbeddedImage) flBackground ).getData( )
							.getBytes( ) );

					img = createImage( data );
				}
				catch ( Exception ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
			}
			else
			{
				try
				{
					final String sUrl = ( (org.eclipse.birt.chart.model.attribute.Image) flBackground ).getURL( );
					img = (java.awt.Image) _ids.loadImage( new URL( sUrl ) );
				}
				catch ( ChartException ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
				catch ( MalformedURLException muex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							muex );
				}
			}

			final Shape shClip = _g2d.getClip( );
			Area ar2 = new Area( r2d );
			if ( shClip != null )
			{
				Area ar1 = new Area( shClip );
				ar2.intersect( ar1 );
			}
			_g2d.setClip( ar2 );

			final Size szImage = _ids.getSize( img );

			int iXRepeat = (int) ( Math.ceil( r2d.width / szImage.getWidth( ) ) );
			int iYRepeat = (int) ( Math.ceil( r2d.height / szImage.getHeight( ) ) );
			ImageObserver io = (ImageObserver) _ids.getObserver( );
			for ( int i = 0; i < iXRepeat; i++ )
			{
				for ( int j = 0; j < iYRepeat; j++ )
				{
					_g2d.drawImage( img,
							(int) ( r2d.x + i * szImage.getWidth( ) ),
							(int) ( r2d.y + j * szImage.getHeight( ) ),
							io );
				}
			}

			// img(); // FLUSHED LATER BY CACHE; DON'T FLUSH HERE
			_g2d.setClip( shClip ); // RESTORE
		}
	}

	/**
	 * In SWING, polygons are defined with 'int' co-ordinates. There is no
	 * concept of a Polygon2D. As a result, we downgrade high-res 'double'
	 * co-ordinates to 'int' co-ordinates.
	 * 
	 * @param la
	 * @return
	 */
	protected static final int[][] getCoordinatesAsInts( Location[] la )
	{
		final int n = la.length;
		final int[] iaX = new int[n];
		final int[] iaY = new int[n];

		for ( int i = 0; i < n; i++ )
		{
			iaX[i] = (int) la[i].getX( );
			iaY[i] = (int) la[i].getY( );
		}

		return new int[][]{
				iaX, iaY
		};
	}

	/**
	 * Make bounds height/width always positive.
	 * 
	 * @param bo
	 * @return
	 */
	protected static final Bounds normalizeBounds( Bounds bo )
	{
		if ( bo.getHeight( ) < 0 )
		{
			bo.setTop( bo.getTop( ) + bo.getHeight( ) );
			bo.setHeight( -bo.getHeight( ) );
		}

		if ( bo.getWidth( ) < 0 )
		{
			bo.setLeft( bo.getLeft( ) + bo.getWidth( ) );
			bo.setWidth( -bo.getWidth( ) );
		}

		return bo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawPolygon(org.eclipse.birt.chart.output.PolygonRenderEvent)
	 */
	public void drawPolygon( PolygonRenderEvent pre ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = pre.getOutline( );
		if ( !validateLineAttributes( pre.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				pre.getBackground( ),
				_ids );
		if ( cFG == null ) // IF UNDEFINED, EXIT
		{
			return;
		}

		// DRAW THE POLYGON
		final Location[] la = pre.getPoints( );
		final int[][] i2a = getCoordinatesAsInts( la );
		Stroke sPrevious = null;
		final Stroke sCurrent = getCachedStroke( lia );
		if ( sCurrent != null ) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke( );
			_g2d.setStroke( sCurrent );
		}

		_g2d.setColor( cFG );
		_g2d.draw( new Polygon( i2a[0], i2a[1], la.length ) );

		if ( sPrevious != null ) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke( sPrevious );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#fillPolygon(org.eclipse.birt.chart.output.PolygonRenderEvent)
	 */
	public void fillPolygon( PolygonRenderEvent pre ) throws ChartException
	{
		final Fill flBackground = pre.getBackground( );
		final Location[] loa = pre.getPoints( );
		final int[][] i2a = getCoordinatesAsInts( loa );

		if ( flBackground instanceof ColorDefinition )
		{
			final ColorDefinition cd = (ColorDefinition) flBackground;
			_g2d.setColor( (Color) _ids.getColor( cd ) );
			_g2d.fill( new Polygon( i2a[0], i2a[1], loa.length ) );
		}
		else if ( flBackground instanceof Gradient )
		{
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor( );
			final ColorDefinition cdEnd = g.getEndColor( );
			// final boolean bRadial = g.isCyclic();
			final double dAngleInDegrees = g.getDirection( );
			final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
			// final int iAlpha = g.getTransparency();

			final double dMinX = BaseRenderer.getX( loa, IConstants.MIN );
			final double dMaxX = BaseRenderer.getX( loa, IConstants.MAX );
			final double dMinY = BaseRenderer.getY( loa, IConstants.MIN );
			final double dMaxY = BaseRenderer.getY( loa, IConstants.MAX );

			/*
			 * if (bRadial) { }
			 */

			if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.gradient.angle",//$NON-NLS-1$
						new Object[]{
							new Double( dAngleInDegrees )
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) );
			}

			Point2D.Double p2dStart, p2dEnd;
			if ( dAngleInDegrees == 90 )
			{
				p2dStart = new Point2D.Double( dMinX, dMaxY );
				p2dEnd = new Point2D.Double( dMinX, dMinY );
			}
			else if ( dAngleInDegrees == -90 )
			{
				p2dStart = new Point2D.Double( dMinX, dMinY );
				p2dEnd = new Point2D.Double( dMinX, dMaxY );
			}
			else if ( dAngleInDegrees > 0 )
			{
				p2dStart = new Point2D.Double( dMinX, dMaxY );
				p2dEnd = new Point2D.Double( dMaxX, dMaxY
						- ( dMaxX - dMinX )
						* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else if ( dAngleInDegrees < 0 )
			{
				p2dStart = new Point2D.Double( dMinX, dMinY );
				p2dEnd = new Point2D.Double( dMaxX, dMinY
						+ ( dMaxX - dMinX )
						* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else
			{
				p2dStart = new Point2D.Double( dMinX, dMinY );
				p2dEnd = new Point2D.Double( dMaxX, dMinY );
			}
			_g2d.setPaint( new GradientPaint( p2dStart,
					(Color) _ids.getColor( cdStart ),
					p2dEnd,
					(Color) _ids.getColor( cdEnd ) ) );
			_g2d.fill( new Polygon( i2a[0], i2a[1], loa.length ) );
		}
		else if ( flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
		{
			java.awt.Image img = null;
			if ( flBackground instanceof EmbeddedImage )
			{
				try
				{
					byte[] data = Base64.decodeBase64( ( (EmbeddedImage) flBackground ).getData( )
							.getBytes( ) );

					img = createImage( data );
				}
				catch ( Exception ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
			}
			else
			{
				try
				{
					final String sUrl = ( (org.eclipse.birt.chart.model.attribute.Image) flBackground ).getURL( );
					img = (java.awt.Image) _ids.loadImage( new URL( sUrl ) );
				}
				catch ( ChartException ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
				catch ( MalformedURLException muex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							muex );
				}
			}

			final Shape shClip = _g2d.getClip( );
			Area ar2 = new Area( new Polygon( i2a[0], i2a[1], loa.length ) );
			if ( shClip != null )
			{
				Area ar1 = new Area( shClip );
				ar2.intersect( ar1 );
			}
			_g2d.setClip( ar2 );

			final double dMinX = BaseRenderer.getX( loa, IConstants.MIN );
			final double dMaxX = BaseRenderer.getX( loa, IConstants.MAX );
			final double dMinY = BaseRenderer.getY( loa, IConstants.MIN );
			final double dMaxY = BaseRenderer.getY( loa, IConstants.MAX );
			final Size szImage = _ids.getSize( img );

			final int iXRepeat = (int) ( Math.ceil( ( dMaxX - dMinX )
					/ szImage.getWidth( ) ) );
			final int iYRepeat = (int) ( Math.ceil( ( dMaxY - dMinY )
					/ szImage.getHeight( ) ) );
			final ImageObserver io = (ImageObserver) _ids.getObserver( );
			for ( int i = 0; i < iXRepeat; i++ )
			{
				for ( int j = 0; j < iYRepeat; j++ )
				{
					_g2d.drawImage( img,
							(int) ( dMinX + i * szImage.getWidth( ) ),
							(int) ( dMinY + j * szImage.getHeight( ) ),
							io );
				}
			}

			// img(); // FLUSHED LATER BY CACHE; DON'T FLUSH HERE
			_g2d.setClip( shClip ); // RESTORE
		}

		/*
		 * if (pre.iObjIndex > 0) { try { Bounds bo = pre.getBounds();
		 * _g2d.setColor(Color.red); _g2d.setFont(new Font("Arial", Font.BOLD,
		 * 14)); _g2d.drawString("{0}" + pre.iObjIndex, (int) (bo.getLeft() +
		 * bo.getWidth() / 2), (int) (bo.getTop() + bo //
		 * i18n_CONCATENATIONS_REMOVED .getHeight() / 2)); } catch (Exception ex ) {
		 * ex.printStackTrace(); } }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#drawArc(org.eclipse.birt.chart.output.ArcRenderEvent)
	 */
	public void drawArc( ArcRenderEvent are ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline( );
		if ( !validateLineAttributes( are.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				are.getBackground( ),
				_ids );
		if ( cFG == null )
		{
			return;
		}

		// DRAW THE ARC
		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke( lia );
		if ( sCurrent != null ) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke( );
			_g2d.setStroke( sCurrent );
		}
		_g2d.setColor( cFG );

		if ( are.getInnerRadius( ) >= 0
				&& are.getOuterRadius( ) > 0
				&& are.getInnerRadius( ) < are.getOuterRadius( ) )
		{
			Shape outerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
					+ ( are.getWidth( ) - 2 * are.getOuterRadius( ) )
					/ 2,
					are.getTopLeft( ).getY( )
							+ ( are.getHeight( ) - 2 * are.getOuterRadius( ) )
							/ 2,
					2 * are.getOuterRadius( ),
					2 * are.getOuterRadius( ),
					are.getStartAngle( ),
					are.getAngleExtent( ),
					Arc2D.PIE );
			Shape innerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
					+ ( are.getWidth( ) - 2 * are.getInnerRadius( ) )
					/ 2,
					are.getTopLeft( ).getY( )
							+ ( are.getHeight( ) - 2 * are.getInnerRadius( ) )
							/ 2,
					2 * are.getInnerRadius( ),
					2 * are.getInnerRadius( ),
					are.getStartAngle( ),
					are.getAngleExtent( ),
					Arc2D.PIE );

			Area fArea = new Area( outerArc );
			fArea.exclusiveOr( new Area( innerArc ) );

			Shape prevClip = _g2d.getClip( );
			Area ar2 = new Area( fArea );
			if ( prevClip != null )
			{
				Area ar1 = new Area( prevClip );
				ar2.intersect( ar1 );
			}
			_g2d.setClip( ar2 );
			_g2d.draw( fArea );
			_g2d.setClip( prevClip );
		}
		else
		{
			_g2d.draw( new Arc2D.Double( are.getTopLeft( ).getX( ),
					are.getTopLeft( ).getY( ),
					are.getWidth( ),
					are.getHeight( ),
					are.getStartAngle( ),
					are.getAngleExtent( ),
					toSwingArcType( are.getStyle( ) ) ) );
		}

		if ( sPrevious != null ) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke( sPrevious );
		}
	}

	/**
	 * 
	 * @param iArcStyle
	 * @return
	 */
	private static final int toSwingArcType( int iArcStyle )
	{
		switch ( iArcStyle )
		{
			case ArcRenderEvent.OPEN :
				return Arc2D.OPEN;
			case ArcRenderEvent.CLOSED :
				return Arc2D.CHORD;
			case ArcRenderEvent.SECTOR :
				return Arc2D.PIE;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#fillArc(org.eclipse.birt.chart.output.ArcRenderEvent)
	 */
	public void fillArc( ArcRenderEvent are ) throws ChartException
	{
		final Fill flBackground = are.getBackground( );
		if ( flBackground instanceof ColorDefinition )
		{
			final Color clrPrevious = _g2d.getColor( );
			final Color currentColor = (Color) _ids.getColor( (ColorDefinition) flBackground );
			_g2d.setColor( currentColor );

			if ( are.getInnerRadius( ) >= 0
					&& are.getOuterRadius( ) > 0
					&& are.getInnerRadius( ) < are.getOuterRadius( ) )
			{
				Shape outerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getOuterRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getOuterRadius( ) )
								/ 2,
						2 * are.getOuterRadius( ),
						2 * are.getOuterRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );
				Shape innerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getInnerRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getInnerRadius( ) )
								/ 2,
						2 * are.getInnerRadius( ),
						2 * are.getInnerRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );

				Area fArea = new Area( outerArc );
				fArea.exclusiveOr( new Area( innerArc ) );

				Shape prevClip = _g2d.getClip( );
				Area ar2 = new Area( fArea );
				if ( prevClip != null )
				{
					Area ar1 = new Area( prevClip );
					ar2.intersect( ar1 );
				}
				_g2d.setClip( ar2 );
				_g2d.fill( fArea );
				_g2d.setClip( prevClip );
			}
			else
			{
				_g2d.fill( new Arc2D.Double( are.getTopLeft( ).getX( ),
						are.getTopLeft( ).getY( ),
						are.getWidth( ),
						are.getHeight( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						toSwingArcType( are.getStyle( ) ) ) );
			}

			_g2d.setColor( clrPrevious ); // RESTORE
		}
		else if ( flBackground instanceof Gradient )
		{
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor( );
			final ColorDefinition cdEnd = g.getEndColor( );
			double dAngleInDegrees = g.getDirection( );
			final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
			Bounds bo = are.getBounds( );

			if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.gradient.angle",//$NON-NLS-1$
						new Object[]{
							new Double( dAngleInDegrees )
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) );
			}

			Point2D.Double p2dStart, p2dEnd;
			if ( dAngleInDegrees == 90 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees == -90 )
			{
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees > 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getHeight( )
								- bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else if ( dAngleInDegrees < 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( ) );
			}

			final Paint pPrevious = _g2d.getPaint( );
			_g2d.setPaint( new GradientPaint( p2dStart,
					(Color) _ids.getColor( cdStart ),
					p2dEnd,
					(Color) _ids.getColor( cdEnd ) ) );

			if ( are.getInnerRadius( ) >= 0
					&& are.getOuterRadius( ) > 0
					&& are.getInnerRadius( ) < are.getOuterRadius( ) )
			{
				Shape outerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getOuterRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getOuterRadius( ) )
								/ 2,
						2 * are.getOuterRadius( ),
						2 * are.getOuterRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );
				Shape innerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getInnerRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getInnerRadius( ) )
								/ 2,
						2 * are.getInnerRadius( ),
						2 * are.getInnerRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );

				Area fArea = new Area( outerArc );
				fArea.exclusiveOr( new Area( innerArc ) );

				Shape prevClip = _g2d.getClip( );
				Area ar2 = new Area( fArea );
				if ( prevClip != null )
				{
					Area ar1 = new Area( prevClip );
					ar2.intersect( ar1 );
				}
				_g2d.setClip( ar2 );
				_g2d.fill( fArea );
				_g2d.setClip( prevClip );
			}
			else
			{
				_g2d.fill( new Arc2D.Double( are.getTopLeft( ).getX( ),
						are.getTopLeft( ).getY( ),
						are.getWidth( ),
						are.getHeight( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						toSwingArcType( are.getStyle( ) ) ) );
			}

			_g2d.setPaint( pPrevious ); // RESTORE
		}
		else if ( flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
		{
			final Bounds bo = are.getBounds( );
			final Rectangle2D.Double r2d = new Rectangle2D.Double( bo.getLeft( ),
					bo.getTop( ),
					bo.getWidth( ),
					bo.getHeight( ) );

			Shape shPreviousClip = _g2d.getClip( );

			if ( are.getInnerRadius( ) >= 0
					&& are.getOuterRadius( ) > 0
					&& are.getInnerRadius( ) < are.getOuterRadius( ) )
			{
				Shape outerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getOuterRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getOuterRadius( ) )
								/ 2,
						2 * are.getOuterRadius( ),
						2 * are.getOuterRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );
				Shape innerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getInnerRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getInnerRadius( ) )
								/ 2,
						2 * are.getInnerRadius( ),
						2 * are.getInnerRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );

				Area fArea = new Area( outerArc );
				fArea.exclusiveOr( new Area( innerArc ) );

				if ( shPreviousClip != null )
				{
					Area ar1 = new Area( shPreviousClip );
					fArea.intersect( ar1 );
				}
				_g2d.setClip( fArea );
			}
			else
			{
				// SETUP THE CLIPPING AREA
				final Shape shArc = new Arc2D.Double( are.getTopLeft( ).getX( ),
						are.getTopLeft( ).getY( ),
						are.getWidth( ),
						are.getHeight( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						toSwingArcType( are.getStyle( ) ) );

				Area ar2 = new Area( shArc );
				if ( shPreviousClip != null )
				{
					Area ar1 = new Area( shPreviousClip );
					ar2.intersect( ar1 );
				}
				_g2d.setClip( ar2 );
			}

			// LOAD THE IMAGE
			java.awt.Image img = null;
			if ( flBackground instanceof EmbeddedImage )
			{
				try
				{
					byte[] data = Base64.decodeBase64( ( (EmbeddedImage) flBackground ).getData( )
							.getBytes( ) );

					img = createImage( data );
				}
				catch ( Exception ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
			}
			else
			{
				try
				{
					final String sUrl = ( (org.eclipse.birt.chart.model.attribute.Image) flBackground ).getURL( );
					img = (java.awt.Image) _ids.loadImage( new URL( sUrl ) );
				}
				catch ( ChartException ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
				catch ( MalformedURLException muex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							muex );
				}
			}

			// REPLICATE THE IMAGE AS NEEDED
			final Size szImage = _ids.getSize( img );
			int iXRepeat = (int) ( Math.ceil( r2d.width / szImage.getWidth( ) ) );
			int iYRepeat = (int) ( Math.ceil( r2d.height / szImage.getHeight( ) ) );
			ImageObserver io = (ImageObserver) _ids.getObserver( );
			for ( int i = 0; i < iXRepeat; i++ )
			{
				for ( int j = 0; j < iYRepeat; j++ )
				{
					_g2d.drawImage( img,
							(int) ( r2d.x + i * szImage.getWidth( ) ),
							(int) ( r2d.y + j * szImage.getHeight( ) ),
							io );
				}
			}

			_g2d.setClip( shPreviousClip ); // RESTORE
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawArea(org.eclipse.birt.chart.event.AreaRenderEvent)
	 */
	public void drawArea( AreaRenderEvent are ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = are.getOutline( );
		if ( !validateLineAttributes( are.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				are.getBackground( ),
				_ids );
		if ( cFG == null ) // IF UNDEFINED, EXIT
		{
			return;
		}

		// BUILD THE GENERAL PATH STRUCTURE
		final GeneralPath gp = new GeneralPath( );
		PrimitiveRenderEvent pre;
		for ( int i = 0; i < are.getElementCount( ); i++ )
		{
			pre = are.getElement( i );
			if ( pre instanceof ArcRenderEvent )
			{
				final ArcRenderEvent acre = (ArcRenderEvent) pre;
				final Arc2D.Double a2d = new Arc2D.Double( acre.getTopLeft( )
						.getX( ),
						acre.getTopLeft( ).getY( ),
						acre.getWidth( ),
						acre.getHeight( ),
						acre.getStartAngle( ),
						acre.getAngleExtent( ),
						toSwingArcType( acre.getStyle( ) ) );
				gp.append( a2d, true );
			}
			else if ( pre instanceof LineRenderEvent )
			{
				final LineRenderEvent lre = (LineRenderEvent) pre;
				final Line2D.Double l2d = new Line2D.Double( lre.getStart( )
						.getX( ),
						lre.getStart( ).getY( ),
						lre.getEnd( ).getX( ),
						lre.getEnd( ).getY( ) );
				gp.append( l2d, true );
			}
		}

		// DRAW THE GENERAL PATH
		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke( lia );
		if ( sCurrent != null ) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke( );
			_g2d.setStroke( sCurrent );
		}

		_g2d.setColor( cFG );
		_g2d.draw( gp );

		if ( sPrevious != null ) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke( sPrevious );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#fillArea(org.eclipse.birt.chart.event.AreaRenderEvent)
	 */
	public void fillArea( AreaRenderEvent are ) throws ChartException
	{
		// SETUP SWING DATA STRUCTURES
		final GeneralPath gp = new GeneralPath( );
		PrimitiveRenderEvent pre;
		for ( int i = 0; i < are.getElementCount( ); i++ )
		{
			pre = are.getElement( i );
			if ( pre instanceof ArcRenderEvent )
			{
				final ArcRenderEvent acre = (ArcRenderEvent) pre;
				final Arc2D.Double a2d = new Arc2D.Double( acre.getTopLeft( )
						.getX( ),
						acre.getTopLeft( ).getY( ),
						acre.getWidth( ),
						acre.getHeight( ),
						acre.getStartAngle( ),
						acre.getAngleExtent( ),
						toSwingArcType( acre.getStyle( ) ) );
				gp.append( a2d, true );
			}
			else if ( pre instanceof LineRenderEvent )
			{
				final LineRenderEvent lre = (LineRenderEvent) pre;
				final Line2D.Double l2d = new Line2D.Double( lre.getStart( )
						.getX( ),
						lre.getStart( ).getY( ),
						lre.getEnd( ).getX( ),
						lre.getEnd( ).getY( ) );
				gp.append( l2d, true );
			}
		}

		// BEGIN FILLING
		final Fill flBackground = are.getBackground( );
		if ( flBackground instanceof ColorDefinition )
		{
			_g2d.setColor( (Color) _ids.getColor( (ColorDefinition) flBackground ) );
		}
		else if ( flBackground instanceof Gradient )
		{
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor( );
			final ColorDefinition cdEnd = g.getEndColor( );
			// boolean bCyclic = g.isCyclic();
			double dAngleInDegrees = g.getDirection( );
			final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
			// int iAlpha = g.getTransparency();
			Bounds bo = are.getBounds( );

			/*
			 * if (bCyclic) { }
			 */

			if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.gradient.angle",//$NON-NLS-1$
						new Object[]{
							new Double( dAngleInDegrees )
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) );
			}

			Point2D.Double p2dStart, p2dEnd;
			if ( dAngleInDegrees == 90 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees == -90 )
			{
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees > 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getHeight( )
								- bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else if ( dAngleInDegrees < 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( ) );
			}
			_g2d.setPaint( new GradientPaint( p2dStart,
					(Color) _ids.getColor( cdStart ),
					p2dEnd,
					(Color) _ids.getColor( cdEnd ) ) );
		}
		else if ( flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
		{
			// TBD
		}
		_g2d.fill( gp );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawText(org.eclipse.birt.chart.event.TextRenderEvent)
	 */
	public void drawText( TextRenderEvent tre ) throws ChartException
	{
		SwingTextRenderer tr = SwingTextRenderer.instance( (SwingDisplayServer) _ids );
		switch ( tre.getAction( ) )
		{
			case TextRenderEvent.UNDEFINED :
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.missing.text.render.action", //$NON-NLS-1$
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) );

			case TextRenderEvent.RENDER_SHADOW_AT_LOCATION :
				tr.renderShadowAtLocation( this,
						tre.getTextPosition( ),
						tre.getLocation( ),
						tre.getLabel( ) );
				break;

			case TextRenderEvent.RENDER_TEXT_AT_LOCATION :
				tr.renderTextAtLocation( this,
						tre.getTextPosition( ),
						tre.getLocation( ),
						tre.getLabel( ) );
				break;

			case TextRenderEvent.RENDER_TEXT_IN_BLOCK :
				tr.renderTextInBlock( this,
						tre.getBlockBounds( ),
						tre.getBlockAlignment( ),
						tre.getLabel( ) );
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.output.IPrimitiveRenderListener#enableInteraction(org.eclipse.birt.chart.output.InteractionEvent)
	 */
	public void enableInteraction( InteractionEvent iev ) throws ChartException
	{
		if ( _iun == null )
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "exception.missing.component.interaction", getULocale( ) ) ); //$NON-NLS-1$
			return;
		}

		final Trigger[] tga = iev.getTriggers( );
		if ( tga == null )
		{
			return;
		}

		Shape clipping = _g2d.getClip( );

		// CREATE AND SETUP THE SHAPES FOR INTERACTION
		TriggerCondition tc;
		ArrayList al;
		final PrimitiveRenderEvent pre = iev.getHotSpot( );
		if ( pre instanceof PolygonRenderEvent )
		{
			final Location[] loa = ( (PolygonRenderEvent) pre ).getPoints( );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}
				al.add( new ShapedAction( iev.getSource( ),
						loa,
						tga[i].getAction( ),
						clipping ) );
			}
		}
		else if ( pre instanceof RectangleRenderEvent )
		{
			final Bounds bo = ( (RectangleRenderEvent) pre ).getBounds( );

			final Location[] loa = new Location[4];
			loa[0] = LocationImpl.create( bo.getLeft( ), bo.getTop( ) );
			loa[1] = LocationImpl.create( bo.getLeft( ), bo.getTop( )
					+ bo.getHeight( ) );
			loa[2] = LocationImpl.create( bo.getLeft( ) + bo.getWidth( ),
					bo.getTop( ) + bo.getHeight( ) );
			loa[3] = LocationImpl.create( bo.getLeft( ) + bo.getWidth( ),
					bo.getTop( ) );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}
				al.add( new ShapedAction( iev.getSource( ),
						loa,
						tga[i].getAction( ),
						clipping ) );
			}
		}
		else if ( pre instanceof OvalRenderEvent )
		{
			final Bounds boEllipse = ( (OvalRenderEvent) pre ).getBounds( );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}
				al.add( new ShapedAction( iev.getSource( ),
						boEllipse,
						tga[i].getAction( ),
						clipping ) );
			}
		}
		else if ( pre instanceof ArcRenderEvent )
		{
			final ArcRenderEvent are = (ArcRenderEvent) pre;
			final Bounds boEllipse = are.getEllipseBounds( );
			double dStart = are.getStartAngle( );
			double dExtent = are.getAngleExtent( );
			int iArcType = toSwingArcType( are.getStyle( ) );

			for ( int i = 0; i < tga.length; i++ )
			{
				tc = tga[i].getCondition( );
				al = (ArrayList) _lhmAllTriggers.get( tc );
				if ( al == null )
				{
					al = new ArrayList( 4 ); // UNDER NORMAL CONDITIONS
					_lhmAllTriggers.put( tc, al );
				}
				al.add( new ShapedAction( iev.getSource( ),
						boEllipse,
						dStart,
						dExtent,
						iArcType,
						tga[i].getAction( ),
						clipping ) );
			}
		}

	}

	/**
	 * Reusable 'strokes' for rendering lines may be obtained from here
	 * 
	 * @param ls
	 * @return
	 */
	public final Stroke getCachedStroke( LineAttributes lia )
	{
		if ( lia == null )
			return null;

		Stroke s = (Stroke) _htLineStyles.get( lia );
		if ( s == null )
		{
			BasicStroke bs = null;
			if ( lia.getStyle( ).getValue( ) == LineStyle.DASHED )
			{
				float[] faStyle = new float[]{
						6.0f, 4.0f
				};
				bs = new BasicStroke( lia.getThickness( ),
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						0,
						faStyle,
						0 );
			}
			else if ( lia.getStyle( ).getValue( ) == LineStyle.DOTTED )
			{
				float[] faStyle = new float[]{
						1.0f, 4.0f
				};
				bs = new BasicStroke( lia.getThickness( ),
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						0,
						faStyle,
						0 );
			}
			else if ( lia.getStyle( ).getValue( ) == LineStyle.DASH_DOTTED )
			{
				float[] faStyle = new float[]{
						6.0f, 4.0f, 1.0f, 4.0f
				};
				bs = new BasicStroke( lia.getThickness( ),
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND,
						0,
						faStyle,
						0 );
			}
			else if ( lia.getStyle( ).getValue( ) == LineStyle.SOLID )
			{
				bs = new BasicStroke( lia.getThickness( ),
						BasicStroke.CAP_ROUND,
						BasicStroke.JOIN_ROUND );
			}
			if ( bs != null )
			{
				_htLineStyles.put( lia, bs );
			}
			return bs;
		}
		return s;
	}

	/**
	 * 
	 * @param s
	 * @param sWordToReplace
	 * @param sReplaceWith
	 * @return
	 */
	public static String csSearchAndReplace( String s, String sWordToReplace,
			String sReplaceWith )
	{
		int i = 0;
		do
		{
			i = s.indexOf( sWordToReplace, i );
			if ( i != -1 )
			{
				s = s.substring( 0, i )
						+ sReplaceWith
						+ s.substring( i + sWordToReplace.length( ) );
				i += sReplaceWith.length( );
			}
		} while ( i != -1 );
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawOval(org.eclipse.birt.chart.event.OvalRenderEvent)
	 */
	public void drawOval( OvalRenderEvent ore ) throws ChartException
	{
		// CHECK IF THE LINE ATTRIBUTES ARE CORRECTLY DEFINED
		final LineAttributes lia = ore.getOutline( );
		if ( !validateLineAttributes( ore.getSource( ), lia ) )
		{
			return;
		}

		// SETUP THE FOREGROUND COLOR (DARKER BACKGROUND IF DEFINED AS NULL)
		final Color cFG = (Color) validateEdgeColor( lia.getColor( ),
				ore.getBackground( ),
				_ids );
		if ( cFG == null )
		{
			return;
		}

		// RENDER THE ELLIPSE WITH THE APPROPRIATE LINE STYLE
		final Bounds bo = ore.getBounds( );
		final Ellipse2D.Double e2d = new Ellipse2D.Double( bo.getLeft( ),
				bo.getTop( ),
				bo.getWidth( ),
				bo.getHeight( ) );

		Stroke sPrevious = null;
		Stroke sCurrent = getCachedStroke( lia );
		if ( sCurrent != null ) // SOME STROKE DEFINED?
		{
			sPrevious = _g2d.getStroke( );
			_g2d.setStroke( sCurrent );
		}

		_g2d.setColor( cFG );
		_g2d.draw( e2d );

		if ( sPrevious != null ) // RESTORE PREVIOUS STROKE
		{
			_g2d.setStroke( sPrevious );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#fillOval(org.eclipse.birt.chart.event.OvalRenderEvent)
	 */
	public void fillOval( OvalRenderEvent ore ) throws ChartException
	{
		final Fill flBackground = ore.getBackground( );
		final Bounds bo = ore.getBounds( );
		final Ellipse2D.Double e2d = new Ellipse2D.Double( bo.getLeft( ),
				bo.getTop( ),
				bo.getWidth( ),
				bo.getHeight( ) );
		if ( flBackground instanceof ColorDefinition )
		{
			final ColorDefinition cd = (ColorDefinition) flBackground;
			_g2d.setColor( (Color) _ids.getColor( cd ) );
			_g2d.fill( e2d );
		}
		else if ( flBackground instanceof Gradient )
		{
			final Gradient g = (Gradient) flBackground;
			final ColorDefinition cdStart = g.getStartColor( );
			final ColorDefinition cdEnd = g.getEndColor( );
			// boolean bCyclic = g.isCyclic();
			double dAngleInDegrees = g.getDirection( );
			final double dAngleInRadians = ( ( -dAngleInDegrees * Math.PI ) / 180.0 );
			// int iAlpha = g.getTransparency();

			/*
			 * if (bCyclic) { }
			 */

			if ( dAngleInDegrees < -90 || dAngleInDegrees > 90 )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.gradient.angle",//$NON-NLS-1$
						new Object[]{
							new Double( dAngleInDegrees )
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale( ) ) );
			}

			Point2D.Double p2dStart, p2dEnd;
			if ( dAngleInDegrees == 90 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees == -90 )
			{
				p2dEnd = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
			}
			else if ( dAngleInDegrees > 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( )
						+ bo.getHeight( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getHeight( )
								- bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else if ( dAngleInDegrees < 0 )
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( )
								+ bo.getWidth( )
								* Math.abs( Math.tan( dAngleInRadians ) ) );
			}
			else
			{
				p2dStart = new Point2D.Double( bo.getLeft( ), bo.getTop( ) );
				p2dEnd = new Point2D.Double( bo.getLeft( ) + bo.getWidth( ),
						bo.getTop( ) );
			}
			_g2d.setPaint( new GradientPaint( p2dStart,
					(Color) _ids.getColor( cdStart ),
					p2dEnd,
					(Color) _ids.getColor( cdEnd ) ) );
			_g2d.fill( e2d );
		}
		else if ( flBackground instanceof org.eclipse.birt.chart.model.attribute.Image )
		{
			java.awt.Image img = null;
			if ( flBackground instanceof EmbeddedImage )
			{
				try
				{
					byte[] data = Base64.decodeBase64( ( (EmbeddedImage) flBackground ).getData( )
							.getBytes( ) );

					img = createImage( data );
				}
				catch ( Exception ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
			}
			else
			{
				try
				{
					final String sUrl = ( (org.eclipse.birt.chart.model.attribute.Image) flBackground ).getURL( );
					img = (java.awt.Image) _ids.loadImage( new URL( sUrl ) );
				}
				catch ( ChartException ilex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							ilex );
				}
				catch ( MalformedURLException muex )
				{
					throw new ChartException( ChartDeviceExtensionPlugin.ID,
							ChartException.RENDERING,
							muex );
				}
			}

			final Shape shClip = _g2d.getClip( );
			Area ar2 = new Area( e2d );
			if ( shClip != null )
			{
				Area ar1 = new Area( shClip );
				ar2.intersect( ar1 );
			}
			_g2d.setClip( ar2 );

			final Size szImage = _ids.getSize( img );

			int iXRepeat = (int) ( Math.ceil( e2d.width / szImage.getWidth( ) ) );
			int iYRepeat = (int) ( Math.ceil( e2d.height / szImage.getHeight( ) ) );
			ImageObserver io = (ImageObserver) _ids.getObserver( );
			for ( int i = 0; i < iXRepeat; i++ )
			{
				for ( int j = 0; j < iYRepeat; j++ )
				{
					_g2d.drawImage( img,
							(int) ( e2d.x + i * szImage.getWidth( ) ),
							(int) ( e2d.y + j * szImage.getHeight( ) ),
							io );
				}
			}

			// img.flush(); // FLUSHED LATER BY CACHE; DON'T FLUSH HERE
			_g2d.setClip( shClip ); // RESTORE
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IDeviceRenderer#getXServer()
	 */
	public IDisplayServer getDisplayServer( )
	{
		return _ids;
	}

	protected Image createImage( byte[] data )
	{
		ImageIcon ii = new ImageIcon( data );

		return ii.getImage( );
	}

	/**
	 * Returns the triggers associated with current renderer.
	 * 
	 * @return
	 */
	protected Map getTriggers( )
	{
		return _lhmAllTriggers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#applyTransformation(org.eclipse.birt.chart.event.TransformationEvent)
	 */
	public void applyTransformation( TransformationEvent tev )
			throws ChartException
	{
		switch ( tev.getTransform( ) )
		{
			case TransformationEvent.TRANSLATE :
				_g2d.translate( tev.getTranslateX( ), tev.getTranslateY( ) );
				break;

			case TransformationEvent.ROTATE :
				_g2d.rotate( ( tev.getRotation( ) * Math.PI ) / 180d );
				break;

			case TransformationEvent.SCALE :
				_g2d.scale( tev.getScale( ), tev.getScale( ) );
				break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#start()
	 */
	public void before( ) throws ChartException
	{
		// Clean previous status.
		_lhmAllTriggers.clear( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#end()
	 */
	public void after( ) throws ChartException
	{
		// FLUSH ALL IMAGES USED IN RENDERING THE CHART CONTENT
		( (SwingDisplayServer) _ids ).getImageCache( ).flush( );
	}
}