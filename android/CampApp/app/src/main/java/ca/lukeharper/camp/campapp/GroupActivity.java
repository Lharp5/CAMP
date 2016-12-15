package ca.lukeharper.camp.campapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {
    public final static String LIST_USERS_MESSAGE = "ca.lukeharper.camp.campapp.LIST_USERS";
    private Group myGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        myGroup = (Group) intent.getParcelableExtra(ListGroupsActivity.GROUP_MESSAGE);
        TextView groupNameText = (TextView) findViewById(R.id.groupName);
        groupNameText.setText(myGroup.getName());
        TextView groupDescText = (TextView) findViewById(R.id.groupDesc);
        groupDescText.setText(myGroup.getDescription());
    }

    public void viewMembers(View view){
        Intent intent = new Intent(this, ListUsersActivity.class);
        intent.putExtra(LIST_USERS_MESSAGE, myGroup.getMembers());
        startActivity(intent);
    }
}
