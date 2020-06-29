package lcd;

import java.util.List;

public class LCD implements ICalculatorEventListener {

    private double sumDistance = 0;
    private double sumConsumption = 0;
    @Override
    public void onDistanceUnitChanged(int distanceUnit) {
        System.out.println("onDistanceUnitChanged: "+distanceUnit);
    }
    @Override
    public void onDistanceChanged(double distance) {
        sumDistance +=distance;
        System.out.println("onDistanceChanged: "+sumDistance + " Km");

    }
    @Override
    public void OnConsumptionUnitChanged(int consumptionUnit) {
        System.out.println("OnConsumptionUnitChanged: "+consumptionUnit);
    }

    @Override
    public void onConsumptionChanged(List<Double> consumptionList) {
        sumConsumption += consumptionList.get(0);
        System.out.println("onConsumptionChanged: "+sumConsumption);
    }

    @Override
    public void onError(boolean isError) {
        System.out.println("onError: "+isError);
    }
}

