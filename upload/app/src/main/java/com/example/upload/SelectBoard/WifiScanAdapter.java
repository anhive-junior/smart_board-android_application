package com.example.upload.SelectBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.R;

import java.util.List;


public class WifiScanAdapter extends RecyclerView.Adapter<WifiScanAdapter.MyViewHolder> {

private List<SelectBoardInstall.device> wifiList;
private Context context;
private View.OnClickListener mOnClickListener;

    public WifiScanAdapter(List<SelectBoardInstall.device> wifiList, Context context) {
        this.wifiList = wifiList;
        this.context=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.wifi_list, viewGroup, false);

        MyViewHolder holder = new MyViewHolder(itemView);
        itemView.setTag(holder);
        itemView.setOnClickListener(mOnClickListener);
        return holder;

    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            String name = wifiList.get(position).getName();

            holder.vName.setText(name);
            holder.context = context;
            holder.position = position;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {

        int itemCount = wifiList.size();

        return itemCount;
    }
    public void setOnClickListener(View.OnClickListener lis) {
        mOnClickListener = lis;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected Context context;
        protected int position;


        public MyViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.ssid_name);

        }
    }

}
