package lg.example.finaltest.framework;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import dcv.finaltest.configuration.IConfigurationService;
import dcv.finaltest.hmiapplication.IHMIListener;
import dcv.finaltest.hmiapplication.IServiceInterface;
import dcv.finaltest.property.IPropertyService;
import lg.example.finaltest.hmiapplication.TestCapability;
import lg.example.finaltest.property.PropertyEvent;

import static dcv.finaltest.property.IPropertyService.PROP_CONSUMPTION_UNIT;

public class CalculatorListenerFromHMI extends IServiceInterface.Stub {
    private static final String TAG = "CalculatorListenerFromHMI";

    private HandlerThread mHandlerThread;
    private CalculatorServiceHandler mHandlerCalculator;
    private Client mClient = null;
    private IPropertyService mPropertyClient;
    private IConfigurationService mConfigurationClient;
    private RegisterCalculatorServiceListener mRegisterCalculatorListener;

    public CalculatorListenerFromHMI(RegisterCalculatorServiceListener listener) {
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mHandlerCalculator = new CalculatorServiceHandler(mHandlerThread.getLooper());
        mRegisterCalculatorListener = listener;
    }

    public void setPropertyService(IPropertyService mPropertyClient) {
        this.mPropertyClient = mPropertyClient;
    }

    public void setConfigurationClient(IConfigurationService mConfigurationClient) {
        this.mConfigurationClient = mConfigurationClient;
    }

    private class CalculatorServiceHandler extends Handler {

        private static final int MSG_REGISTER_CALCULATOR_LISTENER = 1;
        private static final int MSG_UNREGISTER_CALCULATOR_LISTENER = 2;
        private static final int MSG_SET_DISTANCE_UNIT = 4;
        private static final int MSG_SET_CONSUMPTION_UNIT = 5;
        private static final int MSG_RESET_DATA = 6;

        public CalculatorServiceHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_REGISTER_CALCULATOR_LISTENER) {
                IHMIListener listener = (IHMIListener) msg.obj;
                if (listener == null) {
                    return;
                }
                registerCalculatorBinderLocked(listener);
            } else if (msg.what == MSG_UNREGISTER_CALCULATOR_LISTENER) {
                IHMIListener listener = (IHMIListener) msg.obj;
                if (listener == null) {
                    return;
                }
                unregisterCalculatorBinderLocked(listener);
            } else if (msg.what == MSG_SET_DISTANCE_UNIT) {
                setDistanceUnitBinderLocked((Integer) msg.obj);
            } else if (msg.what == MSG_SET_CONSUMPTION_UNIT) {
                setConsumptionUnitBinderLocked((Integer) msg.obj);
            } else {
                setResetBinderLocked();
            }
        }

        @SuppressLint("LongLogTag")
        void registerCalculatorListener(IHMIListener listener) {
            if (!sendMessage(obtainMessage(MSG_REGISTER_CALCULATOR_LISTENER, listener))) {
                Log.e(TAG, "sendMessage fail: " + MSG_REGISTER_CALCULATOR_LISTENER);
            }
        }

        @SuppressLint("LongLogTag")
        void unregisterCalculatorListener(IHMIListener listener) {
            if (!sendMessage(obtainMessage(MSG_UNREGISTER_CALCULATOR_LISTENER, listener))) {
                Log.e(TAG, "sendMessage fail: " + MSG_UNREGISTER_CALCULATOR_LISTENER);
            }
        }

        @SuppressLint("LongLogTag")
        void setDistanceUnitValue(int unit) {
            if (!sendMessage(obtainMessage(MSG_SET_DISTANCE_UNIT, unit))) {
                Log.e(TAG, "sendMessage fail: " + MSG_SET_DISTANCE_UNIT);
            }
        }

        @SuppressLint("LongLogTag")
        void setConsumptionUnitValue(int unit) {
            if (!sendMessage(obtainMessage(MSG_SET_CONSUMPTION_UNIT, unit))) {
                Log.e(TAG, "sendMessage fail: " + MSG_SET_CONSUMPTION_UNIT);
            }
        }

        @SuppressLint("LongLogTag")
        void resetDataListener() {
            if (!sendMessage(obtainMessage(MSG_RESET_DATA))) {
                Log.e(TAG, "sendMessage fail: " + MSG_RESET_DATA);
            }
        }
    }

    private void registerCalculatorBinderLocked(IHMIListener listener) {
        if (mClient == null) {
            mClient = new Client(listener);
        }
    }

    /**
     * in case many client use listener clear in list Client
     */
    private void unregisterCalculatorBinderLocked(IHMIListener listener) {
        if (mClient == null) {
            return;
        }
        mClient.release();
        mClient = null;
    }

    @SuppressLint("LongLogTag")
    private void setConsumptionUnitBinderLocked(int unit) {
        if (mPropertyClient == null) {
            Log.d(TAG, "not connected Property Service !");
            return;
        }
        try {
            mPropertyClient.setProperty(PROP_CONSUMPTION_UNIT, new PropertyEvent(PROP_CONSUMPTION_UNIT, PropertyEvent.STATUS_AVAILABLE, 0, unit));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    private void setDistanceUnitBinderLocked(int unit) {
        if (mPropertyClient == null) {
            Log.d(TAG, "not connected Property Service !");
            return;
        }
        try {
            mPropertyClient.setProperty(IPropertyService.PROP_DISTANCE_UNIT, new PropertyEvent(IPropertyService.PROP_DISTANCE_UNIT, PropertyEvent.STATUS_AVAILABLE, 0, unit));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    private void setResetBinderLocked() {
        if (mPropertyClient == null) {
            Log.d(TAG, "not connected Property Service !");
            return;
        }
        try {
            mPropertyClient.setProperty(IPropertyService.PROP_RESET, new PropertyEvent(IPropertyService.PROP_RESET, PropertyEvent.STATUS_AVAILABLE, 0, true));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class Client implements IBinder.DeathRecipient {
        private final IHMIListener mListener;
        private final IBinder mListenerBinder;

        @SuppressLint("LongLogTag")
        Client(IHMIListener listener) {
            mListener = listener;
            mListenerBinder = listener.asBinder();
            mRegisterCalculatorListener.onRegisterCalculatorSuccess(listener);
            try {
                mListenerBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "\"Failed to link death for recipient. " + e);
                throw new IllegalStateException("Client already dead", e);
            }
        }

        @SuppressLint("LongLogTag")
        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied " + mListenerBinder);
            release();
        }

        void release() {
            mListenerBinder.unlinkToDeath(this, 0);
            mRegisterCalculatorListener.unRegisterCalculatorSuccess();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void registerListener(IHMIListener listener) throws RemoteException {
        Log.d(TAG, " HMIService has been registed ");
        mHandlerCalculator.registerCalculatorListener(listener);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void unregisterListener(IHMIListener listener) throws RemoteException {
        Log.d(TAG, " HMIService has been unregisted ");
        mHandlerCalculator.unregisterCalculatorListener(listener);
    }

    @SuppressLint("LongLogTag")
    @Override
    public TestCapability getCapability() throws RemoteException {
        boolean isConsumptionSupported = mConfigurationClient.isSupport(IConfigurationService.CONFIG_CONSUMPTION);
        boolean isDistanceSupported = mConfigurationClient.isSupport(IConfigurationService.CONFIG_DISTANCE);
        boolean isResetSupported = mConfigurationClient.isSupport(IConfigurationService.CONFIG_RESET);
        return new TestCapability(isDistanceSupported, isConsumptionSupported, isResetSupported);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void setDistanceUnit(int unit) throws RemoteException {
        Log.d(TAG, " HMIService has been set distance unit");
        mHandlerCalculator.setDistanceUnitValue(unit);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void setConsumptionUnit(int unit) throws RemoteException {
        Log.d(TAG, " HMIService has been set consumption unit");
        mHandlerCalculator.setConsumptionUnitValue(unit);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void resetData() throws RemoteException {
        Log.d(TAG, " HMIService has been set reset data");
        mHandlerCalculator.resetDataListener();
    }

}
