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

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *  
 */
public class InsetsComposite extends Composite implements Listener
{

    private transient String sUnits = null;

    public static final int INSETS_CHANGED_EVENT = 1;

    private transient Insets insets = null;

    private transient Group grpInsets = null;

    private transient Composite cmpContent = null;

    private transient TextEditorComposite txtTop = null;

    private transient TextEditorComposite txtLeft = null;

    private transient TextEditorComposite txtBottom = null;

    private transient TextEditorComposite txtRight = null;

    private transient Vector vListeners = null;

    private transient IUIServiceProvider serviceprovider = null;

    /**
     * @param parent
     * @param style
     */
    public InsetsComposite(Composite parent, int style, Insets insets, String sUnits, IUIServiceProvider serviceprovider)
    {
        super(parent, style);
        this.insets = insets;
        this.sUnits = sUnits;
        this.serviceprovider = serviceprovider;
        init();
        placeComponents();
    }

    /**
     *  
     */
    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        this.vListeners = new Vector();
    }

    /**
     *  
     */
    private void placeComponents()
    {
        FillLayout flMain = new FillLayout();
        flMain.marginHeight = 0;
        flMain.marginWidth = 0;

        GridLayout glGroup = new GridLayout();
        glGroup.horizontalSpacing = 5;
        glGroup.verticalSpacing = 5;
        glGroup.marginHeight = 4;
        glGroup.marginWidth = 4;
        glGroup.numColumns = 4;

        this.setLayout(flMain);

        grpInsets = new Group(this, SWT.NONE);
        grpInsets.setLayout(glGroup);
        grpInsets.setText("Insets (in " + sUnits + ")");

        Label lblTop = new Label(grpInsets, SWT.NONE);
        GridData gdLTop = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLTop.heightHint = 20;
        lblTop.setLayoutData(gdLTop);
        lblTop.setText("Top:");

        txtTop = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTTop = new GridData(GridData.FILL_BOTH);
        gdTTop.heightHint = 20;
        gdTTop.widthHint = 45;
        txtTop.setLayoutData(gdTTop);
        double dblPoints = insets.getTop();
        double dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtTop.setText(new Double(dblCurrent).toString());
        txtTop.addListener(this);

        Label lblLeft = new Label(grpInsets, SWT.NONE);
        GridData gdLLeft = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLLeft.heightHint = 20;
        lblLeft.setLayoutData(gdLLeft);
        lblLeft.setText("Left:");

        txtLeft = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTLeft = new GridData(GridData.FILL_BOTH);
        gdTLeft.heightHint = 20;
        gdTLeft.widthHint = 45;
        txtLeft.setLayoutData(gdTLeft);
        dblPoints = insets.getLeft();
        dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtLeft.setText(new Double(dblCurrent).toString());
        txtLeft.addListener(this);

        Label lblBottom = new Label(grpInsets, SWT.NONE);
        GridData gdLBottom = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLBottom.heightHint = 20;
        lblBottom.setLayoutData(gdLBottom);
        lblBottom.setText("Bottom:");

        txtBottom = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTBottom = new GridData(GridData.FILL_BOTH);
        gdTBottom.heightHint = 20;
        gdTBottom.widthHint = 45;
        txtBottom.setLayoutData(gdTBottom);
        dblPoints = insets.getBottom();
        dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtBottom.setText(new Double(dblCurrent).toString());
        txtBottom.addListener(this);

        Label lblRight = new Label(grpInsets, SWT.NONE);
        GridData gdLRight = new GridData(GridData.VERTICAL_ALIGN_CENTER);
        gdLRight.heightHint = 20;
        lblRight.setLayoutData(gdLRight);
        lblRight.setText("Right:");

        txtRight = new TextEditorComposite(grpInsets, SWT.BORDER);
        GridData gdTRight = new GridData(GridData.FILL_BOTH);
        gdTRight.heightHint = 20;
        gdTRight.widthHint = 45;
        txtRight.setLayoutData(gdTRight);
        dblPoints = insets.getRight();
        dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtRight.setText(new Double(dblCurrent).toString());
        txtRight.addListener(this);
    }

    public void setInsets(Insets insets, String sUnits)
    {
        if (insets == null)
        {
            return;
        }
        this.insets = insets;
        this.sUnits = sUnits;

        // Update the UI
        double dblPoints = insets.getBottom();
        double dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtBottom.setText(new Double(dblCurrent).toString());

        dblPoints = insets.getLeft();
        dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtLeft.setText(new Double(dblCurrent).toString());

        dblPoints = insets.getTop();
        dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtTop.setText(new Double(dblCurrent).toString());

        dblPoints = insets.getRight();
        dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
        txtRight.setText(new Double(dblCurrent).toString());

        this.grpInsets.setText("Insets (in " + sUnits + ")");
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void fireEvent()
    {
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            Event se = new Event();
            se.widget = this;
            se.data = this.insets;
            se.type = INSETS_CHANGED_EVENT;
            ((Listener) vListeners.get(iL)).handleEvent(se);
        }
    }

    public Point getPreferredSize()
    {
        return new Point(300, 70);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        double dblCurrent = -1;
        double dblPoints = -1;
        if (event.widget.equals(txtTop))
        {
            try
            {
                dblCurrent = Double.parseDouble(txtTop.getText());
                dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points");
                insets.setTop(dblPoints);
            }
            catch (NumberFormatException e1 )
            {
                dblPoints = insets.getTop();
                dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
                txtTop.setText(new Double(dblCurrent).toString());
            }
        }
        else if (event.widget.equals(txtLeft))
        {
            try
            {
                dblCurrent = Double.parseDouble(txtLeft.getText());
                dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points");
                insets.setLeft(dblPoints);
            }
            catch (NumberFormatException e1 )
            {
                dblPoints = insets.getLeft();
                dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
                txtLeft.setText(new Double(dblCurrent).toString());
            }
        }
        else if (event.widget.equals(txtBottom))
        {
            try
            {
                dblCurrent = Double.parseDouble(txtBottom.getText());
                dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points");
                insets.setLeft(dblPoints);
            }
            catch (NumberFormatException e1 )
            {
                dblPoints = insets.getBottom();
                dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
                txtBottom.setText(new Double(dblCurrent).toString());
            }
        }
        else if (event.widget.equals(txtRight))
        {
            try
            {
                dblCurrent = Double.parseDouble(txtRight.getText());
                dblPoints = serviceprovider.getConvertedValue(dblCurrent, sUnits, "Points");
                insets.setLeft(dblPoints);
            }
            catch (NumberFormatException e1 )
            {
                dblPoints = insets.getRight();
                dblCurrent = serviceprovider.getConvertedValue(dblPoints, "Points", sUnits);
                txtRight.setText(new Double(dblCurrent).toString());
            }
        }
    }
}