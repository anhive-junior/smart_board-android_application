package com.example.upload;

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

import javax.net.ssl.HttpsURLConnection;

public class RequestHandler {
    URL url;
    String response = null;
    String boundary = "^-----^";
    String lineEnd = "\r\n";
    String twoHyphens = "--";

    public String sendPostRequest(String requestURL, String key, String caption, byte[] value){//sendcard
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
            writer.writeBytes("Content-Disposition: form-data; name=\"" + "card" + "\";filename=\"test.jpg\"" + lineEnd);
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
            response = "Connection Error";
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

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "Connection Error";
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

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = br.readLine();
            } else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "Connection Error";
        }

        return response;
    }

}
