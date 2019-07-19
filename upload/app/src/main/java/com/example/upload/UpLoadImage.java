package com.example.upload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class UpLoadImage extends AppCompatActivity implements Button.OnClickListener{
    public String UPLOAD_URL;
    public static final String UPLOAD_KEY = "sendcard";

    private int PICK_IMAGE_REQUEST = 1;
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText val_caption;
    private ImageView imageView;
    private Bitmap bitmap;
    private Bitmap rotatedBitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        UPLOAD_URL = ((GlobalVar)this.getApplication()).getMyAddr() + "/signage/s00_signage.php";
        System.out.println(UPLOAD_URL);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        val_caption = (EditText) findViewById(R.id.input_caption);

        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.buttonChoose :
                showFileChooser();
                break ;
            case R.id.buttonUpload :
                uploadImage();
                break ;
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                /*
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] test = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(test);

                ExifInterface ei = new ExifInterface(bs);
                */
                System.out.println(PathUtil.getPath(getApplicationContext(), filePath));
                ExifInterface ei = new ExifInterface(PathUtil.getPath(getApplicationContext(), filePath));
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                System.out.println(orientation);


                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }

                bitmap = rotatedBitmap;
                //imageView.setImageBitmap(rotatedBitmap);
                imageView.setImageBitmap(bitmap);
                System.out.println("success");
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                imageView.setImageBitmap(bitmap);
            }

        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public byte[] getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        //String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //String encodedImage = imageBytes.toString();
        return imageBytes;
    }

    private void uploadImage(){
        class Process extends AsyncTask<Bitmap,Void,String>{
            ProgressDialog loading;
            RequestHandler rh = new RequestHandler(getApplicationContext());

            @Override
            protected void onPreExecute() {
                loading = ProgressDialog.show(UpLoadImage.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                loading.dismiss();

                Toast.makeText(getApplicationContext(), s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                byte[] uploadImage = getStringImage(bitmap);
                String result = rh.sendPostRequest(UPLOAD_URL, UPLOAD_KEY, val_caption.getText().toString(), uploadImage);

                return result;
            }
        }

        new Process().execute(rotatedBitmap);
    }
}
