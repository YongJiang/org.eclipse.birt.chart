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

import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.swt.SWT;
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
 * @author Actuate Corporation
 * 
 */
public class SeriesGroupingComposite extends Composite implements
		SelectionListener,
		Listener
{

	private transient Group grpContent = null;

	private transient Button btnEnabled = null;

	private transient Label lblType = null;

	private transient Combo cmbType = null;

	private transient Label lblUnit = null;

	private transient Combo cmbUnit = null;

	private transient Label lblInterval = null;

	private transient IntegerSpinControl iscInterval = null;

	private transient Label lblAggregate = null;

	private transient Combo cmbAggregate = null;

	private transient SeriesDefinition sd = null;

	private transient boolean bTypeEnabled = true;

	/**
	 * @param parent
	 * @param style
	 */
	public SeriesGroupingComposite( Composite parent, int style,
			SeriesDefinition sd, boolean bTypeEnabled )
	{
		super( parent, style );
		this.sd = sd;
		this.bTypeEnabled = bTypeEnabled;
		init( );
		placeComponents( );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Layout for content composite
		GridLayout glContent = new GridLayout( );
		glContent.numColumns = 4;
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginWidth = 7;
		glContent.marginHeight = 7;

		this.setLayout( new FillLayout( ) );

		// Content composite
		grpContent = new Group( this, SWT.NONE );
		grpContent.setLayout( glContent );
		grpContent.setText( Messages.getString( "SeriesGroupingComposite.Lbl.Grouping" ) ); //$NON-NLS-1$

		btnEnabled = new Button( grpContent, SWT.CHECK );
		GridData gdBTNEnabled = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		gdBTNEnabled.horizontalSpan = 4;
		btnEnabled.setLayoutData( gdBTNEnabled );
		btnEnabled.setText( Messages.getString( "SeriesGroupingComposite.Lbl.Enabled" ) ); //$NON-NLS-1$
		btnEnabled.addSelectionListener( this );
		if ( sd.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Grouping( ) ) )
		{
			btnEnabled.setSelection( getGrouping( ).isEnabled( ) );
		}
		else
		{
			btnEnabled.setSelection( false );
		}

		boolean bEnableUI = btnEnabled.getSelection( );
		lblType = new Label( grpContent, SWT.NONE );
		GridData gdLBLType = new GridData( );
		lblType.setLayoutData( gdLBLType );
		lblType.setText( Messages.getString( "SeriesGroupingComposite.Lbl.Type" ) ); //$NON-NLS-1$
		lblType.setEnabled( bEnableUI & bTypeEnabled );

		cmbType = new Combo( grpContent, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBType = new GridData( GridData.FILL_HORIZONTAL );
		cmbType.setLayoutData( gdCMBType );
		cmbType.addSelectionListener( this );
		cmbType.setEnabled( bEnableUI & bTypeEnabled );

		lblUnit = new Label( grpContent, SWT.NONE );
		GridData gdLBLUnit = new GridData( );
		lblUnit.setLayoutData( gdLBLUnit );
		lblUnit.setText( Messages.getString( "SeriesGroupingComposite.Lbl.Unit" ) ); //$NON-NLS-1$

		cmbUnit = new Combo( grpContent, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBUnit = new GridData( GridData.FILL_HORIZONTAL );
		cmbUnit.setLayoutData( gdCMBUnit );
		cmbUnit.addSelectionListener( this );

		lblInterval = new Label( grpContent, SWT.NONE );
		GridData gdLBLInterval = new GridData( );
		lblInterval.setLayoutData( gdLBLInterval );
		lblInterval.setText( Messages.getString( "SeriesGroupingComposite.Lbl.Interval" ) ); //$NON-NLS-1$

		int iGroupInterval = 2;
		if ( sd.getGrouping( ) != null )
		{
			iGroupInterval = sd.getGrouping( ).getGroupingInterval( );
		}

		iscInterval = new IntegerSpinControl( grpContent,
				SWT.NONE,
				iGroupInterval );
		GridData gdISCInterval = new GridData( );
		gdISCInterval.widthHint = 80;
		iscInterval.setLayoutData( gdISCInterval );
		iscInterval.setMinimum( 1 );
		iscInterval.addListener( this );

		Label lblDummy = new Label( grpContent, SWT.NONE );
		GridData gdLBLDummy = new GridData( GridData.FILL_HORIZONTAL );
		gdLBLDummy.horizontalSpan = 2;
		lblDummy.setLayoutData( gdLBLDummy );

		// Layout for aggregate composite
		GridLayout glAggregate = new GridLayout( );
		glAggregate.numColumns = 2;
		glAggregate.marginHeight = 0;
		glAggregate.marginWidth = 0;
		glAggregate.horizontalSpacing = 5;
		glAggregate.verticalSpacing = 5;

		Composite cmpAggregate = new Composite( grpContent, SWT.NONE );
		GridData gdCMPAggregate = new GridData( );
		gdCMPAggregate.horizontalSpan = 4;
		cmpAggregate.setLayoutData( gdCMPAggregate );
		cmpAggregate.setLayout( glAggregate );

		lblAggregate = new Label( cmpAggregate, SWT.NONE );
		GridData gdLBLAggregate = new GridData( );
		lblAggregate.setLayoutData( gdLBLAggregate );
		lblAggregate.setText( Messages.getString( "SeriesGroupingComposite.Lbl.AggregateExpression" ) ); //$NON-NLS-1$

		cmbAggregate = new Combo( cmpAggregate, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBAggregate = new GridData( );
		cmbAggregate.setLayoutData( gdCMBAggregate );
		cmbAggregate.addSelectionListener( this );

		populateLists( );
	}

	private void populateLists( )
	{
		SeriesGrouping grouping = getGrouping( );

		boolean bEnableUI = btnEnabled.getSelection( );

		// Populate the data type combo
		NameSet ns = LiteralHelper.dataTypeSet;
		cmbType.setItems( ns.getDisplayNames( ) );

		if ( bEnableUI && grouping.getGroupType( ) != null )
		{
			cmbType.setText( ns.getDisplayNameByName( getGrouping( ).getGroupType( )
					.getName( ) ) );
		}
		else
		{
			cmbType.select( 0 );
		}

		this.lblType.setEnabled( bEnableUI );
		this.cmbType.setEnabled( bEnableUI );

		this.lblInterval.setEnabled( bEnableUI );
		this.iscInterval.setEnabled( bEnableUI );

		// Populate grouping unit combo (applicable only if type is DateTime
		ns = LiteralHelper.groupingUnitTypeSet;
		cmbUnit.setItems( ns.getDisplayNames( ) );

		if ( bEnableUI
				&& grouping.getGroupType( ) != null
				&& grouping.getGroupType( ) == DataType.DATE_TIME_LITERAL
				&& grouping.getGroupingUnit( ) != null )
		{
			cmbUnit.setText( ns.getDisplayNameByName( grouping.getGroupingUnit( )
					.getName( ) ) );
		}
		else
		{
			cmbUnit.select( 0 );
		}
		lblUnit.setEnabled( bEnableUI
				&& DataType.DATE_TIME_LITERAL.getName( )
						.equals( LiteralHelper.dataTypeSet.getNameByDisplayName( cmbType.getText( ) ) ) );
		cmbUnit.setEnabled( lblUnit.getEnabled( ) );

		// Populate grouping aggregate expression combo

		cmbAggregate.setItems( PluginSettings.instance( )
				.getRegisteredAggregateFunctions( ) );
		if ( bEnableUI && grouping.getAggregateExpression( ) != null )
		{
			cmbAggregate.setText( grouping.getAggregateExpression( ) );
		}
		else
		{
			cmbAggregate.select( 0 );
		}
		lblAggregate.setEnabled( bEnableUI );
		cmbAggregate.setEnabled( bEnableUI );
	}

	private SeriesGrouping getGrouping( )
	{
		return sd.getGrouping( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( cmbType ) )
		{
			getGrouping( ).setGroupType( DataType.get( LiteralHelper.dataTypeSet.getNameByDisplayName( cmbType.getText( ) ) ) );

			boolean bEnableUI = btnEnabled.getSelection( );
			boolean bDate = DataType.DATE_TIME_LITERAL.getName( )
					.equals( LiteralHelper.dataTypeSet.getNameByDisplayName( cmbType.getText( ) ) );

			lblUnit.setEnabled( bEnableUI & bDate );
			cmbUnit.setEnabled( bEnableUI & bDate );
			lblInterval.setEnabled( bEnableUI );
			iscInterval.setEnabled( bEnableUI );
			lblAggregate.setEnabled( bEnableUI );
			cmbAggregate.setEnabled( bEnableUI );
		}
		else if ( oSource.equals( cmbUnit ) )
		{
			getGrouping( ).setGroupingUnit( GroupingUnitType.get( LiteralHelper.groupingUnitTypeSet.getNameByDisplayName( cmbUnit.getText( ) ) ) );
		}
		else if ( oSource.equals( cmbAggregate ) )
		{
			getGrouping( ).setAggregateExpression( cmbAggregate.getText( ) );
		}
		else if ( oSource.equals( btnEnabled ) )
		{
			SeriesGrouping grp = null;
			if ( !sd.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Grouping( ) ) )
			{
				grp = SeriesGroupingImpl.create( );
				sd.setGrouping( grp );
			}
			else
			{
				grp = sd.getGrouping( );
			}
			grp.setEnabled( btnEnabled.getSelection( ) );
			boolean bEnableUI = btnEnabled.getSelection( );

			lblType.setEnabled( bEnableUI );
			cmbType.setEnabled( bEnableUI );

			boolean bDate = DataType.DATE_TIME_LITERAL.getName( )
					.equals( LiteralHelper.dataTypeSet.getNameByDisplayName( cmbType.getText( ) ) );

			lblUnit.setEnabled( bEnableUI & bDate );
			cmbUnit.setEnabled( bEnableUI & bDate );
			lblInterval.setEnabled( bEnableUI );
			iscInterval.setEnabled( bEnableUI );
			lblAggregate.setEnabled( bEnableUI );
			cmbAggregate.setEnabled( bEnableUI );
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
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( iscInterval ) )
		{
			getGrouping( ).setGroupingInterval( ( (Integer) event.data ).intValue( ) );
		}
	}

}