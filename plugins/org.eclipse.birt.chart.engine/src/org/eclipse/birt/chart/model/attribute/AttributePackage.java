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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package </b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * Schema file for the chart.model package.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributeFactory
 * @generated
 */
public interface AttributePackage extends EPackage
{

	/**
	 * The package name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "attribute"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.birt.eclipse.org/ChartModelAttribute"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "attribute"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	AttributePackage eINSTANCE = org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl.init( );

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.ActionValueImpl <em>Action Value</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.ActionValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionValue()
	 * @generated
	 */
	int ACTION_VALUE = 0;

	/**
	 * The number of structural features of the the '<em>Action Value</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_VALUE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl <em>Angle3 D</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAngle3D()
	 * @generated
	 */
	int ANGLE3_D = 1;

	/**
	 * The feature id for the '<em><b>XAngle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D__XANGLE = 0;

	/**
	 * The feature id for the '<em><b>YAngle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D__YANGLE = 1;

	/**
	 * The feature id for the '<em><b>ZAngle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D__ZANGLE = 2;

	/**
	 * The number of structural features of the the '<em>Angle3 D</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANGLE3_D_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl <em>Axis Origin</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisOrigin()
	 * @generated
	 */
	int AXIS_ORIGIN = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int AXIS_ORIGIN__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_ORIGIN__VALUE = 1;

	/**
	 * The number of structural features of the the '<em>Axis Origin</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AXIS_ORIGIN_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.BoundsImpl <em>Bounds</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.BoundsImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getBounds()
	 * @generated
	 */
	int BOUNDS = 3;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__LEFT = 0;

	/**
	 * The feature id for the '<em><b>Top</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__TOP = 1;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__WIDTH = 2;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int BOUNDS__HEIGHT = 3;

	/**
	 * The number of structural features of the the '<em>Bounds</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOUNDS_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.FillImpl <em>Fill</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.FillImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFill()
	 * @generated
	 */
	int FILL = 9;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FILL__TYPE = 0;

	/**
	 * The number of structural features of the the '<em>Fill</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl <em>Color Definition</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getColorDefinition()
	 * @generated
	 */
	int COLOR_DEFINITION = 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__TYPE = FILL__TYPE;

	/**
	 * The feature id for the '<em><b>Transparency</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__TRANSPARENCY = FILL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Red</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__RED = FILL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Green</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__GREEN = FILL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Blue</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION__BLUE = FILL_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Color Definition</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLOR_DEFINITION_FEATURE_COUNT = FILL_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.DataPointImpl <em>Data Point</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.DataPointImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPoint()
	 * @generated
	 */
	int DATA_POINT = 5;

	/**
	 * The feature id for the '<em><b>Components</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__COMPONENTS = 0;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__PREFIX = 1;

	/**
	 * The feature id for the '<em><b>Suffix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__SUFFIX = 2;

	/**
	 * The feature id for the '<em><b>Separator</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT__SEPARATOR = 3;

	/**
	 * The number of structural features of the the '<em>Data Point</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl <em>Data Point Component</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponent()
	 * @generated
	 */
	int DATA_POINT_COMPONENT = 6;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_COMPONENT__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Format Specifier</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_COMPONENT__FORMAT_SPECIFIER = 1;

	/**
	 * The number of structural features of the the '<em>Data Point Component</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_POINT_COMPONENT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl <em>Font Definition</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFontDefinition()
	 * @generated
	 */
	int FONT_DEFINITION = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.FormatSpecifierImpl <em>Format Specifier</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.FormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getFormatSpecifier()
	 * @generated
	 */
	int FORMAT_SPECIFIER = 11;

	/**
	 * The number of structural features of the the '<em>Format Specifier</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FORMAT_SPECIFIER_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl <em>Date Format Specifier</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.DateFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatSpecifier()
	 * @generated
	 */
	int DATE_FORMAT_SPECIFIER = 7;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATE_FORMAT_SPECIFIER__TYPE = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Detail</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATE_FORMAT_SPECIFIER__DETAIL = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Date Format Specifier</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATE_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.ExtendedPropertyImpl <em>Extended Property</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.ExtendedPropertyImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getExtendedProperty()
	 * @generated
	 */
	int EXTENDED_PROPERTY = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXTENDED_PROPERTY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXTENDED_PROPERTY__VALUE = 1;

	/**
	 * The number of structural features of the the '<em>Extended Property</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXTENDED_PROPERTY_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__SIZE = 1;

	/**
	 * The feature id for the '<em><b>Bold</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__BOLD = 2;

	/**
	 * The feature id for the '<em><b>Italic</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__ITALIC = 3;

	/**
	 * The feature id for the '<em><b>Strikethrough</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__STRIKETHROUGH = 4;

	/**
	 * The feature id for the '<em><b>Underline</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__UNDERLINE = 5;

	/**
	 * The feature id for the '<em><b>Word Wrap</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__WORD_WRAP = 6;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__ALIGNMENT = 7;

	/**
	 * The feature id for the '<em><b>Rotation</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION__ROTATION = 8;

	/**
	 * The number of structural features of the the '<em>Font Definition</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FONT_DEFINITION_FEATURE_COUNT = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.GradientImpl <em>Gradient</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.GradientImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGradient()
	 * @generated
	 */
	int GRADIENT = 12;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__TYPE = FILL__TYPE;

	/**
	 * The feature id for the '<em><b>Start Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRADIENT__START_COLOR = FILL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRADIENT__END_COLOR = FILL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__DIRECTION = FILL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cyclic</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int GRADIENT__CYCLIC = FILL_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Transparency</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRADIENT__TRANSPARENCY = FILL_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the the '<em>Gradient</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRADIENT_FEATURE_COUNT = FILL_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.ImageImpl <em>Image</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.ImageImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getImage()
	 * @generated
	 */
	int IMAGE = 13;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int IMAGE__TYPE = FILL__TYPE;

	/**
	 * The feature id for the '<em><b>URL</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int IMAGE__URL = FILL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Image</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMAGE_FEATURE_COUNT = FILL_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.InsetsImpl <em>Insets</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.InsetsImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getInsets()
	 * @generated
	 */
	int INSETS = 14;

	/**
	 * The feature id for the '<em><b>Top</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__TOP = 0;

	/**
	 * The feature id for the '<em><b>Left</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__LEFT = 1;

	/**
	 * The feature id for the '<em><b>Bottom</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__BOTTOM = 2;

	/**
	 * The feature id for the '<em><b>Right</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSETS__RIGHT = 3;

	/**
	 * The number of structural features of the the '<em>Insets</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSETS_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl <em>Java Date Format Specifier</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getJavaDateFormatSpecifier()
	 * @generated
	 */
	int JAVA_DATE_FORMAT_SPECIFIER = 15;

	/**
	 * The feature id for the '<em><b>Pattern</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_DATE_FORMAT_SPECIFIER__PATTERN = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Java Date Format Specifier</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA_DATE_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl <em>Java Number Format Specifier</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getJavaNumberFormatSpecifier()
	 * @generated
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER = 16;

	/**
	 * The feature id for the '<em><b>Pattern</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER__PATTERN = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Multiplier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER__MULTIPLIER = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Java Number Format Specifier</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA_NUMBER_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl <em>Line Attributes</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineAttributes()
	 * @generated
	 */
	int LINE_ATTRIBUTES = 17;

	/**
	 * The feature id for the '<em><b>Style</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__STYLE = 0;

	/**
	 * The feature id for the '<em><b>Thickness</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__THICKNESS = 1;

	/**
	 * The feature id for the '<em><b>Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__COLOR = 2;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES__VISIBLE = 3;

	/**
	 * The number of structural features of the the '<em>Line Attributes</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_ATTRIBUTES_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.LocationImpl <em>Location</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.LocationImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 18;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION__X = 0;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LOCATION__Y = 1;

	/**
	 * The number of structural features of the the '<em>Location</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.Location3DImpl <em>Location3 D</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.Location3DImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLocation3D()
	 * @generated
	 */
	int LOCATION3_D = 19;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D__X = LOCATION__X;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D__Y = LOCATION__Y;

	/**
	 * The feature id for the '<em><b>Z</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D__Z = LOCATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Location3 D</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION3_D_FEATURE_COUNT = LOCATION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.MarkerImpl <em>Marker</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.MarkerImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarker()
	 * @generated
	 */
	int MARKER = 20;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__SIZE = 1;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MARKER__VISIBLE = 2;

	/**
	 * The number of structural features of the the '<em>Marker</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKER_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl <em>Number Format Specifier</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getNumberFormatSpecifier()
	 * @generated
	 */
	int NUMBER_FORMAT_SPECIFIER = 21;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__PREFIX = FORMAT_SPECIFIER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Suffix</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__SUFFIX = FORMAT_SPECIFIER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Multiplier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__MULTIPLIER = FORMAT_SPECIFIER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fraction Digits</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER__FRACTION_DIGITS = FORMAT_SPECIFIER_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the the '<em>Number Format Specifier</em>' class.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMBER_FORMAT_SPECIFIER_FEATURE_COUNT = FORMAT_SPECIFIER_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.PaletteImpl <em>Palette</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.PaletteImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPalette()
	 * @generated
	 */
	int PALETTE = 22;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PALETTE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Entries</b></em>' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE__ENTRIES = 1;

	/**
	 * The number of structural features of the the '<em>Palette</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PALETTE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl <em>Rotation3 D</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRotation3D()
	 * @generated
	 */
	int ROTATION3_D = 23;

	/**
	 * The feature id for the '<em><b>Angles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROTATION3_D__ANGLES = 0;

	/**
	 * The number of structural features of the the '<em>Rotation3 D</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROTATION3_D_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl <em>Script Value</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.ScriptValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScriptValue()
	 * @generated
	 */
	int SCRIPT_VALUE = 24;

	/**
	 * The feature id for the '<em><b>Script</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SCRIPT_VALUE__SCRIPT = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Script Value</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl <em>Series Value</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.SeriesValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSeriesValue()
	 * @generated
	 */
	int SERIES_VALUE = 25;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SERIES_VALUE__NAME = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the the '<em>Series Value</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIES_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.TextImpl <em>Text</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.TextImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getText()
	 * @generated
	 */
	int TEXT = 28;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.SizeImpl <em>Size</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.SizeImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSize()
	 * @generated
	 */
	int SIZE = 26;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SIZE__HEIGHT = 0;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SIZE__WIDTH = 1;

	/**
	 * The number of structural features of the the '<em>Size</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIZE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl <em>Style Map</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyleMap()
	 * @generated
	 */
	int STYLE_MAP = 27;

	/**
	 * The feature id for the '<em><b>Component Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_MAP__COMPONENT_NAME = 0;

	/**
	 * The feature id for the '<em><b>Style</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STYLE_MAP__STYLE = 1;

	/**
	 * The number of structural features of the the '<em>Style Map</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STYLE_MAP_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TEXT__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Font</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT__FONT = 1;

	/**
	 * The feature id for the '<em><b>Color</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT__COLOR = 2;

	/**
	 * The number of structural features of the the '<em>Text</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl <em>Text Alignment</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTextAlignment()
	 * @generated
	 */
	int TEXT_ALIGNMENT = 29;

	/**
	 * The feature id for the '<em><b>Horizontal Alignment</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ALIGNMENT__HORIZONTAL_ALIGNMENT = 0;

	/**
	 * The feature id for the '<em><b>Vertical Alignment</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ALIGNMENT__VERTICAL_ALIGNMENT = 1;

	/**
	 * The number of structural features of the the '<em>Text Alignment</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_ALIGNMENT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl <em>Tooltip Value</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTooltipValue()
	 * @generated
	 */
	int TOOLTIP_VALUE = 30;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE__TEXT = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Delay</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE__DELAY = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the the '<em>Tooltip Value</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TOOLTIP_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl <em>URL Value</em>}' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.impl.URLValueImpl
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getURLValue()
	 * @generated
	 */
	int URL_VALUE = 31;

	/**
	 * The feature id for the '<em><b>Base Url</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__BASE_URL = ACTION_VALUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__TARGET = ACTION_VALUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Base Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__BASE_PARAMETER_NAME = ACTION_VALUE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Value Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__VALUE_PARAMETER_NAME = ACTION_VALUE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Series Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URL_VALUE__SERIES_PARAMETER_NAME = ACTION_VALUE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the the '<em>URL Value</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URL_VALUE_FEATURE_COUNT = ACTION_VALUE_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.ActionType <em>Action Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionType()
	 * @generated
	 */
	int ACTION_TYPE = 32;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.Anchor <em>Anchor</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAnchor()
	 * @generated
	 */
	int ANCHOR = 33;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.ChartType <em>Chart Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartType()
	 * @generated
	 */
	int CHART_TYPE = 36;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.DataPointComponentType <em>Data Point Component Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponentType()
	 * @generated
	 */
	int DATA_POINT_COMPONENT_TYPE = 37;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.DataType <em>Data Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataType()
	 * @generated
	 */
	int DATA_TYPE = 38;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.DateFormatDetail <em>Date Format Detail</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatDetail()
	 * @generated
	 */
	int DATE_FORMAT_DETAIL = 39;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.DateFormatType <em>Date Format Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatType()
	 * @generated
	 */
	int DATE_FORMAT_TYPE = 40;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.Direction <em>Direction</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDirection()
	 * @generated
	 */
	int DIRECTION = 41;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.GroupingUnitType <em>Grouping Unit Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGroupingUnitType()
	 * @generated
	 */
	int GROUPING_UNIT_TYPE = 42;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment <em>Horizontal Alignment</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getHorizontalAlignment()
	 * @generated
	 */
	int HORIZONTAL_ALIGNMENT = 43;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.IntersectionType <em>Intersection Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getIntersectionType()
	 * @generated
	 */
	int INTERSECTION_TYPE = 44;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle <em>Leader Line Style</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLeaderLineStyle()
	 * @generated
	 */
	int LEADER_LINE_STYLE = 45;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.LegendItemType <em>Legend Item Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendItemType()
	 * @generated
	 */
	int LEGEND_ITEM_TYPE = 46;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.LineDecorator <em>Line Decorator</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineDecorator()
	 * @generated
	 */
	int LINE_DECORATOR = 47;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.LineStyle <em>Line Style</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineStyle()
	 * @generated
	 */
	int LINE_STYLE = 48;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.MarkerType <em>Marker Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarkerType()
	 * @generated
	 */
	int MARKER_TYPE = 49;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.Orientation <em>Orientation</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getOrientation()
	 * @generated
	 */
	int ORIENTATION = 50;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.Position <em>Position</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPosition()
	 * @generated
	 */
	int POSITION = 51;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.RiserType <em>Riser Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRiserType()
	 * @generated
	 */
	int RISER_TYPE = 52;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.RuleType <em>Rule Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRuleType()
	 * @generated
	 */
	int RULE_TYPE = 53;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.ScaleUnitType <em>Scale Unit Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScaleUnitType()
	 * @generated
	 */
	int SCALE_UNIT_TYPE = 54;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.SortOption <em>Sort Option</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSortOption()
	 * @generated
	 */
	int SORT_OPTION = 55;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.Stretch <em>Stretch</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStretch()
	 * @generated
	 */
	int STRETCH = 56;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.StyledComponent <em>Styled Component</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyledComponent()
	 * @generated
	 */
	int STYLED_COMPONENT = 57;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.TickStyle <em>Tick Style</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTickStyle()
	 * @generated
	 */
	int TICK_STYLE = 58;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.TriggerCondition <em>Trigger Condition</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerCondition()
	 * @generated
	 */
	int TRIGGER_CONDITION = 59;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement <em>Units Of Measurement</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getUnitsOfMeasurement()
	 * @generated
	 */
	int UNITS_OF_MEASUREMENT = 60;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.VerticalAlignment <em>Vertical Alignment</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getVerticalAlignment()
	 * @generated
	 */
	int VERTICAL_ALIGNMENT = 61;

	/**
	 * The meta object id for the '<em>Action Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getActionTypeObject()
	 * @generated
	 */
	int ACTION_TYPE_OBJECT = 62;

	/**
	 * The meta object id for the '<em>Anchor Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAnchorObject()
	 * @generated
	 */
	int ANCHOR_OBJECT = 63;

	/**
	 * The meta object id for the '<em>Axis Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisTypeObject()
	 * @generated
	 */
	int AXIS_TYPE_OBJECT = 64;

	/**
	 * The meta object id for the '<em>Chart Dimension Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartDimensionObject()
	 * @generated
	 */
	int CHART_DIMENSION_OBJECT = 65;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.AxisType <em>Axis Type</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getAxisType()
	 * @generated
	 */
	int AXIS_TYPE = 34;

	/**
	 * The meta object id for the '{@link org.eclipse.birt.chart.model.attribute.ChartDimension <em>Chart Dimension</em>}' enum.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartDimension()
	 * @generated
	 */
	int CHART_DIMENSION = 35;

	/**
	 * The meta object id for the '<em>Chart Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getChartTypeObject()
	 * @generated
	 */
	int CHART_TYPE_OBJECT = 66;

	/**
	 * The meta object id for the '<em>Data Point Component Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataPointComponentTypeObject()
	 * @generated
	 */
	int DATA_POINT_COMPONENT_TYPE_OBJECT = 67;

	/**
	 * The meta object id for the '<em>Data Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDataTypeObject()
	 * @generated
	 */
	int DATA_TYPE_OBJECT = 68;

	/**
	 * The meta object id for the '<em>Date Format Detail Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatDetailObject()
	 * @generated
	 */
	int DATE_FORMAT_DETAIL_OBJECT = 69;

	/**
	 * The meta object id for the '<em>Date Format Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDateFormatTypeObject()
	 * @generated
	 */
	int DATE_FORMAT_TYPE_OBJECT = 70;

	/**
	 * The meta object id for the '<em>Direction Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getDirectionObject()
	 * @generated
	 */
	int DIRECTION_OBJECT = 71;

	/**
	 * The meta object id for the '<em>Grouping Unit Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getGroupingUnitTypeObject()
	 * @generated
	 */
	int GROUPING_UNIT_TYPE_OBJECT = 72;

	/**
	 * The meta object id for the '<em>Horizontal Alignment Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getHorizontalAlignmentObject()
	 * @generated
	 */
	int HORIZONTAL_ALIGNMENT_OBJECT = 73;

	/**
	 * The meta object id for the '<em>ID</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see java.lang.String
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getID()
	 * @generated
	 */
	int ID = 74;

	/**
	 * The meta object id for the '<em>Intersection Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getIntersectionTypeObject()
	 * @generated
	 */
	int INTERSECTION_TYPE_OBJECT = 75;

	/**
	 * The meta object id for the '<em>Leader Line Style Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLeaderLineStyleObject()
	 * @generated
	 */
	int LEADER_LINE_STYLE_OBJECT = 76;

	/**
	 * The meta object id for the '<em>Legend Item Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLegendItemTypeObject()
	 * @generated
	 */
	int LEGEND_ITEM_TYPE_OBJECT = 77;

	/**
	 * The meta object id for the '<em>Line Decorator Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineDecoratorObject()
	 * @generated
	 */
	int LINE_DECORATOR_OBJECT = 78;

	/**
	 * The meta object id for the '<em>Line Style Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getLineStyleObject()
	 * @generated
	 */
	int LINE_STYLE_OBJECT = 79;

	/**
	 * The meta object id for the '<em>Marker Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getMarkerTypeObject()
	 * @generated
	 */
	int MARKER_TYPE_OBJECT = 80;

	/**
	 * The meta object id for the '<em>Orientation Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getOrientationObject()
	 * @generated
	 */
	int ORIENTATION_OBJECT = 81;

	/**
	 * The meta object id for the '<em>Percentage</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPercentage()
	 * @generated
	 */
	int PERCENTAGE = 82;

	/**
	 * The meta object id for the '<em>Percentage Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see java.lang.Double
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPercentageObject()
	 * @generated
	 */
	int PERCENTAGE_OBJECT = 83;

	/**
	 * The meta object id for the '<em>Position Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getPositionObject()
	 * @generated
	 */
	int POSITION_OBJECT = 84;

	/**
	 * The meta object id for the '<em>RGB Value</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRGBValue()
	 * @generated
	 */
	int RGB_VALUE = 85;

	/**
	 * The meta object id for the '<em>RGB Value Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRGBValueObject()
	 * @generated
	 */
	int RGB_VALUE_OBJECT = 86;

	/**
	 * The meta object id for the '<em>Riser Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRiserTypeObject()
	 * @generated
	 */
	int RISER_TYPE_OBJECT = 87;

	/**
	 * The meta object id for the '<em>Rule Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getRuleTypeObject()
	 * @generated
	 */
	int RULE_TYPE_OBJECT = 88;

	/**
	 * The meta object id for the '<em>Scale Unit Type Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getScaleUnitTypeObject()
	 * @generated
	 */
	int SCALE_UNIT_TYPE_OBJECT = 89;

	/**
	 * The meta object id for the '<em>Sort Option Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getSortOptionObject()
	 * @generated
	 */
	int SORT_OPTION_OBJECT = 90;

	/**
	 * The meta object id for the '<em>Stretch Object</em>' data type. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStretchObject()
	 * @generated
	 */
	int STRETCH_OBJECT = 91;

	/**
	 * The meta object id for the '<em>Styled Component Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getStyledComponentObject()
	 * @generated
	 */
	int STYLED_COMPONENT_OBJECT = 92;

	/**
	 * The meta object id for the '<em>Tick Style Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTickStyleObject()
	 * @generated
	 */
	int TICK_STYLE_OBJECT = 93;

	/**
	 * The meta object id for the '<em>Trigger Condition Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getTriggerConditionObject()
	 * @generated
	 */
	int TRIGGER_CONDITION_OBJECT = 94;

	/**
	 * The meta object id for the '<em>Units Of Measurement Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getUnitsOfMeasurementObject()
	 * @generated
	 */
	int UNITS_OF_MEASUREMENT_OBJECT = 95;

	/**
	 * The meta object id for the '<em>Vertical Alignment Object</em>' data type.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @see org.eclipse.birt.chart.model.attribute.impl.AttributePackageImpl#getVerticalAlignmentObject()
	 * @generated
	 */
	int VERTICAL_ALIGNMENT_OBJECT = 96;

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.ActionValue <em>Action Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Action Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ActionValue
	 * @generated
	 */
	EClass getActionValue( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Angle3D <em>Angle3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Angle3 D</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D
	 * @generated
	 */
	EClass getAngle3D( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle <em>XAngle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>XAngle</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getXAngle()
	 * @see #getAngle3D()
	 * @generated
	 */
	EAttribute getAngle3D_XAngle( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle <em>YAngle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>YAngle</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getYAngle()
	 * @see #getAngle3D()
	 * @generated
	 */
	EAttribute getAngle3D_YAngle( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle <em>ZAngle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>ZAngle</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Angle3D#getZAngle()
	 * @see #getAngle3D()
	 * @generated
	 */
	EAttribute getAngle3D_ZAngle( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin <em>Axis Origin</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Axis Origin</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisOrigin
	 * @generated
	 */
	EClass getAxisOrigin( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisOrigin#getType()
	 * @see #getAxisOrigin()
	 * @generated
	 */
	EAttribute getAxisOrigin_Type( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.AxisOrigin#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisOrigin#getValue()
	 * @see #getAxisOrigin()
	 * @generated
	 */
	EReference getAxisOrigin_Value( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Bounds <em>Bounds</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bounds</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds
	 * @generated
	 */
	EClass getBounds( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Bounds#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getLeft()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Left( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Bounds#getTop <em>Top</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Top</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getTop()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Top( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Bounds#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getWidth()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Width( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Bounds#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Bounds#getHeight()
	 * @see #getBounds()
	 * @generated
	 */
	EAttribute getBounds_Height( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition <em>Color Definition</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Color Definition</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition
	 * @generated
	 */
	EClass getColorDefinition( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency <em>Transparency</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transparency</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getTransparency()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Transparency( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed <em>Red</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Red</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getRed()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Red( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue <em>Blue</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Blue</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getBlue()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Blue( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen <em>Green</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Green</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ColorDefinition#getGreen()
	 * @see #getColorDefinition()
	 * @generated
	 */
	EAttribute getColorDefinition_Green( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.DataPoint <em>Data Point</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Point</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint
	 * @generated
	 */
	EClass getDataPoint( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getComponents <em>Components</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Components</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getComponents()
	 * @see #getDataPoint()
	 * @generated
	 */
	EReference getDataPoint_Components( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getPrefix <em>Prefix</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getPrefix()
	 * @see #getDataPoint()
	 * @generated
	 */
	EAttribute getDataPoint_Prefix( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getSuffix <em>Suffix</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Suffix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getSuffix()
	 * @see #getDataPoint()
	 * @generated
	 */
	EAttribute getDataPoint_Suffix( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.DataPoint#getSeparator <em>Separator</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Separator</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPoint#getSeparator()
	 * @see #getDataPoint()
	 * @generated
	 */
	EAttribute getDataPoint_Separator( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent <em>Data Point Component</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Point Component</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent
	 * @generated
	 */
	EClass getDataPointComponent( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent#getType()
	 * @see #getDataPointComponent()
	 * @generated
	 */
	EAttribute getDataPointComponent_Type( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.DataPointComponent#getFormatSpecifier <em>Format Specifier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponent#getFormatSpecifier()
	 * @see #getDataPointComponent()
	 * @generated
	 */
	EReference getDataPointComponent_FormatSpecifier( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier <em>Date Format Specifier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Date Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier
	 * @generated
	 */
	EClass getDateFormatSpecifier( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType()
	 * @see #getDateFormatSpecifier()
	 * @generated
	 */
	EAttribute getDateFormatSpecifier_Type( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail <em>Detail</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Detail</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail()
	 * @see #getDateFormatSpecifier()
	 * @generated
	 */
	EAttribute getDateFormatSpecifier_Detail( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty <em>Extended Property</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Extended Property</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ExtendedProperty
	 * @generated
	 */
	EClass getExtendedProperty( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ExtendedProperty#getName()
	 * @see #getExtendedProperty()
	 * @generated
	 */
	EAttribute getExtendedProperty_Name( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.ExtendedProperty#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ExtendedProperty#getValue()
	 * @see #getExtendedProperty()
	 * @generated
	 */
	EAttribute getExtendedProperty_Value( );

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.attribute.Fill <em>Fill</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Fill</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Fill
	 * @generated
	 */
	EClass getFill( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Fill#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Fill#getType()
	 * @see #getFill()
	 * @generated
	 */
	EAttribute getFill_Type( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.FontDefinition <em>Font Definition</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Font Definition</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition
	 * @generated
	 */
	EClass getFontDefinition( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getName()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Name( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getSize()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Size( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isBold <em>Bold</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bold</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isBold()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Bold( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic <em>Italic</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Italic</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isItalic()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Italic( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough <em>Strikethrough</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Strikethrough</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isStrikethrough()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Strikethrough( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline <em>Underline</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Underline</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isUnderline()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Underline( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap <em>Word Wrap</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Word Wrap</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#isWordWrap()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_WordWrap( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getAlignment <em>Alignment</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getAlignment()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EReference getFontDefinition_Alignment( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation <em>Rotation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rotation</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FontDefinition#getRotation()
	 * @see #getFontDefinition()
	 * @generated
	 */
	EAttribute getFontDefinition_Rotation( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.FormatSpecifier <em>Format Specifier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.FormatSpecifier
	 * @generated
	 */
	EClass getFormatSpecifier( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Gradient <em>Gradient</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Gradient</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient
	 * @generated
	 */
	EClass getGradient( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.Gradient#getStartColor <em>Start Color</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Start Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getStartColor()
	 * @see #getGradient()
	 * @generated
	 */
	EReference getGradient_StartColor( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.Gradient#getEndColor <em>End Color</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>End Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getEndColor()
	 * @see #getGradient()
	 * @generated
	 */
	EReference getGradient_EndColor( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Gradient#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getDirection()
	 * @see #getGradient()
	 * @generated
	 */
	EAttribute getGradient_Direction( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Gradient#isCyclic <em>Cyclic</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cyclic</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#isCyclic()
	 * @see #getGradient()
	 * @generated
	 */
	EAttribute getGradient_Cyclic( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Gradient#getTransparency <em>Transparency</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transparency</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Gradient#getTransparency()
	 * @see #getGradient()
	 * @generated
	 */
	EAttribute getGradient_Transparency( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Image <em>Image</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Image</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Image
	 * @generated
	 */
	EClass getImage( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Image#getURL <em>URL</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>URL</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Image#getURL()
	 * @see #getImage()
	 * @generated
	 */
	EAttribute getImage_URL( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Insets <em>Insets</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Insets</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets
	 * @generated
	 */
	EClass getInsets( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Insets#getTop <em>Top</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Top</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getTop()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Top( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Insets#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Left</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getLeft()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Left( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Insets#getBottom <em>Bottom</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bottom</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getBottom()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Bottom( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Insets#getRight <em>Right</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Right</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Insets#getRight()
	 * @see #getInsets()
	 * @generated
	 */
	EAttribute getInsets_Right( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier <em>Java Date Format Specifier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Java Date Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier
	 * @generated
	 */
	EClass getJavaDateFormatSpecifier( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern <em>Pattern</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pattern</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern()
	 * @see #getJavaDateFormatSpecifier()
	 * @generated
	 */
	EAttribute getJavaDateFormatSpecifier_Pattern( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier <em>Java Number Format Specifier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Java Number Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier
	 * @generated
	 */
	EClass getJavaNumberFormatSpecifier( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getPattern <em>Pattern</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pattern</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getPattern()
	 * @see #getJavaNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getJavaNumberFormatSpecifier_Pattern( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier <em>Multiplier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multiplier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier#getMultiplier()
	 * @see #getJavaNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getJavaNumberFormatSpecifier_Multiplier( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.LineAttributes <em>Line Attributes</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Line Attributes</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes
	 * @generated
	 */
	EClass getLineAttributes( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#getStyle()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EAttribute getLineAttributes_Style( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness <em>Thickness</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Thickness</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#getThickness()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EAttribute getLineAttributes_Thickness( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#getColor <em>Color</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#getColor()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EReference getLineAttributes_Color( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible <em>Visible</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineAttributes#isVisible()
	 * @see #getLineAttributes()
	 * @generated
	 */
	EAttribute getLineAttributes_Visible( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Location <em>Location</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Location</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location
	 * @generated
	 */
	EClass getLocation( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Location#getX <em>X</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location#getX()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_X( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Location#getY <em>Y</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location#getY()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Y( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Location3D <em>Location3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Location3 D</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location3D
	 * @generated
	 */
	EClass getLocation3D( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Location3D#getZ <em>Z</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Z</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Location3D#getZ()
	 * @see #getLocation3D()
	 * @generated
	 */
	EAttribute getLocation3D_Z( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Marker <em>Marker</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marker</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker
	 * @generated
	 */
	EClass getMarker( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Marker#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#getType()
	 * @see #getMarker()
	 * @generated
	 */
	EAttribute getMarker_Type( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Marker#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#getSize()
	 * @see #getMarker()
	 * @generated
	 */
	EAttribute getMarker_Size( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Marker#isVisible <em>Visible</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Marker#isVisible()
	 * @see #getMarker()
	 * @generated
	 */
	EAttribute getMarker_Visible( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier <em>Number Format Specifier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Number Format Specifier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier
	 * @generated
	 */
	EClass getNumberFormatSpecifier( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getPrefix <em>Prefix</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getPrefix()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_Prefix( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getSuffix <em>Suffix</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Suffix</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getSuffix()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_Suffix( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier <em>Multiplier</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multiplier</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getMultiplier()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_Multiplier( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits <em>Fraction Digits</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fraction Digits</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier#getFractionDigits()
	 * @see #getNumberFormatSpecifier()
	 * @generated
	 */
	EAttribute getNumberFormatSpecifier_FractionDigits( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Palette <em>Palette</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Palette</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Palette
	 * @generated
	 */
	EClass getPalette( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Palette#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Palette#getName()
	 * @see #getPalette()
	 * @generated
	 */
	EAttribute getPalette_Name( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.attribute.Palette#getEntries <em>Entries</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entries</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Palette#getEntries()
	 * @see #getPalette()
	 * @generated
	 */
	EReference getPalette_Entries( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.Rotation3D <em>Rotation3 D</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rotation3 D</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Rotation3D
	 * @generated
	 */
	EClass getRotation3D( );

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.birt.chart.model.attribute.Rotation3D#getAngles <em>Angles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Angles</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Rotation3D#getAngles()
	 * @see #getRotation3D()
	 * @generated
	 */
	EReference getRotation3D_Angles( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.ScriptValue <em>Script Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Script Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScriptValue
	 * @generated
	 */
	EClass getScriptValue( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.ScriptValue#getScript <em>Script</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Script</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScriptValue#getScript()
	 * @see #getScriptValue()
	 * @generated
	 */
	EAttribute getScriptValue_Script( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.SeriesValue <em>Series Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Series Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SeriesValue
	 * @generated
	 */
	EClass getSeriesValue( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.SeriesValue#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SeriesValue#getName()
	 * @see #getSeriesValue()
	 * @generated
	 */
	EAttribute getSeriesValue_Name( );

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.attribute.Size <em>Size</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Size</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Size
	 * @generated
	 */
	EClass getSize( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Size#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Size#getHeight()
	 * @see #getSize()
	 * @generated
	 */
	EAttribute getSize_Height( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Size#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Size#getWidth()
	 * @see #getSize()
	 * @generated
	 */
	EAttribute getSize_Width( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.StyleMap <em>Style Map</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Style Map</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyleMap
	 * @generated
	 */
	EClass getStyleMap( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName <em>Component Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Component Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyleMap#getComponentName()
	 * @see #getStyleMap()
	 * @generated
	 */
	EAttribute getStyleMap_ComponentName( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.StyleMap#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyleMap#getStyle()
	 * @see #getStyleMap()
	 * @generated
	 */
	EAttribute getStyleMap_Style( );

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.birt.chart.model.attribute.Text <em>Text</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Text</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text
	 * @generated
	 */
	EClass getText( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.Text#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text#getValue()
	 * @see #getText()
	 * @generated
	 */
	EAttribute getText_Value( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.Text#getFont <em>Font</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Font</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text#getFont()
	 * @see #getText()
	 * @generated
	 */
	EReference getText_Font( );

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.birt.chart.model.attribute.Text#getColor <em>Color</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Color</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Text#getColor()
	 * @see #getText()
	 * @generated
	 */
	EReference getText_Color( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.TextAlignment <em>Text Alignment</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Text Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TextAlignment
	 * @generated
	 */
	EClass getTextAlignment( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment <em>Horizontal Alignment</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Horizontal Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TextAlignment#getHorizontalAlignment()
	 * @see #getTextAlignment()
	 * @generated
	 */
	EAttribute getTextAlignment_HorizontalAlignment( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment <em>Vertical Alignment</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vertical Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TextAlignment#getVerticalAlignment()
	 * @see #getTextAlignment()
	 * @generated
	 */
	EAttribute getTextAlignment_VerticalAlignment( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.TooltipValue <em>Tooltip Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tooltip Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue
	 * @generated
	 */
	EClass getTooltipValue( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getText <em>Text</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue#getText()
	 * @see #getTooltipValue()
	 * @generated
	 */
	EAttribute getTooltipValue_Text( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay <em>Delay</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delay</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TooltipValue#getDelay()
	 * @see #getTooltipValue()
	 * @generated
	 */
	EAttribute getTooltipValue_Delay( );

	/**
	 * Returns the meta object for class '{@link org.eclipse.birt.chart.model.attribute.URLValue <em>URL Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for class '<em>URL Value</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue
	 * @generated
	 */
	EClass getURLValue( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseUrl <em>Base Url</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Url</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getBaseUrl()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_BaseUrl( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.URLValue#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getTarget()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_Target( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.URLValue#getBaseParameterName <em>Base Parameter Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Parameter Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getBaseParameterName()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_BaseParameterName( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.URLValue#getValueParameterName <em>Value Parameter Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value Parameter Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getValueParameterName()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_ValueParameterName( );

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.birt.chart.model.attribute.URLValue#getSeriesParameterName <em>Series Parameter Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Series Parameter Name</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.URLValue#getSeriesParameterName()
	 * @see #getURLValue()
	 * @generated
	 */
	EAttribute getURLValue_SeriesParameterName( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.ActionType <em>Action Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Action Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @generated
	 */
	EEnum getActionType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.Anchor <em>Anchor</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Anchor</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @generated
	 */
	EEnum getAnchor( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.ChartType <em>Chart Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Chart Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @generated
	 */
	EEnum getChartType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.DataPointComponentType <em>Data Point Component Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Data Point Component Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @generated
	 */
	EEnum getDataPointComponentType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.DataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Data Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @generated
	 */
	EEnum getDataType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.DateFormatDetail <em>Date Format Detail</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Date Format Detail</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @generated
	 */
	EEnum getDateFormatDetail( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.DateFormatType <em>Date Format Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Date Format Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @generated
	 */
	EEnum getDateFormatType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.Direction <em>Direction</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Direction</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @generated
	 */
	EEnum getDirection( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.GroupingUnitType <em>Grouping Unit Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Grouping Unit Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @generated
	 */
	EEnum getGroupingUnitType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment <em>Horizontal Alignment</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Horizontal Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @generated
	 */
	EEnum getHorizontalAlignment( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.IntersectionType <em>Intersection Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Intersection Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @generated
	 */
	EEnum getIntersectionType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle <em>Leader Line Style</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Leader Line Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @generated
	 */
	EEnum getLeaderLineStyle( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.LegendItemType <em>Legend Item Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Legend Item Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @generated
	 */
	EEnum getLegendItemType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.LineDecorator <em>Line Decorator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Line Decorator</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @generated
	 */
	EEnum getLineDecorator( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.LineStyle <em>Line Style</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Line Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @generated
	 */
	EEnum getLineStyle( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.MarkerType <em>Marker Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Marker Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @generated
	 */
	EEnum getMarkerType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.Orientation <em>Orientation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Orientation</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @generated
	 */
	EEnum getOrientation( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.Position <em>Position</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Position</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @generated
	 */
	EEnum getPosition( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.RiserType <em>Riser Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Riser Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @generated
	 */
	EEnum getRiserType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.RuleType <em>Rule Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Rule Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @generated
	 */
	EEnum getRuleType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.ScaleUnitType <em>Scale Unit Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Scale Unit Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @generated
	 */
	EEnum getScaleUnitType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.SortOption <em>Sort Option</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Sort Option</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @generated
	 */
	EEnum getSortOption( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.Stretch <em>Stretch</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Stretch</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @generated
	 */
	EEnum getStretch( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.StyledComponent <em>Styled Component</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Styled Component</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @generated
	 */
	EEnum getStyledComponent( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.TickStyle <em>Tick Style</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Tick Style</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @generated
	 */
	EEnum getTickStyle( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.TriggerCondition <em>Trigger Condition</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Trigger Condition</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @generated
	 */
	EEnum getTriggerCondition( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement <em>Units Of Measurement</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Units Of Measurement</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @generated
	 */
	EEnum getUnitsOfMeasurement( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.VerticalAlignment <em>Vertical Alignment</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Vertical Alignment</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @generated
	 */
	EEnum getVerticalAlignment( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.ActionType <em>Action Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Action Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ActionType"
	 *        extendedMetaData="name='ActionType:Object' baseType='ActionType'" 
	 * @generated
	 */
	EDataType getActionTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.Anchor <em>Anchor Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Anchor Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Anchor
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Anchor"
	 *        extendedMetaData="name='Anchor:Object' baseType='Anchor'" 
	 * @generated
	 */
	EDataType getAnchorObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.AxisType <em>Axis Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Axis Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.AxisType"
	 *        extendedMetaData="name='AxisType:Object' baseType='AxisType'" 
	 * @generated
	 */
	EDataType getAxisTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.ChartDimension <em>Chart Dimension Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Chart Dimension Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ChartDimension"
	 *        extendedMetaData="name='ChartDimension:Object' baseType='ChartDimension'" 
	 * @generated
	 */
	EDataType getChartDimensionObject( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.AxisType <em>Axis Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Axis Type</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 * @generated
	 */
	EEnum getAxisType( );

	/**
	 * Returns the meta object for enum '{@link org.eclipse.birt.chart.model.attribute.ChartDimension <em>Chart Dimension</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Chart Dimension</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartDimension
	 * @generated
	 */
	EEnum getChartDimension( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.ChartType <em>Chart Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Chart Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ChartType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ChartType"
	 *        extendedMetaData="name='ChartType:Object' baseType='ChartType'" 
	 * @generated
	 */
	EDataType getChartTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.DataPointComponentType <em>Data Point Component Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Data Point Component Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataPointComponentType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DataPointComponentType"
	 *        extendedMetaData="name='DataPointComponentType:Object' baseType='DataPointComponentType'" 
	 * @generated
	 */
	EDataType getDataPointComponentTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.DataType <em>Data Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Data Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DataType"
	 *        extendedMetaData="name='DataType:Object' baseType='DataType'" 
	 * @generated
	 */
	EDataType getDataTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.DateFormatDetail <em>Date Format Detail Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Date Format Detail Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DateFormatDetail"
	 *        extendedMetaData="name='DateFormatDetail:Object' baseType='DateFormatDetail'" 
	 * @generated
	 */
	EDataType getDateFormatDetailObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.DateFormatType <em>Date Format Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Date Format Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.DateFormatType"
	 *        extendedMetaData="name='DateFormatType:Object' baseType='DateFormatType'" 
	 * @generated
	 */
	EDataType getDateFormatTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.Direction <em>Direction Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Direction Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Direction
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Direction"
	 *        extendedMetaData="name='Direction:Object' baseType='Direction'" 
	 * @generated
	 */
	EDataType getDirectionObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.GroupingUnitType <em>Grouping Unit Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Grouping Unit Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.GroupingUnitType"
	 *        extendedMetaData="name='GroupingUnitType:Object' baseType='GroupingUnitType'" 
	 * @generated
	 */
	EDataType getGroupingUnitTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.HorizontalAlignment <em>Horizontal Alignment Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Horizontal Alignment Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.HorizontalAlignment
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.HorizontalAlignment"
	 *        extendedMetaData="name='HorizontalAlignment:Object' baseType='HorizontalAlignment'" 
	 * @generated
	 */
	EDataType getHorizontalAlignmentObject( );

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>ID</em>}'.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @return the meta object for data type '<em>ID</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='ID' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='[A-Z]'" 
	 * @generated
	 */
	EDataType getID( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.IntersectionType <em>Intersection Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Intersection Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.IntersectionType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.IntersectionType"
	 *        extendedMetaData="name='IntersectionType:Object' baseType='IntersectionType'" 
	 * @generated
	 */
	EDataType getIntersectionTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.LeaderLineStyle <em>Leader Line Style Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Leader Line Style Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LeaderLineStyle
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LeaderLineStyle"
	 *        extendedMetaData="name='LeaderLineStyle:Object' baseType='LeaderLineStyle'" 
	 * @generated
	 */
	EDataType getLeaderLineStyleObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.LegendItemType <em>Legend Item Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Legend Item Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LegendItemType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LegendItemType"
	 *        extendedMetaData="name='LegendItemType:Object' baseType='LegendItemType'" 
	 * @generated
	 */
	EDataType getLegendItemTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.LineDecorator <em>Line Decorator Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Line Decorator Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineDecorator
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LineDecorator"
	 *        extendedMetaData="name='LineDecorator:Object' baseType='LineDecorator'" 
	 * @generated
	 */
	EDataType getLineDecoratorObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.LineStyle <em>Line Style Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Line Style Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.LineStyle
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.LineStyle"
	 *        extendedMetaData="name='LineStyle:Object' baseType='LineStyle'" 
	 * @generated
	 */
	EDataType getLineStyleObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.MarkerType <em>Marker Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Marker Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.MarkerType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.MarkerType"
	 *        extendedMetaData="name='MarkerType:Object' baseType='MarkerType'" 
	 * @generated
	 */
	EDataType getMarkerTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.Orientation <em>Orientation Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Orientation Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Orientation"
	 *        extendedMetaData="name='Orientation:Object' baseType='Orientation'" 
	 * @generated
	 */
	EDataType getOrientationObject( );

	/**
	 * Returns the meta object for data type '<em>Percentage</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Percentage</em>'.
	 * @model instanceClass="double"
	 * @generated
	 */
	EDataType getPercentage( );

	/**
	 * Returns the meta object for data type '
	 * {@link java.lang.Double <em>Percentage Object</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>Percentage Object</em>'.
	 * @see java.lang.Double
	 * @model instanceClass="java.lang.Double"
	 * @generated
	 */
	EDataType getPercentageObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.Position <em>Position Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Position Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Position
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Position"
	 *        extendedMetaData="name='Position:Object' baseType='Position'" 
	 * @generated
	 */
	EDataType getPositionObject( );

	/**
	 * Returns the meta object for data type '<em>RGB Value</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>RGB Value</em>'.
	 * @model instanceClass="int"
	 * @generated
	 */
	EDataType getRGBValue( );

	/**
	 * Returns the meta object for data type '
	 * {@link java.lang.Integer <em>RGB Value Object</em>}'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for data type '<em>RGB Value Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 * @generated
	 */
	EDataType getRGBValueObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.RiserType <em>Riser Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Riser Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.RiserType"
	 *        extendedMetaData="name='RiserType:Object' baseType='RiserType'" 
	 * @generated
	 */
	EDataType getRiserTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.RuleType <em>Rule Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Rule Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.RuleType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.RuleType"
	 *        extendedMetaData="name='RuleType:Object' baseType='RuleType'" 
	 * @generated
	 */
	EDataType getRuleTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.ScaleUnitType <em>Scale Unit Type Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Scale Unit Type Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.ScaleUnitType
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.ScaleUnitType"
	 *        extendedMetaData="name='ScaleUnitType:Object' baseType='ScaleUnitType'" 
	 * @generated
	 */
	EDataType getScaleUnitTypeObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.SortOption <em>Sort Option Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Sort Option Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.SortOption
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.SortOption"
	 *        extendedMetaData="name='SortOption:Object' baseType='SortOption'" 
	 * @generated
	 */
	EDataType getSortOptionObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.Stretch <em>Stretch Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Stretch Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.Stretch
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.Stretch"
	 *        extendedMetaData="name='Stretch:Object' baseType='Stretch'" 
	 * @generated
	 */
	EDataType getStretchObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.StyledComponent <em>Styled Component Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Styled Component Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.StyledComponent
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.StyledComponent"
	 *        extendedMetaData="name='StyledComponent:Object' baseType='StyledComponent'" 
	 * @generated
	 */
	EDataType getStyledComponentObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.TickStyle <em>Tick Style Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Tick Style Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TickStyle
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.TickStyle"
	 *        extendedMetaData="name='TickStyle:Object' baseType='TickStyle'" 
	 * @generated
	 */
	EDataType getTickStyleObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.TriggerCondition <em>Trigger Condition Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Trigger Condition Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.TriggerCondition"
	 *        extendedMetaData="name='TriggerCondition:Object' baseType='TriggerCondition'" 
	 * @generated
	 */
	EDataType getTriggerConditionObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement <em>Units Of Measurement Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Units Of Measurement Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement"
	 *        extendedMetaData="name='UnitsOfMeasurement:Object' baseType='UnitsOfMeasurement'" 
	 * @generated
	 */
	EDataType getUnitsOfMeasurementObject( );

	/**
	 * Returns the meta object for data type '{@link org.eclipse.birt.chart.model.attribute.VerticalAlignment <em>Vertical Alignment Object</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Vertical Alignment Object</em>'.
	 * @see org.eclipse.birt.chart.model.attribute.VerticalAlignment
	 * @model instanceClass="org.eclipse.birt.chart.model.attribute.VerticalAlignment"
	 *        extendedMetaData="name='VerticalAlignment:Object' baseType='VerticalAlignment'" 
	 * @generated
	 */
	EDataType getVerticalAlignmentObject( );

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AttributeFactory getAttributeFactory( );

} //AttributePackage
