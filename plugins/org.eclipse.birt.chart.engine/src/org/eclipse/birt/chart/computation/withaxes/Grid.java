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

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.attribute.LineAttributes;

/**
 * This class ...
 * 
 * @author Actuate Corporation
 */
public final class Grid
{

    int iMajorTickStyle = 0;

    int iMinorTickStyle = 0;

    int iMinorUnitsPerMajorUnit = 0;

    LineAttributes laMajorGrid;

    LineAttributes laMinorGrid;

    public final int getTickStyle(int iMajorOrMinor)
    {
        if (iMajorOrMinor == IConstants.MAJOR)
        {
            return iMajorTickStyle;
        }
        else if (iMajorOrMinor == IConstants.MINOR)
        {
            return iMinorTickStyle;
        }
        return IConstants.TICK_NONE;
    }

    public final LineAttributes getLineAttributes(int iMajorOrMinor)
    {
        if (iMajorOrMinor == IConstants.MAJOR)
        {
            return laMajorGrid;
        }
        else if (iMajorOrMinor == IConstants.MINOR)
        {
            return laMinorGrid;
        }
        return null;
    }

    public final int getMinorCountPerMajor()
    {
        return iMinorUnitsPerMajorUnit;
    }
}

