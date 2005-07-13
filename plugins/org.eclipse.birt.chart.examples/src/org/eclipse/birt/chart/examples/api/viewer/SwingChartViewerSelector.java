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
package org.eclipse.birt.chart.examples.api.viewer;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.exception.BirtException;

/**
 * The selector of charts in Swing JPanel.
 * 
 */
public final class SwingChartViewerSelector extends JPanel implements IUpdateNotifier, ComponentListener
{

    private boolean bNeedsGeneration = true;

    private GeneratedChartState gcs = null;

    private Chart cm = null;
    
    private IDeviceRenderer idr = null;
    
    /**
     * Contructs the layout with a container for displaying chart and a control panel for selecting
     * chart attributes.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        SwingChartViewerSelector scv = new SwingChartViewerSelector();
        
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.addComponentListener(scv);
        
        Container co = jf.getContentPane();
        co.setLayout(new BorderLayout());
        co.add(scv, BorderLayout.CENTER);
        
        // The Viewer Selector will appear in the center of the window
        Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dApp = new Dimension(800, 700);
        jf.setSize(dApp);
        jf.setLocation((dScreen.width - dApp.width)/2, (dScreen.height - dApp.height) / 2);
        
        //Get the name of relevant chart from createXXXChart()
        jf.setTitle(scv.getClass().getName() + " [device="+scv.idr.getClass().getName()+"]");

        ControlPanel cp = scv.new ControlPanel(scv);
        co.add(cp, BorderLayout.SOUTH);
        
        jf.show();
    }
    
    /**
     * Get the connection with SWING device to render the graphics.
     */
    SwingChartViewerSelector()
    {
        final PluginSettings ps = PluginSettings.instance();
        try {
            idr = ps.getDevice("dv.SWING");
        } catch (ChartException ex)
        {
            ex.printStackTrace();
        }
        //The default chart displayed in the container is the simple bar chart.
        cm = PrimitiveCharts.createBarChart();
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
     */
    public void regenerateChart()
    {
        bNeedsGeneration = true;
        repaint();
    }

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#update()
     */
    public void repaintChart()
    {
        repaint();
    }


    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#peerInstance()
     */
    public Object peerInstance()
    {
        return this;
    }
    

    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getDesignTimeModel()
     */
    public Chart getDesignTimeModel()
    {
        return cm;
    }


    /* (non-Javadoc)
     * @see org.eclipse.birt.chart.device.swing.IUpdateNotifier#getRunTimeModel()
     */
    public Chart getRunTimeModel()
    {
        return gcs.getChartModel();
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        
        Graphics2D g2d = (Graphics2D) g;
        idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
        idr.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, this);
        
        Dimension d = getSize();
        Bounds bo = BoundsImpl.create(0, 0, d.width, d.height);
        //Bounds have to be specified in points
        bo.scale(72d/idr.getDisplayServer().getDpiResolution()); 
        
        Generator gr = Generator.instance();
        
        //When the update button has been pushed, build a chart offscreen.
        if (bNeedsGeneration)
        {
            bNeedsGeneration = false;
            try {
	            gcs = gr.build(
	                idr.getDisplayServer(), 
	                cm, null,
	                bo,
	                null
	            );
	        } catch (ChartException ex)
	        {
	            showException(g2d, ex);
	        }
        }
        
        //Draw the previous built chart on screen.
        try {
            gr.render(
                idr,
                gcs
            );
        } catch (ChartException ex)
        {
            showException(g2d, ex);
        }
    }
    
    /**
     * Presents the Exceptions if the chart cannot be displayed properly.
     * @param g2d
     * @param ex
     */
    private final void showException(Graphics2D g2d, Exception ex)
    {
        String sWrappedException = ex.getClass().getName();
        Throwable th = ex;
        while (ex.getCause() != null)
        {
            ex = (Exception) ex.getCause();
        }
        String sException = ex.getClass().getName();
        if (sWrappedException.equals(sException))
        {
            sWrappedException = null;
        }
        
        String sMessage = null;
        if (th instanceof BirtException)
        {
            sMessage = ((BirtException) th).getLocalizedMessage(); 
        }
        else
        {
            sMessage = ex.getMessage();
        }
        
        if (sMessage == null)
        {
            sMessage = "<null>";
        }
        
        StackTraceElement[] stea = ex.getStackTrace();
        Dimension d = getSize();
        
        Font fo = new Font("Monospaced", Font.BOLD, 14);
        g2d.setFont(fo);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(20, 20, d.width - 40, d.height - 40);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(20, 20, d.width - 40, d.height - 40);
        g2d.setClip(20, 20, d.width - 40, d.height - 40);
        int x = 25, y = 20 + fm.getHeight();
        g2d.drawString("Exception:", x, y);
        x += fm.stringWidth("Exception:") + 5;
        g2d.setColor(Color.RED);
        g2d.drawString(sException, x, y); x = 25; y += fm.getHeight();
        if (sWrappedException != null)
        {
	        g2d.setColor(Color.BLACK);
	        g2d.drawString("Wrapped In:", x, y);
	        x += fm.stringWidth("Wrapped In:") + 5;
	        g2d.setColor(Color.RED);
	        g2d.drawString(sWrappedException, x, y); x = 25; y += fm.getHeight();
        }
        g2d.setColor(Color.BLACK); y += 10;
        g2d.drawString("Message:", x, y);
        x += fm.stringWidth("Message:") + 5;
        g2d.setColor(Color.BLUE);
        g2d.drawString(sMessage, x, y); x = 25; y += fm.getHeight();
        g2d.setColor(Color.BLACK); y += 10;
        g2d.drawString("Trace:", x, y); x = 40; y += fm.getHeight();
        g2d.setColor(Color.GREEN.darker());
        for (int i = 0; i < stea.length; i++)
        {
            g2d.drawString(stea[i].getClassName() + ":" + stea[i].getMethodName() + "(...):" + stea[i].getLineNumber(), x, y); x = 40; y += fm.getHeight();
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(ComponentEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(ComponentEvent e)
    {
        bNeedsGeneration = true;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * An inner class: Control Panel, which provides the interactive interface with the user.
     */
    private final class ControlPanel extends JPanel implements ActionListener
    {
        private JComboBox jcbModels = null; 
        private JButton jbUpdate = null;
        private JComboBox jcbDimensions = null;
        private JCheckBox jcbTransposed = null;
        private JCheckBox jcbPercent = null;
        private JCheckBox jcbLogarithmic = null;
        
        private final SwingChartViewerSelector scv;
        
        ControlPanel(SwingChartViewerSelector scv)
        {
            this.scv = scv;
            
            setLayout(new GridLayout(0, 1, 0, 0));
            
            JPanel jp1 = new JPanel(); 
            jp1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

            jp1.add(new JLabel("Choose:"));
            jcbModels = new JComboBox();

            jcbModels.addItem("Bar Chart");
            jcbModels.addItem("Bar Chart(2 Series)");
            jcbModels.addItem("Pie Chart");
            jcbModels.addItem("Pie Chart(4 Series)");
            jcbModels.addItem("Line Chart");
            jcbModels.addItem("Bar/Line Stacked Chart");
            jcbModels.addItem("Scatter Chart");
            jcbModels.addItem("Stock Chart");
            
            
            jcbModels.setSelectedIndex(0);
            jp1.add(jcbModels);

            jcbDimensions = new JComboBox();
            jcbDimensions.addItem("2D");
            jcbDimensions.addItem("2D with Depth");
            jp1.add(jcbDimensions);
            
            jcbTransposed = new JCheckBox("Transposed", false);
            jp1.add(jcbTransposed);
            
            jcbPercent = new JCheckBox("Percent", false);
            jp1.add(jcbPercent);

            jcbLogarithmic = new JCheckBox("Logarithmic", false);
            jp1.add(jcbLogarithmic);
            
            jbUpdate = new JButton("Update");
            jbUpdate.addActionListener(this);
            jp1.add(jbUpdate);
            
            add(jp1);            
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
         */
        public void componentHidden(ComponentEvent cev)
        {
            setVisible(false);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
         */
        public void componentMoved(ComponentEvent cev)
        {
            JFrame jf = (JFrame) cev.getComponent();
            Rectangle r = jf.getBounds();
            setLocation(r.x, r.y + r.height);
            setSize(r.width, 50);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
         */
        public void componentResized(ComponentEvent cev)
        {
            JFrame jf = (JFrame) cev.getComponent();
            Rectangle r = jf.getBounds();
            setLocation(r.x, r.y + r.height);
            setSize(r.width, 50);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
         */
        public void componentShown(ComponentEvent cev)
        {
            JFrame jf = (JFrame) cev.getComponent();
            Rectangle r = jf.getBounds();
            setLocation(r.x, r.y + r.height);
            setSize(r.width, 50);
            setVisible(true);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            int i = jcbModels.getSelectedIndex();
            cm = null;
            switch(i)
            {
	        	case 0:
	        	    cm = PrimitiveCharts.createBarChart();
	        	    break;
	        	case 1:
	        	    cm = PrimitiveCharts.createMultiBarChart();
	        	    break;
	        	case 2:
	        	    cm = PrimitiveCharts.createPieChart();
	        	    break;
	        	case 3:
	        		cm = PrimitiveCharts.createMultiPieChart();
	            	break;
                case 4:
	        	    cm = PrimitiveCharts.createLineChart();
	        	    break;
            	case 5:
            	    cm = PrimitiveCharts.createStackedChart();
            	    break;
            	case 6:
            		cm = PrimitiveCharts.createScatterChart();
            	    break;
            	case 7:
            	    cm = PrimitiveCharts.createStockChart();
            	    break;    
            }
            
            if (cm instanceof ChartWithAxes)
            {
                ChartWithAxes cwa = ((ChartWithAxes) cm);
                cwa.setTransposed(jcbTransposed.isSelected());
                Axis ax = cwa.getPrimaryOrthogonalAxis(cwa.getPrimaryBaseAxes()[0]);
                
                if (jcbLogarithmic.isSelected())
                {
                    if (ax.getType() == AxisType.LINEAR_LITERAL)
                    {
                        ax.setType(AxisType.LOGARITHMIC_LITERAL);
                    }
                }
                else
                {
                    if (ax.getType() == AxisType.LOGARITHMIC_LITERAL)
                    {
                        ax.setType(AxisType.LINEAR_LITERAL);
                    }
                }
                
                ax.setPercent(jcbPercent.isSelected());
                if (ax.isPercent())
                {
                    ax.setFormatSpecifier(JavaNumberFormatSpecifierImpl.create("0'%'"));
                }
                else
                {
                    ax.setFormatSpecifier(null);
                }
            }
            
            i = jcbDimensions.getSelectedIndex();
            switch(i)
            {
            	case 0:
            	    cm.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
            	    break;
            	case 1:
            	    cm.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
            	    break;
            }

            bNeedsGeneration = true;
            scv.repaint();
        }
    }
}