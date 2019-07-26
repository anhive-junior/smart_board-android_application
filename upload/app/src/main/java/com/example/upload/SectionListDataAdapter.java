package com.example.upload;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{

    private ArrayList<SingleDataModel> itemsList;
    //private Context mContext;
    private Activity mActivity;
    private Boolean mCheckboxVisible;
    private HashMap<Integer, String> cardlist;

    private String UPLOAD_URL;
    private String UPLOAD_KEY;
    private ArrayList<String[]> property;
    private String lst = "";
    private DataTransferInterface dtInterface;

    public SectionListDataAdapter(Activity activity, Boolean checkboxvisible, final DataTransferInterface dtInterface, ArrayList<SingleDataModel> itemsList) {
        this.itemsList = itemsList;
        //this.mContext = context;
        this.mActivity = activity;
        this.mCheckboxVisible = checkboxvisible;
        cardlist = new HashMap<>();
        property = new ArrayList<>();
        this.dtInterface = dtInterface;

        Button refresh = (Button) mActivity.findViewById(R.id.button_refresh);
        Button deleteonce = (Button) mActivity.findViewById(R.id.button_deleteonce);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("setplaylist");
                UPLOAD_KEY = "setplaylist";

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
            }
        });

        deleteonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("rmcard");
                UPLOAD_KEY = "rmcard";

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
            }
        });
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {

        final SingleDataModel singleItem = itemsList.get(position);

        holder.itemImage.setImageBitmap(singleItem.thumb);

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowImage.class);
                intent.putExtra("filepath", singleItem.filepath);
                intent.putExtra("name", singleItem.name);
                mActivity.startActivityForResult(intent, 1111);
            }
        });

        if(mCheckboxVisible){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(position);
                System.out.println(singleItem.name);
                System.out.println(isChecked);

                if(isChecked)
                    cardlist.put(position, singleItem.name);
                else if(!isChecked) {
                    System.out.println(position);
                    cardlist.remove(position);
                }
                //notifyDataSetChanged();
            }
        });


       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected ImageView itemImage;
        protected CheckBox checkBox;

        public SingleItemRowHolder(final View view) {
            super(view);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        }
    }



}