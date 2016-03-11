package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class WeeklyMatchFragment extends Fragment {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_weekly_match, container, false);
        return rootview;

    }} // remove extra bracket when recreating to extend public class

    /*

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            TextView user1;
            TextView user2;
            TextView bank1;
            TextView bank2;

            List<List> weeklyMatchResults = new MatchDetails().execute().get();
            List<String> weeklyMatch = weeklyMatchResults.get(0);
            List<String> stocksOne = weeklyMatchResults.get(1);
            List<String> stocksTwo = weeklyMatchResults.get(2);

            ListView user1Stocks = (ListView) view.findViewById(R.id.user1_StockInfo);

            ListView user2Stocks = (ListView) view.findViewById(R.id.user2_StockInfo);

            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stocksOne);

            ArrayAdapter<String> listAdapterTwo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, stocksTwo);

            user1Stocks.setAdapter(listAdapter);

            user2Stocks.setAdapter(listAdapterTwo);

            user1 = (TextView) view.findViewById(R.id.player1);
            //user1.setText(UserLoginInfo.fName + " " + UserLoginInfo.lName);
            bank1 = (TextView) view.findViewById(R.id.bank1);
            bank1.setText("Bank: " + currencyFormat.format(Float.parseFloat(weeklyMatch.get(0))));
            user2 = (TextView) view.findViewById(R.id.user2);
            user2.setText(weeklyMatch.get(1) + " " + weeklyMatch.get(2));
            bank2 = (TextView) view.findViewById(R.id.bank2);
            bank2.setText("Bank: " + currencyFormat.format(Float.parseFloat(weeklyMatch.get(3))));

            weeklyMatch = null;

        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }


    }

    public class MatchDetails extends AsyncTask<Void, Void, List<List>> {
        List<List> results;
        List<String> weeklyMatch;
        List<String> userOneWeeklyStocks;
        List<String> userTwoWeeklyStocks;
        Connection conn;
        ResultSet rs,rs1,rs2,rs3,rs4;
        Statement st,st1,st2,st3,st4;

        @Override
        protected List<List> doInBackground(Void... params) {
            try {
                results =  new ArrayList<>();
                weeklyMatch = new ArrayList<>();
                userOneWeeklyStocks = new ArrayList<>();
                userTwoWeeklyStocks = new ArrayList<>();

                conn = null;
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STANDINGS WHERE EMAIL = '" + UserLoginInfo.userEmail + "'");
                if (rs != null)
                    while(rs.next()) {
                        weeklyMatch.add(rs.getString("BANK"));
                        UserLoginInfo.currOpp = rs.getInt("CURR_OPP");
                    }
                st1 = null;
                if (conn != null)
                    st1 = conn.createStatement();
                rs1 = null;
                if (st1 != null)
                    rs1 = st1.executeQuery("SELECT * FROM USERINFO WHERE USERID = '" + UserLoginInfo.currOpp + "'");
                if (rs1 != null)
                    while(rs1.next()) {
                        weeklyMatch.add(rs1.getString("FIRSTNAME"));
                        weeklyMatch.add(rs1.getString("LASTNAME"));
                    }
                rs2 = null;
                st2 = null;
                if (conn != null)
                    st2 = conn.createStatement();
                if (st2 != null)
                    rs2 = st2.executeQuery("SELECT * FROM L1_Standings WHERE USERID = '" + UserLoginInfo.currOpp + "'");
                if (rs2 != null)
                    while(rs2.next()) {
                        weeklyMatch.add(rs2.getString("BANK"));
                    }

                rs3 = null;
                st3 = null;
                if (conn != null)
                    st3 = conn.createStatement();
                if (st3 != null)
                    rs3 = st3.executeQuery("SELECT * FROM L1_STOCKS WHERE USERID = '" + UserLoginInfo.userID + "'");
                if (rs3 != null)
                    while(rs3.next()) {
                        String tick = (rs3.getString("TICKER_SYMBOL"));
                        String share = (rs3.getString("NUM_SHARES"));
                        userOneWeeklyStocks.add("Owns " + share + " shares of " + tick.toUpperCase());
                    }

                rs4 = null;
                st4 = null;
                if (conn != null)
                    st4 = conn.createStatement();
                if (st4 != null)
                    rs4 = st4.executeQuery("SELECT * FROM L1_STOCKS WHERE USERID = '" + UserLoginInfo.currOpp + "'");
                if (rs4 != null)
                    while(rs4.next()) {
                        String tick = (rs4.getString("TICKER_SYMBOL"));
                        String share = (rs4.getString("NUM_SHARES"));
                        userTwoWeeklyStocks.add("Owns " + share + " shares of " + tick.toUpperCase());

                    }

                results.add(weeklyMatch);
                results.add(userOneWeeklyStocks);
                results.add(userTwoWeeklyStocks);

                return results;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(rs!=null)
                        rs.close();
                    if(rs1!=null)
                        rs1.close();
                    if(rs2!=null)
                        rs2.close();
                    if(st!=null)
                        st.close();
                    if(st1!=null)
                        st1.close();
                    if(st2!=null)
                        st2.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }
    }
}



*/