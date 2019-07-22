package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.upload.ShowList.resizeBitmapImage;
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
        //showimage(intent.getExtras().getString("filepath"), Boolean.FALSE);


        imageView = (ImageView) findViewById(R.id.imageView3);

        buttonBack = (Button) findViewById(R.id.button_back);
        buttonDelete = (Button) findViewById(R.id.button_delete);

        buttonBack.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        final ProgressDialog loading;
        loading = ProgressDialog.show(ShowImage.this, "Loading", "Please wait...",true,true);

        Glide.with(getApplicationContext())
                .load("http://" + intent.getExtras().getString("filepath"))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loading.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loading.dismiss();
                        return false;
                    }
                })
                .into(imageView);
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
                //loading = ProgressDialog.show(ShowImage.this, "Loading", "Please wait...",true,true);
                myBitmap = s;

                if(rm){
                    setResult(1234);
                    finish();
                }
                else{

                    /*
                    Glide.with(getApplicationContext())
                            .load("http://" + data)
                            .into(imageView);
                    */
                    imageView.setImageBitmap(myBitmap);
                }

                loading.dismiss();
            }

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap result = null;

                if(rm) {
                    property.add(new String[]{"card", data});
                    rh.sendPostRequest(UPLOAD_URL, property);
                }
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
                        //result = resizeBitmapImage(result, imageView.getMaxWidth());
                        //result = Bitmap.createScaledBitmap(result, 960, 960, true);
                        connection.disconnect();

                    } catch (IOException e) {
                        System.out.println(e);
                        System.out.println("errorr");
                    }
                }
                return result;
            }
        }

        new Process().execute();
    }

}

