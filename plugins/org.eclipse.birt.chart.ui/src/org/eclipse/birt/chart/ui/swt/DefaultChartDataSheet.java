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

package org.eclipse.birt.chart.ui.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class DefaultChartDataSheet implements IChartDataSheet
{

	private List listeners = new ArrayList( 2 );
	private Chart cm;
	private ChartWizardContext context;

	public void addListener( Listener listener )
	{
		if ( !listeners.contains( listener ) )
		{
			listeners.add( listener );
		}
	}

	public Composite createActionButtons( Composite parent )
	{
		return new Composite( parent, SWT.NONE );
	}

	public Composite createDataDragSource( Composite parent )
	{
		return new Composite( parent, SWT.NONE );
	}

	public Composite createDataSelector( Composite parent )
	{
		return new Composite( parent, SWT.NONE );
	}

	public void removeListener( Listener listener )
	{
		listeners.remove( listener );
	}

	public void notifyListeners( Event event )
	{
		for ( Iterator iterator = listeners.iterator( ); iterator.hasNext( ); )
		{
			( (Listener) iterator.next( ) ).handleEvent( event );
		}
	}

	public void dispose( )
	{
		listeners.clear( );
	}

	public void setChartModel( Chart cm )
	{
		this.cm = cm;

	}

	protected Chart getChartModel( )
	{
		return this.cm;
	}

	public void setContext( IWizardContext context )
	{
		assert context instanceof ChartWizardContext;
		this.context = (ChartWizardContext) context;
	}

	protected ChartWizardContext getContext( )
	{
		return this.context;
	}

}