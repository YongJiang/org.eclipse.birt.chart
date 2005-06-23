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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

/**
 * FillChooserComposite
 */
public class FillChooserComposite extends Composite implements
		SelectionListener,
		MouseListener,
		DisposeListener,
		KeyListener,
		FocusListener
{

	private transient Composite cmpContentInner = null;

	private transient Composite cmpContentOuter = null;

	private transient Composite cmpDropDown = null;

	private transient Composite cmpButtons = null;

	private transient FillCanvas cnvSelection = null;

	private transient Button btnDown = null;

	private transient Label lblTransparency = null;

	private transient Slider srTransparency = null;

	private transient Button btnCustom = null;

	private transient Button btnGradient = null;

	private transient Button btnImage = null;

	private static Color[] colorArray = null;

	private final String[] saImageTypes = new String[]{
			"*.gif", "*.jpg", "*.png" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	};

	private transient boolean bGradientEnabled = true;

	private transient boolean bImageEnabled = true;

	private transient Fill fCurrent = null;

	private transient boolean bTransparencyChanged = false;

	private transient int iTransparency = 0;

	private transient Vector vListeners = null;

	public static final int FILL_CHANGED_EVENT = 1;

	public static final int MOUSE_CLICKED_EVENT = 2;

	private transient boolean bEnabled = true;

	private transient int iSize = 18;

	private boolean bJustFocusLost = false;

	/**
	 * @param parent
	 * @param style
	 */
	public FillChooserComposite( Composite parent, int style, Fill fCurrent,
			boolean bEnableGradient, boolean bEnableImage )
	{
		super( parent, style );
		this.fCurrent = fCurrent;
		this.bGradientEnabled = bEnableGradient;
		this.bImageEnabled = bEnableImage;
		init( );
		placeComponents( );
	}

	/**
	 *  
	 */
	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
		Display display = Display.getDefault( );
		colorArray = this.createColorMap( display );
		vListeners = new Vector( );
	}

	/**
	 *  
	 */
	private void placeComponents( )
	{
		// THE LAYOUT OF THIS COMPOSITE (FILLS EVERYTHING INSIDE IT)
		FillLayout flMain = new FillLayout( );
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;
		setLayout( flMain );

		// THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
		// ITS CONTENT NORTH)
		cmpContentOuter = new Composite( this, SWT.NONE );
		GridLayout glContentOuter = new GridLayout( );
		glContentOuter.verticalSpacing = 0;
		glContentOuter.horizontalSpacing = 0;
		glContentOuter.marginHeight = 0;
		glContentOuter.marginWidth = 0;
		glContentOuter.numColumns = 1;
		cmpContentOuter.setLayout( glContentOuter );

		// THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
		// THE CANVAS + BUTTON)
		cmpContentInner = new Composite( cmpContentOuter, SWT.BORDER );
		GridLayout glContentInner = new GridLayout( );
		glContentInner.verticalSpacing = 0;
		glContentInner.horizontalSpacing = 0;
		glContentInner.marginHeight = 0;
		glContentInner.marginWidth = 0;
		glContentInner.numColumns = 2;
		cmpContentInner.setLayout( glContentInner );
		GridData gdContentInner = new GridData( GridData.FILL_HORIZONTAL );
		cmpContentInner.setLayoutData( gdContentInner );

		// THE CANVAS
		cnvSelection = new FillCanvas( cmpContentInner, SWT.NONE );
		GridData gdCNVSelection = new GridData( GridData.FILL_BOTH );
		gdCNVSelection.heightHint = iSize;
		cnvSelection.setLayoutData( gdCNVSelection );
		cnvSelection.setFill( fCurrent );
		cnvSelection.addMouseListener( this );

		// THE BUTTON
		btnDown = new Button( cmpContentInner, SWT.ARROW | SWT.DOWN );
		GridData gdBDown = new GridData( GridData.FILL );
		gdBDown.verticalAlignment = GridData.BEGINNING;
		gdBDown.widthHint = iSize - 2;
		gdBDown.heightHint = iSize;
		btnDown.setLayoutData( gdBDown );
		btnDown.addSelectionListener( this );

		addDisposeListener( this );
	}

	private Color[] createColorMap( Display display )
	{
		return new Color[]{
				new Color( display, 0, 0, 0 ),
				new Color( display, 154, 50, 0 ),
				new Color( display, 51, 51, 0 ),
				new Color( display, 0, 50, 0 ),
				new Color( display, 0, 50, 100 ),
				new Color( display, 0, 0, 128 ),
				new Color( display, 51, 51, 153 ),
				new Color( display, 51, 51, 51 ),

				new Color( display, 128, 0, 0 ),
				new Color( display, 254, 102, 0 ),
				new Color( display, 124, 124, 0 ),
				new Color( display, 0, 128, 0 ),
				new Color( display, 0, 128, 128 ),
				new Color( display, 0, 0, 254 ),
				new Color( display, 102, 102, 153 ),
				new Color( display, 128, 128, 128 ),

				new Color( display, 254, 0, 0 ),
				new Color( display, 254, 153, 0 ),
				new Color( display, 154, 204, 0 ),
				new Color( display, 51, 153, 102 ),
				new Color( display, 51, 204, 204 ),
				new Color( display, 51, 102, 254 ),
				new Color( display, 128, 0, 128 ),
				new Color( display, 145, 145, 145 ),

				new Color( display, 254, 0, 254 ),
				new Color( display, 254, 204, 0 ),
				new Color( display, 254, 254, 0 ),
				new Color( display, 0, 254, 0 ),
				new Color( display, 0, 254, 254 ),
				new Color( display, 0, 204, 254 ),
				new Color( display, 154, 50, 102 ),
				new Color( display, 192, 192, 192 ),

				new Color( display, 253, 153, 204 ),
				new Color( display, 254, 204, 153 ),
				new Color( display, 254, 254, 153 ),
				new Color( display, 204, 254, 204 ),
				new Color( display, 204, 254, 254 ),
				new Color( display, 153, 204, 254 ),
				new Color( display, 204, 153, 254 ),
				new Color( display, 254, 254, 254 )
		};
	}

	/**
	 *  
	 */
	private void createDropDownComponent( int iXLoc, int iYLoc )
	{
		if ( !bEnabled )
		{
			return;
		}
		int iShellHeight = 256;
		int iShellWidth = 160;
		// Reduce the height based on which buttons are to be shown.
		if ( !bGradientEnabled )
		{
			iShellHeight = iShellHeight - 30;
		}
		if ( !bImageEnabled )
		{
			iShellHeight = iShellHeight - 30;
		}
		Shell shell = new Shell( this.getShell( ), SWT.NONE );
		shell.setLayout( new FillLayout( ) );
		shell.setSize( iShellWidth, iShellHeight );
		shell.setLocation( iXLoc, iYLoc );

		cmpDropDown = new Composite( shell, SWT.NONE );
		GridLayout glDropDown = new GridLayout( );
		glDropDown.marginHeight = 2;
		glDropDown.marginWidth = 2;
		glDropDown.horizontalSpacing = 1;
		glDropDown.verticalSpacing = 4;
		glDropDown.numColumns = 8;
		cmpDropDown.setLayout( glDropDown );
		cmpDropDown.addKeyListener( this );
		cmpDropDown.addFocusListener( this );

		if ( colorArray == null )
		{
			colorArray = createColorMap( getDisplay( ) );
		}
		ColorSelectionCanvas cnv = new ColorSelectionCanvas( cmpDropDown,
				SWT.BORDER,
				colorArray );
		GridData gdCnv = new GridData( GridData.FILL_BOTH );
		gdCnv.horizontalSpan = 8;
		gdCnv.heightHint = 110;
		cnv.setLayoutData( gdCnv );
		cnv.addMouseListener( this );
		if ( this.fCurrent instanceof ColorDefinition )
		{
			cnv.setColor( new Color( this.getDisplay( ),
					( (ColorDefinition) fCurrent ).getRed( ),
					( (ColorDefinition) fCurrent ).getGreen( ),
					( (ColorDefinition) fCurrent ).getBlue( ) ) );
		}

		cmpButtons = new Composite( cmpDropDown, SWT.NONE | SWT.NO_FOCUS );
		GridLayout glButtons = new GridLayout( );
		glButtons.marginHeight = 3;
		glButtons.marginWidth = 4;
		glButtons.horizontalSpacing = 1;
		glButtons.verticalSpacing = 4;
		glButtons.numColumns = 2;
		cmpButtons.setLayout( glButtons );
		GridData gdButtons = new GridData( GridData.FILL_HORIZONTAL );
		gdButtons.horizontalSpan = 8;
		cmpButtons.setLayoutData( gdButtons );
		cmpButtons.addKeyListener( this );

		// Layout for Transparency Composite
		GridLayout glTransparency = new GridLayout( );
		glTransparency.numColumns = 1;
		glTransparency.horizontalSpacing = 5;
		glTransparency.verticalSpacing = 3;
		glTransparency.marginHeight = 4;
		glTransparency.marginWidth = 0;

		Composite cmpTransparency = new Composite( cmpButtons, SWT.NONE
				| SWT.NO_FOCUS );
		GridData gdTransparency = new GridData( GridData.FILL_BOTH );
		gdTransparency.horizontalSpan = 2;
		cmpTransparency.setLayoutData( gdTransparency );
		cmpTransparency.setLayout( glTransparency );

		lblTransparency = new Label( cmpTransparency, SWT.NONE );
		GridData gdLBLTransparency = new GridData( GridData.FILL_HORIZONTAL );
		gdLBLTransparency.horizontalIndent = 2;
		lblTransparency.setLayoutData( gdLBLTransparency );
		lblTransparency.setText( Messages.getString( "FillChooserComposite.Lbl.Opacity" ) ); //$NON-NLS-1$

		srTransparency = new Slider( cmpTransparency, SWT.HORIZONTAL
				| SWT.NO_FOCUS );
		GridData gdTransparent = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL );
		gdTransparent.horizontalSpan = 2;
		srTransparency.setLayoutData( gdTransparent );
		if ( fCurrent == null )
		{
			srTransparency.setValues( 0, 0, 256, 1, 1, 10 );
			srTransparency.setEnabled( false );
		}
		else
		{
			int iValue = 0;
			if ( fCurrent instanceof ColorDefinition )
			{
				iValue = ( (ColorDefinition) fCurrent ).getTransparency( );
				srTransparency.setValues( iValue, 0, 256, 1, 1, 10 );
			}
			else if ( fCurrent instanceof Gradient )
			{
				iValue = ( (Gradient) fCurrent ).getTransparency( );
				srTransparency.setValues( iValue, 0, 256, 1, 1, 10 );
			}
			else
			{
				srTransparency.setEnabled( false );
			}
		}
		lblTransparency.setText( new MessageFormat( Messages.getString( "FillChooserComposite.Lbl.Opacity" ) ) //$NON-NLS-1$
				.format( new Object[]{
					new Integer( srTransparency.getSelection( ) )
				} ) );
		srTransparency.setToolTipText( String.valueOf( srTransparency.getSelection( ) ) );
		srTransparency.addSelectionListener( this );
		srTransparency.addFocusListener( this );

		if ( this.bGradientEnabled )
		{
			btnGradient = new Button( cmpButtons, SWT.NONE );
			GridData gdGradient = new GridData( GridData.FILL_BOTH );
			gdGradient.heightHint = 26;
			gdGradient.horizontalSpan = 2;
			btnGradient.setLayoutData( gdGradient );
			btnGradient.setText( Messages.getString( "FillChooserComposite.Lbl.Gradient" ) ); //$NON-NLS-1$
			btnGradient.addSelectionListener( this );
		}

		btnCustom = new Button( cmpButtons, SWT.NONE );
		GridData gdCustom = new GridData( GridData.FILL_BOTH );
		gdCustom.heightHint = 26;
		gdCustom.horizontalSpan = 2;
		btnCustom.setLayoutData( gdCustom );
		btnCustom.setText( Messages.getString( "FillChooserComposite.Lbl.CustomColor" ) ); //$NON-NLS-1$
		btnCustom.addSelectionListener( this );

		if ( this.bImageEnabled )
		{
			btnImage = new Button( cmpButtons, SWT.NONE );
			GridData gdImage = new GridData( GridData.FILL_BOTH );
			gdImage.heightHint = 26;
			gdImage.horizontalSpan = 2;
			btnImage.setLayoutData( gdImage );
			btnImage.setText( Messages.getString( "FillChooserComposite.Lbl.Image" ) ); //$NON-NLS-1$
			btnImage.addSelectionListener( this );
		}
		shell.layout( );
		shell.open( );
	}

	public void setFill( Fill fill )
	{
		fCurrent = fill;
		cnvSelection.setFill( fill );
		cnvSelection.redraw( );
	}

	public Fill getFill( )
	{
		if ( fCurrent == null
				|| fCurrent.equals( ColorDefinitionImpl.TRANSPARENT( ) ) )
		{
			return null;
		}
		return this.fCurrent;
	}

	public void setEnabled( boolean bState )
	{
		btnDown.setEnabled( bState );
		cnvSelection.setEnabled( bState );
		cnvSelection.redraw( );
		this.bEnabled = bState;
	}

	public boolean isEnabled( )
	{
		return this.bEnabled;
	}

	public Point getPreferredSize( )
	{
		return new Point( 160, 24 );
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	private void toggleDropDown( )
	{
		// fix for linux, since it not send the event correctly to other than
		// current shell.
		if ( bJustFocusLost )
		{
			bJustFocusLost = false;
			return;
		}

		if ( cmpDropDown == null
				|| cmpDropDown.isDisposed( )
				|| !cmpDropDown.isVisible( ) )
		{
			Point pLoc = UIHelper.getScreenLocation( cnvSelection );
			createDropDownComponent( pLoc.x, pLoc.y
					+ cnvSelection.getSize( ).y
					+ 1 );
		}
		else
		{
			cmpDropDown.getShell( ).dispose( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( btnDown ) )
		{
			fireHandleEvent( MOUSE_CLICKED_EVENT );
			toggleDropDown( );
		}
		else if ( oSource.equals( this.btnImage ) )
		{
			FileDialog fDlg = new FileDialog( this.getShell( ), SWT.OPEN );
			cmpDropDown.getParent( ).dispose( );
			fDlg.setFilterExtensions( saImageTypes );
			String sStartFolder = System.getProperty( "user.dir" ); //$NON-NLS-1$
			String sImageFile = ""; //$NON-NLS-1$
			if ( fCurrent instanceof Image )
			{
				String sFullPath = ( (Image) fCurrent ).getURL( ).toString( );
				sImageFile = sFullPath.substring( sFullPath.lastIndexOf( "/" ) + 1 ); //$NON-NLS-1$
				sStartFolder = sFullPath.substring( 0,
						sFullPath.lastIndexOf( "/" ) ); //$NON-NLS-1$
			}
			fDlg.setFilterPath( sStartFolder );
			fDlg.setFileName( sImageFile );
			String sImgPath = fDlg.open( );

			// Do nothing if dialog was cancelled
			if ( sImgPath == null )
			{
				return;
			}

			try
			{
				new URL( sImgPath );
			}
			catch ( MalformedURLException e1 )
			{
				sImgPath = "file:///" + fDlg.getFilterPath( ) + File.separator + fDlg.getFileName( ); //$NON-NLS-1$
			}
			if ( sImgPath != null && sImgPath.trim( ).length( ) > 0 )
			{
				Image imgFill = AttributeFactory.eINSTANCE.createImage( );
				imgFill.setURL( sImgPath );
				if ( fCurrent != null )
				{
					imgFill.eAdapters( ).addAll( fCurrent.eAdapters( ) );
				}
				this.setFill( imgFill );
				fireHandleEvent( FillChooserComposite.FILL_CHANGED_EVENT );
			}
		}
		else if ( oSource.equals( this.btnCustom ) )
		{
			ColorDialog cDlg = new ColorDialog( this.getShell( ), SWT.NONE );
			cmpDropDown.getParent( ).dispose( );
			int iTrans = 255;
			if ( fCurrent instanceof ColorDefinition )
			{
				if ( !fCurrent.equals( ColorDefinitionImpl.TRANSPARENT( ) ) )
				{
					iTransparency = ( (ColorDefinition) fCurrent ).getTransparency( );
				}
				cDlg.setRGB( new RGB( ( (ColorDefinition) this.fCurrent ).getRed( ),
						( (ColorDefinition) this.fCurrent ).getGreen( ),
						( (ColorDefinition) this.fCurrent ).getBlue( ) ) );
			}
			RGB rgb = cDlg.open( );
			if ( rgb != null )
			{
				ColorDefinition cdNew = AttributeFactory.eINSTANCE.createColorDefinition( );
				cdNew.set( rgb.red, rgb.green, rgb.blue );
				cdNew.setTransparency( ( bTransparencyChanged ) ? this.iTransparency
						: iTrans );
				if ( fCurrent != null )
				{
					cdNew.eAdapters( ).addAll( fCurrent.eAdapters( ) );
				}
				this.setFill( cdNew );
				fireHandleEvent( FillChooserComposite.FILL_CHANGED_EVENT );
			}
		}
		else if ( oSource.equals( this.btnGradient ) )
		{
			GradientEditorDialog ged = null;
			cmpDropDown.getParent( ).dispose( );
			if ( fCurrent instanceof Gradient )
			{
				ged = new GradientEditorDialog( this.getShell( ),
						(Gradient) fCurrent );
			}
			else
			{
				ged = new GradientEditorDialog( this.getShell( ), null );
			}
			if ( ged.getGradient( ) != null )
			{
				Fill fTmp = ged.getGradient( );
				if ( fCurrent != null )
				{
					fTmp.eAdapters( ).addAll( fCurrent.eAdapters( ) );
				}
				if ( fCurrent == null || !( fCurrent.equals( fTmp ) ) )
				{
					this.setFill( fTmp );
					fireHandleEvent( FillChooserComposite.FILL_CHANGED_EVENT );
				}
			}
		}
		else if ( oSource.equals( srTransparency ) )
		{
			iTransparency = srTransparency.getSelection( );
			lblTransparency.setText( new MessageFormat( Messages.getString( "FillChooserComposite.Lbl.Opacity" ) ) //$NON-NLS-1$
					.format( new Object[]{
						new Integer( iTransparency )
					} ) );
			srTransparency.setToolTipText( String.valueOf( srTransparency.getSelection( ) ) );

			if ( fCurrent instanceof ColorDefinition )
			{
				( (ColorDefinition) fCurrent ).setTransparency( iTransparency );
			}
			setFill( fCurrent );

			bTransparencyChanged = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	private void fireHandleEvent( int iType )
	{
		for ( int iL = 0; iL < vListeners.size( ); iL++ )
		{
			Event se = new Event( );
			se.widget = this;
			se.data = ( fCurrent != null ) ? fCurrent
					: ColorDefinitionImpl.TRANSPARENT( );
			se.type = iType;
			( (Listener) vListeners.get( iL ) ).handleEvent( se );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown( MouseEvent e )
	{
		if ( !bEnabled )
		{
			return;
		}
		fireHandleEvent( MOUSE_CLICKED_EVENT );
		if ( e.getSource( ).equals( cnvSelection ) )
		{
			toggleDropDown( );
		}
		else if ( e.getSource( ) instanceof ColorSelectionCanvas )
		{
			ColorDefinition cTmp = AttributeFactory.eINSTANCE.createColorDefinition( );
			Color clrTmp = ( (ColorSelectionCanvas) e.getSource( ) ).getColorAt( e.x,
					e.y );
			cTmp.set( clrTmp.getRed( ), clrTmp.getGreen( ), clrTmp.getBlue( ) );
			int iTransparency = 255;
			if ( fCurrent instanceof ColorDefinition && this.iTransparency != 0 )
			{
				iTransparency = ( bTransparencyChanged ) ? this.iTransparency
						: ( (ColorDefinition) fCurrent ).getTransparency( );
			}
			cTmp.setTransparency( iTransparency );
			setFill( cTmp );
			fireHandleEvent( FillChooserComposite.FILL_CHANGED_EVENT );
			cmpDropDown.getShell( ).dispose( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e )
	{
		if ( colorArray != null )
		{
			for ( int iC = 0; iC < colorArray.length; iC++ )
			{
				colorArray[iC].dispose( );
			}
			colorArray = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed( KeyEvent e )
	{
		if ( cmpDropDown != null && !cmpDropDown.getShell( ).isDisposed( ) )
		{
			if ( e.keyCode == SWT.ESC )
			{
				cmpDropDown.getShell( ).dispose( );
				return;
			}
			else if ( e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR )
			{
				this.iTransparency = srTransparency.getSelection( );
				if ( fCurrent instanceof ColorDefinition
						&& bTransparencyChanged )
				{
					( (ColorDefinition) fCurrent ).setTransparency( this.iTransparency );
				}
				this.setFill( fCurrent );
				cmpDropDown.getShell( ).dispose( );
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased( KeyEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusGained( FocusEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	public void focusLost( FocusEvent e )
	{
		if ( e.getSource( ).equals( cmpDropDown )
				|| e.getSource( ).equals( srTransparency ) )
		{
			// Condition added to handle behavior under Linux
			Control cTmp = Display.getCurrent( ).getCursorControl( );
			if ( cTmp.equals( btnGradient )
					|| cTmp.equals( btnCustom )
					|| cTmp.equals( btnImage )
					|| ( e.getSource( ).equals( cmpDropDown ) && cTmp.equals( srTransparency ) ) )
			{
				return;
			}

			if ( cTmp.equals( cnvSelection ) || cTmp.equals( btnDown ) )
			{
				bJustFocusLost = true;
			}

			cmpDropDown.getShell( ).dispose( );
			return;
		}
	}
}

class ColorSelectionCanvas extends Canvas implements PaintListener
{

	Color[] colorMap = null;

	Color colorSelection = null;

	public ColorSelectionCanvas( Composite parent, int iStyle, Color[] colorMap )
	{
		super( parent, iStyle );
		this.addPaintListener( this );
		this.colorMap = colorMap;
	}

	public Color getColor( )
	{
		return colorSelection;
	}

	public void setColor( Color color )
	{
		this.colorSelection = color;
	}

	public void paintControl( PaintEvent pe )
	{
		Color cBlack = new Color( this.getDisplay( ), 0, 0, 0 );
		Color cWhite = new Color( this.getDisplay( ), 255, 255, 255 );
		GC gc = pe.gc;
		gc.setForeground( cBlack );

		int iCellWidth = this.getSize( ).x / 8;
		int iCellHeight = this.getSize( ).y / 5;

		for ( int iR = 0; iR < 5; iR++ )
		{
			for ( int iC = 0; iC < 8; iC++ )
			{
				try
				{
					gc.setBackground( colorMap[( iR * 8 ) + iC] );
				}
				catch ( Throwable e )
				{
					e.printStackTrace( );
				}
				gc.fillRectangle( iC * iCellWidth,
						iR * iCellHeight,
						iCellWidth,
						iCellHeight );
				// Highlight currently selected color if it exists in this list
				if ( colorSelection != null
						&& colorSelection.equals( colorMap[( iR * 8 ) + iC] ) )
				{
					gc.drawRectangle( iC * iCellWidth,
							iR * iCellHeight,
							iCellWidth - 2,
							iCellHeight - 2 );
					gc.setForeground( cWhite );
					gc.drawRectangle( iC * iCellWidth + 1,
							iR * iCellHeight + 1,
							iCellWidth - 3,
							iCellHeight - 3 );
					gc.setForeground( cBlack );
				}
			}
		}
		cBlack.dispose( );
		cWhite.dispose( );
		gc.dispose( );
	}

	/**
	 * This method assumes a color array of 40 color arranged with equal sizes
	 * in a 8x5 grid.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Color getColorAt( int x, int y )
	{
		int iCellWidth = this.getSize( ).x / 8;
		int iCellHeight = this.getSize( ).y / 5;
		int iHCell = x / iCellWidth;
		int iVCell = y / iCellHeight;
		int iArrayIndex = ( ( iVCell ) * 8 ) + iHCell;
		return this.colorMap[iArrayIndex];
	}
}