package com.example.upload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class LoginTest extends AppCompatActivity implements Button.OnClickListener {
    public static String UPLOAD_URL;
    private String loginUrl;
    private String UPLOAD_KEY;
    private ArrayList<String[]> property;
    private Button buttonLogin;
    private EditText edittextID;
    private EditText edittextPassword;
    private String varID;
    private String varPassword;
    private CheckBox checkboxAutologin;
    private Progress progress;

    private SharedPreferences appData;//로그인정보 저장매체
    private boolean autoLogin;//자동로그인여부
    Intent sendIntent;
    private Intent intent;
    private WifiManager wifiManager;
    private String boardName;

    private String varIP;
    private String varPort;
    private String varRest;

    void readWepConfig()
    {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> item = wifi.getConfiguredNetworks();
        int i = item.size();
        Log.d("WifiPreference", "NO OF CONFIG " + i );
        Iterator<WifiConfiguration> iter =  item.iterator();
        for(int j=0;j<i;j++){
            WifiConfiguration config = item.get(j);
            Log.d("WifiPreference", "SSID" + config.SSID);
            Log.d("WifiPreference", "PASSWORD" + config.preSharedKey);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();

        boardName = intent.getStringExtra("boardName");
        /*
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        System.out.println(wifiManager.getWifiState());
        System.out.println(" ");
        System.out.println(wifiManager.getScanResults().get(0).SSID);
        System.out.println(" ");


        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        System.out.println(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }


        //readWepConfig();
        System.out.println(wifiManager.getConfiguredNetworks());
        System.out.println(" ");
        System.out.println(wifiManager.getConnectionInfo());
        System.out.println(" ");
        System.out.println(wifiManager.getDhcpInfo());
        System.out.println(" ");*/

        /* GIve the Server some time for startup */


        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        System.out.println(1);
        load();
        System.out.println(2);

        //자동로그인
        if(autoLogin) {
            login(loginUrl, UPLOAD_KEY, varID, varPassword);
            //finish();//개발용주석처리
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test);

        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);

        checkboxAutologin = (CheckBox) findViewById(R.id.checkbox_autologin);
        edittextID = (EditText) findViewById(R.id.editText_id);
        edittextPassword = (EditText) findViewById(R.id.editText_password);

        progress = new Progress(LoginTest.this, LoginTest.this, 0, 50);

        progress.show();
        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (autoLogin) {
            edittextID.setText(varID);
            edittextPassword.setText(varPassword);
            checkboxAutologin.setChecked(autoLogin);
        }


    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_login :
                varID = edittextID.getText().toString().trim();
                varPassword = edittextPassword.getText().toString().trim();

                ((GlobalVar)this.getApplication()).setMyAddr(varIP, varPort, varRest);
                //UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
                loginUrl = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
                UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + ((GlobalVar)this.getApplication()).getMyRest();
                UPLOAD_KEY = "login";

                login(loginUrl, UPLOAD_KEY, varID, varPassword);
                break ;
        }
    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", checkboxAutologin.isChecked());
        editor.putString("ID", edittextID.getText().toString().trim());
        editor.putString("PWD", edittextPassword.getText().toString().trim());
        editor.putString("KEY", UPLOAD_KEY.trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값

        autoLogin = appData.getBoolean(boardName + "_SAVE_LOGIN_DATA", false);
        varID = appData.getString(boardName + "_ID", "");
        varPassword = appData.getString(boardName + "_PWD", "");
        UPLOAD_KEY = appData.getString(boardName + "_KEY", "");
        varIP = appData.getString(boardName + "_IP", "");
        varPort = appData.getString(boardName + "_PORT", "");
        varRest = appData.getString(boardName + "_REST", "");
        varRest = appData.getString(boardName + "_REST", "");

        ((GlobalVar)this.getApplication()).setMyAddr(varIP, varPort, varRest);
        //UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
        loginUrl = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + ((GlobalVar)this.getApplication()).getMyRest();
        System.out.println(loginUrl);
        System.out.println(UPLOAD_URL);
        System.out.println(varRest);
    }


    private void login(final String address, final String key, final String id, final String password){
        class Process extends AsyncTask<Void,Void,String> {
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                progress.setMax(100);
                System.out.println(s);
                //이상하게 여기만 String compare가 == 안되고 equals는됨
                /*
                String v = "login";
                String c = new String("login");
                System.out.println(s);
                System.out.println(s == v);
                System.out.println(s == c);
                System.out.println(s == "login");
                System.out.println(v == "login");
                System.out.println(c == "login");
                System.out.println(v == c);
                */

                if(s.equals("login")) {
                    save();
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    save();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                property = new ArrayList<>();
                property.add(new String[]{"func", key});
                property.add(new String[]{"user_code", id});
                property.add(new String[]{"input_code", password});

                String result = rh.sendPostRequest(address, property);

                try{
                    result = ParseJson(result);
                }catch (Exception e){
                    System.out.println(e);
                }

                return result;
            }
        }

        new Process().execute();
    }


    public String ParseJson(String s){
        JSONObject jobject;
        String error = s;

        try {
            jobject = new JSONObject(s);
            s = jobject.getString("data");
            jobject = new JSONObject(s);
            s = jobject.getString("mesg");
            return s;
        }catch (JSONException e){
            System.out.println(e);
            return error;
        }
    }

}
