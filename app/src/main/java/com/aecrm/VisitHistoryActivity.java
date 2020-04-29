package com.aecrm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class VisitHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back ;

    ArrayList<HistoryModel> myList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_history);

        //set the default list
        defaultList();
        //


        back = (ImageView)findViewById(R.id.back);

        recyclerView = (RecyclerView)findViewById(R.id.visit_recycler);

        HistoryAdapter adapter = new HistoryAdapter(VisitHistoryActivity.this,myList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VisitHistoryActivity.this));
        recyclerView.setAdapter(adapter);


        //click back

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void defaultList()
    {
        HistoryModel model = new HistoryModel("10-09-2019","XYZ","ORDER");
        HistoryModel model1 = new HistoryModel("11-09-2019","TVS","ORDER, PAYMENT");
        HistoryModel model2 = new HistoryModel("12-09-2020","Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.","PEP","ISSUE");
        HistoryModel model3 = new HistoryModel("12-09-2020","MENLAH","PAYMENT");

        myList.add(model);
        myList.add(model1);
        myList.add(model2);
        myList.add(model3);

    }
}
