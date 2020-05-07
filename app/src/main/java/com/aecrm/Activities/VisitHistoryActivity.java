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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aecrm.Adapters.HistoryAdapter;
import com.aecrm.Models.APIHeaderModel;
import com.aecrm.Models.HistoryModel;
import com.aecrm.Models.PartyModel;
import com.aecrm.Models.UserModel;
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

public class VisitHistoryActivity extends AppCompatActivity {


    APIHeaderModel head = new APIHeaderModel();
    private final String NAMESPACE = head.getNAMESPACE();
    private final String URL = head.getURL();
    private final String METHOD_NAME = "GetVisitHistory";
    private final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
    private String TAG = head.getTAG();

    RecyclerView recyclerView;
    ImageView back ;
    ProgressBar loadingProgressBar;
    Toolbar toolbar;
    //initialize the shared pref
    SharedPreferences sharedPreferences;


    ArrayList<HistoryModel> myList = new ArrayList<>();

    String res;
    // receive the list
    HashMap<String, ArrayList<HistoryModel>> resp = new HashMap<String, ArrayList<HistoryModel> >();
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_history);

        loadingProgressBar = findViewById(R.id.loading);


        //set default arrayList
       // defaultList();
        //call the task used to get the Party's List
        callVHListTask();


        back = (ImageView)findViewById(R.id.back);
        //menu toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView)findViewById(R.id.visit_recycler);

        HistoryAdapter adapter = new HistoryAdapter(VisitHistoryActivity.this,myList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VisitHistoryActivity.this));
        recyclerView.setAdapter(adapter);


        //click back

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VisitHistoryActivity.this,MainActivity.class));
                finish();
            }
        });


    }


//    public void defaultList()
//    {
//        HistoryModel model = new HistoryModel("10-09-2019","XYZ","ORDER");
//        HistoryModel model1 = new HistoryModel("11-09-2019","TVS","ORDER, PAYMENT");
//        HistoryModel model2 = new HistoryModel("12-09-2020","Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem.","PEP","ISSUE");
//        HistoryModel model3 = new HistoryModel("12-09-2020","MENLAH","PAYMENT");
//
//        myList.add(model);
//        myList.add(model1);
//        myList.add(model2);
//        myList.add(model3);
//
//    }


    //////////////////Asynchron Task and API Stuff ///////////////////

    //AsyncCall Web Service
    private class AsyncCallWS  extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getVisitHistory();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Log.i(TAG, "-> "+res);
            if(!(res ==null)) {
                //Type is used for the conversion String into ArrayList
                Type type1 = new TypeToken<HashMap<String, ArrayList<HistoryModel>>>() {
                }.getType();
                //  receive the object from Soap Response
                resp = gson.fromJson(res, type1);
                //Log.i(TAG, resp.get("data").get(0).getName() );


              //  Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                if (!resp.get("data").isEmpty()) {


                    //Store The ArrayList into my List

                    for(int i=0; i< resp.get("data").size();i++)
                    {
                       // HistoryModel hist = new

                        myList.add(new HistoryModel(resp.get("data").get(i).getVisitId(),resp.get("data").get(i).getParty(),
                                resp.get("data").get(i).getPartyCoordinates(),resp.get("data").get(i).getEmployeeCoordinates(),resp.get("data").get(i).getOrder(),
                                resp.get("data").get(i).getPayment(),resp.get("data").get(i).getIssue(),resp.get("data").get(i).getDateVisited(),resp.get("data").get(i).getCheckInTime(),
                                resp.get("data").get(i).getCheckOutTime(),resp.get("data").get(i).getComments(),"visitHistory"));
                    }

                   // Log.i(TAG,"myList  -----"+myList.get(0).getDateVisited().toString());

                    // loading also when you click on sin in
                    loadingProgressBar.setVisibility(View.GONE);

                    recyclerView = (RecyclerView)findViewById(R.id.visit_recycler);

                    HistoryAdapter adapter = new HistoryAdapter(VisitHistoryActivity.this,myList);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(VisitHistoryActivity.this));
                    recyclerView.setAdapter(adapter);
                } else {
                    loadingProgressBar.setVisibility(View.GONE);
                    head.showAlertDialog(VisitHistoryActivity.this, " Empty Value", "Oops !!!", false);
                }
            }else {
                loadingProgressBar.setVisibility(View.GONE);
                head.showAlertDialog(VisitHistoryActivity.this," Empty Value", "Oops !!!", false);

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
    public void getVisitHistory() {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        String user = getUser();
        Log.i(TAG,user+"  "+SOAP_ACTION);

        //Add the property to request object
        request.addProperty("User",user);

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

    //getUser Contact from shared Pref
    public String getUser()
    {
        //initialize the shared pref
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        //get the user details
        String s1 = sharedPreferences.getString("LogUser","");

        //Type is used for the conversion String into User
        Type type1 = new TypeToken<UserModel>() {
        }.getType();
        //  receive the object from Soap Response
        UserModel user = gson.fromJson(s1, type1);

        return  user.getContact();

    }



    //call API TASK
    public void callVHListTask()
    {
        loadingProgressBar.setVisibility(View.VISIBLE);
        //Create instance for AsyncCallWS
        AsyncCallWS mytask = new AsyncCallWS();
        //Call execute
        mytask.execute();
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
