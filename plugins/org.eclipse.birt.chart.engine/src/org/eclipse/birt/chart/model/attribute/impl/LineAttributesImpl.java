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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Line Attributes</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl#getStyle <em>Style</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl#getThickness <em>Thickness</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl#getColor <em>Color</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl#isVisible <em>Visible</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class LineAttributesImpl extends EObjectImpl implements LineAttributes
{

    /**
     * The default value of the '{@link #getStyle() <em>Style</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected static final LineStyle STYLE_EDEFAULT = LineStyle.SOLID_LITERAL;

    /**
     * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected LineStyle style = STYLE_EDEFAULT;

    /**
     * This is true if the Style attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean styleESet = false;

    /**
     * The default value of the '{@link #getThickness() <em>Thickness</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getThickness()
     * @generated
     * @ordered
     */
    protected static final int THICKNESS_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getThickness() <em>Thickness</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getThickness()
     * @generated
     * @ordered
     */
    protected int thickness = THICKNESS_EDEFAULT;

    /**
     * This is true if the Thickness attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean thicknessESet = false;

    /**
     * The cached value of the '{@link #getColor() <em>Color</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getColor()
     * @generated
     * @ordered
     */
    protected ColorDefinition color = null;

    /**
     * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isVisible()
     * @generated
     * @ordered
     */
    protected static final boolean VISIBLE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isVisible()
     * @generated
     * @ordered
     */
    protected boolean visible = VISIBLE_EDEFAULT;

    /**
     * This is true if the Visible attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean visibleESet = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected LineAttributesImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected EClass eStaticClass()
    {
        return AttributePackage.eINSTANCE.getLineAttributes();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LineStyle getStyle()
    {
        return style;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setStyle(LineStyle newStyle)
    {
        LineStyle oldStyle = style;
        style = newStyle == null ? STYLE_EDEFAULT : newStyle;
        boolean oldStyleESet = styleESet;
        styleESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.LINE_ATTRIBUTES__STYLE, oldStyle,
                style, !oldStyleESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetStyle()
    {
        LineStyle oldStyle = style;
        boolean oldStyleESet = styleESet;
        style = STYLE_EDEFAULT;
        styleESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.LINE_ATTRIBUTES__STYLE, oldStyle,
                STYLE_EDEFAULT, oldStyleESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetStyle()
    {
        return styleESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getThickness()
    {
        return thickness;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setThickness(int newThickness)
    {
        int oldThickness = thickness;
        thickness = newThickness;
        boolean oldThicknessESet = thicknessESet;
        thicknessESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.LINE_ATTRIBUTES__THICKNESS,
                oldThickness, thickness, !oldThicknessESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetThickness()
    {
        int oldThickness = thickness;
        boolean oldThicknessESet = thicknessESet;
        thickness = THICKNESS_EDEFAULT;
        thicknessESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.LINE_ATTRIBUTES__THICKNESS,
                oldThickness, THICKNESS_EDEFAULT, oldThicknessESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetThickness()
    {
        return thicknessESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ColorDefinition getColor()
    {
        return color;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetColor(ColorDefinition newColor, NotificationChain msgs)
    {
        ColorDefinition oldColor = color;
        color = newColor;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                AttributePackage.LINE_ATTRIBUTES__COLOR, oldColor, newColor);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setColor(ColorDefinition newColor)
    {
        if (newColor != color)
        {
            NotificationChain msgs = null;
            if (color != null)
                msgs = ((InternalEObject) color).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - AttributePackage.LINE_ATTRIBUTES__COLOR, null, msgs);
            if (newColor != null)
                msgs = ((InternalEObject) newColor).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - AttributePackage.LINE_ATTRIBUTES__COLOR, null, msgs);
            msgs = basicSetColor(newColor, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.LINE_ATTRIBUTES__COLOR, newColor,
                newColor));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setVisible(boolean newVisible)
    {
        boolean oldVisible = visible;
        visible = newVisible;
        boolean oldVisibleESet = visibleESet;
        visibleESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.LINE_ATTRIBUTES__VISIBLE,
                oldVisible, visible, !oldVisibleESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetVisible()
    {
        boolean oldVisible = visible;
        boolean oldVisibleESet = visibleESet;
        visible = VISIBLE_EDEFAULT;
        visibleESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.LINE_ATTRIBUTES__VISIBLE,
                oldVisible, VISIBLE_EDEFAULT, oldVisibleESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetVisible()
    {
        return visibleESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass,
        NotificationChain msgs)
    {
        if (featureID >= 0)
        {
            switch (eDerivedStructuralFeatureID(featureID, baseClass))
            {
                case AttributePackage.LINE_ATTRIBUTES__COLOR:
                    return basicSetColor(null, msgs);
                default:
                    return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
            }
        }
        return eBasicSetContainer(null, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.LINE_ATTRIBUTES__STYLE:
                return getStyle();
            case AttributePackage.LINE_ATTRIBUTES__THICKNESS:
                return new Integer(getThickness());
            case AttributePackage.LINE_ATTRIBUTES__COLOR:
                return getColor();
            case AttributePackage.LINE_ATTRIBUTES__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eSet(EStructuralFeature eFeature, Object newValue)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.LINE_ATTRIBUTES__STYLE:
                setStyle((LineStyle) newValue);
                return;
            case AttributePackage.LINE_ATTRIBUTES__THICKNESS:
                setThickness(((Integer) newValue).intValue());
                return;
            case AttributePackage.LINE_ATTRIBUTES__COLOR:
                setColor((ColorDefinition) newValue);
                return;
            case AttributePackage.LINE_ATTRIBUTES__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eUnset(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.LINE_ATTRIBUTES__STYLE:
                unsetStyle();
                return;
            case AttributePackage.LINE_ATTRIBUTES__THICKNESS:
                unsetThickness();
                return;
            case AttributePackage.LINE_ATTRIBUTES__COLOR:
                setColor((ColorDefinition) null);
                return;
            case AttributePackage.LINE_ATTRIBUTES__VISIBLE:
                unsetVisible();
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean eIsSet(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.LINE_ATTRIBUTES__STYLE:
                return isSetStyle();
            case AttributePackage.LINE_ATTRIBUTES__THICKNESS:
                return isSetThickness();
            case AttributePackage.LINE_ATTRIBUTES__COLOR:
                return color != null;
            case AttributePackage.LINE_ATTRIBUTES__VISIBLE:
                return isSetVisible();
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String toString()
    {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (style: ");
        if (styleESet)
            result.append(style);
        else
            result.append("<unset>");
        result.append(", thickness: ");
        if (thicknessESet)
            result.append(thickness);
        else
            result.append("<unset>");
        result.append(", visible: ");
        if (visibleESet)
            result.append(visible);
        else
            result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /**
     * A convenient method that facilitates initialization of member variables.
     * 
     * Note: Manually written
     * 
     * @param cd
     * @param ls
     * @param iThickness
     */
    public void set(ColorDefinition cd, LineStyle ls, int iThickness)
    {
        setColor(cd);
        setStyle(ls);
        setThickness(iThickness);
    }

    /**
     * A convenient method that facilitates implicit initialization of member variables in creation of an instance.
     * 
     * Note: Manually written
     * 
     * @param cd
     * @param ls
     * @param iThickness
     * 
     * @return
     */
    public static final LineAttributes create(ColorDefinition cd, LineStyle ls, int iThickness)
    {
        final LineAttributes la = AttributeFactory.eINSTANCE.createLineAttributes();
        la.setColor(cd);
        la.setStyle(ls);
        la.setThickness(iThickness);
        la.setVisible(true);
        return la;
    }
} //LineAttributesImpl
