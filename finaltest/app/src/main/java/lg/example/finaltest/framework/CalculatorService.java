package lg.example.finaltest.framework;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import lg.example.finaltest.configuration.IConfigurationService;
import lg.example.finaltest.hmiapplication.IHMIListener;
import lg.example.finaltest.hmiapplication.TestCapability;
import lg.example.finaltest.property.IPropertyEventListener;
import lg.example.finaltest.property.IPropertyService;
import lg.example.finaltest.property.PropertyEvent;

public class CalculatorService extends Service {

    private static final String TAG = "Calculator Service";
    private static final String PROPERTY_PACKAGE_NAME = "dcv.package.property";
    private static final String PROPERTY_ACTION = "dvc.action.BIND";
    private static final String CONFIGURATION_PACKAGE_NAME = "dcv.package.configuration";
    private static final String CONFIGURATION_ACTION = "dvc.action.BIND";

    private static final int TYPE_PROPERTY_SERVICE = 1;
    private static final int TYPE_CONFIGURATION_SERVICE = 2;

    private IPropertyService mPropertyService;
    private IConfigurationService mConfigurationService;

    private IPropertyEventListener mPropertyListener = new CalculatorListenerFromProperty();
    private CalculatorListenerFromHMI mService = new CalculatorListenerFromHMI((IChangeUnitListener) mPropertyListener);


    /**
     * nhận result from property
     */
    private class CalculatorListenerFromProperty extends IPropertyEventListener.Stub implements IChangeUnitListener {
        private static final int SUM_ONE_MINUTE = 120;
        private static final int MAX_SIZE = 15;
        private static final int MESSAGE = 1;

        private double mSumConsumption = 0;
        private int mCount = 0;
        private LinkedList<Double> mListConsumption = new LinkedList<>();
        private double[] mArrayConsumption = new double[MAX_SIZE];

        private boolean isDistanceValueAvailable=false;
        private boolean isDistanceUnitAvailable=false;
        private boolean isConsumptionValueAvailable=false;
        private boolean isConsumptionUnitAvailable=false;
        private boolean isResetAvailable=false;

        private int mConsumptionUnit=1;
        private int mDistanceUnit=2;

        private CalculatorServiceHandler mHandlerService;
        private HandlerThread mHandlerThread;

        public CalculatorListenerFromProperty() {
            mHandlerThread = new HandlerThread(TAG);
            mHandlerThread.start();
            mHandlerService = new CalculatorServiceHandler(mHandlerThread.getLooper());
        }

        @Override
        public void onChangeConsumptionUnit(int unit) {
            mConsumptionUnit=unit;
        }

        @Override
        public void onChangeDistanceUnit(int unit) {
            mDistanceUnit=unit;
        }

        private class CalculatorServiceHandler extends Handler {
            public CalculatorServiceHandler(@NonNull Looper looper) {
                super(looper);
            }

            @Override
            public void handleMessage(@NonNull Message msg) {
                PropertyEvent event = (PropertyEvent) msg.obj;
                int what = event.getPropertyId();

                if (what == IPropertyService.PROP_DISTANCE_VALUE) {
                    handleDistanceValue((Double) msg.obj,event.getStatus());
                } else if (what == IPropertyService.PROP_DISTANCE_UNIT) {
                    handleDistanceUnit((Integer) msg.obj,event.getStatus());
                } else if (what == IPropertyService.PROP_CONSUMPTION_VALUE) {
                    handleConsumptionValue((Double) msg.obj,event.getStatus());
                } else if (what == IPropertyService.PROP_CONSUMPTION_UNIT) {
                    handleConsumptionUnit((Integer) msg.obj,event.getStatus());
                } else if (what == IPropertyService.PROP_RESET) {
                    handleReset((Boolean) msg.obj,event.getStatus());
                } else {
                    Log.e(TAG, "not \"what\" handle message");
                }

            }

            void sendMessageProperty(PropertyEvent event) {
                if (sendMessage(obtainMessage(MESSAGE, event))) {
                    Log.d(TAG, "sendMessage fail: " + MESSAGE);
                }
            }
        }

        private void handleReset(boolean value, int status) {
            if(status==PropertyEvent.STATUS_AVAILABLE){
                isResetAvailable=true;
            }else{
                isResetAvailable=false;
            }
            for (final IHMIListener listener :
                    mService.getClientHMI()) {
                try {
                    listener.onError(value);
                    if(value) {
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                if (isConsumptionUnitAvailable && isConsumptionValueAvailable && isDistanceUnitAvailable && isDistanceValueAvailable && isResetAvailable) {
                                    try {
                                        listener.onError(false);
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
        }

        private void handleConsumptionUnit(int status, int value) {
            if(status==PropertyEvent.STATUS_AVAILABLE){
                isConsumptionUnitAvailable=true;
            }else{
                isConsumptionUnitAvailable=false;
            }
            for (IHMIListener listener :
                    mService.getClientHMI()) {
                try {
                    listener.onDistanceUnitChanged(value);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleConsumptionValue(double value, int status) {
            if(status==PropertyEvent.STATUS_AVAILABLE){
                isConsumptionValueAvailable=true;
            }else{
                isConsumptionValueAvailable=false;
            }
            while (mCount < SUM_ONE_MINUTE) {
                mSumConsumption += (Double) value;
                mCount++;
            }
            if (mCount == SUM_ONE_MINUTE) {
                mCount = 0;
                mListConsumption.add(mSumConsumption);
                mSumConsumption = 0;
                for (IHMIListener listener :
                        mService.getClientHMI()) {
                    convertListToArray();
                    try {
                        listener.onConsumptionChanged(mArrayConsumption);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (mListConsumption.size() == MAX_SIZE) {
                mListConsumption.remove(0);
            }
        }

        private void handleDistanceUnit(int value, int status) {
            if(status==PropertyEvent.STATUS_AVAILABLE){
                isDistanceUnitAvailable=true;
            }else{
                isDistanceUnitAvailable=false;
            }
            for (IHMIListener listener :
                    mService.getClientHMI()) {
                try {
                    listener.onDistanceUnitChanged(value);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleDistanceValue(double value, int status) {
            if(status==PropertyEvent.STATUS_AVAILABLE){
                isDistanceValueAvailable=true;
            }else{
                isDistanceValueAvailable=false;
            }
            for (IHMIListener listener :
                    mService.getClientHMI()) {
                try {
                    listener.onDistanceChanged(value);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onEvent(PropertyEvent event) throws RemoteException {
            if (mService.getClientHMI().isEmpty()) {
                Log.e(TAG, "not register hmi");
                return;
            }
            mHandlerService.sendMessageProperty(event);
        }

        private void convertListToArray() {
            int size = mListConsumption.size();
            for (int i = 0; i < size; i++) {
                mArrayConsumption[i] = mListConsumption.get(i);
            }
        }
    }


    /**
     * tao connection property
     */
    private ServiceConnection mConnectionProperty = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mPropertyService = IPropertyService.Stub.asInterface(iBinder);
            if (mPropertyService == null) {
                Log.e(TAG, "Can not connect to " + componentName.getPackageName());
                return;
            }

            try {
                mPropertyService.registerListener(IPropertyService.PROP_DISTANCE_UNIT, mPropertyListener);
                mPropertyService.registerListener(IPropertyService.PROP_DISTANCE_VALUE, mPropertyListener);
                mPropertyService.registerListener(IPropertyService.PROP_CONSUMPTION_UNIT, mPropertyListener);
                mPropertyService.registerListener(IPropertyService.PROP_CONSUMPTION_VALUE, mPropertyListener);
                mPropertyService.registerListener(IPropertyService.PROP_RESET, mPropertyListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                mPropertyService.unregisterListener(IPropertyService.PROP_DISTANCE_UNIT, mPropertyListener);
                mPropertyService.unregisterListener(IPropertyService.PROP_DISTANCE_VALUE, mPropertyListener);
                mPropertyService.unregisterListener(IPropertyService.PROP_CONSUMPTION_UNIT, mPropertyListener);
                mPropertyService.unregisterListener(IPropertyService.PROP_CONSUMPTION_VALUE, mPropertyListener);
                mPropertyService.unregisterListener(IPropertyService.PROP_RESET, mPropertyListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mPropertyService=null;
        }
    };

    private ServiceConnection mConnectionConfiguration = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mConfigurationService = IConfigurationService.Stub.asInterface(iBinder);
            if (mConfigurationService == null) {
                Log.e(TAG, "Can not connect to " + componentName.getPackageName());
                return;
            }
            try {
                boolean isConsumptionSupported = mConfigurationService.isSupport(IConfigurationService.CONFIG_CONSUMPTION);
                boolean isDistanceSupported = mConfigurationService.isSupport(IConfigurationService.CONFIG_DISTANCE);
                boolean isResetSupported = mConfigurationService.isSupport(IConfigurationService.CONFIG_RESET);
                mService.setCapability(new TestCapability(isDistanceSupported, isConsumptionSupported, isResetSupported));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mConfigurationService=null;
        }
    };

    /**
     * bind property service
     */
    private void connectServiceOther(int typeService, String action, String packageName) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(packageName);
        if (typeService == TYPE_PROPERTY_SERVICE) {
            bindService(intent, mConnectionProperty, Context.BIND_AUTO_CREATE);
        } else if (typeService == TYPE_CONFIGURATION_SERVICE) {
            bindService(intent, mConnectionConfiguration, Context.BIND_AUTO_CREATE);
        } else {
            Log.e(TAG, "not type service");
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String serviceName = intent.getStringExtra("service");
        if (serviceName.equals("hmi")) {
            connectServiceOther(TYPE_PROPERTY_SERVICE, PROPERTY_ACTION, PROPERTY_PACKAGE_NAME);
            connectServiceOther(TYPE_CONFIGURATION_SERVICE, CONFIGURATION_ACTION, CONFIGURATION_PACKAGE_NAME);
            return mService.asBinder();
        } else {
            return null;
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        try {
            mPropertyService.unregisterListener(IPropertyService.PROP_DISTANCE_UNIT, mPropertyListener);
            mPropertyService.unregisterListener(IPropertyService.PROP_DISTANCE_VALUE, mPropertyListener);
            mPropertyService.unregisterListener(IPropertyService.PROP_CONSUMPTION_UNIT, mPropertyListener);
            mPropertyService.unregisterListener(IPropertyService.PROP_CONSUMPTION_VALUE, mPropertyListener);
            mPropertyService.unregisterListener(IPropertyService.PROP_RESET, mPropertyListener);
            mConfigurationService=null;
            mPropertyListener=null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
