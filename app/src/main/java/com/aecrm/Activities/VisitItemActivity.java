package com.aecrm.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aecrm.Models.APIHeaderModel;
import com.aecrm.Models.APIResponse;
import com.aecrm.Models.PartyModel;
import com.aecrm.Models.UserModel;
import com.aecrm.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class VisitItemActivity extends AppCompatActivity {


    //API Links
    APIHeaderModel head = new APIHeaderModel();
    private final String NAMESPACE = head.getNAMESPACE();
    private final String URL = head.getURL();
    String METHOD_NAME="MakeVisit";
    String SOAP_ACTION = NAMESPACE+METHOD_NAME;
    private String TAG = head.getTAG();


    String res;
    // ArrayList<APIResponse> myresp = new ArrayList<APIResponse>();
    HashMap<String, ArrayList<APIResponse>> resp = new HashMap<String, ArrayList<APIResponse>>();

    //Define the shared pref
    SharedPreferences sharedPreferences;
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();

    String party_name,latitude,longitude,isOrder,isPayment,isIssue,comments;

    //Location variables
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;


    TextView partyName;
    Button orderYes,orderNo,payYes,payNo, issueYes,issueNo,submit;
    EditText editIssue;
    ImageView back;
    ProgressBar loadingProgressBar;
    Toolbar toolbar;


    Boolean isSubmitClicked = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_item);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(VisitItemActivity.this);


        //receive the party name
        Bundle bundle = getIntent().getExtras();
        party_name = bundle.getString("name");

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
        loadingProgressBar = findViewById(R.id.loading);

        //menu toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //set the party's name
        partyName.setText(party_name);

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

                isIssue = "true";
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


                isIssue ="false";
                editIssue.setVisibility(View.GONE);
            }
        });

        //click payment yes
        payYes.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                isPayment = "true";
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

                isPayment = "false";
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

                isOrder = "true";
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

                isOrder = "false";
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

                isSubmitClicked = true;
                getLastLocation();
            }
        });



    }


    ///code for getting Latitude and Longitude

    //get Longitude Latitude

    //check Location permission
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    longitude = String.valueOf(location.getLongitude());
                                    latitude = String.valueOf(location.getLatitude());

                                    //check input

                                    if(isSubmitClicked) {

                                        // Toast.makeText(CreatePartyActivity.this,String.valueOf(country.getSelectedItem()),Toast.LENGTH_LONG).show();
                                        if (checkInput() == 3) {
                                            loadingProgressBar.setVisibility(View.VISIBLE);

                                            //Call the task
                                            callMakeVisiTask();
                                        }
                                    }


                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

        }
    };


    //check if input is empty
    public int checkInput()
    {
        int a =0 ;

        //check issue
        if(!(isIssue==null))
        {
            a= a+1;
            if(isIssue =="true")
            {
                comments = editIssue.getText().toString();
            }else{
                comments = "";
            }

        }else{
            head.showAlertDialog(VisitItemActivity.this," Error ISSUE ","please, select Yes or No !!!",false);
            return 0;
        }

        //check Payment
        if(!(isPayment==null))
        {
            a= a+1;


        }else{
            head.showAlertDialog(VisitItemActivity.this," Error PAYMENT ","please, select Yes or No !!!",false);
            return 0;
        }

        //check order
        if(!(isOrder==null))
        {
            a= a+1;

        }else{
            head.showAlertDialog(VisitItemActivity.this," Error ORDER ","please, select Yes or No !!!",false);
            return 0;
        }

        return a;
    }



    //check permissions
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    //request permission
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    //is location enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }





    //////////////////Asynchron Task and API Stuff ///////////////////

    //AsyncCall Web Service
    private class AsyncCallWS  extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            makeVisit();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Log.i(TAG, "-> "+res);
            if(!(res ==null)) {
                Log.i(TAG, res);


                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<APIResponse>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    resp = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                    APIResponse answer = new APIResponse(resp.get("data").get(0).getMessage(), resp.get("data").get(0).getMessageType());


                    Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                    if (answer.getMessageType().equals("Success")) {


                        // loading also when you click on submit
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(VisitItemActivity.this, " Make Visit ", "Successful!!!", false);
                        finish();

                    } else {

                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(VisitItemActivity.this, " Make Visit ", "Failed!!!", false);
                    }
            }else {
                loadingProgressBar.setVisibility(View.GONE);
                head.showAlertDialog(VisitItemActivity.this," Internal Server Error", "Oops !!!", false);

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
    public void makeVisit() {
        //Create request


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //get user Contact
        String user_contact = getUser();


        //Add the property to request object
        request.addProperty("Party",party_name);
        request.addProperty("Lat",latitude);
        request.addProperty("Long",longitude);
        request.addProperty("IsOrder",isOrder);
        request.addProperty("IsPayment",isPayment);
        request.addProperty("IssueRaised",isIssue);
        request.addProperty("Comments",comments);
        request.addProperty("User",user_contact);



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
            Log.i(TAG,res);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //getUser Contact from shared Pref
    public String getUser()
    {
        //initialize the shared pref
        sharedPreferences = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

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
    public void callMakeVisiTask()
    {
        loadingProgressBar.setVisibility(View.VISIBLE);
        //Create instance for AsyncCallWS
        VisitItemActivity.AsyncCallWS mytask = new VisitItemActivity.AsyncCallWS();
        //Call execute
        mytask.execute();
    }



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
