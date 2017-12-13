package com.example.austinzhou.cryptoshare;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;


import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;


/**
 * Created by Austin Zhou on 12/11/2017.
 */

public class Transaction extends AsyncTask<String, Integer, Long> {

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private final String TAG = "Transaction: ";

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



    public static String sign(String wif, String tosign){
        ECKey key = DumpedPrivateKey.fromBase58(null, wif).getKey();
        Sha256Hash hash = Sha256Hash.wrap(tosign);

        // creating signature
        ECKey.ECDSASignature sig = key.sign(hash);

        // encoding
        byte[] res = sig.encodeToDER();

        return bytesToHex(res);

    }
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
