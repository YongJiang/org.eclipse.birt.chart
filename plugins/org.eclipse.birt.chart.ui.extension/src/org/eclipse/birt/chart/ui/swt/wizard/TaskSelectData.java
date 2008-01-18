/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.wizard.data.SelectDataDynamicArea;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewUtil;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * This task is used for data binding. The UI is mainly managed by
 * SelectDataDynamicArea. For the sake of customization, use IChartDataSheet
 * implementation to create specific UI sections.
 * 
 */
public class TaskSelectData extends SimpleTask
		implements
			ITaskChangeListener,
			Listener
{

	private final static int CENTER_WIDTH_HINT = 400;
	private ChartPreviewPainter previewPainter = null;

	private Composite cmpPreview = null;
	private Canvas previewCanvas = null;

	private SelectDataDynamicArea dynamicArea;

	private SashForm foSashForm;
	private Point fLeftSize;
	private Point fRightSize;
	private final int DEFAULT_HEIGHT = 580;
	private Composite fHeaderArea;
	private ScrolledComposite fDataArea;

	public TaskSelectData( )
	{
		super( Messages.getString( "TaskSelectData.TaskExp" ) ); //$NON-NLS-1$
		setDescription( Messages.getString( "TaskSelectData.Task.Description" ) ); //$NON-NLS-1$
	}

	public void createControl( Composite parent )
	{
		getDataSheet( ).setChartModel( getChartModel( ) );
		getDataSheet( ).addListener( this );

		if ( topControl == null || topControl.isDisposed( ) )
		{
			topControl = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout( 3, false );
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			topControl.setLayout( gridLayout );
			topControl.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL ) );

			dynamicArea = new SelectDataDynamicArea( this );
			getCustomizeUI( ).init( );

			foSashForm = new SashForm( topControl, SWT.VERTICAL );
			{
				GridLayout layout = new GridLayout( );
				foSashForm.setLayout( layout );
				GridData gridData = new GridData( GridData.FILL_BOTH );
				// gridData.heightHint = DEFAULT_HEIGHT;
				foSashForm.setLayoutData( gridData );
			}
			foSashForm.addListener( SWT.Resize, this );
			placeComponents( );
			createPreviewPainter( );
			// init( );
		}
		else
		{
			customizeUI( );
		}
		reSize( );
		if ( getChartModel( ) instanceof ChartWithAxes )
		{
			changeTask( null );
		}
		doLivePreview( );
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance( ).refreshAll( );

		ChartUIUtil.bindHelp( getControl( ),
				ChartHelpContextIds.TASK_SELECT_DATA );
	}

	protected void customizeUI( )
	{
		getCustomizeUI( ).init( );
		refreshLeftArea( );
		refreshRightArea( );
		refreshBottomArea( );
		reSize( );
		getCustomizeUI( ).layoutAll( );
	}

	private void reSize( )
	{
		Point headerSize = computeHeaderAreaSize( );
		int weight[] = foSashForm.getWeights( );
		if ( headerSize.y > DEFAULT_HEIGHT / 2 )
		{
			weight[0] = headerSize.y;
			weight[1] = DEFAULT_HEIGHT / 2;
			( (GridData) foSashForm.getLayoutData( ) ).heightHint = weight[0]
					+ weight[1];
		}
		else
		{
			weight[0] = 200;
			weight[1] = 200;
			( (GridData) foSashForm.getLayoutData( ) ).heightHint = DEFAULT_HEIGHT;
		}
	}

	private void refreshLeftArea( )
	{
		getCustomizeUI( ).refreshLeftBindingArea( );
		getCustomizeUI( ).selectLeftBindingArea( true, null );
	}

	private void refreshRightArea( )
	{
		getCustomizeUI( ).refreshRightBindingArea( );
		getCustomizeUI( ).selectRightBindingArea( true, null );
	}

	private void refreshBottomArea( )
	{
		getCustomizeUI( ).refreshBottomBindingArea( );
		getCustomizeUI( ).selectBottomBindingArea( true, null );
	}

	private void placeComponents( )
	{
		ChartAdapter.beginIgnoreNotifications( );
		try
		{
			createHeadArea( );// place two rows

			createDataArea( );

		}
		finally
		{
			// THIS IS IN A FINALLY BLOCK TO ENSURE THAT NOTIFICATIONS ARE
			// ENABLED EVEN IF ERRORS OCCUR DURING UI INITIALIZATION
			ChartAdapter.endIgnoreNotifications( );
		}
	}

	private void createDataArea( )
	{
		fDataArea = new ScrolledComposite( foSashForm, SWT.VERTICAL );
		{
			GridLayout gl = new GridLayout( );
			fDataArea.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_VERTICAL );
			fDataArea.setLayoutData( gd );
			fDataArea.setExpandHorizontal( true );
			fDataArea.setExpandVertical( true );
		}

		Composite dataComposite = new Composite( fDataArea, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 3, false );
			dataComposite.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_BOTH );
			dataComposite.setLayoutData( gd );
		}
		fDataArea.setContent( dataComposite );

		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = fLeftSize.x;
		new Label( dataComposite, SWT.NONE ).setLayoutData( gd );
		getDataSheet( ).createDataSelector( dataComposite );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = fRightSize.x;
		new Label( dataComposite, SWT.NONE ).setLayoutData( gd );

		new Label( dataComposite, SWT.NONE );
		getDataSheet( ).createDataDragSource( dataComposite );
		getDataSheet( ).createActionButtons( dataComposite );

		new Label( dataComposite, SWT.NONE );

		Point size = dataComposite.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		fDataArea.setMinSize( size );
	}

	private void createHeadArea( )
	{
		// Create header area.
		fHeaderArea = new Composite( foSashForm, SWT.NONE );
		{
			GridLayout layout = new GridLayout( 3, false );
			fHeaderArea.setLayout( layout );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			fHeaderArea.setLayoutData( gd );
		}

		{
			Composite cmpLeftContainer = ChartUIUtil.createCompositeWrapper( fHeaderArea );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_CENTER );
			gridData.verticalSpan = 2;
			cmpLeftContainer.setLayoutData( gridData );
			getCustomizeUI( ).createLeftBindingArea( cmpLeftContainer );
			fLeftSize = cmpLeftContainer.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		}
		createPreviewArea( fHeaderArea );
		{
			Composite cmpRightContainer = ChartUIUtil.createCompositeWrapper( fHeaderArea );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_CENTER );
			gridData.verticalSpan = 2;
			cmpRightContainer.setLayoutData( gridData );
			getCustomizeUI( ).createRightBindingArea( cmpRightContainer );
			fRightSize = cmpRightContainer.computeSize( SWT.DEFAULT,
					SWT.DEFAULT );
		}
		{
			Composite cmpBottomContainer = ChartUIUtil.createCompositeWrapper( fHeaderArea );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_BEGINNING );
			cmpBottomContainer.setLayoutData( gridData );
			getCustomizeUI( ).createBottomBindingArea( cmpBottomContainer );
		}
	}

	private Point computeHeaderAreaSize( )
	{
		return fHeaderArea.computeSize( SWT.DEFAULT, SWT.DEFAULT );
	}

	private Point computeDataAreaSize( )
	{
		return fDataArea.computeSize( SWT.DEFAULT, SWT.DEFAULT );
	}

	private void createPreviewArea( Composite parent )
	{
		cmpPreview = ChartUIUtil.createCompositeWrapper( parent );
		{
			GridData gridData = new GridData( GridData.FILL_BOTH );
			gridData.widthHint = CENTER_WIDTH_HINT;
			gridData.heightHint = 200;
			cmpPreview.setLayoutData( gridData );
		}

		Label label = new Label( cmpPreview, SWT.NONE );
		{
			label.setFont( JFaceResources.getBannerFont( ) );
			label.setText( Messages.getString( "TaskSelectData.Label.ChartPreview" ) ); //$NON-NLS-1$
		}

		previewCanvas = new Canvas( cmpPreview, SWT.BORDER );
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			previewCanvas.setLayoutData( gd );
			previewCanvas.setBackground( Display.getDefault( )
					.getSystemColor( SWT.COLOR_WHITE ) );
		}
	}

	private void createPreviewPainter( )
	{
		previewPainter = new ChartPreviewPainter( (ChartWizardContext) getContext( ) );
		previewCanvas.addPaintListener( previewPainter );
		previewCanvas.addControlListener( previewPainter );
		previewPainter.setPreview( previewCanvas );
	}

	private Chart getChartModel( )
	{
		if ( getContext( ) == null )
		{
			return null;
		}
		return ( (ChartWizardContext) getContext( ) ).getModel( );
	}

	protected IDataServiceProvider getDataServiceProvider( )
	{
		return ( (ChartWizardContext) getContext( ) ).getDataServiceProvider( );
	}

	public void dispose( )
	{
		super.dispose( );
		// No need to dispose other widgets
		if ( previewPainter != null )
		{
			previewPainter.dispose( );
		}
		previewPainter = null;
		if ( dynamicArea != null )
		{
			dynamicArea.dispose( );
		}
		dynamicArea = null;

		// Restore color registry
		ColorPalette.getInstance( ).restore( );

		// Remove all registered data definition text
		DataDefinitionTextManager.getInstance( ).removeAll( );
	}

	private ISelectDataCustomizeUI getCustomizeUI( )
	{
		return dynamicArea;
	}

	public void handleEvent( Event event )
	{
		if ( event.data == getDataSheet( ) )
		{
			if ( event.type == IChartDataSheet.EVENT_PREVIEW )
			{
				doLivePreview( );
				updateApplyButton( );
			}
			else if ( event.type == IChartDataSheet.EVENT_QUERY )
			{
				getCustomizeUI( ).refreshBottomBindingArea( );
				getCustomizeUI( ).refreshLeftBindingArea( );
				getCustomizeUI( ).refreshRightBindingArea( );
			}
		}
		else if ( event.type == SWT.Resize )
		{
			autoSash( );
		}
	}

	private void autoSash( )
	{
		int headerHeight = computeHeaderAreaSize( ).y;
		int dataHeight = computeDataAreaSize( ).y;
		int height = foSashForm.getClientArea( ).height;
		int weight[] = foSashForm.getWeights( );
		if ( height > headerHeight && height > dataHeight )
		{
			if ( height > headerHeight + dataHeight )
			{
				weight[0] = height - dataHeight;
				weight[1] = dataHeight + 1;
			}
			else
			{
				weight[0] = headerHeight;
				weight[1] = height - headerHeight;
			}
		}
		foSashForm.setWeights( weight );
	}

	public void changeTask( Notification notification )
	{
		if ( previewPainter != null )
		{
			if ( notification == null )
			{
				if ( getChartModel( ) instanceof ChartWithAxes )
				{
					checkDataTypeForChartWithAxes( );
				}
				return;
			}
			// Only data definition query (not group query) will be validated
			if ( ( notification.getNotifier( ) instanceof Query && ( (Query) notification.getNotifier( ) ).eContainer( ) instanceof Series ) )
			{
				checkDataType( (Query) notification.getNotifier( ),
						(Series) ( (Query) notification.getNotifier( ) ).eContainer( ) );
			}

			if ( notification.getNotifier( ) instanceof SeriesDefinition
					&& getChartModel( ) instanceof ChartWithAxes )
			{
				checkDataTypeForChartWithAxes( );
			}

			// Update Grouping aggregation button
			if ( notification.getNewValue( ) instanceof SeriesGrouping )
			{
				getCustomizeUI( ).refreshLeftBindingArea( );
			}

			// Query and series change need to update Live Preview
			if ( notification.getNotifier( ) instanceof Query
					|| notification.getNotifier( ) instanceof Axis
					|| notification.getNotifier( ) instanceof SeriesDefinition
					|| notification.getNotifier( ) instanceof SeriesGrouping )
			{
				doLivePreview( );
			}
			else if ( ChartPreviewPainter.isLivePreviewActive( ) )
			{
				ChartAdapter.beginIgnoreNotifications( );
				ChartUIUtil.syncRuntimeSeries( getChartModel( ) );
				ChartAdapter.endIgnoreNotifications( );

				doLivePreview( );
			}
			else
			{
				previewPainter.renderModel( getChartModel( ) );
			}
		}
	}

	private void checkDataType( Query query, Series series )
	{
		String expression = query.getDefinition( );

		Axis axis = null;
		for ( EObject o = query; o != null; )
		{
			o = o.eContainer( );
			if ( o instanceof Axis )
			{
				axis = (Axis) o;
				break;
			}
		}

		Collection cRegisteredEntries = ChartUIExtensionsImpl.instance( )
				.getSeriesUIComponents( );
		Iterator iterEntries = cRegisteredEntries.iterator( );

		String sSeries = null;
		while ( iterEntries.hasNext( ) )
		{
			ISeriesUIProvider provider = (ISeriesUIProvider) iterEntries.next( );
			sSeries = provider.getSeriesClass( );

			if ( sSeries.equals( series.getClass( ).getName( ) ) )
			{
				if ( getChartModel( ) instanceof ChartWithAxes )
				{
					DataType dataType = getDataServiceProvider( ).getDataType( expression );
					SeriesDefinition baseSD = (SeriesDefinition) ( ChartUIUtil.getBaseSeriesDefinitions( getChartModel( ) ).get( 0 ) );
					SeriesDefinition orthSD = null;
					orthSD = (SeriesDefinition) series.eContainer( );
					String aggFunc = ChartUtil.getAggregateFuncExpr( orthSD,
							baseSD );

					if ( baseSD != orthSD && baseSD.eContainer( ) != axis // Is
																			// not
																			// without
																			// axis
																			// chart.
							&& ChartUtil.isMagicAggregate( aggFunc ) )
					{
						// Only check aggregation is count in Y axis
						dataType = DataType.NUMERIC_LITERAL;
					}

					if ( isValidatedAxis( dataType, axis.getType( ) ) )
					{
						break;
					}

					AxisType[] axisTypes = provider.getCompatibleAxisType( series );
					for ( int i = 0; i < axisTypes.length; i++ )
					{
						if ( isValidatedAxis( dataType, axisTypes[i] ) )
						{
							axisNotification( axis, axisTypes[i] );
							axis.setType( axisTypes[i] );
							break;
						}
					}
				}

				boolean bException = false;
				try
				{
					provider.validateSeriesBindingType( series,
							getDataServiceProvider( ) );
				}
				catch ( ChartException ce )
				{
					bException = true;
					WizardBase.showException( Messages.getFormattedString( "TaskSelectData.Warning.TypeCheck",//$NON-NLS-1$
							new String[]{
									ce.getLocalizedMessage( ),
									series.getDisplayName( )
							} ) );
					if ( ce.getMessage( ).endsWith( expression ) )
					{
						ChartAdapter.beginIgnoreNotifications( );
						query.setDefinition( "" ); //$NON-NLS-1$
						ChartAdapter.endIgnoreNotifications( );
					}
				}

				if ( !bException )
				{
					WizardBase.removeException( );
				}

				break;
			}
		}
	}

	private boolean isValidatedAxis( DataType dataType, AxisType axisType )
	{
		if ( dataType == null )
		{
			return true;
		}
		else if ( ( dataType == DataType.DATE_TIME_LITERAL )
				&& ( axisType == AxisType.DATE_TIME_LITERAL ) )
		{
			return true;
		}
		else if ( ( dataType == DataType.NUMERIC_LITERAL )
				&& ( ( axisType == AxisType.LINEAR_LITERAL ) || ( axisType == AxisType.LOGARITHMIC_LITERAL ) ) )
		{
			return true;
		}
		else if ( ( dataType == DataType.TEXT_LITERAL )
				&& ( axisType == AxisType.TEXT_LITERAL ) )
		{
			return true;
		}
		return false;
	}

	private void axisNotification( Axis axis, AxisType type )
	{
		ChartAdapter.beginIgnoreNotifications( );
		{
			convertSampleData( axis, type );
			axis.setFormatSpecifier( null );

			EList markerLines = axis.getMarkerLines( );
			for ( int i = 0; i < markerLines.size( ); i++ )
			{
				( (MarkerLine) markerLines.get( i ) ).setFormatSpecifier( null );
			}

			EList markerRanges = axis.getMarkerRanges( );
			for ( int i = 0; i < markerRanges.size( ); i++ )
			{
				( (MarkerRange) markerRanges.get( i ) ).setFormatSpecifier( null );
			}
		}
		ChartAdapter.endIgnoreNotifications( );
	}

	private void convertSampleData( Axis axis, AxisType axisType )
	{
		if ( ( axis.getAssociatedAxes( ) != null )
				&& ( axis.getAssociatedAxes( ).size( ) != 0 ) )
		{
			BaseSampleData bsd = (BaseSampleData) getChartModel( ).getSampleData( )
					.getBaseSampleData( )
					.get( 0 );
			bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
					bsd.getDataSetRepresentation( ),
					0 ) );
		}
		else
		{
			int iStartIndex = getFirstSeriesDefinitionIndexForAxis( axis );
			int iEndIndex = iStartIndex + axis.getSeriesDefinitions( ).size( );

			int iOSDSize = getChartModel( ).getSampleData( )
					.getOrthogonalSampleData( )
					.size( );
			for ( int i = 0; i < iOSDSize; i++ )
			{
				OrthogonalSampleData osd = (OrthogonalSampleData) getChartModel( ).getSampleData( )
						.getOrthogonalSampleData( )
						.get( i );
				if ( osd.getSeriesDefinitionIndex( ) >= iStartIndex
						&& osd.getSeriesDefinitionIndex( ) < iEndIndex )
				{
					osd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
							osd.getDataSetRepresentation( ),
							i ) );
				}
			}
		}
	}

	private int getFirstSeriesDefinitionIndexForAxis( Axis axis )
	{
		List axisList = ( (Axis) ( (ChartWithAxes) getChartModel( ) ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( );
		int index = 0;
		for ( int i = 0; i < axisList.size( ); i++ )
		{
			if ( axis.equals( axisList.get( i ) ) )
			{
				index = i;
				break;
			}
		}
		int iTmp = 0;
		for ( int i = 0; i < index; i++ )
		{
			iTmp += ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChartModel( ),
					i )
					.getSeriesDefinitions( )
					.size( );
		}
		return iTmp;
	}

	private void updateApplyButton( )
	{
		( (ChartWizard) container ).updateApplayButton( );
	}

	private void doLivePreview( )
	{
		// Copy a runtime chart model to do live preview and it will not affect
		// design time chart model, so we can change attributes in runtime model
		// for some special requirements and processes.
		Chart cmRunTime = ChartPreviewUtil.prepareLivePreview( getChartModel( ),
				getDataServiceProvider( ) );

		previewPainter.renderModel( cmRunTime );
	}

	private void checkDataTypeForChartWithAxes( )
	{
		List osds = ChartUIUtil.getAllOrthogonalSeriesDefinitions( getChartModel( ) );
		for ( int i = 0; i < osds.size( ); i++ )
		{
			SeriesDefinition sd = (SeriesDefinition) osds.get( i );
			Series series = sd.getDesignTimeSeries( );
			checkDataType( ChartUIUtil.getDataQuery( sd, 0 ), series );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask#getImage()
	 */
	public Image getImage( )
	{
		return UIHelper.getImage( "icons/obj16/selectdata.gif" ); //$NON-NLS-1$
	}

	private IChartDataSheet getDataSheet( )
	{
		return ( (ChartWizardContext) getContext( ) ).getDataSheet( );
	}
}