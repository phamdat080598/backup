package lg.example.finaltest.hmiapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import lg.example.finaltest.configuration.ConfigurationService;
import lg.example.finaltest.property.IPropertyEventListener;
import lg.example.finaltest.property.IPropertyService;
import lg.example.finaltest.property.PropertyEvent;
import lg.example.finaltest.property.PropertyService;

public class CalculatorService extends Service {

    private static final String TAG = "Calculator Service";
    private static final String PROPERTY_PACKAGE_NAME = "dcv.package.property";
    private static final String PROPERTY_ACTION = "dvc.action.BIND";

    private boolean isConnectedProperty = false;
    private IHMIListener mHMIListener;
    private IPropertyService mPropertyService;
    private IPropertyEventListener mPropertyListener = new CalculatorListenerFromProperty();
    private CalculatorListenerFromHMI mService = new CalculatorListenerFromHMI();

    /**
     * nhận call từ HML
     */
    private class CalculatorListenerFromHMI extends IServiceInterface.Stub {


        @Override
        public void registerListener(IHMIListener listener) throws RemoteException {
            connectPropertyService(PROPERTY_PACKAGE_NAME, PROPERTY_ACTION);
            mHMIListener = listener;
        }

        @Override
        public void unregisterListener(IHMIListener listener) throws RemoteException {
            mHMIListener = null;
        }

        @Override
        public TestCapability getCapability() throws RemoteException {

            return null;
        }

        @Override
        public void setDistanceUnit(int unit) throws RemoteException {

        }

        @Override
        public void setConsumptionUnit(int unit) throws RemoteException {

        }

        @Override
        public void resetData() throws RemoteException {

        }
    }

    /**
     * nhận result from property
     */
    private class CalculatorListenerFromProperty extends IPropertyEventListener.Stub {

        @Override
        public void onEvent(PropertyEvent event) throws RemoteException {

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
                isConnectedProperty = false;
                return;
            }
            isConnectedProperty = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnectedProperty = false;
        }
    };

    /**
     * bind property service
     */
    private void connectPropertyService(String action, String packageName) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(packageName);
        bindService(intent, mConnectionProperty, Context.BIND_AUTO_CREATE);
    }

    /**
     * register với ecu được support
     */
    private void registerPropertyService() {
        if (isConnectedProperty == true) {

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
            return mService.asBinder();
        } else {
            return null;
        }
    }
}
