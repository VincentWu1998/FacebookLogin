package com.android.facebooklogintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.Profile;

public class StatsDisplay extends AppCompatActivity {

    My_Profile curr_profile;
    Profile curr_usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_display);

        Intent intent = getIntent();
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView email_age = (TextView) findViewById(R.id.age);
        curr_profile = new My_Profile(Profile.getCurrentProfile());
        curr_usr = curr_profile.getUser();
        name.setText(name.getText().toString() + curr_usr.getFirstName() + " " +
                curr_usr.getLastName() + ",");
        email_age.setText("Your facebook email is: " + intent.getStringExtra("user_email") +
        " and url to your profile is: " + curr_usr.getLinkUri());
    }
}
