package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BuyFragment extends Fragment {     //Combine both buy and sell tabs here, do more investigation at certain stocks causing a crash

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_buy, container, false);
        return rootview;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                    logger.log(Level.SEVERE,e.toString());
                }
            }
        });


        getQuote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    List<String> results = new Yahoo().execute(setSymbol.getText().toString()).get();
                    setResult(results.get(0), results.get(1), results.get(2),symbolOut,priceOut,changePercentageOut);
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
                String line = shares.getText().toString();
                String pattern = "[0-9]+";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(line);
                if(m.find()) {     //Checks that share amount is comprised of only letters
                    try {
                        BuyStock();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else{
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

        }
        catch(NumberFormatException e)
        {
            Toast a = Toast.makeText(getActivity(), "Tick symbol entered wasn't found. Please try again.", Toast.LENGTH_LONG);
            a.show();
        }
    }

    public void BuyStock() throws SQLException {

        try {
            List<String> results = new Yahoo().execute(tick.getText().toString()).get();
            float mSharePrice = Float.parseFloat(results.get(1));
            float mBankAmt = Float.parseFloat(bankS);
            float mShareNum = Float.parseFloat(shares.getText().toString());
            final float mNewBank = mBankAmt - (mSharePrice * mShareNum);

            String mTick = tick.getText().toString().toUpperCase();
            String mShares = shares.getText().toString();

            if (mSharePrice*(Float.parseFloat(mShares))>(mBankAmt)) {
                Toast b = Toast.makeText(getActivity(),"You do not have sufficient funds. Enter a different amount.", Toast.LENGTH_LONG);
                b.show();
            } else {

                boolean test;

                test = new CheckTask(mTick).execute().get();

                if (test==true) {
                    new BuyTask(mTick, mShares, mNewBank, mSharePrice, mBankAmt, mShareNum).execute();
                }
                else {
                    new BuyTask2(mTick, mShares, mNewBank, mSharePrice, mBankAmt, mShareNum).execute();
                }

                //updates DB to reflect the stock transaction for the user (may need to be changed)
               /*ParseObject stocks = new ParseObject("Stocks");
                stocks.put("TickerSymbol",symbolOut.getText().toString());
                stocks.put("StockPrice",priceOut.getText().toString());
                stocks.put("NumberofStocks",mShareNum);
                stocks.put("UserID" ,ParseUser.getCurrentUser().getObjectId());
                stocks.saveInBackground();// possibly implement callback here for error checking*/

                //Resets fields after clicking Buy once
                bank.setText("Your bank: " + currencyFormat.format(mNewBank));
                tick.setText("");
                shares.setText("");
                symbolOut.setText("");
                setSymbol.setText("");

                //updates user bank account balance after purchasing stocks
                ParseUser user = new ParseUser().getCurrentUser();
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast a = Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG);
                            a.show();
                        }
                        else{
                            logger.log(Level.SEVERE, e.toString());
                            Toast a = Toast.makeText(getActivity(), "Transaction was not successful.", Toast.LENGTH_LONG);
                            a.show();
                        }


                    }
                });
            }

        }catch(InterruptedException e) {
            e.printStackTrace();
        }catch(ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static class BuyTask extends AsyncTask<Void, Void, Void> {

        float newBank;
        float sharePrice;
        float bankAmt;
        float shareNum;

        String tick, shares;

        BuyTask(String mTick, String mShares, float mNewBank, float mSharePrice, float mBankAmt, float mShareNum) {
            tick = mTick;
            shares = mShares;
            newBank = mNewBank;
            sharePrice = mSharePrice;
            bankAmt = mBankAmt;
            shareNum = mShareNum;

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String statement1 = "UPDATE L" + UserLoginInfo.leagueNum + "_STANDINGS SET BANK = '" + newBank + "' WHERE EMAIL = '" + UserLoginInfo.userEmail + "'";

                String statement2 = "INSERT INTO L" + UserLoginInfo.leagueNum + "_STOCKS(userid,email,ticker_symbol,num_shares) VALUES" +
                        " (" + UserLoginInfo.userID + ",'" + UserLoginInfo.userEmail + "','" + tick +
                        "'," + shares + ")";
                return null;

            } catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static class BuyTask2 extends AsyncTask<Void, Void, Void> {
        Connection conn = null;
        ResultSet rs = null;
        Statement st = null;
        Statement statement_1 = null;
        Statement statement_2 = null;

        float newBank;
        float sharePrice;
        float bankAmt;
        float shareNum;
        float temp;
        float newShareNum;

        String tick, shares;

        BuyTask2(String mTick, String mShares, float mNewBank, float mSharePrice, float mBankAmt, float mShareNum) {
            tick = mTick;
            shares = mShares;
            newBank = mNewBank;
            sharePrice = mSharePrice;
            bankAmt = mBankAmt;
            shareNum = mShareNum;

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                conn = null;
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STOCKS WHERE USERID ='" + UserLoginInfo.userID + "' AND TICKER_SYMBOL='" + tick + "'");
                if (rs != null)
                    while (rs.next()) {
                        temp = Float.parseFloat(rs.getString("NUM_SHARES"));
                    }

                newShareNum = temp + (Float.parseFloat(shares));

                String statement1 = "UPDATE L" + UserLoginInfo.leagueNum + "_STANDINGS SET BANK = '" + newBank + "' WHERE EMAIL = '" + UserLoginInfo.userEmail + "'";

                String statement2 = "UPDATE L" + UserLoginInfo.leagueNum + "_STOCKS SET NUM_SHARES=" + newShareNum + "WHERE TICKER_SYMBOL='" + tick + "'";

                statement_1 = conn.createStatement();
                statement_2 = conn.createStatement();

                statement_1.execute(statement1);
                statement_2.execute(statement2);

                return null;

            } catch(SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public static class CheckTask extends AsyncTask<Void, Void, Boolean> {
        ResultSet rs;
        Statement st;
        Connection conn;
        String ticker;

        CheckTask(String mTick) {
            ticker = mTick;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<String> list = new ArrayList<>();

            try {
                conn = null;
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L" + UserLoginInfo.leagueNum + "_STOCKS WHERE USERID ='" + UserLoginInfo.userID + "' AND TICKER_SYMBOL='" + ticker + "'");
                if (rs != null)
                    while (rs.next()) {
                        list.add(rs.getString("TICKER_SYMBOL"));
                    }

                if(list.isEmpty()) {
                    return true;
                }

                else return false;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (st != null)
                        st.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}



