package com.example.upload.SelectBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.R;

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

        boardList.add("test");
        //boardNum = boardList.size();

        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        //my_recycler_view.setHasFixedSize(true);
        adapter = new SelectBoardAdapter(getApplicationContext(), SelectBoard.this, boardList);
        my_recycler_view.setLayoutManager(new GridLayoutManager(this, 3));
        my_recycler_view.setAdapter(adapter);

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);


        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                if(menuItem.getTitle().toString().equals("설치")) intent = new Intent(getApplicationContext(), SelectBoardInstall.class);
                else intent = new Intent(getApplicationContext(), SelectBoardRegister.class);
                startActivity(intent);
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1234 && requestCode == 1111) {
            System.out.println(boardList);
            boardList.remove(boardList.size() - 1);
            System.out.println(boardList);
            System.out.println(data.getExtras());
            boardList.add(data.getStringExtra("boardName"));
            System.out.println(boardList);
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
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
