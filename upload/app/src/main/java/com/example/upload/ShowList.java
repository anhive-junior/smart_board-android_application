package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowList extends AppCompatActivity implements DataTransferInterface{
    public String UPLOAD_URL;
    public static String UPLOAD_KEY = "getslidelist";

    private JSONArray jarray;
    private JSONObject jobject;
    private Data[] myBitmap;
    private ArrayList<String[]> property;
    private Boolean showCheckBoxes = Boolean.FALSE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlist);
        System.out.println("test");
        property = new ArrayList<>();
        property.add(new String[]{"func", UPLOAD_KEY});

        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_signage.php";
        showlist(property);
    }

    public void setValue(String key, String lst) {
        property.clear();
        UPLOAD_KEY = key;
        property.add(new String[]{"func", UPLOAD_KEY});
        if(key == "rmcard")
            property.add(new String[]{"rm_list", "true"});
        property.add(new String[]{"lst", lst});
        System.out.println(property);
        showlist(property);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1234) {
            //finish();
            //startActivity(getIntent());
            property.clear();
            UPLOAD_KEY = "getslidelist";
            property.add(new String[]{"func", UPLOAD_KEY});
            showlist(property);
        }

    }

    public void showlist(final ArrayList<String[]> data){
        class Process extends AsyncTask<Void,Void,Data[]>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(ShowList.this, "Loading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(Data[] s) {
                loading.dismiss();
                myBitmap = s;

                System.out.println(1111);
                System.out.println(s);
                System.out.println(1111);


                if(myBitmap!=null) {
                    if (UPLOAD_KEY == "getslidelist") {
                        // 커스텀 아답타 생성
                        final MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.row, s, showCheckBoxes, myBitmap, ShowList.this, ShowList.this);

                        GridView gv = (GridView) findViewById(R.id.gridview);
                        gv.setAdapter(adapter);
                    } else {
                        //finish();
                        //startActivity(getIntent());
                        property.clear();
                        UPLOAD_KEY = "getslidelist";
                        property.add(new String[]{"func", UPLOAD_KEY});
                        showlist(property);
                    }
                }

            }

            @Override
            protected Data[] doInBackground(Void... params) {
                String temp1 = rh.sendPostRequest(UPLOAD_URL, data);

                if(temp1==null)
                    return null;

                Data[] result = ParseJson(temp1);

                if(UPLOAD_KEY == "getslidelist") {
                    try {
                        for (int i = 0; i < result.length; i++) {
                            URL url = new URL("http://" + result[i].thumbpath);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setReadTimeout(15000);
                            connection.setConnectTimeout(15000);
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            result[i].thumb = BitmapFactory.decodeStream(input);
                            connection.disconnect();
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                        System.out.println("errorr");
                    }
                }
                return result;
            }
        }

        new Process().execute();
    }

    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution){
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height)
        {
            if(maxResolution < width)
            {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }
        else
        {
            if(maxResolution < height)
            {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public Data[] ParseJson(String s){
        try {
            System.out.println(s);
            jobject = new JSONObject(s);
        }catch (JSONException e){
            System.out.println(e);
        }
        try {
            System.out.println(jobject);
            s = jobject.getString("data");
            //textView.setText(s);
            System.out.println(s);
        }catch (JSONException e){
            System.out.println(e);
        }

        try {
            jarray = new JSONArray(s);
            //jobject = new JSONObject(s);
            System.out.println(s);
        }catch (JSONException e){
            System.out.println(e);
        }

        Data[] data = new Data[jarray.length()];
        try {
            for(int i=0; i < jarray.length(); i++){
                data[i] = new Data();
                jobject = jarray.getJSONObject(i);
                data[i].filepath = jobject.getString("test_file");
                data[i].thumbpath = jobject.getString("test_thumb");
                data[i].name = jobject.getString("photo");
            }
        }catch (JSONException e){
            System.out.println(e);
        }
        return data;
    }

}

