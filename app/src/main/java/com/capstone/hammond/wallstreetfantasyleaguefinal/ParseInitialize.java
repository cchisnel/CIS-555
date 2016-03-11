package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.app.Application;

import android.content.Context;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Mike on 11/22/2015.
 */
public class ParseInitialize extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "e0jDgZMTybwJtXDkxuTJh2UtVQkf6sjIEPQCWHbW", "KduZ6N2UXxFOsCXXgrUCXsBGbwzzYJZKAmm2qLTi");
    }

}
