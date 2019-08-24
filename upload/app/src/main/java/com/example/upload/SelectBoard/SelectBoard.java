package com.example.upload.SelectBoard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.R;
import com.example.upload.Tutorial;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class SelectBoard extends AppCompatActivity {
    private RecyclerView my_recycler_view;
    private SelectBoardAdapter adapter;
    private SharedPreferences appData;
    private Intent intent;

    public static int boardNum = 0;
    public static ArrayList<String> boardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectboard);
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        boardList = new ArrayList<>();
        try{
            load();
        }catch (Exception e){
            e.printStackTrace();
        }

        //boardNum = boardList.size();

        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        //my_recycler_view.setHasFixedSize(true);
        adapter = new SelectBoardAdapter(this, SelectBoard.this, boardList);
        my_recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        my_recycler_view.setAdapter(adapter);

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                if(menuItem.getTitle().toString().equals("설치")) {
                    intent = new Intent(getApplicationContext(), SelectBoardInstall.class);
                    startActivity(intent);
                }
                else if(menuItem.getTitle().toString().equals("튜토리얼")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/anhive-junior/smart-board/wiki/Tutorial"));
                    intent.setPackage("com.android.chrome");
                    startActivity(intent);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectBoard.this);
                    builder.setTitle("어떤 모드인가요?");
                    builder.setPositiveButton("AP모드",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent = new Intent(getApplicationContext(), APmode.class);
                                    startActivityForResult(intent, 1111);
                                }
                            });
                    builder.setNegativeButton("유선모드",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent = new Intent(getApplicationContext(), Wiredmode.class);
                                    startActivityForResult(intent, 1111);
                                }
                            });
                    builder.show();
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1234 && requestCode == 1111) {
            boardList.add(data.getStringExtra("boardName"));
            adapter.notifyDataSetChanged();
        }
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        boardNum = appData.getInt("NUMBER OF BOARD", 0);
        System.out.println(boardNum);
        boardList = new ArrayList<>();
        for(int i=1;i<=boardNum;i++){
            boardList.add(appData.getString("BOARD_" + i, ""));
        }
    }

}
