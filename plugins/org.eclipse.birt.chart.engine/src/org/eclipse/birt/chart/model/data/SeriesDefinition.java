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

package org.eclipse.birt.chart.model.data;

import java.util.List;

import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Series Definition</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type represents design-time definition for a series.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery <em>Query</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette <em>Series Palette</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesDefinitions <em>Series Definitions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier <em>Format Specifier</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeries <em>Series</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping <em>Grouping</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting <em>Sorting</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition()
 * @model extendedMetaData="name='SeriesDefinition' kind='elementOnly'"
 * @generated
 */
public interface SeriesDefinition extends EObject
{

	/**
	 * Returns the value of the '<em><b>Query</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Query</em>' containment reference.
	 * @see #setQuery(Query)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Query()
	 * @model containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='Query'"
	 * @generated
	 */
	Query getQuery( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getQuery <em>Query</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Query</em>' containment
	 *            reference.
	 * @see #getQuery()
	 * @generated
	 */
	void setQuery( Query value );

	/**
	 * Returns the value of the '<em><b>Series Palette</b></em>'
	 * containment reference. <!-- begin-user-doc --> Gets the palette
	 * associated with the series definiton instance. This palette will be used
	 * to determine the sequence of colors for the series that are represented
	 * by this definition. <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Series Palette</em>' containment
	 *         reference.
	 * @see #setSeriesPalette(Palette)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_SeriesPalette()
	 * @model containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='SeriesPalette'"
	 * @generated
	 */
	Palette getSeriesPalette( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSeriesPalette <em>Series Palette</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Series Palette</em>' containment
	 *            reference.
	 * @see #getSeriesPalette()
	 * @generated
	 */
	void setSeriesPalette( Palette value );

	/**
	 * Returns the value of the '<em><b>Series Definitions</b></em>'
	 * containment reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.SeriesDefinition}. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Series Definitions</em>' containment
	 *         reference list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_SeriesDefinitions()
	 * @model type="org.eclipse.birt.chart.model.data.SeriesDefinition"
	 *        containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='SeriesDefinitions'"
	 * @generated
	 */
	EList getSeriesDefinitions( );

	/**
	 * Returns the value of the '<em><b>Format Specifier</b></em>'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Format Specifier</em>' containment
	 *         reference.
	 * @see #setFormatSpecifier(FormatSpecifier)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_FormatSpecifier()
	 * @model containment="true" resolveProxies="false" required="true"
	 *        extendedMetaData="kind='element' name='FormatSpecifier'"
	 * @generated
	 */
	FormatSpecifier getFormatSpecifier( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getFormatSpecifier <em>Format Specifier</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Format Specifier</em>'
	 *            containment reference.
	 * @see #getFormatSpecifier()
	 * @generated
	 */
	void setFormatSpecifier( FormatSpecifier value );

	/**
	 * Returns the value of the '<em><b>Series</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.component.Series}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds all the series for the chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Series</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Series()
	 * @model type="org.eclipse.birt.chart.model.component.Series"
	 *        containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList getSeries( );

	/**
	 * Returns the value of the '<em><b>Grouping</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Defines if and how the series data is to be grouped for display. This
	 * should only be applied to Base Series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Grouping</em>' containment reference.
	 * @see #setGrouping(SeriesGrouping)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Grouping()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	SeriesGrouping getGrouping( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getGrouping <em>Grouping</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Grouping</em>' containment
	 *            reference.
	 * @see #getGrouping()
	 * @generated
	 */
	void setGrouping( SeriesGrouping value );

	/**
	 * Returns the value of the '<em><b>Sorting</b></em>' attribute. The
	 * default value is <code>"Ascending"</code>. The literals are from the
	 * enumeration {@link org.eclipse.birt.chart.model.attribute.SortOption}.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines if and how the series data is to be sorted for display. This
	 * should only be applied to Base Series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Sorting</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see #isSetSorting()
	 * @see #unsetSorting()
	 * @see #setSorting(SortOption)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesDefinition_Sorting()
	 * @model default="Ascending" unique="false" unsettable="true"
	 * @generated
	 */
	SortOption getSorting( );

	/**
	 * Sets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting <em>Sorting</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Sorting</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see #isSetSorting()
	 * @see #unsetSorting()
	 * @see #getSorting()
	 * @generated
	 */
	void setSorting( SortOption value );

	/**
	 * Unsets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting <em>Sorting</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetSorting()
	 * @see #getSorting()
	 * @see #setSorting(SortOption)
	 * @generated
	 */
	void unsetSorting( );

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.data.SeriesDefinition#getSorting <em>Sorting</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Sorting</em>' attribute is set.
	 * @see #unsetSorting()
	 * @see #getSorting()
	 * @see #setSorting(SortOption)
	 * @generated
	 */
	boolean isSetSorting( );

	/**
	 * 
	 * @return The design-time series associated with the series definition
	 */
	Series getDesignTimeSeries( );

	/**
	 * 
	 * @return The runtime-time series' associated with the series definition
	 */
	List getRunTimeSeries( );

	/**
	 * 
	 */
	static final SeriesDefinition[] EMPTY_ARRAY = new SeriesDefinition[0];

} // SeriesDefinition
