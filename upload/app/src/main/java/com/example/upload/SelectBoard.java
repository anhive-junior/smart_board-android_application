package com.example.upload;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectBoard extends AppCompatActivity implements Button.OnClickListener {
    private RecyclerView my_recycler_view;
    private SelectBoardAdapter adapter;
    private SharedPreferences appData;
    private Intent in;

    private int boardNum;
    private ArrayList<String> boardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectboard);
        //load();
        boardList = new ArrayList<>();

        boardList.add("test1");
        boardList.add("test2");
        boardList.add("test1");
        boardList.add("test2");
        boardList.add("test1");
        boardList.add(null);
        boardNum = boardList.size();

        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        //my_recycler_view.setHasFixedSize(true);
        adapter = new SelectBoardAdapter(getApplicationContext(), SelectBoard.this, boardList);
        my_recycler_view.setLayoutManager(new GridLayoutManager(this, 3));
        my_recycler_view.setAdapter(adapter);
    }

    @Override
    public void onClick(View view){
        in = null;
        switch (view.getId()) {
            case R.id.usersetting :
                in = new Intent(getApplicationContext(), UserSetting.class);
                break ;
            case R.id.setting :
                in = new Intent(getApplicationContext(), PlayWork.class);
                break ;
            case R.id.button_camera :
                in = new Intent(getApplicationContext(), CameraActivity.class);
                break ;
            case R.id.button_upload :
                in = new Intent(getApplicationContext(), UpLoadImage.class);
                break ;
            case R.id.button_list :
                in = new Intent(getApplicationContext(), ShowList.class);
                break ;
            case R.id.button_slide :
                in = new Intent(getApplicationContext(), ManageSlide.class);
                break ;
        }

        if(in!=null)
            startActivity(in);
    }

    // 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        //editor.putString("KEY", UPLOAD_KEY.trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        boardNum = appData.getInt("NUMBER OF BOARD", 0);
        boardList = new ArrayList<>();
        for(int i=0;i<boardNum;i++){
            boardList.add(appData.getString("BOARD_" + i, ""));
        }
    }

}
