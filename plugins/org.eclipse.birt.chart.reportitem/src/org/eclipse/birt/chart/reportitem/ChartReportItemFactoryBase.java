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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentationInfo;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Basic implementation of report item factory.
 */

public class ChartReportItemFactoryBase implements IChartReportItemFactory
{

	public IActionRenderer createActionRenderer( DesignElementHandle eih,
			IHTMLActionHandler handler, IDataRowExpressionEvaluator evaluator,
			IReportContext context )
	{
		return new BIRTActionRenderer( eih, handler, evaluator, context );
	}

	public IReportItemPresentation createReportItemPresentation(
			IReportItemPresentationInfo info )
	{
		return new ChartReportItemPresentationImpl( );
	}

	public Serializer createSerializer( ExtendedItemHandle eih )
	{
		return SerializerImpl.instance( );
	}

	public ChartCubeQueryHelper createCubeQueryHelper(
			ExtendedItemHandle handle, Chart cm )
	{
		return new ChartCubeQueryHelper( handle, cm );
	}

	public IGroupedDataRowExpressionEvaluator createCubeEvaluator( Chart cm,
			ICubeResultSet set )
	{
		return new BIRTCubeResultSetEvaluator( set );
	}
}
