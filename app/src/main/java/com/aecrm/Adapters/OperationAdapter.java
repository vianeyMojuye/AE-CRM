package com.aecrm.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aecrm.Activities.CreatePartyActivity;
import com.aecrm.Activities.IssueHistoryActivity;
import com.aecrm.CheckInternetConnection;
import com.aecrm.Models.APIHeaderModel;
import com.aecrm.Models.MenuModel;
import com.aecrm.R;
import com.aecrm.Activities.VisitHistoryActivity;
import com.aecrm.Activities.VisitPartyActivity;

import java.util.ArrayList;

public class OperationAdapter extends  RecyclerView.Adapter<OperationAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuModel> myMenuList;


    public OperationAdapter(Context context, ArrayList<MenuModel> myMenuList)
    {
        this.context = context;
        this.myMenuList = myMenuList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_operation_items, parent, false);
        OperationAdapter.MyViewHolder viewHolder = new OperationAdapter.MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // Connection detector class
        final CheckInternetConnection cd = new CheckInternetConnection(context);
        //Internet status flag
        final Boolean[] isConnectionExist = {false};



        final String item = myMenuList.get(position).getPermission();

        holder.itemName.setText(item);

        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Log.i("item --> ",item);
                APIHeaderModel head = new APIHeaderModel();
                // get Internet status
                isConnectionExist[0] = cd.checkInternet();

                if (isConnectionExist[0]) {


                    if (item.equals("Visit Party")) {
                        Intent intent = new Intent(context, VisitPartyActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();


                    } else if (item.equals("Create Party")) {

                        Intent intent = new Intent(context, CreatePartyActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();


                    } else if (item.equals("Issue List")) {

                        Intent intent = new Intent(context, IssueHistoryActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();

                    } else if (item.equals("Visit History")) {

                        Intent intent = new Intent(context, VisitHistoryActivity.class);
                        context.startActivity(intent);
                        ((Activity)context).finish();

                    }

                }else{
                    // Internet connection doesn't exist
                    head.showAlertDialog(context, "No Internet Connection", "Please keep your phone connected to internet", false);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return myMenuList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView itemName;
        public RelativeLayout myLayout;
        public ImageView itemNextButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.operation_itemName);
            myLayout = itemView.findViewById(R.id.my_layout);
            itemNextButton = itemView.findViewById(R.id.operation_itemButton);

        }
    }


}
