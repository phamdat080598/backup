// IConfigurationService.aidl
package lg.example.finaltest.configuration;

// Declare any non-default types here with import statements

interface IConfigurationService {

    const int CONFIG_DISTANCE = 1;
    const int CONFIG_CONSUMPTION = 2;
    const int CONFIG_RESET = 3;

    boolean isSupport(int configID);
}
