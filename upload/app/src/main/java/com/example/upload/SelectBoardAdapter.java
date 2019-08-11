package com.example.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.TooManyListenersException;

public class SelectBoardAdapter extends RecyclerView.Adapter<com.example.upload.SelectBoardAdapter.ItemRowHolder> {

    private ArrayList<String> dataList;
    private Context mContext;
    private Activity mActivity;
    private Boolean mCheckboxVisible = Boolean.FALSE;
    private DataTransferInterface dtInterface;
    private Intent intent;

    public SelectBoardAdapter(Context context, Activity activity, ArrayList<String> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.mActivity = activity;
        this.dtInterface = dtInterface;

    }

    @Override
    public com.example.upload.SelectBoardAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selectboard_item, null);
        com.example.upload.SelectBoardAdapter.ItemRowHolder mh = new com.example.upload.SelectBoardAdapter.ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final com.example.upload.SelectBoardAdapter.ItemRowHolder itemRowHolder, final int position) {

        final String sectionName = dataList.get(position);

        if(sectionName == null){
            System.out.println(dataList);
            /*
            System.out.println(position);
            System.out.println(dataList.get(position));
            System.out.println(dataList);
            dataList.remove(position);
            System.out.println(dataList);
            dataList.add("test");
            System.out.println(dataList);*/

            itemRowHolder.itemTitle.setVisibility(View.GONE);
            itemRowHolder.addButtion.setVisibility(View.VISIBLE);

            itemRowHolder.addButtion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(mContext, SelectBoardSetProperty.class);
                    mActivity.startActivityForResult(intent, 1111);
                }
            });
        }else{
            itemRowHolder.itemTitle.setVisibility(View.VISIBLE);
            itemRowHolder.addButtion.setVisibility(View.GONE);
            itemRowHolder.itemTitle.setText(sectionName);
        }


        itemRowHolder.itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, LoginTest.class);
                System.out.println(itemRowHolder.itemTitle.getText().toString());
                intent.putExtra("boardName", itemRowHolder.itemTitle.getText().toString());
                mActivity.startActivity(intent);
            }
        });


       /*  itemRowHolder.recycler_view_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        //Allow ScrollView to intercept touch events once again.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle RecyclerView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });*/


       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected TextView addButtion;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.board_name);
            this.addButtion = (Button) view.findViewById(R.id.add);

        }

    }
}
