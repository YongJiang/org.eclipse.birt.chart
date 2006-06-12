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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * FontCanvas
 */
public class FontCanvas extends Canvas implements
		PaintListener,
		DisposeListener
{

	private FontDefinition fdCurrent = null;

	private ColorDefinition cdCurrent = null;

	private boolean bUseColor = true;

	private boolean bUseAlignment = true;

	private boolean bUseSize = true;

	private IDeviceRenderer idr;

	/**
	 * @param parent
	 *            Parent composite to which the canvas is to be added
	 * @param style
	 *            SWT style for this composite
	 * @param fdSelected
	 *            FontDefinition instance that holds the font information to be
	 *            displayed
	 * @param cdSelected
	 *            ColorDefinition instance that provides the foreground color
	 *            for text to be displayed
	 */
	public FontCanvas( Composite parent, int style, FontDefinition fdSelected,
			ColorDefinition cdSelected, boolean bUseSize, boolean bUseColor,
			boolean bUseAlignment )
	{
		super( parent, style );
		this.setSize( parent.getClientArea( ).x, parent.getClientArea( ).x );
		this.fdCurrent = fdSelected == null ? FontDefinitionImpl.createEmpty( )
				: fdSelected;
		this.cdCurrent = cdSelected == null ? ColorDefinitionImpl.create( 0,
				0,
				0 ) : cdSelected;
		this.bUseColor = bUseColor;
		this.bUseAlignment = bUseAlignment;
		this.bUseSize = bUseSize;

		try
		{
			idr = PluginSettings.instance( ).getDevice( "dv.SWT" ); //$NON-NLS-1$
		}
		catch ( ChartException pex )
		{
			idr = null;
			WizardBase.displayException( pex );
		}
		addDisposeListener( this );
		addPaintListener( this );
	}

	public void setFontDefinition( FontDefinition fdSelected )
	{
		this.fdCurrent = fdSelected;
	}

	public void setColor( ColorDefinition cdSelected )
	{
		this.cdCurrent = cdSelected;
	}

	/*
	 * (non-Javadoc) Overridden method to render text based on specified font
	 * information.
	 * 
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
	 */
	public void paintControl( PaintEvent pe )
	{
		if ( idr != null && fdCurrent != null && bUseSize )
		{
			idr.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, pe.gc );

			TextRenderEvent tre = new TextRenderEvent( this );
			tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK );

			TextAlignment ta = TextAlignmentImpl.create( );
			ta.setHorizontalAlignment( HorizontalAlignment.CENTER_LITERAL );
			ta.setVerticalAlignment( VerticalAlignment.CENTER_LITERAL );
			tre.setBlockAlignment( ta );

			Bounds bo = BoundsImpl.create( 0,
					0,
					this.getSize( ).x,
					this.getSize( ).y );
			tre.setBlockBounds( bo );

			String fontName = ChartUIUtil.getFontName( fdCurrent );
			Text tx = TextImpl.create( fontName );
			FontDefinition fd = FontDefinitionImpl.copyInstance( fdCurrent );
			fd.setName( fontName );
			if ( !fd.isSetSize( ) )
			{
				fd.setSize( 9 );
			}
			tx.setFont( fd );

			ColorDefinition cdFore, cdBack;
			if ( !this.isEnabled( ) )
			{
				Color cFore = Display.getCurrent( )
						.getSystemColor( SWT.COLOR_DARK_GRAY );
				Color cBack = Display.getCurrent( )
						.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );
				cdFore = ColorDefinitionImpl.create( cFore.getRed( ),
						cFore.getGreen( ),
						cFore.getBlue( ) );
				cdBack = ColorDefinitionImpl.create( cBack.getRed( ),
						cBack.getGreen( ),
						cBack.getBlue( ) );
			}
			else
			{
				Color cBack = Display.getCurrent( )
						.getSystemColor( SWT.COLOR_LIST_BACKGROUND );
				cdBack = ColorDefinitionImpl.create( cBack.getRed( ),
						cBack.getGreen( ),
						cBack.getBlue( ) );
				if ( cdCurrent != null && bUseColor )
				{
					cdFore = ColorDefinitionImpl.copyInstance( cdCurrent );
				}
				else
				{
					cdFore = ColorDefinitionImpl.BLACK( );
				}
			}
			tx.setColor( cdFore );

			Label lb = LabelImpl.create( );
			lb.setBackground( cdBack );
			lb.setCaption( tx );
			tre.setLabel( lb );

			RectangleRenderEvent rre = new RectangleRenderEvent( this );
			rre.setBounds( bo );
			rre.setBackground( cdBack );

			try
			{
				idr.fillRectangle( rre );
				idr.drawText( tre );
			}
			catch ( ChartException e )
			{
				// ignore;
			}

			return;
		}

		Font fSize = null;
		Font fCurrent = null;
		Color cFore = null;
		Color cBack = null;
		GC gc = pe.gc;
		Font fOld = gc.getFont( );

		if ( !this.isEnabled( ) )
		{
			cFore = Display.getCurrent( ).getSystemColor( SWT.COLOR_DARK_GRAY );
			cBack = Display.getCurrent( )
					.getSystemColor( SWT.COLOR_WIDGET_BACKGROUND );
		}
		else
		{
			cBack = Display.getCurrent( )
					.getSystemColor( SWT.COLOR_LIST_BACKGROUND );
			if ( cdCurrent != null
					&& bUseColor
					&& cdCurrent.getTransparency( ) > 0 )
			{
				cFore = new Color( this.getDisplay( ),
						cdCurrent.getRed( ),
						cdCurrent.getGreen( ),
						cdCurrent.getBlue( ) );
			}
			else
			{
				cFore = new Color( this.getDisplay( ), 0, 0, 0 );
			}
		}
		gc.setForeground( cFore );
		gc.setBackground( cBack );
		gc.fillRectangle( 0, 0, this.getSize( ).x, this.getSize( ).y );
		if ( fdCurrent != null )
		{
			// Handle styles
			int iStyle = ( fdCurrent.isSetBold( ) && fdCurrent.isBold( ) ) ? SWT.BOLD
					: SWT.NORMAL;
			iStyle |= ( fdCurrent.isSetItalic( ) && fdCurrent.isItalic( ) ) ? SWT.ITALIC
					: iStyle;

			String sFontName = ChartUIUtil.getFontName( fdCurrent );
			if ( !bUseSize )
			{
				gc.setClipping( 2, 2, this.getSize( ).x - 40, 26 );
				fCurrent = new Font( this.getDisplay( ),
						ChartUIUtil.getFontName( fdCurrent ),
						fOld.getFontData( )[0].getHeight( ),
						iStyle );
			}
			else
			{
				fCurrent = new Font( this.getDisplay( ),
						ChartUIUtil.getFontName( fdCurrent ),
						fdCurrent.isSetSize( ) ? (int) fdCurrent.getSize( ) : 9,
						iStyle );
			}
			gc.setFont( fCurrent );

			// Calculate the location to render text
			int iStartX = 5;
			int iStartY = 3;
			if ( bUseAlignment )
			{
				if ( ChartUIUtil.getFontTextAlignment( fdCurrent )
						.getHorizontalAlignment( )
						.equals( HorizontalAlignment.LEFT_LITERAL ) )
				{
					iStartX = 5;
				}
				else if ( ChartUIUtil.getFontTextAlignment( fdCurrent )
						.getHorizontalAlignment( )
						.equals( HorizontalAlignment.CENTER_LITERAL ) )
				{
					iStartX = this.getSize( ).x
							/ 2
							- ( getStringWidth( gc, sFontName ).x / 2 );
				}
				else if ( ChartUIUtil.getFontTextAlignment( fdCurrent )
						.getHorizontalAlignment( )
						.equals( HorizontalAlignment.RIGHT_LITERAL ) )
				{
					iStartX = this.getSize( ).x
							- getStringWidth( gc, sFontName ).x
							- 5;
				}
				if ( ChartUIUtil.getFontTextAlignment( fdCurrent )
						.getVerticalAlignment( )
						.equals( VerticalAlignment.TOP_LITERAL ) )
				{
					iStartY = 3;
				}
				else if ( ChartUIUtil.getFontTextAlignment( fdCurrent )
						.getVerticalAlignment( )
						.equals( VerticalAlignment.CENTER_LITERAL ) )
				{
					iStartY = ( this.getSize( ).y / 2 );
					if ( bUseSize )
					{
						iStartY -= ( getStringWidth( gc, sFontName ).y / 2 );
					}
					else
					{
						iStartY -= 15;
					}
				}
				else if ( ChartUIUtil.getFontTextAlignment( fdCurrent )
						.getVerticalAlignment( )
						.equals( VerticalAlignment.BOTTOM_LITERAL ) )
				{
					iStartY = this.getSize( ).y;
					if ( bUseSize )
					{
						iStartY -= ( getStringWidth( gc, sFontName ).y ) + 5;
					}
					else
					{
						iStartY -= 30;
					}
				}
			}

			gc.drawText( sFontName, iStartX, iStartY );

			if ( fdCurrent.isUnderline( ) )
			{
				gc.drawLine( iStartX, iStartY
						+ getStringWidth( gc, sFontName ).y
						- gc.getFontMetrics( ).getDescent( ), iStartX
						+ getStringWidth( gc, sFontName ).x
						- gc.getFontMetrics( ).getDescent( ), iStartY
						+ getStringWidth( gc, sFontName ).y
						- gc.getFontMetrics( ).getDescent( ) );
			}

			if ( fdCurrent.isStrikethrough( ) )
			{
				gc.drawLine( iStartX,
						iStartY + ( getStringWidth( gc, sFontName ).y / 2 ) + 1,
						iStartX + getStringWidth( gc, sFontName ).x,
						iStartY + ( getStringWidth( gc, sFontName ).y / 2 ) + 1 );
			}

			if ( !bUseSize )
			{
				gc.setClipping( 1, 1, this.getSize( ).x, this.getSize( ).y );
				fSize = new Font( this.getDisplay( ),
						"Sans-Serif", fOld.getFontData( )[0].getHeight( ), SWT.NORMAL ); //$NON-NLS-1$
				gc.setFont( fSize );

				String sizeString = "(" + ( fdCurrent.isSetSize( ) ? String.valueOf( (int) fdCurrent.getSize( ) ) : "Auto" ) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				Point pt = gc.textExtent( sizeString );
				gc.drawText( sizeString, this.getSize( ).x
						- pt.x
						- this.getBorderWidth( )
						- 2, ( this.getSize( ).y - pt.y ) / 2 - 1 );

				fSize.dispose( );
			}

			fCurrent.dispose( );
		}
		if ( this.isEnabled( ) )
		{
			cFore.dispose( );
		}
		gc.setFont( fOld );
	}

	private Point getStringWidth( GC gc, String sText )
	{
		return gc.textExtent( sText );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e )
	{
		if ( idr != null )
		{
			idr.dispose( );
			idr = null;
		}
	}

}