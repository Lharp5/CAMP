package ca.lukeharper.camp.campapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public final static String USER_MESSAGE = "ca.lukeharper.camp.campapp.USER";
    CampRestHandler rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rest = CampRestHandler.getInstance(this.getApplicationContext());
    }

    //Called when the user clicks the login button
    public void login(View view){
        //TODO authenticate with REST API


        final Intent intent = new Intent(this, ProfileActivity.class);

        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        EditText domain = (EditText) findViewById(R.id.domain);
        String userString = username.getText().toString();
        String passwordString = password.getText().toString();
        String domainString  = domain.getText().toString();

        final Context context = this.getApplicationContext();
        final Toast toast = Toast.makeText(this.getApplicationContext(), "Logging in", Toast.LENGTH_LONG);
        toast.show();
        rest.AuthenticateWithJWT(domainString, userString, passwordString, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                String token;
                toast.cancel();

                rest.getSelfRequest(new Response.Listener<User>() {
                    @Override
                    public void onResponse(User response) {
                            intent.putExtra(USER_MESSAGE, response);
                            startActivity(intent);
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast errorToast = Toast.makeText(context, "Failed", Toast.LENGTH_LONG);
                        errorToast.show();
                    }
                });

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                toast.cancel();
                Toast errorToast = Toast.makeText(context, "Failed", Toast.LENGTH_LONG);
                errorToast.show();
            }
        });
    }
}
