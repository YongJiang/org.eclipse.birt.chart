/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;

/**
 * UI helper used to help chart UI customize composites in it.
 */

public interface IChartUIHelper
{

	/**
	 * Returns if default title is supported and if it should be visible in
	 * title UI section.
	 * 
	 * @return true means visible in title UI section
	 */
	boolean isDefaultTitleSupported( );

	/**
	 * Returns current value of default title.
	 * 
	 * @param context
	 *            wizard context
	 * @return current value of default title.
	 */
	String getDefaultTitle( ChartWizardContext context );

	/**
	 * Updates the title in chart model with current default title value.
	 * 
	 * @param cm
	 *            chart model
	 * @param extendedItem
	 *            extended item object
	 */
	void updateDefaultTitle( Chart cm, Object extendedItem );
}
