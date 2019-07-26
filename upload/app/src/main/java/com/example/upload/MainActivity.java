package com.example.upload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
        private Button buttonSet;
        private Button buttonUpload;
        private Button buttonList;
        private Button buttonSlide;
        private Button buttonVideo;
        private Button buttonPlayWork;
        private Button buttonCamera;
        private Intent in = null;
        private EditText varIP;
        private EditText varPort;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            buttonSet = (Button) findViewById(R.id.button_set);
            buttonUpload = (Button) findViewById(R.id.button_upload);
            buttonList = (Button) findViewById(R.id.button_list);
            buttonSlide = (Button) findViewById(R.id.button_slide);
            buttonVideo = (Button) findViewById(R.id.button_video);
            buttonPlayWork = (Button) findViewById(R.id.button_play);
            buttonCamera = (Button) findViewById(R.id.button_camera);

            buttonSet.setOnClickListener(this);
            buttonUpload.setOnClickListener(this);
            buttonList.setOnClickListener(this);
            buttonSlide.setOnClickListener(this);
            buttonVideo.setOnClickListener(this);
            buttonPlayWork.setOnClickListener(this);
            buttonCamera.setOnClickListener(this);

            varIP = (EditText) findViewById(R.id.input_ip);
            varPort = (EditText) findViewById(R.id.input_port);

            varIP.setText(((GlobalVar)this.getApplication()).getMyIP());
            varPort.setText(((GlobalVar)this.getApplication()).getMyPort());
        }

    @Override
    public void onClick(View view){
        in = null;
        switch (view.getId()) {
            case R.id.button_set :
                ((GlobalVar)this.getApplication()).setMyAddr(varIP.getText().toString(), varPort.getText().toString());
                System.out.println(((GlobalVar)this.getApplication()).getMyAddr());
                break ;
            case R.id.button_upload :
                in = new Intent(getApplicationContext(), UpLoadImage.class);
                break ;
            case R.id.button_list :
                in = new Intent(getApplicationContext(), ShowList_test.class);
                break ;
            case R.id.button_slide :
                in = new Intent(getApplicationContext(), ManageSlide.class);
                break ;
            case R.id.button_video :
                in = new Intent(getApplicationContext(), UpLoadVideo.class);
                break ;
            case R.id.button_play :
                in = new Intent(getApplicationContext(), PlayWork.class);
                break ;
            case R.id.button_camera :
                in = new Intent(getApplicationContext(), CameraActivity.class);
                break ;
        }

        if(in!=null)
            startActivity(in);
    }


}


