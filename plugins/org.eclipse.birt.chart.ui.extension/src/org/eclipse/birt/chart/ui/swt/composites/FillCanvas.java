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

package org.eclipse.birt.chart.ui.swt.composites;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Administrator
 * 
 */
class FillCanvas extends Canvas implements PaintListener
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.composites" ); //$NON-NLS-1$

	Fill fCurrent = null;

	public FillCanvas( Composite parent, int iStyle )
	{
		super( parent, iStyle );
		this.addPaintListener( this );
	}

	public void setFill( Fill fill )
	{
		this.fCurrent = fill;
	}

	public void paintControl( PaintEvent pe )
	{
		Color cBackground = null;

		try
		{
			Color clrTransparencyBackground = Display.getCurrent( )
					.getSystemColor( SWT.COLOR_LIST_BACKGROUND );
			GC gc = pe.gc;
			if ( !this.isEnabled( ) )
			{
				gc.setBackground( Display.getCurrent( )
						.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
				Color cFore = Display.getCurrent( )
						.getSystemColor( SWT.COLOR_DARK_GRAY );
				gc.setForeground( cFore );
				if ( fCurrent == null
						|| fCurrent instanceof ColorDefinition
						&& ( (ColorDefinition) fCurrent ).getTransparency( ) == 0 )
				{
					gc.fillRectangle( 0,
							0,
							this.getSize( ).x,
							this.getSize( ).y );
					if ( fCurrent == null
							|| ( (ColorDefinition) fCurrent ).isSetTransparency( ) )
					{
						gc.drawText( Messages.getString( "FillCanvas.Transparent" ), 2, 2 ); //$NON-NLS-1$
					}
					else
					{
						gc.drawText( Messages.getString( "FillCanvas.Auto" ), 2, 2 ); //$NON-NLS-1$
					}
				}
				else
				{
					gc.fillRectangle( 0,
							0,
							this.getSize( ).x,
							this.getSize( ).y );
					gc.setBackground( cFore );
					gc.fillRectangle( 2,
							2,
							this.getSize( ).x - 4,
							this.getSize( ).y - 4 );
				}
			}
			else
			{
				if ( fCurrent == null
						|| fCurrent instanceof ColorDefinition
						&& ( (ColorDefinition) fCurrent ).getTransparency( ) == 0 )
				{
					gc.setBackground( clrTransparencyBackground );
					gc.fillRectangle( 0,
							0,
							this.getSize( ).x,
							this.getSize( ).y );
					Color cText = new Color( this.getDisplay( ), 0, 0, 0 );
					gc.setForeground( cText );
					if ( fCurrent == null
							|| ( (ColorDefinition) fCurrent ).isSetTransparency( ) )
					{
						gc.drawText( Messages.getString( "FillCanvas.Transparent" ), 2, 2 ); //$NON-NLS-1$
					}
					else
					{
						gc.drawText( Messages.getString( "FillCanvas.Auto" ), 2, 2 ); //$NON-NLS-1$
					}
					cText.dispose( );
				}
				else
				{
					if ( fCurrent instanceof ColorDefinition )
					{
						cBackground = new Color( Display.getDefault( ),
								( (ColorDefinition) fCurrent ).getRed( ),
								( (ColorDefinition) fCurrent ).getGreen( ),
								( (ColorDefinition) fCurrent ).getBlue( ) );
						gc.setBackground( cBackground );
						gc.fillRectangle( 2,
								2,
								this.getSize( ).x - 4,
								this.getSize( ).y - 4 );
					}
					else if ( fCurrent instanceof Image )
					{
						gc.fillRectangle( 2,
								2,
								getSize( ).x - 4,
								this.getSize( ).y - 4 );
						gc.drawImage( getSWTImage( (Image) fCurrent ), 2, 2 );
					}
					else if ( fCurrent instanceof Gradient )
					{
						if ( ( (Gradient) fCurrent ).getStartColor( ) == null
								&& ( (Gradient) fCurrent ).getEndColor( ) == null )
						{
							return;
						}
						Color clrStart = null;
						Color clrEnd = null;
						if ( ( (Gradient) fCurrent ).getStartColor( ) != null )
						{
							clrStart = new Color( Display.getDefault( ),
									( (Gradient) fCurrent ).getStartColor( )
											.getRed( ),
									( (Gradient) fCurrent ).getStartColor( )
											.getGreen( ),
									( (Gradient) fCurrent ).getStartColor( )
											.getBlue( ) );
							gc.setForeground( clrStart );
						}
						if ( ( (Gradient) fCurrent ).getEndColor( ) != null )
						{
							clrEnd = new Color( Display.getDefault( ),
									( (Gradient) fCurrent ).getEndColor( )
											.getRed( ),
									( (Gradient) fCurrent ).getEndColor( )
											.getGreen( ),
									( (Gradient) fCurrent ).getEndColor( )
											.getBlue( ) );
							gc.setBackground( clrEnd );
						}
						gc.fillGradientRectangle( 2,
								2,
								this.getSize( ).x - 4,
								this.getSize( ).y - 4,
								false );
					}
				}
			}
		}
		catch ( Exception ex )
		{
			logger.log( ex );
		}
		finally
		{
			if ( cBackground != null )
			{
				cBackground.dispose( );
			}
		}
	}

	private org.eclipse.swt.graphics.Image getSWTImage( Image modelImage )
	{
		org.eclipse.swt.graphics.Image img = null;
		try
		{
			if ( modelImage instanceof EmbeddedImage )
			{
				ByteArrayInputStream bis = new ByteArrayInputStream( Base64.decodeBase64( ( (EmbeddedImage) modelImage ).getData( )
						.getBytes( ) ) );

				img = new org.eclipse.swt.graphics.Image( Display.getDefault( ),
						bis );
			}
			else
			{
				try
				{
					img = new org.eclipse.swt.graphics.Image( Display.getCurrent( ),
							new URL( modelImage.getURL( ) ).openStream( ) );
				}
				catch ( MalformedURLException e1 )
				{
					img = new org.eclipse.swt.graphics.Image( Display.getCurrent( ),
							new FileInputStream( modelImage.getURL( ) ) );
				}
			}
		}
		catch ( FileNotFoundException ex )
		{
			logger.log( ex );
			ex.printStackTrace( );
		}
		catch ( IOException ex )
		{
			logger.log( ex );
			ex.printStackTrace( );
		}
		return img;
	}

	public void setEnabled( boolean bState )
	{
		super.setEnabled( bState );
		redraw( );
	}
}