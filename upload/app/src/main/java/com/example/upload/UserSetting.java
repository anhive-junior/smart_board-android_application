package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upload.Util.GlobalVar;
import com.example.upload.Util.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserSetting extends AppCompatActivity implements Button.OnClickListener {
    public String test;

    private String UPLOAD_URL;
    private String loginUrl;
    private String UPLOAD_KEY;
    private ArrayList<String[]> property;
    private Button buttonLogin;
    private EditText edittextID;
    private EditText edittextPassword;
    private EditText edittextIP;
    private EditText edittextPort;
    private EditText edittextRest;
    private String varID = "";
    private String varPassword = "";
    private String varIP = "";
    private String varPort = "";
    private String varRest = "";

    private SharedPreferences appData;//로그인정보 저장매체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);

        edittextIP = (EditText) findViewById(R.id.editText_ip);
        edittextPort = (EditText) findViewById(R.id.editText_port);
        edittextID = (EditText) findViewById(R.id.editText_id);
        edittextPassword = (EditText) findViewById(R.id.editText_password);
        edittextRest = (EditText) findViewById(R.id.editText_rest);

        edittextID.setText(varID);
        edittextPassword.setText(varPassword);
        edittextIP.setText(varIP);
        edittextPort.setText(varPort);
        edittextRest.setText(varRest);

    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_login :
                varID = edittextID.getText().toString().trim();
                varPassword = edittextPassword.getText().toString().trim();
                varIP = edittextIP.getText().toString().trim();
                varPort = edittextPort.getText().toString().trim();
                varRest = edittextRest.getText().toString().trim();

                ((GlobalVar)this.getApplication()).setMyAddr(varIP, varPort, varRest);
                loginUrl = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
                UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + ((GlobalVar)this.getApplication()).getMyRest();
                UPLOAD_KEY = "login";

                login(loginUrl, UPLOAD_KEY, varID, varPassword);
                break ;
        }
    }

    private void save() {
        SharedPreferences.Editor editor = appData.edit();

        editor.putString("ID", edittextID.getText().toString().trim());
        editor.putString("PWD", edittextPassword.getText().toString().trim());
        editor.putString("IP", edittextIP.getText().toString().trim());
        editor.putString("PORT", edittextPort.getText().toString().trim());
        editor.putString("REST", edittextRest.getText().toString().trim());
        editor.putString("KEY", UPLOAD_KEY.trim());

        editor.apply();
    }

    private void load() {
        varID = appData.getString("ID", "");
        varPassword = appData.getString("PWD", "");
        UPLOAD_KEY = appData.getString("KEY", "");
        varIP = appData.getString("IP", "");
        varPort = appData.getString("PORT", "");
        varRest = appData.getString("REST", "");

        ((GlobalVar)this.getApplication()).setMyAddr(varIP, varPort, varRest);
        loginUrl = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + ((GlobalVar)this.getApplication()).getMyRest();
    }


    private void login(final String address, final String key, final String id, final String password){
        class Process extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(UserSetting.this, "Loading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();

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
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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

                return ParseJson(result);
            }
        }

        new Process().execute();
    }


    public String ParseJson(String s){
        JSONObject jobject;

        try {
            jobject = new JSONObject(s);
            s = jobject.getString("data");
            jobject = new JSONObject(s);
            s = jobject.getString("mesg");
            return s;
        }catch (JSONException e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
