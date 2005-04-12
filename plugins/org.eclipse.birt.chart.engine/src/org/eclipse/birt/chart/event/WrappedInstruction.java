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

package org.eclipse.birt.chart.event;

import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.factory.DeferredCache;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;

/**
 *  
 */
public final class WrappedInstruction implements Comparable
{

    private final DeferredCache dc;

    /**
     *  
     */
    private final int iInstruction;

    /**
     *  
     */
    private ArrayList alEvents = null;

    /**
     *  
     */
    private PrimitiveRenderEvent pre = null;

    /**
     *  
     */
    public WrappedInstruction(DeferredCache dc, ArrayList alEvents, int iInstruction)
    {
        this.dc = dc;
        this.alEvents = alEvents;
        this.iInstruction = iInstruction;
    }

    /**
     *  
     */
    public WrappedInstruction(DeferredCache dc, PrimitiveRenderEvent pre, int iInstruction)
    {
        this.dc = dc;
        this.pre = pre;
        this.iInstruction = iInstruction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        try
        {
            Bounds bo = null;
            if (this.isModel())
            {
                if (o instanceof PrimitiveRenderEvent)
                {
                    bo = ((PrimitiveRenderEvent) o).getBounds();
                }
                else if (o instanceof WrappedInstruction)
                {
                    bo = ((WrappedInstruction) o).getBounds();
                }
            }
            else
            {
                bo = ((PrimitiveRenderEvent) pre).getBounds();
            }
            return dc.isTransposed() ? PrimitiveRenderEvent.compareTransposed(getBounds(), bo) : PrimitiveRenderEvent
                .compareRegular(getBounds(), bo);
        }
        catch (Exception ex )
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @return
     */
    public final PrimitiveRenderEvent getEvent()
    {
        return pre;
    }

    /**
     * 
     * @return
     */
    public final int getInstruction()
    {
        return iInstruction;
    }

    /**
     *  
     */
    public String toString()
    {
        return Messages.getString(
            "wrapped.instruction.to.string", //$NON-NLS-1$ 
            new Object[] { super.toString(), new Boolean(isModel()), getBounds() }, 
            Locale.getDefault()
        ); // i18n_CONCATENATIONS_REMOVED
    }

    public final Bounds getBounds()
    {
        if (!isModel())
        {
            try
            {
                return pre.getBounds();
            }
            catch (Exception ex )
            {
                ex.printStackTrace();
            }
        }
        else
        {
            Bounds bo = null;
            for (int i = 0; i < alEvents.size(); i++)
            {
                try
                {
                    if (i == 0)
                    {
                        bo = ((PrimitiveRenderEvent) alEvents.get(i)).getBounds();
                    }
                    else
                    {
                        ((BoundsImpl) bo).max(((PrimitiveRenderEvent) alEvents.get(i)).getBounds());
                    }
                }
                catch (Exception ex )
                {
                    ex.printStackTrace();
                }
            }
            return bo;
        }
        return null;
    }

    public boolean isModel()
    {
        return pre == null;
    }

    public ArrayList getModel()
    {
        return alEvents;
    }
}
