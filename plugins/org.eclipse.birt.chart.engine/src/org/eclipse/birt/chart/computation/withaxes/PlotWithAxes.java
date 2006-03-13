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
import java.util.ResourceBundle;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

/**
 * PlotWithAxes
 */
public abstract class PlotWithAxes extends Methods
{

	static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/computation.withaxes" ); //$NON-NLS-1$

	protected int iDimension = TWO_D;

	protected double dSeriesThickness = 10;

	protected double dXAxisPlotSpacing = 0;

	protected double dYAxisPlotSpacing = 0;

	protected double dZAxisPlotSpacing = 0;

	protected ColorDefinition cdShadow = ColorDefinitionImpl.GREY( );

	protected LineAttributes laPlot = null;

	/**
	 * All axes defined in the model are maintained in a fast data structure
	 * containing additional rendering attributes
	 */
	protected AllAxes aax = null;

	/**
	 * A final internal reference to the model used in rendering computations
	 */
	protected ChartWithAxes cwa;

	/**
	 * An internal XServer implementation capable of obtaining text metrics,
	 * etc.
	 */
	protected IDisplayServer ids;

	/**
	 * The runtime context associated with chart generation
	 */
	protected RunTimeContext rtc;

	/**
	 * A computed plot area based on the block dimensions and the axis
	 * attributes and label values
	 */
	protected Bounds boPlotBackground = BoundsImpl.create( 0, 0, 100, 100 );

	/**
	 * Insets maintained as pixels equivalent of the points value specified in
	 * the model used here for fast computations
	 */
	protected Insets insCA = null;

	/**
	 * Ratio for converting a point to a pixel
	 */
	protected transient double dPointToPixel = 0;

	/**
	 * Converts to internal (non public-model) data structures
	 * 
	 * @param cd
	 * @return
	 */
	protected static final int getDimension( ChartDimension cd )
	{
		switch ( cd.getValue( ) )
		{
			case ChartDimension.TWO_DIMENSIONAL :
				return IConstants.TWO_D;
			case ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH :
				return IConstants.TWO_5_D;
			case ChartDimension.THREE_DIMENSIONAL :
				return IConstants.THREE_D;
		}
		return IConstants.UNDEFINED;
	}

	/**
	 * Converts to internal (non public-model) data structures
	 * 
	 * @param ax
	 * @return
	 */
	protected static final int getAxisType( Axis ax )
	{
		int iAxisType = UNDEFINED;
		final AxisType at = ax.getType( );
		switch ( at.getValue( ) )
		{
			case AxisType.LINEAR :
				iAxisType = NUMERICAL | LINEAR;
				break;
			case AxisType.LOGARITHMIC :
				iAxisType = NUMERICAL | LOGARITHMIC;
				break;
			case AxisType.TEXT :
				iAxisType = TEXT;
				break;
			case AxisType.DATE_TIME :
				iAxisType = DATE_TIME;
				break;
		}
		if ( ax.isPercent( ) )
		{
			iAxisType |= PERCENT;
		}
		return iAxisType;
	}

	/**
	 * Converts to internal (non public-model) data structures
	 * 
	 * @param ax
	 * @return
	 */
	protected static final int getTickStyle( Axis ax, int iMajorOrMinor )
	{
		int iTickStyle = TICK_NONE;

		org.eclipse.birt.chart.model.component.Grid gr = ( iMajorOrMinor == MAJOR ) ? ax.getMajorGrid( )
				: ax.getMinorGrid( );
		if ( !gr.isSetTickStyle( ) )
		{
			return iTickStyle;
		}
		final LineAttributes lia = gr.getTickAttributes( );
		if ( !lia.isSetStyle( )
				|| !lia.isSetThickness( )
				|| !lia.isSetVisible( )
				|| !lia.isVisible( ) )
		{
			return iTickStyle;
		}

		final TickStyle ts = gr.getTickStyle( );
		switch ( ts.getValue( ) )
		{
			case TickStyle.LEFT :
				iTickStyle = TICK_LEFT;
				break;
			case TickStyle.RIGHT :
				iTickStyle = TICK_RIGHT;
				break;
			case TickStyle.ABOVE :
				iTickStyle = TICK_ABOVE;
				break;
			case TickStyle.BELOW :
				iTickStyle = TICK_BELOW;
				break;
			case TickStyle.ACROSS :
				iTickStyle = TICK_ACROSS;
				break;
		}
		return iTickStyle;
	}

	/**
	 * Converts to internal (non public-model) data structures
	 * 
	 * @param ax
	 * @return
	 */
	protected static final IntersectionValue getIntersection( Axis ax )
	{
		IntersectionValue iv = null;
		AxisOrigin ao = ax.getOrigin( );
		if ( ao.getType( ) == IntersectionType.MAX_LITERAL )
		{
			iv = IntersectionValue.MAX_VALUE;
		}
		else if ( ao.getType( ) == IntersectionType.MIN_LITERAL )
		{
			iv = IntersectionValue.MIN_VALUE;
		}
		else
		{
			iv = new IntersectionValue( IntersectionValue.VALUE, ao.getValue( ) );
		}
		return iv;
	}

	/**
	 * @return
	 */
	public final int getDimension( )
	{
		return iDimension;
	}

	/**
	 * @return
	 */
	public final double getSeriesThickness( )
	{
		return dSeriesThickness;
	}

	/**
	 * @return
	 */
	public final double getHorizontalSpacingInPixels( )
	{
		return dXAxisPlotSpacing;
	}

	/**
	 * @return
	 */
	public final double getVerticalSpacingInPixels( )
	{
		return dYAxisPlotSpacing;
	}

	/**
	 * This method computes the entire chart within the given bounds. If the
	 * dataset has changed but none of the axis attributes have changed, simply
	 * re-compute without 'rebuilding axes'.
	 * 
	 * @param bo
	 * 
	 */
	public abstract void compute( Bounds bo ) throws ChartException,
			IllegalArgumentException;

	/**
	 * @param sdOrthogonal
	 * @param seOrthogonal
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	public abstract ISeriesRenderingHints getSeriesRenderingHints(
			SeriesDefinition sdOrthogonal, Series seOrthogonal )
			throws ChartException, IllegalArgumentException;

	/**
	 * @throws IllegalArgumentException
	 * @throws ChartException
	 */
	abstract void buildAxes( ) throws IllegalArgumentException, ChartException;

	/**
	 * 
	 * @return
	 */
	public final AllAxes getAxes( )
	{
		return aax;
	}

	/**
	 * @return
	 */
	public final RunTimeContext getRunTimeContext( )
	{
		return rtc;
	}

	/**
	 * 
	 * @return
	 */
	public final IDisplayServer getDisplayServer( )
	{
		return ids;
	}

	/**
	 * 
	 * @return The plot bounds in pixels
	 */
	public final Bounds getPlotBounds( )
	{
		return boPlotBackground;
	}

	/**
	 * 
	 * @return The plot insets in pixels
	 */
	public final Insets getPlotInsets( )
	{
		return insCA;
	}

	/**
	 * 
	 * @param se
	 * @return
	 */
	protected final OneAxis findOrthogonalAxis( Series se )
	{
		Axis[] axaBase = ( (ChartWithAxesImpl) cwa ).getBaseAxes( );
		Axis axPrimaryBase = axaBase[0];
		final Axis[] axaOrthogonal = ( (ChartWithAxesImpl) cwa ).getOrthogonalAxes( axPrimaryBase,
				true );
		Series[] sea;

		for ( int i = 0; i < axaOrthogonal.length; i++ )
		{
			sea = axaOrthogonal[i].getRuntimeSeries( );
			for ( int j = 0; j < sea.length; j++ )
			{
				if ( sea[j] == se )
				{
					if ( i == 0 ) // FIRST ONE IS ALWAYS THE PRIMARY
					// ORTHOGONAL
					{
						return aax.getPrimaryOrthogonal( );
					}
					return aax.getOverlay( i - 1 );
				}
			}
		}
		return null;
	}

	/**
	 * This method validates several crucial properties for an axis associated
	 * with a Chart
	 * 
	 * @param ax
	 *            The axis to validate
	 * @throws ValidationException
	 */
	protected void validateAxis( Axis ax ) throws ChartException
	{
		if ( !ax.isSetType( ) ) // AXIS TYPE UNDEFINED
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					new ChartException( ChartEnginePlugin.ID,
							ChartException.UNDEFINED_VALUE,
							"exception.undefined.axis.data.type", //$NON-NLS-1$
							new Object[]{
								ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) ) );
		}

		if ( !ax.getLabel( ).isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					new ChartException( ChartEnginePlugin.ID,
							ChartException.UNDEFINED_VALUE,
							"exception.undefined.axis.label.visibility", //$NON-NLS-1$
							new Object[]{
								ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) ) );
		}

		if ( !ax.getTitle( ).isSetVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					new ChartException( ChartEnginePlugin.ID,
							ChartException.UNDEFINED_VALUE,
							"exception.undefined.axis.title.visibility", //$NON-NLS-1$
							new Object[]{
								ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) ) );
		}

		if ( !ax.isSetLabelPosition( ) && ax.getLabel( ).isVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					new ChartException( ChartEnginePlugin.ID,
							ChartException.UNDEFINED_VALUE,
							"exception.undefined.axis.label.position", //$NON-NLS-1$ 
							new Object[]{
								ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) ) );
		}

		if ( !ax.isSetTitlePosition( ) && ax.getTitle( ).isVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					new ChartException( ChartEnginePlugin.ID,
							ChartException.UNDEFINED_VALUE,
							"exception.undefined.axis.title.position", //$NON-NLS-1$
							new Object[]{
								ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) ) );
		}

		LineAttributes liaTicks = ax.getMajorGrid( ).getTickAttributes( );
		if ( !ax.getMajorGrid( ).isSetTickStyle( ) && liaTicks.isVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					new ChartException( ChartEnginePlugin.ID,
							ChartException.UNDEFINED_VALUE,
							"exception.undefined.axis.major.tick.style", //$NON-NLS-1$
							new Object[]{
								ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) ) );
		}

		liaTicks = ax.getMinorGrid( ).getTickAttributes( );
		if ( !ax.getMinorGrid( ).isSetTickStyle( ) && liaTicks.isVisible( ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					new ChartException( ChartEnginePlugin.ID,
							ChartException.UNDEFINED_VALUE,
							"exception.undefined.axis.minor.tick.style", //$NON-NLS-1$
							new Object[]{
								ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) ) );
		}

		final int iOrientation = ax.getOrientation( ).getValue( );
		if ( iOrientation == Orientation.VERTICAL )
		{
			int iPosition = -1;
			if ( ax.getLabel( ).isVisible( ) ) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getLabelPosition( ).getValue( );
				if ( iPosition != Position.LEFT && iPosition != Position.RIGHT )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.VALIDATION,
							"exception.illegal.vaxis.label.position", //$NON-NLS-1$
							new Object[]{
									ax.getLabelPosition( ).getName( ), ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) );
				}
			}
			if ( ax.getTitle( ).isVisible( ) ) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getTitlePosition( ).getValue( );
				if ( iPosition != Position.LEFT && iPosition != Position.RIGHT )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.VALIDATION,
							"exception.illegal.vaxis.title.position", //$NON-NLS-1$
							new Object[]{
									ax.getLabelPosition( ).getName( ), ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) );
				}
			}

			int iTickStyle = ax.getMajorGrid( ).getTickStyle( ).getValue( );
			if ( iTickStyle != TickStyle.ACROSS
					&& iTickStyle != TickStyle.LEFT
					&& iTickStyle != TickStyle.RIGHT )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.VALIDATION,
						"exception.illegal.vaxis.major.tick.style", //$NON-NLS-1$
						new Object[]{
								ax.getMajorGrid( ).getTickStyle( ).getName( ),
								ax
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
			iTickStyle = ax.getMinorGrid( ).getTickStyle( ).getValue( );
			if ( iTickStyle != TickStyle.ACROSS
					&& iTickStyle != TickStyle.LEFT
					&& iTickStyle != TickStyle.RIGHT )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.VALIDATION,
						"exception.illegal.vaxis.minor.tick.style", //$NON-NLS-1$
						new Object[]{
								ax.getMinorGrid( ).getTickStyle( ).getName( ),
								ax
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
		}
		else if ( iOrientation == Orientation.HORIZONTAL )
		{
			int iPosition = -1;
			if ( ax.getLabel( ).isVisible( ) ) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getLabelPosition( ).getValue( );
				if ( iPosition != Position.ABOVE && iPosition != Position.BELOW )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.VALIDATION,
							"exception.illegal.haxis.label.position", //$NON-NLS-1$
							new Object[]{
									ax.getLabelPosition( ).getName( ), ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) );
				}
			}
			if ( ax.getTitle( ).isVisible( ) ) // LABEL POSITION (IF VISIBLE)
			{
				iPosition = ax.getTitlePosition( ).getValue( );
				if ( iPosition != Position.ABOVE && iPosition != Position.BELOW )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.VALIDATION,
							"exception.illegal.haxis.title.position", //$NON-NLS-1$ 
							new Object[]{
									ax.getTitlePosition( ).getName( ), ax
							},
							ResourceBundle.getBundle( Messages.ENGINE,
									rtc.getLocale( ) ) );
				}
			}

			int iTickStyle = ax.getMajorGrid( ).getTickStyle( ).getValue( );
			if ( iTickStyle != TickStyle.ACROSS
					&& iTickStyle != TickStyle.ABOVE
					&& iTickStyle != TickStyle.BELOW )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.VALIDATION,
						"exception.illegal.haxis.major.tick.style", //$NON-NLS-1$
						new Object[]{
								ax.getMajorGrid( ).getTickStyle( ).getName( ),
								ax
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
			iTickStyle = ax.getMinorGrid( ).getTickStyle( ).getValue( );
			if ( iTickStyle != TickStyle.ACROSS
					&& iTickStyle != TickStyle.ABOVE
					&& iTickStyle != TickStyle.BELOW )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.VALIDATION,
						"exception.illegal.haxis.minor.tick.style", //$NON-NLS-1$
						new Object[]{
								ax.getMinorGrid( ).getTickStyle( ).getName( ),
								ax
						},
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
		}
	}

	/**
	 * This method converts a generic text dataset to a typed dataset as
	 * expected by the renderer
	 * 
	 * @param ax
	 *            The model's axis for which a series will be queried
	 * @param iType
	 *            The renderer datatype associated with the axis
	 * @param iSeriesIndex
	 *            The series index for which the typed dataset is being built
	 * 
	 * @return
	 */
	protected final DataSetIterator getTypedDataSet( Axis ax, int iType,
			int iSeriesIndex ) throws ChartException, IllegalArgumentException
	{
		final Series[] sea = ax.getRuntimeSeries( );
		if ( sea.length == 0 ) // TBD: PULL FROM SAMPLE DATA
		{
			if ( ( iType & NUMERICAL ) == NUMERICAL )
			{
				return new DataSetIterator( new Double[]{
						new Double( 1 ), new Double( 2 )
				} );
			}
			else if ( ( iType & DATE_TIME ) == DATE_TIME )
			{
				return new DataSetIterator( new Calendar[]{
						new CDateTime( ), new CDateTime( )
				} );
			}
			else if ( ( iType & TEXT ) == TEXT )
			{
				return new DataSetIterator( new String[]{
						"Category1", "Category2", "Category3" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				} );
			}
		}
		return getTypedDataSet( sea[iSeriesIndex], iType );

	}

	/**
	 * @param se
	 * @param iType
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	protected final DataSetIterator getTypedDataSet( Series se, int iType )
			throws ChartException, IllegalArgumentException
	{
		DataSetIterator dsi = new DataSetIterator( se.getDataSet( ) );

		if ( ( dsi.getDataType( ) & TEXT ) == TEXT )
		{
			if ( ( iType & LINEAR ) == LINEAR
					|| ( iType & LOGARITHMIC ) == LOGARITHMIC
					|| ( iType & DATE_TIME ) == DATE_TIME )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.text.data.numerical.datetime.axis", //$NON-NLS-1$ 
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
		}

		if ( ( dsi.getDataType( ) & NUMERICAL ) == NUMERICAL )
		{
			if ( ( iType & DATE_TIME ) == DATE_TIME )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.numerical.data.datetime.axis", //$NON-NLS-1$ 
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
		}

		if ( ( dsi.getDataType( ) & DATE_TIME ) == DATE_TIME )
		{
			if ( ( iType & LINEAR ) == LINEAR
					|| ( iType & LOGARITHMIC ) == LOGARITHMIC )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_SET,
						"exception.datetime.data.numerical.axis", //$NON-NLS-1$ 
						ResourceBundle.getBundle( Messages.ENGINE,
								rtc.getLocale( ) ) );
			}
		}

		return dsi;
	}

	/**
	 * Converts to internal (non public-model) data structures and transposes
	 * value if needed
	 * 
	 * @param iBaseOrOrthogonal
	 * @return
	 */
	protected final int getOrientation( int iBaseOrOrthogonal )
	{
		if ( !cwa.isTransposed( ) )
		{
			return ( iBaseOrOrthogonal == IConstants.BASE || iBaseOrOrthogonal == IConstants.ANCILLARY_BASE ) ? IConstants.HORIZONTAL
					: IConstants.VERTICAL;
		}
		else
		{
			return ( iBaseOrOrthogonal == IConstants.BASE || iBaseOrOrthogonal == IConstants.ANCILLARY_BASE ) ? IConstants.VERTICAL
					: IConstants.HORIZONTAL;
		}
	}

	/**
	 * Transposes the anchor for a given source orientation
	 * 
	 * @param or
	 * @param an
	 * 
	 * @return
	 */
	public final Anchor transposedAnchor( Orientation or, Anchor an )
			throws IllegalArgumentException
	{
		if ( an == null )
		{
			return null; // CENTERED ANCHOR
		}

		final int iOrientation = or.getValue( );
		if ( iOrientation == Orientation.HORIZONTAL )
		{
			switch ( an.getValue( ) )
			{
				case Anchor.NORTH :
					return Anchor.WEST_LITERAL;
				case Anchor.SOUTH :
					return Anchor.EAST_LITERAL;
				case Anchor.EAST :
					return Anchor.NORTH_LITERAL;
				case Anchor.WEST :
					return Anchor.SOUTH_LITERAL;
				case Anchor.NORTH_WEST :
					return Anchor.SOUTH_WEST_LITERAL;
				case Anchor.NORTH_EAST :
					return Anchor.NORTH_WEST_LITERAL;
				case Anchor.SOUTH_WEST :
					return Anchor.SOUTH_EAST_LITERAL;
				case Anchor.SOUTH_EAST :
					return Anchor.NORTH_EAST_LITERAL;
			}
		}
		else if ( iOrientation == Orientation.VERTICAL )
		{
			switch ( an.getValue( ) )
			{
				case Anchor.NORTH :
					return Anchor.EAST_LITERAL;
				case Anchor.SOUTH :
					return Anchor.WEST_LITERAL;
				case Anchor.EAST :
					return Anchor.SOUTH_LITERAL;
				case Anchor.WEST :
					return Anchor.NORTH_LITERAL;
				case Anchor.NORTH_WEST :
					return Anchor.NORTH_EAST_LITERAL;
				case Anchor.NORTH_EAST :
					return Anchor.SOUTH_EAST_LITERAL;
				case Anchor.SOUTH_WEST :
					return Anchor.NORTH_WEST_LITERAL;
				case Anchor.SOUTH_EAST :
					return Anchor.SOUTH_WEST_LITERAL;
			}
		}
		throw new IllegalArgumentException( MessageFormat.format( ResourceBundle.getBundle( Messages.ENGINE,
				rtc.getLocale( ) )
				.getString( "exception.anchor.transpose" ), //$NON-NLS-1$ 
				new Object[]{
						an, or
				} )

		);
	}

	/**
	 * Returns a transpose of the original angle
	 * 
	 * @param dOriginalAngle
	 * @return
	 * @throws UnexpectedInputException
	 */
	public final double getTransposedAngle( double dOriginalAngle )
			throws IllegalArgumentException
	{
		if ( dOriginalAngle >= 0 && dOriginalAngle <= 90 )
		{
			return -( 90 - dOriginalAngle );
		}
		else if ( dOriginalAngle < 0 && dOriginalAngle >= -90 )
		{
			return ( dOriginalAngle + 90 );
		}
		throw new IllegalArgumentException( MessageFormat.format( ResourceBundle.getBundle( Messages.ENGINE,
				rtc.getLocale( ) )
				.getString( "exception.angle.range.transpose" ), //$NON-NLS-1$
				new Object[]{
					new Double( dOriginalAngle )
				} ) );
	}

	/**
	 * Returns a transposed or the original label position as requested
	 * depending on the plot's orientation
	 * 
	 * @param iBaseOrOrthogonal
	 * @param iOriginalPosition
	 * @return
	 * @throws UnexpectedInputException
	 */
	public final int transposeLabelPosition( int iBaseOrOrthogonal,
			int iOriginalPosition ) throws IllegalArgumentException
	{
		if ( !cwa.isTransposed( ) )
		{
			return iOriginalPosition;
		}
		if ( iBaseOrOrthogonal == IConstants.BASE )
		{
			switch ( iOriginalPosition )
			{
				case IConstants.ABOVE :
					return IConstants.RIGHT;
				case IConstants.BELOW :
					return IConstants.LEFT;
				case IConstants.OUTSIDE :
				case IConstants.INSIDE :
					return iOriginalPosition;
			}
		}
		else if ( iBaseOrOrthogonal == IConstants.ORTHOGONAL )
		{
			switch ( iOriginalPosition )
			{
				case IConstants.ABOVE :
					return IConstants.RIGHT;
				case IConstants.BELOW :
					return IConstants.LEFT;
				case IConstants.LEFT :
					return IConstants.BELOW;
				case IConstants.RIGHT :
					return IConstants.ABOVE;
				case IConstants.OUTSIDE :
				case IConstants.INSIDE :
					return iOriginalPosition;
			}
		}
		throw new IllegalArgumentException( MessageFormat.format( ResourceBundle.getBundle( Messages.ENGINE,
				rtc.getLocale( ) )
				.getString( "exception.combination.axis.label.position" ), //$NON-NLS-1$
				new Object[]{
						new Integer( iBaseOrOrthogonal ),
						new Integer( iOriginalPosition )
				} ) );
	}

	/**
	 * Returns a transposed or the original tick style as requested depending on
	 * the plot's orientation
	 * 
	 * @param iBaseOrOrthogonal
	 * @param iOriginalStyle
	 * @return
	 * @throws UnexpectedInputException
	 */
	protected final int transposeTickStyle( int iBaseOrOrthogonal,
			int iOriginalStyle ) throws IllegalArgumentException
	{
		if ( !cwa.isTransposed( )
				|| iOriginalStyle == IConstants.TICK_ACROSS
				|| iOriginalStyle == IConstants.TICK_NONE )
		{
			return iOriginalStyle;
		}

		if ( iBaseOrOrthogonal == IConstants.BASE )
		{
			switch ( iOriginalStyle )
			{
				case IConstants.TICK_BELOW :
					return IConstants.TICK_LEFT;
				case IConstants.TICK_ABOVE :
					return IConstants.TICK_RIGHT;
			}
		}
		else if ( iBaseOrOrthogonal == IConstants.ORTHOGONAL )
		{
			switch ( iOriginalStyle )
			{
				case IConstants.TICK_LEFT :
					return IConstants.TICK_BELOW;
				case IConstants.TICK_RIGHT :
					return IConstants.TICK_ABOVE;
			}
		}
		throw new IllegalArgumentException( MessageFormat.format( ResourceBundle.getBundle( Messages.ENGINE,
				rtc.getLocale( ) )
				.getString( "exception.combination.axis.tick.style" ), //$NON-NLS-1$
				new Object[]{
						new Integer( iBaseOrOrthogonal ),
						new Integer( iOriginalStyle )
				} ) );
	}

	/**
	 * Goals: 1. Adjust the two ends of the vertical axis to fit start/end
	 * labels 2. Compute the horizontal co-ordinate for the axis
	 * 
	 * @param dBlockX
	 * @param dBlockWidth
	 * @param aax
	 * 
	 * @return
	 */
	protected final double adjustHorizontal( double dBlockX,
			double dBlockWidth, AllAxes aax ) throws ChartException,
			IllegalArgumentException
	{
		final OneAxis axPH = aax.areAxesSwapped( ) ? aax.getPrimaryOrthogonal( )
				: aax.getPrimaryBase( );
		final OneAxis axPV = aax.areAxesSwapped( ) ? aax.getPrimaryBase( )
				: aax.getPrimaryOrthogonal( );
		final AutoScale scX = axPH.getScale( );
		final AutoScale scY = axPV.getScale( );
		final int iXLabelLocation = axPH.getLabelPosition( );
		final int iYLabelLocation = axPV.getLabelPosition( );

		final int iYTitleLocation = axPV.getTitlePosition( );
		final Label laXAxisLabels = axPH.getLabel( );
		final Label laYAxisLabels = axPV.getLabel( );
		final Label laYAxisTitle = axPV.getTitle( );
		final int iYTickStyle = axPV.getCombinedTickStyle( );
		final IntersectionValue iv = axPV.getIntersectionValue( );

		// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
		// AXIS-PLOT SPACING
		final boolean bTicksLeft = ( iYTickStyle & TICK_LEFT ) == TICK_LEFT;
		final boolean bTicksRight = ( iYTickStyle & TICK_RIGHT ) == TICK_RIGHT;
		final double dAppliedYAxisPlotSpacing = ( iv.iType == IntersectionValue.MAX || iv.iType == IntersectionValue.MIN ) ? dYAxisPlotSpacing
				: 0;

		// UPDATE Y-AXIS ENDPOINTS DUE TO AXIS LABEL SHIFTS
		double dStart = scY.getStart( ), dEnd = scY.getEnd( );
		scY.computeTicks( ids,
				laYAxisLabels,
				iYLabelLocation,
				VERTICAL,
				dStart,
				dEnd,
				true,
				aax );
		if ( !scY.isStepFixed( ) )
		{
			final Object[] oaMinMax = scY.getMinMax( );
			while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
			{
				if ( !scY.zoomOut( ) )
				{
					break;
				}
				scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
				int tickCount = scY.computeTicks( ids,
						laYAxisLabels,
						iYLabelLocation,
						VERTICAL,
						dStart,
						dEnd,
						true,
						aax );
				if ( scY.getUnit( ) != null
						&& asInteger( scY.getUnit( ) ) == Calendar.YEAR
						&& tickCount <= 3 )
				{
					break;
				}
			}
		}

		double dYAxisLabelsThickness = scY.computeAxisLabelThickness( ids,
				axPV.getLabel( ),
				VERTICAL );
		double dYAxisTitleThickness = 0;
		if ( laYAxisTitle.isVisible( ) )
		{
			final String sPreviousValue = laYAxisTitle.getCaption( ).getValue( );
			laYAxisTitle.getCaption( )
					.setValue( rtc.externalizedMessage( sPreviousValue ) );
			try
			{
				dYAxisTitleThickness = computeBox( ids,
						iYTitleLocation,
						laYAxisTitle,
						0,
						0 ).getWidth( );
			}
			catch ( IllegalArgumentException uiex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						uiex );
			}
			finally
			{
				laYAxisTitle.getCaption( ).setValue( sPreviousValue );
			}
		}
		double dX = getLocation( scX, iv ), dX1 = dX, dX2 = dX;

		// COMPUTE VALUES FOR x1, x, x2
		// x = HORIZONTAL LOCATION OF Y-AXIS ALONG PLOT
		// x1 = LEFT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
		// SPACING)
		// x2 = RIGHT EDGE OF Y-AXIS BAND (DUE TO AXIS LABELS, TITLE, TICKS &
		// SPACING)
		if ( iv.iType == IntersectionValue.MIN )
		{
			if ( scX.getDirection( ) == BACKWARD )
			{
				// switch if scale is backward.
				dX = getLocation( scX, IntersectionValue.MAX_VALUE );
			}

			dX -= dAppliedYAxisPlotSpacing;
			dX1 = dX;
			dX2 = dX;

			if ( bTicksLeft )
			{
				dX1 -= TICK_SIZE;
			}
			if ( iYLabelLocation == LEFT )
			{
				dX1 -= dYAxisLabelsThickness;
				dX2 += Math.max( // IF LABELS ARE LEFT, THEN RIGHT SPACING IS
				// MAX(RT_TICK_SIZE, HORZ_SPACING)
				bTicksRight ? TICK_SIZE : 0,
						dAppliedYAxisPlotSpacing );
			}
			else if ( iYLabelLocation == RIGHT )
			{
				// IF LABELS ARE RIGHT, THEN RIGHT SPACING IS
				// MAX(RT_TICK_SIZE+AXIS_LBL_THCKNESS, HORZ_SPACING)
				dX2 += Math.max( ( bTicksRight ? TICK_SIZE : 0 )
						+ dYAxisLabelsThickness, dAppliedYAxisPlotSpacing );
			}

			if ( iYTitleLocation == LEFT )
			{
				dX1 -= dYAxisTitleThickness;
			}
			else if ( iYTitleLocation == RIGHT )
			{
				dX2 += dYAxisTitleThickness;
			}

			// ENSURE THAT WE DON'T GO BEHIND THE LEFT PLOT BLOCK EDGE
			if ( dX1 < dBlockX )
			{
				final double dDelta = ( dBlockX - dX1 );
				dX1 = dBlockX;
				dX += dDelta;
				dX2 += dDelta;
			}
			final double dDeltaX1 = dX - dX1;
			final double dDeltaX2 = dX2 - dX;

			// COMPUTE THE Y-AXIS BAND THICKNESS AND ADJUST X2 FOR LABELS BELOW
			if ( iYLabelLocation == RIGHT )
			{
				// Y-AXIS BAND IS (x1 -> (x+AxisPlotSpacing))
				dX2 = ( dX + dAppliedYAxisPlotSpacing );
			}
			dYAxisLabelsThickness = dX2 - dX1; // REUSE VARIABLE

			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// UPPER END
			scX.computeAxisStartEndShifts( ids,
					laXAxisLabels,
					HORIZONTAL,
					iXLabelLocation,
					aax );

			if ( scX.getDirection( ) == BACKWARD )
			{
				if ( dYAxisLabelsThickness > scX.getEndShift( ) )
				{
					// REDUCE scX's STARTPOINT TO FIT THE Y-AXIS ON THE LEFT
					dEnd = dX2 - scX.getEndShift( );
				}
				else
				{
					dEnd = scX.getEnd( );
				}
				dStart = scX.getStart( );
			}
			else
			{
				if ( dYAxisLabelsThickness > scX.getStartShift( ) )
				{
					// REDUCE scX's STARTPOINT TO FIT THE Y-AXIS ON THE LEFT
					dStart = dX2 - scX.getStartShift( );
				}
				else
				{
					dStart = scX.getStart( );
				}
				dEnd = scX.getEnd( );
			}
			scX.resetShifts( );

			// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS IF
			// OVERLAPS OCCUR
			scX.setEndPoints( dStart, dEnd );
			scX.computeTicks( ids,
					laXAxisLabels,
					iXLabelLocation,
					HORIZONTAL,
					dStart,
					dEnd,
					true,
					aax );

			if ( !scX.isStepFixed( ) )
			{
				final Object[] oaMinMax = scX.getMinMax( );

				while ( !scX.checkFit( ids, laXAxisLabels, iXLabelLocation ) )
				{
					if ( !scX.zoomOut( ) )
					{
						break;
					}
					scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
					int tickCount = scX.computeTicks( ids,
							laXAxisLabels,
							iXLabelLocation,
							HORIZONTAL,
							dStart,
							dEnd,
							true,
							aax );
					if ( scX.getUnit( ) != null
							&& asInteger( scX.getUnit( ) ) == Calendar.YEAR
							&& tickCount <= 3 )
					{
						break;
					}
				}
			}

			// MOVE THE Y-AXIS TO THE LEFT EDGE OF THE PLOT IF SLACK SPACE
			// EXISTS
			if ( scX.getDirection( ) == BACKWARD )
			{
				if ( dYAxisLabelsThickness < scX.getEndShift( ) )
				{
					dX = scX.getEnd( ) - ( dX2 - dX );
				}
			}
			else
			{
				if ( dYAxisLabelsThickness < scX.getStartShift( ) )
				{
					dX = scX.getStart( ) - ( dX2 - dX );
				}
			}

			dX -= insCA.getLeft( );
			dX2 = dX + dDeltaX2;
			dX1 = dX - dDeltaX1;

			axPV.setTitleCoordinate( ( iYTitleLocation == LEFT ) ? dX1 - 1
					: dX2 + 1 - dYAxisTitleThickness );

		}
		else if ( iv.iType == IntersectionValue.MAX )
		{
			if ( scX.getDirection( ) == BACKWARD )
			{
				// switch if scale is backward.
				dX = getLocation( scX, IntersectionValue.MIN_VALUE );
			}

			dX += dAppliedYAxisPlotSpacing;
			dX1 = dX;
			dX2 = dX;
			if ( bTicksRight )
			{
				dX2 += TICK_SIZE;
			}

			if ( iYLabelLocation == RIGHT )
			{
				dX2 += dYAxisLabelsThickness;
				dX1 -= Math.max( bTicksLeft ? TICK_SIZE : 0,
						dAppliedYAxisPlotSpacing );
			}
			else if ( iYLabelLocation == LEFT )
			{
				dX1 -= Math.max( ( bTicksLeft ? TICK_SIZE : 0 )
						+ dYAxisLabelsThickness, dAppliedYAxisPlotSpacing );
			}
			if ( iYTitleLocation == RIGHT )
			{
				dX2 += dYAxisTitleThickness;
			}
			else if ( iYTitleLocation == LEFT )
			{
				dX1 -= dYAxisTitleThickness;
			}

			// ENSURE THAT WE DON'T GO AHEAD OF THE RIGHT PLOT BLOCK EDGE
			if ( dX2 > dBlockX + dBlockWidth )
			{
				final double dDelta = dX2 - ( dBlockX + dBlockWidth );
				dX2 = dBlockX + dBlockWidth;
				dX -= dDelta;
				dX1 -= dDelta;
			}
			final double dDeltaX1 = dX - dX1;
			final double dDeltaX2 = dX2 - dX;

			// COMPUTE THE Y-AXIS BAND THICKNESS AND ADJUST X1 IF Y-AXIS LABELS
			// ARE ON THE LEFT
			if ( iYLabelLocation == LEFT )
			{
				// Y-AXIS BAND IS ((x-AxisPlotSpacing) -> x2)
				dX1 = ( dX - dAppliedYAxisPlotSpacing );
			}
			dYAxisLabelsThickness = dX2 - dX1; // REUSE VARIABLE

			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// UPPER END
			scX.computeAxisStartEndShifts( ids,
					laXAxisLabels,
					HORIZONTAL,
					iXLabelLocation,
					aax );

			if ( scX.getDirection( ) == BACKWARD )
			{
				if ( dYAxisLabelsThickness > scX.getStartShift( ) )
				{
					// REDUCE scX's ENDPOINT TO FIT THE Y-AXIS ON THE RIGHT
					dStart = dX1 + scX.getStartShift( );
				}
				else
				{
					dStart = scX.getStart( );
				}
				dEnd = scX.getEnd( );
			}
			else
			{
				if ( dYAxisLabelsThickness > scX.getEndShift( ) )
				{
					// REDUCE scX's ENDPOINT TO FIT THE Y-AXIS ON THE RIGHT
					dEnd = dX1 + scX.getEndShift( );
				}
				else
				{
					dEnd = scX.getEnd( );
				}
				dStart = scX.getStart( );
			}

			scX.resetShifts( );

			// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS IF
			// OVERLAPS OCCUR
			scX.setEndPoints( dStart, dEnd );
			scX.computeTicks( ids,
					laXAxisLabels,
					iXLabelLocation,
					HORIZONTAL,
					dStart,
					dEnd,
					true,
					aax );
			if ( !scX.isStepFixed( ) )
			{
				final Object[] oaMinMax = scX.getMinMax( );
				while ( !scX.checkFit( ids, laXAxisLabels, iXLabelLocation ) )
				{
					if ( !scX.zoomOut( ) )
					{
						break;
					}
					scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
					int tickCount = scX.computeTicks( ids,
							laXAxisLabels,
							iXLabelLocation,
							HORIZONTAL,
							dStart,
							dEnd,
							true,
							aax );
					if ( scX.getUnit( ) != null
							&& asInteger( scX.getUnit( ) ) == Calendar.YEAR
							&& tickCount <= 3 )
					{
						break;
					}
				}
			}

			// MOVE THE Y-AXIS TO THE LEFT EDGE OF THE PLOT IF SLACK SPACE
			// EXISTS
			if ( scX.getDirection( ) == BACKWARD )
			{
				if ( dYAxisLabelsThickness < scX.getStartShift( ) )
				{
					dX = scX.getStart( ) - ( dX1 - dX );
				}
			}
			else
			{
				if ( dYAxisLabelsThickness < scX.getEndShift( ) )
				{
					dX = scX.getEnd( ) - ( dX1 - dX );
				}
			}
			dX += insCA.getRight( );
			dX2 = dX + dDeltaX2;
			dX1 = dX - dDeltaX1;

			axPV.setTitleCoordinate( ( iYTitleLocation == LEFT ) ? dX1 - 1
					: dX2 + 1 - dYAxisTitleThickness );

		}
		else
		{
			double dDeltaX1 = 0, dDeltaX2 = 0;
			if ( iYTitleLocation == RIGHT )
			{
				dX2 += dYAxisTitleThickness;
			}
			else if ( iYTitleLocation == LEFT )
			{
				dX1 -= dYAxisTitleThickness;
			}

			if ( iYLabelLocation == LEFT )
			{
				dX1 -= ( bTicksLeft ? TICK_SIZE : 0 ) + dYAxisLabelsThickness;
				dX2 += ( bTicksRight ? TICK_SIZE : 0 );
				dDeltaX1 = dX - dX1;
				dDeltaX2 = dX2 - dX;

				// CHECK IF LEFT EDGE OF Y-AXIS BAND GOES BEHIND THE PLOT LEFT
				// EDGE
				if ( dX1 < dBlockX )
				{
					final Object[] oaMinMax = scX.getMinMax( );
					boolean bForceBreak = false;

					// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE LEFT EDGE
					// OF THE Y-AXIS LABELS WITH THE LEFT EDGE OF THE PLOT
					// AND/OR
					// ENSURE THAT THE START POINT OF THE X-AXIS SCALE IS
					// SUITABLY POSITIONED

					do
					{
						// CANCEL OUT THE ENDPOINT LABEL SHIFT COMPUTATIONS FROM
						// THE X-AXIS
						scX.setEndPoints( scX.getStart( ) - scX.getStartShift( ),
								scX.getEnd( ) + scX.getEndShift( ) ); // RESTORE
						scX.resetShifts( );

						// APPLY THE AXIS REDUCTION FORMULA W.R.T. X-AXIS
						// STARTPOINT
						double[] da = scX.getEndPoints( );
						double dT_RI = dBlockX - dX1; // THRESHOLD -
						// REQUESTEDINTERSECTION
						double dAMin_AMax = da[1] - da[0];
						double dAMax_RI = da[1] - dX;
						double dDelta = ( dT_RI / dAMax_RI ) * dAMin_AMax;
						dStart = da[0] + dDelta;
						dEnd = da[1];

						if ( dStart < dBlockX )
						{
							dStart = dBlockX;
							bForceBreak = true; // ADJUST THE TOP EDGE OF THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}

						// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
						// LABELS IF OVERLAPS OCCUR
						scX.setEndPoints( dStart, dEnd );
						scX.computeTicks( ids,
								laXAxisLabels,
								iXLabelLocation,
								HORIZONTAL,
								dStart,
								dEnd,
								true,
								aax );
						while ( !scX.checkFit( ids,
								laXAxisLabels,
								iXLabelLocation ) )
						{
							if ( !scX.zoomOut( ) )
							{
								bForceBreak = true;
								break;
							}
							scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
							int tickCount = scX.computeTicks( ids,
									laXAxisLabels,
									iXLabelLocation,
									HORIZONTAL,
									dStart,
									dEnd,
									true,
									aax );
							if ( scX.getUnit( ) != null
									&& asInteger( scX.getUnit( ) ) == Calendar.YEAR
									&& tickCount <= 3 )
							{
								bForceBreak = true;
								break;
							}
						}

						dX = getLocation( scX, iv );
						dX1 = dX - dDeltaX1; // RE-CALCULATE X-AXIS BAND LEFT
						// EDGE
					} while ( Math.abs( dX1 - dBlockX ) > 1 && !bForceBreak );
				}
				else
				{
					// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
					// LABELS IF OVERLAPS OCCUR
					dStart = scX.getStart( );
					dEnd = scX.getEnd( );
					scX.setEndPoints( dStart, dEnd );
					scX.computeTicks( ids,
							laXAxisLabels,
							iXLabelLocation,
							HORIZONTAL,
							dStart,
							dEnd,
							true,
							aax );
					if ( !scX.isStepFixed( ) )
					{
						final Object[] oaMinMax = scX.getMinMax( );
						while ( !scX.checkFit( ids,
								laXAxisLabels,
								iXLabelLocation ) )
						{
							if ( !scX.zoomOut( ) )
							{
								break;
							}
							scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
							int tickCount = scX.computeTicks( ids,
									laXAxisLabels,
									iXLabelLocation,
									HORIZONTAL,
									dStart,
									dEnd,
									true,
									aax );
							if ( scX.getUnit( ) != null
									&& asInteger( scX.getUnit( ) ) == Calendar.YEAR
									&& tickCount <= 3 )
							{
								break;
							}
						}
					}
					dX = getLocation( scX, iv );
				}
				dX1 = dX - dDeltaX1;
				dX2 = dX + dDeltaX2;
			}
			else if ( iYLabelLocation == RIGHT )
			{
				dX2 += ( bTicksRight ? TICK_SIZE : 0 ) + dYAxisLabelsThickness;
				dX1 -= ( bTicksLeft ? TICK_SIZE : 0 );
				dDeltaX1 = dX - dX1;
				dDeltaX2 = dX2 - dX;

				// CHECK IF RIGHT EDGE OF Y-AXIS BAND GOES BEHIND THE PLOT RIGHT
				// EDGE
				if ( dX2 > dBlockX + dBlockWidth )
				{
					final Object[] oaMinMax = scX.getMinMax( );
					boolean bForceBreak = false;

					// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE RIGHT EDGE
					// OF THE Y-AXIS LABELS WITH THE RIGHT EDGE OF THE PLOT
					// AND/OR
					// ENSURE THAT THE START POINT OF THE X-AXIS SCALE IS
					// SUITABLY POSITIONED

					do
					{
						// CANCEL OUT THE ENDPOINT LABEL SHIFT COMPUTATIONS FROM
						// THE X-AXIS
						scX.setEndPoints( scX.getStart( ) - scX.getStartShift( ),
								scX.getEnd( ) + scX.getEndShift( ) ); // RESTORE
						scX.resetShifts( );

						// APPLY THE AXIS REDUCTION FORMULA W.R.T. X-AXIS
						// ENDPOINT
						double[] da = scX.getEndPoints( );
						double dT_RI = dX2 - ( dBlockX + dBlockWidth ); // THRESHOLD
						// -
						// REQUESTEDINTERSECTION
						double dAMin_AMax = da[1] - da[0];
						double dAMin_RI = dX - da[0];
						double dDelta = ( dT_RI / dAMin_RI ) * dAMin_AMax;
						dEnd = da[1] - dDelta;
						dStart = da[0];

						if ( dEnd > dBlockX + dBlockWidth )
						{
							dEnd = dBlockX + dBlockWidth;
							bForceBreak = true; // ADJUST THE TOP EDGE OF THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}

						// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
						// LABELS IF OVERLAPS OCCUR
						scX.setEndPoints( dStart, dEnd );
						scX.computeTicks( ids,
								laXAxisLabels,
								iXLabelLocation,
								HORIZONTAL,
								dStart,
								dEnd,
								true,
								aax );
						if ( !scX.isStepFixed( ) )
						{
							while ( !scX.checkFit( ids,
									laXAxisLabels,
									iXLabelLocation ) )
							{
								if ( !scX.zoomOut( ) )
								{
									bForceBreak = true;
									break;
								}
								scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
								int tickCount = scX.computeTicks( ids,
										laXAxisLabels,
										iXLabelLocation,
										HORIZONTAL,
										dStart,
										dEnd,
										true,
										aax );
								if ( scX.getUnit( ) != null
										&& asInteger( scX.getUnit( ) ) == Calendar.YEAR
										&& tickCount <= 3 )
								{
									bForceBreak = true;
									break;
								}
							}
						}
						dX = getLocation( scX, iv );
						dX2 = dX + dDeltaX2; // RE-CALCULATE X-AXIS BAND
						// RIGHT
						// EDGE
					} while ( Math.abs( dX2 - ( dBlockX + dBlockWidth ) ) > 1
							&& !bForceBreak );
				}
				else
				{
					// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
					// LABELS IF OVERLAPS OCCUR
					dStart = scX.getStart( );
					dEnd = scX.getEnd( );
					scX.setEndPoints( dStart, dEnd );
					scX.computeTicks( ids,
							laXAxisLabels,
							iXLabelLocation,
							HORIZONTAL,
							dStart,
							dEnd,
							true,
							aax );
					if ( !scX.isStepFixed( ) )
					{
						final Object[] oaMinMax = scX.getMinMax( );
						while ( !scX.checkFit( ids,
								laXAxisLabels,
								iXLabelLocation ) )
						{
							if ( !scX.zoomOut( ) )
							{
								break;
							}
							scX.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
							int tickCount = scX.computeTicks( ids,
									laXAxisLabels,
									iXLabelLocation,
									HORIZONTAL,
									dStart,
									dEnd,
									true,
									aax );
							if ( scX.getUnit( ) != null
									&& asInteger( scX.getUnit( ) ) == Calendar.YEAR
									&& tickCount <= 3 )
							{
								break;
							}
						}
					}
					dX = getLocation( scX, iv );
				}
				dX2 = dX + dDeltaX2;
				dX1 = dX - dDeltaX1;
			}
			axPV.setTitleCoordinate( ( iYTitleLocation == LEFT ) ? dX1 - 1
					: dX2 + 1 - dYAxisTitleThickness );
		}
		return dX;
	}

	/**
	 * 
	 * @param dBlockY
	 * @param dBlockHeight
	 * @param aax
	 * 
	 * @return
	 */
	protected final double adjustVerticalDueToHorizontal( double dBlockY,
			double dBlockHeight, AllAxes aax ) throws ChartException,
			IllegalArgumentException
	{
		final OneAxis axPH = aax.areAxesSwapped( ) ? aax.getPrimaryOrthogonal( )
				: aax.getPrimaryBase( );
		final OneAxis axPV = aax.areAxesSwapped( ) ? aax.getPrimaryBase( )
				: aax.getPrimaryOrthogonal( );
		final AutoScale scX = axPH.getScale( );
		final AutoScale scY = axPV.getScale( );
		final int iXLabelLocation = axPH.getLabelPosition( );
		final int iYLabelLocation = axPV.getLabelPosition( );
		final int iXTitleLocation = axPH.getTitlePosition( );

		final Label laXAxisTitle = axPH.getTitle( );
		final Label laYAxisLabels = axPV.getLabel( );
		final int iXTickStyle = axPH.getCombinedTickStyle( );
		final IntersectionValue iv = axPH.getIntersectionValue( );

		// COMPUTE THE THICKNESS OF THE AXIS INCLUDING AXIS LABEL BOUNDS AND
		// AXIS-PLOT SPACING
		double dXAxisLabelsThickness = scX.computeAxisLabelThickness( ids,
				axPH.getLabel( ),
				HORIZONTAL );
		double dXAxisTitleThickness = 0;
		if ( laXAxisTitle.isVisible( ) )
		{
			final String sPreviousValue = laXAxisTitle.getCaption( ).getValue( );
			laXAxisTitle.getCaption( )
					.setValue( rtc.externalizedMessage( sPreviousValue ) ); // EXTERNALIZE
			try
			{
				dXAxisTitleThickness = computeBox( ids,
						iXTitleLocation,
						laXAxisTitle,
						0,
						0 ).getHeight( );
			}
			catch ( IllegalArgumentException uiex )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.GENERATION,
						uiex );
			}
			finally
			{
				laXAxisTitle.getCaption( ).setValue( sPreviousValue ); // RESTORE
			}
		}

		double dY = getLocation( scY, iv ), dY1 = dY, dY2 = dY;
		final boolean bTicksAbove = ( iXTickStyle & TICK_ABOVE ) == TICK_ABOVE;
		final boolean bTicksBelow = ( iXTickStyle & TICK_BELOW ) == TICK_BELOW;
		final double dAppliedXAxisPlotSpacing = ( iv.iType == IntersectionValue.MAX || iv.iType == IntersectionValue.MIN ) ? dXAxisPlotSpacing
				: 0;
		final boolean bForwardScale = scY.getDirection( ) == FORWARD;

		// COMPUTE VALUES FOR y1, y, y2
		// y = VERTICAL LOCATION OF X-AXIS ALONG PLOT
		// y1 = UPPER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
		// y2 = LOWER EDGE OF X-AXIS (DUE TO AXIS LABELS, TICKS, SPACING)
		if ( ( bForwardScale && iv.iType == IntersectionValue.MIN )
				|| ( !bForwardScale && iv.iType == IntersectionValue.MAX ) )
		{
			// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MIN'

			dY -= dAppliedXAxisPlotSpacing;
			dY1 = dY;
			dY2 = dY;
			if ( bTicksAbove )
			{
				dY1 -= TICK_SIZE;
			}
			if ( iXLabelLocation == ABOVE )
			{
				dY1 -= dXAxisLabelsThickness;
				dY2 += Math.max( bTicksBelow ? TICK_SIZE : 0,
						dAppliedXAxisPlotSpacing );
			}
			else if ( iXLabelLocation == BELOW )
			{
				dY2 += Math.max( ( bTicksBelow ? TICK_SIZE : 0 )
						+ dXAxisLabelsThickness, dAppliedXAxisPlotSpacing );
			}

			if ( iXTitleLocation == ABOVE )
			{
				dY1 -= dXAxisTitleThickness;
			}
			else if ( iXTitleLocation == BELOW )
			{
				dY2 += dXAxisTitleThickness;
			}

			// ENSURE THAT WE DON'T GO ABOVE THE UPPER PLOT BLOCK EDGE
			if ( dY1 < dBlockY )
			{
				final double dDelta = ( dBlockY - dY1 );
				dY1 = dBlockY;
				dY += dDelta;
				dY2 += dDelta;
			}
			double dDeltaY1 = dY - dY1;
			double dDeltaY2 = dY2 - dY;

			// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
			dXAxisLabelsThickness = 0; // REUSE VARIABLE
			if ( iXLabelLocation == ABOVE )
			{
				// X-AXIS BAND IS (y1 -> y2)
				dXAxisLabelsThickness = dY2 - dY1;
			}
			else if ( iXLabelLocation == BELOW )
			{
				// X-AXIS BAND IS (y1 -> (y+AxisPlotSpacing))
				dY2 = ( dY + dAppliedXAxisPlotSpacing );
				dXAxisLabelsThickness = dY2 - dY1;
			}

			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// UPPER END
			if ( ( bForwardScale && dXAxisLabelsThickness > scY.getStartShift( ) )
					|| ( !bForwardScale && dXAxisLabelsThickness > scY.getEndShift( ) ) )
			{
				// REDUCE scY's ENDPOINT TO FIT THE X-AXIS AT THE TOP
				double dStart = scY.getStart( ), dEnd = dY2 - scY.getEndShift( );

				if ( bForwardScale )
				{
					dStart = dY2 - scY.getStartShift( );
					dEnd = scY.getEnd( );
				}

				scY.resetShifts( );

				// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
				// IF OVERLAPS OCCUR
				scY.setEndPoints( dStart, dEnd );
				scY.computeTicks( ids,
						laYAxisLabels,
						iYLabelLocation,
						VERTICAL,
						dStart,
						dEnd,
						true,
						aax );
				if ( !scY.isStepFixed( ) )
				{
					final Object[] oaMinMax = scY.getMinMax( );
					while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
					{
						if ( !scY.zoomOut( ) )
						{
							break;
						}
						scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
						int tickCount = scY.computeTicks( ids,
								laYAxisLabels,
								iYLabelLocation,
								VERTICAL,
								dStart,
								dEnd,
								true,
								aax );
						if ( scY.getUnit( ) != null
								&& asInteger( scY.getUnit( ) ) == Calendar.YEAR
								&& tickCount <= 3 )
						{
							break;
						}
					}
				}
			}

			dY -= insCA.getTop( );
			dY1 = dY - dDeltaY1;
			dY2 = dY + dDeltaY2;
			axPH.setTitleCoordinate( ( iXTitleLocation == ABOVE ) ? dY1 - 1
					: dY2 + 1 - dXAxisTitleThickness );
		}
		else if ( ( bForwardScale && iv.iType == IntersectionValue.MAX )
				|| ( !bForwardScale && iv.iType == IntersectionValue.MIN ) )
		{
			// NOTE: ENSURE CODE SYMMETRY WITH 'InsersectionValue.MAX'

			dY += dAppliedXAxisPlotSpacing;
			dY1 = dY;
			dY2 = dY;
			if ( bTicksBelow )
			{
				dY2 += TICK_SIZE;
			}
			if ( iXLabelLocation == ABOVE )
			{
				dY1 -= Math.max( ( bTicksAbove ? TICK_SIZE : 0 )
						+ dXAxisLabelsThickness, dAppliedXAxisPlotSpacing );
			}
			else if ( iXLabelLocation == BELOW )
			{
				dY2 += dXAxisLabelsThickness;
				dY1 -= Math.max( bTicksAbove ? TICK_SIZE : 0,
						dAppliedXAxisPlotSpacing );
			}
			if ( iXTitleLocation == ABOVE )
			{
				dY1 -= dXAxisTitleThickness;
			}
			else if ( iXTitleLocation == BELOW )
			{
				dY2 += dXAxisTitleThickness;
			}

			// ENSURE THAT WE DON'T GO BELOW THE LOWER PLOT BLOCK EDGE
			if ( dY2 > dBlockY + dBlockHeight )
			{
				final double dDelta = ( dY2 - ( dBlockY + dBlockHeight ) );
				dY2 = dBlockY + dBlockHeight;
				dY -= dDelta;
				dY1 -= dDelta;
			}

			double dDeltaY1 = dY - dY1;
			double dDeltaY2 = dY2 - dY;

			// COMPUTE THE X-AXIS BAND THICKNESS AND ADJUST Y2 FOR LABELS BELOW
			dXAxisLabelsThickness = 0; // REUSE VARIABLE
			if ( iXLabelLocation == ABOVE )
			{
				// X-AXIS BAND IS ((y+AxisPlotSpacing) -> y2)
				dY1 = ( dY - dAppliedXAxisPlotSpacing );
				dXAxisLabelsThickness = dY2 - dY1;
			}
			else if ( iXLabelLocation == BELOW )
			{
				// X-AXIS BAND IS (y1 -> y2)
				dXAxisLabelsThickness = dY2 - dY1;
			}

			// CHECK IF X-AXIS THICKNESS REQUIRES A PLOT HEIGHT RESIZE AT THE
			// LOWER END
			if ( ( bForwardScale && dXAxisLabelsThickness > scY.getEndShift( ) )
					|| ( !bForwardScale && dXAxisLabelsThickness > scY.getStartShift( ) ) )
			{
				// REDUCE scY's STARTPOINT TO FIT THE X-AXIS AT THE TOP
				double dStart = dY1 + scY.getStartShift( ), dEnd = scY.getEnd( );

				if ( bForwardScale )
				{
					dStart = scY.getStart( );
					dEnd = dY1 + scY.getEndShift( );
				}
				scY.resetShifts( );

				// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS LABELS
				// IF OVERLAPS OCCUR
				scY.setEndPoints( dStart, dEnd );
				scY.computeTicks( ids,
						laYAxisLabels,
						iYLabelLocation,
						VERTICAL,
						dStart,
						dEnd,
						true,
						aax );
				if ( !scY.isStepFixed( ) )
				{
					final Object[] oaMinMax = scY.getMinMax( );
					while ( !scY.checkFit( ids, laYAxisLabels, iYLabelLocation ) )
					{
						if ( !scY.zoomOut( ) )
						{
							break;
						}
						scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
						int tickCount = scY.computeTicks( ids,
								laYAxisLabels,
								iYLabelLocation,
								VERTICAL,
								dStart,
								dEnd,
								true,
								aax );
						if ( scY.getUnit( ) != null
								&& asInteger( scY.getUnit( ) ) == Calendar.YEAR
								&& tickCount <= 3 )
						{
							break;
						}
					}
				}
			}

			// MOVE THE BAND DOWNWARDS BY INSETS.BOTTOM
			dY += insCA.getBottom( );
			dY1 = dY - dDeltaY1;
			dY2 = dY + dDeltaY2;

			// SET THE AXIS TITLE's BOX TOP CO-ORDINATE
			axPH.setTitleCoordinate( ( iXTitleLocation == ABOVE ) ? dY1 - 1
					: dY2 + 1 - dXAxisTitleThickness );
		}
		else
		{
			double dDeltaY1 = 0, dDeltaY2 = 0;
			if ( iXLabelLocation == ABOVE )
			{
				dY1 -= ( bTicksAbove ? TICK_SIZE : 0 ) + dXAxisLabelsThickness;
				dY2 += ( bTicksBelow ? TICK_SIZE : 0 );

				if ( iXTitleLocation == ABOVE )
				{
					dY1 -= dXAxisTitleThickness;
				}
				else if ( iXTitleLocation == BELOW )
				{
					dY2 += dXAxisTitleThickness;
				}
				dDeltaY1 = dY - dY1;
				dDeltaY2 = dY2 - dY;

				// CHECK IF UPPER EDGE OF X-AXIS BAND GOES ABOVE PLOT UPPER EDGE
				if ( dY1 < dBlockY )
				{
					final Object[] oaMinMax = scY.getMinMax( );
					boolean bForceBreak = false;

					// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
					// OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
					// ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
					// POSITIONED

					do
					{
						// CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
						// Y-AXIS
						scY.setEndPoints( scY.getStart( ) + scY.getStartShift( ),
								scY.getEnd( ) - scY.getEndShift( ) ); // RESTORE
						scY.resetShifts( );

						// APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
						// ENDPOINT
						double[] da = scY.getEndPoints( );
						double dT_RI = dBlockY - dY1; // THRESHOLD -
						// REQUESTEDINTERSECTION
						double dAMin_AMax = da[0] - da[1];
						double dAMin_RI = da[0] - dY;
						double dStart = da[0];
						double dEnd = ( dT_RI / dAMin_RI ) * dAMin_AMax + da[1];
						if ( dEnd < dBlockY )
						{
							dEnd = dBlockY;
							bForceBreak = true; // ADJUST THE TOP EDGE OF THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}

						// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
						// LABELS IF OVERLAPS OCCUR
						scY.setEndPoints( dStart, dEnd );
						scY.computeTicks( ids,
								laYAxisLabels,
								iYLabelLocation,
								VERTICAL,
								dStart,
								dEnd,
								true,
								aax );
						if ( !scY.isStepFixed( ) )
						{
							while ( !scY.checkFit( ids,
									laYAxisLabels,
									iYLabelLocation ) )
							{
								if ( !scY.zoomOut( ) )
								{
									bForceBreak = true;
									break;
								}
								scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
								int tickCount = scY.computeTicks( ids,
										laYAxisLabels,
										iYLabelLocation,
										VERTICAL,
										dStart,
										dEnd,
										true,
										aax );
								if ( scY.getUnit( ) != null
										&& asInteger( scY.getUnit( ) ) == Calendar.YEAR
										&& tickCount <= 3 )
								{
									bForceBreak = true;
									break;
								}
							}
						}

						dY = getLocation( scY, iv );
						dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND
						// UPPER
						// EDGE
						dY2 = dY + dDeltaY2; // REDUNDANT: RE-CALCULATE
						// X-AXIS
						// BAND LOWER EDGE
					} while ( Math.abs( dY1 - dBlockY ) > 1 && !bForceBreak );
				}
			}
			else if ( iXLabelLocation == BELOW )
			{
				dY1 -= ( bTicksAbove ? TICK_SIZE : 0 );
				dY2 += ( bTicksBelow ? TICK_SIZE : 0 ) + dXAxisLabelsThickness;

				if ( iXTitleLocation == ABOVE )
				{
					dY1 -= dXAxisTitleThickness;
				}
				else if ( iXTitleLocation == BELOW )
				{
					dY2 += dXAxisTitleThickness;
				}
				dDeltaY1 = dY - dY1;
				dDeltaY2 = dY2 - dY;

				// CHECK IF LOWER EDGE OF X-AXIS BAND GOES BELOW PLOT LOWER EDGE
				if ( dY2 > dBlockY + dBlockHeight )
				{
					final Object[] oaMinMax = scY.getMinMax( );
					boolean bForceBreak = false;

					// A LOOP THAT ITERATIVELY ATTEMPTS TO ADJUST THE TOP EDGE
					// OF THE X-AXIS LABELS WITH THE TOP EDGE OF THE PLOT AND/OR
					// ENSURE THAT THE END POINT OF THE Y-AXIS SCALE IS SUITABLY
					// POSITIONED

					do
					{
						// CANCEL OUT THE END LABEL SHIFT COMPUTATIONS FROM THE
						// Y-AXIS
						scY.setEndPoints( scY.getStart( ) + scY.getStartShift( ),
								scY.getEnd( ) - scY.getEndShift( ) ); // RESTORE
						scY.resetShifts( );

						// APPLY THE AXIS REDUCTION FORMULA W.R.T. Y-AXIS
						// ENDPOINT
						double[] da = scY.getEndPoints( );
						double dX2_X1 = dY2 - ( dBlockY + dBlockHeight ); // THRESHOLD
						// -
						// REQUESTEDINTERSECTION
						double dAMin_AMax = da[0] - da[1];
						double dX2_AMax = dY - da[1];
						double dStart = da[0]
								- ( dX2_X1 / dX2_AMax )
								* dAMin_AMax;
						double dEnd = da[1];

						if ( dStart > dBlockY + dBlockHeight )
						{
							dStart = dBlockY + dBlockHeight;
							bForceBreak = true; // ADJUST THE TOP EDGE OF THE
							// Y-AXIS SCALE TO THE TOP EDGE
							// OF THE PLOT BLOCK
						}

						// LOOP THAT AUTO-RESIZES Y-AXIS AND RE-COMPUTES Y-AXIS
						// LABELS IF OVERLAPS OCCUR
						scY.setEndPoints( dStart, dEnd );
						scY.computeTicks( ids,
								laYAxisLabels,
								iYLabelLocation,
								VERTICAL,
								dStart,
								dEnd,
								true,
								aax );
						if ( !scY.isStepFixed( ) )
						{
							while ( !scY.checkFit( ids,
									laYAxisLabels,
									iYLabelLocation ) )
							{
								if ( !scY.zoomOut( ) )
								{
									bForceBreak = true;
									break;
								}
								scY.updateAxisMinMax( oaMinMax[0], oaMinMax[1] );
								int tickCount = scY.computeTicks( ids,
										laYAxisLabels,
										iYLabelLocation,
										VERTICAL,
										dStart,
										dEnd,
										true,
										aax );
								if ( scY.getUnit( ) != null
										&& asInteger( scY.getUnit( ) ) == Calendar.YEAR
										&& tickCount <= 3 )
								{
									bForceBreak = true;
									break;
								}
							}
						}

						dY = getLocation( scY, iv );
						dY2 = dY + dDeltaY2; // RE-CALCULATE X-AXIS BAND
						// LOWER
						// EDGE
						dY1 = dY - dDeltaY1; // RE-CALCULATE X-AXIS BAND
						// LOWER
						// EDGE
					} while ( Math.abs( dY2 - ( dBlockY + dBlockHeight ) ) > 1
							&& !bForceBreak );
				}
			}

			axPH.setTitleCoordinate( ( iXTitleLocation == ABOVE ) ? dY1 - 1
					: dY2 + 1 - dXAxisTitleThickness );
		}

		return dY;
	}

	/**
	 * Returns if the right-left mode is enabled.
	 * 
	 * @return
	 */
	protected boolean isRightToLeft( )
	{
		if ( rtc == null )
		{
			return false;
		}
		return rtc.isRightToLeft( );
	}

	/**
	 * Switch Position value due to right-left setting.
	 * 
	 * @param po
	 * @return
	 */
	protected Position switchPosition( Position po )
	{
		if ( isRightToLeft( ) )
		{
			if ( po == Position.RIGHT_LITERAL )
			{
				po = Position.LEFT_LITERAL;
			}
			else if ( po == Position.LEFT_LITERAL )
			{
				po = Position.RIGHT_LITERAL;
			}
		}
		return po;
	}

	/**
	 * Switch Anchor value due to right-left setting.
	 * 
	 * @param anchor
	 * @return
	 */
	protected Anchor switchAnchor( Anchor anchor )
	{
		if ( isRightToLeft( ) )
		{
			switch ( anchor.getValue( ) )
			{
				case Anchor.EAST :
					anchor = Anchor.WEST_LITERAL;
					break;
				case Anchor.NORTH_EAST :
					anchor = Anchor.NORTH_WEST_LITERAL;
					break;
				case Anchor.SOUTH_EAST :
					anchor = Anchor.SOUTH_WEST_LITERAL;
					break;
				case Anchor.WEST :
					anchor = Anchor.EAST_LITERAL;
					break;
				case Anchor.NORTH_WEST :
					anchor = Anchor.NORTH_EAST_LITERAL;
					break;
				case Anchor.SOUTH_WEST :
					anchor = Anchor.SOUTH_EAST_LITERAL;
					break;
			}
		}
		return anchor;
	}

	protected int switchTickStyle( int tickStyle )
	{
		if ( isRightToLeft( ) )
		{
			if ( tickStyle == TICK_LEFT )
			{
				tickStyle = TICK_RIGHT;
			}
			else if ( tickStyle == TICK_RIGHT )
			{
				tickStyle = TICK_LEFT;
			}
		}
		return tickStyle;
	}

	protected IntersectionValue switchIntersection( IntersectionValue iv )
	{
		if ( !cwa.isTransposed( ) && isRightToLeft( ) )
		{
			if ( iv.getType( ) == IntersectionValue.MAX )
			{
				iv = IntersectionValue.MIN_VALUE;
			}
			else if ( iv.getType( ) == IntersectionValue.MIN )
			{
				iv = IntersectionValue.MAX_VALUE;
			}
		}
		return iv;
	}
}
