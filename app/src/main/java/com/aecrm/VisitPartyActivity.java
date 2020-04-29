package com.aecrm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import static java.util.Locale.filter;

public class VisitPartyActivity extends AppCompatActivity {

    RecyclerView listReport;
    EditText editSearch;
    ImageView back;
    ArrayList<String> myList = new ArrayList<>();
    VisitPartyAdapter adapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_party);

        listReport = (RecyclerView) findViewById(R.id.list_report);
        editSearch = (EditText)findViewById(R.id.ediSearch);
        back = (ImageView)findViewById(R.id.back);
        //set default arrayList
        defaultArray();
        //set Adapter and recyclerList
        adapter = new VisitPartyAdapter(VisitPartyActivity.this,myList);
        listReport.setHasFixedSize(true);
        listReport.setLayoutManager(new LinearLayoutManager(VisitPartyActivity.this));
        listReport.setAdapter(adapter);

        listReport.setVisibility(View.VISIBLE);

        //implement the editsearch filter
        //adding a TextChangedListener
        //to call a method whenever there is some change on the EditText
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

        //back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
           for (String s : myList) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }


    public void defaultArray()
    {
        myList.add("aaaaaaaaa");
        myList.add("bbbbbbbbb");
        myList.add("aaabbbbbbb");
        myList.add("aabbcccccccc");
        myList.add("cccccccccccc");
        myList.add("aaccddeee");
    }
}
