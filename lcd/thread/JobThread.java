package lcd.thread;

import lcd.Ecu;
import lcd.PropertyValue;

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
