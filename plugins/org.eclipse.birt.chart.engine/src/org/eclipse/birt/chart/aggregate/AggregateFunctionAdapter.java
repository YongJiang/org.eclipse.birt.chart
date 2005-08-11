/**
 * 
 */

package org.eclipse.birt.chart.aggregate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.chart.engine.i18n.Messages;

/**
 *  
 */
public abstract class AggregateFunctionAdapter implements IAggregateFunction
{

	/**
	 *  
	 */
	private int iDataType = UNKNOWN;

	/**
	 *  
	 */
	private Locale lcl = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#accumulate(java.lang.Object)
	 */
	public void accumulate( Object oValue ) throws IllegalArgumentException
	{
		detectTypeChange( oValue );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#getAggregatedValue()
	 */
	public Object getAggregatedValue( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#initialize()
	 */
	public void initialize( )
	{
		iDataType = UNKNOWN;
	}

	/**
	 * Internally detects if the accumulated data's data type has changed in the
	 * aggregate function.
	 * 
	 * @param oValue
	 */
	private final void detectTypeChange( Object oValue )
			throws IllegalArgumentException
	{
		if ( iDataType == UNKNOWN )
		{
			if ( oValue == null )
			{
				return;
			}
			else if ( oValue instanceof Number )
			{
				iDataType = NUMBER;
			}
			else if ( oValue instanceof BigDecimal )
			{
				iDataType = BIGDECIMAL;
			}
			else if ( oValue instanceof Date )
			{
				iDataType = DATE;
			}
			else if ( oValue instanceof Calendar )
			{
				iDataType = CALENDAR;
			}
			else if ( oValue instanceof String )
			{
				iDataType = TEXT;
			}
			else
			{
				iDataType = CUSTOM;
			}
		}
		else
		{
			final int iExistingType = iDataType;
			if ( oValue == null )
			{
				return;
			}
			else if ( oValue instanceof Number )
			{
				iDataType = NUMBER;
			}
			else if ( oValue instanceof BigDecimal )
			{
				iDataType = BIGDECIMAL;
			}
			else if ( oValue instanceof Date )
			{
				iDataType = DATE;
			}
			else if ( oValue instanceof Calendar )
			{
				iDataType = CALENDAR;
			}
			else if ( oValue instanceof String )
			{
				iDataType = TEXT;
			}
			else
			{
				iDataType = CUSTOM;
			}
			if ( iExistingType != iDataType )
			{
				throw new IllegalArgumentException( MessageFormat.format( ResourceBundle.getBundle( Messages.ENGINE,
						lcl )
						.getString( "exception.mixed.data.types" ), //$NON-NLS-1$
						new Object[]{
							getClass( ).getName( )
						} ) );
			}
		}
	}

	/**
	 * Sets a locale associated with this aggregate function instance
	 * 
	 * @param lcl
	 *            A locale associated with this aggregate function instance
	 */
	public final void setLocale( Locale lcl )
	{
		this.lcl = lcl;
	}

	/**
	 * Returns the locale associated with this aggregate function instance to be
	 * used by this aggregate function
	 * 
	 * @return The locale associated with this aggregate function instance
	 */
	protected final Locale getLocale( )
	{
		return ( lcl == null ) ? Locale.getDefault( ) : lcl;
	}

	/**
	 * 
	 * @return
	 */
	protected final int getDataType( )
	{
		return iDataType;
	}

}