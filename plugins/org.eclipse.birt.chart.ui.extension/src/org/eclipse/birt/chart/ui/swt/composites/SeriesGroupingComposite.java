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

import org.eclipse.birt.chart.exception.ChartException;
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class SeriesGroupingComposite extends Composite implements
		SelectionListener
{

	private transient Group grpContent = null;

	private transient Button btnEnabled = null;

	private transient Label lblType = null;

	private transient Combo cmbType = null;

	private transient Label lblUnit = null;

	private transient Combo cmbUnit = null;

	private transient Label lblInterval = null;

	private transient Text iscInterval = null;

	private transient Label lblAggregate = null;

	private transient Combo cmbAggregate = null;

	private transient SeriesDefinition sd = null;

	private transient boolean bTypeEnabled = true;

	private transient boolean fbAggEnabled = true;
	
	/**
	 * @param parent
	 * @param style
	 */
	public SeriesGroupingComposite( Composite parent, int style,
			SeriesDefinition sd, boolean bTypeEnabled )
	{
		this(parent, style, sd, bTypeEnabled, true);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public SeriesGroupingComposite( Composite parent, int style,
			SeriesDefinition sd, boolean bTypeEnabled, boolean bAggEnabled )
	{
		super( parent, style );
		this.sd = sd;
		this.bTypeEnabled = bTypeEnabled;
		this.fbAggEnabled = bAggEnabled;
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

		double iGroupInterval = 2;
		if ( sd.getGrouping( ) != null )
		{
			iGroupInterval = sd.getGrouping( ).getGroupingInterval( );
		}

		iscInterval = new Text( grpContent, SWT.BORDER );
		GridData gdISCInterval = new GridData( GridData.FILL_HORIZONTAL );
		iscInterval.setLayoutData( gdISCInterval );
		iscInterval.setToolTipText( Messages.getString( "SeriesGroupingComposite.Tooltip.SelectIntervalForGrouping" ) ); //$NON-NLS-1$
		if ( iGroupInterval - (long)iGroupInterval == 0 ) {
			iscInterval.setText( String.valueOf( (long)iGroupInterval ) );
		}
		else
		{
			iscInterval.setText( String.valueOf( iGroupInterval ) );
		}
		iscInterval.addSelectionListener( this );
		iscInterval.addFocusListener( new FocusListener() {

			public void focusGained( FocusEvent e )
			{
				// TODO Auto-generated method stub
				
			}

			public void focusLost( FocusEvent e )
			{
				String text = iscInterval.getText( );
				if ( text == null || text.trim( ).length( ) == 0 ) {
					text = "0"; //$NON-NLS-1$
				}
				getGrouping( ).setGroupingInterval( Double.valueOf( text ).doubleValue( ) );				
			}
			
		});
		iscInterval.addVerifyListener( new VerifyListener() {

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events.VerifyEvent)
			 */
			public void verifyText( VerifyEvent e )
			{
				// Check if current format of text is correct, only allow "9999.99" format.
				String text = ((Text)e.getSource( )).getText( );
				if ( e.text != null && e.text.length( ) > 0) {
					StringBuffer sb = new StringBuffer();
					sb.append( text.substring( 0, e.start ) );
					sb.append( e.text );
					sb.append( text.substring( e.start ));
					text = sb.toString( );
				}
				if ( text != null && text.length( ) > 0 && !text.matches( "[0-9]*[.]?[0-9]*" ) ) { //$NON-NLS-1$
					e.doit = false;
				}
			}
			
		});

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

		if ( fbAggEnabled )
		{
		Composite cmpAggregate = new Composite( grpContent, SWT.NONE );
		GridData gdCMPAggregate = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPAggregate.horizontalSpan = 2;
		cmpAggregate.setLayoutData( gdCMPAggregate );
		cmpAggregate.setLayout( glAggregate );

		lblAggregate = new Label( cmpAggregate, SWT.NONE );
		GridData gdLBLAggregate = new GridData( );
		lblAggregate.setLayoutData( gdLBLAggregate );
		lblAggregate.setText( Messages.getString( "SeriesGroupingComposite.Lbl.AggregateExpression" ) ); //$NON-NLS-1$

		cmbAggregate = new Combo( cmpAggregate, SWT.DROP_DOWN | SWT.READ_ONLY );
		cmbAggregate.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cmbAggregate.addSelectionListener( this );
		}
		populateLists( );
	}

	/**
	 * Populate grouping property items by data type.
	 * 
	 */
	private void populateLists(  )
	{
		cmbUnit.removeAll( );
		
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
		if ( fbAggEnabled )
		{
			try
			{
				cmbAggregate.setItems( PluginSettings.instance( )
						.getRegisteredAggregateFunctionDisplayNames( ) );
				cmbAggregate.setData( PluginSettings.instance( )
						.getRegisteredAggregateFunctions( ) );
			}
			catch ( ChartException e )
			{
				e.printStackTrace( );
			}

			if ( bEnableUI && grouping.getAggregateExpression( ) != null )
			{
				int idx = getAggregateIndexByName( grouping.getAggregateExpression( ) );
				if ( cmbAggregate.getItemCount( ) > idx )
				{
					cmbAggregate.select( idx );
				}
			}
			else if ( cmbAggregate.getItemCount( ) > 0 )
			{
				cmbAggregate.select( 0 );
			}
			lblAggregate.setEnabled( bEnableUI );
			cmbAggregate.setEnabled( bEnableUI );
		}
	}

	private int getAggregateIndexByName( String name )
	{
		if ( fbAggEnabled )
		{
			String[] names = (String[]) cmbAggregate.getData( );

			for ( int i = 0; i < names.length; i++ )
			{
				if ( name.equals( names[i] ) )
				{
					return i;
				}
			}
		}
		return 0;
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
			getGrouping( ).setGroupType( DataType.getByName( LiteralHelper.dataTypeSet.getNameByDisplayName( cmbType.getText( ) ) ) );

			boolean bEnableUI = btnEnabled.getSelection( );
			String selName = LiteralHelper.dataTypeSet.getNameByDisplayName( cmbType.getText( ) );
			boolean bEnabled = DataType.DATE_TIME_LITERAL.getName( )
					.equals( selName );
			
			lblUnit.setEnabled( bEnableUI & bEnabled );
			cmbUnit.setEnabled( bEnableUI & bEnabled );
			lblInterval.setEnabled( bEnableUI );
			iscInterval.setEnabled( bEnableUI );
			
			if ( fbAggEnabled )
			{
				lblAggregate.setEnabled( bEnableUI );
				cmbAggregate.setEnabled( bEnableUI );
			}
		}
		else if ( oSource.equals( cmbUnit ) )
		{
			getGrouping( ).setGroupingUnit( GroupingUnitType.getByName( LiteralHelper.groupingUnitTypeSet.getNameByDisplayName( cmbUnit.getText( ) ) ) );
		}
		else if ( oSource.equals( cmbAggregate ) )
		{
			int idx = cmbAggregate.getSelectionIndex( );
			String aggExpr = null;
			if ( idx >= 0 )
			{
				String[] names = (String[]) cmbAggregate.getData( );
				aggExpr = names[idx];
			}
			getGrouping( ).setAggregateExpression( aggExpr );
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

			// refresh UI
			populateLists( );
		}
		else if ( oSource.equals( iscInterval ) )
		{
			getGrouping( ).setGroupingInterval( Double.valueOf( iscInterval.getText( ) ).doubleValue( ) );
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

	/**
	 * Enable grouping and make it read only.
	 * @see 2.3
	 */
	public void stillEnableGroupingSelection( )
	{
		btnEnabled.setSelection( true );
		btnEnabled.setEnabled(  false );
	}

}