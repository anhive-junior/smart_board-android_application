package com.example.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class WifiScanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private List<SelectBoardSetProperty.device> wifiList;
private Context context;
private View.OnClickListener mOnClickListener;

    public WifiScanAdapter(List<SelectBoardSetProperty.device> wifiList, Context context) {
        this.wifiList = wifiList;
        this.context=context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.wifi_list, viewGroup, false);

         MyViewHolder holder = new MyViewHolder(itemView);
            itemView.setTag(holder);
            itemView.setOnClickListener(mOnClickListener);
            return holder;

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


           SelectBoardSetProperty.device device=wifiList.get(position);
           String name=device.getName().toString();

             ((MyViewHolder) holder).vName.setText(name);
             ((MyViewHolder) holder).vName.setTag(device);


            ((MyViewHolder) holder).context = context;
            ((MyViewHolder) holder).position = position;
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
