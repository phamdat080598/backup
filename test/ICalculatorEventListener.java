package test;

import java.util.List;

public interface ICalculatorEventListener {
    void onDistanceUnitChanged(int distanceUnit);
    void onDistanceChanged(double distance);
    void OnConsumptionUnitChanged(int consumptionUnit);
    void onConsumptionChanged(List<Double> consumptionList);
    void onError(boolean isError);
}

