package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.AsyncTask;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class Yahoo extends AsyncTask<String, Void, List<String>> {

        protected List<String> doInBackground(String... symbols) {
            try {
                URL url;

                try {
                    url = new URL(
                            "http://download.finance.yahoo.com/d/quotes.csv?s="
                                    + symbols[0] + "&f=nl1p2");

                    InputStream stream = url.openStream();

                    BufferedInputStream bis = new BufferedInputStream(stream);
                    ByteArrayBuffer baf = new ByteArrayBuffer(50);

                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }

                    String stockTxt = new String(baf.toByteArray());

                    String[] tokens = stockTxt.split(",");

                    String stockSymbol = tokens[0];
                    String stockPrice = tokens[1];
                    String stockChangePercentage = tokens[2];

                    String fstockSymbol = stockSymbol.substring(1,
                            stockSymbol.length() - 1);
                    String fstockChangePercentage = stockChangePercentage
                            .substring(1, stockChangePercentage.length() - 3);

                    List<String> results = new ArrayList<String>();
                    results.add(fstockSymbol);
                    results.add(stockPrice);
                    results.add(fstockChangePercentage);

                    return results;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

