package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BuyFragment extends Fragment {

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    public static String bankS = "0000";
    private static final Logger logger = Logger.getLogger(BuyFragment.class.getName());
    EditText setSymbol;
    EditText tick;
    EditText shares;
    TextView symbolOut;
    TextView priceOut;
    TextView changePercentageOut;
    TextView bank;
    Button getQuote;
    Button buyStock;
    View rootview;
    boolean tickerCheck = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_buy, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Intialize views
        setSymbol = (EditText) view.findViewById(R.id.setSymbol);
        symbolOut = (TextView) view.findViewById(R.id.stockSymbolOutput);
        priceOut = (TextView) view.findViewById(R.id.stockPriceOutput);
        changePercentageOut = (TextView) view.findViewById(R.id.stockChangePriceOutput);
        getQuote = (Button) view.findViewById(R.id.get_quote_button);
        tick = (EditText) view.findViewById(R.id.buyStockName);
        shares = (EditText) view.findViewById(R.id.buySharesAmount);
        buyStock = (Button) view.findViewById(R.id.btnBuyStock);
        bank = (TextView) view.findViewById(R.id.bankTextbox);

        //Retrieves the users current account balance
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    bankS = user.getNumber("PlayerBank").toString();
                    bank.setText("Bank balance: $" + bankS);
                } else {
                    logger.log(Level.SEVERE, e.toString());
                }
            }
        });


        getQuote.setOnClickListener(new View.OnClickListener() {

            //Retrieves stock information via Yahoo API and calls setResult
            @Override
            public void onClick(View v) {
                try {
                    List<String> results = new Yahoo().execute(setSymbol.getText().toString()).get();
                    setResult(results.get(0), results.get(1), results.get(2), symbolOut, priceOut, changePercentageOut);
                    tick.setText((setSymbol.getText().toString()).toUpperCase());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        buyStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks that share amount is comprised of only letters
                String line = shares.getText().toString();
                String pattern = "[0-9]+";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(line);
                if (m.find()) {
                    try {
                        BuyStock();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast a = Toast.makeText(getActivity(), "Share amount may only contain letters. Please try again.", Toast.LENGTH_LONG);
                    a.show();
                }
            }
        });


    }

    public void setResult(String fstockSymbol, String stockPrice, String fstockChangePercentage, TextView symbolOut, TextView priceOut, TextView changePercentageOut) {
        try {
            symbolOut.setText(fstockSymbol);
            priceOut.setText(currencyFormat.format(Float.parseFloat(stockPrice)));
            changePercentageOut.setText(fstockChangePercentage + "%");
            char c = fstockChangePercentage.charAt(0);
            if (c == '+') {
                changePercentageOut.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            } else if (c == '-') {
                changePercentageOut.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }

        } catch (NumberFormatException e) {
            Toast a = Toast.makeText(getActivity(), "Tick symbol entered wasn't found. Please try again.", Toast.LENGTH_LONG);
            a.show();
        }
    }


    public void BuyStock() throws SQLException {

        try {
            List<String> results = new Yahoo().execute(tick.getText().toString()).get();
            float mSharePrice = Float.parseFloat(results.get(1));
            float mBankAmt = Float.parseFloat(bankS);
            final float mShareNum = Float.parseFloat(shares.getText().toString());
            final float mNewBank = mBankAmt - (mSharePrice * mShareNum);
            final String mTick = tick.getText().toString().toUpperCase();
            final String mShares = shares.getText().toString();
            final String stockPrice = priceOut.getText().toString();

            if (mSharePrice * (Float.parseFloat(mShares)) > (mBankAmt)) {
                Toast b = Toast.makeText(getActivity(), "You do not have sufficient funds. Enter a different amount.", Toast.LENGTH_LONG);
                b.show();
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Stocks");
                query.whereEqualTo("UserID", ParseUser.getCurrentUser().getObjectId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> stockL, com.parse.ParseException e) {
                        //Searches parse Stock class for a matching ticker symbol, appends new stock number to object if found
                        for (int i = 0; i < stockL.size(); i++) {

                            String userTicker = stockL.get(i).get("TickerSymbol").toString();

                            if (userTicker.equals(mTick)) {
                                Integer numStocks = Integer.valueOf(stockL.get(i).get("NumberofStocks").toString());
                                Integer newStockNum = Integer.valueOf(mShares) + numStocks;
                                stockL.get(i).put("NumberofStocks", newStockNum);
                                stockL.get(i).saveInBackground();
                                tickerCheck = true;
                                break;
                            }
                        }

                        //Creates a new stock object for the user in the parse Stock class if matching ticker symbol isn't found
                        if (tickerCheck == false) {
                            ParseObject stocks = new ParseObject("Stocks");
                            stocks.put("TickerSymbol", mTick);
                            stocks.put("StockPrice", stockPrice);
                            stocks.put("NumberofStocks", mShareNum);
                            stocks.put("UserID", ParseUser.getCurrentUser().getObjectId());
                            stocks.saveInBackground();
                        }
                    }
                });

                //Resets fields after clicking Buy once
                bank.setText("Your bank: " + currencyFormat.format(mNewBank));
                tick.setText("");
                shares.setText("");
                symbolOut.setText("");
                setSymbol.setText("");
                priceOut.setText("");
                changePercentageOut.setText("");

                //updates user bank account balance after purchasing stocks
                ParseUser user = new ParseUser().getCurrentUser();
                user.put("PlayerBank", mNewBank);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast a = Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG);
                            a.show();
                        } else {
                            logger.log(Level.SEVERE, e.toString());
                            Toast a = Toast.makeText(getActivity(), "Transaction was not successful.", Toast.LENGTH_LONG);
                            a.show();
                        }
                    }
                });
                // Reload Fragment
                Fragment fragmentObject = new BuyFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(fragmentObject);
                ft.attach(fragmentObject);
                ft.commit();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}



