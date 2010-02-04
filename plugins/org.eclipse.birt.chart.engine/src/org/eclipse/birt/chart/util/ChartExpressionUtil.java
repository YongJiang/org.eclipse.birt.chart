/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.data.IDimLevel;
import org.eclipse.birt.core.exception.BirtException;

/**
 * Expression Utility to provide useful methods to handle with expressions.
 * 
 */

public class ChartExpressionUtil
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/trace" ); //$NON-NLS-1$

	/**
	 * Checks if the expression references a binding name
	 * 
	 * @param indicator
	 *            indicator like row or data
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 * @since 2.5.1
	 */
	protected static boolean isBinding( String indicator, String expr,
			boolean hasOperation )
	{
		if ( expr == null )
		{
			return false;
		}
		if ( hasOperation )
		{
			return expr.matches( ".*\\Q" + indicator + "[\"\\E.*\\Q\"]\\E.*" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		String regExp = "\\Q" + indicator + "[\"\\E.*\\Q\"]\\E"; //$NON-NLS-1$ //$NON-NLS-2$
		String regExp2 = "\\Q"//$NON-NLS-1$
				+ indicator
				+ "[\"\\E.*\\Q"//$NON-NLS-1$
				+ indicator
				+ "[\"\\E.*\\Q\"]\\E";//$NON-NLS-1$
		return expr.matches( regExp ) && !expr.matches( regExp2 );
	}

	/**
	 * Returns the binding name
	 * 
	 * @param indicator
	 *            indicator like row or data
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 * @return binding name
	 * @since 2.5.1
	 */
	@SuppressWarnings("unchecked")
	protected static String getBindingName( String indicator, String expr,
			boolean hasOperation )
	{
		if ( isBinding( indicator, expr, hasOperation ) )
		{
			try
			{
				List<IColumnBinding> bindings = ExpressionUtil.extractColumnExpressions( expr,
						indicator );
				if ( !bindings.isEmpty( ) )
				{
					return bindings.get( 0 ).getResultSetColumnName( );
				}
			}
			catch ( BirtException e )
			{
				logger.log( e );
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static void fillBindingNameCollection( Collection<String> names,
			String indicator, String expr )
	{
		try
		{
			List<IColumnBinding> bindings = ExpressionUtil.extractColumnExpressions( expr,
					indicator );
			for ( IColumnBinding binding : bindings )
			{
				names.add( binding.getResultSetColumnName( ) );
			}
		}
		catch ( BirtException e )
		{
			logger.log( e );
		}
	}

	/**
	 * Gets the binding name list in complex expression like data["year"]+"
	 * Q"+data["quarter"]
	 * 
	 * @param indicator
	 *            indicator like row or data
	 * @param expr
	 *            expression
	 * @return binding name list or empty list
	 */
	protected static List<String> getBindingNameList( String indicator,
			String expr )
	{
		List<String> names = new ArrayList<String>( );
		fillBindingNameCollection( names, indicator, expr );
		return names;
	}

	/**
	 * Return the binding name of row["binding"]
	 * 
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 */
	public static String getRowBindingName( String expr, boolean hasOperation )
	{
		return getBindingName( ExpressionUtil.ROW_INDICATOR, expr, hasOperation );
	}

	/**
	 * Return the binding name of data["binding"]
	 * 
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 */
	public static String getCubeBindingName( String expr, boolean hasOperation )
	{
		return getBindingName( ExpressionUtil.DATA_INDICATOR,
				expr,
				hasOperation );
	}

	/**
	 * Checks if the expression references a row binding name
	 * 
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 */
	public static boolean isRowBinding( String expr, boolean hasOperation )
	{
		return isBinding( ExpressionUtil.ROW_INDICATOR, expr, hasOperation );
	}

	/**
	 * Checks if the expression references a data binding name
	 * 
	 * @param expr
	 *            expression
	 * @param hasOperation
	 *            indicates if operation can be allowed in expression
	 */
	public static boolean isCubeBinding( String expr, boolean hasOperation )
	{
		return isBinding( ExpressionUtil.DATA_INDICATOR, expr, hasOperation );
	}

	/**
	 * Gets the cube binding name list in complex expression like data["year"]+"
	 * Q"+data["quarter"]
	 * 
	 * @param expr
	 *            expression
	 * @return binding name list or empty list
	 */
	public static List<String> getCubeBindingNameList( String expr )
	{
		return getBindingNameList( ExpressionUtil.DATA_INDICATOR, expr );
	}

	public static Set<String> getRowBindingNameSet( String expr )
	{
		Set<String> names = new HashSet<String>( );
		fillBindingNameCollection( names, ExpressionUtil.ROW_INDICATOR, expr );
		return names;
	}

	/**
	 * Returns a full binding name for cube or row expression, no matter if
	 * expression is complex or simple. If expression is complex, will create a
	 * new binding name with special characters escaped.
	 * 
	 * @param expr
	 *            expression
	 * @return binding name
	 */
	public static String getFullBindingName( String expr )
	{
		if ( isRowBinding( expr, true ) )
		{
			if ( isRowBinding( expr, false ) )
			{
				return getRowBindingName( expr, false );
			}
			return escapeSpecialCharacters( expr );
		}
		else if ( isCubeBinding( expr, true ) )
		{
			if ( isCubeBinding( expr, false ) )
			{
				return getCubeBindingName( expr, false );
			}
			return escapeSpecialCharacters( expr );
		}
		return expr;
	}

	/**
	 * Check if specified expression is a measure expression.
	 * 
	 * @param expression
	 * @since 2.3
	 */
	public static boolean isMeasureExpresion( String expression )
	{
		if ( expression != null
				&& expression.matches( "\\Qmeasure[\"\\E.*\\Q\"]\\E" ) ) //$NON-NLS-1$
		{
			return true;
		}
		return false;
	}

	/**
	 * This method is to get the measure name that referenced by a measure
	 * reference expression.
	 * 
	 * @param expr
	 * @return measure name
	 * @since 2.3
	 */
	public static String getMeasureName( String expr )
	{
		if ( isMeasureExpresion( expr ) )
		{
			try
			{
				return ExpressionUtil.getReferencedMeasure( expr );
			}
			catch ( BirtException e )
			{
				logger.log( e );
			}
		}
		return null;
	}

	/**
	 * Check if specified expression is a dimension expression.
	 * 
	 * @param expression
	 * @since 2.3
	 */
	public static boolean isDimensionExpresion( String expression )
	{
		if ( expression != null
				&& expression.matches( "\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E" ) ) //$NON-NLS-1$
		{
			return true;
		}
		return false;
	}

	/**
	 * This method is used to get the level name that reference by a level
	 * reference expression of following format:
	 * dimension["dimensionName"]["levelName"].
	 * 
	 * String[0] dimensionName; String[1] levelName;
	 * 
	 * @param expr
	 * @return String[]
	 * @since 2.3
	 */
	public static String[] getLevelNameFromDimensionExpression( String expr )
	{
		if ( ChartExpressionUtil.isDimensionExpresion( expr ) )
		{
			try
			{
				Set<IDimLevel> levels = ExpressionUtil.getReferencedDimLevel( expr );
				if ( !levels.isEmpty( ) )
				{
					IDimLevel level = levels.iterator( ).next( );
					return new String[]{
							level.getDimensionName( ), level.getLevelName( )
					};
				}
			}
			catch ( BirtException e )
			{
				logger.log( e );
			}
		}
		return null;
	}

	/**
	 * Checks if the expression contains string. e.g.
	 * data["year"]+"Q"+data["quarter"]
	 * 
	 * @param expression
	 * @return true if contains
	 */
	public static boolean checkStringInExpression( String expression )
	{
		boolean haveString = false;
		int squareBracketPairingCount = 0;
		for ( int i = 0; i < expression.length( ); i++ )
		{
			if ( expression.charAt( i ) == '[' )
			{
				squareBracketPairingCount++;
			}
			if ( expression.charAt( i ) == ']' )
			{
				squareBracketPairingCount--;
			}

			if ( expression.charAt( i ) == '"' )
			{
				if ( squareBracketPairingCount != 0 )
				{
					haveString = false;
					continue;
				}
				if ( i == 0 || i == expression.length( ) - 1 )
				{
					haveString = true;
					break;
				}
				else
				{
					boolean isStrOperation = false;
					for ( int j = ( i - 1 ); j >= 0; j-- )
					{
						if ( expression.charAt( j ) == ' ' )
						{
							continue;
						}
						else if ( expression.charAt( j ) != '+'
								|| expression.charAt( j ) != '-' )
						{
							isStrOperation = true;
							break;
						}
						else
						{
							isStrOperation = false;
							break;
						}
					}
					if ( isStrOperation )
					{
						haveString = true;
						break;
					}
					else if ( expression.charAt( i - 1 ) != '['
							&& expression.charAt( i + 1 ) != ']' )
					{
						haveString = true;
						break;
					}
				}

			}
		}
		return haveString;
	}

	/**
	 * The method escapes '"','\n',EOF,'\r' and so on from specified
	 * expression/script expression, it returns an expression that can be used
	 * as binding name.
	 * 
	 * @param expression
	 * @return escaped string
	 * @since 2.5.1
	 */
	public static String escapeSpecialCharacters( String expression )
	{
		return expression.replaceAll( "\\\\\"", "" ) //$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll( "\"", "" )//$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll( "\\n", "" )//$NON-NLS-1$ //$NON-NLS-2$
				.replaceAll( new String( new char[]{
					(char) -1
				} ), "" )//$NON-NLS-1$
				.replaceAll( "\\r", "" );//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Creates a JS binding expression with the given name.
	 * 
	 * @param bindingName
	 * @param isCube
	 * @return The binding expression.
	 */
	public static String createBindingExpression( String bindingName,
			boolean isCube )
	{
		StringBuilder sb = new StringBuilder( );
		if ( isCube )
		{
			sb.append( ExpressionUtil.DATA_INDICATOR );
		}
		else
		{
			sb.append( ExpressionUtil.ROW_INDICATOR );
		}
		sb.append( "[\"" ); //$NON-NLS-1$
		sb.append( bindingName );
		sb.append( "\"]" ); //$NON-NLS-1$
		return sb.toString( );
	}

	public static class ExpressionCodec
	{

		public static final String JAVASCRIPT = "javascript"; //$NON-NLS-1$
		protected String sType = JAVASCRIPT;
		protected String sExpr = ""; //$NON-NLS-1$

		public String encode( )
		{
			return sExpr;
		}

		public void decode( String sExpr )
		{
			this.sExpr = sExpr;
		}

		public String getType( )
		{
			return sType;
		}

		public void setType( String type )
		{
			this.sType = type;
		}

		public String getExpression( )
		{
			return sExpr;
		}

		public void setExpression( String sExpr )
		{
			this.sExpr = sExpr;
		}

		public boolean isCubeBinding( boolean hasOperation )
		{
			return ChartExpressionUtil.isCubeBinding( sExpr, hasOperation );
		}

		public boolean isCubeBinding( String expr, boolean hasOperation )
		{
			decode( expr );
			return isCubeBinding( hasOperation );
		}

		public String getCubeBindingName( boolean hasOperation )
		{
			return ChartExpressionUtil.getCubeBindingName( sExpr, hasOperation );
		}

		public String getCubeBindingName( String expr, boolean hasOperation )
		{
			decode( expr );
			return getCubeBindingName( hasOperation );
		}

		public List<String> getCubeBindingNameList( )
		{
			return ChartExpressionUtil.getCubeBindingNameList( sExpr );
		}

		public List<String> getCubeBindingNameList( String expr )
		{
			decode( expr );
			return getCubeBindingNameList( );
		}

		public boolean isRowBinding( boolean hasOperation )
		{
			return ChartExpressionUtil.isRowBinding( sExpr, hasOperation );
		}

		public boolean isRowBinding( String expr, boolean hasOperation )
		{
			decode( expr );
			return isRowBinding( hasOperation );
		}

		public String getRowBindingName( boolean hasOperation )
		{
			return ChartExpressionUtil.getRowBindingName( sExpr, hasOperation );
		}

		public String getRowBindingName( String expr, boolean hasOperation )
		{
			decode( expr );
			return getRowBindingName( hasOperation );
		}

		public Set<String> getRowBindingNameSet( )
		{
			return ChartExpressionUtil.getRowBindingNameSet( sExpr );
		}

		public Set<String> getRowBindingNameSet( String expr )
		{
			decode( expr );
			return getRowBindingNameSet( );
		}

		/**
		 * Returns a full binding name for cube or row expression, no matter if
		 * expression is complex or simple. If expression is complex, will
		 * create a new binding name with special characters escaped.
		 * 
		 * @return binding name
		 */
		public String getFullBindingName( )
		{
			if ( this.isRowBinding( true ) )
			{
				if ( this.isRowBinding( false ) )
				{
					return getRowBindingName( false );
				}
				return escapeSpecialCharacters( sExpr );
			}
			else if ( this.isCubeBinding( true ) )
			{
				if ( this.isCubeBinding( false ) )
				{
					return this.getCubeBindingName( false );
				}
				return escapeSpecialCharacters( sExpr );
			}
			return sExpr;
		}

		/**
		 * Returns a full binding name for cube or row expression, no matter if
		 * expression is complex or simple. If expression is complex, will
		 * create a new binding name with special characters escaped.
		 * 
		 * @param expr
		 *            expression
		 * @return binding name
		 */
		public String getFullBindingName( String expr )
		{
			decode( expr );
			return getFullBindingName( );
		}

		/**
		 * Makes the codec's instance to represent a binding expression with the
		 * given binding name.
		 * 
		 * @param bindingName
		 * @param isCube
		 * @param type
		 */
		public void setBindingName( String bindingName, boolean isCube,
				String type )
		{
			sType = type;
			setBindingName( bindingName, isCube );
		}

		/**
		 * Makes the codec's instance to represent a binding expression with the
		 * given binding name.
		 * 
		 * @param bindingName
		 * @param isCube
		 */
		public void setBindingName( String bindingName, boolean isCube )
		{
			sExpr = createBindingExpression( bindingName, isCube );
		}

		/**
		 * 
		 * @param hasOperation
		 * @return
		 */
		public boolean isBinding( boolean hasOperation )
		{
			if ( isRowBinding( hasOperation ) )
			{
				return true;
			}
			return isCubeBinding( hasOperation );
		}

		/**
		 * 
		 * @param expr
		 * @param hasOperation
		 * @return
		 */
		public boolean isBinding( String expr, boolean hasOperation )
		{
			decode( expr );
			return isBinding( hasOperation );
		}

		/**
		 * Returns a collection of all binding names contained by the
		 * expression.
		 * 
		 * @return A collection of all binding names contained by the
		 *         expression.
		 */
		public Collection<String> getBindingNames( )
		{
			Set<String> set = new HashSet<String>( );
			set.addAll( getRowBindingNameSet( ) );
			set.addAll( getCubeBindingNameList( ) );
			return set;
		}

		/**
		 * Returns a collection of all binding names contained by the
		 * expression.
		 * 
		 * @param expr
		 * @return A collection of all binding names contained by the
		 *         expression.
		 */
		public Collection<String> getBindingNames( String expr )
		{
			decode( expr );
			return getBindingNames( );
		}

		/**
		 * If the whole expression is a single binding name, returns the name,
		 * returns null otherwise.
		 * 
		 * @return If the whole expression is a single binding name, returns the
		 *         name, returns null otherwise.
		 */
		public String getBindingName( )
		{
			String name = getRowBindingName( false );
			if ( name != null )
			{
				return name;
			}
			return getCubeBindingName( false );
		}

		/**
		 * If the whole expression is a single binding name, returns the name,
		 * returns null otherwise.
		 * 
		 * @param expr
		 * @return If the whole expression is a single binding name, returns the
		 *         name, returns null otherwise.
		 */
		public String getBindingName( String expr )
		{
			decode( expr );
			return getBindingName( );
		}
	}

}
