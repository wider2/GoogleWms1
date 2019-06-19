package wms.googlewms;

public class LePoint {

    private Double x;
    private Double y;

    public void LePoint() {}

    public void LePoint(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(Double x) {this.x = x;}

    public void setY(Double y) {this.y = y;}

    public Double getX() { return x; }

    public Double getY() { return y; }

}