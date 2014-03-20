//
//  cgCanvas.java
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester Institute of Technology. All rights reserved.
//

/**
 * This is a simple canvas class for adding functionality for the
 * 2D portion of Computer Graphics.
 *
 */

import Jama.*;

import javax.swing.text.View;
import java.util.*;

/**
 * Methods implemented by Stephen Yingling where noted
 */
public class cgCanvas extends simpleCanvas {
    protected ArrayList<MyPolygon> polys;   //A list of polygons
    protected int id;   //The current id number
    protected Matrix curTransform;  //The current transformation matrix
    protected ClipWindowStruct cws;     //The clipping window
    protected ViewWindowStruct vws;     //The view port

    /**
     * Constructor
     * Altered by Stephen Yingling
     * @param w width of canvas
     * @param h height of canvas
     */
    cgCanvas (int w, int h)
    {
        super (w, h);
        polys = new ArrayList<MyPolygon>();
        id = 0;
        curTransform = Matrix.identity(3,3);
        cws = new ClipWindowStruct();
        vws = new ViewWindowStruct();
    }

    /**
     * Implemented by Stephen Yingling
     * addPoly - Adds and stores a polygon to the canvas.  Note that this
     *           method does not draw the polygon, but merely stores it for
     *           later draw.  Drawing is initiated by the draw() method.
     *
     *           Returns a unique integer id for the polygon.
     *
     * @param x - Array of x coords of the vertices of the polygon to be added.
     * @param y - Array of y coords of the vertices of the polygin to be added.
     * @param n - Number of verticies in polygon
     *
     * @return a unique integer identifier for the polygon
     */
    public int addPoly (float x[], float y[], int n)
    {
        int curid = id;
        id++;
        MyPolygon p = new MyPolygon(x,y,n);
        polys.add(curid, p);

        return curid;
    }

    /**
     * Implemented by Stephen Yingling
     * drawPoly - Draw the polygon with the given id.  Should draw the
     *        polygon after applying the current transformation on the
     *        vertices of the polygon.
     *
     * @param polyID - the ID of the polygon to be drawn.
     */
    public void drawPoly (int polyID)
    {
        MyPolygon p = polys.get(polyID);

        //Apply transform
        p = p.applyTranform(curTransform);


        //Clip
        float fx[], fy[],nx[],ny[];
        Float x[], y[];
        clipper c = new clipper();

        x = p.getXs().toArray(new Float[p.getXs().size()]);
        y= p.getYs().toArray(new Float[p.getYs().size()]);

        fx = new float[x.length];
        fy = new float[y.length];

        for(int i=0; i< x.length;i++){
            fx[i] = x[i];
            fy[i] = y[i];
        }
        nx = new float[100];
        ny = new float[100];
        int len = c.clipPolygon(x.length,fx,fy,nx,ny,cws.left,cws.bttm,cws.right,cws.top);


        //Viewport
        Matrix view;
        double  sx = (vws.width)/(cws.right-cws.left),
                sy = (vws.height)/(cws.top - cws.bttm),
                tx = (cws.right*vws.x - cws.left*(vws.x+vws.width))/(cws.right-cws.left),
                ty = (cws.top*vws.y - cws.bttm*(vws.y+vws.height))/(cws.top- cws.bttm);
        double [][] viewCol= {{sx,0,tx},{0,sy,ty},{0,0,1}};

        view = Matrix.constructWithCopy(viewCol);

        MyPolygon another = new MyPolygon(nx,ny,len);
        another = another.applyTranform(view);

        //Draw
        Rasterizer r = new Rasterizer(this.getHeight());


        ArrayList<Float> xs = another.getXs(), ys= another.getYs();
        r.drawPolygon(len,xs.toArray(new Float[xs.size()]),ys.toArray(new Float[ys.size()]),this);
    }

    /**
     * Implemented by Stephen Yingling
     * clearTransform - sets the current transformation to be the identity
     *
     */
    public void clearTransform()
    {
        curTransform = Matrix.identity(3,3);
    }

    /**
     * Implemented by Stephen Yingling
     * translate - Add a translation to the current transformation by
     *             pre-multiplying the appropriate translation matrix to
     *             the current transformation matrix.
     *
     * @param x - Amount of translation in x.
     * @param y - Amount of translation in y.
     *
     */
    public void translate (float x, float y)
    {
        double mAsA [][] = {{1,0,x},{0,1,y},{0,0,1}};
        Matrix m = Matrix.constructWithCopy(mAsA);
        curTransform = m.times(curTransform);

    }

    /**
     * Implemented by Stephen Yingling
     * rotate - Add a rotation to the current transformation by
     *          pre-multiplying the appropriate rotation matrix to the
     *          current transformation matrix.
     *
     * @param degrees - Amount of rotation in degrees.
     *
     */
    public void rotate (float degrees)
    {   double rads= Math.toRadians(degrees);
        double mAsA [][] = {{Math.cos(rads),-1.0 * Math.sin(rads),0},{Math.sin(rads),Math.cos(rads),0},{0,0,1}};
        Matrix m = Matrix.constructWithCopy(mAsA);
        curTransform = m.times(curTransform);
    }

    /**
     * Implemented by Stephen Yingling
     * scale - Add a scale to the current transformation by pre-multiplying
     *         the appropriate scaling matrix to the current transformation
     *         matrix.
     *
     * @param x - Amount of scaling in x.
     * @param y - Amount of scaling in y.
     *
     */
    public void scale (float x, float y)
    {
        double mAsA [][] = {{x,0,0},{0,y,0},{0,0,1}};
        Matrix m = Matrix.constructWithCopy(mAsA);
        curTransform = m.times(curTransform);
    }

    /**
     * Implemented by Stephen Yingling
     * setClipWindow - defines the clip window
     *
     * @param bottom - y coord of bottom edge of clip window (in world coords)
     * @param top - y coord of top edge of clip window (in world coords)
     * @param left - x coord of left edge of clip window (in world coords)
     * @param right - x coord of right edge of clip window (in world coords)
     *
     */
    public void setClipWindow (float bottom, float top, float left, float right)
    {
        cws.bttm = bottom;
        cws.left = left;
        cws.top = top;
        cws.right = right;
    }


    /**
     * Implemented by Stephen Yingling
     * setViewport - defines the viewport
     *
     * @param x - x coord of lower left of view window (in screen coords)
     * @param y - y coord of lower left of view window (in screen coords)
     * @param width - width of view window (in world coords)
     * @param height - width of view window (in world coords)
     *
     */
    public void setViewport (int x, int y, int width, int height)
    {
        vws.x = x;
        vws.y = y;
        vws. width = width;
        vws.height = height;
    }

    /**
     * A class to act as a struct representing the clipping window
     * Implemented by Stephen Yingling
     */
    public class ClipWindowStruct{
        public ClipWindowStruct(){}
        public float bttm, top, left, right;
    }

    /**
     * A class, acting as a struct, representing the view port
     * Implemented by Stephen Yingling
     */
    public class ViewWindowStruct{
        public ViewWindowStruct(){}
        public int x,y, width, height;
    }
}
