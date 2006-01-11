/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.factory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.StructureChangeEvent;
import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.core.i18n.ResourceHandle;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.script.IChartScriptContext;
import org.eclipse.birt.chart.script.IScriptClassLoader;

/**
 * Encapsulates runtime information associated with each chart generation and
 * rendering session. It contains global objects that are defined per request.
 */
public final class RunTimeContext implements Serializable
{

	private static final long serialVersionUID = 1L;

	/**
	 * The locale associated with the runtime context.
	 */
	private Locale lcl = null;

	/**
	 * A chart script context associated with a chart model.
	 */
	private IChartScriptContext csc = null;

	/**
	 * A script handler associated with a chart model.
	 */
	private transient ScriptHandler sh = null;

	/**
	 * A resource handle capable of retrieving externalized messages.
	 */
	private transient ResourceHandle rh = null;

	/**
	 * An interface reference used to lookup externalized messages.
	 */
	private transient IMessageLookup iml = null;

	/**
	 * A structure definition listener associated with this runtime context.
	 */
	private transient IStructureDefinitionListener isdl = null;

	/**
	 * An action renderer associated with this runtime context.
	 */
	private transient IActionRenderer iar = null;

	/**
	 * An script classLoader associated with this runtime context.
	 */
	private transient IScriptClassLoader iscl = null;

	/**
	 * A map to store user defined state object.
	 */
	private HashMap stateStore = null;

	/**
	 * Specifies if enable scripting support in current context.
	 */
	private boolean enableScripting = true;

	/**
	 * A default zero-arg public constructor used for object creation.
	 */
	public RunTimeContext( )
	{
		stateStore = new HashMap( );
	}

	/**
	 * Puts a state object to the store.
	 * 
	 * @param key
	 * @param state
	 */
	public final void putState( Object key, Object state )
	{
		stateStore.put( key, state );
	}

	/**
	 * Returns the state object from store by the key.
	 * 
	 * @param key
	 * @return
	 */
	public final Object getState( Object key )
	{
		return stateStore.get( key );
	}

	/**
	 * Removes the state object by the key.
	 * 
	 * @param key
	 * @return
	 */
	public final Object removeState( Object key )
	{
		return stateStore.remove( key );
	}

	/**
	 * Clears all the stored states.
	 */
	public final void clearState( )
	{
		stateStore.clear( );
	}

	/**
	 * Returns if scriting is enabled in current context.
	 * 
	 * @return
	 */
	public final boolean isScriptingEnabled( )
	{
		return enableScripting;
	}

	/**
	 * Sepcifies if to enable scripting in current context.
	 * 
	 * @param value
	 */
	public final void setScriptingEnabled( boolean value )
	{
		enableScripting = value;
	}

	/**
	 * Returns the script classLoader if available.
	 * 
	 * @return
	 */
	public final IScriptClassLoader getScriptClassLoader( )
	{
		return iscl;
	}

	/**
	 * Sets the script classLoader.
	 * 
	 * @param value
	 */
	public final void setScriptClassLoader( IScriptClassLoader value )
	{
		iscl = value;
	}

	/**
	 * Internally sets an instance of the structure definition listener for
	 * device renderers that need a structure definition notification when
	 * rendering primitives.
	 * 
	 * @param isdl
	 *            The structure definition listener associated with the runtime
	 *            context.
	 */
	public void setStructureDefinitionListener(
			IStructureDefinitionListener isdl )
	{
		this.isdl = isdl;
	}

	/**
	 * Returns an instance of the structure definition listner for device
	 * renderers that need a structure definition notification when rendering
	 * primitives.
	 * 
	 * @return The structure definition listener associated with the runtime
	 *         context.
	 */
	public IStructureDefinitionListener getStructureDefinitionListener( )
	{
		return isdl;
	}

	/**
	 * Sets an IActionRenderer instance to current runtime context.
	 * 
	 * @param iar
	 */
	public void setActionRenderer( IActionRenderer iar )
	{
		this.iar = iar;
	}

	/**
	 * Returns the IActionRenderer of current runtime context.
	 * 
	 * @return
	 */
	public IActionRenderer getActionRenderer( )
	{
		return this.iar;
	}

	/**
	 * Notifies the structure definition listener of a change in the current
	 * running structure that defines a group of primitives being rendered and
	 * puts them into context with reference to the source object.
	 * 
	 * @param sEventName
	 *            Defines the structure being defined along with the event type
	 * @param oSource
	 *            The source object on which the structure is being defined
	 * 
	 * @return 'true' if the structure definition listener exists and was
	 *         notified of the change or 'false' otherwise.
	 */
	public final boolean notifyStructureChange( String sEventName,
			Object oSource )
	{
		if ( isdl == null )
		{
			return false;
		}
		final StructureChangeEvent scev = (StructureChangeEvent) ( (EventObjectCache) isdl ).getEventObject( oSource,
				StructureChangeEvent.class );
		scev.setEventName( sEventName );
		isdl.changeStructure( scev );
		return true;
	}

	/**
	 * Returns the locale associated with this runtime context.
	 * 
	 * @return The locale associated with this runtime context.
	 */
	public final Locale getLocale( )
	{
		return lcl;
	}

	/**
	 * Sets the locale associated with this runtime context. This is usually
	 * done when chart generation begins.
	 * 
	 * @param lcl
	 *            The locale associated with the runtime context.
	 */
	public final void setLocale( Locale lcl )
	{
		this.lcl = lcl;
	}

	/**
	 * Returns an instance of the resource handle for which chart specific
	 * messages are externalized.
	 * 
	 * @return An instance of the resource handle for which chart specific
	 *         messages are externalized.
	 */
	public final ResourceHandle getResourceHandle( )
	{
		return rh;
	}

	/**
	 * Specifies a resource handle that facilitates retrieval of chart specific
	 * externalized messages.
	 * 
	 * @param rh
	 *            The resource handle.
	 */
	public final void setResourceHandle( ResourceHandle rh )
	{
		this.rh = rh;
	}

	/**
	 * Returns an instance of a transient script handler associated with the
	 * chart being generated. The script handler is capable of executing
	 * callback scripts defined in the chart model.
	 * 
	 * @return An instance of the script handler.
	 */
	public final ScriptHandler getScriptHandler( )
	{
		return sh;
	}

	/**
	 * Sets an instance of a transient script handler associated with the chart
	 * being generated. The script handler is capable of executing callback
	 * scripts defined in the chart model.
	 * 
	 * @param sh
	 *            An instance of the script handler.
	 */
	public final void setScriptHandler( ScriptHandler sh )
	{
		this.sh = sh;
	}

	/**
	 * Returns an instance of a script context associated with the chart being
	 * generated.
	 * 
	 * @return An instance of the script context.
	 */
	public final IChartScriptContext getScriptContext( )
	{
		return csc;
	}

	/**
	 * Sets an instance of a chart script context associated with the chart
	 * being generated.
	 * 
	 * @param csc
	 *            An instance of the chart script context.
	 */
	public final void setScriptContext( IChartScriptContext csc )
	{
		this.csc = csc;
	}

	/**
	 * Defines an externalized message lookup implementation per chart model
	 * being executed.
	 * 
	 * @param iml
	 *            The externalized message lookup implementation.
	 */
	public void setMessageLookup( IMessageLookup iml )
	{
		this.iml = iml;
	}

	/**
	 * A convenience method provided to lookup externalized messages associated
	 * with a given message key.
	 * 
	 * @param sChartKey
	 *            The key using which an externalized message is being looked
	 *            up.
	 * 
	 * @return The externalized message associated with the specified key.
	 */
	public final String externalizedMessage( String sChartKey )
	{
		// The key can be either alone, or with its default value: "key=defaultvalue"

		/* Possible cases: 
		 	chartkey	lookup	badkey	nolookup
		 	-------------------------------------
			a=b			get(a)	b		b
			b			get(b)	b		b
			=b			b		-		b
		 */
		String sKey = sChartKey;
		String sDefaultValue = sChartKey;
		final int iKeySeparator = sChartKey.indexOf( IMessageLookup.KEY_SEPARATOR );
		
		if ( iKeySeparator != -1 )
		{
			// VALUE ON RHS OF IMessageLookup.KEY_SEPARATOR
			sDefaultValue = sChartKey.substring( iKeySeparator + 1 );
			
		}
		
		if ( iml == null )
		{
			// no lookup cases
			return sDefaultValue;
		}
		else
		{
			// lookup cases
			if ( iKeySeparator > 0  )
			{
				// a=b case
				sKey = sChartKey.substring( 0, iKeySeparator  );
			}
			else  if ( iKeySeparator == 0)
			{
				// =b case
				return sDefaultValue;
			}
			else
			{
				// b case
				sKey = sDefaultValue;
			}
			String localizedValue = iml.getMessageValue( sKey, getLocale() );
			if ( localizedValue == null || localizedValue.equals( "" ) )
				return sDefaultValue;
			else
				return localizedValue;
		}
		
	}
}