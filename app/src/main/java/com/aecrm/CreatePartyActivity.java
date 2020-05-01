package com.aecrm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class CreatePartyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    //API Links
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://203.134.206.216:85/aeservice.asmx";
    String SOAP_ACTION = "http://tempuri.org/";
    String METHOD_NAME ;
    private String TAG = "AE CRM";

    String serviceToCall;

    String res;
    // ArrayList<APIResponse> myresp = new ArrayList<APIResponse>();
    HashMap<String, ArrayList<APIResponse>> resp = new HashMap<String, ArrayList<APIResponse>>();
    // Response of CountryListAPI
    HashMap<String, ArrayList<Country>> countryRes = new HashMap<String, ArrayList<Country>>();
    //Define the shared pref
    SharedPreferences sharedPreferences;
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();


    //Location variables
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;


    // dialog Box
    Dialog dialog;
    TextView dialogTitle, dialogTextview;


    Spinner country, state, city;
    ImageButton addCountry, addState, addCity;
    EditText companyName, address1, address2, email, ownerName1, ownerName2, owner1Contact, owner2Contact, dialogEdit;
    String company_name, my_address1, my_address2, my_email,my_city,my_state,my_country, owner_name1, owner_name2, owner1_contact, owner2_contact,longitude,latitude ;
    Button submit, dialogSave;
    Boolean isSubmitClicked = false;
    ProgressBar loadingProgressBar;
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(CreatePartyActivity.this);

        //callCountryTask
        callCountryTask();
        //callStateTask
        callStateTask();
        //callCityTask
        callCityTask();

        country = (Spinner) findViewById(R.id.spinner_country);
        state = (Spinner) findViewById(R.id.spinner_state);
        city = (Spinner) findViewById(R.id.spinner_city);
        addCountry = (ImageButton) findViewById(R.id.add_country);
        addState = (ImageButton) findViewById(R.id.add_state);
        addCity = (ImageButton) findViewById(R.id.add_city);
        back = (ImageView) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit);
        companyName = (EditText)findViewById(R.id.company_name);
        address1 = (EditText)findViewById(R.id.address1);
        address2 = (EditText)findViewById(R.id.address2);
        email = (EditText)findViewById(R.id.email);
        ownerName1 =(EditText)findViewById(R.id.owner_name1);
        ownerName2 = (EditText)findViewById(R.id.owner_name2);
        owner1Contact = (EditText)findViewById(R.id.owner_contact1);
        owner2Contact = (EditText)findViewById(R.id.owner_contact2);
        loadingProgressBar = findViewById(R.id.loading);


        // Spinner click listener
        country.setOnItemSelectedListener(this);

        // Spinner Drop down country elements
//        myCountry.add("India");
//        myCountry.add("Cameroon");
//        myCountry.add("Congo");
//        myCountry.add("Canada");


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

        //submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSubmitClicked = true;
                getLastLocation();
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
    public void setDialog(final String name) {
        /// custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("");

        // set the custom dialog components - text, image and button
        dialogEdit = (EditText) dialog.findViewById(R.id.dialog_edit);
        dialogTextview = (TextView) dialog.findViewById(R.id.dialog_textView);
        dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        dialogSave = (Button) dialog.findViewById(R.id.dialog_save);

        //if name == country

        if (name == "country") {
            dialogTitle.setText("Add Country");
            dialogTextview.setText("Country");


        } else if (name == "state") {
            dialogTitle.setText("Add State");
            dialogTextview.setText("State");

        } else if (name == "city") {
            dialogTitle.setText("Add City");
            dialogTextview.setText("City");
        }


        //click on Save
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if name == country

                if (name == "country") {

                    Toast.makeText(CreatePartyActivity.this, "Add Contact", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else if (name == "state") {


                } else if (name == "city") {

                }

            }
        });

        //show the dialog
        dialog.show();

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

                                    //Toast.makeText(CreatePartyActivity.this, location.getLatitude() + "---" + location.getLongitude(), Toast.LENGTH_LONG).show();

                                    //check input

                                    if(isSubmitClicked) {

                                       // Toast.makeText(CreatePartyActivity.this,String.valueOf(country.getSelectedItem()),Toast.LENGTH_LONG).show();
                                        if (checkInput() == 8) {
                                            loadingProgressBar.setVisibility(View.VISIBLE);

                                            //get the city's name
                                            my_city = String.valueOf(country.getSelectedItem());
                                            //service to call
                                            serviceToCall ="NewParty";
                                            //Create instance for AsyncCallWS
                                            CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
                                            //Call execute
                                            mytask.execute();
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
        //if company Name is null
        if(!companyName.getText().toString().isEmpty())
        {
            a = a+1;
            company_name =   companyName.getText().toString();
        }else{
            companyName.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter Party's name ",Toast.LENGTH_LONG).show();
            return 0;
        }
        //if address1 is null
        if(!address1.getText().toString().isEmpty())
        {
            a = a+1;
            my_address1 =   address1.getText().toString();
        }else{
            address1.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter address1 ",Toast.LENGTH_LONG).show();
            return 0;
        }
        //if address2 is null
        if(!address2.getText().toString().isEmpty())
        {
            a = a+1;
            my_address2 =   address2.getText().toString();
        }else{
            address2.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter address2 ",Toast.LENGTH_LONG).show();
            return 0;
        }
         //if email is null

        if(!email.getText().toString().isEmpty() && email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+") )
        {
            a = a+1;
            my_email =   email.getText().toString();
        }else{
            email.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter email ",Toast.LENGTH_LONG).show();
            return 0;
        }
        //if owner's Name 1 is null
        if(!ownerName1.getText().toString().isEmpty())
        {
            a = a+1;
            owner_name1 =   ownerName1.getText().toString();
        }else{
            ownerName1.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter owner Name1 ",Toast.LENGTH_LONG).show();
            return 0;
        }
         //if owner's Name 2 is null
        if(!ownerName2.getText().toString().isEmpty())
        {
            a = a+1;
            owner_name2 =   ownerName2.getText().toString();
        }else{
            ownerName2.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter owner Name2 ",Toast.LENGTH_LONG).show();
            return 0;
        }
        //if owner's Contact 1 is null
        if(!owner1Contact.getText().toString().isEmpty())
        {
            a = a+1;
            owner1_contact =   owner1Contact.getText().toString();
        }else{
            owner1Contact.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter owner Contact1 ",Toast.LENGTH_LONG).show();
            return 0;
        }
        //if owner's Contact 2 is null
        if(!owner2Contact.getText().toString().isEmpty())
        {
            a = a+1;
            owner2_contact =   owner2Contact.getText().toString();
        }else{
            owner2Contact.requestFocus();
            Toast.makeText(CreatePartyActivity.this,"Enter owner Contact2 ",Toast.LENGTH_LONG).show();
            return 0;
        }
        //check for country , state, city

        return a;
    }

    //reset input
    public  void resetInput()
    {
        companyName.setText("");
        address1.setText("");
        address2.setText("");
        email.setText("");
        ownerName1.setText("");
        ownerName2.setText("");
        owner1Contact.setText("");
        owner2Contact.setText("");
        companyName.requestFocus();
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




    //AsyncCall Web Service
    private class AsyncCallWS  extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");

            if(serviceToCall =="NewParty")
            {
                //create the New PArty
                newParty();
            }else if(serviceToCall =="getCountryList")
            {
                //get CountryvList fom API
                  getCountryList();

            }else if(serviceToCall == "getStateList")
            {
                //get StateList fom API
                getStateList();

            }else if(serviceToCall == "getCityList"){
                //get CityList fom API
                getCityList();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            if(res != null) {
                Log.i(TAG, res);

                //New Party
                if (serviceToCall == "NewParty") {
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<APIResponse>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    resp = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                    APIResponse answer = new APIResponse(resp.get("data").get(0).getMessage(), resp.get("data").get(0).getMessageType());


                    Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                    if (answer.getMessageType().equals("Success")) {

                        //reset inputs
                        resetInput();
                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        showAlertDialog(CreatePartyActivity.this, " Creation New Party ", "SuccessFul!!!", false);


                    } else {

                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        showAlertDialog(CreatePartyActivity.this, " Creation New Party ", "Failed!!!", false);
                    }

                }
                else if (serviceToCall == "getCountryList") {

                    //get Country's List
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<Country>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    countryRes = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                    Country answer = new Country(countryRes.get("data").get(0).getCountryId(), countryRes.get("data").get(0).getCountry(), countryRes.get("data").get(0).getStatus());


                    //strore the Country List in the array required for the spinner

                    for (int i = 0; i < countryRes.get("data").size(); i++) {
                        myCountry.add(countryRes.get("data").get(i).getCountry());
                    }


                    Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));


                }
                else if (serviceToCall == "getStateList") {

                    //get State's List
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<State>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    stateRes = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                    Country answer = new Country(stateRes.get("data").get(0).getCountryId(), countryRes.get("data").get(0).getCountry(), countryRes.get("data").get(0).getStatus());


                    //strore the Country List in the array required for the spinner

                    for (int i = 0; i < countryRes.get("data").size(); i++) {
                        myCountry.add(countryRes.get("data").get(i).getCountry());
                    }


                    Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                }
                else if (serviceToCall == "getCityList") {


                }
            }
            else{


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



    //New Party method connected to the Web service
    public void newParty() {
        //Create request
       METHOD_NAME = "NewParty";
       SOAP_ACTION = SOAP_ACTION+METHOD_NAME;


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //get user Contact
        String user_contact = getUser();


        //this should be removed after getting the country, city, state elements from API
         my_city = "Ludhiana";


        //Add the property to request object
        request.addProperty("Party",company_name);
        request.addProperty("FirstOwner",owner_name1);
        request.addProperty("SecondOwner",owner_name2);
        request.addProperty("FContact",owner1_contact);
        request.addProperty("SContact",owner2_contact);
        request.addProperty("Email",my_email);
        request.addProperty("Address1",my_address1);
        request.addProperty("Address2",my_address2);
        request.addProperty("City",my_city);
        request.addProperty("Latitude",latitude);
        request.addProperty("Longitude",longitude);
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
        sharedPreferences = this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);

        //get the user details
        String s1 = sharedPreferences.getString("LogUser","");

        //Type is used for the conversion String into User
        Type type1 = new TypeToken<User>() {
        }.getType();
        //  receive the object from Soap Response
       User  user = gson.fromJson(s1, type1);

       return  user.getContact();

    }

    //getCountryList
    public void getCountryList()
    {
         METHOD_NAME = "GetCountryList";
        SOAP_ACTION = SOAP_ACTION+METHOD_NAME;


        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //NO parameters

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
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //call the task for fetching the country's list from API
    public void callCountryTask()
    {
        serviceToCall ="getCountryList";
        //Create instance for AsyncCallWS
        CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
        //Call execute
        mytask.execute();
    }

    //getStateList
    public void getStateList()
    {
        METHOD_NAME = "GetStateList";
        SOAP_ACTION = SOAP_ACTION+METHOD_NAME;


        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //NO parameters

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
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //call the task for fetching the state's list from API
    public void callStateTask()
    {
        serviceToCall ="getStateList";
        //Create instance for AsyncCallWS
        CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
        //Call execute
        mytask.execute();
    }

    //getCityList
    public void getCityList()
    {
        METHOD_NAME = "GetCityList";
        SOAP_ACTION = SOAP_ACTION+METHOD_NAME;


        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //NO parameters

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
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //call the task for fetching the state's list from API
    public void callCityTask()
    {
        serviceToCall ="getCityList";
        //Create instance for AsyncCallWS
        CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
        //Call execute
        mytask.execute();
    }














    //Alert DialogBox
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting alert dialog icon
        // alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        // Setting OK Button

        // Setting OK Button

        // Showing Alert Message
        alertDialog.show();
    }



}