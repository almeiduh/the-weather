package com.wit.weather;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class CitiesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ContactGetter.contacts(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        setListAdapter(new CitiesAdapter(this));

        WeatherDatabase database = new WeatherDatabase(this);
        List<City> cities = database.getCities();
        database.close();

        CitiesAdapter listAdapter = (CitiesAdapter) getListAdapter();
        listAdapter.setCityList(cities);
        listAdapter.notifyDataSetChanged();
    }
}
