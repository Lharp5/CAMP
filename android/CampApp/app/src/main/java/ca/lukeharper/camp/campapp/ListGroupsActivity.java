package ca.lukeharper.camp.campapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ListGroupsActivity extends AppCompatActivity {

    public final static String GROUP_MESSAGE = "ca.lukeharper.camp.campapp.GROUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        ArrayList gList= (ArrayList) intent.getParcelableArrayListExtra(ProfileActivity.LIST_GROUPS_MESSAGE);

        RecyclerView recList = (RecyclerView) findViewById(R.id.list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        GroupAdapter ga = new GroupAdapter(gList);
        recList.setAdapter(ga);
    }
}
