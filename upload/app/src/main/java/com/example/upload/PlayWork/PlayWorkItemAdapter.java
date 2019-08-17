package com.example.upload.PlayWork;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.Util.GlobalVar;
import com.example.upload.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayWorkItemAdapter extends RecyclerView.Adapter<PlayWorkItemAdapter.ViewHolder>{

    private ArrayList<PlayWorkProperty> mData;
    private Application application;
    private Context context;
    Activity mActivity;
    private LinearLayout test;
    private HashMap<String, String> playworkdata;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtextView1;
        TextView mtextView2;
        CheckBox mCheckbox;

        @SuppressLint("ClickableViewAccessibility")
        ViewHolder(View itemView) {
            super(itemView) ;

            mtextView1 = itemView.findViewById(R.id.textView1) ;
            mtextView2 = itemView.findViewById(R.id.textView2) ;
            mCheckbox = itemView.findViewById(R.id.checkBox);
            test = itemView.findViewById(R.id.item);

        }
    }

    PlayWorkItemAdapter(ArrayList<PlayWorkProperty> list, Activity activity, Application application, HashMap playworkdata) {
        mData = list ;
        mActivity = activity;
        this.application = application;
        this.playworkdata = playworkdata;

    }

    @Override
    public PlayWorkItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        PlayWorkItemAdapter.ViewHolder vh = new PlayWorkItemAdapter.ViewHolder(view) ;

        return vh ;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final PlayWorkItemAdapter.ViewHolder holder, final int position) {
        ArrayList<PlayWorkProperty> text = mData ;

        if(!text.get(position).getIstitle()) {
            holder.mtextView1.setPadding(32, holder.mtextView1.getPaddingRight(), holder.mtextView1.getPaddingTop(), holder.mtextView1.getPaddingBottom());
        }
        holder.mtextView1.setText(text.get(position).getName());
        holder.mtextView2.setText(text.get(position).getValue());
        if(text.get(position).getIscheckbox()) {
            holder.mCheckbox.setVisibility(View.VISIBLE);
            if (!text.get(position).getIschecked().equals("_")) {
                holder.mCheckbox.setChecked(true);
            }
        }
        else {
            holder.mtextView1.setPaddingRelative(holder.mtextView1.getPaddingLeft(), 16, holder.mtextView1.getPaddingTop(), holder.mtextView1.getPaddingBottom());
        }

        holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(position);
                switch (position){
                    case 1:
                        if(holder.mCheckbox.isChecked())
                            playworkdata.put("time_base", "✔");
                        else
                            playworkdata.put("time_base", "_");
                        break;
                    case 2:
                        if(holder.mCheckbox.isChecked())
                            playworkdata.put("count_base", "✔");
                        else
                            playworkdata.put("count_base", "_");
                        break;
                    case 3:
                        if(holder.mCheckbox.isChecked())
                            playworkdata.put("curate_base", "✔");
                        else
                            playworkdata.put("curate_base", "_");
                        break;
                    case 4:
                        if(holder.mCheckbox.isChecked())
                            playworkdata.put("media_base", "✔");
                        else
                            playworkdata.put("media_base", "_");
                        break;
                    case 6:
                        if(holder.mCheckbox.isChecked())
                            playworkdata.put("as_updated", "✔");
                        else
                            playworkdata.put("as_updated", "_");
                        break;
                    case 7:
                        if(holder.mCheckbox.isChecked())
                            playworkdata.put("as_powerup", "✔");
                        else
                            playworkdata.put("as_powerup", "_");
                        break;
                    case 8:
                        if(holder.mCheckbox.isChecked())
                            playworkdata.put("as_interval", "✔");
                        else
                            playworkdata.put("as_interval", "_");
                        break;
                }

                ((GlobalVar)application).setPlayworkdata(playworkdata);
            }
        });

        test.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(position);
                Intent intent = new Intent(context, Popup.class);
                switch (position){
                    case 1:
                        intent.putExtra("name", 1);
                        mActivity.startActivityForResult(intent, 1);
                        break;
                    case 2:
                        intent.putExtra("name", 2);
                        mActivity.startActivityForResult(intent, 2);
                        break;
                    case 8:
                        intent.putExtra("name", 8);
                        mActivity.startActivityForResult(intent, 8);
                        break;
                    case 10:
                        intent.putExtra("name", 10);
                        mActivity.startActivityForResult(intent, 10);
                        break;
                    case 11:
                        intent.putExtra("name", 11);
                        mActivity.startActivityForResult(intent, 11);
                        break;
                    case 12:
                        intent.putExtra("name", 12);
                        mActivity.startActivityForResult(intent, 12);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}