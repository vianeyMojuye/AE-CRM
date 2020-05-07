package com.aecrm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aecrm.Models.PartyModel;
import com.aecrm.R;
import com.aecrm.Activities.VisitItemActivity;

import java.util.ArrayList;

public class VisitPartyAdapter extends  RecyclerView.Adapter<OperationAdapter.MyViewHolder>
{
    Context context;
    ArrayList<PartyModel> itemList = new ArrayList<>() ;

   public VisitPartyAdapter(Context context, ArrayList<PartyModel> objects)
   {
       this.context = context;
       this.itemList = objects;
   }

   public VisitPartyAdapter(){}

    @NonNull
    @Override
    public OperationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_operation_items, parent, false);
        OperationAdapter.MyViewHolder viewHolder = new OperationAdapter.MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OperationAdapter.MyViewHolder holder, int position) {


        final String item = itemList.get(position).getParty();

        holder.itemName.setText(item);

        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   Intent intent = new Intent(context, VisitItemActivity.class);
                   intent.putExtra("name",item);
                    context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(ArrayList<PartyModel> filterdNames) {
        this.itemList = filterdNames;
        notifyDataSetChanged();
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
