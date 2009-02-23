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

import java.net.URL;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * MarkerIconDialog is invoked when the user chooses "icon" from Marker Type
 * Combo box.
 */
public class MarkerIconDialog extends TrayDialog
		implements
			SelectionListener,
			ModifyListener
{

	private transient Button btnURL;

	private transient Button btnLocal;

	private transient Button btnPreview;

	private Composite inputArea;

	private IconCanvas previewCanvas;

	private transient Button btnBrowse;

	final private static int URI_TYPE = 0;

	final private static int LOCAL_TYPE = 1;

	private int selectedType = -1;

	private Text uriEditor;

	private transient Fill icon;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            shell of LineSeriesAttributeComposite
	 * @param iconPalette
	 *            retrieved from LineSeries
	 */
	public MarkerIconDialog( Shell parent, Fill fill )
	{
		super( parent );

		icon = null;

		if ( fill != null )
		{
			icon = (Fill) EcoreUtil.copy( fill );
		}
	}

	protected Control createContents( Composite parent )
	{
		getShell( ).setText( Messages.getString( "MarkerIconDialog.Title.MarkerIconSelector" ) ); //$NON-NLS-1$
		getShell( ).setSize( 600, 420 );
		UIHelper.centerOnScreen( getShell( ) );
		return super.createContents( parent );
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( new GridLayout( ) );

		createSelectionArea( cmpContent );

		new Label( cmpContent, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite composite = new Composite( cmpContent, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );

		createListArea( composite );
		createPreviewArea( composite );

		new Label( cmpContent, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		return cmpContent;
	}

	protected Control createButtonBar( Composite parent )
	{
		Control cmp = super.createButtonBar( parent );
		updateButton( );
		return cmp;
	}

	/**
	 * Selection Area locates in the top of the dialog.
	 * 
	 * @param parent
	 *            dialog composite
	 */
	private void createSelectionArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );

		Label label = new Label( composite, SWT.NONE );
		label.setText( Messages.getString( "MarkerIconDialog.Lbl.SelectIconFrom" ) ); //$NON-NLS-1$
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );

		Composite selectionArea = new Composite( composite, SWT.NONE );
		selectionArea.setLayout( new FillLayout( SWT.VERTICAL ) );

		btnURL = new Button( selectionArea, SWT.RADIO );
		btnURL.setText( Messages.getString( "MarkerIconDialog.Lbl.URL" ) ); //$NON-NLS-1$
		btnURL.addSelectionListener( this );

		btnLocal = new Button( selectionArea, SWT.RADIO );
		btnLocal.setText( Messages.getString( "MarkerIconDialog.Lbl.Local" ) ); //$NON-NLS-1$
		btnLocal.addSelectionListener( this );
	}

	/**
	 * List Area locates in the left middle of the dialog.
	 * 
	 * @param parent
	 *            dialog composite
	 */
	private void createListArea( Composite parent )
	{
		Composite listArea = new Composite( parent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_BOTH
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		gd.widthHint = 300;
		gd.heightHint = 260;
		listArea.setLayoutData( gd );
		listArea.setLayout( new GridLayout( ) );

		GridLayout gl = new GridLayout( );
		gl.marginWidth = 0;
		gl.marginHeight = 0;

		// Input Area is various depending on the selection (URI or Local).
		inputArea = new Composite( listArea, SWT.NONE );
		GridData gdInputArea = new GridData( GridData.FILL_BOTH
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		inputArea.setLayoutData( gdInputArea );
		inputArea.setLayout( gl );
	}

	/**
	 * Preview Area locates in the right middle of the dialog.
	 * 
	 * @param composite
	 *            dialog composite
	 */
	private void createPreviewArea( Composite composite )
	{
		Composite previewArea = new Composite( composite, SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 250;
		gd.heightHint = 260;
		previewArea.setLayoutData( gd );
		previewArea.setLayout( new FillLayout( ) );

		previewCanvas = new IconCanvas( previewArea );
	}

	/**
	 * Switch in the Selection Area (URI or Local).
	 * 
	 * @param type
	 *            0: URI_TYPE; 1: LOCAL_TYPE
	 */
	private void switchTo( int type )
	{
		if ( type == selectedType )
		{
			// If the selected type is same with the current type,
			// Do nothing.
			return;
		}

		// Clear the current Input Area contents.
		selectedType = type;
		Control[] controls = inputArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}

		// Rearrange the layout and contents of Input Area.
		switch ( type )
		{
			case URI_TYPE :
				swtichToURIType( );
				break;
			case LOCAL_TYPE :
				swtichToLocalType( );
				break;
		}
		inputArea.layout( );
		updateButton( );
	}

	private void swtichToURIType( )
	{
		Label title = new Label( inputArea, SWT.NONE );
		title.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		title.setText( Messages.getString( "MarkerIconDialog.Lbl.EnterURL" ) ); //$NON-NLS-1$

		uriEditor = new Text( inputArea, SWT.SINGLE | SWT.BORDER );
		uriEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		uriEditor.addModifyListener( this );

		Composite innerComp = new Composite( inputArea, SWT.NONE );
		innerComp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		GridLayout gl = new GridLayout( 2, false );
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.verticalSpacing = 2;
		innerComp.setLayout( gl );

		btnPreview = new Button( innerComp, SWT.PUSH );
		btnPreview.setText( Messages.getString( "MarkerIconDialog.Lbl.Preview" ) ); //$NON-NLS-1$
		btnPreview.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		btnPreview.setEnabled( false );
		btnPreview.addSelectionListener( this );

		if ( icon != null )
		{
			String url = ( (Image) icon ).getURL( );
			uriEditor.setText( url == null ? "" : url ); //$NON-NLS-1$
		}
	}

	private void swtichToLocalType( )
	{
		Composite buttonBar = new Composite( inputArea, SWT.NONE );

		GridLayout gl = new GridLayout( );
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		buttonBar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		buttonBar.setLayout( gl );

		Label description = new Label( buttonBar, SWT.NONE );
		description.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );
		description.setText( Messages.getString( "MarkerIconDialog.Lbl.Description" ) ); //$NON-NLS-1$

		btnBrowse = new Button( buttonBar, SWT.PUSH );
		btnBrowse.setText( Messages.getString( "MarkerIconDialog.Lbl.Browse" ) ); //$NON-NLS-1$
		GridData gd = new GridData( GridData.HORIZONTAL_ALIGN_END );
		gd.grabExcessHorizontalSpace = true;
		btnBrowse.setLayoutData( gd );
		btnBrowse.addSelectionListener( this );
	}

	private void updateButton( )
	{
		getButtonOk( ).setEnabled( selectedType == LOCAL_TYPE
				&& icon != null
				&& ( (Image) icon ).getURL( ) != null
				|| ( selectedType == URI_TYPE && trimString( uriEditor.getText( ) ) != null ) );
	}

	private Button getButtonOk( )
	{
		return getButton( IDialogConstants.OK_ID );
	}

	/**
	 * Preview the image when it is a local image file.
	 * 
	 * @param uri
	 *            Image absolute path without "file:///"
	 */
	private void preview( String uri )
	{
		try
		{
			URL url = new URL( uri );
			getButtonOk( ).setEnabled( url.getContent( ) != null );
			previewCanvas.loadImage( url );
		}
		catch ( Exception e )
		{
			getButtonOk( ).setEnabled( false );
			WizardBase.displayException( e );
		}
	}

	/**
	 * If there is no palette associated with the marker, create a new palette.
	 * Otherwise, add the icon into the palette.
	 * 
	 */
	private void checkIcon( )
	{
		if ( selectedType == URI_TYPE )
		{
			if ( icon == null
					|| ( (Image) icon ).getURL( ) != null
					&& !( (Image) icon ).getURL( )
							.equals( trimString( uriEditor.getText( ) ) ) )
			{
				icon = ImageImpl.create( trimString( uriEditor.getText( ) ) );
			}
		}
	}

	protected void okPressed( )
	{
		checkIcon( );
		super.okPressed( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( btnURL ) )
		{
			switchTo( URI_TYPE );
		}
		else if ( e.widget.equals( btnLocal ) )
		{
			switchTo( LOCAL_TYPE );
		}
		else if ( e.widget.equals( btnPreview ) )
		{
			uriEditor.setText( uriEditor.getText( ).trim( ) );
			String path = uriEditor.getText( );
			preview( path );
		}
		else if ( e.widget.equals( btnBrowse ) )
		{
			FileDialog fileChooser = new FileDialog( getShell( ), SWT.OPEN );
			fileChooser.setText( Messages.getString( "MarkerIconDialog.Chooser.Title" ) ); //$NON-NLS-1$
			fileChooser.setFilterExtensions( new String[]{
					"*.gif", "*.jpg", "*.png" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} );

			try
			{
				String path = fileChooser.open( );
				if ( path != null )
				{
					path = new StringBuffer( "file:///" ).append( path ).toString( ); //$NON-NLS-1$
					preview( path );

					icon = ImageImpl.create( path );
				}
			}
			catch ( Throwable ex )
			{
				ex.printStackTrace( );
			}
			updateButton( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent event )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		if ( e.widget.equals( uriEditor ) )
		{
			btnPreview.setEnabled( trimString( uriEditor.getText( ) ) != null );
			getButtonOk( ).setEnabled( false );
		}
	}

	/**
	 * Trim a string. Removes leading and trailing blanks. If the resulting
	 * string is empty, normalizes the string to an null string.
	 * 
	 * @param value
	 *            the string to trim
	 * @return the trimmed string, or null if the string is empty
	 */

	private static String trimString( String value )
	{
		if ( value == null )
		{
			return null;
		}
		value = value.trim( );
		if ( value.length( ) == 0 )
		{
			return null;
		}
		return value;
	}

	/**
	 * 
	 * @return Returns an icon palette to Line series.
	 */
	public Fill getFill( )
	{
		return icon;
	}
}