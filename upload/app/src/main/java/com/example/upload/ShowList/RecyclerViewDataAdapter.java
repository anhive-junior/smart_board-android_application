package com.example.upload.ShowList;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.R;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private Activity mActivity;
    private Boolean mCheckboxVisible = Boolean.FALSE;
    private DataTransferInterface dtInterface;
    private int itemIndex = 0;

    public RecyclerViewDataAdapter(Context context, Activity activity, DataTransferInterface dtInterface, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.mActivity = activity;
        this.dtInterface = dtInterface;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int position) {

        final String sectionName = dataList.get(position).time;

        ArrayList singleSectionItems = dataList.get(position).item;


        itemRowHolder.itemTitle.setText(sectionName);


        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mActivity, mCheckboxVisible, dtInterface, singleSectionItems, itemIndex);

        System.out.println(singleSectionItems.size());
        System.out.println(itemIndex += singleSectionItems.size());

        //itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new GridLayoutManager(mContext, 4));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);


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
        protected RecyclerView recycler_view_list;
        protected LinearLayout linearLayout;
        protected LinearLayout linearLayout2;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.background);
            this.linearLayout2 = (LinearLayout)mActivity.findViewById(R.id.layout_button);

            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mCheckboxVisible = !mCheckboxVisible;
                    if(mCheckboxVisible){
                        linearLayout2.setVisibility(View.VISIBLE);
                    }else{
                        linearLayout2.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                    return false;
                }
            });
        }

    }

}