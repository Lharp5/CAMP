package ca.lukeharper.camp.campapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    public final static String LIST_GROUPS_MESSAGE = "ca.lukeharper.camp.campapp.LIST_GROUPS";
    private User myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        myUser = (User) intent.getParcelableExtra(LoginActivity.USER_MESSAGE);
        TextView userFNText = (TextView) findViewById(R.id.userFN);
        userFNText.setText(myUser.getFirstName());
        TextView userLNText  = (TextView) findViewById(R.id.userLN);
        userLNText.setText(myUser.getLastName());
        TextView userRoleText  = (TextView) findViewById(R.id.userRole);
        userRoleText.setText(myUser.getType().name().toLowerCase());
    }

    public void viewGroups(View view){
        Intent intent = new Intent(this, ListGroupsActivity.class);
        intent.putExtra(LIST_GROUPS_MESSAGE, myUser.getGroups());
        startActivity(intent);
    }
}
