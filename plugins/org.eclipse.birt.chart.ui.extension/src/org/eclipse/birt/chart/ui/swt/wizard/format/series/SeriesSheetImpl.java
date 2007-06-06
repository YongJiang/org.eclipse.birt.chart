/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionsImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesPaletteSheet;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * "Series" subtask. Attention: the series layout order must be consistent with
 * series items in the naviagor tree.
 * 
 */
public class SeriesSheetImpl extends SubtaskSheetImpl implements
		SelectionListener

{

	private static Hashtable htSeriesNames = null;

	private Combo cmbColorBy;

	private ITaskPopupSheet popup = null;
	
	private static final int LABEL_WIDTH_HINT = 80;
	private static final int HORIZONTAL_SPACING = 5;

	private transient Composite cmpList = null;

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_SERIES );
		final int COLUMN_CONTENT = 4;
		final int COLUMN_DETAIL = 6;
		final int COMPOSITE_WIDTH = ( LABEL_WIDTH_HINT + HORIZONTAL_SPACING )
				* COLUMN_DETAIL;
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
			glContent.horizontalSpacing = HORIZONTAL_SPACING;
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

		ScrolledComposite cmpScroll = new ScrolledComposite( cmpContent,
				SWT.V_SCROLL  | SWT.H_SCROLL );
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			gd.horizontalSpan = COLUMN_CONTENT;
			gd.heightHint = 120;
			cmpScroll.setLayoutData( gd );

			cmpScroll.setMinHeight( ( ChartUIUtil.getAllOrthogonalSeriesDefinitions( getChart( ) )
					.size( ) + 1 ) * 24 + 40 );
			cmpScroll.setMinWidth( COMPOSITE_WIDTH );
			cmpScroll.setExpandVertical( true );
			cmpScroll.setExpandHorizontal( true );
		}

		createSeriesOptions( cmpScroll );

		createButtonGroup( cmpContent );
	}

	private void createSeriesOptions( ScrolledComposite cmpScroll )
	{
		final int COLUMN_DETAIL = 6;
		if ( cmpList == null || cmpList.isDisposed( ) )
		{
			cmpList = new Composite( cmpScroll, SWT.NONE );

			GridLayout glContent = new GridLayout( COLUMN_DETAIL, false );
			glContent.horizontalSpacing = HORIZONTAL_SPACING;
			cmpList.setLayout( glContent );
			cmpList.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			cmpScroll.setContent( cmpList );
		}
		else
		{
			Control[] children = cmpList.getChildren( );
			for ( int i = 0; i < children.length; i++ )
			{
				children[i].dispose( );
			}
		}

		Label lblSeries = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalAlignment = SWT.CENTER;
			lblSeries.setLayoutData( gd );
			lblSeries.setFont( JFaceResources.getBannerFont( ) );
			lblSeries.setText( Messages.getString( "SeriesSheetImpl.Label.Series" ) ); //$NON-NLS-1$
		}

		Label lblTitle = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalAlignment = SWT.CENTER;
			lblTitle.setLayoutData( gd );
			lblTitle.setFont( JFaceResources.getBannerFont( ) );
			lblTitle.setText( Messages.getString( "SeriesSheetImpl.Label.Title" ) ); //$NON-NLS-1$
		}

		Label lblType = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalAlignment = SWT.CENTER;
			lblType.setLayoutData( gd );
			lblType.setFont( JFaceResources.getBannerFont( ) );
			lblType.setText( Messages.getString( "SeriesSheetImpl.Label.Type" ) ); //$NON-NLS-1$
		}

		Label lblVisible = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalAlignment = SWT.CENTER;
			lblVisible.setLayoutData( gd );
			lblVisible.setFont( JFaceResources.getBannerFont( ) );
			lblVisible.setText( Messages.getString( "SeriesSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
		}

		Label lblStack = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalAlignment = SWT.CENTER;
			lblStack.setLayoutData( gd );
			lblStack.setFont( JFaceResources.getBannerFont( ) );
			lblStack.setText( Messages.getString( "SeriesSheetImpl.Label.Stacked" ) ); //$NON-NLS-1$
		}

		Label lblTranslucent = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalAlignment = SWT.CENTER;
			lblTranslucent.setLayoutData( gd );
			lblTranslucent.setFont( JFaceResources.getBannerFont( ) );
			lblTranslucent.setText( Messages.getString( "SeriesSheetImpl.Label.Translucent" ) ); //$NON-NLS-1$
		}

		List seriesDefns = ChartUIUtil.getBaseSeriesDefinitions( getChart( ) );
		int treeIndex = 0;

		if ( getValueSeriesDefinition( )[0].getSeries( ).get( 0 ) instanceof PieSeries )
		{
			for ( int i = 0; i < seriesDefns.size( ); i++ )
			{
				new SeriesOptionChoser( ( (SeriesDefinition) seriesDefns.get( i ) ),
						Messages.getString( "SeriesSheetImpl.Label.CategoryBaseSeries" ), //$NON-NLS-1$ 
						i,
						treeIndex++, false ).placeComponents( cmpList );
			}
		}

		seriesDefns = ChartUIUtil.getAllOrthogonalSeriesDefinitions( getChart( ) );

		boolean canStack = true;
		for ( int i = 0; i < seriesDefns.size( ); i++ )
		{
			if ( !( ( (SeriesDefinition) seriesDefns.get( i ) ).getDesignTimeSeries( ) ).canBeStacked( ) )
			{
				canStack = false;
				break;
			}
		}
		for ( int i = 0; i < seriesDefns.size( ); i++ )
		{
			String text = getChart( ) instanceof ChartWithAxes ? Messages.getString( "SeriesSheetImpl.Label.ValueYSeries" ) : Messages.getString( "SeriesSheetImpl.Label.ValueOrthogonalSeries" ); //$NON-NLS-1$ //$NON-NLS-2$		
			new SeriesOptionChoser( ( (SeriesDefinition) seriesDefns.get( i ) ),
					( seriesDefns.size( ) == 1 ? text
							: ( text + " - " + ( i + 1 ) ) ), i, treeIndex++, canStack ).placeComponents( cmpList ); //$NON-NLS-1$
		}

	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 6, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		popup = new SeriesPaletteSheet( Messages.getString( "SeriesSheetImpl.Label.SeriesPalette" ), //$NON-NLS-1$
				getContext( ),
				getCategorySeriesDefinition( ),
				getValueSeriesDefinition( ),
				isGroupedSeries( ) );

		Button btnSeriesPals = createToggleButton( cmp,
				Messages.getString( "SeriesSheetImpl.Label.SeriesPalette&" ), //$NON-NLS-1$
				popup );
		btnSeriesPals.addSelectionListener( this );
	}

	private SeriesDefinition getCategorySeriesDefinition( )
	{
		SeriesDefinition sd = null;
		if ( getChart( ) instanceof ChartWithAxes )
		{
			sd = ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).get( getIndex( ) ) );
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			sd = ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
					.get( getIndex( ) ) );
		}
		return sd;
	}

	private SeriesDefinition[] getValueSeriesDefinition( )
	{
		SeriesDefinition[] sds = null;
		if ( getChart( ) instanceof ChartWithAxes )
		{
			sds = ( (ChartWithAxes) getChart( ) ).getSeriesForLegend( );
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			sds = (SeriesDefinition[]) ( ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).toArray( ) );
		}
		return sds;
	}

	private class SeriesOptionChoser implements SelectionListener, Listener
	{

		private SeriesDefinition seriesDefn;
		private String seriesName;

		private Link linkSeries;
		private ExternalizedTextEditorComposite txtTitle;
		private Combo cmbTypes;
		private Button btnVisible;
		private Button btnStack;
		private Button btnTranslucent;
		
		private boolean canStack;

		private int iSeriesDefinitionIndex = 0;
		// Index of tree item in the navigator tree
		private int treeIndex = 0;

		public SeriesOptionChoser( SeriesDefinition seriesDefn,
				String seriesName, int iSeriesDefinitionIndex, int treeIndex,
				boolean canStack )
		{
			this.seriesDefn = seriesDefn;
			this.seriesName = seriesName;
			this.iSeriesDefinitionIndex = iSeriesDefinitionIndex;
			this.treeIndex = treeIndex;
			this.canStack = canStack;
		}

		public void placeComponents( Composite parent )
		{
			Series series = seriesDefn.getDesignTimeSeries( );

			linkSeries = new Link( parent, SWT.NONE );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				linkSeries.setLayoutData( gd );
				linkSeries.setText( "<a>" + seriesName + "</a>" ); //$NON-NLS-1$//$NON-NLS-2$
				linkSeries.addSelectionListener( this );
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
				// Disable the conversion of the first series
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
					btnStack.setEnabled( canStack
							&& series.canBeStacked( )
							&& getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL );
					if ( series.isStacked( ) && !canStack )
					{
						btnStack.setSelection( false );
						series.setStacked( false );
					}
					else
					{
						btnStack.setSelection( series.isStacked( ) );
					}
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

					// Get a new series of the selected type by using as
					// much
					// information as possible from the existing series

					Series newSeries = getNewSeries( cmbTypes.getText( ),
							series );

					ChartAdapter.beginIgnoreNotifications( );
					if ( !newSeries.canBeStacked( ) )
					{
						for ( int i = 0; i < getValueSeriesDefinition( ).length; i++ )
						{
							if ( ( getValueSeriesDefinition( )[i] ).getDesignTimeSeries( )
									.isStacked( ) )
							{
								( getValueSeriesDefinition( )[i] ).getDesignTimeSeries( )
										.setStacked( false );
							}
						}
					}
					ChartAdapter.endIgnoreNotifications( );

					newSeries.eAdapters( ).addAll( seriesDefn.eAdapters( ) );
					seriesDefn.getSeries( ).set( 0, newSeries );

					createSeriesOptions( (ScrolledComposite) cmpList.getParent( ) );

					cmpList.layout( );
				}
			}
			else if ( e.getSource( ).equals( btnVisible ) )
			{
				series.setVisible( btnVisible.getSelection( ) );
			}
			else if ( e.getSource( ).equals( btnStack ) )
			{
				series.setStacked( btnStack.getSelection( ) );

				// Default label position is inside if Stacked checkbox is
				// selected.
				if ( series instanceof BarSeries && series.isStacked( ) )
				{
					series.setLabelPosition( Position.INSIDE_LITERAL );
				}
			}
			else if ( e.getSource( ).equals( btnTranslucent ) )
			{
				series.setTranslucent( btnTranslucent.getSelection( ) );
			}
			else if ( e.getSource( ).equals( linkSeries ) )
			{
				switchTo( treeIndex );
			}
		}

		private Series getNewSeries( String sSeriesName, final Series oldSeries )
		{
			boolean bException = false;
			try
			{
				// Cache old series
				ChartCacheManager.getInstance( )
						.cacheSeries( iSeriesDefinitionIndex, oldSeries );
				// Find new series
				Series series = ChartCacheManager.getInstance( )
						.findSeries( ( (Series) htSeriesNames.get( sSeriesName ) ).getDisplayName( ),
								iSeriesDefinitionIndex );
				if ( series == null )
				{
					series = (Series)htSeriesNames.get( sSeriesName );
					ChartAdapter.beginIgnoreNotifications( );
					ChartUIUtil.copyGeneralSeriesAttributes( oldSeries, series );
					// newSeries.translateFrom( oldSeries,
					// iSeriesDefinitionIndex,
					// getChart( ) );
					ChartAdapter.endIgnoreNotifications( );
				}
				return series;
			}
			catch ( Exception e )
			{
				bException = true;
				WizardBase.showException( e.getLocalizedMessage( ) );
			}
			if ( !bException )
			{
				WizardBase.removeException( );
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
				populateSeriesTypes( ChartUIExtensionsImpl.instance( )
						.getUIChartTypeExtensions( ),
						series,
						( (ChartWithAxes) getChart( ) ).getOrientation( ) );
				String sDisplayName =ChartUIUtil.getSeriesDisplayName( series );
				cmbTypes.setText( sDisplayName );
			}
			else
			{
				String seriesName = ChartUIUtil.getSeriesDisplayName( series );
				cmbTypes.add( seriesName );
				cmbTypes.select( 0 );
			}
		}

		private void populateSeriesTypes( Collection allChartType,
				Series series, Orientation orientation )
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
						String sDisplayName = ChartUIUtil.getSeriesDisplayName( newSeries );
						htSeriesNames.put( sDisplayName, newSeries );
						cmbTypes.add( sDisplayName );
					}
				}
			}
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
		// Detach popup dialog if there's selected popup button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getText( ) );
		}

		if ( e.widget.equals( cmbColorBy ) )
		{
			if ( !getChart( ).getLegend( )
					.getItemType( )
					.getName( )
					.equals( LiteralHelper.legendItemTypeSet.getNameByDisplayName( cmbColorBy.getText( ) ) ) )
			{
				getChart( ).getLegend( )
						.setItemType( LegendItemType.getByName( LiteralHelper.legendItemTypeSet.getNameByDisplayName( cmbColorBy.getText( ) ) ) );
				if ( ( getChart( ).getLegend( ).getItemType( ).getValue( ) == LegendItemType.CATEGORIES )
						&& isGroupedSeries( ) )
				{
					// Update color palette of base series
					SeriesDefinition[] osds = getValueSeriesDefinition( );
					SeriesDefinition bsd = getCategorySeriesDefinition( );
					bsd.getSeriesPalette( ).shift( 0 );
					for ( int i = 0; i < osds.length; i++ )
					{
						bsd.getSeriesPalette( )
								.getEntries( )
								.set( i,
										EcoreUtil.copy( (Fill) osds[i].getSeriesPalette( )
												.getEntries( )
												.get( 0 ) ) );
					}
					( (SeriesPaletteSheet) popup ).setCategorySeries( bsd );
				}
				( (SeriesPaletteSheet) popup ).setGroupedPalette( isGroupedSeries( ) );
				refreshPopupSheet( );
			}
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}

	private boolean isGroupedSeries( )
	{
		return ( !getValueSeriesDefinition( )[0].getQuery( )
				.getDefinition( )
				.trim( )
				.equals( "" ) ); //$NON-NLS-1$ );
	}
}