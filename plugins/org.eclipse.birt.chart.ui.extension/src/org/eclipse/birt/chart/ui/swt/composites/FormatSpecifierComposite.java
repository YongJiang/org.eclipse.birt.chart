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

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 *  
 */
public class FormatSpecifierComposite extends Composite implements SelectionListener, Listener, ModifyListener
{
    private transient Button btnUndefined = null;

    private transient Button btnStandard = null;

    private transient Button btnAdvanced = null;

    private transient Combo cmbDataType = null;

    // Composites for Standard Properties
    private transient Composite cmpStandardDetails = null;

    private transient StackLayout slStandardDetails = null;

    private transient Composite cmpStandardDateDetails = null;

    private transient Composite cmpDateStandard = null;

    private transient Combo cmbDateType = null;

    private transient Combo cmbDateForm = null;

    private transient Composite cmpStandardNumberDetails = null;

    private transient Composite cmpNumberStandard = null;

    private transient Text txtPrefix = null;

    private transient Text txtSuffix = null;

    private transient Text txtMultiplier = null;

    private transient IntegerSpinControl iscFractionDigits = null;

    // Composites for Advanced Properties
    private transient Composite cmpAdvancedDetails = null;

    private transient StackLayout slAdvancedDetails = null;

    private transient Composite cmpAdvancedDateDetails = null;

    private transient Composite cmpDateAdvanced = null;

    private transient Text txtDatePattern = null;

    private transient Composite cmpAdvancedNumberDetails = null;

    private transient Composite cmpNumberAdvanced = null;

    private transient Text txtNumberPattern = null;

    private transient Text txtAdvMultiplier = null;

    private transient FormatSpecifier formatspecifier = null;

    private transient boolean bEnableEvents = true;

    /**
     * @param parent
     * @param style
     */
    public FormatSpecifierComposite(Composite parent, int style, FormatSpecifier formatspecifier)
    {
        super(parent, style);
        this.formatspecifier = formatspecifier;
        init();
        placeComponents();
    }

    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
    }

    private void placeComponents()
    {
        // Layout for the content composite
        GridLayout glContent = new GridLayout();
        glContent.numColumns = 2;
        glContent.marginHeight = 7;
        glContent.marginWidth = 7;
        glContent.horizontalSpacing = 5;
        glContent.verticalSpacing = 5;

        // Layout for the details composite
        slStandardDetails = new StackLayout();

        // Layout for the details composite
        slAdvancedDetails = new StackLayout();

        this.setLayout(glContent);

        Label lblDataType = new Label(this, SWT.NONE);
        GridData gdLBLDataType = new GridData();
        lblDataType.setLayoutData(gdLBLDataType);
        lblDataType.setText("Data Type:");

        cmbDataType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBDataType = new GridData(GridData.FILL_HORIZONTAL);
        cmbDataType.setLayoutData(gdCMBDataType);
        cmbDataType.addSelectionListener(this);

        btnUndefined = new Button(this, SWT.RADIO);
        GridData gdBTNUndefined = new GridData(GridData.FILL_HORIZONTAL);
        gdBTNUndefined.horizontalSpan = 2;
        btnUndefined.setLayoutData(gdBTNUndefined);
        btnUndefined.setText("Undefined");
        btnUndefined.addSelectionListener(this);

        Label lblDummyStandard = new Label(this, SWT.NONE);
        GridData gdLBLDummyStandard = new GridData();
        gdLBLDummyStandard.horizontalSpan = 2;
        gdLBLDummyStandard.heightHint = 10;
        lblDummyStandard.setLayoutData(gdLBLDummyStandard);

        btnStandard = new Button(this, SWT.RADIO);
        GridData gdBTNStandard = new GridData(GridData.FILL_HORIZONTAL);
        gdBTNStandard.horizontalSpan = 2;
        btnStandard.setLayoutData(gdBTNStandard);
        btnStandard.setText("Standard");
        btnStandard.addSelectionListener(this);

        cmpStandardDetails = new Composite(this, SWT.NONE);
        GridData gdCMPStandardDetails = new GridData(GridData.FILL_BOTH);
        gdCMPStandardDetails.horizontalIndent = 16;
        gdCMPStandardDetails.horizontalSpan = 2;
        cmpStandardDetails.setLayoutData(gdCMPStandardDetails);
        cmpStandardDetails.setLayout(slStandardDetails);

        // Date/Time details Composite
        GridLayout glDate = new GridLayout();
        glDate.verticalSpacing = 5;
        glDate.marginHeight = 0;
        glDate.marginWidth = 0;

        cmpStandardDateDetails = new Composite(cmpStandardDetails, SWT.NONE);
        cmpStandardDateDetails.setLayout(glDate);

        // Date/Time Standard Composite
        // Layout
        GridLayout glDateStandard = new GridLayout();
        glDateStandard.verticalSpacing = 5;
        glDateStandard.numColumns = 2;
        glDateStandard.marginHeight = 2;
        glDateStandard.marginWidth = 2;

        cmpDateStandard = new Composite(cmpStandardDateDetails, SWT.NONE);
        GridData gdGRPDateStandard = new GridData(GridData.FILL_BOTH);
        cmpDateStandard.setLayoutData(gdGRPDateStandard);
        cmpDateStandard.setLayout(glDateStandard);

        Label lblDateType = new Label(cmpDateStandard, SWT.NONE);
        GridData gdLBLDateType = new GridData();
        lblDateType.setLayoutData(gdLBLDateType);
        lblDateType.setText("Type:");

        cmbDateType = new Combo(cmpDateStandard, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBDateType = new GridData(GridData.FILL_HORIZONTAL);
        cmbDateType.setLayoutData(gdCMBDateType);
        cmbDateType.addSelectionListener(this);

        Label lblDateDetails = new Label(cmpDateStandard, SWT.NONE);
        GridData gdLBLDateDetails = new GridData();
        lblDateDetails.setLayoutData(gdLBLDateDetails);
        lblDateDetails.setText("Details:");

        cmbDateForm = new Combo(cmpDateStandard, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBDateForm = new GridData(GridData.FILL_HORIZONTAL);
        cmbDateForm.setLayoutData(gdCMBDateForm);
        cmbDateForm.addSelectionListener(this);

        // Number details Composite
        GridLayout glNumber = new GridLayout();
        glNumber.verticalSpacing = 5;
        glNumber.marginHeight = 0;
        glNumber.marginWidth = 0;

        cmpStandardNumberDetails = new Composite(cmpStandardDetails, SWT.NONE);
        cmpStandardNumberDetails.setLayout(glNumber);

        // Number Standard Composite
        // Layout
        GridLayout glNumberStandard = new GridLayout();
        glNumberStandard.verticalSpacing = 5;
        glNumberStandard.numColumns = 4;
        glNumberStandard.marginHeight = 2;
        glNumberStandard.marginWidth = 2;

        cmpNumberStandard = new Composite(cmpStandardNumberDetails, SWT.NONE);
        GridData gdGRPNumberStandard = new GridData(GridData.FILL_BOTH);
        cmpNumberStandard.setLayoutData(gdGRPNumberStandard);
        cmpNumberStandard.setLayout(glNumberStandard);

        Label lblPrefix = new Label(cmpNumberStandard, SWT.NONE);
        GridData gdLBLPrefix = new GridData();
        lblPrefix.setLayoutData(gdLBLPrefix);
        lblPrefix.setText("Prefix:");

        txtPrefix = new Text(cmpNumberStandard, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTPrefix = new GridData(GridData.FILL_HORIZONTAL);
        txtPrefix.setLayoutData(gdTXTPrefix);
        txtPrefix.addModifyListener(this);

        Label lblSuffix = new Label(cmpNumberStandard, SWT.NONE);
        GridData gdLBLSuffix = new GridData();
        lblSuffix.setLayoutData(gdLBLSuffix);
        lblSuffix.setText("Suffix:");

        txtSuffix = new Text(cmpNumberStandard, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTSuffix = new GridData(GridData.FILL_HORIZONTAL);
        txtSuffix.setLayoutData(gdTXTSuffix);
        txtSuffix.addModifyListener(this);

        Label lblMultiplier = new Label(cmpNumberStandard, SWT.NONE);
        GridData gdLBLMultiplier = new GridData();
        lblMultiplier.setLayoutData(gdLBLMultiplier);
        lblMultiplier.setText("Multiplier:");

        txtMultiplier = new Text(cmpNumberStandard, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTMultiplier = new GridData(GridData.FILL_HORIZONTAL);
        txtMultiplier.setLayoutData(gdTXTMultiplier);
        txtMultiplier.addModifyListener(this);

        Label lblFractionDigit = new Label(cmpNumberStandard, SWT.NONE);
        GridData gdLBLFractionDigit = new GridData();
        lblFractionDigit.setLayoutData(gdLBLFractionDigit);
        lblFractionDigit.setText("Fraction Digits:");

        iscFractionDigits = new IntegerSpinControl(cmpNumberStandard, SWT.NONE, 2);
        GridData gdISCFractionDigits = new GridData(GridData.FILL_HORIZONTAL);
        iscFractionDigits.setLayoutData(gdISCFractionDigits);
        iscFractionDigits.addListener(this);

        Label lblDummyAdvanced = new Label(this, SWT.NONE);
        GridData gdLBLDummyAdvanced = new GridData();
        gdLBLDummyAdvanced.horizontalSpan = 2;
        gdLBLDummyAdvanced.heightHint = 10;
        lblDummyAdvanced.setLayoutData(gdLBLDummyAdvanced);

        btnAdvanced = new Button(this, SWT.RADIO);
        GridData gdBTNAdvanced = new GridData(GridData.FILL_HORIZONTAL);
        gdBTNAdvanced.horizontalSpan = 2;
        btnAdvanced.setLayoutData(gdBTNAdvanced);
        btnAdvanced.setText("Advanced");
        btnAdvanced.addSelectionListener(this);

        cmpAdvancedDetails = new Composite(this, SWT.NONE);
        GridData gdCMPAdvancedDetails = new GridData(GridData.FILL_BOTH);
        gdCMPAdvancedDetails.horizontalIndent = 16;
        gdCMPAdvancedDetails.horizontalSpan = 2;
        cmpAdvancedDetails.setLayoutData(gdCMPAdvancedDetails);
        cmpAdvancedDetails.setLayout(slAdvancedDetails);

        // Date/Time details Composite
        GridLayout glAdvDate = new GridLayout();
        glAdvDate.verticalSpacing = 5;
        glAdvDate.marginHeight = 0;
        glAdvDate.marginWidth = 0;

        cmpAdvancedDateDetails = new Composite(cmpAdvancedDetails, SWT.NONE);
        cmpAdvancedDateDetails.setLayout(glAdvDate);

        // Date/Time Advanced Composite
        // Layout
        GridLayout glDateAdvanced = new GridLayout();
        glDateAdvanced.verticalSpacing = 5;
        glDateAdvanced.numColumns = 2;
        glDateAdvanced.marginHeight = 2;
        glDateAdvanced.marginWidth = 2;

        cmpDateAdvanced = new Composite(cmpAdvancedDateDetails, SWT.NONE);
        GridData gdGRPDateAdvanced = new GridData(GridData.FILL_BOTH);
        cmpDateAdvanced.setLayoutData(gdGRPDateAdvanced);
        cmpDateAdvanced.setLayout(glDateAdvanced);

        Label lblDatePattern = new Label(cmpDateAdvanced, SWT.NONE);
        GridData gdLBLDatePattern = new GridData();
        lblDatePattern.setLayoutData(gdLBLDatePattern);
        lblDatePattern.setText("Pattern:");

        txtDatePattern = new Text(cmpDateAdvanced, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTDatePattern = new GridData(GridData.FILL_HORIZONTAL);
        txtDatePattern.setLayoutData(gdTXTDatePattern);
        txtDatePattern.addModifyListener(this);

        // Number details Composite
        GridLayout glAdvNumber = new GridLayout();
        glAdvNumber.verticalSpacing = 5;
        glAdvNumber.marginHeight = 0;
        glAdvNumber.marginWidth = 0;

        cmpAdvancedNumberDetails = new Composite(cmpAdvancedDetails, SWT.NONE);
        cmpAdvancedNumberDetails.setLayout(glAdvNumber);

        // Number Advanced Composite
        // Layout
        GridLayout glNumberAdvanced = new GridLayout();
        glNumberAdvanced.verticalSpacing = 5;
        glNumberAdvanced.numColumns = 2;
        glNumberAdvanced.marginHeight = 2;
        glNumberAdvanced.marginWidth = 2;

        cmpNumberAdvanced = new Composite(cmpAdvancedNumberDetails, SWT.NONE);
        GridData gdGRPNumberAdvanced = new GridData(GridData.FILL_BOTH);
        cmpNumberAdvanced.setLayoutData(gdGRPNumberAdvanced);
        cmpNumberAdvanced.setLayout(glNumberAdvanced);

        Label lblAdvMultiplier = new Label(cmpNumberAdvanced, SWT.NONE);
        GridData gdLBLAdvMultiplier = new GridData();
        lblAdvMultiplier.setLayoutData(gdLBLAdvMultiplier);
        lblAdvMultiplier.setText("Multiplier:");

        txtAdvMultiplier = new Text(cmpNumberAdvanced, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTAdvMultiplier = new GridData(GridData.FILL_HORIZONTAL);
        txtAdvMultiplier.setLayoutData(gdTXTAdvMultiplier);
        txtAdvMultiplier.addModifyListener(this);

        Label lblNumberPattern = new Label(cmpNumberAdvanced, SWT.NONE);
        GridData gdLBLNumberPattern = new GridData();
        lblNumberPattern.setLayoutData(gdLBLNumberPattern);
        lblNumberPattern.setText("Pattern:");

        txtNumberPattern = new Text(cmpNumberAdvanced, SWT.BORDER | SWT.SINGLE);
        GridData gdTXTNumberPattern = new GridData(GridData.FILL_HORIZONTAL);
        txtNumberPattern.setLayoutData(gdTXTNumberPattern);
        txtNumberPattern.addModifyListener(this);

        populateLists();
    }

    private void populateLists()
    {
        this.bEnableEvents = false;
        cmbDataType.add("Date/Time");
        cmbDataType.add("Number");

        if (formatspecifier instanceof DateFormatSpecifier || formatspecifier instanceof JavaDateFormatSpecifier)
        {
            cmbDataType.select(0);
            if (formatspecifier instanceof DateFormatSpecifier)
            {
                btnStandard.setSelection(true);
            }
            else if (formatspecifier instanceof JavaDateFormatSpecifier)
            {
                btnAdvanced.setSelection(true);
            }
            else
            {
                btnUndefined.setSelection(true);
            }
            slStandardDetails.topControl = this.cmpStandardDateDetails;
            slAdvancedDetails.topControl = this.cmpAdvancedDateDetails;
        }
        else
        {
            cmbDataType.select(1);
            if (formatspecifier instanceof NumberFormatSpecifier)
            {
                btnStandard.setSelection(true);
            }
            else if (formatspecifier instanceof JavaNumberFormatSpecifier)
            {
                btnAdvanced.setSelection(true);
            }
            else
            {
                btnUndefined.setSelection(true);
            }
            slStandardDetails.topControl = this.cmpStandardNumberDetails;
            slAdvancedDetails.topControl = this.cmpAdvancedNumberDetails;
        }
        updateUIState();

        // Populate Date Types
        Object[] oArrDT = DateFormatType.VALUES.toArray();
        for (int iDT = 0; iDT < oArrDT.length; iDT++)
        {
            cmbDateType.add(((DateFormatType) oArrDT[iDT]).getName());
            if (formatspecifier instanceof DateFormatSpecifier
                && ((DateFormatSpecifier) formatspecifier).getType().equals((DateFormatType) oArrDT[iDT]))
            {
                cmbDateType.select(iDT);
            }
        }

        // Populate Date Details
        Object[] oArrDD = DateFormatDetail.VALUES.toArray();
        for (int iDD = 0; iDD < oArrDD.length; iDD++)
        {
            cmbDateForm.add(((DateFormatDetail) oArrDD[iDD]).getName());
            if (formatspecifier instanceof DateFormatSpecifier
                && ((DateFormatSpecifier) formatspecifier).getDetail().equals((DateFormatDetail) oArrDD[iDD]))
            {
                cmbDateForm.select(iDD);
            }
        }
        String str = "";
        if (formatspecifier instanceof JavaDateFormatSpecifier)
        {
            str = ((JavaDateFormatSpecifier) formatspecifier).getPattern();
            if (str == null)
            {
                str = "";
            }
            txtDatePattern.setText(str);
        }
        if (formatspecifier instanceof NumberFormatSpecifier)
        {
            str = ((NumberFormatSpecifier) formatspecifier).getPrefix();
            if (str == null)
            {
                str = "";
            }
            txtPrefix.setText(str);
            str = ((NumberFormatSpecifier) formatspecifier).getSuffix();
            if (str == null)
            {
                str = "";
            }
            txtSuffix.setText(str);
            str = String.valueOf(((NumberFormatSpecifier) formatspecifier).getMultiplier());
            if (str == null
                || !((NumberFormatSpecifier) formatspecifier).eIsSet(AttributePackage.eINSTANCE
                    .getNumberFormatSpecifier_Multiplier()))
            {
                str = "";
            }
            txtMultiplier.setText(str);
            iscFractionDigits.setValue(((NumberFormatSpecifier) formatspecifier).getFractionDigits());
        }
        if (formatspecifier instanceof JavaNumberFormatSpecifier)
        {
            str = String.valueOf(((JavaNumberFormatSpecifier) formatspecifier).getMultiplier());
            if (str == null)
            {
                str = "";
            }
            txtAdvMultiplier.setText(str);
            str = ((JavaNumberFormatSpecifier) formatspecifier).getPattern();
            if (str == null)
            {
                str = "";
            }
            txtNumberPattern.setText(str);
        }
        this.layout();
        this.bEnableEvents = true;
    }

    public FormatSpecifier getFormatSpecifier()
    {
        if (this.btnUndefined.getSelection())
        {
            return null;
        }
        // Build (or set) the format specifier instance
        formatspecifier = buildFormatSpecifier();
        return this.formatspecifier;
    }

    private FormatSpecifier buildFormatSpecifier()
    {
        FormatSpecifier fs = null;
        if (cmbDataType.getText().equals("Date/Time"))
        {
            if (this.btnAdvanced.getSelection())
            {
                fs = JavaDateFormatSpecifierImpl.create(txtDatePattern.getText());
            }
            else if (this.btnStandard.getSelection())
            {
                fs = AttributeFactory.eINSTANCE.createDateFormatSpecifier();
                ((DateFormatSpecifier) fs).setType(DateFormatType.get(cmbDateType.getText()));
                ((DateFormatSpecifier) fs).setDetail(DateFormatDetail.get(cmbDateForm.getText()));
            }
        }
        else
        {
            if (this.btnAdvanced.getSelection())
            {
                fs = JavaNumberFormatSpecifierImpl.create(txtNumberPattern.getText());
                if (txtAdvMultiplier.getText().length() > 0)
                {
                    ((JavaNumberFormatSpecifierImpl) fs).setMultiplier(Double.valueOf(txtAdvMultiplier.getText())
                        .doubleValue());
                }
            }
            else if (this.btnStandard.getSelection())
            {
                fs = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) fs).setPrefix(txtPrefix.getText());
                ((NumberFormatSpecifier) fs).setSuffix(txtSuffix.getText());
                ((NumberFormatSpecifier) fs).setFractionDigits(iscFractionDigits.getValue());
                if (txtMultiplier.getText().length() > 0)
                {
                    ((NumberFormatSpecifier) fs).setMultiplier(Double.valueOf(txtMultiplier.getText()).doubleValue());
                }
            }
        }
        return fs;
    }

    private void updateUIState()
    {
        if (cmbDataType.getText().equals("Number"))
        {
            if (this.btnStandard.getSelection())
            {
                // Enable Standard properties for number
                this.txtPrefix.setEnabled(true);
                this.txtSuffix.setEnabled(true);
                this.txtMultiplier.setEnabled(true);
                this.iscFractionDigits.setEnabled(true);

                // Disable Advanced properties for number
                this.txtAdvMultiplier.setEnabled(false);
                this.txtNumberPattern.setEnabled(false);
            }
            else if (this.btnAdvanced.getSelection())
            {
                // Disable Standard properties for number
                this.txtPrefix.setEnabled(false);
                this.txtSuffix.setEnabled(false);
                this.txtMultiplier.setEnabled(false);
                this.iscFractionDigits.setEnabled(false);

                // Enable Standard properties for number
                this.txtAdvMultiplier.setEnabled(true);
                this.txtNumberPattern.setEnabled(true);
            }
            else
            {
                // Disable both Standard and Advanced properties
                this.txtPrefix.setEnabled(false);
                this.txtSuffix.setEnabled(false);
                this.txtMultiplier.setEnabled(false);
                this.iscFractionDigits.setEnabled(false);

                this.txtAdvMultiplier.setEnabled(false);
                this.txtNumberPattern.setEnabled(false);
            }
        }
        else
        {
            if (this.btnStandard.getSelection())
            {
                // Enable Standard properties for date
                this.cmbDateForm.setEnabled(true);
                this.cmbDateType.setEnabled(true);

                // Disable Standard properties for date
                this.txtDatePattern.setEnabled(false);
            }
            else if (this.btnAdvanced.getSelection())
            {
                // Disable Standard properties for date
                this.cmbDateForm.setEnabled(false);
                this.cmbDateType.setEnabled(false);

                // Enable Standard properties for date
                this.txtDatePattern.setEnabled(true);
            }
            else
            {
                // Disable both Standard and Advanced properties
                this.cmbDateForm.setEnabled(false);
                this.cmbDateType.setEnabled(false);

                this.txtDatePattern.setEnabled(false);
            }
        }
    }

    /**
     * @return A preferred size for this composite when used in a layout
     */
    public Point getPreferredSize()
    {
        return new Point(200, 150);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (!bEnableEvents)
        {
            return;
        }
        if (e.getSource().equals(cmbDataType))
        {
            if (cmbDataType.getText().equals("Number"))
            {
                slStandardDetails.topControl = cmpStandardNumberDetails;
                slAdvancedDetails.topControl = cmpAdvancedNumberDetails;
                updateUIState();
            }
            else
            {
                slStandardDetails.topControl = cmpStandardDateDetails;
                slAdvancedDetails.topControl = cmpAdvancedDateDetails;
                updateUIState();
            }
            cmpStandardDetails.layout();
            cmpAdvancedDetails.layout();
        }
        else if (e.getSource() instanceof Button)
        {
            updateUIState();
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
     * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
     */
    public void modifyText(ModifyEvent e)
    {
        Object oSource = e.getSource();
        this.bEnableEvents = false;
        if (oSource.equals(txtDatePattern))
        {
            if (!(formatspecifier instanceof JavaDateFormatSpecifier))
            {
                formatspecifier = JavaDateFormatSpecifierImpl.create("");
            }
            ((JavaDateFormatSpecifier) formatspecifier).setPattern(txtDatePattern.getText());
        }
        else if (oSource.equals(txtPrefix))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
                ((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getValue());
                try
                {
                    String str = txtMultiplier.getText();
                    if (str.length() > 0)
                    {
                        ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(str).doubleValue());
                    }
                }
                catch (NumberFormatException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
        }
        else if (oSource.equals(txtSuffix))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
                ((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getValue());
                try
                {
                    String str = txtMultiplier.getText();
                    if (str.length() > 0)
                    {
                        ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(str).doubleValue());
                    }
                }
                catch (NumberFormatException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
        }
        else if (oSource.equals(txtMultiplier))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
                ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
                ((NumberFormatSpecifier) formatspecifier).setFractionDigits(iscFractionDigits.getValue());
            }
            try
            {
                if ("".equals(txtMultiplier.getText()))
                {
                    ((NumberFormatSpecifier) formatspecifier).eUnset(AttributePackage.eINSTANCE
                        .getNumberFormatSpecifier_Multiplier());
                }
                else
                {
                    ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(txtMultiplier.getText())
                        .doubleValue());
                }
            }
            catch (NumberFormatException e1 )
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        else if (oSource.equals(txtAdvMultiplier))
        {
            if (!(formatspecifier instanceof JavaNumberFormatSpecifier))
            {
                formatspecifier = JavaNumberFormatSpecifierImpl.create(txtNumberPattern.getText());
            }
            try
            {
                if ("".equals(txtAdvMultiplier.getText()))
                {
                    ((JavaNumberFormatSpecifier) formatspecifier).eUnset(AttributePackage.eINSTANCE
                        .getJavaNumberFormatSpecifier_Multiplier());
                }
                else
                {
                    ((JavaNumberFormatSpecifier) formatspecifier).setMultiplier(new Double(txtAdvMultiplier.getText())
                        .doubleValue());
                }
            }
            catch (NumberFormatException e1 )
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        else if (oSource.equals(txtNumberPattern))
        {
            if (!(formatspecifier instanceof JavaNumberFormatSpecifier))
            {
                formatspecifier = JavaNumberFormatSpecifierImpl.create("");
            }
            ((JavaNumberFormatSpecifier) formatspecifier).setPattern(txtNumberPattern.getText());
        }
        this.bEnableEvents = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        this.bEnableEvents = false;
        if (event.widget.equals(iscFractionDigits))
        {
            if (!(formatspecifier instanceof NumberFormatSpecifier))
            {
                formatspecifier = NumberFormatSpecifierImpl.create();
                ((NumberFormatSpecifier) formatspecifier).setPrefix(txtPrefix.getText());
                ((NumberFormatSpecifier) formatspecifier).setSuffix(txtSuffix.getText());
                try
                {
                    String str = txtMultiplier.getText();
                    if (str.length() > 0)
                    {
                        ((NumberFormatSpecifier) formatspecifier).setMultiplier(new Double(str).doubleValue());
                    }
                }
                catch (NumberFormatException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            ((NumberFormatSpecifier) formatspecifier).setFractionDigits(((Integer) event.data).intValue());
        }
        this.bEnableEvents = true;
    }

}