package com.android.facebooklogintest;

/**
 * Created by Vincent Wu on 3/30/2018.
 */

import com.facebook.Profile;

public class My_Profile {

    private Profile user;

    public My_Profile(Profile user) {
        this.user = user;
    }

    public Profile getUser() {
        return this.user;
    }

    // In the case where we have to change users manually?
    public void setUser(Profile user) {
        this.user = user;
    }
}
