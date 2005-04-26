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

package org.eclipse.birt.chart.model.component.util;

import java.util.List;

import org.eclipse.birt.chart.model.component.*;

import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ChartPreferences;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> The <b>Switch </b> for the model's inheritance hierarchy. It supports the call
 * {@link #doSwitch(EObject) doSwitch(object)}to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the inheritance hierarchy until a non-null result is
 * returned, which is the result of the switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.birt.chart.model.component.ComponentPackage
 * @generated
 */
public class ComponentSwitch
{

    /**
     * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected static ComponentPackage modelPackage;

    /**
     * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ComponentSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = ComponentPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that
     * result. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public Object doSwitch(EObject theEObject)
    {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that
     * result. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(EClass theEClass, EObject theEObject)
    {
        if (theEClass.eContainer() == modelPackage)
        {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else
        {
            List eSuperTypes = theEClass.getESuperTypes();
            return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch((EClass) eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that
     * result. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(int classifierID, EObject theEObject)
    {
        switch (classifierID)
        {
            case ComponentPackage.AXIS:
            {
                Axis axis = (Axis) theEObject;
                Object result = caseAxis(axis);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case ComponentPackage.CHART_PREFERENCES:
            {
                ChartPreferences chartPreferences = (ChartPreferences) theEObject;
                Object result = caseChartPreferences(chartPreferences);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case ComponentPackage.GRID:
            {
                Grid grid = (Grid) theEObject;
                Object result = caseGrid(grid);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case ComponentPackage.LABEL:
            {
                Label label = (Label) theEObject;
                Object result = caseLabel(label);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case ComponentPackage.MARKER_LINE:
            {
                MarkerLine markerLine = (MarkerLine) theEObject;
                Object result = caseMarkerLine(markerLine);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case ComponentPackage.MARKER_RANGE:
            {
                MarkerRange markerRange = (MarkerRange) theEObject;
                Object result = caseMarkerRange(markerRange);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case ComponentPackage.SCALE:
            {
                Scale scale = (Scale) theEObject;
                Object result = caseScale(scale);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            case ComponentPackage.SERIES:
            {
                Series series = (Series) theEObject;
                Object result = caseSeries(series);
                if (result == null)
                    result = defaultCase(theEObject);
                return result;
            }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Axis</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Axis</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAxis(Axis object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Chart Preferences</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will terminate the switch. <!--
     * end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Chart Preferences</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseChartPreferences(ChartPreferences object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Grid</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Grid</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGrid(Grid object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Label</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Label</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseLabel(Label object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Marker Line</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Marker Line</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseMarkerLine(MarkerLine object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Marker Range</em>'. <!-- begin-user-doc
     * --> This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc
     * -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Marker Range</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseMarkerRange(MarkerRange object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Scale</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Scale</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseScale(Scale object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>Series</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>Series</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseSeries(Series object)
    {
        return null;
    }

    /**
     * Returns the result of interpretting the object as an instance of '<em>EObject</em>'. <!-- begin-user-doc -->
     * This implementation returns null; returning a non-null result will terminate the switch, but this is the last
     * case anyway. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public Object defaultCase(EObject object)
    {
        return null;
    }

} //ComponentSwitch
