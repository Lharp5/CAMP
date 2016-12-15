package ca.lukeharper.camp.campapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Luke on 2016-12-10.
*/

public class CampRestHandler {
    private static CampRestHandler mInstance;
    private RequestQueue mRequestQueue;
    //the context here is the ApplicationContext so its okay
    private static Context mCtx;
    private String baseURL;

    private CampAuthenticatedUser authUser;



    public void setBaseURL(String url){
        baseURL = url + "/";
    }

    public void getUserRequest(UserStub userStub, final Response.Listener<User> resp, final Response.ErrorListener err){
        makeJSONObjRequest("users/"+userStub.getID()+"/", Request.Method.GET, null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject userJSON = response.getJSONObject("User");
                    JSONArray groupList = response.getJSONArray("Groups");
                    User user = new User(userJSON);
                    user.addGroup(groupList);
                    resp.onResponse(user);
                } catch (JSONException exp) {
                    Toast error = Toast.makeText(mCtx, "User Not Found.", Toast.LENGTH_LONG);
                    error.show();
                }
            }
        }, err);
    }

    public void getSelfRequest(final Response.Listener<User> resp, final Response.ErrorListener err){
        makeJSONObjRequest("self/", Request.Method.GET, null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject userJSON = response.getJSONObject("User");
                    JSONArray groupList = response.getJSONArray("Groups");
                    User self = new User(userJSON);
                    self.addGroup(groupList);

                    //storing ourselves
                    authUser.setCampUser(self);
                    resp.onResponse(self);
                } catch (JSONException exp) {
                    Toast error = Toast.makeText(mCtx, "User Not Found.", Toast.LENGTH_LONG);
                    error.show();
                }
            }
        }, err);
    }

    public void getGroupRequest(GroupStub groupStub, final Response.Listener<Group> resp, final Response.ErrorListener err){
        makeJSONObjRequest("groups/"+groupStub.getID()+"/", Request.Method.GET, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject groupJSON = response.getJSONObject("Group");
                    JSONArray memberList = response.getJSONArray("Members");
                    Group group = new Group(groupJSON);
                    group.addMember(memberList);

                    resp.onResponse(group);
                } catch (JSONException exp) {
                    err.onErrorResponse(new VolleyError("Group Not Found"));
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                if(error.networkResponse != null){
                    //todo make my own default error handler, to throw appropriate errors to user..
                    switch(error.networkResponse.statusCode){
                        case 404:
                            err.onErrorResponse(new VolleyError("Group Not Found"));
                            break;
                        default:
                            err.onErrorResponse(new VolleyError("Group Not Found"));
                    }
                }
            }
        });
    }

    public void makeJSONObjRequest(String partialURL, int method, JSONObject data,Response.Listener<JSONObject> resp, Response.ErrorListener err){
        addToRequestQueue(new JsonObjectRequest(method, baseURL+partialURL, data, resp, err){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                if(authUser != null && authUser.getToken() != null)
                    headers.put("Authorization", "JWT "+authUser.getToken());
                return headers;
            }
        });
    }

    public void AuthenticateWithJWT(String url, final String username, final String password, final Response.Listener<JSONObject> resp, Response.ErrorListener err){
        setBaseURL(url);

        JSONObject data = new JSONObject();
        try{
            data.put("username", username);
            data.put("password", password);
        } catch(JSONException e){
            System.out.println("Error Encoding Object: "+e.toString());
            return;
        }

        makeJSONObjRequest("api-token-auth/",Request.Method.POST, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                authUser.setAuthenticationData(username, password);
                try {
                    authUser.setToken(response.getString("token"));
                }catch(JSONException exp){
                    Toast successToast = Toast.makeText(mCtx, "Network Error.", Toast.LENGTH_LONG);
                    successToast.show();
                    return;
                }

                //Calling the users request
                resp.onResponse(response);
            }
        }, err);
    }

    private CampRestHandler(Context context){
        authUser = CampAuthenticatedUser.getInstance();
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized CampRestHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new CampRestHandler(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            //getApplicationContext() iis key, it keeps you from leaking the
            //Activity or BroadcastReciever if someone passes one in.

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
}
