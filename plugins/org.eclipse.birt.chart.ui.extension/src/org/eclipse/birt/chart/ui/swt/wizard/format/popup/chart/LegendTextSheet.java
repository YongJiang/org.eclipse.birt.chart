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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierPreview;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 */

public class LegendTextSheet extends AbstractPopupSheet implements Listener
{

	private transient Composite cmpContent = null;

	private transient FontDefinitionComposite fdcFont = null;

	private transient LineAttributesComposite lineSeparator;
	
	private transient FillChooserComposite fccBackground;

	private transient FillChooserComposite fccShadow;

	private transient LineAttributesComposite outlineText;

	private transient InsetsComposite icText;

	private transient FormatSpecifierPreview fsp;

	private transient Button btnFormatSpecifier;

	private Spinner spnEllipsis;

	private boolean isByCategory;

	private boolean containsYOG;

	public LegendTextSheet( String title, ChartWizardContext context )
	{
		super( title, context, true );
		isByCategory = getChart( ).getLegend( ).getItemType( ) != LegendItemType.SERIES_LITERAL;
		containsYOG = ChartUtil.containsYOptionalGrouping( getChart( ) );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_LEGEND_BLOCK );
		
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glMain = new GridLayout( );
			glMain.horizontalSpacing = 5;
			glMain.verticalSpacing = 5;
			glMain.marginHeight = 7;
			glMain.marginWidth = 7;
			cmpContent.setLayout( glMain );
		}

		Group grpTxtArea = new Group( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( 2, false );
			layout.marginHeight = 7;
			layout.marginWidth = 7;
			grpTxtArea.setLayout( layout );
			grpTxtArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			grpTxtArea.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.TextArea" ) ); //$NON-NLS-1$
		}
		
		Label lblFormat = new Label( grpTxtArea, SWT.NONE );
		{
			lblFormat.setText( Messages.getString( "DialLabelSheet.Label.Format" ) ); //$NON-NLS-1$
			lblFormat.setEnabled( isByCategory || containsYOG );
		}

		Composite cmpFormat = new Composite( grpTxtArea, SWT.BORDER );
		{
			GridLayout layout = new GridLayout( 2, false );
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			layout.horizontalSpacing = 0;
			cmpFormat.setLayout( layout );
			cmpFormat.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			cmpFormat.setBackground( cmpFormat.getDisplay( )
						.getSystemColor( SWT.COLOR_WHITE ) );
			cmpFormat.setEnabled( isByCategory || containsYOG );

		}

		fsp = new FormatSpecifierPreview( cmpFormat, SWT.NONE, false );
		{
			GridData gd = new GridData( );
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.CENTER;
			fsp.setLayoutData( gd );
			fsp.updatePreview( getChart( ).getLegend( ).getFormatSpecifier( ) );
			fsp.setEnabled( isByCategory || containsYOG );
		}

		btnFormatSpecifier = new Button( cmpFormat, SWT.PUSH );
		{
			GridData gd = new GridData( );
			// gd.widthHint = 20;
			gd.heightHint = 20;
			btnFormatSpecifier.setLayoutData( gd );
			btnFormatSpecifier.setToolTipText( Messages.getString( "BaseDataDefinitionComponent.Text.EditFormat" ) ); //$NON-NLS-1$
			//			btnFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
			// btnFormatSpecifier.getImage( )
			// .setBackground( btnFormatSpecifier.getBackground( ) );
			btnFormatSpecifier.setText( Messages.getString("Format.Button.Label") ); //$NON-NLS-1$
			btnFormatSpecifier.addListener( SWT.Selection, this );
			btnFormatSpecifier.setEnabled( isByCategory || containsYOG );
		}

		new Label( grpTxtArea, SWT.NONE ).setText( Messages.getString( "LegendTextSheet.Label.Font" ) ); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite( grpTxtArea,
				SWT.NONE,
				getContext( ),
				getLegend( ).getText( ).getFont( ),
				getLegend( ).getText( ).getColor( ),
				false );
		GridData gdFDCFont = new GridData( GridData.FILL_HORIZONTAL );
		// gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.grabExcessVerticalSpace = false;
		fdcFont.setLayoutData( gdFDCFont );
		fdcFont.addListener( this );

		new Label( grpTxtArea, SWT.NONE ).setText( Messages.getString("LegendTextSheet.Label.Ellipsis") ); //$NON-NLS-1$
		spnEllipsis = new Spinner( grpTxtArea, SWT.BORDER );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			spnEllipsis.setLayoutData( gd );
			spnEllipsis.setMinimum( 0 );
			spnEllipsis.setSelection( getLegend( ).getEllipsis( ) );
			spnEllipsis.setToolTipText( Messages.getString("LegendTextSheet.Tooltip.Ellipsis") ); //$NON-NLS-1$
			spnEllipsis.addListener( SWT.Selection, this );
		}

		Label lblShadow = new Label( grpTxtArea, SWT.NONE );
		GridData gdLBLShadow = new GridData( );
		lblShadow.setLayoutData( gdLBLShadow );
		lblShadow.setText( Messages.getString( "ClientAreaAttributeComposite.Lbl.Shadow" ) ); //$NON-NLS-1$

		fccShadow = new FillChooserComposite( grpTxtArea,
				SWT.NONE,
				getContext( ),
				getLegend( ).getClientArea( ).getShadowColor( ),
				false,
				false );
		GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
		fccShadow.setLayoutData( gdFCCShadow );
		fccShadow.addListener( this );
		
		Label lblBackground = new Label( grpTxtArea, SWT.NONE );
		lblBackground.setText( Messages.getString( "Shared.mne.Background_K" ) ); //$NON-NLS-1$

		fccBackground = new FillChooserComposite( grpTxtArea, SWT.DROP_DOWN
				| SWT.READ_ONLY, getContext( ), getChart( ).getLegend( )
				.getClientArea( )
				.getBackground( ), true, true );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			fccBackground.setLayoutData( gridData );
			fccBackground.addListener( this );
		}

		Group grpOutline = new Group( grpTxtArea, SWT.NONE );
		GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPOutline.horizontalSpan = 2;
		grpOutline.setLayoutData( gdGRPOutline );
		grpOutline.setLayout( new FillLayout( ) );
		grpOutline.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.Outline" ) ); //$NON-NLS-1$

		outlineText = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getLegend( ).getClientArea( ).getOutline( ),
				true,
				true,
				true );
		outlineText.addListener( this );
		outlineText.setAttributesEnabled( true );

		icText = new InsetsComposite( grpTxtArea,
				SWT.NONE,
				getLegend( ).getClientArea( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
		gdInsets.horizontalSpan = 2;
		icText.setLayoutData( gdInsets );
		icText.addListener( this );

		Group grpSeparator = new Group( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( );
			layout.marginHeight = 0;
			layout.marginWidth = 5;
			grpSeparator.setLayout( layout );
			grpSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			grpSeparator.setText( Messages.getString( "LegendTextSheet.Label.Separator" ) ); //$NON-NLS-1$
		}

		lineSeparator = new LineAttributesComposite( grpSeparator,
				SWT.NONE,
				getContext( ),
				getLegend( ).getSeparator( ),
				true,
				true,
				true );
		{
			lineSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			lineSeparator.addListener( this );
			lineSeparator.setAttributesEnabled( true );
		}

		return cmpContent;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( fdcFont ) )
		{
			getLegend( ).getText( )
					.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
			getLegend( ).getText( )
					.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
		}
		else if ( event.widget == spnEllipsis )
		{
			getLegend( ).setEllipsis( spnEllipsis.getSelection( ) );
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			getLegend( ).getClientArea( )
					.setShadowColor( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( fccBackground ) )
		{
			getLegend( ).getClientArea( )
					.setBackground( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( icText ) )
		{
			getLegend( ).getClientArea( ).setInsets( (Insets) event.data );
		}
		else if ( event.widget.equals( outlineText ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getLegend( ).getClientArea( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( event.widget.equals( lineSeparator ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getLegend( ).getSeparator( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if(event.widget.equals( btnFormatSpecifier ))
		{
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					getChart( ).getLegend( ).getFormatSpecifier( ),
					getEntryType( ),
					Messages.getString( "BaseDataDefinitionComponent.Text.EditFormat" ) ); //$NON-NLS-1$
			if ( editor.open( ) == Window.OK )
			{
				getChart( ).getLegend( )
						.setFormatSpecifier( editor.getFormatSpecifier( ) );
				fsp.updatePreview( editor.getFormatSpecifier( ) );
			}			
		}
	}

	private Legend getLegend( )
	{
		return getChart( ).getLegend( );
	}

	private AxisType getEntryType( )
	{
		DataType type = DataType.TEXT_LITERAL;
		if(isByCategory)
		{
			type = getCategoryQueryType( );
		}
		else if ( containsYOG )
		{
			type = getContext( ).getDataServiceProvider( )
					.getDataType( ChartUtil.getYOptoinalExpressions( getChart( ) )[0] );
		}
		if ( type == DataType.NUMERIC_LITERAL )
		{
			return AxisType.LINEAR_LITERAL;
		}
		else if ( type == DataType.DATE_TIME_LITERAL )
		{
			return AxisType.DATE_TIME_LITERAL;
		}

		return AxisType.TEXT_LITERAL;
	}

	private DataType getCategoryQueryType( )
	{
		String query = ChartUIUtil.getDataQuery( ChartUIUtil.getBaseSeriesDefinitions( getChart( ) )
				.get( 0 ),
				0 )
				.getDefinition( );
		return getContext( ).getDataServiceProvider( ).getDataType( query );
	}

}
