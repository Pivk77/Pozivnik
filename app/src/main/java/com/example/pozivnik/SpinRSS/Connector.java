package com.example.pozivnik.SpinRSS;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connector {
    public static Object connect(String urlAddress)
    {
        try
        {
            URL url = new URL(urlAddress);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setConnectTimeout(15000);
            con.setReadTimeout(15000);
            con.setDoInput(true);

            return con;

        } catch (IOException e) {
            e.printStackTrace();
            return ErrorTracker.CONNECTION_ERROR;
        }
    }
}
