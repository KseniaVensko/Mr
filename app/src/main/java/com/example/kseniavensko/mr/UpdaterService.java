package com.example.kseniavensko.mr;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?cnt=5&units=metric&APPID=3718d7f90e7b081ca8f46aa4305c05ea&q=%s";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
           // String city = this.getSharedPreferences("sk.tuke.smart.mrilko", MODE_PRIVATE).getString("city", "kosice");
            String city = "Kosice";
            URL url = new URL(String.format(WEATHER_URL, city ));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();

            Log.i(TAG, "Connecting to " + url);
            Log.i(TAG, "Status: " + connection.getResponseCode());

            // if result differs from HTTP status code 200 (OK), quit
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Response code is not OK");
                return;
            }

            // get the result as String
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + '\n');
            }

            // show the result in log
            Log.i(TAG, String.format("GET: %s", stringBuilder.toString()));

            // the magic goes here

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}