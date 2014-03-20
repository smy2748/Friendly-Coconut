//
//  Clipper.java
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//  Edited by Stephen Yingling
//

/**
 * Object for performing clipping
 * Edited by Stephen Yingling
 */

public class clipper {

    //Enum for which boundary is being clipped against
    public static enum clipSide  {RIGHT,LEFT,TOP,BOTTOM};
    
    /**
     * clipPolygon
     *
     * Implemented by Stephen Yingling
     * 
     * Clip the polygon with vertex count in and vertices inx/iny
     * against the rectangular clipping region specified by lower-left corner
     * (x0,y0) and upper-right corner (x1,y1). The resulting vertices are
     * placed in outx/outy.  
     * 
     * The routine should return the with the vertex count of polygon
     * resultinhg from the clipping.
     *
     * @param in the number of vertices in the polygon to be clipped
     * @param inx - x coords of vertices of polygon to be clipped.
     * @param iny - y coords of vertices of polygon to be clipped.
     * @param outx - x coords of vertices of polygon resulting after clipping.
     * @param outy - y coords of vertices of polygon resulting after clipping.
     * @param x0 - x coord of lower left of clipping rectangle.
     * @param y0 - y coord of lower left of clipping rectangle.
     * @param x1 - x coord of upper right of clipping rectangle.
     * @param y1 - y coord of upper right of clipping rectangle.
     *
     * @return number of vertices in the polygon resulting after clipping
     * 
     */
    public int clipPolygon(int in, float inx[], float iny[], float outx[],
                    float outy[], float x0, float y0, float x1, float y1)
    {
        float outx1[]= new float[50], outy1[] = new float[50];

        //Clip against left side
        int len = clipAgainstBoundary(in,inx,iny,outx,outy,x0,y0,x0,y1,clipSide.LEFT);

        //Clip against bottom side
        len = clipAgainstBoundary(len,outx,outy,outx1,outy1,x0,y0,x1,y0,clipSide.BOTTOM);

        //Clip against right side
        len = clipAgainstBoundary(len, outx1,outy1,outx,outy,x1,y0,x1,y1,clipSide.RIGHT);

        //Clip against top
        len = clipAgainstBoundary(len, outx,outy,outx1,outy1,x0,y1,x1,y1,clipSide.TOP);

        //copy values into outx and out y
        for(int i=0; i< len; i++){
            outx[i] = outx1[i];
            outy[i] = outy1[i];
        }

        return len; // should return number of verrices in clipped poly.
    }

    /**
     * Clips against the given boundary
     * Author: Stephen Yingling
     * @param in the number of vertices in the polygon to be clipped
     * @param inx - x coords of vertices of polygon to be clipped.
     * @param iny - y coords of vertices of polygon to be clipped.
     * @param outx - x coords of vertices of polygon resulting after clipping.
     * @param outy - y coords of vertices of polygon resulting after clipping.
     * @param x0 - x coord of point 0 of the clipping edge
     * @param y0 - y coord of point 0 of the clipping edge
     * @param x1 - x coord of point 1 of the clipping edge
     * @param y1 - y coord of point 1 of the clipping edge
     * @param c - The enum value representing which edge of the clipping rectangle is being used
     *
     * @return number of vertices in the polygon resulting after clipping
     */
    public int clipAgainstBoundary(int in, float inx[], float iny[], float outx[],
                                  float outy[], float x0, float y0, float x1, float y1, clipSide c){

        //If there are no vertices left in the pologon, return 0
        if(in <= 0){
            return 0;
        }

        int outLength = 0;

        //set p to last point in poly
        float px = inx[in-1], py = iny[in-1],sx, sy;

        //point of intersections
        float i[];

        //for each point s in the polygon
        for(int j=0; j< in; j++){
            sx = inx[j];
            sy= iny[j];

            //If point s is inside
            if(inside(sx,sy,x0,y0,x1,y1,c)){
                //if point p is also inside, output s
                if(inside(px,py,x0,y0,x1,y1,c)){
                    outLength = output(sx,sy,outx,outy,outLength);
                }

                //if point p is not inside, calculate intersection with edge
                //output the intersection and s
                else{
                    i = intersect(px,py,sx,sy,x0,y0,x1,y1,c);
                    outLength = output(i[0],i[1],outx, outy,outLength);
                    outLength = output(sx,sy,outx, outy,outLength);
                }
            }
            //If point s is outside
            else{
                //If point p is inside, output intersection
                if(inside(px,py,x0,y0,x1,y1,c)){
                    i = intersect(px, py, sx, sy, x0, y0, x1, y1, c);
                    outLength = output(i[0],i[1],outx, outy,outLength);
                }
            }

            //set point p=s
            px = sx;
            py = sy;
        }

        return outLength;   //Length of the new vertice list
    }

    /**
     *  Determines whether or not a point is within the given boundary
     *  Author: Stephen Yingling
     * @param x The x coordinate of the point being checked
     * @param y The y coordinate of the point being checked
     * @param x0 The x coordinate of point 0 of the boundary edge
     * @param y0 The y coordinate of point 0 of the boundary edge
     * @param x1 The x coordinate of point 1 of the boundary edge
     * @param y1 The y coordinate of point 1 of the boundary edge
     * @param c The enum determining which edge is being clipped against
     * @return True if the point is within the boundary, false if not
     */
    public boolean inside(float x, float y, float x0, float y0, float x1, float y1, clipSide c){

        //Check that the point is above the bootom
        if(c.equals(clipSide.BOTTOM)){
            if (y < y0){return false;}
        }

        //Check that the point is below the top
        if(c.equals(clipSide.TOP)){
            if(y > y1){return false;}
        }

        //Check the the point is right of the left boundary
        if(c.equals(clipSide.LEFT)){
            if(x<x0){return false;}
        }

        //Check that the point is left of the right boundary
        if(c.equals(clipSide.RIGHT)){
            if(x>x1){return false;}
        }

        return true;
    }

    /**
     * Places the point x,y in the output arrays and returns the new length
     * Author: Stephen Yingling
     * @param x The x coordinate of the point being added
     * @param y The y coordinate of the point being checked
     * @param outx The output array of x coordinates
     * @param outy The output array of y coordinates
     * @param length The current number of vertices in the outx and outy arrays
     * @return The new length of vertices in the outx and outy arrays
     */
    public int output(float x, float y, float outx[], float outy[], int length){
        outx[length] = x;
        outy[length] = y;

        return length+1;

    }

    /**
     * Finds and returns the intersection point of the line PS and the boundary edge
     * Author Stephen Yingling
     * @param px X coordinate of point p
     * @param py y coordinate of point p
     * @param sx X coordinate of point s
     * @param sy y coordinate of point s
     * @param x0 The x coordinate of point 0 of the boundary edge
     * @param y0 The y coordinate of point 0 of the boundary edge
     * @param x1 The x coordinate of point 1 of the boundary edge
     * @param y1 The y coordinate of point 1 of the boundary edge
     * @param c The enum determining which edge is being clipped against
     * @return A 2 entry array with the x and y coordinate of the intersection point
     */
    public float[] intersect(float px, float py, float sx, float sy, float x0, float y0, float x1, float y1,clipSide c){
        float[] result = new float[2];

        //calculate dx and dy
        float dx = px-sx, dy = py-sy;

        //Clip against horizontal edges
        if(c.equals(clipSide.BOTTOM) || c.equals(clipSide.TOP)){
            result[1] = y1;
            result[0] = (dx/dy)*(y1-py) + px;
        }

        //Clip against vertical edges
        if(c.equals(clipSide.LEFT) || c.equals(clipSide.RIGHT)){
            result[0] = x0;
            result[1] = (dy/dx)*(x0-px)+py;
        }

        return result;
    }

}
