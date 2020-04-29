package com.aecrm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{

    Context context;
    ArrayList<HistoryModel>  myList ;


    public HistoryAdapter(Context context, ArrayList<HistoryModel>  myList)
    {
        this.context = context;
        this.myList = myList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.issue_history_adapter,parent,false);
        HistoryAdapter.MyViewHolder viewHolder = new HistoryAdapter.MyViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

         HistoryModel model = myList.get(position);

        //here we should set the date and issue

        holder.date.setText(model.getDate());
        holder.name.setText(model.getName());
        holder.purpose.setText(model.getPurpose());

        //if the purpose is not and issue then hide it else show the issue
        if(model.getPurpose().toLowerCase().equals("issue") )
        {
            holder.issue.setText(model.getIssue());

        }else
        {
            holder.issue.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return myList.size();
    }


    public  static class  MyViewHolder extends RecyclerView.ViewHolder{

        public TextView date,issue,name,purpose;

        public MyViewHolder(View view)
        {
            super(view);

            date = (TextView) view.findViewById(R.id.date);
            issue = (TextView) view.findViewById(R.id.issue_details);
            name = (TextView)view.findViewById(R.id.party_name);
            purpose =(TextView)view.findViewById(R.id.purpose);

        }
    }
}
