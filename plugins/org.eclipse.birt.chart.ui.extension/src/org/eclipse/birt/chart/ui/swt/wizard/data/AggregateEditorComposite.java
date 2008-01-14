/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
 
package org.eclipse.birt.chart.ui.swt.wizard.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The class is defined to edit aggregates on value series.
 *  
 * @since BIRT 2.3
 */
public class AggregateEditorComposite extends Composite implements
		MouseListener
{
	private static final String AGG_DISPLAY_NONE = Messages.getString("AggregateEditorComposite.AggregateDisplayName.None"); //$NON-NLS-1$
	
	private static final String AGG_FUNC_NONE = "None"; //$NON-NLS-1$
	
	private Button fBtnAggImage;
	
	private Button fBtnDropDown;
	
	private AggregateDropDownEditorComposite fAggregateEditor;
	
	private SeriesDefinition fSeriesDefi;
	
	private ChartWizardContext fChartContext;

	private SeriesGrouping fGrouping;

	/** Holds the width of each marker UI block */
	private final static int BLOCK_WIDTH = 200;

	/** Holds the width of each marker UI block */
	private final static int BLOCK_HEIGHT = 120;

	
	public AggregateEditorComposite( Composite parent, SeriesDefinition sd, ChartWizardContext context )
	{
		super( parent, SWT.BORDER );
		setSeriesDefinition( sd );
		fChartContext = context;
		placeComponents( );
	}

	public void setSeriesDefinition( SeriesDefinition sd )
	{
		fSeriesDefi = sd;
		if ( sd.getGrouping( ) != null )
		{
			fGrouping = (SeriesGrouping) EcoreUtil.copy( sd.getGrouping( ) );
		}
		else
		{
			fGrouping = SeriesGroupingImpl.create( );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	public void setEnabled( boolean enabled)
	{
		super.setEnabled( enabled );
		fBtnAggImage.setEnabled( enabled );
		fBtnDropDown.setEnabled( enabled );
	}
	
	private void placeComponents( )
	{
		GridLayout layout = new GridLayout( 2, false );
		layout.horizontalSpacing = 2;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		setLayout( layout );

		fBtnAggImage = new Button( this, SWT.NONE );
		{
			GridData gd = new GridData( GridData.FILL );
			ChartUIUtil.setChartImageButtonSizeByPlatform( gd );
			fBtnAggImage.setLayoutData( gd );
			fBtnAggImage.addMouseListener( this );
			fBtnAggImage.setImage( UIHelper.getImage( ChartUIConstants.IMAGE_SIGMA ) );
		}

		fBtnDropDown = new Button( this, SWT.TOGGLE | SWT.ARROW | SWT.DOWN );
		{
			GridData gd = new GridData( );
			ChartUIUtil.setChartImageButtonHeightByPlatform( gd );
			gd.widthHint = 16;
			fBtnDropDown.setLayoutData( gd );
			fBtnDropDown.addMouseListener( this );
		}
	}

	private void toggleDropDown( )
	{
		if ( fAggregateEditor == null || fAggregateEditor.isDisposed( ) )
		{
			createDropDownComponent( );
		}
		else
		{
			fAggregateEditor.getShell( ).close( );
		}
	}

	private void createDropDownComponent( )
	{
		Point pLoc = UIHelper.getScreenLocation( fBtnAggImage.getParent( ) );
		int iXLoc = pLoc.x;
		int iYLoc = pLoc.y + fBtnAggImage.getParent( ).getSize( ).y;
		int iShellWidth = BLOCK_WIDTH;
		int iShellHeight = BLOCK_HEIGHT;

		if ( ( getStyle( ) & SWT.RIGHT_TO_LEFT ) != 0 )
		{
			iXLoc -= iShellWidth;
		}

		// Avoid the right boundary out of screen
		if ( iXLoc + iShellWidth > this.getDisplay( ).getClientArea( ).width )
		{
			iXLoc = this.getDisplay( ).getClientArea( ).width - iShellWidth;
		}

		Shell shell = new Shell( this.getShell( ), SWT.NONE );
		shell.setLayout( new FillLayout( ) );
		shell.setSize( iShellWidth, iShellHeight );
		shell.setLocation( iXLoc, iYLoc );

		setSeriesDefinition( fSeriesDefi );
		fAggregateEditor = new AggregateDropDownEditorComposite( shell, SWT.NONE, null );
		
		shell.layout( );
		shell.open( );
	}

	private boolean isSetAggregate( )
	{
		return ( fGrouping.isEnabled( ) && fGrouping.isSetEnabled( ) );
	}

	/**
	 * The class is defined to edit aggregates. 
	 */
	private class AggregateDropDownEditorComposite extends Composite implements
			PaintListener,
			Listener,
			SelectionListener
	{
//		private Button fBtnAggEnabled;

		private Composite fCmpAggregate;

		private Label fLabelAggregate;

		private Combo fCmbAggregate;

		private Composite fAggParameterComposite;

		private List fAggParamtersTextWidgets = new ArrayList( );
		
		private Map fExprBuilderWidgetsMap = new HashMap();

		private boolean isPressingKey = false;

		private String fTitle = null;

		private Button fBtnOK;

		private Button fBtnCancel;

		AggregateDropDownEditorComposite( Composite parent, int style, String title)
		{
			super( parent, style );
			placeComponents( );
			fTitle = ( title == null || title.length( ) == 0 ) ? Messages.getString( "AggregateEditorComposite.AggregateParameterDefinition.Title" ) : title; //$NON-NLS-1$
		}

		private void placeComponents( )
		{
			GridLayout glDropDown = new GridLayout( );
			this.setLayout( glDropDown );

//			fBtnAggEnabled = new Button( this, SWT.CHECK );
//			{
//				fBtnAggEnabled.setText( Messages.getString("AggregateEditorComposite.Aggregate.Enabled") ); //$NON-NLS-1$
//				fBtnAggEnabled.addSelectionListener( this );
//				fBtnAggEnabled.addListener( SWT.FocusOut, this );
//				fBtnAggEnabled.addListener( SWT.Traverse, this );
//				fBtnAggEnabled.setFocus( );
//				
//				fBtnAggEnabled.setSelection( isSetAggregate( ) );
//			}

			fCmpAggregate = new Composite( this, SWT.NONE );
			GridData gdCMPAggregate = new GridData( GridData.FILL_HORIZONTAL );
			gdCMPAggregate.horizontalSpan = 2;
			fCmpAggregate.setLayoutData( gdCMPAggregate );

			GridLayout glAggregate = new GridLayout( );
			glAggregate.numColumns = 2;
			glAggregate.marginHeight = 0;
			glAggregate.marginWidth = 0;
			glAggregate.horizontalSpacing = 5;
			glAggregate.verticalSpacing = 5;

			fCmpAggregate.setLayout( glAggregate );

			fLabelAggregate = new Label( fCmpAggregate, SWT.NONE );
			GridData gdLBLAggregate = new GridData( );
			fLabelAggregate.setLayoutData( gdLBLAggregate );
			fLabelAggregate.setText( Messages.getString( "SeriesGroupingComposite.Lbl.AggregateExpression" ) ); //$NON-NLS-1$

			fCmbAggregate = new Combo( fCmpAggregate, SWT.DROP_DOWN |
					SWT.READ_ONLY );
			fCmbAggregate.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			fCmbAggregate.addSelectionListener( this );
			fCmbAggregate.addListener( SWT.FocusOut, this );
			fCmbAggregate.addListener( SWT.Traverse, this );
			
			fAggParameterComposite = new Composite( fCmpAggregate, SWT.NONE );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL |
					GridData.GRAB_HORIZONTAL );
			gridData.horizontalIndent = 0;
			gridData.horizontalSpan = 2;
			gridData.exclude = true;
			fAggParameterComposite.setLayoutData( gridData );
			GridLayout layout = new GridLayout( );
			// layout.horizontalSpacing = layout.verticalSpacing = 0;
			layout.marginWidth = layout.marginHeight = 0;
			layout.numColumns = 3;
			fAggParameterComposite.setLayout( layout );
			( (GridData) fAggParameterComposite.getLayoutData( ) ).heightHint = 0;
			
			// Populate aggregate function names.
			populateAggFuncNames( );
			setAggregatesState( );
			
			// Add Buttons.
			Composite btnComposite = new Composite( fCmpAggregate, SWT.NONE );
			gridData = new GridData( GridData.HORIZONTAL_ALIGN_END |
					GridData.VERTICAL_ALIGN_CENTER );
			gridData.horizontalIndent = 0;
			gridData.horizontalSpan = 2;
			btnComposite.setLayoutData( gridData );

			GridLayout glBtn = new GridLayout( );
			glBtn.numColumns = 2;
			glBtn.marginHeight = 0;
			glBtn.marginWidth = 0;
			glBtn.horizontalSpacing = 5;
			glBtn.verticalSpacing = 5;
			btnComposite.setLayout( glBtn );

			fBtnOK = new Button( btnComposite, SWT.NONE );
			GridData gd = new GridData( );
			gd.widthHint = 50;
			fBtnOK.setLayoutData( gd );
			fBtnOK.addSelectionListener( this );
			fBtnOK.setText( Messages.getString( "AggregateEditorComposite.Button.OK" ) ); //$NON-NLS-1$

			fBtnCancel = new Button( btnComposite, SWT.NONE );
			gd = new GridData( );
			gd.widthHint = 50;
			fBtnCancel.setLayoutData( gd );
			fBtnCancel.addSelectionListener( this );
			fBtnCancel.setText( Messages.getString( "AggregateEditorComposite.Button.Cancel" ) ); //$NON-NLS-1$

			getShell( ).pack( );
			
			populateAggFuncNames( );
			setAggregatesState( );
		}

		void focusLost( FocusEvent e )
		{
			Control currentControl = isPressingKey  ? Display.getCurrent( )
					.getFocusControl( ) : Display.getCurrent( )
					.getCursorControl( );
			// Set default value back
			isPressingKey = false;

			// If current control is the dropdown button, that means users want
			// to close it manually. Otherwise, close it silently when clicking
			// other areas.
			if ( currentControl != fBtnAggImage
					&& currentControl != fBtnDropDown
					&& !isChildrenOfThis( currentControl ) )
			{
				closeAggregateEditor( getShell( ) );
			}
		}
		
		private boolean isChildrenOfThis( Control control )
		{
			while ( control != null )
			{
				if ( control == this )
				{
					return true;
				}
				control = control.getParent( );
			}
			return false;
		}
		
		public void handleEvent( Event event )
		{
			switch ( event.type )
			{
				case SWT.FocusOut :
					focusLost( new FocusEvent( event ) );
					break;

				case SWT.Traverse :
					switch ( event.detail )
					{
						case SWT.TRAVERSE_TAB_NEXT :
						case SWT.TRAVERSE_TAB_PREVIOUS :
							// Indicates getting focus control rather than
							// cursor control
							event.doit = true;
							isPressingKey = true;
					}
					break;

			}
		}
		
		private void populateAggFuncNames( )
		{
			String aggFuncName = null;
			fCmbAggregate.removeAll( );

			String[] aggDisplayNames = null;
			String[] aggFunctions = null;
			if ( isBaseGroupingDefined( ) )
			{
				try
				{
					String[] names = aggDisplayNames = PluginSettings.instance( )
							.getRegisteredAggregateFunctionDisplayNames( );
					String[] funcs = PluginSettings.instance( )
							.getRegisteredAggregateFunctions( );
					aggDisplayNames = new String[names.length + 1];
					aggFunctions = new String[names.length + 1];
					
					SeriesDefinition baseSD = (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( fChartContext.getModel( ) ).get( 0 );
					String aggFunc = baseSD.getGrouping( ).getAggregateExpression( );
					int index = 0;
					for ( int i = 0;i < funcs.length; i++)
					{
						if ( funcs[i].equals( aggFunc ))
						{
							index = i;
							break;
						}
					}
					String noneItem = AGG_DISPLAY_NONE + "(" + names[index] + ")"; //$NON-NLS-1$ //$NON-NLS-2$
					aggDisplayNames[0] = noneItem;
					aggFunctions[0] = AGG_FUNC_NONE;
					for (int i = 1; i < aggDisplayNames.length; i ++)
					{
						aggDisplayNames[i] = names[i - 1];
						aggFunctions[i] = funcs[i - 1];
					}
				}
				catch ( ChartException e )
				{
					WizardBase.displayException( e );
				}
			}
			else
			{
				try
				{
					String[] names = PluginSettings.instance( )
							.getRegisteredAggregateFunctionDisplayNames( IAggregateFunction.RUNNING_AGGR );
					String[] funcs = PluginSettings.instance( )
							.getRegisteredAggregateFunctions( IAggregateFunction.RUNNING_AGGR );
					aggDisplayNames = new String[names.length + 1];
					aggFunctions = new String[names.length + 1];
					aggDisplayNames[0] = AGG_DISPLAY_NONE;
					aggFunctions[0] = AGG_FUNC_NONE;
					for (int i = 1; i < aggDisplayNames.length; i ++)
					{
						aggDisplayNames[i] = names[i - 1];
						aggFunctions[i] = funcs[i - 1];
					}
				}
				catch ( ChartException e )
				{
					WizardBase.displayException( e );
				}
			}

			fCmbAggregate.setItems( aggDisplayNames );
			fCmbAggregate.setData( aggFunctions );
			
			if ( fGrouping.getAggregateExpression( ) != null )
			{
				int idx = getAggregateIndexByName( fGrouping.getAggregateExpression( ) );
				if ( fCmbAggregate.getItemCount( ) > idx )
				{
					fCmbAggregate.select( idx );
				}
			}
			else if ( fCmbAggregate.getItemCount( ) > 0 )
			{
				fCmbAggregate.select( 0 );
			}

			aggFuncName = ( (String[]) fCmbAggregate.getData( ) )[fCmbAggregate.getSelectionIndex( )];

			// Populate aggregate parameters.
			showAggregateParameters( aggFuncName );
		}

		private int getAggregateIndexByName( String name )
		{
			String[] names = (String[]) fCmbAggregate.getData( );

			for ( int i = 0; i < names.length; i++ )
			{
				if ( name.equals( names[i] ) )
				{
					return i;
				}
			}
			return 0;
		}

		private void showAggregateParameters( String aggFuncName )
		{
			// Remove old parameters widgets.
			Control[] children = fAggParameterComposite.getChildren( );
			for ( int i = 0; i < children.length; i++ )
			{
				children[i].dispose( );
			}
			fAggParamtersTextWidgets.clear( );
			fExprBuilderWidgetsMap.clear( );
			
			IAggregateFunction aFunc = null;
			try
			{
				aFunc = PluginSettings.instance( )
						.getAggregateFunction( aggFuncName );
			}
			catch ( ChartException e )
			{
				;
			}
			
			String[] args = null;
			if ( aFunc != null ) 
			{
				args = aFunc.getDisplayParameters( );
			}
			
			if ( aFunc != null && args != null && args.length > 0 )
			{
				( (GridData) fAggParameterComposite.getLayoutData( ) ).exclude = false;
				( (GridData) fAggParameterComposite.getLayoutData( ) ).heightHint = SWT.DEFAULT;
				for ( int i = 0; i < args.length; i++ )
				{
					Label lblArg = new Label( fAggParameterComposite, SWT.NONE );
					lblArg.setText( args[i] + ":" ); //$NON-NLS-1$
					GridData gd = new GridData( );
					lblArg.setLayoutData( gd );

					Text txtArg = new Text( fAggParameterComposite, SWT.BORDER );
					GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
					gridData.horizontalIndent = 0;
					txtArg.setLayoutData( gridData );
					fAggParamtersTextWidgets.add( txtArg );
					
					txtArg.addSelectionListener( this );
					txtArg.addListener( SWT.FocusOut, this );
					txtArg.addListener( SWT.Traverse, this );
					txtArg.addFocusListener( new FocusListener( ) {

						public void focusGained( FocusEvent e )
						{
							// TODO Auto-generated method stub

						}

						public void focusLost( FocusEvent e )
						{
							setAggParameter( (Text) e.getSource( ) );
						}
					} );
					
					txtArg.addModifyListener( new ModifyListener( ) {

						public void modifyText( ModifyEvent e )
						{
							setAggParameter( (Text) e.getSource( ) );
						}
					} );
					Button btnBuilder = new Button( fAggParameterComposite, SWT.PUSH );
					{
						fExprBuilderWidgetsMap.put( btnBuilder, txtArg );
						GridData gdBTNBuilder = new GridData( );
						ChartUIUtil.setChartImageButtonSizeByPlatform( gdBTNBuilder );
						btnBuilder.setLayoutData( gdBTNBuilder );
						btnBuilder.setImage( UIHelper.getImage( "icons/obj16/expressionbuilder.gif" ) ); //$NON-NLS-1$
						
						btnBuilder.setToolTipText( Messages.getString( "DataDefinitionComposite.Tooltip.InvokeExpressionBuilder" ) ); //$NON-NLS-1$
						btnBuilder.getImage( ).setBackground( btnBuilder.getBackground( ) );
						btnBuilder.setEnabled( fChartContext.getUIServiceProvider( )
								.isInvokingSupported( ) );
						btnBuilder.setVisible( fChartContext.getUIServiceProvider( )
								.isEclipseModeSupported( ) );
						btnBuilder.addSelectionListener( this );
						btnBuilder.addListener( SWT.FocusOut, this );
						btnBuilder.addListener( SWT.Traverse, this );
					}
				}
			}
			else
			{
				( (GridData) fAggParameterComposite.getLayoutData( ) ).heightHint = 0;
				// ( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
			}

			fAggParameterComposite.layout( );
			fCmpAggregate.layout( );

			Composite c = fAggParameterComposite;
			while ( c != getShell( ) )
			{
				c.layout( );
				c = c.getParent( );
			}
			getShell( ).pack( );

			populateAggParameters( );
		}

		private void setAggParameter( Text oSource )
		{
			String text = ( (Text) oSource ).getText( );
			int index = fAggParamtersTextWidgets.indexOf( oSource );
			EList parameters = fGrouping
					.getAggregateParameters( );
			for ( int i = parameters.size( ); i < fAggParamtersTextWidgets.size( ); i++ )
			{
				parameters.add( null );
			}
			parameters.set( index, text );
		}

		private void populateAggParameters( )
		{
			SeriesGrouping grouping = fGrouping;
			EList aggPars = grouping.getAggregateParameters( );
			if ( aggPars.size( ) > 0 )
			{
				int size = aggPars.size( ) > fAggParamtersTextWidgets.size( ) ? fAggParamtersTextWidgets.size( )
						: aggPars.size( );
				for ( int i = 0; i < size; i++ )
				{
					String value = (String) aggPars.get( i );
					if ( value != null )
					{
						( (Text) fAggParamtersTextWidgets.get( i ) ).setText( value );
					}
				}
			}
		}

		private void setAggregatesState( )
		{
			if ( isSetAggregate( ) )
			{
//				fCmbAggregate.setEnabled( true );
				for ( int i = 0; i < fAggParamtersTextWidgets.size( ); i++ )
				{
					( (Text) fAggParamtersTextWidgets.get( i ) ).setEnabled( true );
				}
			}
			else
			{
//				fCmbAggregate.setEnabled( false );
				for ( int i = 0; i < fAggParamtersTextWidgets.size( ); i++ )
				{
					( (Text) fAggParamtersTextWidgets.get( i ) ).setEnabled( false );
				}
			}
		}

		private boolean isAggParametersWidget( Object source )
		{
			return fAggParamtersTextWidgets.contains( source );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetSelected( SelectionEvent e )
		{
			Object source = e.getSource( );
//			if ( source == fBtnAggEnabled )
//			{
//				fGrouping.setEnabled( fBtnAggEnabled.getSelection( ) );
//				populateAggFuncNames( );
//				setAggregatesState( );
//			}
//			else
			if ( source == fCmbAggregate )
			{
				String aggFunc = ( (String[]) fCmbAggregate.getData( ) )[fCmbAggregate.getSelectionIndex( )];
				showAggregateParameters( aggFunc );
				if (AGG_FUNC_NONE.equals( aggFunc ))
				{
					fGrouping.setEnabled( false );
					fGrouping.setAggregateExpression( null );
				}
				else
				{
					fGrouping.setEnabled( true );
					fGrouping.setAggregateExpression( aggFunc );
				}
			}
			else if ( isAggParametersWidget( source ) )
			{
				setAggParameter( (Text) source );
			}
			else if ( isBuilderBtnWidget( source ) )
			{
				try
				{
					Text txtArg = (Text) fExprBuilderWidgetsMap.get( source );
					String sExpr = fChartContext.getUIServiceProvider( )
							.invoke( IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS,
									txtArg.getText( ),
									fChartContext.getExtendedItem( ),
									fTitle );
					txtArg.setText( sExpr );
					setAggParameter( txtArg );
				}
				catch ( ChartException e1 )
				{
					WizardBase.displayException( e1 );
				}
			}
			else if ( source == fBtnOK )
			{
				fSeriesDefi.setGrouping( fGrouping );
				fSeriesDefi.getGrouping( ).eAdapters( ).addAll( fSeriesDefi.eAdapters( ) );
				
				closeAggregateEditor( getShell( ) );
			}
			else if ( source == fBtnCancel )
			{
				closeAggregateEditor( getShell( ) );
			}
		}

		private boolean isBuilderBtnWidget( Object source )
		{
			return fExprBuilderWidgetsMap.containsKey( source );
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl( PaintEvent e )
		{
			//
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick( MouseEvent e )
	{
		;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown( MouseEvent e )
	{
		;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp( MouseEvent e )
	{
		toggleDropDown( );
	}
	
	/**
	 * Close Aggregate editor window.
	 * @param shell
	 */
	private void closeAggregateEditor( Shell shell )
	{
		if ( shell != null && !shell.isDisposed( ) )
		{
			shell.close( );
		}
		fBtnAggImage.setSelection( false );
	}
	
	/**
	 * @return
	 */
	private boolean isBaseGroupingDefined( )
	{
		SeriesDefinition baseSD = (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( fChartContext.getModel( ) )
				.get( 0 );
		if ( baseSD.getGrouping( ) != null && baseSD.getGrouping( ).isEnabled( ) )
		{
			return true;
		}

		return false;
	}
}
