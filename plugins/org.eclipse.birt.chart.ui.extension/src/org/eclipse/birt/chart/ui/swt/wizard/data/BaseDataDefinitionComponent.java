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

package org.eclipse.birt.chart.ui.swt.wizard.data;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DataTextDropListener;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.IQueryExpressionManager;
import org.eclipse.birt.chart.ui.swt.SimpleTextTransfer;
import org.eclipse.birt.chart.ui.swt.composites.BaseGroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.composites.GroupSortingDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class BaseDataDefinitionComponent extends DefaultSelectDataComponent implements
		SelectionListener,
		ModifyListener,
		FocusListener,
		KeyListener,
		IQueryExpressionManager
{

	protected Composite cmpTop;

	private CCombo cmbDefinition;

	protected Text txtDefinition = null;

	private Button btnBuilder = null;

	private Button btnGroup = null;

	protected Query query = null;

	protected SeriesDefinition seriesdefinition = null;

	protected ChartWizardContext context = null;

	private String sTitle = null;

	private String description = ""; //$NON-NLS-1$

	private String tooltipWhenBlank = Messages.getString( "BaseDataDefinitionComponent.Tooltip.InputValueExpression" ); //$NON-NLS-1$

	protected boolean isQueryModified;

	private final String queryType;

	private int style = BUTTON_NONE;

	private AggregateEditorComposite fAggEditorComposite;

	/** Indicates no button */
	public static final int BUTTON_NONE = 0;

	/** Indicates button for group sorting will be created */
	public static final int BUTTON_GROUP = 1;

	/** Indicates button for aggregation will be created */
	public static final int BUTTON_AGGREGATION = 2;

	/**
	 * 
	 * @param queryType
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param sTitle
	 */
	public BaseDataDefinitionComponent( String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String sTitle )
	{
		this( BUTTON_NONE, queryType, seriesdefinition, query, context, sTitle );
	}

	/**
	 * 
	 * @param style
	 *            Specify buttons by using '|'. See {@link #BUTTON_GROUP},
	 *            {@link #BUTTON_NONE}, {@link #BUTTON_AGGREGATION}
	 * @param queryType
	 *            query type. See {@link ChartUIConstants#QUERY_CATEGORY},
	 *            {@link ChartUIConstants#QUERY_VALUE},
	 *            {@link ChartUIConstants#QUERY_OPTIONAL}
	 * @param seriesdefinition
	 * @param query
	 * @param context
	 * @param sTitle
	 */
	public BaseDataDefinitionComponent( int style, String queryType,
			SeriesDefinition seriesdefinition, Query query,
			ChartWizardContext context, String sTitle )
	{
		super( );
		assert query != null;
		this.query = query;
		this.queryType = queryType;
		this.seriesdefinition = seriesdefinition;
		this.context = context;
		this.sTitle = ( sTitle == null || sTitle.length( ) == 0 ) ? Messages.getString( "BaseDataDefinitionComponent.Text.SpecifyDataDefinition" ) //$NON-NLS-1$
				: sTitle;
		this.style = style;
	}

	public Composite createArea( Composite parent )
	{
		int numColumns = 2;
		if ( description != null && description.length( ) > 0 )
		{
			numColumns++;
		}
		if ( ( style & BUTTON_AGGREGATION ) == BUTTON_AGGREGATION )
		{
			numColumns++;
		}
		if ( ( style & BUTTON_GROUP ) == BUTTON_GROUP )
		{
			numColumns++;
		}

		cmpTop = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( );
			glContent.numColumns = numColumns;
			glContent.marginHeight = 0;
			glContent.marginWidth = 0;
			glContent.horizontalSpacing = 2;
			cmpTop.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpTop.setLayoutData( gd );
		}

		if ( description != null && description.length( ) > 0 )
		{
			Label lblDesc = new Label( cmpTop, SWT.NONE );
			lblDesc.setText( description );
			lblDesc.setToolTipText( tooltipWhenBlank );
		}

		if ( ( style & BUTTON_AGGREGATION ) == BUTTON_AGGREGATION )
		{
			createAggregationItem( cmpTop );
		}

		Object[] predefinedQuery = context.getPredefinedQuery( queryType );
		if ( predefinedQuery != null )
		{
			cmbDefinition = new CCombo( cmpTop,
					context.getDataServiceProvider( )
							.checkState( IDataServiceProvider.PART_CHART ) ? SWT.READ_ONLY
							| SWT.BORDER
							: SWT.BORDER );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 80;
			gd.grabExcessHorizontalSpace = true;
			cmbDefinition.setLayoutData( gd );

			if ( predefinedQuery.length > 0 )
			{
				populateExprComboItems( predefinedQuery );
			}
			else if ( context.getDataServiceProvider( )
					.checkState( IDataServiceProvider.SHARE_QUERY ) )
			{
				// The sharing binding case only allow valid expressions, so
				// disable the component if not have valid expressions.
				cmbDefinition.setEnabled( false );
			}			

			cmbDefinition.addListener( SWT.Selection, new Listener( ) {

				public void handleEvent( Event event )
				{
					String oldQuery = query.getDefinition( ) == null ? "" : query.getDefinition( ); //$NON-NLS-1$

					// Do nothing for the same query
					if ( cmbDefinition.getText( ).equals( oldQuery ) )
					{
						return;
					}

					Object checkResult = context.getDataServiceProvider( )
							.checkData( queryType, cmbDefinition.getText( ) );
					if ( checkResult != null && checkResult instanceof Boolean )
					{
						if ( !( (Boolean) checkResult ).booleanValue( ) )
						{
							// Can't select expressions of one dimension to set
							// on category series and Y optional at one time.
							WizardBase.showException( Messages.getString( "BaseDataDefinitionComponent.WarningMessage.ExpressionsForbidden" ) ); //$NON-NLS-1$
							cmbDefinition.setText( oldQuery );
							return;
						}
						else
						{
							WizardBase.removeException( );
						}
					}

					updateQuery( cmbDefinition.getText( ) );

					// Set category/Y optional expression by value series
					// expression if it is crosstab sharing.
					if ( !oldQuery.equals( cmbDefinition.getText( ) )
							&& queryType == ChartUIConstants.QUERY_VALUE )
					{
						if ( context.getDataServiceProvider( )
								.update( ChartUIConstants.QUERY_VALUE,
										cmbDefinition.getText( ) ) )
						{
							Event e = new Event( );
							e.data = BaseDataDefinitionComponent.this;
							e.widget = cmbDefinition;
							e.type = IChartDataSheet.EVENT_QUERY;
							context.getDataSheet( ).notifyListeners( e );
						}
					}

					// Change direction once category query is changed in xtab
					// case
					if ( context.getDataServiceProvider( )
							.checkState( IDataServiceProvider.PART_CHART )
							&& ChartUIConstants.QUERY_CATEGORY.equals( queryType )
							&& context.getModel( ) instanceof ChartWithAxes )
					{
						( (ChartWithAxes) context.getModel( ) ).setTransposed( cmbDefinition.getSelectionIndex( ) > 0 );
					}
				}
			} );

			cmbDefinition.addModifyListener( this );
			cmbDefinition.addFocusListener( this );
			cmbDefinition.addKeyListener( this );
			
			initComboExprText( );
		}
		else
		{
			txtDefinition = new Text( cmpTop, SWT.BORDER | SWT.SINGLE );
			GridData gdTXTDefinition = new GridData( GridData.FILL_HORIZONTAL );
			gdTXTDefinition.widthHint = 80;
			gdTXTDefinition.grabExcessHorizontalSpace = true;
			txtDefinition.setLayoutData( gdTXTDefinition );
			if ( query != null && query.getDefinition( ) != null )
			{
				txtDefinition.setText( query.getDefinition( ) );
				txtDefinition.setToolTipText( getTooltipForDataText( query.getDefinition( ) ) );
			}
			txtDefinition.addModifyListener( this );
			txtDefinition.addFocusListener( this );
			txtDefinition.addKeyListener( this );
		}

		// Listener for handling dropping of custom table header
		Control dropControl = getInputControl( );
		DropTarget target = new DropTarget( dropControl, DND.DROP_COPY );
		Transfer[] types = new Transfer[]{
			SimpleTextTransfer.getInstance( )
		};
		target.setTransfer( types );
		// Add drop support
		target.addDropListener( new DataTextDropListener( dropControl ) );
		// Add color manager
		DataDefinitionTextManager.getInstance( )
				.addDataDefinitionText( dropControl, this );

		btnBuilder = new Button( cmpTop, SWT.PUSH );
		{
			GridData gdBTNBuilder = new GridData( );
			ChartUIUtil.setChartImageButtonSizeByPlatform( gdBTNBuilder );
			btnBuilder.setLayoutData( gdBTNBuilder );
			btnBuilder.setImage( UIHelper.getImage( "icons/obj16/expressionbuilder.gif" ) ); //$NON-NLS-1$
			btnBuilder.addSelectionListener( this );
			btnBuilder.setToolTipText( Messages.getString( "DataDefinitionComposite.Tooltip.InvokeExpressionBuilder" ) ); //$NON-NLS-1$
			btnBuilder.getImage( ).setBackground( btnBuilder.getBackground( ) );
			btnBuilder.setEnabled( context.getUIServiceProvider( )
					.isInvokingSupported( ) );
			btnBuilder.setVisible( context.getUIServiceProvider( )
					.isEclipseModeSupported( ) );
		}

		if ( ( style & BUTTON_GROUP ) == BUTTON_GROUP )
		{
			btnGroup = new Button( cmpTop, SWT.PUSH );
			GridData gdBTNGroup = new GridData( );
			ChartUIUtil.setChartImageButtonSizeByPlatform( gdBTNGroup );
			btnGroup.setLayoutData( gdBTNGroup );
			btnGroup.setImage( UIHelper.getImage( "icons/obj16/group.gif" ) ); //$NON-NLS-1$
			btnGroup.addSelectionListener( this );
			btnGroup.setToolTipText( Messages.getString( "BaseDataDefinitionComponent.Label.EditGroupSorting" ) ); //$NON-NLS-1$
		}

		// Updates color setting
		setColor( );

		// In shared binding, only support predefined query
		IDataServiceProvider provider = context.getDataServiceProvider( );
		boolean isCubeNoMultiDimensions = ( provider.checkState( IDataServiceProvider.HAS_CUBE ) || provider.checkState( IDataServiceProvider.SHARE_CROSSTAB_QUERY ) )
				&& !provider.checkState( IDataServiceProvider.MULTI_CUBE_DIMENSIONS );
		if ( context.getDataServiceProvider( )
				.checkState( IDataServiceProvider.PART_CHART )
				|| context.getDataServiceProvider( )
						.checkState( IDataServiceProvider.SHARE_QUERY ) )
		{
			// Sharing query with crosstab allows user to edit category and Y
			// optional expression, so here doesn't disable the text field if it
			// is SHARE_CROSSTAB_QUERY.
			if ( txtDefinition != null
					&& !context.getDataServiceProvider( )
							.checkState( IDataServiceProvider.SHARE_CROSSTAB_QUERY ) )
			{
				txtDefinition.setEnabled( false );
			}

			btnBuilder.setEnabled( false );
			if ( btnGroup != null )
			{
				btnGroup.setEnabled( false );
			}
		}
		if ( cmbDefinition != null
				&& ChartUIConstants.QUERY_OPTIONAL.equals( queryType )
				&& isCubeNoMultiDimensions )
		{
			cmbDefinition.setEnabled( false );
		}

		setTooltipForInputControl( );
		return cmpTop;
	}

	/**
	 * Initialize combo text and data.
	 */
	private void initComboExprText( )
	{
		if ( isTableSharedBinding( ) )
		{
			initComboExprTextForSharedBinding( );
		}
		else
		{
			cmbDefinition.setText( query.getDefinition( ) );
		}
	}

	/**
	 * Check if current is using table shared binding.
	 * 
	 * @return
	 * @since 2.3
	 */
	private boolean isTableSharedBinding( )
	{
		return context.getDataServiceProvider( )
				.checkState( IDataServiceProvider.SHARE_QUERY )
				&& cmbDefinition != null
				&& cmbDefinition.getData( ) != null;
	}

	/**
	 * Initialize combo text and data for shared binding.
	 */
	private void initComboExprTextForSharedBinding( )
	{
		setUITextForSharedBinding( cmbDefinition, query.getDefinition( ) );
	}

	/**
	 * Populate expression items for combo.
	 * 
	 * @param predefinedQuery
	 */
	private void populateExprComboItems( Object[] predefinedQuery )
	{

		if ( predefinedQuery[0] instanceof Object[] )
		{
			String[] items = new String[predefinedQuery.length];
			Object[] data = new Object[predefinedQuery.length];
			for ( int i = 0; i < items.length; i++ )
			{
				items[i] = (String) ( (Object[]) predefinedQuery[i] )[0];
				data[i] = ( (Object[]) predefinedQuery[i] )[1];
			}

			cmbDefinition.setItems( items );
			cmbDefinition.setData( data );
		}
		else if ( predefinedQuery[0] instanceof String )
		{
			String[] items = new String[predefinedQuery.length];
			for ( int i = 0; i < items.length; i++ )
			{
				items[i] = (String) predefinedQuery[i];
			}
			cmbDefinition.setItems( items );
		}
	}

	public void selectArea( boolean selected, Object data )
	{
		if ( data instanceof Object[] )
		{
			Object[] array = (Object[]) data;
			seriesdefinition = (SeriesDefinition) array[0];
			query = (Query) array[1];
			setUIText( getInputControl( ), query.getDefinition( ) );
			DataDefinitionTextManager.getInstance( )
					.addDataDefinitionText( getInputControl( ), this );
			if ( fAggEditorComposite != null )
			{
				fAggEditorComposite.setSeriesDefinition( seriesdefinition );
			}
		}
		setColor( );
	}

	private void setColor( )
	{
		if ( query != null )
		{
			Color cColor = ColorPalette.getInstance( )
					.getColor( query.getDefinition( ) );
			if ( getInputControl( ) != null )
			{
				ChartUIUtil.setBackgroundColor( getInputControl( ),
						true,
						cColor );
			}
		}
	}

	public void dispose( )
	{
		if ( getInputControl( ) != null )
		{
			DataDefinitionTextManager.getInstance( )
					.removeDataDefinitionText( getInputControl( ) );
		}
		super.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( btnBuilder ) )
		{
			handleBuilderAction( );
		}
		else if ( e.getSource( ).equals( btnGroup ) )
		{
			handleGroupAction( );
		}
	}

	/**
	 * Handle grouping/sorting action.
	 */
	protected void handleGroupAction( )
	{
		SeriesDefinition sdBackup = (SeriesDefinition) EcoreUtil.copy( seriesdefinition );
		GroupSortingDialog groupDialog = createGroupSortingDialog( sdBackup );

		if ( groupDialog.open( ) == Window.OK )
		{
			if ( !sdBackup.eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
			{
				seriesdefinition.eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			}
			else
			{
				seriesdefinition.setSorting( sdBackup.getSorting( ) );
			}

			seriesdefinition.setSortKey( sdBackup.getSortKey( ) );
			seriesdefinition.getSortKey( )
					.eAdapters( )
					.addAll( seriesdefinition.eAdapters( ) );

			seriesdefinition.setGrouping( sdBackup.getGrouping( ) );
			seriesdefinition.getGrouping( )
					.eAdapters( )
					.addAll( seriesdefinition.eAdapters( ) );
			ChartUIUtil.checkGroupType( context, context.getModel( ) );
			ChartUIUtil.isValidAggregation( context,
					seriesdefinition.getGrouping( ),
					true );
		}
	}

	/**
	 * Handle builder dialog action.
	 */
	private void handleBuilderAction( )
	{
		try
		{
			String sExpr = context.getUIServiceProvider( )
					.invoke( IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS,
							getExpression( getInputControl( ) ),
							context.getExtendedItem( ),
							sTitle );
			boolean isSuccess = setUIText( getInputControl( ), sExpr );
			query.setDefinition( sExpr );

			if ( !isSuccess )
			{
				Event event = new Event( );
				event.type = IChartDataSheet.EVENT_QUERY;
				event.data = queryType;
				context.getDataSheet( ).notifyListeners( event );
			}
		}
		catch ( ChartException e1 )
		{
			WizardBase.displayException( e1 );
		}
	}

	/**
	 * Create instance of <code>GroupSortingDialog</code> for base series or Y
	 * series.
	 * 
	 * @param sdBackup
	 * @return
	 */
	protected GroupSortingDialog createGroupSortingDialog(
			SeriesDefinition sdBackup )
	{
		return new BaseGroupSortingDialog( cmpTop.getShell( ),
				context,
				sdBackup );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		if ( e.getSource( ).equals( getInputControl( ) ) )
		{
			isQueryModified = true;
			// Reset tooltip
			setTooltipForInputControl( );
		}
	}

	/**
	 * Set tooltip for input control.
	 */
	private void setTooltipForInputControl( )
	{
		getInputControl( ).setToolTipText( getTooltipForDataText( getExpression( getInputControl( ) ) ) );
	}

	/**
	 * Sets the description in the left of data text box.
	 * 
	 * @param description
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	public void focusGained( FocusEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void focusLost( FocusEvent e )
	{
		// Null event is fired by Drop Listener manually
		if ( e == null || e.widget.equals( getInputControl( ) ) )
		{
			saveQuery( );
		}
	}

	protected void saveQuery( )
	{
		if ( isQueryModified )
		{
			updateQuery( ChartUIUtil.getText( getInputControl( ) ) );
			// Refresh color from ColorPalette
			setColor( );
			getInputControl( ).getParent( ).layout( );
			
			Event e = new Event( );
			e.text = query.getDefinition( ) == null ? "" //$NON-NLS-1$
					: query.getDefinition( );
			e.data = e.text;
			e.widget = getInputControl( );
			e.type = 0;
			fireEvent( e );
			
			isQueryModified = false;
		}
	}

	private String getTooltipForDataText( String queryText )
	{
		if ( isTableSharedBinding( ) )
		{
			int index = cmbDefinition.getSelectionIndex( );
			if ( index >= 0 )
			{
				ColumnBindingInfo cbi = (ColumnBindingInfo) ( (Object[]) cmbDefinition.getData( ) )[index];
				if ( cbi.getColumnType( ) == ColumnBindingInfo.GROUP_COLUMN
						|| cbi.getColumnType( ) == ColumnBindingInfo.AGGREGATE_COLUMN )
				{
					return cbi.getTooltip( );
				}
			}
		}
		if ( queryText.trim( ).length( ) == 0 )
		{
			return tooltipWhenBlank;
		}
		return queryText;
	}

	public void keyPressed( KeyEvent e )
	{
		if ( e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR )
		{
			saveQuery( );
		}
	}

	public void keyReleased( KeyEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void setTooltipWhenBlank( String tootipWhenBlank )
	{
		this.tooltipWhenBlank = tootipWhenBlank;
	}

	private void createAggregationItem( Composite composite )
	{
		SeriesDefinition baseSD = (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( context.getModel( ) )
				.get( 0 );
		boolean enabled = ChartUIUtil.isGroupingSupported( context )
				&& ( PluginSettings.instance( ).inEclipseEnv( ) || baseSD.getGrouping( )
						.isEnabled( ) );
		fAggEditorComposite = new AggregateEditorComposite( composite,
				seriesdefinition,
				context,
				enabled );
	}

	private Control getInputControl( )
	{
		if ( txtDefinition != null )
		{
			return txtDefinition;
		}
		if ( cmbDefinition != null )
		{
			return cmbDefinition;
		}
		return null;
	}

	private String getExpression( Control control )
	{
		return getActualExpression( control );
	}

	private boolean setUIText( Control control, String expression )
	{
		if ( control == null || control.isDisposed( ) )
		{
			return false;
		}

		if ( control instanceof Text )
		{
			( (Text) control ).setText( expression );
		}
		else if ( control instanceof CCombo )
		{
			if ( isTableSharedBinding( ) )
			{
				setUITextForSharedBinding( (CCombo) control, expression );
			}
			else
			{
				( (CCombo) control ).setText( expression );
			}
		}

		return true;
	}

	/**
	 * @param control
	 * @param expression
	 */
	private void setUITextForSharedBinding( CCombo control, String expression )
	{
		Object[] data = (Object[]) control.getData( );
		if ( data == null || data.length == 0 )
		{
			control.setText( expression );
		}
		else
		{
			String expr = getDisplayExpressionForSharedBinding( control, expression );
			control.setText( expr );
		}
	}

	/**
	 * Update query by specified expression.
	 * <p>
	 * Under shared binding case, update grouping/aggregate attributes of chart
	 * model if the selected item is group/aggregate expression.
	 */
	public void updateQuery( String expression )
	{
		if ( !isTableSharedBinding( ) )
		{
			setQueryExpression( expression );
			return;
		}

		updateQueryForSharedBinding( expression );
	}

	/**
	 * Update query expression for sharing query with table.
	 * 
	 * @param expression
	 */
	private void updateQueryForSharedBinding( String expression )
	{
		Object[] data = (Object[]) cmbDefinition.getData( );
		if ( data != null && data.length > 0 )
		{
			String expr = expression;
			if ( ChartUIConstants.QUERY_CATEGORY.equals( queryType )
					|| ChartUIConstants.QUERY_OPTIONAL.equals( queryType ) )
			{
				boolean isGroupExpr = false;
				for ( int i = 0; i < data.length; i++ )
				{
					ColumnBindingInfo chi = (ColumnBindingInfo) data[i];
					int type = chi.getColumnType( );

					if ( type == ColumnBindingInfo.GROUP_COLUMN )
					{
						String groupRegex = ChartUtil.createRegularRowExpression( chi.getName( ),
								false );
						String regex = ChartUtil.createRegularRowExpression( chi.getName( ),
								true );
						if ( expression.matches( regex ) )
						{
							isGroupExpr = true;
							expr = expression.replaceAll( groupRegex,
									chi.getExpression( ) );
							break;
						}
					}
				}

				if ( ChartUIConstants.QUERY_CATEGORY.equals( queryType ) )
				{
					if ( isGroupExpr )
					{
						seriesdefinition.getGrouping( ).setEnabled( true );
					}
					else
					{
						seriesdefinition.getGrouping( ).setEnabled( false );
					}
				}

			}
			else if ( ChartUIConstants.QUERY_VALUE.equals( queryType ) )
			{
				boolean isAggregationExpr = false;
				ColumnBindingInfo chi = null;
				for ( int i = 0; i < data.length; i++ )
				{
					chi = (ColumnBindingInfo) data[i];
					int type = chi.getColumnType( );

					if ( type == ColumnBindingInfo.AGGREGATE_COLUMN )
					{
						String aggRegex = ChartUtil.createRegularRowExpression( chi.getName( ),
								false );
						String regex = ChartUtil.createRegularRowExpression( aggRegex,
								true );
						if ( expression.matches( regex ) )
						{
							isAggregationExpr = true;
							expr = expression.replaceAll( aggRegex,
									chi.getExpression( ) );
							break;
						}
					}
				}

				if ( isAggregationExpr )
				{
					seriesdefinition.getGrouping( ).setEnabled( true );
					seriesdefinition.getGrouping( )
							.setAggregateExpression( chi.getChartAggExpression( ) );
				}
				else
				{
					seriesdefinition.getGrouping( ).setEnabled( false );
					seriesdefinition.getGrouping( )
							.setAggregateExpression( null );
				}
			}
			
			setQueryExpression( expr );

		}
		else
		{
			setQueryExpression( expression );
		}
	}

	private void setQueryExpression( String expression )
	{
		if ( query != null )
		{
			query.setDefinition( expression );
		}
		else
		{
			query = QueryImpl.create( expression );
			query.eAdapters( ).addAll( seriesdefinition.eAdapters( ) );
			// Since the data query must be non-null, it's created in
			// ChartUIUtil.getDataQuery(), assume current null is a grouping
			// query
			seriesdefinition.setQuery( query );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IQueryExpressionManager#getQuery()
	 */
	public Query getQuery( )
	{
		if ( query == null )
		{
			query = QueryImpl.create( getExpression( getInputControl( ) ) );
			query.eAdapters( ).addAll( seriesdefinition.eAdapters( ) );
			// Since the data query must be non-null, it's created in
			// ChartUIUtil.getDataQuery(), assume current null is a grouping
			// query
			seriesdefinition.setQuery( query );
		}

		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IQueryExpressionManager#getDisplayExpression()
	 */
	public String getDisplayExpression( )
	{
		if ( cmbDefinition != null && isTableSharedBinding( ) )
		{
			return getDisplayExpressionForSharedBinding( cmbDefinition, query.getDefinition( ) );
		}
		else
		{
			String expr = query.getDefinition( );
			return ( expr == null ) ? "" : expr; //$NON-NLS-1$
		}
	}

	private String getDisplayExpressionForSharedBinding( CCombo combo, String expression )
	{
		String expr = expression;
		Object[] data = (Object[]) combo.getData( );
		for ( int i = 0; data != null && i < data.length; i++ )
		{
			ColumnBindingInfo chi = (ColumnBindingInfo) data[i];
			if ( chi.getExpression( ) == null )
			{
				continue;
			}
			
			String columnExpr = null;
			try
			{
				columnExpr = ( (IColumnBinding) ExpressionUtil.extractColumnExpressions( chi.getExpression( ) )
						.get( 0 ) ).getResultSetColumnName( );
			}
			catch ( BirtException e )
			{
				continue;
			}
			
			String columnRegex = ChartUtil.createRegularRowExpression( columnExpr,
					false );
			String regex = ChartUtil.createRegularRowExpression( columnExpr,
					true );

			if ( expression != null && expression.matches( regex ) )
			{
				if ( queryType == ChartUIConstants.QUERY_CATEGORY )
				{
					boolean sdGrouped = seriesdefinition.getGrouping( )
							.isEnabled( );
					boolean groupedBinding = ( chi.getColumnType( ) == ColumnBindingInfo.GROUP_COLUMN );
					if ( sdGrouped && groupedBinding )
					{
						expr = expression.replaceAll( columnRegex,
								ExpressionUtil.createJSRowExpression( chi.getName( ) ) );
						break;
					}
				}
				else if ( queryType == ChartUIConstants.QUERY_OPTIONAL )
				{
					expr = expression.replaceAll( columnRegex,
							ExpressionUtil.createJSRowExpression( chi.getName( ) ) );
					break;
				}
			}
		}

		return ( expr == null ) ? "" : expr; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IQueryExpressionManager#isValidExpression(java.lang.String)
	 */
	public boolean isValidExpression( String expression )
	{
		if ( context.getDataServiceProvider( )
				.checkState( IDataServiceProvider.SHARE_QUERY ) )
		{
			int index = cmbDefinition.indexOf( expression );
			if ( index < 0 )
			{
				return false;
			}
			return true;
		}
		return true;
	}
	
	/**
	 * The method is used to get actual expression from input control.For shared
	 * binding case, the expression is stored in data field of combo widget.
	 * 
	 * @param control
	 * @return
	 * @since 2.3
	 */
	private String getActualExpression( Control control )
	{
		if ( control instanceof Text )
		{
			return ( (Text) control ).getText( );
		}
		if ( control instanceof CCombo )
		{
			Object[] data = (Object[]) control.getData( );
			if ( data != null
					&& data.length > 0
					&& data[0] instanceof ColumnBindingInfo )
			{
				String txt = ( (CCombo) control ).getText( );
				String[] items = ( (CCombo) control ).getItems( );
				int index = 0;
				for ( ; items != null
						&& items.length > 0
						&& index < items.length; index++ )
				{
					if ( items[index].equals( txt ) )
					{
						break;
					}
				}
				if ( items != null && index >= 0 && index < items.length )
				{
					return ( (ColumnBindingInfo) data[index] ).getExpression( );
				}
			}
			return ( (CCombo) control ).getText( );
		}
		return ""; //$NON-NLS-1$
	}
}
