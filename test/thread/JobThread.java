package test.thread;

import test.Ecu;
import test.PropertyValue;

public class JobThread extends Thread {
    private PropertyValue value;

    public JobThread(PropertyValue value) {
        this.value = value;
    }

    /**
     * thực thi gửi tín hiệu từ Ecu lên LCD
     * */
    @Override
    public void run() {
        ((Ecu)value.getmValue()).notifyChange(value);
    }
}
