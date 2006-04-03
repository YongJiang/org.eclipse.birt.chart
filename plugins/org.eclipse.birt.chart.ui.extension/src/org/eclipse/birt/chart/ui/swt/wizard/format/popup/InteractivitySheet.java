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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup;

import org.eclipse.birt.chart.ui.swt.composites.TriggerEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class InteractivitySheet extends AbstractPopupSheet
{

	private transient EList triggers = null;
	private transient boolean bEnableURLParameters = false;
	private transient boolean bEnableShowTooltipValue = false;

	public InteractivitySheet( String title, ChartWizardContext context,
			EList triggers, boolean bEnableURLParameters,
			boolean bEnableShowTooltipValue )
	{
		super( title, context, false );
		this.triggers = triggers;
		this.bEnableURLParameters = bEnableURLParameters;
		this.bEnableShowTooltipValue = bEnableShowTooltipValue;
	}

	protected Composite getComponent( Composite parent )
	{
		return new TriggerEditorComposite( parent,
				SWT.NONE,
				triggers,
				getContext( ),
				bEnableURLParameters,
				bEnableShowTooltipValue );
	}

}
