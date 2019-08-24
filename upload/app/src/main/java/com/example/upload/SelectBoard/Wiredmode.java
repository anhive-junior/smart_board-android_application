package com.example.upload.SelectBoard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upload.R;

import java.util.ArrayList;
import java.util.List;

public class Wiredmode extends AppCompatActivity {
    private EditText boardName;
    private EditText boardIP;
    private Button setComplete;
    private ProgressDialog loading;

    private SharedPreferences appData;//로그인정보 저장매체
    private TextView boardPort;
    private TextView boardRest;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.wiredmode);
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        setComplete = (Button) findViewById(R.id.button_set);
        boardName = (EditText) findViewById(R.id.board_name);
        boardPort = (EditText) findViewById(R.id.board_port);
        boardRest = (EditText) findViewById(R.id.board_rest);
        boardIP = (EditText) findViewById(R.id.board_ip);


        setComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectBoard.boardList.contains(boardName.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Wiredmode.this);
                    builder.setMessage("중복된 이름은 사용할 수 없습니다");
                    builder.show();
                }else{
                    loading = ProgressDialog.show(Wiredmode.this, "Uploading Image", "Please wait...",true,true);

                    SharedPreferences preferences = getSharedPreferences("appData", MODE_PRIVATE);
                    if(preferences.getString(boardName.getText().toString() + "_IP", "") != ""){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Wiredmode.this);
                        builder.setMessage("이미 등록된 액자입니다");
                        builder.show();
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("boardName", boardName.getText().toString());
                        setResult(1234, intent);

                        SharedPreferences.Editor editor = appData.edit();
                        editor.putInt("NUMBER OF BOARD", SelectBoard.boardNum + 1);
                        editor.putString("BOARD_" + (SelectBoard.boardNum + 1), boardName.getText().toString().trim());
                        editor.putString(boardName.getText().toString() + "_IP", boardIP.getText().toString().trim());
                        editor.putString(boardName.getText().toString() + "_PORT", boardPort.getText().toString().trim());
                        editor.putString(boardName.getText().toString() + "_REST", boardRest.getText().toString().trim());
                        editor.apply();

                        loading.dismiss();
                        finish();
                    }
                }

            }
        });
    }

}
