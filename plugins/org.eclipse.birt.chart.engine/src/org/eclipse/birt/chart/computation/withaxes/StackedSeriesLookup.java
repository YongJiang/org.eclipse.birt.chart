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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.exception.UndefinedValueException;
import org.eclipse.birt.chart.exception.UnexpectedInputException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.emf.common.util.EList;

/**
 * This class implements a double lookup data structure for stacked series. It also maintains simulates min/max value
 * for each unit needed to build the scale.
 */
public final class StackedSeriesLookup
{

    /**
     *  
     */
    private final Hashtable htAxisToStackGroups;

    /**
     *  
     */
    private final Hashtable htSeriesToStackGroup;

    /**
     *  
     */
    private int iCachedUnitCount = 0;

    /**
     *  
     */
    StackedSeriesLookup()
    {
        htAxisToStackGroups = new Hashtable();
        htSeriesToStackGroup = new Hashtable();
    }

    /**
     * 
     * @param ax
     * @return
     */
    public final ArrayList getStackGroups(Axis ax)
    {
        return (ArrayList) htAxisToStackGroups.get(ax);
    }

    /**
     * 
     * @param ax
     * @return
     */
    public final int getSeriesCount(Axis ax)
    {
        final ArrayList alSG = (ArrayList) htAxisToStackGroups.get(ax);
        if (alSG == null || alSG.isEmpty())
        {
            return 0;
        }

        int iCount = 0;
        StackGroup sg;
        for (int i = 0; i < alSG.size(); i++)
        {
            sg = (StackGroup) alSG.get(i);
            iCount += sg.alSeries.size();
        }
        return iCount;
    }

    /**
     * @param se
     * @return The stack group associated with a specified Series
     */
    public final StackGroup getStackGroup(Series se)
    {
        return (StackGroup) htSeriesToStackGroup.get(se);
    }

    /**
     * @param sg
     * @param iUnitIndex
     * @return An AxisUnit corresponding to a given stack group and specified unit index
     */
    public final AxisSubUnit getSubUnit(StackGroup sg, int iUnitIndex)
    {
        if (sg == null || !htSeriesToStackGroup.contains(sg))
        {
            return null;
        }

        // IF NOT YET INITIALIZED, DO SO LAZILY
        if (sg.alUnitPositions == null)
        {
            sg.alUnitPositions = new ArrayList(8);
        }

        // IF NOT YET CONTAINED, ADD LAZILY
        if (sg.alUnitPositions.size() <= iUnitIndex)
        {
            sg.alUnitPositions.add(new AxisSubUnit());
        }

        return (AxisSubUnit) sg.alUnitPositions.get(iUnitIndex);
    }

    /**
     * Returns an AxisUnit needed to 'remember' the position of the next stacked bar to be rendered. If a series is not
     * 'stackable' or not 'set as stacked', this method will return 'null'.
     * 
     * @param ax
     * @param se
     * @param iUnitIndex
     * 
     * @return
     */
    public final AxisSubUnit getUnit(Series se, int iUnitIndex)
    {
        // LOOKUP STACKED GROUP FOR SERIES
        StackGroup sg = (StackGroup) htSeriesToStackGroup.get(se);
        if (sg == null)
        {
            return null;
        }

        // IF NOT YET INITIALIZED, DO SO LAZILY
        if (sg.alUnitPositions == null)
        {
            sg.alUnitPositions = new ArrayList(8);
        }

        // IF NOT YET CONTAINED, ADD LAZILY
        if (sg.alUnitPositions.size() <= iUnitIndex)
        {
            sg.alUnitPositions.add(new AxisSubUnit());
        }

        return (AxisSubUnit) sg.alUnitPositions.get(iUnitIndex);
    }

    /**
     *  
     */
    public final void resetSubUnits()
    {
        Enumeration e = htSeriesToStackGroup.elements();
        StackGroup sg;
        AxisSubUnit asu;

        while (e.hasMoreElements())
        {
            sg = (StackGroup) e.nextElement();
            if (sg.alUnitPositions != null)
            {
                for (int i = 0; i < sg.alUnitPositions.size(); i++)
                {
                    asu = (AxisSubUnit) sg.alUnitPositions.get(i);
                    asu.reset();
                }
            }
        }
    }

    /**
     * 
     * @param cwa
     * @return @throws
     *         UndefinedValueException
     * @throws UnexpectedInputException
     */
    static final StackedSeriesLookup create(ChartWithAxes cwa) throws UndefinedValueException, UnexpectedInputException
    {
        if (cwa == null) // NPE CHECK
        {
            return null;
        }

        final StackedSeriesLookup ssl = new StackedSeriesLookup();
        final Axis axBase = cwa.getBaseAxes()[0];
        final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axBase, true);

        EList el;
        ArrayList alSeries;
        int iSeriesCount;
        StackGroup sg, sgSingle;
        Series se;
        boolean bStackedSet;
        SeriesDefinition sd;
        int iSharedUnitIndex, iSharedUnitCount, iDataSetCount;
        ArrayList alSGCopies;
        DataSetIterator dsi = null;

        for (int i = 0; i < axaOrthogonal.length; i++) // EACH AXIS
        {
            iSharedUnitIndex = 0;
            iSharedUnitCount = 0;
            sgSingle = null; // RESET PER AXIS
            el = axaOrthogonal[i].getSeriesDefinitions();
            alSGCopies = new ArrayList(4);
            iSharedUnitCount = 0;

            for (int j = 0; j < el.size(); j++) // EACH SERIES DEFINITION
            {
                sd = (SeriesDefinition) el.get(j);
                alSeries = (ArrayList) sd.getRunTimeSeries();
                iSeriesCount = alSeries.size();
                if (iSeriesCount > 1)
                {
                    bStackedSet = false;
                    sg = null;

                    for (int k = 0; k < iSeriesCount; k++) // EACH SERIES
                    {
                        se = (Series) alSeries.get(k);
                        dsi = new DataSetIterator(se.getDataSet());
                        iDataSetCount = dsi.size();
                        if (ssl.iCachedUnitCount == 0)
                        {
                            ssl.iCachedUnitCount = iDataSetCount;
                        }
                        else if (ssl.iCachedUnitCount != iDataSetCount)
                        {
                            throw new UnexpectedInputException("Mismatch (" + ssl.iCachedUnitCount + "!="
                                + iDataSetCount + ") in dataset count found in stacked runtime series");
                        }
                        if (se.canBeStacked())
                        {
                            if (!se.isSetStacked())
                            {
                                throw new UndefinedValueException("The stacked property for stackable series " + se
                                    + " has not been explicitly set.");
                            }
                            if (se.canShareAxisUnit())
                            {
                                if (se.isStacked())
                                {
                                    if (k > 0 && !bStackedSet)
                                    {
                                        throw new UnexpectedInputException("Series definition " + sd
                                            + " contains a mix of stacked and unstacked series.");
                                    }
                                    if (k == 0) // ONE GROUP FOR ALL STACKED
                                    // SERIES
                                    {
                                        sg = new StackGroup(iSharedUnitIndex++);
                                        alSGCopies.add(sg);
                                        iSharedUnitCount++;
                                    }
                                    bStackedSet = true;
                                    ssl.htSeriesToStackGroup.put(se, sg);
                                    sg.addSeries(se); // REQUIRE REVERSE
                                    // LOOKUP
                                }
                                else
                                {
                                    if (k > 0 && bStackedSet)
                                    {
                                        throw new UnexpectedInputException("Series definition " + sd
                                            + " contains a mix of stacked and unstacked series.");
                                    }
                                    sg = new StackGroup(iSharedUnitIndex++); // NEW
                                    // GROUP
                                    // FOR
                                    // EACH
                                    // UNSTACKED
                                    // SERIES
                                    alSGCopies.add(sg);
                                    iSharedUnitCount++;
                                    ssl.htSeriesToStackGroup.put(se, sg);
                                    sg.addSeries(se); // REQUIRE REVERSE
                                    // LOOKUP
                                }
                            }
                            else
                            {
                                if (se.isStacked())
                                {
                                    if (k > 0 && !bStackedSet)
                                    {
                                        throw new UnexpectedInputException("Series definition " + sd
                                            + " contains a mix of stacked and unstacked series.");
                                    }
                                    if (k == 0) // ONE GROUP FOR ALL STACKED
                                    // SERIES
                                    {
                                        sg = new StackGroup(-1); // UNSET
                                        // BECAUSE
                                        // DOESNT
                                        // SHARE AXIS
                                        // UNITS
                                        alSGCopies.add(sg);
                                    }
                                    bStackedSet = true;
                                    ssl.htSeriesToStackGroup.put(se, sg);
                                    sg.addSeries(se); // REQUIRE REVERSE
                                    // LOOKUP
                                }
                                else
                                {
                                    if (k > 0 && bStackedSet)
                                    {
                                        throw new UnexpectedInputException("Series definition " + sd
                                            + " contains a mix of stacked and unstacked series.");
                                    }
                                    sg = new StackGroup(-1); // NEW GROUP FOR
                                    // EACH UNSTACKED
                                    // SERIES
                                    alSGCopies.add(sg);
                                    ssl.htSeriesToStackGroup.put(se, sg);
                                    sg.addSeries(se); // REQUIRE REVERSE
                                    // LOOKUP
                                }
                            }
                        }
                        else
                        // e.g. each custom series in its own stack (not stacked
                        // but
                        {
                            sg = new StackGroup(-1); // ONE PER UNSTACKED
                            // SERIES (SHARED INDEX
                            // IS UNSET)
                            alSGCopies.add(sg);
                            ssl.htSeriesToStackGroup.put(se, sg);
                            sg.addSeries(se); // REQUIRE REVERSE LOOKUP
                        }
                    }
                }
                else
                // ONE OR LESS SERIES USE THE SINGLE STACK GROUP
                {
                    for (int k = 0; k < iSeriesCount; k++) // EACH SERIES
                    // (iSeriesCount
                    // SHOULD BE ONE)
                    {
                        se = (Series) alSeries.get(k);
                        dsi = new DataSetIterator(se.getDataSet());
                        iDataSetCount = dsi.size();
                        if (ssl.iCachedUnitCount == 0)
                        {
                            ssl.iCachedUnitCount = iDataSetCount;
                        }
                        else if (ssl.iCachedUnitCount != iDataSetCount)
                        {
                            throw new UnexpectedInputException("Mismatch (" + ssl.iCachedUnitCount + "!="
                                + iDataSetCount + ") in dataset count found in stacked runtime series");
                        }
                        if (se.canBeStacked())
                        {
                            if (se.canShareAxisUnit())
                            {
                                if (se.isStacked())
                                {
                                    if (sgSingle == null)
                                    {
                                        sgSingle = new StackGroup(iSharedUnitIndex++);
                                        alSGCopies.add(sgSingle);
                                        iSharedUnitCount++;
                                    }
                                    ssl.htSeriesToStackGroup.put(se, sgSingle);
                                    sgSingle.addSeries(se); // REQUIRE REVERSE
                                    // LOOKUP
                                }
                                else
                                {
                                    sg = new StackGroup(iSharedUnitIndex++); // ONE
                                    // PER
                                    // UNSTACKED
                                    // SERIES
                                    // (SHARED
                                    // INDEX
                                    // IS
                                    // SET)
                                    iSharedUnitCount++;
                                    alSGCopies.add(sg);
                                    ssl.htSeriesToStackGroup.put(se, sg);
                                    sg.addSeries(se); // REQUIRE REVERSE
                                    // LOOKUP
                                }
                            }
                            else
                            // e.g. each line series in its own stack
                            {
                                sg = new StackGroup(-1); // ONE PER UNSTACKED
                                // SERIES (SHARED
                                // INDEX IS UNSET)
                                alSGCopies.add(sg);
                                ssl.htSeriesToStackGroup.put(se, sg);
                                sg.addSeries(se); // REQUIRE REVERSE LOOKUP
                            }
                        }
                        else
                        // e.g. each custom series in its own stack (not stacked
                        // but
                        {
                            sg = new StackGroup(-1); // ONE PER UNSTACKED
                            // SERIES (SHARED INDEX
                            // IS UNSET)
                            alSGCopies.add(sg);
                            ssl.htSeriesToStackGroup.put(se, sg);
                            sg.addSeries(se); // REQUIRE REVERSE LOOKUP
                        }
                    }
                }
            }

            if (iSharedUnitCount < 1)
                iSharedUnitCount = 1;
            for (int j = 0; j < alSGCopies.size(); j++)
            {
                sg = (StackGroup) alSGCopies.get(j);
                //if (sg.getSharedIndex() >= 0)
                {
                    sg.updateCount(iSharedUnitCount);
                }
            }

            ssl.htAxisToStackGroups.put(axaOrthogonal[i], alSGCopies);
        }

        return ssl;
    }

    public final int getUnitCount()
    {
        return iCachedUnitCount;
    }
}