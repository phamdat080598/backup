package dcv.finaltest.hmiapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import dcv.finaltest.configuration.ConfigurationService;
import dcv.finaltest.configuration.IConfigurationService;
import dcv.finaltest.core.ICoreService;
import dcv.finaltest.property.IPropertyService;
import dcv.finaltest.property.PropertyService;

public class HMIService extends Service {
    public static String TAG = "HMIService";
    private IConfigurationService mConfigService;
    private IPropertyService mPropertyService;
    private TestCoreService mBinder = new TestCoreService();

    private class TestCoreService extends ICoreService.Stub {

        @Override
        public IPropertyService getPropertyService() throws RemoteException {
            return mPropertyService;
        }

        @Override
        public IConfigurationService getConfigurationService() throws RemoteException {
            return mConfigService;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mConfigService = new ConfigurationService();
        mPropertyService = new PropertyService();
    }

    public HMIService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
