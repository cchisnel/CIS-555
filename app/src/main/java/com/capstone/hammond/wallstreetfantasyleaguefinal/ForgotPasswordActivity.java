package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

/**
 * Created by dylan on 3/3/2016.
 */
public class ForgotPasswordActivity extends Activity {

    View rootView;
    Button submitButton;
    Button cancelButton;
    EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        email = (EditText) findViewById(R.id.emailAddressTextbox);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String _email = email.getText().toString();

                //Validates email address provided
                if (!_email.equals("") && _email.contains("@")) {
                    passwordReset(_email);
                } else {
                    Toast a = Toast.makeText(ForgotPasswordActivity.this, "Email can't be blank and must contain an '@' sign. Please try again.", Toast.LENGTH_LONG);
                    a.show();
                }
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

    }

    private void passwordReset(String email) {
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast a = Toast.makeText(ForgotPasswordActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG);
                    a.show();
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                } else {
                    Toast a = Toast.makeText(ForgotPasswordActivity.this, "An account with this email address wasn't found. Please try again.", Toast.LENGTH_LONG);
                    a.show();
                }
            }
        });
    }

}

