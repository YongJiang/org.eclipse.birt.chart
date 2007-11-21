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

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.report.designer.internal.ui.util.PixelConverter;
import org.eclipse.birt.report.designer.ui.preferences.IStatusChangeListener;
import org.eclipse.birt.report.designer.ui.preferences.OptionsConfigurationBlock;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.preferences.StatusInfo;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 */
public class ChartConfigurationBlock extends OptionsConfigurationBlock
{

	private static final Key PREF_ENALBE_LIVE = getKey( ChartReportItemUIActivator.ID,
			ChartReportItemUIActivator.PREFERENCE_ENALBE_LIVE );
	private static final Key PREF_MAX_ROW = getKey( ChartReportItemUIActivator.ID,
			ChartReportItemUIActivator.PREFERENCE_MAX_ROW );
	private final static String ENABLE_BUTTON = Messages.getString( "ChartPreferencePage.Label.EnableLivePreview" );
	private static final String ENABLED = "true";
	private static final String DISABLED = "false";
	private static final int MAX_ROW_LIMIT = 10000;
	private PixelConverter fPixelConverter;

	public ChartConfigurationBlock( IStatusChangeListener context,
			IProject project )
	{
		super( context, PreferenceFactory.getInstance( )
				.getPreferences( ChartReportItemUIActivator.getDefault( ),
						project ), project, getKeys( ) );
	}

	private static Key[] getKeys( )
	{
		Key[] keys = new Key[]{
				PREF_ENALBE_LIVE, PREF_MAX_ROW
		};
		return keys;
	}

	protected boolean hasProjectSpecificOptions( IProject project )
	{
		return PreferenceFactory.getInstance( )
				.getPreferences( ChartReportItemUIActivator.getDefault( ),
						project )
				.hasSpecialSettings( );
	}
	/*
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	protected Control createContents( Composite parent )
	{
		fPixelConverter = new PixelConverter( parent );
		setShell( parent.getShell( ) );

		Composite mainComp = new Composite( parent, SWT.NONE );
		mainComp.setFont( parent.getFont( ) );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComp.setLayout( layout );

		Composite othersComposite = createBuildPathTabContent( mainComp );
		GridData gridData = new GridData( GridData.FILL,
				GridData.FILL,
				true,
				true );
		gridData.heightHint = fPixelConverter.convertHeightInCharsToPixels( 20 );
		othersComposite.setLayoutData( gridData );

		validateSettings( null, null, null );

		return mainComp;
	}

	private Composite createBuildPathTabContent( Composite parent )
	{

		Composite pageContent = new Composite( parent, SWT.NONE );

		GridData data = new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData( data );

		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 3;
		pageContent.setLayout( layout );

		String[] enableDisableValues = new String[]{
				ENABLED, DISABLED
		};

		addCheckBox( pageContent,
				ENABLE_BUTTON,
				PREF_ENALBE_LIVE,
				enableDisableValues,
				0 );
		addTextField( pageContent,
				Messages.getString( "ChartPreferencePage.Label.MaxRowNumber" ),
				PREF_MAX_ROW,
				0,
				0 );
		return pageContent;
	}

	protected void validateSettings( Key changedKey, String oldValue,
			String newValue )
	{
		fContext.statusChanged( validatePositiveNumber( getValue( PREF_MAX_ROW ) ) );
	}

	protected IStatus validatePositiveNumber( final String number )
	{

		final StatusInfo status = new StatusInfo( );
		String errorMessage = Messages.getString( "ChartPreferencePage.Error.MaxRowInvalid", //$NON-NLS-1$
				new Object[]{
					new Integer( MAX_ROW_LIMIT )
				} );
		if ( number.length( ) == 0 )
		{
			status.setError( errorMessage );
		}
		else
		{
			try
			{
				final int value = Integer.parseInt( number );
				if ( value < 1 || value > MAX_ROW_LIMIT )
				{
					status.setError( errorMessage );
				}
			}
			catch ( NumberFormatException exception )
			{
				status.setError( errorMessage );
			}
		}
		return status;
	}
}
