package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ScheduleFragment extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_schedule, container, false);
        return rootview;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            ListView scheduleL = (ListView) view.findViewById(R.id.scheduleList);

            String[] scheduleItems = new ScheduleTask().execute().get();

            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, scheduleItems);

            scheduleL.setAdapter(listAdapter);


        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(ExecutionException e) {
            e.printStackTrace();
        }




    }

    public class ScheduleTask extends AsyncTask<Void, Void, String[]> {
        List<Integer> scheduleList;
        ResultSet rs,rs1,rs2,rs3,rs4,rs5,rs6;
        Statement st,st1,st2,st3,st4,st5,st6;
        Connection conn;
        int a,b,c,d,e,f,g,h,i,j,k,l,m;
        String oppFirst = null;
        String oppLast = null;

        @Override
        protected String[] doInBackground(Void... params) {

            try {
                conn = null;
                rs = null;
                st = null;
                if (conn != null)
                    st = conn.createStatement();
                if (st != null)
                    rs = st.executeQuery("SELECT * FROM L1_SCHEDULE WHERE USERID = '" + UserLoginInfo.userID + "'");
                if (rs != null)
                    while(rs.next()) {
                        a = (rs.getInt("WEEK_1_OPP"));
                        b = (rs.getInt("WEEK_2_OPP"));
                        c = (rs.getInt("WEEK_3_OPP"));
                        d = (rs.getInt("WEEK_4_OPP"));
                        e = (rs.getInt("WEEK_5_OPP"));
                        f = (rs.getInt("WEEK_6_OPP"));
                        g = (rs.getInt("WEEK_7_OPP"));
                        h = (rs.getInt("WEEK_8_OPP"));
                        i = (rs.getInt("WEEK_9_OPP"));
                        j = (rs.getInt("WEEK_10_OPP"));
                        k = (rs.getInt("WEEK_11_OPP"));
                        l = (rs.getInt("WEEK_12_OPP"));
                        m = (rs.getInt("WEEK_13_OPP"));

                    }

                int[] scheduleA = {a,b,c,d,e,f,g,h,i,j,k,l,m};
                String[] scheduleB = new String[13];

                for(int i=0; i<13; i++) {
                    rs1 = null;
                    st1 = null;
                    if (conn != null)
                        st1 = conn.createStatement();
                    if (st1 != null)
                        rs1 = st1.executeQuery("SELECT * FROM USERINFO WHERE USERID=" + scheduleA[i] );
                    if (rs1 != null)
                        while (rs1.next()) {
                            oppFirst = rs1.getString("FIRSTNAME");
                            oppLast = rs1.getString("LASTNAME");
                        }
                    scheduleB[i] = ("Week " + (i+1) + ":       You vs. " + oppFirst + " " + oppLast);
                }

                return scheduleB;


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

    }


}
