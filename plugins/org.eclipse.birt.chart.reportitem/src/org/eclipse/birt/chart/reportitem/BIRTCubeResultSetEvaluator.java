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

import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;

/**
 * Data expression evaluator for cube query.
 * 
 */

public class BIRTCubeResultSetEvaluator extends
		DataRowExpressionEvaluatorAdapter implements
		IGroupedDataRowExpressionEvaluator
{

	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	protected final ICubeResultSet rs;

	protected final ICubeQueryResults qr;

	protected ICubeCursor cubeCursor;

	protected EdgeCursor mainEdgeCursor;

	protected EdgeCursor subEdgeCursor;

	private List<Integer> lstBreaks = new ArrayList<Integer>( );

	private int iIndex = 0;

	private boolean bWithoutSub = false;

	public BIRTCubeResultSetEvaluator( ICubeResultSet rs )
	{
		this.rs = rs;
		this.qr = null;
	}

	public BIRTCubeResultSetEvaluator( ICubeQueryResults qr )
	{
		this.rs = null;
		this.qr = qr;
	}

	public int[] getGroupBreaks( int groupLevel )
	{
		if ( lstBreaks.size( ) <= 1 )
		{
			if ( bWithoutSub && iIndex > 0 )
			{
				// If no sub edge cursor, break every data
				int[] breaks = new int[iIndex - 1];
				for ( int i = 0; i < breaks.length; i++ )
				{
					breaks[i] = i + 1;
				}
				return breaks;
			}
			return new int[0];
		}
		// Remove the last index as requirement
		int[] breaks = new int[lstBreaks.size( ) - 1];
		for ( int i = 0; i < breaks.length; i++ )
		{
			breaks[i] = lstBreaks.get( i );
		}
		return breaks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java
	 * .lang.String)
	 */
	public Object evaluate( String expression )
	{
		Object result = null;
		try
		{
			// String bindingName = ChartXTabUtil.getBindingName( expression,
			// false );
			// if ( bindingName != null )
			// {
			// // Use DtE's method to evaluate binding for the sake of
			// // performance
			// result = cubeCursor.getObject( bindingName );
			// }
			if ( rs != null )
			{
				// If not binding name, evaluate it via report engine
				result = rs.evaluate( expression );
			}
			else
			{
				// In the case of Live preview, no engine's result set, use the
				// binding name directly. This is a minor bug, but difficult to
				// evaluate the expression in design time.
				result = cubeCursor.getObject( ChartXTabUtil.getBindingName( expression,
						true ) );
			}
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		catch ( BirtException e )
		{
			logger.log( e );
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluateGlobal
	 * (java.lang.String)
	 */
	public Object evaluateGlobal( String expression )
	{
		return evaluate( expression );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next( )
	{
		iIndex++;
		try
		{
			if ( subEdgeCursor != null )
			{
				// Break if sub cursor reaches end
				if ( subEdgeCursor.next( ) )
				{
					return true;
				}

				// Add break index for each start point
				lstBreaks.add( new Integer( iIndex ) );

				subEdgeCursor.first( );
				return mainEdgeCursor.next( );
			}
			else
			{
				return mainEdgeCursor.next( );
			}
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close( )
	{
		if ( rs != null )
		{
			rs.close( );
		}
		if ( qr != null )
		{
			try
			{
				qr.close( );
			}
			catch ( BirtException e )
			{
				logger.log( e );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first( )
	{
		try
		{
			initCubeCursor( );

			mainEdgeCursor.first( );
			if ( subEdgeCursor != null )
			{
				subEdgeCursor.first( );
			}
			else
			{
				bWithoutSub = true;
			}
			return true;
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		catch ( BirtException e )
		{
			logger.log( e );
		}
		return false;
	}

	protected void initCubeCursor( ) throws OLAPException, BirtException
	{
		if ( cubeCursor == null )
		{
			if ( rs != null )
			{
				cubeCursor = (ICubeCursor) rs.getCubeCursor( );
			}
			else
			{
				cubeCursor = qr.getCubeCursor( );
			}

			List<EdgeCursor> edges = cubeCursor.getOrdinateEdge( );
			if ( edges.size( ) == 0 )
			{
				throw new ChartException( ChartReportItemPlugin.ID,
						ChartException.DATA_BINDING,
						Messages.getString( "exception.no.cube.edge" ) ); //$NON-NLS-1$
			}
			else if ( edges.size( ) == 1 )
			{
				this.mainEdgeCursor = edges.get( 0 );
				this.subEdgeCursor = null;
			}
			else
			{
				this.mainEdgeCursor = edges.get( 0 );
				this.subEdgeCursor = edges.get( 1 );;
			}
		}
	}

	public boolean needCategoryGrouping( )
	{
		return false;
	}

	public boolean needOptionalGrouping( )
	{
		return false;
	}
}
