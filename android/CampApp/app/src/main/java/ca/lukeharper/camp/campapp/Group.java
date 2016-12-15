package ca.lukeharper.camp.campapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Luke on 2016-12-09.
 */

public class Group extends GroupStub implements Parcelable{
    private String description;

    private ArrayList<UserStub> memberList;

    public Group(int gid, String n, String d) {
        super(gid, n);
        description = d;
        memberList = new ArrayList<UserStub>();
    }

    public Group(Parcel source){
        super(source);
        description = source.readString();
        memberList = new ArrayList<UserStub>();
        memberList = source.readArrayList(UserStub.class.getClassLoader());

    }
    public Group(JSONObject groupJSON) throws JSONException {
        super(groupJSON);
        description = groupJSON.getString("description");
        memberList = new ArrayList<UserStub>();
    }

    public ArrayList<UserStub> getMembers() {
        return memberList;
    }

    public void addMember(UserStub userStub){
        memberList.add(userStub);
    }

    public void addMember(JSONArray uList) throws JSONException{
        if(uList != null){
            for(int i=0; i<uList.length(); i++){
                memberList.add(new UserStub(uList.getJSONObject(i)));
            }
        }
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(description);
        dest.writeList(memberList);
    }

    public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }

        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }
    };
}
