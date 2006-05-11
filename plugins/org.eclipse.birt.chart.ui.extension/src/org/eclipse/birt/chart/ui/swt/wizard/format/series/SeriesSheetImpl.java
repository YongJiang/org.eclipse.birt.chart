/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IChangeWithoutNotification;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * "Series" subtask. Attention: the series layout order must be consistent with
 * series items in the naviagor tree.
 * 
 */
public class SeriesSheetImpl extends SubtaskSheetImpl
		implements
			SelectionListener

{

	private static Hashtable htSeriesNames = null;

	private transient Combo cmbColorBy;

	private transient Cursor curHand = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		final int COLUMN_CONTENT = 4;
		cmpContent = new Composite( parent, SWT.NONE ) {

			public Point computeSize( int wHint, int hHint, boolean changed )
			{
				// Return a fixed height as preferred size of scrolled composite
				Point p = super.computeSize( wHint, hHint, changed );
				p.y = 200;
				return p;
			}
		};
		{
			GridLayout glContent = new GridLayout( COLUMN_CONTENT, false );
			glContent.horizontalSpacing = 10;
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		new Label( cmpContent, SWT.NONE ).setText( Messages.getString( "ChartSheetImpl.Label.ColorBy" ) ); //$NON-NLS-1$

		cmbColorBy = new Combo( cmpContent, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gridData = new GridData( );
			gridData.horizontalSpan = COLUMN_CONTENT - 1;
			cmbColorBy.setLayoutData( gridData );
			NameSet ns = LiteralHelper.legendItemTypeSet;
			cmbColorBy.setItems( ns.getDisplayNames( ) );
			cmbColorBy.select( ns.getSafeNameIndex( getChart( ).getLegend( )
					.getItemType( )
					.getName( ) ) );
			cmbColorBy.addSelectionListener( this );
		}

		Label separator = new Label( cmpContent, SWT.SEPARATOR | SWT.HORIZONTAL );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = COLUMN_CONTENT;
			separator.setLayoutData( gd );
		}

		final int COLUMN_DETAIL = 6;

		ScrolledComposite cmpScroll = new ScrolledComposite( cmpContent,
				SWT.V_SCROLL );
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			gd.horizontalSpan = COLUMN_CONTENT;
			cmpScroll.setLayoutData( gd );

			cmpScroll.setMinHeight( ( ChartUIUtil.getAllOrthogonalSeriesDefinitions( getChart( ) )
					.size( ) + 1 ) * 24 + 40 );
			cmpScroll.setExpandVertical( true );
			cmpScroll.setExpandHorizontal( true );
		}

		Composite cmpList = new Composite( cmpScroll, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( COLUMN_DETAIL, false );
			glContent.horizontalSpacing = 10;
			cmpList.setLayout( glContent );

			cmpScroll.setContent( cmpList );
		}

		Label lblSeries = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblSeries.setLayoutData( gd );
			lblSeries.setFont( JFaceResources.getBannerFont( ) );
			lblSeries.setText( Messages.getString( "SeriesSheetImpl.Label.Series" ) ); //$NON-NLS-1$
		}

		Label lblTitle = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblTitle.setLayoutData( gd );
			lblTitle.setFont( JFaceResources.getBannerFont( ) );
			lblTitle.setText( Messages.getString( "SeriesSheetImpl.Label.Title" ) ); //$NON-NLS-1$
		}

		Label lblType = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblType.setLayoutData( gd );
			lblType.setFont( JFaceResources.getBannerFont( ) );
			lblType.setText( Messages.getString( "SeriesSheetImpl.Label.Type" ) ); //$NON-NLS-1$
		}

		Label lblVisible = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblVisible.setLayoutData( gd );
			lblVisible.setFont( JFaceResources.getBannerFont( ) );
			lblVisible.setText( Messages.getString( "SeriesSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
		}

		Label lblStack = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblStack.setLayoutData( gd );
			lblStack.setFont( JFaceResources.getBannerFont( ) );
			lblStack.setText( Messages.getString( "SeriesSheetImpl.Label.Stacked" ) ); //$NON-NLS-1$
		}

		Label lblTranslucent = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblTranslucent.setLayoutData( gd );
			lblTranslucent.setFont( JFaceResources.getBannerFont( ) );
			lblTranslucent.setText( Messages.getString( "SeriesSheetImpl.Label.Translucent" ) ); //$NON-NLS-1$
		}

		List seriesDefns = ChartUIUtil.getBaseSeriesDefinitions( getChart( ) );
		int treeIndex = 0;
		for ( int i = 0; i < seriesDefns.size( ); i++ )
		{
			new SeriesOptionChoser( ( (SeriesDefinition) seriesDefns.get( i ) ),
					getChart( ) instanceof ChartWithAxes
							? Messages.getString( "SeriesSheetImpl.Label.CategoryXSeries" ) : Messages.getString( "SeriesSheetImpl.Label.CategoryBaseSeries" ), //$NON-NLS-1$ //$NON-NLS-2$
					i,
					treeIndex++ ).placeComponents( cmpList );
		}

		seriesDefns = ChartUIUtil.getAllOrthogonalSeriesDefinitions( getChart( ) );
		for ( int i = 0; i < seriesDefns.size( ); i++ )
		{
			String text = getChart( ) instanceof ChartWithAxes
					? Messages.getString( "SeriesSheetImpl.Label.ValueYSeries" ) : Messages.getString( "SeriesSheetImpl.Label.ValueOrthogonalSeries" ); //$NON-NLS-1$ //$NON-NLS-2$
			new SeriesOptionChoser( ( (SeriesDefinition) seriesDefns.get( i ) ),
					( seriesDefns.size( ) == 1 ? text
							: ( text + " - " + ( i + 1 ) ) ) + ":", i, treeIndex++ ).placeComponents( cmpList ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void onShow( Object context, Object container )
	{
		super.onShow( context, container );
		curHand = new Cursor( Display.getDefault( ), SWT.CURSOR_HAND );
	}

	public Object onHide( )
	{
		curHand.dispose( );
		return super.onHide( );
	}

	private class SeriesOptionChoser
			implements
				SelectionListener,
				Listener,
				MouseListener,
				MouseTrackListener
	{

		private transient SeriesDefinition seriesDefn;
		private transient String seriesName;

		private transient Label lblSeries;
		private transient ExternalizedTextEditorComposite txtTitle;
		private transient Combo cmbTypes;
		private transient Button btnVisible;
		private transient Button btnStack;
		private transient Button btnTranslucent;

		private transient int iSeriesDefinitionIndex = 0;
		// Index of tree item in the navigator tree
		private transient int treeIndex = 0;

		public SeriesOptionChoser( SeriesDefinition seriesDefn,
				String seriesName, int iSeriesDefinitionIndex, int treeIndex )
		{
			this.seriesDefn = seriesDefn;
			this.seriesName = seriesName;
			this.iSeriesDefinitionIndex = iSeriesDefinitionIndex;
			this.treeIndex = treeIndex;
		}

		public void placeComponents( Composite parent )
		{
			Series series = seriesDefn.getDesignTimeSeries( );

			lblSeries = new Label( parent, SWT.NONE );
			{
				GridData gd = new GridData( );
				lblSeries.setLayoutData( gd );
				lblSeries.setText( seriesName );
				lblSeries.setForeground( Display.getDefault( )
						.getSystemColor( SWT.COLOR_DARK_BLUE ) );
				lblSeries.addMouseListener( this );
				lblSeries.addMouseTrackListener( this );
			}

			List keys = null;
			if ( getContext( ).getUIServiceProvider( ) != null )
			{
				keys = getContext( ).getUIServiceProvider( )
						.getRegisteredKeys( );
			}

			txtTitle = new ExternalizedTextEditorComposite( parent,
					SWT.BORDER | SWT.SINGLE,
					-1,
					-1,
					keys,
					getContext( ).getUIServiceProvider( ),
					series.getSeriesIdentifier( ).toString( ) );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				txtTitle.setLayoutData( gd );
				txtTitle.addListener( this );
			}

			cmbTypes = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				cmbTypes.setLayoutData( gd );
				cmbTypes.addSelectionListener( this );
				//Disable the conversion of the first series
				if ( iSeriesDefinitionIndex == 0 )
				{
					cmbTypes.setEnabled( false );
				}
			}

			if ( !series.getClass( ).isAssignableFrom( SeriesImpl.class ) )
			{
				btnVisible = new Button( parent, SWT.CHECK );
				{
					GridData gd = new GridData( );
					gd.horizontalAlignment = SWT.CENTER;
					btnVisible.setLayoutData( gd );
					btnVisible.setSelection( series.isVisible( ) );
					btnVisible.addSelectionListener( this );
				}

				btnStack = new Button( parent, SWT.CHECK );
				{
					GridData gd = new GridData( );
					gd.horizontalAlignment = SWT.CENTER;
					btnStack.setLayoutData( gd );
					btnStack.setEnabled( series.canBeStacked( )
							&& getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL );
					btnStack.setSelection( series.isStacked( ) );
					btnStack.addSelectionListener( this );
				}

				btnTranslucent = new Button( parent, SWT.CHECK );
				{
					GridData gd = new GridData( );
					gd.horizontalAlignment = SWT.CENTER;
					btnTranslucent.setLayoutData( gd );
					btnTranslucent.setSelection( series.isTranslucent( ) );
					btnTranslucent.addSelectionListener( this );
				}
			}
			else
			{
				// Occupy a blank area
				Label dummy = new Label( parent, SWT.CHECK );
				GridData gd = new GridData( );
				gd.horizontalSpan = 3;
				dummy.setLayoutData( gd );
			}

			populateLists( seriesDefn.getDesignTimeSeries( ) );
		}

		public void widgetSelected( SelectionEvent e )
		{
			Series series = seriesDefn.getDesignTimeSeries( );

			if ( e.getSource( ).equals( cmbTypes ) )
			{
				if ( seriesDefn.getDesignTimeSeries( )
						.canParticipateInCombination( ) )
				{
					// Get a new series of the selected type by using as much
					// information as possible from the existing series
					Series newSeries = getNewSeries( cmbTypes.getText( ),
							series );
					newSeries.eAdapters( ).addAll( seriesDefn.eAdapters( ) );
					seriesDefn.getSeries( ).set( 0, newSeries );

					// Refresh UI
					btnVisible.setSelection( newSeries.isVisible( ) );
					btnStack.setEnabled( newSeries.canBeStacked( ) );
					btnStack.setSelection( newSeries.isStacked( ) );
					btnTranslucent.setSelection( newSeries.isTranslucent( ) );
					txtTitle.setText( newSeries.getSeriesIdentifier( )
							.toString( ) );
				}
			}
			else if ( e.getSource( ).equals( btnVisible ) )
			{
				series.setVisible( btnVisible.getSelection( ) );
			}
			else if ( e.getSource( ).equals( btnStack ) )
			{
				series.setStacked( btnStack.getSelection( ) );
			}
			else if ( e.getSource( ).equals( btnTranslucent ) )
			{
				series.setTranslucent( btnTranslucent.getSelection( ) );
			}
		}

		private Series getNewSeries( String sSeriesName, final Series oldSeries )
		{
			try
			{
				// Cache old series
				ChartCacheManager.getInstance( )
						.cacheSeries( iSeriesDefinitionIndex, oldSeries );
				// Find new series
				Series series = ChartCacheManager.getInstance( )
						.findSeries( (String) htSeriesNames.get( sSeriesName ),
								iSeriesDefinitionIndex );
				if ( series == null )
				{
					Class seriesClass = Class.forName( (String) htSeriesNames.get( sSeriesName ) );
					Method createMethod = seriesClass.getDeclaredMethod( "create", new Class[]{} ); //$NON-NLS-1$
					final Series newSeries = (Series) createMethod.invoke( seriesClass,
							new Object[]{} );
					ChartAdapter.changeChartWithoutNotification( new IChangeWithoutNotification( ) {

						public Object run( )
						{
							ChartUIUtil.copyGeneralSeriesAttributes( oldSeries,
									newSeries );
							// newSeries.translateFrom( oldSeries,
							// iSeriesDefinitionIndex,
							// getChart( ) );
							return null;
						}
					} );
					series = newSeries;
				}
				return series;
			}
			catch ( Exception e )
			{
				WizardBase.displayException( e );
			}
			return null;
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void handleEvent( Event event )
		{
			if ( event.widget.equals( txtTitle ) )
			{
				seriesDefn.getDesignTimeSeries( )
						.setSeriesIdentifier( txtTitle.getText( ) );
			}
		}

		private void populateLists( Series series )
		{
			// Populate Series Types List
			if ( series.canParticipateInCombination( ) )
			{
				try
				{
					populateSeriesTypes( PluginSettings.instance( )
							.getRegisteredSeries( ), series );
				}
				catch ( ChartException e )
				{
					e.printStackTrace( );
				}
			}
			else
			{
				String seriesName = PluginSettings.instance( )
						.getSeriesDisplayName( series.getClass( ).getName( ) );
				cmbTypes.add( seriesName );
				cmbTypes.select( 0 );
			}
		}

		private void populateSeriesTypes( String[] allSeriesTypes, Series series )
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
						cmbTypes.add( sDisplayName );
						if ( allSeriesTypes[i].equals( series.getClass( )
								.getName( ) ) )
						{
							cmbTypes.select( cmbTypes.getItemCount( ) - 1 );
						}
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace( );
				}
			}
		}

		public void mouseDoubleClick( MouseEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void mouseDown( MouseEvent e )
		{
			switchTo( treeIndex );
		}

		public void mouseUp( MouseEvent e )
		{

		}

		public void mouseEnter( MouseEvent e )
		{
			lblSeries.setCursor( curHand );
		}

		public void mouseExit( MouseEvent e )
		{
			lblSeries.setCursor( null );
		}

		public void mouseHover( MouseEvent e )
		{
			// TODO Auto-generated method stub

		}

		private void switchTo( int index )
		{
			TreeItem currentItem = getParentTask( ).getNavigatorTree( )
					.getSelection( )[0];
			TreeItem[] children = currentItem.getItems( );
			if ( index < children.length )
			{
				// Switch to specified subtask
				getParentTask( ).switchToTreeItem( children[index] );
			}
		}

	}

	public void widgetSelected( SelectionEvent e )
	{
		if ( e.widget.equals( cmbColorBy ) )
		{
			getChart( ).getLegend( )
					.setItemType( LegendItemType.getByName( LiteralHelper.legendItemTypeSet.getNameByDisplayName( cmbColorBy.getText( ) ) ) );
		}

	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}
}