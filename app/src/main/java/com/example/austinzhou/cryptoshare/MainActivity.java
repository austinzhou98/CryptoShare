package com.example.austinzhou.cryptoshare;

import android.content.Context;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.json.JSONObject;
import org.spongycastle.asn1.util.Dump;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.spec.ECParameterSpec;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "Crypto: ";
    private String key = "cSEGLQDrimNFDT5jN36XGi7QVmeimbN68ZLjfDW9MaCrAVbyksmh";

    private final String url = "https://api.blockcypher.com/v1/btc/test3/addrs/";
    private final String addresGenUrl = "https://api.blockcypher.com/v1/btc/test3/addrs";
    private final String txnurl = "https://api.blockcypher.com/v1/btc/test3/txs/new";
    private final String sendtxn = "https://api.blockcypher.com/v1/btc/test3/txs/send?token=" + ApiKey.key;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.e(TAG,Transaction.sign("cMswthSmmcXnncqoSnXfWxU81HmnrbNW5XvN1hjcAaBAd9iUCM5i","cbd0ab68a88504fc50328a6647e06d8ef51ae9848b30fc1dcba92b9fa29befae"));
        Log.e(TAG, Coin.COIN.toFriendlyString());

       //addressBalance("mxixd2F8cFBSPEcmpEFjQpUpFcKUBnKCgc");
        //newAddress();

        AddressJson sender = new AddressJson(
                "08df2ce43caea677749f281a107066f4803fd6cc56e0cd8da1ff1b96719f4781",
                "026ceea3b8e930f867f205fcaffdd8dc3b9da852c2db2cd6eaf341fb399e6acd1c",
                "mxixd2F8cFBSPEcmpEFjQpUpFcKUBnKCgc",
                "cMswthSmmcXnncqoSnXfWxU81HmnrbNW5XvN1hjcAaBAd9iUCM5i");

        AddressJson receiver = new AddressJson(
                "6e11a5d51a519117eb6b08308389b8f2f36a639cc5912d7f333140ff0ba4c133",
                "03086a868f58d6358be029f1df0077532b05b2b6ee056bdd1945011de15ec74fb4",
                "muvPLvCncjKrbVDqKdQMQqgCgSuMuWRwjr",
                "cRGfJmPKwnywBLQed15FCfHnTSnNsb5416vceNcZiUKoHhG1r5zk");

        sendTransaction(sender, receiver, 1000000);

    }

    public void addressBalance(String address){
        Ion.with(this)
                .load(url + address + "/balance")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e(TAG, result.toString());
                    }
                });
    }

    public void newAddress(){
        Ion.with(this)
                .load(addresGenUrl)
                .setJsonObjectBody(new JsonObject())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        AddressJson address = new AddressJson(
                                result.getAsJsonPrimitive("private").getAsString(),
                                result.getAsJsonPrimitive("public").getAsString(),
                                result.getAsJsonPrimitive("address").getAsString(),
                                result.getAsJsonPrimitive("wif").getAsString());
                        Log.e(TAG, address.getPrivateKey());
                        Log.e(TAG, address.getPublicKey());
                        Log.e(TAG, address.getAddress());
                        Log.e(TAG, address.getWif());
                    }
                });
    }

    //curl -d '{"inputs":[{"addresses": ["CEztKBAYNoUEEaPYbkyFeXC5v8Jz9RoZH9"]}],"outputs":[{"addresses": ["C1rGdt7QEPGiwPMFhNKNhHmyoWpa5X92pn"], "value": 1000000}]}' https://api.blockcypher.com/v1/bcy/test/txs/new

    public void sendTransaction(final AddressJson sender, final AddressJson receiver, final long value){
        JsonObject json = new JsonObject();

        JsonArray inputs = new JsonArray();
        JsonObject inputAddresses = new JsonObject();
        JsonArray inAddresses = new JsonArray();
        inAddresses.add(sender.getAddress());
        inputAddresses.add("addresses",inAddresses);
        inputs.add(inputAddresses);
        json.add("inputs",inputs);

        JsonArray outputs = new JsonArray();
        JsonObject outputAddresses = new JsonObject();
        JsonArray outAddresses = new JsonArray();
        outAddresses.add(receiver.getAddress());
        outputAddresses.add("addresses",outAddresses);
        outputAddresses.addProperty("value", value);
        outputs.add(outputAddresses);
        json.add("outputs",outputs);


        Log.e(TAG,json.toString());
        Ion.with(this).load(txnurl).setJsonObjectBody(json).asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String signme = result.get("tosign").getAsJsonArray().get(0).getAsString();
                        String signed = Transaction.sign(sender.getWif(),signme);
                        Log.e(TAG, signed);
                        JsonArray signatures = new JsonArray();
                        signatures.add(signed);
                        JsonArray publickeys = new JsonArray();
                        publickeys.add(sender.getPublicKey());
                        result.add("signatures",signatures);
                        result.add("pubkeys",publickeys);
                        Log.e(TAG,result.toString());
                        Ion.with(MainActivity.this).load(sendtxn).setJsonObjectBody(result).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Log.e(TAG,result.toString());
                            }
                        });
                    }
                });
    }

}
