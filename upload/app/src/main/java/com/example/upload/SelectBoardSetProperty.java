package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.upload.Login.UPLOAD_URL;
import static com.example.upload.UpLoadImage.getStringImage;

public class SelectBoardSetProperty extends AppCompatActivity {
    EditText boardName;
    EditText ssid;
    EditText pwd;
    Button setComplete;
    private ArrayList<String[]> property;
    private ProgressDialog loading;

    private SharedPreferences appData;//로그인정보 저장매체

    private String UPLOAD_URL = "http://192.168.201.1:80/signage/s00_signage.php";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.selectboardsetproperty);

        setComplete = (Button) findViewById(R.id.button_set);
        boardName = (EditText) findViewById(R.id.editText_boardname);
        ssid = (EditText) findViewById(R.id.editText_ssid);
        pwd = (EditText) findViewById(R.id.editText_pwd);


        property = new ArrayList<>();
        property.add(new String[]{"func", "apsetting"});


        setComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                System.out.println(boardName.getText().toString());
                intent.putExtra("boardName", boardName.getText().toString());
                setResult(1234, intent);
                loading = ProgressDialog.show(SelectBoardSetProperty.this, "Uploading Image", "Please wait...",true,true);
                sendinfo();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
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
                new Thread(new Server()).start();
                while(Server.result == null);

                SharedPreferences.Editor editor = appData.edit();
                editor.putString(boardName.getText().toString() + "_IP", Server.result);
                editor.putString(boardName.getText().toString() + "_PORT", "80");
                editor.putString(boardName.getText().toString() + "_REST", "/signage/s00_signage.php");

                Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
                loading.dismiss();
                finish();

            }

            @Override
            protected String doInBackground(Void... params) {
                property.add(new String[]{"ap", ssid.getText().toString()});
                property.add(new String[]{"pass", pwd.getText().toString()});
                String result = rh.sendPostRequest(UPLOAD_URL, property);

                return result;
            }
        }

        //new Process().execute(rotatedBitmap);
        new Process().execute();
    }

}
