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
 * A no-op adapter implementation for the {@link org.eclipse.birt.chart.device.IDisplayServer}
 * interface definition.
 */
public abstract class DisplayAdapter implements IDisplayServer
{
    /**
     * An internal instance of the locale being used for processing
     */
    private transient Locale lcl = null;
    
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.chart.device.IDisplayServer#debug()
     */
    public void debug()
    {
        // DO NOTHING
    }
    
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.chart.device.IDisplayServer#logCreation(java.lang.Object)
     */
    public void logCreation(Object oMisc)
    {
        // DO NOTHING
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#createFont(org.eclipse.birt.chart.model.attribute.FontDefinition)
     */
    public Object createFont(FontDefinition fd)
    {
        // NO-OP ADAPTER DEFAULT IMPLEMENTATION
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getColor(org.eclipse.birt.chart.model.attribute.ColorDefinition)
     */
    public Object getColor(ColorDefinition cd)
    {
        // NO-OP ADAPTER DEFAULT IMPLEMENTATION
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getDpiResolution()
     */
    public int getDpiResolution()
    {
        // NO-OP ADAPTER DEFAULT IMPLEMENTATION
        return 96;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#loadImage(java.net.URL)
     */
    public Object loadImage(URL url) throws ImageLoadingException
    {
        // NO-OP ADAPTER DEFAULT IMPLEMENTATION
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getSize(java.lang.Object)
     */
    public Size getSize(Object oImage)
    {
        // NO-OP ADAPTER DEFAULT IMPLEMENTATION
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getObserver()
     */
    public Object getObserver()
    {
        // NO-OP ADAPTER DEFAULT IMPLEMENTATION
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDisplayServer#getTextMetrics(org.eclipse.birt.chart.model.component.Label)
     */
    public ITextMetrics getTextMetrics(Label la)
    {
        // NO-OP ADAPTER DEFAULT IMPLEMENTATION
        return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.IDisplayServer#getLocale()
     */
    public final Locale getLocale()
    {
        return (lcl == null) ? Locale.getDefault() : lcl;
    }
    
    /**
     * A convenience method provided to associate a locale with
     * a display server
     * 
     * @param lcl   The locale to be set
     */
    public final void setLocale(Locale lcl)
    {
        this.lcl = lcl;
    }
}
