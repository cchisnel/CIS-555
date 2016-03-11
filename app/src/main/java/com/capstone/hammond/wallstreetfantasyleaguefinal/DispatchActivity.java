package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Created by Mike on 11/22/2015.
 */
public class DispatchActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check to see if the user is logged in. If they are, then proceed to the home page. If not, then proceed to login.
        if(ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, NavDrawer.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }


    }
}
