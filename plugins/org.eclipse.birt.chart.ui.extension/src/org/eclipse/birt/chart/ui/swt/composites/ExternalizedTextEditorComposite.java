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

import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 *  
 */
public class ExternalizedTextEditorComposite extends Canvas implements SelectionListener, Listener
{
    private transient TextEditorComposite txtSelection = null;

    private transient Button btnDown = null;

    private transient int iSize = 20;

    private transient int iStyle = SWT.SINGLE;

    private transient int iHeightHint = -1;

    private transient int iWidthHint = -1;

    private transient Vector vListeners = null;

    public static final int TEXT_CHANGED_EVENT = 1;

    public static final String SEPARATOR = "="; //$NON-NLS-1$

    private transient String sKey = null;

    private transient String sCurrent = ""; //$NON-NLS-1$

    private transient List keys = null;

    private transient IUIServiceProvider serviceprovider = null;

    private transient boolean bEnabled = true;

    public ExternalizedTextEditorComposite(Composite parent, int style, int iHeightHint, int iWidthHint, List keys,
        IUIServiceProvider serviceprovider, String sText)
    {
        super(parent, SWT.NONE);
        this.iStyle = style;
        this.iHeightHint = iHeightHint;
        this.iWidthHint = iWidthHint;
        this.keys = keys;
        this.serviceprovider = serviceprovider;
        sKey = getKey(sText);
        sCurrent = getValue(sText);
        init();
        placeComponents();
    }

    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        vListeners = new Vector();
    }

    private void placeComponents()
    {
        GridLayout glContent = new GridLayout();
        glContent.numColumns = 2;
        glContent.horizontalSpacing = 1;
        glContent.marginHeight = 0;
        glContent.marginWidth = 0;

        this.setLayout(glContent);

        txtSelection = new TextEditorComposite(this, iStyle);
        if (sKey == null || sKey.length() == 0)
        {
            txtSelection.setEnabled(true);
        }
        else
        {
            txtSelection.setEnabled(false);
        }
        txtSelection.setText(sCurrent);

        GridData gdTXTSelection = new GridData(GridData.FILL_HORIZONTAL);
        if (iHeightHint > 0)
        {
            gdTXTSelection.heightHint = iHeightHint - 10;
        }
        if (iWidthHint > 0)
        {
            gdTXTSelection.widthHint = iWidthHint;
        }
        txtSelection.setLayoutData(gdTXTSelection);
        txtSelection.addListener(this);

        btnDown = new Button(this, SWT.PUSH);
        GridData gdBTNDown = new GridData(GridData.VERTICAL_ALIGN_END);
        gdBTNDown.heightHint = iSize;
        gdBTNDown.widthHint = iSize;
        btnDown.setText(" ... "); //$NON-NLS-1$
        btnDown.setToolTipText(Messages.getString("ExternalizedTextEditorComposite.Lbl.EditText")); //$NON-NLS-1$
        btnDown.setLayoutData(gdBTNDown);
        btnDown.addSelectionListener(this);
    }

    public void setEnabled(boolean bState)
    {
        this.txtSelection.setEnabled(bState);
        this.btnDown.setEnabled(bState);
        this.bEnabled = bState;
    }

    public boolean isEnabled()
    {
        return this.bEnabled;
    }

    public void setText(String str)
    {
        sKey = getKey(str);
        sCurrent = getValue(str);
        txtSelection.setText(sCurrent);
    }

    public String getText()
    {
        return buildString();
    }

    private String buildString()
    {
        return sKey + ExternalizedTextEditorComposite.SEPARATOR + sCurrent;
    }

    public String getKey(String str)
    {
        int iSeparator = str.indexOf(SEPARATOR);
        if (iSeparator == -1)
        {
            iSeparator = 0;
        }
        return str.substring(0, iSeparator);
    }

    public String getValue(String str)
    {
        String sTmp = ""; //$NON-NLS-1$
        sTmp = getKey(str);
        if ("".equals(sTmp)) //$NON-NLS-1$
        {
            int iSeparator = str.indexOf(SEPARATOR) + SEPARATOR.length();
            if (iSeparator == (-1 + SEPARATOR.length()))
            {
                iSeparator = 0;
            }
            return str.substring(iSeparator);
        }
        sTmp = serviceprovider.getValue(sTmp);
        if (sTmp == null || "".equals(sTmp)) //$NON-NLS-1$
        {
            sTmp = Messages.getString("ExternalizedTextEditorComposite.Warn.KeyNotFound"); //$NON-NLS-1$
        }
        return sTmp;
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void fireEvent()
    {
        Event event = new Event();
        event.widget = this;
        event.type = TEXT_CHANGED_EVENT;
        event.data = buildString();
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            ((Listener) vListeners.elementAt(iL)).handleEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        ExternalizedTextEditorDialog editor = new ExternalizedTextEditorDialog(getShell(), SWT.APPLICATION_MODAL,
            buildString(), keys, serviceprovider);
        String sTxt = editor.open();
        if (sTxt != null)
        {
            this.setText(sTxt);
            if (sKey == null || sKey.length() == 0)
            {
                txtSelection.setEnabled(true);
            }
            else
            {
                txtSelection.setEnabled(false);
            }
            fireEvent();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        sCurrent = txtSelection.getText();
        fireEvent();
    }
}