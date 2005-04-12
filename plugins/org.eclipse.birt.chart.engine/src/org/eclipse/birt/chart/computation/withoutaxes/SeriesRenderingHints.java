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

package org.eclipse.birt.chart.computation.withoutaxes;

import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.DataFormatException;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;

/**
 *  
 */
public class SeriesRenderingHints implements ISeriesRenderingHints
{
    /**
     * 
     */
    private final DataPointHints[] dpha;
    
    /**
     * 
     */
    private final PlotWithoutAxes pwoa;

    /**
     * @param pwoa
     * @param dpha
     */
    SeriesRenderingHints(PlotWithoutAxes pwoa, DataPointHints[] dpha)
    {
        this.pwoa = pwoa;
        this.dpha = dpha;
    }

    /**
     * 
     * @return
     */
    public final DataPointHints[] getDataPoints()
    {
        return dpha;
    }

    /**
     * 
     * @return
     * @throws DataFormatException
     */
    public final Double[] asDoubleValues() throws DataFormatException
    {
        final int iCount = dpha.length;
        final Double[] doa = new Double[iCount];
        NumberDataElement nde;
        Object o;

        for (int i = 0; i < iCount; i++)
        {
            o = dpha[i].getOrthogonalValue();
            if (o instanceof NumberDataElement)
            {
                nde = (NumberDataElement) o;
                doa[i] = new Double(nde.getValue());
            }
            else if (o == null)
            {
                doa[i] = null;
            }
            else
            {
                throw new DataFormatException(
                    "exception.dataset.non.numerical.to.numerical", //$NON-NLS-1$
                    new Object[] { o },
                    ResourceBundle.getBundle(
                        Messages.ENGINE, 
                        pwoa.getRunTimeContext().getLocale()
                    )
                ); // i18n_CONCATENATIONS_REMOVED
            }
        }
        return doa;
    }

    /**
     * 
     * @return
     * @throws DataFormatException
     */
    public final double[] asPrimitiveDoubleValues() throws DataFormatException
    {
        final int iCount = dpha.length;
        final double[] doa = new double[iCount];
        Object o;

        int j = 0;
        for (int i = 0; i < iCount; i++)
        {
            o = dpha[i].getOrthogonalValue();
            if (o instanceof Double)
            {
                doa[j++] = ((Double) o).doubleValue();
            }
            else if (o == null)
            {
                continue;
            }
            else
            {
                throw new DataFormatException(
                    "exception.dataset.non.numerical.to.numerical", //$NON-NLS-1$
                    new Object[] { o },
                    ResourceBundle.getBundle(
                        Messages.ENGINE, 
                        pwoa.getRunTimeContext().getLocale()
                    )
                ); // i18n_CONCATENATIONS_REMOVED
            }
        }
        final double[] doaTrimmed = new double[j];
        System.arraycopy(doa, 0, doaTrimmed, 0, j);
        return doaTrimmed;
    }
}
