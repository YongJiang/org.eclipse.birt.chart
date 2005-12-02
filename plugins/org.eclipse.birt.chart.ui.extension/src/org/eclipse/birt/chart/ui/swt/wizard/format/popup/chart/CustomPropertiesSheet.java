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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.composites.ExtendedPropertyEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class CustomPropertiesSheet extends AbstractPopupSheet
{

	private transient Composite cmpContent;

	public CustomPropertiesSheet( Composite parent, Chart chart )
	{
		super( parent, chart, false );
		cmpTop = getComponent( parent );
	}

	/*
	 * (non-Javadoc) k
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( );
			glContent.horizontalSpacing = 5;
			glContent.verticalSpacing = 5;
			glContent.marginHeight = 7;
			glContent.marginWidth = 7;
			cmpContent.setLayout( glContent );
		}

		Composite cmpExtenedComposite = new ExtendedPropertyEditorComposite( cmpContent,
				SWT.NONE,
				chart );
		{
			GridData gd = new GridData( );
			gd.heightHint = 300;
			cmpExtenedComposite.setLayoutData( gd );
		}

		return cmpContent;
	}
}