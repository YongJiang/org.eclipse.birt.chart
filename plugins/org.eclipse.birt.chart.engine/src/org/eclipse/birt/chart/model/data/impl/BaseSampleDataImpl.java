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

import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Base Sample Data</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl#getDataSetRepresentation <em>Data Set Representation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BaseSampleDataImpl extends EObjectImpl implements BaseSampleData
{

    /**
     * The default value of the '{@link #getDataSetRepresentation() <em>Data Set Representation</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDataSetRepresentation()
     * @generated @ordered
     */
    protected static final String DATA_SET_REPRESENTATION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDataSetRepresentation() <em>Data Set Representation</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDataSetRepresentation()
     * @generated @ordered
     */
    protected String dataSetRepresentation = DATA_SET_REPRESENTATION_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected BaseSampleDataImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass()
    {
        return DataPackage.eINSTANCE.getBaseSampleData();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getDataSetRepresentation()
    {
        return dataSetRepresentation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setDataSetRepresentation(String newDataSetRepresentation)
    {
        String oldDataSetRepresentation = dataSetRepresentation;
        dataSetRepresentation = newDataSetRepresentation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION, oldDataSetRepresentation, dataSetRepresentation));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
                return getDataSetRepresentation();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eSet(EStructuralFeature eFeature, Object newValue)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
                setDataSetRepresentation((String)newValue);
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
                setDataSetRepresentation(DATA_SET_REPRESENTATION_EDEFAULT);
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
                return DATA_SET_REPRESENTATION_EDEFAULT == null ? dataSetRepresentation != null : !DATA_SET_REPRESENTATION_EDEFAULT.equals(dataSetRepresentation);
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (dataSetRepresentation: ");
        result.append(dataSetRepresentation);
        result.append(')');
        return result.toString();
    }

} //BaseSampleDataImpl
