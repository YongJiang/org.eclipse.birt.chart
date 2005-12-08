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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionsImpl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.internal.CustomPreviewTable;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class SelectDataDynamicArea implements ISelectDataCustomizeUI
{

	private transient ITask task = null;
	private transient Hashtable htSeriesUIProviders = null;

	private transient CustomPreviewTable customTable = null;

	protected transient List subLeftAreas = new ArrayList( );
	protected transient List subRightAreas = new ArrayList( );

	protected transient Composite cmpLeftArea = null;
	protected transient Composite cmpRightArea = null;
	protected transient Composite cmpBottomArea = null;

	protected transient ISelectDataComponent bottomArea;

	private transient int[] seriesIndex = new int[0];

	public SelectDataDynamicArea( ITask task )
	{
		this.task = task;
	}

	protected ChartWizardContext getContext( )
	{
		return (ChartWizardContext) task.getContext( );
	}

	protected Chart getChartModel( )
	{
		return getContext( ).getModel( );
	}

	public void selectLeftBindingArea( boolean selected, Object data )
	{
		for ( int i = 0; i < subLeftAreas.size( ); i++ )
		{
			( (ISelectDataComponent) subLeftAreas.get( i ) ).selectArea( selected,
					data );
		}
	}

	public void selectRightBindingArea( boolean selected, Object data )
	{
		for ( int i = 0; i < subRightAreas.size( ); i++ )
		{
			( (ISelectDataComponent) subRightAreas.get( i ) ).selectArea( selected,
					data );
		}
	}

	public void selectBottomBindingArea( boolean selected, Object data )
	{
		bottomArea.selectArea( selected, data );
	}

	public void dispose( )
	{
		List list = subLeftAreas;
		list.addAll( subRightAreas );
		for ( int i = 0; i < list.size( ); i++ )
		{
			( (ISelectDataComponent) list.get( i ) ).dispose( );
		}
		bottomArea.dispose( );
		list.clear( );
	}

	public ISelectDataComponent getAreaComponent( int areaType,
			SeriesDefinition seriesdefinition, IUIServiceProvider builder,
			Object oContext, String sTitle )
	{
		if ( this.htSeriesUIProviders == null )
		{
			htSeriesUIProviders = new Hashtable( );
			initSeriesDataUIProviders( );
		}
		return ( (ISeriesUIProvider) htSeriesUIProviders.get( seriesdefinition.getDesignTimeSeries( )
				.getClass( )
				.getName( ) ) ).getSeriesDataComponent( areaType,
				seriesdefinition,
				builder,
				oContext,
				sTitle );
	}

	private void initSeriesDataUIProviders( )
	{
		// Get collection of registered UI Providers
		Collection cRegisteredEntries = ChartUIExtensionsImpl.instance( )
				.getSeriesUIComponents( );
		Iterator iterEntries = cRegisteredEntries.iterator( );
		while ( iterEntries.hasNext( ) )
		{
			ISeriesUIProvider provider = (ISeriesUIProvider) iterEntries.next( );
			String sSeries = provider.getSeriesClass( );
			htSeriesUIProviders.put( sSeries, provider );
		}
	}

	public void refreshLeftBindingArea( )
	{
		subLeftAreas.clear( );
		Composite cmpContainer = cmpLeftArea.getParent( );
		cmpLeftArea.dispose( );
		createLeftBindingArea( cmpContainer );

		cmpContainer.layout( );
	}

	public void refreshRightBindingArea( )
	{
		subRightAreas.clear( );
		Composite cmpContainer = cmpRightArea.getParent( );
		cmpRightArea.dispose( );
		createRightBindingArea( cmpContainer );

		cmpContainer.layout( );
	}

	public void refreshBottomBindingArea( )
	{
		Composite cmpContainer = cmpBottomArea.getParent( );
		cmpBottomArea.dispose( );
		createBottomBindingArea( cmpContainer );

		cmpContainer.layout( );
	}

	public void createLeftBindingArea( Composite parent )
	{
		cmpLeftArea = ChartUIUtil.createCompositeWrapper( parent );
		{
			cmpLeftArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_CENTER ) );
		}

		if ( getChartModel( ) instanceof ChartWithAxes )
		{
			int axisNum = ChartUIUtil.getOrthogonalAxisNumber( getChartModel( ) );
			EList[] seriesDefnArray = new EList[axisNum];
			EList axisList = getYAxisListForProcessing( );
			if ( axisList != null && !axisList.isEmpty( ) )
			{
				for ( int i = 0; i < axisList.size( ); i++ )
				{
					seriesDefnArray[i] = ( (Axis) axisList.get( i ) ).getSeriesDefinitions( );
				}
			}
			ISelectDataComponent component = new MultipleSeriesSelectorComponent( getChartModel( ),
					seriesDefnArray,
					getContext( ).getUIServiceProvider( ),
					getContext( ).getExtendedItem( ),
					"", //$NON-NLS-1$
					this );
			subLeftAreas.add( component );
			component.createArea( cmpLeftArea );
		}
		else
		{
			MultipleSeriesSelectorComponent component = new MultipleSeriesSelectorComponent( getChartModel( ),
					getValueSeriesDefinitionForProcessing( ),
					getContext( ).getUIServiceProvider( ),
					getContext( ).getExtendedItem( ),
					"", //$NON-NLS-1$
					this );
			if ( getChartModel( ) instanceof DialChart )
			{
				component.setAreaTitle( Messages.getString( "DialBottomAreaComponent.Label.GaugeValueDefinition" ) ); //$NON-NLS-1$
			}
			else
			{
				component.setAreaTitle( Messages.getString( "PieLeftAreaComponent.Label.SliceSizeDefinition" ) ); //$NON-NLS-1$
			}
			component.createArea( cmpLeftArea );
			subLeftAreas.add( component );
		}
	}

	public void createRightBindingArea( Composite parent )
	{
		cmpRightArea = ChartUIUtil.createCompositeWrapper( parent );
		cmpRightArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_CENTER ) );

		if ( getChartModel( ) instanceof ChartWithAxes )
		{
			int axisNum = ChartUIUtil.getOrthogonalAxisNumber( getChartModel( ) );
			EList[] seriesDefnArray = new EList[axisNum];
			EList axisList = getYAxisListForProcessing( );
			if ( axisList != null && !axisList.isEmpty( ) )
			{
				for ( int i = 0; i < axisList.size( ); i++ )
				{
					seriesDefnArray[i] = ( (Axis) axisList.get( i ) ).getSeriesDefinitions( );
				}
			}
			ISelectDataComponent component = new MultipleSeriesComponent( seriesDefnArray,
					getContext( ).getUIServiceProvider( ),
					getContext( ).getExtendedItem( ),
					Messages.getString( "AbstractSelectDataCustomizeUI.Label.SeriesGrouping" ), this ); //$NON-NLS-1$
			subRightAreas.add( component );
			component.createArea( cmpRightArea );
		}
		else
		{
			ISelectDataComponent component = new MultipleSeriesComponent( getValueSeriesDefinitionForProcessing( ),
					getContext( ).getUIServiceProvider( ),
					getContext( ).getExtendedItem( ),
					Messages.getString( "AbstractSelectDataCustomizeUI.Label.SeriesGrouping" ), this ); //$NON-NLS-1$
			subRightAreas.add( component );
			component.createArea( cmpRightArea );
		}
	}

	private EList getYAxisListForProcessing( )
	{
		Assert.isTrue( getChartModel( ) instanceof ChartWithAxes );
		return ( (Axis) ( (ChartWithAxes) getChartModel( ) ).getAxes( ).get( 0 ) ).getAssociatedAxes( );
	}

	public void createBottomBindingArea( Composite parent )
	{
		bottomArea = getContext( ).getChartType( ).getBaseUI( getChartModel( ),
				this,
				getContext( ).getUIServiceProvider( ),
				getContext( ).getExtendedItem( ),
				"" ); //$NON-NLS-1$
		cmpBottomArea = bottomArea.createArea( parent );
	}

	private SeriesDefinition getBaseSeriesDefinitionForProcessing( )
	{
		return (SeriesDefinition) ( (ChartWithoutAxes) getChartModel( ) ).getSeriesDefinitions( )
				.get( 0 );
	}

	private EList getValueSeriesDefinitionForProcessing( )
	{
		return getBaseSeriesDefinitionForProcessing( ).getSeriesDefinitions( );
	}

	public Object getCustomPreviewTable( )
	{
		return customTable;
	}

	public void setCustomPreviewTable( CustomPreviewTable customTable )
	{
		this.customTable = customTable;
	}

	public void layoutAll( )
	{
		if ( cmpBottomArea != null && !cmpBottomArea.isDisposed( ) )
		{
			cmpBottomArea.getParent( ).getParent( ).layout( );
		}
	}

	public int[] getSeriesIndex( )
	{
		return seriesIndex;
	}

	public void setSeriesIndex( int[] seriesIndex )
	{
		this.seriesIndex = seriesIndex;
	}

	public void init( )
	{
		// Reset selected series index to 0
		seriesIndex = new int[ChartUIUtil.getOrthogonalAxisNumber( getChartModel( ) )];
	}
}
