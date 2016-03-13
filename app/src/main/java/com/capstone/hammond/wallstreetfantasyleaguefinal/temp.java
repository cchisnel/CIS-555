package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class temp extends Fragment {
   /* private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    Button sellStock;
    Button search;
    EditText tick;
    EditText shares;
    EditText setSymbol;
    TextView bankView;
    static String bankS;
    TextView symbolOut;
    TextView priceOut;
    TextView changePercentageOut;
    private static final Logger logger = Logger.getLogger(BuyFragment.class.getName());
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_sell, container, false);
        return rootview;
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sellStock = (Button) view.findViewById(R.id.sellStockButton);
        tick = (EditText) view.findViewById(R.id.sellStockName);
        shares = (EditText) view.findViewById(R.id.sellSharesAmount);
        bankView = (TextView) view.findViewById(R.id.bankTextbox);
        search = (Button) view.findViewById(R.id.searchButton);
        setSymbol = (EditText) view.findViewById(R.id.setSymbolTextbox);
        symbolOut = (TextView) view.findViewById(R.id.symbolOutputTextbox);
        priceOut = (TextView) view.findViewById(R.id.priceOutputTextbox);
        changePercentageOut = (TextView) view.findViewById(R.id.stockChangeTextbox);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");               //Displays account balance for user
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user, com.parse.ParseException e) {
                if (e == null) {
                    bankS = user.getNumber("PlayerBank").toString();
                    bankView.setText("Bank balance: $" + bankS);
                } else {
                    logger.log(Level.SEVERE, e.toString());
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() { //grabs and sets the price, name, and change for the specified stock
            @Override
            public void onClick(View v) {

                try {
                    List<String> results = new Yahoo().execute(setSymbol.getText().toString()).get();

                    BuyFragment fragment = new BuyFragment();
                    fragment.setResult(results.get(0), results.get(1), results.get(2),setSymbol,priceOut, changePercentageOut);
                    tick.setText((setSymbol.getText().toString()).toUpperCase());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        sellStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    sellStockM();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /*try{
        List<String> results = new SellableStocks().execute().get();

        ListView userStocks = (ListView) view.findViewById(R.id.userStocks);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, results);

        userStocks.setAdapter(listAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("L" + UserLoginInfo.leagueNum + "_Standings");
        query.whereEqualTo("userID", Integer.parseInt(UserLoginInfo.userID));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseObject object = objects.get(0);
                    BuyFragment.bankS = object.getString("bank");
                } else {
                    // Error
                }
            }
        });

        bankView.setText("Your bank: " + (currencyFormat.format(Float.parseFloat(BuyFragment.bankS))));

    }catch(InterruptedException e) {
        e.printStackTrace();
    }catch(ExecutionException e) {
        e.printStackTrace();
    }

    public void sellStockM() throws SQLException {

        try {

            List<String> results = new Yahoo().execute(tick.getText().toString()).get();
            float mSharePrice = Float.parseFloat(results.get(1));
            float mBankAmt = Float.parseFloat(bankS);
            float mShareNum = Float.parseFloat(shares.getText().toString());
            float mNewBank = mBankAmt - (mSharePrice * mShareNum);

            String mTick = tick.getText().toString().toUpperCase();
            String mShares = shares.getText().toString();

            boolean test,test2;

            test = new BuyFragment.CheckTask(mTick).execute().get();

            test2 = new CheckTask2(mShares, mTick).execute().get();


            if(test==true) {
                Toast c = Toast.makeText(getActivity(), "You do not have any stocks from that company", Toast.LENGTH_LONG);
                c.show();
            }

            if(test2==true) {
                Toast d = Toast.makeText(getActivity(),"You do not have that many stocks from that company",Toast.LENGTH_LONG);
                d.show();
            }
            else {

                new SellTask(mTick, mShares, mNewBank, mSharePrice, mBankAmt, mShareNum).execute();

                bankView.setText("Your bank: " + currencyFormat.format(mNewBank));

                tick.setText("");

                shares.setText("");

                Toast a = Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG);
                a.show();

                //Need to update users bank account info within parse

                /*List<String> results2 = new SellableStocks().execute().get();

                ListView userStocks = (ListView)rootview.findViewById(R.id.user1_StockInfo);

                ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, results2);

                userStocks.setAdapter(listAdapter);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("L" + UserLoginInfo.leagueNum + "_Standings");
                query.whereEqualTo("userID", Integer.parseInt(UserLoginInfo.userID));
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            ParseObject object = objects.get(0);
                            BuyFragment.bankS = object.getString("bank");
                        } else {
                            // Error
                        }
                    }
                });

                //bankView.setText("Your bank: " + (currencyFormat.format(Float.parseFloat(BuyFragment.bankS))));

            }



        }catch(InterruptedException e) {
            e.printStackTrace();
        }catch(ExecutionException e) {
            e.printStackTrace();
        }

    }

    /*public class SellableStocks extends AsyncTask<Void, Void, List<String>> {
        List<String> results;
        Connection conn;
        ResultSet rs;
        Statement st;

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                results =  new ArrayList<>();

                conn = null;
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L" + UserLoginInfo.leagueNum + "_STOCKS WHERE USERID = '" + UserLoginInfo.userID + "'");
                if (rs != null)
                    while(rs.next()) {
                        String tick = (rs.getString("TICKER_SYMBOL"));
                        String share = (rs.getString("NUM_SHARES"));
                        results.add(share + " shares of " + tick.toUpperCase());
                    }

                return results;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(rs!=null)
                        rs.close();
                    if(st!=null)
                        st.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }


    public static class SellTask extends AsyncTask<Void, Void, Void> {
        Connection conn = null;
        ResultSet rs;
        Statement st;
        Statement statement_1 = null;
        Statement statement_2 = null;

        float newBank;
        float sharePrice;
        float bankAmt;
        float shareNum;
        float temp;
        float newShareNum;

        String tick, shares;

        SellTask(String mTick, String mShares, float mNewBank, float mSharePrice, float mBankAmt, float mShareNum) {
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
                    rs = st.executeQuery("SELECT * FROM L" + UserLoginInfo.leagueNum + "_STOCKS WHERE USERID ='" + UserLoginInfo.userID + "' AND TICKER_SYMBOL='" + tick + "'");
                if (rs != null)
                    while (rs.next()) {
                        temp = Float.parseFloat(rs.getString("NUM_SHARES"));
                    }

                String statement1 = "UPDATE L" + UserLoginInfo.leagueNum + "_Standings SET BANK = '" + newBank + "' WHERE EMAIL = '" + UserLoginInfo.userEmail + "'";

                newShareNum = temp - (Float.parseFloat(shares));

                String statement2 = null;

                if(newShareNum==0) {
                    statement2 = "DELETE FROM L" + UserLoginInfo.leagueNum + "_STOCKS WHERE TICKER_SYMBOL='" + tick + "'";
                } else {
                    statement2 = "UPDATE L" + UserLoginInfo.leagueNum + "_STOCKS SET NUM_SHARES =" + newShareNum + " WHERE TICKER_SYMBOL='" + tick + "' AND USERID=" + UserLoginInfo.userID;
                }

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

    public static class CheckTask2 extends AsyncTask<Void, Void, Boolean> {
        ResultSet rs;
        Statement st;
        Connection conn;
        String mNShares, ticker;

        CheckTask2(String shareNum, String tickerS) {
            mNShares = shareNum;
            ticker = tickerS;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<String> list = new ArrayList<>();
            String nShares;
            nShares = null;

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
                        nShares = (rs.getString("NUM_SHARES"));
                    }

                if((Float.parseFloat(mNShares))>(Float.parseFloat(nShares))) {
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
    }*/



}

































