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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Insets</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines the insets for an element.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Insets#getTop <em>Top</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Insets#getLeft <em>Left</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Insets#getBottom <em>Bottom</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.Insets#getRight <em>Right</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInsets()
 * @model @generated
 */
public interface Insets extends EObject
{

    /**
     * Returns the value of the '<em><b>Top</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
     * begin-model-doc -->
     * 
     * Specifies the top component of the insets.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Top</em>' attribute.
     * @see #isSetTop()
     * @see #unsetTop()
     * @see #setTop(double)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInsets_Top()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     * @generated
     */
    double getTop();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getTop <em>Top</em>}' attribute.
     * <!-- begin-user-doc --> Sets the top component of the insets. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Top</em>' attribute.
     * @see #isSetTop()
     * @see #unsetTop()
     * @see #getTop()
     * @generated
     */
    void setTop(double value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getTop <em>Top</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetTop()
     * @see #getTop()
     * @see #setTop(double)
     * @generated
     */
    void unsetTop();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getTop <em>Top</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Top</em>' attribute is set.
     * @see #unsetTop()
     * @see #getTop()
     * @see #setTop(double)
     * @generated
     */
    boolean isSetTop();

    /**
     * Returns the value of the '<em><b>Left</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Specifies the left component of the insets.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Left</em>' attribute.
     * @see #isSetLeft()
     * @see #unsetLeft()
     * @see #setLeft(double)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInsets_Left()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     * @generated
     */
    double getLeft();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getLeft <em>Left</em>}' attribute.
     * <!-- begin-user-doc --> Sets the left component of the insets. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Left</em>' attribute.
     * @see #isSetLeft()
     * @see #unsetLeft()
     * @see #getLeft()
     * @generated
     */
    void setLeft(double value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getLeft <em>Left</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetLeft()
     * @see #getLeft()
     * @see #setLeft(double)
     * @generated
     */
    void unsetLeft();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getLeft <em>Left</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Left</em>' attribute is set.
     * @see #unsetLeft()
     * @see #getLeft()
     * @see #setLeft(double)
     * @generated
     */
    boolean isSetLeft();

    /**
     * Returns the value of the '<em><b>Bottom</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Specifies the bottom component of the insets.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Bottom</em>' attribute.
     * @see #isSetBottom()
     * @see #unsetBottom()
     * @see #setBottom(double)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInsets_Bottom()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     * @generated
     */
    double getBottom();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getBottom <em>Bottom</em>}'
     * attribute. <!-- begin-user-doc --> Sets the bottom component of the insets. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Bottom</em>' attribute.
     * @see #isSetBottom()
     * @see #unsetBottom()
     * @see #getBottom()
     * @generated
     */
    void setBottom(double value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getBottom <em>Bottom</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetBottom()
     * @see #getBottom()
     * @see #setBottom(double)
     * @generated
     */
    void unsetBottom();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.Insets#getBottom <em>Bottom</em>}' attribute is set. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Bottom</em>' attribute is set.
     * @see #unsetBottom()
     * @see #getBottom()
     * @see #setBottom(double)
     * @generated
     */
    boolean isSetBottom();

    /**
     * Returns the value of the '<em><b>Right</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Specifies the right component of the insets.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Right</em>' attribute.
     * @see #isSetRight()
     * @see #unsetRight()
     * @see #setRight(double)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getInsets_Right()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     * @generated
     */
    double getRight();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getRight <em>Right</em>}'
     * attribute. <!-- begin-user-doc --> Sets the right component of the insets. <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Right</em>' attribute.
     * @see #isSetRight()
     * @see #unsetRight()
     * @see #getRight()
     * @generated
     */
    void setRight(double value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getRight <em>Right</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetRight()
     * @see #getRight()
     * @see #setRight(double)
     * @generated
     */
    void unsetRight();

    /**
     * Returns whether the value of the '{@link org.eclipse.birt.chart.model.attribute.Insets#getRight <em>Right</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Right</em>' attribute is set.
     * @see #unsetRight()
     * @see #getRight()
     * @see #setRight(double)
     * @generated
     */
    boolean isSetRight();

    /**
     * Returns a new instance with scaled members w.r.t this instance.
     * 
     * NOTE: Manually written
     * 
     * @param dScale
     * @return
     */
    Insets scaledInstance(double dScale);

    /**
     * A convenience method for setting all members of an existing instance
     * 
     * NOTE: Manually written
     * 
     * @param dTop
     * @param dLeft
     * @param dBottom
     * @param dRight
     */
    void set(double dTop, double dLeft, double dBottom, double dRight);

    /**
     * A convenience method provdided to detect if all members are below a certain value
     * 
     * NOTE: Manually written
     * 
     * @return
     */
    boolean areLessThan(double dValue);
} // Insets
