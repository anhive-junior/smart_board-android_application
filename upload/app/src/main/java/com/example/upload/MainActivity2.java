package com.example.upload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
        private Button buttonUpload;
        private Button buttonList;
        private Button buttonSlide;
        private Button buttonCamera;;
        private Button buttonUsersetting;
        private Button buttonSetting;
        private Intent in = null;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            buttonUsersetting = (Button) findViewById(R.id.usersetting);
            buttonSetting = (Button) findViewById(R.id.setting);
            buttonCamera = (Button) findViewById(R.id.button_camera);
            buttonUpload = (Button) findViewById(R.id.button_upload);
            buttonList = (Button) findViewById(R.id.button_list);
            buttonSlide = (Button) findViewById(R.id.button_slide);

            buttonUsersetting.setOnClickListener(this);
            buttonSetting.setOnClickListener(this);
            buttonCamera.setOnClickListener(this);
            buttonUpload.setOnClickListener(this);
            buttonList.setOnClickListener(this);
            buttonSlide.setOnClickListener(this);
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


}


