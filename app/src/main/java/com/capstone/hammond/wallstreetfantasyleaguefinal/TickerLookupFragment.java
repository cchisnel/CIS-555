package com.capstone.hammond.wallstreetfantasyleaguefinal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Dylan on 2016-04-06.
 */
public class TickerLookupFragment extends Fragment {

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ticker_lookup, container, false);
        return rootView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        WebView myWebView = (WebView) view.findViewById(R.id.tickerWebView);
        myWebView.loadUrl("http://eoddata.com/symbols.aspx");

        //Necessary so that user interaction doesn't boot to default web browser
        myWebView.setWebViewClient(new WebViewClient());
    }
}

