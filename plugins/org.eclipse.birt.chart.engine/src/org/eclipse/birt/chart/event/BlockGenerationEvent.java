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

import org.eclipse.birt.chart.model.layout.Block;

/**
 * BlockGenerationEvent
 */
public class BlockGenerationEvent extends ChartEvent
{

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5869588499778117671L;

	/**
     * @param oSource
     */
    public BlockGenerationEvent(Object oSource)
    {
        super(oSource);
    }

    /**
     * 
     * @param bl
     */
    public final void updateBlock(Block bl)
    {
        source = bl;
    }
    
    public void reset()
    {
    	source = null;
    }
}
