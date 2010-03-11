/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.ui.ChartFilterFactory;
import org.eclipse.birt.chart.reportitem.ui.ChartReportItemUIUtil;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.ChartPageGenerator;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.FilterModelProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 * The filter delegate will create different filter provider for common and
 * sharing query cases at runtime.
 * 
 * @since 2.3
 */
public class ChartFilterProviderDelegate extends AbstractFilterHandleProvider
{

	private final AbstractFilterHandleProvider baseProvider;

	public ChartFilterProviderDelegate(
			AbstractFilterHandleProvider baseProvider )
	{
		this.baseProvider = baseProvider;
		this.input = baseProvider.getInput( );
	}

	@Override
	public List<Object> getContentInput( )
	{
		return baseProvider.getContentInput( );
	}

	@Override
	public FilterModelProvider getModelAdapter( )
	{
		return baseProvider.getModelAdapter( );
	}

	@Override
	public ParamBindingHandle[] getBindingParams( )
	{
		return baseProvider.getBindingParams( );
	}

	@Override
	public void setBindingParams( ParamBindingHandle[] bindingParams )
	{
		this.bindingParams = bindingParams;
		baseProvider.setBindingParams( bindingParams );
	}

	@Override
	public void setContentInput( List<Object> contentInput )
	{
		this.contentInput = contentInput;
		baseProvider.setContentInput( contentInput );
	}

	@Override
	public void setModelAdapter( FilterModelProvider modelAdapter )
	{
		this.modelAdapter = modelAdapter;
		baseProvider.setModelAdapter( modelAdapter );
	}

	@Override
	public IFormProvider getConcreteFilterProvider( )
	{
		return baseProvider.getConcreteFilterProvider( );
	}

	public boolean doAddItem( int pos ) throws Exception
	{
		return baseProvider.doAddItem( pos );
	}

	public boolean doDeleteItem( int pos ) throws Exception
	{
		return baseProvider.doDeleteItem( pos );
	}

	public boolean doEditItem( int pos )
	{
		return baseProvider.doEditItem( pos );
	}

	public String[] getColumnNames( )
	{
		return baseProvider.getColumnNames( );
	}

	public String getColumnText( Object element, int columnIndex )
	{
		return baseProvider.getColumnText( element, columnIndex );
	}

	public int[] getColumnWidths( )
	{
		return baseProvider.getColumnWidths( );
	}

	public CellEditor[] getEditors( Table table )
	{
		return baseProvider.getEditors( table );
	}

	public Object[] getElements( Object inputElement )
	{
		return baseProvider.getElements( inputElement );
	}

	public Object getValue( Object element, String property )
	{
		return baseProvider.getValue( element, property );
	}

	public String getDisplayName( )
	{
		return baseProvider.getDisplayName( );
	}

	@Override
	public boolean needRebuilded( NotificationEvent event )
	{
		return baseProvider.needRebuilded( event );
	}

	@Override
	public boolean doMoveItem( int oldPos, int newPos ) throws Exception
	{
		return baseProvider.doMoveItem( oldPos, newPos );
	}

	@Override
	public Image getImage( Object element, int columnIndex )
	{
		return baseProvider.getImage( element, columnIndex );
	}

	@Override
	public boolean isAddEnable( )
	{
		return baseProvider.isAddEnable( );
	}

	@Override
	public boolean isDeleteEnable( )
	{
		return baseProvider.isDeleteEnable( );
	}

	@Override
	public boolean isDownEnable( )
	{
		return baseProvider.isDownEnable( );
	}

	@Override
	public boolean isEditable( )
	{
		return baseProvider.isEditable( );
	}

	@Override
	public boolean isEditEnable( )
	{
		return baseProvider.isEditEnable( );
	}

	@Override
	public boolean isEnable( )
	{
		return baseProvider.isEnable( );
	}

	@Override
	public boolean isUpEnable( )
	{
		return baseProvider.isUpEnable( );
	}

	@Override
	public Object load( )
	{
		return baseProvider.load( );
	}

	@Override
	public boolean modify( Object data, String property, Object value )
			throws Exception
	{
		return baseProvider.modify( data, property, value );
	}

	@Override
	public boolean needRefreshed( NotificationEvent event )
	{
		return baseProvider.needRefreshed( event );
	}

	@Override
	public void save( Object value ) throws SemanticException
	{
		baseProvider.save( value );
	}

	@Override
	public void setInput( Object input )
	{
		baseProvider.setInput( input );
		this.input = input;
	}

	@Override
	public Object getInput( )
	{
		return baseProvider.getInput( );
	}

	/**
	 * Create filter provider by specified input.
	 * 
	 * @param input
	 * @param providerInput
	 * @return the created filter provider
	 * @since 2.3
	 */
	public static AbstractFilterHandleProvider createFilterProvider(
			Object input, Object providerInput )
	{
		AbstractFilterHandleProvider currentProvider = null;

		Object handle = null;
		if ( input instanceof List<?> )
		{
			handle = ( (List<?>) input ).get( 0 );
		}
		else
		{
			handle = input;
		}

		AbstractFilterHandleProvider baseProvider = getAdapter( new ChartPageGenerator( ),
				AbstractFilterHandleProvider.class );
		if ( baseProvider == null )
		{
			baseProvider = new FilterHandleProvider( );
		}

		if ( handle instanceof ReportItemHandle
				&& ChartCubeUtil.getBindingCube( (DesignElementHandle) handle ) != null )

		{
			// It is in cube mode.
			if ( ( (ReportItemHandle) handle ).getCube( ) != null
					&& ( ChartItemUtil.isChildOfMultiViewsHandle( (DesignElementHandle) handle ) || ( (ReportItemHandle) handle ).getDataBindingReference( ) != null ) )
			{
				// Sharing crosstab/multi-view
				ReportItemHandle ref = ChartReportItemUtil.getReportItemReference( (ReportItemHandle) handle );
				try
				{
					ChartFilterFactory cff = ChartReportItemUIUtil.createChartFilterFactory( handle );

					if ( cff.isChartHandle( ref ) )
					{
						currentProvider = new ChartShareCubeFiltersHandleProvider( new FilterHandleProvider( ) );
					}
					else
					{
						currentProvider = new ChartShareCrosstabFiltersHandleProvider( );
					}
				}
				catch ( ExtendedElementException e )
				{
					ChartWizard.displayException( e );
					currentProvider = new ChartCubeFilterHandleProvider( new FilterHandleProvider( ) );
				}
			}
			else
			{
				currentProvider = new ChartCubeFilterHandleProvider( new FilterHandleProvider( ) );
			}
		}

		else
		{
			// It is in table mode.
			if ( ChartItemUtil.isChildOfMultiViewsHandle( (DesignElementHandle) handle ) )
			{
				// Chart is in multi-view.
				currentProvider = new ChartShareFiltersHandleProvider( baseProvider );
			}
			else
			{
				currentProvider = new ChartFilterHandleProvider( baseProvider );
			}
		}

		if ( input != null )
		{
			currentProvider.setInput( input );
		}

		return currentProvider;
	}

	private static <T> T getAdapter( Object adaptable, Class<T> type )
	{
		return type.cast( ElementAdapterManager.getAdapter( adaptable, type ) );
	}

	@Override
	public boolean canModify( Object element, String property )
	{
		return baseProvider.canModify( element, property );
	}

	@Override
	public boolean isReadOnly( )
	{
		return baseProvider.isReadOnly( );
	}

	@Override
	public void setReadOnly( boolean isReadOnly )
	{
		baseProvider.setReadOnly( isReadOnly );
	}

	@Override
	public void add( int pos ) throws Exception
	{
		baseProvider.add( pos );
	}

	@Override
	public boolean edit( int pos )
	{
		return baseProvider.edit( pos );
	}

	@Override
	public FormContentProvider getFormContentProvider(
			IModelEventProcessor listener, IDescriptorProvider provider )
	{
		return baseProvider.getFormContentProvider( listener, provider );
	}

	@Override
	public void transModify( Object data, String property, Object value )
			throws Exception
	{
		baseProvider.transModify( data, property, value );
	}

	@Override
	public boolean canReset( )
	{
		return baseProvider.canReset( );
	}

	@Override
	public void enableReset( boolean canReset )
	{
		baseProvider.enableReset( canReset );
	}

	@Override
	public void reset( ) throws SemanticException
	{
		baseProvider.reset( );
	}
}
