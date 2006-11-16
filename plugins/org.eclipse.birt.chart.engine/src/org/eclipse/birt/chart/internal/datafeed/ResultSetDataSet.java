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

package org.eclipse.birt.chart.internal.datafeed;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.internal.datafeed.ResultSetWrapper;

/**
 * Maintains a subset of a resultset by defining a selective list of columns and
 * a row range to be extracted from a full resultset. An instance of this class
 * is provided to a custom data set processor that is capable of converting the
 * resultset subset content into the expected chart dataset format.
 */
public final class ResultSetDataSet implements IResultSetDataSet
{

	/**
	 * Indexes of the columns extracted from the parent resultset
	 */
	private final int[] iaColumnIndexes;

	/**
	 * Bounds of the subset with respect to the parent resultset
	 */
	private final long lStartRow, lEndRow;

	/**
	 * An internal iterator capable of visiting each row in the resultset subset
	 */
	private final Iterator it;

	/**
	 * The current row number being visited
	 */
	private long lRow = 0;

	/**
	 * The parent resultset wrapper of which this instance is a subset
	 */
	private final ResultSetWrapper rsw;

	/**
	 * Temporary variable used in conjunction with the iterator
	 */
	private final Object[] oaTuple;

	/**
	 * Number of columns in this resultset subset
	 */
	private final int iColumnCount;

	private final boolean listMode;

	private final int listDataType;

	/**
	 * The constructor that creates an instance of a resultset subset by
	 * extracting appropriate columns and a row range from a resultset
	 * 
	 * @param liResultSet
	 * @param iColumnIndex
	 * @param lStartRow
	 * @param lEndRow
	 */
	public ResultSetDataSet( ResultSetWrapper rsw, int[] iaColumnIndexes,
			long lStartRow, long lEndRow )
	{
		this.rsw = rsw;
		this.iColumnCount = iaColumnIndexes.length;
		this.iaColumnIndexes = iaColumnIndexes;
		this.lStartRow = lStartRow;
		this.lEndRow = lEndRow;
		this.oaTuple = new Object[iaColumnIndexes.length];

		this.listMode = false;
		this.listDataType = IConstants.UNDEFINED;

		// SCROLL TO START ROW
		it = rsw.iterator( );
		if ( lRow < lStartRow )
		{
			while ( lRow < lStartRow )
			{
				lRow++;
				it.next( );
			}
		}
	}

	/**
	 * Creates the resultset using a given list.
	 * 
	 * @param lst
	 */
	public ResultSetDataSet( List lst, int dataType )
	{
		this.rsw = null;
		this.iColumnCount = 1;
		this.iaColumnIndexes = new int[]{
			0
		};
		this.lStartRow = 0;
		this.lEndRow = lst.size( );
		this.oaTuple = new Object[iColumnCount];

		this.listMode = true;
		this.listDataType = dataType;

		it = lst.iterator( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#hasNext()
	 */
	public boolean hasNext( )
	{
		return ( lRow < lEndRow );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#next()
	 */
	public Object[] next( )
	{
		lRow++;
		if ( lRow > lEndRow )
		{
			return null;
		}

		if ( listMode )
		{
			oaTuple[0] = it.next( );
		}
		else
		{
			final Object[] oaResultSet = (Object[]) it.next( );
			for ( int i = 0; i < iColumnCount; i++ )
			{
				if ( iaColumnIndexes[i] != -1 )
				{
					//ignore the column if the column index is -1.
					oaTuple[i] = oaResultSet[iaColumnIndexes[i]];
				}
			}
		}
		return oaTuple;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#getDataType()
	 */
	public int getDataType( )
	{
		if ( listMode )
		{
			return listDataType;
		}

		if ( iaColumnIndexes.length == 1 )
		{
			return rsw.getColumnDataType( iaColumnIndexes[0] );
		}
		return IConstants.UNDEFINED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#getColumnCount()
	 */
	public int getColumnCount( )
	{
		return iColumnCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#getSize()
	 */
	public long getSize( )
	{
		return lEndRow - lStartRow;
	}
}
