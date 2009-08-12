/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. 
 * All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;

/**
 * BarChart
 */
public class BarChart extends AbstractBarChart
{

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_BAR;

	/**
	 * Constructor of the class.
	 */
	public BarChart( )
	{
		super( "Bar", TYPE_LITERAL, RiserType.RECTANGLE_LITERAL ); //$NON-NLS-1$
	}
}