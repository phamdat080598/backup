package lg.example.clientoneipc;

import android.os.Parcel;
import android.os.Parcelable;

public class One implements Parcelable {

    private String name;


    public One(String name) {
        this.name = name;
    }

    public One() {
    }

    protected One(Parcel in) {
        name = in.readString();
    }

    public static final Creator<One> CREATOR = new Parcelable.Creator<One>() {
        @Override
        public One createFromParcel(Parcel in) {
            return new One(in);
        }

        @Override
        public One[] newArray(int size) {
            return new One[size];
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
