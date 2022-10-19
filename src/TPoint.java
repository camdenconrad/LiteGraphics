import java.awt.*;

public class TPoint extends Point {
    public TPoint(int t) {
        this.t = t;
    }

    public TPoint(int x, int y) {
        super(x,y);
        this.t = 1;
    }

    public TPoint(Point p) {
        super(p);
        this.t = 1;
    }

    public TPoint(int x, int y, double t) {
        super(x, y);
        this.t = t;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double t = 1;

}
