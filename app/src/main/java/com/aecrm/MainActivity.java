package com.aecrm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    String myListOperation [] = {
            "Visit Party",
            "Create Party"
    } ;

    String myListReport [] = {
            "Issue History",
            "Visit History"
    } ;

    RecyclerView myOperations, myReports;
    LinearLayout operation,report;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operation = (LinearLayout) findViewById(R.id.operation);
        report = (LinearLayout) findViewById(R.id.report);

        myOperations = (RecyclerView)findViewById(R.id.list_operation);
        myReports = (RecyclerView)findViewById(R.id.list_report);
       logout = (Button)findViewById(R.id.logOut);

         // Operation Clicks
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myOperations.getVisibility() == View.VISIBLE)
                {
                    myOperations.setVisibility(View.GONE);
                }else{

                    OperationAdapter adapter = new OperationAdapter(MainActivity.this,myListOperation);
                    myOperations.setHasFixedSize(true);
                    myOperations.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    myOperations.setAdapter(adapter);

                    myOperations.setVisibility(View.VISIBLE);
                }
            }
        });

        //Reports Click
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myReports.getVisibility() == View.VISIBLE)
                {
                    myReports.setVisibility(View.GONE);
                }else{

                    OperationAdapter adapter = new OperationAdapter(MainActivity.this,myListReport);
                    myReports.setHasFixedSize(true);
                    myReports.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    myReports.setAdapter(adapter);

                    myReports.setVisibility(View.VISIBLE);
                }


            }
        });


        //Log Out click
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear the logged user object in the shar PRef

                //initialize the shared pref
             SharedPreferences   sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                //creating an Editor object ; to Edit(write into the file)
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //storing the VoiceMArkerModel object
                editor.putString("LogUser","");
                //apply the changes
                editor.apply();
                //call the login activity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}
