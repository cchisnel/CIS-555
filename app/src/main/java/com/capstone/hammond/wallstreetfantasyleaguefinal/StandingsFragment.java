package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StandingsFragment extends Fragment {

    ListView rankNameL;
    ListView rankNumL;
    TextView leagueView;
    View rootview;
    String parseLeague;
    ArrayList<String> parseUserL = new ArrayList<>();
    ArrayList<Integer> parseStockL = new ArrayList<>();
    ArrayList<Integer> parseBankL = new ArrayList<>();
    ArrayList<Integer> arrayTotal = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_standings, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        leagueView = (TextView) view.findViewById(R.id.leagueNameView);
        rankNameL = (ListView) view.findViewById(R.id.rankListView);
        rankNumL = (ListView) view.findViewById(R.id.moneyListView);

        // Query current user's league
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    parseLeague = user.getString("League");
                    leagueView.setText(parseLeague);
                } else {
                    // error message
                }
            }
        });

        // Query User List
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("_User");
        query2.whereEqualTo("League", parseLeague); //TODO is undefined
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> userL, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userL.size(); i++) {

                        String parseUser = userL.get(i).getString("username");
                        parseUserL.add(parseUser);
                    }
                } else {
                    // error message
                }
            }
        });

        // Query bought stocks and store in array to be added
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Stock");
        query3.whereEqualTo("UserID", parseUserL);
        query3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stockL, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < stockL.size(); i++) {

                        int numStocks = stockL.get(i).getInt("NumberofStocks");
                        int stockPrice = Integer.parseInt(stockL.get(i).getString("StockPrice"));
                        int stockTotal = numStocks * stockPrice;
                        parseStockL.add(stockTotal);
                    }
                } else {
                    // error message
                }
            }
        });

        // Query playerbank
        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("_User");
        query4.whereContainedIn("username", Arrays.asList(parseUserL));
        query4.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> userL, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userL.size(); i++) {

                        int bank = userL.get(i).getInt("PlayerBank");
                        parseBankL.add(bank);
                        arrayTotal.add(parseBankL.get(i) + parseStockL.get(i));
                    }
                } else {
                    // error message
                }
            }
        });

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, parseUserL);
        final ArrayAdapter<Integer> arrayAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayTotal);
        rankNameL.setAdapter(arrayAdapter);
        rankNumL.setAdapter(arrayAdapter2);
    }
}