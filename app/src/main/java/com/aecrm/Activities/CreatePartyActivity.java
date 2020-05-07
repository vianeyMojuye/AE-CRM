package com.aecrm.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aecrm.CheckInternetConnection;
import com.aecrm.Models.APIHeaderModel;
import com.aecrm.Models.APIResponse;
import com.aecrm.Models.CityModel;
import com.aecrm.Models.CountryModel;
import com.aecrm.Models.StateModel;
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

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CreatePartyActivity extends AppCompatActivity {


    //API Links
    APIHeaderModel head = new APIHeaderModel();
    private final String NAMESPACE = head.getNAMESPACE();
    private final String URL = head.getURL();
    String SOAP_ACTION;
    String METHOD_NAME ;
    private String TAG = head.getTAG();
    String serviceToCall;

    String res;
    // ArrayList<APIResponse> myresp = new ArrayList<APIResponse>();
    HashMap<String, ArrayList<APIResponse>> resp = new HashMap<String, ArrayList<APIResponse>>();
    // Response of CountryListAPI
    HashMap<String, ArrayList<CountryModel>> countryRes = new HashMap<String, ArrayList<CountryModel>>();
    // Response of StateListAPI
    HashMap<String, ArrayList<StateModel>> stateRes = new HashMap<String, ArrayList<StateModel>>();
    // Response of CityListAPI
    HashMap<String, ArrayList<CityModel>> cityRes = new HashMap<String, ArrayList<CityModel>>();

    //Define the shared pref
    SharedPreferences sharedPreferences;
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();


    //Location variables
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;


    // dialog Box
    Dialog dialog;
    TextView dialogTitle, dialogTextview,dialogSCTextView,dialogPCTextView;


    RelativeLayout country, state, city;
    ImageButton addCountry, addState, addCity;
    EditText companyName, address1, address2, email, ownerName1, ownerName2, owner1Contact, owner2Contact,
            dialogEdit,dialogShortNameEdit, dialogPC,dialogSC;
    String company_name, my_address1, my_address2="", my_email,my_city,my_state,my_country, owner_name1, owner_name2="", owner1_contact, owner2_contact="",longitude,latitude ;
    Button submit, dialogSave;
    TextView btn_country, btn_state,btn_city;
    Boolean isSubmitClicked = false;
    ProgressBar loadingProgressBar, dialogLP;
    ImageView back;
    Toolbar toolbar;


    //Spinner Dialog Box
    SpinnerDialog spinnerDialog,spinnerDialog1,spinnerDialog2;

    // Spinner Drop down elements
    ArrayList<String> myCountry = new ArrayList<>();
    ArrayList<String> myState = new ArrayList<>();
    ArrayList<String> myCity = new ArrayList<>();
    // Creating adapter for spinner
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> stateAdapter;
    ArrayAdapter<String> cityAdapter;

    // Connection detector class
    CheckInternetConnection cd;
    //Internet status flag
    Boolean isConnectionExist = false;

    //add Country,state,city inputs
    String stringSCC,stringShortname,stringPincode,stringStatecode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);
        //check internet connection
        cd = new CheckInternetConnection(getApplicationContext());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(CreatePartyActivity.this);

        // get Internet status
        isConnectionExist = cd.checkInternet();

        if(isConnectionExist) {
            //callCountryTask
            callCountryTask();
        }else{
            // Internet connection doesn't exist
            head.showAlertDialog(CreatePartyActivity.this, "No Internet Connection", "Please enable Internet then reload this page !!!", false);

        }

        country = (RelativeLayout)findViewById(R.id.countrybtn);
        state  = (RelativeLayout)findViewById(R.id.statebtn) ;
        city  = (RelativeLayout)findViewById(R.id.citybtn) ;
        btn_country = (TextView) findViewById(R.id.btnCountry);
        btn_state  = (TextView)findViewById(R.id.btnState) ;
        btn_city  = (TextView)findViewById(R.id.btnCity) ;
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
        //menu toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set SpinnerDialogs

        spinnerDialog = new SpinnerDialog(CreatePartyActivity.this,myCountry,"Select item");
        spinnerDialog1 = new SpinnerDialog(CreatePartyActivity.this,myState,"Select item");
        spinnerDialog2 = new SpinnerDialog(CreatePartyActivity.this,myCity,"Select item");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                    my_country = item;
                    btn_country.setText(item);


                Toast.makeText(CreatePartyActivity.this, "Select :"+item, Toast.LENGTH_SHORT).show();
            }
        });
        spinnerDialog1.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {


                    my_state = item;
                    btn_state.setText(item);

                Toast.makeText(CreatePartyActivity.this, "Select :"+item, Toast.LENGTH_SHORT).show();
            }
        });
        spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                    my_city = item;
                    btn_city.setText(item);

                Toast.makeText(CreatePartyActivity.this, "Select :"+item, Toast.LENGTH_SHORT).show();
            }
        });

        // Click country // select country
         country.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 spinnerDialog.showSpinerDialog();
             }
         });
         // click state // select state
         state.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 spinnerDialog1.showSpinerDialog();
             }
         });
        // click city // select city
         city.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 spinnerDialog2.showSpinerDialog();
             }
         });





        //add country
        addCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDialog("country");
            }
        });
        //add State
        addState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDialog("state");
            }
        });
         //add City
        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDialog("city");
            }
        });


        //back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CreatePartyActivity.this,MainActivity.class));
                finish();
            }
        });

        //submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get Internet status
                isConnectionExist = cd.checkInternet();

                if(isConnectionExist) {
                    isSubmitClicked = true;
                    getLastLocation();
                }else{
                    // Internet connection doesn't exist
                    head.showAlertDialog(CreatePartyActivity.this, "No Internet Connection", "Please keep your phone connected to internet ", false);

                }
            }
        });


    }

    //set menu

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




    // set our custon Dialog
    public void setDialog(final String name) {
        /// custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("");

        // set the custom dialog components - text, image and button
        dialogEdit = (EditText) dialog.findViewById(R.id.dialog_edit);
        dialogShortNameEdit = (EditText) dialog.findViewById(R.id.shortName_edit);
        dialogPC = (EditText)dialog.findViewById(R.id.pinCode_edit);
        dialogSC = (EditText) dialog.findViewById(R.id.stateCode_edit);
        dialogTextview = (TextView) dialog.findViewById(R.id.dialog_textView);
        dialogPCTextView =(TextView)dialog.findViewById(R.id.pinCode_textView) ;
        dialogSCTextView = (TextView) dialog.findViewById(R.id.stateCode_textView);
        dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        dialogSave = (Button) dialog.findViewById(R.id.dialog_save);

        dialogLP = (ProgressBar)dialog.findViewById(R.id.loading);


        //if name == country

        if (name == "country") {
            dialogTitle.setText("Add Country");
            dialogTextview.setText("Country");


        } else if (name == "state") {
            dialogTitle.setText("Add State");
            dialogTextview.setText("State");
            dialogSCTextView.setVisibility(View.VISIBLE);
            dialogSC.setVisibility(View.VISIBLE);

        } else if (name == "city") {
            dialogTitle.setText("Add City");
            dialogTextview.setText("City");
            dialogPCTextView.setVisibility(View.VISIBLE);
            dialogPC.setVisibility(View.VISIBLE);
        }


        //click on Save
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get Internet status
                isConnectionExist = cd.checkInternet();

                if(isConnectionExist) {
                    //if name == country

                    if (name == "country") {

                        if (!dialogEdit.getText().toString().isEmpty() && !dialogShortNameEdit.getText().toString().isEmpty()) {
                            stringSCC = dialogEdit.getText().toString();
                            stringShortname = dialogShortNameEdit.getText().toString();


                            //1- call task API
                            callAddCountryTask();


                            loadingProgressBar.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(CreatePartyActivity.this, "Please fill all the Fields ", Toast.LENGTH_LONG).show();

                        }

                    } else if (name == "state") {

                        if (!dialogEdit.getText().toString().isEmpty() && !dialogShortNameEdit.getText().toString().isEmpty() &&
                                !dialogSC.getText().toString().isEmpty()) {


                            stringSCC = dialogEdit.getText().toString();
                            stringShortname = dialogShortNameEdit.getText().toString();
                            stringStatecode = dialogSC.getText().toString();

                            //1- check if country has been selected
                            if (my_country != null) {
                                //2- call the task for API
                                callAddStateTask();

                                loadingProgressBar.setVisibility(View.VISIBLE);

                            } else {
                                head.showAlertDialog(CreatePartyActivity.this, "Submit Failed", "Please select a country first  !!!", false);
                            }

                            dialog.dismiss();
                        } else {
                            Toast.makeText(CreatePartyActivity.this, "Please fill all the Fields ", Toast.LENGTH_LONG).show();

                        }

                    } else if (name == "city") {

                        if (!dialogEdit.getText().toString().isEmpty() && !dialogShortNameEdit.getText().toString().isEmpty() &&
                                !dialogPC.getText().toString().isEmpty()) {


                            stringSCC = dialogEdit.getText().toString();
                            stringShortname = dialogShortNameEdit.getText().toString();
                            stringPincode = dialogPC.getText().toString();

                            //1- check if country has been selected
                            if (my_state != null) {
                                //2- call the task for API
                                callAddCityTask();

                                loadingProgressBar.setVisibility(View.VISIBLE);

                            } else {
                                head.showAlertDialog(CreatePartyActivity.this, "Submit Failed", "Please select a state first  !!!", false);
                            }

                            dialog.dismiss();
                        } else {
                            Toast.makeText(CreatePartyActivity.this, "Please fill all the Fields ", Toast.LENGTH_LONG).show();

                        }
                    }
                }else{
                    // Internet connection doesn't exist
                    head.showAlertDialog(CreatePartyActivity.this, "No Internet Connection", "Please keep your phone connected to internet ", false);
                    dialog.dismiss();
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
                                        if (checkInput() == 5) {
                                            loadingProgressBar.setVisibility(View.VISIBLE);

                                            //get the city's name
                                          //  my_city = String.valueOf(country.getSelectedItem());
                                            if(my_city !=null )
                                            {
                                                //service to call
                                                serviceToCall ="NewParty";
                                                //Create instance for AsyncCallWS
                                                CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
                                                //Call execute
                                                mytask.execute();
                                            }else{
                                                Toast.makeText(CreatePartyActivity.this,"Select City ",Toast.LENGTH_LONG).show();
                                            }
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
//        if(!address2.getText().toString().isEmpty())
//        {
//            a = a+1;
//            my_address2 =   address2.getText().toString();
//        }else{
//            address2.requestFocus();
//            Toast.makeText(CreatePartyActivity.this,"Enter address2 ",Toast.LENGTH_LONG).show();
//            return 0;
//        }
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
//        if(!ownerName2.getText().toString().isEmpty())
//        {
//            a = a+1;
//            owner_name2 =   ownerName2.getText().toString();
//        }else{
//            ownerName2.requestFocus();
//            Toast.makeText(CreatePartyActivity.this,"Enter owner Name2 ",Toast.LENGTH_LONG).show();
//            return 0;
//        }
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
//        if(!owner2Contact.getText().toString().isEmpty())
//        {
//            a = a+1;
//            owner2_contact =   owner2Contact.getText().toString();
//        }else{
//            owner2Contact.requestFocus();
//            Toast.makeText(CreatePartyActivity.this,"Enter owner Contact2 ",Toast.LENGTH_LONG).show();
//            return 0;
//        }
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
        btn_city.setText("Select City");
        btn_state.setText("Select State");
        btn_country.setText("Select Country");
        my_country = null;
        my_state = null;
        my_city = null;
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
            }else if(serviceToCall=="addCountry")
            {
                //call add Country
                addCountry();
            }else if(serviceToCall=="addState"){

                //call add State
                addState();
            }else if(serviceToCall=="addCity"){
                //call add city
                addCity();
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


                  //  Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                    if (answer.getMessageType().equals("Success")) {

                        //reset inputs
                        resetInput();
                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation New Party ", "SuccessFul!!!", false);


                    } else {

                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation New Party ", "Failed!!!", false);
                    }

                }
                else if (serviceToCall == "getCountryList") {

                    //get Country's List
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<CountryModel>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    countryRes = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                   // Country answer = new Country(countryRes.get("data").get(0).getCountryId(), countryRes.get("data").get(0).getCountry(), countryRes.get("data").get(0).getStatus());
                    myCountry.clear();

                    //strore the Country List in the array required for the spinner

                    for (int i = 0; i < countryRes.get("data").size(); i++) {
                        myCountry.add(countryRes.get("data").get(i).getCountry());
                    }

                    Log.i(TAG,"CountryList ->   "+ myCountry.toString());

                    //callStateTask
                    callStateTask();


                }
                else if (serviceToCall == "getStateList") {

                    //get State's List
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<StateModel>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    stateRes = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );
                    myState.clear();

                    //strore the State's List in the array required for the spinner

                    for (int i = 0; i < stateRes.get("data").size(); i++) {
                        myState.add(stateRes.get("data").get(i).getState());
                    }

                    Log.i(TAG,"StateList ->   "+ myState.toString());

                    //callCityTask
                    callCityTask();

                }
                else if (serviceToCall == "getCityList") {

                    //get City's List
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<CityModel>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    cityRes = gson.fromJson(res, type1);

                    myCity.clear();
                    //strore the City's List in the array required for the spinner

                    for (int i = 0; i < cityRes.get("data").size(); i++) {
                        myCity.add(cityRes.get("data").get(i).getCity());
                    }
                    Log.i(TAG,"CityList ->   "+ myCity.toString());

                }else if(serviceToCall=="addCountry")
                {
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<APIResponse>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    resp = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                    APIResponse answer = new APIResponse(resp.get("data").get(0).getMessage(), resp.get("data").get(0).getMessageType());


                    //  Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                    if (answer.getMessageType().equals("Success")) {


                        //2- call the country's List
                        callCountryTask();
                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation Country ", "SuccessFul!!!", false);


                    } else {

                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation Country", "Failed!!!", false);
                    }
                    //reset
                    stringSCC =null; stringShortname = null;stringStatecode =null;stringPincode=null;
                }else if(serviceToCall=="addState")
                {
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<APIResponse>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    resp = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                    APIResponse answer = new APIResponse(resp.get("data").get(0).getMessage(), resp.get("data").get(0).getMessageType());


                    //  Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                    if (answer.getMessageType().equals("Success")) {

                        //2- call the country's List
                        callCountryTask();
                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation State ", "SuccessFul!!!", false);


                    } else {

                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation State", "Failed!!!", false);
                    }
                    //reset
                    btn_country.setText("Select Country");
                    stringSCC =null; stringShortname = null;stringStatecode =null;stringPincode=null;
                }else if(serviceToCall=="addCity")
                {
                    //Type is used for the conversion String into ArrayList
                    Type type1 = new TypeToken<HashMap<String, ArrayList<APIResponse>>>() {
                    }.getType();
                    //  receive the object from Soap Response
                    resp = gson.fromJson(res, type1);
                    //Log.i(TAG, resp.get("data").get(0).getName() );

                    APIResponse answer = new APIResponse(resp.get("data").get(0).getMessage(), resp.get("data").get(0).getMessageType());


                    //  Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                    if (answer.getMessageType().equals("Success")) {

                        //2- call the country's List
                        callCountryTask();
                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation City ", "SuccessFul!!!", false);


                    } else {

                        // loading also when you click on sin in
                        loadingProgressBar.setVisibility(View.GONE);
                        head.showAlertDialog(CreatePartyActivity.this, " Creation City", "Failed!!!", false);
                    }
                    //reset
                    btn_state.setText("Select State");
                    stringSCC =null; stringShortname = null;stringStatecode =null;stringPincode=null;
                }
            }
            else{

                head.showAlertDialog(CreatePartyActivity.this, "Internal Server Error", "Oops !!!", false);

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
       SOAP_ACTION =  NAMESPACE+METHOD_NAME;


        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //get user Contact
        String user_contact = getUser();


        //this should be removed after getting the country, city, state elements from API
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
        Type type1 = new TypeToken<UserModel>() {
        }.getType();
        //  receive the object from Soap Response
       UserModel user = gson.fromJson(s1, type1);

       return  user.getContact();

    }

    //getCountryList
    public void getCountryList()
    {
         METHOD_NAME = "GetCountryList";
        SOAP_ACTION =  NAMESPACE+METHOD_NAME;


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
            //Log.i(TAG,res);
           // System.out.println(res);

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
        SOAP_ACTION =  NAMESPACE+METHOD_NAME;


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
          //  Log.i(TAG,res);
           // System.out.println(res);

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
        SOAP_ACTION =  NAMESPACE+METHOD_NAME;


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
           // Log.i(TAG,res);
           // System.out.println(res);

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


    //addcountry
    public void addCountry()
    {
        METHOD_NAME = "NewCountry";
        SOAP_ACTION =  NAMESPACE+METHOD_NAME;
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        String user = getUser();

        //Add the property to request object
         request.addProperty("Country",stringSCC);
         request.addProperty("ShortName",stringShortname);
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

    //add state
    public void addState()
    {
        METHOD_NAME = "NewState";
        SOAP_ACTION =  NAMESPACE+METHOD_NAME;
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        String user = getUser();

        //country name without short name
        String[] reg  = my_country.split(" ",-1);
        int lt  = 0;
        String out ="" ;
        for(String a : reg)
        {
            if(lt == 0)
            { out = a;}
            lt = lt+1;
        }

        my_country = out.trim();

        //Add the property to request object
         request.addProperty("State",stringSCC);
         request.addProperty("Code",stringStatecode);
         request.addProperty("Country",my_country);
         request.addProperty("ShortName",stringShortname);
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
    // add city
    public void addCity()
    {
        METHOD_NAME = "NewCity";
        SOAP_ACTION =  NAMESPACE+METHOD_NAME;
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        String user = getUser();

        //state  name without short name
        String[] reg  = my_state.split(" ",-1);
        int lt  = 0;
        String out ="" ;
        for(String a : reg)
        {
            if(lt == 0)
            { out = a;}
            lt = lt+1;
        }
        my_state = out.trim();

        //Add the property to request object
         request.addProperty("State",my_state);
         request.addProperty("City",stringSCC);
         request.addProperty("PinCode",stringPincode);
         request.addProperty("ShortName",stringShortname);
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


    //call the task for adding a country
    public void callAddCountryTask()
    {
        serviceToCall ="addCountry";
        //Create instance for AsyncCallWS
        CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
        //Call execute
        mytask.execute();
    }
    //call the task for adding a State
    public void callAddStateTask()
    {
        serviceToCall ="addState";
        //Create instance for AsyncCallWS
        CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
        //Call execute
        mytask.execute();
    }
  //call the task for adding a City
    public void callAddCityTask()
    {
        serviceToCall ="addCity";
        //Create instance for AsyncCallWS
        CreatePartyActivity.AsyncCallWS mytask = new CreatePartyActivity.AsyncCallWS();
        //Call execute
        mytask.execute();
    }










}