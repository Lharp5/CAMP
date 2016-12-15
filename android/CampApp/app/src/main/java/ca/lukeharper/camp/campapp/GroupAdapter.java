package ca.lukeharper.camp.campapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Luke on 2016-12-09.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private List<GroupStub> groupList;

    public GroupAdapter(List<GroupStub> gList){
        groupList = gList;
    }

    @Override public int getItemCount(){
        return groupList.size();
    }

    @Override
    public void onBindViewHolder(GroupViewHolder groupViewHolder, int i){
        GroupStub g = groupList.get(i);
        groupViewHolder.groupStub = g;
        groupViewHolder.groupName.setText(g.getName());

        if(CampAuthenticatedUser.getInstance().canViewGroup(g)) {
            if (CampAuthenticatedUser.getInstance().isInGroup(g)) {
                groupViewHolder.groupSubtitle.setText("In Group");
            }
        }
        else{
            groupViewHolder.itemView.setClickable(false);
        }
    }



    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_card, viewGroup, false);
        return new GroupViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        protected TextView groupName;
        protected TextView groupSubtitle;
        protected GroupStub groupStub;
        public GroupViewHolder(View v){
            super(v);
            groupName = (TextView) v.findViewById(R.id.name);
            groupSubtitle = (TextView) v.findViewById(R.id.subtitle);
            groupSubtitle.setText("");
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    CampRestHandler rest = CampRestHandler.getInstance(v.getContext().getApplicationContext());
                    final Intent intent = new Intent(v.getContext(), GroupActivity.class);
                    rest.getGroupRequest(groupStub, new Response.Listener<Group>() {
                        @Override
                        public void onResponse(Group response) {
                            intent.putExtra(ListGroupsActivity.GROUP_MESSAGE, response);
                            v.getContext().startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast displayError = Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_LONG);
                            displayError.show();
                        }
                    });
                }
            });
        }
    }

}
