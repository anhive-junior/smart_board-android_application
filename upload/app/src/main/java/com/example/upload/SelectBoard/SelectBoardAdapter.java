package com.example.upload.SelectBoard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.Login;
import com.example.upload.R;

import java.util.ArrayList;

public class SelectBoardAdapter extends RecyclerView.Adapter<SelectBoardAdapter.ItemRowHolder> {

    private ArrayList<String> dataList;
    private Context mContext;
    private Activity mActivity;
    private Intent intent;

    public SelectBoardAdapter(Context context, Activity activity, ArrayList<String> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    public SelectBoardAdapter.ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selectboard_item, null);
        SelectBoardAdapter.ItemRowHolder mh = new SelectBoardAdapter.ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SelectBoardAdapter.ItemRowHolder itemRowHolder, final int position) {

        final String sectionName = dataList.get(position);
        itemRowHolder.itemTitle.setText(sectionName);

        itemRowHolder.itemTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, Login.class);
                System.out.println(itemRowHolder.itemTitle.getText().toString());
                intent.putExtra("boardName", itemRowHolder.itemTitle.getText().toString());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.board_name);

        }

    }
}
