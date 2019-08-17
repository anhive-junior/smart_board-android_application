package com.example.upload.SelectBoard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.R;

import java.util.ArrayList;
import java.util.List;

public class SelectBoardRegister extends AppCompatActivity {
    private EditText boardName;
    private Button setComplete;
    private ProgressDialog loading;

    private SharedPreferences appData;//로그인정보 저장매체

    private WifiManager wifi;
    private int ipAddress;
    private Thread thread;
    private WifiScanAdapter boardAdapter;
    private List<SelectBoardInstall.device> boardList = new ArrayList<>();
    private RecyclerView recyclerBoard;
    private TextView boardIP;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.selectboardregister);
        appData = getSharedPreferences("appData", MODE_PRIVATE);

        setComplete = (Button) findViewById(R.id.button_set);
        boardIP = (TextView) findViewById(R.id.board_ip);
        boardName = (EditText) findViewById(R.id.editText_board_name);

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ipAddress = wifi.getConnectionInfo().getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
        ip = ip.substring(0, ip.lastIndexOf(".") + 1) + "255";

        boardAdapter = new WifiScanAdapter(boardList, getApplicationContext());

        recyclerBoard = (RecyclerView) findViewById(R.id.recycler_board);
        recyclerBoard.setLayoutManager(new LinearLayoutManager(this));
        recyclerBoard.setAdapter(boardAdapter);

        thread = new Thread(new Broadcast(ip));
        thread.start();

        boardIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardList.clear();
                for(String i:Broadcast.result) boardList.add(new SelectBoardInstall.device(i));
                boardAdapter.notifyDataSetChanged();
            }
        });

        setComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SelectBoard.boardList.contains(boardName.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectBoardRegister.this);
                    builder.setMessage("중복된 이름은 사용할 수 없습니다");
                    builder.show();
                }else{
                    loading = ProgressDialog.show(SelectBoardRegister.this, "Uploading Image", "Please wait...",true,true);

                    SharedPreferences preferences = getSharedPreferences("appData", MODE_PRIVATE);
                    if(preferences.getString(boardName.getText().toString() + "_IP", "") != ""){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SelectBoardRegister.this);
                        builder.setMessage("이미 등록된 액자입니다");
                        builder.show();
                    }else{
                        Intent intent = new Intent();
                        System.out.println(boardName.getText().toString());
                        intent.putExtra("boardName", boardName.getText().toString());
                        setResult(1234, intent);

                        SharedPreferences.Editor editor = appData.edit();
                        editor.putInt("NUMBER OF BOARD", SelectBoard.boardNum + 1);
                        editor.putString("BOARD_" + (SelectBoard.boardNum + 1), boardName.getText().toString());
                        editor.putString(boardName.getText().toString() + "_IP", boardIP.getText().toString());
                        editor.putString(boardName.getText().toString() + "_PORT", "80");
                        editor.putString(boardName.getText().toString() + "_REST", "/signage/s00_signage.php");
                        editor.apply();

                        loading.dismiss();
                        thread.interrupt();
                        finish();
                    }
                }

            }
        });
    }

}
