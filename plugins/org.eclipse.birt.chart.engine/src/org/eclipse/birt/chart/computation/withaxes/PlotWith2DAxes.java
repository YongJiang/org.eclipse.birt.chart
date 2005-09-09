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

package org.eclipse.birt.chart.computation.withaxes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This class is capable of computing the content of a chart (with axes) based
 * on preferred sizes, text rotation, fit ability, scaling, etc and prepares it
 * for rendering.
 * 
 * WARNING: This is an internal class and subject to change
 */
public final class PlotWith2DAxes extends PlotWithAxes
{

	/**
	 * This complex reference is used in rendering stacked series otherwise
	 * unused.
	 */
	private StackedSeriesLookup ssl = null;

	/**
	 * The default constructor
	 * 
	 * @param _ids
	 *            The display server using which the chart is computed
	 * @param _cwa
	 *            An instance of the model (ChartWithAxes)
	 * @throws ValidationException
	 * @throws UndefinedValueException
	 * @throws UnexpectedInputException
	 */
	public PlotWith2DAxes( IDisplayServer _ids, ChartWithAxes _cwa,
			RunTimeContext _rtc ) throws IllegalArgumentException,
			ChartException
	{
		cwa = _cwa;
		ids = _ids;
		rtc = _rtc;
		ssl = new StackedSeriesLookup( _rtc );
		dPointToPixel = ids.getDpiResolution( ) / 72d;
		buildAxes( ); // CREATED ONCE
	}

	/**
	 * Internally maps the EMF model to internal (non-public) rendering fast
	 * data structures
	 */
	final void buildAxes( ) throws IllegalArgumentException, ChartException
	{
		final Axis[] axa = cwa.getPrimaryBaseAxes( );
		final Axis axPrimaryBase = axa[0]; // NOTE: FOR REL 1 AXIS RENDERS, WE
		// SUPPORT A SINGLE PRIMARY BASE AXIS
		// ONLY
		if ( !axPrimaryBase.isSetOrientation( ) )
		{
			axPrimaryBase.setOrientation( Orientation.HORIZONTAL_LITERAL );
		}
		validateAxis( axPrimaryBase );
		final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis( axPrimaryBase );
		validateAxis( axPrimaryOrthogonal );
		if ( !axPrimaryOrthogonal.isSetOrientation( ) )
		{
			axPrimaryOrthogonal.setOrientation( Orientation.VERTICAL_LITERAL );
		}
		final Axis[] axaOverlayOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase,
				false );
		aax = new AllAxes( cwa.getPlot( )
				.getClientArea( )
				.getInsets( )
				.scaledInstance( dPointToPixel ) ); // CONVERSION
		insCA = aax.getInsets( );

		// SeriesDefinition sdBase = null;
		// // ONLY SUPPORT 1 BASE AXIS
		// if ( !axPrimaryBase.getSeriesDefinitions( ).isEmpty( ) )
		// {
		// // OK TO ASSUME THAT 1 BASE SERIES DEFINITION EXISTS
		// sdBase = (SeriesDefinition) axPrimaryBase.getSeriesDefinitions( )
		// .get( 0 );
		// final ArrayList alRuntimeBaseSeries = sdBase.getRunTimeSeries( );
		// if ( alRuntimeBaseSeries != null )
		// {
		// // OK TO ASSUME THAT 1 BASE RUNTIME SERIES EXISTS
		// seBaseRuntime = (Series) sdBase.getRunTimeSeries( ).get( 0 );
		// }
		// }

		aax.swapAxes( cwa.isTransposed( ) );

		// SETUP THE PRIMARY BASE-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryBase = new OneAxis( axPrimaryBase );
		oaxPrimaryBase.set( getOrientation( IConstants.BASE ),
				transposeLabelPosition( IConstants.BASE,
						getLabelPosition( axPrimaryBase.getLabelPosition( ) ) ),
				transposeLabelPosition( IConstants.BASE,
						getLabelPosition( axPrimaryBase.getTitlePosition( ) ) ),
				axPrimaryBase.isSetCategoryAxis( )
						&& axPrimaryBase.isCategoryAxis( ) );
		oaxPrimaryBase.setGridProperties( axPrimaryBase.getMajorGrid( )
				.getLineAttributes( ),
				axPrimaryBase.getMinorGrid( ).getLineAttributes( ),
				axPrimaryBase.getMajorGrid( ).getTickAttributes( ),
				axPrimaryBase.getMinorGrid( ).getTickAttributes( ),
				transposeTickStyle( IConstants.BASE,
						getTickStyle( axPrimaryBase, MAJOR ) ),
				transposeTickStyle( IConstants.BASE,
						getTickStyle( axPrimaryBase, MINOR ) ),
				axPrimaryBase.getScale( ).getMinorGridsPerUnit( ) );

		if ( cwa.isTransposed( ) )
		{
			// TRANSPOSE ROTATION OF LABELS AS APPROPRIATE
			final Label laAxisLabels = (Label) EcoreUtil.copy( axPrimaryBase.getLabel( ) );
			final Label laAxisTitle = (Label) EcoreUtil.copy( axPrimaryBase.getTitle( ) );
			laAxisLabels.getCaption( )
					.getFont( )
					.setRotation( transposeAngle( laAxisLabels.getCaption( )
							.getFont( )
							.getRotation( ) ) );
			laAxisTitle.getCaption( )
					.getFont( )
					.setRotation( transposeAngle( laAxisTitle.getCaption( )
							.getFont( )
							.getRotation( ) ) );
			oaxPrimaryBase.set( laAxisLabels, laAxisTitle ); // ASSOCIATE
			// FONT,
			// ETC
		}
		else
		{
			oaxPrimaryBase.set( axPrimaryBase.getLabel( ),
					axPrimaryBase.getTitle( ) ); // ASSOCIATE FONT
		}

		oaxPrimaryBase.set( getIntersection( axPrimaryBase ) );
		oaxPrimaryBase.set( axPrimaryBase.getLineAttributes( ) );
		aax.definePrimary( oaxPrimaryBase ); // ADD TO AXIS SET

		// SETUP THE PRIMARY ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryOrthogonal = new OneAxis( axPrimaryOrthogonal );
		oaxPrimaryOrthogonal.set( getOrientation( IConstants.ORTHOGONAL ),
				transposeLabelPosition( IConstants.ORTHOGONAL,
						getLabelPosition( axPrimaryOrthogonal.getLabelPosition( ) ) ),
				transposeLabelPosition( IConstants.ORTHOGONAL,
						getLabelPosition( axPrimaryOrthogonal.getTitlePosition( ) ) ),
				axPrimaryOrthogonal.isSetCategoryAxis( )
						&& axPrimaryOrthogonal.isCategoryAxis( ) );
		oaxPrimaryOrthogonal.setGridProperties( axPrimaryOrthogonal.getMajorGrid( )
				.getLineAttributes( ),
				axPrimaryOrthogonal.getMinorGrid( ).getLineAttributes( ),
				axPrimaryOrthogonal.getMajorGrid( ).getTickAttributes( ),
				axPrimaryOrthogonal.getMinorGrid( ).getTickAttributes( ),
				transposeTickStyle( IConstants.ORTHOGONAL,
						getTickStyle( axPrimaryOrthogonal, MAJOR ) ),
				transposeTickStyle( IConstants.ORTHOGONAL,
						getTickStyle( axPrimaryOrthogonal, MINOR ) ),
				axPrimaryOrthogonal.getScale( ).getMinorGridsPerUnit( ) );

		if ( cwa.isTransposed( ) )
		{
			// TRANSPOSE ROTATION OF LABELS AS APPROPRIATE
			final Label laAxisLabels = (Label) EcoreUtil.copy( axPrimaryOrthogonal.getLabel( ) );
			final Label laAxisTitle = (Label) EcoreUtil.copy( axPrimaryOrthogonal.getTitle( ) );
			laAxisLabels.getCaption( )
					.getFont( )
					.setRotation( transposeAngle( laAxisLabels.getCaption( )
							.getFont( )
							.getRotation( ) ) );
			laAxisTitle.getCaption( )
					.getFont( )
					.setRotation( transposeAngle( laAxisTitle.getCaption( )
							.getFont( )
							.getRotation( ) ) );
			oaxPrimaryOrthogonal.set( laAxisLabels, laAxisTitle ); // ASSOCIATE
			// FONT, ETC
		}
		else
		{
			oaxPrimaryOrthogonal.set( axPrimaryOrthogonal.getLabel( ),
					axPrimaryOrthogonal.getTitle( ) ); // ASSOCIATE FONT,
			// ETC
		}
		oaxPrimaryOrthogonal.set( getIntersection( axPrimaryOrthogonal ) );
		oaxPrimaryOrthogonal.set( axPrimaryOrthogonal.getLineAttributes( ) );
		aax.definePrimary( oaxPrimaryOrthogonal ); // ADD TO AXIS SET

		// SETUP THE OVERLAY AXES
		aax.initOverlays( axaOverlayOrthogonal.length,
				getOrientation( IConstants.ORTHOGONAL ) );
		OneAxis oaxOverlayOrthogonal;
		for ( int i = 0; i < axaOverlayOrthogonal.length; i++ )
		{
			validateAxis( axaOverlayOrthogonal[i] );
			if ( !axaOverlayOrthogonal[i].isSetOrientation( ) )
			{
				axaOverlayOrthogonal[i].setOrientation( Orientation.VERTICAL_LITERAL );
			}

			oaxOverlayOrthogonal = new OneAxis( axaOverlayOrthogonal[i] );
			oaxOverlayOrthogonal.set( getOrientation( IConstants.ORTHOGONAL ),
					transposeLabelPosition( IConstants.ORTHOGONAL,
							getLabelPosition( axaOverlayOrthogonal[i].getLabelPosition( ) ) ),
					transposeLabelPosition( IConstants.ORTHOGONAL,
							getLabelPosition( axaOverlayOrthogonal[i].getTitlePosition( ) ) ),
					axaOverlayOrthogonal[i].isSetCategoryAxis( )
							&& axaOverlayOrthogonal[i].isCategoryAxis( ) );
			oaxOverlayOrthogonal.setGridProperties( axaOverlayOrthogonal[i].getMajorGrid( )
					.getLineAttributes( ),
					axaOverlayOrthogonal[i].getMinorGrid( ).getLineAttributes( ),
					axaOverlayOrthogonal[i].getMajorGrid( ).getTickAttributes( ),
					axaOverlayOrthogonal[i].getMinorGrid( ).getTickAttributes( ),
					transposeTickStyle( IConstants.ORTHOGONAL,
							getTickStyle( axaOverlayOrthogonal[i], MAJOR ) ),
					transposeTickStyle( IConstants.ORTHOGONAL,
							getTickStyle( axaOverlayOrthogonal[i], MINOR ) ),
					axaOverlayOrthogonal[i].getScale( ).getMinorGridsPerUnit( ) );

			if ( cwa.isTransposed( ) )
			{
				// TRANSPOSE ROTATION OF LABELS AS APPROPRIATE
				final Label laAxisLabels = (Label) EcoreUtil.copy( axaOverlayOrthogonal[i].getLabel( ) );
				final Label laAxisTitle = (Label) EcoreUtil.copy( axaOverlayOrthogonal[i].getTitle( ) );
				laAxisLabels.getCaption( )
						.getFont( )
						.setRotation( transposeAngle( laAxisLabels.getCaption( )
								.getFont( )
								.getRotation( ) ) );
				laAxisTitle.getCaption( )
						.getFont( )
						.setRotation( transposeAngle( laAxisTitle.getCaption( )
								.getFont( )
								.getRotation( ) ) );
				oaxOverlayOrthogonal.set( laAxisLabels, laAxisTitle ); // ASSOCIATE
				// FONT,
				// ETC
			}
			else
			{
				oaxOverlayOrthogonal.set( axaOverlayOrthogonal[i].getLabel( ),
						axaOverlayOrthogonal[i].getTitle( ) );
			}
			oaxOverlayOrthogonal.set( axaOverlayOrthogonal[i].getLineAttributes( ) );
			oaxOverlayOrthogonal.set( getIntersection( axaOverlayOrthogonal[i] ) );
			aax.defineOverlay( i, oaxOverlayOrthogonal );
		}

		// BUILD STACKED STRUCTURE (FOR STACKED SERIES) ASSOCIATED WITH EACH
		// ORTHOGONAL AXIS
		ssl = StackedSeriesLookup.create( cwa, rtc );
	}

	/**
	 * This method pulls out the 'min' and 'max' value for all datasets
	 * associated with a single axis using the custom data source processor
	 * implementation
	 * 
	 * @param ax
	 *            The orthogonal axis for which the min/max values are being
	 *            computed
	 * @param iType
	 *            The renderer's axis data type
	 * 
	 * @return
	 */
	private final Object getMinMax( Axis ax, int iType ) throws ChartException,
			IllegalArgumentException
	{
		final Series[] sea = ax.getRuntimeSeries( );
		final int iSeriesCount = sea.length;
		Series se;
		DataSet ds;

		Object oV1, oV2, oMin = null, oMax = null;

		PluginSettings ps = PluginSettings.instance( );
		IDataSetProcessor iDSP = null;
		boolean bAnyStacked = false; // ANY STACKED SERIES ASSOCIATED WITH
		// AXIS
		// 'ax'

		for ( int i = 0; i < iSeriesCount; i++ )
		{
			if ( sea[i].isStacked( ) )
			{
				if ( sea[i].canBeStacked( ) )
				{
					bAnyStacked = true;
					continue;
				}
				else
				{
					throw new IllegalArgumentException( MessageFormat.format( ResourceBundle.getBundle( Messages.ENGINE,
							rtc.getLocale( ) )
							.getString( "exception.unstackable.is.stacked" ), //$NON-NLS-1$
							new Object[]{
								sea[i]
							} ) );
				}
			}

			iDSP = ps.getDataSetProcessor( sea[i].getClass( ) );
			ds = sea[i].getDataSet( );

			oV1 = iDSP.getMinimum( ds );
			oV2 = iDSP.getMaximum( ds );

			if ( ( iType & NUMERICAL ) == NUMERICAL )
			{
				if ( oV1 != null ) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
				{
					if ( oMin == null )
					{
						oMin = oV1;
					}
					else
					{
						final double dV1 = asDouble( oV1 ).doubleValue( );
						if ( Math.min( asDouble( oMin ).doubleValue( ), dV1 ) == dV1 )
						{
							oMin = oV1;
						}
					}
				}

				if ( oV2 != null ) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
				{
					if ( oMax == null )
					{
						oMax = oV2;
					}
					else
					{
						final double dV2 = asDouble( oV2 ).doubleValue( );
						if ( Math.max( asDouble( oMax ).doubleValue( ), dV2 ) == dV2 )
						{
							oMax = oV2;
						}
					}
				}
			}
			else if ( ( iType & DATE_TIME ) == DATE_TIME )
			{
				if ( oV1 != null ) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
				{
					if ( oMin == null )
					{
						oMin = oV1;
					}
					else
					{
						final CDateTime cdtV1 = asDateTime( oV1 );
						final CDateTime cdtMin = asDateTime( oMin );
						if ( cdtV1.before( cdtMin ) )
						{
							oMin = cdtV1;
						}
					}
				}

				if ( oV2 != null ) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
				{
					if ( oMax == null )
					{
						oMax = oV2;
					}
					else
					{
						final CDateTime cdtV2 = asDateTime( oV2 );
						final CDateTime cdtMax = asDateTime( oMax );
						if ( cdtV2.after( cdtMax ) )
						{
							oMax = cdtV2;
						}
					}
				}
			}
		}

		// ONLY NUMERIC VALUES ARE SUPPORTED IN STACKED ELEMENT COMPUTATIONS
		if ( bAnyStacked || ax.isPercent( ) )
		{
			if ( ax.getType( ).getValue( ) == AxisType.DATE_TIME )
			{
				throw new IllegalArgumentException( MessageFormat.format( ResourceBundle.getBundle( Messages.ENGINE,
						rtc.getLocale( ) )
						.getString( "exception.stacked.datetime.axis.series" ), //$NON-NLS-1$
						new Object[]{
							ax
						} ) );
			}
			Object oValue;
			int iSeriesPerGroup;
			double dGroupMin, dGroupMax, dValue, dAbsTotal, dPercentMax = 0, dPercentMin = 0;
			double dAxisMin = Double.MAX_VALUE, dAxisMax = Double.MIN_VALUE;
			ArrayList alSeriesGroupsPerAxis = ssl.getStackGroups( ax );
			ArrayList alSeriesPerGroup;
			StackGroup sg;
			DataSetIterator[] dsi = new DataSetIterator[ssl.getSeriesCount( ax )];

			if ( alSeriesGroupsPerAxis == null )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.internal.stack.series.setup", //$NON-NLS-1$
						new Object[]{
							ax
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
			logger.log( ILogger.INFORMATION,
					Messages.getString( "info.processing.stacked.info", //$NON-NLS-1$
							new Object[]{
								ax
							}, rtc.getLocale( ) ) );
			int iSeriesIndex, iDataSetCount = ssl.getUnitCount( );

			for ( int k = 0; k < iDataSetCount; k++ ) // PER UNIT
			{
				dAbsTotal = 0;
				iSeriesIndex = 0;
				for ( int i = 0; i < alSeriesGroupsPerAxis.size( ); i++ )
				{
					sg = (StackGroup) alSeriesGroupsPerAxis.get( i );
					alSeriesPerGroup = sg.getSeries( );
					iSeriesPerGroup = alSeriesPerGroup.size( );

					if ( iSeriesPerGroup > 0 )
					{
						se = (Series) alSeriesPerGroup.get( 0 );
						ds = se.getDataSet( );
						if ( dsi[iSeriesIndex] == null )
						{
							dsi[iSeriesIndex] = new DataSetIterator( ds );
							if ( ( dsi[iSeriesIndex].getDataType( ) & IConstants.NUMERICAL ) != IConstants.NUMERICAL )
							{
								throw new ChartException( ChartEnginePlugin.ID,
										ChartException.DATA_SET,
										"exception.percent.stacked.non.numerical", //$NON-NLS-1$ 
										ResourceBundle.getBundle( Messages.ENGINE,
												rtc.getLocale( ) ) );
							}
						}
						iDataSetCount = dsi[iSeriesIndex].size( ); // ALL
						// SERIES
						// MUST HAVE
						// THE SAME
						// DATASET
						// ELEMENT
						// COUNT

						dGroupMin = 0;
						dGroupMax = 0;
						if ( ax.isPercent( ) )
						{
							dAbsTotal = 0;
						}
						for ( int j = 0; j < iSeriesPerGroup; j++ ) // FOR ALL
						// SERIES
						{
							se = (Series) alSeriesPerGroup.get( j ); // EACH
							// SERIES
							if ( j > 0 ) // ALREADY DONE FOR '0'
							{
								if ( dsi[iSeriesIndex] == null )
								{
									ds = se.getDataSet( ); // DATA SET
									dsi[iSeriesIndex] = new DataSetIterator( ds );
									if ( ( dsi[iSeriesIndex].getDataType( ) & IConstants.NUMERICAL ) != IConstants.NUMERICAL )
									{
										throw new ChartException( ChartEnginePlugin.ID,
												ChartException.DATA_SET,
												"exception.percent.stacked.non.numerical", //$NON-NLS-1$ 
												ResourceBundle.getBundle( Messages.ENGINE,
														rtc.getLocale( ) ) );
									}
								}
							}
							oValue = dsi[iSeriesIndex].next( ); // EACH ROW OF
							// DATA
							if ( oValue != null ) // NULL CHECK
							{
								dValue = ( (Double) oValue ).doubleValue( ); // EXTRACT
								// WRAPPED
								// VALUE
								dAbsTotal += Math.abs( dValue );
								if ( dValue > 0 )
								{
									dGroupMax += dValue; // UPDATE MAX
								}
								else if ( dValue < 0 )
								{
									dGroupMin += dValue; // UPDATE MIN
								}
							}
							iSeriesIndex++;
						}
						final AxisSubUnit au = ssl.getSubUnit( sg, k );
						au.setPositiveTotal( dGroupMax );
						au.setNegativeTotal( dGroupMin );

						// FOR EACH UNIT, UPDATE THE MIN/MAX BASED ON ALL
						// STACKED SERIES
						dAxisMax = Math.max( dGroupMax, dAxisMax );
						dAxisMin = Math.min( dGroupMin, dAxisMin );
						if ( ax.isPercent( ) )
						{
							if ( dAbsTotal != 0d )
							{
								dPercentMax = Math.max( ( dGroupMax / dAbsTotal ) * 100d,
										dPercentMax );
								dPercentMin = Math.min( ( dGroupMin / dAbsTotal ) * 100d,
										dPercentMin );
							}
						}
					}
				}
			}
			if ( ax.isPercent( ) ) // HANDLE PERCENT
			{
				if ( dPercentMax >= 100 )
					dPercentMax = 99.9;
				if ( dPercentMin <= -100 )
					dPercentMin = -99.9;
				if ( dPercentMax == 0 && dPercentMin == 0 )
				{
					dPercentMax = 99;
				}
				dAxisMin = dPercentMin;
				dAxisMax = dPercentMax;
			}
			if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
			{
				dAxisMin = 1;
			}
			oMin = new Double( dAxisMin );
			oMax = new Double( dAxisMax );
		}

		// IF NO DATASET WAS FOUND BECAUSE NO SERIES WERE ATTACHED TO AXES,
		// SIMULATE MIN/MAX VALUES
		if ( oMin == null && oMax == null )
		{
			if ( iType == DATE_TIME )
			{
				oMin = new CDateTime( 1, 1, 2005 );
				oMax = new CDateTime( 1, 1, 2006 );
			}
			else if ( ( iType & NUMERICAL ) == NUMERICAL )
			{
				if ( ( iType & PERCENT ) == PERCENT )
				{
					oMin = new Double( 0 );
					oMax = new Double( 99.99 );
				}
				else if ( ( iType & LOGARITHMIC ) == LOGARITHMIC )
				{
					oMin = new Double( 1 );
					oMax = new Double( 999 );
				}
				else
				{
					oMin = new Double( -0.9 );
					oMax = new Double( 0.9 );
				}
			}
		}

		if ( iType == DATE_TIME )
		{
			try
			{
				return new Calendar[]{
						asDateTime( oMin ), asDateTime( oMax )
				};
			}
			catch ( ClassCastException ex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.numerical.data.datetime.axis", //$NON-NLS-1$ 
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );

			}
		}
		else if ( ( iType & NUMERICAL ) == NUMERICAL )
		{
			try
			{
				return new double[]{
						asDouble( oMin ).doubleValue( ),
						asDouble( oMax ).doubleValue( )
				};
			}
			catch ( ClassCastException ex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.datetime.data.numerical.axis", //$NON-NLS-1$ 
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
		}
		return null;
	}

	/**
	 * This method computes the entire chart within the given bounds. If the
	 * dataset has changed but none of the axis attributes have changed, simply
	 * re-compute without 'rebuilding axes'.
	 * 
	 * @param bo
	 * 
	 */
	public final void compute( Bounds bo ) throws ChartException,
			IllegalArgumentException
	{
		bo = bo.scaledInstance( dPointToPixel ); // CONVERSION
		dSeriesThickness = ( ids.getDpiResolution( ) / 72d )
				* cwa.getSeriesThickness( );

		// MAINTAIN IN LOCAL VARIABLES FOR PERFORMANCE/CONVENIENCE
		double dX = bo.getLeft( ) + insCA.getLeft( );
		double dY = bo.getTop( ) + insCA.getTop( );
		double dW = bo.getWidth( ) - insCA.getLeft( ) - insCA.getRight( );
		double dH = bo.getHeight( ) - insCA.getTop( ) - insCA.getBottom( );

		iDimension = getDimension( cwa.getDimension( ) );
		dXAxisPlotSpacing = cwa.getPlot( ).getHorizontalSpacing( )
				* dPointToPixel; // CONVERSION
		dYAxisPlotSpacing = cwa.getPlot( ).getVerticalSpacing( )
				* dPointToPixel; // CONVERSION

		if ( iDimension == TWO_5_D )
		{
			dY += dSeriesThickness;
			dH -= dSeriesThickness;
			dW -= dSeriesThickness;
			bo.setHeight( dH );
			bo.setTop( dY );
			bo.setWidth( dW );
		}

		// PLACE OVERLAYS FIRST TO REDUCE VIRTUAL PLOT BOUNDS
		if ( aax.getOverlayCount( ) > 0 )
		{
			if ( aax.areAxesSwapped( ) ) // ORTHOGONAL OVERLAYS = HORIZONTAL
			{
				updateOverlayScales( aax, dX, dX + dW, dY, dH );
				dY = aax.getStart( );
				dH = aax.getLength( );
			}
			else
			// ORTHOGONAL OVERLAYS = VERTICAL
			{
				updateOverlayScales( aax, dY - dH, dY, dX, dW );
				dX = aax.getStart( );
				dW = aax.getLength( );
			}
		}

		double dStart, dEnd;
		final Axis[] axa = cwa.getPrimaryBaseAxes( );
		final Axis axPrimaryBase = axa[0];
		final Axis axPrimaryOrthogonal = cwa.getPrimaryOrthogonalAxis( axPrimaryBase );
		Scale sc = axPrimaryBase.getScale( );

		// COMPUTE PRIMARY-BASE-AXIS PROPERTIES AND ITS SCALE
		AutoScale scPrimaryBase = null;
		OneAxis oaxPrimaryBase = aax.getPrimaryBase( );
		int iAxisType = getAxisType( axPrimaryBase );

		Object oaData = null;
		if ( iAxisType == TEXT || oaxPrimaryBase.isCategoryScale( ) )
		{
			oaData = getTypedDataSet( axPrimaryBase, iAxisType, 0 );
		}
		else if ( ( iAxisType & NUMERICAL ) == NUMERICAL )
		{
			oaData = getMinMax( axPrimaryBase, iAxisType );
		}
		else if ( ( iAxisType & DATE_TIME ) == DATE_TIME )
		{
			oaData = getMinMax( axPrimaryBase, iAxisType );
		}

		DataSetIterator dsi = ( oaData instanceof DataSetIterator ) ? (DataSetIterator) oaData
				: new DataSetIterator( oaData, iAxisType );
		oaData = null;

		dStart = ( aax.areAxesSwapped( ) ) ? dY + dH : dX;
		dEnd = ( aax.areAxesSwapped( ) ) ? dY : dStart + dW;
		scPrimaryBase = AutoScale.computeScale( ids,
				oaxPrimaryBase,
				dsi,
				iAxisType,
				dStart,
				dEnd,
				sc.getMin( ),
				sc.getMax( ),
				sc.isSetStep( ) ? new Double( sc.getStep( ) ) : null,
				axPrimaryBase.getFormatSpecifier( ),
				rtc, false );
		oaxPrimaryBase.set( scPrimaryBase ); // UPDATE SCALE ON PRIMARY-BASE
		// AXIS

		// COMPUTE PRIMARY-ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
		AutoScale scPrimaryOrthogonal = null;
		OneAxis oaxPrimaryOrthogonal = aax.getPrimaryOrthogonal( );
		iAxisType = getAxisType( axPrimaryOrthogonal );
		oaData = null;
		if ( ( iAxisType & NUMERICAL ) == NUMERICAL
				|| ( iAxisType & DATE_TIME ) == DATE_TIME )
		{
			dsi = new DataSetIterator( getMinMax( axPrimaryOrthogonal,
					iAxisType ), iAxisType );
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_FORMAT,
					"exception.orthogonal.axis.numerical.datetime", //$NON-NLS-1$
					ResourceBundle.getBundle( Messages.ENGINE, rtc.getLocale( ) ) );
		}

		dStart = ( aax.areAxesSwapped( ) ) ? dX : dY + dH;
		dEnd = ( aax.areAxesSwapped( ) ) ? dX + dW : dY;
		sc = axPrimaryOrthogonal.getScale( );
		scPrimaryOrthogonal = AutoScale.computeScale( ids,
				oaxPrimaryOrthogonal,
				dsi,
				iAxisType,
				dStart,
				dEnd,
				sc.getMin( ),
				sc.getMax( ),
				sc.isSetStep( ) ? new Double( sc.getStep( ) ) : null,
				axPrimaryOrthogonal.getFormatSpecifier( ),
				rtc, false );
		oaxPrimaryOrthogonal.set( scPrimaryOrthogonal ); // UPDATE SCALE ON
		// PRIMARY-ORTHOGONAL
		// AXIS

		// ITERATIVELY ADJUST THE PRIMARY ORTHOGONAL AXIS POSITION DUE TO THE
		// SCALE, START/END LABELS
		double dYAxisLocation = adjustHorizontal( dX, dW, aax );

		// ITERATIVELY ADJUST THE PRIMARY BASE AXIS POSITION DUE TO THE SCALE,
		// START/END LABELS
		double dXAxisLocation = adjustVerticalDueToHorizontal( dY, dH, aax );

		// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
		iAxisType = getAxisType( axPrimaryOrthogonal );
		oaData = getTypedDataSet( axPrimaryOrthogonal, iAxisType, 0 );
		scPrimaryOrthogonal.setData( dsi );

		// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
		iAxisType = getAxisType( axPrimaryBase );
		if ( iAxisType != IConstants.TEXT )
		{
			scPrimaryBase.setData( getTypedDataSet( axPrimaryBase, iAxisType, 0 ) );
		}

		scPrimaryBase.resetShifts( );
		scPrimaryOrthogonal.resetShifts( );

		// UPDATE THE SIZES OF THE OVERLAY AXES
		updateOverlayAxes( aax );
		growBaseAxis( aax, bo );

		// dXAxisLocation = getLocation(scY,
		// oaxPrimaryOrthogonal.getIntersectionValue()); // UPDATE FOR OVERLAYS
		final OneAxis axPH = aax.areAxesSwapped( ) ? aax.getPrimaryOrthogonal( )
				: aax.getPrimaryBase( );
		final OneAxis axPV = aax.areAxesSwapped( ) ? aax.getPrimaryBase( )
				: aax.getPrimaryOrthogonal( );
		axPH.setAxisCoordinate( dXAxisLocation );
		axPV.setAxisCoordinate( dYAxisLocation );

		double[] daX = axPH.getScale( ).getEndPoints( );
		double[] daY = axPV.getScale( ).getEndPoints( );

		boPlotBackground.setLeft( daX[0] - insCA.getLeft( ) );
		boPlotBackground.setWidth( daX[1]
				- daX[0]
				+ insCA.getLeft( )
				+ insCA.getRight( )
				+ 1 );
		boPlotBackground.setTop( daY[1] - insCA.getTop( ) );
		boPlotBackground.setHeight( daY[0]
				- daY[1]
				+ insCA.getTop( )
				+ insCA.getBottom( )
				+ 1 );
		if ( iDimension == TWO_5_D )
		{
			boPlotBackground.delta( dSeriesThickness, -dSeriesThickness, 0, 0 );
		}
	}

	/**
	 * This method attempts to stretch the base axis so it fits snugly (w.r.t.
	 * horizontal/vertical spacing) with the overlay axes (if any)
	 * 
	 * @param aax
	 */
	private final void growBaseAxis( AllAxes aax, Bounds bo )
			throws ChartException
	{
		// if (true) return;
		OneAxis oaxBase = aax.getPrimaryBase( );
		OneAxis oaxOrthogonal = aax.getPrimaryOrthogonal( );
		AutoScale scBase = oaxBase.getScale( );

		if ( !aax.areAxesSwapped( ) ) // STANDARD ORIENTATION
		{
			// IF PRIMARY ORTHOGONAL AXIS IS NOT ON THE RIGHT
			if ( aax.getOverlayCount( ) > 0
					&& oaxOrthogonal.getIntersectionValue( ).getType( ) != IConstants.MAX )
			{
				// IF ANY OVERLAY ORTHOGONAL AXES ARE ON THE RIGHT
				if ( aax.anyOverlayPositionedAt( IConstants.MAX ) )
				{
					scBase.computeAxisStartEndShifts( ids,
							oaxBase.getLabel( ),
							HORIZONTAL,
							oaxBase.getLabelPosition( ),
							aax );
					final double dRightThreshold = bo.getLeft( )
							+ bo.getWidth( );
					double dEnd = scBase.getEnd( );
					final double dEndShift = scBase.getEndShift( );
					if ( dEnd + dEndShift < dRightThreshold )
					{
						dEnd += dEndShift;
						scBase.computeTicks( ids,
								oaxBase.getLabel( ),
								oaxBase.getLabelPosition( ),
								HORIZONTAL,
								scBase.getStart( ),
								dEnd,
								false,
								null );
					}
				}
			}

			// IF PRIMARY ORTHOGONAL AXIS IS NOT ON THE LEFT
			else if ( aax.getOverlayCount( ) > 0
					&& oaxOrthogonal.getIntersectionValue( ).getType( ) != IConstants.MIN )
			{
				// IF ANY OVERLAY ORTHOGONAL AXES ARE ON THE LEFT
				if ( aax.anyOverlayPositionedAt( IConstants.MIN ) )
				{
					scBase.computeAxisStartEndShifts( ids,
							oaxBase.getLabel( ),
							HORIZONTAL,
							oaxBase.getLabelPosition( ),
							aax );
					final double dLeftThreshold = bo.getLeft( );
					double dStart = scBase.getStart( );
					final double dEndShift = scBase.getEndShift( );
					final double dStartShift = scBase.getStartShift( );
					if ( dStart - dStartShift > dLeftThreshold )
					{
						dStart -= dStartShift;
						final double dEnd = scBase.getEnd( ) + dEndShift;
						scBase.computeTicks( ids,
								oaxBase.getLabel( ),
								oaxBase.getLabelPosition( ),
								HORIZONTAL,
								dStart,
								dEnd,
								false,
								null );
					}
				}
			}
		}
		else
		{
			// IF PRIMARY ORTHOGONAL AXIS IS NOT AT THE TOP
			if ( aax.getOverlayCount( ) > 0
					&& oaxOrthogonal.getIntersectionValue( ).getType( ) != IConstants.MAX )
			{
				// IF ANY OVERLAY ORTHOGONAL AXES ARE AT THE TOP
				if ( aax.anyOverlayPositionedAt( IConstants.MAX ) )
				{
					scBase.computeAxisStartEndShifts( ids,
							oaxBase.getLabel( ),
							VERTICAL,
							oaxBase.getLabelPosition( ),
							aax );
					final double dTopThreshold = bo.getTop( );
					double dEnd = scBase.getEnd( );
					final double dEndShift = Math.floor( scBase.getEndShift( ) );
					if ( dEnd - dEndShift > dTopThreshold )
					{
						dEnd = dEnd - dEndShift;
						final double dStart = scBase.getStart( );
						scBase.computeTicks( ids,
								oaxBase.getLabel( ),
								oaxBase.getLabelPosition( ),
								VERTICAL,
								dStart,
								dEnd,
								false,
								null );
					}
				}
			}

			// IF PRIMARY ORTHOGONAL AXIS IS NOT AT THE BOTTOM
			else if ( aax.getOverlayCount( ) > 0
					&& oaxOrthogonal.getIntersectionValue( ).getType( ) != IConstants.MIN )
			{
				// IF ANY OVERLAY ORTHOGONAL AXES IS AT THE BOTTOM
				if ( aax.anyOverlayPositionedAt( IConstants.MIN ) )
				{
					scBase.computeAxisStartEndShifts( ids,
							oaxBase.getLabel( ),
							VERTICAL,
							oaxBase.getLabelPosition( ),
							aax );
					final double dBottomThreshold = bo.getTop( )
							+ bo.getHeight( );
					double dStart = scBase.getStart( );
					final double dEndShift = scBase.getEndShift( );
					final double dStartShift = scBase.getStartShift( );
					if ( dStart + dStartShift < dBottomThreshold )
					{
						dStart += dStartShift;
						final double dEnd = scBase.getEnd( ) - dEndShift;
						scBase.computeTicks( ids,
								oaxBase.getLabel( ),
								oaxBase.getLabelPosition( ),
								VERTICAL,
								dStart,
								dEnd,
								false,
								null );
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param aax
	 * @param dAxisStart
	 * @param dAxisEnd
	 * @param dBlockStart
	 * @param dBlockLength
	 * 
	 * @throws PluginException
	 * @throws DataSetException
	 * @throws GenerationException
	 */
	private final void updateOverlayScales( AllAxes aax, double dAxisStart,
			double dAxisEnd, double dBlockStart, double dBlockLength )
			throws ChartException, IllegalArgumentException
	{
		final Axis[] axa = ( (ChartWithAxesImpl) cwa ).getPrimaryBaseAxes( );
		final Axis axPrimaryBase = axa[0];
		final Axis[] axaOrthogonal = ( (ChartWithAxesImpl) cwa ).getOrthogonalAxes( axPrimaryBase,
				false );

		IntersectionValue iv;
		AutoScale sc = null;
		OneAxis oaxOverlay = null;
		int iTickStyle, iAxisType, j, iTitleLocation;
		int iOverlayCount = aax.getOverlayCount( );
		int iOrientation = aax.getOrientation( );
		double dStart, dEnd, dAxisLabelsThickness;
		Label laAxisTitle;
		Scale scModel;

		for ( int i = 0; i < iOverlayCount; i++ ) // ITERATE THROUGH EACH
		// OVERLAY ORTHOGONAL AXIS
		{
			j = iOverlayCount - i - 1; // GO BACKWARDS TO ENSURE CORRECT
			// RENDERING ORDER
			oaxOverlay = aax.getOverlay( j ); // UPDATE A PREVIOUSLY DEFINED
			// OVERLAY AXIS AUTO COMPUTE SCALE
			iTickStyle = oaxOverlay.getCombinedTickStyle( );
			iTitleLocation = oaxOverlay.getTitlePosition( );
			laAxisTitle = oaxOverlay.getTitle( );
			iAxisType = getAxisType( axaOrthogonal[j] );

			scModel = axaOrthogonal[j].getScale( );
			sc = AutoScale.computeScale( ids,
					oaxOverlay,
					new DataSetIterator( getMinMax( axaOrthogonal[j], iAxisType ),
							iAxisType ),
					iAxisType,
					dAxisStart,
					dAxisEnd,
					scModel.getMin( ),
					scModel.getMax( ),
					scModel.isSetStep( ) ? new Double( scModel.getStep( ) )
							: null,
					axaOrthogonal[j].getFormatSpecifier( ),
					rtc, false );

			oaxOverlay.set( sc );
			iv = oaxOverlay.getIntersectionValue( );

			// UPDATE AXIS ENDPOINTS DUE TO ITS AXIS LABEL SHIFTS
			dStart = sc.getStart( );
			dEnd = sc.getEnd( );
			sc.computeTicks( ids,
					oaxOverlay.getLabel( ),
					oaxOverlay.getLabelPosition( ),
					iOrientation,
					dStart,
					dEnd,
					true,
					null );
			if ( !sc.isStepFixed( ) )
			{
				final Object[] oaMinMax = sc.getMinMax( );
				while ( !sc.checkFit( ids,
						oaxOverlay.getLabel( ),
						oaxOverlay.getLabelPosition( ) ) )
				{
					sc.zoomOut( );
					sc.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
					sc.computeTicks( ids,
							oaxOverlay.getLabel( ),
							oaxOverlay.getLabelPosition( ),
							iOrientation,
							dStart,
							dEnd,
							true,
							null );
				}
			}
			dAxisLabelsThickness = sc.computeAxisLabelThickness( ids,
					oaxOverlay.getLabel( ),
					iOrientation );
			double dAxisTitleThickness = 0;
			sc.resetShifts( );

			if ( iOrientation == VERTICAL )
			{
				// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS
				// AND AXIS-PLOT SPACING
				double dX = 0, dX1 = 0, dX2 = 0; // Y-AXIS BAND VERTICAL
				// CO-ORDINATES
				final boolean bTicksLeft = ( iTickStyle & TICK_LEFT ) == TICK_LEFT; // 'boolean'
				// FOR
				// CONVENIENCE
				// &
				// READABILITY
				final boolean bTicksRight = ( iTickStyle & TICK_RIGHT ) == TICK_RIGHT; // 'boolean'
				// FOR
				// CONVENIENCE
				// &
				// READABILITY
				final double dAppliedYAxisPlotSpacing = dYAxisPlotSpacing;
				if ( laAxisTitle.isVisible( ) )
				{
					final String sPreviousValue = laAxisTitle.getCaption( )
							.getValue( );
					laAxisTitle.getCaption( )
							.setValue( rtc.externalizedMessage( sPreviousValue ) );
					dAxisTitleThickness = computeBox( ids,
							iTitleLocation,
							laAxisTitle,
							0,
							0 ).getWidth( );
					laAxisTitle.getCaption( ).setValue( sPreviousValue );
				}

				// COMPUTE VALUES FOR x1, x, x2
				// x = HORIZONTAL LOCATION OF Y-AXIS ALONG PLOT
				// x1 = LEFT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				// x2 = RIGHT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				if ( iv.getType( ) == IntersectionValue.MIN ) // LEFT OF PLOT
				{
					// NOTE: ENSURE CODE SYMMETRY WITH 'iaLabelPositions[i] ==
					// RIGHT'
					dX = dBlockStart;
					dX -= dAppliedYAxisPlotSpacing;
					dX1 = dX;
					dX2 = dX;
					if ( bTicksLeft )
					{
						dX1 -= TICK_SIZE;
					}
					if ( oaxOverlay.getLabelPosition( ) == LEFT )
					{
						dX1 -= dAxisLabelsThickness;
						dX2 += Math.max( bTicksRight ? TICK_SIZE : 0,
								dAppliedYAxisPlotSpacing );
					}
					else if ( oaxOverlay.getLabelPosition( ) == RIGHT )
					{
						dX2 += Math.max( ( bTicksRight ? TICK_SIZE : 0 )
								+ dAxisLabelsThickness,
								dAppliedYAxisPlotSpacing );
					}

					if ( iTitleLocation == LEFT )
					{
						dX1 -= dAxisTitleThickness;
					}
					else if ( iTitleLocation == RIGHT )
					{
						dX2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
					if ( dX1 < dBlockStart )
					{
						final double dDelta = ( dBlockStart - dX1 );
						dX1 = dBlockStart;
						dX += dDelta;
						dX2 += dDelta;
					}
					dBlockStart += ( dX2 - dX1 ); // SHIFT LEFT EDGE >>
				}
				else if ( iv.getType( ) == IntersectionValue.MAX ) // RIGHT
				{
					// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
					dX = dBlockStart + dBlockLength;
					dX += dAppliedYAxisPlotSpacing;
					dX1 = dX;
					dX2 = dX;
					if ( bTicksRight )
					{
						dX2 += TICK_SIZE;
					}

					if ( oaxOverlay.getLabelPosition( ) == RIGHT )
					{
						dX2 += dAxisLabelsThickness;
						dX1 -= Math.max( bTicksLeft ? TICK_SIZE : 0,
								dAppliedYAxisPlotSpacing );
					}
					else if ( oaxOverlay.getLabelPosition( ) == LEFT )
					{
						dX1 -= Math.max( ( bTicksLeft ? TICK_SIZE : 0 )
								+ dAxisLabelsThickness,
								dAppliedYAxisPlotSpacing );
					}

					if ( iTitleLocation == LEFT )
					{
						dX1 -= dAxisTitleThickness;
					}
					else if ( iTitleLocation == RIGHT )
					{
						dX2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK
					// EDGE
					if ( dX2 > dBlockStart + dBlockLength )
					{
						final double dDelta = dX2
								- ( dBlockStart + dBlockLength );
						dX2 = dBlockStart + dBlockLength;
						dX -= dDelta;
						dX1 -= dDelta;
					}

					dAxisLabelsThickness = dX2 - dX1; // REUSE VARIABLE
				}
				dBlockLength -= dAxisLabelsThickness; // SHIFT RIGHT EDGE <<

				double dDelta = 0;
				if ( iv.getType( ) == IntersectionValue.MIN )
				{
					dDelta = -insCA.getLeft( );
				}
				else if ( iv.getType( ) == IntersectionValue.MAX )
				{
					dDelta = insCA.getRight( );
				}

				oaxOverlay.setAxisCoordinate( dX + dDelta );
				oaxOverlay.setTitleCoordinate( ( iTitleLocation == LEFT ) ? dX1
						+ dDelta
						- 1 : dX2 + 1 - dAxisTitleThickness + dDelta // dX1<=>dX<=>dX2
				// INCORPORATES
						// TITLE
						);
			}
			else if ( iOrientation == HORIZONTAL )
			{
				// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS
				// AND AXIS-PLOT SPACING
				double dY = 0, dY1 = dY, dY2 = dY; // X-AXIS
				// BAND
				// VERTICAL
				// CO-ORDINATES
				final boolean bTicksAbove = ( iTickStyle & TICK_ABOVE ) == TICK_ABOVE; // 'boolean'
				// FOR
				// CONVENIENCE
				// &
				// READABILITY
				final boolean bTicksBelow = ( iTickStyle & TICK_BELOW ) == TICK_BELOW; // 'boolean'
				// FOR
				// CONVENIENCE
				// &
				// READABILITY
				final double dAppliedXAxisPlotSpacing = dXAxisPlotSpacing;
				if ( laAxisTitle.isVisible( ) )
				{
					final String sPreviousValue = laAxisTitle.getCaption( )
							.getValue( );
					laAxisTitle.getCaption( )
							.setValue( rtc.externalizedMessage( sPreviousValue ) );
					dAxisTitleThickness = computeBox( ids,
							iTitleLocation,
							laAxisTitle,
							0,
							0 ).getHeight( );
					laAxisTitle.getCaption( ).setValue( sPreviousValue );
				}

				// COMPUTE VALUES FOR y1, y, y2
				// y = VERTICAL LOCATION OF X-AXIS ALONG PLOT
				// y1 = UPPER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				// y2 = LOWER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS,
				// SPACING)
				if ( iv.getType( ) == IntersectionValue.MAX ) // ABOVE THE
				// PLOT
				{
					dY = dBlockStart;
					dY -= dAppliedXAxisPlotSpacing;
					dY1 = dY;
					dY2 = dY;
					if ( bTicksAbove )
					{
						dY1 -= TICK_SIZE;
					}
					if ( oaxOverlay.getLabelPosition( ) == ABOVE )
					{
						dY1 -= dAxisLabelsThickness;
						dY2 += Math.max( bTicksBelow ? TICK_SIZE : 0,
								dAppliedXAxisPlotSpacing );
					}
					else if ( oaxOverlay.getLabelPosition( ) == BELOW )
					{
						dY2 += Math.max( ( bTicksBelow ? TICK_SIZE : 0 )
								+ dAxisLabelsThickness,
								dAppliedXAxisPlotSpacing );
					}

					if ( iTitleLocation == ABOVE )
					{
						dY1 -= dAxisTitleThickness;
					}
					else if ( iTitleLocation == BELOW )
					{
						dY2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
					if ( dY1 < dBlockStart )
					{
						final double dDelta = ( dBlockStart - dY1 );
						dY1 = dBlockStart;
						dY += dDelta;
						dY2 += dDelta;
					}
					dBlockStart += ( dY2 - dY1 ); // SHIFT TOP EDGE >>
				}
				else if ( iv.getType( ) == IntersectionValue.MIN ) // BELOW THE
				// PLOT
				{
					// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'
					dY = dBlockStart + dBlockLength;
					dY += dAppliedXAxisPlotSpacing;
					dY1 = dY;
					dY2 = dY;
					if ( bTicksBelow )
					{
						dY2 += TICK_SIZE;
					}

					if ( oaxOverlay.getLabelPosition( ) == BELOW )
					{
						dY2 += dAxisLabelsThickness;
						dY1 -= Math.max( bTicksAbove ? TICK_SIZE : 0,
								dAppliedXAxisPlotSpacing );
					}
					else if ( oaxOverlay.getLabelPosition( ) == ABOVE )
					{
						dY1 -= Math.max( ( bTicksAbove ? TICK_SIZE : 0 )
								+ dAxisLabelsThickness,
								dAppliedXAxisPlotSpacing );
					}

					if ( iTitleLocation == ABOVE )
					{
						dY1 -= dAxisTitleThickness;
					}
					else if ( iTitleLocation == BELOW )
					{
						dY2 += dAxisTitleThickness;
					}

					// ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK
					// EDGE
					if ( dY2 > dBlockStart + dBlockLength )
					{
						final double dDelta = dY2
								- ( dBlockStart + dBlockLength );
						dY2 = dBlockStart + dBlockLength;
						dY -= dDelta;
						dY1 -= dDelta;
					}
				}
				double dDelta = 0;
				if ( iv.getType( ) == IntersectionValue.MAX )
				{
					dDelta = -insCA.getTop( );
				}
				else if ( iv.getType( ) == IntersectionValue.MIN )
				{
					dDelta = insCA.getBottom( );
				}

				oaxOverlay.setAxisCoordinate( dY + dDelta );
				oaxOverlay.setTitleCoordinate( ( iTitleLocation == ABOVE ) ? dY1
						+ dDelta
						- 1
						: dY2 + 1 - dAxisTitleThickness + dDelta // dY1<=>dX<=>dY2
				// INCORPORATES
						// TITLE
						);

				dBlockLength -= ( dY2 - dY1 ); // SHIFT BOTTOM EDGE <<
			}
		}

		aax.setBlockCordinates( dBlockStart, dBlockLength );
	}

	private final void updateOverlayAxes( AllAxes aax ) throws ChartException,
			IllegalArgumentException
	{
		int iDirection = ( aax.getOrientation( ) == HORIZONTAL ) ? 1 : -1;
		final Axis[] axa = cwa.getPrimaryBaseAxes( );
		final Axis axPrimaryBase = axa[0]; // NOTE: FOR REL 1 AXIS RENDERS, WE
		// SUPPORT A SINGLE PRIMARY BASE AXIS
		// ONLY
		final Axis[] axaOverlayOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase,
				false );

		OneAxis axOverlay, axPrimary = aax.getPrimaryOrthogonal( );
		AutoScale scOA, sc = axPrimary.getScale( );
		double dStart, dEnd;
		Object[] oaMinMax;
		int iAxisType = ( aax.getOverlayCount( ) > 0 ) ? aax.getOverlay( 0 )
				.getScale( )
				.getType( ) : 0;

		Label la;
		for ( int i = 0; i < aax.getOverlayCount( ); i++ )
		{
			axOverlay = aax.getOverlay( i );
			la = axOverlay.getLabel( );
			scOA = axOverlay.getScale( );
			scOA.setEndPoints( scOA.getStart( )
					- scOA.getStartShift( )
					* iDirection, scOA.getEnd( )
					+ scOA.getEndShift( )
					* iDirection );
			/*
			 * dStart = scOA.getStart() + dMaxSS * iDirection; dEnd =
			 * scOA.getEnd() - dMaxES * iDirection;
			 */

			dStart = sc.getStart( );
			dEnd = sc.getEnd( );

			scOA.setEndPoints( dStart, dEnd );
			scOA.computeTicks( ids,
					la,
					axOverlay.getLabelPosition( ),
					aax.getOrientation( ),
					dStart,
					dEnd,
					false,
					null );
			if ( !scOA.isStepFixed( ) )
			{
				oaMinMax = scOA.getMinMax( );
				while ( !scOA.checkFit( ids, la, axOverlay.getLabelPosition( ) ) )
				{
					scOA.zoomOut( );
					scOA.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
					scOA.computeTicks( ids,
							la,
							axOverlay.getLabelPosition( ),
							aax.getOrientation( ),
							dStart,
							dEnd,
							false,
							null );
				}
			}

			// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
			scOA.setData( getTypedDataSet( axaOverlayOrthogonal[i],
					iAxisType,
					0 ) );
		}

		/*
		 * if (aax.areAxesSwapped()) { final AutoScale scPrimaryHorizontal =
		 * aax.getPrimaryOrthogonal().getScale();
		 * scPrimaryHorizontal.setEndPoints( scPrimaryHorizontal.getStart() +
		 * scPrimaryHorizontal.getEndShift(), scPrimaryHorizontal.getEnd() -
		 * scPrimaryHorizontal.getStartShift() ); } else { final AutoScale
		 * scPrimaryHorizontal = aax.getPrimaryBase().getScale();
		 * scPrimaryHorizontal.setEndPoints( scPrimaryHorizontal.getStart() +
		 * scPrimaryHorizontal.getEndShift(), scPrimaryHorizontal.getEnd() -
		 * scPrimaryHorizontal.getStartShift() ); }
		 */
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#getSeriesRenderingHints(org.eclipse.birt.chart.model.data.SeriesDefinition, org.eclipse.birt.chart.model.component.Series)
	 */
	public final ISeriesRenderingHints getSeriesRenderingHints(
			SeriesDefinition sdOrthogonal, Series seOrthogonal )
			throws ChartException, IllegalArgumentException
	{
		if ( seOrthogonal == null
				|| seOrthogonal.getClass( ) == SeriesImpl.class ) // EMPTY
		// PLOT
		// RENDERING
		// TECHNIQUE
		{
			return null;
		}
		OneAxis oaxOrthogonal = findOrthogonalAxis( seOrthogonal );
		if ( oaxOrthogonal == null )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.NOT_FOUND,
					"exception.axis.series.link.broken", //$NON-NLS-1$
					new Object[]{
						seOrthogonal
					},
					ResourceBundle.getBundle( Messages.ENGINE, rtc.getLocale( ) ) );
		}
		final OneAxis oaxBase = aax.getPrimaryBase( );
		final SeriesDefinition sdBase = (SeriesDefinition) oaxBase.getModelAxis( )
				.getSeriesDefinitions( )
				.get( 0 );

		final AutoScale scBase = oaxBase.getScale( );
		final AutoScale scOrthogonal = oaxOrthogonal.getScale( );
		final int iTickCount = scBase.getTickCount( );
		int iUnitCount = iTickCount;
		final double dUnitSize = scBase.getUnitSize( );
		if ( scBase.getType( ) == IConstants.DATE_TIME )
		{
			// TBD: HANDLE DATETIME VALUE VS TEXT AXIS
		}

		double[] daTickCoordinates = scBase.getTickCordinates( );
		Object oDataBase = null;
		DataSetIterator dsiDataBase = scBase.getData( );
		Object oDataOrthogonal;
		DataSetIterator dsiDataOrthogonal = getTypedDataSet( seOrthogonal,
				oaxOrthogonal.getScale( ).getType( ) );
		double dOrthogonalZero = 0;
		if ( ( scOrthogonal.getType( ) & NUMERICAL ) == NUMERICAL )
		{
			dOrthogonalZero = getLocation( scOrthogonal, 0 );
		}
		else
		{
			dOrthogonalZero = scOrthogonal.getStart( );
		}
		double dBaseZero = ( ( scBase.getType( ) & NUMERICAL ) == IConstants.NUMERICAL && !oaxBase.isCategoryScale( ) ) ? getLocation( scBase,
				0 )
				: scBase.getStart( );

		if ( scBase.getType( ) == TEXT || oaxBase.isCategoryScale( ) )
		{
			iUnitCount--;
		}

		double dX = 0, dY = 0, dLength = 0;
		Location lo;

		final int iBaseCount = dsiDataBase.size( );
		final int iOrthogonalCount = dsiDataOrthogonal.size( );
		DataPointHints[] dpa = null;

		if ( iBaseCount != iOrthogonalCount ) // DO NOT COMPUTE DATA POINT
		// HINTS
		// FOR OUT-OF-SYNC DATA
		{
			logger.log( ILogger.INFORMATION,
					Messages.getString( "exception.base.orthogonal.inconsistent.count", //$NON-NLS-1$
							new Object[]{
									new Integer( iBaseCount ),
									new Integer( iOrthogonalCount )
							},
							rtc.getLocale( ) ) );
		}
		else
		{
			dpa = new DataPointHints[iBaseCount];
			final boolean bScatter = ( oaxBase.getScale( ).getType( ) != IConstants.TEXT && !oaxBase.isCategoryScale( ) );

			// OPTIMIZED PRE-FETCH FORMAT SPECIFIERS FOR ALL DATA POINTS
			final DataPoint dp = seOrthogonal.getDataPoint( );
			final EList el = dp.getComponents( );
			DataPointComponent dpc;
			DataPointComponentType dpct;
			FormatSpecifier fsBase = null, fsOrthogonal = null, fsSeries = null;
			for ( int i = 0; i < el.size( ); i++ )
			{
				dpc = (DataPointComponent) el.get( i );
				dpct = dpc.getType( );
				if ( dpct == DataPointComponentType.BASE_VALUE_LITERAL )
				{
					fsBase = dpc.getFormatSpecifier( );
					if ( fsBase == null ) // BACKUP
					{
						fsBase = sdBase.getFormatSpecifier( );
					}
				}
				else if ( dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL )
				{
					fsOrthogonal = dpc.getFormatSpecifier( );
					if ( fsOrthogonal == null ) // BACKUP
					{
						fsOrthogonal = sdOrthogonal.getFormatSpecifier( );
					}
				}
				else if ( dpct == DataPointComponentType.SERIES_VALUE_LITERAL )
				{
					fsSeries = dpc.getFormatSpecifier( );
				}
			}

			dsiDataBase.reset( );
			dsiDataOrthogonal.reset( );
			for ( int i = 0; i < iBaseCount; i++ )
			{
				oDataBase = dsiDataBase.next( );
				oDataOrthogonal = dsiDataOrthogonal.next( );

				if ( !bScatter )
				{
					if ( aax.areAxesSwapped( ) )
					{
						dY = daTickCoordinates[0] - dUnitSize * i;
						try
						{
							dX = getLocation( scOrthogonal, oDataOrthogonal );
						}
						catch ( IllegalArgumentException nvex )
						{
							dX = dOrthogonalZero;
						}
						catch ( ChartException dfex )
						{
							dX = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					}
					else
					{
						dX = daTickCoordinates[0] + dUnitSize * i;
						try
						{
							dY = getLocation( scOrthogonal, oDataOrthogonal );
						}
						catch ( IllegalArgumentException nvex )
						{
							dY = dOrthogonalZero;
						}
						catch ( ChartException dfex )
						{
							dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					}
				}
				else
				// SCATTER CHARTS (BASE AXIS != CATEGORY AXIS)
				{
					try
					{
						dX = getLocation( scBase, oDataBase );
					}
					catch ( IllegalArgumentException nvex )
					{
						dX = dBaseZero;
					}
					catch ( ChartException dfex )
					{
						dX = dBaseZero; // FOR CUSTOM DATA ELEMENTS
					}

					try
					{
						dY = getLocation( scOrthogonal, oDataOrthogonal );
					}
					catch ( IllegalArgumentException nvex )
					{
						dY = dOrthogonalZero; // MAP TO ZERO
					}
					catch ( ChartException dfex )
					{
						dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
					}

					if ( aax.areAxesSwapped( ) )
					{
						final double dTemp = dX;
						dX = dY;
						dY = dTemp;
					}
				}
				lo = LocationImpl.create( dX, dY );
				dLength = ( i < iTickCount - 1 ) ? daTickCoordinates[i + 1]
						- daTickCoordinates[i] : 0;

				dpa[i] = new DataPointHints( oDataBase,
						oDataOrthogonal,
						seOrthogonal.getSeriesIdentifier( ),
						seOrthogonal.getDataPoint( ),
						fsBase,
						fsOrthogonal,
						fsSeries,
						lo,
						dLength,
						rtc );
			}
		}
		return new SeriesRenderingHints( this,
				oaxBase.getAxisCoordinate( ),
				scOrthogonal.getStart( ),
				dOrthogonalZero,
				dSeriesThickness,
				daTickCoordinates,
				dpa,
				scBase,
				scOrthogonal,
				ssl,
				dsiDataBase,
				dsiDataOrthogonal );
	}

	/**
	 * 
	 * @return
	 */
	public final StackedSeriesLookup getStackedSeriesLookup( )
	{
		return ssl;
	}

}