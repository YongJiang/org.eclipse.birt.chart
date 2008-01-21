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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class ChartBindingGroupDescriptorProvider extends
		BindingGroupDescriptorProvider
{

	protected List getAvailableDataBindingReferenceList(
			ReportItemHandle element )
	{
		return element.getAvailableDataBindingReferenceList( );
	}
}
