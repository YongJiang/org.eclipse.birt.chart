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

package org.eclipse.birt.chart.model.data.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

/**
 * <!-- begin-user-doc --> The <b>Resource Factory </b> associated with the package. <!-- end-user-doc -->
 * @see org.eclipse.birt.chart.model.data.util.DataResourceImpl
 * @generated
 */
public class DataResourceFactoryImpl extends XMLResourceFactoryImpl
{

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected ExtendedMetaData extendedMetaData;

    /**
     * Creates an instance of the resource factory.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public DataResourceFactoryImpl()
    {
        super();
        extendedMetaData = ExtendedMetaData.INSTANCE;
    }

    /**
     * Creates an instance of the resource.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Resource createResource(URI uri)
    {
        XMLResource result = new DataResourceImpl(uri);
        result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
        result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);

        result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
        result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

        result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
        return result;
    }

} //DataResourceFactoryImpl
