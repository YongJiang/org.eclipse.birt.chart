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

import java.util.Collection;

import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 *  
 */
public class TriggerEditorDialog implements SelectionListener
{
    private transient Shell shell = null;

    private transient Button btnAccept = null;

    private transient Button btnCancel = null;

    private transient EList triggers = null;

    private transient Collection vOriginalTriggers = null;

    private transient TriggerEditorComposite triggereditor = null;

    /**
     *  
     */
    public TriggerEditorDialog(EList triggers)
    {
        super();
        this.triggers = triggers;
        vOriginalTriggers = EcoreUtil.copyAll(triggers);

        shell = new Shell(Display.getCurrent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        shell.setLayout(new FillLayout());
        placeComponents();
        shell.setText("Trigger Editor Dialog:");
        shell.setSize(400, 340);
        UIHelper.centerOnScreen(shell);
        shell.layout();
        shell.open();
        while (!shell.isDisposed())
        {
            if (!shell.getDisplay().readAndDispatch())
            {
                shell.getDisplay().sleep();
            }
        }
    }

    private void placeComponents()
    {
        GridLayout glContent = new GridLayout();
        glContent.marginHeight = 7;
        glContent.marginWidth = 7;
        glContent.verticalSpacing = 5;

        Composite cmpContent = new Composite(shell, SWT.NONE);
        cmpContent.setLayout(glContent);

        triggereditor = new TriggerEditorComposite(cmpContent, SWT.NONE, triggers);
        GridData gdTriggerEditor = new GridData(GridData.FILL_HORIZONTAL);
        triggereditor.setLayoutData(gdTriggerEditor);

        GridLayout glButtons = new GridLayout();
        glButtons.numColumns = 2;
        glButtons.horizontalSpacing = 5;

        Composite cmpButtons = new Composite(cmpContent, SWT.NONE);
        GridData gdButtons = new GridData(GridData.FILL_HORIZONTAL);
        cmpButtons.setLayoutData(gdButtons);
        cmpButtons.setLayout(glButtons);

        btnAccept = new Button(cmpButtons, SWT.PUSH);
        GridData gdBTNAccept = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gdBTNAccept.grabExcessHorizontalSpace = true;
        btnAccept.setLayoutData(gdBTNAccept);
        btnAccept.setText("   OK   ");
        btnAccept.addSelectionListener(this);

        btnCancel = new Button(cmpButtons, SWT.PUSH);
        GridData gdBTNCancel = new GridData(GridData.HORIZONTAL_ALIGN_END);
        gdBTNCancel.grabExcessHorizontalSpace = false;
        btnCancel.setLayoutData(gdBTNCancel);
        btnCancel.setText("Cancel");
        btnCancel.addSelectionListener(this);
    }

    public EList getTriggers()
    {
        return triggers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (e.getSource().equals(btnAccept))
        {
            shell.dispose();
        }
        else if (e.getSource().equals(btnCancel))
        {
            triggers.clear();
            triggers.addAll(vOriginalTriggers);
            shell.dispose();
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
}