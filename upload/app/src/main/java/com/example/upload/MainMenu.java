package com.example.upload;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.upload.Camera.CameraActivity;
import com.example.upload.PlayWork.PlayWork;
import com.example.upload.ShowList.ShowList;
import com.example.upload.UpLoadImage.UpLoadImage;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
        private ImageButton buttonUpload;
        private ImageButton buttonList;
        private ImageButton buttonSlide;
        private ImageButton buttonCamera;;
        private ImageButton buttonUsersetting;
        private ImageButton buttonSetting;
        private Intent intent;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mainmenu);

            buttonCamera = (ImageButton) findViewById(R.id.button_camera);
            buttonUpload = (ImageButton) findViewById(R.id.button_upload);
            buttonList = (ImageButton) findViewById(R.id.button_list);
            buttonSlide = (ImageButton) findViewById(R.id.button_slide);

            buttonCamera.setOnClickListener(this);
            buttonUpload.setOnClickListener(this);
            buttonList.setOnClickListener(this);
            buttonSlide.setOnClickListener(this);

            setActionBar();
        }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_camera :
                intent = new Intent(getApplicationContext(), CameraActivity.class);
                break ;
            case R.id.button_upload :
                intent = new Intent(getApplicationContext(), UpLoadImage.class);
                break ;
            case R.id.button_list :
                intent = new Intent(getApplicationContext(), ShowList.class);
                break ;
            case R.id.button_slide :
                intent = new Intent(getApplicationContext(), ManageSlide.class);
                break;
        }
        startActivity(intent);
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        actionBar.setCustomView(mCustomView);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        buttonSetting = (ImageButton) findViewById(R.id.setting);
        buttonUsersetting = (ImageButton) findViewById(R.id.usersetting);

        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               intent = new Intent(getApplicationContext(), PlayWork.class);
               startActivity(intent);
            }
        });

        buttonUsersetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), UserSetting.class);
                startActivity(intent);
            }
        });

    }


}


