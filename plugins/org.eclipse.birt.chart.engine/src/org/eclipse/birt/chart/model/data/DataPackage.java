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

package org.eclipse.birt.chart.model.data;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package </b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * Schema file for the chart.model package.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.data.DataFactory
 * @generated
 */
public interface DataPackage extends EPackage
{

	/**
	 * The package name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "data"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.birt.eclipse.org/ChartModelData"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "data"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	DataPackage eINSTANCE = org.eclipse.birt.chart.model.data.impl.DataPackageImpl.init( );

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.ActionImpl <em>Action</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.ActionImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getAction()
	 * @generated
	 */
	int ACTION = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ACTION__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION__VALUE = 1;

	/**
	 * The number of structural features of the the '<em>Action</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl <em>Base Sample Data</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getBaseSampleData()
	 * @generated
	 */
	int BASE_SAMPLE_DATA = 1;

	/**
	 * The feature id for the '<em><b>Data Set Representation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION = 0;

	/**
	 * The number of structural features of the the '<em>Base Sample Data</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_SAMPLE_DATA_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.DataElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.DataElementImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDataElement()
	 * @generated
	 */
	int DATA_ELEMENT = 2;

	/**
	 * The number of structural features of the the '<em>Element</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.DataSetImpl <em>Set</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.DataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDataSet()
	 * @generated
	 */
	int DATA_SET = 3;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_SET__VALUES = 0;

	/**
	 * The number of structural features of the the '<em>Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl <em>Date Time Data Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDateTimeDataElement()
	 * @generated
	 */
	int DATE_TIME_DATA_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_ELEMENT__VALUE = DATA_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Date Time Data Element</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_ELEMENT_FEATURE_COUNT = DATA_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl <em>Date Time Data Set</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getDateTimeDataSet()
	 * @generated
	 */
	int DATE_TIME_DATA_SET = 5;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the the '<em>Date Time Data Set</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_TIME_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl <em>Number Data Element</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNumberDataElement()
	 * @generated
	 */
	int NUMBER_DATA_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_ELEMENT__VALUE = DATA_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Number Data Element</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_ELEMENT_FEATURE_COUNT = DATA_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl <em>Number Data Set</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getNumberDataSet()
	 * @generated
	 */
	int NUMBER_DATA_SET = 7;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the the '<em>Number Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMBER_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl <em>Orthogonal Sample Data</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.OrthogonalSampleDataImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getOrthogonalSampleData()
	 * @generated
	 */
	int ORTHOGONAL_SAMPLE_DATA = 8;

	/**
	 * The feature id for the '<em><b>Data Set Representation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORTHOGONAL_SAMPLE_DATA__DATA_SET_REPRESENTATION = 0;

	/**
	 * The feature id for the '<em><b>Series Definition Index</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORTHOGONAL_SAMPLE_DATA__SERIES_DEFINITION_INDEX = 1;

	/**
	 * The number of structural features of the the '<em>Orthogonal Sample Data</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORTHOGONAL_SAMPLE_DATA_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.QueryImpl <em>Query</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.QueryImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getQuery()
	 * @generated
	 */
	int QUERY = 9;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUERY__DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY__RULES = 1;

	/**
	 * The number of structural features of the the '<em>Query</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.RuleImpl <em>Rule</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.RuleImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getRule()
	 * @generated
	 */
	int RULE = 10;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RULE__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int RULE__VALUE = 1;

	/**
	 * The number of structural features of the the '<em>Rule</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.SampleDataImpl <em>Sample Data</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.SampleDataImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSampleData()
	 * @generated
	 */
	int SAMPLE_DATA = 11;

	/**
	 * The feature id for the '<em><b>Base Sample Data</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA__BASE_SAMPLE_DATA = 0;

	/**
	 * The feature id for the '<em><b>Orthogonal Sample Data</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA = 1;

	/**
	 * The feature id for the '<em><b>Ancillary Sample Data</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA__ANCILLARY_SAMPLE_DATA = 2;

	/**
	 * The number of structural features of the the '<em>Sample Data</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SAMPLE_DATA_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl <em>Series Definition</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSeriesDefinition()
	 * @generated
	 */
	int SERIES_DEFINITION = 12;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__QUERY = 0;

	/**
	 * The feature id for the '<em><b>Series Palette</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SERIES_PALETTE = 1;

	/**
	 * The feature id for the '<em><b>Series Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SERIES_DEFINITIONS = 2;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__FORMAT_SPECIFIER = 3;

	/**
	 * The feature id for the '<em><b>Series</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SERIES = 4;

	/**
	 * The feature id for the '<em><b>Grouping</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__GROUPING = 5;

	/**
	 * The feature id for the '<em><b>Sorting</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION__SORTING = 6;

	/**
	 * The number of structural features of the the '<em>Series Definition</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_DEFINITION_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl <em>Series Grouping</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getSeriesGrouping()
	 * @generated
	 */
	int SERIES_GROUPING = 13;

	/**
	 * The feature id for the '<em><b>Enabled</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__ENABLED = 0;

	/**
	 * The feature id for the '<em><b>Grouping Unit</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUPING_UNIT = 1;

	/**
	 * The feature id for the '<em><b>Grouping Origin</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUPING_ORIGIN = 2;

	/**
	 * The feature id for the '<em><b>Grouping Interval</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUPING_INTERVAL = 3;

	/**
	 * The feature id for the '<em><b>Group Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__GROUP_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Aggregate Expression</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING__AGGREGATE_EXPRESSION = 5;

	/**
	 * The number of structural features of the the '<em>Series Grouping</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_GROUPING_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.StockDataSetImpl <em>Stock Data Set</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.StockDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getStockDataSet()
	 * @generated
	 */
	int STOCK_DATA_SET = 14;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STOCK_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the the '<em>Stock Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STOCK_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.TextDataSetImpl <em>Text Data Set</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.TextDataSetImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getTextDataSet()
	 * @generated
	 */
	int TEXT_DATA_SET = 15;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT_DATA_SET__VALUES = DATA_SET__VALUES;

	/**
	 * The number of structural features of the the '<em>Text Data Set</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_DATA_SET_FEATURE_COUNT = DATA_SET_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.data.impl.TriggerImpl <em>Trigger</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.data.impl.TriggerImpl
	 * @see org.eclipse.birt.chart.model.data.impl.DataPackageImpl#getTrigger()
	 * @generated
	 */
	int TRIGGER = 16;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TRIGGER__CONDITION = 0;

	/**
	 * The feature id for the '<em><b>Action</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGER__ACTION = 1;

	/**
	 * The number of structural features of the the '<em>Trigger</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRIGGER_FEATURE_COUNT = 2;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.Action <em>Action</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Action</em>'.
	 * @see org.eclipse.birt.chart.model.data.Action
	 * @generated
	 */
	EClass getAction( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.Action#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.data.Action#getType()
	 * @see #getAction()
	 * @generated
	 */
	EAttribute getAction_Type( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.data.Action#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.Action#getValue()
	 * @see #getAction()
	 * @generated
	 */
	EReference getAction_Value( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.BaseSampleData <em>Base Sample Data</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.BaseSampleData
	 * @generated
	 */
	EClass getBaseSampleData( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.BaseSampleData#getDataSetRepresentation <em>Data Set Representation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Set Representation</em>'.
	 * @see org.eclipse.birt.chart.model.data.BaseSampleData#getDataSetRepresentation()
	 * @see #getBaseSampleData()
	 * @generated
	 */
	EAttribute getBaseSampleData_DataSetRepresentation( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.DataElement <em>Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.birt.chart.model.data.DataElement
	 * @generated
	 */
	EClass getDataElement( );

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.DataSet <em>Set</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.DataSet
	 * @generated
	 */
	EClass getDataSet( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.DataSet#getValues <em>Values</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Values</em>'.
	 * @see org.eclipse.birt.chart.model.data.DataSet#getValues()
	 * @see #getDataSet()
	 * @generated
	 */
	EAttribute getDataSet_Values( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.DateTimeDataElement <em>Date Time Data Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Date Time Data Element</em>'.
	 * @see org.eclipse.birt.chart.model.data.DateTimeDataElement
	 * @generated
	 */
	EClass getDateTimeDataElement( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue()
	 * @see #getDateTimeDataElement()
	 * @generated
	 */
	EAttribute getDateTimeDataElement_Value( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.DateTimeDataSet <em>Date Time Data Set</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Date Time Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.DateTimeDataSet
	 * @generated
	 */
	EClass getDateTimeDataSet( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.NumberDataElement <em>Number Data Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Number Data Element</em>'.
	 * @see org.eclipse.birt.chart.model.data.NumberDataElement
	 * @generated
	 */
	EClass getNumberDataElement( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.NumberDataElement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.NumberDataElement#getValue()
	 * @see #getNumberDataElement()
	 * @generated
	 */
	EAttribute getNumberDataElement_Value( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.NumberDataSet <em>Number Data Set</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Number Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.NumberDataSet
	 * @generated
	 */
	EClass getNumberDataSet( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData <em>Orthogonal Sample Data</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Orthogonal Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.OrthogonalSampleData
	 * @generated
	 */
	EClass getOrthogonalSampleData( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getDataSetRepresentation <em>Data Set Representation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Set Representation</em>'.
	 * @see org.eclipse.birt.chart.model.data.OrthogonalSampleData#getDataSetRepresentation()
	 * @see #getOrthogonalSampleData()
	 * @generated
	 */
	EAttribute getOrthogonalSampleData_DataSetRepresentation( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex <em>Series Definition Index</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Series Definition Index</em>'.
	 * @see org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex()
	 * @see #getOrthogonalSampleData()
	 * @generated
	 */
	EAttribute getOrthogonalSampleData_SeriesDefinitionIndex( );

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.Query <em>Query</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Query</em>'.
	 * @see org.eclipse.birt.chart.model.data.Query
	 * @generated
	 */
	EClass getQuery( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.Query#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Definition</em>'.
	 * @see org.eclipse.birt.chart.model.data.Query#getDefinition()
	 * @see #getQuery()
	 * @generated
	 */
	EAttribute getQuery_Definition( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.data.Query#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rules</em>'.
	 * @see org.eclipse.birt.chart.model.data.Query#getRules()
	 * @see #getQuery()
	 * @generated
	 */
	EReference getQuery_Rules( );

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.data.Rule <em>Rule</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Rule</em>'.
	 * @see org.eclipse.birt.chart.model.data.Rule
	 * @generated
	 */
	EClass getRule( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.Rule#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.data.Rule#getType()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_Type( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.Rule#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.data.Rule#getValue()
	 * @see #getRule()
	 * @generated
	 */
	EAttribute getRule_Value( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.SampleData <em>Sample Data</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData
	 * @generated
	 */
	EClass getSampleData( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.data.SampleData#getBaseSampleData <em>Base Sample Data</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Base Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData#getBaseSampleData()
	 * @see #getSampleData()
	 * @generated
	 */
	EReference getSampleData_BaseSampleData( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.data.SampleData#getOrthogonalSampleData <em>Orthogonal Sample Data</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Orthogonal Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData#getOrthogonalSampleData()
	 * @see #getSampleData()
	 * @generated
	 */
	EReference getSampleData_OrthogonalSampleData( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.data.SampleData#getAncillarySampleData <em>Ancillary Sample Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Ancillary Sample Data</em>'.
	 * @see org.eclipse.birt.chart.model.data.SampleData#getAncillarySampleData()
	 * @see #getSampleData()
	 * @generated
	 */
	EReference getSampleData_AncillarySampleData( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.SeriesDefinition <em>Series Definition</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Series Definition</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition
	 * @generated
	 */
	EClass getSeriesDefinition( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Query</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_Query( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette <em>Series Palette</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Series Palette</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_SeriesPalette( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesDefinitions <em>Series Definitions</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Series Definitions</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesDefinitions()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_SeriesDefinitions( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier <em>Format Specifier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_FormatSpecifier( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeries <em>Series</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Series</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSeries()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_Series( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping <em>Grouping</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Grouping</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EReference getSeriesDefinition_Grouping( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting <em>Sorting</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sorting</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting()
	 * @see #getSeriesDefinition()
	 * @generated
	 */
	EAttribute getSeriesDefinition_Sorting( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.SeriesGrouping <em>Series Grouping</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Series Grouping</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping
	 * @generated
	 */
	EClass getSeriesGrouping( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled <em>Enabled</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Enabled</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_Enabled( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit <em>Grouping Unit</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grouping Unit</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_GroupingUnit( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingOrigin <em>Grouping Origin</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Grouping Origin</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingOrigin()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EReference getSeriesGrouping_GroupingOrigin( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval <em>Grouping Interval</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Grouping Interval</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_GroupingInterval( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType <em>Group Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Group Type</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_GroupType( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression <em>Aggregate Expression</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Aggregate Expression</em>'.
	 * @see org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression()
	 * @see #getSeriesGrouping()
	 * @generated
	 */
	EAttribute getSeriesGrouping_AggregateExpression( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.StockDataSet <em>Stock Data Set</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stock Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.StockDataSet
	 * @generated
	 */
	EClass getStockDataSet( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.TextDataSet <em>Text Data Set</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Text Data Set</em>'.
	 * @see org.eclipse.birt.chart.model.data.TextDataSet
	 * @generated
	 */
	EClass getTextDataSet( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.data.Trigger <em>Trigger</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trigger</em>'.
	 * @see org.eclipse.birt.chart.model.data.Trigger
	 * @generated
	 */
	EClass getTrigger( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.data.Trigger#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Condition</em>'.
	 * @see org.eclipse.birt.chart.model.data.Trigger#getCondition()
	 * @see #getTrigger()
	 * @generated
	 */
	EAttribute getTrigger_Condition( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.data.Trigger#getAction <em>Action</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Action</em>'.
	 * @see org.eclipse.birt.chart.model.data.Trigger#getAction()
	 * @see #getTrigger()
	 * @generated
	 */
	EReference getTrigger_Action( );

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DataFactory getDataFactory( );

} //DataPackage
