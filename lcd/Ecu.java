package lcd;

import java.util.ArrayList;

public abstract class Ecu {
    private ArrayList<IEcuEventListener> mListeners;
    public void registerListener(IEcuEventListener listener) {
        mListeners.add(listener);
    }
    public void unregisterListener(IEcuEventListener listener) {
        mListeners.remove(listener);
    }

    public Ecu() {
        mListeners = new ArrayList<>();
    }

    public void notifyChange(PropertyValue value) {
        for (int i = 0; i < mListeners.size(); i ++)
            mListeners.get(i).onChangeEvent(value);
    }
}