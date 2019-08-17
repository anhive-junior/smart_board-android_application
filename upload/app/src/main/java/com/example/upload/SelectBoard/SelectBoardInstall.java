package com.example.upload.SelectBoard;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.R;
import com.example.upload.Util.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class SelectBoardInstall extends AppCompatActivity {
    public static class device{
        private String name;

        device(){
        }

        device(String name){
            this.name = name;
        }

        public String getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(String capabilities) {
            this.capabilities = capabilities;
        }

        private String capabilities;

        public void setName(String name) {
            this.name = name;
        }

        public String getName (){
            return name;
        }
    }
    private TextView boardSSID;
    private TextView routerSSID;
    private EditText boardPWD;
    private EditText routerPWD;
    private Button setComplete;
    private ArrayList<String[]> property = new ArrayList<>();
    private ProgressDialog loading;
    private WifiManager wifi;
    private WifiScanAdapter boardAdapter;
    private WifiScanAdapter routerAdapter;
    private List<ScanResult> wifiData;
    private List<device> boardList = new ArrayList<>();
    private List<device> routerList = new ArrayList<>();
    private int netCount=0;
    private RecyclerView recyclerBoard;
    private RecyclerView recyclerSSID;
    private static String TAG="WifiFragment";
    private String boardName;
    private String boardSecurity;
    private int temp;

    private ConnectivityManager cm;
    private boolean isWiFi;
    private NetworkInfo activeNework;

    private String UPLOAD_URL = "http://192.168.201.1:80/signage/s00_signage.php";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.selectboardinstall);

        property.add(new String[]{"func", "apsetting"});

        boardPWD = (EditText) findViewById(R.id.board_pwd);
        routerPWD = (EditText) findViewById(R.id.router_pwd);
        boardSSID = (TextView) findViewById(R.id.board_ssid);
        routerSSID = (TextView) findViewById(R.id.router_ssid);
        setComplete = (Button) findViewById(R.id.button_set);

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()){
            Toast.makeText(this, "Wifi enabling...", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                wifiData=wifi.getScanResults();
                netCount=wifiData.size();
                Log.d("Wifi","Total Wifi Network"+netCount);
            }
        },new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        boardAdapter = new WifiScanAdapter(boardList, getApplicationContext());
        routerAdapter = new WifiScanAdapter(routerList, getApplicationContext());

        recyclerBoard = (RecyclerView) findViewById(R.id.recycler_board);
        recyclerBoard.setLayoutManager(new LinearLayoutManager(this));
        recyclerBoard.setAdapter(boardAdapter);

        recyclerSSID = (RecyclerView) findViewById(R.id.recycler_router);
        recyclerSSID.setLayoutManager(new LinearLayoutManager(this));
        recyclerSSID.setAdapter(routerAdapter);

        wifi.startScan();
        boardSSID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardList.clear();
                try {
                    temp=netCount -1;
                    while (temp>=0){
                        device d= new device();
                        d.setName(wifiData.get(temp).SSID);
                        d.setCapabilities(wifiData.get(temp).capabilities);
                        boardList.add(d);
                        boardAdapter.notifyDataSetChanged();
                        temp-=1;
                    }
                }
                catch (Exception e){
                    Log.d("Wifi", e.getMessage());
                }
            }
        });

        routerSSID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routerList.clear();
                try {
                    temp=netCount -1;
                    while (temp>=0){
                        device d= new device();
                        d.setName(wifiData.get(temp).SSID);
                        d.setCapabilities(wifiData.get(temp).capabilities);
                        routerList.add(d);
                        routerAdapter.notifyDataSetChanged();
                        temp-=1;
                    }
                }
                catch (Exception e){
                    Log.d("Wifi", e.getMessage());
                }
            }
        });

        boardAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final device d=(device)v.findViewById(R.id.ssid_name).getTag();
                System.out.println(d.getName());
                boardSSID.setText(d.getName());

                boardName = d.getName();
                boardSecurity = d.getCapabilities();
                boardList.clear();
                boardAdapter.notifyDataSetChanged();
            }
        });

        routerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final device d=(device)v.findViewById(R.id.ssid_name).getTag();
                System.out.println(d.getName());
                routerSSID.setText(d.getName());

                //routerName = d.getName();
                //routerSecurity = d.getCapabilities();
                routerList.clear();
                routerAdapter.notifyDataSetChanged();

            }
        });

        setComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(SelectBoardInstall.this, "Uploading Image", "Please wait...",true,true);

                connectWiFi(boardName, boardPWD.getText().toString(), boardSecurity);

                do{
                    try{
                        activeNework = cm.getActiveNetworkInfo();
                        isWiFi = activeNework.getType() == ConnectivityManager.TYPE_WIFI;
                    }catch (Exception e){
                        isWiFi = false;
                    }
                    System.out.println(wifi.getConnectionInfo().getSSID());
                    System.out.println(isWiFi);
                }while(!("\"" + boardName + "\"").equals(wifi.getConnectionInfo().getSSID()) && isWiFi);

                sendinfo();
            }
        });
    }

    private void sendinfo(){
        class Process extends AsyncTask<Void,Void,String> {
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                System.out.println(s);
                loading.dismiss();
                finish();
                /*
                if(s.equals("Error Registering")){
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    return;
                }*/
            }

            @Override
            protected String doInBackground(Void... params) {
                property.add(new String[]{"ap", "\"" + routerSSID.getText().toString() + "\""});
                property.add(new String[]{"pass", "\"" + routerPWD.getText().toString() + "\""});

                String result = "Error Registering";

                try {
                    result = rh.sendPostRequest(UPLOAD_URL, property);
                    return result;
                }catch (Exception e){
                    return result;
                }

            }
        }
        new Process().execute();
    }

    private void connectWiFi(String SSID, String password, String Security) {
        try {

            Log.d(TAG, "Item clicked, SSID " + SSID + " Security : " + Security);

            String networkSSID = SSID;
            String networkPass = password;

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            if (Security.toUpperCase().contains("WEP")) {
                Log.v("rht", "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (networkPass.matches("^[0-9a-fA-F]+$")) {
                    conf.wepKeys[0] = networkPass;
                } else {
                    conf.wepKeys[0] = "\"".concat(networkPass).concat("\"");
                }

                conf.wepTxKeyIndex = 0;

            } else if (Security.toUpperCase().contains("WPA")) {
                Log.v(TAG, "Configuring WPA");

                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                conf.preSharedKey = "\"" + networkPass + "\"";

            } else {
                Log.v(TAG, "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);

            Log.v(TAG, "Add result " + networkId);

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    Log.v(TAG, "WifiConfiguration SSID " + i.SSID);

                    boolean isDisconnected = wifiManager.disconnect();
                    Log.v(TAG, "isDisconnected : " + isDisconnected);

                    boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                    Log.v(TAG, "isEnabled : " + isEnabled);

                    boolean isReconnected = wifiManager.reconnect();
                    Log.v(TAG, "isReconnected : " + isReconnected);

                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
