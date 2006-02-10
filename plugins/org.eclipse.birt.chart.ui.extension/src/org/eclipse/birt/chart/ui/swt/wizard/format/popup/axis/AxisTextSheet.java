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

import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class AxisTextSheet extends AbstractPopupSheet
		implements
			SelectionListener,
			Listener
{

	private transient Composite cmpContent = null;

	private transient LabelAttributesComposite lacTitle = null;

	private transient LabelAttributesComposite lacLabel = null;

	private transient Button cbStaggered;

	private transient Axis axis;

	private transient int axisType;

	public AxisTextSheet( Composite parent, ChartWizardContext context,
			Axis axis, int axisType )
	{
		super( parent, context, true );
		this.axis = axis;
		this.axisType = axisType;
		cmpTop = getComponent( parent );
	}

	protected Composite getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glMain = new GridLayout( );
			glMain.numColumns = 2;
			glMain.horizontalSpacing = 5;
			glMain.verticalSpacing = 5;
			glMain.marginHeight = 7;
			glMain.marginWidth = 7;
			cmpContent.setLayout( glMain );
		}

		if ( axisType == AngleType.Z )
		{
			lacTitle = new LabelAttributesComposite( cmpContent,
					SWT.NONE,
					Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Title" ),//$NON-NLS-1$
					getAxisForProcessing( ).getTitlePosition( ),
					getAxisForProcessing( ).getTitle( ),
					chart.getUnits( ),
					false,
					true,
					serviceprovider,
					true );
		}
		else
		{
			lacTitle = new LabelAttributesComposite( cmpContent,
					SWT.NONE,
					Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Title" ),//$NON-NLS-1$
					getAxisForProcessing( ).getTitlePosition( ),
					getAxisForProcessing( ).getTitle( ),
					chart.getUnits( ),
					true,
					true,
					serviceprovider,
					getPositionScope( ),
					true );
		}
		GridData gdLACTitle = new GridData( GridData.FILL_HORIZONTAL );
		gdLACTitle.widthHint = 200;
		lacTitle.setLayoutData( gdLACTitle );
		lacTitle.addListener( this );

		Group grpLabel = new Group( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( );
			layout.marginWidth = 0;
			layout.verticalSpacing = 0;
			grpLabel.setLayout( layout );
			grpLabel.setText( Messages.getString( "BaseAxisLabelAttributeSheetImpl.Lbl.Label" ) ); //$NON-NLS-1$
		}

		cbStaggered = new Button( grpLabel, SWT.CHECK );
		{
			GridData gd = new GridData( );
			gd.horizontalIndent = 10;
			// gd.verticalIndent = 10;
			cbStaggered.setLayoutData( gd );
			cbStaggered.setSelection( getAxisForProcessing( ).isSetStaggered( )
					&& getAxisForProcessing( ).isStaggered( ) );
			cbStaggered.setText( Messages.getString( "AxisTextSheet.Label.StaggerLabels" ) ); //$NON-NLS-1$
			cbStaggered.addSelectionListener( this );
			cbStaggered.setEnabled( getAxisForProcessing( ).getLabel( )
					.isVisible( ) );
		}

		if ( axisType == AngleType.Z )
		{
			lacLabel = new LabelAttributesComposite( grpLabel,
					SWT.NONE,
					null,// Replace group with composite
					getAxisForProcessing( ).getLabelPosition( ),
					getAxisForProcessing( ).getLabel( ),
					chart.getUnits( ),
					false,
					false,
					serviceprovider,
					false );
		}
		else
		{
			lacLabel = new LabelAttributesComposite( grpLabel,
					SWT.NONE,
					null,// Replace group with composite
					getAxisForProcessing( ).getLabelPosition( ),
					getAxisForProcessing( ).getLabel( ),
					chart.getUnits( ),
					true,
					false,
					serviceprovider,
					getPositionScope( ),
					false );
		}
		GridData gdLACLabel = new GridData( GridData.FILL_HORIZONTAL );
		// gdLACLabel.verticalIndent = -7;
		gdLACLabel.widthHint = 200;
		lacLabel.setLayoutData( gdLACLabel );
		lacLabel.addListener( this );
		lacLabel.setEnabled( getAxisForProcessing( ).getLabel( ).isVisible( ) );

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( lacTitle ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					getAxisForProcessing( ).setTitlePosition( (Position) event.data );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getAxisForProcessing( ).getTitle( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getAxisForProcessing( ).getTitle( )
							.setInsets( (Insets) event.data );
					break;
			}
		}
		else if ( event.widget.equals( lacLabel ) )
		{
			switch ( event.type )
			{
				case LabelAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.POSITION_CHANGED_EVENT :
					getAxisForProcessing( ).setLabelPosition( (Position) event.data );
					break;
				case LabelAttributesComposite.FONT_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getCaption( )
							.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
					getAxisForProcessing( ).getLabel( )
							.getCaption( )
							.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
					break;
				case LabelAttributesComposite.BACKGROUND_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.setBackground( (Fill) event.data );
					break;
				case LabelAttributesComposite.SHADOW_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.setShadowColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_STYLE_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_WIDTH_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LabelAttributesComposite.OUTLINE_COLOR_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LabelAttributesComposite.OUTLINE_VISIBILITY_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
				case LabelAttributesComposite.INSETS_CHANGED_EVENT :
					getAxisForProcessing( ).getLabel( )
							.setInsets( (Insets) event.data );
					break;
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
		if ( e.getSource( ).equals( cbStaggered ) )
		{
			getAxisForProcessing( ).setStaggered( cbStaggered.getSelection( ) );
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

	private int getPositionScope( )
	{
		// Vertical position for X axis
		if ( axisType == AngleType.X )
		{
			return LabelAttributesComposite.ALLOW_VERTICAL_POSITION;
		}
		return LabelAttributesComposite.ALLOW_HORIZONTAL_POSITION;
	}

}
