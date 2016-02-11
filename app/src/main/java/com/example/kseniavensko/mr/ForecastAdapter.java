package com.example.kseniavensko.mr;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by ksenia.vensko on 10.02.16.
 */
public class ForecastAdapter extends ArrayAdapter {
    private final JSONObject[] data;
    private final Context context;

    public ForecastAdapter(Context context, JSONObject[] data) {
        super(context, R.layout.row_layout, data);
            this.data = data;
            this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        JSONObject json = this.data[position];
        rowView.setTag(json.toString());
        try {
            // icon
            String main = json.getJSONArray("weather").getJSONObject(0).getString("main");
            main = main.toLowerCase().replace(" ", "_");
            ImageView iv = (ImageView) rowView.findViewById(R.id.weather_icon);
            int id = this.context
                    .getResources()
                    .getIdentifier("art_" + main, "drawable", this.context.getPackageName());
            iv.setImageResource(id);

            // date
            TextView tv = (TextView) rowView.findViewById(R.id.day);

            String day;
            if(position < 2) {
                // Today, Tomorrow
                day = (String) DateUtils.getRelativeTimeSpanString(json.getLong("dt") * 1000L,
                        System.currentTimeMillis(), // now
                        DateUtils.DAY_IN_MILLIS);
            }else{
                // Monday, Tuesday, ..., Sunday
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                day = dayFormat.format(json.getLong("dt") * 1000L);
            }

            tv.setText(day);

            // description
            tv = (TextView) rowView.findViewById(R.id.weather_description);
            tv.setText(json.getJSONArray("weather").getJSONObject(0).getString("description"));

            // temperature - max
            tv = (TextView)rowView.findViewById(R.id.temp_max);
            tv.setText(json.getJSONObject("temp").getString("max") + "°C");

            // temperature - min
            tv = (TextView)rowView.findViewById(R.id.temp_min);
            tv.setText(json.getJSONObject("temp").getString("min") + "°C");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
    }
}
