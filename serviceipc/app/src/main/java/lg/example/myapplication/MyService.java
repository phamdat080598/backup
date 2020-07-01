package lg.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {

    private static final String ACTION_SERVER ="server";
    private static final String ACTION_CLIENT_ONE ="client-one";
    private static final String LOG = "LOG-SERVER";
    public static final String KEY_SERVER="key";

    private IRemoteObjectOne.Stub mBinderOne = new IRemoteObjectOne.Stub() {

        @Override
        public void run(One one) throws RemoteException {
            Log.d(LOG,"server : "+one.getName());
        }
    };

    private IRemoteObjectTwo.Stub mBinderTwo = new IRemoteObjectTwo.Stub() {
        @Override
        public void fly(Two two) throws RemoteException {

        }
    };

    private IRemoteObjectThree.Stub mBinderThree = new IRemoteObjectThree.Stub() {
        @Override
        public void jump(Three three) throws RemoteException {

        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        switch (intent.getStringExtra(KEY_SERVER)){
            case ACTION_SERVER:
                Log.d(LOG,"hello server connection!!!");
                return mBinderTwo;
            case ACTION_CLIENT_ONE:
                Log.d(LOG,"hello client one connection!!!");
                return mBinderOne;
            default:
                return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG,"onDestroy");
    }
}
