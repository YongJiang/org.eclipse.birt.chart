/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPreviewable;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.CompoundTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.TreeCompoundTask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * UI constants for chart builder
 * 
 */
public class SubtaskSheetImpl implements
		ISubtaskSheet,
		ShellListener,
		ChartUIConstants,
		ITaskPreviewable
{

	private String sNodePath = ""; //$NON-NLS-1$

	private String sTitle = ""; //$NON-NLS-1$

	private int subtaskIndex = 0;

	protected Composite cmpContent = null;

	private ChartWizardContext context = null;

	private WizardBase wizard;

	private Shell popupShell;

	private ITaskPopupSheet popupSheet;

	private ITask parentTask;

	private static boolean POPUP_ATTACHING = false;

	private Map<String, Button> popupButtonRegistry = new HashMap<String, Button>( 6 );

	private Map<String, ITaskPopupSheet> popupSheetRegistry = new HashMap<String, ITaskPopupSheet>( 6 );

	private Map<String, String> lastPopupRegistry = new HashMap<String, String>( 3 );

	public SubtaskSheetImpl( )
	{
		super( );
	}

	public void createControl( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		FillLayout fillLayout = new FillLayout( );
		cmpContent.setLayout( fillLayout );
	}

	public Object onHide( )
	{
		// No need to clear popup selection because it's closed automatically
		ChartWizard.POPUP_CLOSING_BY_USER = false;
		detachPopup( );
		ChartWizard.POPUP_CLOSING_BY_USER = true;

		if ( cmpContent != null )
		{
			cmpContent.dispose( );
		}
		popupButtonRegistry.clear( );
		popupSheetRegistry.clear( );
		return getContext( );
	}

	public void onShow( Object context, Object container )
	{
		this.context = (ChartWizardContext) context;
		this.wizard = (WizardBase) container;
	}

	protected Chart getChart( )
	{
		return context.getModel( );
	}

	protected ChartWizardContext getContext( )
	{
		return context;
	}

	protected WizardBase getWizard( )
	{
		return wizard;
	}

	protected void setWizard( WizardBase wizard )
	{
		this.wizard = wizard;
	}

	/**
	 * Detaches the popup dialogue if the name is same with the widget. Called
	 * when clicking buttons manually.
	 * 
	 * @param widget
	 *            the button widget
	 * @return detach result
	 */
	protected boolean detachPopup( Widget widget )
	{
		if ( widget instanceof Button
				&& popupShell != null
				&& !popupShell.isDisposed( )
				&& !isButtonSelected( ) )
		{
			getWizard( ).detachPopup( );
			popupShell = null;

			// Clear selection if user unselected the button.
			setCurrentPopupSelection( null );
			getParentTask( ).setPopupSelection( null );
			return true;
		}
		return false;
	}

	public boolean detachPopup( )
	{
		if ( popupShell != null && !popupShell.isDisposed( ) )
		{
			getWizard( ).detachPopup( );
			popupShell = null;
			return true;
		}
		return false;
	}

	protected Shell createPopupShell( )
	{
		POPUP_ATTACHING = true;
		Shell shell = getWizard( ).createPopupContainer( );
		shell.addShellListener( this );
		shell.setImage( UIHelper.getImage( "icons/obj16/chartbuilder.gif" ) ); //$NON-NLS-1$
		POPUP_ATTACHING = false;
		return shell;
	}

	/**
	 * Selects all registered buttons
	 * 
	 * @param isSelected
	 *            selection status
	 */
	final protected void selectAllButtons( boolean isSelected )
	{
		Iterator<Button> buttons = popupButtonRegistry.values( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			buttons.next( ).setSelection( isSelected );
		}
	}

	/**
	 * Checks if there's a selected button.
	 */
	protected boolean isButtonSelected( )
	{
		Iterator<Button> buttons = popupButtonRegistry.values( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			if ( buttons.next( ).getSelection( ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether the specified widget is registered in current subtask
	 */
	protected boolean isRegistered( Widget widget )
	{
		Iterator<Button> buttons = popupButtonRegistry.values( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			if ( buttons.next( ).equals( widget ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all registered toggle buttons.
	 */
	protected Collection<Button> getToggleButtons( )
	{
		return popupButtonRegistry.values( );
	}

	protected void refreshPopupSheet( )
	{
		if ( popupShell != null && !popupShell.isDisposed( ) )
		{
			popupSheet.refreshComponent( popupShell );
		}
	}

	public void setIndex( int index )
	{
		subtaskIndex = index;
	}

	protected int getIndex( )
	{
		return subtaskIndex;
	}

	/**
	 * Creates a toggle button to popup and registers it. Note that all toggle
	 * buttons are registered, each button enable should call
	 * setToggleButtonEnabled().
	 * 
	 * @param parent
	 *            control parent
	 * @param buttonId
	 *            button id without node path
	 * @param popupName
	 *            button text and registry key
	 * @param popupSheet
	 *            popup sheet
	 * @param bEnabled
	 *            enabled state
	 * @return button control
	 */
	protected Button createToggleButton( Composite parent, String buttonId,
			String popupName, ITaskPopupSheet popupSheet, boolean bEnabled )
	{
		Button button = new Button( parent, SWT.TOGGLE );
		button.setText( popupName );

		// Use GC to calculate the button width
		GC gc = new GC( parent );
		int width = Math.max( 80, gc.textExtent( popupName ).x + 8 );
		gc.dispose( );

		// To set the span for Button if width is too long
		// int horizontalSpan = width / 66;

		GridData gd = new GridData( );
		gd.widthHint = width;
		// gd.horizontalSpan = horizontalSpan;
		button.setLayoutData( gd );

		String id = getNodePath( ) + buttonId;
		// Save the button id for holding the same type of buttons
		button.setData( buttonId );
		popupButtonRegistry.put( id, button );
		popupSheetRegistry.put( id, popupSheet );
		if ( !getContext( ).isEnabled( id ) )
		{
			// Disable the button if it's registered as disabled
			button.setEnabled( false );
		}
		else
		{
			button.setEnabled( bEnabled );
		}

		return button;
	}

	/**
	 * Creates a toggle button to popup and registers it. Note that all toggle
	 * buttons are registered, each button enable should call
	 * setToggleButtonEnabled().
	 * 
	 * @param parent
	 *            control parent
	 * @param buttonId
	 *            button id without node path
	 * @param popupName
	 *            button text and registry key
	 * @param popupSheet
	 *            popup sheet
	 * @return button control
	 */
	protected Button createToggleButton( Composite parent, String buttonId,
			String popupName, ITaskPopupSheet popupSheet )
	{
		return createToggleButton( parent,
				buttonId,
				popupName,
				popupSheet,
				true );
	}

	/**
	 * Finds the toggle button by exclusive id or button id
	 * 
	 * @param buttonId
	 *            exclusive id or button id
	 * @return the toggle button or null
	 */
	protected Button getToggleButton( String buttonId )
	{
		Button button = popupButtonRegistry.get( buttonId );
		if ( button == null )
		{
			button = popupButtonRegistry.get( getNodePath( ) + buttonId );
		}
		return button;
	}

	protected void setToggleButtonEnabled( String buttonId, boolean isEnabled )
	{
		String id = getNodePath( ) + buttonId;
		if ( getContext( ).isEnabled( buttonId ) )
		{
			getToggleButton( id ).setEnabled( isEnabled );
		}
		else
		{
			getToggleButton( id ).setEnabled( false );
		}
	}

	public void setParentTask( ITask parentTask )
	{
		assert parentTask instanceof TreeCompoundTask;
		this.parentTask = parentTask;
	}

	protected TreeCompoundTask getParentTask( )
	{
		return (TreeCompoundTask) parentTask;
	}

	protected void switchTo( String subtaskPath )
	{
		if ( parentTask instanceof CompoundTask )
		{
			( (CompoundTask) parentTask ).switchTo( subtaskPath );
		}
	}

	public void setNodePath( String nodePath )
	{
		this.sNodePath = nodePath;
	}

	public String getNodePath( )
	{
		return sNodePath;
	}

	public void shellActivated( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void shellClosed( ShellEvent e )
	{
		Control focusControl = Display.getDefault( ).getFocusControl( );
		if ( focusControl instanceof Text )
		{
			// Focus saving the text by focus out
			focusControl.notifyListeners( SWT.FocusOut, null );
		}

		if ( e.widget.equals( popupShell ) )
		{
			if ( !POPUP_ATTACHING )
			{
				selectAllButtons( false );
			}

			if ( ChartWizard.POPUP_CLOSING_BY_USER )
			{
				// Clear selection if user closed the popup.
				setCurrentPopupSelection( null );
				getParentTask( ).setPopupSelection( null );
			}
		}
	}

	public void shellDeactivated( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void shellDeiconified( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void shellIconified( ShellEvent e )
	{
		// TODO Auto-generated method stub

	}

	public boolean attachPopup( String buttonId )
	{
		// If general selection is null or not existent, to open subtask
		// selection.
		boolean affectTaskSelection = true;
		String id = buttonId == null ? null : getNodePath( ) + buttonId;
		if ( id == null || !popupSheetRegistry.containsKey( id ) )
		{
			buttonId = getCurrentPopupSelection( );
			id = buttonId == null ? null : getNodePath( ) + buttonId;
			// Keep task selection since user doesn't change it
			affectTaskSelection = false;
		}

		// If subtask selection is null, do nothing.
		if ( buttonId == null )
		{
			return false;
		}

		detachPopup( );

		if ( popupSheetRegistry.containsKey( id )
				&& getToggleButton( id ).isEnabled( ) )
		{
			// Select the button
			selectAllButtons( false );
			getToggleButton( id ).setSelection( true );

			// Store last popup selection
			setCurrentPopupSelection( buttonId );
			if ( affectTaskSelection )
			{
				getParentTask( ).setPopupSelection( buttonId );
			}

			// Open the popup
			popupShell = createPopupShell( );
			popupSheet = popupSheetRegistry.get( id );
			popupSheet.getUI( popupShell );

			getWizard( ).attachPopup( popupSheet.getTitle( ), -1, -1 );

			return true;
		}
		return false;
	}

	private String getCurrentPopupSelection( )
	{
		return lastPopupRegistry.get( getContext( ).getWizardID( ) );
	}

	private void setCurrentPopupSelection( String lastPopup )
	{
		lastPopupRegistry.put( getContext( ).getWizardID( ), lastPopup );
	}

	public void dispose( )
	{
		// To be overridden
	}

	public Control getControl( )
	{
		return cmpContent;
	}

	/**
	 * @deprecated For later use
	 */
	public String getDescription( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated For later use
	 */
	public String getErrorMessage( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated For later use
	 */
	public Image getImage( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated For later use
	 */
	public String getMessage( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle( )
	{
		return this.sTitle;
	}

	/**
	 * @deprecated For later use
	 */
	public void performHelp( )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @deprecated For later use
	 */
	public void setDescription( String description )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @deprecated For later use
	 */
	public void setImageDescriptor( ImageDescriptor image )
	{
		// TODO Auto-generated method stub

	}

	public void setTitle( String title )
	{
		this.sTitle = title;
	}

	public void setVisible( boolean visible )
	{
		getControl( ).setVisible( visible );
	}

	public boolean isPreviewable( )
	{
		// Doesn't support preview by default
		return false;
	}

	public ChartPreviewPainter createPreviewPainter( )
	{
		return null;
	}

	public void doPreview( )
	{

	}

	public Canvas getPreviewCanvas( )
	{
		return null;
	}
}