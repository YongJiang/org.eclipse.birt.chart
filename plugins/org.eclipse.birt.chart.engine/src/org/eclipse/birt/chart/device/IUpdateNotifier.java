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

import org.eclipse.birt.chart.model.Chart;

/**
 * This interface defines methods that allow model updating via a device event handler used in conjunction with a device
 * renderer.
 */
public interface IUpdateNotifier
{
    /**
     * Requests the container to regenerate the chart using the design-time model
     */
    void regenerateChart();

    /**
     * Requests the container to repaint the last generated chart
     */
    void repaintChart();

    /**
     * @return An instance of the peer (component) used for device-specific actions
     */
    Object peerInstance();

    /**
     * @return An instance of the chart design-time model
     */
    Chart getDesignTimeModel();

    /**
     * @return An instance of the chart run-time model for the last generated instance
     */
    Chart getRunTimeModel();
}