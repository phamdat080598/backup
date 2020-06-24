package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumptionCalculator implements IEcuEventListener {

    public static int DISTANCE_KILOMET = 1;
    public static int CONSUMPTION_LIT_KILOMET = 1;
    private ICalculatorEventListener listenerCalculator;

    private EcuDistance distance;
    private EcuConsumption consumption;
    private EcuReset reset;

    private ExecutorService executorLCD;//mỗi khi có tín hiệu từ Ecu được gửi lên calculator,threadPool thực thi runnable gửi tín hiệu từ calculator lên LCD
    private ExecutorService executorCalculator;

    public ConsumptionCalculator(EcuDistance distance, EcuConsumption consumption, EcuReset reset, LCD lcd) {
        this.distance = distance;
        this.consumption = consumption;
        this.reset = reset;
        listenerCalculator = lcd;

        executorLCD = Executors.newFixedThreadPool(1);
        executorCalculator = Executors.newFixedThreadPool(3);
    }

    @Override
    public void onChangeEvent(PropertyValue value) {
        Runnable work = new WorkerRunnable(value, listenerCalculator);
        executorLCD.execute(work);
    }

    public static void main(String[] args) {
         EcuDistance distance = new EcuDistance();
         EcuConsumption consumption = new EcuConsumption();
        EcuReset reset = new EcuReset();
        LCD lcd = new LCD();

         ConsumptionCalculator calculator = new ConsumptionCalculator(distance, consumption, reset, lcd);

        calculator.registerDistance();
        calculator.registerConsumption();
        calculator.registerReset();

        for (int i = 0; i < 5; i++) {
            PropertyValue<EcuDistance> valueDistance = new PropertyValue<>(101, PropertyValue.STATUS_AVAILABLE, 10000, distance);
            distance.setDistance(i);
            distance.setUnit(DISTANCE_KILOMET);
            calculator.executorCalculator.execute(new WorkerThread(distance, valueDistance));
        }
    }

    public void registerDistance() {
        distance.registerListener(this);
    }

    public void registerConsumption() {
        consumption.registerListener(this);
    }

    public void registerReset() {
        reset.registerListener(this);
    }

    /**
     * cancel event Ecu
     */
    public void unRegister() {
        distance.unregisterListener(this);
        consumption.unregisterListener(this);
        reset.unregisterListener(this);
    }

}