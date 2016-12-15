package ca.lukeharper.camp.campapp;

import android.os.Parcelable;
import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Luke on 2016-12-09.
 */

public class User extends UserStub implements Parcelable {
    private UserType type;

    private ArrayList<GroupStub> groupList;

    public User(int uid, String fn, String ln, String ut) {
        super(uid, fn, ln);
        type = UserType.valueOf(ut);
        groupList = new ArrayList<GroupStub>();
    }

    public User(Parcel source){
        super(source);
        type = UserType.valueOf(source.readString());
        groupList = source.readArrayList(GroupStub.class.getClassLoader());
    }

    public User(JSONObject userJSON) throws JSONException{
        super(userJSON);
        type = UserType.valueOf(userJSON.getString("role"));
        groupList = new ArrayList<GroupStub>();
    }

    public ArrayList<GroupStub> getGroups(){
        return groupList;
    }

    public void addGroup(GroupStub groupStub){
        groupList.add(groupStub);
    }
    public void addGroup(JSONArray gList) throws JSONException{
        if(gList != null){
            for(int i=0; i<gList.length(); i++){
                groupList.add(new GroupStub(gList.getJSONObject(i)));
            }
        }
    }

    public UserType getType(){
        return type;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(type.name());
        dest.writeList(groupList);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
    };


}
