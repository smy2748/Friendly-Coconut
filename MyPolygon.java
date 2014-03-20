import Jama.Matrix;

import java.util.ArrayList;

/**
 * Created by Stephen Yingling on 3/19/14.
 */
public class MyPolygon {
    protected ArrayList<Float> xs;
    protected ArrayList<Float> ys;

    public MyPolygon(){
        xs = new ArrayList<Float>();
        ys = new ArrayList<Float>();
    }

    public MyPolygon(float x[], float y[], int n){
        xs = new ArrayList<Float>();
        ys = new ArrayList<Float>();

        for(int i=0; i< n; i++){
            xs.add(x[i]);
            ys.add(y[i]);
        }
    }

    public ArrayList<Float> getXs() {
        return xs;
    }

    public void setXs(ArrayList<Float> xs) {
        this.xs = xs;
    }

    public ArrayList<Float> getYs() {
        return ys;
    }

    public void setYs(ArrayList<Float> ys) {
        this.ys = ys;
    }

    public MyPolygon applyTranform(Matrix m){
        MyPolygon result = null;
        double col [][]= {{1},{1},{1}};
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
