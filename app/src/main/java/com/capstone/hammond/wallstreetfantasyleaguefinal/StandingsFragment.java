package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class StandingsFragment extends Fragment {
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    List<Integer> idList = new ArrayList<>();
    List<String> winList = new ArrayList<>();
    List<String> lossList = new ArrayList<>();
    List<String> firstName = new ArrayList<>();
    List<String> lastName = new ArrayList<>();
    List<String> tieList = new ArrayList<>();
    List<String> winPercentList = new ArrayList<>();


    int resultID;
    String resultWins;
    String resultLosses;
    String resultTies;
    String resultWP;
    String firstNameS;
    String lastNameS;

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_standings, container, false);
        return rootview;

    }


}

    /*

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            List<List> standingsResult = new Standings().execute().get();

            firstName = standingsResult.get(0);
            lastName = standingsResult.get(1);
            winList = standingsResult.get(2);
            lossList = standingsResult.get(3);
            tieList = standingsResult.get(4);
            winPercentList = standingsResult.get(5);

            TextView player1;
            TextView player2;
            TextView player3;
            TextView player4;
            TextView player5;
            TextView player6;

            TextView player1Record;
            TextView player2Record;
            TextView player3Record;
            TextView player4Record;
            TextView player5Record;
            TextView player6Record;

            TextView player1WP;
            TextView player2WP;
            TextView player3WP;
            TextView player4WP;
            TextView player5WP;
            TextView player6WP;

            player1 = (TextView) view.findViewById(R.id.player1);
            player1.setText("1. " + firstName.get(0) + " " + lastName.get(0));
            player2 = (TextView) view.findViewById(R.id.player2);
            player2.setText("2. " + firstName.get(1) + " " + lastName.get(1));
            player3 = (TextView) view.findViewById(R.id.player3);
            player3.setText("3. " + firstName.get(2) + " " + lastName.get(2));
            player4 = (TextView) view.findViewById(R.id.player4);
            player4.setText("4. " + firstName.get(3) + " " + lastName.get(3));
            player5 = (TextView) view.findViewById(R.id.player5);
            player5.setText("5. " + firstName.get(4) + " " + lastName.get(4));
            player6 = (TextView) view.findViewById(R.id.player6);
            player6.setText("6. " + firstName.get(5) + " " + lastName.get(5));

            player1Record = (TextView) view.findViewById(R.id.player1_record);
            player1Record.setText(winList.get(0) + "-" + lossList.get(0) + "-" + tieList.get(0));
            player2Record = (TextView) view.findViewById(R.id.player2_record);
            player2Record.setText(winList.get(1) + "-" + lossList.get(1) + "-" + tieList.get(1));
            player3Record = (TextView) view.findViewById(R.id.player3_record);
            player3Record.setText(winList.get(2) + "-" + lossList.get(2) + "-" + tieList.get(2));
            player4Record = (TextView) view.findViewById(R.id.player4_record);
            player4Record.setText(winList.get(3) + "-" + lossList.get(3) + "-" + tieList.get(3));
            player5Record = (TextView) view.findViewById(R.id.player5_record);
            player5Record.setText(winList.get(4) + "-" + lossList.get(4) + "-" + tieList.get(4));
            player6Record = (TextView) view.findViewById(R.id.player6_record);
            player6Record.setText(winList.get(5) + "-" + lossList.get(5) + "-" + tieList.get(5));

            float p1wp = Float.parseFloat(winPercentList.get(0));
            float p2wp = Float.parseFloat(winPercentList.get(1));
            float p3wp = Float.parseFloat(winPercentList.get(2));
            float p4wp = Float.parseFloat(winPercentList.get(3));
            float p5wp = Float.parseFloat(winPercentList.get(3));
            float p6wp = Float.parseFloat(winPercentList.get(5));

            player1WP = (TextView) view.findViewById(R.id.player1_wperc);
            player1WP.setText(percentFormat.format(p1wp));
            player2WP = (TextView) view.findViewById(R.id.player2_wperc);
            player2WP.setText(percentFormat.format(p2wp));
            player3WP = (TextView) view.findViewById(R.id.player3_wperc);
            player3WP.setText(percentFormat.format(p3wp));
            player4WP = (TextView) view.findViewById(R.id.player4_wperc);
            player4WP.setText(percentFormat.format(p4wp));
            player5WP = (TextView) view.findViewById(R.id.player5_wperc);
            player5WP.setText(percentFormat.format(p5wp));
            player6WP = (TextView) view.findViewById(R.id.player6_wperc);
            player6WP.setText(percentFormat.format(p6wp));

            standingsResult = null;



        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }




    }

    public class Standings extends AsyncTask<Void, Void, List<List>> {
        List<List> standingsResult;
        ResultSet rs,rs1,rs2,rs3,rs4,rs5,rs6;
        Statement st,st1,st2,st3,st4,st5,st6;
        Connection conn;

        @Override
        protected List<List> doInBackground(Void... params) {

            List<List> standingsResult;

            try {
                conn = null;
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_STANDINGS");
                if (rs != null)
                    while(rs.next()) {
                        resultID = rs.getInt("USERID");
                        idList.add(resultID);
                        resultWins = rs.getString("WINS");
                        winList.add(resultWins);
                        resultLosses = rs.getString("LOSSES");
                        lossList.add(resultLosses);
                        resultTies = rs.getString("TIES");
                        tieList.add(resultTies);
                        resultWP = rs.getString("WIN_PERCENT");
                        winPercentList.add(resultWP);
                    }

                st1 = null;
                if (conn != null)
                    st1 = conn.createStatement();
                rs1 = null;
                if (st1 != null)
                    rs1 = st1.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(0)));
                if (rs1 != null)
                    while(rs1.next()) {
                        firstNameS = rs1.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs1.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st2 = null;
                if (conn != null)
                    st2 = conn.createStatement();
                rs2 = null;
                if (st2 != null)
                    rs2 = st2.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(1)));
                if (rs2 != null)
                    while(rs2.next()) {
                        firstNameS = rs2.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs2.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st3 = null;
                if (conn != null)
                    st3 = conn.createStatement();
                rs3 = null;
                if (st3 != null)
                    rs3 = st3.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(2)));
                if (rs3 != null)
                    while(rs3.next()) {
                        firstNameS = rs3.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs3.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st4 = null;
                if (conn != null)
                    st4 = conn.createStatement();
                rs4 = null;
                if (st4 != null)
                    rs4 = st4.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(3)));
                if (rs4 != null)
                    while(rs4.next()) {
                        firstNameS = rs4.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs4.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st5 = null;
                if (conn != null)
                    st5 = conn.createStatement();
                rs5 = null;
                if (st5 != null)
                    rs5 = st5.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(4)));
                if (rs5 != null)
                    while(rs5.next()) {
                        firstNameS = rs5.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs5.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }
                st6 = null;
                if (conn != null)
                    st6 = conn.createStatement();
                rs6 = null;
                if (st6 != null)
                    rs6 = st6.executeQuery("SELECT * FROM USERINFO WHERE USERID = " + (idList.get(5)));
                if (rs6 != null)
                    while(rs6.next()) {
                        firstNameS = rs6.getString("FIRSTNAME");
                        firstName.add(firstNameS);
                        lastNameS = rs6.getString("LASTNAME");
                        lastName.add(lastNameS);
                    }

                standingsResult = new ArrayList<>();
                standingsResult.add(firstName);
                standingsResult.add(lastName);
                standingsResult.add(winList);
                standingsResult.add(lossList);
                standingsResult.add(tieList);
                standingsResult.add(winPercentList);

                return standingsResult;

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
                    if(rs3!=null)
                        rs3.close();
                    if(rs4!=null)
                        rs4.close();
                    if(rs5!=null)
                        rs5.close();
                    if(rs6!=null)
                        rs6.close();
                    if(st!=null)
                        st.close();
                    if(st1!=null)
                        st1.close();
                    if(st2!=null)
                        st2.close();
                    if(st3!=null)
                        st3.close();
                    if(st4!=null)
                        st4.close();
                    if(st5!=null)
                        st5.close();
                    if(st6!=null)
                        st6.close();

                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<List> lists) {
            super.onPostExecute(lists);

            standingsResult = null;
        }
    }
}



*/