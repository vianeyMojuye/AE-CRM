//package com.aecrm;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.View;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapPrimitive;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransportSE;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class CreatePartyCallAPI {
//
//    //API Links
//    private final String NAMESPACE = "http://tempuri.org/";
//    private final String URL = "http://203.134.206.216:85/aeservice.asmx";
//    String SOAP_ACTION = "http://tempuri.org/";
//    String METHOD_NAME ;
//    private String TAG = "AE CRM";
//
//    String serviceToCall;
//
//    String res;
//
//    Context context;
//    //Define the shared pref
//    SharedPreferences sharedPreferences;
//    // Gson use to convert Object into String and vise-versa
//    Gson gson = new Gson();
//
//
//    public  CreatePartyCallAPI(Context context)
//    {
//        this.context = context;
//    }
//
//
//    //AsyncCall Web Service
//    private class AsyncCallWS  extends AsyncTask<String, Void, Void> {
//
//
//        @Override
//        protected Void doInBackground(String... params) {
//            Log.i(TAG, "doInBackground");
//
//            if(serviceToCall =="NewParty")
//            {
//                //create the New PArty
//                newParty();
//            }else if(serviceToCall =="getCountryList")
//            {
//                //get CountryvList fom API
//                getCountryList();
//
//            }else if(serviceToCall == "getStateList")
//            {
//                //get CountryList fom API
//                getStateList();
//
//            }else if(serviceToCall == "getCityList"){
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            Log.i(TAG, "onPostExecute");
//            Log.i(TAG, res);
//
//            //New Party
//            if(serviceToCall =="NewParty") {
//                //Type is used for the conversion String into ArrayList
//                Type type1 = new TypeToken<HashMap<String, ArrayList<APIResponse>>>() {
//                }.getType();
//                //  receive the object from Soap Response
//                resp = gson.fromJson(res, type1);
//                //Log.i(TAG, resp.get("data").get(0).getName() );
//
//                APIResponse answer = new APIResponse(resp.get("data").get(0).getMessage(), resp.get("data").get(0).getMessageType());
//
//
//                Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));
//
//                if (answer.getMessageType().equals("Success")) {
//
//                    //reset inputs
//                    resetInput();
//                    // loading also when you click on sin in
//                    loadingProgressBar.setVisibility(View.GONE);
//                    showAlertDialog(CreatePartyActivity.this, " Creation New Party ", "SuccessFul!!!", false);
//
//
//                } else {
//
//                    // loading also when you click on sin in
//                    loadingProgressBar.setVisibility(View.GONE);
//                    showAlertDialog(CreatePartyActivity.this, " Creation New Party ", "Failed!!!", false);
//                }
//
//            }
//            else if(serviceToCall =="getCountryList"){
//
//                //get Country's List
//                //Type is used for the conversion String into ArrayList
//                Type type1 = new TypeToken<HashMap<String, ArrayList<Country>>>() {
//                }.getType();
//                //  receive the object from Soap Response
//                countryRes = gson.fromJson(res, type1);
//                //Log.i(TAG, resp.get("data").get(0).getName() );
//
//                Country answer = new Country(countryRes.get("data").get(0).getCountryId(),countryRes.get("data").get(0).getCountry(),countryRes.get("data").get(0).getStatus() );
//
//
//                //strore the Country List in the array required for the spinner
//
//                for (int i =0; i< countryRes.get("data").size() ; i++)
//                {
//                    myCountry.add(countryRes.get("data").get(i).getCountry());
//                }
//
//
//                Log.i(TAG, String.valueOf(resp.get("data").isEmpty()));
//
//
//            }
//            else if(serviceToCall =="getStateList"){
//
//            }
//            else if(serviceToCall =="getCityList"){
//
//
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Log.i(TAG, "onPreExecute ");
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            Log.i(TAG, "onProgressUpdate");
//        }
//
//    }
//
//
//    //New Party method connected to the Web service
//    public void newParty() {
//        //Create request
//        METHOD_NAME = "NewParty";
//        SOAP_ACTION = SOAP_ACTION+METHOD_NAME;
//
//
//        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//
//        //get user Contact
//        String user_contact = getUser();
//
//
//        //this should be removed after getting the country, city, state elements from API
//        my_city = "Ludhiana";
//
//
//        //Add the property to request object
//        request.addProperty("Party",company_name);
//        request.addProperty("FirstOwner",owner_name1);
//        request.addProperty("SecondOwner",owner_name2);
//        request.addProperty("FContact",owner1_contact);
//        request.addProperty("SContact",owner2_contact);
//        request.addProperty("Email",my_email);
//        request.addProperty("Address1",my_address1);
//        request.addProperty("Address2",my_address2);
//        request.addProperty("City",my_city);
//        request.addProperty("Latitude",latitude);
//        request.addProperty("Longitude",longitude);
//        request.addProperty("User",user_contact);
//
//
//
//        //Create envelope
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//                SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        //Set output SOAP object
//        envelope.setOutputSoapObject(request);
//        //Create HTTP call object
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//
//        try {
//            //Invole web service
//            androidHttpTransport.call(SOAP_ACTION, envelope);
//            //Get the response
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
//            //Assign it to fahren static variable
//            res = response.getValue().toString();
//            Log.i(TAG,res);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //getUser Contact from shared Pref
//    public String getUser()
//    {
//        //initialize the shared pref
//        sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//
//        //get the user details
//        String s1 = sharedPreferences.getString("LogUser","");
//
//        //Type is used for the conversion String into User
//        Type type1 = new TypeToken<User>() {
//        }.getType();
//        //  receive the object from Soap Response
//        User  user = gson.fromJson(s1, type1);
//
//        return  user.getContact();
//
//    }
//
//    //getCountryList
//    public void getCountryList()
//    {
//        METHOD_NAME = "GetCountryList";
//        SOAP_ACTION = SOAP_ACTION+METHOD_NAME;
//
//
//        //Create request
//        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//
//        //NO parameters
//
//        //Create envelope
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//                SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        //Set output SOAP object
//        envelope.setOutputSoapObject(request);
//        //Create HTTP call object
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//
//        try {
//            //Invole web service
//            androidHttpTransport.call(SOAP_ACTION, envelope);
//            //Get the response
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
//            //Assign it to fahren static variable
//            res = response.getValue().toString();
//            Log.i(TAG,res);
//            System.out.println(res);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    //call the task for fetching the country's list from API
//    public void callCountryTask()
//    {
//        serviceToCall ="getCountryList";
//        //Create instance for AsyncCallWS
//        AsyncCallWS mytask = new AsyncCallWS();
//        //Call execute
//        mytask.execute();
//    }
//
//    //getStateList
//    public void getStateList()
//    {
//        METHOD_NAME = "GetStateList";
//        SOAP_ACTION = SOAP_ACTION+METHOD_NAME;
//
//
//        //Create request
//        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//
//        //NO parameters
//
//        //Create envelope
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//                SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        //Set output SOAP object
//        envelope.setOutputSoapObject(request);
//        //Create HTTP call object
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//
//        try {
//            //Invole web service
//            androidHttpTransport.call(SOAP_ACTION, envelope);
//            //Get the response
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
//            //Assign it to fahren static variable
//            res = response.getValue().toString();
//            Log.i(TAG,res);
//            System.out.println(res);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    //call the task for fetching the state's list from API
//    public void callStateTask()
//    {
//        serviceToCall ="getStateList";
//        //Create instance for AsyncCallWS
//       AsyncCallWS mytask = new AsyncCallWS();
//        //Call execute
//        mytask.execute();
//    }
//
//
//}
