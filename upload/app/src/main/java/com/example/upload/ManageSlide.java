package com.example.upload;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageSlide extends AppCompatActivity implements Button.OnClickListener{
    public String UPLOAD_URL;
    public static final String UPLOAD_KEY = "control";

    private ArrayList<String[]> property;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manageslide);
        property = new ArrayList<>();
        property.add(new String[]{"func", UPLOAD_KEY});

        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_signage.php";
        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/videolist.php";

        Button buttonPrevious = (Button) findViewById(R.id.button17);
        Button buttonPlayhold = (Button) findViewById(R.id.button16);
        Button buttonNext = (Button) findViewById(R.id.button15);
        Button buttonRestart= (Button) findViewById(R.id.button14);
        Button buttonRotateLeft= (Button) findViewById(R.id.button13);
        Button buttonRotateRight= (Button) findViewById(R.id.button12);
        Button buttonFlipped= (Button) findViewById(R.id.button11);
        Button buttonMirrored= (Button) findViewById(R.id.button10);
        Button buttonLarger= (Button) findViewById(R.id.button9);
        Button buttonSmaller= (Button) findViewById(R.id.button8);
        Button buttonFittosize= (Button) findViewById(R.id.button7);
        Button button100p= (Button) findViewById(R.id.button6);
        Button buttonUpward= (Button) findViewById(R.id.button4);
        Button buttonDownward= (Button) findViewById(R.id.button3);
        Button buttonLeftward= (Button) findViewById(R.id.button2);
        Button buttonRightward= (Button) findViewById(R.id.button);

        buttonPrevious.setOnClickListener(this);
        buttonPlayhold.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
        buttonRestart.setOnClickListener(this);
        buttonRotateLeft.setOnClickListener(this);
        buttonRotateRight.setOnClickListener(this);
        buttonFlipped.setOnClickListener(this);
        buttonMirrored.setOnClickListener(this);
        buttonLarger.setOnClickListener(this);
        buttonSmaller.setOnClickListener(this);
        buttonFittosize.setOnClickListener(this);
        button100p.setOnClickListener(this);
        buttonUpward.setOnClickListener(this);
        buttonDownward.setOnClickListener(this);
        buttonLeftward.setOnClickListener(this);
        buttonRightward.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button :
                manageslide("rigthward");
                break ;
            case R.id.button2 :
                manageslide("leftward");
                break ;
            case R.id.button3 :
                manageslide("downward");
                break ;
            case R.id.button4 :
                manageslide("upward");
                break ;
            case R.id.button6 :
                manageslide("100p");
                break ;
            case R.id.button7 :
                manageslide("fittosize");
                break ;
            case R.id.button8 :
                manageslide("smaller");
                break ;
            case R.id.button9 :
                manageslide("larger");
                break ;
            case R.id.button10 :
                manageslide("mirrored");
                break ;
            case R.id.button11 :
                manageslide("flipped");
                break ;
            case R.id.button12 :
                manageslide("rotate_right");
                break ;
            case R.id.button13 :
                manageslide("rotate_left");
                break ;
            case R.id.button14 :
                manageslide("restart");
                break ;
            case R.id.button15 :
                manageslide("next");
                break ;
            case R.id.button16 :
                manageslide("playhold");
                break ;
            case R.id.button17 :
                manageslide("previous");
                break ;
        }
    }

    private void manageslide(final String msg){
        class Process extends AsyncTask<Bitmap,Void,String>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(ManageSlide.this, "Loading", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();
                System.out.println(s);

                Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                property.add(new String[]{"ctrl", msg});

                String result = rh.sendPostRequest(UPLOAD_URL, property);

                return result;
            }
        }

        new Process().execute();
    }
}
