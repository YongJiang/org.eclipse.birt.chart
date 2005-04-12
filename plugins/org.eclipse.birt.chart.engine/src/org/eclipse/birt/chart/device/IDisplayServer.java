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

package org.eclipse.birt.chart.device;

import java.net.URL;
import java.util.Locale;

import org.eclipse.birt.chart.exception.ImageLoadingException;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.component.Label;

/**
 * Provides generic services to a device renderer for computing metrics
 * and centralized creation of device specific resources. This class is
 * analogus to an X server interface associated with a device.
 */
public interface IDisplayServer
{
    /**
     * Lists out all resources that were previously logged explicitly
     * by the <code>logCreation(Object)</code> method or those internally
     * created by each of the resource creation methods. Once this method
     * is invoked, all entries written into the log should be flushed.
     */
    void debug();
    
    /**
     * Log creation of a resource for which leaks are to be tracked
     * 
     * @param   o   A device-specific object being created
     */
    void logCreation(Object o);
    
    /**
     * Attempts to create a new font resource associated with a specific
     * device for use in rendering or computations
     * 
     * @param   fd  An font description for which a device specific resource is being requested
     * 
     * @return  A device specific font
     */
    Object createFont(FontDefinition fd);

    /**
     * Attempts to create a new color resource associated with a specific device
     * 
     * @param   cd  A color description for which a device specific resource is being requested
     *     
     * @return  A device specific color
     */
    Object getColor(ColorDefinition cd);

    /**
     * Returns the resolution of the device in dots per inch
     * As an example, for a display screen, the dots correspond to pixels
     * and a typical value for a Win32 OS is 96 DPI.
     * 
     * @return  The integral dots per inch associated with the device
     */
    int getDpiResolution();

    /**
     * Attempts to use device specific libraries to load an image
     * for use with the device renderer
     * 
     * @param   url   The URL associated with the image location
     * 
     * @return  An instance of an image associated with the specified URL
     * 
     * @throws ImageLoadingException
     */
    Object loadImage(URL url) throws ImageLoadingException;

    /**
     * Returns the size(width, height) of the device specific image
     * that was previously loaded by the <code>loadImage(URL)</code> method 
     * 
     * @param   oImage    The image for which the size is being requested
     * 
     * @return  The size of the image
     */
    Size getSize(Object oImage);

    /**
     * An observer is typically associated with certain device types
     * to aid in image loading and image metadata retrieval.
     * 
     * @return  An image observer associated with a specific device renderer
     */
    Object getObserver();

    /**
     * An instance of a text metrics computation class capable of
     * providing text metric information associated with a given Label
     * to aid in typically computing the size of rendered text 
     * 
     * @param   la  The Label instance for which text metrics are being requested
     * 
     * @return  Text metrics associated with the specified Label instance
     */
    ITextMetrics getTextMetrics(Label la);
    
    /**
     * Provides the locale to display server implementations
     * as needed to retrieve localized resources for presentation.
     * 
     * @return
     */
    Locale getLocale();

}
