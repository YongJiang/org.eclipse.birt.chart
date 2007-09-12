/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

import com.ibm.icu.util.Calendar;

/**
 * An internal processor which populates the user datasets.
 */
public class UserDataSetProcessor
{

	/**
	 * Populates the trigger datasets from given data source. Only Text data is
	 * supported now.
	 * 
	 * @param oResultSetDef
	 * @throws ChartException
	 */
	public DataSet[] populate( Object oResultSetDef ) throws ChartException
	{
		DataSet[] ds = new DataSet[0];

		if ( oResultSetDef instanceof IResultSetDataSet )
		{
			final IResultSetDataSet rsds = (IResultSetDataSet) oResultSetDef;
			final long lRowCount = rsds.getSize( );

			if ( lRowCount <= 0 )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.ZERO_DATASET,
						"exception.empty.dataset",//$NON-NLS-1$
						Messages.getResourceBundle( ) );
			}

			final int columnCount = rsds.getColumnCount( );
			ds = new DataSet[columnCount];
			// init dataset
			for ( int k = 0; k < columnCount; k++ )
			{
				switch ( rsds.getDataType( k ) )
				{
					case IConstants.TEXT :
						final String[] saDataSet = new String[(int) lRowCount];
						ds[k] = TextDataSetImpl.create( saDataSet );
						break;

					case IConstants.DATE_TIME :
						final Calendar[] caDataSet = new Calendar[(int) lRowCount];
						ds[k] = DateTimeDataSetImpl.create( caDataSet );
						break;

					case IConstants.NUMERICAL :
						final Double[] doaDataSet = new Double[(int) lRowCount];
						ds[k] = NumberDataSetImpl.create( doaDataSet );
						break;

					default :
						throw new ChartException( ChartEnginePlugin.ID,
								ChartException.DATA_SET,
								"exception.unknown.trigger.datatype", //$NON-NLS-1$
								Messages.getResourceBundle( ) );
				}
			}

			int i = 0;
			while ( rsds.hasNext( ) )
			{
				
				Object row[] = rsds.next( );
				for ( int k = 0; k < columnCount; k++ )
				{
					Object value = null;
					switch ( rsds.getDataType( k ) )
					{
						case IConstants.TEXT :
							value = row[k];
							break;

						case IConstants.DATE_TIME :
							value = Methods.asDateTime( row[k] );
							break;

						case IConstants.NUMERICAL :
							value = Methods.asDouble( row[k] );
							break;

						default :
							throw new ChartException( ChartEnginePlugin.ID,
									ChartException.DATA_SET,
									"exception.unknown.trigger.datatype", //$NON-NLS-1$
									Messages.getResourceBundle( ) );
					}
					((Object[])ds[k].getValues( ))[i] = value;
				}
				i++;
			}
		}

		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_SET,
					"exception.unknown.custom.dataset", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		return ds;
	}
}
