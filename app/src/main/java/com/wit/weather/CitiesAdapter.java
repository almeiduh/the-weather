package com.wit.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aribeiro on 17/8/14.
 */
public class CitiesAdapter extends BaseAdapter {

    private List<City> cityList;
    private Context context;

    public CitiesAdapter(Context context) {
        this.context = context;
    }

    private List<City> getCityList() {
        if (cityList == null) {
            cityList = new ArrayList<City>();
        }
        return cityList;
    }

    @Override
    public int getCount() {
        return getCityList().size();
    }

    @Override
    public City getItem(int i) {
        return getCityList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = view;

        if (rowView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, viewGroup, false);

        }
        City item = getItem(i);

        CheckedTextView textView = (CheckedTextView) rowView.findViewById(android.R.id.text1);

        WeatherDatabase database = new WeatherDatabase(context);
        boolean subscribed = database.isCitySubscribed(item.getName());
        database.close();

        rowView.setTag(item.getName());
        textView.setChecked(subscribed);
        textView.setText(item.getName());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cityName = (String) view.getTag();
                CheckedTextView ctv = (CheckedTextView) view;

                boolean subscribe = !ctv.isChecked();
                ctv.setChecked(subscribe);

                WeatherDatabase database = new WeatherDatabase(context);
                database.saveCitySubscription(cityName, subscribe);
                database.close();

            }
        });

        return rowView;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
