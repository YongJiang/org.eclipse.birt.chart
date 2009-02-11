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

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class PlotClientAreaSheet extends AbstractPopupSheet implements
		Listener,
		ModifyListener,
		SelectionListener
{

	static final int AUTO = -1;

	private transient Composite cmpContent;

	private transient Combo cmbAnchor;

	private transient Combo cmbStretch;

	private transient LineAttributesComposite outlineIncluding;

	private transient LineAttributesComposite outlineWithin;

	private transient InsetsComposite icIncluding;

	private transient InsetsComposite icWithin;

	private transient IntegerSpinControl iscVSpacing;

	private transient IntegerSpinControl iscHSpacing;

	private transient LocalizedNumberEditorComposite txtHeight;

	private transient LocalizedNumberEditorComposite txtWidth;

	private transient FillChooserComposite fccShadow;

	private Button btnHeight;

	private Button btnWidth;

	public PlotClientAreaSheet( String title, ChartWizardContext context )
	{
		super( title, context, true );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent,
				ChartHelpContextIds.POPUP_PLOT_AREA_FORMAT );

		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( new GridLayout( ) );

		Group grpAreaIncluding = new Group( cmpContent, SWT.NONE );
		{
			grpAreaIncluding.setLayout( new GridLayout( 2, true ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			grpAreaIncluding.setLayoutData( gd );
			grpAreaIncluding.setText( getChart( ) instanceof ChartWithAxes ? Messages.getString( "ChartPlotSheetImpl.Label.AreaIncludingAxes" ) : Messages.getString( "ChartPlotSheetImpl.Label.PlotArea" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Composite leftComp = new Composite( grpAreaIncluding, SWT.NONE );
		GridData gdL = new GridData( GridData.FILL_HORIZONTAL );
		leftComp.setLayout( new GridLayout( 3, false ) );
		leftComp.setLayoutData( gdL );
		Composite rightComp = new Composite( grpAreaIncluding, SWT.NONE );
		GridData gdR = new GridData( GridData.FILL_HORIZONTAL );
		rightComp.setLayout( new GridLayout( 1, false ) );
		rightComp.setLayoutData( gdR );

		Label lblAnchor = new Label( leftComp, SWT.NONE );
		GridData gdLBLAnchor = new GridData( );
		lblAnchor.setLayoutData( gdLBLAnchor );
		lblAnchor.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Anchor" ) ); //$NON-NLS-1$

		cmbAnchor = new Combo( leftComp, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCBAnchor = new GridData( GridData.FILL_HORIZONTAL );
		gdCBAnchor.horizontalSpan = 2;
		cmbAnchor.setLayoutData( gdCBAnchor );
		cmbAnchor.addSelectionListener( this );

		Label lblStretch = new Label( leftComp, SWT.NONE );
		{
			GridData gd = new GridData( );
			lblStretch.setLayoutData( gd );
			lblStretch.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Stretch" ) ); //$NON-NLS-1$
		}

		cmbStretch = new Combo( leftComp, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmbStretch.setLayoutData( gd );
			cmbStretch.addSelectionListener( this );
		}

		Label lblVerticalSpacing = new Label( leftComp, SWT.NONE );
		lblVerticalSpacing.setLayoutData( new GridData( ) );
		lblVerticalSpacing.setText( Messages.getString( "BlockAttributeComposite.Lbl.VerticalSpacing" ) ); //$NON-NLS-1$

		iscVSpacing = new IntegerSpinControl( leftComp,
				SWT.NONE,
				getBlockForProcessing( ).getVerticalSpacing( ) );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		iscVSpacing.setLayoutData( gd );
		iscVSpacing.addListener( this );

		Label lblHorizontalSpacing = new Label( leftComp, SWT.NONE );
		lblHorizontalSpacing.setLayoutData( new GridData( ) );
		lblHorizontalSpacing.setText( Messages.getString( "BlockAttributeComposite.Lbl.HorizontalSpacing" ) ); //$NON-NLS-1$

		iscHSpacing = new IntegerSpinControl( leftComp,
				SWT.NONE,
				getBlockForProcessing( ).getHorizontalSpacing( ) );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		iscHSpacing.setLayoutData( gd );
		iscHSpacing.addListener( this );

		new Label( leftComp, SWT.NONE ).setText( Messages.getString( "PlotClientAreaSheet.Label.HeightHint" ) ); //$NON-NLS-1$

		btnHeight = new Button( leftComp, SWT.CHECK );
		btnHeight.setText( Messages.getString("PlotClientAreaSheet.Btn.Auto") ); //$NON-NLS-1$
		gd = new GridData( );
		btnHeight.setLayoutData( gd );
		btnHeight.addSelectionListener( this );

		txtHeight = new LocalizedNumberEditorComposite( leftComp, SWT.BORDER );
		new TextNumberEditorAssistField( txtHeight.getTextControl( ), null );
		{
			txtHeight.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			txtHeight.addModifyListener( this );
		}

		new Label( leftComp, SWT.NONE ).setText( Messages.getString( "PlotClientAreaSheet.Label.WidthHint" ) ); //$NON-NLS-1$

		btnWidth = new Button( leftComp, SWT.CHECK );
		btnWidth.setText( Messages.getString("PlotClientAreaSheet.Btn.Auto") ); //$NON-NLS-1$
		gd = new GridData( );
		btnWidth.setLayoutData( gd );
		btnWidth.addSelectionListener( this );

		txtWidth = new LocalizedNumberEditorComposite( leftComp, SWT.BORDER );
		new TextNumberEditorAssistField( txtWidth.getTextControl( ), null );
		{
			txtWidth.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			txtWidth.addModifyListener( this );
		}

		updateHeightWidthHint( );

		Group grpOutline = new Group( rightComp, SWT.NONE );
		GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPOutline.widthHint = 150;
		grpOutline.setLayoutData( gdGRPOutline );
		grpOutline.setLayout( new FillLayout( ) );
		grpOutline.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Outline" ) ); //$NON-NLS-1$

		outlineIncluding = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getOutline( ),
				true,
				true,
				false );
		outlineIncluding.addListener( this );

		icIncluding = new InsetsComposite( rightComp,
				SWT.NONE,
				getBlockForProcessing( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
		icIncluding.setLayoutData( gdInsets );

		Group grpAreaWithin = new Group( cmpContent, SWT.NONE );
		{
			grpAreaWithin.setLayout( new GridLayout( 4, false ) );
			gd = new GridData( GridData.FILL_HORIZONTAL );
			grpAreaWithin.setLayoutData( gd );
			grpAreaWithin.setText( getChart( ) instanceof ChartWithAxes ? Messages.getString( "ChartPlotSheetImpl.Label.AreaWithinAxes" ) : Messages.getString( "ChartPlotSheetImpl.Label.ClientArea" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		createClientArea( grpAreaWithin );
		populateLists( );
		return cmpContent;
	}

	private void updateHeightWidthHint( )
	{
		if ( getBlockForProcessing( ).isSetHeightHint( ) )
		{
			double hint = getBlockForProcessing( ).getHeightHint( );
			if ( hint == AUTO )
			{
				btnHeight.setSelection( true );
				txtHeight.unsetValue( );
				txtHeight.setEnabled( false );
			}
			else
			{
				btnHeight.setSelection( false );
				txtHeight.setValue( hint );
			}
		}
		else
		{
			btnHeight.setSelection( true );
			txtHeight.unsetValue( );
			txtHeight.setEnabled( false );
		}

		if ( getBlockForProcessing( ).isSetWidthHint( ) )
		{
			double hint = getBlockForProcessing( ).getWidthHint( );
			if ( hint == AUTO )
			{
				btnWidth.setSelection( true );
				txtWidth.unsetValue( );
				txtWidth.setEnabled( false );
			}
			else
			{
				btnWidth.setSelection( false );
				txtWidth.setValue( hint );
			}
		}
		else
		{
			btnWidth.setSelection( true );
			txtWidth.unsetValue( );
			txtWidth.setEnabled( false );
		}
	}

	private void createClientArea( Group grpAreaWithin )
	{
		// WithinAxes area is not supported in 3D
		boolean isNot3D = !ChartUIUtil.is3DType( getChart( ) );
		Label lblShadow = new Label( grpAreaWithin, SWT.NONE );
		{
			GridData gdLBLShadow = new GridData( );
			lblShadow.setLayoutData( gdLBLShadow );
			lblShadow.setText( Messages.getString( "ClientAreaAttributeComposite.Lbl.Shadow" ) ); //$NON-NLS-1$
			lblShadow.setEnabled( isNot3D );
		}

		fccShadow = new FillChooserComposite( grpAreaWithin,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getClientArea( ).getShadowColor( ),
				false,
				false );
		{
			GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
			fccShadow.setLayoutData( gdFCCShadow );
			fccShadow.addListener( this );
			fccShadow.setEnabled( isNot3D );
		}

		Group grpOutline = new Group( grpAreaWithin, SWT.NONE );
		{
			GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
			gdGRPOutline.horizontalSpan = 2;
			gdGRPOutline.verticalSpan = 2;
			grpOutline.setLayoutData( gdGRPOutline );
			grpOutline.setLayout( new FillLayout( ) );
			grpOutline.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Outline" ) ); //$NON-NLS-1$
		}

		outlineWithin = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getClientArea( ).getOutline( ),
				true,
				true,
				false );
		{
			outlineWithin.addListener( this );
			outlineWithin.setAttributesEnabled( ChartUIUtil.is3DWallFloorSet( getChart( ) ) );
		}

		icWithin = new InsetsComposite( grpAreaWithin,
				SWT.NONE,
				getBlockForProcessing( ).getClientArea( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		{
			GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
			gdInsets.horizontalSpan = 2;
			icWithin.setLayoutData( gdInsets );
			icWithin.setEnabled( isNot3D );
		}
	}

	private void populateLists( )
	{
		// Set block Anchor property
		NameSet ns = LiteralHelper.anchorSet;
		cmbAnchor.setItems( ns.getDisplayNames( ) );
		cmbAnchor.select( ns.getSafeNameIndex( getBlockForProcessing( ).getAnchor( )
				.getName( ) ) );

		// Set the block Stretch property
		ns = LiteralHelper.stretchSet;
		cmbStretch.setItems( ns.getDisplayNames( ) );
		cmbStretch.select( ns.getSafeNameIndex( getBlockForProcessing( ).getStretch( )
				.getName( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		if ( e.widget.equals( txtHeight ) )
		{
			if ( txtHeight.isSetValue( ) )
			{
				getBlockForProcessing( ).setHeightHint( txtHeight.getValue( ) );
			}
			else
			{
				getBlockForProcessing( ).unsetHeightHint( );
			}
		}
		else if ( e.widget.equals( txtWidth ) )
		{
			if ( txtWidth.isSetValue( ) )
			{
				getBlockForProcessing( ).setWidthHint( txtWidth.getValue( ) );
			}
			else
			{
				getBlockForProcessing( ).unsetWidthHint( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( outlineIncluding ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( event.widget.equals( outlineWithin ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			getBlockForProcessing( ).getClientArea( )
					.setShadowColor( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( iscHSpacing ) )
		{
			getBlockForProcessing( ).setHorizontalSpacing( ( (Integer) event.data ).intValue( ) );
		}
		else if ( event.widget.equals( iscVSpacing ) )
		{
			getBlockForProcessing( ).setVerticalSpacing( ( (Integer) event.data ).intValue( ) );
		}
		else if ( event.widget.equals( icIncluding ) )
		{
			getBlockForProcessing( ).setInsets( (Insets) event.data );
		}
		else if ( event.widget.equals( icWithin ) )
		{
			getBlockForProcessing( ).getClientArea( )
					.setInsets( (Insets) event.data );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( cmbAnchor ) )
		{
			getBlockForProcessing( ).setAnchor( Anchor.getByName( LiteralHelper.anchorSet.getNameByDisplayName( cmbAnchor.getText( ) ) ) );
		}
		else if ( oSource.equals( cmbStretch ) )
		{
			getBlockForProcessing( ).setStretch( Stretch.getByName( LiteralHelper.stretchSet.getNameByDisplayName( cmbStretch.getText( ) ) ) );
		}
		else if ( oSource == btnHeight )
		{
			if ( btnHeight.getSelection( ) )
			{
				getBlockForProcessing( ).setHeightHint( AUTO );
				txtHeight.setEnabled( false );
			}
			else
			{
				txtHeight.setEnabled( true );
				if ( txtHeight.isSetValue( ) )
				{
					getBlockForProcessing( ).setHeightHint( txtHeight.getValue( ) );
				}
				else
				{
					getBlockForProcessing( ).unsetHeightHint( );
				}
			}
		}
		else if ( oSource == btnWidth )
		{
			if ( btnWidth.getSelection( ) )
			{
				getBlockForProcessing( ).setWidthHint( AUTO );
				txtWidth.setEnabled( false );
			}
			else
			{
				txtWidth.setEnabled( true );
				if ( txtWidth.isSetValue( ) )
				{
					getBlockForProcessing( ).setWidthHint( txtWidth.getValue( ) );
				}
				else
				{
					getBlockForProcessing( ).unsetWidthHint( );
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getTitleText()
	 */
	public String getTitleText( )
	{
		return Messages.getString( "AttributeSheetImpl.Title.SheetTitle" ); //$NON-NLS-1$
	}

	private Plot getBlockForProcessing( )
	{
		return getChart( ).getPlot( );
	}
}