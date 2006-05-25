/***********************************************************************
 * Copyright (c) 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.examples.api.pdf;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.pdf.PDFRendererImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;

import com.ibm.icu.util.ULocale;

/**
 * Example class that generates a PDF file based on a BIRT Chart Model.  
 *
 */
public class PDFChartGenerator {

	/**
	 * Generates a pdf chart to a file
	 */
	public static void generateChart(){
		//Tell chart engine that we are running in stand alone mode.  Note running in an eclipse environment.
		System.setProperty("STANDALONE", "true");
		
		//Create the chart we want to render
		Chart cm = ChartModels.createHSChart( );
		
		//Create the pdf renderer
		IDeviceRenderer idr = new PDFRendererImpl();

		try
		{
			RunTimeContext rtc = new RunTimeContext( );
			rtc.setULocale( ULocale.getDefault( ) );

			final Generator gr = Generator.instance( );
			GeneratedChartState gcs = null;
			//Set the chart size
			Bounds bo = BoundsImpl.create( 0, 0, 450, 300 );
			gcs = gr.build( idr.getDisplayServer( ), cm, bo, null, rtc, null );

			//Specify the file to write to. 
			idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER, "d:/temp/test.pdf" ); //$NON-NLS-1$

			//generate the chart
			gr.render( idr, gcs );
		}
		catch ( ChartException ce )
		{
			ce.printStackTrace( );
		}		
	}	
	
	public static final void main(String argv[]){
		generateChart();
	}
}
