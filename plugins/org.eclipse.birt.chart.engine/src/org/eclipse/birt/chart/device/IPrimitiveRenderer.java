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

import java.util.EventListener;

import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.RenderingException;

/**
 * This interface represents low level primitive rendering notifications
 * 
 * @author Actuate Corporation
 */
public interface IPrimitiveRenderer extends EventListener
{

    /**
     * This method is responsible for clipping an arbitrary area on the target rendering device's graphic context.
     * 
     * @param cre
     *            Encapsulated information that defines the area to be clipped
     * @throws RenderingException
     */
    public void setClip(ClipRenderEvent cre);

    /**
     * This method is responsible for drawing an image on the target rendering device's graphic context.
     * 
     * @param ire
     *            Encapsulated information that defines a polygon and its attributes
     * @throws RenderingException
     */
    public void drawImage(ImageRenderEvent ire) throws RenderingException;

    /**
     * This method is responsible for drawing a line on the target rendering device's graphic context.
     * 
     * @param lre
     *            Encapsulated information that defines a line and its attributes
     * @throws RenderingException
     */
    public void drawLine(LineRenderEvent lre) throws RenderingException;

    /**
     * This method is responsible for drawing a rectangle on the target rendering device's graphic context.
     * 
     * @param rre
     *            Encapsulated information that defines a rectangle and its attributes
     * @throws RenderingException
     */
    public void drawRectangle(RectangleRenderEvent rre) throws RenderingException;

    /**
     * This method is responsible for filling a rectangle on the target rendering device's graphic context.
     * 
     * @param rre
     *            Encapsulated information that defines a rectangle and its attributes
     * @throws RenderingException
     */
    public void fillRectangle(RectangleRenderEvent rre) throws RenderingException;

    /**
     * This method is responsible for drawing a polygon on the target rendering device's graphic context.
     * 
     * @param pre
     *            Encapsulated information that defines a polygon and its attributes
     * @throws RenderingException
     */
    public void drawPolygon(PolygonRenderEvent pre) throws RenderingException;

    /**
     * This method is responsible for filling a polygon on the target rendering device's graphic context.
     * 
     * @param pre
     *            Encapsulated information that defines a polygon and its attributes
     * @throws RenderingException
     */
    public void fillPolygon(PolygonRenderEvent pre) throws RenderingException;

    /**
     * This method is responsible for drawing an elliptical arc on the target rendering device's graphic context.
     * 
     * @param are
     *            Encapsulated information that defines the arc and its attributes
     * @throws RenderingException
     */
    public void drawArc(ArcRenderEvent are) throws RenderingException;

    /**
     * This method is responsible for filling an elliptical arc on the target rendering device's graphic context.
     * 
     * @param are
     *            Encapsulated information that defines an arc and its attributes
     * @throws RenderingException
     */
    public void fillArc(ArcRenderEvent are) throws RenderingException;

    /**
     * 
     * @param ie
     * @throws RenderingException
     */
    public void enableInteraction(InteractionEvent ie) throws RenderingException;

    /**
     * This method is responsible for drawing a custom defined area on the target rendering device's graphic context.
     * 
     * @param are
     *            Encapsulated information that defines the area and its attributes
     * @throws RenderingException
     */
    public void drawArea(AreaRenderEvent are) throws RenderingException;

    /**
     * This method is responsible for filling a custom defined area on the target rendering device's graphic context.
     * 
     * @param are
     *            Encapsulated information that defines the area and its attributes
     * @throws RenderingException
     */
    public void fillArea(AreaRenderEvent are) throws RenderingException;

    /**
     * This method is responsible for drawing an oval area on the target rendering device's graphic context.
     * 
     * @param ore
     *            Encapsulated information that defines the oval and its attributes
     * @throws RenderingException
     */
    public void drawOval(OvalRenderEvent ore) throws RenderingException;

    /**
     * This method is responsible for filling an oval area on the target rendering device's graphic context.
     * 
     * @param ore
     *            Encapsulated information that defines the oval and its attributes
     * @throws RenderingException
     */
    public void fillOval(OvalRenderEvent ore) throws RenderingException;

    /**
     * This method renders text on the target rendering device's graphic context using one of the three methods:
     * 
     * 1. Renders text (with optional insets, border, fill, etc) with the encapsulating container rectangle's corner or
     * edge aligning against a given point 2. Renders a shadow offset with the encapsulating container rectangle's
     * corner or edge aligning against a given point 3. Renders text (with optional insets, border, fill, etc) with the
     * encapsulating container rectangle's bounding box aligned with a parent block's bounding box
     * 
     * @param ore
     *            Encapsulated information that defines the text being rendered, its position and various other
     *            attributes
     * @throws RenderingException
     */
    public void drawText(TextRenderEvent tre) throws RenderingException;

    /**
     * This method is capable of applying a global transformation on the device specific graphics context Available
     * transformation types are: SCALE, TRANSLATE, ROTATE
     * 
     * @param tev
     * @throws RenderingException
     */
    public void applyTransformation(TransformationEvent tev) throws RenderingException;
}