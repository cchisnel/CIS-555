package com.capstone.hammond.wallstreetfantasyleaguefinal;


/**
 * Created by casey chisnell on 3/10/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateJoinLeagueFragment extends Fragment {


    EditText mLeagueName;
    Button mSignUpButton;
    String userSelection, LeagueName;
    private static final Logger logger = Logger.getLogger(BuyFragment.class.getName());

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mSignUpButton = (Button) view.findViewById(R.id.btnjoinleague);
        mLeagueName = (EditText) view.findViewById(R.id.txtTeamName);


        final ListView leaguesL = (ListView) view.findViewById(R.id.leagueList);





        updateLeagueList(leaguesL);

        leaguesL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //grabs league name from user selection
                userSelection = ((TextView) v).getText().toString();
                LeagueName = userSelection.substring(userSelection.indexOf("League") + 5, userSelection.indexOf(","));
            }
        });
    }

    private void updateLeagueList(ListView leaguesL) {
        // Creates and populates an ArrayList of objects from parse
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        leaguesL.setAdapter(listAdapter);

        //Queries parse for the users League information
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("UserID", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> leaguesL, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < leaguesL.size(); i++) {

                        String parseLeague = leaguesL.get(i).get("League").toString();


                        listAdapter.add(" " + parseLeague);
                    }

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                     logger.log(Level.SEVERE, e.toString());
                }
            }

        });



        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ParseUser user = new ParseUser();
                //user.put("League", leaguename);
                ParseUser user = new ParseUser().getCurrentUser();
                String leaguename = mLeagueName.getText().toString();
                user.put("League", leaguename);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast a = Toast.makeText(getActivity(), "You have joined the league.", Toast.LENGTH_LONG);
                            a.show();

                        } else {
                            Toast a = Toast.makeText(getActivity(), "You are unable to join at this time.", Toast.LENGTH_LONG);
                            a.show();
                        }
                    }
                });


            }
        });

    }



    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_create_join_league, container, false);
        return rootview;





    }


}

