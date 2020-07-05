package lg.example.finaltest.framework;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import dcv.finaltest.configuration.IConfigurationService;
import dcv.finaltest.core.ICoreService;
import dcv.finaltest.property.IPropertyEventListener;
import dcv.finaltest.property.IPropertyService;


public class CalculatorService extends Service {

    private static final String TAG = "Calculator Service";
    private static final String CORE_PACKAGE_NAME = "dcv.finaltest.hmiapplication";
    private static final String CORE_ACTION = "dcv.finaltest.BIND";

    private ICoreService mCoreService;
    private IPropertyService mPropertyClient;
    private IConfigurationService mConfigurationClient;
    private IPropertyEventListener mPropertyListener;
    private CalculatorListenerFromHMI mService;

    private ServiceConnection mConnectionCoreService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mCoreService = ICoreService.Stub.asInterface(iBinder);
            if (mCoreService == null) {
                Log.e(TAG, "Can not connect to " + componentName.getPackageName());
                return;
            }
            try {
                mPropertyClient = mCoreService.getPropertyService();
                mConfigurationClient = mCoreService.getConfigurationService();
                registerProperty();
                mService.setConfigurationClient(mConfigurationClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            try {
                mPropertyClient.unregisterListener(IPropertyService.PROP_DISTANCE_UNIT, mPropertyListener);
                mPropertyClient.unregisterListener(IPropertyService.PROP_DISTANCE_VALUE, mPropertyListener);
                mPropertyClient.unregisterListener(IPropertyService.PROP_CONSUMPTION_UNIT, mPropertyListener);
                mPropertyClient.unregisterListener(IPropertyService.PROP_CONSUMPTION_VALUE, mPropertyListener);
                mPropertyClient.unregisterListener(IPropertyService.PROP_RESET, mPropertyListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }finally {
                mConfigurationClient = null;
                mPropertyClient = null;
                mService.setPropertyService(null);
                mService.setConfigurationClient(null);
            }
        }
    };

    private void registerProperty() {
        mService.setPropertyService(mPropertyClient);
        try {
            mPropertyClient.registerListener(IPropertyService.PROP_DISTANCE_UNIT, mPropertyListener);
            mPropertyClient.registerListener(IPropertyService.PROP_DISTANCE_VALUE, mPropertyListener);
            mPropertyClient.registerListener(IPropertyService.PROP_CONSUMPTION_UNIT, mPropertyListener);
            mPropertyClient.registerListener(IPropertyService.PROP_CONSUMPTION_VALUE, mPropertyListener);
            mPropertyClient.registerListener(IPropertyService.PROP_RESET, mPropertyListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * bind property service
     */
    private void connectServiceOther(String action, String packageName) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(packageName);
        bindService(intent, mConnectionCoreService, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        connectServiceOther(CORE_ACTION, CORE_PACKAGE_NAME);
        mPropertyListener = new CalculatorListenerFromProperty();
        mService = new CalculatorListenerFromHMI((RegisterCalculatorServiceListener) mPropertyListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mService.asBinder();
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }
}
