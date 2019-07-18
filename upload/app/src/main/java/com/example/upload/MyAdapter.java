package com.example.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

class MyAdapter extends BaseAdapter implements Button.OnClickListener{
    Context context;
    int layout;
    Data[] img;
    Data[] myBitmap;
    LayoutInflater inf;
    Boolean showCheckBoxes;
    Activity activity;
    HashMap<Integer, String> cardlist;
    String UPLOAD_URL;
    String UPLOAD_KEY;
    public ArrayList<String[]> property = null;
    String lst = "";
    DataTransferInterface dtInterface;


    public MyAdapter(Context context, int layout, Data[] img, Boolean showCheckBoxes, Data[] myBitmap, Activity activity, DataTransferInterface dtInterface) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        this.showCheckBoxes = showCheckBoxes;
        this.myBitmap = myBitmap;
        this.activity = activity;
        cardlist = new HashMap<>();
        property = new ArrayList<>();
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        UPLOAD_URL = ((GlobalVar)this.context).getMyAddr() + "/signage/s00_signage.php";
        this.dtInterface = dtInterface;

        Button buttonRefresh = (Button)activity.findViewById(R.id.button_refresh);
        Button buttonDeleteOnce = (Button)activity.findViewById(R.id.button_deleteonce);

        buttonRefresh.setOnClickListener(this);
        buttonDeleteOnce.setOnClickListener(this);
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return img[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        iv.setImageBitmap(img[position].thumb);

        LinearLayout mLinearLayout = (LinearLayout)activity.findViewById(R.id.layout_button);
        CheckBox mCheckBox = (CheckBox)convertView.findViewById(R.id.checkbox);

        if(showCheckBoxes){
            mCheckBox.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);

        }else{
            mCheckBox.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.GONE);
            mCheckBox.setChecked(false);
        }

        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCheckBoxes = !showCheckBoxes;
                if(!showCheckBoxes)
                    cardlist.clear();
                notifyDataSetChanged();
                return true;
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowImage.class);
                intent.putExtra("filepath", myBitmap[position].filepath);
                intent.putExtra("name", myBitmap[position].name);
                activity.startActivityForResult(intent, 1111);
            }
        });


        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(position);
                System.out.println(myBitmap[position].name);
                System.out.println(isChecked);

                if(isChecked)
                    cardlist.put(position, myBitmap[position].name);
                else if(!isChecked) {
                    System.out.println(position);
                    cardlist.remove(position);
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_refresh :
                UPLOAD_KEY = "setplaylist";
                break ;
            case R.id.button_deleteonce :
                UPLOAD_KEY = "rmcard";
                break ;
        }

        int j=0;
        for(String i: cardlist.values()) {
            System.out.println(i);
            if(j==0)
                lst += i;
            else
                lst += "|" + i;
            j++;
        }
        System.out.println(lst);

        property.add(new String[]{"func", UPLOAD_KEY});
        property.add(new String[]{"lst", lst});

        dtInterface.setValue(UPLOAD_KEY, lst);

        //System.out.println(rh.sendPostRequest(UPLOAD_URL, property));
    }
}

