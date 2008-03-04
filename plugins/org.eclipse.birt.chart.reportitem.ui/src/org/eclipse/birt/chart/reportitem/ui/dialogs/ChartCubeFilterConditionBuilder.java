/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.ChartCubeQueryHelper;
import org.eclipse.birt.chart.reportitem.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ui.ChartReportItemUIActivator;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.SelectValueDialog;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;


/**
 * The dialog maintains the filters against chart using cube set case.
 *  
 * @since 2.3
 */
public class ChartCubeFilterConditionBuilder extends TitleAreaDialog
{

	protected static Logger logger = Logger.getLogger( FilterConditionBuilder.class.getName( ) );

	public static final String DLG_TITLE_NEW = Messages.getString( "FilterConditionBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "FilterConditionBuilder.DialogTitle.Edit" ); //$NON-NLS-1$
	public static final String DLG_MESSAGE_NEW = Messages.getString( "FilterConditionBuilder.DialogMessage.New" ); //$NON-NLS-1$
	public static final String DLG_MESSAGE_EDIT = Messages.getString( "FilterConditionBuilder.DialogMessage.Edit" ); //$NON-NLS-1$

	protected transient String[] popupItems = null;

	private static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	protected final String NULL_STRING = null;
	protected Composite dummy1, dummy2;
	protected Label label1, label2;

	protected List valueList = new ArrayList( );

	protected List selValueList = new ArrayList( );

	/**
	 * Usable operators for building map rule conditions.
	 */
	protected static final String[][] OPERATOR;

	private ParamBindingHandle[] bindingParams = null;

	private transient boolean refreshItems = true;

	protected transient ReportElementHandle currentItem = null;

	protected static String[] EMPTY_ARRAY = new String[]{};

	protected List columnList;

	protected int valueVisible;

	protected Table table;
	protected TableViewer tableViewer;

	/**
	 * Constant, represents empty String array.
	 */
	protected static final String[] EMPTY = new String[0];

	private Map fExprMap = new LinkedHashMap();

	protected String title, message;
	
	protected IChoiceSet choiceSet;

	/**
	 * @param title
	 */
	public ChartCubeFilterConditionBuilder( String title, String message )
	{
		this( UIUtil.getDefaultShell( ), title, message );
	}
	
	/**
	 * @param parentShell
	 * @param title
	 */
	public ChartCubeFilterConditionBuilder( Shell parentShell, String title,
			String message )
	{
		super( parentShell );
		this.title = title;
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder#setColumnList(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	protected void setColumnList( DesignElementHandle handle )
	{
		if ( handle instanceof ExtendedItemHandle )
		{
			try
			{
				fExprMap = getChartExprDefinitions( (ExtendedItemHandle) handle );
				columnList = new ArrayList( fExprMap.keySet( ) );
				return;
			}
			catch ( ExtendedElementException e )
			{
				e.printStackTrace();
			}
		}
		
		columnList = new ArrayList();
	}

	private Map getChartExprDefinitions( ExtendedItemHandle handle ) throws ExtendedElementException
	{
		Map exprMap = new LinkedHashMap();
		IReportItem item = handle.getReportItem( );
		Chart cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( ChartReportItemConstants.PROPERTY_CHART );
		SeriesDefinition sd = (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( cm ).get(0);
		Query query = (Query) sd.getDesignTimeSeries( ).getDataDefinition( ).get( 0 );
		if ( query != null && query.getDefinition( ) != null && !"".equals( query.getDefinition( ) )) //$NON-NLS-1$
		{
			exprMap.put( Messages.getString("ChartCubeFilterConditionBuilder.Expression.CategoryItem.Prefix") + query.getDefinition( ), query.getDefinition( ) ); //$NON-NLS-1$
		}
		
		List sdList = ChartUIUtil.getAllOrthogonalSeriesDefinitions( cm );
		if ( sdList.size( ) <= 0 )
		{
			return exprMap;
		}
		
		Query q = ((SeriesDefinition)sdList.get( 0 )).getQuery( );
		if ( q != null && q.getDefinition( ) != null && !"".equals( q.getDefinition( ) )) //$NON-NLS-1$
		{
			exprMap.put( Messages.getString("ChartCubeFilterConditionBuilder.Expression.YOptionItem.Prefix") + q.getDefinition( ), q.getDefinition( ) ); //$NON-NLS-1$
		}
		
		for (Iterator iter = sdList.iterator( ); iter.hasNext( ); )
		{
			SeriesDefinition sDefintion = (SeriesDefinition) iter.next( );
			for (Iterator i = sDefintion.getDesignTimeSeries( ).getDataDefinition( ).iterator( ); i.hasNext( ); )
			{
				query = (Query) i.next( );
				if ( query != null && query.getDefinition( ) != null && !"".equals( query.getDefinition( )  )) //$NON-NLS-1$
				{
					exprMap.put( Messages.getString("ChartCubeFilterConditionBuilder.Expression.ValueItemPrefix") + query.getDefinition( ), query.getDefinition( ) ); //$NON-NLS-1$
				}
			}
		}
		return exprMap;
	}
	
	protected String[] getDataSetColumns( )
	{
		if ( columnList.isEmpty( ) )
		{
			return EMPTY;
		}
		String[] values = new String[columnList.size( )];
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			values[i] = (String) columnList.get( i );
		}
		return values;
	}
	
	protected Object getResultSetColumn( String name )
	{
		if ( columnList.isEmpty( ) )
		{
			return null;
		}
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			if ( columnList.get( i ).equals( name ) )
			{
				return columnList.get( i );
			}
		}
		return null;
	}
	
	protected String getColumnName( Object obj )
	{
		return (String) obj;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		try
		{
			if ( inputHandle == null )
			{
				FilterConditionElementHandle filter = DesignElementFactory.getInstance( )
						.newFilterConditionElement( );
				filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				
				filter.setExpr( getExpression( expression.getText( ) ) );

				if ( valueVisible == 3 )
				{
					filter.setValue1( valueList );
					filter.setValue2( "" ); //$NON-NLS-1$
				}
				else
				{
					assert ( !expressionValue1.isDisposed( ) );
					assert ( !expressionValue2.isDisposed( ) );
					if ( expressionValue1.getVisible( ) )
					{
						filter.setValue1( DEUtil.resolveNull( expressionValue1.getText( ) ) );
					}
					else
					{
						filter.setValue1( NULL_STRING );
					}

					if ( expressionValue2.getVisible( ) )
					{
						filter.setValue2( DEUtil.resolveNull( expressionValue2.getText( ) ) );
					}
					else
					{
						filter.setValue2( NULL_STRING );
					}
				}
		
				PropertyHandle propertyHandle = designHandle.getPropertyHandle( ChartReportItemUtil.PROPERTY_CUBE_FILTER );
				propertyHandle.add( filter );
			}
			else
			{
				inputHandle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				if ( valueVisible == 3 )
				{
					inputHandle.setValue1( valueList );
					inputHandle.setValue2( NULL_STRING );
				}
				else
				{
					assert ( !expressionValue1.isDisposed( ) );
					assert ( !expressionValue2.isDisposed( ) );
					if ( expressionValue1.getVisible( ) )
					{
						inputHandle.setValue1( DEUtil.resolveNull( expressionValue1.getText( ) ) );
					}
					else
					{
						inputHandle.setValue1( NULL_STRING );
					}

					if ( expressionValue2.getVisible( ) )
					{
						inputHandle.setValue2( DEUtil.resolveNull( expressionValue2.getText( ) ) );
					}
					else
					{
						inputHandle.setValue2( NULL_STRING );
					}
				}
				inputHandle.setExpr( getExpression( expression.getText( ) ) );
			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		super.okPressed( );
	}

	private String getExpression( String displayExpr )
	{
		String expr = (String) fExprMap.get( displayExpr );
		if ( expr == null )
		{
			expr = displayExpr;
		}
		return DEUtil.resolveNull( expr );
	}
	
	private String getDisplayExpression( String expression )
	{
		for ( Iterator iter = fExprMap.entrySet( ).iterator( ); iter.hasNext( ); )
		{
			Entry entry = (Entry) iter.next( );
			if ( expression == null && entry.getValue( ) == null )
			{
				return DEUtil.resolveNull( expression );
			}
			else if ( expression != null &&
					expression.equals( entry.getValue( ) ) )
			{
				return (String) entry.getKey( );
			}
		}
		return DEUtil.resolveNull( expression );
	}
	
	public void setReportElement( ReportElementHandle reportItem )
	{
		currentItem = reportItem;
	}

	/**
	 * 
	 */
	public void setBindingParams( ParamBindingHandle[] params )
	{
		this.bindingParams = params;
	}


	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( FilterCondition.FILTER_COND_STRUCT,
				FilterCondition.OPERATOR_MEMBER );
		IChoice[] chs = chset.getChoices( new AlphabeticallyComparator( ) );
		OPERATOR = new String[chs.length][2];

		for ( int i = 0; i < chs.length; i++ )
		{
			OPERATOR[i][0] = chs[i].getDisplayName( );
			OPERATOR[i][1] = chs[i].getName( );
		}
	}

	/**
	 * Returns the operator value by its display name.
	 * 
	 * @param name
	 */
	public static String getValueForOperator( String name )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][0].equals( name ) )
			{
				return OPERATOR[i][1];
			}
		}

		return null;
	}

	/**
	 * Returns how many value fields this operator needs.
	 * 
	 * @param operatorValue
	 */
	public static int determineValueVisible( String operatorValue )
	{
		if ( DesignChoiceConstants.FILTER_OPERATOR_ANY.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_FALSE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_TRUE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NULL.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL.equals( operatorValue ) )
		{
			return 0;
		}
		else if ( DesignChoiceConstants.FILTER_OPERATOR_LT.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_LE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_EQ.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_GE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_GT.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_LIKE.equals( operatorValue ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN.equals( operatorValue ) )
		{
			return 2;
		}
		else if ( DesignChoiceConstants.FILTER_OPERATOR_IN.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_IN.equals( operatorValue ) )
		{
			return 3;
		}

		return 1;
	}

	/**
	 * Returns the operator display name by its value.
	 * 
	 * @param value
	 */
	public static String getNameForOperator( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return OPERATOR[i][0];
			}
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the index for given operator value in the operator list.
	 * 
	 * @param value
	 */
	protected static int getIndexForOperatorValue( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return i;
			}
		}

		return 0;
	}

	protected FilterConditionElementHandle inputHandle;

	protected Combo expression, operator;

	protected Button addBtn, editBtn, delBtn, delAllBtn;

	protected Combo expressionValue1, expressionValue2, addExpressionValue;

	protected Composite valueListComposite;

	protected Label andLable;

	protected DesignElementHandle designHandle;

	protected static final String VALUE_OF_THIS_DATA_ITEM = Messages.getString( "FilterConditionBuilder.choice.ValueOfThisDataItem" ); //$NON-NLS-1$

	private String fCurrentExpr = ""; //$NON-NLS-1$
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent,
				IHelpContextIds.INSERT_EDIT_FILTER_CONDITION_DIALOG_ID );

		Composite area = (Composite) super.createDialogArea( parent );
		Composite contents = new Composite( area, SWT.NONE );
		contents.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		contents.setLayout( new GridLayout( ) );

		this.setTitle( title );
		this.setMessage( message );
		getShell( ).setText( title );

		applyDialogFont( contents );
		initializeDialogUnits( area );

		createFilterConditionContent( contents );

		return area;
	}

	protected void createFilterConditionContent( Composite innerParent )
	{

		Composite anotherParent = new Composite( innerParent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		anotherParent.setLayoutData( gd );
		GridLayout glayout = new GridLayout( 4, false );
		anotherParent.setLayout( glayout );

		Label lb = new Label( anotherParent, SWT.NONE );
		lb.setText( Messages.getString( "FilterConditionBuilder.text.Condition" ) ); //$NON-NLS-1$

		Label lb2 = new Label( anotherParent, SWT.NONE );
		lb2.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( anotherParent, SWT.NONE );

		Composite condition = new Composite( innerParent, SWT.NONE );
		gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 180;
		condition.setLayoutData( gd );
		glayout = new GridLayout( 4, false );
		condition.setLayout( glayout );

		expression = new Combo( condition, SWT.NONE );
		GridData gdata = new GridData( );
		gdata.widthHint = 100;
		expression.setLayoutData( gdata );
		expression.addListener( SWT.Selection, ComboModify );
		expression.setItems( getDataSetColumns( ) );
		if ( expression.getItemCount( ) == 0 )
		{
			expression.add( DEUtil.resolveNull( null ) );
		}
		expression.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( expression.getText( ).equals( VALUE_OF_THIS_DATA_ITEM )
						&& designHandle instanceof DataItemHandle )
				{
					expression.setText( DEUtil.getColumnExpression( ( (DataItemHandle) designHandle ).getResultSetColumn( ) ) );
				}
				updateButtons( );
			}
		} );
		expression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
				if ( !expression.getText( ).equals( fCurrentExpr ) )
				{
					needRefreshList = true;
					fCurrentExpr = expression.getText( );
				}
			}
		} );

		Button expBuilder = new Button( condition, SWT.PUSH );
		// expBuilder.setText( "..." ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.heightHint = 20;
		gdata.widthHint = 20;
		expBuilder.setLayoutData( gdata );
		UIUtil.setExpressionButtonImage( expBuilder );
		expBuilder.setToolTipText( Messages.getString( "FilterConditionBuilder.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
		expBuilder.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				editValue( expression );
			}
		} );

		operator = new Combo( condition, SWT.READ_ONLY );
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			operator.add( OPERATOR[i][0] );
		}
		operator.addSelectionListener( OpoertorSelection );

		create2ValueComposite( condition );

		if ( inputHandle != null )
		{
			syncViewProperties( );
		}

		lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	protected Listener expValueVerifyListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			// TODO Auto-generated method stub
			Combo thisCombo = (Combo) event.widget;
			String text = event.text;
			if ( text != null && thisCombo.indexOf( text ) >= 0 )
			{
				event.doit = false;
			}
			else
			{
				event.doit = true;
			}
		}
	};

	private Listener expValueSelectionListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			// TODO Auto-generated method stub
			Combo thisCombo = (Combo) event.widget;
			int selectionIndex = thisCombo.getSelectionIndex( );
			if ( selectionIndex < 0 )
			{
				return;
			}
			String value = popupItems[selectionIndex];

			boolean isAddClick = false;
			if ( tableViewer != null
					&& ( addBtn != null && ( !addBtn.isDisposed( ) ) ) )
			{
				isAddClick = true;
			}

			boolean returnValue = false;
			if ( value != null )
			{
				String newValues[] = new String[1];
				if ( value.equals( ( actions[0] ) ) )
				{
					if ( designHandle instanceof ReportItemHandle && ((ReportItemHandle)designHandle).getCube( ) != null )
					{
						List selectValueList = getSelectValueList( );
						if ( selectValueList == null
								|| selectValueList.size( ) == 0 )
						{
							MessageDialog.openInformation( null,
									Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
									Messages.getString( Messages.getString("ChartCubeFilterConditionBuilder.SelectValueDialog.messages.info.selectVauleUnavailable") ) ); //$NON-NLS-1$

						}
						else
						{
							SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
									.getDisplay( )
									.getActiveShell( ),
									Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
							if ( isAddClick )
							{
								dialog.setMultipleSelection( true );
							}
							dialog.setSelectedValueList( selectValueList );
							if ( bindingParams != null )
							{
								dialog.setBindingParams( bindingParams );
							}
							if ( dialog.open( ) == IDialogConstants.OK_ID )
							{
								returnValue = true;
								newValues = dialog.getSelectedExprValues( );
							}
						}
					}
					else
					{
						MessageDialog.openInformation( null,
								Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
								Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
					}
				}
				else if ( value.equals( actions[1] ) )
				{
					ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							thisCombo.getText( ) );

					if ( expressionProvider == null )
						dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
					else
						dialog.setExpressionProvier( expressionProvider );

					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						returnValue = true;
						newValues[0] = dialog.getResult( );
					}
				}
				else if ( selectionIndex > 3 )
				{
					newValues[0] = "params[\"" + value + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				if(returnValue)
				{
					if(addExpressionValue == thisCombo)
					{
						thisCombo.setText(""); //$NON-NLS-1$
						addBtn.setEnabled( false );
					}else					
					if(newValues.length == 1)
					{
						thisCombo.setText(DEUtil.resolveNull( newValues[0] ));
					}
					
					if ( isAddClick )
					{
						
						
						boolean change = false;
						for(int i = 0; i < newValues.length; i ++)
						{
							if ( valueList.indexOf( DEUtil.resolveNull( newValues[i] ) ) < 0 )
							{
								valueList.add(  DEUtil.resolveNull( newValues[i] ) );
								change = true;
							}					
						}
						if(change)
						{
							tableViewer.refresh( );
							updateButtons( );
							addExpressionValue.setFocus( );
						}

					}

				}
			}
		}

	};

	private int create2ValueComposite( Composite condition )
	{

		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			return 0;
		}

		if ( valueListComposite != null && !valueListComposite.isDisposed( ) )
		{
			valueListComposite.dispose( );
			valueListComposite = null;
		}

		GridData expgd = new GridData( );
		expgd.widthHint = 100;

		expressionValue1 = new Combo( condition, SWT.NONE );
		expressionValue1.setLayoutData( expgd );
		expressionValue1.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		expressionValue1.addListener( SWT.Verify, expValueVerifyListener );
		expressionValue1.addListener( SWT.Selection, expValueSelectionListener );

		refreshList( );
		expressionValue1.setItems( popupItems );

		dummy1 = createDummy( condition, 3 );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "FilterConditionBuilder.text.AND" ) ); //$NON-NLS-1$
		andLable.setEnabled( false );
		// andLable.setVisible( false );

		dummy2 = createDummy( condition, 3 );

		expressionValue2 = new Combo( condition, SWT.NONE );
		expressionValue2.setLayoutData( expgd );
		expressionValue2.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		expressionValue2.addListener( SWT.Verify, expValueVerifyListener );
		expressionValue2.addListener( SWT.Selection, expValueSelectionListener );

		expressionValue2.setItems( popupItems );

		// expressionValue2.setVisible( false );

		if ( operator.getItemCount( ) > 0
				&& operator.getSelectionIndex( ) == -1 )
		{
			operator.select( 0 );
		}

		condition.getParent( ).layout( true, true );
		return 1;
	}

	private int createValueListComposite( Composite parent )
	{

		if ( valueListComposite != null && !valueListComposite.isDisposed( ) )
		{
			return 0;
		}

		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			expressionValue1.dispose( );
			expressionValue1 = null;

			dummy1.dispose( );
			dummy1 = null;

			expressionValue2.dispose( );
			expressionValue2 = null;

			dummy2.dispose( );
			dummy2 = null;

			andLable.dispose( );
			andLable = null;
		}

		valueListComposite = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.horizontalSpan = 4;
		valueListComposite.setLayoutData( gdata );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 4;
		valueListComposite.setLayout( layout );

		Group group = new Group( valueListComposite, SWT.NONE );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 118;
		data.horizontalSpan = 3;
		data.horizontalIndent = 0;
		data.horizontalAlignment = SWT.BEGINNING;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData( data );
		layout = new GridLayout( );
		layout.numColumns = 4;
		group.setLayout( layout );

		new Label( group, SWT.NONE ).setText( Messages.getString( "FilterConditionBuilder.label.value" ) ); //$NON-NLS-1$

		GridData expgd = new GridData( );
		expgd.widthHint = 100;

		addExpressionValue = new Combo( group, SWT.NONE );
		addExpressionValue.setLayoutData( expgd );

		addBtn = new Button( group, SWT.PUSH );
		addBtn.setText( Messages.getString( "FilterConditionBuilder.button.add" ) ); //$NON-NLS-1$
		addBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.add.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( addBtn );
		addBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				String value = addExpressionValue.getText( ).trim( );
				if ( valueList.indexOf( value ) < 0 )
				{
					valueList.add( value );
					tableViewer.refresh( );
					updateButtons( );
					addExpressionValue.setFocus( );
					addExpressionValue.setText( "" ); //$NON-NLS-1$
				}
				else
				{
					addBtn.setEnabled( false );
				}

			}
		} );

		new Label( group, SWT.NONE );

		int tableStyle = SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION;
		table = new Table( group, tableStyle );
		data = new GridData( GridData.FILL_VERTICAL );
		data.horizontalSpan = 4;
		table.setLayoutData( data );

		table.setHeaderVisible( false );
		table.setLinesVisible( true );
		TableColumn column;
		int i;
		String[] columNames = new String[]{
			Messages.getString( "FilterConditionBuilder.list.item1" ), //$NON-NLS-1$
		};
		int[] columLength = new int[]{
			288
		};
		for ( i = 0; i < columNames.length; i++ )
		{
			column = new TableColumn( table, SWT.NONE, i );
			column.setText( columNames[i] );
			column.setWidth( columLength[i] );
		}
		table.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				checkEditDelButtonStatus( );
			}
		} );

		table.addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{
				// TODO Auto-generated method stub
				if ( e.keyCode == SWT.DEL )
				{
					int index = table.getSelectionIndex( );
					if ( index > -1 )
					{
						valueList.remove( index );
						tableViewer.refresh( );
						if ( valueList.size( ) > 0 )
						{
							if ( valueList.size( ) <= index )
							{
								index = index - 1;
							}
							table.select( index );
						}
						updateButtons( );
					}
					else
					{
						delBtn.setEnabled( false );
					}
				}

			}

			public void keyReleased( KeyEvent e )
			{
				// TODO Auto-generated method stub

			}

		} );
		table.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
				if ( selection.getFirstElement( ) != null
						&& selection.getFirstElement( ) instanceof String )
				{
					String initValue = (String) selection.getFirstElement( );

					ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
							initValue );

					if ( designHandle != null )
					{
						if ( expressionProvider == null )
							expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
						else
							expressionBuilder.setExpressionProvier( expressionProvider );
					}

					if ( expressionBuilder.open( ) == OK )
					{
						String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
						int index = table.getSelectionIndex( );
						valueList.remove( index );
						valueList.add( index, result );
						tableViewer.refresh( );
						table.select( index );
					}
					updateButtons( );
				}
				else
				{
					editBtn.setEnabled( false );
				}
			}
		} );

		tableViewer = new TableViewer( table );
		tableViewer.setUseHashlookup( true );
		tableViewer.setColumnProperties( columNames );
		tableViewer.setLabelProvider( tableLableProvier );
		tableViewer.setContentProvider( tableContentProvider );

		Composite rightPart = new Composite( valueListComposite, SWT.NONE );
		data = new GridData( GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_END );
		rightPart.setLayoutData( data );
		layout = new GridLayout( );
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout( layout );

		editBtn = new Button( rightPart, SWT.PUSH );
		editBtn.setText( Messages.getString( "FilterConditionBuilder.button.edit" ) ); //$NON-NLS-1$
		editBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.edit.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( editBtn );
		editBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
				if ( selection.getFirstElement( ) != null
						&& selection.getFirstElement( ) instanceof String )
				{
					String initValue = (String) selection.getFirstElement( );

					ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
							initValue );

					if ( designHandle != null )
					{
						if ( expressionProvider == null )
							expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
						else
							expressionBuilder.setExpressionProvier( expressionProvider );
					}

					if ( expressionBuilder.open( ) == OK )
					{
						String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
						int index = table.getSelectionIndex( );
						valueList.remove( index );
						valueList.add( index, result );
						tableViewer.refresh( );
						table.select( index );
					}
					updateButtons( );
				}
				else
				{
					editBtn.setEnabled( false );
				}
			}

		} );

		delBtn = new Button( rightPart, SWT.PUSH );
		delBtn.setText( Messages.getString( "FilterConditionBuilder.button.delete" ) ); //$NON-NLS-1$
		delBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.delete.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( delBtn );
		delBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				int index = table.getSelectionIndex( );
				if ( index > -1 )
				{
					valueList.remove( index );
					tableViewer.refresh( );
					if ( valueList.size( ) > 0 )
					{
						if ( valueList.size( ) <= index )
						{
							index = index - 1;
						}
						table.select( index );
					}
					updateButtons( );
				}
				else
				{
					delBtn.setEnabled( false );
				}
			}

		} );

		delAllBtn = new Button( rightPart, SWT.PUSH );
		delAllBtn.setText( Messages.getString( "FilterConditionBuilder.button.deleteall" ) ); //$NON-NLS-1$
		delAllBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.deleteall.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( delAllBtn );
		delAllBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				int count = valueList.size( );
				if ( count > 0 )
				{
					valueList.clear( );
					tableViewer.refresh( );
					updateButtons( );
				}
				else
				{
					delAllBtn.setEnabled( false );
				}
			}

		} );

		addExpressionValue.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkAddButtonStatus( );
				updateButtons( );
			}
		} );

		addExpressionValue.addListener( SWT.Verify, expValueVerifyListener );
		addExpressionValue.addListener( SWT.Selection,
				expValueSelectionListener );
		refreshList( );
		addExpressionValue.setItems( popupItems );

		parent.getParent( ).layout( true, true );
		return 1;

	}

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			// TODO Auto-generated method stub
			if ( columnIndex == 0 )
			{
				return (String) element;
			}
			return ""; //$NON-NLS-1$
		}

		public void addListener( ILabelProviderListener listener )
		{
			// TODO Auto-generated method stub

		}

		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		public boolean isLabelProperty( Object element, String property )
		{
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{
			// TODO Auto-generated method stub

		}
	};

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			// TODO Auto-generated method stub

		}

		public Object[] getElements( Object inputElement )
		{
			// TODO Auto-generated method stub
			if ( inputElement == null )
			{
				return new Object[0];
			}
			else if ( inputElement instanceof List )
			{
				return ( (List) inputElement ).toArray( );
			}
			return null;
		}
	};

	protected SelectionListener OpoertorSelection = new SelectionListener( ) {

		public void widgetSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub
			String value = getValueForOperator( operator.getText( ) );

			valueVisible = determineValueVisible( value );

			if ( valueVisible == 3 )
			{
				int ret = createValueListComposite( operator.getParent( ) );
				if ( ret != 0 )
				{
					if ( inputHandle != null )
					{
						valueList = new ArrayList( inputHandle.getValue1List( ) );
					}
										
					tableViewer.setInput( valueList );
				}
			}
			else
			{
				int ret = create2ValueComposite( operator.getParent( ) );
				if ( ret != 0 && inputHandle != null )
				{
					expressionValue1.setText( DEUtil.resolveNull( inputHandle.getValue1( ) ) );
					expressionValue2.setText( DEUtil.resolveNull( inputHandle.getValue2( ) ) );
				}

			}

			if ( valueVisible == 0 )
			{
				expressionValue1.setVisible( false );
				expressionValue2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( valueVisible == 1 )
			{
				expressionValue1.setVisible( true );
				expressionValue2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( valueVisible == 2 )
			{
				expressionValue1.setVisible( true );
				expressionValue2.setVisible( true );
				andLable.setVisible( true );
				andLable.setEnabled( true );
			}
			updateButtons( );
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}
	};

	protected Listener ComboModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			Assert.isLegal( e.widget instanceof Combo );
			Combo combo = (Combo) e.widget;
			String newValue = combo.getText( );
			String value = DEUtil.getExpression( getResultSetColumn( newValue ) );
			if ( value != null )
				newValue = value;
			combo.setText( newValue );
			updateButtons( );
		}
	};

	protected Composite createDummy( Composite parent, int colSpan )
	{
		Composite dummy = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( );
		gdata.widthHint = 22;
		gdata.horizontalSpan = colSpan;
		gdata.heightHint = 10;
		dummy.setLayoutData( gdata );

		return dummy;
	}

	private Text createText( Composite parent )
	{
		Text txt = new Text( parent, SWT.BORDER );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.widthHint = 100;
		txt.setLayoutData( gdata );

		return txt;
	}

	/*
	 * Update handle for the Map Rule builder
	 */
	public void updateHandle( FilterConditionElementHandle handle, int handleCount )
	{
		this.inputHandle = handle;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		this.designHandle = handle;
		setColumnList( this.designHandle );
	}

	protected IExpressionProvider expressionProvider;

	public void setDesignHandle( DesignElementHandle handle,
			IExpressionProvider provider )
	{
		setDesignHandle( handle );
		this.expressionProvider = provider;
		setColumnList( this.designHandle );
	}

	/*
	 * Return the hanle of Map Rule builder
	 */
	public FilterConditionElementHandle getInputHandle( )
	{
		return inputHandle;
	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons( )
	{
		enableInput( isExpressionOK( ) );
		if ( getButton( IDialogConstants.OK_ID ) != null )
		{
			getButton( IDialogConstants.OK_ID ).setEnabled( isConditionOK( ) );
		}

	}

	protected void enableInput( boolean val )
	{
		operator.setEnabled( val );
		if ( valueVisible != 3 )
		{
			if ( expressionValue1 != null )
				expressionValue1.setEnabled( val );
			if ( expressionValue2 != null )
				expressionValue2.setEnabled( val );
			if ( andLable != null )
			{
				andLable.setEnabled( val );
			}

		}
		else
		{
			setControlEnable( valueListComposite, val );
			if ( val )
			{
				checkAddButtonStatus( );
				checkEditDelButtonStatus( );
			} // or set all the children control to false
		}

	}

	protected void setControlEnable( Control control, boolean bool )
	{
		if ( control == null || control.isDisposed( ) )
		{
			return;
		}
		control.setEnabled( bool );
		Composite tmp = null;
		if ( control instanceof Composite )
		{
			tmp = (Composite) control;
		}
		if ( tmp != null && tmp.getChildren( ) != null )
		{
			for ( int i = 0; i < tmp.getChildren( ).length; i++ )
			{
				setControlEnable( tmp.getChildren( )[i], bool );
			}
		}
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	protected boolean isExpressionOK( )
	{
		if ( expression == null )
		{
			return false;
		}

		if ( expression.getText( ) == null
				|| expression.getText( ).length( ) == 0 )
		{
			return false;
		}

		return true;
	}

	/**
	 * Gets if the condition is available.
	 */
	protected boolean isConditionOK( )
	{
		if ( expression == null )
		{
			return false;
		}

		if ( !isExpressionOK( ) )
		{
			return false;
		}

		return checkValues( );
	}

	/**
	 * Gets if the values of the condition is(are) available.
	 */
	protected boolean checkValues( )
	{
		if ( valueVisible == 3 )
		{
			if ( valueList.size( ) <= 0 )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			assert ( !expressionValue1.isDisposed( ) );
			assert ( !expressionValue2.isDisposed( ) );

			if ( expressionValue1.getVisible( ) )
			{
				if ( expressionValue1.getText( ) == null
						|| expressionValue1.getText( ).trim( ).length( ) == 0 )
				{
					return false;
				}
			}

			if ( expressionValue2.getVisible( ) )
			{
				if ( expressionValue2.getText( ) == null
						|| expressionValue2.getText( ).trim( ).length( ) == 0 )
				{
					return false;
				}
			}
		}

		return true;
	}

	protected void checkAddButtonStatus( )
	{
		if ( addExpressionValue != null )
		{
			String value = addExpressionValue.getText( );
			if ( value == null
					|| value.length( ) == 0
					|| value.trim( ).length( ) == 0 )
			{
				addBtn.setEnabled( false );
				return;
			}
			if ( value != null )
			{
				value = value.trim( );
			}
			if ( valueList.indexOf( value ) < 0 )
			{
				addBtn.setEnabled( true );
			}
			else
			{
				addBtn.setEnabled( false );
			}
		}
	}

	protected void checkEditDelButtonStatus( )
	{
		if ( tableViewer == null )
		{
			return;
		}
		boolean enabled = ( tableViewer.getSelection( ) == null ) ? false
				: true;
		if ( enabled == true
				&& tableViewer.getSelection( ) instanceof StructuredSelection )
		{
			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection( );
			if ( selection.toList( ).size( ) <= 0 )
			{
				enabled = false;
			}
		}
		editBtn.setEnabled( enabled );
		delBtn.setEnabled( enabled );

		enabled = table.getItemCount( ) > 0 ? true : false;
		delAllBtn.setEnabled( enabled );

	}

	/**
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties( )
	{

		expression.setText( DEUtil.resolveNull( getDisplayExpression( inputHandle.getExpr( ) ) ) );
		operator.select( getIndexForOperatorValue( inputHandle.getOperator( ) ) );
		valueVisible = determineValueVisible( inputHandle.getOperator( ) );

		if ( valueVisible == 3 )
		{
			createValueListComposite( operator.getParent( ) );
			valueList = new ArrayList( inputHandle.getValue1List( ) );
			tableViewer.setInput( valueList );
		}
		else
		{
			create2ValueComposite( operator.getParent( ) );
			expressionValue1.setText( DEUtil.resolveNull( inputHandle.getValue1( ) ) );
			expressionValue2.setText( DEUtil.resolveNull( inputHandle.getValue2( ) ) );
		}

		if ( valueVisible == 0 )
		{
			expressionValue1.setVisible( false );
			expressionValue2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( valueVisible == 1 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( false );

			andLable.setVisible( false );
		}
		else if ( valueVisible == 2 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( true );;
			andLable.setVisible( true );
			andLable.setEnabled( true );
		}
		else if ( valueVisible == 3 )
		{
			if ( expression.getText( ).length( ) == 0 )
			{
				valueListComposite.setEnabled( false );
			}
			else
			{
				valueListComposite.setEnabled( true );
			}
		}

	}

	protected void editValue( Control control )
	{
		String initValue = null;
		if ( control instanceof Text )
		{
			initValue = ( (Text) control ).getText( );
		}
		else if ( control instanceof Combo )
		{
			initValue = ( (Combo) control ).getText( );
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
				initValue );

		if ( designHandle != null )
		{
			if ( expressionProvider == null )
				expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
			else
				expressionBuilder.setExpressionProvier( expressionProvider );
		}

		if ( expressionBuilder.open( ) == OK )
		{
			String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
			if ( control instanceof Text )
			{
				( (Text) control ).setText( result );
			}
			else if ( control instanceof Combo )
			{
				( (Combo) control ).setText( result );
			}
		}
		updateButtons( );
	}

	/**
	 * Sets the model input.
	 * 
	 * @param input
	 */
	public void setInput( Object inputHandle )
	{
		if ( inputHandle instanceof FilterConditionElementHandle )
		{
			this.inputHandle = (FilterConditionElementHandle) inputHandle;
		}
		else
		{
			this.inputHandle = null;
		}

	}

	private void refreshList( )
	{
		if ( refreshItems )
		{
			ArrayList finalItems = new ArrayList( 10 );
			for ( int n = 0; n < actions.length; n++ )
			{
				finalItems.add( actions[n] );
			}

			if ( currentItem != null )
			{
				// addParamterItems( finalItems );
			}
			popupItems = (String[]) finalItems.toArray( EMPTY_ARRAY );
		}
		refreshItems = false;
	}

	private transient boolean needRefreshList = true;
	
	/**
	 * @return
	 */
	private List getSelectValueList( )
	{
		if ( needRefreshList == false )
		{
			return selValueList;
		}
		CubeHandle cube = null;
		if ( designHandle instanceof ExtendedItemHandle )
		{
			cube = ( (ExtendedItemHandle) designHandle ).getCube( );
		}
		if ( cube == null
				|| ( !( cube instanceof TabularCubeHandle ) )
				|| expression.getText( ).length( ) == 0 )
		{
			return new ArrayList( );
		}
		Iterator iter = null;

		// get cubeQueryDefn
		IBaseCubeQueryDefinition cubeQueryDefn = null;
		DataRequestSession session = null;
		try
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );

			IReportItem item = ( (ExtendedItemHandle) designHandle ).getReportItem( );
			Chart cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( ChartReportItemUtil.PROPERTY_CHART );
			cubeQueryDefn = new ChartCubeQueryHelper( (ExtendedItemHandle) designHandle,
					cm ).createCubeQuery( null );
			iter = session.getCubeQueryUtil( )
					.getMemberValueIterator( (TabularCubeHandle) cube,
							getExpression( expression.getText( ) ),
							(ICubeQueryDefinition) cubeQueryDefn );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		selValueList = new ArrayList( );
		int count = 0;
		int MAX_COUNT = PreferenceFactory.getInstance( )
		.getPreferences( ChartReportItemUIActivator.getDefault( ),
				UIUtil.getCurrentProject( ) )
		.getInt( ChartReportItemUIActivator.PREFERENCE_MAX_ROW );
		while ( iter != null && iter.hasNext( ) )
		{
			Object obj = iter.next( );
			if ( obj != null )
			{
				if ( selValueList.indexOf( obj ) < 0 )
				{
					selValueList.add( obj );
					if ( ++count >= MAX_COUNT )
					{
						break;
					}
				}

			}

		}
		needRefreshList = false;
		return selValueList;
	}

	public int open( )
	{
		if ( getShell( ) == null )
		{
			// create the window
			create( );
		}
		updateButtons( );
		return super.open( );
	}

}