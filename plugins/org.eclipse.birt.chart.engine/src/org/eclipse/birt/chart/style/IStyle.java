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

package org.eclipse.birt.chart.style;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;

/**
 * IStyle
 */
public interface IStyle
{

	/**
	 * Returns the font of current style.
	 * 
	 * @return
	 */
	FontDefinition getFont( );

	/**
	 * Returns the color of current style.
	 * 
	 * @return
	 */
	ColorDefinition getColor( );

	/**
	 * Returns the background color of current style.
	 * 
	 * @return
	 */
	ColorDefinition getBackgroundColor( );

	/**
	 * Returns the background image of current style.
	 * 
	 * @return
	 */
	Image getBackgroundImage( );

	/**
	 * Returns the padding of current style.
	 * 
	 * @return
	 */
	Insets getPadding( );
}
