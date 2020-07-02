// IPropertyEventListener.aidl
package lg.example.finaltest.property;
import lg.example.finaltest.property.PropertyEvent;
// Declare any non-default types here with import statements

interface IPropertyEventListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onEvent(in PropertyEvent event);
}
