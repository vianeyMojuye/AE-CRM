package com.aecrm.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aecrm.Models.UserModel;
import com.aecrm.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ProfileActivity extends AppCompatActivity {

    TextView name,role;
    ImageView back;
    Toolbar toolbar;
    //initialize the shared pref
    SharedPreferences   sharedPreferences;
    // Gson use to convert Object into String and vise-versa
    Gson gson = new Gson();

    UserModel user = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get the user
        user = getUser();

        name =(TextView)findViewById(R.id.name_profile);
        role = (TextView)findViewById(R.id.role_profile);

        //menu toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back = (ImageView)findViewById(R.id.back);


        //set name and Role
        name.setText(user.getName());
        role.setText(user.getRole());

        //press back button;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

                startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));

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
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                finish();

                break;
            default:

                break;
        }
        return  true;
    }


    //getUser  from shared Pref
    public UserModel getUser()
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

        return  user;

    }

}
