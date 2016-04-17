package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.app.Application;
import com.parse.Parse;

public class ParseInitialize extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "e0jDgZMTybwJtXDkxuTJh2UtVQkf6sjIEPQCWHbW", "KduZ6N2UXxFOsCXXgrUCXsBGbwzzYJZKAmm2qLTi");
    }

}
