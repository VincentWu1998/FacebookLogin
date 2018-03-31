package com.android.facebooklogintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;


public class MainActivity extends AppCompatActivity implements Serializable{

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    public static My_Profile curr_usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Our app should log out by default
        disconnectFromFacebook();
        LoginManager.getInstance().logOut();

        final boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
        System.out.println("We are not logged in, this is: " + loggedIn);

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        final TextView hello_user = (TextView) findViewById(R.id.hello_user);
        final Button displayStats = (Button) findViewById(R.id.display_button);
        final TextView or_view = (TextView) findViewById(R.id.or);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("email", "user_birthday", "user_friends"));
        System.out.println("Read Permissions set!");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setFacebookData(loginResult);

                //Retrieve new Profile
                if(Profile.getCurrentProfile() == null) {
                    System.out.println("New profile in use.");
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            System.out.println("Profile has changed");
                            // Perform GUI update when login success
                            loginSuccessGUIUpdate(currentProfile, hello_user, or_view, displayStats);
                        }
                    };
                }
                else {
                    System.out.println("Old profile still in use.");
                    Profile profile = Profile.getCurrentProfile();
                    // Perform GUI update when login success
                    loginSuccessGUIUpdate(profile, hello_user, or_view, displayStats);
                }
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

    private void setFacebookData(final LoginResult loginResult) {
        System.out.println("Requesting graph");
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {
                            String email = response.getJSONObject().getString("email");
                            String birthday = response.getJSONObject().getString("user_birthday");
                            String friends = response.getJSONObject().getString("user_friends");

                            curr_usr = new My_Profile(Profile.getCurrentProfile());
                            System.out.println("Email: " + email);
                            System.out.println("Birthday: " + birthday);
                            System.out.println("Friends: " + friends);

                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
    }

    public void disconnectFromFacebook() {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    public void loginSuccessGUIUpdate (Profile currentProfile, TextView hello_user, TextView or_view, Button displayStats) {
        // Perform GUI update when login success
        curr_usr = new My_Profile(currentProfile);
        profileTracker.stopTracking();
        hello_user.setVisibility(View.VISIBLE);
        hello_user.setText(getString(R.string.greeting) + curr_usr.getUser().getFirstName()
                + ", you have two options: ");
        or_view.setVisibility(View.VISIBLE);
        displayStats.setVisibility(View.VISIBLE);

        displayStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switch_intent = new Intent(MainActivity.this,
                        StatsDisplay.class);
                startActivity(switch_intent);
            }
        });
    }
}