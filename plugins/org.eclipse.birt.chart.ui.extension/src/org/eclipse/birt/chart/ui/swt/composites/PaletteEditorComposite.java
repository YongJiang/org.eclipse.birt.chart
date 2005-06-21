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
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * This class implements a palette editor widget capable of maintaining a list
 * of fill definitions. Entries may be updated, new entries added at the end or
 * the existing fill entries may be removed.
 */
public final class PaletteEditorComposite extends Composite implements
		PaintListener,
		ControlListener,
		DisposeListener,
		SelectionListener,
		MouseListener,
		Listener
{

	/**
	 * An internally maintained list of fills directly referenced into a palette
	 */
	private final EList elPaletteEntries;

	/**
	 * Miscellaneous variables used in
	 */
	private int iViewY = 0, iViewHeight = 0, iVisibleCount = 0,
			iSelectedIndex = 0;

	/**
	 * The height of each color entry in the palette
	 */
	private final int iItemHeight = 30;

	/**
	 * The vertical scrollbar associated with the widget
	 */
	private final ScrollBar sb;

	/**
	 * An offscreen image used to render the palette entries using double
	 * buffering. This image is re-created when a composite resize occurs.
	 */
	private Image imgBuffer = null;

	/**
	 * Associated with the offscreen image and whose lifecycle depends on the
	 * buffered image's lifecycle
	 */
	private GC gc = null;

	/**
	 * Used to edit the color definition in-place
	 */
	private Control coEditor = null;

	/**
	 * Buttons provided to alter the contents of the palette
	 */
	private Button btnAdd, btnRemove, btnUp, btnDown;

	/**
	 *  
	 */
	private FillChooserComposite fccNewEntry = null;

	/**
	 *  
	 */
	private Composite coPaletteEntries = null;

	/**
	 *  
	 */
	private IDeviceRenderer idrSWT = null;

	/**
	 *  
	 */
	private IDisplayServer idsSWT = null;

	/**
	 * The constructor expects a default palette
	 * 
	 * @param coParent
	 * @param alFills
	 */
	public PaletteEditorComposite( Composite coParent, Palette pa )
	{
		super( coParent, SWT.NONE );
		GridLayout gl = new GridLayout( );
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		setLayout( gl );

		coPaletteEntries = new Composite( this, SWT.V_SCROLL );
		GridData gd = new GridData( GridData.FILL_BOTH );
		coPaletteEntries.setLayoutData( gd );
		elPaletteEntries = pa.getEntries( );
		sb = coPaletteEntries.getVerticalBar( );
		sb.addSelectionListener( this );

		Composite coControlPanel = new Composite( this, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		coControlPanel.setLayoutData( gd );
		gl = new GridLayout( );
		gl.numColumns = 5;
		coControlPanel.setLayout( gl );
		btnAdd = new Button( coControlPanel, SWT.PUSH );
		gd = new GridData( );
		btnAdd.setLayoutData( gd );
		btnAdd.setText( Messages.getString( "PaletteEditorComposite.Lbl.Add" ) ); //$NON-NLS-1$
		btnAdd.addSelectionListener( this );
		fccNewEntry = new FillChooserComposite( coControlPanel,
				SWT.NONE,
				ColorDefinitionImpl.WHITE( ),
				true,
				true );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		fccNewEntry.setLayoutData( gd );
		btnRemove = new Button( coControlPanel, SWT.PUSH );
		gd = new GridData( );
		btnRemove.setLayoutData( gd );
		btnRemove.setText( Messages.getString( "PaletteEditorComposite.Lbl.Remove" ) ); //$NON-NLS-1$
		btnRemove.addSelectionListener( this );
		btnUp = new Button( coControlPanel, SWT.ARROW | SWT.UP );
		gd = new GridData( );
		btnUp.setLayoutData( gd );
		btnUp.setText( Messages.getString( "PaletteEditorComposite.Lbl.Up" ) ); //$NON-NLS-1$
		btnUp.addSelectionListener( this );
		btnDown = new Button( coControlPanel, SWT.ARROW | SWT.DOWN );
		gd = new GridData( );
		btnDown.setLayoutData( gd );
		btnDown.setText( Messages.getString( "PaletteEditorComposite.Lbl.Down" ) ); //$NON-NLS-1$
		btnDown.addSelectionListener( this );

		addControlListener( this );
		addDisposeListener( this );
		coPaletteEntries.addPaintListener( this );
		coPaletteEntries.addMouseListener( this );

		final PluginSettings ps = PluginSettings.instance( );
		try
		{
			idrSWT = ps.getDevice( "dv.SWT" ); //$NON-NLS-1$
			idsSWT = idrSWT.getDisplayServer( );
		}
		catch ( ChartException pex )
		{
			DefaultLoggerImpl.instance( ).log( pex );
			return;
		}
	}

	/**
	 * Repaints the palette content
	 */
	public final void paintControl( PaintEvent pev )
	{
		Composite co = (Composite) pev.getSource( );
		GC gcComposite = pev.gc;
		Display d = pev.display;
		Rectangle rCA = coPaletteEntries.getClientArea( );
		if ( coEditor == null )
		{
			coEditor = new FillChooserComposite( co, SWT.NONE, null, true, true );
			coEditor.setBounds( 3, 3, rCA.width - 6, iItemHeight - 6 );
			( (FillChooserComposite) coEditor ).addListener( this );
		}

		if ( imgBuffer == null )
		{
			imgBuffer = new Image( d, rCA.width, rCA.height );
			gc = new GC( imgBuffer );
			idrSWT.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, gc );
		}
		gc.setBackground( getBackground( ) );
		gc.fillRectangle( rCA );

		iViewHeight = rCA.height;
		int iStartIndex = iViewY / iItemHeight;
		if ( iStartIndex < 0 )
		{
			iStartIndex = 0;
		}
		iVisibleCount = iViewHeight / iItemHeight + 2;
		int iAvailableItems = Math.min( iVisibleCount, elPaletteEntries.size( )
				- iStartIndex );
		int iY = -( iViewY % iItemHeight );

		gc.setForeground( d.getSystemColor( SWT.COLOR_GRAY ) );

		final RectangleRenderEvent rre = (RectangleRenderEvent) ( (EventObjectCache) idrSWT ).getEventObject( this,
				RectangleRenderEvent.class );
		final Bounds bo = BoundsImpl.create( 0, 0, 0, 0 );
		rre.setOutline( LineAttributesImpl.create( ColorDefinitionImpl.BLACK( ),
				LineStyle.SOLID_LITERAL,
				1 ) );
		rre.setBounds( bo );
		Fill fi;

		for ( int i = iStartIndex; i < iStartIndex + iAvailableItems; i++ )
		{
			fi = (Fill) elPaletteEntries.get( i );
			bo.set( 3, iY + 3, rCA.width - 6, iItemHeight - 6 );
			rre.setBackground( fi );
			try
			{
				idrSWT.fillRectangle( rre );
				idrSWT.drawRectangle( rre );
			}
			catch ( ChartException rex )
			{
				DefaultLoggerImpl.instance( ).log( rex );
			}
			if ( i == iSelectedIndex )
			{
				// WITHIN RANGE; SHOW EDITOR AND UPDATE POSITION
				if ( !coEditor.isVisible( ) )
				{
					coEditor.setVisible( true );
				}
				coEditor.setLocation( 3, iY + 3 );
				( (FillChooserComposite) coEditor ).setFill( fi );
			}
			iY += iItemHeight;
		}

		// OUT OF RANGE; HIDE EDITOR
		if ( iSelectedIndex < iStartIndex
				|| iSelectedIndex >= iStartIndex + iAvailableItems )
		{
			if ( coEditor.isVisible( ) )
			{
				coEditor.setVisible( false );
			}
		}
		gcComposite.drawImage( imgBuffer, rCA.x, rCA.y );
	}

	/**
	 * The scrollbar's thumb is updated based on the palette entry count and the
	 * current selection
	 */
	private final void updateScrollBar( )
	{
		sb.setPageIncrement( iViewHeight );
		sb.setMaximum( iItemHeight * elPaletteEntries.size( ) - iViewHeight );
		sb.setSelection( iViewY );
	}

	/**
	 * 
	 * @param iIndex
	 */
	private final void scrollToView( int iIndex )
	{
		if ( iIndex == -1 )
		{
			return;
		}

		int iStartIndex = iViewY / iItemHeight;
		if ( iStartIndex < 0 )
		{
			iStartIndex = 0;
		}

		if ( iIndex > iStartIndex && iIndex < iStartIndex + iVisibleCount - 1 )
		{
			iViewY = ( iIndex * iItemHeight ) - iViewHeight + iItemHeight;
			if ( iViewY < 0 )
				iViewY = 0;
		}
		else if ( iIndex <= iStartIndex )
		{
			int iMoveUpTo = iIndex - iVisibleCount;
			if ( iMoveUpTo < 0 )
			{
				iMoveUpTo = 0;
			}
			iViewY = iMoveUpTo * iItemHeight;
		}
		else
		{
			// ADJUST LOWER END IF WE GO BEYOND
			int iY = ( iIndex - iStartIndex )
					* iItemHeight
					- ( iViewY % iItemHeight );
			if ( iY + iItemHeight > iViewHeight ) // BELOW THE LOWER EDGE
			{
				iViewY += iY + iItemHeight - iViewHeight;
			}
		}
		updateScrollBar( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlResized( ControlEvent arg0 )
	{
		updateScrollBar( );
		if ( imgBuffer != null )
		{
			gc.dispose( );
			imgBuffer.dispose( );
			gc = null;
			imgBuffer = null;
		}

		if ( coEditor != null )
		{
			final Rectangle rCA = coPaletteEntries.getClientArea( );
			coEditor.setSize( rCA.width - 6, iItemHeight - 6 );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent arg0 )
	{
		if ( imgBuffer != null )
		{
			gc.dispose( );
			imgBuffer.dispose( );
			gc = null;
			imgBuffer = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent sev )
	{
		if ( sev.getSource( ) == sb )
		{
			int iSelection = sb.getSelection( );
			iViewY = iSelection;
			coPaletteEntries.redraw( );
		}
		else
		{
			final Button btn = (Button) sev.getSource( );
			if ( btn == btnAdd )
			{
				append( (Fill) EcoreUtil.copy( fccNewEntry.getFill( ) ) );
			}
			else if ( btn == btnRemove )
			{
				remove( iSelectedIndex );
			}
			else if ( ( btn.getStyle( ) & SWT.UP ) == SWT.UP )
			{
				if ( iSelectedIndex > 0 )
				{
					swap( iSelectedIndex, iSelectedIndex - 1 );
				}
			}
			else if ( ( btn.getStyle( ) & SWT.DOWN ) == SWT.DOWN )
			{
				if ( iSelectedIndex < elPaletteEntries.size( ) - 1 )
				{
					swap( iSelectedIndex, iSelectedIndex + 1 );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown( MouseEvent mev )
	{
		int iClickedY = mev.y;
		int iStartIndex = iViewY / iItemHeight;
		if ( iStartIndex < 0 )
		{
			iStartIndex = 0;
		}
		int iY = -( iViewY % iItemHeight );
		int iClickedIndex = iStartIndex + ( iClickedY - iY ) / iItemHeight;
		if ( iClickedIndex < 0 || iClickedIndex > elPaletteEntries.size( ) )
		{
			return;
		}

		iSelectedIndex = iClickedIndex;
		coPaletteEntries.redraw( );
	}

	/**
	 * Returns the index of the currently selected fill in the palette list
	 * 
	 * @return
	 */
	public final int getSelectedIndex( )
	{
		return iSelectedIndex;
	}

	/**
	 * Returns the currently selected fill
	 * 
	 * @return
	 */
	public final ColorDefinitionImpl getSelectedFill( )
	{
		return (ColorDefinitionImpl) elPaletteEntries.get( iSelectedIndex );
	}

	/**
	 * Removes an entry from the list at the specified index
	 * 
	 * @param iIndex
	 */
	public final void remove( int iIndex )
	{
		if ( iIndex < 0 || iIndex >= elPaletteEntries.size( ) )
		{
			return;
		}

		elPaletteEntries.remove( iIndex );
		if ( iIndex < iSelectedIndex )
		{
			iSelectedIndex--;
		}
		else if ( iIndex == iSelectedIndex )
		{
			if ( iSelectedIndex > elPaletteEntries.size( ) - 1 )
			{
				iSelectedIndex--;
			}
		}

		if ( elPaletteEntries.isEmpty( ) )
		{
			iSelectedIndex = -1;
			sb.setEnabled( false );
		}
		scrollToView( iSelectedIndex );
		coPaletteEntries.redraw( );
	}

	/**
	 * Updates the current selected entry with the specified fill
	 * 
	 * @param cdi
	 */
	public final void updateSelectionFill( Fill f )
	{
		if ( iSelectedIndex == -1 )
		{
			return;
		}
		elPaletteEntries.set( iSelectedIndex, f );
		coPaletteEntries.redraw( );
	}

	/**
	 * Appends a new fill to the end of the palette list and selects it
	 * 
	 * @param cdi
	 */
	public final void append( Fill fi )
	{
		elPaletteEntries.add( fi );
		iSelectedIndex = elPaletteEntries.size( ) - 1;
		if ( !sb.isEnabled( ) )
		{
			sb.setEnabled( true );
		}
		scrollToView( iSelectedIndex );
		coPaletteEntries.redraw( );
	}

	/**
	 * Swap two consecutive entries
	 * 
	 * @param iIndex1
	 * @param iIndex2
	 */
	private final void swap( int iIndex1, int iIndex2 )
	{
		final Object o1 = elPaletteEntries.get( iIndex1 );
		final Object o2 = elPaletteEntries.get( iIndex2 );
		int iSmaller = Math.min( iIndex1, iIndex2 );
		elPaletteEntries.remove( iSmaller );
		elPaletteEntries.remove( iSmaller );
		if ( iIndex1 < iIndex2 )
		{
			elPaletteEntries.add( iSmaller, o1 );
			elPaletteEntries.add( iSmaller, o2 );
		}
		else
		{
			elPaletteEntries.add( iSmaller, o2 );
			elPaletteEntries.add( iSmaller, o1 );
		}
		if ( iSelectedIndex == iIndex1 )
		{
			iSelectedIndex = iIndex2;
		}
		else if ( iSelectedIndex == iIndex2 )
		{
			iSelectedIndex = iIndex1;
		}
		scrollToView( iSelectedIndex );
		coPaletteEntries.redraw( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event ev )
	{
		updateSelectionFill( (Fill) ev.data );
	}

	// UNUSED INTERFACE METHODS:

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlMoved( ControlEvent arg0 )
	{
		// NO ACTION HERE
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent sev )
	{
		// NO ACTION HERE
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick( MouseEvent arg0 )
	{
		// NO ACTION HERE
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp( MouseEvent arg0 )
	{
		// NO ACTION HERE
	}
}