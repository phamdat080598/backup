package test;

public class WorkerThread implements Runnable {
    private test.Ecu ecu;
    private PropertyValue value;

    public WorkerThread(test.Ecu ecu, PropertyValue value) {
        this.ecu = ecu;
        this.value = value;
    }

    /**
     * thực thi gửi tín hiệu từ Ecu lên LCD
     * */
    @Override
    public void run() {
        ecu.notifyChange(value);
    }
}
