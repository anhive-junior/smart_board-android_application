package com.example.upload;

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

import com.example.upload.Util.GlobalVar;
import com.example.upload.Util.Progress;
import com.example.upload.Util.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements Button.OnClickListener {
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
    private Intent intent;
    private String boardName;
    private String varIP;
    private String varPort;
    private String varRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();

        boardName = intent.getStringExtra("boardName");

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        //자동로그인
        if(autoLogin) {
            login(loginUrl, UPLOAD_KEY, varID, varPassword);
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_test);

        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);

        checkboxAutologin = (CheckBox) findViewById(R.id.checkbox_autologin);
        edittextID = (EditText) findViewById(R.id.editText_id);
        edittextPassword = (EditText) findViewById(R.id.editText_password);

        progress = new Progress(Login.this, Login.this, 0, 50);

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
        editor.putBoolean(boardName + "_SAVE_LOGIN_DATA", checkboxAutologin.isChecked());
        editor.putString(boardName + "_ID", edittextID.getText().toString().trim());
        editor.putString(boardName + "_PWD", edittextPassword.getText().toString().trim());
        editor.putString(boardName + "_KEY", UPLOAD_KEY.trim());

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
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
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
