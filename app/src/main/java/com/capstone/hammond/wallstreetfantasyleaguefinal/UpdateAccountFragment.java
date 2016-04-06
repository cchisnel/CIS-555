package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dylan on 2/26/2016.
 */
public class UpdateAccountFragment extends android.support.v4.app.Fragment {

    EditText emailAddress;
    EditText password1;
    EditText password2;
    Button updateEmail;
    Button updatePassword;
    Button accountScreen;
    View rootView;
    private static final Logger logger = Logger.getLogger(UpdateAccountFragment.class.getName());


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_update_account,container, false);
        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailAddress = (EditText) view.findViewById(R.id.updateEmailTextbox);
        password1 = (EditText) view.findViewById(R.id.newPasswordTextbox);
        password2 = (EditText) view.findViewById(R.id.newPassword2Textbox);
        updateEmail = (Button) view.findViewById(R.id.updateEmailButton);
        updatePassword = (Button) view.findViewById(R.id.updatePasswordButton);
        accountScreen = (Button) view.findViewById(R.id.accountScreenButton);


        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _emailAddress = emailAddress.getText().toString();
                boolean retValue = _emailAddress.contains("@");
                if(retValue){
                    ParseUser user = ParseUser.getCurrentUser();
                    user.setEmail(_emailAddress);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast a = Toast.makeText(getActivity(), "Your email address has been updated.", Toast.LENGTH_LONG);
                                a.show();

                            } else {
                                Toast a = Toast.makeText(getActivity(), "Your email address does not contain a valid domain name.", Toast.LENGTH_LONG);
                                a.show();
                            }
                        }
                    });
                }
                else{
                    Toast a = Toast.makeText(getActivity(), "Your email address doesn't contain the '@' symbol. Please try again.", Toast.LENGTH_LONG);
                    a.show();
                }


            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _password1 = password1.getText().toString();
                String _password2 = password2.getText().toString();

                if(_password1.equals(_password2) && _password1.length() >= 6){
                    ParseUser user = ParseUser.getCurrentUser();
                    user.setPassword(_password1);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast a = Toast.makeText(getActivity(), "Your password was updated successfully.", Toast.LENGTH_LONG);
                                a.show();

                            } else {
                                logger.log(Level.SEVERE,e.toString());
                                Toast a = Toast.makeText(getActivity(), "Your password was unable to be reset at this time.", Toast.LENGTH_LONG);
                                a.show();
                            }
                        }
                    });
                }
                else{

                    Toast a = Toast.makeText(getActivity(), "Passwords don't meet the length requirements or match.", Toast.LENGTH_LONG);
                    a.show();
                }


            }
        });

        accountScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AccountFragment();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



    }


}
