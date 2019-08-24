package com.example.upload;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.upload.ShowList.ShowImageAdapter;
import com.example.upload.ShowList.ShowList;

import java.util.ArrayList;

public class Tutorial extends AppCompatActivity{
    private ArrayList<String> itemsList;
    private int currentitem;
    private Intent intent;
    private ViewPager viewPager ;
    private TutorialAdapter pagerAdapter ;
    private ArrayList<Drawable> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/anhive-junior/smart-board/wiki/Tutorial"));
        intent.setPackage("com.android.chrome");
        startActivity(intent);
//
//        items.add(null);
//        items.add(null);
//        items.add(null);
//        items.add(null);
//
//        viewPager = (ViewPager) findViewById(R.id.viewPager) ;
//        pagerAdapter = new TutorialAdapter(Tutorial.this, this, items) ;
//        viewPager.setAdapter(pagerAdapter) ;
    }
}

