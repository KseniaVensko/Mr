package com.example.kseniavensko.mr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String[] forecast = {
                "today - one, two, three, four",
                "tomorrow - one, two, three, four",
                "friday - one, two, three, four",
                "saturday - one, two, three, four",
                "sunday - one, two, three, four"
        };

        String data = getIntent().getStringExtra("city");
        try {
            JSONObject json = new JSONObject(data);

            JSONArray jsonArray = json.getJSONArray("list");

            ArrayList<JSONObject> list = new ArrayList<>();
            for (int i = 0; i < 5; ++i) {
                list.add(jsonArray.getJSONObject(i));
            }
            // ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.weather_description, forecast);

            ForecastAdapter adapter = new ForecastAdapter(this, list.toArray(new JSONObject[list.size()]));
            ListView lv = (ListView) findViewById(R.id.listView);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), String.format("Position %d", position), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("data", (String)view.getTag());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
