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

import java.text.ParseException;
import java.util.Date;
import java.util.Vector;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * This class is intended to be used in the ChartBuilder UI where direct changes
 * to the model are being made via Text Fields. It internally holds a regular
 * text field but only fires events when focus is lost IF the text has been
 * modified. It is intended to reduce the number changes made to the model to
 * make the UI more responsive and to reduce the number of times the Preview is
 * refreshed.
 * 
 * @author Actuate Corporation
 */
public class TextEditorComposite extends Composite implements
		ModifyListener,
		FocusListener,
		KeyListener
{
	
	public static final int TYPE_NONE = 0;
	public static final int TYPE_NUMBERIC = 1;
	public static final int TYPE_DATETIME = 2;

	private transient String sText = null;

	private transient boolean bTextModified = false;

	private transient int iStyle = SWT.NONE;

	private transient Text txtValue = null;

	private transient Vector vListeners = null;

	public static final int TEXT_MODIFIED = 0;

	public static final int TEXT_FRACTION_CONVERTED = 1;

	private transient boolean bEnabled = true;

	private transient int valueType = TYPE_NONE;

	private transient String defaultValue = "0"; //$NON-NLS-1$

	/**
	 * Constructor. Default argument value of isFractionSupported is true.
	 * 
	 * @param parent
	 * @param iStyle
	 */
	public TextEditorComposite( Composite parent, int iStyle )
	{
		this( parent, iStyle, TYPE_NONE );
	}

	/**
	 * 
	 * @param parent
	 * @param iStyle
	 * @param isNumber
	 *            If this argument is true, only number value is valid. The
	 *            fraction value, like "1/3" also is supported as a double
	 *            value.
	 */
	public TextEditorComposite( Composite parent, int iStyle, boolean isNumber )
	{
		this( parent, iStyle, isNumber ? TYPE_NUMBERIC : TYPE_NONE );
	}
	
	/**
	 * 
	 * @param parent
	 * @param iStyle
	 * @param valueType
	 *            Value type for validation, valid type is {@link #TYPE_DATETIME},
	 *            {@link #TYPE_NUMBERIC} or {@link #TYPE_NONE}
	 */
	public TextEditorComposite( Composite parent, int iStyle, int valueType )
	{
		super( parent, SWT.NONE );
		this.iStyle = iStyle;
		this.valueType = valueType;
		init( );
		placeComponents( );
		initAccessible( );
	}

	private void init( )
	{
		sText = ""; //$NON-NLS-1$
		vListeners = new Vector( );
		this.setLayout( new FillLayout( ) );
	}

	private void placeComponents( )
	{
		txtValue = new Text( this, iStyle );
		if ( valueType == TYPE_NUMBERIC )
		{
			txtValue.setToolTipText( Messages.getString( "TextEditorComposite.Tooltip.EnterDecimalOrFractionValue" ) ); //$NON-NLS-1$
		}
		else if ( valueType == TYPE_DATETIME )
		{
			txtValue.setToolTipText( "MM-dd-yyyy HH:mm:ss" ); //$NON-NLS-1$
		}
		txtValue.addModifyListener( this );
		txtValue.addFocusListener( this );
		txtValue.addKeyListener( this );
	}

	public void setEnabled( boolean bState )
	{
		this.txtValue.setEnabled( bState );
		this.bEnabled = bState;
	}

	public boolean isEnabled( )
	{
		return this.bEnabled;
	}

	public void setText( String sText )
	{
		txtValue.setText( sText );
	}

	public String getText( )
	{
		return txtValue.getText( );
	}

	public void setToolTipText( String string )
	{
		txtValue.setToolTipText( string );
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	/**
	 * Sets the default value when current text is for numeric only. If the
	 * inputed text is not numeric, will use this default value.
	 * 
	 * @param value
	 *            default value
	 */
	public void setDefaultValue( String value )
	{
		this.defaultValue = value;
	}

	private void fireEvent( )
	{		
		boolean isFractionConverted = false;
		if ( valueType == TYPE_NUMBERIC )
		{
			int iDelimiter = sText.indexOf( '/' );
			if ( iDelimiter < 0 )
			{
				iDelimiter = sText.indexOf( ':' );
			}
			if ( iDelimiter > 0 )
			{
				// Handle the fraction conversion
				isFractionConverted = true;
				String numerator = sText.substring( 0, iDelimiter );
				String denominator = sText.substring( iDelimiter + 1 );
				try
				{
					this.sText = String.valueOf( Double.parseDouble( numerator )
							/ Double.parseDouble( denominator ) );
				}
				catch ( NumberFormatException e )
				{
					this.sText = defaultValue == null ? "" : defaultValue; //$NON-NLS-1$
				}
				this.txtValue.setText( sText );
			}
			else
			{
				// Test if the text is a number format
				try
				{
					NumberFormat.getInstance( )
							.parse( this.sText )
							.doubleValue( );
				}
				catch ( ParseException e )
				{
					this.sText = defaultValue == null ? "" : defaultValue; //$NON-NLS-1$
					this.txtValue.setText( this.sText );
				}
			}
		}
		else if ( valueType == TYPE_DATETIME )
		{
			SimpleDateFormat sdf = new SimpleDateFormat( "MM-dd-yyyy HH:mm:ss" ); //$NON-NLS-1$
			try
			{
				sdf.parse( this.sText );
			}
			catch ( ParseException e )
			{
				if ( defaultValue == null  )
				{
					Date today = new Date( );
					this.sText = sdf.format( today );
				}
				else
				{
					this.sText = defaultValue;
				}
				this.txtValue.setText( this.sText );
			}
		}

		for ( int i = 0; i < vListeners.size( ); i++ )
		{
			Event e = new Event( );
			e.data = this.sText;
			e.widget = this;
			e.type = TEXT_MODIFIED;
			( (Listener) vListeners.get( i ) ).handleEvent( e );

			if ( isFractionConverted )
			{
				e = new Event( );
				e.data = this.sText;
				e.widget = this;
				e.type = TEXT_FRACTION_CONVERTED;
				( (Listener) vListeners.get( i ) ).handleEvent( e );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		this.bTextModified = true;
		this.sText = txtValue.getText( );
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
		if ( bTextModified )
		{
			fireEvent( );
		}
	}

	public void keyPressed( KeyEvent e )
	{
		if ( e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR )
		{
			if ( bTextModified )
			{
				fireEvent( );
			}
		}
	}

	public void keyReleased( KeyEvent e )
	{
		// TODO Auto-generated method stub

	}

	void initAccessible( )
	{
		getAccessible( ).addAccessibleListener( new AccessibleAdapter( ) {

			public void getHelp( AccessibleEvent e )
			{
				e.result = getToolTipText( );
			}
		} );

		getAccessible( ).addAccessibleTextListener( new AccessibleTextAdapter( ) {

			public void getCaretOffset( AccessibleTextEvent e )
			{
				e.offset = txtValue.getCaretPosition( );
			}
		} );

		getAccessible( ).addAccessibleControlListener( new AccessibleControlAdapter( ) {

			public void getChildAtPoint( AccessibleControlEvent e )
			{
				Point testPoint = toControl( new Point( e.x, e.y ) );
				if ( getBounds( ).contains( testPoint ) )
				{
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation( AccessibleControlEvent e )
			{
				Rectangle location = getBounds( );
				Point pt = toDisplay( new Point( location.x, location.y ) );
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount( AccessibleControlEvent e )
			{
				e.detail = 0;
			}

			public void getRole( AccessibleControlEvent e )
			{
				e.detail = ACC.ROLE_TEXT;
			}

			public void getState( AccessibleControlEvent e )
			{
				e.detail = ACC.STATE_NORMAL;
			}

			public void getValue( AccessibleControlEvent e )
			{
				e.result = getText( );
			}
		} );
	}
}