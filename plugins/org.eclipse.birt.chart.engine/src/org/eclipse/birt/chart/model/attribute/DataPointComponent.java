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

package org.eclipse.birt.chart.model.attribute;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Data Point Component</b></em>'. <!--
 * end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type represents the data value being displayed in the plot area for each data value.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getFormatSpecifier <em>Format Specifier</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDataPointComponent()
 * @model @generated
 */
public interface DataPointComponent extends EObject
{

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute. The default value is <code>"Base_Value"</code>.
     * The literals are from the enumeration {@link org.eclipse.birt.chart.model.attribute.DataPointComponentType}.
     * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the type of data point component.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Type</em>' attribute.
     * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
     * @see #isSetType()
     * @see #unsetType()
     * @see #setType(DataPointComponentType)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDataPointComponent_Type()
     * @model default="Base_Value" unique="false" unsettable="true" required="true"
     * @generated
     */
    DataPointComponentType getType();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getType <em>Type</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Type</em>' attribute.
     * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
     * @see #isSetType()
     * @see #unsetType()
     * @see #getType()
     * @generated
     */
    void setType(DataPointComponentType value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getType <em>Type</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetType()
     * @see #getType()
     * @see #setType(DataPointComponentType)
     * @generated
     */
    void unsetType();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getType <em>Type</em>}' attribute is set. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Type</em>' attribute is set.
     * @see #unsetType()
     * @see #getType()
     * @see #setType(DataPointComponentType)
     * @generated
     */
    boolean isSetType();

    /**
     * Returns the value of the '<em><b>Format Specifier</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the format specifier to be used for this data point component in the data label.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Format Specifier</em>' containment reference.
     * @see #setFormatSpecifier(FormatSpecifier)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDataPointComponent_FormatSpecifier()
     * @model containment="true" resolveProxies="false" required="true"
     * @generated
     */
    FormatSpecifier getFormatSpecifier();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getFormatSpecifier <em>Format Specifier</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Format Specifier</em>' containment reference.
     * @see #getFormatSpecifier()
     * @generated
     */
    void setFormatSpecifier(FormatSpecifier value);

} // DataPointComponent
