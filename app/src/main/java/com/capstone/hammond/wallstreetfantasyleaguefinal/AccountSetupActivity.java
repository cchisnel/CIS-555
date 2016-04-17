package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountSetupActivity extends ActionBarActivity {

    EditText mUserName;
    EditText mEmail;
    EditText mPassword;
    EditText mConfirmPassword;
    Button cancelButton;
    Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        //Initialize views
        mUserName = (EditText) findViewById(R.id.username);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountSetupActivity.this, LoginActivity.class));
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptNewUser();
            }

        });
    }

    public void attemptNewUser() {
        // Store values at the time of the login attempt.
        String username = mUserName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        // Reset errors.
        mEmail.setError(null);
        mPassword.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUserName.setError("Username field can't be blank");
            focusView = mUserName;
            cancel = true;
        } else if (!isUserNameValid(username)) {
            mUserName.setError("Username must be at least six characters");
            focusView = mUserName;
            cancel = true;
        }

        //Ensures username can't contain special characters
        String line = username;
        String pattern = "^[a-zA-Z0-9]*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (!m.find()) {
            mUserName.setError("Username may only contain letters and numbers, no special characters");
            focusView = mUserName;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPassword.setError("Password is required to be at least six characters in length");
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid confirm password.
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError("Password is required to be at least six characters in length");
            focusView = mConfirmPassword;
            cancel = true;
        } else if (!isConfirmPasswordValid(password, confirmPassword)) {
            mConfirmPassword.setError("Both passwords must match");
            focusView = mConfirmPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email address field can't be blank");
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError("Email address must contain a @ symbol");
            focusView = mEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            Toast a = Toast.makeText(AccountSetupActivity.this, "Please wait while you are being signed up...", Toast.LENGTH_LONG);
            a.show();


            // send data to parse ========================================================================================================

            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.put("PlayerBank", 50000);


//=========================================================================================================
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Toast.makeText(AccountSetupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(AccountSetupActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private boolean isUserNameValid(String username) {
        return username.length() >= 6;
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
