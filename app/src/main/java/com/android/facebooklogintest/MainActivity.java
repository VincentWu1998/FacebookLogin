package com.android.facebooklogintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.io.Serializable;
import java.util.Arrays;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;


public class MainActivity extends AppCompatActivity implements Serializable{

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        final TextView hello_user = (TextView) findViewById(R.id.hello_user);
        final Button displayStats = (Button) findViewById(R.id.display_button);
        final TextView or_view = (TextView) findViewById(R.id.or);

        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        if (AccessToken.getCurrentAccessToken() != null) {
            if (!AccessToken.getCurrentAccessToken().isExpired()) {
                Profile profile = Profile.getCurrentProfile();
                hello_user.setVisibility(View.VISIBLE);
                hello_user.setText(getString(R.string.greeting) + profile.getFirstName() + ", you have two options: ");
                or_view.setVisibility(View.VISIBLE);
                displayStats.setVisibility(View.VISIBLE);
            }
        }

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                hello_user.setVisibility(View.VISIBLE);
                hello_user.setText(getString(R.string.greeting) + profile.getFirstName() + ", you have two options: ");
                or_view.setVisibility(View.VISIBLE);
                displayStats.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println(exception.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    updateText();
                }
            }
        };
        accessTokenTracker.startTracking();
    }

    public void updateText() {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView txtView = (TextView) findViewById(R.id.hello_user);
                txtView.setVisibility(View.INVISIBLE);
                TextView orView = (TextView) findViewById(R.id.or);
                orView.setVisibility(View.INVISIBLE);
                txtView.setText(getString(R.string.greeting));
                Button button_disp = (Button) findViewById(R.id.display_button);
                button_disp.setVisibility(View.INVISIBLE);
            }
        });
    }
}