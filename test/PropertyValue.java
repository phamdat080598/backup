package test;
public class PropertyValue<T> {
    private final int mPropertyId;
    private final int mStatus;
    private final long mTimestamp;
    private final T mValue;
    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_UNAVAILABLE = 1;

    public PropertyValue(int propertyId, int status,long timestamp,  T value) {
        mPropertyId = propertyId;
        mStatus = status;
        mTimestamp = timestamp;
        mValue = value;
    }

    public int getmStatus() {
        return mStatus;
    }

    public T getmValue() {
        return mValue;
    }
}
