package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements Button.OnClickListener {

    private String UPLOAD_URL;
    private String UPLOAD_KEY;
    private ArrayList<String[]> property;
    private Button buttonLogin;
    private EditText edittextID;
    private EditText edittextPassword;
    private EditText edittextIP;
    private EditText edittextPort;
    private String varID;
    private String varPassword;
    private String varIP;
    private String varPort;
    private CheckBox checkboxAutologin;

    private SharedPreferences appData;//로그인정보 저장매체
    private boolean autoLogin;//자동로그인여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        //자동로그인
        if(autoLogin) {
            login(UPLOAD_URL, UPLOAD_KEY, varID, varPassword);
            //finish();//개발용주석처리
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);

        checkboxAutologin = (CheckBox) findViewById(R.id.checkbox_autologin);
        edittextIP = (EditText) findViewById(R.id.editText_ip);
        edittextPort = (EditText) findViewById(R.id.editText_port);
        edittextID = (EditText) findViewById(R.id.editText_id);
        edittextPassword = (EditText) findViewById(R.id.editText_password);

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (autoLogin) {
            edittextID.setText(varID);
            edittextPassword.setText(varPassword);
            edittextIP.setText(varIP);
            edittextPort.setText(varPort);
            checkboxAutologin.setChecked(autoLogin);
        }


    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_login :
                varID = edittextID.getText().toString().trim();
                varPassword = edittextPassword.getText().toString().trim();
                varIP = edittextIP.getText().toString().trim();
                varPort = edittextPort.getText().toString().trim();

                ((GlobalVar)this.getApplication()).setMyAddr(varIP, varPort);
                UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
                UPLOAD_KEY = "login";

                login(UPLOAD_URL, UPLOAD_KEY, varID, varPassword);
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
        editor.putString("IP", edittextIP.getText().toString().trim());
        editor.putString("PORT", edittextPort.getText().toString().trim());
        editor.putString("KEY", UPLOAD_KEY.trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        autoLogin = appData.getBoolean("SAVE_LOGIN_DATA", false);
        varID = appData.getString("ID", "");
        varPassword = appData.getString("PWD", "");
        UPLOAD_KEY = appData.getString("KEY", "");
        varIP = appData.getString("IP", "");
        varPort = appData.getString("PORT", "");

        ((GlobalVar)this.getApplication()).setMyAddr(varIP, varPort);
        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_login.php";
    }


    private void login(final String address, final String key, final String id, final String password){
        class Process extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(Login.this, "Loading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();
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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

        try {
            jobject = new JSONObject(s);
            s = jobject.getString("data");
            jobject = new JSONObject(s);
            s = jobject.getString("mesg");
            return s;
        }catch (JSONException e){
            System.out.println(e);
        }

        return "Parsing Error";
    }

}
