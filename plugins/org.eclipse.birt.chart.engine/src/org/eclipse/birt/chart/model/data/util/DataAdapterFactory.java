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

package org.eclipse.birt.chart.model.data.util;

import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataElement;
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
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory </b> for the model. It provides an adapter <code>createXXX</code>
 * method for each class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.data.DataPackage
 * @generated
 */
public class DataAdapterFactory extends AdapterFactoryImpl
{

    /**
     * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected static DataPackage modelPackage;

    /**
     * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public DataAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = DataPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc --> This
     * implementation returns <code>true</code> if the object is either the model's package or is an instance object
     * of the model. <!-- end-user-doc -->
     * 
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    public boolean isFactoryForType(Object object)
    {
        if (object == modelPackage)
        {
            return true;
        }
        if (object instanceof EObject)
        {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch the delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DataSwitch modelSwitch = new DataSwitch()
    {
        public Object caseAction(Action object)
        {
            return createActionAdapter();
        }

        public Object caseBaseSampleData(BaseSampleData object)
        {
            return createBaseSampleDataAdapter();
        }

        public Object caseDataElement(DataElement object)
        {
            return createDataElementAdapter();
        }

        public Object caseDataSet(DataSet object)
        {
            return createDataSetAdapter();
        }

        public Object caseDateTimeDataElement(DateTimeDataElement object)
        {
            return createDateTimeDataElementAdapter();
        }

        public Object caseDateTimeDataSet(DateTimeDataSet object)
        {
            return createDateTimeDataSetAdapter();
        }

        public Object caseNumberDataElement(NumberDataElement object)
        {
            return createNumberDataElementAdapter();
        }

        public Object caseNumberDataSet(NumberDataSet object)
        {
            return createNumberDataSetAdapter();
        }

        public Object caseOrthogonalSampleData(OrthogonalSampleData object)
        {
            return createOrthogonalSampleDataAdapter();
        }

        public Object caseQuery(Query object)
        {
            return createQueryAdapter();
        }

        public Object caseRule(Rule object)
        {
            return createRuleAdapter();
        }

        public Object caseSampleData(SampleData object)
        {
            return createSampleDataAdapter();
        }

        public Object caseSeriesDefinition(SeriesDefinition object)
        {
            return createSeriesDefinitionAdapter();
        }

        public Object caseSeriesGrouping(SeriesGrouping object)
        {
            return createSeriesGroupingAdapter();
        }

        public Object caseStockDataSet(StockDataSet object)
        {
            return createStockDataSetAdapter();
        }

        public Object caseTextDataSet(TextDataSet object)
        {
            return createTextDataSetAdapter();
        }

        public Object caseTrigger(Trigger object)
        {
            return createTriggerAdapter();
        }

        public Object defaultCase(EObject object)
        {
            return createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param target
     *            the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    public Adapter createAdapter(Notifier target)
    {
        return (Adapter) modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.data.Action <em>Action</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.Action
     * @generated
     */
    public Adapter createActionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.BaseSampleData <em>Base Sample Data</em>}'. <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.BaseSampleData
     * @generated
     */
    public Adapter createBaseSampleDataAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.DataElement <em>Element</em>}'. <!-- begin-user-doc --> This default
     * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance
     * will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.DataElement
     * @generated
     */
    public Adapter createDataElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.data.DataSet <em>Set</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.DataSet
     * @generated
     */
    public Adapter createDataSetAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.DateTimeDataElement <em>Date Time Data Element</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.DateTimeDataElement
     * @generated
     */
    public Adapter createDateTimeDataElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.DateTimeDataSet <em>Date Time Data Set</em>}'. <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.DateTimeDataSet
     * @generated
     */
    public Adapter createDateTimeDataSetAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.NumberDataElement <em>Number Data Element</em>}'. <!-- begin-user-doc
     * --> This default implementation returns null so that we can easily ignore cases; it's useful to ignore a case
     * when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.NumberDataElement
     * @generated
     */
    public Adapter createNumberDataElementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.NumberDataSet <em>Number Data Set</em>}'. <!-- begin-user-doc --> This
     * default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.NumberDataSet
     * @generated
     */
    public Adapter createNumberDataSetAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.OrthogonalSampleData <em>Orthogonal Sample Data</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.OrthogonalSampleData
     * @generated
     */
    public Adapter createOrthogonalSampleDataAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.data.Query <em>Query</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.Query
     * @generated
     */
    public Adapter createQueryAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.data.Rule <em>Rule</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.Rule
     * @generated
     */
    public Adapter createRuleAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.SampleData <em>Sample Data</em>}'. <!-- begin-user-doc --> This default
     * implementation returns null so that we can easily ignore cases; it's useful to ignore a case when inheritance
     * will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.SampleData
     * @generated
     */
    public Adapter createSampleDataAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.SeriesDefinition <em>Series Definition</em>}'. <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.SeriesDefinition
     * @generated
     */
    public Adapter createSeriesDefinitionAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.SeriesGrouping <em>Series Grouping</em>}'. <!-- begin-user-doc --> This
     * default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.SeriesGrouping
     * @generated
     */
    public Adapter createSeriesGroupingAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.StockDataSet <em>Stock Data Set</em>}'. <!-- begin-user-doc --> This
     * default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.StockDataSet
     * @generated
     */
    public Adapter createStockDataSetAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '
     * {@link org.eclipse.birt.chart.model.data.TextDataSet <em>Text Data Set</em>}'. <!-- begin-user-doc --> This
     * default implementation returns null so that we can easily ignore cases; it's useful to ignore a case when
     * inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.TextDataSet
     * @generated
     */
    public Adapter createTextDataSetAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.birt.chart.model.data.Trigger <em>Trigger</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore cases; it's useful
     * to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.eclipse.birt.chart.model.data.Trigger
     * @generated
     */
    public Adapter createTriggerAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for the default case. <!-- begin-user-doc --> This default implementation returns null.
     * <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter()
    {
        return null;
    }

} //DataAdapterFactory
