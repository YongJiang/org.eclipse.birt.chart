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

package org.eclipse.birt.chart.ui.swt.interfaces;

import org.eclipse.birt.chart.ui.swt.ChartPreviewPainter;
import org.eclipse.swt.widgets.Canvas;

/**
 * Provides preview functionality if current task supports preview
 */

public interface ITaskPreviewable
{

	/**
	 * Indicates if current task can preview chart or not.
	 * 
	 * @return support preview or not
	 */
	boolean isPreviewable( );

	/**
	 * Returns the canvas which displays preview result
	 * 
	 * @return canvas
	 */
	Canvas getPreviewCanvas( );

	/**
	 * Creates preview painter
	 * 
	 * @return preview painter
	 */
	ChartPreviewPainter createPreviewPainter( );

	/**
	 * Renders the preview in canvas
	 */
	void doPreview( );
}
