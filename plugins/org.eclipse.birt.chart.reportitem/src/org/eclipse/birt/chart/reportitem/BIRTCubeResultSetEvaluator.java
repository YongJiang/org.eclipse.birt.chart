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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

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

	protected List<Integer> lstBreaks = new ArrayList<Integer>( );

	protected int iIndex = 0;

	protected boolean bWithoutSub = false;

	// Use DTE APIs with rhino to evaluate expression like data["x"]+data["xx"]
	private Context cx;
	private ImporterTopLevel global;
	private ScriptableObject dataObject;

	public BIRTCubeResultSetEvaluator( ICubeResultSet rs )
	{
		this.rs = rs;
		this.qr = null;
	}

	public BIRTCubeResultSetEvaluator( ICubeQueryResults qr )
	{
		this.rs = null;
		this.qr = qr;

		// Must evaluate complex expressions with rhino
		cx = Context.enter( );
		global = new ImporterTopLevel( );
		// Add data expression evaluator
		dataObject = new ScriptableObject( ) {

			private static final long serialVersionUID = 7514367894454591834L;

			private Map<String, Object> dataValues = new HashMap<String, Object>( );

			@Override
			public String getClassName( )
			{
				return ExpressionUtil.DATA_INDICATOR;
			}

			@Override
			public boolean has( String name, Scriptable start )
			{
				return dataValues.containsKey( name );
			}

			@Override
			public void put( String name, Scriptable start, Object value )
			{
				dataValues.put( name, value );
			}

			@Override
			public Object get( String name, Scriptable start )
			{
				return dataValues.get( name );
			}
		};
		global.put( ExpressionUtil.DATA_INDICATOR, global, dataObject );
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
			if ( rs != null )
			{
				// If not binding name, evaluate it via report engine
				result = rs.evaluate( expression );
			}
			else
			{
				// DTE only supports evaluating data binding name, so chart
				// engine must evaluate complex expression with rhino.
				// Get each data element and set the evaluated value to rhino
				// context
				List<String> bindingNames = ChartXTabUtil.getBindingNameList( expression );
				for ( String bindingName : bindingNames )
				{
					dataObject.put( bindingName,
							global,
							cubeCursor.getObject( bindingName ) );
				}
				result = cx.evaluateString( global,
						expression,
						"<inline>", 1, null ); //$NON-NLS-1$
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
				lstBreaks.add( Integer.valueOf( iIndex ) );

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
			finally
			{
				Context.exit( );
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
