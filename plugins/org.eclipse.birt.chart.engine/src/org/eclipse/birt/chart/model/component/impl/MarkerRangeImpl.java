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

package org.eclipse.birt.chart.model.component.impl;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Marker Range</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getOutline <em>Outline</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getFill <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getStartPosition <em>Start Position</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getEndPosition <em>End Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getAssociatedLabel <em>Associated Label</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl#getLabelPosition <em>Label Position</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MarkerRangeImpl extends EObjectImpl implements MarkerRange
{

    /**
     * The cached value of the '{@link #getOutline() <em>Outline</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getOutline()
     * @generated
     * @ordered
     */
    protected LineAttributes outline = null;

    /**
     * The cached value of the '{@link #getFill() <em>Fill</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getFill()
     * @generated
     * @ordered
     */
    protected Fill fill = null;

    /**
     * The cached value of the '{@link #getStartValue() <em>Start Value</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getStartValue()
     * @generated
     * @ordered
     */
    protected DataElement startValue = null;

    /**
     * The cached value of the '{@link #getEndValue() <em>End Value</em>}' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getEndValue()
     * @generated
     * @ordered
     */
    protected DataElement endValue = null;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getLabel()
     * @generated
     * @ordered
     */
    protected Label label = null;

    /**
     * The default value of the '{@link #getLabelAnchor() <em>Label Anchor</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getLabelAnchor()
     * @generated
     * @ordered
     */
    protected static final Anchor LABEL_ANCHOR_EDEFAULT = Anchor.NORTH_LITERAL;

    /**
     * The cached value of the '{@link #getLabelAnchor() <em>Label Anchor</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getLabelAnchor()
     * @generated
     * @ordered
     */
    protected Anchor labelAnchor = LABEL_ANCHOR_EDEFAULT;

    /**
     * This is true if the Label Anchor attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean labelAnchorESet = false;

    /**
     * The default value of the '{@link #getLabelPosition() <em>Label Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getLabelPosition()
     * @generated
     * @ordered
     */
    protected static final Position LABEL_POSITION_EDEFAULT = Position.ABOVE_LITERAL;

    /**
     * The cached value of the '{@link #getLabelPosition() <em>Label Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getLabelPosition()
     * @generated
     * @ordered
     */
    protected Position labelPosition = LABEL_POSITION_EDEFAULT;

    /**
     * This is true if the Label Position attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean labelPositionESet = false;

    /**
     * The cached value of the '{@link #getFormatSpecifier() <em>Format Specifier</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getFormatSpecifier()
     * @generated
     * @ordered
     */
    protected FormatSpecifier formatSpecifier = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MarkerRangeImpl()
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
        return ComponentPackage.eINSTANCE.getMarkerRange();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LineAttributes getOutline()
    {
        return outline;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetOutline(LineAttributes newOutline, NotificationChain msgs)
    {
        LineAttributes oldOutline = outline;
        outline = newOutline;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__OUTLINE, oldOutline, newOutline);
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
    public void setOutline(LineAttributes newOutline)
    {
        if (newOutline != outline)
        {
            NotificationChain msgs = null;
            if (outline != null)
                msgs = ((InternalEObject) outline).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__OUTLINE, null, msgs);
            if (newOutline != null)
                msgs = ((InternalEObject) newOutline).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__OUTLINE, null, msgs);
            msgs = basicSetOutline(newOutline, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__OUTLINE, newOutline,
                newOutline));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Fill getFill()
    {
        return fill;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetFill(Fill newFill, NotificationChain msgs)
    {
        Fill oldFill = fill;
        fill = newFill;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__FILL, oldFill, newFill);
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
    public void setFill(Fill newFill)
    {
        if (newFill != fill)
        {
            NotificationChain msgs = null;
            if (fill != null)
                msgs = ((InternalEObject) fill).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__FILL, null, msgs);
            if (newFill != null)
                msgs = ((InternalEObject) newFill).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__FILL, null, msgs);
            msgs = basicSetFill(newFill, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__FILL, newFill, newFill));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public DataElement getStartValue()
    {
        return startValue;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetStartValue(DataElement newStartValue, NotificationChain msgs)
    {
        DataElement oldStartValue = startValue;
        startValue = newStartValue;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__START_VALUE, oldStartValue, newStartValue);
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
    public void setStartValue(DataElement newStartValue)
    {
        if (newStartValue != startValue)
        {
            NotificationChain msgs = null;
            if (startValue != null)
                msgs = ((InternalEObject) startValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__START_VALUE, null, msgs);
            if (newStartValue != null)
                msgs = ((InternalEObject) newStartValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__START_VALUE, null, msgs);
            msgs = basicSetStartValue(newStartValue, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__START_VALUE,
                newStartValue, newStartValue));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public DataElement getEndValue()
    {
        return endValue;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetEndValue(DataElement newEndValue, NotificationChain msgs)
    {
        DataElement oldEndValue = endValue;
        endValue = newEndValue;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__END_VALUE, oldEndValue, newEndValue);
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
    public void setEndValue(DataElement newEndValue)
    {
        if (newEndValue != endValue)
        {
            NotificationChain msgs = null;
            if (endValue != null)
                msgs = ((InternalEObject) endValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__END_VALUE, null, msgs);
            if (newEndValue != null)
                msgs = ((InternalEObject) newEndValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__END_VALUE, null, msgs);
            msgs = basicSetEndValue(newEndValue, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__END_VALUE,
                newEndValue, newEndValue));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Label getLabel()
    {
        return label;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs)
    {
        Label oldLabel = label;
        label = newLabel;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__LABEL, oldLabel, newLabel);
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
    public void setLabel(Label newLabel)
    {
        if (newLabel != label)
        {
            NotificationChain msgs = null;
            if (label != null)
                msgs = ((InternalEObject) label).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__LABEL, null, msgs);
            if (newLabel != null)
                msgs = ((InternalEObject) newLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__LABEL, null, msgs);
            msgs = basicSetLabel(newLabel, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__LABEL, newLabel,
                newLabel));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Anchor getLabelAnchor()
    {
        return labelAnchor;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setLabelAnchor(Anchor newLabelAnchor)
    {
        Anchor oldLabelAnchor = labelAnchor;
        labelAnchor = newLabelAnchor == null ? LABEL_ANCHOR_EDEFAULT : newLabelAnchor;
        boolean oldLabelAnchorESet = labelAnchorESet;
        labelAnchorESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__LABEL_ANCHOR,
                oldLabelAnchor, labelAnchor, !oldLabelAnchorESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetLabelAnchor()
    {
        Anchor oldLabelAnchor = labelAnchor;
        boolean oldLabelAnchorESet = labelAnchorESet;
        labelAnchor = LABEL_ANCHOR_EDEFAULT;
        labelAnchorESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.MARKER_RANGE__LABEL_ANCHOR,
                oldLabelAnchor, LABEL_ANCHOR_EDEFAULT, oldLabelAnchorESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetLabelAnchor()
    {
        return labelAnchorESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Position getLabelPosition()
    {
        return labelPosition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setLabelPosition(Position newLabelPosition)
    {
        Position oldLabelPosition = labelPosition;
        labelPosition = newLabelPosition == null ? LABEL_POSITION_EDEFAULT : newLabelPosition;
        boolean oldLabelPositionESet = labelPositionESet;
        labelPositionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__LABEL_POSITION,
                oldLabelPosition, labelPosition, !oldLabelPositionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetLabelPosition()
    {
        Position oldLabelPosition = labelPosition;
        boolean oldLabelPositionESet = labelPositionESet;
        labelPosition = LABEL_POSITION_EDEFAULT;
        labelPositionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.MARKER_RANGE__LABEL_POSITION,
                oldLabelPosition, LABEL_POSITION_EDEFAULT, oldLabelPositionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetLabelPosition()
    {
        return labelPositionESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public FormatSpecifier getFormatSpecifier()
    {
        return formatSpecifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetFormatSpecifier(FormatSpecifier newFormatSpecifier, NotificationChain msgs)
    {
        FormatSpecifier oldFormatSpecifier = formatSpecifier;
        formatSpecifier = newFormatSpecifier;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
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
    public void setFormatSpecifier(FormatSpecifier newFormatSpecifier)
    {
        if (newFormatSpecifier != formatSpecifier)
        {
            NotificationChain msgs = null;
            if (formatSpecifier != null)
                msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER, null, msgs);
            if (newFormatSpecifier != null)
                msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER, null, msgs);
            msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER,
                newFormatSpecifier, newFormatSpecifier));
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
                case ComponentPackage.MARKER_RANGE__OUTLINE:
                    return basicSetOutline(null, msgs);
                case ComponentPackage.MARKER_RANGE__FILL:
                    return basicSetFill(null, msgs);
                case ComponentPackage.MARKER_RANGE__START_VALUE:
                    return basicSetStartValue(null, msgs);
                case ComponentPackage.MARKER_RANGE__END_VALUE:
                    return basicSetEndValue(null, msgs);
                case ComponentPackage.MARKER_RANGE__LABEL:
                    return basicSetLabel(null, msgs);
                case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
                    return basicSetFormatSpecifier(null, msgs);
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                return getOutline();
            case ComponentPackage.MARKER_RANGE__FILL:
                return getFill();
            case ComponentPackage.MARKER_RANGE__START_VALUE:
                return getStartValue();
            case ComponentPackage.MARKER_RANGE__END_VALUE:
                return getEndValue();
            case ComponentPackage.MARKER_RANGE__LABEL:
                return getLabel();
            case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
                return getLabelAnchor();
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
                return getLabelPosition();
            case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
                return getFormatSpecifier();
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                setOutline((LineAttributes) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__FILL:
                setFill((Fill) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__START_VALUE:
                setStartValue((DataElement) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__END_VALUE:
                setEndValue((DataElement) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__LABEL:
                setLabel((Label) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
                setLabelAnchor((Anchor) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
                setLabelPosition((Position) newValue);
                return;
            case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
                setFormatSpecifier((FormatSpecifier) newValue);
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                setOutline((LineAttributes) null);
                return;
            case ComponentPackage.MARKER_RANGE__FILL:
                setFill((Fill) null);
                return;
            case ComponentPackage.MARKER_RANGE__START_VALUE:
                setStartValue((DataElement) null);
                return;
            case ComponentPackage.MARKER_RANGE__END_VALUE:
                setEndValue((DataElement) null);
                return;
            case ComponentPackage.MARKER_RANGE__LABEL:
                setLabel((Label) null);
                return;
            case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
                unsetLabelAnchor();
                return;
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
                unsetLabelPosition();
                return;
            case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
                setFormatSpecifier((FormatSpecifier) null);
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
            case ComponentPackage.MARKER_RANGE__OUTLINE:
                return outline != null;
            case ComponentPackage.MARKER_RANGE__FILL:
                return fill != null;
            case ComponentPackage.MARKER_RANGE__START_VALUE:
                return startValue != null;
            case ComponentPackage.MARKER_RANGE__END_VALUE:
                return endValue != null;
            case ComponentPackage.MARKER_RANGE__LABEL:
                return label != null;
            case ComponentPackage.MARKER_RANGE__LABEL_ANCHOR:
                return isSetLabelAnchor();
            case ComponentPackage.MARKER_RANGE__LABEL_POSITION:
                return isSetLabelPosition();
            case ComponentPackage.MARKER_RANGE__FORMAT_SPECIFIER:
                return formatSpecifier != null;
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
        result.append(" (labelAnchor: ");
        if (labelAnchorESet)
            result.append(labelAnchor);
        else
            result.append("<unset>");
        result.append(", labelPosition: ");
        if (labelPositionESet)
            result.append(labelPosition);
        else
            result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /**
     * A convenience method provided to add a marker range instance to an axis
     * 
     * @param ax
     * @param de
     */
    public static final MarkerRange create(Axis ax, DataElement deStart, DataElement deEnd, Fill f)
    {
        final MarkerRange mr = ComponentFactory.eINSTANCE.createMarkerRange();
        final LineAttributes liaOutline = LineAttributesImpl.create(null, LineStyle.SOLID_LITERAL, 1);
        mr.setOutline(liaOutline);
        mr.setFill(f);
        mr.setStartValue(deStart);
        mr.setEndValue(deEnd);
        mr.setLabel(LabelImpl.create());
        /*
         * mr.setAnchor( ax.getOrientation().getValue() == Orientation.HORIZONTAL ? Anchor.NORTH_EAST_LITERAL :
         * Anchor.NORTH_WEST_LITERAL );
         */
        ax.getMarkerRanges().add(mr);
        //mr.setFormatSpecifier(ax.getFormatSpecifier());
        return mr;
    }

} //MarkerRangeImpl
