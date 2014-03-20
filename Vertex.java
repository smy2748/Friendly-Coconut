/**
 * Created by Stephen Yingling on 3/19/14.
 */
public class Vertex {

    protected float x;
    protected float y;

    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vertex(){}

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
