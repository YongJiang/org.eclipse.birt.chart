/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.DateFormatDetail;
import org.eclipse.birt.chart.model.attribute.DateFormatType;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.ScaleUnitType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.UnitsOfMeasurement;
import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * LiteralHelper
 */
public class LiteralHelper
{

	/**
	 * Comment for <code>actionTypeSet</code>
	 */
	public static final NameSet actionTypeSet = getActionTypeSet( );
	/**
	 * Comment for <code>axisTypeSet</code>
	 */
	public static final NameSet axisTypeSet = getAxisTypeSet( );
	/**
	 * Comment for <code>dataPointComponentTypeSet</code>
	 */
	public static final NameSet dataPointComponentTypeSet = getDataPointComponentTypeSet( );
	/**
	 * Comment for <code>dataTypeSet</code>
	 */
	public static final NameSet dataTypeSet = getDataTypeSet( );
	/**
	 * Comment for <code>directionSet</code>
	 */
	public static final NameSet directionSet = getDirectionSet( );
	/**
	 * Comment for <code>groupingUnitTypeSet</code>
	 */
	public static final NameSet groupingUnitTypeSet = getGroupingUnitTypeSet( );
	/**
	 * Comment for <code>intersectionTypeSet</code>
	 */
	public static final NameSet intersectionTypeSet = getIntersectionTypeSet( );
	/**
	 * Comment for <code>legendItemTypeSet</code>
	 */
	public static final NameSet legendItemTypeSet = getLegendItemTypeSet( );
	/**
	 * Comment for <code>markerTypeSet</code>
	 */
	public static final NameSet markerTypeSet = getMarkerTypeSet( );
	/**
	 * Comment for <code>supportedMarkerTypeSet</code>
	 */
	public static final NameSet supportedMarkerTypeSet = getSupportedMarkerTypeSet( );
	/**
	 * Comment for <code>orientationSet</code>
	 */
	public static final NameSet orientationSet = getOrientationSet( );
	/**
	 * Comment for <code>riserTypeSet</code>
	 */
	public static final NameSet riserTypeSet = getRiserTypeSet( );
	/**
	 * Comment for <code>scaleUnitTypeSet</code>
	 */
	public static final NameSet scaleUnitTypeSet = getScaleUnitTypeSet( );
	/**
	 * Comment for <code>sortOptionSet</code>
	 */
	public static final NameSet sortOptionSet = getSortOptionSet( );
	/**
	 * Comment for <code>stretchSet</code>
	 */
	public static final NameSet stretchSet = getStretchSet( );
	/**
	 * Comment for <code>triggerConditionSet</code>
	 */
	public static final NameSet triggerConditionSet = getTriggerConditionSet( );
	/**
	 * Comment for <code>unitsOfMeasurementSet</code>
	 */
	public static final NameSet unitsOfMeasurementSet = getUnitsOfMeasurementSet( );
	/**
	 * Comment for <code>leaderLineStyleSet</code>
	 */
	public static final NameSet leaderLineStyleSet = getLeaderLineStyleSet( );
	/**
	 * Comment for <code>anchorSet</code>
	 */
	public static final NameSet anchorSet = getAnchorSet( );
	/**
	 * Comment for <code>dateFormatDetailSet</code>
	 */
	public static final NameSet dateFormatDetailSet = getDateFormatDetailSet( );
	/**
	 * Comment for <code>dateFormatTypeSet</code>
	 */
	public static final NameSet dateFormatTypeSet = getDateFormatTypeSet( );

	/**
	 * Comment for <code>verticalTickStyleSet</code>
	 */
	public static final NameSet verticalTickStyleSet = getVerticalTickStyleSet( );
	/**
	 * Comment for <code>horizontalTickStyleSet</code>
	 */
	public static final NameSet horizontalTickStyleSet = getHorizontalTickStyleSet( );
	/**
	 * Comment for <code>fullTickStyleSet</code>
	 */
	public static final NameSet fullTickStyleSet = getFullTickStyleSet( );

	/**
	 * Comment for <code>horizontalPositionSet</code>
	 */
	public static final NameSet horizontalPositionSet = getHorizontalPositionSet( );
	/**
	 * Comment for <code>verticalPositionSet</code>
	 */
	public static final NameSet verticalPositionSet = getVerticalPositionSet( );
	/**
	 * Comment for <code>inoutPositionSet</code>
	 */
	public static final NameSet inoutPositionSet = getInoutPositionSet( );
	/**
	 * Comment for <code>fullPositionSet</code>
	 */
	public static final NameSet fullPositionSet = getFullPositionSet( );
	/**
	 * Comment for <code>legendBehaviorTypeSet</code>
	 */
	public static final NameSet legendBehaviorTypeSet = getLegendBehaviorTypeSet( );

	private LiteralHelper( )
	{
		// no instance.
	}

	/**
	 * @return
	 */
	private static NameSet getAxisTypeSet( )
	{
		String prefix = "AxisType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, toStringNameArray( AxisType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getIntersectionTypeSet( )
	{
		String prefix = "IntersectionType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( IntersectionType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getScaleUnitTypeSet( )
	{
		String prefix = "ScaleUnitType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( ScaleUnitType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getSortOptionSet( )
	{
		String prefix = "SortOption."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( SortOption.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getDataTypeSet( )
	{
		String prefix = "DataType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, toStringNameArray( DataType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getGroupingUnitTypeSet( )
	{
		String prefix = "GroupingUnitType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( GroupingUnitType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getTriggerConditionSet( )
	{
		String prefix = "TriggerCondition."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, new String[]{
				TriggerCondition.ONCLICK_LITERAL.getName( ),
				TriggerCondition.ONDBLCLICK_LITERAL.getName( ),
				TriggerCondition.ONMOUSEDOWN_LITERAL.getName( ),
				TriggerCondition.ONMOUSEUP_LITERAL.getName( ),
				TriggerCondition.ONMOUSEOVER_LITERAL.getName( ),
				TriggerCondition.ONMOUSEMOVE_LITERAL.getName( ),
				TriggerCondition.ONMOUSEOUT_LITERAL.getName( ),
				TriggerCondition.ONFOCUS_LITERAL.getName( ),
				TriggerCondition.ONBLUR_LITERAL.getName( ),
				TriggerCondition.ONKEYDOWN_LITERAL.getName( ),
				TriggerCondition.ONKEYPRESS_LITERAL.getName( ),
				TriggerCondition.ONKEYUP_LITERAL.getName( ),
				TriggerCondition.ONLOAD_LITERAL.getName( ),
		} );
	}

	/**
	 * @return
	 */
	private static NameSet getActionTypeSet( )
	{
		String prefix = "ActionType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( ActionType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getUnitsOfMeasurementSet( )
	{
		String prefix = "UnitsOfMeasurement."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( UnitsOfMeasurement.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getRiserTypeSet( )
	{
		String prefix = "RiserType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( RiserType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getMarkerTypeSet( )
	{
		String prefix = "MarkerType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( MarkerType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getSupportedMarkerTypeSet( )
	{
		String prefix = "MarkerType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, new String[]{
				MarkerType.CROSSHAIR_LITERAL.getName( ),
				MarkerType.TRIANGLE_LITERAL.getName( ),
				MarkerType.BOX_LITERAL.getName( ),
				MarkerType.CIRCLE_LITERAL.getName( ),
				MarkerType.ICON_LITERAL.getName( ),
				MarkerType.NABLA_LITERAL.getName( ),
				MarkerType.DIAMOND_LITERAL.getName( ),
				MarkerType.FOUR_DIAMONDS_LITERAL.getName( ),
				MarkerType.BUTTON_LITERAL.getName( ),
				MarkerType.SEMI_CIRCLE_LITERAL.getName( ),
				MarkerType.HEXAGON_LITERAL.getName( ),
				MarkerType.RECTANGLE_LITERAL.getName( ),
				MarkerType.STAR_LITERAL.getName( ),
				MarkerType.COLUMN_LITERAL.getName( ),
				MarkerType.CROSS_LITERAL.getName( )
		} );
	}

	/**
	 * @return
	 */
	private static NameSet getDataPointComponentTypeSet( )
	{
		String prefix = "DataPointComponentType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( DataPointComponentType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getStretchSet( )
	{
		String prefix = "Stretch."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, toStringNameArray( Stretch.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getOrientationSet( )
	{
		String prefix = "Orientation."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( Orientation.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getDirectionSet( )
	{
		String prefix = "Direction."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( Direction.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getLegendItemTypeSet( )
	{
		String prefix = "LegendItemType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( LegendItemType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getVerticalTickStyleSet( )
	{
		String prefix = "TickStyle."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, new String[]{
				TickStyle.ACROSS_LITERAL.getName( ),
				TickStyle.LEFT_LITERAL.getName( ),
				TickStyle.RIGHT_LITERAL.getName( )
		} );
	}

	/**
	 * @return
	 */
	private static NameSet getHorizontalTickStyleSet( )
	{
		String prefix = "TickStyle."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, new String[]{
				TickStyle.ACROSS_LITERAL.getName( ),
				TickStyle.ABOVE_LITERAL.getName( ),
				TickStyle.BELOW_LITERAL.getName( )
		} );
	}

	/**
	 * @return
	 */
	private static NameSet getFullTickStyleSet( )
	{
		String prefix = "TickStyle."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( TickStyle.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getLeaderLineStyleSet( )
	{
		String prefix = "LeaderLineStyle."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( LeaderLineStyle.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getAnchorSet( )
	{
		String prefix = "Anchor."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, toStringNameArray( Anchor.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getHorizontalPositionSet( )
	{
		String prefix = "Position."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, new String[]{
				Position.LEFT_LITERAL.getName( ),
				Position.RIGHT_LITERAL.getName( ),
		} );
	}

	/**
	 * @return
	 */
	private static NameSet getVerticalPositionSet( )
	{
		String prefix = "Position."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, new String[]{
				Position.ABOVE_LITERAL.getName( ),
				Position.BELOW_LITERAL.getName( ),
		} );
	}

	/**
	 * @return
	 */
	private static NameSet getInoutPositionSet( )
	{
		String prefix = "Position."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, new String[]{
				Position.INSIDE_LITERAL.getName( ),
				Position.OUTSIDE_LITERAL.getName( ),
		} );
	}

	/**
	 * @return
	 */
	private static NameSet getFullPositionSet( )
	{
		String prefix = "Position."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix, suffix, toStringNameArray( Position.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getDateFormatDetailSet( )
	{
		String prefix = "DateFormatDetail."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( DateFormatDetail.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getDateFormatTypeSet( )
	{
		String prefix = "DateFormatType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( DateFormatType.VALUES ) );
	}

	/**
	 * @return
	 */
	private static NameSet getLegendBehaviorTypeSet( )
	{
		String prefix = "LegendBehaviorType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet( prefix,
				suffix,
				toStringNameArray( LegendBehaviorType.VALUES ) );
	}

	private static String[] toStringNameArray( List objList )
	{
		if ( objList == null )
		{
			return null;
		}

		String[] rt = new String[objList.size( )];
		int i = 0;

		for ( Iterator itr = objList.iterator( ); itr.hasNext( ); )
		{
			Object obj = itr.next( );

			if ( obj instanceof AbstractEnumerator )
			{
				rt[i++] = ( (AbstractEnumerator) obj ).getName( );
			}
			else
			{
				rt[i++] = String.valueOf( obj );
			}
		}

		return rt;
	}
}
