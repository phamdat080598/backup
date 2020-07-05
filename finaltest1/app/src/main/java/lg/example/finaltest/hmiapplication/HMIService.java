package lg.example.finaltest.hmiapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import dcv.finaltest.configuration.IConfigurationService;
import dcv.finaltest.hmiapplication.IHMIListener;
import dcv.finaltest.hmiapplication.IServiceInterface;
import dcv.finaltest.property.IPropertyService;
import lg.example.finaltest.configuration.ConfigurationService;
import lg.example.finaltest.property.PropertyService;


public class HMIService extends Service {
    public static String TAG = "HMIService";
    private IConfigurationService mConfigService;
    private IPropertyService mPropertyService;
    private IServiceInterface mService;
    private final IBinder mBinder = new LocalBinder();
    private HMIListener hmiListener = new HMIListener();

    private class HMIListener extends IHMIListener.Stub {


        @Override
        public void onDistanceUnitChanged(int distanceUnit) throws RemoteException {

        }

        @Override
        public void onDistanceChanged(double distance) throws RemoteException {

        }

        @Override
        public void OnConsumptionUnitChanged(int consumptionUnit) throws RemoteException {

        }

        @Override
        public void onConsumptionChanged(double[] consumptionList) throws RemoteException {

        }

        @Override
        public void onError(boolean isError) throws RemoteException {

        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IServiceInterface.Stub.asInterface(service);
            if (mService == null) {
                Log.e(TAG, "Can not connect to " + name.getPackageName());
                return;
            }

            try {
                mService.registerListener(hmiListener);
            } catch (RemoteException e) {
                Log.e(TAG, "Call registerListener failed" + e.getMessage());
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
            mService.unregisterListener(hmiListener);
            } catch (RemoteException e) {
                Log.e(TAG, "Call registerListener failed" + e.getMessage());
            }
                mService = null;
            }
    };

    public class LocalBinder extends Binder {
        HMIService getService() {
            return HMIService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mConfigService = new ConfigurationService();
        mPropertyService = new PropertyService();
    }

    public void connectStudentService(String action, String packageName) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(packageName);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnectStudentService() {
        if (mService != null) unbindService(mConnection);
    }

    public HMIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        String serviceName = intent.getStringExtra("service");
        if (serviceName.equals("config"))
            return mConfigService.asBinder();
        else if (serviceName.equals("property"))
            return mPropertyService.asBinder();
        else if (serviceName.equals("hmi"))
            return mBinder;
        else
            return null;
    }
}
