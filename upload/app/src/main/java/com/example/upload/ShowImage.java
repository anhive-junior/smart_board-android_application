package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowImage extends AppCompatActivity{
    public String UPLOAD_URL;
    public static final String UPLOAD_KEY = "rmcard";

    private Bitmap myBitmap;
    private ImageView imageView;
    private Button buttonDelete;
    private Button buttonBack;
    private ArrayList<String[]> property;

    private long start;
    private long end;

    private ArrayList<String> itemsList;
    private int currentitem;

    private ViewPager viewPager ;
    private ShowImageAdapter pagerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimagetest);
        property = new ArrayList<>();
        property.add(new String[]{"func", UPLOAD_KEY});

        //UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_signage.php";
        UPLOAD_URL = Login.UPLOAD_URL;
        Intent intent = getIntent();
        itemsList = ShowList.itemList;
        currentitem = intent.getIntExtra("currentitem", 0);

        viewPager = (ViewPager) findViewById(R.id.viewPager) ;
        pagerAdapter = new ShowImageAdapter(ShowImage.this, this, itemsList, currentitem) ;
        viewPager.setAdapter(pagerAdapter) ;
        viewPager.setCurrentItem(currentitem);



        /*
        start = System.currentTimeMillis();
        //showimage(intent.getExtras().getString("filepath"), Boolean.FALSE);

        final ProgressDialog loading;
        loading = ProgressDialog.show(ShowImage.this, "Loading", "Please wait...",true,true);

        Glide.with(getApplicationContext())
                .load("http://" + intent.getExtras().getString("filepath"))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loading.dismiss();

                        end = System.currentTimeMillis();
                        System.out.println((end - start)/1000.0);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loading.dismiss();
                        return false;
                    }
                })
                .into(imageView);*/



    }



}

