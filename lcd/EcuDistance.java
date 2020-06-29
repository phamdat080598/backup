package lcd;

public class EcuDistance extends Ecu {
    private int unit;
    private double distance;

    public EcuDistance() {
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
