package test;

public class WorkerRunnable implements Runnable {

    private PropertyValue value;
    private ICalculatorEventListener listener;

    public WorkerRunnable(PropertyValue value, ICalculatorEventListener listener) {
        this.value =value;
        this.listener = listener;
    }

        @Override
    public void run() {
        if(value.getmStatus()==PropertyValue.STATUS_UNAVAILABLE){
            listener.onError(true);
        }else {
            if (value.getmValue() instanceof EcuDistance) {
                EcuDistance distance = (EcuDistance) value.getmValue();
                listener.onDistanceChanged(distance.getDistance());
                listener.onDistanceUnitChanged(distance.getUnit());
            } else if (value.getmValue() instanceof EcuConsumption) {
                EcuConsumption consumption = (EcuConsumption) value.getmValue();
                listener.onConsumptionChanged(consumption.getConsumption());
                listener.OnConsumptionUnitChanged(consumption.getUnit());
            } else {
                System.out.println("Màn hình LCD delay 1000s");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listener.onError(true);
            }
        }
    }
}
