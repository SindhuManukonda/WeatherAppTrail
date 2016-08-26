package openweather.openweather;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import openweather.openweather.provider.MyContentProvider;


public class MyIntentService extends IntentService {
    private static final String DEBUG_TAG = "TOPIC APP";
    //private static String DATA_URL = "http://api.openweathermap.org/data/2.5/forecast?q=Hyderabad,ind&mode=json";

    private static String DATA_URL = "http://api.openweathermap.org/data/2.5/forecast?q=Hyderabad,ind&mode=json&APPID=fb14773e8e1e835cab2c3af73bcb38ed";
    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent arg0) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Log.d(DEBUG_TAG, "HIT SERVICE");


        if (networkInfo != null && networkInfo.isConnected()) {
            // Get the data
            try {
                downloadUrl(DATA_URL);
            } catch (IOException e) {
                Log.d(DEBUG_TAG, "Unable to retrieve web page. URL may be invalid.");
            }
        } else {
            Log.d(DEBUG_TAG, "No network connections available");
        }
    }

    public void parseIt(String result) throws IOException {

        ContentResolver myCR = getContentResolver();

        // Delete all of the the existing rows in the table
        // Set parameters as follows:
        // where = "1" (2nd parameter)
        // selectionArgs = null (3rd parameter)
        int rowsDeleted = myCR.delete(MyContentProvider.CONTENT_URI, "1", null);

        Log.d(DEBUG_TAG, result);

        try {
            JSONObject resultobject = new JSONObject(result);
            String weather_list = resultobject.getString("list");
            JSONArray jarray = new JSONArray(weather_list);
            int length = jarray.length();
            for (int j = 0; j < length; j++) {
                JSONObject productObject = jarray.getJSONObject(j);
                String json_main = productObject.getString("main");
                String json_weather = productObject.getString("weather");


                String time = productObject.getString("dt_txt");
                Log.i("Log_tag", "temp1_date" + time);
                JSONObject json_temp = new JSONObject(json_main);
                JSONArray json_weathertype = new JSONArray(json_weather);
                String description = null;
                for (int i = 0; i < json_weathertype.length(); i++) {
                    JSONObject jobj_weather = json_weathertype.getJSONObject(i);
                    description = jobj_weather.getString("description");
                }
                Double temp = json_temp.getDouble("temp");
                Double pressure = json_temp.getDouble("pressure");
                int humidity = json_temp.getInt("humidity");


                Log.i("Log_tag", "temp" + temp + "description" + description + "humidity" + humidity + "\n");

                // insert record into database
                ContentValues values = new ContentValues();
                values.put(MyDBHandler.WEATHER_TEMP, temp);
                values.put(MyDBHandler.WEATHER_DESCRIPTION, description);
                values.put(MyDBHandler.WEATHER_HUMIDITY, humidity);
                values.put(MyDBHandler.WEATHER_TIME, time);

                Log.i("Log_tag", "insert data is" + values.toString());
                myCR.insert(MyContentProvider.CONTENT_URI, values);

            }                Log.d(DEBUG_TAG, "Load Data Completed");

        } catch (JSONException e) {
            String message = e.getLocalizedMessage();
            Log.d(DEBUG_TAG, "JSON Error:" + message);
            Log.d(DEBUG_TAG, "JSON Error Additional Data:" + result);
        }

    }


    /**
     * The downloadURL method gets the JSON data from the server.
     */
    private void downloadUrl(String myURL) throws IOException {
        HttpURLConnection conn = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(myURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);                         // milliseconds
            conn.setConnectTimeout(15000);                      // milliseconds
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            inputStream = conn.getInputStream();
            String contentAsString = readIt(inputStream);


            // Parse the data
            try {
                parseIt(contentAsString);
            } catch (IOException e) {
                Log.d(DEBUG_TAG, "Error parsing JSON");
            }

            // Notify the Content Provider of the change
            getContentResolver().notifyChange(MyContentProvider.CONTENT_URI, null);

        } finally {
            // This was not in the Connecting to the Network tutorial code sample
            // but the HttpURLConnection manual page indicates we should probably do this
            if (conn != null) {
                conn.disconnect();
            }

            // This can actually throw an IOException,
            // so close the HttpURLConnection first.
            if (inputStream != null) {
                try {
                    inputStream.close();

                } catch (IOException e) {
                    Log.e(DEBUG_TAG, "Error closing InputStream");
                }
            }
        }
    }

    public String readIt(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        inputStream.close();
        return result;
    }
}


