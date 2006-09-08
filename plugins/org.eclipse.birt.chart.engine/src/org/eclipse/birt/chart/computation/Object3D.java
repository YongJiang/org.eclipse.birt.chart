
package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.internal.computations.Matrix;
import org.eclipse.birt.chart.internal.computations.Polygon;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.Location3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * This class represents a 3D object and is used to perform all 3d operations.
 */
public class Object3D
{

	private Vector[] va;

	private Vector[] viewVa;

	private Vector center;

	private Vector normal;

	private double xMax, xMin;

	private double yMax, yMin;

	private double zMax, zMin;

	/**
	 * Construction by an empty array of coordinates
	 */
	public Object3D( int points )
	{
		va = new Vector[points];
	}

	/**
	 * Construction by one 3d coordinate
	 */
	public Object3D( Location3D la )
	{
		this( new Location3D[]{
			la
		} );
	}

	/**
	 * Construction by an array of 3d coordinates
	 */
	public Object3D( Location3D[] loa )
	{
		va = new Vector[loa.length];
		for ( int i = 0; i < va.length; i++ )
		{
			va[i] = new Vector( loa[i] );
			loa[i].linkToVector( va[i] );
		}
	}

	/**
	 * Construction by another Object3D object
	 */
	public Object3D( Object3D original )
	{
		if ( original == null )
		{
			return;
		}

		this.va = new Vector[original.va.length];
		for ( int i = 0; i < original.va.length; i++ )
		{
			this.va[i] = new Vector( original.va[i] );
		}
		center = original.center;
		normal = original.normal;
		zMax = original.zMax;
		zMin = original.zMin;
		yMax = original.yMax;
		yMin = original.yMin;
		xMax = original.xMax;
		xMin = original.xMin;
	}

	/**
	 * Returns the 3d coordinates for this object.
	 */
	public Location3D[] getLocation3D( )
	{
		Location3D[] loa3d = new Location3D[va.length];
		for ( int i = 0; i < va.length; i++ )
		{
			loa3d[i] = Location3DImpl.create( va[i].get( 0 ),
					va[i].get( 1 ),
					va[i].get( 2 ) );
		}
		return loa3d;
	}

	/**
	 * returns the normal vector (pointing outside the enclosed volume for
	 * oriented polygons.)
	 */
	public Vector getNormal( )
	{
		if ( normal == null )
		{
			if ( va == null || va.length < 3 )
			{
				return null;
			}

			// create vectors with first three points and returns cross products
			Vector v1 = new Vector( va[1] );
			v1.sub( va[0] );
			Vector v2 = new Vector( va[2] );
			v2.sub( va[0] );

			normal = v1.crossProduct( v2 );
		}

		return normal;
	}

	/**
	 * Returns center of gravity of polygon
	 */
	public Vector getCenter( )
	{
		if ( center == null )
		{
			if ( va == null || va.length == 0 )
			{
				return null;
			}

			double m = va.length;

			center = new Vector( );

			for ( int i = 0; i < m; i++ )
			{
				center.add( va[i] );
			}
			center.scale( 1d / m );
		}
		return center;
	}

	/**
	 * Resets all values to defaults.
	 */
	public void reset( )
	{
		this.center = null;
		this.normal = null;
		this.va = null;
		this.viewVa = null;
		this.zMax = 0;
		this.zMin = 0;
		this.yMax = 0;
		this.yMin = 0;
		this.xMax = 0;
		this.xMin = 0;
	}

	/**
	 * Returns maximum X value for this object
	 */
	public double getXMax( )
	{
		return xMax;
	}

	/**
	 * Returns minimum X value for this object
	 */
	public double getXMin( )
	{
		return xMin;
	}

	/**
	 * Returns maximum Y value for this object
	 */
	public double getYMax( )
	{
		return yMax;
	}

	/**
	 * Returns minimum Y value for this object
	 */
	public double getYMin( )
	{
		return yMin;
	}

	/**
	 * Returns maximum Z value for this object
	 */
	public double getZMax( )
	{
		return zMax;
	}

	/**
	 * Returns minimum Z value for this object
	 */
	public double getZMin( )
	{
		return zMin;
	}

	/**
	 * Performs transformation by given matrix
	 */
	public void transform( Matrix m )
	{
		for ( int i = 0; i < va.length; i++ )
		{
			va[i].multiply( m );
		}
		if ( center != null )
		{
			// center.multiply( m );
		}
		if ( normal != null )
		{
			// normal.multiply( m );
		}
	}

	private void computeExtremums( )
	{
		xMin = Double.MAX_VALUE;
		xMax = -Double.MAX_VALUE;

		yMin = Double.MAX_VALUE;
		yMax = -Double.MAX_VALUE;

		zMin = Double.MAX_VALUE;
		zMax = -Double.MAX_VALUE;

		for ( int i = 0; i < va.length; i++ )
		{
			xMin = Math.min( xMin, va[i].get( 0 ) );
			xMax = Math.max( xMax, va[i].get( 0 ) );

			yMin = Math.min( yMin, va[i].get( 1 ) );
			yMax = Math.max( yMax, va[i].get( 1 ) );

			zMin = Math.min( zMin, va[i].get( 2 ) );
			zMax = Math.max( zMax, va[i].get( 2 ) );
		}
	}

	/**
	 * Check and clip vectors by given engine.
	 */
	public void clip( Engine3D engine )
	{
		byte retval;

		List lst = new ArrayList( );

		switch ( va.length )
		{
			case 0 :
				break;
			case 1 :
			{
				Vector start = new Vector( va[0] );
				Vector end = new Vector( va[0] );

				retval = engine.checkClipping( start, end );

				if ( retval != Engine3D.OUT_OF_RANGE_BOTH )
				{
					lst.add( start );
				}
			}
				break;
			case 2 :
			{
				Vector start = new Vector( va[0] );
				Vector end = new Vector( va[1] );

				retval = engine.checkClipping( start, end );

				if ( retval != Engine3D.OUT_OF_RANGE_BOTH )
				{
					lst.add( start );
					lst.add( end );
				}
			}
				break;

			default :
			{
				boolean endClipped = false;

				for ( int i = 0; i < va.length; i++ )
				{
					Vector start = null;
					Vector end = null;

					if ( i == va.length - 1 )
					{
						start = new Vector( va[i] );
						end = new Vector( va[0] );
					}
					else
					{
						start = new Vector( va[i] );
						end = new Vector( va[i + 1] );
					}

					retval = engine.checkClipping( start, end );

					if ( retval != Engine3D.OUT_OF_RANGE_BOTH )
					{
						if ( i == 0
								|| ( retval & Engine3D.OUT_OF_RANGE_START ) != 0
								|| endClipped )
						{
							lst.add( start );
						}

						endClipped = false;

						if ( ( retval & Engine3D.OUT_OF_RANGE_END ) != 0 )
						{
							endClipped = true;
						}

						if ( i != va.length - 1 || endClipped )
						{
							lst.add( end );
						}

					}
				}
			}
				break;
		}
		va = (Vector[]) lst.toArray( new Vector[lst.size( )] );
	}

	/**
	 * Prepars for Z-sorting
	 */
	public void prepareZSort( )
	{
		computeExtremums( );
		getNormal( );
		getCenter( );

		viewVa = new Vector[va.length];
		for ( int i = 0; i < va.length; i++ )
		{
			viewVa[i] = new Vector( va[i] );
		}

	}

	/**
	 * Perspective transformation of the vectors.
	 */
	public void perspective( double distance )
	{
		for ( int i = 0; i < va.length; i++ )
		{
			va[i].perspective( distance );
		}
		if ( center != null )
		{
			// center.perspective( distance );
		}
		// computeExtremums( );
	}

	/**
	 * Returns vectors in model frame for this object
	 */
	public Vector[] getVectors( )
	{
		return va;
	}

	/**
	 * Returns vectors in viewer frame for this object
	 */
	public Vector[] getViewerVectors( )
	{
		return viewVa;
	}

	/**
	 * Returns the projected 2D coordinates for this object
	 */
	public Location[] getPoints2D( double xOffset, double yOffset )
	{
		Location[] locations = new Location[va.length];
		for ( int i = 0; i < va.length; i++ )
		{
			locations[i] = LocationImpl.create( va[i].get( 0 ) + xOffset,
					va[i].get( 1 ) + yOffset );
		}
		return locations;
	}

	/**
	 * Computes the normal vector for a line to a plane
	 */
	private Vector getLineNormalToPlane( Vector p1, Vector p2, Object3D plane )
	{
		// The basic idea here is to find one proper direction vector
		// perpendicular to given line according to given plane.
		//
		// 1. we find one plane perpendicular to the given plane and in which
		// the given line resides.
		//
		// 2. we find one line in the computed plane which is perpendicular
		// to the given line, this is just the direction vector we need.
		//
		// To compute the plane(1):
		//
		// assume the line(L) equation is:
		// > A0x + B0y + C0z + D0 = 0
		// > A1x + B1y + C1z + D1 = 0
		//
		// the plane(P) equation is:
		// > Lx + My + Nz + D = 0
		//
		// we get the equation for plane pencil(PC) which contains L is:
		// > lamda*(A0x + B0y + C0z + D0) + mu*(A1x + B1y + C1z + D1) = 0
		//
		// so the normal vector for PC is:
		// > N = (lamda*A0 + mu*A1)i + (lamda*B0 + mu*B1)j + (lamda*C0 + mu+C1)k
		//
		// and normal vector for P is:
		// > N1 = Li + Mj + Nk
		//
		// since N is perpendicular to N1, we can compute the relation between
		// <lamda> and <mu>, thus get the plane equation we need.
		//
		// To find the line(2):
		//
		// assume the equation for computed plane PN is:
		// > V1*(x-P1) + V2*(y-P2) + V3*(z-P3) = 0
		//
		// pick one point in PN:
		// > PT = (P1, P2, P3)
		//
		// now we need to find another plane PVN which contains PT and also is
		// perpendicular to L. Once we find it, combine with the line equation
		// of L we can compute another point PT1 both resides in the line L and
		// plane PVN. Then the vetor Vt:(PT, PT1) is just what we need.
		//

		// the line direction vector
		Vector dv = new Vector( p1 );
		dv.sub( p2 );

		int status = 0;

		if ( dv.get( 0 ) == 0 )
		{
			status |= 1;
		}
		if ( dv.get( 1 ) == 0 )
		{
			status |= 2;
		}
		if ( dv.get( 2 ) == 0 )
		{
			status |= 4;
		}

		// plane equation:
		// pn0*X + pn1*Y + pn2*Z + delta = 0
		Vector pn = plane.getNormal( );
		// double delta = -plane.getViewerVectors( )[0].scalarProduct( pn );

		double x0, y0, z0, x1, y1, z1;

		// TODO temp inital value
		Vector nm = new Vector( plane.getNormal( ) );

		switch ( status )
		{
			case 0 :
				// no projection is zero
				// line equation:
				// (X-p1x)/dv0 = (Y-p1y)/dv1 = (Z-p1z)/dv2
				x0 = dv.get( 0 )
						* ( p1.get( 0 )
								/ dv.get( 0 )
								- p1.get( 1 )
								/ dv.get( 1 ) - ( ( pn.get( 0 )
								* dv.get( 1 )
								* dv.get( 2 ) - pn.get( 1 )
								* dv.get( 0 )
								* dv.get( 2 ) ) / ( pn.get( 2 )
								* dv.get( 0 )
								* dv.get( 1 ) - pn.get( 1 )
								* dv.get( 0 )
								* dv.get( 2 ) ) )
								* ( p1.get( 2 ) / dv.get( 2 ) - p1.get( 1 )
										/ dv.get( 1 ) ) );
				y0 = z0 = 0;

				double alpha = ( ( p1.get( 1 ) / dv.get( 1 ) - p1.get( 0 )
						/ dv.get( 0 ) )
						* dv.get( 0 )
						* dv.get( 0 )
						+ ( p1.get( 1 ) / dv.get( 1 ) - p1.get( 2 )
								/ dv.get( 2 ) )
						* dv.get( 2 )
						* dv.get( 2 ) + dv.get( 0 ) * x0 )
						/ ( dv.get( 0 )
								* dv.get( 0 )
								+ dv.get( 1 )
								* dv.get( 1 ) + dv.get( 2 ) * dv.get( 2 ) );

				x1 = ( alpha + p1.get( 0 ) / dv.get( 0 ) - p1.get( 1 )
						/ dv.get( 1 ) )
						* dv.get( 0 );
				y1 = alpha * dv.get( 1 );
				z1 = ( alpha + p1.get( 2 ) / dv.get( 2 ) - p1.get( 1 )
						/ dv.get( 1 ) )
						* dv.get( 2 );
				nm = new Vector( x1, y1, z1 );
				nm.sub( new Vector( x0, y0, z0 ) );
				break;
			case 1 :
				// X projection is Zero
				// line equation:
				// X=p1x; (Y-p1y)/dv1 = (Z-p1z)/dv2
				// TODO
				break;
			case 2 :
				// Y projection is Zero
				// line equation:
				// Y=p1y; (X-p1x)/dv0 = (Z-p1z)/dv2
				// TODO
				break;
			case 4 :
				// Z projection is Zero
				// line equation:
				// Z=p1z; (X-p1x)/dv0 = (Y-p1y)/dv1
				// TODO
				break;
			default :
				// more than one projection is zero, shouldn't go here
				assert false;
				break;
		}

		return nm;
	}

	/**
	 * Returns if current object is totally aside the given object. "outside"
	 * means along the direction of the viewer vector.
	 */
	protected boolean testAside( Object3D obj, boolean outside, Engine3D engine )
	{
		if ( viewVa.length == 0 || obj.getViewerVectors( ).length == 0 )
		{
			// skip empty object.
			return true;
		}

		// handle case for neither is polygon
		if ( viewVa.length < 3 && obj.getViewerVectors( ).length < 3 )
		{
			// handle two lines case
			// if ( viewVa.length == 2 && obj.getViewerVectors( ).length == 2 )
			// {
			// Vector pv1 = new Vector( viewVa[0] );
			// Vector pv2 = new Vector( viewVa[1] );
			// Vector qv1 = new Vector( obj.getViewerVectors( )[0] );
			// Vector qv2 = new Vector( obj.getViewerVectors( )[1] );
			//
			// pv1.sub( qv1 );
			// pv2.sub( qv1 );
			// qv2.sub( qv1 );
			//
			// return pv1.crossProduct( qv2 )
			// .scalarProduct( qv2.crossProduct( pv2 ) ) >= 0;
			// }

			// TODO test two lines or point in a line.
			return true;
		}

		Vector normal = null;
		Vector ov = viewVa[0];
		Vector[] tva = obj.getViewerVectors( );
		Vector viewDirection = new Vector( 0, 0, 1 );

		if ( viewVa.length < 3 )
		{
			if ( viewVa.length == 2 )
			{
				// find a proper normal vector for this line to plane.
				normal = getLineNormalToPlane( viewVa[0], viewVa[1], obj );
			}
			else if ( viewVa.length == 1 )
			{
				// test point and polygon
				normal = new Vector( obj.getNormal( ) );
			}
		}
		else
		{
			normal = new Vector( getNormal( ) );
		}

		// check if the normal vector of face points to the same direction
		// of the viewing direction
		if ( normal.scalarProduct( viewDirection ) <= 0 )
		{
			normal.inverse( );
		}

		double d = -normal.scalarProduct( ov );

		for ( int i = 0; i < tva.length; i++ )
		{
			double p = tva[i].scalarProduct( normal ) + d;

			if ( outside )
			{
				if ( ChartUtil.mathLT( p, 0 ) )
				{
					return false;
				}
			}
			else
			{
				if ( ChartUtil.mathGT( p, 0 ) )
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Tests if two objects intersects.
	 */
	protected boolean testIntersect( Object3D near, Engine3D engine )
	{
		Vector[] va1 = getViewerVectors( );
		Vector[] va2 = near.getViewerVectors( );

		Vector v;
		Polygon p1 = new Polygon( );
		for ( int i = 0; i < va1.length; i++ )
		{
			v = engine.perspective( va1[i] );
			v = engine.view2Canvas( v );
			p1.add( v.get( 0 ), v.get( 1 ) );
		}

		Polygon p2 = new Polygon( );
		for ( int i = 0; i < va2.length; i++ )
		{
			v = engine.perspective( va2[i] );
			v = engine.view2Canvas( v );
			p2.add( v.get( 0 ), v.get( 1 ) );
		}

		return p1.intersects( p2 );
	}

	/**
	 * Tests if two objects overlap in X diretion.
	 */
	protected boolean testXOverlap( Object3D near )
	{
		return ( ( this.getXMax( ) >= near.getXMax( ) && this.getXMin( ) < near.getXMax( ) ) || ( this.getXMax( ) < near.getXMax( ) && this.getXMax( ) > near.getXMin( ) ) );
	}

	/**
	 * Tests if two objects overlap in Y diretion.
	 */
	protected boolean testYOverlap( Object3D near )
	{
		return ( ( this.getYMax( ) >= near.getYMax( ) && this.getYMin( ) < near.getYMax( ) ) || ( this.getYMax( ) < near.getYMax( ) && this.getYMax( ) > near.getYMin( ) ) );
	}

	/**
	 * Tests if two objects need swapping.
	 */
	public boolean testSwap( Object3D near, Engine3D engine )
	{
		Object3D far = this;
		boolean swap = false;
		if ( far.testXOverlap( near ) && far.testYOverlap( near ) )
		{
			if ( !( near.testAside( far, true, engine ) ) )
			{
				if ( !( far.testAside( near, false, engine ) ) )
				{
					if ( far.testIntersect( near, engine ) )
					{
						swap = true;
					}
				}
			}
		}
		return swap;
	}

	/**
	 * Tests if two objects overlap in Z diretion.
	 */
	public boolean testZOverlap( Object3D near )
	{
		return ( ( this.getZMax( ) >= near.getZMax( ) && this.getZMin( ) < near.getZMax( ) ) || ( this.getZMax( ) < near.getZMax( ) && this.getZMax( ) > near.getZMin( ) ) );
	}
}
