package com.example.app_nhan_dien_benh_la_lua;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


//import com.example.test_ai.ml.Model;
import com.example.app_nhan_dien_benh_la_lua.ml.Model;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;




public class Activity_Select_Library extends AppCompatActivity {
    ImageView img_rs;
    Button btn_result, btn_nnvdt;
    EditText edt_rs;
    int imageSize = 256;
    String result;
    Uri uri_img_rs;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_library);
        img_rs = findViewById(R.id.img_rs);
        btn_result = findViewById(R.id.btn_result);
        edt_rs = findViewById(R.id.edt_rs);
        btn_nnvdt = findViewById(R.id.btn_nnvct);
        btn_nnvdt.setEnabled(false); // Để vô hiệu hóa button điều trị
        openImagePicker();

        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classifyImage(uriToBitmap(uri_img_rs, getContentResolver(), 256, 256));
                Log.d("ketqua", result);
                edt_rs.setText(result);
                btn_nnvdt.setEnabled(true);
                Log.d("ketqa", ""+flag);
            }
        });

        btn_nnvdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
                if (flag == 1){
                    url = "https://vnfarm.com.vn/benh-chay-bia-la-lua";
                } else if (flag == 2) {
                    url = "https://tanixa.com/benh-dom-nau-tren-lua/";
                }else {
                    url = "https://vnfarm.com.vn/benh-dao-on-tren-lua";
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            img_rs.setImageURI(selectedImageUri); // Hiển thị ảnh lên img_rs
            uri_img_rs = selectedImageUri;
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 400);
    }



    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Cháy Bìa Lá", "Đốm Nâu", "Đạo Ôn"};
            flag = maxPos + 1;
            result = classes[maxPos];

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public Bitmap uriToBitmap(Uri uri, ContentResolver contentResolver, int desiredWidth, int desiredHeight) {
        try {
            // Lấy Bitmap từ URI
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);

            // Scale Bitmap
            return Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Xử lý lỗi nếu có
        }
    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(resultCode == RESULT_OK){
//            if(requestCode == 3){
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                int dimension = Math.min(image.getWidth(), image.getHeight());
//                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
//                imageView.setImageBitmap(image);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
//            }else{
//                Uri dat = data.getData();
//                Bitmap image = null;
//                try {
//                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                imageView.setImageBitmap(image);
//
//                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//                classifyImage(image);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


//    private String encodeImage(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
//
//    public String BitMapToString(Bitmap bitmap){
//        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//        byte [] b=baos.toByteArray();
//        String temp= Base64.encodeToString(b, Base64.DEFAULT);
//        return temp;
//    }
//    public Bitmap StringToBitMap(String encodedString){
//        try {
//            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
//            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        } catch(Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }



}