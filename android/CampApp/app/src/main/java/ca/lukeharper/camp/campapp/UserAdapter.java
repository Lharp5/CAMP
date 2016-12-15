package ca.lukeharper.camp.campapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

/**
 * Created by Luke on 2016-12-10.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
private List<UserStub> userList;

public UserAdapter(List<UserStub> uList){
        userList = uList;
        }

@Override public int getItemCount(){
        return userList.size();
        }

@Override
public void onBindViewHolder(UserViewHolder userViewHolder, int i){
    UserStub u = userList.get(i);
    userViewHolder.userStub = u;
    userViewHolder.userName.setText(u.getLastName() + ", "+u.getFirstName());
    userViewHolder.itemView.setClickable(false);
    if(CampAuthenticatedUser.getInstance().canViewMemberDetails(u)){
        userViewHolder.itemView.setClickable(true);
    }
}

@Override
public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basic_card, viewGroup, false);
        return new UserViewHolder(itemView);
        }

@Override
public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        }

public class UserViewHolder extends RecyclerView.ViewHolder{
    protected TextView userName;
    protected UserStub userStub;
    protected TextView userSubtitle;
    public UserViewHolder(View v){
        super(v);
        userName = (TextView) v.findViewById(R.id.name);
        userSubtitle = (TextView) v.findViewById(R.id.subtitle);
        userSubtitle.setText("");
        v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                CampRestHandler rest = CampRestHandler.getInstance(v.getContext().getApplicationContext());
                final Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                rest.getUserRequest(userStub, new Response.Listener<User>() {
                    @Override
                    public void onResponse(User response) {
                        intent.putExtra(LoginActivity.USER_MESSAGE, response);
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