package ca.lukeharper.camp.campapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Luke on 2016-12-10.
 */

public class ListUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        ArrayList uList= (ArrayList) intent.getParcelableArrayListExtra(GroupActivity.LIST_USERS_MESSAGE);

        RecyclerView recList = (RecyclerView) findViewById(R.id.list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        UserAdapter ua = new UserAdapter(uList);
        recList.setAdapter(ua);
    }
}
