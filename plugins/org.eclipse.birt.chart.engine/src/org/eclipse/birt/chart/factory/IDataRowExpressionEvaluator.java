/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.factory;
/**
 * This interface provide expression evaluations of any string expression
 * based on a row context. This is usually associated with an underlying
 * resultset.
 *  
 */
public interface IDataRowExpressionEvaluator
{
	/**
	 * Evaluates the expression based on the current row
	 * @param A String expression
	 * @return An Object representing the evaluated expression. The Object
	 * must be of a type String, Number, Date, Calendar, or it will be evaluated 
	 * as a String using toString().
	 */
	Object evaluate( String expression );
	
	/**
	 * Moves to the first row. Optional if already positioned on the first row
	 * when passed to Generator.bindData()
	 */
	void first();
	
	/**
	 * Moves to the next row.
	 * @return False if the last row has been reached. True otherwise.
	 */
	boolean next();
	
	/**
	 * Closes the underlying resultset. This is optional (it can be a no-op if the
	 * close is handled externally or not needed).
	 *
	 */
	void close();
}
