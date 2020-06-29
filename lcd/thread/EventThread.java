package lcd.thread;

import lcd.EcuConsumption;
import lcd.EcuDistance;
import lcd.ICalculatorEventListener;
import lcd.PropertyValue;

public class EventThread extends Thread {
    private JobQueue<PropertyValue> job;
    private ICalculatorEventListener listener;

    public EventThread(JobQueue<PropertyValue> job, ICalculatorEventListener listener) {
        this.job = job;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (true){
            PropertyValue value = job.poll();
            if(value!=null){
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
    }
}
