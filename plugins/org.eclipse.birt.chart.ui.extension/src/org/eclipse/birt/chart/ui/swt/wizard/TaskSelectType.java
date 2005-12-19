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

package org.eclipse.birt.chart.ui.swt.wizard;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * TaskSelectType
 */
public class TaskSelectType extends SimpleTask
		implements
			SelectionListener,
			DisposeListener,
			ITaskChangeListener
{

	private transient Chart chartModel = null;

	private transient Composite cmpTask = null;

	private transient Composite cmpPreview = null;

	private transient Composite cmpType = null;

	private transient Composite cmpMisc = null;

	private transient Composite cmpTypeButtons = null;

	private transient Composite cmpSubTypes = null;

	private transient Canvas previewCanvas = null;

	private transient ChartPreviewPainter previewPainter = null;

	private transient LinkedHashMap htTypes = null;

	private transient RowData rowData = new RowData( 80, 80 );

	private transient String sSubType = null;

	private transient String sType = null;

	// Stored in IChartType
	private transient String sDimension = null;

	private transient Table table = null;

	private transient Vector vSubTypeNames = null;

	private transient Orientation orientation = Orientation.VERTICAL_LITERAL;

	private transient Label lblOrientation = null;
	private transient Button cbOrientation = null;

	private transient Label lblMultipleY = null;
	private transient Button cbMultipleY = null;

	private transient Label lblSeriesType = null;
	private transient Combo cbSeriesType = null;

	private transient Label lblDimension = null;
	private transient Combo cbDimension = null;

	private transient Label lblOutput = null;
	private transient Combo cbOutput = null;

	private static final String LEADING_BLANKS = "  "; //$NON-NLS-1$

	private static Hashtable htSeriesNames = null;

	public TaskSelectType( )
	{
		super( Messages.getString( "TaskSelectType.TaskExp" ) ); //$NON-NLS-1$
		// this.chartModel = chartModel;
		if ( chartModel != null )
		{
			this.sType = chartModel.getType( );
			this.sSubType = chartModel.getSubType( );
			this.sDimension = translateDimensionString( chartModel.getDimension( )
					.getName( ) );
			if ( chartModel instanceof ChartWithAxes )
			{
				this.orientation = ( (ChartWithAxes) chartModel ).getOrientation( );
			}
		}
		htTypes = new LinkedHashMap( );
	}

	public Composite getUI( Composite parent )
	{
		if ( cmpTask == null )
		{
			cmpTask = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout( );
			gridLayout.marginWidth = 100;
			cmpTask.setLayout( gridLayout );
			cmpTask.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL ) );
			cmpTask.addDisposeListener( this );
			if ( context != null )
			{
				this.chartModel = ( (ChartWizardContext) context ).getModel( );
			}
			placeComponents( );
			updateAdapters( );
		}
		doLivePreview( );
		return cmpTask;
	}

	private void placeComponents( )
	{
		createPreviewArea( );
		createTypeArea( );
		createMiscArea( );
		setDefaultTypeSelection( );

		refreshChart( );
		populateSeriesTypesList( );
		createChartPainter( );
	}

	private void createChartPainter( )
	{
		previewPainter = new ChartPreviewPainter( ( (ChartWizardContext) getContext( ) ).getProcessor( ),
				container );
		previewCanvas.addPaintListener( previewPainter );
		previewCanvas.addControlListener( previewPainter );
		previewPainter.setPreview( previewCanvas );
	}

	private void createPreviewArea( )
	{
		cmpPreview = new Composite( cmpTask, SWT.NONE );
		cmpPreview.setLayout( new GridLayout( ) );

		GridData gridData = new GridData( GridData.FILL_BOTH );
		gridData.heightHint = 500;
		gridData.heightHint = 250;
		cmpPreview.setLayoutData( gridData );

		Label label = new Label( cmpPreview, SWT.NONE );
		{
			label.setText( Messages.getString( "TaskSelectType.Label.Preview" ) ); //$NON-NLS-1$
			label.setFont( JFaceResources.getBannerFont( ) );
		}

		previewCanvas = new Canvas( cmpPreview, SWT.BORDER );
		previewCanvas.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		previewCanvas.setBackground( Display.getDefault( )
				.getSystemColor( SWT.COLOR_WHITE ) );
	}

	private void createTypeArea( )
	{
		cmpType = new Composite( cmpTask, SWT.NONE );
		cmpType.setLayout( new GridLayout( 2, false ) );
		cmpType.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		createHeader( );
		createTable( );
		addChartTypes( );
		createComposite( new Vector( ) );
	}

	private void createMiscArea( )
	{
		cmpMisc = new Composite( cmpTask, SWT.NONE );
		cmpMisc.setLayout( new GridLayout( 4, false ) );
		cmpMisc.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL ) );

		GridData gridData = null;

		lblDimension = new Label( cmpMisc, SWT.NONE );
		lblDimension.setText( Messages.getString( "ChartSelector.Lbl.Dimension" ) ); //$NON-NLS-1$

		// Add the ComboBox for Dimensions
		cbDimension = new Combo( cmpMisc, SWT.DROP_DOWN | SWT.READ_ONLY );
		gridData = new GridData( GridData.GRAB_HORIZONTAL );
		cbDimension.setLayoutData( gridData );
		cbDimension.addSelectionListener( this );

		lblOutput = new Label( cmpMisc, SWT.NONE );
		{
			gridData = new GridData( );
			gridData.horizontalIndent = 50;
			lblOutput.setLayoutData( gridData );
			lblOutput.setText( Messages.getString( "TaskSelectType.Label.OutputFormat" ) ); //$NON-NLS-1$
		}

		// Add the ComboBox for Output Format
		cbOutput = new Combo( cmpMisc, SWT.DROP_DOWN | SWT.READ_ONLY );
		gridData = new GridData( GridData.GRAB_HORIZONTAL );
		cbOutput.setLayoutData( gridData );
		cbOutput.addSelectionListener( this );

		lblMultipleY = new Label( cmpMisc, SWT.NONE );
		lblMultipleY.setText( Messages.getString( "TaskSelectType.Label.MultipleYAxis" ) ); //$NON-NLS-1$

		// Add the checkBox for Multiple Y Axis
		cbMultipleY = new Button( cmpMisc, SWT.CHECK );
		cbMultipleY.setText( Messages.getString( "TaskSelectType.Label.AddSecondaryAxis" ) ); //$NON-NLS-1$
		cbMultipleY.addSelectionListener( this );

		lblSeriesType = new Label( cmpMisc, SWT.NONE );
		{
			gridData = new GridData( );
			gridData.horizontalIndent = 50;
			lblSeriesType.setLayoutData( gridData );
			lblSeriesType.setText( Messages.getString( "TaskSelectType.Label.SeriesType" ) ); //$NON-NLS-1$
			lblSeriesType.setEnabled( false );
		}

		// Add the ComboBox for Series Type
		cbSeriesType = new Combo( cmpMisc, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gd = new GridData( GridData.GRAB_HORIZONTAL );
			cbSeriesType.setLayoutData( gd );
			cbSeriesType.setEnabled( false );
			cbSeriesType.addSelectionListener( this );
		}

		lblOrientation = new Label( cmpMisc, SWT.NONE );
		lblOrientation.setText( Messages.getString( "TaskSelectType.Label.Oritention" ) ); //$NON-NLS-1$

		// Add the CheckBox for Orientation
		cbOrientation = new Button( cmpMisc, SWT.CHECK );
		cbOrientation.setText( Messages.getString( "TaskSelectType.Label.FlipAxis" ) ); //$NON-NLS-1$
		gridData = new GridData( );
		gridData.horizontalSpan = 3;
		cbOrientation.setLayoutData( gridData );
		cbOrientation.addSelectionListener( this );

		populateLists( );
	}

	/**
	 * This method initializes msgComposite
	 * 
	 */
	private void createHeader( )
	{
		Label lblTypes = new Label( cmpType, SWT.NO_FOCUS );
		{
			lblTypes.setText( Messages.getString( "TaskSelectType.Label.SelectChartType" ) ); //$NON-NLS-1$
		}

		Label lblSubtypes = new Label( cmpType, SWT.NO_FOCUS );
		{
			lblSubtypes.setText( Messages.getString( "TaskSelectType.Label.SelectSubtype" ) ); //$NON-NLS-1$
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalIndent = 5;
			lblSubtypes.setLayoutData( gd );
		}
	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTable( )
	{
		GridData gdTable = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.FILL_VERTICAL );
		table = new Table( cmpType, SWT.BORDER );
		table.setLayoutData( gdTable );
		table.setToolTipText( Messages.getString( "ChartSelector.Lbl.ChartTypes2" ) ); //$NON-NLS-1$
		table.addSelectionListener( this );
	}

	/**
	 * 
	 */
	private void addChartTypes( )
	{
		populateTypesTable( );
		updateUI( );
	}

	/**
	 * 
	 */
	private void populateTypesTable( )
	{
		Collection cTypes = ChartUIExtensionsImpl.instance( )
				.getUIChartTypeExtensions( );
		Iterator iterTypes = cTypes.iterator( );
		while ( iterTypes.hasNext( ) )
		{
			IChartType type = (IChartType) iterTypes.next( );
			htTypes.put( type.getName( ), type );
		}
	}

	private void updateUI( )
	{
		Iterator iter = htTypes.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String sTypeTmp = (String) iter.next( );
			TableItem tItem = new TableItem( table, SWT.NONE );
			tItem.setText( LEADING_BLANKS
					+ ( (IChartType) htTypes.get( sTypeTmp ) ).getName( ) );
			tItem.setImage( ( (IChartType) htTypes.get( sTypeTmp ) ).getImage( ) );
		}
	}

	/**
	 * This method initializes cmpSubTypes
	 * 
	 */
	private void createComposite( Vector vSubTypes )
	{
		GridData gdTypes = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL );
		cmpSubTypes = new Composite( cmpType, SWT.NONE );
		createGroups( vSubTypes );
		cmpSubTypes.setLayoutData( gdTypes );
		cmpSubTypes.setToolTipText( Messages.getString( "ChartSelector.Lbl.ChartSubtypes" ) ); //$NON-NLS-1$
		cmpSubTypes.setLayout( new GridLayout( ) );
		cmpSubTypes.setVisible( true );
	}

	/**
	 * This method initializes cmpTypeButtons
	 * 
	 */
	private void createGroups( Vector vSubTypes )
	{
		vSubTypeNames = new Vector( );
		if ( cmpTypeButtons != null && !cmpTypeButtons.isDisposed( ) )
		{
			// Remove existing buttons
			cmpTypeButtons.dispose( );
		}
		cmpTypeButtons = new Composite( cmpSubTypes, SWT.NONE );
		RowLayout rowLayout = new RowLayout( );
		rowLayout.marginTop = 0;
		rowLayout.marginLeft = 0;
		rowLayout.marginBottom = 12;
		rowLayout.marginRight = 12;
		rowLayout.spacing = 4;
		cmpTypeButtons.setLayout( rowLayout );

		// Add new buttons for this type
		for ( int iC = 0; iC < vSubTypes.size( ); iC++ )
		{
			IChartSubType subType = (IChartSubType) vSubTypes.get( iC );
			vSubTypeNames.add( subType.getName( ) );
			Button btnType = new Button( cmpTypeButtons, SWT.TOGGLE );
			btnType.setData( subType.getName( ) );
			btnType.setImage( subType.getImage( ) );
			btnType.setLayoutData( rowData );
			btnType.addSelectionListener( this );
			btnType.setToolTipText( subType.getDescription( ) );
			btnType.getImage( ).setBackground( btnType.getBackground( ) );
			btnType.setVisible( true );
			cmpTypeButtons.layout( true );
		}
		cmpTypeButtons.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		cmpSubTypes.layout( true );
	}

	private void populateLists( )
	{
		if ( "Horizontal".equals( this.orientation ) ) //$NON-NLS-1$
		{
			cbOrientation.setSelection( true );
		}
		else
		{
			cbOrientation.setSelection( false );
		}

		cbOutput.setItems( getOutputFormats( ) );

		String sCurrentFormat = ( (ChartWizardContext) getContext( ) ).getOutputFormat( );

		cbOutput.setText( ( sCurrentFormat == null )
				? ( (ChartWizardContext) getContext( ) ).getDefaultOutputFormat( )
				: sCurrentFormat );
	}

	private String[] getOutputFormats( )
	{
		try
		{
			String[][] outputFormatArray = PluginSettings.instance( )
					.getRegisteredOutputFormats( );
			String[] formats = new String[outputFormatArray.length];
			for ( int i = 0; i < formats.length; i++ )
			{
				formats[i] = outputFormatArray[i][0];
			}
			return formats;
		}
		catch ( ChartException e )
		{
			ChartWizard.displayException( e );
		}
		return new String[0];
	}

	/**
	 * 
	 */
	private void populateDimensionCombo( String sSelectedType )
	{
		cbDimension.removeAll( );
		String[] strArr = ( (IChartType) htTypes.get( sSelectedType ) ).getSupportedDimensions( );
		cbDimension.setItems( strArr );
		for ( int iD = 0; iD < strArr.length; iD++ )
		{
			if ( ( strArr[iD].equals( sDimension ) )
					|| ( strArr[iD].equals( ( (IChartType) htTypes.get( this.sType ) ).getDefaultDimension( ) ) ) )
			{
				cbDimension.select( iD );
			}
		}
	}

	private void populateSeriesTypes( String[] allSeriesTypes, Series series )
			throws ChartException
	{
		for ( int i = 0; i < allSeriesTypes.length; i++ )
		{
			try
			{
				Class seriesClass = Class.forName( allSeriesTypes[i] );
				Method createMethod = seriesClass.getDeclaredMethod( "create", new Class[]{} ); //$NON-NLS-1$
				Series newSeries = (Series) createMethod.invoke( seriesClass,
						new Object[]{} );
				if ( htSeriesNames == null )
				{
					htSeriesNames = new Hashtable( 20 );
				}
				String sDisplayName = PluginSettings.instance( )
						.getSeriesDisplayName( allSeriesTypes[i] );
				htSeriesNames.put( sDisplayName, allSeriesTypes[i] );
				if ( newSeries.canParticipateInCombination( ) )
				{
					cbSeriesType.add( sDisplayName );
					if ( allSeriesTypes[i].equals( series.getClass( ).getName( ) ) )
					{
						cbSeriesType.select( cbSeriesType.getItemCount( ) - 1 );
					}
				}
			}
			catch ( Exception e )
			{
				ChartWizard.displayException( e );
			}
		}
	}

	/*
	 * This method translates the dimension string from the model to that
	 * maintained by the UI (for readability).
	 */
	private String translateDimensionString( String sDimensionValue )
	{
		String dimensionName = ""; //$NON-NLS-1$
		if ( sDimensionValue.equals( ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			dimensionName = IChartType.TWO_DIMENSION_TYPE;
		}
		else if ( sDimensionValue.equals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName( ) ) )
		{
			dimensionName = IChartType.TWO_DIMENSION_WITH_DEPTH_TYPE;
		}
		else if ( sDimensionValue.equals( ChartDimension.THREE_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			dimensionName = IChartType.THREE_DIMENSION_TYPE;
		}
		return dimensionName;
	}

	private void addOverlayAxis( )
	{
		if ( chartModel instanceof ChartWithoutAxes )
		{
			throw new IllegalArgumentException( Messages.getString( "TaskSelectType.Exception.CannotSupportAxes" ) ); //$NON-NLS-1$
		}
		// Create a clone of the existing Y Axis
		Axis yAxis = (Axis) ( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( ).get( 0 );
		Axis overlayAxis = (Axis) EcoreUtil.copy( yAxis );
		// Now update overlay axis to set the properties that are different from
		// the original
		overlayAxis.setPrimaryAxis( false );
		overlayAxis.setOrigin( AxisOriginImpl.create( IntersectionType.MAX_LITERAL,
				null ) );
		overlayAxis.setLabelPosition( Position.RIGHT_LITERAL );
		overlayAxis.setTitlePosition( Position.RIGHT_LITERAL );
		overlayAxis.getTitle( )
				.getCaption( )
				.setValue( Messages.getString( "TaskSelectType.Caption.OverlayAxis1" ) ); //$NON-NLS-1$

		// Retain the first series of the axis. Remove others
		if ( overlayAxis.getSeriesDefinitions( ).size( ) > 1 )
		{
			EList list = overlayAxis.getSeriesDefinitions( );
			for ( int i = list.size( ) - 1; i > 0; i-- )
			{
				list.remove( i );
			}
		}

		// Add the notification listeners from the old axis to the new one
		overlayAxis.eAdapters( ).addAll( yAxis.eAdapters( ) );

		// Update overlay series definition(retain the group query, clean the data query)
		SeriesDefinition sdOverlay = (SeriesDefinition) overlayAxis.getSeriesDefinitions( )
				.get( 0 );
		EList dds = sdOverlay.getDesignTimeSeries( ).getDataDefinition( );
		for ( int i = 0; i < dds.size( ); i++ )
		{
			( (Query) dds.get( i ) ).setDefinition( "" ); //$NON-NLS-1$
		}
		sdOverlay.getSeriesPalette( ).update( -1 );

		// Update the chart model with the new Axis
		( (Axis) ( (ChartWithAxes) chartModel ).getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.add( overlayAxis );

		// Update the sample values for the new overlay series
		SampleData sd = chartModel.getSampleData( );

		// Create a new OrthogonalSampleData instance from the existing one
		int currentSize = sd.getOrthogonalSampleData( ).size( );
		for ( int i = 0; i < currentSize; i++ )
		{
			OrthogonalSampleData sdOrthogonal = (OrthogonalSampleData) EcoreUtil.copy( (EObject) chartModel.getSampleData( )
					.getOrthogonalSampleData( )
					.get( i ) );
			sdOrthogonal.setSeriesDefinitionIndex( currentSize + i );
			sdOrthogonal.eAdapters( ).addAll( sd.eAdapters( ) );
			sd.getOrthogonalSampleData( ).add( sdOrthogonal );
		}
	}

	private void removeOverlayAxis( )
	{
		if ( chartModel instanceof ChartWithoutAxes )
		{
			throw new IllegalArgumentException( Messages.getString( "TaskSelectType.Exception.CannotSupportAxes" ) ); //$NON-NLS-1$
		}
		/*
		 * // ASSUMPTIONS All Y series will only be added to the primary axis.
		 * All Overlay series will only be added to the second axis.
		 */
		int iSDIndex = ( (Axis) ( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
				.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( )
				.size( );
		// Remove the sample data for the overlay series
		EList list = chartModel.getSampleData( ).getOrthogonalSampleData( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			// Check each entry if it is associated with the series definition
			// to be removed
			if ( ( (OrthogonalSampleData) list.get( i ) ).getSeriesDefinitionIndex( ) >= iSDIndex )
			{
				list.remove( i );
				// Reset counter
				i--;
			}
		}
		// Remove the second (Overlay) Y Axis. (The primary Y axis is always the
		// first axis in the list)
		( (Axis) ( (ChartWithAxes) chartModel ).getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.remove( 1 );
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	public void widgetSelected( SelectionEvent e )
	{
		// Indicates whether need to update chart model
		boolean needUpdateModel = false;
		Object oSelected = e.getSource( );
		if ( oSelected.getClass( ).equals( Button.class ) )
		{
			needUpdateModel = true;

			if ( oSelected.equals( cbOrientation ) )
			{
				if ( cbOrientation.getSelection( ) )
				{
					this.orientation = Orientation.HORIZONTAL_LITERAL;
				}
				else
				{
					this.orientation = Orientation.VERTICAL_LITERAL;
				}
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );
			}
			else if ( oSelected.equals( cbMultipleY ) )
			{
				lblSeriesType.setEnabled( cbMultipleY.getSelection( ) );
				cbSeriesType.setEnabled( cbMultipleY.getSelection( ) );

				ChartAdapter.ignoreNotifications( true );
				if ( cbMultipleY.getSelection( ) )
				{
					addOverlayAxis( );
				}
				else
				{
					removeOverlayAxis( );
				}
				ChartAdapter.ignoreNotifications( false );
			}
			else
			{
				Button btn = (Button) e.getSource( );
				if ( btn.getSelection( ) )
				{
					if ( this.sSubType != null
							&& !getSubtypeFromButton( btn ).equals( sSubType ) )
					{
						int iTypeIndex = vSubTypeNames.indexOf( sSubType );
						if ( iTypeIndex >= 0 )
						{
							( (Button) cmpTypeButtons.getChildren( )[iTypeIndex] ).setSelection( false );
							cmpTypeButtons.redraw( );
						}
					}
					sSubType = getSubtypeFromButton( btn );
				}
				else
				{
					if ( this.sSubType != null
							&& getSubtypeFromButton( btn ).equals( sSubType ) )
					{
						// Clicking on the same button should not cause it to be
						// unselected
						btn.setSelection( true );
						needUpdateModel = false;
					}
				}
			}
		}
		else if ( oSelected.getClass( ).equals( Table.class ) )
		{
			sType = ( (TableItem) e.item ).getText( ).trim( );
			if ( !chartModel.getType( ).equals( sType ) )
			{
				sSubType = null;
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );

				// Pack to display enough space for different chart
				container.packWizard( );
				cmpMisc.layout( );

				needUpdateModel = true;
			}
		}
		else if ( oSelected.equals( cbDimension ) )
		{
			String newDimension = cbDimension.getItem( cbDimension.getSelectionIndex( ) );
			if ( !newDimension.equals( sDimension ) )
			{
				sDimension = newDimension;
				createAndDisplayTypesSheet( this.sType );
				setDefaultSubtypeSelection( );

				needUpdateModel = true;
			}
		}
		else if ( oSelected.equals( cbSeriesType ) )
		{
			String oldSeriesName = ( (SeriesDefinition) ChartUIUtil.getOrthogonalSeriesDefinitions( chartModel,
					1 )
					.get( 0 ) ).getDesignTimeSeries( ).getDisplayName( );
			if ( !cbSeriesType.getText( ).equals( oldSeriesName ) )
			{
				needUpdateModel = true;
				changeOverlaySeriesType( );
			}
		}
		else if ( oSelected.equals( cbOutput ) )
		{
			( (ChartWizardContext) getContext( ) ).setOutputFormat( cbOutput.getText( ) );
		}

		// Following operations need new model
		if ( needUpdateModel )
		{
			// Update chart model
			refreshChart( );

			if ( oSelected.getClass( ).equals( Table.class ) )
			{
				// Ensure populate list after chart model generated
				populateSeriesTypesList( );
			}
			else if ( oSelected.equals( cbMultipleY ) )
			{
				if ( chartModel != null && chartModel instanceof ChartWithAxes )
				{
					if ( ChartUIUtil.getOrthogonalAxisNumber( chartModel ) > 1 )
					{
						Axis overlayAxis = ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) chartModel,
								1 );
						String sDisplayName = PluginSettings.instance( )
								.getSeriesDisplayName( ( (SeriesDefinition) overlayAxis.getSeriesDefinitions( )
										.get( 0 ) ).getDesignTimeSeries( )
										.getClass( )
										.getName( ) );
						cbSeriesType.select( cbSeriesType.indexOf( sDisplayName ) );
					}
				}
			}
		}
	}

	private void updateAdapters( )
	{
		// Refresh all adapters
		EContentAdapter adapter = ( (ChartWizard) container ).getAdapter( );
		chartModel.eAdapters( ).remove( adapter );
		TreeIterator iterator = chartModel.eAllContents( );
		while ( iterator.hasNext( ) )
		{
			Object oModel = iterator.next( );
			if ( oModel instanceof EObject )
			{
				( (EObject) oModel ).eAdapters( ).remove( adapter );
			}
		}
		chartModel.eAdapters( ).add( adapter );
	}

	private boolean is3D( )
	{
		return IChartType.THREE_DIMENSION_TYPE.equals( sDimension );
	}

	private void changeOverlaySeriesType( )
	{
		try
		{
			// CREATE A NEW SERIES INSTANCE OF APPROPRIATE TYPE...USING THE
			// create() METHOD
			Class seriesClass = Class.forName( htSeriesNames.get( cbSeriesType.getText( ) )
					.toString( ) );
			Method createMethod = seriesClass.getDeclaredMethod( "create", new Class[]{} ); //$NON-NLS-1$
			// CHANGE ALL OVERLAY SERIES TO NEW SELECTED TYPE
			Axis XAxis = (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 );
			int iSeriesDefinitionIndex = 0 + ( (Axis) XAxis.getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).size( ); // SINCE
			// THIS IS FOR THE ORTHOGONAL OVERLAY SERIES DEFINITION
			int iOverlaySeriesCount = ( (Axis) XAxis.getAssociatedAxes( )
					.get( 1 ) ).getSeriesDefinitions( ).size( );
			// DISABLE NOTIFICATIONS WHILE MODEL UPDATE TAKES PLACE
			ChartAdapter.ignoreNotifications( true );
			for ( int i = 0; i < iOverlaySeriesCount; i++ )
			{
				Series newSeries = (Series) createMethod.invoke( seriesClass,
						new Object[]{} );
				newSeries.translateFrom( ( (SeriesDefinition) ( (Axis) XAxis.getAssociatedAxes( )
						.get( 1 ) ).getSeriesDefinitions( ).get( i ) ).getDesignTimeSeries( ),
						iSeriesDefinitionIndex,
						chartModel );
				// ADD THE MODEL ADAPTERS TO THE NEW SERIES
				newSeries.eAdapters( ).addAll( chartModel.eAdapters( ) );
				// UPDATE THE SERIES DEFINITION WITH THE SERIES INSTANCE
				( (SeriesDefinition) ( (Axis) XAxis.getAssociatedAxes( )
						.get( 1 ) ).getSeriesDefinitions( ).get( i ) ).getSeries( )
						.clear( );
				( (SeriesDefinition) ( (Axis) XAxis.getAssociatedAxes( )
						.get( 1 ) ).getSeriesDefinitions( ).get( i ) ).getSeries( )
						.add( newSeries );
			}
		}
		catch ( Exception e )
		{
			ChartWizard.displayException( e );
		}
		finally
		{
			// ENABLE NOTIFICATIONS IN CASE EXCEPTIONS OCCUR
			ChartAdapter.ignoreNotifications( true );
		}
	}

	private void populateSeriesTypesList( )
	{
		// Populate Series Types List
		cbSeriesType.removeAll( );
		Series series = getSeriesDefinitionForProcessing( ).getDesignTimeSeries( );
		if ( series.canParticipateInCombination( ) )
		{
			try
			{
				populateSeriesTypes( PluginSettings.instance( )
						.getRegisteredSeries( ), series );
			}
			catch ( ChartException e )
			{
				ChartWizard.displayException( e );
			}
		}
		else
		{
			String seriesName = PluginSettings.instance( )
					.getSeriesDisplayName( series.getClass( ).getName( ) );
			cbSeriesType.add( seriesName );
			cbSeriesType.select( 0 );
		}

		// Select the appropriate current series type if overlay series exists
		if ( this.chartModel != null && chartModel instanceof ChartWithAxes )
		{
			Axis xAxis = ( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 ) );
			if ( xAxis.getAssociatedAxes( ).size( ) > 1 )
			{
				Axis overlayAxis = (Axis) xAxis.getAssociatedAxes( ).get( 1 );
				String sDisplayName = PluginSettings.instance( )
						.getSeriesDisplayName( ( (SeriesDefinition) overlayAxis.getSeriesDefinitions( )
								.get( 0 ) ).getDesignTimeSeries( )
								.getClass( )
								.getName( ) );
				cbSeriesType.setText( sDisplayName );
			}
			else
			{
				cbSeriesType.select( 0 );
			}
		}
	}

	/**
	 * This method populates the subtype panel (creating its components if
	 * necessary). It gets called when the type selection changes or when the
	 * dimension selection changes (since not all sub types are supported for
	 * all dimension selections).
	 * 
	 * @param sSelectedType
	 *            Type from Type List
	 */
	private void createAndDisplayTypesSheet( String sSelectedType )
	{
		IChartType chartType = (IChartType) htTypes.get( sSelectedType );
		lblOrientation.setEnabled( chartType.supportsTransposition( )
				&& !is3D( ) );
		cbOrientation.setEnabled( chartType.supportsTransposition( ) && !is3D( ) );
		Vector vSubTypes = null;

		// Show the subtypes for the selected type based on current selections
		// of dimension and orientation
		if ( this.sDimension != null && this.orientation != null )
		{
			vSubTypes = new Vector( chartType.getChartSubtypes( sDimension,
					orientation ) );
		}

		if ( vSubTypes == null || vSubTypes.size( ) == 0 )
		{
			vSubTypes = new Vector( chartType.getChartSubtypes( chartType.getDefaultDimension( ),
					Orientation.VERTICAL_LITERAL ) );
			this.sDimension = chartType.getDefaultDimension( );
			this.orientation = Orientation.VERTICAL_LITERAL;
		}
		Orientation orientationTmp = this.orientation;

		// Update the UI with information for selected type
		createGroups( vSubTypes );
		populateDimensionCombo( sSelectedType );
		if ( orientationTmp == Orientation.HORIZONTAL_LITERAL )
		{
			this.cbOrientation.setSelection( true );
		}
		else
		{
			this.cbOrientation.setSelection( false );
		}
		cmpType.layout( );
	}

	private void setDefaultSubtypeSelection( )
	{
		if ( sSubType == null )
		{
			( (Button) cmpTypeButtons.getChildren( )[0] ).setSelection( true );
			sSubType = getSubtypeFromButton( cmpTypeButtons.getChildren( )[0] );
		}
		else
		{
			Control[] buttons = cmpTypeButtons.getChildren( );
			boolean bSelected = false;
			for ( int iB = 0; iB < buttons.length; iB++ )
			{
				if ( getSubtypeFromButton( buttons[iB] ).equals( sSubType ) )
				{
					( (Button) buttons[iB] ).setSelection( true );
					bSelected = true;
					break;
				}
			}
			// If specified subType is not found, select default
			if ( !bSelected )
			{
				( (Button) cmpTypeButtons.getChildren( )[0] ).setSelection( true );
				sSubType = getSubtypeFromButton( cmpTypeButtons.getChildren( )[0] );
			}
		}
		cmpTypeButtons.redraw( );
	}

	private void setDefaultTypeSelection( )
	{
		if ( table.getItems( ).length > 0 )
		{
			if ( sType == null )
			{
				table.select( 0 );
				sType = ( table.getSelection( )[0] ).getText( ).trim( );
			}
			else
			{
				TableItem[] tiAll = table.getItems( );
				for ( int iTI = 0; iTI < tiAll.length; iTI++ )
				{
					if ( tiAll[iTI].getText( ).trim( ).equals( sType ) )
					{
						table.select( iTI );
						break;
					}
				}
			}
			createAndDisplayTypesSheet( sType );
			setDefaultSubtypeSelection( );
		}
	}

	public void widgetDisposed( DisposeEvent e )
	{
		super.dispose( );
		// No need to dispose other widgets
		cmpTask = null;
		chartModel = null;
		previewPainter.dispose( );
		previewPainter = null;
		sSubType = null;
		sType = null;
		sDimension = null;
		vSubTypeNames = null;
		orientation = Orientation.VERTICAL_LITERAL;
	}

	private void refreshChart( )
	{
		// DISABLE PREVIEW REFRESH DURING CONVERSION
		ChartAdapter.ignoreNotifications( true );
		IChartType chartType = (IChartType) htTypes.get( sType );
		try
		{
			chartModel = chartType.getModel( sSubType,
					this.orientation,
					this.sDimension,
					this.chartModel );
			updateAdapters( );
		}
		catch ( Exception e )
		{
			ChartWizard.displayException( e );
		}

		// RE-ENABLE PREVIEW REFRESH
		ChartAdapter.ignoreNotifications( false );

		updateSelection( );
		if ( context == null )
		{
			context = new ChartWizardContext( chartModel );
		}
		else
		{
			( (ChartWizardContext) context ).setModel( chartModel );
		}
		( (ChartWizardContext) context ).setChartType( chartType );
		setContext( context );

		// Refresh preview
		doLivePreview( );
	}

	private SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		// TODO Attention: all index is 0
		SeriesDefinition sd = null;
		if ( chartModel instanceof ChartWithAxes )
		{
			sd = ( (SeriesDefinition) ( (Axis) ( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( )
					.get( 0 ) );
		}
		else if ( chartModel instanceof ChartWithoutAxes )
		{
			sd = (SeriesDefinition) ( (SeriesDefinition) ( (ChartWithoutAxes) chartModel ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).get( 0 );
		}
		return sd;
	}

	/**
	 * Updates UI selection according to Chart type
	 * 
	 */
	private void updateSelection( )
	{
		if ( chartModel instanceof ChartWithAxes )
		{
			lblMultipleY.setEnabled( !is3D( ) );
			cbMultipleY.setEnabled( !is3D( ) );
			lblSeriesType.setEnabled( cbMultipleY.getSelection( ) );
			cbSeriesType.setEnabled( cbMultipleY.getSelection( ) );
		}
		else
		{
			cbMultipleY.setSelection( false );
			lblMultipleY.setEnabled( false );
			cbMultipleY.setEnabled( false );
			lblSeriesType.setEnabled( false );
			cbSeriesType.setEnabled( false );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#getContext()
	 */
	public IWizardContext getContext( )
	{
		ChartWizardContext context = (ChartWizardContext) super.getContext( );
		if ( context == null )
		{
			context = new ChartWizardContext( this.chartModel );
		}
		else
		{
			context.setModel( this.chartModel );
		}
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.frameworks.taskwizard.interfaces.ITask#setContext(org.eclipse.birt.frameworks.taskwizard.interfaces.IWizardContext)
	 */
	public void setContext( IWizardContext context )
	{
		super.setContext( context );
		this.chartModel = ( (ChartWizardContext) context ).getModel( );
		if ( chartModel != null )
		{
			this.sType = chartModel.getType( );
			this.sSubType = chartModel.getSubType( );
			this.sDimension = translateDimensionString( chartModel.getDimension( )
					.getName( ) );
			if ( chartModel instanceof ChartWithAxes )
			{
				this.orientation = ( (ChartWithAxes) chartModel ).getOrientation( );
				int iYAxesCount = ( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( ).size( );
				// IF THE UI HAS BEEN INITIALIZED...I.E. IF setContext() IS
				// CALLED AFTER getUI()
				if ( iYAxesCount > 1
						&& ( lblMultipleY != null && !lblMultipleY.isDisposed( ) ) )
				{
					lblMultipleY.setEnabled( !is3D( ) );
					cbMultipleY.setEnabled( !is3D( ) );
					lblSeriesType.setEnabled( !is3D( ) );
					cbSeriesType.setEnabled( !is3D( ) );
					cbMultipleY.setSelection( iYAxesCount > 1 );
					// TODO: Update the series type based on series type for the
					// second Y axis
				}
			}

		}
	}

	public void changeTask( Notification notification )
	{
		if ( previewPainter != null )
		{
			previewPainter.renderModel( chartModel );
		}
	}

	private boolean hasDataSet( )
	{
		return ( (ChartWizardContext) getContext( ) ).getDataServiceProvider( )
				.getReportDataSet( ) != null
				|| ( (ChartWizardContext) getContext( ) ).getDataServiceProvider( )
						.getBoundDataSet( ) != null;
	}

	private void doLivePreview( )
	{
		if ( ChartUIUtil.checkDataBinding( chartModel ) && hasDataSet( ) )
		{
			// Enable live preview
			ChartPreviewPainter.setEnableLivePreview( true );
			// Make sure not affect model changed
			ChartAdapter.ignoreNotifications( true );
			try
			{
				ChartUIUtil.doLivePreview( chartModel,
						( (ChartWizardContext) getContext( ) ).getDataServiceProvider( ) );
			}
			// Includes RuntimeException
			catch ( Exception e )
			{
				// Enable sample data instead
				ChartPreviewPainter.setEnableLivePreview( false );
			}
			ChartAdapter.ignoreNotifications( false );
		}
		else
		{
			// Disable live preview
			ChartPreviewPainter.setEnableLivePreview( false );
		}
		changeTask( null );
	}

	private String getSubtypeFromButton( Control button )
	{
		return (String) button.getData( );
	}

}
