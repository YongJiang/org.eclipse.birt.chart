/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.TextAdapter;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.component.Label;

/**
 * Provides metrics for a label containing either one or multiple lines of text.
 */
public final class ChartTextMetrics extends TextAdapter
{

	private int iLineCount = 0;

	private Object oText = null;

	private Graphics2D g2d = null;

	private FontMetrics fm = null;

	private ChartTextLayout[] tla = null;

	private String[] fsa = null;

	private transient Object bi = null;

	private Label la = null;

	private final IDisplayServer xs;

	private Insets ins = null;

	/**
	 * The constructor initializes a tiny image that provides a graphics context
	 * capable of performing computations in the absence of a visual component
	 * 
	 * @param _xs
	 * @param _la
	 */
	public ChartTextMetrics( IDisplayServer _xs, Label _la )
	{
		if ( bi == null )
		{
			bi = new BufferedImage( 1, 1, BufferedImage.TYPE_INT_RGB );
			g2d = (Graphics2D) ( (BufferedImage) bi ).getGraphics( );

			g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON );
			g2d.setRenderingHint( RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY );
		}
		else
		{
			g2d = (Graphics2D) ( (BufferedImage) bi ).getGraphics( );
		}
		xs = _xs;
		la = _la;

		computeTextAntialiasing( );
		reuse( la );
	}

	/**
	 * Only antialias rotated, bold text, and font size > 13
	 * 
	 */
	private void computeTextAntialiasing( )
	{
		FontDefinition font = la.getCaption( ).getFont( );

		if ( font.isBold( )
				|| ( font.getRotation( ) % 90 != 0 )
				|| font.getSize( ) > 13 )
		{
			g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		}
		else
		{
			g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF );
		}

	}

	/**
	 * Allows reuse of the multi-line text element for computing bounds of
	 * changed font/text content
	 * 
	 * @param fd
	 */
	public final void reuse( Label la, double forceWrappingSize )
	{
		final Font f = (Font) xs.createFont( la.getCaption( ).getFont( ) );
		fm = g2d.getFontMetrics( f );
		final FontRenderContext frc = g2d.getFontRenderContext( );

		String s = la.getCaption( ).getValue( );
		if ( s == null )
		{
			s = IConstants.NULL_STRING;
		}
		else
		{
			// trim leading and trailing spaces.
			s = s.trim( );
		}

		if ( s.length( ) == 0 ) // TextLayout DOESN'T LIKE EMPTY STRINGS
		{
			s = IConstants.ONE_SPACE;
		}
		String[] sa = splitOnBreaks( s, forceWrappingSize );
		if ( sa == null )
		{
			iLineCount = 1;
			oText = s;
			tla = new ChartTextLayout[1];
			fsa = new String[1];
			tla[0] = new ChartTextLayout( s, f.getAttributes( ), frc );
			fsa[0] = s;
		}
		else
		{
			iLineCount = sa.length;
			oText = sa;
			tla = new ChartTextLayout[iLineCount];
			fsa = new String[iLineCount];
			for ( int i = 0; i < iLineCount; i++ )
			{
				tla[i] = new ChartTextLayout( sa[i], f.getAttributes( ), frc );
				fsa[i] = sa[i];
			}
		}
		ins = la.getInsets( ).scaledInstance( pointsToPixels( ) );

		if ( forceWrappingSize > 0 )
		{
			// update label with new broken content.
			StringBuffer sb = new StringBuffer( );
			for ( int i = 0; i < fsa.length; i++ )
			{
				sb.append( fsa[i] ).append( "\n" ); //$NON-NLS-1$
			}

			if ( sb.length( ) > 0 )
			{
				sb.deleteCharAt( sb.length( ) - 1 );
			}

			la.getCaption( ).setValue( sb.toString( ) );
		}
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getHeight( )
	{
		return fm.getHeight( );
	}

	/**
	 * 
	 * @param fm
	 * @return
	 */
	public final double getDescent( )
	{
		return fm.getDescent( );
	}

	/**
	 * 
	 * @param fm
	 * @return The width of the line containing the maximum width (if multiline
	 *         split by hard breaks) or the width of the single line of text
	 */
	private final double stringWidth( )
	{
		Rectangle2D r2d;

		if ( iLineCount > 1 )
		{
			double dWidth, dMaxWidth = 0;
			for ( int i = 0; i < iLineCount; i++ )
			{
				/**
				 * //r2d = tla[i].getBounds( );
				 * 
				 * Not use the textLayout.getBounds(), this is not correct when
				 * the string contains full pitch characters.
				 */
				r2d = fm.getStringBounds( fsa[0], g2d );

				dWidth = r2d.getWidth( );
				if ( dWidth > dMaxWidth )
				{
					dMaxWidth = dWidth;
				}
			}

			/**
			 * Fixed for java.awt.font.TextLine.getBounds() bug, when string is
			 * blank, e.g. " ", it will return an negative result.
			 */
			return Math.max( 0, dMaxWidth );
		}
		else if ( iLineCount == 1 )
		{
			/**
			 * // double w = tla[0].getBounds( ).getWidth( );
			 * 
			 * Not use the textLayout.getBounds(), this is not correct when the
			 * string contains full pitch characters.
			 */
			double w = fm.getStringBounds( fsa[0], g2d ).getWidth( );

			/**
			 * Fixed for java.awt.font.TextLine.getBounds() bug, when string is
			 * blank, e.g. " ", it will return an negative result.
			 */
			return Math.max( 0, w );
		}

		return 0;
	}

	final double pointsToPixels( )
	{
		return ( xs.getDpiResolution( ) / 72d );
	}

	public final double getFullHeight( )
	{
		return getHeight( )
				* getLineCount( )
				+ ( ins.getTop( ) + ins.getBottom( ) );
	}

	public final double getFullWidth( )
	{
		return stringWidth( ) + ( ins.getLeft( ) + ins.getRight( ) );
	}

	/**
	 * 
	 * @return The number of lines created due to the hard breaks inserted
	 */
	public final int getLineCount( )
	{
		return iLineCount;
	}

	/**
	 * 
	 * @return The line requested for
	 */
	public final String getLine( int iIndex )
	{
		return ( iLineCount > 1 ) ? ( (String[]) oText )[iIndex]
				: (String) oText;
	}

	public final ChartTextLayout getLayout( int iIndex )
	{
		return ( iLineCount > 1 ) ? tla[iIndex] : tla[0];
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private String[] splitOnBreaks( String s, double maxSize )
	{
		final ArrayList<String> al = new ArrayList<String>( );

		int i = 0, j;
		do
		{
			j = s.indexOf( '\n', i );
			if ( j == -1 )
			{
				j = s.length( );
			}
			String ss = s.substring( i, j );
			if ( ss != null && ss.length( ) > 0 )
			{
				// check max size.
				if ( maxSize > 0 )
				{
					Rectangle2D size = fm.getStringBounds( ss, g2d );
					if ( size.getWidth( ) > maxSize )
					{
						// try fuzzy match first
						int estCount = (int) ( maxSize / size.getWidth( ) )
								* ss.length( );

						if ( estCount < 1 )
						{
							estCount = ss.length( );
						}

						String fs;
						Rectangle2D fsize;
						int curPos = 0;

						while ( ss.length( ) > 0 )
						{
							fs = ss.substring( 0, Math.min( estCount,
									ss.length( ) ) );
							fsize = fm.getStringBounds( fs, g2d );

							if ( fsize.getWidth( ) <= maxSize )
							{
								al.add( fs );
								curPos = fs.length( );
							}
							else
							{
								boolean matched = false;

								// decrease the count and test again.
								int curCount = Math.min( estCount - 1,
										ss.length( ) );
								while ( curCount > 1 )
								{
									fs = ss.substring( 0, curCount );
									fsize = fm.getStringBounds( fs, g2d );

									if ( fsize.getWidth( ) <= maxSize )
									{
										al.add( fs );
										curPos = fs.length( );
										matched = true;
										break;
									}
									else
									{
										curCount--;
									}
								}

								if ( !matched )
								{
									al.add( fs );
									curPos = fs.length( );
								}
							}

							ss = ss.substring( curPos );
							curPos = 0;
						}

					}
					else
					{
						al.add( ss );
					}
				}
				else
				{
					al.add( ss );
				}
			}
			i = j + 1;
		} while ( j != -1 && j < s.length( ) );

		final int n = al.size( );
		if ( n == 1 || n == 0 )
		{
			return null;
		}

		final String[] sa = new String[n];
		for ( i = 0; i < al.size( ); i++ )
		{
			sa[i] = al.get( i );
		}
		return sa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.ITextMetrics#dispose()
	 */
	public void dispose( )
	{
		if ( bi != null )
		{
			( (BufferedImage) bi ).flush( );
			bi = null;
			g2d.dispose( );
			g2d = null;
		}
	}
}