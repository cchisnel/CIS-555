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
    Button submit;
    Button cancel;
    EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        submit = (Button) findViewById(R.id.submitButton);
        cancel = (Button) findViewById(R.id.cancelButton);
        email = (EditText) findViewById(R.id.emailAddressTextbox);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String _email = email.getText().toString();
                ParseUser.requestPasswordResetInBackground(_email, new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast a = Toast.makeText(ForgotPasswordActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG);
                            a.show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        } else {
                            Toast a = Toast.makeText(ForgotPasswordActivity.this, "Email could not be sent at this time.", Toast.LENGTH_LONG);
                            a.show();
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

    }

}

