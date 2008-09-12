/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * Data binding dialog for Charts
 */

public class ChartColumnBindingDialog extends ColumnBindingDialog
{

	private ChartWizardContext context;
	private Button btnRefresh;

	/** The field indicates if all bindings are read-only in chart. */
	private boolean fIsReadOnly;

	public ChartColumnBindingDialog( ReportItemHandle input, Shell parent,
			ChartWizardContext context )
	{
		super( input, parent, false, true );
		this.context = context;
	}

	protected void handleEditEvent( )
	{
		ComputedColumnHandle bindingHandle = null;
		int pos = bindingTable.getTable( ).getSelectionIndex( );
		if ( pos > -1 )
		{
			bindingHandle = (ComputedColumnHandle) ( DEUtil.getBindingHolder( inputElement ) ).getColumnBindings( )
					.getAt( pos );
		}
		if ( bindingHandle == null )
			return;

		DataColumnBindingDialog dialog = new DataColumnBindingDialog( false );
		dialog.setInput( inputElement, bindingHandle, context );
		dialog.setExpressionProvider( expressionProvider );
		if ( dialog.open( ) == Dialog.OK )
		{
			if ( bindingTable != null )
				bindingTable.getTable( ).setSelection( pos );
		}
	}

	/**
	 * Disable/enable button to make all items in the dialog read-only.
	 * 
	 * @since 2.3
	 */
	private void updateButtonStatusForReadOnly( )
	{
		if ( fIsReadOnly )
		{
			btnAdd.setEnabled( false );
			btnEdit.setEnabled( false );
			btnDel.setEnabled( false );
			getAggregationButton( ).setEnabled( false );
			btnRefresh.setEnabled( false );
		}
	}

	protected int addButtons( Composite cmp, final Table table )
	{
		Listener[] listeners = getAggregationButton( ).getListeners( SWT.Selection );
		if ( listeners.length > 0 )
		{
			getAggregationButton( ).removeListener( SWT.Selection, listeners[0] );
		}
		getAggregationButton( ).addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event event )
			{
				DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
				dialog.setInput( inputElement, null, context );
				dialog.setExpressionProvider( expressionProvider );
				dialog.setAggreate( true );
				if ( dialog.open( ) == Dialog.OK )
				{
					if ( bindingTable != null )
					{
						refreshBindingTable( );
						bindingTable.getTable( )
								.setSelection( bindingTable.getTable( )
										.getItemCount( ) - 1 );
					}
				}

				refreshBindingTable( );
				if ( table.getItemCount( ) > 0 )
					setSelectionInTable( table.getItemCount( ) - 1 );
				updateButtons( );
			}

		} );

		btnRefresh = new Button( cmp, SWT.PUSH );
		btnRefresh.setText( Messages.getString( "ChartColumnBindingDialog.Button.Refresh" ) ); //$NON-NLS-1$

		GridData data = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
		data.widthHint = Math.max( 60, btnRefresh.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnRefresh.setLayoutData( data );
		btnRefresh.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event event )
			{
				try
				{
					List<ComputedColumn> columnList = new ArrayList<ComputedColumn>( );

					CubeHandle cubeHandle = ChartXTabUtil.getBindingCube( inputElement );
					if ( cubeHandle != null )
					{
						if ( inputElement.getCube( ) == null )
						{
							// It inherits bindings from crosstab or sharing
							// query with crosstab, only need to refresh
							// bindings.
							refreshBindingTable( );
						}
						else
						{
							// It uses cube set, needs to added available new
							// dimension or measure to current report item as
							// bindings.
							
							// Add levels
							List<LevelHandle> levels = ChartXTabUtil.getAllLevels( cubeHandle );
							for ( Iterator<LevelHandle> iter = levels.iterator( ); iter.hasNext( ); )
							{
								LevelHandle levelHandle = iter.next( );
								ComputedColumn column = StructureFactory.newComputedColumn( inputElement,
										ChartXTabUtil.createLevelBindingName( levelHandle ) );
								column.setDataType( levelHandle.getDataType( ) );
								column.setExpression( ChartXTabUtil.createDimensionExpression( levelHandle ) );
								columnList.add( column );
							}
							// Add measures
							List<MeasureHandle> measures = ChartXTabUtil.getAllMeasures( cubeHandle );
							for ( Iterator<MeasureHandle> iter = measures.iterator( ); iter.hasNext( ); )
							{
								MeasureHandle measureHandle = iter.next( );
								ComputedColumn column = StructureFactory.newComputedColumn( inputElement,
										ChartXTabUtil.createMeasureBindingName( measureHandle ) );
								column.setDataType( measureHandle.getDataType( ) );
								column.setExpression( ExpressionUtil.createJSMeasureExpression( measureHandle.getName( ) ) );
								column.setAggregateFunction( measureHandle.getFunction( ) );
								columnList.add( column );
							}

							if ( columnList.size( ) > 0 )
							{
								for ( Iterator<ComputedColumn> iter = columnList.iterator( ); iter.hasNext( ); )
								{
									DEUtil.addColumn( inputElement,
											iter.next( ),
											false );
								}
							}
						}
					}
					else
					{

						DataSetHandle dataSetHandle = inputElement.getDataSet( );

						if ( dataSetHandle == null )
						{
							// It inherits bindings from table or sharing query
							// with table, only need to refresh bindings.
							refreshBindingTable( );
						}
						else
						{
							// It uses data set, needs to added available new
							// computed columns of data set to current report
							// item as new bindings.
							List resultSetColumnList = DataUtil.getColumnList( dataSetHandle );
							for ( Iterator iterator = resultSetColumnList.iterator( ); iterator.hasNext( ); )
							{
								ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) iterator.next( );
								ComputedColumn column = StructureFactory.newComputedColumn( inputElement,
										resultSetColumn.getColumnName( ) );
								column.setDataType( resultSetColumn.getDataType( ) );
								column.setExpression( DEUtil.getExpression( resultSetColumn ) );
								columnList.add( column );
							}

							if ( columnList.size( ) > 0 )
							{
								for ( Iterator<ComputedColumn> iter = columnList.iterator( ); iter.hasNext( ); )
								{
									DEUtil.addColumn( inputElement,
											iter.next( ),
											false );
								}
							}
						}
					}
					bindingTable.setInput( inputElement );
				}
				catch ( SemanticException e )
				{
					WizardBase.displayException( e );
				}
			}
		} );

		// Return the number of buttons
		return 2;
	}

	protected void updateButtons( )
	{
		super.updateButtons( );
		getAggregationButton( ).setEnabled( btnAdd.isEnabled( ) );

		updateButtonStatusForReadOnly( );
	}

	protected void addBinding( ComputedColumn column )
	{
		try
		{
			DEUtil.addColumn( inputElement, column, true );
		}
		catch ( SemanticException e )
		{
			WizardBase.showException( e.getLocalizedMessage( ) );
		}
	}

	protected List getBindingList( DesignElementHandle inputElement )
	{
		Iterator iterator = ChartReportItemUtil.getColumnDataBindings( (ReportItemHandle) inputElement );
		List list = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			list.add( iterator.next( ) );
		}
		return list;
	}

	protected void setShellStyle( int newShellStyle )
	{
		super.setShellStyle( newShellStyle
				| SWT.DIALOG_TRIM
				| SWT.RESIZE
				| SWT.APPLICATION_MODAL );
	}

	/**
	 * Set read-only flag.
	 * 
	 * @param isReadOnly
	 * @since 2.3
	 */
	public void setReadOnly( boolean isReadOnly )
	{
		fIsReadOnly = isReadOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog#
	 * setDialogInput
	 * (org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog
	 * , org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	@Override
	protected void setDialogInput( DataColumnBindingDialog dialog,
			ComputedColumnHandle bindingHandle )
	{
		if ( dialog != null )
		{
			dialog.setInput( inputElement, bindingHandle, context );
		}
	}
}
