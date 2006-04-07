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

package org.eclipse.birt.chart.reportitem;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IRowSet;
import org.eclipse.birt.report.engine.extension.ReportItemGenerationBase;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

import com.ibm.icu.util.ULocale;

/**
 * ChartReportItemGenerationImpl
 */
public class ChartReportItemGenerationImpl extends ReportItemGenerationBase
{

	private Chart cm = null;

	private DesignElementHandle handle = null;

	private RunTimeContext rtc = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#setModelObject(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public void setModelObject( ExtendedItemHandle eih )
	{
		super.setModelObject( modelHandle );

		IReportItem item = null;
		try
		{
			item = eih.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( e );
		}
		if ( item == null )
		{
			try
			{
				eih.loadExtendedElement( );
				item = eih.getReportItem( );
			}
			catch ( ExtendedElementException eeex )
			{
				logger.log( eeex );
			}
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemPresentationImpl.log.UnableToLocateWrapper" ) ); //$NON-NLS-1$
				return;
			}
		}
		handle = eih;
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( "chart.instance" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#serialize(java.io.OutputStream)
	 */
	public void serialize( OutputStream ostream ) throws BirtException
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream( ostream );
			oos.writeObject( rtc );
			oos.flush( );
			oos.close( );
		}
		catch ( Exception e )
		{
			logger.log( e );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#needSerialization()
	 */
	public boolean needSerialization( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IReportItemGeneration#onRowSets(org.eclipse.birt.report.engine.extension.IRowSet[])
	 */
	public void onRowSets( IRowSet[] rowSets ) throws BirtException
	{
		String javaHandlerClass = handle.getEventHandlerClass( );
		if ( javaHandlerClass != null && javaHandlerClass.length( ) > 0 )
		{
			// use java handler if available.
			cm.setScript( javaHandlerClass );
		}

		// prepare the chart model.
		rtc = Generator.instance( ).prepare( cm,
				new BIRTExternalContext( context ),
				new BIRTScriptClassLoader( ),
				ULocale.getDefault( ) );

		// check
		if ( rowSets == null
				|| rowSets.length != 1
				|| rowSets[0] == null
				|| queries == null
				|| queries[0] == null )
		{
			// if the Data rows are null/empty, do nothing.
			return;
		}

		// Evaluate the expressions so that they are registered by the DtE
		// in the Report Document
		IRowSet rowSet = rowSets[0];
		Map expressions = queries[0].getResultSetExpressions( );
		while ( rowSet.next( ) )
		{
			for ( Iterator iter = expressions.keySet( ).iterator( ); iter.hasNext( ); )
			{
				rowSet.evaluate( ExpressionUtil.createRowExpression( (String) iter.next( ) ) );
			}
		}
	}

}
