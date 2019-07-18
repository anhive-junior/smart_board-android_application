package com.example.upload;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> implements Button.OnClickListener{


    private ArrayList<PlayWorkProperty> mData = null;
    public Context context;
    View view;
    Activity mActivity;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtextView1;
        TextView mtextView2;
        CheckBox mCheckbox;
        LinearLayout test;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            mtextView1 = itemView.findViewById(R.id.textView1) ;
            mtextView2 = itemView.findViewById(R.id.textView2) ;
            mCheckbox = itemView.findViewById(R.id.checkBox);
            test = itemView.findViewById(R.id.item);
        }
    }

    public Button testb;
    // 생성자에서 데이터 리스트 객체를 전달받음.
    SimpleTextAdapter(ArrayList<PlayWorkProperty> list, Activity activity) {
        mData = list ;
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    View.OnClickListener testclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "test", Toast.LENGTH_LONG).show();
        }
    };


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SimpleTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        SimpleTextAdapter.ViewHolder vh = new SimpleTextAdapter.ViewHolder(view) ;

        //testb = (Button) view.findViewById(R.id.set_playlistpolicy);
        //testb.setOnClickListener(this);
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SimpleTextAdapter.ViewHolder holder, int position) {
        ArrayList<PlayWorkProperty> text = mData ;
        holder.mtextView1.setText(text.get(position).getName());
        holder.mtextView2.setText(text.get(position).getValue());
        if(text.get(position).getIscheckbox())
            holder.mCheckbox.setVisibility(View.VISIBLE);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}