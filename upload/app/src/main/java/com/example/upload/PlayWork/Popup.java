package com.example.upload.PlayWork;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.upload.Util.GlobalVar;
import com.example.upload.R;

import java.util.HashMap;

public class Popup extends AppCompatActivity {
    private HashMap<String, String> playworkdata;
    private Intent intent;
    EditText mEditText1;
    EditText mEditText2;
    EditText mEditText3;
    CheckBox mCheckBox1;
    CheckBox mCheckBox2;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.popup);

        playworkdata = new HashMap<>();

        playworkdata = ((GlobalVar)getApplication()).getPlayworkdata();

        intent = getIntent();
        mEditText1 = findViewById(R.id.editText);
        mEditText2 = findViewById(R.id.editText2);
        mEditText3 = findViewById(R.id.editText3);
        mCheckBox1 = findViewById(R.id.checkBox1);
        mCheckBox2 = findViewById(R.id.checkBox2);

        switch(intent.getExtras().getInt("name")){
            case 1:
                mEditText1.setVisibility(View.VISIBLE);
                mEditText2.setVisibility(View.VISIBLE);
                mEditText3.setVisibility(View.VISIBLE);
                mEditText1.setText(playworkdata.get("time_begin"));
                mEditText2.setText(playworkdata.get("time_days"));
                mEditText3.setText(playworkdata.get("time_hours"));
                break;
            case 2:
                mEditText1.setVisibility(View.VISIBLE);
                mEditText1.setText(playworkdata.get("count_max"));
                break;
            case 8:
                mEditText1.setVisibility(View.VISIBLE);
                mEditText1.setText(playworkdata.get("reload_interval"));
                break;
            case 10:
                mCheckBox1.setVisibility(View.VISIBLE);
                mCheckBox2.setVisibility(View.VISIBLE);
                mCheckBox1.setText("순차");
                mCheckBox2.setText("무작위");
                if(!playworkdata.get("squential_play").equals("_"))
                    mCheckBox1.setChecked(true);
                if(!playworkdata.get("randon_play").equals("_"))
                    mCheckBox2.setChecked(true);
                break;
            case 11:
                mEditText1.setVisibility(View.VISIBLE);
                mEditText1.setText(playworkdata.get("slide_interval"));
                break;
            case 12:
                mCheckBox1.setVisibility(View.VISIBLE);
                mCheckBox2.setVisibility(View.VISIBLE);
                mCheckBox1.setText("사진맞춤");
                mCheckBox2.setText("화면맞춤(줌)");
                if(!playworkdata.get("photo_zoom").equals("_"))
                    mCheckBox1.setChecked(true);
                if(!playworkdata.get("screen_zoom").equals("_"))
                    mCheckBox2.setChecked(true);
                break;
            default:
                break;
        }
        setResult(1234);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(intent.getExtras().getInt("name")){
            case 1:
                playworkdata.put("time_begin", mEditText1.getText().toString());
                playworkdata.put("time_days", mEditText2.getText().toString());
                playworkdata.put("time_hours", mEditText3.getText().toString());
                break;
            case 2:
                playworkdata.put("count_max", mEditText1.getText().toString());
                break;
            case 8:
                playworkdata.put("reload_interval", mEditText1.getText().toString());
                break;
            case 10:
                if(mCheckBox1.isChecked())
                    playworkdata.put("squential_play", "✔");
                else
                    playworkdata.put("squential_play", "_");
                if(mCheckBox2.isChecked())
                    playworkdata.put("randon_play", "✔");
                else
                    playworkdata.put("randon_play", "_");
                break;
            case 11:
                playworkdata.put("slide_interval", mEditText1.getText().toString());
                break;
            case 12:
                if(mCheckBox1.isChecked())
                    playworkdata.put("photo_zoom", "✔");
                else
                    playworkdata.put("photo_zoom", "_");
                if(mCheckBox2.isChecked())
                    playworkdata.put("screen_zoom", "✔");
                else
                    playworkdata.put("screen_zoom", "_");
                break;
            default:
                break;
        }
        ((GlobalVar)getApplication()).setPlayworkdata(playworkdata);
        setResult(333);
        finish();
        /*
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }*/
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if( event.getAction() == KeyEvent.ACTION_DOWN ){ //키 다운 액션 감지
            if( keyCode == KeyEvent.KEYCODE_BACK ){ //BackKey 다운일 경우만 처리
                //BackKey 이벤트일 경우 해야할 코드 작성
                switch(intent.getExtras().getInt("name")){
                    case 1:
                        playworkdata.put("time_begin", mEditText1.getText().toString());
                        playworkdata.put("time_days", mEditText2.getText().toString());
                        playworkdata.put("time_hours", mEditText3.getText().toString());
                        break;
                    case 2:
                        playworkdata.put("count_max", mEditText1.getText().toString());
                        break;
                    case 8:
                        playworkdata.put("reload_interval", mEditText1.getText().toString());
                        break;
                    case 10:
                        break;
                    case 11:
                        playworkdata.put("slide_interval", mEditText1.getText().toString());
                        break;
                    case 12:
                        break;
                    default:
                        break;
                }
                ((GlobalVar)getApplication()).setPlayworkdata(playworkdata);
                setResult(333);
                finish();

                return true; // 리턴이 true인 경우 기존 BackKey의 기본액션이 그대로 행해 지게 됩니다.
                // 리턴을 false로 할 경우 기존 BackKey의 기본액션이 진행 되지 않습니다.
                // 따라서 별도의 종료처리 혹은 다이얼로그 처리를 통한
                //BackKey기본액션을 구현 해주셔야 합니다.
            }
        }
        return super.onKeyDown( keyCode, event );
    }

}
