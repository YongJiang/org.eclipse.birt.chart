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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.PaletteImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Series Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getQuery <em>Query</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSeriesPalette <em>Series Palette</em>}
 * </li>
 * <li>
 * {@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSeriesDefinitions <em>Series Definitions</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSeries <em>Series</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getGrouping <em>Grouping</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl#getSorting <em>Sorting</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class SeriesDefinitionImpl extends EObjectImpl implements SeriesDefinition
{

    /**
     * The cached value of the '{@link #getQuery() <em>Query</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getQuery()
     * @generated
     * @ordered
     */
    protected Query query = null;

    /**
     * The cached value of the '{@link #getSeriesPalette() <em>Series Palette</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSeriesPalette()
     * @generated
     * @ordered
     */
    protected Palette seriesPalette = null;

    /**
     * The cached value of the '{@link #getSeriesDefinitions() <em>Series Definitions</em>}' containment reference
     * list. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSeriesDefinitions()
     * @generated
     * @ordered
     */
    protected EList seriesDefinitions = null;

    /**
     * The cached value of the '{@link #getSeries() <em>Series</em>}' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getSeries()
     * @generated
     * @ordered
     */
    protected EList series = null;

    /**
     * The cached value of the '{@link #getGrouping() <em>Grouping</em>}' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getGrouping()
     * @generated
     * @ordered
     */
    protected SeriesGrouping grouping = null;

    /**
     * The default value of the '{@link #getSorting() <em>Sorting</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getSorting()
     * @generated
     * @ordered
     */
    protected static final SortOption SORTING_EDEFAULT = SortOption.ASCENDING_LITERAL;

    /**
     * The cached value of the '{@link #getSorting() <em>Sorting</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getSorting()
     * @generated
     * @ordered
     */
    protected SortOption sorting = SORTING_EDEFAULT;

    /**
     * This is true if the Sorting attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean sortingESet = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected SeriesDefinitionImpl()
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
        return DataPackage.eINSTANCE.getSeriesDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Query getQuery()
    {
        return query;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetQuery(Query newQuery, NotificationChain msgs)
    {
        Query oldQuery = query;
        query = newQuery;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                DataPackage.SERIES_DEFINITION__QUERY, oldQuery, newQuery);
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
    public void setQuery(Query newQuery)
    {
        if (newQuery != query)
        {
            NotificationChain msgs = null;
            if (query != null)
                msgs = ((InternalEObject) query).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - DataPackage.SERIES_DEFINITION__QUERY, null, msgs);
            if (newQuery != null)
                msgs = ((InternalEObject) newQuery).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - DataPackage.SERIES_DEFINITION__QUERY, null, msgs);
            msgs = basicSetQuery(newQuery, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__QUERY, newQuery,
                newQuery));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Palette getSeriesPalette()
    {
        return seriesPalette;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetSeriesPalette(Palette newSeriesPalette, NotificationChain msgs)
    {
        Palette oldSeriesPalette = seriesPalette;
        seriesPalette = newSeriesPalette;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                DataPackage.SERIES_DEFINITION__SERIES_PALETTE, oldSeriesPalette, newSeriesPalette);
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
    public void setSeriesPalette(Palette newSeriesPalette)
    {
        if (newSeriesPalette != seriesPalette)
        {
            NotificationChain msgs = null;
            if (seriesPalette != null)
                msgs = ((InternalEObject) seriesPalette).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - DataPackage.SERIES_DEFINITION__SERIES_PALETTE, null, msgs);
            if (newSeriesPalette != null)
                msgs = ((InternalEObject) newSeriesPalette).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - DataPackage.SERIES_DEFINITION__SERIES_PALETTE, null, msgs);
            msgs = basicSetSeriesPalette(newSeriesPalette, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__SERIES_PALETTE,
                newSeriesPalette, newSeriesPalette));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EList getSeriesDefinitions()
    {
        if (seriesDefinitions == null)
        {
            seriesDefinitions = new EObjectContainmentEList(SeriesDefinition.class, this,
                DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS);
        }
        return seriesDefinitions;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EList getSeries()
    {
        if (series == null)
        {
            series = new EObjectContainmentEList(Series.class, this, DataPackage.SERIES_DEFINITION__SERIES);
        }
        return series;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public SeriesGrouping getGrouping()
    {
        return grouping;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetGrouping(SeriesGrouping newGrouping, NotificationChain msgs)
    {
        SeriesGrouping oldGrouping = grouping;
        grouping = newGrouping;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                DataPackage.SERIES_DEFINITION__GROUPING, oldGrouping, newGrouping);
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
    public void setGrouping(SeriesGrouping newGrouping)
    {
        if (newGrouping != grouping)
        {
            NotificationChain msgs = null;
            if (grouping != null)
                msgs = ((InternalEObject) grouping).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - DataPackage.SERIES_DEFINITION__GROUPING, null, msgs);
            if (newGrouping != null)
                msgs = ((InternalEObject) newGrouping).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - DataPackage.SERIES_DEFINITION__GROUPING, null, msgs);
            msgs = basicSetGrouping(newGrouping, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__GROUPING, newGrouping,
                newGrouping));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public SortOption getSorting()
    {
        return sorting;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setSorting(SortOption newSorting)
    {
        SortOption oldSorting = sorting;
        sorting = newSorting == null ? SORTING_EDEFAULT : newSorting;
        boolean oldSortingESet = sortingESet;
        sortingESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_DEFINITION__SORTING, oldSorting,
                sorting, !oldSortingESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetSorting()
    {
        SortOption oldSorting = sorting;
        boolean oldSortingESet = sortingESet;
        sorting = SORTING_EDEFAULT;
        sortingESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_DEFINITION__SORTING, oldSorting,
                SORTING_EDEFAULT, oldSortingESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetSorting()
    {
        return sortingESet;
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
                case DataPackage.SERIES_DEFINITION__QUERY:
                    return basicSetQuery(null, msgs);
                case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
                    return basicSetSeriesPalette(null, msgs);
                case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
                    return ((InternalEList) getSeriesDefinitions()).basicRemove(otherEnd, msgs);
                case DataPackage.SERIES_DEFINITION__SERIES:
                    return ((InternalEList) getSeries()).basicRemove(otherEnd, msgs);
                case DataPackage.SERIES_DEFINITION__GROUPING:
                    return basicSetGrouping(null, msgs);
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
            case DataPackage.SERIES_DEFINITION__QUERY:
                return getQuery();
            case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
                return getSeriesPalette();
            case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
                return getSeriesDefinitions();
            case DataPackage.SERIES_DEFINITION__SERIES:
                return getSeries();
            case DataPackage.SERIES_DEFINITION__GROUPING:
                return getGrouping();
            case DataPackage.SERIES_DEFINITION__SORTING:
                return getSorting();
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
            case DataPackage.SERIES_DEFINITION__QUERY:
                setQuery((Query) newValue);
                return;
            case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
                setSeriesPalette((Palette) newValue);
                return;
            case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
                getSeriesDefinitions().clear();
                getSeriesDefinitions().addAll((Collection) newValue);
                return;
            case DataPackage.SERIES_DEFINITION__SERIES:
                getSeries().clear();
                getSeries().addAll((Collection) newValue);
                return;
            case DataPackage.SERIES_DEFINITION__GROUPING:
                setGrouping((SeriesGrouping) newValue);
                return;
            case DataPackage.SERIES_DEFINITION__SORTING:
                setSorting((SortOption) newValue);
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
            case DataPackage.SERIES_DEFINITION__QUERY:
                setQuery((Query) null);
                return;
            case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
                setSeriesPalette((Palette) null);
                return;
            case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
                getSeriesDefinitions().clear();
                return;
            case DataPackage.SERIES_DEFINITION__SERIES:
                getSeries().clear();
                return;
            case DataPackage.SERIES_DEFINITION__GROUPING:
                setGrouping((SeriesGrouping) null);
                return;
            case DataPackage.SERIES_DEFINITION__SORTING:
                unsetSorting();
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
            case DataPackage.SERIES_DEFINITION__QUERY:
                return query != null;
            case DataPackage.SERIES_DEFINITION__SERIES_PALETTE:
                return seriesPalette != null;
            case DataPackage.SERIES_DEFINITION__SERIES_DEFINITIONS:
                return seriesDefinitions != null && !seriesDefinitions.isEmpty();
            case DataPackage.SERIES_DEFINITION__SERIES:
                return series != null && !series.isEmpty();
            case DataPackage.SERIES_DEFINITION__GROUPING:
                return grouping != null;
            case DataPackage.SERIES_DEFINITION__SORTING:
                return isSetSorting();
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
        result.append(" (sorting: ");
        if (sortingESet)
            result.append(sorting);
        else
            result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /**
     * A convenience method provided to create a series definition instance and initialize its member variables
     * 
     * NOTE: Manually written
     * 
     * @return
     */
    public static final SeriesDefinition create()
    {
        final SeriesDefinition sd = DataFactory.eINSTANCE.createSeriesDefinition();
        sd.setQuery(QueryImpl.create(IConstants.EMPTY_STRING));
        sd.setSeriesPalette(PaletteImpl.create(ColorDefinitionImpl.GREY()));
        return sd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getDesignTimeSeries()
     */
    public final Series getDesignTimeSeries()
    {
        final EList el = getSeries();
        Series se;
        for (int i = 0; i < el.size(); i++)
        {
            se = (Series) el.get(i);
            if (se.getDataSet() == null)
            {
                return se;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.data.SeriesDefinition#getRunTimeSeries()
     */
    public final ArrayList getRunTimeSeries()
    {
        final ArrayList alRTS = new ArrayList();
        final EList el = getSeries();
        Series se;
        for (int i = 0; i < el.size(); i++)
        {
            se = (Series) el.get(i);
            if (se.getDataSet() != null)
            {
                alRTS.add(se);
            }
        }
        return alRTS;
    }
} //SeriesDefinitionImpl
