package dcv.finaltest.hmiapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import dcv.finaltest.configuration.IConfigurationService;
import dcv.finaltest.core.ICoreService;
import dcv.finaltest.property.IPropertyService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TestMainActivity";
    private ICoreService mCoreService;
    private IConfigurationService mConfigurationService;
    private IPropertyService mPropertyService;
    private ServiceConnection mCoreServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected connect to Core service");
            mCoreService = ICoreService.Stub.asInterface(service);
            if (mCoreService != null) {
                try {
                    mPropertyService = mCoreService.getPropertyService();
                    mConfigurationService = mCoreService.getConfigurationService();
                    Log.d(TAG, "onServiceConnected connect to Core service done.");
                } catch (RemoteException e) {
                    Log.e(TAG, "onServiceConnected connect to Core service fail." + e.getMessage());
                }

            } else {
                Log.e(TAG, "onServiceConnected connect to Core service fail.");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPropertyService = null;
            mConfigurationService = null;
            mCoreService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setAction("dcv.finaltest.BIND");
        intent.setPackage("dcv.finaltest.hmiapplication");
        if (!bindService(intent, mCoreServiceConnection, Context.BIND_AUTO_CREATE)) {
            Log.e(TAG, "bindService connect to Core service fail.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mCoreServiceConnection);
        if (mStudentService != null) {
            unbindService(mConnection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public interface IActivityCallback {
        void onOnConnected(boolean isSuccess);
        void onDisconnected();
    }
    IActivityCallback mCallback;
    IServiceInterface mStudentService;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStudentService = IServiceInterface.Stub.asInterface(service);
            if (mStudentService != null) {
                if (mCallback != null) {
                    mCallback.onOnConnected(true);
                }
            } else {
                if (mCallback != null) {
                    mCallback.onOnConnected(false);
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mStudentService = null;
            if (mCallback != null) {
                mCallback.onDisconnected();
                mCallback = null;
            }
        }
    };

    public void connectToStudentService(String action, String pakage, IActivityCallback callback ) {
        mCallback = callback;
        Intent intent = new Intent();
        intent.setAction(action);
        intent.setPackage(pakage);
        if (!bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
            mCallback.onOnConnected(false);
        }
    }

    public IServiceInterface getStudentService() {
        return mStudentService;
    }

    public IConfigurationService getConfigurationService() { return mConfigurationService;}

    public IPropertyService getPropertyService() { return mPropertyService;}

}