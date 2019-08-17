package com.example.upload.ShowList;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.upload.R;

import java.util.ArrayList;

public class ShowImage extends AppCompatActivity{
    private ArrayList<String> itemsList;
    private int currentitem;
    private Intent intent;
    private ViewPager viewPager ;
    private ShowImageAdapter pagerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showimagetest);

        intent = getIntent();
        itemsList = ShowList.itemList;
        currentitem = intent.getIntExtra("currentitem", 0);

        viewPager = (ViewPager) findViewById(R.id.viewPager) ;
        pagerAdapter = new ShowImageAdapter(ShowImage.this, this, itemsList, currentitem) ;
        viewPager.setAdapter(pagerAdapter) ;
        viewPager.setCurrentItem(currentitem);
    }
}

