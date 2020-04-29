package com.aecrm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreatePartyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Dialog dialog;

    Spinner country,state,city;
    ImageButton addCountry, addState, addCity;
    EditText companyName,address1,address2,email,ownerName1,ownerName2,owner1Contact,owner2Contact, dialogEdit;
    TextView dialogTitle,dialogTextview;
    Button submit, dialogSave;
    ImageView back;

    // Spinner Drop down elements
    ArrayList<String> myCountry = new ArrayList<>();
    ArrayList<String> myState = new ArrayList<>();
    ArrayList<String> myCity = new ArrayList<>();
    // Creating adapter for spinner
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> cityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);


        country = (Spinner)findViewById(R.id.spinner_country);
        addCountry =(ImageButton)findViewById(R.id.add_country);
        back =(ImageView) findViewById(R.id.back);
        // Spinner click listener
        country.setOnItemSelectedListener(this);

        // Spinner Drop down country elements
        myCountry.add("India");
        myCountry.add("Cameroon");
        myCountry.add("Congo");
        myCountry.add("Canada");



        // adapter for spinner
        countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myCountry);
        // Drop down layout style - list view with radio button
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        country.setAdapter(countryAdapter);


        //add country
        addCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDialog("country");
            }
        });

        //back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





    // set our custon Dialog
    public void setDialog(final String name)
    {
        /// custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("");

        // set the custom dialog components - text, image and button
        dialogEdit = (EditText)dialog.findViewById(R.id.dialog_edit);
        dialogTextview = (TextView)dialog.findViewById(R.id.dialog_textView);
        dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
        dialogSave = (Button)dialog.findViewById(R.id.dialog_save);

        //if name == country

        if(name == "country")
        {
            dialogTitle.setText("Add Country");
            dialogTextview.setText("Country");


        }else if(name == "state")
        {
            dialogTitle.setText("Add State");
            dialogTextview.setText("State");

        }else if(name == "city")
        {
            dialogTitle.setText("Add City");
            dialogTextview.setText("City");
        }



        //click on Save
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if name == country

                if(name == "country")
                {

                    Toast.makeText(CreatePartyActivity.this, "Add Contact", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }else if(name == "state")
                {


                }else if(name == "city")
                {

                }

            }
        });

        //show the dialog
        dialog.show();

    }







}
