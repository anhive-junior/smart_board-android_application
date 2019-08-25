package com.example.upload.ShowList;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upload.Login;
import com.example.upload.Util.GlobalVar;
import com.example.upload.Util.Progress;
import com.example.upload.R;
import com.example.upload.Util.RequestHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowList extends AppCompatActivity implements DataTransferInterface {
    public String UPLOAD_URL;
    public static String UPLOAD_KEY = "getslidelist";
    public static ArrayList<String> itemList;

    private JSONArray jarray;
    private JSONObject jobject;
    private ArrayList<String[]> property;
    ArrayList<SectionDataModel> testdata = new ArrayList<>();
    SectionDataModel sectionDataModel;
    SingleDataModel singleDataModel;

    private RecyclerView my_recycler_view;
    private RecyclerViewDataAdapter adapter;
    private Progress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlist_test);

        itemList = new ArrayList<>();
        property = new ArrayList<>();
        property.add(new String[]{"func", UPLOAD_KEY});

        progress = new Progress(ShowList.this, ShowList.this, 0, 50);

        UPLOAD_URL = Login.UPLOAD_URL;
        showlist(property);
    }


    public void setValue(String key, String lst) {
        property.clear();
        UPLOAD_KEY = key;
        property.add(new String[]{"func", UPLOAD_KEY});
        if(key == "rmcard") property.add(new String[]{"rm_list", "true"});
        property.add(new String[]{"lst", lst});
        showlist(property);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1234) {
            property.clear();
            UPLOAD_KEY = "getslidelist";
            property.add(new String[]{"func", UPLOAD_KEY});
            showlist(property);
        }
    }


    public void showlist(final ArrayList<String[]> data){
        class Process extends AsyncTask<Void,Void,ArrayList> {
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                progress.show();
            }

            @Override
            protected void onPostExecute(ArrayList s) {
                progress.setMax(100);

                if (UPLOAD_KEY == "getslidelist") {
                    my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
                    adapter = new RecyclerViewDataAdapter(getApplicationContext(), ShowList.this, ShowList.this, testdata);
                    my_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    my_recycler_view.setAdapter(adapter);
                }else{
                    property.clear();
                    UPLOAD_KEY = "getslidelist";
                    property.add(new String[]{"func", UPLOAD_KEY});
                    showlist(property);
                }

            }

            @Override
            protected ArrayList doInBackground(Void... params) {
                String temp = rh.sendPostRequest(UPLOAD_URL, data);

                if(temp==null) return null;
                else return ParseJson(temp);
            }
        }

        new Process().execute();
    }

    public ArrayList ParseJson(String s){
        try {
            jobject = new JSONObject(s);
            s = jobject.getString("data");
            jarray = new JSONArray(s);
            testdata.clear();
            sectionDataModel = null;

            for(int i=0; i < jarray.length(); i++){
                jobject = jarray.getJSONObject(i);

                if(jobject.getString("block").length() != 0){
                    if(sectionDataModel != null){
                        testdata.add(sectionDataModel);
                    }
                    sectionDataModel = new SectionDataModel(jobject.getString("block"));
                }
                singleDataModel = new SingleDataModel();
                singleDataModel.filepath = jobject.getString("test_file");
                singleDataModel.thumbpath = jobject.getString("test_thumb");
                singleDataModel.name = jobject.getString("photo");
                itemList.add(jobject.getString("test_file"));
                try {
                    URL url = new URL("http://" + singleDataModel.thumbpath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(15000);
                    connection.setConnectTimeout(15000);
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    singleDataModel.thumb = BitmapFactory.decodeStream(input);
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sectionDataModel.item.add(singleDataModel);
            }
            testdata.add(sectionDataModel);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return testdata;
    }

}
