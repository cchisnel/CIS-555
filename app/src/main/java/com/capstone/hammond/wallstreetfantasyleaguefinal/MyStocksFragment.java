package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Dylan on 2016-03-12.
 */
public class MyStocksFragment extends Fragment {
    View rootview;
    private static final Logger logger = Logger.getLogger(MyStocksFragment.class.getName());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_my_stocks, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the listview
        //ArrayList<String> stockList = new ArrayList<String>();

        // Creates and populates an ArrayList of objects from parse
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        final ListView stocksL = (ListView) view.findViewById(R.id.stockList);
        stocksL.setAdapter(listAdapter);

        //Queries parse for the users stock information
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stocks");
        query.whereEqualTo("UserID", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stockL, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < stockL.size(); i++) {

                        String tickerSymbol = stockL.get(i).get("TickerSymbol").toString();

                        String numStocks = stockL.get(i).get("NumberofStocks").toString();

                        listAdapter.add("Stock: " + tickerSymbol + ", Number of stocks held: " + numStocks);

                    }

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    logger.log(Level.SEVERE, e.toString());
                }

            }

        });
    }
}


