/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup;

import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class InteractivitySheet extends AbstractPopupSheet
{

	private final EList triggers;
	private final boolean bEnableURLParameters;
	private final boolean bEnableShowTooltipValue;
	private final int iInteractivityType;

	/**
	 * 
	 * @param title
	 * @param context
	 * @param triggers
	 * @param iInteractivityType
	 *            see <code>TriggerSupportMatrix</code>
	 * @param bEnableURLParameters
	 * @param bEnableShowTooltipValue
	 */
	public InteractivitySheet( String title, ChartWizardContext context,
			EList triggers, int iInteractivityType,
			boolean bEnableURLParameters, boolean bEnableShowTooltipValue )
	{
		super( title, context, false );
		this.triggers = triggers;
		this.bEnableURLParameters = bEnableURLParameters;
		this.bEnableShowTooltipValue = bEnableShowTooltipValue;
		this.iInteractivityType = iInteractivityType;
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_INTERACTIVITY );
		final TriggerDataComposite triggerUI = new TriggerDataComposite( parent,
				SWT.NONE,
				triggers,
				getContext( ),
				iInteractivityType,
				bEnableURLParameters,
				bEnableShowTooltipValue );
		parent.getShell( ).addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				triggerUI.markSaveWhenClosing( );
			}
		} );
		return triggerUI;
	}

}
