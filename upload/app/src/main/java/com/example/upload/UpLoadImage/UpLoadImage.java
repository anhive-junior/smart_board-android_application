package com.example.upload.UpLoadImage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.upload.Login;
import com.example.upload.Util.GlobalVar;
import com.example.upload.Util.Progress;
import com.example.upload.R;
import com.example.upload.Util.RequestHandler;

import java.io.ByteArrayOutputStream;

public class UpLoadImage extends AppCompatActivity implements Button.OnClickListener{
    public String UPLOAD_URL;
    public static final String UPLOAD_KEY = "sendcard";

    private int PICK_IMAGE_REQUEST = 1;
    private ImageButton buttonChoose;
    private ImageButton buttonUpload;
    private EditText val_caption;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;
    private String filename;
    private int cut;
    private Progress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        UPLOAD_URL = Login.UPLOAD_URL;
        System.out.println(UPLOAD_URL);

        buttonChoose = (ImageButton) findViewById(R.id.buttonChoose);
        buttonUpload = (ImageButton) findViewById(R.id.buttonUpload);
        val_caption = (EditText) findViewById(R.id.input_caption);

        imageView = (ImageView) findViewById(R.id.imageView);

        progress = new Progress(UpLoadImage.this, UpLoadImage.this, 0, 50);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        showFileChooser();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.buttonChoose :
                showFileChooser();
                break ;
            case R.id.buttonUpload :
                uploadImage();
                break ;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            progress.show();

            Glide.with(this)
                    .asBitmap()
                    .load(filePath)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                            progress.setMax(100);
                            bitmap = resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

            try {
                filename = PathUtil.getPath(getApplicationContext(), filePath);
                cut = filename.lastIndexOf('/');
                filename = filename.substring(cut + 1);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    public static byte[] getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
    }

    private void uploadImage(){
        class Process extends AsyncTask<Bitmap,Void,String>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(UpLoadImage.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                byte[] uploadImage = getStringImage(bitmap);
                return rh.sendPostRequest(UPLOAD_URL, UPLOAD_KEY, val_caption.getText().toString(), uploadImage, filename);
            }
        }
        new Process().execute(bitmap);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( event.getAction() == KeyEvent.ACTION_DOWN ){
            if( keyCode == KeyEvent.KEYCODE_BACK ){
                finish();
            }
        }
        return super.onKeyDown( keyCode, event );
    }
}
