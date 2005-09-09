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

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Rule;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory </b>. <!-- end-user-doc -->
 * @generated
 */
public class DataFactoryImpl extends EFactoryImpl implements DataFactory
{

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataFactoryImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create( EClass eClass )
	{
		switch ( eClass.getClassifierID( ) )
		{
			case DataPackage.ACTION :
				return createAction( );
			case DataPackage.BASE_SAMPLE_DATA :
				return createBaseSampleData( );
			case DataPackage.DATA_ELEMENT :
				return createDataElement( );
			case DataPackage.DATA_SET :
				return createDataSet( );
			case DataPackage.DATE_TIME_DATA_ELEMENT :
				return createDateTimeDataElement( );
			case DataPackage.DATE_TIME_DATA_SET :
				return createDateTimeDataSet( );
			case DataPackage.NUMBER_DATA_ELEMENT :
				return createNumberDataElement( );
			case DataPackage.NUMBER_DATA_SET :
				return createNumberDataSet( );
			case DataPackage.ORTHOGONAL_SAMPLE_DATA :
				return createOrthogonalSampleData( );
			case DataPackage.QUERY :
				return createQuery( );
			case DataPackage.RULE :
				return createRule( );
			case DataPackage.SAMPLE_DATA :
				return createSampleData( );
			case DataPackage.SERIES_DEFINITION :
				return createSeriesDefinition( );
			case DataPackage.SERIES_GROUPING :
				return createSeriesGrouping( );
			case DataPackage.STOCK_DATA_SET :
				return createStockDataSet( );
			case DataPackage.TEXT_DATA_SET :
				return createTextDataSet( );
			case DataPackage.TRIGGER :
				return createTrigger( );
			default :
				throw new IllegalArgumentException( "The class '" + eClass.getName( ) + "' is not a valid classifier" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Action createAction( )
	{
		ActionImpl action = new ActionImpl( );
		return action;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public BaseSampleData createBaseSampleData( )
	{
		BaseSampleDataImpl baseSampleData = new BaseSampleDataImpl( );
		return baseSampleData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataElement createDataElement( )
	{
		DataElementImpl dataElement = new DataElementImpl( );
		return dataElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataSet createDataSet( )
	{
		DataSetImpl dataSet = new DataSetImpl( );
		return dataSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DateTimeDataElement createDateTimeDataElement( )
	{
		DateTimeDataElementImpl dateTimeDataElement = new DateTimeDataElementImpl( );
		return dateTimeDataElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DateTimeDataSet createDateTimeDataSet( )
	{
		DateTimeDataSetImpl dateTimeDataSet = new DateTimeDataSetImpl( );
		return dateTimeDataSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NumberDataElement createNumberDataElement( )
	{
		NumberDataElementImpl numberDataElement = new NumberDataElementImpl( );
		return numberDataElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NumberDataSet createNumberDataSet( )
	{
		NumberDataSetImpl numberDataSet = new NumberDataSetImpl( );
		return numberDataSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public OrthogonalSampleData createOrthogonalSampleData( )
	{
		OrthogonalSampleDataImpl orthogonalSampleData = new OrthogonalSampleDataImpl( );
		return orthogonalSampleData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Query createQuery( )
	{
		QueryImpl query = new QueryImpl( );
		return query;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Rule createRule( )
	{
		RuleImpl rule = new RuleImpl( );
		return rule;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public SampleData createSampleData( )
	{
		SampleDataImpl sampleData = new SampleDataImpl( );
		return sampleData;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public SeriesDefinition createSeriesDefinition( )
	{
		SeriesDefinitionImpl seriesDefinition = new SeriesDefinitionImpl( );
		return seriesDefinition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public SeriesGrouping createSeriesGrouping( )
	{
		SeriesGroupingImpl seriesGrouping = new SeriesGroupingImpl( );
		return seriesGrouping;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public StockDataSet createStockDataSet( )
	{
		StockDataSetImpl stockDataSet = new StockDataSetImpl( );
		return stockDataSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TextDataSet createTextDataSet( )
	{
		TextDataSetImpl textDataSet = new TextDataSetImpl( );
		return textDataSet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Trigger createTrigger( )
	{
		TriggerImpl trigger = new TriggerImpl( );
		return trigger;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DataPackage getDataPackage( )
	{
		return (DataPackage) getEPackage( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static DataPackage getPackage( )
	{
		return DataPackage.eINSTANCE;
	}

} //DataFactoryImpl
