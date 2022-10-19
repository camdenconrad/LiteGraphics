public class Position {
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private final double x;
    private final double y;

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Position(double x, double y) {
    this.x = x;
    this.y = y;
}
}
