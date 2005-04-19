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

package org.eclipse.birt.chart.computation;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.exception.UnexpectedInputException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.util.CDateTime;

/**
 * An immutable class with convenience methods provided to retrieve data
 * from the dataset
 */
public final class DataSetIterator implements Iterator
{

    /**
     *  
     */
    private double[] da = null;

    /**
     *  
     */
    private Double[] dda = null;

    /**
     *  
     */
    private Calendar[] ca = null;

    /**
     *  
     */
    private long[] la = null;

    /**
     *  
     */
    private String[] sa = null;

    /**
     *  
     */
    private Collection co = null;

    /**
     *  
     */
    private int iDataType = IConstants.UNDEFINED;

    /**
     *  
     */
    private int iContentType = IConstants.UNDEFINED;

    /**
     *  
     */
    private int iCursor = 0;

    /**
     *  
     */
    private Iterator it = null;

    /**
     *  
     */
    private int iRowCount = 0;

    /**
     *  
     */
    private Calendar cReused = null;

    /**
     *  
     */
    private Object[] oa = null;

    /**
     * 
     * @param ds
     * @throws UnexpectedInputException
     */
    public DataSetIterator(Double[] dda)
    {
        this.dda = dda;
        iDataType = IConstants.NUMERICAL;
        iContentType = IConstants.NON_PRIMITIVE_ARRAY;
        iRowCount = dda.length;
    }

    /**
     * 
     * @param sa
     */
    public DataSetIterator(String[] sa)
    {
        this.sa = sa;
        iDataType = IConstants.TEXT;
        iContentType = IConstants.NON_PRIMITIVE_ARRAY;
        iRowCount = sa.length;
    }

    /**
     * 
     * @param sa
     */
    public DataSetIterator(Calendar[] ca)
    {
        this.ca = ca;
        iDataType = IConstants.DATE_TIME;
        iContentType = IConstants.NON_PRIMITIVE_ARRAY;
        iRowCount = ca.length;
        updateDateTimeValues();
    }

    /**
     * 
     * @param ds
     * @throws UnexpectedInputException
     */
    public DataSetIterator(Object oContent, int iDataType) throws UnexpectedInputException
    {
        if ((iDataType & IConstants.LINEAR) == IConstants.LINEAR
            || (iDataType & IConstants.LOGARITHMIC) == IConstants.LOGARITHMIC)
        {
            iDataType = IConstants.NUMERICAL;
        }
        this.iDataType = iDataType;
        if (iDataType == IConstants.NUMERICAL)
        {
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof double[])
            {
                iContentType = IConstants.PRIMITIVE_ARRAY;
                da = (double[]) oContent;
            }
            else if (oContent instanceof Double[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                dda = (Double[]) oContent;
            }
        }
        else if (iDataType == IConstants.DATE_TIME)
        {
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof long[])
            {
                iContentType = IConstants.PRIMITIVE_ARRAY;
                la = (long[]) oContent;
                cReused = Calendar.getInstance();
            }
            else if (oContent instanceof Calendar[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                ca = (Calendar[]) oContent;
            }
            updateDateTimeValues();
        }
        else if (iDataType == IConstants.TEXT)
        {
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof String[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                sa = (String[]) oContent;
            }
        }
        else
        // e.g. StockObject[]
        {
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof Object[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                oa = (Object[]) oContent;
            }
            iDataType = IConstants.UNDEFINED;
        }

        if (iContentType == IConstants.UNDEFINED)
        {
            throw new UnexpectedInputException(
                "exception.process.content.type", //$NON-NLS-1$
                new Object[] { oContent, new Integer(iDataType) },
                ResourceBundle.getBundle(
                    Messages.ENGINE, 
                    Locale.getDefault() // LOCALE?
                )
            ); // i18n_CONCATENATIONS_REMOVED 
        }

        if (co != null)
        {
            it = co.iterator();
        }

        iRowCount = getRowCountInternal();
    }

    /**
     * 
     * @param ds
     * @throws UnexpectedInputException
     */
    public DataSetIterator(DataSet ds) throws UnexpectedInputException
    {
        Object oContent = ds.getValues();
        if (ds instanceof NumberDataSet)
        {
            iDataType = IConstants.NUMERICAL;
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof double[])
            {
                iContentType = IConstants.PRIMITIVE_ARRAY;
                da = (double[]) oContent;
            }
            else if (oContent instanceof Double[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                dda = (Double[]) oContent;
            }
        }
        else if (ds instanceof DateTimeDataSet)
        {
            iDataType = IConstants.DATE_TIME;
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof long[])
            {
                iContentType = IConstants.PRIMITIVE_ARRAY;
                la = (long[]) oContent;
                cReused = Calendar.getInstance();
            }
            else if (oContent instanceof Calendar[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                ca = (Calendar[]) oContent;
            }
            updateDateTimeValues();
        }
        else if (ds instanceof TextDataSet)
        {
            iDataType = IConstants.TEXT;
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof String[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                sa = (String[]) oContent;
            }
        }
        else
        // e.g. StockObject[]
        {
            iDataType = IConstants.OTHER;
            if (oContent instanceof Collection)
            {
                iContentType = IConstants.COLLECTION;
                co = (Collection) oContent;
            }
            else if (oContent instanceof Object[])
            {
                iContentType = IConstants.NON_PRIMITIVE_ARRAY;
                oa = (Object[]) oContent;
            }
            iDataType = IConstants.UNDEFINED;
        }

        if (iContentType == IConstants.UNDEFINED)
        {
            throw new UnexpectedInputException(
                "exception.process.content.dataset", //$NON-NLS-1$
                new Object[] { oContent, ds },
                ResourceBundle.getBundle(
                    Messages.ENGINE, 
                    Locale.getDefault() // LOCALE?
                )
            ); // i18n_CONCATENATIONS_REMOVED 
        }

        if (co != null)
        {
            it = co.iterator();
        }

        iRowCount = getRowCountInternal();
    }

    /**
     * @return
     */
    public final boolean isEmpty()
    {
        return iRowCount <= 0;
    }
    
    /**
     * @return
     */
    private final int getRowCountInternal()
    {
        if (iContentType == IConstants.COLLECTION)
        {
            return co.size();
        }
        else if (iDataType == IConstants.TEXT)
        {
            return sa.length;
        }
        else if (iDataType == IConstants.NUMERICAL)
        {
            if (iContentType == IConstants.PRIMITIVE_ARRAY)
            {
                return da.length;
            }
            else if (iContentType == IConstants.NON_PRIMITIVE_ARRAY)
            {
                return dda.length;
            }
        }
        else if (iDataType == IConstants.DATE_TIME)
        {
            if (iContentType == IConstants.PRIMITIVE_ARRAY)
            {
                return la.length;
            }
            else if (iContentType == IConstants.NON_PRIMITIVE_ARRAY)
            {
                return ca.length;
            }
        }
        else if (iDataType == IConstants.TEXT)
        {
            return sa.length;
        }
        else if (oa != null)
        {
            return oa.length;
        }
        return -1; // <<<=== SHOULD NEVER REACH HERE
    }

    /**
     * @return
     */
    public final double nextPrimitiveDouble()
    {
        return da[iCursor++];
    }

    /**
     * @return
     */
    public final Double nextDouble()
    {
        if (it != null)
        {
            iCursor++;
            return (Double) it.next();
        }
        return dda[iCursor++];
    }

    /**
     * @return
     */
    public final Calendar nextDateTime()
    {
        if (it != null)
        {
            iCursor++;
            return (Calendar) it.next();
        }
        return ca[iCursor++];
    }

    /**
     * @return
     */
    public final String nextText()
    {
        if (it != null)
        {
            iCursor++;
            return (String) it.next();
        }
        return sa[iCursor++];
    }

    /**
     * @return
     */
    public final Object nextObject()
    {
        return oa[iCursor++];
    }

    /**
     * @return
     */
    public final Calendar nextPrimitiveDateTime()
    {
        cReused.setTimeInMillis(la[iCursor++]);
        return cReused;
    }

    /**
     * 
     * @return
     */
    public boolean hasNext()
    {
        if (it != null)
        {
            return it.hasNext();
        }
        else
        {
            return (iCursor < iRowCount);
        }
    }

    /**
     * 
     * @return
     */
    public final Object next()
    {
        if (iCursor >= iRowCount)
        {
            throw new RuntimeException(
                new ChartException(
                    "exception.out.of.bounds", //$NON-NLS-1$
                    ResourceBundle.getBundle(
                        Messages.ENGINE, 
                        Locale.getDefault() // LOCALE?
                    )
                )
            ); 
        }

        if (it != null)
        {
            iCursor++;
            return it.next();
        }

        if (iDataType == IConstants.NUMERICAL)
        {
            if (iContentType == IConstants.NON_PRIMITIVE_ARRAY)
            {
                return nextDouble();
            }
            else if (iContentType == IConstants.PRIMITIVE_ARRAY)
            {
                return new Double(nextPrimitiveDouble());
            }
        }
        else if (iDataType == IConstants.DATE_TIME)
        {
            if (iContentType == IConstants.NON_PRIMITIVE_ARRAY)
            {
                return nextDateTime();
            }
            else if (iContentType == IConstants.PRIMITIVE_ARRAY)
            {
                return nextPrimitiveDateTime();
            }
        }
        else if (iDataType == IConstants.TEXT)
        {
            return nextText();
        }
        else
        // OTHER
        {
            return nextObject();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove()
    {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @return
     */
    public int size()
    {
        return iRowCount;
    }

    /**
     *  
     */
    public final void reset()
    {
        iCursor = 0;
        if (co != null)
        {
            it = co.iterator();
        }
    }

    /**
     * 
     * @return
     */
    public final Object first()
    {
        reset();
        return next();
    }

    /**
     * 
     * @return
     */
    public final Object last()
    {
        // TBD: OPTIMIZE FOR DIRECT ACCESS TO LAST ELEMENT IN ARRAY
        reset();
        Object o = null;
        while (hasNext())
        {
            o = next();
        }
        return o;
    }

    /**
     * Frees all references to data held internally in this structure
     */
    public final void clear()
    {
        dda = null;
        ca = null;
        da = null;
        la = null;
        oa = null;
        sa = null;
        if (co != null)
        {
            //co.clear();
            co = null;
        }
        iContentType = IConstants.UNDEFINED;
        iDataType = IConstants.UNDEFINED;
        iRowCount = 0;
        iCursor = 0;
        cReused = null;
        it = null;
    }

    /**
     *  
     */
    public final void notifyDataUpdate()
    {
        reset();
        iRowCount = getRowCountInternal();
    }

    /**
     *  
     */
    final void updateDateTimeValues()
    {
        iRowCount = getRowCountInternal();
        Calendar cValue;
        CDateTime[] cdta = new CDateTime[size()];
        reset();
        int i = 0;
        while (hasNext())
        {
            cValue = (Calendar) next();
            cdta[i++] = (cValue == null) ? null : new CDateTime(cValue);
        }

        final int iUnit = CDateTime.computeUnit(cdta); //CDateTime.getDifference( new CDateTime( caMin ), new
        // CDateTime( caMax ) );
        for (i = 0; i < cdta.length; i++)
        {
            cdta[i].clearBelow(iUnit);
        }

        clear();
        ca = cdta;
        iDataType = IConstants.DATE_TIME;
        iContentType = IConstants.NON_PRIMITIVE_ARRAY;
        iRowCount = ca.length;
    }

    /**
     * @return
     */
    public final int getDataType()
    {
        return iDataType;
    }
}
