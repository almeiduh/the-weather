package com.wit.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WeatherDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "weather.db";

    private static final String CREATE_TABLE = "CREATE TABLE CITY ("
            + " NAME TEXT,"
            + " SUBSCRIBED BOOLEAN"
            + ");";

    public WeatherDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);

        List<String> list = new ArrayList<String>() {{
            add("Lisboa");
            add("Coimbra");
            add("Porto");
            add("Leiria");
            add("Santarém");
            add("Aveiro");
            add("Beja");
            add("Braga");
            add("Bragança");
            add("Castelo Branco");
            add("Évora");
            add("Faro");
            add("Guarda");
            add("Portalegre");
            add("Setúbal");
            add("Viana do Castelo");
            add("Vila Real");
            add("Viseu");
        }};

        for (String s : list) {
            ContentValues v = new ContentValues();
            v.put("NAME", s);
            v.put("SUBSCRIBED", false);

            sqLiteDatabase.insert("CITY", null, v);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public List<City> getCities() {

        List<City> cities = new ArrayList<City>();

        SQLiteDatabase readableDatabase = getReadableDatabase();

        Cursor cursor = readableDatabase.query(
                "CITY",
                new String[]{"NAME", "SUBSCRIBED"},
                null, null, null, null, null);

        try {
            if (cursor != null) {

                while (cursor.moveToNext()) {

                    City city = new City();

                    city.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                    city.setSubscribed(Integer.valueOf(1).equals(cursor.getInt(cursor.getColumnIndex("SUBSCRIBED"))));
                    cities.add(city);
                }
            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }

            readableDatabase.close();
        }


        return cities;

    }

    public void saveCitySubscription(String name, boolean subscribe) {

        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("SUBSCRIBED", subscribe);

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.update("CITY", values, "NAME = ?", new String[]{name});
        writableDatabase.close();

    }

    public List<String> getSubscribedCities() {

        SQLiteDatabase readableDatabase = getReadableDatabase();

        Cursor cursor = readableDatabase.query("CITY", new String[]{"NAME"}, "SUBSCRIBED = ?", new String[]{"1"},
                null, null, null, null);

        List<String> names = new ArrayList<String>();

        try {

            if (cursor != null) {

                while (cursor.moveToNext()) {
                    names.add(cursor.getString(cursor.getColumnIndex("NAME")));
                }
            }
        } finally {
            cursor.close();
            readableDatabase.close();
        }

        return names;

    }

    public boolean isCitySubscribed(String cityName) {

        boolean subscribed = false;
        SQLiteDatabase readableDatabase = getReadableDatabase();

        Cursor cursor = readableDatabase.query("CITY", new String[]{"SUBSCRIBED"}, "NAME = ?", new String[]{cityName},
                null, null, null, null);

        try {
            if (cursor != null) {

                if (cursor.moveToNext()) {
                    subscribed = Integer.valueOf(1).equals(cursor.getInt(cursor.getColumnIndex("SUBSCRIBED")));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            readableDatabase.close();
        }

        return subscribed;

    }
}
