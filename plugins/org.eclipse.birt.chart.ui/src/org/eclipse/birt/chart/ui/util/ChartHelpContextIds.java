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

package org.eclipse.birt.chart.ui.util;

/**
 * 
 */

public class ChartHelpContextIds
{

	public static final String PREFIX = "org.eclipse.birt.chart.cshelp."; //$NON-NLS-1$

	/*----------TASK----------*/
	public static final String TASK_SELECT_TYPE = PREFIX + "SelectChartType_ID"; //$NON-NLS-1$

	public static final String TASK_SELECT_DATA = PREFIX + "SelectChartData_ID"; //$NON-NLS-1$

	/*----------SUBTASK----------*/
	public static final String SUBTASK_CHART = PREFIX + "FormatChartArea_ID"; //$NON-NLS-1$

	public static final String SUBTASK_PLOT = PREFIX + "FormatChartPlot_ID"; //$NON-NLS-1$

	public static final String SUBTASK_LEGEND = PREFIX + "FormatChartLegend_ID"; //$NON-NLS-1$

	public static final String SUBTASK_TITLE = PREFIX + "FormatChartTitle_ID"; //$NON-NLS-1$
	
	public static final String SUBTASK_AXIS = PREFIX + "FormatChartAxis_ID"; //$NON-NLS-1$

	public static final String SUBTASK_XAXIS = PREFIX + "FormatChartXAxis_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YAXIS = PREFIX + "FormatChartYAxis_ID"; //$NON-NLS-1$

	public static final String SUBTASK_ZAXIS = PREFIX + "FormatChartZAxis_ID"; //$NON-NLS-1$

	public static final String SUBTASK_SERIES = PREFIX + "FormatChartSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_XSERIES = PREFIX
			+ "FormatChartXSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_BAR = PREFIX
			+ "FormatBarChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_LINE = PREFIX
			+ "FormatLineChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_AREA = PREFIX
			+ "FormatAreaChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_SCATTER = PREFIX
			+ "FormatScatterChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_STOCK = PREFIX
			+ "FormatStockChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_PIE = PREFIX
			+ "FormatPieChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_METER = PREFIX
			+ "FormatMeterChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_BUBBLE = PREFIX
			+ "FormatBubbleChartYSeries_ID"; //$NON-NLS-1$

	public static final String SUBTASK_YSERIES_DIFFERENCE = PREFIX
			+ "FormatDifferenceChartYSeries_ID"; //$NON-NLS-1$
	
	public static final String SUBTASK_YSERIES_GANTT = PREFIX
	+ "FormatGanttChartYSeries_ID"; //$NON-NLS-1$

	/*----------POPUP----------*/
	public static final String POPUP_CHART_TITLE_FORMAT = PREFIX
			+ "TitleFormat_ID"; //$NON-NLS-1$

	public static final String POPUP_CHART_OUTLINE = PREFIX + "Outline_ID"; //$NON-NLS-1$

	public static final String POPUP_CHART_CUSTOM = PREFIX
			+ "ChartCustomProperties_ID"; //$NON-NLS-1$

	public static final String POPUP_CHART_GENERAL = PREFIX
			+ "ChartGeneralProperties_ID"; //$NON-NLS-1$

	public static final String POPUP_PLOT_AREA_FORMAT = PREFIX
			+ "AreaFormat_ID"; //$NON-NLS-1$

	public static final String POPUP_LEGEND_LAYOUT = PREFIX + "LegendLayout_ID"; //$NON-NLS-1$

	public static final String POPUP_AXIS_GRIDLINES = PREFIX + "Gridlines_ID"; //$NON-NLS-1$

	public static final String POPUP_AXIS_MARKERS = PREFIX + "Markers_ID"; //$NON-NLS-1$

	public static final String POPUP_AXIS_SCALE = PREFIX + "Scale_ID"; //$NON-NLS-1$

	public static final String POPUP_SERIES_PALETTE = PREFIX
			+ "SeriesPalette_ID"; //$NON-NLS-1$

	public static final String POPUP_SERIES_LABEL = PREFIX
			+ "DataPointLabels_ID"; //$NON-NLS-1$

	public static final String POPUP_SERIES_CURVE_FITTING = PREFIX
			+ "CurveFitting_ID"; //$NON-NLS-1$

	public static final String POPUP_SERIES_LINE_MARKER = PREFIX
			+ "FormatLineChartSeriesMarkers_ID"; //$NON-NLS-1$

	public static final String POPUP_SERIES_METER_REGION = PREFIX
			+ "MeterChartRegions_ID"; //$NON-NLS-1$

	/** Used in interactivity popup and dialog */
	public static final String POPUP_INTERACTIVITY = PREFIX
			+ "Interactivity_ID"; //$NON-NLS-1$

	/** Used in popup chart-text, axis-text or series(pie)-titles */
	public static final String POPUP_TEXT_FORMAT = PREFIX + "TextFormat_ID"; //$NON-NLS-1$

	public static final String POPUP_DIAL_NEEDLES = PREFIX + "DialNeedles_ID"; //$NON-NLS-1$

	public static final String POPUP_DIAL_LABELS = PREFIX + "DialLabels_ID"; //$NON-NLS-1$

	public static final String POPUP_TITLE_TEXT = PREFIX + "TitleText_ID"; //$NON-NLS-1$

	public static final String POPUP_TITLE_BLOCK = PREFIX + "TitleBlock_ID"; //$NON-NLS-1$

	public static final String POPUP_LEGEND_TITLE = PREFIX + "LegendTitle_ID"; //$NON-NLS-1$

	public static final String POPUP_LEGEND_BLOCK = PREFIX + "LegendBlock_ID"; //$NON-NLS-1$
	/*----------DIALOG----------*/
	public static final String DIALOG_DATA_SET_FILTER = PREFIX
			+ "DataSetFilter_ID"; //$NON-NLS-1$

	public static final String DIALOG_DATA_SET_PARAMETER = PREFIX
			+ "DataSetParameter_ID"; //$NON-NLS-1$

	public static final String DIALOG_DATA_SET_COLUMN_BINDING = PREFIX
			+ "SelectDataBinding_ID"; //$NON-NLS-1$

	public static final String DIALOG_NEW_DATA_SET = PREFIX + "NewDataSet_ID"; //$NON-NLS-1$

	public static final String DIALOG_EXPRESSION_BUILDER = PREFIX
			+ "ExpressionBuilder_ID"; //$NON-NLS-1$

	public static final String DIALOG_EDIT_FORMAT = PREFIX + "FormatData_ID"; //$NON-NLS-1$

	public static final String DIALOG_EDIT_URL = PREFIX + "EditURL_ID"; //$NON-NLS-1$

	public static final String DIALOG_EXTERNALIZE_TEXT = PREFIX
			+ "ExternalizeText_ID"; //$NON-NLS-1$

	public static final String DIALOG_FONT_EDITOR = PREFIX + "FontEditor_ID"; //$NON-NLS-1$

	public static final String DIALOG_COLOR_GRADIENT = PREFIX
			+ "GradientEditor_ID"; //$NON-NLS-1$

	public static final String DIALOG_COLOR_IMAGE = PREFIX + "SelectImage_ID"; //$NON-NLS-1$

	public static final String DIALOG_COLOR_BACKGROUND = PREFIX
			+ "FormatChartAreaBackground_ID"; //$NON-NLS-1$

	public static final String DIALOG_COLOR_CUSTOM = PREFIX
			+ "BackgroundColor_ID"; //$NON-NLS-1$

	public static final String DIALOG_GROUP_AND_SORT = PREFIX
			+ "GroupAndSort_ID"; //$NON-NLS-1$

	/*----------PREFERENCE----------*/
	public static final String PREFERENCE_CHART = PREFIX
			+ "Preferences_BIRTChart_ID"; //$NON-NLS-1$
	
	/*----------VIEW----------*/
	public static final String VIEW_CHART_EXAMPLE = PREFIX + "ChartExamples_ID"; //$NON-NLS-1$

}
