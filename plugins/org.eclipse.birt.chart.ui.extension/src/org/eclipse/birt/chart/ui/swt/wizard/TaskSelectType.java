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

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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
public class TaskSelectType extends SimpleTask implements
		SelectionListener,
		ITaskChangeListener
{

	private Chart chartModel = null;

	private Composite cmpPreview = null;

	private Composite cmpType = null;

	private Composite cmpMisc = null;

	private Composite cmpRight = null;

	private Composite cmpLeft = null;

	private Composite cmpTypeButtons = null;

	private Composite cmpSubTypes = null;

	private Canvas previewCanvas = null;

	private ChartPreviewPainter previewPainter = null;

	private LinkedHashMap htTypes = null;

	private RowData rowData = new RowData( 80, 80 );

	private String sSubType = null;

	private String sType = null;

	private String sOldType = null;

	// Stored in IChartType
	private String sDimension = null;

	private Table table = null;

	private Vector vSubTypeNames = null;

	private Orientation orientation = null;

	private Label lblOrientation = null;
	private Button cbOrientation = null;

	private Label lblMultipleY = null;
	private Combo cbMultipleY = null;

	private Label lblSeriesType = null;
	private Combo cbSeriesType = null;

	private Label lblDimension = null;
	private Combo cbDimension = null;

	private Label lblOutput = null;
	private Combo cbOutput = null;

	private SashForm foSashForm;

	private static final String LEADING_BLANKS = "  "; //$NON-NLS-1$

	private static Hashtable htSeriesNames = null;

	public TaskSelectType( )
	{
		super( Messages.getString( "TaskSelectType.TaskExp" ) ); //$NON-NLS-1$
		setDescription( Messages.getString( "TaskSelectType.Task.Description" ) ); //$NON-NLS-1$

		if ( chartModel != null )
		{
			sType = chartModel.getType( );
			sOldType = sType;
			sSubType = chartModel.getSubType( );
			sDimension = translateDimensionString( chartModel.getDimension( )
					.getName( ) );
			if ( chartModel instanceof ChartWithAxes )
			{
				orientation = ( (ChartWithAxes) chartModel ).getOrientation( );
			}
		}
		htTypes = new LinkedHashMap( );
	}

	public void createControl( Composite parent )
	{
		if ( topControl == null || topControl.isDisposed( ) )
		{
			topControl = new Composite( parent, SWT.NONE );
			GridLayout gridLayout = new GridLayout( 2, false );
			gridLayout.marginWidth = 80;
			topControl.setLayout( gridLayout );
			topControl.setLayoutData( new GridData( GridData.GRAB_HORIZONTAL
					| GridData.GRAB_VERTICAL ) );
			if ( context != null )
			{
				chartModel = ( (ChartWizardContext) context ).getModel( );
			}
			placeComponents( );
			updateAdapters( );
		}

		// Update dimension combo and related sub-types in case of axes changed
		// outside
		if ( ( (ChartWizardContext) getContext( ) ).isMoreAxesSupported( ) )
		{
			updateDimensionCombo( sType );
			createAndDisplayTypesSheet( sType );
			setDefaultSubtypeSelection( );
			cmpMisc.layout( );
		}
		doLivePreview( );

		ChartUIUtil.bindHelp( getControl( ),
				ChartHelpContextIds.TASK_SELECT_TYPE );
	}

	private void placeComponents( )
	{
		foSashForm = new SashForm(topControl, SWT.VERTICAL);
		{
			GridLayout layout = new GridLayout( );
			foSashForm.setLayout( layout );
			GridData gridData = new GridData( GridData.FILL_BOTH );
			gridData.heightHint = 570;
			foSashForm.setLayoutData( gridData );
		}
		
		createPreviewArea( );
		createTypeArea( );
		setDefaultTypeSelection( );

		refreshChart( );
		populateSeriesTypesList( );
		createChartPainter( );
	}

	private void createChartPainter( )
	{
		previewPainter = new ChartPreviewPainter( (ChartWizardContext) getContext( ) );
		previewCanvas.addPaintListener( previewPainter );
		previewCanvas.addControlListener( previewPainter );
		previewPainter.setPreview( previewCanvas );
	}

	private void createPreviewArea( )
	{
		cmpPreview = new Composite( foSashForm, SWT.NONE );
		cmpPreview.setLayout( new GridLayout( ) );

		GridData gridData = new GridData( GridData.FILL_BOTH );
		gridData.horizontalSpan = 2;
		gridData.heightHint = 270;
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
		ScrolledComposite sc = new ScrolledComposite( foSashForm, SWT.V_SCROLL );
		{
			GridLayout layout = new GridLayout( );
			sc.setLayout( layout );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			sc.setLayoutData( gridData );
			sc.setExpandHorizontal( true );
			sc.setExpandVertical( true );
		}
		
		cmpType = new Composite( sc, SWT.NONE );
		cmpType.setLayout( new GridLayout( 2, false ) );
		cmpType.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sc.setContent( cmpType );
		
		createTypeTable( );
		addChartTypes( );

		createDetails( );
		
		Point size = cmpType.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		sc.setMinSize( size );
	}

	private void createDetails( )
	{
		cmpRight = new Composite( cmpType, SWT.NONE );
		cmpRight.setLayout( new GridLayout( ) );
		cmpRight.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		createComposite( new Vector( ) );
		createMiscArea( );
	}

	private void createMiscArea( )
	{
		cmpMisc = new Composite( cmpRight, SWT.NONE );
		cmpMisc.setLayout( new GridLayout( 4, false ) );
		cmpMisc.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		lblDimension = new Label( cmpMisc, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			lblDimension.setLayoutData( gd );
			lblDimension.setText( Messages.getString( "TaskSelectType.Label.Dimension" ) ); //$NON-NLS-1$
		}

		// Add the ComboBox for Dimensions
		cbDimension = new Combo( cmpMisc, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cbDimension.setLayoutData( gd );
			cbDimension.addSelectionListener( this );
		}

		lblOutput = new Label( cmpMisc, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalIndent = 10;
			lblOutput.setLayoutData( gd );
			lblOutput.setText( Messages.getString( "TaskSelectType.Label.OutputFormat" ) ); //$NON-NLS-1$
		}

		// Add the ComboBox for Output Format
		cbOutput = new Combo( cmpMisc, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cbOutput.setLayoutData( gd );
			cbOutput.addSelectionListener( this );
		}

		lblMultipleY = new Label( cmpMisc, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			lblMultipleY.setLayoutData( gd );
			lblMultipleY.setText( Messages.getString( "TaskSelectType.Label.MultipleYAxis" ) ); //$NON-NLS-1$
		}

		// Add the checkBox for Multiple Y Axis
		cbMultipleY = new Combo( cmpMisc, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			cbMultipleY.setItems( new String[]{
					Messages.getString( "TaskSelectType.Selection.None" ), //$NON-NLS-1$
					Messages.getString( "TaskSelectType.Selection.SecondaryAxis" ), //$NON-NLS-1$
					Messages.getString( "TaskSelectType.Selection.MoreAxes" ) //$NON-NLS-1$
			} );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cbMultipleY.setLayoutData( gd );
			cbMultipleY.addSelectionListener( this );

			int axisNum = ChartUIUtil.getOrthogonalAxisNumber( chartModel );
			selectMultipleAxis( axisNum );
		}

		lblSeriesType = new Label( cmpMisc, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalIndent = 10;
			lblSeriesType.setLayoutData( gd );
			lblSeriesType.setText( Messages.getString( "TaskSelectType.Label.SeriesType" ) ); //$NON-NLS-1$
			lblSeriesType.setEnabled( false );
		}

		// Add the ComboBox for Series Type
		cbSeriesType = new Combo( cmpMisc, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cbSeriesType.setLayoutData( gd );
			cbSeriesType.setEnabled( false );
			cbSeriesType.addSelectionListener( this );
		}

		lblOrientation = new Label( cmpMisc, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			lblOrientation.setLayoutData( gd );
			lblOrientation.setText( Messages.getString( "TaskSelectType.Label.Oritention" ) ); //$NON-NLS-1$
		}

		// Add the CheckBox for Orientation
		cbOrientation = new Button( cmpMisc, SWT.CHECK );
		{
			cbOrientation.setText( Messages.getString( "TaskSelectType.Label.FlipAxis" ) ); //$NON-NLS-1$
			GridData gd = new GridData( );
			gd.horizontalSpan = 3;
			cbOrientation.setLayoutData( gd );
			cbOrientation.addSelectionListener( this );
		}

		populateLists( );
	}

	/**
	 * This method initializes table
	 * 
	 */
	private void createTypeTable( )
	{
		cmpLeft = new Composite( cmpType, SWT.NONE );
		cmpLeft.setLayout( new GridLayout( ) );
		cmpLeft.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Label lblTypes = new Label( cmpLeft, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			lblTypes.setLayoutData( gd );
			lblTypes.setText( Messages.getString( "TaskSelectType.Label.SelectChartType" ) ); //$NON-NLS-1$
		}

		table = new Table( cmpLeft, SWT.BORDER);
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			table.setLayoutData( gd );
			table.setToolTipText( Messages.getString( "TaskSelectType.Label.ChartTypes" ) ); //$NON-NLS-1$
			table.addSelectionListener( this );
		}
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
					+ ( (IChartType) htTypes.get( sTypeTmp ) ).getDisplayName( ) );
			tItem.setData( ( (IChartType) htTypes.get( sTypeTmp ) ).getName( ) );
			tItem.setImage( ( (IChartType) htTypes.get( sTypeTmp ) ).getImage( ) );
		}
	}

	/**
	 * This method initializes cmpSubTypes
	 * 
	 */
	private void createComposite( Vector vSubTypes )
	{
		Label lblSubtypes = new Label( cmpRight, SWT.NO_FOCUS );
		{
			lblSubtypes.setText( Messages.getString( "TaskSelectType.Label.SelectSubtype" ) ); //$NON-NLS-1$
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalIndent = 5;
			lblSubtypes.setLayoutData( gd );
		}

		GridData gdTypes = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL );
		cmpSubTypes = new Composite( cmpRight, SWT.NONE );
		createGroups( vSubTypes );
		cmpSubTypes.setLayoutData( gdTypes );
		cmpSubTypes.setToolTipText( Messages.getString( "TaskSelectType.Label.ChartSubtypes" ) ); //$NON-NLS-1$
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
		if ( this.orientation == Orientation.HORIZONTAL_LITERAL )
		{
			cbOrientation.setSelection( true );
		}
		else
		{
			cbOrientation.setSelection( false );
		}

		cbOutput.setItems( getOutputFormats( ) );

		String sCurrentFormat = ( (ChartWizardContext) getContext( ) ).getOutputFormat( );
		int index = cbOutput.indexOf( sCurrentFormat );
		if ( index == -1 )
		{
			index = cbOutput.indexOf( ( (ChartWizardContext) getContext( ) ).getDefaultOutputFormat( ) );
		}
		cbOutput.select( index );

	}

	private String[] getOutputFormats( )
	{
		boolean bException = false;
		try
		{
			return ChartUtil.getSupportedOutputFormats( );
		}
		catch ( ChartException e )
		{
			bException = true;
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
		if ( !bException )
		{
			WizardBase.removeException( );
		}
		return new String[0];
	}

	private void populateSeriesTypes( Collection allChartType, Series series,
			Orientation orientation )
	{
		Iterator iterTypes = allChartType.iterator( );
		while ( iterTypes.hasNext( ) )
		{
			IChartType type = (IChartType) iterTypes.next( );
			Series newSeries = type.getSeries( );

			if ( htSeriesNames == null )
			{
				htSeriesNames = new Hashtable( 20 );
			}

			if ( newSeries.canParticipateInCombination( ) )
			{
				if ( !( newSeries instanceof StockSeries )
						|| ( orientation.getValue( ) == Orientation.VERTICAL ) )
				{
					String sDisplayName = newSeries.getDisplayName( );
					htSeriesNames.put( sDisplayName, newSeries );
					cbSeriesType.add( sDisplayName );
				}
				if ( type.getName( ).equals( chartModel.getType( ) ) )
				{
					cbSeriesType.select( cbSeriesType.getItemCount( ) - 1 );
				}
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
					orientation = Orientation.HORIZONTAL_LITERAL;
				}
				else
				{
					orientation = Orientation.VERTICAL_LITERAL;
				}
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );
				populateSeriesTypesList( );
				ChartCacheManager.getInstance( ).cacheOrientation( sType,
						orientation );
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

					// Cache label position for stacked or non-stacked case.
					ChartUIUtil.saveLabelPositionIntoCache( getSeriesDefinitionForProcessing( ) );

					sSubType = getSubtypeFromButton( btn );
					ChartCacheManager.getInstance( ).cacheSubtype( sType,
							sSubType );
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
			sType = ( (String) ( (TableItem) e.item ).getData( ) ).trim( );
			if ( !sOldType.equals( sType ) )
			{
				sOldType = sType;
				// Get the cached orientation
				this.orientation = ChartCacheManager.getInstance( )
						.findOrientation( sType );

				if ( chartModel != null
						&& chartModel instanceof ChartWithAxes
						&& ChartCacheManager.getInstance( )
								.findCategory( sType ) != null )
				{
					boolean bCategory = ChartCacheManager.getInstance( )
							.findCategory( sType )
							.booleanValue( );
					( (Axis) ( (ChartWithAxes) chartModel ).getAxes( ).get( 0 ) ).setCategoryAxis( bCategory );
				}
				sSubType = null;
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );

				cmpMisc.layout( );

				needUpdateModel = true;
			}
		}
		else if ( oSelected.equals( cbMultipleY ) )
		{
			needUpdateModel = true;
			lblSeriesType.setEnabled( isTwoAxesEnabled( ) );

			Axis xAxis = ( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 ) );

			( (ChartWizardContext) getContext( ) ).setMoreAxesSupported( cbMultipleY.getSelectionIndex( ) == 2 );

			if ( chartModel instanceof ChartWithoutAxes )
			{
				throw new IllegalArgumentException( Messages.getString( "TaskSelectType.Exception.CannotSupportAxes" ) ); //$NON-NLS-1$
			}

			// Prevent notifications rendering preview
			ChartAdapter.beginIgnoreNotifications( );
			int iAxisNumber = ChartUIUtil.getOrthogonalAxisNumber( chartModel );
			if ( cbMultipleY.getSelectionIndex( ) == 0 )
			{
				// Keeps one axis
				if ( iAxisNumber > 1 )
				{
					ChartUIUtil.removeLastAxes( (ChartWithAxes) chartModel,
							iAxisNumber - 1 );
				}
			}
			else if ( cbMultipleY.getSelectionIndex( ) == 1 )
			{
				// Keeps two axes
				if ( iAxisNumber == 1 )
				{
					ChartUIUtil.addAxis( (ChartWithAxes) chartModel );
				}
				else if ( iAxisNumber > 2 )
				{
					ChartUIUtil.removeLastAxes( (ChartWithAxes) chartModel,
							iAxisNumber - 2 );
				}
			}
			ChartAdapter.endIgnoreNotifications( );

			if ( xAxis.getAssociatedAxes( ).size( ) > 1 )
			{
				Axis overlayAxis = (Axis) xAxis.getAssociatedAxes( ).get( 1 );
				String sDisplayName = ( (SeriesDefinition) overlayAxis.getSeriesDefinitions( )
						.get( 0 ) ).getDesignTimeSeries( ).getDisplayName( );
				cbSeriesType.setText( sDisplayName );
			}
			cbSeriesType.setEnabled( isTwoAxesEnabled( ) );

			// Update dimension combo and related sub-types
			if ( updateDimensionCombo( sType ) )
			{
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );
			}

			// Pack to display enough space for combo
			cmpMisc.layout( );
		}
		else if ( oSelected.equals( cbDimension ) )
		{
			String newDimension = cbDimension.getItem( cbDimension.getSelectionIndex( ) );
			if ( !newDimension.equals( sDimension ) )
			{
				sDimension = newDimension;
				createAndDisplayTypesSheet( sType );
				setDefaultSubtypeSelection( );

				needUpdateModel = true;
			}
		}
		else if ( oSelected.equals( cbSeriesType ) )
		{
// if ( !cbSeriesType.getText( ).equals( oldSeriesName ) )
// {
			needUpdateModel = true;
			changeOverlaySeriesType( );
// }
		}
		else if ( oSelected.equals( cbOutput ) )
		{
			( (ChartWizardContext) getContext( ) ).setOutputFormat( cbOutput.getText( ) );
			// Update apply button
			( (ChartWizard) container ).updateApplayButton( );
		}

		// Following operations need new model
		if ( needUpdateModel )
		{
			// Update apply button
			ChartAdapter.notifyUpdateApply( );
			// Update chart model
			refreshChart( );

			if ( oSelected.getClass( ).equals( Table.class ) )
			{
				// Ensure populate list after chart model generated
				populateSeriesTypesList( );

				// Auto rotates Axis title when changing chart type that may
				// cause transposition
				if ( chartModel instanceof ChartWithAxes )
				{
					rotateAxisTitle( (ChartWithAxes) chartModel );
				}
			}
			else if ( oSelected.equals( cbOrientation ) )
			{
				// Auto rotates Axis title when transposing
				if ( chartModel instanceof ChartWithAxes )
				{
					rotateAxisTitle( (ChartWithAxes) chartModel );
				}
			}
		}
	}

	/**
	 * Updates the dimension combo according to chart type and axes number
	 * 
	 * @param sSelectedType
	 *            Chart type
	 * @return whether the dimension is changed after updating
	 */
	private boolean updateDimensionCombo( String sSelectedType )
	{
		// Remember last selection
		boolean isOldExist = false;

		// Update valid dimension list
		IChartType chartType = (IChartType) htTypes.get( sSelectedType );
		String[] dimensionArray = chartType.getSupportedDimensions( );
		int axesNum = ChartUIUtil.getOrthogonalAxisNumber( chartModel );

		if ( sDimension == null )
		{
			// Initialize dimension
			sDimension = chartType.getDefaultDimension( );
			isOldExist = true;
		}
		cbDimension.removeAll( );
		for ( int i = 0; i < dimensionArray.length; i++ )
		{
			boolean isSupported = chartType.isDimensionSupported( dimensionArray[i],
					axesNum,
					0 );
			if ( isSupported )
			{
				cbDimension.add( dimensionArray[i] );
			}
			if ( !isOldExist && sDimension.equals( dimensionArray[i] ) )
			{
				isOldExist = isSupported;
			}
		}

		// Select the previous selection or the default
		if ( !isOldExist )
		{
			sDimension = chartType.getDefaultDimension( );
		}
		cbDimension.setText( sDimension );
		return !isOldExist;
	}

	private boolean isTwoAxesEnabled( )
	{
		return cbMultipleY.getSelectionIndex( ) == 1;
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
		boolean bException = false;
		try
		{
			// CHANGE ALL OVERLAY SERIES TO NEW SELECTED TYPE
			Axis XAxis = (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
					.get( 0 );
			int iSeriesDefinitionIndex = 0 + ( (Axis) XAxis.getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).size( ); // SINCE
			// THIS IS FOR THE ORTHOGONAL OVERLAY SERIES DEFINITION
			int iOverlaySeriesCount = ( (Axis) XAxis.getAssociatedAxes( )
					.get( 1 ) ).getSeriesDefinitions( ).size( );
			// DISABLE NOTIFICATIONS WHILE MODEL UPDATE TAKES PLACE
			ChartAdapter.beginIgnoreNotifications( );
			for ( int i = 0; i < iOverlaySeriesCount; i++ )
			{
				Series newSeries = (Series) htSeriesNames.get( cbSeriesType.getText( ) );
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
			bException = true;
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
		finally
		{
			// ENABLE NOTIFICATIONS IN CASE EXCEPTIONS OCCUR
			ChartAdapter.endIgnoreNotifications( );
		}

		if ( !bException )
		{
			ChartWizard.removeException( );
		}
	}

	private void populateSeriesTypesList( )
	{
		// Populate Series Types List
		cbSeriesType.removeAll( );
		Series series = getSeriesDefinitionForProcessing( ).getDesignTimeSeries( );
		if ( series.canParticipateInCombination( ) )
		{
			populateSeriesTypes( ChartUIExtensionsImpl.instance( )
					.getUIChartTypeExtensions( ), series, this.orientation );
		}
		else
		{
			String seriesName = series.getDisplayName( );;
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
				if ( !overlayAxis.getSeriesDefinitions( ).isEmpty( ) )
				{
					String sDisplayName = ( (SeriesDefinition) overlayAxis.getSeriesDefinitions( )
							.get( 0 ) ).getDesignTimeSeries( ).getDisplayName( );
					cbSeriesType.setText( sDisplayName );
				}
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

		// Update dimension
		updateDimensionCombo( sSelectedType );

		if ( this.sDimension == null )
		{
			this.sDimension = chartType.getDefaultDimension( );
		}
		if ( this.orientation == null )
		{
			this.orientation = chartType.getDefaultOrientation( );
		}

		// Show the subtypes for the selected type based on current selections
		// of dimension and orientation
		Vector vSubTypes = new Vector( chartType.getChartSubtypes( sDimension,
				orientation ) );

		if ( vSubTypes.size( ) == 0 )
		{
			vSubTypes = new Vector( chartType.getChartSubtypes( chartType.getDefaultDimension( ),
					chartType.getDefaultOrientation( ) ) );
			this.sDimension = chartType.getDefaultDimension( );
			this.orientation = chartType.getDefaultOrientation( );
		}

		// If two orientations are not supported, to get the default.
		if ( !cbOrientation.isEnabled( ) )
		{
			this.orientation = chartType.getDefaultOrientation( );
		}
		// Cache the orientation for each chart type.
		ChartCacheManager.getInstance( ).cacheOrientation( sType, orientation );

		if ( chartModel == null )
		{
			ChartCacheManager.getInstance( ).cacheCategory( sType, true );
		}
		else if ( chartModel instanceof ChartWithAxes )
		{
			ChartCacheManager.getInstance( )
					.cacheCategory( sType,
							( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
									.get( 0 ) ).isCategoryAxis( ) );
		}

		// Update the UI with information for selected type
		createGroups( vSubTypes );
		if ( this.orientation == Orientation.HORIZONTAL_LITERAL )
		{
			this.cbOrientation.setSelection( true );
		}
		else
		{
			this.cbOrientation.setSelection( false );
		}
		cmpRight.layout( );
	}

	private void setDefaultSubtypeSelection( )
	{
		if ( sSubType == null )
		{
			// Try to get cached subtype
			sSubType = ChartCacheManager.getInstance( ).findSubtype( sType );
		}

		if ( sSubType == null )
		{
			// Get the default subtype
			( (Button) cmpTypeButtons.getChildren( )[0] ).setSelection( true );
			sSubType = getSubtypeFromButton( cmpTypeButtons.getChildren( )[0] );
			ChartCacheManager.getInstance( ).cacheSubtype( sType, sSubType );
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
				ChartCacheManager.getInstance( ).cacheSubtype( sType, sSubType );
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
				sType = (String) ( table.getSelection( )[0] ).getData( );
			}
			else
			{
				TableItem[] tiAll = table.getItems( );
				for ( int iTI = 0; iTI < tiAll.length; iTI++ )
				{
					if ( tiAll[iTI].getData( ).equals( sType ) )
					{
						table.select( iTI );
						break;
					}
				}
			}
			sOldType = sType;
			createAndDisplayTypesSheet( sType );
			setDefaultSubtypeSelection( );
		}
	}

	public void dispose( )
	{
		super.dispose( );
		// No need to dispose other widgets
		chartModel = null;
		if ( previewPainter != null )
		{
			previewPainter.dispose( );
		}
		previewPainter = null;
		sSubType = null;
		sType = null;
		sDimension = null;
		vSubTypeNames = null;
		orientation = null;
	}

	private void refreshChart( )
	{
		// DISABLE PREVIEW REFRESH DURING CONVERSION
		ChartAdapter.beginIgnoreNotifications( );
		IChartType chartType = (IChartType) htTypes.get( sType );
		boolean bException = false;
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
			bException = true;
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
		if ( !bException )
		{
			WizardBase.removeException( );
		}

		// RE-ENABLE PREVIEW REFRESH
		ChartAdapter.endIgnoreNotifications( );

		updateSelection( );
		( (ChartWizardContext) context ).setModel( chartModel );
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
			lblSeriesType.setEnabled( isTwoAxesEnabled( ) );
			cbSeriesType.setEnabled( isTwoAxesEnabled( ) );
		}
		else
		{
			cbMultipleY.select( 0 );
			( (ChartWizardContext) getContext( ) ).setMoreAxesSupported( false );

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
		context.setModel( this.chartModel );
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
			this.sType = ( (ChartWizardContext) context ).getChartType( )
					.getName( );
			this.sSubType = chartModel.getSubType( );
			this.sDimension = translateDimensionString( chartModel.getDimension( )
					.getName( ) );
			if ( chartModel instanceof ChartWithAxes )
			{
				this.orientation = ( (ChartWithAxes) chartModel ).getOrientation( );
				int iYAxesCount = ChartUIUtil.getOrthogonalAxisNumber( chartModel );
				// IF THE UI HAS BEEN INITIALIZED...I.E. IF setContext() IS
				// CALLED AFTER getUI()
				if ( iYAxesCount > 1
						&& ( lblMultipleY != null && !lblMultipleY.isDisposed( ) ) )
				{
					lblMultipleY.setEnabled( !is3D( ) );
					cbMultipleY.setEnabled( !is3D( ) );
					lblSeriesType.setEnabled( !is3D( ) && isTwoAxesEnabled( ) );
					cbSeriesType.setEnabled( !is3D( ) && isTwoAxesEnabled( ) );
					selectMultipleAxis( iYAxesCount );
					// TODO: Update the series type based on series type for the
					// second Y axis
				}
			}

		}
	}

	private void selectMultipleAxis( int yAxisNum )
	{
		if ( ( (ChartWizardContext) getContext( ) ).isMoreAxesSupported( ) )
		{
			cbMultipleY.select( 2 );
		}
		else
		{
			if ( yAxisNum > 2 )
			{
				cbMultipleY.select( 2 );
				( (ChartWizardContext) getContext( ) ).setMoreAxesSupported( true );
			}
			else
			{
				cbMultipleY.select( yAxisNum > 0 ? yAxisNum - 1 : 0 );
			}
		}
	}

	public void changeTask( Notification notification )
	{
		if ( previewPainter != null )
		{
			// To update data type after chart type conversion
			if ( chartModel instanceof ChartWithAxes )
			{
				checkDataTypeForChartWithAxes( );
			}
			previewPainter.renderModel( chartModel );
		}
	}

	private void checkDataTypeForChartWithAxes( )
	{
		// To check the data type of base series and orthogonal series in chart
		// with axes
		List sdList = new ArrayList( );
		sdList.addAll( ChartUIUtil.getBaseSeriesDefinitions( chartModel ) );
		sdList.addAll( ChartUIUtil.getAllOrthogonalSeriesDefinitions( chartModel ) );
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = (SeriesDefinition) sdList.get( i );
			Series series = sd.getDesignTimeSeries( );
			checkDataType( ChartUIUtil.getDataQuery( sd, 0 ), series );
		}
	}

	protected IDataServiceProvider getDataServiceProvider( )
	{
		return ( (ChartWizardContext) getContext( ) ).getDataServiceProvider( );
	}

	private void doLivePreview( )
	{
		if ( getDataServiceProvider( ).isLivePreviewEnabled( )
				&& ChartUIUtil.checkDataBinding( chartModel ) )
		{
			// Enable live preview
			ChartPreviewPainter.activateLivePreview( true );
			// Make sure not affect model changed
			ChartAdapter.beginIgnoreNotifications( );
			try
			{
				ChartUIUtil.doLivePreview( chartModel, getDataServiceProvider( ) );
			}
			// Includes RuntimeException
			catch ( Exception e )
			{
				// Enable sample data instead
				ChartPreviewPainter.activateLivePreview( false );
			}
			ChartAdapter.endIgnoreNotifications( );
		}
		else
		{
			// Disable live preview
			ChartPreviewPainter.activateLivePreview( false );
		}
		changeTask( null );
	}

	private String getSubtypeFromButton( Control button )
	{
		return (String) button.getData( );
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
				}

				if ( !bException )
				{
					WizardBase.removeException( );
				}

				if ( chartModel instanceof ChartWithAxes )
				{
					DataType dataType = getDataServiceProvider( ).getDataType( expression );
					SeriesDefinition sd = (SeriesDefinition) ( ChartUIUtil.getBaseSeriesDefinitions( chartModel ).get( 0 ) );
					if ( sd != null )
					{
						if ( sd.getGrouping( ).isEnabled( )
								&& ( sd.getGrouping( )
										.getAggregateExpression( )
										.equals( "Count" )//$NON-NLS-1$
								|| sd.getGrouping( )
										.getAggregateExpression( )
										.equals( "DistinctCount" ) ) ) //$NON-NLS-1$
						{
							dataType = DataType.NUMERIC_LITERAL;
						}
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
							// Avoid modifying model to notify an event loop
							ChartAdapter.beginIgnoreNotifications( );
							axis.setType( axisTypes[i] );
							ChartAdapter.endIgnoreNotifications( );
							break;
						}
					}
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
			BaseSampleData bsd = (BaseSampleData) chartModel.getSampleData( )
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

			int iOSDSize = chartModel.getSampleData( )
					.getOrthogonalSampleData( )
					.size( );
			for ( int i = 0; i < iOSDSize; i++ )
			{
				OrthogonalSampleData osd = (OrthogonalSampleData) chartModel.getSampleData( )
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
		List axisList = ( (Axis) ( (ChartWithAxes) chartModel ).getAxes( )
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
			iTmp += ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) chartModel,
					i )
					.getSeriesDefinitions( )
					.size( );
		}
		return iTmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask#getImage()
	 */
	public Image getImage( )
	{
		return UIHelper.getImage( "icons/obj16/selecttype.gif" ); //$NON-NLS-1$
	}

	/**
	 * Rotates Axis Title when transposing
	 * 
	 * @param cwa
	 *            chart model
	 */
	private void rotateAxisTitle( ChartWithAxes cwa )
	{
		boolean bRender = false;
		ChartAdapter.beginIgnoreNotifications( );
		boolean bVertical = orientation.getValue( ) == Orientation.VERTICAL;
		Axis aX = ChartUIUtil.getAxisXForProcessing( cwa );
		if ( aX.getTitle( ).isVisible( ) )
		{
			bRender = true;
			aX.getTitle( ).getCaption( ).getFont( ).setRotation( bVertical ? 0
					: 90 );
		}
		EList aYs = aX.getAssociatedAxes( );
		for ( int i = 0; i < aYs.size( ); i++ )
		{
			Axis aY = (Axis) aYs.get( i );
			if ( aY.getTitle( ).isVisible( ) )
			{
				bRender = true;
				aY.getTitle( )
						.getCaption( )
						.getFont( )
						.setRotation( bVertical ? 90 : 0 );
			}
		}
		ChartAdapter.endIgnoreNotifications( );
		if ( bRender )
		{
			previewPainter.renderModel( chartModel );
		}
	}

}