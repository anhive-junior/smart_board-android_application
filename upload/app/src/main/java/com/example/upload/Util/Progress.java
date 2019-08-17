package com.example.upload.Util;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;


import com.example.upload.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class Progress {

    Context mContext;
    Activity mActivity;
    int count;
    int max;

    public Progress(Context context, Activity activity, int count, int max){
        mContext = context;
        mActivity = activity;
        this.count = count;
        this.max = max;
    }

    public void setMax(int max){
        this.max = max;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.progressbar, null);
        builder.setView(view);

        final DonutProgress bar = (DonutProgress) view.findViewById(R.id.progressbar);
        final AlertDialog dialog = builder.create();

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.progress_anim2);
        set.setTarget(bar);
        set.start();

        final Handler barHandler=new Handler();

        new Thread(){
            public void run() {
                count = 0;
                while(count < 100){
                    while (count < max) {
                        try {
                            Thread.sleep(1);
                        } catch (Exception e) {
                        }
                        barHandler.post(new Runnable() {
                            public void run() {
                                bar.setProgress(count);
                            }
                        });
                        count++;
                    }
                }
                dialog.dismiss();
            }
        }.start();

        dialog.show();
    }
}
