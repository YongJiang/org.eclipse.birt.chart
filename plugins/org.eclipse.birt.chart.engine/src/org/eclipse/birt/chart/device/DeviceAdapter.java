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

import java.util.Locale;

import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureChangeEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.RenderingException;

/**
 * A no-op adapter implementation for the {@link org.eclipse.birt.chart.device.IDeviceRenderer}
 * interface definition.
 */
public abstract class DeviceAdapter extends EventObjectCache implements IDeviceRenderer
{
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String, java.lang.Object)
     */
    public void setProperty(String sProperty, Object oValue)
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#getGraphicsContext()
     */
    public Object getGraphicsContext()
    {
        // DO NOTHING IN NO-OP IMPL
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#getXServer()
     */
    public IDisplayServer getDisplayServer()
    {
        // DO NOTHING IN NO-OP IMPL
        return null;
    }

    
    /**
     * A convenience method the provides the locale as needed to
     * fetch localized resources.
     * 
     * @return  The locale instance
     */
    public final Locale getLocale()
    {
        final IDisplayServer ids = getDisplayServer();
        if (ids == null)
        {
            return Locale.getDefault();
        }
        return ids.getLocale(); // ALREADY BEING CHECKED FOR NULL
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#needsStructureDefinition()
     */
    public boolean needsStructureDefinition()
    {
        // THE NO-OP IMPLEMENTATION INDICATES THAT STRUCTURE DEFINITION
        // NOTIFICATIONS ARE NOT REQUIRED
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#before()
     */
    public void before() throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#after()
     */
    public void after() throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#setClip(org.eclipse.birt.chart.event.ClipRenderEvent)
     */
    public void setClip(ClipRenderEvent cre)
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawImage(org.eclipse.birt.chart.event.ImageRenderEvent)
     */
    public void drawImage(ImageRenderEvent ire) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawLine(org.eclipse.birt.chart.event.LineRenderEvent)
     */
    public void drawLine(LineRenderEvent lre) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
     */
    public void drawRectangle(RectangleRenderEvent rre) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillRectangle(org.eclipse.birt.chart.event.RectangleRenderEvent)
     */
    public void fillRectangle(RectangleRenderEvent rre) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawPolygon(org.eclipse.birt.chart.event.PolygonRenderEvent)
     */
    public void drawPolygon(PolygonRenderEvent pre) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillPolygon(org.eclipse.birt.chart.event.PolygonRenderEvent)
     */
    public void fillPolygon(PolygonRenderEvent pre) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawArc(org.eclipse.birt.chart.event.ArcRenderEvent)
     */
    public void drawArc(ArcRenderEvent are) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillArc(org.eclipse.birt.chart.event.ArcRenderEvent)
     */
    public void fillArc(ArcRenderEvent are) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#enableInteraction(org.eclipse.birt.chart.event.InteractionEvent)
     */
    public void enableInteraction(InteractionEvent ie) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawArea(org.eclipse.birt.chart.event.AreaRenderEvent)
     */
    public void drawArea(AreaRenderEvent are) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillArea(org.eclipse.birt.chart.event.AreaRenderEvent)
     */
    public void fillArea(AreaRenderEvent are) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawOval(org.eclipse.birt.chart.event.OvalRenderEvent)
     */
    public void drawOval(OvalRenderEvent ore) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#fillOval(org.eclipse.birt.chart.event.OvalRenderEvent)
     */
    public void fillOval(OvalRenderEvent ore) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#drawText(org.eclipse.birt.chart.event.TextRenderEvent)
     */
    public void drawText(TextRenderEvent tre) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#applyTransformation(org.eclipse.birt.chart.event.TransformationEvent)
     */
    public void applyTransformation(TransformationEvent tev) throws RenderingException
    {
        // DO NOTHING IN NO-OP IMPL
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.IStructureDefinitionListener#changeStructure(org.eclipse.birt.chart.event.StructureChangeEvent)
     */
    public void changeStructure(StructureChangeEvent scev)
    {
        // DO NOTHING IN NO-OP IMPL
        System.out.println("[STRUCT]: scev={0}" + scev.getEventName(false)); // i18n_CONCATENATIONS_REMOVED
    }
    
    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.chart.device.IDeviceRenderer#presentException(java.lang.Exception)
     */
    public void presentException(Exception cexp)
    {
        // DO NOTHING IN NO-OP IMPL
    }
}
