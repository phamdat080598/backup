package lg.example.finaltest.framework;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.Timer;
import java.util.TimerTask;
import dcv.finaltest.hmiapplication.IHMIListener;
import dcv.finaltest.property.IPropertyEventListener;
import dcv.finaltest.property.IPropertyService;
import lg.example.finaltest.property.PropertyEvent;

public class CalculatorListenerFromProperty extends IPropertyEventListener.Stub implements RegisterCalculatorServiceListener {
    private static final String TAG = "CalculatorListenerFromProperty";
    private final int KM_L_UNIT = 0;
    private final int L_100KM_UNIT = 1;
   // private final int SUM_ONE_MINUTE = 20;
    private final int MAX_SIZE = 15;
    private final int MESSAGE_PROPERTY = 1;

    private double mSumConsumption = 0;
    //private int mCount = 0;
    private int lastIndexConsumption = -1;
    private int mConsumptionUnit = KM_L_UNIT;
    private double[] mArrayConsumption = new double[MAX_SIZE];

    private boolean isDistanceValueAvailable = false;
    private boolean isDistanceUnitAvailable = false;
    private boolean isConsumptionValueAvailable = false;
    private boolean isConsumptionUnitAvailable = false;
    private boolean isResetAvailable = false;

    private CalculatorServiceHandler mHandlerService;
    private HandlerThread mHandlerThread;
    private TimerTask timerOneMinute;

    private IHMIListener mHMIListener;

    public CalculatorListenerFromProperty() {
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mHandlerService = new CalculatorServiceHandler(mHandlerThread.getLooper());
    }

    /**
     * receiver HMI connected success Calculator
     * use IHMIListener feedback
     */
    @Override
    public void onRegisterCalculatorSuccess(IHMIListener listener) {
        mHMIListener = listener;
    }

    @Override
    public void unRegisterCalculatorSuccess() {
        mHMIListener = null;
    }

    /**
     * class handler send message to queue of HandlerThread
     */
    private class CalculatorServiceHandler extends Handler {

        public CalculatorServiceHandler(@NonNull Looper looper) {
            super(looper);
        }

        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage(@NonNull Message msg) {
            PropertyEvent event = (PropertyEvent) msg.obj;
            int what = event.getPropertyId();
            Object value = event.getValue();

            if (what == IPropertyService.PROP_DISTANCE_VALUE) {
                distanceValueChangedBinderLocked((Double) value, event.getStatus());
            } else if (what == IPropertyService.PROP_DISTANCE_UNIT) {
                distanceUnitChangedBinderLocked((Integer) value, event.getStatus());
            } else if (what == IPropertyService.PROP_CONSUMPTION_VALUE) {
                consumptionValueChangedBinderLocked((Double) value, event.getStatus());
            } else if (what == IPropertyService.PROP_CONSUMPTION_UNIT) {
                consumptionUnitChangedBinderLocked((Integer) value, event.getStatus());
            } else if (what == IPropertyService.PROP_RESET) {
                setErrorBinderLocked((Boolean) value, event.getStatus());
            } else {
                Log.e(TAG, "not \"what\" handle message");
            }
        }

        @SuppressLint("LongLogTag")
        void sendMessageProperty(PropertyEvent event) {
            if (!sendMessage(obtainMessage(MESSAGE_PROPERTY, event))) {
                Log.e(TAG, "sendMessage fail: " + MESSAGE_PROPERTY);
            }
        }
    }

    /**
     * response error to HMI,if error=true then after 1s response error=false
     */
    @SuppressLint("LongLogTag")
    private void setErrorBinderLocked(boolean value, int status) {
        if (status == PropertyEvent.STATUS_AVAILABLE) {
            isResetAvailable = true;
        } else {
            isResetAvailable = false;
            try {
                mHMIListener.onError(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                Log.e(TAG, "error status unavailable");
                return;
            }
        }
        try {
            mHMIListener.onError(value);
            if (value) {
                resetData();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (isConsumptionUnitAvailable && isConsumptionValueAvailable && isDistanceUnitAvailable && isDistanceValueAvailable && isResetAvailable) {
                            try {
                                mHMIListener.onError(false);
                                mHMIListener.onConsumptionChanged(mArrayConsumption);
                                mHMIListener.onDistanceChanged(0.0);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                long delay = 1000L;
                Timer timer = new Timer("reset delay 1000s");
                timer.schedule(timerTask, delay);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * response Consumption Unit then changed
     */
    @SuppressLint("LongLogTag")
    private void consumptionUnitChangedBinderLocked(int value, int status) {
        if (status == PropertyEvent.STATUS_AVAILABLE) {
            isConsumptionUnitAvailable = true;
        } else {
            isConsumptionUnitAvailable = false;
            try {
                mHMIListener.onError(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                Log.e(TAG, "consumption unit changed status unavailable");
                return;
            }
        }
        try {
            changeUnitConsumptionArray(value);
            mHMIListener.OnConsumptionUnitChanged(value);
            mConsumptionUnit = value;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * response Consumption Value then changed
     */
    @SuppressLint("LongLogTag")
    private void consumptionValueChangedBinderLocked(double value, int status) {
        if (status == PropertyEvent.STATUS_AVAILABLE) {
            isConsumptionValueAvailable = true;
        } else {
            isConsumptionValueAvailable = false;
            try {
                mHMIListener.onError(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                Log.e(TAG, "consumption value changed status unavailable");
                return;
            }
        }
        mSumConsumption += value;
//        mCount++;
//        if (mCount == SUM_ONE_MINUTE) {
//            if(mConsumptionUnit==L_100KM_UNIT){
//                mSumConsumption=100.0/mSumConsumption;
//            }
//            lastIndexConsumption++;
//            if (lastIndexConsumption == MAX_SIZE) {
//                removeIndexOne();
//                lastIndexConsumption--;
//            }
//            mArrayConsumption[lastIndexConsumption]=mSumConsumption;
//            try {
//                mHMIListener.onConsumptionChanged(mArrayConsumption);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            mSumConsumption = 0;
//            mCount = 0;
//        }
        if (timerOneMinute == null) {
            timerOneMinute = new TimerTask() {
                @Override
                public void run() {
                    if (mConsumptionUnit == L_100KM_UNIT) {
                        mSumConsumption = 100.0 / mSumConsumption;
                    }
                    lastIndexConsumption++;
                    if (lastIndexConsumption == MAX_SIZE) {
                        removeIndexOne();
                        lastIndexConsumption--;
                    }
                    mArrayConsumption[lastIndexConsumption] = mSumConsumption;
                    try {
                        mHMIListener.onConsumptionChanged(mArrayConsumption);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    mSumConsumption = 0;
                }
            };
            long delay = 10000L;
            Timer timer = new Timer("replay one minute");
            timer.schedule(timerOneMinute, delay, delay);
        }
    }

    /**
     * response Distance Unit then changed
     */
    @SuppressLint("LongLogTag")
    private void distanceUnitChangedBinderLocked(int value, int status) {
        if (status == PropertyEvent.STATUS_AVAILABLE) {
            isDistanceUnitAvailable = true;
            Log.d(TAG,"error "+status );
        } else {
            isDistanceUnitAvailable = false;
            Log.d(TAG,"error "+status );
            try {
                mHMIListener.onError(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                Log.e(TAG, "consumption unit changed status unavailable");
                return;
            }
        }
        try {
            mHMIListener.onDistanceUnitChanged(value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * response Distance Value then changed
     */
    @SuppressLint("LongLogTag")
    private void distanceValueChangedBinderLocked(double value, int status) {
        if (status == PropertyEvent.STATUS_AVAILABLE) {
            isDistanceValueAvailable = true;
        } else {
            isDistanceValueAvailable = false;
            try {
                mHMIListener.onError(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                Log.e(TAG, "consumption unit changed status unavailable");
                return;
            }
        }
        try {
            mHMIListener.onDistanceChanged(value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * receiver event from Property Service
     * send event to message queue of HandlerThread
     */
    @SuppressLint("LongLogTag")
    @Override
    public void onEvent(PropertyEvent event) throws RemoteException {
        if (mHMIListener == null) {
            Log.e(TAG, "client HMI not register!");
            return;
        }
//        if(lastIndexConsumption==3){//test unavalible
//            PropertyEvent event1 = new PropertyEvent(event.getPropertyId(),PropertyEvent.STATUS_UNAVAILABLE,event.getTimestamp(),event.getValue());
//            mHandlerService.sendMessageProperty(event1);
//        }

        mHandlerService.sendMessageProperty(event);
    }

    /**
     * reset value on Consumption Array = 0
     */
    private void resetData() {
        if(lastIndexConsumption==-1){
            return;
        }
        for (int i = 0; i <= lastIndexConsumption; i++) {
            mArrayConsumption[i] = 0.0;
        }
       // mCount = 0;
        mSumConsumption = 0.0;
        lastIndexConsumption = -1;
    }

    @SuppressLint("LongLogTag")
    private void changeUnitConsumptionArray(int unit) {
        if (mConsumptionUnit == unit||lastIndexConsumption==-1) {
            return;
        }
        for (int i = 0; i <= lastIndexConsumption; i++) {
            mArrayConsumption[i] = 100.0 / mArrayConsumption[i];
        }
        try {
            mHMIListener.onConsumptionChanged(mArrayConsumption);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void removeIndexOne() {
        for (int i = 0; i < MAX_SIZE - 1; i++) {
            mArrayConsumption[i] = mArrayConsumption[i + 1];
        }
    }
}

