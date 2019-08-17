package com.example.upload.ShowList;

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
import com.example.upload.Login;
import com.example.upload.Util.Progress;
import com.example.upload.R;
import com.example.upload.Util.RequestHandler;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class ShowImageAdapter extends PagerAdapter{
    private Context mContext;
    private ArrayList<String> itemsList;
    private int currentitem;
    private Activity mActivity;
    private String UPLOAD_URL;
    private ArrayList<String[]> property;
    private String UPLOAD_KEY = "rmcard";
    private Progress progress;


    public ShowImageAdapter(Activity activity, Context context, ArrayList<String> itemsList, int currentitem) {
        mContext = context;
        mActivity = activity;
        this.itemsList = itemsList;
        this.currentitem = currentitem;
        UPLOAD_URL = Login.UPLOAD_URL;
        property = new ArrayList<>();
        property.add(new String[]{"func", UPLOAD_KEY});
        progress = new Progress(activity, activity, 0, 25);

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = null ;
        System.out.println(position);

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.showimagepage, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView3);

            progress.show();

            Glide.with(mContext)
                    .load("http://" + itemsList.get(position))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progress.setMax(100);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progress.setMax(100);
                            return false;
                        }
                    })
                    .into(imageView);


            Button buttonBack = (Button) view.findViewById(R.id.button_back);
            Button buttonDelete = (Button) view.findViewById(R.id.button_delete);
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showimage(itemsList.get(position), TRUE);
                }
            });
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("remove " + position);
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

    private void showimage(final String data, final Boolean rm){
        class Process extends AsyncTask<Void, Void, String> {
            RequestHandler rh = new RequestHandler(mContext);

            @Override
            protected void onPreExecute() {
                progress.show();
            }

            @Override
            protected void onPostExecute(String s) {
                if(rm){
                    mActivity.setResult(1234);
                    mActivity.finish();
                }
                progress.setMax(100);
            }

            @Override
            protected String doInBackground(Void... params) {
                if(rm) {
                    property.add(new String[]{"card", data});
                    return rh.sendPostRequest(UPLOAD_URL, property);
                }
                return null;
            }
        }

        new Process().execute();
    }
}