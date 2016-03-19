package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.net.ParseException;
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
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Dylan on 2016-03-14.
 */
public class SellFragment extends Fragment {
    View rootview;
    EditText shareAmount;
    Button sellButton;
    String tickerSymbol, userSelection;
    String numStocks;
    float currentBalance, newBalance, mSharePrice;
    private static final Logger logger = Logger.getLogger(SellFragment.class.getName());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_sell, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ListView stocksL = (ListView) view.findViewById(R.id.stockList);
        shareAmount = (EditText) view.findViewById(R.id.shareAmountTextbox);
        sellButton = (Button) view.findViewById(R.id.sellButton);

        super.onViewCreated(view, savedInstanceState);

        updateStockList(stocksL);

        stocksL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //grabs tickersymbol from user selection
                userSelection = ((TextView) v).getText().toString();
                tickerSymbol = userSelection.substring(userSelection.indexOf("Stock") + 7, userSelection.indexOf(","));
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mshareAmount = shareAmount.getText().toString();

                if (tickerSymbol == null) {
                    Toast a = Toast.makeText(getActivity(), "You must first select a stock from the list to sell. Please try again.", Toast.LENGTH_LONG);
                    a.show();
                } else {
                    //Checks that only numbers can be entered for share amount
                    String pattern = "[0-9]+";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(mshareAmount);
                    if (m.find()) {
                        try {
                            //updates bank account and the parse Stock class
                            updateStock();
                            updateAccountBalance();

                            //Reloads fragment
                            Fragment newFragment = new SellFragment();
                            android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.container, newFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();

                        } catch (ParseException e) {
                            logger.log(Level.SEVERE, e.toString());
                        }
                    } else {
                        Toast a = Toast.makeText(getActivity(), "Share amount may only contain letters. Please try again.", Toast.LENGTH_LONG);
                        a.show();
                    }
                }

            }
        });
    }

    private void updateStockList(ListView stocksL) {
        // Creates and populates an ArrayList of objects from parse
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        stocksL.setAdapter(listAdapter);

        //Queries parse for the users stock information
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stocks");
        query.whereEqualTo("UserID", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stockL, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < stockL.size(); i++) {

                        String parseTicker = stockL.get(i).get("TickerSymbol").toString();
                        String numStocks = stockL.get(i).get("NumberofStocks").toString();

                        listAdapter.add("Stock: " + parseTicker + ", Number of stocks held: " + numStocks);
                    }

                } else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    logger.log(Level.SEVERE, e.toString());
                }
            }

        });
    }

    //Takes the assumption that each stock only has one object within the parse Stock Class
    private void updateStock() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Stocks");
        query.whereEqualTo("UserID", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stockL, com.parse.ParseException e) {

                for (int i = 0; i < stockL.size(); i++) {
                    String userTicker = stockL.get(i).get("TickerSymbol").toString();
                    numStocks = stockL.get(i).getNumber("NumberofStocks").toString();

                    if (userTicker.equals(tickerSymbol)) {

                        //Subtracts number number of stocks in parse from the number the user entered
                        Integer newStockNum = Integer.valueOf(numStocks) - Integer.valueOf(shareAmount.getText().toString());

                        if (newStockNum == 0)     //removes the whole stock object from parse Stock Class
                        {
                            stockL.get(i).deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "Sell transaction was successfull.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Sell transaction was unable to be processed at this time.", Toast.LENGTH_SHORT).show();
                                        logger.log(Level.SEVERE, e.toString());
                                    }
                                }
                            });
                            break;
                        } else if (newStockNum < 0) {
                            Toast.makeText(getActivity(), "Your trying to sell more shares than you currently own. Please try again.", Toast.LENGTH_SHORT).show();
                            break;
                        } else {  //updates the users current number of stocks in parse Stock class

                            stockL.get(i).put("NumberofStocks", newStockNum);
                            stockL.get(i).saveInBackground();
                            Toast.makeText(getActivity(), "Sell transaction was successfull.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void updateAccountBalance() {

        try {
            //grabs the most current price from Yahoo Finance API
            List<String> results = new Yahoo().execute(tickerSymbol).get();
            mSharePrice = Float.parseFloat(results.get(1));
        } catch (InterruptedException e) {

        } catch (ExecutionException n) {

        }

        //updates the users current account balance to take account for the sold stock
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, com.parse.ParseException e) {
                if (e == null) {
                    float mShareAmount = Float.parseFloat(shareAmount.getText().toString());
                    currentBalance = user.getNumber("PlayerBank").floatValue();
                    newBalance = currentBalance + (mShareAmount * mSharePrice);
                    user.put("PlayerBank", newBalance);
                    user.saveInBackground();
                } else {
                    logger.log(Level.SEVERE, e.toString());
                }
            }
        });
    }
}


