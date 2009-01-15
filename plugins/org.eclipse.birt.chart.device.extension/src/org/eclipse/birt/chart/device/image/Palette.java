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

package org.eclipse.birt.chart.device.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * Representation of an indexed palette of arbitrary size. This can be used by
 * image encoders which require limited palettes (eg 256 colour PNGs). The
 * palette is auto-generated from a number of images and potentially some extra
 * colours which must be included. Optional transparency is supported, but only
 * in a binary way - any colour which is not totally opaque is deemed to be
 * totally transparent. (This means we can get away with a single index for all
 * transparent colours.) The generation method is pretty basic, removing a
 * single colour at a time. This is fine for images with a smallish number of
 * colours to begin with (eg tiled collections of 256-colour images) but bad for
 * full-colour photos.
 */
final class Palette
{

	Vector<ColourEntry> colours = new Vector<ColourEntry>( );

	int transparentIndex;

	Palette( BufferedImage[] images, Color[] extraColours, int size,
			boolean wantTransparent )
	{
		Hashtable<Integer,ColourEntry> tempHash = SecurityUtil.newHashtable( );
		ColourEntry transparent = new ColourEntry( );

		if ( wantTransparent )
		{
			transparent.population = 0;
			transparent.argb = 0;

			colours.addElement( transparent );
			transparentIndex = 0;
		}
		else
			transparentIndex = -1;

		// Create color population from images
		int imagePop = 0;
		for ( int k = 0; k < images.length; k++ )
		{
			BufferedImage image = images[k];
			int w = image.getWidth( ), h = image.getHeight( );
			int[] row = new int[w];
			imagePop += h * w;
			// Work out which colors are used.
			for ( int i = 0; i < h; i++ )
			{
				image.getRGB( 0, i, w, 1, row, 0, w );
				for ( int j = 0; j < w; j++ )
				{
					int col = row[j];
					Integer x = new Integer( col );
					ColourEntry ce = tempHash.get( x );
					if ( ce == null )
					{
						// Map non-opaque colors appropriately
						if ( ( col >>> 24 ) != 0xff )
							ce = transparent;
						else
						{
							ce = new ColourEntry( );
							ce.argb = col;
							ce.population = 0;
							colours.add( ce );
						}
						tempHash.put( x, ce );
					}
					ce.population++;
				}
			}
		}

		// Now from extra colors - double the total population.
		if ( extraColours != null )
		{
			for ( int i = 0; i < extraColours.length; i++ )
			{
				int col = extraColours[i].getRGB( );
				Integer x = new Integer( col );
				ColourEntry ce = tempHash.get( x );
				if ( ce == null )
				{
					// Map non-opaque colors appropriately
					if ( ( col >>> 24 ) != 0xff )
						ce = transparent;
					else
					{
						ce = new ColourEntry( );
						ce.argb = col;
						ce.population = 0;
						colours.add( ce );
					}
					tempHash.put( x, ce );
				}
				ce.population += imagePop / extraColours.length;
			}
		}

		/*
		 * Reduction method: Find the colour with the minimum population Find
		 * the colour closest to it Merge the two
		 */
		while ( colours.size( ) > size )
		{
			int l = colours.size( );
			ColourEntry minCe = colours.elementAt( 1 );
			int minPop = minCe.population;
			int minIndex = 1;
			// Find colour with min population
			for ( int i = 2; i < l; i++ )
			{
				ColourEntry ce = colours.elementAt( i );
				if ( ce.population < minPop )
				{
					minPop = ce.population;
					minCe = ce;
					minIndex = i;
				}
			}
			// Find colour closest to it
			int closeIndex = 1;
			if ( minIndex == 1 )
				closeIndex = 2;
			ColourEntry closeCe = colours.elementAt( closeIndex );
			int closeDiff = closeCe.compare( minCe );
			for ( int i = closeIndex + 1; i < l; i++ )
			{
				if ( i == minIndex )
					continue;
				ColourEntry ce =  colours.elementAt( i );
				int diff = ce.compare( minCe );
				if ( diff < closeDiff )
				{
					closeDiff = diff;
					closeCe = ce;
					closeIndex = i;
				}
			}
			// Merge the two
			int totalPop = closeCe.population + minCe.population;
			closeCe.setRed( ( closeCe.getRed( ) * closeCe.population + minCe.getRed( )
					* minCe.population )
					/ totalPop );
			closeCe.setGreen( ( closeCe.getGreen( ) * closeCe.population + minCe.getGreen( )
					* minCe.population )
					/ totalPop );
			closeCe.setBlue( ( closeCe.getBlue( ) * closeCe.population + minCe.getBlue( )
					* minCe.population )
					/ totalPop );
			closeCe.population = totalPop;
			minCe.follow = closeCe;
			colours.removeElementAt( minIndex );
		}

		if ( wantTransparent )
		{
			// Find an unused RGB value for transparent, just for clarity
			// - there are some braindead tools around...
			// Start off with white and work down...
			int argb = 0xffffffff;
			while ( true )
			{
				ColourEntry ce = tempHash.get( new Integer( argb ) );
				if ( ce == null )
					break;
				argb--;
			}
			transparent.argb = ( argb & 0xffffff );
		}
		for ( int i = 0; i < colours.size( ); i++ )
		{
			ColourEntry ce = colours.elementAt( i );
			ce.index = i;
			hashPut( ce.argb, ce );
		}
	}

	Palette( BufferedImage image, int size, boolean wantTransparent )
	{
		this( new BufferedImage[]{
			image
		}, null, size, wantTransparent );
	}

	int getRed( int index )
	{
		return ( colours.elementAt( index ) ).getRed( );
	}

	int getGreen( int index )
	{
		return ( colours.elementAt( index ) ).getGreen( );
	}

	int getBlue( int index )
	{
		return ( colours.elementAt( index ) ).getBlue( );
	}

	int getSize( )
	{
		return colours.size( );
	}

	int getTransparentIndex( )
	{
		return transparentIndex;
	}

	byte[] getIndices( int[] argbs, int offset, int length )
	{
		byte[] ret = new byte[length];
		for ( int index = offset, l = 0; l < length; index++, l++ )
			ret[l] = (byte) getIndex( argbs[index] );
		return ret;
	}

	int getIndex( int argb )
	{
		if ( ( argb >>> 24 ) != 0xff && transparentIndex != -1 )
			return transparentIndex;
		ColourEntry ce = hashGet( argb );
		if ( ce == null )
		{
			// Find colour closest to it
			int minIndex = 0;
			int minDiff = Integer.MAX_VALUE;
			for ( int i = 0; i < colours.size( ); i++ )
			{
				if ( i == transparentIndex )
					continue;
				ColourEntry entry = colours.elementAt( i );
				int diff = entry.compare( argb );
				if ( diff < minDiff )
				{
					minDiff = diff;
					minIndex = i;
				}
			}
			ce = colours.elementAt( minIndex );
			hashPut( argb, ce );
		}
		return ce.index;
	}

	private ColourEntry[][][] ces = new ColourEntry[256][][];

	private ColourEntry hashGet( int argb )
	{
		int red = ( argb >> 16 ) & 0xff;
		int green = ( argb >> 8 ) & 0xff;
		int blue = argb & 0xff;

		if ( ces[red] == null || ces[red][green] == null )
			return null;
		return ces[red][green][blue];
	}

	private void hashPut( int argb, ColourEntry ce )
	{
		int red = ( argb >> 16 ) & 0xff;
		int green = ( argb >> 8 ) & 0xff;
		int blue = argb & 0xff;

		if ( ces[red] == null )
			ces[red] = new ColourEntry[256][];
		if ( ces[red][green] == null )
			ces[red][green] = new ColourEntry[256];
		ces[red][green][blue] = ce;
	}

	private static class ColourEntry
	{

		int population;

		int argb;

		ColourEntry follow = null;

		int index;

		/*
		 * Compare two colours. We ignore the alpha value as transparency is
		 * just the first colour, which is ignored in the algorithm.
		 */
		int compare( ColourEntry ce )
		{
			int x = argb, y = ce.argb;
			int ret = 0;
			for ( int i = 0; i < 3; i++ )
			{
				int dist = ( x & 0xff ) - ( y & 0xff );
				ret += dist * dist;
				x >>>= 8;
				y >>>= 8;
			}
			return ret;
		}

		/*
		 * Compare two colours. We ignore the alpha value as transparency is
		 * just the first colour, which is ignored in the algorithm.
		 */
		int compare( int y )
		{
			int x = argb;
			int ret = 0;
			for ( int i = 0; i < 3; i++ )
			{
				int dist = ( x & 0xff ) - ( y & 0xff );
				ret += dist * dist;
				x >>>= 8;
				y >>>= 8;
			}
			return ret;
		}

		int getRed( )
		{
			return ( argb >> 16 ) & 0xff;
		}

		int getBlue( )
		{
			return argb & 0xff;
		}

		int getGreen( )
		{
			return ( argb >> 8 ) & 0xff;
		}

		void setRed( int red )
		{
			argb = ( argb & 0xff00ffff ) | ( red << 16 );
		}

		void setGreen( int green )
		{
			argb = ( argb & 0xffff00ff ) | ( green << 8 );
		}

		void setBlue( int blue )
		{
			argb = ( argb & 0xffffff00 ) | blue;
		}
	}

}