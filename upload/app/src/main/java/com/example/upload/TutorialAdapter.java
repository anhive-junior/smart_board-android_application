package com.example.upload;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.upload.Util.Progress;
import com.example.upload.Util.RequestHandler;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class TutorialAdapter extends PagerAdapter{
    private Context mContext;
    private ArrayList<Drawable> itemsList;
    private Activity mActivity;

    public TutorialAdapter(Activity activity, Context context, ArrayList<Drawable> itemsList) {
        mContext = context;
        mActivity = activity;
        this.itemsList = itemsList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null ;
        System.out.println(position);

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tutorial_item, container, false);


        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}