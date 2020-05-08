package com.aecrm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aecrm.Activities.MainActivity;
import com.aecrm.Models.MenuModel;
import com.aecrm.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> menuTitle;
    ArrayList<MenuModel> myMenuList;


    public MenuAdapter(Context context, ArrayList<String> menuTitle,ArrayList<MenuModel> myMenuList )
    {
        this.context = context;
        this.menuTitle = menuTitle;
        this.myMenuList = myMenuList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.main_menu_adapter, parent, false);
        MenuAdapter.MyViewHolder viewHolder = new MenuAdapter.MyViewHolder(listItem);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


         String myTitle = menuTitle.get(position);
        //set the title text
        holder.titletext.setText(myTitle);

        //subList
        final ArrayList<MenuModel> subMenuList = new ArrayList<>();



        //if we click the menublock
        holder.titleblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subMenuList.clear();
                //filter the menuList according to the title selected
                for (MenuModel model: myMenuList) {

                    if(model.getParent().equals(holder.titletext.getText().toString()))
                    {
                        subMenuList.add(model);
                    }
                }

                ///show or hide the sulist menu recycler view
                if(holder.recyclerView.getVisibility() == View.VISIBLE)
                {
                    holder.recyclerView.setVisibility(View.GONE);
                }else{

                    OperationAdapter adapter = new OperationAdapter(context,subMenuList);
                    holder.recyclerView.setHasFixedSize(true);
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.recyclerView.setAdapter(adapter);

                    holder.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return menuTitle.size();
    }

    public static  class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView titletext;
        RecyclerView recyclerView;
        RelativeLayout myLayout,titleblock;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titletext = itemView.findViewById(R.id.operatation_text);
            recyclerView = itemView.findViewById(R.id.list_operation);
            titleblock = itemView.findViewById(R.id.operation);
            myLayout = itemView.findViewById(R.id.menu_adapter);
        }
    }
}
