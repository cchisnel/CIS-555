package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;



public class AccountFragment extends Fragment {
    View rootview;
    Button updateAccount;
    String bankAmount;
    TextView username;
    TextView email;
    TextView bankBalance;
    private static final Logger logger = Logger.getLogger(UpdateAccountFragment.class.getName());
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_account, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //Initialize views
        updateAccount = (Button)  view.findViewById(R.id.updateInfo);
        username = (TextView) view.findViewById(R.id.user_name);
        email = (TextView)view.findViewById(R.id.user_email);
        bankBalance = (TextView) view.findViewById(R.id.bankamount);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");       //queries parse for current account balance
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    bankAmount = user.getNumber("PlayerBank").toString();
                    bankBalance.setText("Bank balance: $" + bankAmount);
                    email.setText("Email: " + user.getString("email"));
                } else {
                    logger.log(Level.SEVERE, e.toString());
                }
            }
        });

        username.setText("Username: " + UserLoginInfo.username);        //sets the additional textboxes within the fragment

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new UpdateAccountFragment();
                android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

       /* getView().setFocusableInTouchMode(true);        //forces back button to
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });*/

    }

}
