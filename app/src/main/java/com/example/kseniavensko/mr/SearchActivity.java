package com.example.kseniavensko.mr;
// curl

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//import sk.tuke.smart.mrilko.R;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    //   private static final String URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&APPID=%s&q=%s";
    private static final String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?cnt=5&units=metric&APPID=%s&q=%s";
    private static final String APPID = "3718d7f90e7b081ca8f46aa4305c05ea";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "received message");
        }
    };

    private void onConnectionChange() {
        Button button = (Button) findViewById(R.id.searchButton);
        if (isNetworkAvailable()) {
            //enable
            button.setEnabled(true);
        }
        else {
            // disable
            button.setEnabled(false);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        //filter.addAction("com.example.kseniavensko.mr.MESSAGE");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(this.receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.unregisterReceiver(this.receiver);
    }

    public class ForecastAsyncTask extends AsyncTask<String, Void, JSONObject> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.progress = new ProgressDialog(SearchActivity.this);
            this.progress.setMessage("Searching...");
            this.progress.show();
        }

        @Override
        protected JSONObject doInBackground(String... cities) {
            try {
                URL url = new URL(String.format(URL, APPID, cities[0]));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.i(TAG, "Connecting to " + url);
                Log.i(TAG, "Status: " + connection.getResponseCode());

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                // get the result as String
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + '\n');
                }

                // show the result in log
                //              Log.i(TAG, String.format("GET: %s", stringBuilder.toString()));

                // return the result as JSONObject
                return new JSONObject(stringBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            this.progress.dismiss();

            if (jsonObject == null) {
                Toast.makeText(getApplicationContext(), "Some error ocured", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                // TODO: refactor this
                intent.putExtra("city", jsonObject.toString());
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public void onSearchClicked(View view) {
        if (isNetworkAvailable() == false) {
            Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText input = (EditText) findViewById(R.id.input);
        String query = input.getText().toString();

        if (query.isEmpty()) {
            Toast.makeText(this,
                    "No city provided - nothing to search",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ForecastAsyncTask task = new ForecastAsyncTask();
        task.execute(query);
    }
}