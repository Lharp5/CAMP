package ca.lukeharper.camp.campapp;

import java.util.ArrayList;

/**
 * Created by Luke on 2016-12-11.
 */

public class CampAuthenticatedUser{
    private static CampAuthenticatedUser instance;
    private String username;
    private String password;

    private User self;

    private String token;

    private CampAuthenticatedUser(){
        username = "";
        password = "";
    }

    public void setAuthenticationData(String u, String p){
        username = u;
        password = p;
    }

    public void setToken(String t){
        token = t;
    }

    public String getToken(){
        return token;
    }

    public String getUsername(){
        return username;
    }

    public void setCampUser(User s){
        self = s;
    }

    public static synchronized CampAuthenticatedUser getInstance(){
        if(instance == null){
            instance = new CampAuthenticatedUser();
        }
        return instance;
    }

    public boolean isInGroup(GroupStub groupStub){
        if(groupStub == null){
            return false;
        }

        ArrayList<GroupStub> gsList = self.getGroups();

        for(GroupStub stub : gsList) {
            if (groupStub.getID() == stub.getID()) {
                return true; // found in group we can view the details
            }
        }
        return false;
    }

    public boolean canListUsers(){

        return self.getType() == UserType.administrator;
    }

    public boolean canListGroups(){
        return self.getType() == UserType.administrator;
    }

    public boolean canViewMemberDetails(UserStub userStub){
        if(userStub == null)
            return false;

        return userStub.getID() == self.getID() || self.getType() != UserType.camper;
    }

    public boolean canViewGroup(GroupStub groupStub){
        if(groupStub == null){
            return false;
        }
        return self.getType() == UserType.administrator || isInGroup(groupStub);
    }
}
