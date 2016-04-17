package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CurrentMarketTrendsFragment extends Fragment {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    TextView spPrice;
    TextView spPercent;
    TextView nasdaqPrice;
    TextView nasdaqPercent;
    TextView nyPrice;
    TextView nyPercent;
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_current_market_trends, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Intialize views
        spPrice = (TextView) view.findViewById(R.id.sp_price);
        spPercent = (TextView) view.findViewById(R.id.sp_percent);
        nasdaqPrice = (TextView) view.findViewById(R.id.nasdaq_price);
        nasdaqPercent = (TextView) view.findViewById(R.id.nasdaq_percent);
        nyPrice = (TextView) view.findViewById(R.id.ny_price);
        nyPercent = (TextView) view.findViewById(R.id.ny_percent);

        // Initialize the user's information to the global variable class.
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseObject object = objects.get(0);
                    UserLoginInfo.username = object.getString("username");
                    UserLoginInfo.userEmail = object.getString("email");
                    UserLoginInfo.userID = object.getString("userID");
                    UserLoginInfo.leagueNum = object.getString("leagueNum");
                } else {
                    // Error
                    Toast.makeText(getActivity(), "Error: " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {

            // Get the price, percent, and name of S&P, Nasdaq, and NYSE using Yahoo API
            List<String> resultsS = new Yahoo().execute("^GSPC").get();
            setResult(spPrice, spPercent, resultsS.get(0), resultsS.get(1), resultsS.get(2));
            List<String> resultsN = new Yahoo().execute("^IXIC").get();
            setResult(nasdaqPrice, nasdaqPercent, resultsN.get(0), resultsN.get(1), resultsN.get(2));
            List<String> resultsD = new Yahoo().execute("^NYA").get();
            setResult(nyPrice, nyPercent, resultsD.get(0), resultsD.get(1), resultsD.get(2));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Change the textViews to be the results of the stock market.
    public void setResult(TextView textViewOne, TextView textViewTwo, String fstockSymbol, String stockPrice, String fstockChangePercentage) {
        textViewOne.setText(currencyFormat.format(Float.parseFloat(stockPrice)));
        textViewTwo.setText(fstockChangePercentage + "%");
        char c = fstockChangePercentage.charAt(0);
        if (c == '+') {
            textViewTwo.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (c == '-') {
            textViewTwo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }
}
