package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UpLoadVideo extends AppCompatActivity implements Button.OnClickListener {
    public String UPLOAD_URL;
    public static String UPLOAD_KEY;

    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonDelete;
    private Button buttonShow;

    private static final int SELECT_VIDEO = 3;

    private String selectedPath;
    VideoView videoView;
    MediaController mediaController;
    private EditText val_caption;

    private ArrayList<String[]> property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video);
        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_signage.php";

        mediaController = new MediaController(this);
        videoView = (VideoView)findViewById(R.id.videoView);
        videoView.setMediaController(mediaController);//videoView에 컨트롤러 설정

        val_caption = (EditText) findViewById(R.id.input_caption);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonShow = (Button) findViewById(R.id.buttonShow);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonShow.setOnClickListener(this);

        Button buttonPrevious = (Button) findViewById(R.id.button17);
        Button buttonPlayhold = (Button) findViewById(R.id.button16);
        Button buttonNext = (Button) findViewById(R.id.button15);
        Button buttonRestart= (Button) findViewById(R.id.button14);
        Button buttonRotateLeft= (Button) findViewById(R.id.button13);
        Button buttonRotateRight= (Button) findViewById(R.id.button12);
        Button buttonFlipped= (Button) findViewById(R.id.button11);
        Button buttonMirrored= (Button) findViewById(R.id.button10);

        buttonPrevious.setOnClickListener(this);
        buttonPlayhold.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonRestart.setOnClickListener(this);
        buttonRotateLeft.setOnClickListener(this);
        buttonRotateRight.setOnClickListener(this);
        buttonFlipped.setOnClickListener(this);
        buttonMirrored.setOnClickListener(this);
    }

    //버튼설정
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonChoose :
                chooseVideo();
                break ;
            case R.id.buttonUpload :
                uploadVideo();
                break ;
                /*//서버지원필요
            case R.id.buttonDelete :
                chooseVideo();
                break ;
            case R.id.buttonShow :
                uploadVideo();
                break ;
            case R.id.button10 :
                monitorcontrol("mirrored");
                break ;
            case R.id.button11 :
                monitorcontrol("flipped");
                break ;
                */
            case R.id.button12 :
                monitorcontrol("decrease volume");
                break ;
            case R.id.button13 :
                monitorcontrol("increase volume");
                break ;
            case R.id.button14 :
                monitorcontrol("restart");
                break ;
            case R.id.button15 :
                monitorcontrol("seek +30 seconds");
                break ;
            case R.id.button16 :
                monitorcontrol("pause/resume");
                break ;
            case R.id.button17 :
                monitorcontrol("seek -30 seconds");
                break ;
        }
    }

    //Video선택
    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    //비디오를 videoView에 set
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {

                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                videoView.setVideoPath(selectedPath);
                videoView.seekTo(100);//썸네일

            }
        }
    }

    //URI로부터 파일경로 추출
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }


    //서버로 Video업로드
    private void uploadVideo() {
        UPLOAD_KEY = "upvideo";
        class UploadVideo extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(UpLoadVideo.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();

                Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler(getApplicationContext());
                //서버IP주소, 서버명령어, 메모, video 파일경로를 서버연결함수에 전달
                String result = rh.sendPostRequest(UPLOAD_URL, UPLOAD_KEY, val_caption.getText().toString(), selectedPath);
                return result;
            }
        }

        new UploadVideo().execute();
    }

    private void monitorcontrol(final String msg){
        UPLOAD_KEY = "video_control";
        //int cut = file.lastIndexOf('/');
        //String fileName = file.substring(cut + 1);

        property = new ArrayList<>();
        property.add(new String[]{"func", UPLOAD_KEY});
        property.add(new String[]{"video", UPLOAD_KEY});


        class Process extends AsyncTask<Bitmap,Void,String>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(UpLoadVideo.this, "Loading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();
                System.out.println(s);

                Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                property.add(new String[]{"ctrl", msg});

                String result = rh.sendPostRequest(UPLOAD_URL, property);

                return result;
            }
        }

        new Process().execute();
    }

}
