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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.factory.ActionEvaluatorAdapter;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * A BIRT action evaluator implementation.
 */
public class BIRTActionEvaluator extends ActionEvaluatorAdapter
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IActionEvaluator#getActionExpressions(org.eclipse.birt.chart.model.data.Action)
	 */
	public String[] getActionExpressions( Action action, StructureSource source )
	{
		if ( action instanceof MultipleActions )
		{
			List<String> expList = new ArrayList<String>( );
			for ( Action subAction : ( (MultipleActions) action ).getActions( ) )
			{
				if ( subAction.getValue( ) instanceof URLValue )
				{
					getURLValueExpressions( expList,
							(URLValue) subAction.getValue( ) );
				}
			}
			if ( expList.size( ) > 0 )
			{
				return (String[]) expList.toArray( new String[expList.size( )] );
			}
		}
		else if ( ActionType.URL_REDIRECT_LITERAL.equals( action.getType( ) ) )
		{
			List<String> expList = new ArrayList<String>( );
			
			if ( action.getValue( ) instanceof URLValue )
			{
				URLValue uv = (URLValue) action.getValue( );
				getURLValueExpressions( expList, uv );
			}
			else if ( action.getValue() instanceof MultiURLValues )
			{
				for ( URLValue uv : ((MultiURLValues)action.getValue()).getURLValues( ) )
				{
					getURLValueExpressions( expList, uv );	
				}
			}
			
			
			if ( expList.size( ) > 0 )
			{
				return (String[]) expList.toArray( new String[expList.size( )] );
			}
		}
		else if ( ActionType.SHOW_TOOLTIP_LITERAL.equals( action.getType( ) ) )
		{
			if ( StructureType.SERIES.equals( source.getType( ) ) )
			{
				TooltipValue tv = (TooltipValue) action.getValue( );

				String exp = tv.getText( );
				if ( exp != null && exp.trim( ).length( ) > 0 )
				{
					return new String[]{
						exp
					};
				}
			}
		}

		return null;
	}

	/**
	 * @param expList
	 * @param uv
	 */
	private void getURLValueExpressions( List<String> expList, URLValue uv )
	{
		String sa = uv.getBaseUrl( );

		try
		{
			ActionHandle handle = ModuleUtil.deserializeAction( sa );

			
			String exp;

			if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( handle.getLinkType( ) ) )
			{
				ExpressionHandle expHandle = handle.getExpressionProperty( org.eclipse.birt.report.model.api.elements.structures.Action.URI_MEMBER );
				if ( ExpressionType.JAVASCRIPT.equals( expHandle.getType( ) ) )
				{// only add when type is js.
					exp = expHandle.getStringExpression( );

					if ( !expList.contains( exp ) )
					{
						expList.add( exp );
					}
				}
			}
			else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( handle.getLinkType( ) ) )
			{
				exp = handle.getTargetBookmark( );

				if ( !expList.contains( exp ) )
				{
					expList.add( exp );
				}
			}
			else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( handle.getLinkType( ) ) )
			{
				exp = handle.getTargetBookmark( );

				if ( exp != null && !expList.contains( exp ) )
				{
					expList.add( exp );
				}

				for ( Iterator itr = handle.getSearch( ).iterator( ); itr.hasNext( ); )
				{
					SearchKeyHandle skh = (SearchKeyHandle) itr.next( );
					exp = skh.getExpression( );

					if ( !expList.contains( exp ) )
					{
						expList.add( exp );
					}
				}

				for ( Iterator itr = handle.getParamBindings( ).iterator( ); itr.hasNext( ); )
				{
					ParamBindingHandle pbh = (ParamBindingHandle) itr.next( );
					exp = pbh.getExpression( );

					if ( !expList.contains( exp ) )
					{
						expList.add( exp );
					}
				}

			}
		}
		catch ( DesignFileException e )
		{
			logger.log( e );
		}
	}
}
