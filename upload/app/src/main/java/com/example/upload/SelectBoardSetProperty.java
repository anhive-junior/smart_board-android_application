package com.example.upload;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.upload.Login.UPLOAD_URL;
import static com.example.upload.UpLoadImage.getStringImage;

public class SelectBoardSetProperty extends AppCompatActivity {
    public class device{
        private String name;

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
    private EditText boardName;
    private TextView box;
    private TextView router;
    private EditText pwd;
    private Button setComplete;
    private ArrayList<String[]> property = new ArrayList<>();
    private ProgressDialog loading;

    private SharedPreferences appData;//로그인정보 저장매체

    private WifiManager wifi;
    private WifiScanAdapter boxAdapter;
    private WifiScanAdapter routerAdapter;
    private List<ScanResult> wifiData;
    private List<device> wifiList = new ArrayList<>();
    private List<device> boxList = new ArrayList<>();
    private List<device> routerList = new ArrayList<>();
    private int netCount=0;
    private RecyclerView recyclerBox;
    private RecyclerView recyclerSsid;
    private static String TAG="WifiFragment";
    private String boxName;
    private String boxSecurity;
    private String routerName;
    private String routerSecurity;
    private int temp;

    private String UPLOAD_URL = "http://192.168.201.1:80/signage/s00_signage.php";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.selectboardsetproperty);
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        property.add(new String[]{"func", "apsetting"});

        setComplete = (Button) findViewById(R.id.button_set);
        boardName = (EditText) findViewById(R.id.editText_boardname);
        pwd = (EditText) findViewById(R.id.editText_pwd);
        box = (TextView) findViewById(R.id.textView_box);
        router = (TextView) findViewById(R.id.textView_ssid);

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()){
            Toast.makeText(this, "Wifi enabling...", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }


        System.out.println(wifi.getConnectionInfo().getSSID());
        System.out.println(" ");

        //register Broadcast receiver
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                wifiData=wifi.getScanResults();
                netCount=wifiData.size();
                // wifiScanAdapter.notifyDataSetChanged();
                Log.d("Wifi","Total Wifi Network"+netCount);
            }
        },new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        boxAdapter = new WifiScanAdapter(boxList, getApplicationContext());
        routerAdapter = new WifiScanAdapter(routerList, getApplicationContext());

        recyclerBox = (RecyclerView) findViewById(R.id.recycler_box);
        recyclerBox.setLayoutManager(new LinearLayoutManager(this));
        recyclerBox.setAdapter(boxAdapter);

        recyclerSsid = (RecyclerView) findViewById(R.id.recycler_ssid);
        recyclerSsid.setLayoutManager(new LinearLayoutManager(this));
        recyclerSsid.setAdapter(routerAdapter);

        wifi.startScan();
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxList.clear();
                try {
                    temp=netCount -1;
                    while (temp>=0){
                        device d= new device();
                        d.setName(wifiData.get(temp).SSID);
                        d.setCapabilities(wifiData.get(temp).capabilities);
                        boxList.add(d);
                        boxAdapter.notifyDataSetChanged();
                        temp-=1;
                    }
                }
                catch (Exception e){
                    Log.d("Wifi", e.getMessage());
                }
            }
        });

        router.setOnClickListener(new View.OnClickListener() {
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

        boxAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final device d=(device)v.findViewById(R.id.ssid_name).getTag();
                System.out.println(d.getName());
                box.setText(d.getName());

                boxName = d.getName();
                boxSecurity = d.getCapabilities();
                boxList.clear();
                boxAdapter.notifyDataSetChanged();
            }
        });

        routerAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final device d=(device)v.findViewById(R.id.ssid_name).getTag();
                System.out.println(d.getName());
                router.setText(d.getName());

                routerName = d.getName();
                routerSecurity = d.getCapabilities();
                routerList.clear();
                routerAdapter.notifyDataSetChanged();

            }
        });

        setComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectBoard.boardList.contains(boardName.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectBoardSetProperty.this);
                    builder.setMessage("중복된 이름은 사용할 수 없습니다");
                    builder.show();
                }else{
                    Intent intent = new Intent();
                    System.out.println(boardName.getText().toString());
                    intent.putExtra("boardName", boardName.getText().toString());
                    setResult(1234, intent);


                    SharedPreferences.Editor editor = appData.edit();
                    editor.putInt("NUMBER OF BOARD", SelectBoard.boardNum + 1);
                    editor.putString("BOARD_" + (SelectBoard.boardNum + 1), boardName.getText().toString());
                    editor.putString(Server.result, boardName.getText().toString() + "_IP");
                    editor.putString(boardName.getText().toString() + "_PORT", "80");
                    editor.putString(boardName.getText().toString() + "_REST", "/signage/s00_signage.php");
                    editor.apply();

                    finish();
                    /*
                    connectWiFi(boxName, "DD79019089", boxSecurity);
                    while (!("\"" + boxName + "\"").equals(wifi.getConnectionInfo().getSSID()));
                    System.out.println("test11");

                    //System.out.println("success");

                    loading = ProgressDialog.show(SelectBoardSetProperty.this, "Uploading Image", "Please wait...",true,true);
                    sendinfo();*/
                }

            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //finish();
        /*
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }*/
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if( event.getAction() == KeyEvent.ACTION_DOWN ){ //키 다운 액션 감지
            if( keyCode == KeyEvent.KEYCODE_BACK ){ //BackKey 다운일 경우만 처리
                //BackKey 이벤트일 경우 해야할 코드 작성
                finish();

                return true; // 리턴이 true인 경우 기존 BackKey의 기본액션이 그대로 행해 지게 됩니다.
                // 리턴을 false로 할 경우 기존 BackKey의 기본액션이 진행 되지 않습니다.
                // 따라서 별도의 종료처리 혹은 다이얼로그 처리를 통한
                //BackKey기본액션을 구현 해주셔야 합니다.
            }
        }
        return super.onKeyDown( keyCode, event );
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
                connectWiFi(routerName, pwd.getText().toString(), routerSecurity);
                while (!("\"" + routerName + "\"").equals(wifi.getConnectionInfo().getSSID()));
                new Thread(new Server()).start();
                while(Server.result == null);

                SharedPreferences preferences = getSharedPreferences("appData", MODE_PRIVATE);
                if(preferences.getString(boardName.getText().toString() + "_IP", "") != ""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectBoardSetProperty.this);
                    builder.setMessage("이미 연결된 액자입니다");
                    builder.show();
                }else{
                    SharedPreferences.Editor editor = appData.edit();
                    editor.putInt("NUMBER OF BOARD", SelectBoard.boardNum + 1);
                    editor.putString("BOARD_" + (SelectBoard.boardNum + 1), boardName.getText().toString());
                    editor.putString(Server.result, boardName.getText().toString() + "_IP");
                    editor.putString(boardName.getText().toString() + "_PORT", "80");
                    editor.putString(boardName.getText().toString() + "_REST", "/signage/s00_signage.php");
                    editor.apply();

                    Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
                    loading.dismiss();
                    finish();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                property.add(new String[]{"ap", "\"" + router.getText().toString() + "\""});
                property.add(new String[]{"pass", "\"" + pwd.getText().toString() + "\""});
                String result = rh.sendPostRequest(UPLOAD_URL, property);

                return result;
            }
        }

        //new Process().execute(rotatedBitmap);
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
