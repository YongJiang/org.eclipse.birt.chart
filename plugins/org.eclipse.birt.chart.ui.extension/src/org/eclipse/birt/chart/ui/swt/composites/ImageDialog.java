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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.PatternImage;
import org.eclipse.birt.chart.model.attribute.impl.EmbeddedImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
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

public class ImageDialog extends TrayDialog
{

	private final static String DATA_BASE64 = "data:;base64,"; //$NON-NLS-1$

	final private static int URI_TYPE = 0;

	final private static int EMBEDDED_TYPE = 1;

	private Button embedded, uri, browseButton, previewButton;

	private Composite inputArea;

	private ImageCanvas previewCanvas;

	private Text uriEditor;

	private int selectedType = -1;

	private Fill fCurrent;

	private String imageData;

	private Label title;

	private static final ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.composites" ); //$NON-NLS-1$

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

	@Override
	protected Control createContents( Composite parent )
	{
		Control ct = super.createContents( parent );
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.DIALOG_COLOR_IMAGE );
		initDialog( );
		return ct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
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

			@Override
			public void widgetSelected( SelectionEvent e )
			{
				selectedType = URI_TYPE;
				title.setText( Messages.getString( "ImageDialog.label.EnterURL" ) ); //$NON-NLS-1$
				updateButtons( );
			}

		} );
		embedded = new Button( selectionArea, SWT.RADIO );
		embedded.setText( Messages.getString( "ImageDialog.label.EmbeddedImage" ) ); //$NON-NLS-1$
		embedded.addSelectionListener( new SelectionAdapter( ) {

			@Override
			public void widgetSelected( SelectionEvent e )
			{
				selectedType = EMBEDDED_TYPE;
				title.setText( Messages.getString( "ImageDialog.label.EnterEmbed" ) ); //$NON-NLS-1$
				updateButtons( );
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

		title = new Label( inputArea, SWT.NONE );
		title.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

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

		browseButton = new Button( innerComp, SWT.PUSH );
		browseButton.setText( Messages.getString( "ImageDialog.label.Browse" ) ); //$NON-NLS-1$
		browseButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		browseButton.addSelectionListener( new SelectionAdapter( ) {

			@Override
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
		browseButton.setVisible( embedded.getSelection( ) );

		previewButton = new Button( innerComp, SWT.PUSH );
		previewButton.setText( Messages.getString( "ImageDialog.label.Preview" ) ); //$NON-NLS-1$
		previewButton.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );
		previewButton.addSelectionListener( new SelectionAdapter( ) {

			@Override
			public void widgetSelected( SelectionEvent e )
			{
				preview( removeQuote( uriEditor.getText( ) ) );
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
		previewCanvas = new ImageCanvas( previewArea );
	}

	private void preview( String uri )
	{
		previewCanvas.update( uri );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */

	@Override
	protected void okPressed( )
	{
		switch ( selectedType )
		{
			case URI_TYPE :
				fCurrent = ImageImpl.create( removeQuote( uriEditor.getText( ).trim( ) ) );
				break;
			case EMBEDDED_TYPE :
				BufferedInputStream bis = null;
				try
				{
					fCurrent = EmbeddedImageImpl.create( new File( uriEditor.getText( )
							.trim( ) ).getName( ),
							imageData );
					bis = new BufferedInputStream( new URL( uriEditor.getText( )
							.trim( ) ).openStream( ) );
					ByteArrayOutputStream bos = new ByteArrayOutputStream( );

					byte[] buf = new byte[1024];
					int count = bis.read( buf );
					while ( count != -1 )
					{
						bos.write( buf, 0, count );

						count = bis.read( buf );
					}

					String data = new String( Base64.encodeBase64( bos.toByteArray( ) ) );

					( (EmbeddedImage) fCurrent ).setData( data );
				}
				catch ( Exception e )
				{
					WizardBase.displayException( e );
				}
				finally
				{
					if ( bis != null )
					{
						try
						{
							bis.close( );
						}
						catch ( IOException e )
						{
							WizardBase.displayException( e );
						}
					}
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

		if ( selectedType == EMBEDDED_TYPE )
		{
			title.setText( Messages.getString( "ImageDialog.label.EnterEmbed" ) ); //$NON-NLS-1$
		}
		else
		{
			title.setText( Messages.getString( "ImageDialog.label.EnterURL" ) ); //$NON-NLS-1$
		}

		getButton( IDialogConstants.OK_ID ).setEnabled( false );
		browseButton.setVisible( embedded.getSelection( ) );
		
		initPreview( );

		return true;
	}

	private void initPreview( )
	{
		String sUrl = ( selectedType == EMBEDDED_TYPE ) ? "data:;base64," //$NON-NLS-1$
				+ imageData : removeQuote( uriEditor.getText( )
				.trim( ) );
		previewCanvas.update( sUrl );

	}

	private void initURIEditor( )
	{
		String uri = ""; //$NON-NLS-1$
		if ( fCurrent instanceof Image && !( fCurrent instanceof PatternImage ) )
		{
			uri = ( (Image) fCurrent ).getURL( );

			if ( fCurrent instanceof EmbeddedImage )
			{
				imageData = ( (EmbeddedImage) fCurrent ).getData( );
			}
		}

		uriEditor.setText( uri );
		uriEditor.setFocus( );
	}

	private void updateButtons( )
	{
		boolean complete = uriEditor.getText( ) != null
				&& uriEditor.getText( ).trim( ).length( ) > 0;
		URL url = null;
		try
		{
			// handle double quotation
			url = new URL( removeQuote( uriEditor.getText( ).trim( ) ) );
			if ( selectedType == EMBEDDED_TYPE )
			{
				File file = new File( url.getPath( ) );
				complete = file.exists( ) && file.isAbsolute( );
			}
		}
		catch ( Exception e )
		{
			complete = false;
		}
		
		previewButton.setEnabled( complete );
		getButton( IDialogConstants.OK_ID ).setEnabled( complete );
		if ( !complete )
		{
			// previewCanvas.clear( );
		}
		browseButton.setVisible( embedded.getSelection( ) );
	}

	/**
	 * @return image model in the form of Fill
	 */
	public Fill getResult( )
	{
		return fCurrent;
	}
	
	/**
	 * Remove the quote if the string enclosed width quote .
	 * 
	 * @param string
	 * @return string
	 */
	private String removeQuote( String string )
	{
		if ( string == null )
		{
			return ""; //$NON-NLS-1$
		}
		if ( string.trim( ).length( ) >= 2
				&& string.trim( ).startsWith( "\"" ) //$NON-NLS-1$
				&& string.trim( ).endsWith( "\"" ) ) //$NON-NLS-1$
		{
			return string.trim( ).substring( 1, string.trim( ).length( ) - 1 );
		}
		return string.trim( );
	}

	private static class ImageCanvas extends Composite implements PaintListener
	{

		private ImageData imageData;

		public ImageCanvas( Composite parent )
		{
			super( parent, SWT.NONE );
			addPaintListener( this );
		}

		public void update( String sUrl )
		{
			if ( sUrl == null )
			{
				return;
			}

			InputStream in = null;

			try
			{

				if ( sUrl.startsWith( DATA_BASE64 ) )
				{
					byte[] buf = Base64.decodeBase64( sUrl.substring( DATA_BASE64.length( ) )
							.getBytes( ) );
					in = new ByteArrayInputStream( buf );
				}
				else
				{
					in = new BufferedInputStream( new URL( sUrl ).openStream( ) );
				}

				ImageData[] datas = new ImageLoader( ).load( in );

				if ( datas.length > 0 )
				{
					imageData = datas[0];
				}
			}
			catch ( Exception e )
			{
				imageData = null;
				logger.log( e );
			}
			finally
			{
				if ( in != null )
				{
					try
					{
						in.close( );
					}
					catch ( Exception e )
					{
						// do nothing
					}
				}
			}

			redraw( );
		}

		public void paintControl( PaintEvent e )
		{
			if ( imageData == null )
			{
				return;
			}
			GC gc = e.gc;
			Rectangle destRect = getClientArea( );
			org.eclipse.swt.graphics.Image img = new org.eclipse.swt.graphics.Image( gc.getDevice( ),
					imageData );
			Rectangle srcRect = img.getBounds( );
			if ( srcRect.width > 0 && srcRect.height > 0 )
			{
				int x = 0, y = 0, w = destRect.width, h = destRect.height;

				if ( srcRect.width < w && srcRect.height < h )
				{
					w = srcRect.width;
					h = srcRect.height;
					x = ( destRect.width - w ) / 2;
					y = ( destRect.height - h ) / 2;
				}
				else
				{
					if ( ( 1d * srcRect.width / srcRect.height ) >= ( 1d * destRect.width / destRect.height ) )
					{
						double rate = 1d * w / srcRect.width;
						h = (int) ( srcRect.height * rate + 0.5 );
						y = ( destRect.height - h ) / 2;
					}
					else
					{
						double rate = 1d * h / srcRect.height;
						w = (int) ( srcRect.width * rate + 0.5 );
						x = ( destRect.width - w ) / 2;
					}
				}

				gc.drawImage( img,
						0,
						0,
						srcRect.width,
						srcRect.height,
						x,
						y,
						w,
						h );
			}
			img.dispose( );
		}

	}

}