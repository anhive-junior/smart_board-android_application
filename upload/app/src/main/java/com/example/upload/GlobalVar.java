package com.example.upload;


import android.app.Application;

import java.util.HashMap;

public class GlobalVar extends Application {
    public String Address;
    public String IP;
    public String Port;
    public String Rest;
    private HashMap<String, String> playworkdata;

    public String getMyAddr(){
        return Address;
    }

    public String getMyRest(){
        return Rest;
    }

    public void setMyAddr(String ip, String port, String rest){
        this.Address = "http://" + ip + ":" + port;
        this.IP = ip;
        this.Port = port;
        this.Rest = rest;
    }

    public String getMyIP(){
        return IP;
    }

    public String getMyPort(){
        return Port;
    }


    public HashMap<String, String> getPlayworkdata() {
        return this.playworkdata;
    }

    public void setPlayworkdata(HashMap playworkdata){
        this.playworkdata = playworkdata;
    }


}