package lg.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Two implements Parcelable {
    private String name;

    protected Two(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Two> CREATOR = new Parcelable.Creator<Two>() {
        @Override
        public Two createFromParcel(Parcel in) {
            return new Two(in);
        }

        @Override
        public Two[] newArray(int size) {
            return new Two[size];
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
