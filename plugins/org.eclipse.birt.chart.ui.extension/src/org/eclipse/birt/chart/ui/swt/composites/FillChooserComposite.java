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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 *  
 */
public class FillChooserComposite extends Composite implements SelectionListener, MouseListener, DisposeListener,
    KeyListener
{

    private transient Composite cmpContentInner = null;

    private transient Composite cmpContentOuter = null;

    private transient Composite cmpDropDown = null;

    private transient FillCanvas cnvSelection = null;

    private transient Button btnDown = null;

    private transient Button btnTransparent = null;

    private transient Button btnCustom = null;

    private transient Button btnGradient = null;

    private transient Button btnImage = null;

    private static Color[] colorArray = null;

    private final String[] saImageTypes = new String[]
    {
        "*.gif", "*.jpg", "*.png"
    };

    private transient boolean bGradientEnabled = true;

    private transient boolean bImageEnabled = true;

    private transient Fill fCurrent = null;

    private transient Vector vListeners = null;

    public static final int FILL_CHANGED_EVENT = 1;

    public static final int MOUSE_CLICKED_EVENT = 2;

    private transient int iSize = 20;

    /**
     * @param parent
     * @param style
     */
    public FillChooserComposite(Composite parent, int style, Fill fCurrent, boolean bEnableGradient,
        boolean bEnableImage)
    {
        super(parent, style);
        this.fCurrent = fCurrent;
        this.bGradientEnabled = bEnableGradient;
        this.bImageEnabled = bEnableImage;
        init();
        placeComponents();
    }

    /**
     *  
     */
    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        Display display = Display.getDefault();
        colorArray = new Color[]
        {
            new Color(display, 0, 0, 0), new Color(display, 154, 50, 0), new Color(display, 51, 51, 0),
            new Color(display, 0, 50, 0), new Color(display, 0, 50, 100), new Color(display, 0, 0, 128),
            new Color(display, 51, 51, 153), new Color(display, 51, 51, 51),

            new Color(display, 128, 0, 0), new Color(display, 254, 102, 0), new Color(display, 124, 124, 0),
            new Color(display, 0, 128, 0), new Color(display, 0, 128, 128), new Color(display, 0, 0, 254),
            new Color(display, 102, 102, 153), new Color(display, 128, 128, 128),

            new Color(display, 254, 0, 0), new Color(display, 254, 153, 0), new Color(display, 154, 204, 0),
            new Color(display, 51, 153, 102), new Color(display, 51, 204, 204), new Color(display, 51, 102, 254),
            new Color(display, 128, 0, 128), new Color(display, 145, 145, 145),

            new Color(display, 254, 0, 254), new Color(display, 254, 204, 0), new Color(display, 254, 254, 0),
            new Color(display, 0, 254, 0), new Color(display, 0, 254, 254), new Color(display, 0, 204, 254),
            new Color(display, 154, 50, 102), new Color(display, 192, 192, 192),

            new Color(display, 253, 153, 204), new Color(display, 254, 204, 153), new Color(display, 254, 254, 153),
            new Color(display, 204, 254, 204), new Color(display, 204, 254, 254), new Color(display, 153, 204, 254),
            new Color(display, 204, 153, 254), new Color(display, 254, 254, 254)
        };
        vListeners = new Vector();
    }

    /**
     *  
     */
    private void placeComponents()
    {
        // THE LAYOUT OF THIS COMPOSITE (FILLS EVERYTHING INSIDE IT)
        FillLayout flMain = new FillLayout();
        flMain.marginHeight = 0;
        flMain.marginWidth = 0;
        setLayout(flMain);

        // THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
        // ITS CONTENT NORTH)
        cmpContentOuter = new Composite(this, SWT.NONE);
        GridLayout glContentOuter = new GridLayout();
        glContentOuter.verticalSpacing = 0;
        glContentOuter.horizontalSpacing = 0;
        glContentOuter.marginHeight = 0;
        glContentOuter.marginWidth = 0;
        glContentOuter.numColumns = 1;
        cmpContentOuter.setLayout(glContentOuter);
        GridData gdContentOuter = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentOuter.setLayoutData(gdContentOuter);

        // THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
        // THE CANVAS + BUTTON)
        cmpContentInner = new Composite(cmpContentOuter, SWT.BORDER);
        GridLayout glContentInner = new GridLayout();
        glContentInner.verticalSpacing = 0;
        glContentInner.horizontalSpacing = 0;
        glContentInner.marginHeight = 0;
        glContentInner.marginWidth = 0;
        glContentInner.numColumns = 2;
        cmpContentInner.setLayout(glContentInner);
        GridData gdContentInner = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentInner.setLayoutData(gdContentInner);

        // THE CANVAS
        cnvSelection = new FillCanvas(cmpContentInner, SWT.NONE);
        GridData gdCNVSelection = new GridData(GridData.FILL_BOTH);
        gdCNVSelection.heightHint = iSize;
        cnvSelection.setLayoutData(gdCNVSelection);
        cnvSelection.setFill(fCurrent);
        cnvSelection.addMouseListener(this);

        // THE BUTTON
        btnDown = new Button(cmpContentInner, SWT.ARROW | SWT.DOWN);
        GridData gdBDown = new GridData(GridData.FILL);
        gdBDown.verticalAlignment = GridData.BEGINNING;
        gdBDown.widthHint = iSize;
        gdBDown.heightHint = iSize;
        btnDown.setLayoutData(gdBDown);
        btnDown.addSelectionListener(this);

        addDisposeListener(this);
    }

    /**
     *  
     */
    private void createDropDownComponent(int iXLoc, int iYLoc)
    {
        int iShellHeight = 220;
        int iShellWidth = 160;
        // Reduce the height based on which buttons are to be shown.
        if (!bGradientEnabled)
        {
            iShellHeight = iShellHeight - 30;
        }
        if (!bImageEnabled)
        {
            iShellHeight = iShellHeight - 30;
        }
        Shell shell = new Shell(this.getDisplay(), SWT.APPLICATION_MODAL);
        shell.setLayout(new FillLayout());
        shell.setSize(iShellWidth, iShellHeight);
        shell.setLocation(iXLoc, iYLoc);

        cmpDropDown = new Composite(shell, SWT.NONE);
        GridLayout glDropDown = new GridLayout();
        glDropDown.marginHeight = 2;
        glDropDown.marginWidth = 2;
        glDropDown.horizontalSpacing = 1;
        glDropDown.verticalSpacing = 1;
        glDropDown.numColumns = 8;
        cmpDropDown.setLayout(glDropDown);
        cmpDropDown.addKeyListener(this);

        ColorSelectionCanvas cnv = new ColorSelectionCanvas(cmpDropDown, SWT.BORDER, colorArray);
        GridData gdCnv = new GridData(GridData.FILL_BOTH);
        gdCnv.horizontalSpan = 8;
        gdCnv.heightHint = 100;
        cnv.setLayoutData(gdCnv);
        cnv.addMouseListener(this);
        if (this.fCurrent instanceof ColorDefinition)
        {
            cnv.setColor(new Color(this.getDisplay(), ((ColorDefinition) fCurrent).getRed(),
                ((ColorDefinition) fCurrent).getGreen(), ((ColorDefinition) fCurrent).getBlue()));
        }

        btnTransparent = new Button(cmpDropDown, SWT.TOGGLE);
        GridData gdTransparent = new GridData(GridData.FILL_BOTH);
        gdTransparent.heightHint = 26;
        gdTransparent.horizontalSpan = 8;
        btnTransparent.setLayoutData(gdTransparent);
        btnTransparent.setText("Transparent");
        btnTransparent.addSelectionListener(this);
        if (fCurrent instanceof ColorDefinition)
        {
            if (((ColorDefinition) fCurrent).getTransparency() == 100)
            {
                btnTransparent.setSelection(true);
            }
            else
            {
                btnTransparent.setSelection(false);
            }
        }
        else if (fCurrent instanceof Gradient)
        {
            if (((Gradient) fCurrent).getTransparency() == 100)
            {
                btnTransparent.setSelection(true);
            }
            else
            {
                btnTransparent.setSelection(false);
            }
        }

        if (this.bGradientEnabled)
        {
            btnGradient = new Button(cmpDropDown, SWT.NONE);
            GridData gdGradient = new GridData(GridData.FILL_BOTH);
            gdTransparent.heightHint = 26;
            gdGradient.horizontalSpan = 8;
            btnGradient.setLayoutData(gdGradient);
            btnGradient.setText("Gradient...");
            btnGradient.addSelectionListener(this);
        }

        btnCustom = new Button(cmpDropDown, SWT.NONE);
        GridData gdCustom = new GridData(GridData.FILL_BOTH);
        gdTransparent.heightHint = 26;
        gdCustom.horizontalSpan = 8;
        btnCustom.setLayoutData(gdCustom);
        btnCustom.setText("Custom Color...");
        btnCustom.addSelectionListener(this);

        if (this.bImageEnabled)
        {
            btnImage = new Button(cmpDropDown, SWT.NONE);
            GridData gdImage = new GridData(GridData.FILL_BOTH);
            gdTransparent.heightHint = 26;
            gdImage.horizontalSpan = 8;
            btnImage.setLayoutData(gdImage);
            btnImage.setText("Image...");
            btnImage.addSelectionListener(this);
        }

        shell.open();
    }

    public void setFill(Fill fill)
    {
        //        this.fCurrent = fill;
        fCurrent = fill;
        cnvSelection.setFill(fill);
        cnvSelection.redraw();
    }

    public Fill getFill()
    {
        return this.fCurrent;
    }

    public Point getPreferredSize()
    {
        return new Point(160, 24);
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void toggleDropDown()
    {
        if (cmpDropDown == null || cmpDropDown.isDisposed() || !cmpDropDown.isVisible())
        {
            Point pLoc = UIHelper.getScreenLocation(cnvSelection);
            createDropDownComponent(pLoc.x, pLoc.y + cnvSelection.getSize().y + 1);
        }
        else
        {
            cmpDropDown.getParent().dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Object oSource = e.getSource();
        if (oSource.equals(btnDown))
        {
            fireHandleEvent(MOUSE_CLICKED_EVENT);
            toggleDropDown();
        }
        else if (oSource.equals(this.btnImage))
        {
            FileDialog fDlg = new FileDialog(this.getShell(), SWT.OPEN);
            fDlg.setFilterExtensions(saImageTypes);
            String sStartFolder = System.getProperty("user.dir");
            String sImageFile = "";
            if (fCurrent instanceof Image)
            {
                String sFullPath = ((Image) fCurrent).getURL().toString();
                sImageFile = sFullPath.substring(sFullPath.lastIndexOf("/") + 1);
                sStartFolder = sFullPath.substring(0, sFullPath.lastIndexOf("/"));
            }
            fDlg.setFilterPath(sStartFolder);
            fDlg.setFileName(sImageFile);
            fDlg.open();

            String sImgPath = fDlg.getFileName();

            try
            {
                new URL(sImgPath);
            }
            catch (MalformedURLException e1 )
            {
                sImgPath = "file:///" + fDlg.getFilterPath() + File.separator + fDlg.getFileName();
            }
            if (sImgPath != null && sImgPath.trim().length() > 0)
            {
                Image imgFill = AttributeFactory.eINSTANCE.createImage();
                imgFill.setURL(sImgPath);
                this.setFill(imgFill);
                fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
            }
            cmpDropDown.getParent().dispose();
        }
        else if (oSource.equals(this.btnCustom))
        {
            ColorDialog cDlg = new ColorDialog(this.getShell(), SWT.NONE);
            if (fCurrent instanceof ColorDefinition)
            {
                cDlg.setRGB(new RGB(((ColorDefinition) this.fCurrent).getRed(), ((ColorDefinition) this.fCurrent)
                    .getGreen(), ((ColorDefinition) this.fCurrent).getBlue()));
            }
            cDlg.open();
            RGB rgb = cDlg.getRGB();
            if (rgb != null)
            {
                ColorDefinition cdNew = AttributeFactory.eINSTANCE.createColorDefinition();
                cdNew.set(rgb.red, rgb.green, rgb.blue);
                this.setFill(cdNew);
                fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
            }
            cmpDropDown.getParent().dispose();
        }
        else if (oSource.equals(btnTransparent))
        {
            if (fCurrent instanceof ColorDefinition)
            {
                if (btnTransparent.getSelection())
                {
                    ((ColorDefinition) fCurrent).setTransparency(100);
                }
                else
                {
                    ((ColorDefinition) fCurrent).setTransparency(0);
                }
            }
            else if (fCurrent instanceof Gradient)
            {
                if (btnTransparent.getSelection())
                {
                    ((Gradient) fCurrent).setTransparency(100);
                }
                else
                {
                    ((Gradient) fCurrent).setTransparency(0);
                }
            }
            this.setFill(fCurrent);
            fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
            cmpDropDown.getParent().dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }

    private void fireHandleEvent(int iType)
    {
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            Event se = new Event();
            se.widget = this;
            se.data = fCurrent;
            se.type = iType;
            ((Listener) vListeners.get(iL)).handleEvent(se);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDoubleClick(MouseEvent e)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDown(MouseEvent e)
    {
        fireHandleEvent(MOUSE_CLICKED_EVENT);
        if (e.getSource().equals(cnvSelection))
        {
            toggleDropDown();
        }
        else if (e.getSource() instanceof ColorSelectionCanvas)
        {
            ColorDefinition cTmp = AttributeFactory.eINSTANCE.createColorDefinition();
            Color clrTmp = ((ColorSelectionCanvas) e.getSource()).getColorAt(e.x, e.y);
            cTmp.set(clrTmp.getRed(), clrTmp.getGreen(), clrTmp.getBlue());
            setFill(cTmp);
            fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
            cmpDropDown.getShell().dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseUp(MouseEvent e)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent e)
    {
        if (colorArray != null)
        {
            for (int iC = 0; iC < colorArray.length; iC++)
            {
                colorArray[iC].dispose();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
        if (cmpDropDown != null && !cmpDropDown.getShell().isDisposed())
        {
            if (e.keyCode == SWT.ESC)
            {
                cmpDropDown.getShell().dispose();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }
}

class ColorSelectionCanvas extends Canvas implements PaintListener
{

    Color[] colorMap = null;

    Color colorSelection = null;

    public ColorSelectionCanvas(Composite parent, int iStyle, Color[] colorMap)
    {
        super(parent, iStyle);
        this.addPaintListener(this);
        this.colorMap = colorMap;
    }

    public Color getColor()
    {
        return colorSelection;
    }

    public void setColor(Color color)
    {
        this.colorSelection = color;
    }

    public void paintControl(PaintEvent pe)
    {
        Color cBlack = new Color(this.getDisplay(), 0, 0, 0);
        Color cWhite = new Color(this.getDisplay(), 255, 255, 255);
        GC gc = pe.gc;
        gc.setForeground(cBlack);

        int iCellWidth = this.getSize().x / 8;
        int iCellHeight = this.getSize().y / 5;

        for (int iR = 0; iR < 5; iR++)
        {
            for (int iC = 0; iC < 8; iC++)
            {
                gc.setBackground(colorMap[(iR * 8) + iC]);
                gc.fillRectangle(iC * iCellWidth, iR * iCellHeight, iCellWidth, iCellHeight);
                // Hilight currently selected color if it exists in this list
                if (colorSelection != null && colorSelection.equals(colorMap[(iR * 8) + iC]))
                {
                    gc.drawRectangle(iC * iCellWidth, iR * iCellHeight, iCellWidth - 2, iCellHeight - 2);
                    gc.setForeground(cWhite);
                    gc.drawRectangle(iC * iCellWidth + 1, iR * iCellHeight + 1, iCellWidth - 3, iCellHeight - 3);
                    gc.setForeground(cBlack);
                }
            }
        }
        cBlack.dispose();
        cWhite.dispose();
        gc.dispose();
    }

    /**
     * This method assumes a color array of 40 colors arranged with equal sizes in a 8x5 grid.
     * 
     * @param x
     * @param y
     * @return
     */
    public Color getColorAt(int x, int y)
    {
        int iCellWidth = this.getSize().x / 8;
        int iCellHeight = this.getSize().y / 5;
        int iHCell = x / iCellWidth;
        int iVCell = y / iCellHeight;
        int iArrayIndex = ((iVCell) * 8) + iHCell;
        return this.colorMap[iArrayIndex];
    }
}

class FillCanvas extends Canvas implements PaintListener
{

    Fill fCurrent = null;

    public FillCanvas(Composite parent, int iStyle)
    {
        super(parent, iStyle);
        this.addPaintListener(this);
    }

    public void setFill(Fill fill)
    {
        this.fCurrent = fill;
    }

    public void paintControl(PaintEvent pe)
    {
        Color cBackground = null;
        GC gc = pe.gc;
        if (fCurrent != null)
        {
            if (fCurrent instanceof ColorDefinition)
            {
                if (((ColorDefinition) fCurrent).getTransparency() == 100)
                {
                    gc.fillRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4);
                    Color cBlack = new Color(this.getDisplay(), 0, 0, 0);
                    gc.setForeground(cBlack);
                    gc.drawText("Transparent", 2, 2);
                    cBlack.dispose();
                }
                else
                {
                    cBackground = new Color(Display.getDefault(), ((ColorDefinition) fCurrent).getRed(),
                        ((ColorDefinition) fCurrent).getGreen(), ((ColorDefinition) fCurrent).getBlue());
                    gc.setBackground(cBackground);
                    gc.fillRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4);
                }
            }
            else if (fCurrent instanceof Image)
            {
                gc.fillRectangle(2, 2, getSize().x - 4, this.getSize().y - 4);
                gc.drawImage(getSWTImage((Image) fCurrent), 2, 2);
            }
        }
        if (cBackground != null)
        {
            cBackground.dispose();
        }
    }

    private org.eclipse.swt.graphics.Image getSWTImage(Image modelImage)
    {
        org.eclipse.swt.graphics.Image img = null;
        try
        {
            try
            {
                img = new org.eclipse.swt.graphics.Image(Display.getCurrent(), new URL(modelImage.getURL())
                    .openStream());
            }
            catch (MalformedURLException e1 )
            {
                img = new org.eclipse.swt.graphics.Image(Display.getCurrent(), new FileInputStream(modelImage.getURL()));
            }
        }
        catch (FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }
}