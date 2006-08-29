/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractScaleSheet;

/**
 * 
 */

public class DialScaleSheet extends AbstractScaleSheet
{

	private DialSeries series;

	public DialScaleSheet( String title, ChartWizardContext context,
			DialSeries series )
	{
		super( title, context );
		this.series = series;
	}

	protected Scale getScale( )
	{
		return series.getDial( ).getScale( );
	}

	protected int getValueType( )
	{
		return TextEditorComposite.TYPE_NUMBERIC;
	}

}
