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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Size</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type holds dimensions.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Size#getHeight <em>Height</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Size#getWidth <em>Width</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getSize()
 * @model
 * @generated
 */
public interface Size extends EObject
{

    /**
     * Returns the value of the '<em><b>Height</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Holds the height of the chart
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Height</em>' attribute.
     * @see #isSetHeight()
     * @see #unsetHeight()
     * @see #setHeight(double)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getSize_Height()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     * @generated
     */
    double getHeight();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Size#getHeight <em>Height</em>}'
     * attribute. <!-- begin-user-doc --> Sets the height component of the size. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Height</em>' attribute.
     * @see #isSetHeight()
     * @see #unsetHeight()
     * @see #getHeight()
     * @generated
     */
    void setHeight(double value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Size#getHeight <em>Height</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetHeight()
     * @see #getHeight()
     * @see #setHeight(double)
     * @generated
     */
    void unsetHeight();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.attribute.Size#getHeight <em>Height</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Height</em>' attribute is set.
     * @see #unsetHeight()
     * @see #getHeight()
     * @see #setHeight(double)
     * @generated
     */
    boolean isSetHeight();

    /**
     * Returns the value of the '<em><b>Width</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Holds the width of the chart
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Width</em>' attribute.
     * @see #isSetWidth()
     * @see #unsetWidth()
     * @see #setWidth(double)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getSize_Width()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     * @generated
     */
    double getWidth();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Size#getWidth <em>Width</em>}' attribute.
     * <!-- begin-user-doc --> Sets the width component of the size. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Width</em>' attribute.
     * @see #isSetWidth()
     * @see #unsetWidth()
     * @see #getWidth()
     * @generated
     */
    void setWidth(double value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Size#getWidth <em>Width</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetWidth()
     * @see #getWidth()
     * @see #setWidth(double)
     * @generated
     */
    void unsetWidth();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.attribute.Size#getWidth <em>Width</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Width</em>' attribute is set.
     * @see #unsetWidth()
     * @see #getWidth()
     * @see #setWidth(double)
     * @generated
     */
    boolean isSetWidth();

    /**
     * Scales the size instance as specified via the 'dScale' parameter
     * 
     * @param dScale
     */
    void scale(double dScale);
} // Size
