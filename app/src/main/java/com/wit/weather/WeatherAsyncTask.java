package com.wit.weather;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WeatherAsyncTask
        extends AsyncTask<String, Integer, List<Weather>> {

    private WeakReference<Activity> context;
    private static final int BUFFER_SIZE = 1024;
    private final byte[] buffer = new byte[BUFFER_SIZE];

    public WeatherAsyncTask(Activity context) {
        this.context = new WeakReference<Activity>(context);
    }

    @Override
    protected List<Weather> doInBackground(String... places) {

        List<Weather> weatherList = new ArrayList<Weather>();

        if (places != null && places.length > 0) {

            int progress = 0;

            for (int i1 = 0; i1 < places.length; i1++) {
                String place = places[i1];

                URL url;
                HttpURLConnection urlConnection = null;
                try {

                    String urlString = null;

                    String encodedPlace = URLEncoder.encode(place, "UTF-8");

                    Context theContext = context.get();
                    if (theContext != null) {
                        urlString = theContext.getString(R.string.weather);
                        urlString = String.format(urlString, encodedPlace);
                    }

                    url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    ByteArrayOutputStream writer = new ByteArrayOutputStream();

                    int bytesRead;
                    while ((bytesRead = in.read(buffer, 0, buffer.length)) > 0) {
                        writer.write(buffer, 0, bytesRead);
                    }

                    String jsonString = new String(writer.toByteArray());

                    in.close();

                    JSONObject jsonObject = new JSONObject(jsonString);

                    double temperature = 0;

                    if (jsonObject.has("main")) {
                        temperature = jsonObject.getJSONObject("main").getDouble("temp");
                    }

                    if (jsonObject.has("weather")) {
                        JSONArray weatherJsonArray = jsonObject.getJSONArray("weather");

                        int nrOfWeathers = weatherJsonArray.length();
                        for (int i = 0; i < nrOfWeathers; i++) {

                            JSONObject weatherJsonObject = weatherJsonArray.getJSONObject(i);

                            Weather weather = new Weather();
                            weather.setDescription(weatherJsonObject.getString("description"));
                            weather.setSky(weatherJsonObject.getString("main"));
                            weather.setImageUrl(String.format(context.get().getString(R.string.icon),
                                    weatherJsonObject.getString("icon")));

                            weather.setCity(place);
                            // convert to celsius
                            weather.setTemperature(temperature - 273.15);

                            weatherList.add(weather);
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

                progress = (i1 + 1) * 100 / places.length;
                publishProgress(progress);

            }
        }

        return weatherList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Activity activity = context.get();
        if (activity != null) {
            ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressbar);
            progressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(List<Weather> weathers) {
        super.onPostExecute(weathers);
        Activity activity = context.get();
        if (activity != null) {
            ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressbar);
            progressBar.setProgress(0);

            ListActivity listActivity = (ListActivity) activity;

            WeatherAdapter adapter = new WeatherAdapter(listActivity);
            adapter.setWeatherList(weathers);

            WeatherAdapter listAdapter = (WeatherAdapter) listActivity.getListAdapter();
            listAdapter.setWeatherList(weathers);
            listAdapter.notifyDataSetChanged();

            Intent sendBroadcast = new Intent("com.wit.weather.action.UPDATE_FINISHED");
            activity.sendBroadcast(sendBroadcast);
        }
    }
}
