package com.example.upload;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {
    URL url;
    String response = null;
    String boundary = "^-----^";
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    Context mContext;


    RequestHandler(Context context){
        mContext = context;
    }

    public String sendPostRequest(String requestURL, String key, String caption, byte[] value, String filename){//sendcard
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(os));
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"" + "func" + "\"" + lineEnd);
            writer.writeBytes(lineEnd);
            writer.writeBytes(key);
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"" + "card" + "\";filename=\"" + filename + "\"" + lineEnd);
            writer.writeBytes(lineEnd);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(value);
            bytesAvailable = byteArrayInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = byteArrayInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                writer.write(buffer, 0, bufferSize);
                bytesAvailable = byteArrayInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = byteArrayInputStream.read(buffer, 0, bufferSize);
            }
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"" + "caption" + "\"" + lineEnd);
            writer.writeBytes(lineEnd);
            writer.writeBytes(caption);
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return response;
    }

    public String sendPostRequest(String requestURL, String key, String caption, String file){//upvideo
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        int cut = file.lastIndexOf('/');
        String fileName = file.substring(cut + 1);

        File sourceFile = new File(file);
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist");
            return null;
        }

        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            setCookieHeader(conn);

            OutputStream os = conn.getOutputStream();
            DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(os));
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"" + "func" + "\"" + lineEnd);
            writer.writeBytes(lineEnd);
            writer.writeBytes(key);
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"" + "video" + "\";filename=\"" + fileName + "\"" + lineEnd);
            writer.writeBytes(lineEnd);

            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            bytesAvailable = fileInputStream.available();
            Log.i("Huzza", "Initial .available : " + bytesAvailable);
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                writer.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + lineEnd);
            writer.writeBytes("Content-Disposition: form-data; name=\"" + "caption" + "\"" + lineEnd);
            writer.writeBytes(lineEnd);
            if(caption.length() == 0)
                writer.writeBytes("C:\\fakepath\\" + fileName);
            else
                writer.writeBytes(caption);
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            writer.flush();
            writer.close();
            os.close();

            getCookieHeader(conn);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return response;
    }

    public String sendPostRequest(String requestURL, ArrayList<String[]> property){//rmcard, manageslide, getslide, getslidelist
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            setCookieHeader(conn);

            OutputStream os = conn.getOutputStream();
            DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(os));
            writer.writeBytes(lineEnd);

            for(int i=0;i<property.size();i++){
                writer.writeBytes(twoHyphens + boundary + lineEnd);
                writer.writeBytes("Content-Disposition: form-data; name=\"" + property.get(i)[0] + "\"" + lineEnd);
                writer.writeBytes(lineEnd);
                writer.writeBytes(property.get(i)[1]);
                writer.writeBytes(lineEnd);
            }

            writer.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            writer.flush();
            writer.close();
            os.close();

            getCookieHeader(conn);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return response;
    }

    public String sendtest(String requestURL){//rmcard, manageslide, getslide, getslidelist
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.setRequestProperty("User-Agent", "mobile_app");
            setCookieHeader(conn);
            //conn.setRequestProperty("Connection", "keep-alive");


            /*
            OutputStream os = conn.getOutputStream();
            DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(os));
            writer.writeBytes(lineEnd);

            writer.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            writer.flush();
            writer.close();
            os.close();*/


            getCookieHeader(conn);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
                //response = sessionid;
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
            //response = "Connection Error";
        }

        return response;
    }

    private void getCookieHeader(HttpURLConnection conn){//Set-Cookie에 배열로 돼있는 쿠키들을 스트링 한줄로 변환
        List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
        //cookies -> [JSESSIONID=D3F829CE262BC65853F851F6549C7F3E; Path=/smartudy; HttpOnly] -> []가 쿠키1개임.
        //Path -> 쿠키가 유효한 경로 ,/smartudy의 하위 경로에 위의 쿠키를 사용 가능.
        if (cookies != null) {
            for (String cookie : cookies) {
                System.out.println(cookies);
                if(cookie.split(";\\s*")[0].contains("PHPSESSID")){
                    System.out.println("testtest");
                    String sessionid = cookie.split(";\\s*")[0];
                    setSessionIdInSharedPref(sessionid);
                }
                //JSESSIONID=FB42C80FC3428ABBEF185C24DBBF6C40를 얻음.
                //세션아이디가 포함된 쿠키를 얻었음.

            }
        }
    }

    private void setSessionIdInSharedPref(String sessionid){
        SharedPreferences pref = mContext.getSharedPreferences("sessionCookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if(pref.getString("sessionid",null) == null){ //처음 로그인하여 세션아이디를 받은 경우
            Log.d("LOG","처음 로그인하여 세션 아이디를 pref에 넣었습니다."+sessionid);
        }else if(!pref.getString("sessionid",null).equals(sessionid)){ //서버의 세션 아이디 만료 후 갱신된 아이디가 수신된경우
            Log.d("LOG","기존의 세션 아이디"+pref.getString("sessionid",null)+"가 만료 되어서 "
                    +"서버의 세션 아이디 "+sessionid+" 로 교체 되었습니다.");
        }
        edit.putString("sessionid",sessionid);
        edit.apply(); //비동기 처리
    }

    private void setCookieHeader(HttpURLConnection conn){
        SharedPreferences pref = mContext.getSharedPreferences("sessionCookie",Context.MODE_PRIVATE);
        String sessionid = pref.getString("sessionid",null);
        if(sessionid!=null) {
            Log.d("LOG","세션 아이디"+sessionid+"가 요청 헤더에 포함 되었습니다.");
            conn.setRequestProperty("cookie", sessionid);
        }
    }



}
