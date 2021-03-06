package com.capstone.hammond.wallstreetfantasyleaguefinal;


/**
 * Created by Casey Chisnell on 3/10/2016.
 */


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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateJoinLeagueFragment extends Fragment {

    View rootview;
    EditText mLeagueName;
    Button mSignUpButton;
    String userSelection, LeagueName;
    private static final Logger logger = Logger.getLogger(BuyFragment.class.getName());


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_create_join_league, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        final ListView leaguesL = (ListView) view.findViewById(R.id.leagueList);
        mSignUpButton = (Button) view.findViewById(R.id.btnjoinleague);
        mLeagueName = (EditText) view.findViewById(R.id.txtTeamName);

        super.onViewCreated(view, savedInstanceState);

        updateLeagueList(leaguesL);

        leaguesL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //grabs league name from user selection and sets textBox accordingly
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                userSelection = ((TextView) v).getText().toString();
                LeagueName = userSelection.substring(6);
                mLeagueName.setText(LeagueName);
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser().getCurrentUser();
                final String leagueName = mLeagueName.getText().toString();

                if (leagueName.length() == 0) {

                    Toast.makeText(getActivity(), "League name must not be blank. Please try again", Toast.LENGTH_SHORT).show();
                } else {
                    String line = leagueName;
                    String pattern = "^[a-zA-Z0-9]*$";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(line);
                    if (!m.find()) {
                       mLeagueName.setError("League name can't contain special characters.");
                    }
                    else {
                        //Signs the user up for their selected league
                        user.put("League", leagueName);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast a = Toast.makeText(getActivity(), "You have joined the league.", Toast.LENGTH_LONG);
                                    a.show();
                                    ParseObject Leagues = new ParseObject("Leagues");
                                    Leagues.put("League", leagueName);
                                    Leagues.put("UserID", ParseUser.getCurrentUser().getObjectId());
                                    Leagues.saveInBackground();

                                    //Reloads fragment
                                    Fragment newFragment = new CreateJoinLeagueFragment();
                                    android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, newFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();

                                } else {
                                    Toast a = Toast.makeText(getActivity(), "You are unable to join at this time.", Toast.LENGTH_LONG);
                                    a.show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void updateLeagueList(ListView leaguesL) {
        // Creates and populates an ArrayList of objects from parse
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        leaguesL.setAdapter(listAdapter);

        //Queries parse for all Leagues
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Leagues");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> leaguesL, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < leaguesL.size(); i++) {
                        String parseLeague = leaguesL.get(i).get("League").toString();
                        listAdapter.add("League " + parseLeague);
                    }
                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    logger.log(Level.SEVERE, e.toString());
                }
            }
        });
    }
}

