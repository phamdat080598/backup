// IPropertyService.aidl
package lg.example.finaltest.property;

import lg.example.finaltest.property.IPropertyEventListener;

interface IPropertyService {

    const int PROP_DISTANCE_UNIT = 1;
    const int PROP_DISTANCE_VALUE = 2;
    const int PROP_CONSUMPTION_UNIT = 3;
    const int PROP_CONSUMPTION_VALUE = 4;
    const int PROP_RESET = 5;

    void registerListener(int propID, in IPropertyEventListener callback);
    void unregisterListener(int propID, in IPropertyEventListener callback);
    void setProperty(int prodID, int value);
}
