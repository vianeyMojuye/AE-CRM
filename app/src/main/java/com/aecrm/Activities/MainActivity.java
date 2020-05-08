package com.aecrm.Activities;

import androidx.annotation.MenuRes;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aecrm.Adapters.MenuAdapter;
import com.aecrm.Adapters.OperationAdapter;
import com.aecrm.CheckInternetConnection;
import com.aecrm.Models.APIHeaderModel;
import com.aecrm.Models.CountryModel;
import com.aecrm.Models.MenuModel;
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

public class MainActivity extends AppCompatActivity {

    APIHeaderModel head = new APIHeaderModel();
    private final String NAMESPACE = head.getNAMESPACE();
    private final String URL = head.getURL();
    private final String METHOD_NAME = "GetMenus";
    private final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
    private String TAG = head.getTAG();

    //Define the shared pref
    SharedPreferences sharedPreferences;
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();


    RecyclerView  myMenu;
    Toolbar toolbar;
    ProgressBar loadingProgressBar ;
    
    String res;

    // Connection detector class
    CheckInternetConnection cd;
    //Internet status flag
    Boolean isConnectionExist = false;

    HashMap<String, ArrayList<MenuModel>> MenuRes = new HashMap<String, ArrayList<MenuModel>>();
    ArrayList<String> levelList1 = new ArrayList<>();
    ArrayList<MenuModel> listMenu = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the shared pref
        sharedPreferences = this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);

      
        myMenu = (RecyclerView)findViewById(R.id.menu_list);
       toolbar = (Toolbar)findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        loadingProgressBar = findViewById(R.id.loading);



        //check internet connection
        cd = new CheckInternetConnection(getApplicationContext());
        // get Internet status
        isConnectionExist = cd.checkInternet();

        if(isConnectionExist) {

            loadingProgressBar.setVisibility(View.VISIBLE);
            //Create instance for AsyncCallWS
            MainActivity.AsyncCallWS task = new MainActivity.AsyncCallWS();
            //Call execute
            task.execute();

        }else
        {
            loadingProgressBar.setVisibility(View.GONE);
            // Internet connection doesn't exist
            head.showAlertDialog(MainActivity.this, "No Internet Connection", "Please connect your device to internet and re-open the app ", false);

        }




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

                startActivity(new Intent(MainActivity.this,ProfileActivity.class));

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
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();

            break;
            default:

            break;
        }
        return  true;
    }



    //AsyncCall Web Service
    private class AsyncCallWS  extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            getMenu();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Log.i(TAG, "-> "+res);
            loadingProgressBar.setVisibility(View.GONE);

            if(!(res ==null)) {

                //get Country's List
                //Type is used for the conversion String into ArrayList
                Type type1 = new TypeToken<HashMap<String, ArrayList<MenuModel>>>() {
                }.getType();
                //  receive the object from Soap Response
                MenuRes = gson.fromJson(res, type1);

                //strore the Menu List in the array required for the spinner

                for (int i = 0; i < MenuRes.get("data").size(); i++) {

                    if(Integer.parseInt(MenuRes.get("data").get(i).getIndexLevel()) <=1)
                    {

                        //add the parent name or menu title into levelList1 (IndexLevel ==0)
                        if(MenuRes.get("data").get(i).getIndexLevel().equals("0"))
                        {
                            levelList1.add(MenuRes.get("data").get(i).getPermission());
                        }
                        //add the Level2 Value
                        else if(MenuRes.get("data").get(i).getIndexLevel().equals("1")){

                            listMenu.add(new MenuModel(MenuRes.get("data").get(i).getPermissionId(),
                                    MenuRes.get("data").get(i).getPermission(),MenuRes.get("data").get(i).getParent(),MenuRes.get("data").get(i).getIndexLevel(),
                                    MenuRes.get("data").get(i).getURL(),MenuRes.get("data").get(i).getSpriteURL()));


                        }

                    }


                }

                Log.i(TAG," Level1 -> "+levelList1.toString());
                Log.i(TAG," Level2 -> "+listMenu.toString());

                //fill the menu List with the elements fetched from the data base

                MenuAdapter adapter = new MenuAdapter(MainActivity.this,levelList1,listMenu);
                myMenu.setHasFixedSize(true);
                myMenu.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                myMenu.setAdapter(adapter);

                myMenu.setVisibility(View.VISIBLE);





            }else{

                // Null Value
                head.showAlertDialog(MainActivity.this, "Internal Server Error", "Oops !!!", false);

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

    //getMenu method connected to the Web service
    public void getMenu() {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        String user = getUser();
        Log.i(TAG, "user -->"+user);

        //Add the property to request object
        request.addProperty("user",user);



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


}
