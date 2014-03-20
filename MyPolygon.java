import Jama.Matrix;

import java.util.ArrayList;

/**
 * Created by Stephen Yingling on 3/19/14.
 *
 * A class to hold polygon information
 */
public class MyPolygon {
    protected ArrayList<Float> xs;  //X vertices
    protected ArrayList<Float> ys;  //Y vertices

    /**
     * Construct a new empty polygon
     */
    public MyPolygon(){
        xs = new ArrayList<Float>();
        ys = new ArrayList<Float>();
    }

    /**
     * Construct a polygon with the given vertices
     * @param x The x vertices of the polygon
     * @param y The y vertices of the polygon
     * @param n The number of vertices in the polygon
     */
    public MyPolygon(float x[], float y[], int n){
        xs = new ArrayList<Float>();
        ys = new ArrayList<Float>();

        for(int i=0; i< n; i++){
            xs.add(x[i]);
            ys.add(y[i]);
        }
    }

    /**
     * Returns the list of x vertices
     * @return The list of X vertices
     */
    public ArrayList<Float> getXs() {
        return xs;
    }

    /**
     * Setsthe list of x veritces
     * @param xs The x vertices
     */
    public void setXs(ArrayList<Float> xs) {
        this.xs = xs;
    }

    /**
     * Returns the list of Y vertices
     * @return The list of Y vertices
     */
    public ArrayList<Float> getYs() {
        return ys;
    }

    /**
     * Sets the list of y vertices
     * @param ys The list of y vertices
     */
    public void setYs(ArrayList<Float> ys) {
        this.ys = ys;
    }

    /**
     * Applies the transformation matrix m to all vertices in the polygon
     * @param m The transformation matrix
     * @return A new polygon with all the vertices transformed by m
     */
    public MyPolygon applyTranform(Matrix m){
        MyPolygon result = null;
        double col [][]= {{1},{1},{1}}; //Column vector
        Matrix colM;
        ArrayList<Float> newxs, newys;
        newxs = new ArrayList<Float>();
        newys = new ArrayList<Float>();
        for(int i=0; i< xs.size(); i++){
            col[0][0] = xs.get(i);
            col[1][0] = ys.get(i);
            colM = Matrix.constructWithCopy(col);
            colM = m.times(colM);
            newxs.add(i, (float) colM.get(0, 0));
            newys.add(i, (float) colM.get(1, 0));
        }
        result = new MyPolygon();
        result.setXs(newxs);
        result.setYs(newys);
        return result;
    }
}
