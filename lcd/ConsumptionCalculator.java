package lcd;

import lcd.thread.EventThread;
import lcd.thread.JobQueue;
import lcd.thread.JobThread;

public class ConsumptionCalculator implements IEcuEventListener {

    public static int DISTANCE_ONE_KILOMET = 1;
    public static int UNIT_DISTANCE=1;
    public static int CONSUMPTION_LIT_KILOMET = 1;
    private ICalculatorEventListener listenerCalculator;

    private EcuDistance distance;
    private EcuConsumption consumption;
    private EcuReset reset;

    private JobQueue<PropertyValue> queueJob = new JobQueue<>();

    public ConsumptionCalculator(EcuDistance distance, EcuConsumption consumption, EcuReset reset, LCD lcd) {
        this.distance = distance;
        this.consumption = consumption;
        this.reset = reset;
        listenerCalculator = lcd;

    }

    public JobQueue<PropertyValue> getQueueJob() {
        return queueJob;
    }

    /**
     * đảm bảo tính toàn vẹn cho queue do cùng lúc nhiều thread cùng truy cập
     * */
    @Override
    public synchronized  void onChangeEvent(PropertyValue value) {
        queueJob.add(value);
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

        EventThread event = new EventThread(calculator.getQueueJob(),calculator.listenerCalculator);
        event.start();

        PropertyValue<EcuDistance> valueDistance = new PropertyValue<>(101, PropertyValue.STATUS_AVAILABLE, 10000, distance);
        distance.setDistance(UNIT_DISTANCE);
        distance.setUnit(DISTANCE_ONE_KILOMET);

        JobThread job = new JobThread(valueDistance);

        JobThread job1 = new JobThread(valueDistance);

        JobThread job2 = new JobThread(valueDistance);

        JobThread job3 = new JobThread(valueDistance);
        job.start();
        job1.start();
        job2.start();
        job3.start();



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