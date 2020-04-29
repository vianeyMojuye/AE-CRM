package com.aecrm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class VisitItemActivity extends AppCompatActivity {

    TextView partyName;
    String name;
    Button orderYes,orderNo,payYes,payNo, issueYes,issueNo,submit;
    EditText editIssue;
    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_item);

        //receive the party name
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");

        //hold the xml items
        partyName = (TextView)findViewById(R.id.party_name);
        orderYes = (Button) findViewById(R.id.order_yes);
        orderNo = (Button) findViewById(R.id.order_no);
        payYes = (Button) findViewById(R.id.pay_yes);
        payNo = (Button) findViewById(R.id.pay_no);
        issueYes = (Button) findViewById(R.id.issue_yes);
        issueNo = (Button) findViewById(R.id.issue_no);
        submit = (Button)findViewById(R.id.submit);
        editIssue = (EditText)findViewById(R.id.edit_issue);
        back  = (ImageView)findViewById(R.id.back);

        //set the party's name
        partyName.setText(name);

        //click issue yes
        issueYes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                issueYes.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_green));
                issueYes.setTextColor(getResources().getColor(R.color.colorGreen));
                issueNo.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_black));
                issueNo.setTextColor(getResources().getColor(R.color.colorBlack));

                editIssue.setVisibility(View.VISIBLE);
            }
        });

        // click on issue no
        issueNo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                issueYes.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_black));
                issueYes.setTextColor(getResources().getColor(R.color.colorBlack));
                issueNo.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_red));
                issueNo.setTextColor(getResources().getColor(R.color.colorRed));


                editIssue.setVisibility(View.GONE);
            }
        });

        //click payment yes
        payYes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                payYes.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_green));
                payYes.setTextColor(getResources().getColor(R.color.colorGreen));
                payNo.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_black));
                payNo.setTextColor(getResources().getColor(R.color.colorBlack));

            }
        });

        // click on Payment no
        payNo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                payYes.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_black));
                payYes.setTextColor(getResources().getColor(R.color.colorBlack));
                payNo.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_red));
                payNo.setTextColor(getResources().getColor(R.color.colorRed));


            }
        });

        //click order yes
        orderYes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                orderYes.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_green));
                orderYes.setTextColor(getResources().getColor(R.color.colorGreen));
                orderNo.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_black));
                orderNo.setTextColor(getResources().getColor(R.color.colorBlack));


            }
        });

        // click on order no
        orderNo.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                orderYes.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_black));
                orderYes.setTextColor(getResources().getColor(R.color.colorBlack));
                orderNo.setBackground(ContextCompat.getDrawable(VisitItemActivity.this,R.drawable.button_red));
                orderNo.setTextColor(getResources().getColor(R.color.colorRed));

            }
        });

        //click on back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //click on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}
