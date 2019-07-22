package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class ShowImage extends AppCompatActivity implements Button.OnClickListener {
    public String UPLOAD_URL;
    public static final String UPLOAD_KEY = "rmcard";

    private Bitmap myBitmap;
    private ImageView imageView;
    private Button buttonDelete;
    private Button buttonBack;
    private ArrayList<String[]> property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimage);
        property = new ArrayList<>();
        property.add(new String[]{"func", UPLOAD_KEY});

        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_signage.php";
        Intent intent = getIntent();
        showimage(intent.getExtras().getString("filepath"), Boolean.FALSE);


        buttonBack = (Button) findViewById(R.id.button_back);
        buttonDelete = (Button) findViewById(R.id.button_delete);

        buttonBack.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_back :
                finish();
                break ;
            case R.id.button_delete :
                Intent intent = getIntent();
                showimage(intent.getExtras().getString("name"), TRUE);
                break ;
        }
    }

    private void showimage(final String data, final Boolean rm){
        class Process extends AsyncTask<Void,Void,Bitmap>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(ShowImage.this, "Loading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                myBitmap = s;

                if(rm){
                    setResult(1234);
                    finish();
                }

                else{
                    imageView = (ImageView) findViewById(R.id.imageView3);
                    Glide.with(getApplicationContext())
                            .load("http://" + data)
                            .into(imageView);
                    loading.dismiss();

                    //imageView.setImageBitmap(myBitmap);
                }
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap result = null;

                if(rm) {
                    property.add(new String[]{"card", data});
                    rh.sendPostRequest(UPLOAD_URL, property);
                }/*
                else {


                    try {
                        URL url = new URL("http://" + data);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setReadTimeout(15000);
                        connection.setConnectTimeout(15000);
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        result = BitmapFactory.decodeStream(input);
                        result = Bitmap.createScaledBitmap(result, 960, 960, true);
                        connection.disconnect();

                    } catch (IOException e) {
                        System.out.println(e);
                        System.out.println("errorr");
                    }
                }*/
                return result;
            }
        }

        new Process().execute();
    }

}

