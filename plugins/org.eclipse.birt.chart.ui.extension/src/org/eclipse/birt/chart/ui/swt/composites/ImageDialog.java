/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.util.Base64;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
 * Dialog to build the image element.
 */

public class ImageDialog extends Dialog
{

	final private static int URI_TYPE = 0;

	final private static int EMBEDDED_TYPE = 1;

	private Button embedded, uri, previewButton;

	private Composite inputArea;

	private IconCanvas previewCanvas;

	private Text uriEditor;

	private int selectedType = -1;

	private Fill fCurrent;

	private String imageData;

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 */
	public ImageDialog( Shell parentShell, Fill fCurrent )
	{
		super( parentShell );

		this.fCurrent = fCurrent;
	}

	protected Control createContents( Composite parent )
	{
		Control ct = super.createContents( parent );
		initDialog( );
		return ct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite topCompostie = (Composite) super.createDialogArea( parent );
		createSelectionArea( topCompostie );

		new Label( topCompostie, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite composite = new Composite( topCompostie, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );

		createInputArea( composite );
		createPreviewArea( composite );

		new Label( topCompostie, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		return topCompostie;
	}

	private void createSelectionArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayout( new GridLayout( 2, false ) );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Label label = new Label( composite, SWT.NONE );
		label.setText( Messages.getString( "ImageDialog.label.SelectImageType" ) ); //$NON-NLS-1$
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );

		Composite selectionArea = new Composite( composite, SWT.NONE );
		selectionArea.setLayout( new FillLayout( SWT.VERTICAL ) );

		uri = new Button( selectionArea, SWT.RADIO );
		uri.setText( Messages.getString( "ImageDialog.label.URLImage" ) ); //$NON-NLS-1$
		uri.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				selectedType = URI_TYPE;
				updateButtons();
			}

		} );
		embedded = new Button( selectionArea, SWT.RADIO );
		embedded.setText( Messages.getString( "ImageDialog.label.EmbeddedImage" ) ); //$NON-NLS-1$
		embedded.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				selectedType = EMBEDDED_TYPE;
				updateButtons();
			}
		} );
	}

	private void createInputArea( Composite parent )
	{
		inputArea = new Composite( parent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_BOTH
				| GridData.HORIZONTAL_ALIGN_BEGINNING );
		gd.widthHint = 300;
		gd.heightHint = 300;
		inputArea.setLayoutData( gd );
		inputArea.setLayout( new GridLayout( ) );

		Label title = new Label( inputArea, SWT.NONE );
		title.setText( Messages.getString( "ImageDialog.label.EnterURL" ) ); //$NON-NLS-1$

		uriEditor = new Text( inputArea, SWT.SINGLE | SWT.BORDER );
		uriEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		uriEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Composite innerComp = new Composite( inputArea, SWT.NONE );
		innerComp.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		innerComp.setLayout( new GridLayout( 2, false ) );

		Button inputButton = new Button( innerComp, SWT.PUSH );
		inputButton.setText( Messages.getString( "ImageDialog.label.Browse" ) ); //$NON-NLS-1$
		inputButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		inputButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				FileDialog fileChooser = new FileDialog( getShell( ), SWT.OPEN );
				fileChooser.setText( Messages.getString( "ImageDialog.label.SelectFile" ) ); //$NON-NLS-1$
				fileChooser.setFilterExtensions( new String[]{
						"*.gif", "*.jpg", "*.png" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				} );
				try
				{
					String fullPath = fileChooser.open( );
					if ( fullPath != null )
					{
						String fileName = fileChooser.getFileName( );
						if ( fileName != null )
						{
							imageData = null;
							fullPath = new StringBuffer( "file:///" ).append( fullPath ).toString( ); //$NON-NLS-1$
							preview( fullPath );
							uriEditor.setText( fullPath );
						}
					}
				}
				catch ( Throwable e )
				{
					e.printStackTrace( );
				}
			}
		} );

		previewButton = new Button( innerComp, SWT.PUSH );
		previewButton.setText( Messages.getString( "ImageDialog.label.Preview" ) ); //$NON-NLS-1$
		previewButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		previewButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				preview( uriEditor.getText( ) );
			}
		} );

	}

	private void createPreviewArea( Composite composite )
	{
		Composite previewArea = new Composite( composite, SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 250;
		gd.heightHint = 300;
		previewArea.setLayoutData( gd );
		previewArea.setLayout( new FillLayout( ) );

		previewCanvas = new IconCanvas( previewArea );
	}

	private void preview( String uri )
	{
		try
		{
			if ( imageData != null )
			{
				ByteArrayInputStream bis = new ByteArrayInputStream( Base64.decode( imageData ) );
				previewCanvas.loadImage( bis );
			}
			else
			{
				previewCanvas.loadImage( new URL( uri ) );
			}
		}
		catch ( Exception e )
		{
			WizardBase.displayException( e );
		}
	}

	private void clearPreview( )
	{
		previewCanvas.clear( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */

	protected void okPressed( )
	{
		switch ( selectedType )
		{
			case URI_TYPE :
				fCurrent = ImageImpl.create( uriEditor.getText( ).trim( ) );
				break;
			case EMBEDDED_TYPE :
				fCurrent = EmbeddedImageImpl.create( uriEditor.getText( )
						.trim( ), imageData );
				try
				{

					BufferedInputStream bis = new BufferedInputStream( new URL( uriEditor.getText( )
							.trim( ) ).openStream( ) );
					ByteArrayOutputStream bos = new ByteArrayOutputStream( );

					byte[] buf = new byte[1024];
					int count = bis.read( buf );
					while ( count != -1 )
					{
						bos.write( buf, 0, count );

						count = bis.read( buf );
					}

					String data = Base64.encodeBytes( bos.toByteArray( ) );

					( (EmbeddedImage) fCurrent ).setData( data );
				}
				catch ( Exception e )
				{
					WizardBase.displayException( e );
				}
				break;
		}
		super.okPressed( );
	}

	protected boolean initDialog( )
	{
		getShell( ).setText( Messages.getString( "ImageDialog.label.SelectImage" ) ); //$NON-NLS-1$

		initURIEditor( );

		if ( fCurrent instanceof EmbeddedImage )
		{
			embedded.setSelection( true );
			selectedType = EMBEDDED_TYPE;
		}
		else
		{// initialize as URI mode by default
			uri.setSelection( true );
			selectedType = URI_TYPE;
		}

		getButton( IDialogConstants.OK_ID ).setEnabled( false );
		
		return true;
	}

	private void initURIEditor( )
	{
		String uri = ""; //$NON-NLS-1$
		if ( fCurrent instanceof Image )
		{
			uri = ( (Image) fCurrent ).getURL( );

			if ( fCurrent instanceof EmbeddedImage )
			{
				imageData = ( (EmbeddedImage) fCurrent ).getData( );
			}
		}

		uriEditor.setText( uri );
		uriEditor.setFocus( );
		// Listener will be called automatically
		clearPreview( );
	}

	private void updateButtons( )
	{
		boolean complete = uriEditor.getText( ) != null
				&& uriEditor.getText( ).trim( ).length( ) > 0;
		previewButton.setEnabled( complete );
		getButton( IDialogConstants.OK_ID ).setEnabled( complete );
	}

	/**
	 * @return
	 */
	public Fill getResult( )
	{
		return fCurrent;
	}

}