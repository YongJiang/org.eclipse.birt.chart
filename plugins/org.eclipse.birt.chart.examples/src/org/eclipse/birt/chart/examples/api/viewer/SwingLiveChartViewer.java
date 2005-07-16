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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;

/**
 * Generates a combination of live chart (Line chart + bar chart) using a Swing JPanel.
 */

public final class SwingLiveChartViewer extends JPanel
{
    /**
     * A chart model instance
     */
    private Chart cm = null;
    
    /**
     * The swing rendering device
     */
    private IDeviceRenderer dRenderer = null;
    
    /**
     * Maintains the structure of the chart for quick refresh
     */
    private GeneratedChartState gcState = null;
    
    /**
     * Used in building the chart for the first time
     */
    private boolean bFirstPaint = true;
    
    /**
     * execute application 
     * @param args
     */
    public static void main(String[] args)
    {
        SwingLiveChartViewer lcViewer = new SwingLiveChartViewer();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(lcViewer, BorderLayout.CENTER);
        
        // Center window on the screen
        Dimension dScreen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dApp = new Dimension(600, 480);
        frame.setSize(dApp);
        frame.setLocation((dScreen.width - dApp.width)/2, (dScreen.height - dApp.height) / 2);
        
        frame.setTitle(lcViewer.getClass().getName() + " [device="+lcViewer.dRenderer.getClass().getName()+"]");
        frame.show();
    }
    
    /**
     * Constructor
     */
    SwingLiveChartViewer()
    {
        final PluginSettings ps = PluginSettings.instance();
        try {
        	dRenderer = ps.getDevice("dv.SWING");
        } catch (ChartException ex)
        {
            ex.printStackTrace();
        }
        cm = createLiveChart();
    }
    
    /**
     * Called to refresh the panel that renders the chart 
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        dRenderer.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);
        Dimension d = getSize();
        Bounds bo = BoundsImpl.create(0, 0, d.width, d.height);
        bo.scale(72d/dRenderer.getDisplayServer().getDpiResolution());
        
        final Generator gr = Generator.instance();
        if (bFirstPaint)
        {
            bFirstPaint = false;
            try {
            	gcState = gr.build(
		        	dRenderer.getDisplayServer(), 
		            cm, null,
		            bo,
		            null
		        );
            } catch (ChartException ex)
            {
                ex.printStackTrace();
            }
            
            Timer t = new Timer();
            t.schedule(new ChartRefresh(), 1000);
        }
        try {
            gr.render(
            		dRenderer, gcState 
            );
        } catch (ChartException ex)
        {
            ex.printStackTrace();
        }
    }
    

    //Live Date Set 
	private static final String[] sa = { "One", "Two", "Three", "Four", "Five",
			"Six", "Seven", "Eight", "Nine", "Ten" };
	private static final double[] da1 = { 56.99, 352.95, -201.95, 299.95,
			-95.95, 25.45, 129.33, -26.5, 43.5, 122 };

	private static final double[] da2 = { 20, 35, 59, 105, 150, -37, -65, -99,
			-145, -185 };
	
	/**
	 * Creates a chart instance that may be used to demo live/animated
	 * charts with scrolling data
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createLiveChart() {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		
		//Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(
				ColorDefinitionImpl.create(255, 255, 225));
		
		//Legend
		Legend lg = cwaBar.getLegend();
		LineAttributes lia = lg.getOutline();
		lg.getText().getFont().setSize(16);
		lia.setStyle(LineStyle.SOLID_LITERAL);
		lg.getInsets().setLeft(10);
		lg.getInsets().setRight(10);

		//Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Live Chart Demo");

		//X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		
		xAxisPrimary.setType(AxisType.TEXT_LITERAL);		
		xAxisPrimary.getOrigin().setType(IntersectionType.VALUE_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		
		xAxisPrimary.getTitle().getCaption().setValue("Category Text X-Axis");
		xAxisPrimary.setTitlePosition(Position.BELOW_LITERAL);
		
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(75);
		xAxisPrimary.setLabelPosition(Position.BELOW_LITERAL);
		
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.create(64, 64, 64));
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);		

		//Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		
		yAxisPrimary.getLabel().getCaption().setValue("Price Axis");
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(37);
		yAxisPrimary.setLabelPosition(Position.LEFT_LITERAL);
		
		yAxisPrimary.setTitlePosition(Position.LEFT_LITERAL);
		yAxisPrimary.getTitle().getCaption().setValue("Linear Value Y-Axis");
		
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.RED());
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);

		//Associate with Data Set
		TextDataSet categoryValues = TextDataSetImpl.create(sa);
		NumberDataSet seriesOneValues = NumberDataSetImpl.create(da1);
		NumberDataSet seriesTwoValues = NumberDataSetImpl.create(da2);

		//X-Series
		Series seCategory = SeriesImpl.create();
		seCategory.setDataSet(categoryValues);
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		//Y-Series (1)
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.setSeriesIdentifier("Unit Price");
		bs1.setDataSet(seriesOneValues);
		bs1.setRiserOutline(null);
		bs1.setRiser(RiserType.RECTANGLE_LITERAL);

		//Y-Series (2)
		LineSeries ls1 = (LineSeries) LineSeriesImpl.create();
		ls1.setSeriesIdentifier("Quantity");
		ls1.setDataSet(seriesTwoValues);
		ls1.getLineAttributes().setColor(ColorDefinitionImpl.GREEN());
		ls1.getMarker().setType(MarkerType.BOX_LITERAL);
		ls1.setCurve(true);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeriesPalette().update(1);
		sdY.getSeries().add(bs1);
		sdY.getSeries().add(ls1);

		return cwaBar;
	}

	/**
	 * A method for changing the data value
	 * @param cwa
	 * @param iOffset
	 */
	static final void scrollData(ChartWithAxes cwa) {
		//Scroll the bar (Y) series
		double dTemp = da1[0];
		for (int i = 0; i < da1.length - 1; i++) {
			da1[i] = da1[i + 1];
		}
		da1[da1.length - 1] = dTemp;

		//Scroll the line (Y) series
		dTemp = da2[0];
		for (int i = 0; i < da2.length - 1; i++) {
			da2[i] = da2[i + 1];
		}
		da2[da2.length - 1] = dTemp;

		//Scroll X series
		String sTemp = sa[0];
		for (int i = 0; i < sa.length - 1; i++) {
			sa[i] = sa[i + 1];
		}
		sa[sa.length - 1] = sTemp;
	}
    
    /**
     * A background thread that scrolls/refreshes the chart (offscreeen)
     */
    private final class ChartRefresh extends TimerTask
    {
        public final void run()
        {
            for (;;)
            {
	            final Generator gr = Generator.instance();
	            scrollData((ChartWithAxes) cm);
	            
	            // Refresh
	            try {	                
	                gr.refresh(gcState);
	            } catch (ChartException ex)
	            {
	                ex.printStackTrace();
	            }
	            repaint();
	            
	            // Delay
	            try {
	                Thread.sleep(500);
	            } catch (InterruptedException iex)
	            {
	                iex.printStackTrace();
	            }
            }
        }
    }
}
