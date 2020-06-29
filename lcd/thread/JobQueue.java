package lcd.thread;

import java.util.LinkedList;

public class JobQueue<T> extends LinkedList<T> {
    @Override
    public synchronized boolean add(T o) {
        notifyAll();
        return super.add(o);
    }

    @Override
    public synchronized T poll() {
        if(size()==0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.poll();
    }
}
