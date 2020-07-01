package lg.example.clientoneipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import lg.example.myapplication.IRemoteObjectOne;


public class MainActivity extends AppCompatActivity {

    private static final String SERVER_ACTION = "lg.example.myapplication.MyService.BIND";
    private static final String SERVER_PACKAGE = "lg.example.myapplication";
    private static final String CLIENT_ACTION ="client-one";
    private static final String KEY_SERVER = "key";
    private static final String TAG ="client-one";

    private IRemoteObjectOne mService;
    private boolean isConnected = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected starting");
            mService = IRemoteObjectOne.Stub.asInterface(iBinder);
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService();

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isConnected){
            unbindService(mServiceConnection);
            isConnected=false;
        }
    }

    private void bindService(){
        Intent intent = new Intent();
        intent.setAction(SERVER_ACTION);
        intent.setPackage(SERVER_PACKAGE);
        intent.putExtra(KEY_SERVER,CLIENT_ACTION);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initView(){
        findViewById(R.id.btnConnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mService.run(new One("Client One !!"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}