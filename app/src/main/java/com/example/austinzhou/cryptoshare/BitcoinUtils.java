package com.example.austinzhou.cryptoshare;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;


/**
 * Created by Austin Zhou on 11/1/2017.
 */

public class BitcoinUtils extends AsyncTask<String, Integer, Long> {

    private final String TAG = "Balance: ";

    @Override
    protected Long doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);

            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream inStream = connection.getInputStream();
            InputStreamReader inStreamReader = new InputStreamReader(inStream, Charset.forName("UTF-8"));

            Gson gson = new Gson();
            JsonElement element = gson.fromJson (inStreamReader, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();

            Log.e("yo",jsonObj.toString());

            return jsonObj.getAsJsonPrimitive("balance").getAsLong();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        //change the balance
        Log.e("YO: ",result.toString());
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}