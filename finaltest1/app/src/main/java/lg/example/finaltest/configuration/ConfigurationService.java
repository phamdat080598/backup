package lg.example.finaltest.configuration;

import android.os.RemoteException;

import dcv.finaltest.configuration.IConfigurationService;

public class ConfigurationService extends IConfigurationService.Stub{

    @Override
    public boolean isSupport(int configID) throws RemoteException {
        if (configID == CONFIG_CONSUMPTION) return true;
        if (configID == CONFIG_DISTANCE) return true;
        if (configID == CONFIG_RESET) return true;
        return false;
    }
}
