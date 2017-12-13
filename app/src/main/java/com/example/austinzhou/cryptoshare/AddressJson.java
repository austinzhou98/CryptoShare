package com.example.austinzhou.cryptoshare;

/**
 * Created by Austin Zhou on 12/11/2017.
 */

public class AddressJson {
    private String privateKey;
    private String publicKey;
    private String address;
    private String wif;

    public AddressJson(String privateKey, String publicKey, String address, String wif) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.address = address;
        this.wif = wif;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWif() {
        return wif;
    }

    public void setWif(String wif) {
        this.wif = wif;
    }
}
