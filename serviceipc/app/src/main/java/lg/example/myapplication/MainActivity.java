package lg.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ACTION = "server";
    private IRemoteObjectTwo mService;
    private boolean isConnected =false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IRemoteObjectTwo.Stub.asInterface(iBinder);
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isConnected=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    protected void onDestroy() {
        super.onDestroy();
        if(isConnected){
            unbindService(connection);
            isConnected=false;
        }
    }

    private void bindService(){
        Intent intent = new Intent(this,MyService.class);
        intent.putExtra(MyService.KEY_SERVER,CLIENT_ACTION);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }

    private void initView(){
        findViewById(R.id.btnConnection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService();
            }
        });
    }
}