package com.android.facebooklogintest;

/**
 * Created by Vincent Wu on 3/30/2018.
 */

import com.facebook.Profile;

public class My_Profile {

    private Profile user;
    private String email;

    public My_Profile(Profile user) {
        this.user = user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }

    public Profile getUser() {
        return this.user;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
