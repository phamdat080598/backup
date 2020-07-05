package lg.example.finaltest.property;
import android.annotation.SuppressLint;
import android.os.RemoteException;
import android.util.ArraySet;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dcv.finaltest.property.IPropertyEventListener;
import dcv.finaltest.property.IPropertyService;

public class PropertyService extends IPropertyService.Stub {
    private static String TAG = "PropertyService";
    private Map<Integer, ArraySet<IPropertyEventListener>> map = new HashMap<>();
    private Timer timer;
    private TimerTask timerTask;
    private int consumptionUnit = 1;
    private double consumptionValue = 1.0;
    private int distanceUnit = 1;
    private int distanceValue = 1;
    private int reset = 1;

    @SuppressLint("NewApi")
    public PropertyService() {

        map.put(PROP_CONSUMPTION_UNIT, new ArraySet<IPropertyEventListener>());
        map.put(PROP_CONSUMPTION_VALUE, new ArraySet<IPropertyEventListener>());
        map.put(PROP_DISTANCE_UNIT, new ArraySet<IPropertyEventListener>());
        map.put(PROP_DISTANCE_VALUE, new ArraySet<IPropertyEventListener>());
        map.put(PROP_RESET, new ArraySet<IPropertyEventListener>());


    }

    @Override
    public void registerListener(int propID, IPropertyEventListener callback) throws RemoteException {


    }

    @Override
    public void unregisterListener(int propID, IPropertyEventListener callback) throws RemoteException {

    }

    @Override
    public void setProperty(int prodID, PropertyEvent value) throws RemoteException {

    }


}
