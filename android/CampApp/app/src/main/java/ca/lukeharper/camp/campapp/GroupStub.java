package ca.lukeharper.camp.campapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Luke on 2016-12-10.
 */

public class GroupStub implements Parcelable {
    protected int id;
    protected String name;

    private ArrayList memberList;

    public GroupStub(int gid, String n) {
        id = gid;
        name = n;
    }

    public GroupStub(Parcel source){
        id = source.readInt();
        name = source.readString();
    }

    public GroupStub(JSONObject gsJSON) throws JSONException{
        id = gsJSON.getInt("id");
        name = gsJSON.getString("name");
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<GroupStub> CREATOR = new Parcelable.Creator<GroupStub>() {
        @Override
        public GroupStub[] newArray(int size) {
            return new GroupStub[size];
        }

        @Override
        public GroupStub createFromParcel(Parcel source) {
            return new GroupStub(source);
        }
    };
}
