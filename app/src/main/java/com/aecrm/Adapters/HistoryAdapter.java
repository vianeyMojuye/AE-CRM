package com.aecrm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aecrm.CheckInternetConnection;
import com.aecrm.Models.APIHeaderModel;
import com.aecrm.Models.HistoryModel;
import com.aecrm.R;

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


        // Connection detector class
        final CheckInternetConnection cd = new CheckInternetConnection(context);
        //Internet status flag
        final Boolean[] isConnectionExist = {false};


        HistoryModel model = myList.get(position);

        //here we should set the date and issue
        String purpose="";

        holder.date.setText(model.getDateVisited());
        holder.name.setText(model.getParty());

        APIHeaderModel head = new APIHeaderModel();
        // get Internet status
        isConnectionExist[0] = cd.checkInternet();

        if (isConnectionExist[0]) {

        if(model.getModelType().contains("visitHistory")) {
                //purpose
                if (model.getOrder().contains("Yes")) {
                    purpose = purpose + " ORDER ";
                }
                if (model.getPayment().contains("Yes")) {
                    purpose = purpose + " PAYMENT ";
                }
                if (model.getIssue().contains("Yes")) {
                    purpose = purpose + " ISSUE ";
                }
            }else if (model.getModelType().contains("issueHistory"))
            {
                purpose = purpose + " ISSUE ";
            }


            holder.purpose.setText(purpose);

            //if the purpose is not and issue then hide it else show the issue
            if(purpose.contains("ISSUE") )
            {
                holder.issue.setText(model.getComments());

            }else
            {
                holder.issue.setVisibility(View.GONE);

            }

        }else{
            // Internet connection doesn't exist
            head.showAlertDialog(context, "No Internet Connection", "Please keep your phone connected to internet", false);

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
