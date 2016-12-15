package ca.lukeharper.camp.campapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luke on 2016-12-10.
 */

public class UserStub implements Parcelable {
    protected int id;
    protected String firstName;
    protected String lastName;
    public UserStub (int uid, String fn, String ln) {
        id = uid;
        firstName = fn;
        lastName = ln;
    }

    public UserStub(Parcel source){
        id = source.readInt();
        firstName = source.readString();
        lastName = source.readString();
    }

    public UserStub(JSONObject usJSON) throws JSONException{
        id = usJSON.getInt("id");
        firstName = usJSON.getString("first_name");
        lastName = usJSON.getString("last_name");
    }

    public int getID() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }

    public static final Creator<UserStub> CREATOR = new Creator<UserStub>() {
        @Override
        public UserStub[] newArray(int size) {
            return new UserStub[size];
        }

        @Override
        public UserStub createFromParcel(Parcel source) {
            return new UserStub(source);
        }
    };
}
