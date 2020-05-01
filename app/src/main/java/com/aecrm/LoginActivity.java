package com.aecrm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://203.134.206.216:85/aeservice.asmx";
    private final String SOAP_ACTION = "http://tempuri.org/AuthenticateLogin";
    private final String METHOD_NAME = "AuthenticateLogin";
    private String TAG = "AE CRM";

    EditText username,password;
    Button login;
    ProgressBar loadingProgressBar;

    //Define the shared pref
    SharedPreferences sharedPreferences;
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();

    String usernam,passwor, res;
    // Connection detector class
    CheckInternetConnection cd;
    //Internet status flag
    Boolean isConnectionExist = false;


    // ArrayList<User> myresp = new ArrayList<User>();
    HashMap<String, ArrayList<User>> resp = new HashMap<String, ArrayList<User> >();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize the shared pref
        sharedPreferences = this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);

        //check if there is a logged user
        checkLogUser();


        username = (EditText)findViewById(R.id.loginID);
        password = (EditText)findViewById(R.id.password);
        login =(Button)findViewById(R.id.signUp);
        loadingProgressBar = findViewById(R.id.loading);



        //check internet connection
        cd = new CheckInternetConnection(getApplicationContext());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if Celcius username control is not empty
                if (username.getText().length() != 0 && username.getText().toString() != "")
                {
                    //Check if password  control is not empty
                    if (password.getText().length() != 0 && password.getText().toString() != "")
                    {
                        // get Internet status
                        isConnectionExist = cd.checkInternet();

                        if(isConnectionExist) {
                            usernam = username.getText().toString();
                            passwor = password.getText().toString();

                            loadingProgressBar.setVisibility(View.VISIBLE);

                            //Create instance for AsyncCallWS
                            AsyncCallWS task = new AsyncCallWS();
                            //Call execute
                            task.execute();
                        }else{
                            username.setText("");
                            password.setText("");
                            // Internet connection doesn't exist
                            showAlertDialog(LoginActivity.this, "No Internet Connection", "Your device doesn't have internet access", false);

                        }


                    }else{
                        password.requestFocus();
                        showAlertDialog(LoginActivity.this, "Empty Password ", "Please fill all the fields", false);

                    }


                }else{
                    username.requestFocus();
                    showAlertDialog(LoginActivity.this, "Empty USER ID ", "Please fill all the fields", false);

                }

            }
        });
    }




    //AsyncCall Web Service
    private class AsyncCallWS  extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            Login(usernam,passwor);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Log.i(TAG, "-> "+res);
            if(!(res ==null)) {
                //Type is used for the conversion String into ArrayList
                Type type1 = new TypeToken<HashMap<String, ArrayList<User>>>() {
                }.getType();
                //  receive the object from Soap Response
                resp = gson.fromJson(res, type1);
                //Log.i(TAG, resp.get("data").get(0).getName() );


                Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));

                if (!resp.get("data").isEmpty()) {

                    User user = new User(resp.get("data").get(0).getName(), resp.get("data").get(0).getContact(),
                            resp.get("data").get(0).getEmail(), resp.get("data").get(0).getRole(),
                            resp.get("data").get(0).getPicture());


                    //store the user data in the SharedPref
                    storeLogUser(user);


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    String myJson = gson.toJson(user);
                    intent.putExtra("user", myJson);
                    startActivity(intent);
                    finish();
                    // loading also when you click on sin in
                    loadingProgressBar.setVisibility(View.GONE);
                } else {
                    username.setText("");
                    password.setText("");
                    showAlertDialog(LoginActivity.this, "Incorrect Login Credentials !!!", "Please enter correct values", false);
                }
            }else {
                loadingProgressBar.setVisibility(View.GONE);
                showAlertDialog(LoginActivity.this, "Incorrect Login Credentials !!! ", "Please enter correct values", false);

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
    public void Login(String username, String password) {
        //Create request
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);


        //Add the property to request object
        request.addProperty("Contact",username);
        request.addProperty("Password",password);



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

   //write into logged  user sharedPref
    public  void storeLogUser(User user)
    {

        // convert User object into String
        //convert to string using gson
        String stringUser= gson.toJson(user);

        //creating an Editor object ; to Edit(write into the file)
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //storing the VoiceMArkerModel object
        editor.putString("LogUser",stringUser);

        //apply the changes
        editor.apply();
    }

    //get user present in shared pref
    public void checkLogUser()
    {
        //get User

        String s1 = sharedPreferences.getString("LogUser","");

        Log.i(TAG,s1);

        if(!(s1 == ""))
        {
            ///call the Home Screen ;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            String myJson = gson.toJson(s1);
            intent.putExtra("user", myJson);
            startActivity(intent);
            finish();

        }
    }







}
