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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lg.example.finaltest.hmiapplication.IHMIListener;
import lg.example.finaltest.hmiapplication.IServiceInterface;
import lg.example.finaltest.hmiapplication.TestCapability;

public class CalculatorListenerFromHMI extends IServiceInterface.Stub {

    private static final String TAG = "CalculatorListenerFromHMI";

    private HandlerThread mHandlerThread;
    private CalculatorServiceHandler mHandlerCalculator;

    private Map<IBinder,Client>  mClientMap = new ConcurrentHashMap<>();

    private TestCapability capability;
    private IChangeUnitListener changeUnitListener;

    /**
     * set capability
     * */
    public void setCapability(TestCapability capability) {
        this.capability = capability;
    }

    private class CalculatorServiceHandler extends Handler{

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
            if(msg.what == MSG_REGISTER_CALCULATOR_LISTENER){
                IHMIListener listener = (IHMIListener) msg.obj;
                if(listener==null){
                    return;
                }
                registerCalculatorBinderLocked(listener);
            }else if(msg.what == MSG_UNREGISTER_CALCULATOR_LISTENER){
                IHMIListener listener = (IHMIListener) msg.obj;
                if(listener==null){
                    return;
                }
                unregisterCalculatorBinderLocked(listener);
            }else if(msg.what == MSG_SET_DISTANCE_UNIT){
                setDistanceUnitBinderLocked((Integer)msg.obj);
            }else if(msg.what == MSG_SET_CONSUMPTION_UNIT){
                setConsumptionUnitBinderLocked((Integer)msg.obj);
            }else{
                setResetBinderLocked();
            }
        }

        @SuppressLint("LongLogTag")
        void registerCalculatorListener(IHMIListener listener){
            if(!sendMessage(obtainMessage(MSG_REGISTER_CALCULATOR_LISTENER,listener))){
                Log.e(TAG, "sendMessage fail: "+MSG_REGISTER_CALCULATOR_LISTENER);
            }
        }

        @SuppressLint("LongLogTag")
        void unregisterCalculatorListener(IHMIListener listener){
            if(!sendMessage(obtainMessage(MSG_UNREGISTER_CALCULATOR_LISTENER,listener))) {
                Log.e(TAG, "sendMessage fail: " + MSG_UNREGISTER_CALCULATOR_LISTENER);
            }
        }

        @SuppressLint("LongLogTag")
        void setDistanceUnitValue(int unit){
            if(!sendMessage(obtainMessage(MSG_SET_DISTANCE_UNIT,unit))){
                Log.e(TAG, "sendMessage fail: " + MSG_SET_DISTANCE_UNIT);
            }
        }

        @SuppressLint("LongLogTag")
        void setConsumptionUnitValue(int unit){
            if(!sendMessage(obtainMessage(MSG_SET_CONSUMPTION_UNIT,unit))){
                Log.e(TAG, "sendMessage fail: " + MSG_SET_CONSUMPTION_UNIT);
            }
        }

        @SuppressLint("LongLogTag")
        void  resetDataListener(){
            if(!sendMessage(obtainMessage(MSG_RESET_DATA))){
                Log.e(TAG, "sendMessage fail: " + MSG_RESET_DATA);
            }
        }
    }

    private void setResetBinderLocked() {

    }

    private void setConsumptionUnitBinderLocked(int unit) {
        changeUnitListener.onChangeConsumptionUnit(unit);
    }

    private void setDistanceUnitBinderLocked(int unit) {
        changeUnitListener.onChangeConsumptionUnit(unit);
    }

    private class Client implements IBinder.DeathRecipient {
        private final IHMIListener mListener;
        private final IBinder mListenerBinder;

        @SuppressLint("LongLogTag")
        Client(IHMIListener listener) {
            mListener = listener;
            mListenerBinder = listener.asBinder();
            try {
                mListenerBinder.linkToDeath(this, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "\"Failed to link death for recipient. " + e);
                throw new IllegalStateException("Client already dead", e);
            }
            mClientMap.put(mListenerBinder, this);
        }

        @SuppressLint("LongLogTag")
        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied " + mListenerBinder);
            release();
        }

        void release() {
            mListenerBinder.unlinkToDeath(this, 0);
            mClientMap.remove(mListenerBinder);
        }
    }

    private void registerCalculatorBinderLocked(IHMIListener listener) {
        IBinder iBinder = listener.asBinder();
        Client client = mClientMap.get(iBinder);
        if(client==null){
            client = new Client(listener);
        }
    }

    private void unregisterCalculatorBinderLocked(IHMIListener listener) {
        IBinder iBinder = listener.asBinder();
        Client client = mClientMap.get(iBinder);
        if(client==null){
            return;
        }
        client.release();
    }


    public CalculatorListenerFromHMI(IChangeUnitListener changeUnitListener) {
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mHandlerCalculator = new CalculatorServiceHandler(mHandlerThread.getLooper());
        this.changeUnitListener = changeUnitListener;
    }

    public List<IHMIListener> getClientHMI(){
        Set<IBinder> keyBinder = mClientMap.keySet();
        ArrayList<IHMIListener> clientHMI= new ArrayList<>();
        for (IBinder binder:keyBinder) {
            clientHMI.add(mClientMap.get(binder).mListener);
        }
        return clientHMI;
    }

    @Override
    public void registerListener(IHMIListener listener) throws RemoteException {
        mHandlerCalculator.registerCalculatorListener(listener);
    }

    @Override
    public void unregisterListener(IHMIListener listener) throws RemoteException {
        mHandlerCalculator.unregisterCalculatorListener(listener);
    }

    @Override
    public TestCapability getCapability() throws RemoteException {
        if(capability==null){
            return null;
        }
        return capability;
    }

    @Override
    public void setDistanceUnit(int unit) throws RemoteException {
        mHandlerCalculator.setDistanceUnitValue(unit);
    }

    @Override
    public void setConsumptionUnit(int unit) throws RemoteException {
        mHandlerCalculator.setConsumptionUnitValue(unit);
    }

    @Override
    public void resetData() throws RemoteException {
        mHandlerCalculator.resetDataListener();
    }
}
