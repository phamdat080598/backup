package dcv.finaltest.property;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PropertyService extends IPropertyService.Stub {
    private static String TAG = "PropertyService";
    private HandlerThread mHandlerThread;
    private PropertyServiceHandler mEventHandler;
    private final Map<IBinder, Client> mClientMap = new ConcurrentHashMap<>();
    private final Map<Integer, List<Client>> mPropIdClientMap = new ConcurrentHashMap<>();
    private Map<Integer, PropertyEvent> map = new HashMap<>();
    private Timer timer;
    private TimerTask timerTask;

    private static class PropertyListenerMessage {
        private int mPropId;
        private IPropertyEventListener mListener;

        PropertyListenerMessage(int propId, IPropertyEventListener listener) {
            mPropId = propId;
            mListener = listener;
        }
    };

    private class PropertyServiceHandler extends Handler {
        private static final int MSG_REGISTER_PROPERTY_LISTENER = 1;
        private static final int MSG_UNREGISTER_PROPERTY_LISTENER = 2;
        private static final int MSG_SET_PROPERTY_VALUE= 3;
        private static final int MSG_SEND_PROPERTY_VALUE= 4;

        PropertyServiceHandler(Looper looper) {
            super(looper);
        }

        public void registerPropertyListener(PropertyListenerMessage msg) {
            if (!sendMessage(obtainMessage(MSG_REGISTER_PROPERTY_LISTENER, msg))) {
                Log.e(TAG, "sendMessage Failed: MSG_REGISTER_PROPERTY_LISTENER");
            }
        }

        public void unregisterPropertyListener(PropertyListenerMessage msg) {
            if (!sendMessage(obtainMessage(MSG_UNREGISTER_PROPERTY_LISTENER, msg))) {
                Log.e(TAG, "sendMessage Failed: MSG_UNREGISTER_PROPERTY_LISTENER");
            }
        }

        public void setPropertyValue(PropertyEvent msg) {
            if (!sendMessage(obtainMessage(MSG_SET_PROPERTY_VALUE, msg))) {
                Log.e(TAG, "sendMessage Failed: MSG_SET_PROPERTY_VALUE");
            }
        }

        public void sendPropertyValue(int msg) {
            if (!sendMessage(obtainMessage(MSG_SEND_PROPERTY_VALUE, msg))) {
                Log.e(TAG, "sendMessage Failed: MSG_SET_PROPERTY_VALUE");
            }
        }

        public void handleMessage(Message msg) {
            if (msg.what == MSG_REGISTER_PROPERTY_LISTENER) {
                PropertyListenerMessage message = (PropertyListenerMessage) msg.obj;
                if (message == null)
                    return;

                int propId = message.mPropId;
                IPropertyEventListener listener = message.mListener;
                registerListenerBinderLocked(propId, listener);
            } else if (msg.what == MSG_UNREGISTER_PROPERTY_LISTENER) {
                PropertyListenerMessage message = (PropertyListenerMessage) msg.obj;
                if (message == null)
                    return;

                int propId = message.mPropId;
                IPropertyEventListener listener = message.mListener;
                unregisterListenerBinderLocked(propId, listener.asBinder());
            } else if (msg.what == MSG_SET_PROPERTY_VALUE) {
                PropertyEvent message = (PropertyEvent) msg.obj;
                if (message == null)
                    return;

                setPropertyBinderLocked(message);
            } else if (msg.what == MSG_SEND_PROPERTY_VALUE) {
                int message = (int) msg.obj;
                sendPropertyBinderLocked(message);
            }
        }
    }

    private void sendPropertyBinderLocked(int propId) {
        PropertyEvent event = map.get(propId);
        if (event == null) {
            Log.e(TAG, "Does not exist property ID = " + propId);
        } else {
            List<Client> ls = mPropIdClientMap.get(propId);
            if (ls == null) {
                Log.e(TAG, "Does not exist register list of ID = " + propId);
            } else {
                for (Client client: ls) {
                    PropertyEvent newEvent = new PropertyEvent(event.getPropertyId(), event.getStatus(), event.getTimestamp(), event.getValue());
                    try {
                        client.mListener.onEvent(newEvent);
                    }catch (RemoteException ex) {
                        Log.e(TAG, "onEvent calling failed: " + ex);
                    }
                }
            }
        }

    }

    private void setPropertyBinderLocked(PropertyEvent message) {
        int propId = message.getPropertyId();
        PropertyEvent event = map.get(propId);
        if (event == null) {
            Log.e(TAG, "Does not exist property ID = " + propId);
        } else {
            if ((propId != PROP_CONSUMPTION_VALUE) && (event.getValue() == message.getValue()) && (event.getStatus() == event.getStatus()) ){
                Log.d(TAG, "Same value with propId = " + propId);
                return;
            }
            PropertyEvent newEvent = new PropertyEvent(event.getPropertyId(), message.getStatus(), event.getTimestamp(), message.getValue());
            map.put(propId, newEvent);
            sendPropertyBinderLocked(propId);

            if (propId == IPropertyService.PROP_RESET) {
                PropertyEvent distanceUnit = map.get(IPropertyService.PROP_DISTANCE_UNIT);
                newEvent = new PropertyEvent(distanceUnit.getPropertyId(), distanceUnit.getStatus(), distanceUnit.getTimestamp(), 0);
                map.put(PROP_DISTANCE_UNIT, newEvent);
                sendPropertyBinderLocked(PROP_DISTANCE_UNIT);

                PropertyEvent distanceValue = map.get(IPropertyService.PROP_DISTANCE_VALUE);
                newEvent = new PropertyEvent(distanceValue.getPropertyId(), distanceValue.getStatus(), distanceValue.getTimestamp(), 0.0);
                map.put(PROP_DISTANCE_VALUE, newEvent);
                sendPropertyBinderLocked(PROP_DISTANCE_VALUE);

                PropertyEvent consumptionUnit = map.get(IPropertyService.PROP_CONSUMPTION_UNIT);
                newEvent = new PropertyEvent(consumptionUnit.getPropertyId(), consumptionUnit.getStatus(), consumptionUnit.getTimestamp(), 0);
                map.put(PROP_CONSUMPTION_UNIT, newEvent);
                sendPropertyBinderLocked(PROP_CONSUMPTION_UNIT);

                PropertyEvent resetEvent = map.get(IPropertyService.PROP_RESET);
                newEvent = new PropertyEvent(resetEvent.getPropertyId(), resetEvent.getStatus(), resetEvent.getTimestamp(), false);
                map.put(PROP_RESET, newEvent);
                sendPropertyBinderLocked(PROP_RESET);
            } else if (event.getValue() != message.getValue()) {
                if (propId == IPropertyService.PROP_DISTANCE_UNIT)  {
                    double rate = ((int)event.getValue() == 0) ?  0.621371 : 1.60934;
                    PropertyEvent distanceValue = map.get(IPropertyService.PROP_DISTANCE_VALUE);
                    newEvent = new PropertyEvent(distanceValue.getPropertyId(), distanceValue.getStatus(), distanceValue.getTimestamp(), (double)distanceValue.getValue() * rate);
                    map.put(PROP_DISTANCE_VALUE, newEvent);
                    sendPropertyBinderLocked(IPropertyService.PROP_DISTANCE_VALUE);
                }
            }

        }
    }

    private void unregisterListenerBinderLocked(int propId, IBinder listenerBinder) {
        Client client = mClientMap.get(listenerBinder);
        List<Client> propertyClients = mPropIdClientMap.get(propId);
        if ((client == null) || (propertyClients == null)) {
            Log.e(TAG, "unregisterListenerBinderLocked: Listener was not previously registered.");
        } else {
            propertyClients.remove(client);
            client.release();
        }
    }

    private void registerListenerBinderLocked(int propId, IPropertyEventListener listener) {
        IBinder listenerBinder = listener.asBinder();
        Client client = mClientMap.get(listenerBinder);
        if (client == null) {
            client = new Client(listener, propId);
        }
        List<Client> clients = mPropIdClientMap.get(propId);
        if (clients == null) {
            clients = new CopyOnWriteArrayList<Client>();
            mPropIdClientMap.put(propId, clients);
        }
        if (!clients.contains(client)) {
            clients.add(client);
        }

        PropertyEvent event = map.get(propId);
        if (event != null) {
            PropertyEvent newEvent = new PropertyEvent(event.getPropertyId(), event.getStatus(), event.getTimestamp(), event.getValue());
            try {
                listener.onEvent(newEvent);
            }catch (RemoteException ex) {
                Log.e(TAG, "onEvent calling failed: " + ex);
            }

        }
    }

    private class Client implements IBinder.DeathRecipient {
        private final IPropertyEventListener mListener;
        private final IBinder mListenerBinder;
        private final int mPropId;

        Client(IPropertyEventListener listener, int propId) {
            mPropId = propId;
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

        @Override
        public void binderDied() {
            Log.d(TAG, "binderDied " + mListenerBinder);
            release();
        }

        void release() {
            mListenerBinder.unlinkToDeath(this, 0);
            mPropIdClientMap.get(mPropId).remove(this);
            mClientMap.remove(mListenerBinder);
        }
    }

    public PropertyService() {
        map.put(PROP_CONSUMPTION_UNIT, new PropertyEvent(PROP_CONSUMPTION_UNIT, PropertyEvent.STATUS_AVAILABLE, 0, 0));
        map.put(PROP_CONSUMPTION_VALUE, new PropertyEvent(PROP_CONSUMPTION_VALUE, PropertyEvent.STATUS_AVAILABLE, 0, 8.5));
        map.put(PROP_DISTANCE_UNIT, new PropertyEvent(PROP_DISTANCE_UNIT, PropertyEvent.STATUS_AVAILABLE, 0, 0));
        map.put(PROP_DISTANCE_VALUE, new PropertyEvent(PROP_DISTANCE_VALUE, PropertyEvent.STATUS_AVAILABLE, 0, 0.0));
        map.put(PROP_RESET, new PropertyEvent(PROP_RESET, PropertyEvent.STATUS_AVAILABLE, 0, false));

        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mEventHandler = new PropertyServiceHandler(mHandlerThread.getLooper());

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                PropertyEvent event = map.get(PROP_CONSUMPTION_VALUE);
                Random ran = new Random();
                double newValue = ((double) event.getValue());
                double al = ran.nextDouble();
                if (newValue < 4.0) {
                    newValue += al;
                } else if (newValue > 20.0) {
                    newValue -= al;
                } else {
                    newValue += (ran.nextInt(2) == 0 ) ? al : (al*(-1.0));
                }

                PropertyEvent consumptionUnit = map.get(PROP_CONSUMPTION_UNIT);
                if ((int)consumptionUnit.getValue() == 0) {
                    newValue = 100.0 / newValue;
                }
                PropertyEvent newEvent = new PropertyEvent(PROP_CONSUMPTION_VALUE, event.getStatus(), event.getTimestamp(), newValue);
                mEventHandler.setPropertyValue(newEvent);

                PropertyEvent distance = map.get(PROP_DISTANCE_VALUE);
                newValue = (double) distance.getValue() + (ran.nextDouble() / 100.0);
                PropertyEvent newDistance = new PropertyEvent(PROP_DISTANCE_VALUE, distance.getStatus(), distance.getTimestamp(), newValue);
                mEventHandler.setPropertyValue(newDistance);
            }
        };
        timer.schedule(timerTask, 500, 500);

    }

    @Override
    public void registerListener(int propID, IPropertyEventListener callback) throws RemoteException {
        Log.d(TAG, "PropID " + propID + " has been registered");
        mEventHandler.registerPropertyListener(new PropertyListenerMessage(propID, callback));
    }

    @Override
    public void unregisterListener(int propID, IPropertyEventListener callback) throws RemoteException {
        Log.d(TAG, "PropID " + propID + " has been unregistered");
        mEventHandler.unregisterPropertyListener(new PropertyListenerMessage(propID,  callback));
    }

    @Override
    public void setProperty(int prodID, PropertyEvent value) throws RemoteException {
        Log.d(TAG, "PropID " + prodID + " has been set");
        mEventHandler.setPropertyValue(value);
    }

}
