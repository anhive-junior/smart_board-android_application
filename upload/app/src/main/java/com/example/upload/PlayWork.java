package com.example.upload;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class PlayWork extends AppCompatActivity implements Button.OnClickListener {
    public String UPLOAD_URL;
    public static String UPLOAD_KEY;
    private HashMap<String, String> playworkdata;

    private JSONObject jobject;
    private ArrayList<String[]> property;
    private ArrayList<PlayWorkProperty> list;
    private PlayWorkItemAdapter adapter;

    private Application mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playwork);
        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_signage.php";
        playworkdata = new HashMap<>();
        property = new ArrayList<>();
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        getproperty("get");
        System.out.println(4444);

        mApplication = this.getApplication();

        Button buttonSet = (Button)findViewById(R.id.set);
        Button buttonGet = (Button)findViewById(R.id.get);

        buttonSet.setOnClickListener(this);
        buttonGet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set :
                getproperty("set");
                break ;
            case R.id.get :
                getproperty("get");
                break ;
        }
    }


    public void setdata(){
        list = new ArrayList<>();
        list.add(new PlayWorkProperty("재생목록 구성방법"));
        list.add(new PlayWorkProperty("시간기준 : ", playworkdata.get("time_begin") + "시간 ~ " + playworkdata.get("time_days") + "일 " + playworkdata.get("time_hours") + "시간", TRUE, playworkdata.get("time_base")));
        list.add(new PlayWorkProperty("건수기준 : ", playworkdata.get("count_max") + "건 (최근 등록순으로)", TRUE, playworkdata.get("count_base")));
        list.add(new PlayWorkProperty("직접선택 : (재생목록에서)", "", TRUE, playworkdata.get("curate_base")));
        list.add(new PlayWorkProperty("등록파일 전체(직접선택 연계)", "", TRUE, playworkdata.get("media_base")));
        list.add(new PlayWorkProperty("재생목록 자동갱신 방법 : "));
        list.add(new PlayWorkProperty("신규사진 등록시", "", TRUE, playworkdata.get("as_updated")));
        list.add(new PlayWorkProperty("장비를 켤 때 적용", "", TRUE, playworkdata.get("as_powerup")));
        list.add(new PlayWorkProperty("주기 : ", playworkdata.get("reload_interval") + "초 (자동 목록 구성시)", TRUE, playworkdata.get("as_interval")));
        list.add(new PlayWorkProperty("재생 방식"));
        list.add(new PlayWorkProperty("재생순서 : ", "순차/무작위"));
        list.add(new PlayWorkProperty("재생시간간격 : ", playworkdata.get("slide_interval") + "초 (최소 3초이상)"));
        list.add(new PlayWorkProperty("화면확대 : ", "사진맞춤/화면맞춤(줌)"));

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        adapter = new PlayWorkItemAdapter(list, PlayWork.this, getApplication(), playworkdata);
        recyclerView.setAdapter(adapter);
    }

    //playworkdata에 데이터 갱신
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1234) {
            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
        }
        else if (resultCode == 333) {
            setdata();
        }

    }

    public void getproperty(final String method){
        class Process extends AsyncTask<Void,Void,HashMap> {
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(PlayWork.this, "Loading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(HashMap s) {
                loading.dismiss();
                System.out.println(1111);
                System.out.println(s);
                System.out.println(1111);

                setdata();

                ((GlobalVar)getApplication()).setPlayworkdata(playworkdata);

            }

            @Override
            protected HashMap doInBackground(Void... params) {
                String key;
                String temp1;

                if(method.equals("get")) {
                    key = "get_playlistpolicy";
                    property.add(new String[]{"func", key});
                    temp1 = rh.sendPostRequest(UPLOAD_URL, property);
                    ParseJson(temp1, key);
                    property.clear();

                    key = "get_playmode";
                    property.add(new String[]{"func", key});
                    temp1 = rh.sendPostRequest(UPLOAD_URL, property);
                    property.clear();

                    return ParseJson(temp1, key);
                }
                else{
                    //playworkdata.replace("screen_zoom", "✔");
                    key = "set_playlistpolicy";
                    property.add(new String[]{"func", key});
                    for(String name : playworkdata.keySet())
                        property.add(new String[]{name, playworkdata.get(name)});
                    rh.sendPostRequest(UPLOAD_URL, property);
                    property.clear();


                    key = "set_playmode";
                    property.add(new String[]{"func", key});
                    for(String name : playworkdata.keySet())
                        property.add(new String[]{name, playworkdata.get(name)});
                    rh.sendPostRequest(UPLOAD_URL, property);
                    property.clear();

                    return null;
                }

            }
        }

        new Process().execute();
    }

    public HashMap ParseJson(String s, String key){
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
            //jarray = new JSONArray(s);
            jobject = new JSONObject(s);
            System.out.println(s);
        }catch (JSONException e){
            System.out.println(e);
        }

        try {
            System.out.println(key);
            System.out.println(key == "get_playlistpolicy");
            System.out.println(key == "get_playmode");
            if(key.equals("get_playlistpolicy")) {
                playworkdata.put("time_base", jobject.getString("time_base"));
                playworkdata.put("time_begin", jobject.getString("time_begin"));
                playworkdata.put("time_days", jobject.getString("time_days"));
                playworkdata.put("time_hours", jobject.getString("time_hours"));

                playworkdata.put("count_base", jobject.getString("count_base"));
                playworkdata.put("count_max", jobject.getString("count_max"));

                playworkdata.put("curate_base", jobject.getString("curate_base"));
                playworkdata.put("media_base", jobject.getString("media_base"));

                playworkdata.put("as_updated", jobject.getString("as_updated"));
                playworkdata.put("as_powerup", jobject.getString("as_powerup"));

                playworkdata.put("as_interval", jobject.getString("as_interval"));
                playworkdata.put("reload_interval", jobject.getString("reload_interval"));
            }
            else if(key.equals("get_playmode")){
                playworkdata.put("squential_play", jobject.getString("squential_play"));
                playworkdata.put("randon_play", jobject.getString("randon_play"));

                playworkdata.put("slide_interval", jobject.getString("slide_interval"));

                playworkdata.put("photo_zoom", jobject.getString("photo_zoom"));
                playworkdata.put("screen_zoom", jobject.getString("screen_zoom"));
            }

        }catch (JSONException e){
            System.out.println(e);
        }


        System.out.println(123123);
        System.out.println(playworkdata.get("time_begin"));
        System.out.println(123123);
        return playworkdata;
    }

}


