/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import java.util.Locale;

import org.eclipse.birt.chart.model.component.Label;

import com.ibm.icu.util.ULocale;

/**
 * Provides a layer of abstraction for text metrics retrieval via display server
 * dependent implementations.
 */
public interface ITextMetrics
{

	/**
	 * Permits reuse of the text metrics instance for a new label with new
	 * attributes.
	 */
	void reuse( Label la );

	/**
	 * Permits reuse of the text metrics instance for a new label with new
	 * attributes and a forcing wrapping size.
	 * 
	 * @since 2.1
	 */
	void reuse( Label la, double forceWrappingSize );

	/**
	 * Returns the height of a single line of text using the font defined in the
	 * contained label
	 * 
	 * @return The height of a single line of text using the font defined in the
	 *         contained label
	 */
	double getHeight( );

	/**
	 * Returns the descent of a single line of text using the font defined in
	 * the contained label
	 * 
	 * @return The descent of a single line of text using the font defined in
	 *         the contained label
	 */
	double getDescent( );

	/**
	 * Returns the full height of all lines of text using the font defined in
	 * the contained label
	 * 
	 * @return The full height of all lines of text using the font defined in
	 *         the contained label
	 */
	double getFullHeight( );

	/**
	 * Returns the max width of the widest line of text using the font defined
	 * in the contained label
	 * 
	 * @return The max width of the widest line of text using the font defined
	 *         in the contained label
	 */
	double getFullWidth( );

	/**
	 * Returns the number of lines of text associated with the label to be
	 * rendered
	 * 
	 * @return The number of lines of text associated with the label to be
	 *         rendered
	 */
	int getLineCount( );

	/**
	 * The text associated with a line index for multi-line text
	 * 
	 * @param iIndex
	 *            The line to be retrieved from multi-line text
	 * 
	 * @return A line of text (subset)
	 */
	String getLine( int iIndex );

	/**
	 * Perform a cleanup when this object is not required anymore
	 */
	void dispose( );

	/**
	 * Returns the locale associated with the text metrics implementer
	 * 
	 * @return The locale
	 * @deprecated use {@link #getULocale()} instead.
	 */
	Locale getLocale( );

	/**
	 * Returns the locale associated with the text metrics implementer
	 * 
	 * @return The locale
	 * @since 2.1
	 */
	ULocale getULocale( );
}
