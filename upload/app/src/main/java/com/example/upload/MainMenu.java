package com.example.upload;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

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
        private Button buttonUsersetting;
        private Button buttonSetting;
        private Intent intent;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mainmenu);

            buttonUsersetting = (Button) findViewById(R.id.usersetting);
            buttonSetting = (Button) findViewById(R.id.setting);
            buttonCamera = (ImageButton) findViewById(R.id.button_camera);
            buttonUpload = (ImageButton) findViewById(R.id.button_upload);
            buttonList = (ImageButton) findViewById(R.id.button_list);
            buttonSlide = (ImageButton) findViewById(R.id.button_slide);

            buttonUsersetting.setOnClickListener(this);
            buttonSetting.setOnClickListener(this);
            buttonCamera.setOnClickListener(this);
            buttonUpload.setOnClickListener(this);
            buttonList.setOnClickListener(this);
            buttonSlide.setOnClickListener(this);
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
            case R.id.usersetting :
                intent = new Intent(getApplicationContext(), UserSetting.class);
                break ;
            case R.id.setting :
                intent = new Intent(getApplicationContext(), PlayWork.class);
                break ;
        }
        startActivity(intent);
    }


}


