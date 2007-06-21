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

import org.eclipse.birt.chart.reportitem.ui.ReportItemUIUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
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

	public ChartColumnBindingDialog( Shell parent )
	{
		super( parent, false, false );
	}

	protected int addButtons( Composite cmp, final Table table )
	{
		Button btnAddAgg = new Button( cmp, SWT.PUSH );
		btnAddAgg.setText( Messages.getString( "ChartColumnBindingDialog.Button.AddAggregation" ) ); //$NON-NLS-1$
		GridData data = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
		data.widthHint = Math.max( 60, btnAddAgg.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnAddAgg.setLayoutData( data );
		btnAddAgg.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event event )
			{
				DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
				dialog.setInput( inputElement );
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

		// Return the number of buttons
		return 1;
	}

	protected void addBinding( ComputedColumn column )
	{
		try
		{
			DEUtil.addColumn( inputElement, column, true );
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	protected List getBindingList( DesignElementHandle inputElement )
	{
		Iterator iterator = ReportItemUIUtil.getColumnDataBindings( (ReportItemHandle) inputElement );
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
				| SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL );
	}
}