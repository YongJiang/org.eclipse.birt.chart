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

package org.eclipse.birt.chart.ui.swt.interfaces;

import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;

/**
 * IUIServiceProvider
 */
public interface IUIServiceProvider
{

	/**
	 * Constant indicating a fatal error in the model
	 */
	public static final int FATAL_ERRORS = -10;

	/**
	 * Constant indicating a major (though not fatal) problem in the model
	 */
	public static final int CRITICAL_ERRORS = -5;

	/**
	 * Constant indicating a minor error in the model
	 */
	public static final int MINOR_ERRORS = -1;

	/**
	 * Constant indicating no detectable problems exist in the model
	 */
	public static final int NO_ERRORS = 0;

	/**
	 * Constant indicating possible problems detected in the model
	 */
	public static final int POSSIBLE_ERRORS = 1;

	/**
	 * Constant indicating a common expression builder.
	 */
	public static final int COMMAND_EXPRESSION = 1;

	/**
	 * Constant indicating an expression builder specifically for chart.
	 */
	public static final int COMMAND_CHART_EXPRESSION = 2;

	/**
	 * Constant indicating a hyper-link builder.
	 */
	public static final int COMMAND_HYPERLINK = 3;

	/**
	 * This method will be used by the Chart Builder UI to invoke the expression
	 * builder with any previously defined expression. The parameter may be null
	 * if a new expression is to be built.
	 * 
	 * @param sExpression
	 *            the expression to be displayed in the builder (after re-entry)
	 * @param oContext
	 *            the application-specific context used by the Expression
	 *            Builder for each invocation
	 * @param sTitle
	 *            the title to be used for the Expression Builder Dialog
	 * 
	 * @return The final expression string built by the user in the expression
	 *         builder
	 * 
	 * @deprecated use invoke( int command, String value, Object context, String
	 *             sTitle ) instead.
	 */
	public String invoke( String sExpression, Object Context, String sTitle );

	/**
	 * This method will be used by the Chart Builder UI to invoke the expression
	 * builder with any previously defined expression. The parameter may be null
	 * if a new expression is to be built.
	 * 
	 * @param sExpression
	 *            the expression to be displayed in the builder (after re-entry)
	 * @param oContext
	 *            the application-specific context used by the Expression
	 *            Builder for each invocation
	 * @param sTitle
	 *            the title to be used for the Expression Builder Dialog
	 * @param isChartProvider
	 *            specified for chart expression provider.
	 * @return The final expression string built by the user in the expression
	 *         builder
	 * @deprecated use invoke( int command, String value, Object context, String
	 *             sTitle ) instead.
	 */
	public String invoke( String sExpression, Object Context, String sTitle,
			boolean isChartProvider );

	/**
	 * This method will be used by the Chart Builder UI to invoke numerous
	 * builder by a specified command.
	 * 
	 * @param command
	 *            Indicate which command will be executed.
	 * @param value
	 *            initial value.
	 * @param context
	 *            command context.
	 * @param sTitle
	 *            dialog title if applicable.
	 * 
	 * @since 2.0
	 */
	public String invoke( int command, String value, Object context,
			String sTitle ) throws ChartException;
	
	/**
	 * Returns whether all outside builder invokings are supported
	 * @since 2.1
	 */
	public boolean isInvokingSupported( );

	/**
	 * This method will be used by the Chart Builder UI to validate the model
	 * and show any error messages before the user leaves the dialog.
	 * 
	 * @param chartModel
	 *            the model to be validated
	 * @param oContext
	 *            the application-specific context associated with the extended
	 *            chart item
	 * @return an array of user-friendly messages indicating problems with the
	 *         model
	 */
	public String[] validate( Chart chartModel, Object oContext );

	/**
	 * Fetches the list of registered keys for externalizing chart content
	 * 
	 * @return List containing available keys for externalized content
	 */
	public List getRegisteredKeys( );

	/**
	 * Fetches the value for the specified key from the properties file
	 * appropriate for the current locale
	 * 
	 * @param sKey
	 *            the lookup key for the externalized string
	 * @return the value associated with the key for the current locale, null if
	 *         resource key is blank
	 */
	public String getValue( String sKey );

	/**
	 * Gets the result of converting the given value between the specified
	 * absolute units of measurement. Any implementation of this method needs to
	 * support conversion between at least the following units: Inches,
	 * Centimeters, Millimeters, Points and Pixels
	 * 
	 * @param dOriginalValue
	 *            the value to be converted
	 * @param sFromUnits
	 *            the units of measurement from which the conversion is to be
	 *            done
	 * @param sToUnits
	 *            the units of measurement to which the conversion is to be done
	 * @return the converted value
	 */
	public double getConvertedValue( double dOriginalValue, String sFromUnits,
			String sToUnits );
}