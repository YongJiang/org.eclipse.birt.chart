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

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Tooltip Value</b></em>'. <!-- end-user-doc
 * -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines the value for a 'Show_Tooltip' action.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getText <em>Text</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay <em>Delay</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTooltipValue()
 * @model @generated
 */
public interface TooltipValue extends ActionValue
{

    /**
     * Returns the value of the '<em><b>Text</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Holds the text for the tooltip.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Text</em>' attribute.
     * @see #setText(String)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTooltipValue_Text()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getText();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getText <em>Text</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Text</em>' attribute.
     * @see #getText()
     * @generated
     */
    void setText(String value);

    /**
     * Returns the value of the '<em><b>Delay</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * Specifies the delay afer which the tooltip is to be shown.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Delay</em>' attribute.
     * @see #isSetDelay()
     * @see #unsetDelay()
     * @see #setDelay(int)
     * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTooltipValue_Delay()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
     * @generated
     */
    int getDelay();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay <em>Delay</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Delay</em>' attribute.
     * @see #isSetDelay()
     * @see #unsetDelay()
     * @see #getDelay()
     * @generated
     */
    void setDelay(int value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay <em>Delay</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetDelay()
     * @see #getDelay()
     * @see #setDelay(int)
     * @generated
     */
    void unsetDelay();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay <em>Delay</em>}' attribute is set. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Delay</em>' attribute is set.
     * @see #unsetDelay()
     * @see #getDelay()
     * @see #setDelay(int)
     * @generated
     */
    boolean isSetDelay();

} // TooltipValue
