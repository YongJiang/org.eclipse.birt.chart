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

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartDlg;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemFigureProvider;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * 
 */
public class ChartReportItemUIImpl extends ReportItemFigureProvider
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * 
	 */
	public ChartReportItemUIImpl( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#getFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public final IFigure createFigure( ExtendedItemHandle eih )
	{
		try
		{
			eih.loadExtendedElement( );
		}
		catch ( ExtendedElementException eeex )
		{
			logger.log( eeex );
		}
		try
		{
			final IReportItem iri = eih.getReportItem( );
			final DesignerRepresentation dr = new DesignerRepresentation( (ChartReportItemImpl) iri );
			( (ChartReportItemImpl) iri ).setDesignerRepresentation( dr ); // UPDATE
			// LINK
			return dr;
		}
		catch ( ExtendedElementException e )
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#updateFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 *      org.eclipse.draw2d.IFigure)
	 */
	public final void updateFigure( ExtendedItemHandle eih, IFigure ifg )
	{
		try
		{
			final DimensionHandle dhHeight = eih.getHeight( );
			final DimensionHandle dhWidth = eih.getWidth( );

			double dOriginalHeight = dhHeight.getMeasure( );
			String sHeightUnits = dhHeight.getUnits( );

			double dOriginalWidth = dhWidth.getMeasure( );
			String sWidthUnits = dhWidth.getUnits( );

			if ( sHeightUnits == null || sWidthUnits == null )
			{
				// check invalid case
				return;
			}

			// USE THE SWT DISPLAY SERVER TO CONVERT POINTS TO PIXELS
			final IDisplayServer idsSWT = ChartDlg.getDisplayServer( ); // REUSE
			// Convert from pixels to points first...since DimensionUtil does
			// not
			// provide conversion services to and from
			// Pixels
			if ( sHeightUnits == DesignChoiceConstants.UNITS_PX )
			{
				dOriginalHeight = ChartUtil.convertPixelsToPoints( idsSWT,
						dOriginalHeight );
				sHeightUnits = DesignChoiceConstants.UNITS_PT;
			}
			// convert percentage to points
			if ( sHeightUnits == DesignChoiceConstants.UNITS_PERCENTAGE )
			{
				IFigure parentFigure = ifg.getParent( );
				if ( parentFigure != null )
				{
					int height = (int) ( ( parentFigure.getSize( ).height - parentFigure.getInsets( )
							.getHeight( ) )
							* dOriginalHeight / 100 );
					dOriginalHeight = ChartUtil.convertPixelsToPoints( idsSWT,
							height );
					sHeightUnits = DesignChoiceConstants.UNITS_PT;
				}
			}
			double dHeightInPoints = DimensionUtil.convertTo( dOriginalHeight,
					sHeightUnits,
					DesignChoiceConstants.UNITS_PT ).getMeasure( );
			// Convert from pixels to points first...since DimensionUtil does
			// not
			// provide conversion services to and from
			// Pixels
			if ( sWidthUnits == DesignChoiceConstants.UNITS_PX )
			{
				dOriginalWidth = ( dOriginalWidth * 72d )
						/ idsSWT.getDpiResolution( );
				sWidthUnits = DesignChoiceConstants.UNITS_PT;
			}
			// convert percentage to points
			if ( sWidthUnits == DesignChoiceConstants.UNITS_PERCENTAGE )
			{
				IFigure parentFigure = ifg.getParent( );
				if ( parentFigure != null )
				{

					int width = (int) ( ( parentFigure.getSize( ).width - parentFigure.getInsets( )
							.getWidth( ) )
							* dOriginalWidth / 100 );
					dOriginalWidth = ChartUtil.convertPixelsToPoints( idsSWT,
							width );
					sWidthUnits = DesignChoiceConstants.UNITS_PT;
				}
			}
			double dWidthInPoints = DimensionUtil.convertTo( dOriginalWidth,
					sWidthUnits,
					DesignChoiceConstants.UNITS_PT ).getMeasure( );
			final double dHeightInPixels = ( idsSWT.getDpiResolution( ) * dHeightInPoints ) / 72d;
			final double dWidthInPixels = ( idsSWT.getDpiResolution( ) * dWidthInPoints ) / 72d;

			final ChartReportItemImpl crii;
			// UPDATE THE MODEL
			try
			{
				eih.loadExtendedElement( );
				crii = (ChartReportItemImpl) eih.getReportItem( );

				// update the handle relationship.
				crii.setHandle( eih );
			}
			catch ( ExtendedElementException eeex )
			{
				logger.log( eeex );
				return;
			}
			final Chart cm = (Chart) crii.getProperty( "chart.instance" ); //$NON-NLS-1$
			if ( dWidthInPoints > 0 )
				cm.getBlock( ).getBounds( ).setWidth( dWidthInPoints );
			if ( dHeightInPoints > 0 )
				cm.getBlock( ).getBounds( ).setHeight( dHeightInPoints );
			if ( crii.getDesignerRepresentation( ) != null )
			{
				( (DesignerRepresentation) crii.getDesignerRepresentation( ) ).setDirty( true );
			}

			// UPDATE THE FIGURE
			Dimension newSize = ifg.getBounds( ).getCopy( ).getSize( );
			if ( dWidthInPixels > 0 )
				newSize.width = (int) dWidthInPixels;
			if ( dHeightInPixels > 0 )
				newSize.height = (int) dHeightInPixels;
			ifg.setSize( newSize );
		}
		catch ( Exception ex )
		{
			logger.log( ex );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemUI#disposeFigure(org.eclipse.birt.report.model.api.ExtendedItemHandle,
	 *      org.eclipse.draw2d.IFigure)
	 */
	public final void disposeFigure( ExtendedItemHandle eih, IFigure ifg )
	{
		logger.log( ILogger.INFORMATION,
				Messages.getString( "ChartReportItemUIImpl.log.ReceivedNotification" ) ); //$NON-NLS-1$
		( (DesignerRepresentation) ifg ).dispose( );
	}
}