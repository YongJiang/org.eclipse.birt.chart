/**
 * <copyright>
 * </copyright>
 *
 * $Id: BubbleDataSetImpl.java,v 1.1 2006/12/28 03:49:27 anonymous Exp $
 */

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.data.BubbleDataSet;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Bubble Data Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class BubbleDataSetImpl extends DataSetImpl implements BubbleDataSet
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BubbleDataSetImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return DataPackage.Literals.BUBBLE_DATA_SET;
	}

	/**
	 * A convenience method to create an initialized 'BubbleDataSet' instance
	 * 
	 * @param oValues
	 *            The Collection (of BubbleEntry) or BubbleEntry[] of values
	 *            associated with this dataset
	 * 
	 * @return
	 */
	public static final BubbleDataSet create( Object oValues )
	{
		final BubbleDataSet bds = DataFactory.eINSTANCE.createBubbleDataSet( );
		( (BubbleDataSetImpl) bds ).initialize( );
		bds.setValues( oValues );
		return bds;
	}

	/**
	 * This method performs any initialization of the instance when created
	 * 
	 * Note: Manually written
	 */
	protected void initialize( )
	{
	}

} // BubbleDataSetImpl