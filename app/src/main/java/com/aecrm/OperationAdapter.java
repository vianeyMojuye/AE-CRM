package com.aecrm;

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

public class OperationAdapter extends  RecyclerView.Adapter<OperationAdapter.MyViewHolder> {

    Context context;
    String itemList[] =new String[2] ;


    public OperationAdapter(Context context, String[] objects)
    {
        this.context = context;
        this.itemList = objects;
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


        final String item = itemList[position];

        holder.itemName.setText(item);

        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item =="Visit Party")
                {
                    Intent intent = new Intent(context,VisitPartyActivity.class);
                    context.startActivity(intent);

                }else if(item =="Create Party"){

                    Intent intent = new Intent(context,CreatePartyActivity.class);
                    context.startActivity(intent);

                }else if(item =="Issue History"){

                    Intent intent = new Intent(context,IssueHistoryActivity.class);
                    context.startActivity(intent);
                }else if(item =="Visit History"){

                    Intent intent = new Intent(context,VisitHistoryActivity.class);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.length;
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
