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

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * The implementation of <code>IGroupedDataResultSet</code> for chart.
 * 
 * @since BIRT 2.3
 */
public class BIRTGroupedQueryResultSetEvaluator
		extends
		BaseGroupedQueryResultSetEvaluator
{

	private IQueryResultSet fQueryResultSet;

	/**
	 * Constructor.
	 * 
	 * @param resultSet
	 * @param hasAggregation
	 * @param cm
	 * @throws ChartException
	 */
	public BIRTGroupedQueryResultSetEvaluator( IQueryResultSet resultSet,
			boolean hasAggregation, Chart cm ) throws ChartException
	{
		super( resultSet.getResultIterator( ), hasAggregation, cm );
		
		fQueryResultSet = resultSet;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param resultSet
	 * @param hasAggregation
	 * @param isSubQuery
	 * @param cm
	 * @throws ChartException
	 * @since 2.3
	 */
	public BIRTGroupedQueryResultSetEvaluator( IQueryResultSet resultSet,
			boolean hasAggregation, boolean isSubQuery, Chart cm,
			ExtendedItemHandle handle )
			throws ChartException
	{
		super( resultSet.getResultIterator( ),
				hasAggregation,
				isSubQuery,
				cm,
				handle );
		fQueryResultSet = resultSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close( )
	{
		fQueryResultSet.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
	 */
	public Object evaluate( String expression )
	{
		try
		{
			return fQueryResultSet.evaluate( expression );
		}
		catch ( BirtException e )
		{
			sLogger.log( e );
		}
		return null;
	}
}
