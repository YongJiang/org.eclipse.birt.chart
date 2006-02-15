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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FractionNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerLineImpl;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class AxisMarkersSheet extends AbstractPopupSheet
		implements
			SelectionListener,
			Listener
{

	private transient Composite cmpContent;

	private transient Composite cmpList = null;

	private transient Button btnAddLine = null;

	private transient Button btnAddRange = null;

	private transient Button btnRemove = null;

	private transient List lstMarkers = null;

	private transient Group grpGeneral = null;

	private transient StackLayout slMarkers = null;

	private transient Composite cmpLine = null;

	private transient Group grpMarkerLine = null;

	private transient Label lblValue = null;

	private transient TextEditorComposite txtValue = null;

	private transient Button btnLineFormatSpecifier = null;

	private transient Label lblAnchor = null;

	private transient Combo cmbLineAnchor = null;

	private transient LineAttributesComposite liacMarkerLine = null;

	private transient Composite cmpRange = null;

	private transient Group grpMarkerRange = null;

	private transient Label lblStartValue = null;

	private transient TextEditorComposite txtStartValue = null;

	private transient Button btnStartFormatSpecifier = null;

	private transient Label lblEndValue = null;

	private transient TextEditorComposite txtEndValue = null;

	private transient Button btnEndFormatSpecifier = null;

	private transient Label lblRangeAnchor = null;

	private transient Combo cmbRangeAnchor = null;

	private transient Label lblRangeFill = null;

	private transient FillChooserComposite fccRange = null;

	private transient LineAttributesComposite liacMarkerRange = null;

	private transient LabelAttributesComposite lacLabel = null;

	private transient int iLineCount = 0;

	private transient int iRangeCount = 0;

	private transient Axis axis;

	public AxisMarkersSheet( Composite parent, ChartWizardContext context,
			Axis axis )
	{
		super( parent, context, false );
		this.axis = axis;
		cmpTop = getComponent( parent );
	}

	protected Composite getComponent( Composite parent )
	{
		// Layout for the main composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 2;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		// Layout for the List composite
		GridLayout glList = new GridLayout( );
		glList.numColumns = 3;
		glList.horizontalSpacing = 5;
		glList.verticalSpacing = 5;
		glList.marginHeight = 0;
		glList.marginWidth = 0;

		cmpList = new Composite( cmpContent, SWT.NONE );
		GridData gdCMPList = new GridData( GridData.FILL_BOTH );
		gdCMPList.horizontalSpan = 2;
		cmpList.setLayoutData( gdCMPList );
		cmpList.setLayout( glList );

		// Layout for the buttons composite
		GridLayout glButtons = new GridLayout( );
		glButtons.numColumns = 3;
		glButtons.horizontalSpacing = 5;
		glButtons.verticalSpacing = 5;
		glButtons.marginHeight = 5;
		glButtons.marginWidth = 0;

		Composite cmpButtons = new Composite( cmpList, SWT.NONE );
		GridData gdCMPButtons = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPButtons.horizontalSpan = 3;
		cmpButtons.setLayoutData( gdCMPButtons );
		cmpButtons.setLayout( glButtons );

		btnAddLine = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNAddLine = new GridData( GridData.FILL_HORIZONTAL );
		btnAddLine.setLayoutData( gdBTNAddLine );
		btnAddLine.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.AddLine" ) ); //$NON-NLS-1$
		btnAddLine.addSelectionListener( this );

		btnAddRange = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNAddRange = new GridData( GridData.FILL_HORIZONTAL );
		btnAddRange.setLayoutData( gdBTNAddRange );
		btnAddRange.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.AddRange" ) ); //$NON-NLS-1$
		btnAddRange.addSelectionListener( this );

		btnRemove = new Button( cmpButtons, SWT.PUSH );
		GridData gdBTNRemove = new GridData( GridData.FILL_HORIZONTAL );
		btnRemove.setLayoutData( gdBTNRemove );
		btnRemove.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.RemoveEntry" ) ); //$NON-NLS-1$
		btnRemove.addSelectionListener( this );

		lstMarkers = new List( cmpList, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL );
		GridData gdLSTMarkers = new GridData( GridData.FILL_HORIZONTAL );
		gdLSTMarkers.horizontalSpan = 3;
		gdLSTMarkers.heightHint = 100;
		lstMarkers.setLayoutData( gdLSTMarkers );
		lstMarkers.addSelectionListener( this );

		// Layout for the general composite
		slMarkers = new StackLayout( );
		slMarkers.marginHeight = 0;
		slMarkers.marginWidth = 0;

		grpGeneral = new Group( cmpContent, SWT.NONE );
		GridData gdCMPGeneral = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL );
		gdCMPGeneral.widthHint = 180;
		grpGeneral.setLayoutData( gdCMPGeneral );
		grpGeneral.setLayout( slMarkers );
		grpGeneral.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerProperties" ) ); //$NON-NLS-1$

		// Layout for the Marker Line composite
		GridLayout glMarkerLine = new GridLayout( );
		glMarkerLine.numColumns = 3;
		glMarkerLine.horizontalSpacing = 5;
		glMarkerLine.verticalSpacing = 5;
		glMarkerLine.marginHeight = 7;
		glMarkerLine.marginWidth = 7;

		cmpLine = new Composite( grpGeneral, SWT.NONE );
		GridData gdGRPLine = new GridData( GridData.FILL_HORIZONTAL );
		cmpLine.setLayoutData( gdGRPLine );
		cmpLine.setLayout( glMarkerLine );

		// Layout for Value composite
		GridLayout glValue = new GridLayout( );
		glValue.numColumns = 3;
		glValue.horizontalSpacing = 5;
		glValue.verticalSpacing = 5;
		glValue.marginHeight = 0;
		glValue.marginWidth = 0;

		Composite cmpValue = new Composite( cmpLine, SWT.NONE );
		GridData gdCMPValue = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPValue.horizontalSpan = 3;
		cmpValue.setLayoutData( gdCMPValue );
		cmpValue.setLayout( glValue );

		lblValue = new Label( cmpValue, SWT.NONE );
		GridData gdLBLValue = new GridData( );
		gdLBLValue.horizontalIndent = 5;
		gdLBLValue.widthHint = 38;
		lblValue.setLayoutData( gdLBLValue );
		lblValue.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Value" ) ); //$NON-NLS-1$

		txtValue = new TextEditorComposite( cmpValue,
				SWT.BORDER | SWT.SINGLE,
				true );
		GridData gdTXTValue = new GridData( GridData.FILL_HORIZONTAL );
		txtValue.setLayoutData( gdTXTValue );
		txtValue.addListener( this );

		btnLineFormatSpecifier = new Button( cmpValue, SWT.PUSH );
		GridData gdBTNLineFormatSpecifier = new GridData( );
		gdBTNLineFormatSpecifier.heightHint = 18;
		gdBTNLineFormatSpecifier.widthHint = 18;
		btnLineFormatSpecifier.setLayoutData( gdBTNLineFormatSpecifier );
		btnLineFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
		btnLineFormatSpecifier.addSelectionListener( this );
		btnLineFormatSpecifier.getImage( )
				.setBackground( btnLineFormatSpecifier.getBackground( ) );

		lblAnchor = new Label( cmpLine, SWT.NONE );
		GridData gdLBLAnchor = new GridData( );
		gdLBLAnchor.horizontalIndent = 5;
		lblAnchor.setLayoutData( gdLBLAnchor );
		lblAnchor.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Anchor" ) ); //$NON-NLS-1$

		cmbLineAnchor = new Combo( cmpLine, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBAnchor = new GridData( GridData.FILL_HORIZONTAL );
		gdCMBAnchor.horizontalSpan = 2;
		cmbLineAnchor.setLayoutData( gdCMBAnchor );
		cmbLineAnchor.addSelectionListener( this );

		grpMarkerLine = new Group( cmpLine, SWT.NONE );
		GridData gdGRPMarkerLine = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPMarkerLine.horizontalSpan = 3;
		grpMarkerLine.setLayoutData( gdGRPMarkerLine );
		grpMarkerLine.setLayout( new FillLayout( ) );
		grpMarkerLine.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerLineAttributes" ) ); //$NON-NLS-1$

		liacMarkerLine = new LineAttributesComposite( grpMarkerLine,
				SWT.NONE,
				null,
				true,
				true,
				true );
		liacMarkerLine.addListener( this );

		// Layout for the Marker Range composite
		GridLayout glMarkerRange = new GridLayout( );
		glMarkerRange.numColumns = 3;
		glMarkerRange.horizontalSpacing = 5;
		glMarkerRange.verticalSpacing = 5;
		glMarkerRange.marginHeight = 7;
		glMarkerRange.marginWidth = 7;

		cmpRange = new Composite( grpGeneral, SWT.NONE );
		GridData gdGRPRange = new GridData( GridData.FILL_HORIZONTAL );
		cmpRange.setLayoutData( gdGRPRange );
		cmpRange.setLayout( glMarkerRange );

		// Layout for Value composite
		GridLayout glRangeValue = new GridLayout( );
		glRangeValue.numColumns = 3;
		glRangeValue.horizontalSpacing = 2;
		glRangeValue.verticalSpacing = 5;
		glRangeValue.marginHeight = 0;
		glRangeValue.marginWidth = 0;

		Composite cmpRangeValue = new Composite( cmpRange, SWT.NONE );
		GridData gdCMPRangeValue = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPRangeValue.horizontalSpan = 3;
		cmpRangeValue.setLayoutData( gdCMPRangeValue );
		cmpRangeValue.setLayout( glRangeValue );

		lblStartValue = new Label( cmpRangeValue, SWT.NONE );
		GridData gdLBLStartValue = new GridData( );
		gdLBLStartValue.horizontalIndent = 5;
		lblStartValue.setLayoutData( gdLBLStartValue );
		lblStartValue.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.StartValue" ) ); //$NON-NLS-1$

		txtStartValue = new TextEditorComposite( cmpRangeValue, SWT.BORDER
				| SWT.SINGLE, true );
		GridData gdTXTStartValue = new GridData( GridData.FILL_HORIZONTAL );
		txtStartValue.setLayoutData( gdTXTStartValue );
		txtStartValue.addListener( this );

		btnStartFormatSpecifier = new Button( cmpRangeValue, SWT.PUSH );
		GridData gdBTNStartFormatSpecifier = new GridData( );
		gdBTNStartFormatSpecifier.heightHint = 18;
		gdBTNStartFormatSpecifier.widthHint = 18;
		btnStartFormatSpecifier.setLayoutData( gdBTNStartFormatSpecifier );
		btnStartFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
		btnStartFormatSpecifier.addSelectionListener( this );
		btnStartFormatSpecifier.getImage( )
				.setBackground( btnStartFormatSpecifier.getBackground( ) );

		lblEndValue = new Label( cmpRangeValue, SWT.NONE );
		GridData gdLBLEndValue = new GridData( );
		gdLBLEndValue.horizontalIndent = 5;
		lblEndValue.setLayoutData( gdLBLEndValue );
		lblEndValue.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.EndValue" ) ); //$NON-NLS-1$

		txtEndValue = new TextEditorComposite( cmpRangeValue, SWT.BORDER
				| SWT.SINGLE, true );
		GridData gdTXTEndValue = new GridData( GridData.FILL_HORIZONTAL );
		txtEndValue.setLayoutData( gdTXTEndValue );
		txtEndValue.addListener( this );

		btnEndFormatSpecifier = new Button( cmpRangeValue, SWT.PUSH );
		GridData gdBTNEndFormatSpecifier = new GridData( );
		gdBTNEndFormatSpecifier.heightHint = 18;
		gdBTNEndFormatSpecifier.widthHint = 18;
		btnEndFormatSpecifier.setLayoutData( gdBTNEndFormatSpecifier );
		btnEndFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
		btnEndFormatSpecifier.addSelectionListener( this );
		btnEndFormatSpecifier.getImage( )
				.setBackground( btnEndFormatSpecifier.getBackground( ) );

		lblRangeAnchor = new Label( cmpRange, SWT.NONE );
		GridData gdLBLRangeAnchor = new GridData( );
		gdLBLRangeAnchor.horizontalIndent = 5;
		gdLBLRangeAnchor.widthHint = 54;
		lblRangeAnchor.setLayoutData( gdLBLRangeAnchor );
		lblRangeAnchor.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Anchor" ) ); //$NON-NLS-1$

		cmbRangeAnchor = new Combo( cmpRange, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBRangeAnchor = new GridData( GridData.FILL_HORIZONTAL );
		gdCMBRangeAnchor.horizontalSpan = 2;
		cmbRangeAnchor.setLayoutData( gdCMBRangeAnchor );
		cmbRangeAnchor.addSelectionListener( this );

		lblRangeFill = new Label( cmpRange, SWT.NONE );
		GridData gdLBLRangeFill = new GridData( );
		gdLBLRangeFill.horizontalIndent = 5;
		gdLBLRangeFill.widthHint = 54;
		lblRangeFill.setLayoutData( gdLBLRangeFill );
		lblRangeFill.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.Fill" ) ); //$NON-NLS-1$

		fccRange = new FillChooserComposite( cmpRange,
				SWT.NONE,
				getContext( ),
				null,
				true,
				true );
		GridData gdFCCRange = new GridData( GridData.FILL_HORIZONTAL );
		gdFCCRange.horizontalSpan = 2;
		fccRange.setLayoutData( gdFCCRange );
		fccRange.addListener( this );

		grpMarkerRange = new Group( cmpRange, SWT.NONE );
		GridData gdGRPMarkerRange = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPMarkerRange.horizontalSpan = 3;
		grpMarkerRange.setLayoutData( gdGRPMarkerRange );
		grpMarkerRange.setLayout( new FillLayout( ) );
		grpMarkerRange.setText( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.RangeOutline" ) ); //$NON-NLS-1$

		liacMarkerRange = new LineAttributesComposite( grpMarkerRange,
				SWT.NONE,
				null,
				true,
				true,
				true );
		liacMarkerRange.addListener( this );

		lacLabel = new LabelAttributesComposite( cmpContent,
				SWT.NONE,
				Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerLabelProperties" ), //$NON-NLS-1$
				Position.ABOVE_LITERAL,
				LabelImpl.create( ),
				chart.getUnits( ),
				false,
				true,
				serviceprovider,
				true );
		GridData gdLACLabel = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL );
		lacLabel.setLayoutData( gdLACLabel );
		lacLabel.addListener( this );

		slMarkers.topControl = cmpLine;

		populateLists( );

		refreshButtons( );

		setAllEnabled( !getAxisForProcessing( ).isCategoryAxis( )
				|| getAxisForProcessing( ).getType( )
						.equals( AxisType.TEXT_LITERAL ) );

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( lacLabel ) )
		{
			if ( this.lstMarkers.getSelection( ).length != 0 )
			{
				switch ( event.type )
				{
					case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
						getSelectedMarkerLabel( ).setVisible( ( (Boolean) event.data ).booleanValue( ) );
						break;
					case LabelAttributesComposite.FONT_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getCaption( )
								.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
						getSelectedMarkerLabel( ).getCaption( )
								.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
						break;
					case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
						getSelectedMarkerLabel( ).setBackground( (Fill) event.data );
						break;
					case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
						getSelectedMarkerLabel( ).setShadowColor( (ColorDefinition) event.data );
						break;
					case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getOutline( )
								.setStyle( (LineStyle) event.data );
						break;
					case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getOutline( )
								.setThickness( ( (Integer) event.data ).intValue( ) );
						break;
					case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getOutline( )
								.setColor( (ColorDefinition) event.data );
						break;
					case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
						getSelectedMarkerLabel( ).getOutline( )
								.setVisible( ( (Boolean) event.data ).booleanValue( ) );
						break;
					case LabelAttributesComposite.INSETS_CHANGED_EVENT :
						getSelectedMarkerLabel( ).setInsets( (Insets) event.data );
						break;
				}
			}
		}
		else if ( event.widget.equals( fccRange ) )
		{
			( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) ) ).setFill( (Fill) event.data );
		}
		else if ( event.widget.equals( txtValue ) )
		{
			if ( event.type == TextEditorComposite.TEXT_MODIFIED )
			{
				int iMarkerIndex = getMarkerIndex( );
				( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( iMarkerIndex ) ).setValue( this.getTypedDataElement( txtValue.getText( ) ) );
			}
			if ( event.type == TextEditorComposite.TEXT_FRACTION_CONVERTED )
			{
				// Change FormatSpecifier if the text is fraction and has been converted
				MarkerLine line = (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) );
				if ( !( line.getFormatSpecifier( ) instanceof FractionNumberFormatSpecifier ) )
				{
					FractionNumberFormatSpecifier ffs = FractionNumberFormatSpecifierImpl.create( );
					line.setFormatSpecifier( ffs );
				}
			}
		}
		else if ( event.widget.equals( txtStartValue ) )
		{
			int iMarkerIndex = getMarkerIndex( );
			( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
					.get( iMarkerIndex ) ).setStartValue( this.getTypedDataElement( txtStartValue.getText( ) ) );
		}
		else if ( event.widget.equals( txtEndValue ) )
		{
			int iMarkerIndex = getMarkerIndex( );
			( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
					.get( iMarkerIndex ) ).setEndValue( this.getTypedDataElement( txtEndValue.getText( ) ) );
		}
		else if ( event.widget.equals( liacMarkerLine ) )
		{
			if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) ) ).getLineAttributes( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) ) ).getLineAttributes( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) ) ).getLineAttributes( )
						.setColor( (ColorDefinition) event.data );
			}
			else
			{
				( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) ) ).getLineAttributes( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
			}
		}
		else if ( event.widget.equals( liacMarkerRange ) )
		{
			if ( event.type == LineAttributesComposite.STYLE_CHANGED_EVENT )
			{
				( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) ) ).getOutline( )
						.setStyle( (LineStyle) event.data );
			}
			else if ( event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT )
			{
				( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) ) ).getOutline( )
						.setThickness( ( (Integer) event.data ).intValue( ) );
			}
			else if ( event.type == LineAttributesComposite.COLOR_CHANGED_EVENT )
			{
				( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) ) ).getOutline( )
						.setColor( (ColorDefinition) event.data );
			}
			else
			{
				( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) ) ).getOutline( )
						.setVisible( ( (Boolean) event.data ).booleanValue( ) );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnAddLine ) )
		{
			MarkerLine line = MarkerLineImpl.create( getAxisForProcessing( ),
					getTypedDataElement( "" ) ); //$NON-NLS-1$
			line.eAdapters( ).addAll( getAxisForProcessing( ).eAdapters( ) );
			iLineCount++;
			buildList( );
			lstMarkers.select( lstMarkers.getItemCount( ) - 1 );
			updateUIForSelection( );
			if ( lstMarkers.getItemCount( ) == 1 )
			{
				// Enable UI elements
				setState( true );
			}

			refreshButtons( );
		}
		else if ( e.getSource( ).equals( btnAddRange ) )
		{
			MarkerRange range = MarkerRangeImpl.create( getAxisForProcessing( ),
					getTypedDataElement( "" ), //$NON-NLS-1$
					getTypedDataElement( "" ), ColorDefinitionImpl.TRANSPARENT( ) ); //$NON-NLS-1$
			range.eAdapters( ).addAll( getAxisForProcessing( ).eAdapters( ) );
			iRangeCount++;
			buildList( );
			lstMarkers.select( lstMarkers.getItemCount( ) - 1 );
			updateUIForSelection( );
			if ( lstMarkers.getItemCount( ) == 1 )
			{
				// Enable UI elements
				setState( true );
			}

			refreshButtons( );
		}
		else if ( e.getSource( ).equals( btnRemove ) )
		{
			if ( lstMarkers.getSelection( ).length == 0 )
			{
				return;
			}
			String sSelectedMarker = lstMarkers.getSelection( )[0];
			boolean bLine = sSelectedMarker.startsWith( "MarkerLine" ); //$NON-NLS-1$
			int iMarkerIndex = getMarkerIndex( );
			if ( bLine )
			{
				getAxisForProcessing( ).getMarkerLines( ).remove( iMarkerIndex );
				iLineCount--;
			}
			else
			{
				getAxisForProcessing( ).getMarkerRanges( )
						.remove( iMarkerIndex );
				iRangeCount--;
			}
			buildList( );
			if ( lstMarkers.getItemCount( ) > 0 )
			{
				lstMarkers.select( 0 );
				updateUIForSelection( );
			}
			else
			{
				setState( false );
				resetUI( );
			}

			refreshButtons( );
		}
		else if ( e.getSource( ).equals( lstMarkers ) )
		{
			updateUIForSelection( );
			refreshButtons( );
		}
		else if ( e.getSource( ).equals( cmbLineAnchor ) )
		{
			( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
					.get( getMarkerIndex( ) ) ).setLabelAnchor( Anchor.getByName( LiteralHelper.anchorSet.getNameByDisplayName( cmbLineAnchor.getText( ) ) ) );
		}
		else if ( e.getSource( ).equals( cmbRangeAnchor ) )
		{
			( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) ) ).setLabelAnchor( Anchor.getByName( LiteralHelper.anchorSet.getNameByDisplayName( cmbRangeAnchor.getText( ) ) ) );
		}
		else if ( e.getSource( ).equals( btnLineFormatSpecifier ) )
		{
			String sAxisTitle = ""; //$NON-NLS-1$
			try
			{
				sAxisTitle = new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.ForAxis" ) ).format( new Object[]{getAxisForProcessing( ).getTitle( ).getCaption( ).getValue( )} ); //$NON-NLS-1$
			}
			catch ( NullPointerException e1 )
			{
			}

			FormatSpecifier formatspecifier = null;
			if ( ( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
					.get( getMarkerIndex( ) ) ).getFormatSpecifier( ) != null )
			{
				formatspecifier = ( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) ) ).getFormatSpecifier( );
			}
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerLine" ) ).format( new Object[]{new Integer( getMarkerIndex( ) + 1 ), sAxisTitle} ) ); //$NON-NLS-1$
			if ( !editor.wasCancelled( ) )
			{
				if ( editor.getFormatSpecifier( ) == null )
				{
					( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
							.get( getMarkerIndex( ) ) ).eUnset( ComponentPackage.eINSTANCE.getMarkerLine_FormatSpecifier( ) );
					return;
				}
				( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
						.get( getMarkerIndex( ) ) ).setFormatSpecifier( editor.getFormatSpecifier( ) );
			}
		}
		else if ( e.getSource( ).equals( btnStartFormatSpecifier )
				|| e.getSource( ).equals( btnEndFormatSpecifier ) )
		{
			String sAxisTitle = ""; //$NON-NLS-1$
			try
			{
				String sTitleString = getAxisForProcessing( ).getTitle( )
						.getCaption( )
						.getValue( );
				int iSeparatorIndex = sTitleString.indexOf( ExternalizedTextEditorComposite.SEPARATOR );
				if ( iSeparatorIndex > 0 )
				{
					sTitleString = sTitleString.substring( iSeparatorIndex );
				}
				else if ( iSeparatorIndex == 0 )
				{
					sTitleString = sTitleString.substring( ExternalizedTextEditorComposite.SEPARATOR.length( ) );
				}
				sAxisTitle = new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.ForAxis" ) ).format( new Object[]{sTitleString} ); //$NON-NLS-1$ 
			}
			catch ( NullPointerException e1 )
			{
			}

			FormatSpecifier formatspecifier = null;
			if ( ( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
					.get( getMarkerIndex( ) ) ).getFormatSpecifier( ) != null )
			{
				formatspecifier = ( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) ) ).getFormatSpecifier( );
			}
			FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
					formatspecifier,
					new MessageFormat( Messages.getString( "BaseAxisMarkerAttributeSheetImpl.Lbl.MarkerRange" ) ).format( new Object[]{new Integer( getMarkerIndex( ) + 1 ), sAxisTitle} ) ); //$NON-NLS-1$
			if ( !editor.wasCancelled( ) )
			{
				if ( editor.getFormatSpecifier( ) == null )
				{
					( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
							.get( getMarkerIndex( ) ) ).eUnset( ComponentPackage.eINSTANCE.getMarkerRange_FormatSpecifier( ) );
					return;
				}
				( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
						.get( getMarkerIndex( ) ) ).setFormatSpecifier( editor.getFormatSpecifier( ) );
			}
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

	private Axis getAxisForProcessing( )
	{
		return axis;
	}

	private String getValueAsString( DataElement de )
	{
		String sValue = ""; //$NON-NLS-1$
		if ( de instanceof NumberDataElement )
		{
			sValue = String.valueOf( ( (NumberDataElement) de ).getValue( ) );
		}
		else if ( de instanceof DateTimeDataElement )
		{
			Date date = new Date( ( (DateTimeDataElement) de ).getValue( ) );
			SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault( ) ); //$NON-NLS-1$
			sValue = sdf.format( date );
		}
		return sValue;
	}

	private int getMarkerIndex( )
	{
		String sSelectedMarker = lstMarkers.getSelection( )[0];
		int iSelectionIndex = lstMarkers.getSelectionIndex( );
		if ( sSelectedMarker.startsWith( "MarkerLine" ) ) //$NON-NLS-1$
		{
			return iSelectionIndex;
		}
		return iSelectionIndex
				- ( getAxisForProcessing( ).getMarkerLines( ).size( ) );
	}

	private void buildList( )
	{
		// Clear any existing contents
		lstMarkers.removeAll( );

		// Populate list of markers
		iLineCount = getAxisForProcessing( ).getMarkerLines( ).size( );
		iRangeCount = getAxisForProcessing( ).getMarkerRanges( ).size( );
		for ( int iLines = 0; iLines < iLineCount; iLines++ )
		{
			lstMarkers.add( "MarkerLine - " + ( iLines + 1 ) ); //$NON-NLS-1$
		}
		for ( int iRanges = 0; iRanges < iRangeCount; iRanges++ )
		{
			lstMarkers.add( "MarkerRange - " + ( iRanges + 1 ) ); //$NON-NLS-1$
		}
	}

	private void refreshButtons( )
	{
		btnRemove.setEnabled( lstMarkers.getSelectionIndex( ) != -1 );
	}

	private void updateUIForSelection( )
	{
		String sSelectedMarker = lstMarkers.getSelection( )[0];
		// Switch stack layout topControl
		if ( sSelectedMarker.startsWith( "MarkerLine" ) ) //$NON-NLS-1$
		{
			slMarkers.topControl = cmpLine;
			grpGeneral.layout( );

			int iLineIndex = getMarkerIndex( );
			MarkerLine line = ( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
					.get( iLineIndex ) );

			// Update the value fields
			txtValue.setText( getValueAsString( line.getValue( ) ) );

			// Update the Anchor field
			cmbLineAnchor.setText( LiteralHelper.anchorSet.getDisplayNameByName( line.getLabelAnchor( )
					.getName( ) ) );

			// Update the Line attribute fields
			liacMarkerLine.setLineAttributes( line.getLineAttributes( ) );

			// Update the Label attribute fields
			lacLabel.setLabel( line.getLabel( ), chart.getUnits( ) );
		}
		else
		{
			slMarkers.topControl = cmpRange;
			grpGeneral.layout( );

			int iRangeIndex = getMarkerIndex( );
			MarkerRange range = (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
					.get( iRangeIndex );

			// Update the value fields
			txtStartValue.setText( getValueAsString( range.getStartValue( ) ) );
			txtEndValue.setText( getValueAsString( range.getEndValue( ) ) );

			// Update the anchor field
			cmbRangeAnchor.setText( LiteralHelper.anchorSet.getDisplayNameByName( range.getLabelAnchor( )
					.getName( ) ) );

			// Update the fill
			fccRange.setFill( range.getFill( ) );

			// Update the Line attribute fields
			liacMarkerRange.setLineAttributes( range.getOutline( ) );

			// Update the Label attribute fields
			lacLabel.setLabel( range.getLabel( ), chart.getUnits( ) );
		}
	}

	private void populateLists( )
	{
		buildList( );

		NameSet ns = LiteralHelper.anchorSet;

		// Populate combo boxes
		cmbLineAnchor.setItems( ns.getDisplayNames( ) );
		cmbLineAnchor.select( 0 );

		cmbRangeAnchor.setItems( ns.getDisplayNames( ) );
		cmbRangeAnchor.select( 0 );

		if ( lstMarkers.getItemCount( ) > 0 )
		{
			lstMarkers.select( 0 );
			updateUIForSelection( );
		}
		else
		{
			setState( false );
		}
	}

	private void setState( boolean bState )
	{
		btnLineFormatSpecifier.setEnabled( bState );
		lblAnchor.setEnabled( bState );
		cmbLineAnchor.setEnabled( bState );
		lblValue.setEnabled( bState );
		txtValue.setEnabled( bState );
		liacMarkerLine.setEnabled( bState );

		btnStartFormatSpecifier.setEnabled( bState );
		btnEndFormatSpecifier.setEnabled( bState );
		lblRangeAnchor.setEnabled( bState );
		cmbRangeAnchor.setEnabled( bState );
		lblStartValue.setEnabled( bState );
		txtStartValue.setEnabled( bState );
		lblEndValue.setEnabled( bState );
		txtEndValue.setEnabled( bState );
		liacMarkerRange.setEnabled( bState );
		lacLabel.setEnabled( bState );
		lblRangeFill.setEnabled( bState );
		fccRange.setEnabled( bState );

		this.grpGeneral.setEnabled( bState );
		this.grpMarkerLine.setEnabled( bState );
		this.grpMarkerRange.setEnabled( bState );
	}

	private void resetUI( )
	{
		cmbLineAnchor.select( 0 );
		cmbRangeAnchor.select( 0 );

		slMarkers.topControl = cmpLine;
		txtValue.setText( "" ); //$NON-NLS-1$
		liacMarkerLine.setLineAttributes( null );
		liacMarkerLine.layout( );
		txtStartValue.setText( "" ); //$NON-NLS-1$
		txtEndValue.setText( "" ); //$NON-NLS-1$
		fccRange.setFill( null );
		liacMarkerRange.setLineAttributes( null );
		liacMarkerRange.layout( );
		lacLabel.setLabel( LabelImpl.create( ), chart.getUnits( ) );
		lacLabel.layout( );
	}

	private org.eclipse.birt.chart.model.component.Label getSelectedMarkerLabel( )
	{
		String sSelectedMarker = lstMarkers.getSelection( )[0];
		int iMarkerIndex = getMarkerIndex( );
		if ( sSelectedMarker.startsWith( "MarkerLine" ) ) //$NON-NLS-1$
		{
			return ( (MarkerLine) getAxisForProcessing( ).getMarkerLines( )
					.get( iMarkerIndex ) ).getLabel( );
		}
		return ( (MarkerRange) getAxisForProcessing( ).getMarkerRanges( )
				.get( iMarkerIndex ) ).getLabel( );
	}

	private DataElement getTypedDataElement( String strDataElement )
	{
		Axis axis = getAxisForProcessing( );
		if ( strDataElement.trim( ).length( ) == 0 )
		{
			if ( axis.getType( ).equals( AxisType.DATE_TIME_LITERAL )
					&& !axis.isCategoryAxis( ) )
			{
				Date dateElement = new Date( );
				Calendar cal = Calendar.getInstance( TimeZone.getDefault( ),
						Locale.getDefault( ) );
				cal.setTime( dateElement );
				return DateTimeDataElementImpl.create( cal );
			}
			return NumberDataElementImpl.create( 0.0 );
		}
		SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault( ) ); //$NON-NLS-1$
		NumberFormat nf = NumberFormat.getNumberInstance( Locale.getDefault( ) );
		if ( axis.getType( ).equals( AxisType.DATE_TIME_LITERAL )
				&& !axis.isCategoryAxis( ) )
		{
			try
			{
				Date dateElement = sdf.parse( strDataElement );
				Calendar cal = Calendar.getInstance( TimeZone.getDefault( ),
						Locale.getDefault( ) );
				cal.setTime( dateElement );
				return DateTimeDataElementImpl.create( cal );
			}
			catch ( ParseException e )
			{
				return DateTimeDataElementImpl.create( new Date( ).getTime( ) );
			}
		}
		try
		{
			Number numberElement = nf.parse( strDataElement );
			return NumberDataElementImpl.create( numberElement.doubleValue( ) );
		}
		catch ( ParseException e1 )
		{
			return NumberDataElementImpl.create( 0.0 );
		}
	}

	private void setEnabled( Control control, boolean isEnabled )
	{
		control.setEnabled( isEnabled && control.isEnabled( ) );
	}

	private void setAllEnabled( boolean isEnabled )
	{
		setState( isEnabled && lstMarkers.getItemCount( ) > 0 );
		lacLabel.setEnabled( isEnabled || lacLabel.isEnabled( ) );

		setEnabled( cmpContent, isEnabled );
		setEnabled( btnAddLine, isEnabled );
		setEnabled( btnAddRange, isEnabled );
		setEnabled( btnRemove, isEnabled );

		// TODO disable all?
	}

}
