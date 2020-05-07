package com.aecrm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aecrm.Adapters.VisitPartyAdapter;
import com.aecrm.CheckInternetConnection;
import com.aecrm.Models.APIHeaderModel;
import com.aecrm.Models.PartyModel;
import com.aecrm.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Locale.filter;

public class VisitPartyActivity extends AppCompatActivity {


    APIHeaderModel head = new APIHeaderModel();
    private final String NAMESPACE = head.getNAMESPACE();
    private final String URL = head.getURL();
    private final String METHOD_NAME = "GetPartyList";
    private final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
    private String TAG = head.getTAG();


    RecyclerView listReport;
    EditText editSearch;
    ImageView back;
    ProgressBar loadingProgressBar;
    Toolbar toolbar;
    //initialize the shared pref
    SharedPreferences sharedPreferences;

    ArrayList<PartyModel> myList = new ArrayList<>();
    VisitPartyAdapter adapter ;

    String res;
    // receive the list
    HashMap<String, ArrayList<PartyModel>> resp = new HashMap<String, ArrayList<PartyModel> >();
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();

    // Connection detector class
    CheckInternetConnection cd;
    //Internet status flag
    Boolean isConnectionExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_party);

        loadingProgressBar = findViewById(R.id.loading);
        //check internet connection
        cd = new CheckInternetConnection(getApplicationContext());

        //set default arrayList
      //  defaultArray();
        //call the task used to get the Party's List
        callPartyListTask();

        listReport = (RecyclerView) findViewById(R.id.list_report);
        editSearch = (EditText)findViewById(R.id.ediSearch);
        back = (ImageView)findViewById(R.id.back);

        //menu toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                startActivity(new Intent(VisitPartyActivity.this,MainActivity.class));
                finish();
            }
        });

    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<PartyModel> filterdNames = new ArrayList<>();

        //looping through existing elements
           for (PartyModel s : myList) {
            //if the existing elements contains the search input
            if (s.getParty().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdNames);
    }


    public void defaultArray()
    {
        myList.add(new PartyModel("aaaa","aaa@aa.com"));
        myList.add(  new PartyModel("bbbaaa","bb@bb.com"));
        myList.add(new PartyModel("ccbbbbaaaaccc","cc@cc.com"));
        myList.add(  new PartyModel("dddbccccc","dd@dd.com"));
        myList.add(new PartyModel("eeeeebbbbbbbee","ee@ee.com"));
        myList.add(  new PartyModel("ffaaaaaafff","ff@f.com"));

    }

    //////////////////Asynchron Task and API Stuff ///////////////////

    //AsyncCall Web Service
    private class AsyncCallWS  extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getPartyList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Log.i(TAG, "-> "+res);
            if(!(res ==null)) {
                //Type is used for the conversion String into ArrayList
                Type type1 = new TypeToken<HashMap<String, ArrayList<PartyModel>>>() {
                }.getType();
                //  receive the object from Soap Response
                resp = gson.fromJson(res, type1);
                //Log.i(TAG, resp.get("data").get(0).getName() );


                Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                if (!resp.get("data").isEmpty()) {

                 //Store The ArrayList into my List

                    for(PartyModel party : resp.get("data"))
                    {
                        myList.add(party);
                        //set Adapter and recyclerList
                        adapter = new VisitPartyAdapter(VisitPartyActivity.this,myList);
                        listReport.setHasFixedSize(true);
                        listReport.setLayoutManager(new LinearLayoutManager(VisitPartyActivity.this));
                        listReport.setAdapter(adapter);

                        listReport.setVisibility(View.VISIBLE);
                    }

                    // loading also when you click on sin in
                    loadingProgressBar.setVisibility(View.GONE);
                } else {
                    loadingProgressBar.setVisibility(View.GONE);
                    head.showAlertDialog(VisitPartyActivity.this, " Internal Server Error", "Oops !!!", false);
                }
            }else {
                loadingProgressBar.setVisibility(View.GONE);
                head.showAlertDialog(VisitPartyActivity.this," Internal Server Error", "Oops !!!", false);

            }
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute ");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }



    //login method connected to the Web service
    public void getPartyList() {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        //Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        //Set output SOAP object
        envelope.setOutputSoapObject(request);
        //Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            //Invole web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            //Assign it to fahren static variable
            res = response.getValue().toString();
            //res = response.getPropertyCount();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //call API TASK
    public void callPartyListTask()
    {
        // get Internet status
        isConnectionExist = cd.checkInternet();

        if(isConnectionExist) {
            loadingProgressBar.setVisibility(View.VISIBLE);
            //Create instance for AsyncCallWS
            AsyncCallWS mytask = new AsyncCallWS();
            //Call execute
            mytask.execute();
        }else
        {
            // Internet connection doesn't exist
            head.showAlertDialog(VisitPartyActivity.this, "No Internet Connection", "Your device doesn't have internet access", false);
        }
    }


    // set menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId()))
        {
            case R.id.menu_profile:

                //call activity profile

                startActivity(new Intent(this,ProfileActivity.class));

                break;
            case R.id.menu_logout:

                //clear the logged user object in the shar PRef
                sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                //creating an Editor object ; to Edit(write into the file)
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //storing the VoiceMArkerModel object
                editor.putString("LogUser","");
                //apply the changes
                editor.apply();
                //call the login activity
                startActivity(new Intent(this,LoginActivity.class));
                finish();

                break;
            default:

                break;
        }
        return  true;
    }




}
