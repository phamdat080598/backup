package test;

import java.util.List;

public class EcuConsumption extends Ecu {
    private int unit;
    private List<Double> consumption;

    public EcuConsumption() {
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public List<Double> getConsumption() {
        return consumption;
    }

    public void setConsumption(List<Double> consumption) {
        this.consumption = consumption;
    }
}
