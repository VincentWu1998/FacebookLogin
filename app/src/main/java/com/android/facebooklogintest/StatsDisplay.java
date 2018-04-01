package com.android.facebooklogintest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class StatsDisplay extends AppCompatActivity {

    ImageView profilePic;
    ProfilePictureView profilePictureView;
    My_Profile curr_profile;
    Profile curr_usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_display);

        Intent intent = getIntent();
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView email_age = (TextView) findViewById(R.id.age);
        profilePictureView = (ProfilePictureView) findViewById(R.id.ProfilePicture);

        curr_profile = new My_Profile(Profile.getCurrentProfile());
        curr_usr = curr_profile.getUser();
        name.setText(name.getText().toString() + curr_usr.getFirstName() + " " +
                curr_usr.getLastName() + ",");
        email_age.setText("Your facebook email is: " + intent.getStringExtra("user_email") +
                         " \n and url to your profile is: " + curr_usr.getLinkUri());
        profilePictureView.setProfileId(curr_usr.getId());
    }

    public static Bitmap getFacebookProfilePicture(String userID) {
        Bitmap bitmap = null;
        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            try {
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (IOException io_ex) {
                System.out.println("Error: " + io_ex.getMessage());
                io_ex.printStackTrace();
            }
        } catch (MalformedURLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }
}
