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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartReportStyleProcessor;
import org.eclipse.birt.chart.reportitem.QueryHelper;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartExpressionProvider;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartDlg;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.HyperlinkBuilder;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.text.NumberFormat;

/**
 * ChartReportItemBuilderImpl
 */
public class ChartReportItemBuilderImpl extends ReportItemBuilderUI
		implements
			IUIServiceProvider
{

	private static int iInstanceCount = 0;

	private transient ExtendedItemHandle extendedHandle = null;

	private transient String taskId = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public ChartReportItemBuilderImpl( )
	{
		super( );
	}

	/**
	 * Open the chart with specified task
	 * 
	 * @param taskId
	 *            specified task to open
	 */
	public ChartReportItemBuilderImpl( String taskId )
	{
		super( );
		this.taskId = taskId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI#open(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public int open( ExtendedItemHandle eih )
	{
		if ( iInstanceCount > 0 ) // LIMIT TO ONE INSTANCE
		{
			return Window.CANCEL;
		}
		iInstanceCount++;

		try
		{
			IReportItem item = null;
			try
			{
				item = eih.getReportItem( );
				if ( item == null )
				{
					eih.loadExtendedElement( );
					item = eih.getReportItem( );
				}
			}
			catch ( ExtendedElementException exception )
			{
				logger.log( exception );
			}
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemBuilderImpl.log.UnableToLocate" ) ); //$NON-NLS-1$
				iInstanceCount--;
				return Window.CANCEL;
			}

			final ChartReportItemImpl crii = ( (ChartReportItemImpl) item );
			final Chart cm = (Chart) crii.getProperty( "chart.instance" ); //$NON-NLS-1$
			final Chart cmClone = ( cm == null ) ? null
					: (Chart) EcoreUtil.copy( cm );

			// Set the ExtendedItemHandle instance (for use by the Chart Builder
			// UI
			this.extendedHandle = eih;

			// True: use V1.0; False: use V2.0 prototype
			boolean isOld = false;

			if ( isOld )
			{
				final ChartDlg cdBuilder = new ChartDlg( cmClone,
						this,
						eih,
						new ChartReportStyleProcessor( eih, true ) );

				if ( cdBuilder.getChart( ) != null ) // NOT CANCELLED
				{
					// TODO: Added till the model team sorts out pass-through
					// for
					// setProperty
					crii.executeSetModelCommand( eih, cm, cdBuilder.getChart( ) );

					try
					{
						final Bounds bo = cdBuilder.getChart( )
								.getBlock( )
								.getBounds( );

						// Modified to fix Bugzilla #99331
						NumberFormat nf = NumberFormat.getInstance( );

						if ( eih.getWidth( ).getStringValue( ) == null )
						{
							eih.setWidth( nf.format( bo.getWidth( ) ) + "pt" ); //$NON-NLS-1$
						}
						if ( eih.getHeight( ).getStringValue( ) == null )
						{
							eih.setHeight( nf.format( bo.getHeight( ) ) + "pt" ); //$NON-NLS-1$
						}
					}
					catch ( SemanticException smx )
					{
						logger.log( smx );
					}

					if ( crii.getDesignerRepresentation( ) != null )
					{
						( (DesignerRepresentation) crii.getDesignerRepresentation( ) ).setDirty( true );
					}

					iInstanceCount--;
					// Reset the ExtendedItemHandle instance since it is no
					// longer
					// needed
					this.extendedHandle = null;

					return Window.OK;
				}
			}
			else
			{
				// Use workbench shell to open the dialog
				Shell parentShell = null;
				if ( PlatformUI.isWorkbenchRunning( ) )
				{
					parentShell = PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( );
				}
				ChartWizard chartBuilder = new ChartWizard( parentShell );
				ChartWizardContext context = new ChartWizardContext( cmClone );
				context.setUIServiceProvider( this );
				context.setDataServiceProvider( new ReportDataServiceProvider( eih ) );
				Object of = eih.getProperty( "outputFormat" ); //$NON-NLS-1$
				if ( of instanceof String )
				{
					context.setOutputFormat( (String) of );
				}
				context.setExtendedItem( eih );
				context.setProcessor( new ChartReportStyleProcessor( eih, false ) );
				context = (ChartWizardContext) chartBuilder.open( null,
						taskId,
						context );
				if ( context != null && context.getModel( ) != null )
				{
					// update the output format property information.
					eih.setProperty( "outputFormat", context.getOutputFormat( ) ); //$NON-NLS-1$

					// TODO: Added till the model team sorts out pass-through
					// for
					// setProperty
					crii.executeSetModelCommand( eih, cm, context.getModel( ) );

					try
					{
						final Bounds bo = context.getModel( )
								.getBlock( )
								.getBounds( );

						// Modified to fix Bugzilla #99331
						NumberFormat nf = NumberFormat.getInstance( );

						if ( eih.getWidth( ).getStringValue( ) == null )
						{
							eih.setWidth( nf.format( bo.getWidth( ) ) + "pt" ); //$NON-NLS-1$
						}
						if ( eih.getHeight( ).getStringValue( ) == null )
						{
							eih.setHeight( nf.format( bo.getHeight( ) ) + "pt" ); //$NON-NLS-1$
						}
					}
					catch ( SemanticException smx )
					{
						logger.log( smx );
					}

					if ( crii.getDesignerRepresentation( ) != null )
					{
						( (DesignerRepresentation) crii.getDesignerRepresentation( ) ).setDirty( true );
					}

					iInstanceCount--;
					// Reset the ExtendedItemHandle instance since it is no
					// longer
					// needed
					this.extendedHandle = null;

					return Window.OK;
				}
			}

			iInstanceCount--;
			// Reset the ExtendedItemHandle instance since it is no longer
			// needed
			this.extendedHandle = null;
			return Window.CANCEL;
		}
		catch ( Exception e )
		{
			iInstanceCount--;
			throw new RuntimeException( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IExpressionBuilder#invoke(java.lang.String)
	 */
	public String invoke( String sExpression, Object oContext, String sTitle )
	{
		final ExpressionBuilder eb = new ExpressionBuilder( sExpression );
		eb.setExpressionProvier( new ExpressionProvider( SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ),
				DEUtil.getDataSetList( (ExtendedItemHandle) oContext ) ) );
		if ( sTitle != null )
		{
			eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
		}
		if ( eb.open( ) == Window.OK )
		{
			sExpression = eb.getResult( );
		}
		return sExpression;
	}

	public String invoke( String sExpression, Object oContext, String sTitle,
			boolean isChartProvider )
	{
		final ExpressionBuilder eb = new ExpressionBuilder( sExpression );
		eb.setExpressionProvier( new ChartExpressionProvider( ) );
		if ( sTitle != null )
		{
			eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
		}
		if ( eb.open( ) == Window.OK )
		{
			sExpression = eb.getResult( );
		}
		return sExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#validate(org.eclipse.birt.chart.model.Chart,
	 *      java.lang.Object)
	 */
	public final String[] validate( Chart cm, Object oContext )
	{
		final ArrayList alProblems = new ArrayList( 4 );

		// CHECK FOR UNBOUND DATASET
		final ExtendedItemHandle eih = (ExtendedItemHandle) oContext;
		if ( DEUtil.getDataSetList( eih ).size( ) == 0 )
		{
			alProblems.add( Messages.getString( "ChartReportItemBuilderImpl.problem.hasNotBeenFound" ) ); //$NON-NLS-1$
		}

		// CHECK FOR UNDEFINED SERIES QUERIES (DO NOT NEED THE RUNTIME CONTEXT)
		final QueryHelper.SeriesQueries[] qsqa = new QueryHelper( ).getSeriesQueryDefinitions( cm );
		Collection co;
		for ( int i = 0; i < qsqa.length; i++ )
		{
			co = qsqa[i].validate( );
			if ( co != null )
			{
				alProblems.addAll( co );
			}
		}

		return (String[]) alProblems.toArray( QueryHelper.CAST_STRING_ARRAY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getRegisteredKeys()
	 */
	public final List getRegisteredKeys( )
	{
		return extendedHandle.getModuleHandle( ).getMessageKeys( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getValue(java.lang.String)
	 */
	public final String getValue( String sKey )
	{
		String value = extendedHandle.getModuleHandle( ).getMessage( sKey );
		if ( value == null || value.equals("" ) ) //$NON-NLS-1$
			return sKey;
		else
			return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getConvertedValue(double,
	 *      java.lang.String, java.lang.String)
	 */
	public final double getConvertedValue( double dOriginalValue,
			String sFromUnits, String sToUnits )
	{
		if ( sFromUnits == null || sToUnits == null )
		{
			return dOriginalValue;
		}
		double dResult = -1d;

		// CONVERT FROM PIXELS
		final IDisplayServer ids = ChartDlg.getDisplayServer( );
		if ( sFromUnits.equalsIgnoreCase( "pixels" ) ) //$NON-NLS-1$
		{
			dOriginalValue = ( dOriginalValue * 72d ) / ids.getDpiResolution( );
		}

		// Convert to target units - Will convert to Points if target is Points,
		// Pixels or Unknown
		dResult = ( DimensionUtil.convertTo( dOriginalValue,
				getBIRTUnitsFor( sFromUnits ),
				getBIRTUnitsFor( sToUnits ) ) ).getMeasure( );

		// Special handling to convert TO Pixels
		if ( sToUnits.equalsIgnoreCase( "pixels" ) ) //$NON-NLS-1$
		{
			dResult = ( ids.getDpiResolution( ) * dResult ) / 72d;
		}
		return dResult;
	}

	/**
	 * @param sUnits
	 */
	private static String getBIRTUnitsFor( String sUnits )
	{
		if ( sUnits.equalsIgnoreCase( "inches" ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.UNITS_IN;
		}
		else if ( sUnits.equalsIgnoreCase( "centimeters" ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.UNITS_CM;
		}
		else
		{
			return DesignChoiceConstants.UNITS_PT;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(int,
	 *      java.lang.String, java.lang.Object, java.lang.String)
	 */
	public String invoke( int command, String value, Object context,
			String sTitle ) throws ChartException
	{
		ExpressionBuilder eb = null;

		switch ( command )
		{
			case IUIServiceProvider.COMMAND_EXPRESSION :
				eb = new ExpressionBuilder( value );
				eb.setExpressionProvier( new ExpressionProvider( SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( ),
						DEUtil.getDataSetList( (ExtendedItemHandle) context ) ) );
				if ( sTitle != null )
				{
					eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
				}
				if ( eb.open( ) == Window.OK )
				{
					value = eb.getResult( );
				}
				break;
			case IUIServiceProvider.COMMAND_CHART_EXPRESSION :
				eb = new ExpressionBuilder( value );
				eb.setExpressionProvier( new ChartExpressionProvider( ) );
				if ( sTitle != null )
				{
					eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
				}
				if ( eb.open( ) == Window.OK )
				{
					value = eb.getResult( );
				}
				break;
			case IUIServiceProvider.COMMAND_HYPERLINK :
				HyperlinkBuilder hb = new HyperlinkBuilder( );
				try
				{
					hb.setInputString( value );
					if ( sTitle != null )
					{
						hb.setTitle( hb.getTitle( ) + " - " + sTitle ); //$NON-NLS-1$
					}
					if ( hb.open( ) == Window.OK )
					{
						value = hb.getResultString( );
					}
				}
				catch ( Exception e )
				{
					throw new ChartException( ChartReportItemPlugin.ID,
							ChartException.UNDEFINED_VALUE,
							e );
				}
				break;
		}

		return value;
	}

	public boolean isInvokingSupported( )
	{
		return true;
	}
}