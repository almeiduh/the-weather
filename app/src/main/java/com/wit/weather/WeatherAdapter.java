package com.wit.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aribeiro on 16/8/14.
 */
public class WeatherAdapter extends BaseAdapter {

    private List<Weather> weatherList;
    private Context context;

    public WeatherAdapter(Context context) {
        this.context = context;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
        if (this.weatherList == null) {
            this.weatherList = new ArrayList<Weather>();
        }
    }

    @Override
    public int getCount() {
        return getWeatherList().size();
    }

    @Override
    public Weather getItem(int i) {
        return getWeatherList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return getWeatherList().get(i).hashCode();
    }

    private List<Weather> getWeatherList() {
        if (weatherList == null) {
            weatherList = new ArrayList<Weather>();
        }

        return weatherList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = view;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item, viewGroup, false);
        }

        Weather item = getItem(i);

        ImageView image = (ImageView) rowView.findViewById(R.id.imageview_image);
        TextView sky = (TextView) rowView.findViewById(R.id.textview_sky);
        TextView city = (TextView) rowView.findViewById(R.id.textview_city);
        TextView description = (TextView) rowView.findViewById(R.id.textview_description);
        TextView temperature = (TextView) rowView.findViewById(R.id.textview_temperature);

        sky.setText(item.getSky());
        city.setText(item.getCity());
        description.setText(item.getDescription());
        temperature.setText(String.format("%.1f ºC",item.getTemperature()));

        Picasso.with(context).load(item.getImageUrl()).into(image);

        return rowView;
    }
}
