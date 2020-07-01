package lg.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Three implements Parcelable {
    private String name;

    protected Three(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Three> CREATOR = new Parcelable.Creator<Three>() {
        @Override
        public Three createFromParcel(Parcel in) {
            return new Three(in);
        }

        @Override
        public Three[] newArray(int size) {
            return new Three[size];
        }
    };

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
}
