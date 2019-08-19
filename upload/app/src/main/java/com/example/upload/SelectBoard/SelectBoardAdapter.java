package com.example.upload.SelectBoard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        itemRowHolder.itemIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, Login.class);
                intent.putExtra("boardName", itemRowHolder.itemTitle.getText().toString());
                mActivity.startActivity(intent);
            }
        });

        itemRowHolder.itemIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected ImageView itemIcon;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.board_name);
            this.itemIcon = (ImageView) view.findViewById(R.id.icon);

        }

    }
}
